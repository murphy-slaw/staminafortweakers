package net.funkpla.staminafortweakers;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class StaminaHudOverlay implements HudRenderCallback {
    private static final Identifier EMPTY_STAMINA = new Identifier(StaminaForTweakers.MOD_ID, "textures/stamina/stamina_bar_empty.png");
    private static final Identifier FILLED_STAMINA = new Identifier(StaminaForTweakers.MOD_ID, "textures/stamina/stamina_bar_filled.png");
    private static final int BAR_LENGTH = 81;
    private static final int BAR_HEIGHT = 8;

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            x = width / 2;
            y = height;
        }

        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 10; i++) {
            context.drawTexture(EMPTY_STAMINA, x - 91, y - 47, 0, 0, BAR_LENGTH, BAR_HEIGHT, BAR_LENGTH, BAR_HEIGHT);
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        float scaledStamina = (float) (player.getAttributeValue(StaminaForTweakers.STAMINA) / player.getAttributeValue(StaminaForTweakers.MAX_STAMINA));

        context.drawTexture(FILLED_STAMINA, x - 91, y - 47, 0, 0, (int) (BAR_LENGTH * scaledStamina), BAR_HEIGHT, BAR_LENGTH, BAR_HEIGHT);
    }
}