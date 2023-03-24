plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.kinsnopia"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
    dependencies {
        api("io.github.willena:sqlite-jdbc:3.40.0.0")
    }
}

application {
    mainClass.set("DBDecrypt")
}