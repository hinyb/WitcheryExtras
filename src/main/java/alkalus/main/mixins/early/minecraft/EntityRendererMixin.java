package alkalus.main.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import alkalus.main.mixins.hooks.ClientRenderCache;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    private Minecraft mc;

    @ModifyVariable(method = "orientCamera", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
    private float applyOffset(float origin) {
        EntityLivingBase player = this.mc.renderViewEntity;
        if (player != null && !player.isPlayerSleeping() && !player.isRiding()) {
            return origin + ClientRenderCache.shapeShiftYOffset;
        }
        return origin;
    }
}
