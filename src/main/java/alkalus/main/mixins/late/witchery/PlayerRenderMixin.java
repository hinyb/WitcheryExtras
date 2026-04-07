package alkalus.main.mixins.late.witchery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.potion.PotionEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.client.PlayerRender;
import com.emoniph.witchery.integration.ModHookMorph;
import com.emoniph.witchery.util.EntitySizeInfo;

import alkalus.main.core.util.ClientRenderCache;

@Mixin(value = PlayerRender.class, remap = false)
public class PlayerRenderMixin {

    @Inject(
            method = "onRenderTick",
            at = @At(value = "NEW", target = "com/emoniph/witchery/util/EntitySizeInfo"),
            cancellable = true)
    private void onRenderTick(CallbackInfo ci) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        EntitySizeInfo size = new EntitySizeInfo(player);
        PotionEffect shrunk = Witchery.Potions.RESIZING != null
                ? player.getActivePotionEffect(Witchery.Potions.RESIZING)
                : null;
        if ((shrunk != null || !size.isDefault) && !ModHookMorph.isMorphed(player, true)) {
            float amp = shrunk != null ? PotionResizing.getScaleFactor(shrunk.getAmplifier()) : 1.0F;
            float scale = size.defaultHeight / 1.8F * amp;
            float targetOffset = 1.8F * (1.0F - scale);
            float currentOffset = ClientRenderCache.shapeShiftYOffset;
            if (currentOffset != targetOffset) {
                if (currentOffset < targetOffset) {
                    currentOffset = Math.min(currentOffset + 0.01F, targetOffset);
                } else {
                    currentOffset = Math.max(currentOffset - 0.01F, targetOffset);
                }
            }
            player.yOffset = 1.62f - currentOffset;
            ClientRenderCache.shapeShiftYOffset = currentOffset;
        } else {
            player.yOffset = 1.62f;
            ClientRenderCache.shapeShiftYOffset = 0;
        }
        ci.cancel();
    }
}
