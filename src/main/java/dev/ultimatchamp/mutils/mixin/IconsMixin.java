package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.nio.file.Files;

@Mixin(Icons.class)
public class IconsMixin {
    @Inject(method = "getIcon", at = @At("RETURN"), cancellable = true)
    private void mutils$setIcons(ResourcePack resourcePack, String fileName, CallbackInfoReturnable<InputSupplier<InputStream>> cir) {
        if (ModpackUtilsConfig.instance().customIcon) {
            var icon = ModpackUtilsConfig.CONFIG_DIR.resolve("mutils/icons/" + fileName);

            if (Files.exists(icon)) {
                cir.setReturnValue(() -> Files.newInputStream(icon));
            } else {
                ModpackUtils.LOGGER.error("[ModpackUtils] Custom icon file not found: {}", fileName);
            }
        }
    }
}
