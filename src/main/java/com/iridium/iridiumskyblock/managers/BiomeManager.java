package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Biomes;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.database.IridiumUser;
import com.iridium.iridiumteams.database.Team;
import com.iridium.iridiumteams.database.TeamBank;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BiomeManager<T extends Team, U extends IridiumUser<T>> {
    private final IridiumSkyblock iridiumSkyblock;

    public BiomeManager(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    public void buy(Player player, Biomes.BiomeItem biomeItem) {
        if (!canPurchase(player, biomeItem)) {
            player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().cannotAfford
                    .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            iridiumSkyblock.getBiomes().failSound.play(player);
            return;
        }

        purchase(player, biomeItem);

        XBiome biome = biomeItem.biome;

        User user = iridiumSkyblock.getUserManager().getUser(player);
        Optional<Island> island = iridiumSkyblock.getTeamManager().getTeamViaPlayerLocation(user.getPlayer());
        Optional<XBiome> biomeOptional = XBiome.matchXBiome(biomeItem.biome.toString());

        if (!island.isPresent() || !biomeOptional.isPresent()) {
            if(!island.isPresent()) {

                island = iridiumSkyblock.getTeamManager().getTeamViaNameOrPlayer(user.getName());
                if(!island.isPresent())
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveTeam)
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix));

                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notOnIsland)
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix));
            }

            if(!biomeOptional.isPresent())
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noBiome)
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix));

        } else {
            IridiumSkyblock.getInstance().getIslandManager().setIslandBiome(island.get(), biomeOptional.get());
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    .replace("%biome%", WordUtils.capitalizeFully(biome.toString().toLowerCase().replace("_", " ")))));

            // Run the command
            if(!biomeItem.command.equals(""))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), biomeItem.command.replace("%player%", player.getName()));

            iridiumSkyblock.getBiomes().successSound.play(player);

            List<Placeholder> bankPlaceholders = iridiumSkyblock.getBankItemList().stream()
                    .map(BankItem::getName)
                    .map(name -> new Placeholder(name + "_cost", formatPrice(getBankBalance(player, name))))
                    .collect(Collectors.toList());
            double moneyCost = biomeItem.buyCost.money;

            player.sendMessage(StringUtils.color(StringUtils.processMultiplePlaceholders(iridiumSkyblock.getMessages().boughtBiome
                            .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)
                            .replace("%biome%", StringUtils.color(String.valueOf(biomeItem.biome)))
                            .replace("%vault_cost%", formatPrice(moneyCost)),
                    bankPlaceholders)
            ));
        }

    }

    private double getBankBalance(Player player, String bankItem) {
        User user = iridiumSkyblock.getUserManager().getUser(player);
        return iridiumSkyblock.getTeamManager().getTeamViaID(user.getTeamID())
                .map(team -> iridiumSkyblock.getTeamManager().getTeamBank(team, bankItem))
                .map(TeamBank::getNumber)
                .orElse(0.0);
    }

    private void setBankBalance(Player player, String bankItem, double amount) {
        User user = iridiumSkyblock.getUserManager().getUser(player);
        Optional<Island> team = iridiumSkyblock.getTeamManager().getTeamViaID(user.getTeamID());
        if (!team.isPresent()) return;
        iridiumSkyblock.getTeamManager().getTeamBank(team.get(), bankItem).setNumber(amount);
    }

    private boolean canPurchase(Player player, Biomes.BiomeItem biomeItem) {
        double moneyCost = biomeItem.buyCost.money;
        Economy economy = iridiumSkyblock.getEconomy();
        for (String bankItem : biomeItem.buyCost.bankItems.keySet()) {
            double cost = biomeItem.buyCost.bankItems.get(bankItem);
            if (getBankBalance(player, bankItem) < cost) return false;
        }

        return moneyCost == 0 || economy != null && economy.getBalance(player) >= moneyCost;
    }

    private void purchase(Player player, Biomes.BiomeItem biomeItem) {
        double moneyCost = biomeItem.buyCost.money;
        iridiumSkyblock.getEconomy().withdrawPlayer(player, moneyCost);

        for (String bankItem : biomeItem.buyCost.bankItems.keySet()) {
            double cost = biomeItem.buyCost.bankItems.get(bankItem);
            setBankBalance(player, bankItem, getBankBalance(player, bankItem) - cost);
        }
    }

    public String formatPrice(double value) {
        if (iridiumSkyblock.getBiomes().abbreviatePrices) {
            return iridiumSkyblock.getConfiguration().numberFormatter.format(value);
        }
        return String.valueOf(value);
    }
}
