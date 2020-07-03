package com.bajuh.hireforwork;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class PacketOpenGui {



    public PacketOpenGui(PacketBuffer buf) {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public PacketOpenGui() {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            ScreenManager.openScreen(packetIn.getContainerType(), this.client, packetIn.getWindowId(), packetIn.getTitle()));
        ctx.get().setPacketHandled(true);
    }

}
