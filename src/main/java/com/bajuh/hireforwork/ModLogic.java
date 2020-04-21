package com.bajuh.hireforwork;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.UUID;

public class ModLogic {

    public static void onPlayerInteractWithEntity(PlayerInteractEvent.EntityInteract event, Entity targetEntity){
        // Only run on client
        if (!targetEntity.world.isRemote)
            return;

        if (targetEntity instanceof VillagerEntity){
            VillagerEntity villager = (VillagerEntity)targetEntity;
            VillagerData villagerData = villager.getVillagerData();
            VillagerProfession profession = villagerData.getProfession();
            /*if (profession == VillagerProfession.NITWIT)*/{
                PlayerEntity player = event.getPlayer();
                villager.setCustomer(player);
                HireScreen.open(player, villager);
                event.setCanceled(true);
            }
        }
    }

    public static class FollowLivingEntityGoal extends Goal {
        private static final int DELAY = 10;

        private final AgeableEntity follower;
        private final LivingEntity target;
        private final double moveSpeed;
        private final double range;
        private int delayCounter;
        private boolean isFollowing;

        public FollowLivingEntityGoal(AgeableEntity follower, LivingEntity target, double speed, double range) {
            this.follower = follower;
            this.target = target;
            this.moveSpeed = speed;
            this.range = range;
            this.isFollowing = false;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (!this.target.isAlive()) {
                return false;
            }
            else {
                double distanceSquared = this.follower.getDistanceSq(this.target);
                boolean shouldExecute = !(distanceSquared < 10.0D) && !(distanceSquared > range);
                isFollowing = shouldExecute;
                if (isFollowing){
                    Entry.LOGGER.debug("Should follow");
                }
                return shouldExecute;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.delayCounter = 0;
            Entry.LOGGER.debug("Task started");
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.isFollowing = false;
            Entry.LOGGER.debug("Task reseted");
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (!isFollowing)
                return;

            if (--this.delayCounter <= 0) {
                this.delayCounter = DELAY;
                this.follower.getNavigator().tryMoveToEntityLiving(this.target, this.moveSpeed);
            }
        }
    }

    public static class ImitateLivingEntityGoal extends Goal {

        private AgeableEntity villager;
        private LivingEntity player;
        private boolean execute = false;

        public ImitateLivingEntityGoal(AgeableEntity villager, LivingEntity player) {

            this.villager = villager;
            this.player = player;
        }

        @Override
        public boolean shouldExecute() {
            return execute = true;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting();
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        @Override
        public void tick() {
            super.tick();

            if (!execute)
                return;

            villager.swingArm(Hand.MAIN_HAND);

            ItemStack playerHand = player.getHeldItemMainhand();
            Item playerItem = playerHand.getItem();
            Item villagerItem = null;
            if (playerItem instanceof AxeItem){
                villagerItem = Items.STONE_AXE;
            }
            else if (playerItem instanceof SwordItem){
                villagerItem = Items.STONE_SWORD;
            }
            else if (playerItem instanceof PickaxeItem){
                villagerItem = Items.STONE_PICKAXE;
            }

            if (villagerItem != null){
                villager.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(villagerItem));
                // villager.getDataManager().reg
                villager.setActiveHand(Hand.MAIN_HAND);

                BlockPos blockUnderVillager = villager.getPosition().add(0, -1, 0);
                Entry.LOGGER.debug(String.format("blockUnderVillager: %dX %dY %dZ", blockUnderVillager.getX(), blockUnderVillager.getY(), blockUnderVillager.getZ()));
                IFluidState ifluidstate = this.villager.world.getFluidState(blockUnderVillager);
                boolean flag = villager.world.setBlockState(blockUnderVillager, ifluidstate.getBlockState(), 11);
                BlockState state = villager.world.getBlockState(blockUnderVillager);
                Block block = state.getBlock();
                block.onPlayerDestroy(villager.world, blockUnderVillager, state);
                Entry.LOGGER.debug("Digged!");
                execute = false;
            }
            // PlayerController.clickBlock
        }
    }

    public static void hireVillager(ServerWorld world, UUID playerId, UUID villagerId) {
        PlayerEntity player = (PlayerEntity)world.getEntityByUuid(playerId);
        VillagerEntity villager = (VillagerEntity)world.getEntityByUuid(villagerId);
        villager.setCustomer(null);
        villager.goalSelector.addGoal(5, new ImitateLivingEntityGoal(villager, player));
        villager.goalSelector.addGoal(6, new FollowLivingEntityGoal(villager, player, 1D, 10000D));
    }
}
