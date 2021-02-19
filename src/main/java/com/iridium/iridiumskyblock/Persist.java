package com.iridium.iridiumskyblock;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class Persist {
    private final ObjectMapper objectMapper;
    private final PersistType persistType;

    public Persist(PersistType persistType) {
        this.persistType = persistType;
        switch (persistType) {
            case YAML:
                objectMapper = new ObjectMapper(new YAMLFactory());
                break;
            default:
                objectMapper = new ObjectMapper(new JsonFactory());
        }
    }

    public static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    // ------------------------------------------------------------ //
    // GET NAME - What should we call this type of object?
    // ------------------------------------------------------------ //

    public static String getName(Object o) {
        return getName(o.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    // ------------------------------------------------------------ //
    // GET FILE - In which file would we like to store this object?
    // ------------------------------------------------------------ //

    public File getFile(String name) {
        return new File(IridiumSkyblock.getInstance().getDataFolder(), name + persistType.getExtension());
    }

    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    public File getFile(Object obj) {
        return getFile(getName(obj));
    }

    // SAVE

    public void save(Object instance) {
        save(instance, getFile(instance));
    }

    public void save(Object instance, File file) {
        try {
            objectMapper.writeValue(file, instance);
        } catch (IOException e) {
            IridiumSkyblock.getInstance().getLogger().severe("Failed to save " + file.toString() + ": " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(IridiumSkyblock.getInstance());
        }
    }

    public String toString(Object instance) {
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (IOException e) {
            IridiumSkyblock.getInstance().getLogger().severe("Failed to save " + instance.toString() + ": " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(IridiumSkyblock.getInstance());
        }
        return "";
    }

    // LOAD BY CLASS

    public <T> T load(Class<T> clazz) {
        return load(clazz, getFile(clazz));
    }

    public <T> T load(Class<T> clazz, File file) {
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                IridiumSkyblock.getInstance().getLogger().severe("Failed to parse " + file.toString() + ": " + e.getMessage());
                Bukkit.getPluginManager().disablePlugin(IridiumSkyblock.getInstance());
            }
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T load(Class<T> clazz, String content) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            Bukkit.getPluginManager().disablePlugin(IridiumSkyblock.getInstance());
        }

        return null;
    }

    public enum PersistType {
        YAML(".yml"), JSON(".json");
        private final String extension;

        PersistType(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }
}