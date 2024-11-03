//? if forge {
/*package dev.ultimatchamp.mutils.loaders.forge;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Mod("mutils")
public class ModpackUtilsForge {
    public ModpackUtilsForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        ModpackUtilsConfig.load();

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () ->  new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ModpackUtilsConfig.createScreen(parent))
        );
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (ModpackUtils.getLatestVersion() == null) {
            ModpackUtils.LOGGER.error("[ModpackUtils] Failed to fetch the modpack info!");
        }

        var mmcText = ModpackUtils.getMmcText();
        if (mmcText != null) {
            try {
                Files.writeString(ModpackUtilsConfig.CONFIG_DIR.resolve("isxander-main-menu-credits.json"), mmcText, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Failed to implement MainMenuCredits Integration: ", e);
            }
        }
    }
}
*///?}
