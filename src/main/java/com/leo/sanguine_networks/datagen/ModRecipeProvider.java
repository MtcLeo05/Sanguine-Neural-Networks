package com.leo.sanguine_networks.datagen;

import com.leo.sanguine_networks.init.ModBlocks;
import com.leo.sanguine_networks.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                ModBlocks.VIRTUAL_SACRIFICER.get(),
                1
        )
        .pattern(" D ")
        .pattern("SOS")
        .pattern("BCB")
        .define('O', Blocks.OBSIDIAN)
        .define('S', BloodMagicItems.IMBUED_SLATE.get())
        .define('D', BloodMagicItems.DAGGER_OF_SACRIFICE.get())
        .define('C', Items.COMPARATOR)
        .define('B', BloodMagicItems.REAGENT_BINDING.get())
        .unlockedBy("hasItem", has(BloodMagicItems.IMBUED_SLATE.get()))
        .save(pWriter);

        ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                ModItems.WRENCH.get(),
                1
            )
            .pattern(" Rs")
            .pattern(" SR")
            .pattern("S  ")
            .define('R', Items.REDSTONE)
            .define('s', BloodMagicItems.SLATE.get())
            .define('S', Items.STICK)
            .unlockedBy("hasItem", has(BloodMagicItems.SLATE.get()))
            .save(pWriter, "wrench");
    }

}
