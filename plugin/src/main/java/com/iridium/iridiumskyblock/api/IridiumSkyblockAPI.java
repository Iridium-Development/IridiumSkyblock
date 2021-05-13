package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IridiumSkyblock;

/**
 * General api for IridiumSkyblock.
 * It is accessible via {@link IridiumSkyblockAPI#getInstance()}.
 */
public class IridiumSkyblockAPI {

    private static IridiumSkyblockAPI instance;
    private final IridiumSkyblock iridiumSkyblock;

    /**
     * Constructor for api initialization.
     *
     * @param iridiumSkyblock The instance of the {@link IridiumSkyblock} class
     */
    private IridiumSkyblockAPI(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Accesses the api instance.
     * Might be null if this method is called when {@link IridiumSkyblock}'s startup method is still being executed.
     *
     * @return The instance of this api
     * @since 3.0.0
     */
    public static IridiumSkyblockAPI getInstance() {
        return instance;
    }

    /**
     * Initializes the api. For internal use only.
     *
     * @param iridiumSkyblock The {@link IridiumSkyblock} instance used by the plugin
     */
    public static synchronized void initializeAPI(IridiumSkyblock iridiumSkyblock) {
        if (instance == null) {
            instance = new IridiumSkyblockAPI(iridiumSkyblock);
        }
    }

}
