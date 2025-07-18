package net.funkpla.staminafortweakers.compat.vc_gliders;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.venturecraft.gliders.common.compat.trinket.CuriosTrinketsUtil;
import net.venturecraft.gliders.common.item.GliderItem;
import net.venturecraft.gliders.data.GliderData;
import net.venturecraft.gliders.util.GliderUtil;

public class VCGlidersCompat {
    public static void setGlide(ItemStack glider, boolean canGlide){
        GliderItem.setGlide(glider,canGlide);
    }

    public static boolean isGliding(LivingEntity entity){
        return GliderUtil.isGlidingWithActiveGlider(entity);
    }

    public static void crashGlider(LivingEntity entity) {
        GliderData.get(entity).ifPresent(data -> {
            ItemStack glider = CuriosTrinketsUtil.getInstance().getFirstFoundGlider(entity);
            if (glider != null && glider.getItem() instanceof GliderItem) {
                VCGlidersCompat.setGlide(glider, false);
                data.sync();
            }
        });
    }
}

