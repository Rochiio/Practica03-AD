import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val mockkVersion: String = "1.13.2"
// Koin
val koin_ktor_version: String by project
val ksp_version: String by project
val koin_ksp_version: String by project
val koin_version: String by project
plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.20"
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
// Use KSP Generated sources
sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}
dependencies {
    //KOIN
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    ksp ("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
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
    implementation("junit:junit:4.13.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")

    //SHA-512
    implementation("com.google.guava:guava:31.1-jre")

    //Cache
    implementation("io.github.reactivecircus.cache4k:cache4k:0.9.0")

    //MOCKK
    testImplementation("io.mockk:mockk:${mockkVersion}")

    //SHA-512
    implementation("com.google.guava:guava:31.1-jre")
    //Terminal
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")

    //ktorfit
    ksp("de.jensklingenberg.ktorfit:ktorfit-ksp:1.0.0-beta16")
    implementation("de.jensklingenberg.ktorfit:ktorfit-lib:1.0.0-beta16")

    // Para serializar en Json con Ktor
    implementation("io.ktor:ktor-client-serialization:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")


    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation ("io.insert-koin:koin-test:$koin_version")

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