package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SkyblockBiomeProvider extends BiomeProvider {

    List<Biome> biomeList = new ArrayList<>();

    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {

        // Apparently this can happen.
        if(worldInfo == null) { return Biome.THE_VOID; }

        // APPARENTLY THIS CAN HAPPEN???
        if(biomeList.isEmpty()) {
            getBiomes(worldInfo);
        }

        if(biomeList.size() == 1) {
            return biomeList.get(0);
        }

        return getBiomeFromNoise(worldInfo, x, z);
    }

    // This is a required override to tell the generator what biomes it's allowed to use.
    @Override
    @NotNull
    public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {

        this.biomeList = sortBiomeData(validateList(getBiomeDataConfig(worldInfo.getEnvironment())));

        IridiumSkyblock.getInstance().getLogger().info("Biomes for: " + worldInfo.getName());
        for(int i = 0; i < biomeList.size(); i++) {
            IridiumSkyblock.getInstance().getLogger().info((i + 1) + " - " + biomeList.get(i).toString());
        }

        return biomeList;
    }

    private List<Generators.BiomeDataConfig> validateList(List<Generators.BiomeDataConfig> biomeDataConfigList) {

        for(Generators.BiomeDataConfig biomeDataConfig : biomeDataConfigList) {
            if(biomeDataConfig.biome.get() == null) {
                IridiumSkyblock.getInstance().getLogger().warning("XBiome: \"" + biomeDataConfig.biome + "\" cannot be read.");
                IridiumSkyblock.getInstance().getLogger().warning("Removing this entry from the list.");
            }
        }

        List<Generators.BiomeDataConfig> listCopy = biomeDataConfigList.stream().filter(
                biomeDataConfig -> biomeDataConfig.biome.get() != null).collect(Collectors.toList());

        return listCopy;
    }

    private List<Biome> sortBiomeData(List<Generators.BiomeDataConfig> biomeDataConfigList) {

        List<Generators.BiomeDataConfig> sortedList = biomeDataConfigList.stream()
            .sorted((entry1, entry2) -> {
                Generators.BiomeData biome1 = entry1.biomeData;
                Generators.BiomeData biome2 = entry2.biomeData;

                int temperatureCompare = Double.compare(biome1.temperature, biome2.temperature);
                if (temperatureCompare != 0) return temperatureCompare;

                int humidityCompare = Double.compare(biome1.humidity, biome2.humidity - 1);
                if (humidityCompare != 0) return humidityCompare;

                return Double.compare(biome1.height, biome2.height);
            }).collect(Collectors.toList());

        if(IridiumSkyblock.getInstance().getGenerators().inverseBiomeGradient) {
            Collections.reverse(sortedList);
        }

        // Not using a stream here because we need to ensure this stays in order.
        List<Biome> biomeList = new ArrayList<>();
        for(Generators.BiomeDataConfig biomeDataConfig : sortedList) {
            biomeList.add(biomeDataConfig.biome.get());
        }

        return biomeList;
    }

    private Biome getBiomeFromNoise(WorldInfo worldInfo, int x, int z) {

        Random random = new Random(worldInfo.getSeed());

        // Noise is always a value between -1 and 1.
        // Here, we're associating a noise value with an index value in the array.

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(random, IridiumSkyblock.getInstance().getGenerators().simplexBiomeOctave);
        generator.setScale(IridiumSkyblock.getInstance().getGenerators().simplexBiomeScale);

        double noise = generator.noise(x, z, 1, 2, true);
        int normalizedIndex;

        if(IridiumSkyblock.getInstance().getGenerators().biomeCurve) {
            normalizedIndex = (int) (0.5 * Math.pow(((noise + 1) * 0.5 * biomeList.size()), 2));
        } else {
            normalizedIndex = (int) ((noise + 1) * 0.5 * biomeList.size());
        }

        // Coin flip
        if(IridiumSkyblock.getInstance().getGenerators().increaseRandom) {
            if(random.nextBoolean()) {
                normalizedIndex = random.nextBoolean() ?  normalizedIndex + 1 : normalizedIndex - 1;
            }
        }

        // Checks
        if(normalizedIndex >= biomeList.size()) normalizedIndex = biomeList.size() - 1;
        if(normalizedIndex < 0) normalizedIndex = 0;

        return biomeList.get(normalizedIndex);
    }

    private List<Generators.BiomeDataConfig> getBiomeDataConfig(@NotNull World.Environment environment) {
        switch(IridiumSkyblock.getInstance().getConfiguration().generatorType.toLowerCase()) {
            case "superflat": {}
            case "superflat-legacy": { return getFlatGenerator(environment).biomeDataConfig; }
            case "ocean": {}
            case "ocean-legacy": { return getOceanGenerator(environment).biomeDataConfig; }
            default: { return getSkyblockGenerator(environment).biomeDataConfig; }
        }
    }

    private Generators.FlatGeneratorWorld getFlatGenerator(World.Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().flatGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().flatGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().flatGenerator.overworld;
            }
        }
    }

    private Generators.OceanGeneratorWorld getOceanGenerator(World.Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().oceanGenerator.overworld;
            }
        }
    }

    private Generators.SkyblockGeneratorWorld getSkyblockGenerator(World.Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.overworld;
            }
        }
    }
}
