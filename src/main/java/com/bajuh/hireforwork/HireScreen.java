package com.bajuh.hireforwork;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.NPCMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class HireScreen extends MerchantScreen {

    private static final int WIDTH = 276;
    private static final int HEIGHT = 166;

    private final ResourceLocation GUI = new ResourceLocation(Contants.ModID, "textures/gui/hire_menu_bg.png");

    private PlayerEntity player;
    private VillagerEntity villager;

    public HireScreen(PlayerEntity player, VillagerEntity villager) {
        ScreenManager.openScreen(packetIn.getContainerType(), this.client, packetIn.getWindowId(), packetIn.getTitle());
        super(new MerchantContainer(player.currentWindowId, player.inventory, new NPCMerchant(player)));

        this.player = player;
        this.villager = villager;
    }

    @Override
    protected void init() {
        addButton(
            new ImageButton(0, 0, 12, 19, 276, 0, 0,
                GUI, button -> hire()));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void hire() {
        Networking.INSTANCE.sendToServer(new PacketHire(player.getUniqueID(), villager.getUniqueID()));
        minecraft.displayGuiScreen(null);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(relX, relY, 0, 0, WIDTH, HEIGHT);
        super.render(mouseX, mouseY, partialTicks);
    }

    public static void open(PlayerEntity player, VillagerEntity villager) {
        Minecraft.getInstance().displayGuiScreen(new HireScreen(player, villager));
    }
}

