package com.henrikstabell.actuallysnowmen;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;

public class Configuration {

    public static final ForgeConfigSpec CONFIG_SPEC;
    private static final Configuration CONFIG;

    public final BooleanValue shouldSnowGolemsDropPumpkin;
    public final BooleanValue canAddPumpkinToSnowGolem;
    public final BooleanValue shouldSnowGolemWithoutPumpkinBeSnowman;

    static
    {
        Pair<Configuration,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Configuration::new);

        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    Configuration(ForgeConfigSpec.Builder builder)
    {
        shouldSnowGolemsDropPumpkin = builder
                .comment("True if snow golems with carved pumpkins on their head should drop carved pumpkins on death, otherwise false.")
                .define("shouldSnowGolemsDropPumpkin", true);
        canAddPumpkinToSnowGolem = builder
                .comment("True if it should be possible to add a carved pumpkin to a snow golem that does not have a carved pumpkin by right clicking a carved pumpkin on a snow golem, otherwise false.")
                .define("canAddPumpkinToSnowGolem", true);
        shouldSnowGolemWithoutPumpkinBeSnowman = builder
                .comment("True if snow golems without pumpkins should be named 'Snowman', otherwise false")
                .define("shouldSnowGolemWithoutPumpkinBeSnowman", true);
    }

    public static boolean shouldSnowGolemsDropPumpkin() {
        return CONFIG.shouldSnowGolemsDropPumpkin.get();
    }

    public static boolean canAddPumpkinToSnowGolem() {
        return CONFIG.canAddPumpkinToSnowGolem.get();
    }

    public static boolean shouldSnowGolemWithoutPumpkinBeSnowman() {
        return CONFIG.shouldSnowGolemWithoutPumpkinBeSnowman.get();
    }

}
