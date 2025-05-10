package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*? if >1.21.4 {*/import java.net.URI;/*?}*/

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void mutils$showUpdateMessage(ClientboundLoginPacket packet, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null) return;

        if (ModpackUtilsConfig.instance().chatWelcome) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(
                    ModpackUtilsConfig.instance().chatWelcomeMessage
                            .replaceAll("<modpack-name>", ModpackUtilsConfig.instance().modpackName)
                            .replaceAll("<version>", ModpackUtilsConfig.instance().localVersion)
            ).withStyle(arg -> arg.withColor(ChatFormatting.GREEN)), false);
        }

        if (ModpackUtilsConfig.instance().menuAlert && ModpackUtils.updateAvailable() && ModpackUtils.getLatestVersion() != null) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(ModpackUtilsConfig.instance().chatMessage).withStyle(arg -> arg.withColor(ChatFormatting.YELLOW)), false);
            Minecraft.getInstance().player.displayClientMessage(
                    Component.literal(ModpackUtilsConfig.instance().modpackName + " " + ModpackUtilsConfig.instance().localVersion + " --> " + ModpackUtils.getLatestVersion())
                            .withStyle(arg -> arg
                                    .withUnderlined(true)
                                    .withColor(ChatFormatting.YELLOW)
                                    .withClickEvent(/*? if >1.21.4 {*/new ClickEvent.OpenUrl(/*?} else {*//*new ClickEvent(ClickEvent.Action.OPEN_URL,*//*?}*/
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                    /*? if >1.21.4 {*/URI.create/*?}*/(ModpackUtilsConfig.instance().changelogLink) :
                                                    ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                            /*? if >1.21.4 {*/URI.create/*?}*/("https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtils.getLatestVersion()) :
                                                            /*? if >1.21.4 {*/URI.create/*?}*/("https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLatestFileId())
                                    ))
                            ),
                    false
            );
        }

        if (ModpackUtilsConfig.instance().ramChatAlert) {
            var allocatedRam = ModpackUtils.getAllocatedRam();
            var minRam = ModpackUtilsConfig.instance().minRam;

            if (minRam > allocatedRam) {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("mutils.text.lowRam").withStyle(arg -> arg.withColor(ChatFormatting.RED)), false);
                Minecraft.getInstance().player.displayClientMessage(
                        Component.literal(allocatedRam + " --> " + minRam)
                                .withStyle(arg -> arg
                                        .withColor(ChatFormatting.RED)
                                ),
                        false
                );
            }
        }
    }
}
