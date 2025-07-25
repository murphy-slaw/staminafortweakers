package net.funkpla.staminafortweakers.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.funkpla.staminafortweakers.Constants;
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
        var mobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.of(this.id, ':'));
        if (mobEffect != null) {
            return Optional.of(new MobEffectInstance(mobEffect, this.duration, this.amplifier, this.ambient, this.visible));
        }
        Constants.LOG.info("Effect {} not found in mob effect registry", this.id);
        return Optional.empty();
    }
}
