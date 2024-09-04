package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumskyblock.enhancements.GeneratorEnhancementData;
import com.iridium.iridiumskyblock.enhancements.SizeEnhancementData;
import com.iridium.iridiumskyblock.enhancements.VoidEnhancementData;
import com.iridium.iridiumteams.enhancements.Enhancement;
import com.iridium.iridiumteams.enhancements.EnhancementType;

import java.util.Arrays;

public class Enhancements extends com.iridium.iridiumteams.configs.Enhancements {

    public Enhancements() {
        super("&9");
        this.membersEnhancement.item.slot = 10;
        this.warpsEnhancement.item.slot = 12;
        this.potionEnhancements.get("haste").item.slot = 14;
        this.potionEnhancements.get("speed").item.slot = 15;
        this.potionEnhancements.get("jump").item.slot = 16;

        this.farmingEnhancement.levels.forEach((integer, farmingEnhancementData) -> farmingEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.spawnerEnhancement.levels.forEach((integer, spawnerEnhancementData) -> spawnerEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.experienceEnhancement.levels.forEach((integer, experienceEnhancementData) -> experienceEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.flightEnhancement.levels.forEach((integer, flightEnhancementData) -> flightEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.membersEnhancement.levels.forEach((integer, membersEnhancementData) -> membersEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.warpsEnhancement.levels.forEach((integer, warpsEnhancementData) -> warpsEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.potionEnhancements.get("haste").levels.forEach((integer, potionEnhancementData) -> potionEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.potionEnhancements.get("speed").levels.forEach((integer, potionEnhancementData) -> potionEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
        this.potionEnhancements.get("jump").levels.forEach((integer, potionEnhancementData) -> potionEnhancementData.bankCosts = new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build());
    }

    public Enhancement<SizeEnhancementData> sizeEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.GRASS_BLOCK, 11, 1, "&9&lSize Upgrade", Arrays.asList(
            "&7Need more room to expand? Buy this",
            "&7upgrade to increase your island size.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Current Size: &9%size%x%size% Blocks",
            "&9&l * &7Upgrade Cost: &9%vault_cost%, %Crystals_cost% Island Crystals",
            "&9&lLevels:",
            "&9&l * &7Level 1: &950x50 Blocks",
            "&9&l * &7Level 2: &975x75 Blocks",
            "&9&l * &7Level 3: &9100x100 Blocks",
            "&9&l * &7Level 4: &9125x125 Blocks",
            "&9&l * &7Level 5: &9150x150 Blocks",
            "",
            "&9[!] &7Must be level %minLevel% to purchase",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, SizeEnhancementData>()
            .put(0, new SizeEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 50))
            .put(1, new SizeEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 75))
            .put(2, new SizeEnhancementData(10, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 100))
            .put(3, new SizeEnhancementData(10, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 125))
            .put(4, new SizeEnhancementData(15, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 150))
            .build());

    public Enhancement<VoidEnhancementData> voidEnhancement = new Enhancement<>(false, EnhancementType.UPGRADE, new Item(XMaterial.RED_BED, 0, 1, "&9&lVoid Upgrade", Arrays.asList(
            "&7Keep falling off your island?",
            "&7upgrade to improve void teleport.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Upgrade Cost: &9%vault_cost%, %Crystals_cost% Island Crystals",
            "&9&lLevels:",
            "&9&l * &7Level 1: &9No Void Teleport",
            "&9&l * &7Level 2: &9100% Item Loss",
            "&9&l * &7Level 3: &975% Item Loss",
            "&9&l * &7Level 4: &950% Item Loss",
            "&9&l * &7Level 5: &925% Item Loss",
            "&9&l * &7Level 6: &90% Item Loss",
            "",
            "&9[!] &7Must be level %minLevel% to purchase",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, VoidEnhancementData>()
            .put(0, new VoidEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), true, 0.00))
            .build());

    public Enhancement<GeneratorEnhancementData> generatorEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.DIAMOND_ORE, 13, 1, "&9&lGenerator Upgrade", Arrays.asList(
            "&7Want to improve your generator? Buy this",
            "&7upgrade to improve your island generator.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Upgrade Cost: &9%vault_cost%, %Crystals_cost% Island Crystals",
            "",
            "&9[!] &7Must be level %minLevel% to purchase",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, GeneratorEnhancementData>()
            .put(0, new GeneratorEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.COBBLESTONE, 3)
                            .put(XMaterial.COAL_ORE, 1)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 1)
                            .build()))
            .put(1, new GeneratorEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.REDSTONE_ORE, 10)
                            .put(XMaterial.LAPIS_ORE, 10)
                            .put(XMaterial.COAL_ORE, 20)
                            .put(XMaterial.COBBLESTONE, 40)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 20)
                            .put(XMaterial.GLOWSTONE, 20)
                            .put(XMaterial.NETHERRACK, 20)
                            .build()))
            .put(2, new GeneratorEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.IRON_ORE, 10)
                            .put(XMaterial.REDSTONE_ORE, 10)
                            .put(XMaterial.GOLD_ORE, 10)
                            .put(XMaterial.LAPIS_ORE, 10)
                            .put(XMaterial.COAL_ORE, 20)
                            .put(XMaterial.COBBLESTONE, 40)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 10)
                            .put(XMaterial.GLOWSTONE, 10)
                            .put(XMaterial.NETHER_QUARTZ_ORE, 10)
                            .put(XMaterial.NETHER_GOLD_ORE, 10)
                            .put(XMaterial.NETHERRACK, 10)
                            .build()))
            .put(3, new GeneratorEnhancementData(5, 10000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.DIAMOND_ORE, 3)
                            .put(XMaterial.IRON_ORE, 10)
                            .put(XMaterial.REDSTONE_ORE, 10)
                            .put(XMaterial.GOLD_ORE, 10)
                            .put(XMaterial.LAPIS_ORE, 10)
                            .put(XMaterial.COAL_ORE, 20)
                            .put(XMaterial.COBBLESTONE, 40)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 10)
                            .put(XMaterial.GLOWSTONE, 10)
                            .put(XMaterial.NETHER_QUARTZ_ORE, 10)
                            .put(XMaterial.NETHER_GOLD_ORE, 10)
                            .put(XMaterial.NETHERRACK, 10)
                            .put(XMaterial.ANCIENT_DEBRIS, 1)
                            .build()))
            .build());
}
