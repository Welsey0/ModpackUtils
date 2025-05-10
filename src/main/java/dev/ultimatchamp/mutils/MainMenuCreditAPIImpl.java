//? if fabric {
package dev.ultimatchamp.mutils;

import com.google.common.collect.Lists;
import dev.isxander.mainmenucredits.api.MainMenuCreditAPI;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

/*? if >1.21.4 {*/import java.net.URI;/*?}*/
import java.util.List;

public class MainMenuCreditAPIImpl implements MainMenuCreditAPI {
    @Override
    public List<Component> getTitleScreenBottomRight() {
        ModpackUtilsConfig.load(); // Workaround

        List<Component> list = Lists.newArrayList();

        switch (ModpackUtilsConfig.instance().mainMenuCreditsIntegeration) {
            case NORMAL ->
                    list.add(Component.literal(ModpackUtilsConfig.instance().modpackName + " " + ModpackUtilsConfig.instance().localVersion)
                            .withStyle(arg ->
                                    arg.withClickEvent(
                                            /*? if >1.21.4 {*/new ClickEvent.OpenUrl(/*?} else {*//*new ClickEvent(ClickEvent.Action.OPEN_URL,*//*?}*/
                                                    ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                            /*? if >1.21.4 {*/URI.create/*?}*/(ModpackUtilsConfig.instance().modpackHome) :
                                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                                    /*? if >1.21.4 {*/URI.create/*?}*/("https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion) :
                                                                    /*? if >1.21.4 {*/URI.create/*?}*/("https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLocalFileId())
                                            )
                                    )
                            )
                    );
            case FANCY -> {
                list.add(Component.literal(ModpackUtilsConfig.instance().localVersion).withStyle(arg -> arg
                                .withColor(TextColor.parseColor("#55FF55").getOrThrow())
                                .withClickEvent(
                                        /*? if >1.21.4 {*/new ClickEvent.OpenUrl(/*?} else {*//*new ClickEvent(ClickEvent.Action.OPEN_URL,*//*?}*/
                                                ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                        /*? if >1.21.4 {*/URI.create/*?}*/(ModpackUtilsConfig.instance().changelogLink) :
                                                        ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                                /*? if >1.21.4 {*/URI.create/*?}*/("https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion) :
                                                                /*? if >1.21.4 {*/URI.create/*?}*/("https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLocalFileId())
                                        )
                                )
                        )
                );

                list.add(Component.literal(ModpackUtilsConfig.instance().modpackName).withStyle(arg -> arg
                                .withColor(TextColor.parseColor("#FF00FF").getOrThrow())
                                .withClickEvent(
                                        /*? if >1.21.4 {*/new ClickEvent.OpenUrl(/*?} else {*//*new ClickEvent(ClickEvent.Action.OPEN_URL,*//*?}*/
                                                ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                        /*? if >1.21.4 {*/URI.create/*?}*/(ModpackUtilsConfig.instance().modpackHome) :
                                                        ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                                /*? if >1.21.4 {*/URI.create/*?}*/("https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId) :
                                                                /*? if >1.21.4 {*/URI.create/*?}*/("https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId)
                                        )
                                )
                        )
                );
            }
            case CUSTOM -> {
                if (ModpackUtilsConfig.instance().customMmc.size() >= 3 && ModpackUtilsConfig.instance().customMmc.size() % 3 == 0) {
                    for (int i = 0; i < ModpackUtilsConfig.instance().customMmc.size(); i += 3) {
                        String text = ModpackUtilsConfig.instance().customMmc.get(i)
                                .replaceAll("<modpack-name>", ModpackUtilsConfig.instance().modpackName)
                                .replaceAll("<version>", ModpackUtilsConfig.instance().localVersion);

                        String color = ModpackUtilsConfig.instance().customMmc.get(i + 1);

                        String link = ModpackUtilsConfig.instance().customMmc.get(i + 2)
                                .replaceAll("<modpack-home>",
                                        ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                ModpackUtilsConfig.instance().modpackHome :
                                                ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                        "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId :
                                                        "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId)
                                .replaceAll("<version-link>",
                                        ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                ModpackUtilsConfig.instance().changelogLink :
                                                ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                        "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion :
                                                        "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLocalFileId());
                        list.add(Component.literal(text).withStyle(arg -> arg
                                .withColor(TextColor.parseColor(color).getOrThrow())
                                .withClickEvent(/*? if >1.21.4 {*/new ClickEvent.OpenUrl(/*?} else {*//*new ClickEvent(ClickEvent.Action.OPEN_URL,*//*?}*//*? if >1.21.4 {*/URI.create/*?}*/(link)))
                        ));
                    }
                } else {
                    ModpackUtils.LOGGER.error("[ModpackUtils] Incorrect syntax for custom MainMenuCredits integration!");
                }
            }
        }

        return list;
    }
}
//?}
