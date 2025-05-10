//? if fabric {
package dev.ultimatchamp.mutils.loaders.fabric;

import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.fabricmc.api.ClientModInitializer;

public class ModpackUtilsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModpackUtilsConfig.load();
    }
}
//?}
