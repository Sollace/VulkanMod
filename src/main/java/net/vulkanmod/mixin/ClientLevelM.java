package net.vulkanmod.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLevel.class)
public class ClientLevelM {

    // TODO: Method moved due to lighting engine rewrite
    /*
    @Inject(method = "setLightReady", at = @At("RETURN"))
    private void setLightReady(int i, int j, CallbackInfo ci) {
        WorldRenderer.getInstance().setSectionsLightReady(i, j);
    }
    */
}
