plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.iridium"
version = "3.0.0"
description = "IridiumSkyblock"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.mvdw-software.be/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.sk89q.com/artifactory/repo")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://nexus.savagelabs.net/repository/maven-releases/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    // Dependencies that we want to shade in
    implementation("de.tr7zw:item-nbt-api:2.7.1")
    implementation("com.iridium:IridiumColorAPI:1.0.4")
    implementation("com.github.cryptomorin:XSeries:7.8.0")
    implementation("org.jetbrains:annotations:16.0.1")
    implementation("com.j256.ormlite:ormlite-core:5.3")
    implementation("com.j256.ormlite:ormlite-jdbc:5.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.1")
    implementation("org.yaml:snakeyaml:1.27")
    implementation("io.papermc:paperlib:1.0.6")

    // Other dependencies that are not required or already available at runtime
    shadow("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    shadow("org.spigotmc:spigot:1.16.4")
    shadow("org.projectlombok:lombok:1.18.16")

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.16")
}

tasks {
    jar {
        dependsOn("shadowJar")
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("de.tr7zw.changeme.nbtapi", "com.iridium.iridiumskyblock.nbtapi")
        relocate("com.iridium.iridiumcolorapi", "com.iridium.iridiumskyblock")
        relocate("org.yaml.snakeyaml", "com.iridium.iridiumskyblock.snakeyaml")
        relocate("io.papermc.lib", "com.iridium.iridiumskyblock.paperlib")
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
