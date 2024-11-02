package dev.ultimatchamp.mutils.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModpackUtilsGui {
    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(ModpackUtilsConfig.handler(), (defaults, config, builder) -> builder
                .title(Text.translatable("mutils.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("stat.generalButton"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("mutils.menuAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("mutils.menuAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.menuAlert,
                                        () -> config.menuAlert,
                                        (value) -> config.menuAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("mutils.chatAlert"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("mutils.chatAlert.desc"))
                                        .build())
                                .binding(
                                        defaults.chatAlert,
                                        () -> config.chatAlert,
                                        (value) -> config.chatAlert = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("mutils.chatMessage"))
                                .binding(
                                        defaults.chatMessage,
                                        () -> config.chatMessage,
                                        (value) -> config.chatMessage = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<ModpackUtilsConfig.Platforms>createBuilder()
                                .name(Text.translatable("mutils.platform"))
                                .binding(
                                        defaults.platform,
                                        () -> config.platform,
                                        (value) -> config.platform = value
                                )
                                .customController(opt -> new EnumController<>(opt, ModpackUtilsConfig.Platforms.class))
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("mutils.modpackId"))
                                .binding(
                                        defaults.modpackId,
                                        () -> config.modpackId,
                                        (value) -> config.modpackId = value
                                )
                                .available(ModpackUtilsConfig.instance().platform != ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("mutils.localVersion"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("mutils.localVersion.desc"))
                                        .build())
                                .binding(
                                        defaults.localVersion,
                                        () -> config.localVersion,
                                        (value) -> config.localVersion = value
                                )
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("mutils.versionAPI"))
                                .binding(
                                        defaults.versionAPI,
                                        () -> config.versionAPI,
                                        (value) -> config.versionAPI = value
                                )
                                .available(ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatable("mutils.changelogLink"))
                                .binding(
                                        defaults.changelogLink,
                                        () -> config.changelogLink,
                                        (value) -> config.changelogLink = value
                                )
                                .available(ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())
                .save(ModpackUtilsConfig.handler()::save)
        ).generateScreen(parent);
    }
}
