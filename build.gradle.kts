plugins {
    java
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.8"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.iridium"
version = "4.1.0-B6"
description = "Skyblock Redefined"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    // Dependencies that we want to shade in
    implementation("org.jetbrains:annotations:26.0.2")
    implementation("com.j256.ormlite:ormlite-core:6.1")
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")
    implementation("com.iridium:IridiumTeams:2.5.13")
    implementation("commons-lang:commons-lang:2.6")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.38")
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("net.ess3:EssentialsXSpawn:2.16.1")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.11")

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

bukkit {
    main = "com.iridium.iridiumskyblock.IridiumSkyblock"
    apiVersion = "1.13"
    author = "Peaches_MLG"
    authors = listOf("das_", "sh0inx", "SlashRemix", "Faun471")
    website = "https://www.spigotmc.org/resources/62480"

    loadBefore = listOf("Multiverse-Core")
    depend = listOf("Vault")
    softDepend = listOf(
        "MVdWPlaceholderAPI",
        "PlaceholderAPI",
        "Essentials",
        "EssentialsSpawn",
        "RoseStacker",
        "WorldEdit",
        "ObsidianStacker"
    )

    commands {
        register("iridiumskyblock") {
            description = "Main plugin command"
            aliases = listOf("is", "island")
        }
    }
}

tasks {
    // Add the shadowJar task to the build task
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        fun relocate(origin: String) =
                relocate(origin, "com.iridium.iridiumskyblock.dependencies${origin.substring(origin.lastIndexOf('.'))}")

        // Remove the archive classifier suffix
        archiveClassifier.set("")

        // Relocate dependencies
        relocate("com.iridium.iridiumteams")
        relocate("com.iridium.iridiumcore")
        relocate("com.j256.ormlite")
        relocate("de.jeff_media.updatechecker")
        relocate("org.bstats")
        relocate("org.intellij.lang.annotations")
        relocate("org.jetbrains.annotations")
        relocate("org.jnbt")

        // Relocate IridiumCore dependencies
        relocate("de.tr7zw.changeme.nbtapi")
        relocate("com.iridium.iridiumcolorapi")
        relocate("org.yaml.snakeyaml")
        relocate("io.papermc.lib")
        relocate("com.cryptomorin.xseries")
        relocate("com.fasterxml.jackson")
        relocate("org.apache.commons")

        // Remove unnecessary files from the jar
        minimize()
    }

    // Set UTF-8 as the encoding
    compileJava {
        options.encoding = "UTF-8"
    }

    compileJava {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    compileTestJava {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
}

// Maven publishing
publishing {
    publications.create<MavenPublication>("maven") {
        setGroupId("com.iridium")
        setArtifactId("IridiumSkyblock")
        setVersion(version)
        artifact(tasks["shadowJar"])
    }
}
