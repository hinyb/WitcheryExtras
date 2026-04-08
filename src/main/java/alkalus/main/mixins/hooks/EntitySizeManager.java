package alkalus.main.mixins.hooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.util.EntitySizeInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class EntitySizeManager {

    private static float getTargetYOffset(EntityPlayer player) {
        EntitySizeInfo size = new EntitySizeInfo(player);
        PotionEffect shrunk = Witchery.Potions.RESIZING != null
                ? player.getActivePotionEffect(Witchery.Potions.RESIZING)
                : null;
        if (shrunk == null && size.isDefault) {
            return 0;
        }
        float amp = shrunk != null ? PotionResizing.getScaleFactor(shrunk.getAmplifier()) : 1.0F;
        float scale = size.defaultHeight / 1.8F * amp;
        return 1.8F * (1.0F - scale);
    }

    public static float shapeShiftYOffset = 0f;

    public static float getTargetYOffsetClient(EntityPlayer player) {
        return getTargetYOffset(player);
    }

    private static final Map<UUID, StanceGracePeriod> timer = new HashMap<>();

    // Because it gets instantly resent
    public static float getTargetYOffsetServer(EntityPlayer player) {
        float currentOffset = getTargetYOffset(player);
        return timer.computeIfAbsent(player.getUniqueID(), k -> new StanceGracePeriod())
                .getAdjustedOffset(currentOffset);
    }

    private static class StanceGracePeriod {

        private float effectiveOffset = 0;
        private float lastOffset = 0;
        private int lastUpdateTick = 0;
        private static final int GRACE_PERIOD = 1;

        public float getAdjustedOffset(float currentOffset) {
            int tick = MinecraftServer.getServer().getTickCounter();
            if (currentOffset != lastOffset) {
                lastUpdateTick = tick;
                lastOffset = currentOffset;
                if (currentOffset < effectiveOffset) {
                    effectiveOffset = currentOffset;
                }
            }
            if (tick - lastUpdateTick > GRACE_PERIOD) {
                effectiveOffset = currentOffset;
            }
            return effectiveOffset;
        }
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        timer.remove(event.player.getUniqueID());
    }
}
