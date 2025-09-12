package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.RegenGUI;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.commands.Command;
import com.iridium.iridiumteams.commands.ConfirmableCommand;
import com.iridium.iridiumteams.database.TeamBank;
import com.iridium.iridiumteams.gui.ConfirmationGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class RegenCommand extends ConfirmableCommand<Island, User> {

    public RegenCommand() {
        super(Collections.singletonList("regen"), "Regenerate your Island", "%prefix% &7/is regen <schematic>", "", 300, true);
    }

    @Override
    public boolean isCommandValid(User user, Island island, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length == 0 && IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().size() > 1) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getTeamPermission(island, IridiumSkyblock.getInstance().getUserManager().getUser(player), "regen")) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotRegenIsland
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return false;
            }

            player.openInventory(new RegenGUI(player).getInventory());
            return false;
        }

        Optional<String> schematic = IridiumSkyblock.getInstance().getSchematics().schematics.keySet().stream()
                .filter(config -> IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().size() == 1 || config.equalsIgnoreCase(args[0]))
                .findFirst();
        if (!schematic.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownSchematic
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return false;
        }

        Schematics.SchematicConfig schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.get(schematic.get());

        if (island.getLevel() < schematicConfig.minLevel) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notHighEnoughLevel
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    .replace("%level%", String.valueOf(schematicConfig.minLevel))));
            return false;
        }

        if (schematicConfig.regenCost.money != 0 || !schematicConfig.regenCost.bankItems.isEmpty()) {
            return IridiumSkyblock.getInstance().getSchematicManager().buy(player, schematicConfig);
        }

        return true;
    }

    @Override
    public boolean executeAfterConfirmation(User user, Island island, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        Schematics.SchematicConfig schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.get(args[0]);

        regenerateIsland(player, island, schematicConfig, iridiumTeams);
        return true;
    }

    private void regenerateIsland(Player player, Island island, Schematics.SchematicConfig schematicConfig, IridiumTeams<Island, User> iridiumTeams) {
        List<Placeholder> bankPlaceholders = IridiumSkyblock.getInstance().getBankItemList().stream()
                .map(BankItem::getName)
                .map(name -> new Placeholder(name + "_cost", formatPrice(getBankBalance(player, name))))
                .collect(Collectors.toList());

        sendConfirmationMessage(player, island, schematicConfig, bankPlaceholders);

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().regeneratingIsland
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
        ));

        IridiumSkyblock.getInstance().getIslandManager().clearTeamInventory(island);
        regenerateAndTeleport(player, island, schematicConfig);
    }

    private void sendConfirmationMessage(Player player, Island island, Schematics.SchematicConfig schematicConfig, List<Placeholder> bankPlaceholders) {
        IridiumSkyblock.getInstance().getIslandManager().getTeamMembers(island).stream()
                .map(User::getPlayer)
                .filter(Objects::nonNull)
                .forEach(member -> member.sendMessage(StringUtils.color(StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getMessages().paidForRegen
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                .replace("%player%", player.getName())
                                .replace("%schematic%", StringUtils.color(schematicConfig.item.displayName))
                                .replace("%vault_cost%", formatPrice(schematicConfig.regenCost.money)),
                        bankPlaceholders)
                )));
    }

    private void regenerateAndTeleport(Player player, Island island, Schematics.SchematicConfig schematicConfig) {
        IridiumSkyblock.getInstance().getIslandManager().generateIsland(island, schematicConfig).thenRun(() ->
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                    if (IridiumSkyblock.getInstance().getTeamManager().teleport(player, island.getHome(), island)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        ));
                    }
                })
        );
    }

    private double getBankBalance(Player player, String bankItem) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        return IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(user.getTeamID())
                .map(team -> IridiumSkyblock.getInstance().getTeamManager().getTeamBank(team, bankItem))
                .map(TeamBank::getNumber)
                .orElse(0.0);
    }

    private double round(double value, int places) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public String formatPrice(double value) {
        value = round(value, 2);
        if (IridiumSkyblock.getInstance().getSchematics().abbreviatePrices) {
            return IridiumSkyblock.getInstance().getConfiguration().numberFormatter.format(value);
        }
        return String.valueOf(value);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        return new ArrayList<>(IridiumSkyblock.getInstance().getSchematics().schematics.keySet());
    }
}
