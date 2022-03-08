package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all IridiumSkyblock chunk generators.
 */
public abstract class IridiumChunkGenerator extends ChunkGenerator {

    /**
     * Returns what a made with this generator is mainly consisting of.<p>
     * Used for performance improvements.
     *
     * @param world the world that should be checked
     * @return the most used material of the chunk generator in this generator
     */
    public abstract XMaterial getMainMaterial(@Nullable World world);

    /**
     * Returns whether the main material returned by {@link IridiumChunkGenerator#getMainMaterial(World)}
     * should be ignored.
     *
     * @return if the main material should be ignored
     */
    public boolean ignoreMainMaterial() {
        return getMainMaterial(null) == XMaterial.AIR;
    }

}
