package net.funkpla.staminafortweakers.mixin.client;

import com.mojang.authlib.GameProfile;
import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Inject(
            method = "canSprint()Z",
            at = @At("HEAD"),
            cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        double stamina = this.getAttributeValue(StaminaForTweakers.STAMINA);
        double max_stamina = this.getAttributeValue(StaminaForTweakers.MAX_STAMINA);
        boolean isNotExhausted = ((stamina / max_stamina) * 100) <= config.exhaustedPercentage;
        cir.setReturnValue(
                isNotExhausted || this.hasVehicle() || (float) this.getHungerManager().getFoodLevel() > 6.0F || this.getAbilities().allowFlying
        );
    }

}