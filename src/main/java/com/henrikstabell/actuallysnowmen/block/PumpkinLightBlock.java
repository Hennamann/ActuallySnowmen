package com.henrikstabell.actuallysnowmen.block;

import com.henrikstabell.actuallysnowmen.ActuallySnowmen;
import com.henrikstabell.actuallysnowmen.block.entity.PumpkinLightBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PumpkinLightBlock extends LightBlock implements EntityBlock {

    public PumpkinLightBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == ActuallySnowmen.PUMPKIN_LIGHT_BLOCK_ENTITYTYPE ? PumpkinLightBlockEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ActuallySnowmen.PUMPKIN_LIGHT_BLOCK_ENTITYTYPE.create(pos, state);
    }
}
