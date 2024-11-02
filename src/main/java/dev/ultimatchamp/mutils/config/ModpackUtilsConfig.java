package dev.ultimatchamp.mutils.config;

import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else neo {
/*import net.neoforged.fml.loading.FMLPaths;
*///?} else {
/*import net.minecraftforge.fml.loading.FMLPaths;
*///?}

public class ModpackUtilsConfig {
    private static final ConfigClassHandler<ModpackUtilsConfig> GSON = ConfigClassHandler.createBuilder(ModpackUtilsConfig.class)
            .id(Identifier.of("mutils", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(
                            //? if fabric {
                            FabricLoader.getInstance().getConfigDir().resolve("mutils.json5")
                            //?} else {
                            /*FMLPaths.CONFIGDIR.get().resolve("mutils.json5")
                            *///?}
                    )
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean menuAlert = true;

    @SerialEntry
    public boolean chatAlert = true;

    @SerialEntry
    public String chatMessage = "There is an update available for your modpack!";

    @SerialEntry
    public Platforms platform = Platforms.MODRINTH;

    public enum Platforms implements NameableEnum {
        CUSTOM("generator.custom"),
        MODRINTH("aiutd.modrinth");

        private final String displayName;

        Platforms(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Text getDisplayName() {
            return Text.translatable(displayName);
        }
    }

    @SerialEntry
    public String modpackId = "<modpack-id>";

    @SerialEntry
    public String localVersion = "1.0.0";

    @SerialEntry
    public String versionAPI = "https://raw.githubusercontent.com/<you>/<modpack-name>/main/Packwiz/pack.toml";

    @SerialEntry
    public String changelogLink = "https://raw.githubusercontent.com/<you>/<modpack-name>/main/CHANGELOG.md";

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
