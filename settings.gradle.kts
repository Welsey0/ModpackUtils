pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6"
}

stonecutter {
    create(rootProject) {
        //vers("26.2-neo", "26.2")
        vers("26.2-fabric", "26.2")

        vcsVersion = "26.2-fabric"
    }
}

rootProject.name = "ModpackUtils Continued"
