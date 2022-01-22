package com.henrikstabell.actuallysnowmen;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final ForgeConfigSpec CONFIG_SPEC;
    private static final Config CONFIG;

    public final BooleanValue shouldSnowGolemsDropPumpkin;
    public final BooleanValue canAddPumpkinToSnowGolem;
    public final BooleanValue shouldSnowGolemWithoutPumpkinBeSnowman;
    public final BooleanValue canMakeSnowGolemFromPowderedSnow;

    static
    {
        Pair<Config,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);

        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    Config(ForgeConfigSpec.Builder builder)
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
        canMakeSnowGolemFromPowderedSnow = builder
                .comment("True if it should be possible to make snow golems using powdered snow, otherwise false")
                .define("canMakeSnowGolemFromPoweredSnow", true);

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

    public static boolean canMakeSnowGolemFromPowderedSnow() {
        return CONFIG.canMakeSnowGolemFromPowderedSnow.get();
    }

}
