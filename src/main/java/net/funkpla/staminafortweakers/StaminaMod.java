package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.packet.C2SRecievers;
import net.funkpla.staminafortweakers.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StaminaMod implements ModInitializer {
    public static final String MOD_ID = "staminafortweakers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation locate(String path) {
        return new ResourceLocation(MOD_ID, path);
    }


    @Override
    public void onInitialize() {
        Attributes.register();
        Enchantments.register();
        StatusEffects.register();
        SoundEvents.register();
        Potions.register();
        C2SRecievers.registerPackets();
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
            config.validatePostStart();
        });

        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, entity) -> {
            if (level.isClientSide() || player.isCreative() || player.isSpectator())
                return;
            ((Miner) player).depleteStaminaForBlockBreak();
        });

        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (!(level.isClientSide() || player.isCreative() || player.isSpectator())) {
                ((Attacker) player).setAttacked(true);
            }
            return InteractionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!(player.isCreative() || player.isSpectator())) {
                if (stack.getItem() == Items.SHIELD) {
                    if (((Exhaustible) player).isExhausted() || !((Exhaustible) player).isShieldAllowed()) {
                        return InteractionResultHolder.fail(stack);
                    }
                }
            }
            return InteractionResultHolder.pass(stack);
        });

        /*
         * TOO MUCH COFFEE
         */

        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            if (player.hasEffect(StatusEffects.TIRELESSNESS)) {
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            return null;
        });
    }

}