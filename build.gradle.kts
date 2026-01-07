plugins {
    id("dev.architectury.loom") version "1.12"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("org.moddedmc.wiki.toolkit") version "0.4.1"
}

var loader = project.property("loom.platform")

var isFabric = loader == "fabric"
var isNeo = loader == "neoforge"

var isSnapshot = false
var mcVer: String = project.property("deps.minecraft_version") as String
if (mcVer.contains("-") || mcVer.contains("w")) {
    isSnapshot = true
    mcVer = mcVer.replace("-", "")
}

version = "${project.property("mod_version")}+$loader.$mcVer"
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("mod_name") as String)
}

repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    mavenCentral()
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("deps.minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${project.property("deps.minecraft_version")}:${project.property("deps.parchment_version")}@zip")
    })

    if (isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
        modImplementation("maven.modrinth:modmenu:${project.property("deps.modmenu_version")}")
        modImplementation("maven.modrinth:main-menu-credits:${project.property("deps.mmc_version")}")
    } else if (isNeo) {
        "neoForge"("net.neoforged:neoforge:${project.property("deps.neoforge")}")
    }

    modImplementation("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")
}

stonecutter {
    const("fabric", isFabric)
    const("neoforge", isNeo)
}

tasks.processResources {
    val replaceProperties = mapOf(
        "minecraft_range" to project.property("deps.mc_range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.version,
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )
    replaceProperties.forEach { (key, value) -> inputs.property(key, value) }

    if (isFabric) {
        filesMatching("fabric.mod.json") {
            expand(replaceProperties)
        }

        exclude("META-INF/neoforge.mods.toml")
    } else if (isNeo) {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(replaceProperties)
        }

        exclude("fabric.mod.json")
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

publishMods {
    val modVersion = project.property("mod_version") as String
    type = when {
        modVersion.contains("alpha") -> ALPHA
        modVersion.contains("beta") || modVersion.contains("rc") || isSnapshot -> BETA
        else -> STABLE
    }

    changelog.set("# ${project.version}\n${rootProject.file("CHANGELOG.md").readText()}")
    file.set(tasks.remapJar.get().archiveFile)
    displayName.set("ModpackUtils ${project.version}")

    if (isFabric) {
        modLoaders.addAll("fabric", "quilt")
    } else if (isNeo) {
        modLoaders.add("neoforge")
    }

    val mrOptions = modrinthOptions {
        projectId.set(project.property("modrinthId") as String)
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))

        requires("yacl")

        // Discord
        announcementTitle.set("Download from Modrinth")
    }

    val cfOptions = curseforgeOptions {
        projectId.set(project.property("curseforgeId") as String)
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))

        requires("yacl")

        // Discord
        announcementTitle.set("Download from CurseForge")
        projectSlug.set("mutils")
    }

    when (project.property("deps.minecraft_version") as String) {
        "1.21.1" -> {
            modrinth("m1.21.1") {
                from(mrOptions)
                minecraftVersionRange {
                    start = "1.21"
                    end = "1.21.1"
                }
            }
            curseforge("c1.21.1") {
                from(cfOptions)
                minecraftVersionRange {
                    start = "1.21"
                    end = "1.21.1"
                }
            }
        }
        "1.21.4" -> {
            modrinth("m1.21.4") {
                from(mrOptions)
                minecraftVersionRange {
                    start = if (isFabric) "1.21" else "1.21.2"
                    end = "1.21.4"
                }
            }
            curseforge("c1.21.4") {
                from(cfOptions)
                minecraftVersionRange {
                    start = "1.21"
                    end = "1.21.4"
                }
            }
        }
        "1.21.5" -> {
            modrinth("m1.21.5") {
                from(mrOptions)
                minecraftVersions.add("1.21.5")
            }
            curseforge("c1.21.5") {
                from(cfOptions)
                minecraftVersions.add("1.21.5")
            }
        }
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("UltimatChamp/${project.property("mod_name")}")
        commitish.set("main")

        // Discord
        announcementTitle.set("Download from GitHub")
    }

    discord {
        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
        username.set("${project.property("mod_name")} Releases")
        avatarUrl.set("https://cdn.modrinth.com/data/wklFEiuR/images/690d8f555972de3b24cd7ee82c083ebb6a3e2155.png")

        style {
            look.set("MODERN")
            thumbnailUrl.set("https://cdn.modrinth.com/data/wklFEiuR/images/eaaf432d67e179d959d3168664b036066569c56d.png")
        }
    }
}

wiki {
    docs {
        register("mutils") {
            root.set(rootProject.file("docs"))
        }
    }
}
