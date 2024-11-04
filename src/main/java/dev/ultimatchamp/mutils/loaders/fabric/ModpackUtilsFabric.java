//? if fabric {
package dev.ultimatchamp.mutils.loaders.fabric;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.fabricmc.api.ClientModInitializer;

public class ModpackUtilsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModpackUtilsConfig.load();

        if (ModpackUtils.getLatestVersion() == null) {
            ModpackUtils.LOGGER.error("[ModpackUtils] Failed to fetch the modpack info!");
        }
    }
}
//?}
