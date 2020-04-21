package com.bajuh.hireforwork;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Contants.ModID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void handleCropRightClick(final PlayerInteractEvent.EntityInteract event) {
        Entity targetEntity = event.getTarget();
        ModLogic.onPlayerInteractWithEntity(event, targetEntity);
    }

}
