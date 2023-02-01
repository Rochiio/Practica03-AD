import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val mockkVersion: String = "1.13.2"

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("plugin.serialization") version "1.7.21"
	id("com.google.devtools.ksp") version "1.7.21-1.0.8"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
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

	//Terminal
	implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")


	//ktorfit
	ksp("de.jensklingenberg.ktorfit:ktorfit-ksp:1.0.0-beta16")
	implementation("de.jensklingenberg.ktorfit:ktorfit-lib:1.0.0-beta16")

	// Para serializar en Json con Ktor
	implementation("io.ktor:ktor-client-serialization:2.1.3")
	implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
	implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")

	//MOCKK
	testImplementation("io.mockk:mockk:${mockkVersion}")
	testImplementation(kotlin("test"))
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
