package com.henrikstabell.actuallysnowmen.integration;

import com.henrikstabell.actuallysnowmen.ActuallySnowmen;
import com.henrikstabell.actuallysnowmen.block.PumpkinLightBlock;
import com.henrikstabell.actuallysnowmen.block.entity.PumpkinLightBlockEntity;
import com.henrikstabell.actuallysnowmen.integration.overrides.WailaBlockOverrideProvider;
import com.henrikstabell.actuallysnowmen.integration.overrides.WailaEntityOverrideProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(id = ActuallySnowmen.MODID + ":waila_plugin")
public class ActuallySnowmenWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.addOverride(new WailaBlockOverrideProvider(), PumpkinLightBlock.class);
        registrar.addOverride(new WailaEntityOverrideProvider(), PumpkinLightBlockEntity.class);
    }
}
