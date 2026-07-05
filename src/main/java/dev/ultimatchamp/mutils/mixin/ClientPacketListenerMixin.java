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

import java.net.URI;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void mutils$showUpdateMessage(ClientboundLoginPacket packet, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null) return;

        if (ModpackUtilsConfig.instance().chatWelcome) {
            Minecraft.getInstance().player.sendSystemMessage((Component) Component.literal(
                    ModpackUtilsConfig.instance().chatWelcomeMessage
                            .replaceAll("<modpack-name>", ModpackUtilsConfig.instance().modpackName)
                            .replaceAll("<version>", ModpackUtilsConfig.instance().localVersion)
            ).withStyle(arg -> arg.withColor(ChatFormatting.GREEN)));
        }

        if (ModpackUtilsConfig.instance().menuAlert && ModpackUtils.updateAvailable() && ModpackUtils.getLatestVersion() != null) {
            Minecraft.getInstance().player.sendSystemMessage((Component) Component.literal(ModpackUtilsConfig.instance().chatMessage).withStyle(arg -> arg.withColor(ChatFormatting.YELLOW)));
            Minecraft.getInstance().player.sendSystemMessage(
                    (Component) Component.literal(ModpackUtilsConfig.instance().modpackName + " " + ModpackUtilsConfig.instance().localVersion + " --> " + ModpackUtils.getLatestVersion())
                            .withStyle(arg -> arg
                                    .withUnderlined(true)
                                    .withColor(ChatFormatting.YELLOW)
                                    .withClickEvent(new ClickEvent.OpenUrl(
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM ?
                                                    URI.create(ModpackUtilsConfig.instance().changelogLink) :
                                                    ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ?
                                                            URI.create("https://modrinth.com" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtils.getLatestVersion()) :
                                                            URI.create("https://curseforge.com" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLatestFileId())
                                    ))
                            )
            );
        }

        if (ModpackUtilsConfig.instance().ramChatAlert) {
            var allocatedRam = ModpackUtils.getAllocatedRam();
            var minRam = ModpackUtilsConfig.instance().minRam;

            if (minRam > allocatedRam) {
                Minecraft.getInstance().player.sendSystemMessage((Component) Component.translatable("mutils.text.lowRam").withStyle(arg -> arg.withColor(ChatFormatting.RED)));
                Minecraft.getInstance().player.sendSystemMessage(
                        (Component) Component.literal(allocatedRam + " --> " + minRam)
                                .withStyle(arg -> arg
                                        .withColor(ChatFormatting.RED)
                                )
                );
            }
        }
    }
}