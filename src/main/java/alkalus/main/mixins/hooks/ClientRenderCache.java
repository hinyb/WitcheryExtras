package alkalus.main.mixins.hooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRenderCache {

    public static float shapeShiftYOffset = 0f;
}
