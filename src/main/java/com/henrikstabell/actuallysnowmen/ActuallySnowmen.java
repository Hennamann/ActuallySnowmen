package com.henrikstabell.actuallysnowmen;

import com.henrikstabell.actuallysnowmen.block.PumpkinLightBlock;
import com.henrikstabell.actuallysnowmen.block.entity.PumpkinLightBlockEntity;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ActuallySnowmen.MODID)
@Mod.EventBusSubscriber(modid = ActuallySnowmen.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ActuallySnowmen
{

    public static final String MODID = "actuallysnowmen";
    private static final Logger LOGGER = LogManager.getLogger();
    private BlockPattern pureSnowGolemFull;
    private BlockPattern purePowderedSnowGolemFull;
    private BlockPattern powderedSnowGolemFull;
    private final TextComponent componentTextSnowMan = new TextComponent("Snowman");

    public static Block PUMPKIN_LIGHT_BLOCK;
    public static BlockEntityType<PumpkinLightBlockEntity> PUMPKIN_LIGHT_BLOCK_ENTITYTYPE;

    public ActuallySnowmen() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> blockRegisterEvent) {

        PUMPKIN_LIGHT_BLOCK = (PumpkinLightBlock)(new PumpkinLightBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0F, 3600000.8F).noDrops().noOcclusion().randomTicks().lightLevel(PumpkinLightBlock.LIGHT_EMISSION)).setRegistryName(MODID, "pumpkin_light"));
        blockRegisterEvent.getRegistry().register(PUMPKIN_LIGHT_BLOCK);

    }

    @SubscribeEvent
    public static void registerBE(RegistryEvent.Register<BlockEntityType<?>> event) {
        PUMPKIN_LIGHT_BLOCK_ENTITYTYPE = BlockEntityType.Builder.of(PumpkinLightBlockEntity::new, PUMPKIN_LIGHT_BLOCK).build(null);
        PUMPKIN_LIGHT_BLOCK_ENTITYTYPE.setRegistryName(MODID, "pumpkin_light");
        event.getRegistry().register(PUMPKIN_LIGHT_BLOCK_ENTITYTYPE);
    }

    @SubscribeEvent
    public void onSnowGolemUpdate(LivingEvent.LivingUpdateEvent event) {
        BlockState blockState = PUMPKIN_LIGHT_BLOCK.defaultBlockState();
        blockState.setValue(BlockStateProperties.LEVEL, Config.snowGolemLightLevel());

        Level world = event.getEntityLiving().getCommandSenderWorld();
        BlockPos pos = event.getEntityLiving().eyeBlockPosition();

        if (Config.shouldSnowGolemWithPumpkinEmitLight()) {
            if (event.getEntityLiving() instanceof SnowGolem) {
                if (((SnowGolem) event.getEntityLiving()).hasPumpkin()) {
                    world.setBlockAndUpdate(pos, blockState);
                }
            }
        }
    }

    @SubscribeEvent
    public void onSnowGolemDeath(LivingDeathEvent event) {
        if (Config.shouldSnowGolemsDropPumpkin()) {
            if (event.getEntity() instanceof SnowGolem) {
                if (((SnowGolem) event.getEntity()).hasPumpkin()) {
                    event.getEntity().getCommandSenderWorld().addFreshEntity(new ItemEntity(event.getEntity().getCommandSenderWorld(), event.getEntity().xo, event.getEntity().yo, event.getEntity().zo, new ItemStack(Items.CARVED_PUMPKIN, 1)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickSnowGolem(PlayerInteractEvent.EntityInteract event) {
        if (Config.canAddPumpkinToSnowGolem()) {
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
                } else if (Config.shouldSnowGolemWithoutPumpkinBeSnowman()) {
                    if (event.getPlayer().getMainHandItem().getItem() == Items.SHEARS) {
                        if (!((SnowGolem) event.getTarget()).isLeashed()) {
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
    }

    @SubscribeEvent
    public void onSnowGolemCreation(BlockEvent.EntityPlaceEvent event) {
        Level level = event.getEntity().getCommandSenderWorld();
        BlockPos pos = event.getPos();
        SnowGolem snowGolem = EntityType.SNOW_GOLEM.create(level);
        BlockPattern.BlockPatternMatch pureSnowGolemPattern = this.getOrCreatePureSnowGolemFull().find(level, pos);
        BlockPattern.BlockPatternMatch purePowderedSnowGolemPattern = this.getOrCreatePurePowderedSnowGolemFull().find(level, pos);
        BlockPattern.BlockPatternMatch powderedSnowGolemPattern = this.getOrCreatePowderedSnowGolemFull().find(level, pos);

        if (event.getPlacedBlock() == Blocks.SNOW_BLOCK.defaultBlockState()) {
            if (pureSnowGolemPattern != null) {
                for (int i = 0; i < this.getOrCreatePureSnowGolemFull().getHeight(); ++i) {
                    BlockInWorld blockinworld = pureSnowGolemPattern.getBlock(0, i, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }

                BlockPos patternPos = pureSnowGolemPattern.getBlock(0, 2, 0).getPos();
                snowGolem.moveTo((double) patternPos.getX() + 0.5D, (double) patternPos.getY() + 0.05D, (double) patternPos.getZ() + 0.5D, 0.0F, 0.0F);
                snowGolem.setPumpkin(false);
                if (Config.shouldSnowGolemWithoutPumpkinBeSnowman()) {
                    snowGolem.setCustomName(componentTextSnowMan);
                }
                level.addFreshEntity(snowGolem);

                for (ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, snowGolem.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, snowGolem);
                }

                for (int l = 0; l < this.getOrCreatePureSnowGolemFull().getHeight(); ++l) {
                    BlockInWorld blockInWorld = pureSnowGolemPattern.getBlock(0, l, 0);
                    level.blockUpdated(blockInWorld.getPos(), Blocks.AIR);
                }
            }
        } else if (event.getPlacedBlock().getBlock() instanceof PowderSnowBlock && Config.canMakeSnowGolemFromPowderedSnow()) {
            if (purePowderedSnowGolemPattern != null) {
                for (int i = 0; i < this.getOrCreatePurePowderedSnowGolemFull().getHeight(); ++i) {
                    BlockInWorld blockinworld = purePowderedSnowGolemPattern.getBlock(0, i, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }

                BlockPos patternPos = purePowderedSnowGolemPattern.getBlock(0, 2, 0).getPos();
                snowGolem.moveTo((double) patternPos.getX() + 0.5D, (double) patternPos.getY() + 0.05D, (double) patternPos.getZ() + 0.5D, 0.0F, 0.0F);
                snowGolem.setPumpkin(false);
                if (Config.shouldSnowGolemWithoutPumpkinBeSnowman()) {
                    snowGolem.setCustomName(componentTextSnowMan);
                }
                level.addFreshEntity(snowGolem);

                for (ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, snowGolem.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, snowGolem);
                }

                for (int l = 0; l < this.getOrCreatePureSnowGolemFull().getHeight(); ++l) {
                    BlockInWorld blockInWorld = purePowderedSnowGolemPattern.getBlock(0, l, 0);
                    level.blockUpdated(blockInWorld.getPos(), Blocks.AIR);
                }
            }
        } else if (event.getPlacedBlock().getBlock() instanceof CarvedPumpkinBlock && Config.canMakeSnowGolemFromPowderedSnow()) {
            if (powderedSnowGolemPattern != null) {
                for (int i = 0; i < this.getOrCreatePowderedSnowGolemFull().getHeight(); ++i) {
                    BlockInWorld blockinworld = powderedSnowGolemPattern.getBlock(0, i, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }

                BlockPos patternPos = powderedSnowGolemPattern.getBlock(0, 2, 0).getPos();
                snowGolem.moveTo((double) patternPos.getX() + 0.5D, (double) patternPos.getY() + 0.05D, (double) patternPos.getZ() + 0.5D, 0.0F, 0.0F);
                level.addFreshEntity(snowGolem);

                for (ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, snowGolem.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, snowGolem);
                }

                for (int l = 0; l < this.getOrCreatePowderedSnowGolemFull().getHeight(); ++l) {
                    BlockInWorld blockInWorld = powderedSnowGolemPattern.getBlock(0, l, 0);
                    level.blockUpdated(blockInWorld.getPos(), Blocks.AIR);
                }
            }
        }
    }

    private BlockPattern getOrCreatePureSnowGolemFull() {
        if (this.pureSnowGolemFull == null) {
            this.pureSnowGolemFull = BlockPatternBuilder.start().aisle("#", "#", "#").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.pureSnowGolemFull;
    }

    private BlockPattern getOrCreatePurePowderedSnowGolemFull() {
        if (this.purePowderedSnowGolemFull == null) {
            this.purePowderedSnowGolemFull = BlockPatternBuilder.start().aisle("#", "#", "#").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.POWDER_SNOW))).build();
        }
        return this.purePowderedSnowGolemFull;
    }

    private BlockPattern getOrCreatePowderedSnowGolemFull() {
        if (this.powderedSnowGolemFull == null) {
            this.powderedSnowGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.CARVED_PUMPKIN))).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.POWDER_SNOW))).build();
        }
        return this.powderedSnowGolemFull;
    }
}
