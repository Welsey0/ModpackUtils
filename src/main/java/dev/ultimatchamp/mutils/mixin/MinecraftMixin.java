package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >1.21.1 {
import net.minecraft.client.gui.components.toasts.ToastManager;
//?} else {
//import net.minecraft.client.gui.components.toasts.ToastComponent;
//?}

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    //? if >1.21.1 {
    public abstract ToastManager getToastManager();
    //?} else {
    //public abstract ToastComponent getToasts();
    //?}

    @Inject(method = "onGameLoadFinished", at = @At("RETURN"))
    private void mutils$showUpdateToast(CallbackInfo ci) {
        if (ModpackUtilsConfig.instance().menuAlert && ModpackUtils.updateAvailable() && ModpackUtils.getLatestVersion() != null) {
            SystemToast.add(
                    //? if >1.21.1 {
                    this.getToastManager(),
                    //?} else {
                    //this.getToasts(),
                    //?}
                    SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                    Component.translatable("mutils.text.updateAvailable")
                            .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD),
                    Component.literal(ModpackUtilsConfig.instance().modpackName + " ")
                            .withStyle(ChatFormatting.BOLD)
                            .append(Component.literal(ModpackUtilsConfig.instance().localVersion +
                                    " --> " + ModpackUtils.getLatestVersion()).withStyle(ChatFormatting.GOLD))
            );
            ModpackUtils.LOGGER.warn("[ModpackUtils] {}: {} --> {}", ModpackUtilsConfig.instance().chatMessage, ModpackUtilsConfig.instance().localVersion, ModpackUtils.getLatestVersion());
        }

        if (ModpackUtilsConfig.instance().ramMenuAlert) {
            var allocatedRam = ModpackUtils.getAllocatedRam();
            var minRam = ModpackUtilsConfig.instance().minRam;

            if (minRam > allocatedRam) {
                SystemToast.add(
                        //? if >1.21.1 {
                        this.getToastManager(),
                        //?} else {
                        //this.getToasts(),
                        //?}
                        SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                        Component.translatable("mutils.text.lowRam")
                                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
                        Component.literal(allocatedRam + " --> " + minRam)
                                .withStyle(ChatFormatting.GOLD)
                );
                ModpackUtils.LOGGER.error("[ModpackUtils] Low ram allocated: {} --> {}", allocatedRam, minRam);
            }
        }
    }
}
