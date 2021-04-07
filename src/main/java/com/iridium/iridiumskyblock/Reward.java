package com.iridium.iridiumskyblock;

import com.cryptomorin.xseries.XSound;
import com.iridium.iridiumskyblock.database.Island;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@NoArgsConstructor
public class Reward {

    public Item item;
    public List<String> commands;
    public int islandExperience;
    public XSound sound;

    public Reward(Item item, int islandExperience, List<String> commands, XSound sound) {
        this.item = item;
        this.islandExperience = islandExperience;
        this.commands = commands;
        this.sound = sound;
    }

    public void claim(Player player, Island island) {
        island.setExperience(island.getExperience() + islandExperience);
        commands.forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
    }

}
