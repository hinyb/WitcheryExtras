package alkalus.main.mixins.late.witchery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.emoniph.witchery.client.RenderEntityViewer;

import alkalus.main.core.util.ClientRenderCache;

@Mixin(value = RenderEntityViewer.class, remap = false)
public class RenderEntityViewerMixin extends EntityRenderer {

    @Final
    @Shadow
    private Minecraft mc;

    public RenderEntityViewerMixin(Minecraft p_i45076_1_, IResourceManager p_i45076_2_) {
        super(p_i45076_1_, p_i45076_2_);
    }

    @Inject(method = "setOffset", at = @At(value = "TAIL"), remap = false)
    public void setOffset(float offset, CallbackInfo ci) {
        mc.thePlayer.yOffset = 1.62f - offset;
        ClientRenderCache.shapeShiftYOffset = offset;
    }

    /**
     * @author hinyb
     * @reason skip the view shifting logic
     */
    @Overwrite(remap = false)
    private boolean canShiftView() {
        return false;
    }
}
