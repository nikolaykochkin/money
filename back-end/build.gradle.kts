plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val telegrambotsVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")

    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-rest-client-reactive-jackson")

    implementation("io.quarkus:quarkus-hibernate-reactive-rest-data-panache")
    implementation("io.quarkus:quarkus-hibernate-reactive-panache")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-reactive-pg-client")

    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")

    implementation("org.telegram:telegrambots:${telegrambotsVersion}")
    implementation("org.telegram:telegrambots-abilities:${telegrambotsVersion}")

// TODO: 29.7.23. Migrations
//    implementation("io.quarkus:quarkus-flyway")
//    implementation("io.quarkus:quarkus-jdbc-postgresql")
//    implementation("io.quarkus:quarkus-scheduler"

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "name.nikolaikochkin"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
