group = "com.iridium"
version = "3.0.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // Dependencies that we want to shade in
    implementation("de.tr7zw:item-nbt-api:2.7.1")
    implementation("org.bstats:bstats-bukkit:2.2.1")
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
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly(project(":nms"))
    compileOnly(project(":nms:common"))
    compileOnly(project(":nms:v1_16_R3"))
}