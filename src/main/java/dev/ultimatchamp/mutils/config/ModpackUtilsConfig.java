package dev.ultimatchamp.mutils.config;

import com.google.common.collect.Lists;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else neoforge {
//import net.neoforged.fml.loading.FMLPaths;
//?} else {
//import net.minecraftforge.fml.loading.FMLPaths;
//?}

public class ModpackUtilsConfig {
    public static Path CONFIG_DIR =
            //? if fabric {
            FabricLoader.getInstance().getConfigDir();
            //?} else {
            //FMLPaths.CONFIGDIR.get();
            //?}

    private static final ConfigClassHandler<ModpackUtilsConfig> GSON = ConfigClassHandler.createBuilder(ModpackUtilsConfig.class)
            .id(Identifier.fromNamespaceAndPath("mutils", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_DIR.resolve("mutils/mutils.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Update Notifier")
    public boolean menuAlert = false;

    @SerialEntry
    public boolean chatAlert = false;

    @SerialEntry
    public String chatMessage = "There is an update available for your modpack!";

    @SerialEntry
    public Platforms platform = Platforms.MODRINTH;

    public enum Platforms implements NameableEnum {
        CUSTOM("generator.custom"),
        MODRINTH("mutils.modrinth"),
        CURSEFORGE("mutils.curseforge");

        private final String displayName;

        Platforms(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(displayName);
        }
    }

    @SerialEntry
    public String modpackName = "My Modpack";

    @SerialEntry
    public String modpackId = "my-modpack";

    @SerialEntry
    public String modpackHome = "https://github.com/me/My-Modpack";

    @SerialEntry
    public String localVersion = "1.0.0";

    @SerialEntry
    public Boolean checkMcVersion = false;

    @SerialEntry
    public String loader =
            //? if fabric {
            "fabric";
            //?} else if neoforge {
            //"neoforge";
            //?}

    @SerialEntry
    public String versionAPI = "https://raw.githubusercontent.com/me/My-Modpack/main/Packwiz/pack.toml";

    @SerialEntry
    public String changelogLink = "https://raw.githubusercontent.com/me/My-Modpack/main/CHANGELOG.md";

    @SerialEntry
    public List<String> versionType = List.of("release", "beta", "alpha");

    @SerialEntry
    public List<String> nameFilters = Lists.newArrayList();

    @SerialEntry
    public List<String> versionFilters = Lists.newArrayList();

    @SerialEntry(comment = "Chat Welcome")
    public Boolean chatWelcome = false;

    @SerialEntry
    public String chatWelcomeMessage = "Welcome to <modpack-name> <version>!";

    @SerialEntry(comment = "Window Utils")
    public boolean customIcon = false;

    @SerialEntry
    public boolean customTitle = false;

    @SerialEntry
    public String title = "<modpack-name> <version> | Minecraft <mc>";

    //? if fabric {
    @SerialEntry(comment = "MMC")
    public MmcStyle mainMenuCreditsIntegeration = MmcStyle.OFF;

    public enum MmcStyle implements NameableEnum {
        OFF("options.off"),
        NORMAL("options.difficulty.normal"),
        FANCY("options.graphics.fancy"),
        CUSTOM("generator.custom");

        private final String displayName;

        MmcStyle(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(displayName);
        }
    }

    @SerialEntry
    public List<String> customMmc = List.of(
            "<version>",
            "#FF00FF",
            "<version-link>",
            "<modpack-name>",
            "#55FF55",
            "<modpack-home>"
    );
    //?}

    @SerialEntry(comment = "Ram Alert")
    public boolean ramMenuAlert = false;

    @SerialEntry
    public boolean ramChatAlert = false;

    @SerialEntry
    public Integer minRam = 8192;

    public static ConfigClassHandler<ModpackUtilsConfig> handler() {
        return GSON;
    }

    public static void load() {
        GSON.load();
    }

    public static ModpackUtilsConfig instance() {
        return GSON.instance();
    }

    public static Screen createScreen(@Nullable Screen parent) {
        return ModpackUtilsGui.createConfigScreen(parent);
    }
}
