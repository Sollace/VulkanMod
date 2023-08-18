package net.vulkanmod.mixin.matrix;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Matrix4f.class, remap = false)
public abstract class Matrix4fM {

    @Shadow public abstract Matrix4f perspective(float fovy, float aspect, float zNear, float zFar, boolean zZeroToOne);
    @Shadow public abstract Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar, boolean zZeroToOne);

    /**
     * @author Collateral
     * @reason Set zZeroToOne parameter to true
     *
     * TODO: Side-effect changed: Original returned "this", new method returns a copy
     */
    @Overwrite(remap = false)
    public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar) {
        return new Matrix4f().setOrtho(left, right, bottom, top, zNear, zFar, true);
    }

    /**
     * @author Collateral
     * @reason Set zZeroToOne parameter to true
     *
     * TODO: Side-effect changed: Original returned "this", new method returns a copy
     */
    @Overwrite(remap = false)
    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
        return this.ortho(left, right, bottom, top, zNear, zFar, true);
    }

    /**
     * @author Collateral
     * @reason Set zZeroToOne parameter to true
     */
    @Overwrite(remap = false)
    public Matrix4f perspective(float fovy, float aspect, float zNear, float zFar) {
        return this.perspective(fovy, aspect, zNear, zFar, true);
    }

    /**
     * @author Collateral
     * @reason Set zZeroToOne parameter to true
     */
    @Overwrite(remap = false)
    public Matrix4f setPerspective(float fovy, float aspect, float zNear, float zFar) {
        return new Matrix4f().setPerspective(fovy, aspect, zNear, zFar, true);
    }
}
