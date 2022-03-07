plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.iridium"
version = "3.2.6"
description = "IridiumSkyblock"

repositories {
    mavenCentral()
    maven("https://repo.mvdw-software.com/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://jitpack.io")
    maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://hub.jeff-media.com/nexus/repository/jeff-media-public/")
}

dependencies {
    // Dependencies that we want to shade in
    implementation("org.jetbrains:annotations:22.0.0")
    implementation("com.iridium:IridiumCore:1.5.3")
    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("com.github.Redempt:Crunch:1.0.0")
    implementation("com.j256.ormlite:ormlite-core:6.1")
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")
    implementation("de.jeff_media:SpigotUpdateChecker:1.3.0")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly("net.ess3:EssentialsXSpawn:2.16.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.9.2")
    compileOnly("be.maximvdw:MVdWPlaceholderAPI:2.1.1-SNAPSHOT") {
        exclude("org.spigotmc")
    }
    compileOnly("com.gc:AdvancedSpawners:1.2.6")
    compileOnly("dev.rosewood:rosestacker:1.4.2")
    compileOnly("com.github.OmerBenGera:WildStackerAPI:master")
    compileOnly("com.songoda:UltimateStacker:2.1.7")
    compileOnly("com.songoda:EpicSpawners:7.0.8")
    compileOnly("com.sk89q:WorldEdit:7.2.6")

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.mockito:mockito-inline:4.3.1")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:1.15.0")
}

tasks {
    // "Replace" the build task with the shadowJar task (probably bad but who cares)
    jar {
        dependsOn("shadowJar")
        enabled = false
    }

    shadowJar {
        fun relocate(origin: String) = relocate(origin, "com.iridium.iridiumskyblock.dependencies${origin.substring(origin.lastIndexOf('.'))}")

        // Remove the archive classifier suffix
        archiveClassifier.set("")

        // Relocate dependencies
        relocate("com.j256.ormlite")
        relocate("org.bstats")
        relocate("de.jeff_media.updatechecker")

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

    test {
        useJUnitPlatform()
    }

    compileJava {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    compileTestJava {
        sourceCompatibility = JavaVersion.VERSION_16.toString()
        targetCompatibility = JavaVersion.VERSION_16.toString()
    }
}

// Set the Java version and vendor
java {
    toolchain {
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}

// Maven publishing
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
