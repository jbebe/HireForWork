package com.bajuh.hireforwork;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Contants.ModID)
public class Entry
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Entry() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Entry::init);
    }

    private static void init(final FMLCommonSetupEvent event){
        Networking.registerMessages();
    }
}
