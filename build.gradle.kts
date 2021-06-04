plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.iridium"
version = "3.0.3"
description = "IridiumSkyblock"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("https://repo.mvdw-software.com/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://jitpack.io")
    maven("https://nexus.savagelabs.net/repository/maven-releases/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.jeff-media.de/maven2/")
}

dependencies {
    // Dependencies that we want to shade in
    implementation("org.jetbrains", "annotations", "16.0.1")
    implementation("com.iridium", "IridiumCore", "1.0.4")
    implementation("org.bstats", "bstats-bukkit", "2.2.1")
    implementation("com.github.j256", "ormlite-core", "master-SNAPSHOT")
    implementation("com.j256.ormlite", "ormlite-jdbc", "5.3")
    implementation("de.jeff_media", "SpigotUpdateChecker", "1.2.0")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok", "lombok", "1.18.20")
    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.ess3", "EssentialsXSpawn", "2.16.1")
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly("me.clip", "placeholderapi", "2.9.2")
    compileOnly("be.maximvdw", "MVdWPlaceholderAPI", "2.1.1-SNAPSHOT") {
        exclude("org.spigotmc")
    }

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok", "lombok", "1.18.20")
}

tasks {
    // "Replace" the build task with the shadowJar task (probably bad but who cares)
    jar {
        dependsOn("shadowJar")
        enabled = false
    }

    // Relocate dependencies, remove the archive classifier suffix
    shadowJar {
        archiveClassifier.set("")
        relocate("com.fasterxml.jackson", "com.iridium.iridiumskyblock.dependencies.fasterxml")
        relocate("com.j256.ormlite", "com.iridium.iridiumskyblock.dependencies.ormlite")
        relocate("org.bstats", "com.iridium.iridiumskyblock.dependencies.bstats")

        // Remove unnecessary files from the jar
        minimize()
    }

    // Set UTF-8 as the encoding
    compileJava {
        options.encoding = "UTF-8"
    }

    // Process Placeholders for the plugin.yml
    processResources {
        filesMatching("**/plugin.yml") {
            expand(rootProject.project.properties)
        }

        // Always re-run this task
        outputs.upToDateWhen { false }
    }
}

// Maven publishing
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
