plugins {
	id("java-library")
	alias(libs.plugins.jetbrains.kotlin.jvm)
	alias(libs.plugins.serialization)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
	implementation(project(":domain"))
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.ktor.client.websockets)
	implementation(libs.ktor.client.cio)
	implementation( libs.kotlinx.serialization.json)
	implementation(project(":shared_domain"))
}