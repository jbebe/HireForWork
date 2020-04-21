package com.bajuh.hireforwork;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;


public class PacketHire {

    private final UUID playerId;
    private final UUID villagerId;

    public PacketHire(PacketBuffer buf) {
        playerId = buf.readUniqueId();
        villagerId = buf.readUniqueId();
    }

    public PacketHire(UUID playerId, UUID villagerId) {
        this.playerId = playerId;
        this.villagerId = villagerId;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(playerId);
        buf.writeUniqueId(villagerId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().getServerWorld();
            ModLogic.hireVillager(world, playerId, villagerId);
        });
        ctx.get().setPacketHandled(true);
    }

}
