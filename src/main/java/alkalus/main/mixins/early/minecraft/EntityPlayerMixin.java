package alkalus.main.mixins.early.minecraft;

import net.minecraft.client.entity.EntityClientPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import alkalus.main.mixins.hooks.EntitySizeManager;

@Mixin(EntityClientPlayerMP.class)
public class EntityPlayerMixin {

    @Inject(method = "onUpdate", at = @At(value = "HEAD"))
    private void onUpdate(CallbackInfo ci) {
        EntityClientPlayerMP player = (EntityClientPlayerMP) (Object) this;
        float targetOffset = EntitySizeManager.getTargetYOffsetClient(player);
        if (targetOffset != 0) {
            float currentOffset = EntitySizeManager.shapeShiftYOffset;
            if (currentOffset != targetOffset) {
                if (currentOffset < targetOffset) {
                    currentOffset = Math.min(currentOffset + 0.01F, targetOffset);
                } else {
                    currentOffset = Math.max(currentOffset - 0.01F, targetOffset);
                }
            }
            player.yOffset = 1.62f - currentOffset;
            EntitySizeManager.shapeShiftYOffset = currentOffset;
        } else {
            player.yOffset = 1.62f;
            EntitySizeManager.shapeShiftYOffset = 0;
        }
    }
}
