dependencies {
    // Dependencies that we want to shade in
    implementation("de.tr7zw:item-nbt-api:2.7.1")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("com.iridium:IridiumColorAPI:1.0.4")
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
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    // Include all the nms sub-modules
    val multiVersionProjects = project(":multiversion").dependencyProject.subprojects
    multiVersionProjects.forEach { compileOnly(it) }
}