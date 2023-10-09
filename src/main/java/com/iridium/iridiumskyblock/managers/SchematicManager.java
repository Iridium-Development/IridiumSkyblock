package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.schematics.FastAsyncWorldEdit;
import com.iridium.iridiumskyblock.schematics.SchematicAsync;
import com.iridium.iridiumskyblock.schematics.SchematicPaster;
import com.iridium.iridiumskyblock.schematics.WorldEdit;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.database.TeamBank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SchematicManager {

    public SchematicPaster schematicPaster;
    public final Map<String, File> schematicFiles;
    public final TreeMap<String, SchematicPaster> availablePasters;

    private final boolean worldEdit = Bukkit.getPluginManager().isPluginEnabled("WorldEdit");
    private final boolean fawe = Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")
            || Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit");

    public SchematicManager() {

        availablePasters = new TreeMap<>();
        availablePasters.put("internalAsync", new SchematicAsync());
        if ((worldEdit) && WorldEdit.isWorking())
            availablePasters.put("worldedit", new WorldEdit());
        if ((fawe) && FastAsyncWorldEdit.isWorking())
            availablePasters.put("fawe", new FastAsyncWorldEdit());

        if ((worldEdit) && !WorldEdit.isWorking()) {
            IridiumSkyblock.getInstance().getLogger()
                    .warning("WorldEdit version doesn't support minecraft version, disabling WorldEdit integration");
        }
        if ((fawe) && !FastAsyncWorldEdit.isWorking()) {
            IridiumSkyblock.getInstance().getLogger().warning(
                    "FAWE version does not implement API correctly, did you miss an update? Disabling FAWE integration");
        }

        setPasterFromConfig();

        this.schematicFiles = new HashMap<>();
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }

    public void reload() {
        loadCache();
        setPasterFromConfig();
        schematicPaster.clearCache();
    }

    private void setPasterFromConfig() {
        String paster = IridiumSkyblock.getInstance().getConfiguration().paster;
        if (availablePasters.containsKey(paster))
            this.schematicPaster = availablePasters.get(paster);
        else {
            IridiumSkyblock.getInstance().getLogger().warning("Configuration error, selected paster [" + paster + "] is not available, available choices are " + availablePasters.keySet());
            this.schematicPaster = new SchematicAsync();
        }
    }

    public void loadCache() {
        schematicFiles.clear();
        File parent = new File(IridiumSkyblock.getInstance().getDataFolder(), "schematics");
        for (File file : parent.listFiles()) {
            schematicFiles.put(file.getName(), file);
        }
    }

    public CompletableFuture<Void> pasteSchematic(Island island, Schematics.SchematicConfig schematic) {
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();

        if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.NORMAL, true)) {
            completableFutures.add(pasteSchematic(island, schematic.overworld, IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NORMAL)));
        }
        if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.NETHER, true)) {
            completableFutures.add(pasteSchematic(island, schematic.nether, IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NETHER)));
        }
        if (IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(World.Environment.THE_END, true)) {
            completableFutures.add(pasteSchematic(island, schematic.end, IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.THE_END)));
        }

        return CompletableFuture.runAsync(() -> completableFutures.forEach(CompletableFuture::join));
    }

    private CompletableFuture<Void> pasteSchematic(Island island, Schematics.SchematicWorld schematic, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Location location = island.getCenter(world);
        location.add(0, schematic.islandHeight, 0);
        File file = schematicFiles.getOrDefault(schematic.schematicID,
                schematicFiles.values().stream().findFirst().orElse(null));
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            if (file == null) {
                location.getBlock().setType(Material.BEDROCK);
                IridiumSkyblock.getInstance().getLogger().warning("Could not find schematic " + schematic.schematicID);
            } else {
                if (fawe) {
                    Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(),
                            () -> schematicPaster.paste(file, location, schematic.ignoreAirBlocks, completableFuture));
                } else {
                    schematicPaster.paste(file, location, schematic.ignoreAirBlocks, completableFuture);
                }
            }
        });
        return completableFuture;
    }

    public boolean processTransaction(Player player, Schematics.SchematicConfig schematic) {

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(user.getTeamID());

        if(!island.isPresent()){
            //NO ISLAND
            return false;
        }

        TeamBank bank;

        double bankBalance;
        List<Placeholder> bankPlaceholders;
        double moneyCost = schematic.regenCost.money;
        Economy economy = IridiumSkyblock.getInstance().getEconomy();

        if(moneyCost != 0 && economy != null && economy.getBalance(player) < moneyCost) {
            //Cannot Afford
            return false;
        }

        for (String schematicBankItem : schematic.regenCost.bankItems.keySet()) {

            TeamBank bank = IridiumSkyblock.getInstance().getTeamManager().getTeamBank(island.get(), schematicBankItem);
            double bankBalance = bank.getNumber();

            double cost = round(schematic.regenCost.bankItems.get(schematicBankItem), 2);

            if (bank.getNumber() < cost) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotAfford
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                IridiumSkyblock.getInstance().getSchematics().failSound.play(player);

                return false;
            }

            double finalBankBalance = bankBalance;
            bankPlaceholders = IridiumSkyblock.getInstance().getBankItemList().stream()
                    .map(BankItem::getName)
                    .map(name -> new Placeholder(name + "_cost", formatPrice(finalBankBalance)))
                    .collect(Collectors.toList());

            player.sendMessage(StringUtils.color(StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getMessages().paidForRegen
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%player%", player.getName())
                            .replace("%schematic%", StringUtils.color(schematic.item.displayName))
                            .replace("%bank_cost%", formatPrice(cost))
                            .replace ("%vault_cost%", formatPrice(moneyCost)),
                    bankPlaceholders)
            ));

            bank.setNumber(bankBalance - cost);
        }

        economy.withdrawPlayer(player, moneyCost);

        IridiumSkyblock.getInstance().getSchematics().successSound.play(player);

        return true;
    }

    private double round(double value, int places) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public String formatPrice(double value) {
        if (IridiumSkyblock.getInstance().getSchematics().abbreviatePrices) {
            return IridiumSkyblock.getInstance().getConfiguration().numberFormatter.format(value);
        }
        return String.valueOf(value);
    }
}
