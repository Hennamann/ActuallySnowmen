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
    public final BooleanValue shouldSnowGolemWithPumpkinEmitLight;
    public final BooleanValue shouldSnowGolemsLeaveSnowBehind;

    public final IntValue snowGolemLightLevel;

    static
    {
        Pair<Config,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);

        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    Config(ForgeConfigSpec.Builder builder)
    {
        shouldSnowGolemsDropPumpkin = builder
                .comment("True if Snow Golems with carved pumpkins on their head should drop carved pumpkins on death, otherwise false.")
                .translation("config.actuallysnowmen.shouldSnowGolemsDropPumpkin")
                .define("shouldSnowGolemsDropPumpkin", true);
        canAddPumpkinToSnowGolem = builder
                .comment("True if it should be possible to add a carved pumpkin to a Snow Golem that does not have a carved pumpkin by right clicking a carved pumpkin on a snow golem, otherwise false.")
                .translation("config.actuallysnowmen.canAddPumpkinToSnowGolem")
                .define("canAddPumpkinToSnowGolem", true);
        shouldSnowGolemWithoutPumpkinBeSnowman = builder
                .comment("True if Snow Golems without pumpkins should be named 'Snowman', otherwise false")
                .translation("config.actuallysnowmen.shouldSnowGolemWithoutPumpkinBeSnowman")
                .define("shouldSnowGolemWithoutPumpkinBeSnowman", true);
        canMakeSnowGolemFromPowderedSnow = builder
                .comment("True if it should be possible to make Snow Golems using powdered snow, otherwise false")
                .translation("config.actuallysnowmen.canMakeSnowGolemFromPowderedSnow")
                .define("canMakeSnowGolemFromPoweredSnow", true);
        shouldSnowGolemWithPumpkinEmitLight = builder
                .comment("True if Snow Golems with pumpkins should emit light, otherwise false")
                .translation("config.actuallysnowmen.shouldSnowGolemWithPumpkinEmitLight")
                .define("shouldSnowGolemWithPumpkinEmitLight", true);
        shouldSnowGolemsLeaveSnowBehind = builder
                .comment("True if Snow Golems should not leave snow behind while walking, otherwise false")
                .translation("config.actuallysnowmen.shouldSnowGolemsLeaveSnowBehind")
                .define("shouldSnowGolemsLeaveSnowBehind", false);
        snowGolemLightLevel = builder
                .comment("Sets the light level emitted from Snow Golems, where 1 is minimum/weakest and 15 is maximum")
                .translation("config.actuallysnowmen.snowGolemLightLevel")
                .defineInRange("snowGolemLightLevel", 6, 1, 15);
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

    public static boolean shouldSnowGolemWithPumpkinEmitLight() {
        return CONFIG.shouldSnowGolemWithPumpkinEmitLight.get();
    }

    public static boolean shouldSnowGolemsLeaveSnowBehind() {
        return CONFIG.shouldSnowGolemsLeaveSnowBehind.get();
    }

    public static int snowGolemLightLevel() {
        return CONFIG.snowGolemLightLevel.get();
    }

}
