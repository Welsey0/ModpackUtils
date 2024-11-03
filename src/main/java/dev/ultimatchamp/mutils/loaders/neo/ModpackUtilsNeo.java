//? if neo {
/*package dev.ultimatchamp.mutils.loaders.neo;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Mod("mutils")
public class ModpackUtilsNeo {
    public ModpackUtilsNeo(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        ModpackUtilsConfig.load();

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> ModpackUtilsConfig.createScreen(parent));
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
