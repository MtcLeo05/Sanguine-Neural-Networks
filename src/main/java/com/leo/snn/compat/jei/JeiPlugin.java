package com.leo.snn.compat.jei;

import com.leo.snn.Config;
import com.leo.snn.SanguineNeuralNetworks;
import com.leo.snn.client.screen.VSacrificerScreen;
import com.leo.snn.init.ModBlockEntities;
import com.leo.snn.init.ModBlocks;
import dev.shadowsoffire.hostilenetworks.Hostile;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    List<VSacrificer> tiers = new ArrayList<>();

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SanguineNeuralNetworks.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new VSacrificer(registration.getJeiHelpers().getGuiHelper()),
            new Catalyst(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<VSRecipe> vsRecipes = new ArrayList<>();
        Config.models.forEach((model, values) -> {
            ItemStack stack = new ItemStack(Hostile.Items.DATA_MODEL.get());
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag modelData = new CompoundTag();
            modelData.putString("id", model);
            tag.put("data_model", modelData);
            stack.setTag(tag);

            VSRecipe recipe = new VSRecipe(
                stack,
                values.first,
                values.second
            );

            vsRecipes.add(recipe);
        });

        registration.addRecipes(
            VSacrificer.RECIPE_TYPE,
            vsRecipes
        );


        List<CatalystRecipe> catalystRecipes = new ArrayList<>();
        Config.catalysts.forEach((item, values) -> {
            CatalystRecipe recipe = new CatalystRecipe(item, values.first, values.second);
            catalystRecipes.add(recipe);
        });

        registration.addRecipes(
            Catalyst.RECIPE_TYPE,
            catalystRecipes
        );
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
            ModBlocks.V_SACRIFICER.get().asItem().getDefaultInstance(),
            VSacrificer.RECIPE_TYPE
        );

        registration.addRecipeCatalyst(
            ModBlocks.V_SACRIFICER.get().asItem().getDefaultInstance(),
            Catalyst.RECIPE_TYPE
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
            VSacrificerScreen.class,
            40,
            40,
            113,
            9,
            VSacrificer.RECIPE_TYPE
        );
    }
}
