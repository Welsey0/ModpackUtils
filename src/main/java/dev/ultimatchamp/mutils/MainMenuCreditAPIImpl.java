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
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                    "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion :
                                                    ModpackUtilsConfig.instance().modpackHome
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
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                    "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion :
                                                    ModpackUtilsConfig.instance().changelogLink
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
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                    "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId :
                                                    ModpackUtilsConfig.instance().modpackHome
                                    )
                            )
                    )
            );
        }

        return list;
    }
}
//?}
