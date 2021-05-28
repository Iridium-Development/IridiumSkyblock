dependencies {
    // Dependencies that we want to shade in
    implementation("de.tr7zw:item-nbt-api:2.7.1")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("com.iridium:IridiumColorAPI:1.0.4")
    implementation("org.jetbrains:annotations:16.0.1")
    implementation("com.github.j256:ormlite-core:master-SNAPSHOT")
    implementation("com.j256.ormlite:ormlite-jdbc:5.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.1")
    implementation("org.yaml:snakeyaml:1.27")
    implementation("io.papermc:paperlib:1.0.6")
    implementation("de.jeff_media:SpigotUpdateChecker:1.2.0")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.ess3:EssentialsXSpawn:2.16.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.9.2")
    compileOnly("be.maximvdw:MVdWPlaceholderAPI:2.1.1-SNAPSHOT") {
        exclude("org.spigotmc")
    }

    // Include all the nms sub-modules
    val multiVersionProjects = project(":multiversion").dependencyProject.subprojects
    multiVersionProjects.forEach { compileOnly(it) }
}

tasks {
    build {
        dependsOn(processResources)
    }

    processResources {
        // Placeholders for the plugin.yml
        filesMatching("**/plugin.yml") {
            expand(rootProject.project.properties)
        }

        // Always re-run this task
        outputs.upToDateWhen { false }
    }
}
