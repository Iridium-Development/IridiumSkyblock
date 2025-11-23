plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version "9.2.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.iridium"
version = "4.1.3-b1_KEPLAR"
description = "Skyblock Redefined"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")  // spigotmc
    maven("https://ci.ender.zone/plugin/repository/everything/")             // enderzone
    maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/") // our super cool repo
    maven("https://papermc.io/repo/repository/maven-public/")                // papermc
    maven("https://jitpack.io")                                              // jitpack
    maven("https://maven.enginehub.org/repo/")                               // maven
}

dependencies {
    // Dependencies that we want to shade in
    implementation("org.jetbrains:annotations:26.0.2-1")                          // annotations
    implementation("com.j256.ormlite:ormlite-core:6.1")                           // SQL OrmLite
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")                           // SQL OrmLite java bridge
    implementation("com.iridium:IridiumTeams:2.6.10")                             // teams
    // implementation("commons-lang:commons-lang:2.6")                            // lang (needed for generator config)

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.42")                               // annotations
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")                   // spigot
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")                             // vault
    compileOnly("net.ess3:EssentialsXSpawn:2.16.1")                               // essentialsx spawn
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.13-SNAPSHOT")           // WE, AWE, & FAWE
    compileOnly("net.dmulloy2:ProtocolLib:5.3.0")                                 // protocol lib (5.4.0+ is java 17+)

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.42")                       // annotations
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
        "ObsidianStacker",
        "ProtocolLib"
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

        // Remove Shadow annotation metadata as it breaks Paper's remapping
        exclude("META-INF/annotations.shadow.kotlin_module")
    }

    compileJava {
        options.encoding = "UTF-8"

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
