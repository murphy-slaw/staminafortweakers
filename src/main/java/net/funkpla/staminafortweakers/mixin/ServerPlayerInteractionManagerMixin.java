package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Miner;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow
    @Final
    protected ServerPlayerEntity player;
    @Shadow
    private boolean mining;

    @Inject(method = "processBlockBreakingAction", at = @At("TAIL"))
    private void stopMining(CallbackInfo ci) {
        if (!this.mining) {
            ((Miner) this.player).setMining(false);
        }
    }
}
