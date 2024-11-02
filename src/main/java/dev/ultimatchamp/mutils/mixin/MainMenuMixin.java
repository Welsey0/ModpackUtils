package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MainMenuMixin {
    @Shadow
    @Final
    private ToastManager toastManager;

    @Inject(at = @At("RETURN"), method = "collectLoadTimes")
    private void mutils$showUpdateToast(CallbackInfo ci) {
        if (ModpackUtilsConfig.instance().menuAlert && ModpackUtils.updateAvailable()) {
            SystemToast.show(this.toastManager, SystemToast.Type.PERIODIC_NOTIFICATION, Text.literal(ModpackUtilsConfig.instance().chatMessage), Text.literal(ModpackUtilsConfig.instance().localVersion + " --> " + ModpackUtils.getLatestVersion()));
            ModpackUtils.LOGGER.warn("[ModpackUtils] {}: {} --> {}", ModpackUtilsConfig.instance().chatMessage, ModpackUtilsConfig.instance().localVersion, ModpackUtils.getLatestVersion());
        }
    }
}
