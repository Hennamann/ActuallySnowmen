package com.henrikstabell.actuallysnowmen.integration.overrides;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WailaBlockOverrideProvider implements IBlockComponentProvider {

    @Override
    public @Nullable BlockState getOverride(IBlockAccessor accessor, IPluginConfig config) {
        return EMPTY_BLOCK_STATE;
    }
}
