package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.packet.payload.MovementPacketPayload;
import net.funkpla.staminafortweakers.packet.payload.SwimPacketPayload;
import net.funkpla.staminafortweakers.packet.payload.WeaponSwingPacketPayload;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
  private static final String PROTOCOL_VERSION = "1";

  public static void registerPackets(RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
    registrar.playToServer(
        MovementPacketPayload.TYPE,
        MovementPacketPayload.CODEC,
        new DirectionalPayloadHandler<>(PacketHandler::handle, PacketHandler::handle));
    registrar.playToServer(
        SwimPacketPayload.TYPE,
        SwimPacketPayload.CODEC,
        new DirectionalPayloadHandler<>(PacketHandler::handle, PacketHandler::handle));
    registrar.playToServer(
        WeaponSwingPacketPayload.TYPE,
        WeaponSwingPacketPayload.CODEC,
        new DirectionalPayloadHandler<>(PacketHandler::handle, PacketHandler::handle));
  }

  public static void handle(final MovementPacketPayload msg, final IPayloadContext ctx) {
    ctx.enqueueWork(() -> handleMovementPacket(msg, ctx));
  }

  public static void handleMovementPacket(
      final MovementPacketPayload packet, final IPayloadContext ctx) {
    if (ctx.listener() instanceof ServerPacketListener) {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      ((Swimmer) sender).setHasMovementInput(packet.moving());
    }
  }

  public static void sendMovementPacket(boolean moving) {
    PacketDistributor.sendToServer(new MovementPacketPayload(moving));
  }

  public static void handle(final SwimPacketPayload msg, final IPayloadContext ctx) {
    ctx.enqueueWork(() -> handleSwimPacket(msg, ctx));
  }

  public static void handleSwimPacket(final SwimPacketPayload packet, final IPayloadContext ctx) {
    if (ctx.listener() instanceof ServerPacketListener) {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      ((Swimmer) sender).setSwamUp(packet.swamUp());
    }
  }

  public static void sendSwimPacket(boolean swamUp) {
    PacketDistributor.sendToServer(new SwimPacketPayload(swamUp));
  }

  public static void handle(final WeaponSwingPacketPayload msg, final IPayloadContext ctx) {
    ctx.enqueueWork(() -> handleWeaponSwingPacket(msg, ctx));
  }

  public static void handleWeaponSwingPacket(
      final WeaponSwingPacketPayload packet, final IPayloadContext ctx) {
    if (ctx.listener() instanceof ServerPacketListener) {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      if (!sender.swinging) ((Attacker) sender).setSwungWeapon(packet.swung());
    }
  }

  public static void sendWeaponSwingPacket(boolean swung) {
    PacketDistributor.sendToServer(new WeaponSwingPacketPayload(swung));
  }
}
