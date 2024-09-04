package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.RandomAccessList;
import com.iridium.iridiumskyblock.enhancements.GeneratorEnhancementData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.*;

public class BlockFormListener implements Listener {

    private final Map<Integer, RandomAccessList<XMaterial>> normalOreLevels = new HashMap<>();
    private final Map<Integer, RandomAccessList<XMaterial>> netherOreLevels = new HashMap<>();

    private final List<XMaterial> generatorMaterials = Arrays.asList(XMaterial.STONE, XMaterial.COBBLESTONE, XMaterial.BASALT);

    public BlockFormListener() {
        for (Map.Entry<Integer, GeneratorEnhancementData> oreUpgrade : IridiumSkyblock.getInstance().getEnhancements().generatorEnhancement.levels.entrySet()) {
            normalOreLevels.put(oreUpgrade.getKey(), new RandomAccessList<>(oreUpgrade.getValue().ores));
            netherOreLevels.put(oreUpgrade.getKey(), new RandomAccessList<>(oreUpgrade.getValue().netherOres));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        XMaterial newMaterial = XMaterial.matchXMaterial(event.getNewState().getType());
        if (!generatorMaterials.contains(newMaterial)) return;
        IridiumSkyblock.getInstance().getIslandManager().getTeamViaLocation(event.getNewState().getLocation()).ifPresent(island -> {
            int upgradeLevel = IridiumSkyblock.getInstance().getIslandManager().getTeamEnhancement(island, "generator").getLevel();
            boolean isBasaltGenerator = newMaterial == XMaterial.BASALT;
            RandomAccessList<XMaterial> randomMaterialList = isBasaltGenerator ? netherOreLevels.get(upgradeLevel) : normalOreLevels.get(upgradeLevel);
            if (randomMaterialList == null) return;

            Optional<XMaterial> xMaterialOptional = randomMaterialList.nextElement();
            if (!xMaterialOptional.isPresent()) return;

            if( isBasaltGenerator && IridiumSkyblock.getInstance().getConfiguration().netherOnlyGenerator && event.getNewState().getWorld().getEnvironment() != World.Environment.NETHER) return;

            Material material = xMaterialOptional.get().parseMaterial();
            if (material == Material.COBBLESTONE && newMaterial == XMaterial.STONE) material = Material.STONE;
            if (material != null) event.getNewState().setType(material);
        });
    }
}