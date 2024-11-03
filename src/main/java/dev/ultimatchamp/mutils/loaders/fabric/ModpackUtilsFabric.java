//? if fabric {
package dev.ultimatchamp.mutils.loaders.fabric;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.fabricmc.api.ClientModInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class ModpackUtilsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModpackUtilsConfig.load();
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
//?}
