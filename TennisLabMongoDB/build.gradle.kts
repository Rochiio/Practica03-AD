import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val mockkVersion: String = "1.13.2"

plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Mongo Kotlin Asincrono
    implementation("org.litote.kmongo:kmongo-async:4.7.2")
    // Corrutinas Mongo
    implementation("org.litote.kmongo:kmongo-coroutine:4.7.2")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Logger
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.4")

    // Serializacion JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //MOCKK
    testImplementation("io.mockk:mockk:${mockkVersion}")

    //Terminal
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}