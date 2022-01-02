package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.upgrades.OresUpgrade;
import com.iridium.iridiumskyblock.utils.RandomAccessList;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BlockFormListener implements Listener {

    private static final Map<Integer, RandomAccessList<XMaterial>> normalOreLevels = new HashMap<>();
    private static final Map<Integer, RandomAccessList<XMaterial>> netherOreLevels = new HashMap<>();

    public static void generateOrePossibilities() {
        for (Map.Entry<Integer, OresUpgrade> oreUpgrade : IridiumSkyblock.getInstance().getUpgrades().oresUpgrade.upgrades.entrySet()) {
            normalOreLevels.put(oreUpgrade.getKey(), new RandomAccessList<>(oreUpgrade.getValue().ores));
            netherOreLevels.put(oreUpgrade.getKey(), new RandomAccessList<>(oreUpgrade.getValue().netherOres));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;

        XMaterial newMaterial = XMaterial.matchXMaterial(event.getNewState().getType());
        // Custom basalt generators should only work in nether
        if (newMaterial == XMaterial.COBBLESTONE || newMaterial == XMaterial.STONE || (newMaterial == XMaterial.BASALT && event.getBlock().getLocation().getWorld().getEnvironment() == World.Environment.NETHER)) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getNewState().getLocation());
            if (island.isPresent()) {
                int upgradeLevel = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "generator").getLevel();
                RandomAccessList<XMaterial> randomMaterialList = newMaterial == XMaterial.BASALT ? netherOreLevels.get(upgradeLevel) : normalOreLevels.get(upgradeLevel);
                if (randomMaterialList == null) return;

                Optional<XMaterial> xMaterialOptional = randomMaterialList.nextElement();
                if (!xMaterialOptional.isPresent()) return;

                Material material = xMaterialOptional.get().parseMaterial();
                if (material == Material.COBBLESTONE && newMaterial == XMaterial.STONE) material = Material.STONE;
                if (material != null) event.getNewState().setType(material);
            }
        }
    }

}
