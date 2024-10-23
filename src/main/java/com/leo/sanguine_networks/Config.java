package com.leo.sanguine_networks;

import com.leo.sanguine_networks.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = SanguineNeuralNetworks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue V_SACRIFICER_SPEED = BUILDER
        .comment("How much time in ticks should the virtual sacrificer wait before producing blood [100]")
        .defineInRange("sacrificer_speed", 100, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue V_SACRIFICER_ENERGY = BUILDER
        .comment("The energy capacity of the virtual sacrificer [1000000]")
        .defineInRange("sacrificer_energy", 1000000, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue ITERATION_DATA = BUILDER
        .comment("How much data to give to the model each iteration [1]")
        .comment("Set to 0 to disable")
        .defineInRange("sacrificer_data", 1, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue FAULTY_DATA = BUILDER
        .comment("Should data be given to faulty models? [true]")
        .define("faulty_data", true);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CATALYST_CONFIG = BUILDER
        .comment("A list of catalyst that can be used to increase blood produced")
        .comment("Each entry must follow this syntax: item_id#multiplier#uses")
        .comment("The item id should include the mod id and must be a valid item, the multiplier can be a floating point, the uses cannot")
        .defineList("catalysts", List.of(
            "hostilenetworks:overworld_prediction#1.5#10",
            "hostilenetworks:nether_prediction#1.75#25",
            "hostilenetworks:end_prediction#2#50",
            "hostilenetworks:twilight_prediction#2.25#50",
            "minecraft:nether_star#5#1000"
        ), Config::validateCatalyst);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MODEL_CONFIG = BUILDER
        .comment("A list of models that can be used to produce blood")
        .comment("Each entry must follow this syntax: entity_id#blood#energy")
        .comment("The item id should include the mod id and must be a valid model, the blood and the energy are ints, the blood is in mb and the energy is RF/t")
        .defineList("models", List.of(
            "hostilenetworks:blaze#250#1000",
            "hostilenetworks:wither_skeleton#250#1000",
            "hostilenetworks:witch#250#1000",
            "hostilenetworks:vindicator#250#1000",
            "hostilenetworks:evoker#250#1000",
            "hostilenetworks:hoglin#250#1000",
            "hostilenetworks:enderman#250#1000",
            "hostilenetworks:shulker#250#1000",
            "hostilenetworks:ghast#250#1000",
            "hostilenetworks:squid#75#100",
            "hostilenetworks:chicken#75#100",
            "hostilenetworks:cod#75#100",
            "hostilenetworks:cow#75#100",
            "hostilenetworks:pig#75#100",
            "hostilenetworks:rabbit#75#100",
            "hostilenetworks:sheep#75#100",
            "hostilenetworks:polar_bear#75#100",
            "hostilenetworks:guardian#75#100",
            "hostilenetworks:mooshroom#75#100",
            "hostilenetworks:glow_squid#75#100",
            "hostilenetworks:zombie#125#250",
            "hostilenetworks:zombified_piglin#125#250",
            "hostilenetworks:skeleton#125#250",
            "hostilenetworks:slime#125#250",
            "hostilenetworks:magma_cube#125#250",
            "hostilenetworks:snow_golem#125#250",
            "hostilenetworks:spider#125#250",
            "hostilenetworks:creeper#125#250",
            "hostilenetworks:drowned#125#250",
            "hostilenetworks:phantom#125#250",
            "hostilenetworks:wither#1000#2500",
            "hostilenetworks:ender_dragon#1000#2500",
            "hostilenetworks:elder_guardian#1000#2500",
            "hostilenetworks:iron_golem#1000#2500",
            "hostilenetworks:warden#1500#3000"
        ), Config::validateModels);

    private static final ForgeConfigSpec.ConfigValue<List<? extends Double>> TIER_MODIFIER = BUILDER
        .comment("A list of floats that defines how much blood should each tier of data generates [0, 0.5, 1, 1.5, 3]")
        .comment("The list must remain with a length of 5, each missing entry is filled with a 0 and additional entries will be ignored")
        .comment("Each entry defines the modifier for: Faulty, Basic, Advanced, Superior, Self-Aware")
        .defineList("tiers", List.of(0d, 0.5d, 1d, 1.5d, 3d), Config::validateTiers);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int sacrificerSpeed;
    public static int sacrificerEnergy;
    public static int sacrificerData;

    public static boolean faultyData;

    public static Map<Item, Pair<Float, Integer>> catalysts;
    public static Map<String, Pair<Integer, Integer>> models;

    public static List<Float> tiers;

    private static boolean validateCatalyst(final Object obj) {
        return obj instanceof final String itemName && itemName.matches("^[a-z0-9_-]+:[a-z0-9_-]+#[0-9]+[.]?[0-9]*#[0-9]+");
    }

    private static boolean validateModels(final Object obj) {
        return obj instanceof final String itemName && itemName.matches("^[a-z0-9_-]+:[a-z0-9_-]+#[0-9]+?#[0-9]+");
    }

    private static boolean validateTiers(final Object obj) {
        return obj instanceof final Double d && d >= 0;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        sacrificerSpeed = V_SACRIFICER_SPEED.get();
        sacrificerEnergy = V_SACRIFICER_ENERGY.get();
        sacrificerData = ITERATION_DATA.get();

        faultyData = FAULTY_DATA.get();

        catalysts = new HashMap<>();
        models = new HashMap<>();
        tiers = new ArrayList<>();

        for (String string : CATALYST_CONFIG.get()) {
            String[] splits = string.split("#");
            ResourceLocation itemID = new ResourceLocation(splits[0]);
            float multiplier = Float.parseFloat(splits[1]);
            int uses = Integer.parseInt(splits[2]);

            if(ForgeRegistries.ITEMS.getValue(itemID) == null) continue;

            Item item = ForgeRegistries.ITEMS.getValue(itemID);

            catalysts.put(
                item,
                Pair.of(multiplier, uses)
            );
        }

        for (String string : MODEL_CONFIG.get()) {
            String[] splits = string.split("#");
            int blood = Integer.parseInt(splits[1]);
            int energy = Integer.parseInt(splits[2]);

            blood = Math.abs(blood);
            energy = Math.abs(energy);

            models.put(
                splits[0],
                Pair.of(blood, energy)
            );
        }

        for (Double d : TIER_MODIFIER.get()) {
            tiers.add((float) Math.abs(d));
        }

        while(tiers.size() < 5){
            tiers.add(0f);
        }
    }
}
