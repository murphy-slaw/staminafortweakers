package net.funkpla.staminafortweakers.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEffectConfig implements INamedEffect {
    private String id;

    public Optional<Holder.Reference<MobEffect>> getEffect() {
        var m = BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.bySeparator(this.id, ':'));
        return m;
    }
}
