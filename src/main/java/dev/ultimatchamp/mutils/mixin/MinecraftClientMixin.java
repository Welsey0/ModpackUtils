package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    private ToastManager toastManager;

    @Inject(method = "collectLoadTimes", at = @At("RETURN"))
    private void mutils$showUpdateToast(CallbackInfo ci) {
        if (ModpackUtilsConfig.instance().menuAlert && ModpackUtils.updateAvailable() && ModpackUtils.getLatestVersion() != null) {
            SystemToast.show(this.toastManager, SystemToast.Type.PERIODIC_NOTIFICATION, Text.translatable("mutils.text.updateAvailable").formatted(Formatting.DARK_AQUA, Formatting.BOLD), Text.literal(ModpackUtilsConfig.instance().modpackName + " ").formatted(Formatting.BOLD).append(Text.literal(ModpackUtilsConfig.instance().localVersion + " --> " + ModpackUtils.getLatestVersion()).formatted(Formatting.GOLD)));
            ModpackUtils.LOGGER.warn("[ModpackUtils] {}: {} --> {}", ModpackUtilsConfig.instance().chatMessage, ModpackUtilsConfig.instance().localVersion, ModpackUtils.getLatestVersion());
        }
    }
}
