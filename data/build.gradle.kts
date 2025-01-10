plugins {
	id("java-library")
	alias(libs.plugins.jetbrains.kotlin.jvm)
	alias(libs.plugins.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.ktorfit)
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
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.ktorfit)
	implementation(libs.koin.core)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.ktor.serialization.kotlinx.json)

	implementation(libs.exposed.core)
	implementation(libs.exposed.crypt)
	implementation(libs.exposed.dao)
	implementation(libs.exposed.jdbc)
	//implementation(libs.sqlite.jdbc)
}