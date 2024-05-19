package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Biomes;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.database.IridiumUser;
import com.iridium.iridiumteams.database.TeamBank;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

public class BiomeManager {

    public void buy(Player player, Biomes.BiomeItem biomeItem) {
        XBiome biome = biomeItem.biome;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        Optional<XBiome> biomeOptional = XBiome.matchXBiome(biomeItem.biome.toString());

        if (!canPurchase(player, biomeItem)) {
            IridiumSkyblock.getInstance().getBiomes().failSound.play(player);
            return;
        }

        if (!biomeOptional.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noBiome
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return;
        }

        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveTeam
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return;
        }

        purchase(player, biomeItem);

        IridiumSkyblock.getInstance().getCommands().biomeCommand.getCooldownProvider().applyCooldown(player);

        IridiumSkyblock.getInstance().getIslandManager().setIslandBiome(island.get(), biomeOptional.get());
        IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island.get()).stream().map(IridiumUser::getPlayer).forEach((teamMember) -> {
            if (teamMember != null) {
                teamMember.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%player%", player.getName())
                        .replace("%biome%", WordUtils.capitalizeFully(biome.toString().toLowerCase().replace("_", " ")))
                ));
            }
        });
        IridiumSkyblock.getInstance().getBiomes().successSound.play(player);

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

        if(biomeItem.minLevel > 1) {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getTeamViaID(user.getTeamID());

            if(!island.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveTeam
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            if(island.get().getLevel() < biomeItem.minLevel) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notHighEnoughLevel
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%level%", String.valueOf(biomeItem.minLevel))));
                return false;
            }
        }

        double moneyCost = biomeItem.buyCost.money;
        Economy economy = IridiumSkyblock.getInstance().getEconomy();
        for (String bankItem : biomeItem.buyCost.bankItems.keySet()) {
            double cost = biomeItem.buyCost.bankItems.get(bankItem);
            if (getBankBalance(player, bankItem) < cost) return false;
        }

        if(!(moneyCost == 0 || economy != null && economy.getBalance(player) >= moneyCost)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotAfford
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return false;
        }

        return true;
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
