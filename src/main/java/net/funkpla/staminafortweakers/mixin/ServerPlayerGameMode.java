package net.funkpla.staminafortweakers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(net.minecraft.server.level.ServerPlayerGameMode.class)
public interface ServerPlayerGameMode {
    @Accessor
    boolean getIsDestroyingBlock();
}