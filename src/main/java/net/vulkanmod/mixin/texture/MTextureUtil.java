package net.vulkanmod.mixin.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TextureUtil.class)
public class MTextureUtil {

    /**
     * @author Collateral
     * @reason Remove special logic
     */
    @Overwrite(remap = false)
    public static int generateTextureId() {
        return GlStateManager._genTexture();
    }

    /**
     * @author Collateral
     * @reason Replace with NOOP
     */
    @Overwrite(remap = false)
    public static void prepareImage(NativeImage.InternalGlFormat internalGlFormat, int id, int j, int k, int l) {}
}
