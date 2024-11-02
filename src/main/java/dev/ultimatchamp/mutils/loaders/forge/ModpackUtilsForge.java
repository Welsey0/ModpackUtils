//? if forge {
/*package dev.ultimatchamp.mutils.loaders.forge;

import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

    private void onClientSetup(FMLClientSetupEvent event) {}
}
*///?}
