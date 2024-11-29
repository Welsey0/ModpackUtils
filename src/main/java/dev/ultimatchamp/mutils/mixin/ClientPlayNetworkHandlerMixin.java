package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.ModpackUtils;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void mutils$showUpdateMessage(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null) {
            return;
        }

        if (ModpackUtilsConfig.instance().chatWelcome) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal(
                    ModpackUtilsConfig.instance().chatWelcomeMessage
                            .replaceAll("<modpack-name>", ModpackUtilsConfig.instance().modpackName)
                            .replaceAll("<version>", ModpackUtilsConfig.instance().localVersion)
            ).styled(arg -> arg.withColor(Formatting.GREEN)), false);
        }

        if (ModpackUtilsConfig.instance().menuAlert && ModpackUtils.updateAvailable() && ModpackUtils.getLatestVersion() != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal(ModpackUtilsConfig.instance().chatMessage).styled(arg -> arg.withColor(Formatting.YELLOW)), false);
            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal(ModpackUtilsConfig.instance().modpackName + " " + ModpackUtilsConfig.instance().localVersion + " --> " + ModpackUtils.getLatestVersion())
                            .styled(arg -> arg
                                    .withUnderline(true)
                                    .withColor(Formatting.YELLOW)
                                    .withClickEvent(new ClickEvent(
                                            ClickEvent.Action.OPEN_URL,
                                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CUSTOM
                                                    ? ModpackUtilsConfig.instance().changelogLink
                                                    : ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH
                                                    ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion
                                                    : "https://www.curseforge.com/minecraft/modpacks/" + ModpackUtilsConfig.instance().modpackId + "/" + ModpackUtils.getLatestFileId()
                                    ))
                            ),
                    false
            );
        }

        if (ModpackUtilsConfig.instance().ramChatAlert) {
            var allocatedRam = ModpackUtils.getAllocatedRam();
            var minRam = ModpackUtilsConfig.instance().minRam;

            if (minRam > allocatedRam) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("mutils.text.lowRam").styled(arg -> arg.withColor(Formatting.RED)), false);
                MinecraftClient.getInstance().player.sendMessage(
                        Text.literal(allocatedRam + " --> " + minRam)
                                .styled(arg -> arg
                                        .withColor(Formatting.RED)
                                ),
                        false
                );
            }
        }
    }
}
