package com.henrikstabell.actuallysnowmen.block.entity;

import com.henrikstabell.actuallysnowmen.ActuallySnowmen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class PumpkinLightBlockEntity extends BlockEntity {

    public PumpkinLightBlockEntity(BlockPos pos, BlockState state) {
        super(ActuallySnowmen.PUMPKIN_LIGHT_BLOCK_ENTITYTYPE, pos, state);
    }

    public static <T extends BlockEntity> void tick(Level world, BlockPos pos, BlockState state, T be) {
        List<SnowGolem> nearbySnowGolems = world.getEntitiesOfClass(SnowGolem.class, new AABB(pos).inflate(2));
        nearbySnowGolems.removeIf(snowGolem -> !snowGolem.hasPumpkin());

        if ((long) nearbySnowGolems.size() == 0) {
            world.removeBlock(pos, false);
        }
    }
}
