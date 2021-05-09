package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.upgrades.OresUpgrade;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.*;

public class BlockFormListener implements Listener {

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        XMaterial newMaterial = XMaterial.matchXMaterial(event.getNewState().getType());
        if (newMaterial.equals(XMaterial.COBBLESTONE) || newMaterial.equals(XMaterial.BASALT)) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getNewState().getLocation());
            if (island.isPresent()) {
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "generator");
                OresUpgrade oresUpgrade = IridiumSkyblock.getInstance().getUpgrades().oresUpgrade.upgrades.get(islandUpgrade.getLevel());
                Map<XMaterial, Integer> ores = newMaterial.equals(XMaterial.BASALT) ? oresUpgrade.netherOres : oresUpgrade.ores;
                List<XMaterial> materials = new ArrayList<>();
                Random random = new Random();

                for (Map.Entry<XMaterial, Integer> entries : ores.entrySet()) {
                    for (int i = 0; i < entries.getValue(); i++) {
                        materials.add(entries.getKey());
                    }
                }

                Material material = materials.get(random.nextInt(materials.size())).parseMaterial();
                if (material != null) event.getNewState().setType(material);
            }
        }
    }
}
