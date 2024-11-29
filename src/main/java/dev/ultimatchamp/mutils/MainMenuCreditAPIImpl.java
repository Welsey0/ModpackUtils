//? if fabric {
package dev.ultimatchamp.mutils;

import com.google.common.collect.Lists;
import dev.isxander.mainmenucredits.api.MainMenuCreditAPI;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.List;

public class MainMenuCreditAPIImpl implements MainMenuCreditAPI {
    @Override
    public List<Text> getTitleScreenBottomRight() {
        ModpackUtilsConfig.load(); // Workaround

        List<Text> list = Lists.newArrayList();

        if (ModpackUtilsConfig.instance().mainMenuCreditsIntegeration == ModpackUtilsConfig.MmcStyle.NORMAL) {
            list.add(Text.literal(ModpackUtilsConfig.instance().modpackName + " " + ModpackUtilsConfig.instance().localVersion)
                    .styled(arg ->
                            arg.withClickEvent(
                                    new ClickEvent(
                                            ClickEvent.Action.OPEN_URL,
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM
                                                    ? ModpackUtilsConfig.instance().modpackHome
                                                    : ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH
                                                    ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion
                                                    : "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLocalFileId()
                                    )
                            )
                    )
            );
        } else if (ModpackUtilsConfig.instance().mainMenuCreditsIntegeration == ModpackUtilsConfig.MmcStyle.FANCY) {
            list.add(Text.literal(ModpackUtilsConfig.instance().localVersion).styled(arg -> arg
                            //? if >1.20.1 {
                            .withColor(TextColor.parse("#55FF55").getOrThrow())
                            //?} else {
                            /*.withColor(TextColor.parse("#55FF55"))
                            *///?}
                            .withClickEvent(
                                    new ClickEvent(
                                            ClickEvent.Action.OPEN_URL,
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM
                                                    ? ModpackUtilsConfig.instance().changelogLink
                                                    : ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH
                                                    ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion
                                                    : "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLocalFileId()
                                    )
                            )
                    )
            );

            list.add(Text.literal(ModpackUtilsConfig.instance().modpackName).styled(arg -> arg
                            //? if >1.20.1 {
                            .withColor(TextColor.parse("#FF00FF").getOrThrow())
                            //?} else {
                            /*.withColor(TextColor.parse("#FF00FF"))
                            *///?}
                            .withClickEvent(
                                    new ClickEvent(
                                            ClickEvent.Action.OPEN_URL,
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM
                                                    ? ModpackUtilsConfig.instance().modpackHome
                                                    : ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH
                                                    ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId
                                                    : "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId
                                    )
                            )
                    )
            );
        } else if (ModpackUtilsConfig.instance().mainMenuCreditsIntegeration == ModpackUtilsConfig.MmcStyle.CUSTOM) {
            if (ModpackUtilsConfig.instance().customMmc.size() >= 3 && ModpackUtilsConfig.instance().customMmc.size() % 3 == 0) {
                for (int i = 0; i < ModpackUtilsConfig.instance().customMmc.size(); i += 3) {
                    String text = ModpackUtilsConfig.instance().customMmc.get(i)
                            .replaceAll("<modpack-name>", ModpackUtilsConfig.instance().modpackName)
                            .replaceAll("<version>", ModpackUtilsConfig.instance().localVersion);

                    String color = ModpackUtilsConfig.instance().customMmc.get(i + 1);

                    String link = ModpackUtilsConfig.instance().customMmc.get(i + 2)
                            .replaceAll("<modpack-home>",
                                    ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM
                                            ? ModpackUtilsConfig.instance().modpackHome
                                            : ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH
                                            ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId
                                            : "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId)
                            .replaceAll("<version-link>",
                                    ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM
                                            ? ModpackUtilsConfig.instance().changelogLink
                                            : ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH
                                            ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion
                                            : "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLocalFileId());
                    list.add(Text.literal(text).styled(arg -> arg
                            //? if >1.20.1 {
                            .withColor(TextColor.parse(color).getOrThrow())
                            //?} else {
                            /*.withColor(TextColor.parse(color))
                            *///?}
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
                    ));
                }
            } else {
                ModpackUtils.LOGGER.error("[ModpackUtils] Incorrect syntax for custom MainMenuCredits integration!");
            }
        }

        return list;
    }
}
//?}
