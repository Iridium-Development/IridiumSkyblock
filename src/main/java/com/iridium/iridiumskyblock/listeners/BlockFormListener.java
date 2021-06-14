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
    private static Map<Integer, Material[]> upgradesParsed, upgradesParsedNether;

    public static void parseMaterialFrequencies() {
        upgradesParsed = new LinkedTreeMap<>();
        upgradesParsedNether = new LinkedTreeMap<>();
        for (Map.Entry<Integer, OresUpgrade> upgrades : IridiumSkyblock.getInstance().getUpgrades().oresUpgrade.upgrades.entrySet()) {
            int oreFrequencySum = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().ores.entrySet()) {
                oreFrequencySum += entries.getValue();
            }
            int netherOreFrequencySum = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().netherOres.entrySet()) {
                netherOreFrequencySum += entries.getValue();
            }
            Material[] oreFrequency = new Material[oreFrequencySum];
            int index = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().ores.entrySet()) {
                Material material = entries.getKey().parseMaterial();
                for (int i = 0; i < entries.getValue(); i++) {
                    oreFrequency[index++] = material;
                }
            }
            Material[] oreFrequencyNether = new Material[netherOreFrequencySum];
            int indexNether = 0;
            for (Map.Entry<XMaterial, Integer> entries : upgrades.getValue().netherOres.entrySet()) {
                Material material = entries.getKey().parseMaterial();
                for (int i = 0; i < entries.getValue(); i++) {
                    oreFrequencyNether[indexNether++] = material;
                }
            }
            upgradesParsed.put(upgrades.getKey(), oreFrequency);
            upgradesParsedNether.put(upgrades.getKey(), oreFrequencyNether);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        XMaterial newMaterial = XMaterial.matchXMaterial(event.getNewState().getType());
        boolean nether = event.getBlock().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER);
        // Custom basalt generators should only work in nether
        if (newMaterial.equals(XMaterial.COBBLESTONE) || newMaterial.equals(XMaterial.STONE) ||
                (newMaterial.equals(XMaterial.BASALT) && nether)) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getNewState().getLocation());
            if (island.isPresent()) {
                Material[] materials = (nether ? upgradesParsed : upgradesParsedNether).get(IridiumSkyblock.getInstance().getIslandManager()
                        .getIslandUpgrade(island.get(), "generator").getLevel());
                Material material = materials[random.nextInt(materials.length)];
                //turn cobblestone into stone if the generator is a stone generator
                if (material == Material.COBBLESTONE && newMaterial == XMaterial.STONE)
                    material = Material.STONE;
                if (material != null) event.getNewState().setType(material);
            }
        }
    }
}