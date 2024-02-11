package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
import com.iridium.iridiumteams.Reward;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class Configuration extends com.iridium.iridiumteams.configs.Configuration {
    public Configuration() {
        super("&9", "Island", "IridiumSkyblock");
        this.createRequiresName = false;


        this.levelRewards = ImmutableMap.<Integer, Reward>builder()
                .put(1, new Reward(new Item(XMaterial.EXPERIENCE_BOTTLE, 1, "&9&lLevel %island_level% Reward", Arrays.asList(
                        "&7Island Level %island_level% Rewards:",
                        "&9&l* &91000 Money",
                        "&9&l* &95 Island Crystals",
                        "",
                        "&9&l[!] &9Left click to redeem"
                )), Collections.emptyList(), 0, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 200, 0, XSound.ENTITY_PLAYER_LEVELUP))

                .put(5, new Reward(new Item(XMaterial.EXPERIENCE_BOTTLE, 1, "&9&lLevel %island_level% Reward", Arrays.asList(
                        "&7Island Level %island_level% Rewards:",
                        "&9&l* &910000 Money",
                        "&9&l* &910 Island Crystals",
                        "",
                        "&9&l[!] &9Left click to redeem"
                )), Collections.emptyList(), 0, new ImmutableMap.Builder<String, Double>().put("Crystals", 10.00).build(), 2000, 0, XSound.ENTITY_PLAYER_LEVELUP))
                .build();
    }

    public String defaultIslandName = "%owner%'s Island";
    public String islandCreateTitle = "&9&lIsland Created";
    public String islandCreateSubTitle = "&7IridiumSkyblock by Peaches_MLG";
    public String defaultDescription = "Default island description :c";
    public String worldName = "IridiumSkyblock";
    public String spawnWorldName = "world";
    public String islandTitleTop = "&9%island_name%";
    public String islandTitleBottom = "&7%island_description%";
    public String paster = "worldedit";


    public boolean obsidianBucket = true;
    public boolean netherOnlyGenerator = false;
    public boolean endPortalPick = true;
    public boolean removeIslandBlocksOnDelete = false;
    public boolean clearInventoryOnRegen = false;
    public boolean clearEnderChestOnRegen = false;
    public boolean allowPvPOnIslands = false;
    public boolean islandCreateOnJoin = false;
    public int distance = 151;
    public int netherUnlockLevel = 10;
    public int endUnlockLevel = 20;
    public int pasterDelayInTick = 1;
    public int pasterLimitPerTick = 10;

    public Item islandCrystal = new Item(XMaterial.NETHER_STAR, 1, "&9*** &9&lIsland Crystal &9***", Arrays.asList(
            "",
            "&9%amount% Island Crystals",
            "&9&l[!] &9Right-Click to Redeem"
    ));

    public Map<World.Environment, Boolean> enabledWorlds = new ImmutableMap.Builder<World.Environment, Boolean>()
            .put(World.Environment.NORMAL, true)
            .put(World.Environment.NETHER, true)
            .put(World.Environment.THE_END, true)
            .build();


    public Color defaultBorderColor = Color.BLUE;
    public Map<Color, Boolean> enabledBorders = new ImmutableMap.Builder<Color, Boolean>()
            .put(Color.BLUE, true)
            .put(Color.RED, true)
            .put(Color.GREEN, true)
            .put(Color.OFF, true)
            .build();

}
