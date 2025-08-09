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
        vers("1.21.5-fabric", "1.21.5")
        vers("1.21.5-neo", "1.21.5")

        vers("1.21.4-fabric", "1.21.4")
        vers("1.21.4-neo", "1.21.4")

        vers("1.21.1-neo", "1.21.1") // :pain:

        vcsVersion = "1.21.5-fabric"

        vers("1.21.8-fabric", "1.21.8"),
        vers("1.21.8-neo", "1.21.8")
    }
}

rootProject.name = "ModpackUtils"
