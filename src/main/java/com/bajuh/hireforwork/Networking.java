package com.bajuh.hireforwork;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Contants.ModID, Contants.ChannelID), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(),
                PacketOpenGui.class,
                PacketOpenGui::toBytes,
                PacketOpenGui::new,
                PacketOpenGui::handle);

        INSTANCE.registerMessage(nextID(),
                PacketHire.class,
                PacketHire::toBytes,
                PacketHire::new,
                PacketHire::handle);
    }
}
