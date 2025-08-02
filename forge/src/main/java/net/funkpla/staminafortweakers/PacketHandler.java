package net.funkpla.staminafortweakers;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
  private static final String PROTOCOL_VERSION = "1";
  public static final SimpleChannel INSTANCE =
      NetworkRegistry.newSimpleChannel(
          Common.locate("main"),
          () -> PROTOCOL_VERSION,
          PROTOCOL_VERSION::equals,
          PROTOCOL_VERSION::equals);
  public static int messageId;

  public static void registerPackets() {
    INSTANCE.registerMessage(
        messageId++, SwimPacket.class, SwimPacket::encoder, SwimPacket::new, SwimPacket::handle);
    INSTANCE.registerMessage(
        messageId++,
        MovementInputPacket.class,
        MovementInputPacket::encoder,
        MovementInputPacket::new,
        MovementInputPacket::handle);
    INSTANCE.registerMessage(
        messageId++,
        WeaponSwingPacket.class,
        WeaponSwingPacket::encoder,
        WeaponSwingPacket::new,
        WeaponSwingPacket::handle);
  }

  public static void sendSwimPacket() {
    INSTANCE.sendToServer(new SwimPacket(true));
  }

  public static void sendMovementInputPacket(boolean hasMovementInput) {
    INSTANCE.sendToServer(new MovementInputPacket(hasMovementInput));
  }

  public static void sendWeaponSwingPacket() {
    INSTANCE.sendToServer(new WeaponSwingPacket(true));
  }

  public static void handle(SwimPacket msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> handleSwimPacket(msg, ctx));
    ctx.get().setPacketHandled(true);
  }

  public static void handle(MovementInputPacket msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> handleMovementInputPacket(msg, ctx));
    ctx.get().setPacketHandled(true);
  }

  public static void handle(WeaponSwingPacket msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> handleWeaponSwingPacket(msg, ctx));
    ctx.get().setPacketHandled(true);
  }

  public static void handleSwimPacket(SwimPacket packet, Supplier<NetworkEvent.Context> ctx) {
    PacketListener listener = ctx.get().getNetworkManager().getPacketListener();
    if (listener instanceof ServerPacketListener) {
      ServerPlayer sender = ctx.get().getSender();
      if (sender != null) ((Swimmer) sender).setSwamUp(packet.swamUp);
    }
  }

  public static void handleMovementInputPacket(
      MovementInputPacket packet, Supplier<NetworkEvent.Context> ctx) {
    PacketListener listener = ctx.get().getNetworkManager().getPacketListener();
    if (listener instanceof ServerPacketListener) {
      ServerPlayer sender = ctx.get().getSender();
      if (sender != null) ((Swimmer) sender).setHasMovementInput(packet.hasMovementInput);
    }
  }

  public static void handleWeaponSwingPacket(
      WeaponSwingPacket packet, Supplier<NetworkEvent.Context> ctx) {
    PacketListener listener = ctx.get().getNetworkManager().getPacketListener();
    if (listener instanceof ServerPacketListener) {
      ServerPlayer sender = ctx.get().getSender();
      if (sender != null) ((Attacker) sender).setSwungWeapon(packet.swung);
    }
  }

  public static class SwimPacket {
    public boolean swamUp;

    public SwimPacket(boolean swamUp) {
      this.swamUp = swamUp;
    }

    public SwimPacket(FriendlyByteBuf buffer) {
      swamUp = buffer.readBoolean();
    }

    public void encoder(FriendlyByteBuf buffer) {
      buffer.writeBoolean(swamUp);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PacketHandler.handle(this, ctx));
      ctx.get().setPacketHandled(true);
    }
  }

  public static class MovementInputPacket {
    public boolean hasMovementInput;

    public MovementInputPacket(boolean hasMovementInput) {
      this.hasMovementInput = hasMovementInput;
    }

    public MovementInputPacket(FriendlyByteBuf buffer) {
      hasMovementInput = buffer.readBoolean();
    }

    public void encoder(FriendlyByteBuf buffer) {
      buffer.writeBoolean(hasMovementInput);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PacketHandler.handle(this, ctx));
      ctx.get().setPacketHandled(true);
    }
  }

  public static class WeaponSwingPacket {
    public boolean swung;

    public WeaponSwingPacket(boolean swung) {
      this.swung = swung;
    }

    public WeaponSwingPacket(FriendlyByteBuf buffer) {
      swung = buffer.readBoolean();
    }

    public void encoder(FriendlyByteBuf buffer) {
      buffer.writeBoolean(swung);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PacketHandler.handle(this, ctx));
      ctx.get().setPacketHandled(true);
    }
  }
}
