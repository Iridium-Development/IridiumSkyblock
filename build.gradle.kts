plugins {
    java
    `maven-publish`
    id("me.bristermitten.pdm") version "0.0.33"
}

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
}

dependencies {
    // Dependencies that are loaded using PDM
    pdm("de.tr7zw:item-nbt-api:2.7.1")
    pdm("com.iridium:IridiumColorAPI:1.0.2")
    pdm("com.github.cryptomorin:XSeries:7.8.0")
    pdm("org.jetbrains:annotations:16.0.1")
    pdm("com.j256.ormlite:ormlite-core:5.3")
    pdm("com.j256.ormlite:ormlite-jdbc:5.3")
    pdm("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    pdm("com.fasterxml.jackson.core:jackson-core:2.12.1")
    pdm("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
    pdm("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.1")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.16.4")
    compileOnly("org.yaml:snakeyaml:1.27")
    compileOnly("org.projectlombok:lombok:1.18.16")

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.16")
}

group = "com.peaches"
version = "3.0.0"
description = "IridiumSkyblock"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.getByName("jar").dependsOn(project.tasks.getByName("pdm"))

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
