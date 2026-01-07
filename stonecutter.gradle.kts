import dev.kikugie.stonecutter.ide.RunConfigType

plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.11-fabric"

stonecutter.generateRunConfigs = listOf(RunConfigType.CHISEL, RunConfigType.SWITCH)

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledReleaseMod", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}
