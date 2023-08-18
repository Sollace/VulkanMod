package net.vulkanmod.mixin.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.vulkanmod.vulkan.Drawer;
import net.vulkanmod.vulkan.Framebuffer;
import net.vulkanmod.vulkan.util.DrawUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTarget.class)
public class RenderTargetMixin {

    @Shadow public int viewWidth;
    @Shadow public int viewHeight;
    @Shadow public int width;
    @Shadow public int height;

    Framebuffer framebuffer;

    /**
     * @author Collateral
     * @reason Use Drawer
     */
    @Overwrite
    public void clear(boolean getError) {}

    /**
     * @author Collateral
     * @reason TODO
     */
    @Overwrite
    public void resize(int i, int j, boolean bl) {
        if(this.framebuffer != null) {
            this.framebuffer.cleanUp();
        }

        this.viewWidth = i;
        this.viewHeight = j;
        this.width = i;
        this.height = j;

        //TODO
//        this.framebuffer = new Framebuffer(this.width, this.height, Framebuffer.DEFAULT_FORMAT);
    }

    /**
     * @author Collateral
     * @reason Use Drawer
     */
    @Overwrite
    public void bindWrite(boolean updateViewport) {
        Drawer.getInstance().beginRendering(framebuffer);
    }

    /**
     * @author Collateral
     * @reason Replace with NOOP
     */
    @Overwrite
    public void unbindWrite() {

    }

    /**
     * @author Collateral
     * @reason Use DrawUtil
     */
    @Overwrite
    private void _blitToScreen(int width, int height, boolean disableBlend) {
        RenderSystem.depthMask(false);

        DrawUtil.drawFramebuffer(this.framebuffer);

        RenderSystem.depthMask(true);
    }
}
