package com.iridium.iridiumskyblock.commands.home;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.Command;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CurrentOffsetCommand extends Command {

    /**
     * The default constructor.
     */
    public CurrentOffsetCommand() {
        super(Collections.singletonList("currentOffset"), "Displays the relative location from the center", "%prefix% &7/is home currentOffset", "iridiumskyblock.currentoffset", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender The CommandSender which executes this command
     * @param arguments   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        Location playerLocation = player.getLocation();
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
        if (!islandOptional.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notOnAnIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Location offset = playerLocation.subtract(islandOptional.get().getCenter(player.getWorld()));
        sender.sendMessage(
                StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandCenterOffset
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%x%", String.valueOf(offset.getBlockX() + 0.5))
                        .replace("%y%", String.valueOf(offset.getBlockY() + 0.5))
                        .replace("%z%", String.valueOf(offset.getBlockZ() + 0.5))
                        .replace("%yaw%", String.valueOf(playerLocation.getYaw()))
                )
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
