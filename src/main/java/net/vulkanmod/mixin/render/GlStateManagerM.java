package net.vulkanmod.mixin.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.vulkanmod.gl.GlFramebuffer;
import net.vulkanmod.gl.GlTexture;
import net.vulkanmod.vulkan.Drawer;
import net.vulkanmod.vulkan.VRenderSystem;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.IntBuffer;

@Mixin(value = GlStateManager.class, remap = false)
public class GlStateManagerM {

    /**
     * @author Collateral
     * @reason Use GlTexture
     */
    @Overwrite
    public static void _bindTexture(int i) {
        GlTexture.bindTexture(i);
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _disableBlend() {
        RenderSystem.assertOnRenderThread();
        VRenderSystem.disableBlend();
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _enableBlend() {
        RenderSystem.assertOnRenderThread();
        VRenderSystem.enableBlend();
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _blendFunc(int i, int j) {
        RenderSystem.assertOnRenderThread();
        VRenderSystem.blendFunc(i, j);

    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _blendFuncSeparate(int i, int j, int k, int l) {
        RenderSystem.assertOnRenderThread();
        VRenderSystem.blendFuncSeparate(i, j, k, l);

    }

    /**
     * @author Collateral
     * @reason Use Drawer
     */
    @Overwrite
    public static void _disableScissorTest() {
        Drawer.resetScissor();
    }

    /**
     * @author Collateral
     * @reason Does nothing
     */
    @Overwrite
    public static void _enableScissorTest() {}

    /**
     * @author Collateral
     * @reason Use Drawer
     */
    @Overwrite
    public static void _viewport(int x, int y, int width, int height) {
        Drawer.setViewport(x, y, width, height);
    }

    /**
     * @author Collateral
     * @reason Use Drawer
     */
    @Overwrite
    public static void _scissorBox(int x, int y, int width, int height) {
        Drawer.setScissor(x, y, width, height);
    }

    //TODO
    /**
     * @author Collateral
     * @reason TODO
     */
    @Overwrite
    public static int _getError() {
        return 0;
    }

    /**
     * @author Collateral
     * @reason Use GlTexture
     */
    @Overwrite
    public static void _texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, @Nullable IntBuffer pixels) {
        GlTexture.texImage2D(target, level, internalFormat, width, height, border, format, type, pixels != null ? MemoryUtil.memByteBuffer(pixels) : null);
    }

    /**
     * @author Collateral
     * @reason TODO
     */
    @Overwrite
    public static void _texSubImage2D(int target, int level, int offsetX, int offsetY, int width, int height, int format, int type, long pixels) {
        //TODO
    }

    /**
     * @author Collateral
     * @reason TODO
     */
    @Overwrite
    public static void _texParameter(int i, int j, int k) {
        //TODO
    }

    /**
     * @author Collateral
     * @reason TODO
     */
    @Overwrite
    public static void _texParameter(int i, int j, float k) {
        //TODO
    }

    /**
     * @author Collateral
     * @reason TODO
     */
    @Overwrite
    public static void _pixelStore(int pname, int param) {
        //TODO
    }

    /**
     * @author Collateral
     * @reason Use GlTexture
     */
    @Overwrite
    public static int _getTexLevelParameter(int i, int j, int k) {
        RenderSystem.assertInInitPhase();
        return GlTexture.texLevelParameter(i, j, k);
    }

    /**
     * @author Collateral
     * @reason Use GlTexture
     */
    @Overwrite
    public static int _genTexture() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GlTexture.genTextureId();
    }

    /**
     * @author Collateral
     * @reason Use GlTexture
     */
    @Overwrite
    public static void _deleteTexture(int i) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlTexture.glDeleteTextures(i);
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        RenderSystem.assertOnRenderThread();
        VRenderSystem.colorMask(red, green, blue, alpha);
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _depthFunc(int i) {
        RenderSystem.assertOnRenderThreadOrInit();
        VRenderSystem.depthFunc(i);
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _clearColor(float f, float g, float h, float i) {
        RenderSystem.assertOnRenderThreadOrInit();
        VRenderSystem.clearColor(f, g, h, i);
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _clear(int mask, boolean bl) {
        RenderSystem.assertOnRenderThreadOrInit();
        VRenderSystem.clear(mask);
    }

    /**
     * @author Collateral
     * @reason Change to NOOP
     */
    @Overwrite
    public static void _glUseProgram(int i) {}

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _disableDepthTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        VRenderSystem.disableDepthTest();
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _enableDepthTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        VRenderSystem.enableDepthTest();
    }

    /**
     * @author Collateral
     * @reason Use VRenderSystem
     */
    @Overwrite
    public static void _depthMask(boolean bl) {
        RenderSystem.assertOnRenderThread();
        VRenderSystem.depthMask(bl);

    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static int glGenFramebuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GlFramebuffer.genFramebufferId();
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static int glGenRenderbuffers() {
        //TODO
        RenderSystem.assertOnRenderThreadOrInit();
        return GlFramebuffer.genFramebufferId();
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static void _glBindFramebuffer(int i, int j) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.bindFramebuffer(i, j);
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static void _glFramebufferTexture2D(int i, int j, int k, int l, int m) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.glFramebufferTexture2D(i, j, k, l, m);
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static void _glBindRenderbuffer(int i, int j) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.bindRenderbuffer(i, j);
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static void _glFramebufferRenderbuffer(int i, int j, int k, int l) {
        //TODO
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.glFramebufferRenderbuffer(i, j, k, l);
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static void _glRenderbufferStorage(int i, int j, int k, int l) {
        RenderSystem.assertOnRenderThreadOrInit();
//        GL30.glRenderbufferStorage(i, j, k, l);
        GlFramebuffer.glRenderbufferStorage(i, j, k, l);
    }

    /**
     * @author Collateral
     * @reason Use GlFramebuffer
     */
    @Overwrite
    public static int glCheckFramebufferStatus(int i) {
        RenderSystem.assertOnRenderThreadOrInit();
//        return GL30.glCheckFramebufferStatus(i);
        return GlFramebuffer.glCheckFramebufferStatus(i);
    }
}
