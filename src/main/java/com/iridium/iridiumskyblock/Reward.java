package com.iridium.iridiumskyblock;

import com.cryptomorin.xseries.XSound;
import com.iridium.iridiumskyblock.database.Island;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Represents a reward for a {@link Mission}.
 * Serialized in Configuration files.
 */
@NoArgsConstructor
public class Reward {

    public Item item;
    public List<String> commands;
    public int islandExperience;
    public XSound sound;

    /**
     * The default constructor.
     *
     * @param item             The item which represents this reward
     * @param islandExperience The amount of experience this rewards gives
     * @param commands         The commands this reward should execute upon completion
     * @param sound            The sound that should be played upon completion
     */
    public Reward(Item item, int islandExperience, List<String> commands, XSound sound) {
        this.item = item;
        this.islandExperience = islandExperience;
        this.commands = commands;
        this.sound = sound;
    }

    /**
     * Claims this reward for the provided user and island.
     * Sets the experience, executes the commands and plays the sound.
     * TODO: Make this actually play the sound
     *
     * @param player The Player which should receive the reward
     * @param island The Island of the Player
     */
    public void claim(Player player, Island island) {
        island.setExperience(island.getExperience() + islandExperience);
        commands.forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
    }

}
