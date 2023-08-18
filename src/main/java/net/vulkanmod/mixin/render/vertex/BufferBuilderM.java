package net.vulkanmod.mixin.render.vertex;

import com.mojang.blaze3d.vertex.*;
import net.vulkanmod.interfaces.ExtendedVertexBuilder;
import net.vulkanmod.interfaces.VertexFormatMixed;
import net.vulkanmod.render.vertex.VertexUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.util.List;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderM extends DefaultedVertexConsumer
        implements BufferVertexConsumer, ExtendedVertexBuilder {

    @Override
    @Shadow public abstract void endVertex();

    @Shadow private ByteBuffer buffer;

    @Shadow private int nextElementByte;
    @Shadow private boolean fastFormat;
    @Shadow private boolean fullFormat;
    @Shadow private int elementIndex;
    @Shadow private @Nullable VertexFormatElement currentElement;
    @Shadow private VertexFormat format;

    @Shadow @Nullable private Vector3f[] sortingPoints;
    @Nullable
    @Shadow private VertexSorting sorting;
    @Shadow private VertexFormat.Mode mode;

    @Shadow protected abstract it.unimi.dsi.fastutil.ints.IntConsumer intConsumer(int i, VertexFormat.IndexType indexType);

    private long bufferPtr;
    private long ptr;
    private int offset;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void setPtrC(int initialCapacity, CallbackInfo ci) {
        this.bufferPtr = MemoryUtil.memAddress0(this.buffer);
    }

    @Inject(method = "ensureCapacity", at = @At(value = "RETURN"))
    private void setPtr(int initialCapacity, CallbackInfo ci) {
        this.bufferPtr = MemoryUtil.memAddress0(this.buffer);
    }

    @Override
    public void vertex(float x, float y, float z, int packedColor, float u, float v, int overlay, int light, int packedNormal) {
        this.ptr = this.nextElementPtr();

        if(this.format == DefaultVertexFormat.NEW_ENTITY) {
            MemoryUtil.memPutFloat(ptr + 0, x);
            MemoryUtil.memPutFloat(ptr + 4, y);
            MemoryUtil.memPutFloat(ptr + 8, z);

            MemoryUtil.memPutInt(ptr + 12, packedColor);

            MemoryUtil.memPutFloat(ptr + 16, u);
            MemoryUtil.memPutFloat(ptr + 20, v);

            MemoryUtil.memPutInt(ptr + 24, overlay);

            MemoryUtil.memPutInt(ptr + 28, light);
            MemoryUtil.memPutInt(ptr + 32, packedNormal);

            this.nextElementByte += 36;
            this.endVertex();

        }
        else {
            this.vertex(x, y, z);
            this.fastColor(packedColor);
            this.fastUv(u, v);
            this.fastOverlay(overlay);
            this.light(light);
            this.fastNormal(packedNormal);
            this.endVertex();
//            throw new RuntimeException("unaccepted format: " + this.format);
        }

    }

    public void vertex(float x, float y, float z) {
        MemoryUtil.memPutFloat(ptr + 0, x);
        MemoryUtil.memPutFloat(ptr + 4, y);
        MemoryUtil.memPutFloat(ptr + 8, z);

        this.nextElement();
    }

    public void fastColor(int packedColor) {
        if (this.currentElement.getUsage() != VertexFormatElement.Usage.COLOR) return;

        MemoryUtil.memPutFloat(ptr + 12, packedColor);

        this.nextElement();
    }

    public void fastUv(float u, float v) {
        if (this.currentElement.getUsage() != VertexFormatElement.Usage.UV) return;

        MemoryUtil.memPutFloat(ptr + 16, u);
        MemoryUtil.memPutFloat(ptr + 20, v);

        this.nextElement();
    }

    public void fastOverlay(int o) {
        if (this.currentElement.getUsage() != VertexFormatElement.Usage.UV) return;

        MemoryUtil.memPutInt(ptr + 24, o);

        this.nextElement();
    }

    public void light(int l) {
        if (this.currentElement.getUsage() != VertexFormatElement.Usage.UV) return;

        MemoryUtil.memPutInt(ptr + 28, l);

        this.nextElement();
    }

    public void fastNormal(int packedNormal) {
        if (this.currentElement.getUsage() != VertexFormatElement.Usage.NORMAL) return;

        MemoryUtil.memPutInt(ptr + 32, packedNormal);

        this.nextElement();
    }

    /**
     * @author Collateral
     * @reason Implement fastFormat using MemoryUtil
     */
    @Override
    @Overwrite
    public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        if (this.fastFormat) {
            long ptr = this.nextElementPtr();
            MemoryUtil.memPutFloat(ptr + 0, x);
            MemoryUtil.memPutFloat(ptr + 4, y);
            MemoryUtil.memPutFloat(ptr + 8, z);

            int temp = VertexUtil.packColor(red, green, blue, alpha);
            MemoryUtil.memPutInt(ptr + 12, temp);

            MemoryUtil.memPutFloat(ptr + 16, u);
            MemoryUtil.memPutFloat(ptr + 20, v);

            byte i;
            if (this.fullFormat) {
                MemoryUtil.memPutInt(ptr + 24, overlay);
                i = 28;
            } else {
                i = 24;
            }

            MemoryUtil.memPutInt(ptr + i, light);

            temp = VertexUtil.packNormal(normalX, normalY, normalZ);
            MemoryUtil.memPutInt(ptr + i + 4, temp);

            this.nextElementByte += i + 8;
            this.endVertex();
        } else {
            super.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ);
        }
    }

    /**
     * @author Collateral
     * @reason Replace getElements() with getFastList()
     */
    @Override
    @Overwrite
    public void nextElement() {
        VertexFormatElement vertexFormatElement;
        List<VertexFormatElement> list = ((VertexFormatMixed)(this.format)).getFastList();

        this.elementIndex = (this.elementIndex + 1) % list.size();
        this.nextElementByte += this.currentElement.getByteSize();

        this.currentElement = vertexFormatElement = list.get(this.elementIndex);

        if (vertexFormatElement.getUsage() == VertexFormatElement.Usage.PADDING) {
            this.nextElement();
        }
        if (this.defaultColorSet && this.currentElement.getUsage() == VertexFormatElement.Usage.COLOR) {
            BufferVertexConsumer.super.color(this.defaultR, this.defaultG, this.defaultB, this.defaultA);
        }
    }

    @Override
    public void putByte(int index, byte value) {
        MemoryUtil.memPutByte(this.bufferPtr + this.nextElementByte + index, value);
    }

    @Override
    public void putShort(int index, short value) {
        MemoryUtil.memPutShort(this.bufferPtr + this.nextElementByte + index, value);
    }

    @Override
    public void putFloat(int index, float value) {
        MemoryUtil.memPutFloat(this.bufferPtr + this.nextElementByte + index, value);
    }

    private long nextElementPtr() {
        return (this.bufferPtr + this.nextElementByte);
    }

    protected void setNextElementByte(int i) {
        this.nextElementByte = i;
    }
}

/*
@Mixin(VertexSorting.class)
abstract class VertexSortingM {

    / **
     * @author Sollace
     * @reason Replace IntArrays.mergeSort with SortUtil.mergeSort
     * /
    @Overwrite
    public static VertexSorting byDistance(DistanceFunction distanceFunction) {
        return vector3fs -> {
            float[] fs = new float[vector3fs.length];
            int[] is = new int[vector3fs.length];
            for (int i2 = 0; i2 < vector3fs.length; ++i2) {
                fs[i2] = distanceFunction.apply(vector3fs[i2]);
                is[i2] = i2;
            }
            IntArrays.mergeSort(is, (i, j) -> Floats.compare(fs[j], fs[i]));
            //SortUtil.mergeSort(is, (i, j) -> Floats.compare(fs[j], fs[i]));
            return is;
        };
    }

}
*/
