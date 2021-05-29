package com.iridium.iridiumskyblock.upgrades;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.Map;

@NoArgsConstructor
public class OresUpgrade extends UpgradeData {
    @JsonSerialize
    private Map<XMaterial, Integer> ores;
    @JsonSerialize
    private Map<XMaterial, Integer> netherOres;
    @JsonIgnore
    private Material[] oresArray;
    @JsonIgnore
    private Material[] netherOresArray;
    @JsonIgnore
    private boolean initialized = false;

    public OresUpgrade(int money, int crystals, Map<XMaterial, Integer> ores, Map<XMaterial, Integer> netherOres) {
        super(money, crystals);
        this.ores = ores;
        this.netherOres = netherOres;
    }

    @JsonIgnore
    public Material[] getOres() {
        if (!initialized) initialize();
        return oresArray;
    }

    @JsonIgnore
    public Material[] getNetherOres() {
        if (!initialized) initialize();
        return netherOresArray;
    }

    @JsonIgnore
    private void initialize() {
        int oreFrequencySum = 0;
        for (Map.Entry<XMaterial, Integer> entries : ores.entrySet()) {
            oreFrequencySum += entries.getValue();
        }
        this.oresArray = new Material[oreFrequencySum];
        int index = 0;
        for (Map.Entry<XMaterial, Integer> entries : ores.entrySet()) {
            Material material = entries.getKey().parseMaterial();
            for (int i = 0; i < entries.getValue(); i++) {
                this.oresArray[index++] = material;
            }
        }

        int netherOreFrequencySum = 0;
        for (Map.Entry<XMaterial, Integer> entries : netherOres.entrySet()) {
            netherOreFrequencySum += entries.getValue();
        }
        this.netherOresArray = new Material[netherOreFrequencySum];
        int indexNether = 0;
        for (Map.Entry<XMaterial, Integer> entries : netherOres.entrySet()) {
            Material material = entries.getKey().parseMaterial();
            for (int i = 0; i < entries.getValue(); i++) {
                this.netherOresArray[indexNether++] = material;
            }
        }
        initialized = true;
    }
}
