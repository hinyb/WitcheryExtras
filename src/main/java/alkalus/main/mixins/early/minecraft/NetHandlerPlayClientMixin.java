package alkalus.main.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import alkalus.main.core.util.ClientRenderCache;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {

    @Shadow
    private Minecraft gameController;

    @Redirect(
            method = "handlePlayerPosLook",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/play/server/S08PacketPlayerPosLook;func_148928_d()D"))
    private double applyOffset(S08PacketPlayerPosLook packet) {
        if (this.gameController.renderViewEntity != null
                && !(this.gameController.currentScreen instanceof GuiSleepMP)) {
            return packet.func_148928_d() - ClientRenderCache.shapeShiftYOffset;
        }
        return packet.func_148928_d();
    }
}
