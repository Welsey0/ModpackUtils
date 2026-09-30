plugins {
    id("dev.architectury.loom-no-remap") version "1.17.477"
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
    maven("https://maven.terraformersmc.com/")
    mavenCentral()
}

loom {
    runConfigs.all {
        // Silenced compiler warnings by checking availability dynamically
        this::class.members.find { it.name == "ideConfigGenerated" }?.let {
            try { it.call(this, true) } catch (e: Exception) {}
        }
    }
}

dependencies {
    val mcVersion = project.property("deps.minecraft_version") as String
    minecraft("com.mojang:minecraft:$mcVersion")
    
    if (!mcVersion.startsWith("26")) {
        // Reflection fallback ensures 1.2x mappings stay here but won't crash 26.x compilation
        try {
            val depsHandler = project.dependencies
            val loomExt = project.extensions.getByName("loom")
            val layeredMethod = loomExt.javaClass.getMethod("layered", org.gradle.api.Action::class.java)
            val officialMethod = loomExt.javaClass.getMethod("officialMojangMappings")
            
            val layeredAction = org.gradle.api.Action<Any> { officialMethod.invoke(loomExt) }
            val layeredResult = layeredMethod.invoke(loomExt, layeredAction)
            
            val mappingsMethod = depsHandler.javaClass.methods.find { it.name == "mappings" && it.parameterCount == 1 }
            mappingsMethod?.invoke(depsHandler, layeredResult)
        } catch (ignored: Exception) {}
    }

    if (isNeo) {
        "neoForge"("net.neoforged:neoforge:${project.property("deps.neoforge")}")
    }

    // Determine configuration type safely based on current active loom execution target
    val modConfig = if (mcVersion.startsWith("26")) "implementation" else "modImplementation"

    // Core library dependency
    modConfig("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")
    
    modConfig("dev.isxander:main-menu-credits:1.2.0") 
    
    // Explicitly mount loader environment and APIs into the active compiler classpaths
    if (isFabric) {
        if (project.hasProperty("deps.fabric_loader") && (project.property("deps.fabric_loader") as String).isNotEmpty()) {
            modConfig("net.fabricmc:fabric-loader:${project.property("deps.fabric_loader")}")
        }
        if (project.hasProperty("deps.fabric_api_version") && (project.property("deps.fabric_api_version") as String).isNotEmpty()) {
            modConfig("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fabric_api_version")}")
        }
        if (project.hasProperty("deps.modmenu_version") && (project.property("deps.modmenu_version") as String).isNotEmpty()) {
            modConfig("com.terraformersmc:modmenu:${project.property("deps.modmenu_version")}")
        }
    }
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
    
    val mcVersion = project.property("deps.minecraft_version") as String
    val targetJavaVersion = if (mcVersion.startsWith("26")) JavaVersion.VERSION_25 else JavaVersion.VERSION_21
    sourceCompatibility = targetJavaVersion
    targetCompatibility = targetJavaVersion
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

publishMods {
    val mcVersion = project.property("deps.minecraft_version") as String
    val modVersion = project.property("mod_version") as String
    type = when {
        modVersion.contains("alpha") -> ALPHA
        modVersion.contains("beta") || modVersion.contains("rc") || isSnapshot -> BETA
        else -> STABLE
    }

    changelog.set("# ${project.version}\n${rootProject.file("CHANGELOG.md").readText()}")
    
    if (!mcVersion.startsWith("26")) {
        // Safe named dynamic lookup for remapJar task avoids type validation crashes
        file.set(tasks.named("remapJar").map { (it as org.gradle.api.tasks.bundling.AbstractArchiveTask).archiveFile }.get())
    } else {
        file.set(tasks.jar.get().archiveFile)
    }
    
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
        projectSlug.set("mutilsc")
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
        "26.3" -> {
            modrinth("m26.3") {
                from(mrOptions)
                minecraftVersions.add("26.3")
            }
            curseforge("c26.3") {
                from(cfOptions)
                minecraftVersions.add("26.3")
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
        register("mutilsc") {
            root.set(rootProject.file("docs"))
        }
    }
}