package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Class which handles users.
 */
public class UserManager {

    /**
     * Gets a {@link User}'s info. Creates one if he doesn't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return The user data
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        Optional<User> userOptional = getUserByUUID(offlinePlayer.getUniqueId());
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            Optional<String> name = Optional.ofNullable(offlinePlayer.getName());
            User user = new User(offlinePlayer.getUniqueId(), name.orElse(""));
            IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().addEntry(user);
            return user;
        }
    }

    /**
     * Disables the user's flight
     *
     * @param user Disable for which user
     */
    public void disableFlight(User user) {
        Player player = user.toPlayer();
        if (user.isFlying()) {
            user.setFlying(false);
            if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightDisabled
                        .replace("%player%", player.getName())
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                );
            }

        }
    }

    /**
     * Finds an User by his {@link UUID}.
     *
     * @param uuid The uuid of the onlyForPlayers
     * @return the User class of the onlyForPlayers
     */
    public Optional<User> getUserByUUID(@NotNull UUID uuid) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getUser(uuid);
    }

}
