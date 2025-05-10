package dev.ultimatchamp.mutils.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModpackUtilsGui {
    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(ModpackUtilsConfig.handler(), (defaults, config, builder) -> builder
                .title(Component.translatable("mutils.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("mutils.category.update"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.menuAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.menuAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.menuAlert,
                                        () -> config.menuAlert,
                                        (value) -> config.menuAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.chatAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.chatAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.chatAlert,
                                        () -> config.chatAlert,
                                        (value) -> config.chatAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("gui.abuseReport.type.chat"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.chatMessage.desc"))
                                        .build())
                                .binding(
                                        defaults.chatMessage,
                                        () -> config.chatMessage,
                                        (value) -> config.chatMessage = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<ModpackUtilsConfig.Platforms>createBuilder()
                                .name(Component.translatable("telemetry.property.platform.title"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.platform.desc"))
                                        .build())
                                .binding(
                                        defaults.platform,
                                        () -> config.platform,
                                        (value) -> config.platform = value
                                )
                                .customController(opt -> new EnumController<>(opt, ModpackUtilsConfig.Platforms.class))
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.modpackName"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.modpackName.desc"))
                                        .build())
                                .binding(
                                        defaults.modpackName,
                                        () -> config.modpackName,
                                        (value) -> config.modpackName = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.modpackId"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.modpackId.desc"))
                                        .build())
                                .binding(
                                        defaults.modpackId,
                                        () -> config.modpackId,
                                        (value) -> config.modpackId = value
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.modpackHome"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.modpackHome.desc"))
                                        .build())
                                .binding(
                                        defaults.modpackHome,
                                        () -> config.modpackHome,
                                        (value) -> config.modpackHome = value
                                )
                                .available(ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.localVersion"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.localVersion.desc"))
                                        .build())
                                .binding(
                                        defaults.localVersion,
                                        () -> config.localVersion,
                                        (value) -> config.localVersion = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.checkMcVersion"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.checkMcVersion.desc"))
                                        .build())
                                .binding(
                                        defaults.checkMcVersion,
                                        () -> config.checkMcVersion,
                                        (value) -> config.checkMcVersion = value
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.loader"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.loader.desc"))
                                        .build())
                                .binding(
                                        defaults.loader,
                                        () -> config.loader,
                                        (value) -> config.loader = value
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.versionAPI"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.versionAPI.desc"))
                                        .build())
                                .binding(
                                        defaults.versionAPI,
                                        () -> config.versionAPI,
                                        (value) -> config.versionAPI = value
                                )
                                .available(ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mutils.changelogLink"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.changelogLink.desc"))
                                        .build())
                                .binding(
                                        defaults.changelogLink,
                                        () -> config.changelogLink,
                                        (value) -> config.changelogLink = value
                                )
                                .available(ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Component.translatable("mutils.versionType"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.versionType.desc"))
                                        .build())
                                .binding(
                                        defaults.versionType,
                                        () -> config.versionType,
                                        val -> config.versionType = val
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .minimumNumberOfEntries(1)
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Component.translatable("mutils.nameFilters"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.nameFilters.desc"))
                                        .build())
                                .binding(
                                        defaults.nameFilters,
                                        () -> config.nameFilters,
                                        val -> config.nameFilters = val
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Component.translatable("mutils.versionFilters"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.versionFilters.desc"))
                                        .build())
                                .binding(
                                        defaults.versionFilters,
                                        () -> config.versionFilters,
                                        val -> config.versionFilters = val
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("mutils.category.chatWelcome"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.category.chatWelcome"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.chatAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.chatWelcome,
                                        () -> config.chatWelcome,
                                        (value) -> config.chatWelcome = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("gui.abuseReport.type.chat"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.chatWelcomeMessage.desc"))
                                        .build())
                                .binding(
                                        defaults.chatWelcomeMessage,
                                        () -> config.chatWelcomeMessage,
                                        (value) -> config.chatWelcomeMessage = value
                                )
                                .available(ModpackUtilsConfig.instance().chatWelcome)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("mutils.category.window"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.customIcon"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.customIcon.desc"))
                                        .build())
                                .binding(
                                        defaults.customIcon,
                                        () -> config.customIcon,
                                        (value) -> config.customIcon = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.customTitle"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.customTitle.desc"))
                                        .build())
                                .binding(
                                        defaults.customTitle,
                                        () -> config.customTitle,
                                        (value) -> config.customTitle = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("mco.backup.entry.name"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.cTitle.desc"))
                                        .build())
                                .binding(
                                        defaults.title,
                                        () -> config.title,
                                        (value) -> config.title = value
                                )
                                .available(ModpackUtilsConfig.instance().customTitle)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())
                //? if fabric {
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("mutils.category.mmc"))
                        .option(Option.<ModpackUtilsConfig.MmcStyle>createBuilder()
                                .name(Component.translatable("mutils.mmcIntg"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.mmcIntg.desc"))
                                        .build())
                                .binding(
                                        defaults.mainMenuCreditsIntegeration,
                                        () -> config.mainMenuCreditsIntegeration,
                                        (value) -> config.mainMenuCreditsIntegeration = value
                                )
                                .customController(opt -> new EnumController<>(opt, ModpackUtilsConfig.MmcStyle.class))
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.customMmc.desc"))
                                        .build())
                                .binding(
                                        defaults.customMmc,
                                        () -> config.customMmc,
                                        val -> config.customMmc = val
                                )
                                .available(ModpackUtilsConfig.instance().mainMenuCreditsIntegeration == ModpackUtilsConfig.MmcStyle.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .minimumNumberOfEntries(3)
                                .build())
                        .build())
                //?}
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("mutils.category.ramAlert"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.ramMenuAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.ramMenuAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.ramMenuAlert,
                                        () -> config.ramMenuAlert,
                                        (value) -> config.ramMenuAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("mutils.ramChatAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Component.translatable("mutils.ramChatAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.ramChatAlert,
                                        () -> config.ramChatAlert,
                                        (value) -> config.ramChatAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("mutils.minRam"))
                                .binding(
                                        defaults.minRam,
                                        () -> config.minRam,
                                        (value) -> config.minRam = value
                                )
                                .controller(IntegerFieldControllerBuilder::create)
                                .build())
                        .build())
                .save(ModpackUtilsConfig.handler()::save)
        ).generateScreen(parent);
    }
}
