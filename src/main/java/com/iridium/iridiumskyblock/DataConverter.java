package com.iridium.iridiumskyblock;

import com.google.common.io.CharStreams;
import com.iridium.iridiumskyblock.configs.SQL;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class DataConverter {

    public static void copyLegacyData() {
        String[] fileNames = Objects.requireNonNull(IridiumSkyblock.getInstance().getDataFolder().list());
        if (!Arrays.asList(fileNames).contains("config.json")) {
            // Don't run this converter
            return;
        }

        IridiumSkyblock.getInstance().getLogger().info("Starting 2.6.7 -> 3.0.0 conversion process...");
        new File(IridiumSkyblock.getInstance().getDataFolder() + File.separator + "old_data" + File.separator + "schematics" + File.separator).mkdirs();

        Arrays.stream(Objects.requireNonNull(IridiumSkyblock.getInstance().getDataFolder()
                .listFiles((file, fileName) -> fileName.endsWith(".json") || fileName.endsWith(".db"))))
                .forEach(file -> {
                    try {
                        Path source = Paths.get(file.getAbsolutePath());
                        Path target = Paths.get(file.getParentFile().getAbsolutePath() + File.separator + "old_data" + File.separator + file.getName());
                        IridiumSkyblock.getInstance().getLogger().info("Moving " + file.getName() + " to " + target.toFile().getAbsolutePath() + "...");
                        Files.move(source, target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Arrays.stream(Objects.requireNonNull(new File(IridiumSkyblock.getInstance().getDataFolder().getAbsolutePath() + File.separator + "schematics" + File.separator)
                .listFiles((file, fileName) -> fileName.endsWith(".schem") || fileName.endsWith(".schematic"))))
                .forEach(file -> {
                    try {
                        Path source = Paths.get(file.getAbsolutePath());
                        Path target = Paths.get(file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "old_data" + File.separator + "schematics" + File.separator + file.getName());
                        IridiumSkyblock.getInstance().getLogger().info("Moving " + file.getName() + " to " + target.toFile().getAbsolutePath() + "...");
                        Files.move(source, target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void deleteDuplicateRecords() {
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().deleteDuplicates();
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().deleteDuplicates();
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().deleteDuplicates();
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().deleteDuplicates();
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().deleteDuplicates();
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager().deleteDuplicates();
    }

    public static void updateDatabaseData(int oldVersion, int newVersion, ConnectionSource connectionSource, SQL.Driver driver) {
        IridiumSkyblock.getInstance().getLogger().info("Updating database from version " + oldVersion + " to " + newVersion);

        try {
            DatabaseConnection connection = connectionSource.getReadWriteConnection(null);
            for (int version = oldVersion; version <= newVersion; version++) {
                InputStream inputStream = findPatchInputStream(version, driver);
                if (inputStream == null) continue;

                for (String statement : CharStreams.toString(new InputStreamReader(inputStream)).split("\n")) {
                    connection.executeStatement(statement, DatabaseConnection.DEFAULT_RESULT_FLAGS);
                }
            }

            connectionSource.releaseConnection(connection);
            IridiumSkyblock.getInstance().getLogger().info("Update successful!");
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    private static InputStream findPatchInputStream(int version, SQL.Driver driver) {
        InputStream inputStream = IridiumSkyblock.getInstance().getResource("patch_" + version + ".sql");
        if (inputStream == null) {
            inputStream = IridiumSkyblock.getInstance().getResource("patch_" + version + "_" + driver.name().toLowerCase() + ".sql");
        }

        return inputStream;
    }

}
