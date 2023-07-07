package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Biomes;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.database.TeamBank;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BiomeManager {

    public void buy(Player player, Biomes.BiomeItem biomeItem) {
        XBiome biome = biomeItem.biome;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getTeamManager().getTeamViaPlayerLocation(user.getPlayer());
        Optional<XBiome> biomeOptional = XBiome.matchXBiome(biomeItem.biome.toString());

        if (!canPurchase(player, biomeItem)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotAfford
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            IridiumSkyblock.getInstance().getBiomes().failSound.play(player);
            return;
        }

        if (!biomeOptional.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noBiome)
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            );
            return;
        }

        if (!island.isPresent()) {
            island = IridiumSkyblock.getInstance().getTeamManager().getTeamViaNameOrPlayer(user.getName());
            if (!island.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveTeam)
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                );
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notOnIsland)
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                );
            }
            return;
        }

        purchase(player, biomeItem);

        IridiumSkyblock.getInstance().getIslandManager().setIslandBiome(island.get(), biomeOptional.get());
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%biome%", WordUtils.capitalizeFully(biome.toString().toLowerCase().replace("_", " ")))
        ));

        // Run the command
        if (!biomeItem.command.equals("")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), biomeItem.command.replace("%player%", player.getName()));
        }

        IridiumSkyblock.getInstance().getBiomes().successSound.play(player);

        List<Placeholder> bankPlaceholders = IridiumSkyblock.getInstance().getBankItemList().stream()
                .map(BankItem::getName)
                .map(name -> new Placeholder(name + "_cost", formatPrice(getBankBalance(player, name))))
                .collect(Collectors.toList());

        player.sendMessage(StringUtils.color(StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getMessages().boughtBiome
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%biome%", StringUtils.color(String.valueOf(biomeItem.biome)))
                        .replace("%vault_cost%", formatPrice(biomeItem.buyCost.money)),
                bankPlaceholders)
        ));

    }

    private double getBankBalance(Player player, String bankItem) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        return IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(user.getTeamID())
                .map(team -> IridiumSkyblock.getInstance().getTeamManager().getTeamBank(team, bankItem))
                .map(TeamBank::getNumber)
                .orElse(0.0);
    }

    private void setBankBalance(Player player, String bankItem, double amount) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> team = IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(user.getTeamID());
        if (!team.isPresent()) return;
        IridiumSkyblock.getInstance().getTeamManager().getTeamBank(team.get(), bankItem).setNumber(amount);
    }

    private boolean canPurchase(Player player, Biomes.BiomeItem biomeItem) {
        double moneyCost = biomeItem.buyCost.money;
        Economy economy = IridiumSkyblock.getInstance().getEconomy();
        for (String bankItem : biomeItem.buyCost.bankItems.keySet()) {
            double cost = biomeItem.buyCost.bankItems.get(bankItem);
            if (getBankBalance(player, bankItem) < cost) return false;
        }

        return moneyCost == 0 || economy != null && economy.getBalance(player) >= moneyCost;
    }

    private void purchase(Player player, Biomes.BiomeItem biomeItem) {
        double moneyCost = biomeItem.buyCost.money;
        IridiumSkyblock.getInstance().getEconomy().withdrawPlayer(player, moneyCost);

        for (String bankItem : biomeItem.buyCost.bankItems.keySet()) {
            double cost = biomeItem.buyCost.bankItems.get(bankItem);
            setBankBalance(player, bankItem, getBankBalance(player, bankItem) - cost);
        }
    }

    public String formatPrice(double value) {
        if (IridiumSkyblock.getInstance().getBiomes().abbreviatePrices) {
            return IridiumSkyblock.getInstance().getConfiguration().numberFormatter.format(value);
        }
        return String.valueOf(value);
    }
}
