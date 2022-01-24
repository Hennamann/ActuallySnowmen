package com.henrikstabell.actuallysnowmen.integration.overrides;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class WailaEntityOverrideProvider implements IEntityComponentProvider {

    @Override
    public @Nullable Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return EMPTY_ENTITY;
    }
}
