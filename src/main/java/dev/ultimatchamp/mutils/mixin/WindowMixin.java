package dev.ultimatchamp.mutils.mixin;

import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Window.class)
public class WindowMixin {
    @ModifyArg(method = "setTitle", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowTitle(JLjava/lang/CharSequence;)V"), index = 1)
    private CharSequence mutils$setTitle(CharSequence title) {
        if (ModpackUtilsConfig.instance().customTitle) {
            var newTitle = ModpackUtilsConfig.instance().title;
            newTitle = newTitle.replaceAll("<modpack-name>", ModpackUtilsConfig.instance().modpackName);
            newTitle = newTitle.replaceAll("<mc>", MinecraftVersion.CURRENT.getName());
            newTitle = newTitle.replaceAll("<version>", ModpackUtilsConfig.instance().localVersion);

            return newTitle;
        }
        return title;
    }
}
