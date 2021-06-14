package com.iridium.iridiumskyblock.listeners;

import com.google.gson.internal.LinkedTreeMap;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.upgrades.OresUpgrade;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class BlockFormListener implements Listener {

    private Random random = new Random();
    /**
     * upgradesParsed is a {@link Map} collection that contains {@link Integer}
     * as key for the upgrade level and a two dimensional
     * {@link Material} array where dimension one is for choosing
     * the values for overworld or nether and dimension two are
     * materials inserted as many times as their chance of forming.
     */
    private final Map<Integer, Material[][]> upgradesParsed;

    public BlockFormListener() {
        upgradesParsed = new LinkedTreeMap<>();
        for (Map.Entry<Integer, OresUpgrade> upgrades : IridiumSkyblock.getInstance().getUpgrades().oresUpgrade.upgrades.entrySet()) {
            Material[][] oresArrays;
            int oreFrequencySum = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().ores.entrySet()) {
                oreFrequencySum += entries.getValue();
            }
            int netherOreFrequencySum = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().netherOres.entrySet()) {
                netherOreFrequencySum += entries.getValue();
            }
            oresArrays = new Material[][]{new Material[oreFrequencySum], new Material[netherOreFrequencySum]};
            oresArrays[0] = new Material[oreFrequencySum];
            int index = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().ores.entrySet()) {
                Material material = entries.getKey().parseMaterial();
                for (int i = 0; i < entries.getValue(); i++) {
                    oresArrays[0][index++] = material;
                }
            }
            oresArrays[1] = new Material[netherOreFrequencySum];
            int indexNether = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().netherOres.entrySet()) {
                Material material = entries.getKey().parseMaterial();
                for (int i = 0; i < entries.getValue(); i++) {
                    oresArrays[1][indexNether++] = material;
                }
            }
            upgradesParsed.put(upgrades.getKey(), oresArrays);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        XMaterial newMaterial = XMaterial.matchXMaterial(event.getNewState().getType());
        // Custom basalt generators should only work in nether
        if (newMaterial.equals(XMaterial.COBBLESTONE) || newMaterial.equals(XMaterial.STONE) || (newMaterial.equals(XMaterial.BASALT) && event.getBlock().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER))) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getNewState().getLocation());
            if (island.isPresent()) {
                Material[] materials = upgradesParsed.get(IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "generator").getLevel())[newMaterial.equals(XMaterial.BASALT) ? 1 : 0];
                Material material = materials[random.nextInt(materials.length)];
                if (material == Material.COBBLESTONE && event.getBlock().getType() == Material.STONE)
                    material = Material.STONE;
                if (material != null) event.getNewState().setType(material);
            }
        }
    }
}