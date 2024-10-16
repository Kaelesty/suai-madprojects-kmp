plugins {
	id("java-library")
	alias(libs.plugins.jetbrains.kotlin.jvm)
	kotlin("plugin.serialization") version "2.0.20"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
	implementation(libs.koin.core)
	implementation(libs.koin.compose)
	implementation(libs.kotlinx.serialization.json)
	implementation(project(":shared_domain"))
}