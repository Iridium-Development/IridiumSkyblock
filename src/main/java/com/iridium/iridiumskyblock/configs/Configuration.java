package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
import com.iridium.iridiumcore.utils.NumberFormatter;
import com.iridium.iridiumskyblock.Reward;
import com.iridium.iridiumskyblock.generators.GeneratorType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The main configuration of IridiumSkyblock (configuration.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {

    public String prefix = "<GRADIENT:09C6F9>&lIridiumSkyblock</GRADIENT:045DE9> &8»";
    public String worldName = "IridiumSkyblock";
    public String islandCreateTitle = "&b&lIsland Created";
    public String islandCreateSubTitle = "&7IridiumSkyblock by Peaches_MLG";
    public String dateTimeFormat = "EEEE, MMMM dd HH:mm:ss";
    public String spawnWorldName = "world";
    public String defaultCommandSyntax = "/is %command%";
    public String islandLevelEquation = "abs(cbrt($1+1))";
    public String defaultIslandName = "%island_owner_name%'s Island";

    public boolean enableCheckVersion = true;
    public boolean defaultIslandPublic = true;
    public boolean voidTeleport = true;
    public boolean netherIslands = true;
    public boolean endIslands = true;
    public boolean respawnOnIsland = true;
    public boolean obsidianBucket = true;
    public boolean patreonMessage = true;
    public boolean islandMenu = true;
    public boolean backButtons = true;
    public boolean createIslandOnHome = true;

    public IslandRegenSettings regenSettings = new IslandRegenSettings();

    public IslandDeleteSettings deleteSettings = new IslandDeleteSettings();

    public IslandDamageSettings pvpSettings = new IslandDamageSettings();

    public int distance = 151;
    public int islandRecalculateInterval = 5;
    public int maxIslandName = 16;
    public int minIslandName = 3;
    public int teleportDelay = 0;

    public Item islandCrystal = new Item(XMaterial.NETHER_STAR, 1, "&b*** &b&lIsland Crystal &b***", Arrays.asList("", "&b%amount% Island Crystals", "&b&l[!] &bRight-Click to Redeem"));

    public XSound islandLevelUpSound = XSound.ENTITY_PLAYER_LEVELUP;

    public NumberFormatter numberFormatter = new NumberFormatter();

    public GeneratorSettings generatorSettings = new GeneratorSettings();

    public Map<Integer, Integer> islandTopSlots = ImmutableMap.<Integer, Integer>builder()
            .put(1, 4)
            .put(2, 12)
            .put(3, 14)
            .put(4, 19)
            .put(5, 20)
            .put(6, 21)
            .put(7, 22)
            .put(8, 23)
            .put(9, 24)
            .put(10, 25)
            .build();

    public Map<Integer, Integer> islandWarpSlots = ImmutableMap.<Integer, Integer>builder()
            .put(1, 9)
            .put(2, 11)
            .put(3, 13)
            .put(4, 15)
            .put(5, 17)
            .build();

    /**
     * The Rewards the island gets for leveling up
     * The integer represents the reward they will get, if the island level is divisible by this number they will get that reward
     * Higest number takes priority
     * <p>
     * e.g. 1 will give the reward to every level since every number is divisible by 1
     * 5 will give the reward to levels 5 10 15 20 25 ect since they are divisible by 5
     */
    public Map<Integer, Reward> islandLevelRewards = ImmutableMap.<Integer, Reward>builder()
            .put(1, new Reward(new Item(XMaterial.EXPERIENCE_BOTTLE, 1, "&b&lLevel %island_level% Reward", Arrays.asList(
                    "&7Island Level %island_level% Rewards:",
                    "&b&l* &b5 Island Crystals",
                    "&b&l* &b200 Island Money",
                    "",
                    "&b&l[!] &bLeft click to redeem"
            )), Collections.emptyList(), 0, 5, 200, 0, XSound.ENTITY_PLAYER_LEVELUP))

            .put(5, new Reward(new Item(XMaterial.EXPERIENCE_BOTTLE, 1, "&b&lLevel %island_level% Reward", Arrays.asList(
                    "&7Island Level %island_level% Rewards:",
                    "&b&l* &b15 Island Crystals",
                    "&b&l* &b2000 Island Money",
                    "",
                    "&b&l[!] &bLeft click to redeem"
            )), Collections.emptyList(), 0, 15, 2000, 0, XSound.ENTITY_PLAYER_LEVELUP))
            .build();

    /**
     * Settings for the {@link org.bukkit.generator.ChunkGenerator} of IridiumSkyblock.
     * Allows fine-tuning of the {@link com.iridium.iridiumskyblock.generators.OceanGenerator}.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratorSettings {

        public GeneratorType generatorType = GeneratorType.SKYBLOCK;
        public int waterHeight = 93;
        public int minOceanFloorLevel = 10;
        public int maxOceanFloorLevel = 25;
        public XMaterial oceanFloorBottomMaterial = XMaterial.GRAVEL;
        public XMaterial oceanFloorTopMaterial = XMaterial.SAND;

    }

    /**
     * The default settings for island regeneration.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IslandRegenSettings {

        public double moneyPrice = 0;
        public int crystalPrice = 0;
        public boolean clearInventories = false;
        public boolean clearEnderChests = false;
        public boolean resetIslandBank = false;
        public boolean resetVaultBalances = false;
        public boolean resetUpgrades = true;
        public boolean resetMissions = true;
        public boolean resetBoosters = true;
        public boolean clearWarps = true;
        public boolean resetBorderColour = true;
        public boolean resetPermissions = true;
        public boolean unbanAll = false;
        public boolean giveUpInvites = true;
        public boolean kickMembers = false;
        public boolean makeIslandPrivate = false;

    }

    /**
     * The default settings for island delete.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IslandDeleteSettings {

        public boolean clearInventories = false;
        public boolean clearEnderChests = false;
        public boolean resetVaultBalances = false;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class IslandDamageSettings {

        public boolean pvpOnIslands = false;
        public boolean pvpBetweenMembers = false;
        public boolean mobsVisitorTargeting = false;

        public List<EntityDamageEvent.DamageCause> membersPreventedDamages = Arrays.asList(
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
        );

        public List<EntityDamageEvent.DamageCause> visitorsPreventedDamages = Arrays.asList(
                EntityDamageEvent.DamageCause.PROJECTILE,
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.MAGIC,
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                EntityDamageEvent.DamageCause.FLY_INTO_WALL,
                EntityDamageEvent.DamageCause.FALLING_BLOCK,
                EntityDamageEvent.DamageCause.THORNS,
                EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                EntityDamageEvent.DamageCause.HOT_FLOOR,
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.CONTACT,
                EntityDamageEvent.DamageCause.FIRE_TICK
        );

    }

}
