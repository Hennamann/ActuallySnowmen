package com.henrikstabell.actuallysnowmen;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("actuallysnowmen")
public class ActuallySnowmen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private BlockPattern pureSnowGolemBase;
    private final TextComponent componentTextSnowMan = new TextComponent("Snowman");

    public ActuallySnowmen() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.CONFIG_SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onSnowGolemDeath(LivingDeathEvent event) {
        if (Configuration.shouldSnowGolemsDropPumpkin()) {
            if (event.getEntity() instanceof SnowGolem) {
                if (((SnowGolem) event.getEntity()).hasPumpkin()) {
                    event.getEntity().getCommandSenderWorld().addFreshEntity(new ItemEntity(event.getEntity().getCommandSenderWorld(), event.getEntity().xo, event.getEntity().yo, event.getEntity().zo, new ItemStack(Items.CARVED_PUMPKIN, 1)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickSnowGolem(PlayerInteractEvent.EntityInteract event) {
        if (Configuration.canAddPumpkinToSnowGolem()) {
            if (event.getTarget() instanceof SnowGolem) {
                if (event.getPlayer().getMainHandItem().getItem() == Items.CARVED_PUMPKIN || event.getPlayer().getMainHandItem().getItem() == Items.JACK_O_LANTERN) {
                    if (!((SnowGolem) event.getTarget()).hasPumpkin()) {
                        if (!event.getPlayer().isCreative()) {
                            event.getPlayer().getMainHandItem().setCount(event.getEntityLiving().getMainHandItem().getCount() - 1);
                        }
                        ((SnowGolem) event.getTarget()).setPumpkin(true);
                        if (event.getTarget().getCustomName() == componentTextSnowMan) {
                            event.getTarget().setCustomName(null);
                            event.getTarget().setCustomNameVisible(false);
                        }
                    }
                } else if (Configuration.shouldSnowGolemWithoutPumpkinBeSnowman()) {
                    if (event.getPlayer().getMainHandItem().getItem() == Items.SHEARS) {
                        if (event.getTarget().getCustomName() == componentTextSnowMan || event.getTarget().getCustomName() == null) {
                            if (((SnowGolem) event.getTarget()).hasPumpkin()) {
                                event.getTarget().setCustomNameVisible(true);
                                event.getTarget().setCustomName(componentTextSnowMan);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSnowGolemCreation(BlockEvent.EntityPlaceEvent event) {
        Level level = event.getEntity().getCommandSenderWorld();
        BlockPos pos = event.getPos();
        BlockPattern.BlockPatternMatch snowGolemPattern = this.getOrCreatePureSnowGolem().find(level, pos);

        if (event.getPlacedBlock() == Blocks.SNOW_BLOCK.defaultBlockState()) {
            if (snowGolemPattern != null) {
                for (int i = 0; i < this.getOrCreatePureSnowGolem().getHeight(); ++i) {
                    BlockInWorld blockinworld = snowGolemPattern.getBlock(0, i, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }

                SnowGolem snowGolem = EntityType.SNOW_GOLEM.create(level);
                BlockPos blockPos = snowGolemPattern.getBlock(0, 2, 0).getPos();
                snowGolem.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                snowGolem.setPumpkin(false);
                if (Configuration.shouldSnowGolemWithoutPumpkinBeSnowman()) {
                    snowGolem.setCustomName(componentTextSnowMan);
                }
                level.addFreshEntity(snowGolem);

                for (ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, snowGolem.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, snowGolem);
                }

                for (int l = 0; l < this.getOrCreatePureSnowGolem().getHeight(); ++l) {
                    BlockInWorld blockInWorld = snowGolemPattern.getBlock(0, l, 0);
                    level.blockUpdated(blockInWorld.getPos(), Blocks.AIR);
                }
            }
        }
    }

    private BlockPattern getOrCreatePureSnowGolem() {
        if (this.pureSnowGolemBase == null) {
            this.pureSnowGolemBase = BlockPatternBuilder.start().aisle("#", "#", "#").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.pureSnowGolemBase;
    }
}
