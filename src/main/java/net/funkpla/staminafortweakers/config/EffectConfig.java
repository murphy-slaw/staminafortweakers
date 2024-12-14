package net.funkpla.staminafortweakers.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.funkpla.staminafortweakers.StaminaMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EffectConfig implements INamedEffect {
    private String id;
    private int duration;
    private int amplifier;
    private boolean ambient;
    private boolean visible;

    public Optional<MobEffectInstance> getEffectInstance() {
        var mobEffect = BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.bySeparator(this.id, ':'));
        if (mobEffect.isPresent()){
            return Optional.of(new MobEffectInstance(mobEffect.get(), this.duration, this.amplifier, this.ambient, this.visible));
        }
        StaminaMod.LOGGER.warn("Effect {} not found in mob effect registry", this.id);
        return Optional.empty();
    }
}
