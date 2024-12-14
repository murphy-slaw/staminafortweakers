package net.funkpla.staminafortweakers.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEffectConfig implements INamedEffect{
    private String id;

public Optional<MobEffect> getEffect(){
    var m = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.of(this.id, ':'));
    if (m != null){
        return Optional.of(m);
    }
    return Optional.empty();
}
}
