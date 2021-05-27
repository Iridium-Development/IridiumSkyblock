package com.iridium.iridiumskyblock;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class DataConverter {

    private static IridiumSkyblock iridiumSkyblock;

    public static void run(IridiumSkyblock instance) {
        iridiumSkyblock = instance;

        v3_0_0();
    }

    @SneakyThrows
    private static void v3_0_0() {
        String[] fileNames = Objects.requireNonNull(iridiumSkyblock.getDataFolder().list());
        if (!Arrays.asList(fileNames).contains("bank.json")) {
            // Don't run this converter
            return;
        }

        iridiumSkyblock.getLogger().info("Starting 2.6.7 -> 3.0.0 conversion process...");
        new File(iridiumSkyblock.getDataFolder() + File.separator + "old_data" + File.separator + "schematics" + File.separator).mkdirs();

        Arrays.stream(Objects.requireNonNull(iridiumSkyblock.getDataFolder()
            .listFiles((file, fileName) -> fileName.endsWith(".json") || fileName.endsWith(".db"))))
            .forEach(file -> {
                try {
                    Path source = Paths.get(file.getAbsolutePath());
                    Path target = Paths.get(file.getParentFile().getAbsolutePath() + File.separator + "old_data" + File.separator + file.getName());
                    iridiumSkyblock.getLogger().info("Moving " + file.getName() + " to " + target.toFile().getAbsolutePath() + "...");
                    Files.move(source, target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        Arrays.stream(Objects.requireNonNull(new File(iridiumSkyblock.getDataFolder().getAbsolutePath() + File.separator + "schematics" + File.separator)
            .listFiles((file, fileName) -> fileName.endsWith(".schem") || fileName.endsWith(".schematic"))))
            .forEach(file -> {
                try {
                    Path source = Paths.get(file.getAbsolutePath());
                    Path target = Paths.get(file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "old_data" + File.separator + "schematics" + File.separator + file.getName());
                    iridiumSkyblock.getLogger().info("Moving " + file.getName() + " to " + target.toFile().getAbsolutePath() + "...");
                    Files.move(source, target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        String host = "";
        String database = "";
        String username = "";
        String password = "";
        String port = "";

        Pattern hostPattern = Pattern.compile(".+\"host\":\\s\"(.+)\"(.+)?");
        Pattern databasePattern = Pattern.compile(".+\"database\":\\s\"(.+)\"(.+)?");
        Pattern usernamePattern = Pattern.compile(".+\"username\":\\s\"(.+)\"(.+)?");
        Pattern passwordPattern = Pattern.compile(".+\"password\":\\s(.+),?");
        Pattern portPattern = Pattern.compile(".+\"port\":\\s(.+),?");

        for (String line : Files.lines(Paths.get(new File(iridiumSkyblock.getDataFolder() + File.separator + "old_data" + File.separator + "sql.json").getAbsolutePath())).collect(Collectors.toList())) {
            Matcher hostPatternMatcher = hostPattern.matcher(line);
            Matcher databasePatternMatcher = databasePattern.matcher(line);
            Matcher usernamePatternMatcher = usernamePattern.matcher(line);
            Matcher passwordPatternMatcher = passwordPattern.matcher(line);
            Matcher portPatternMatcher = portPattern.matcher(line);

            if (hostPatternMatcher.find()) host = hostPatternMatcher.group(1);
            if (databasePatternMatcher.find()) database = databasePatternMatcher.group(1);
            if (usernamePatternMatcher.find()) username = usernamePatternMatcher.group(1);
            if (passwordPatternMatcher.find()) password = passwordPatternMatcher.group(1);
            if (portPatternMatcher.find()) port = portPatternMatcher.group(1);
        }

        String connectionString;
        if (username.isEmpty()) {
            connectionString = "jdbc:sqlite:" + new File(IridiumSkyblock.getInstance().getDataFolder(),"old_data" + File.separator + "IridiumSkyblock.db");
        } else {
            connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database;
        }

        ConnectionSource connectionSource = new JdbcConnectionSource(
            connectionString,
            username,
            password,
            DatabaseTypeUtils.createDatabaseType(connectionString)
        );

        ResultSet resultSet = connectionSource.getReadOnlyConnection("users").getUnderlyingConnection().prepareStatement("SELECT * FROM users").executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + ":" + resultSet.getString(2));
        }
    }

}
