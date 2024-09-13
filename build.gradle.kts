plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "2.0.20" apply true
	id("com.stehno.natives") version "0.3.1"
    id("io.freefair.lombok") version "8.10" apply true
}

group = "Valkyr"
version = "1.0"

repositories {
    mavenCentral()
}

subprojects {

	apply(plugin = "java")
	apply(plugin = "com.stehno.natives")
	apply(plugin = "io.freefair.lombok")
	apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
		implementation("com.google.code.gson:gson:2.8.5")
		implementation("com.googlecode.json-simple:json-simple:1.1.1")
		implementation("commons-io:commons-io:2.6")
		implementation("org.apache.commons:commons-lang3:3.10")
		implementation("com.google.guava:guava:27.0.1-jre")
		implementation("org.apache.ant:ant:1.9.4")
		implementation("com.github.jponge:lzma-java:1.2")
		implementation("org.lwjgl.lwjgl:lwjgl:2.9.3")
		implementation("org.lwjgl.lwjgl:lwjgl_util:2.9.3")
		implementation("org.slf4j:slf4j-api:1.7.30")
		implementation("org.slf4j:slf4j-log4j12:1.7.30")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.20")
		implementation("org.reflections:reflections:0.9.11")
		implementation("org.reflections:reflections:0.9.11")
		implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    }

}