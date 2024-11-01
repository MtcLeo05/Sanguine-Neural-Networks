package com.leo.sanguine_networks.compat.jei;

import com.leo.sanguine_networks.Config;
import com.leo.sanguine_networks.SanguineNeuralNetworks;
import com.leo.sanguine_networks.client.screen.VSacrificerScreen;
import com.leo.sanguine_networks.init.ModBlocks;
import com.leo.sanguine_networks.recipe.CatalystRecipe;
import com.leo.sanguine_networks.recipe.ModelRecipe;
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

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SanguineNeuralNetworks.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new VirtualSacrificer(registration.getJeiHelpers().getGuiHelper()),
            new Catalyst(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ModelRecipe> bloodRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModelRecipe.Type.INSTANCE);

        registration.addRecipes(
            VirtualSacrificer.RECIPE_TYPE,
            bloodRecipes
        );

        List<CatalystRecipe> catalystRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CatalystRecipe.Type.INSTANCE);

        registration.addRecipes(
            Catalyst.RECIPE_TYPE,
            catalystRecipes
        );
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
            ModBlocks.VIRTUAL_SACRIFICER.get().asItem().getDefaultInstance(),
            VirtualSacrificer.RECIPE_TYPE
        );

        registration.addRecipeCatalyst(
            ModBlocks.VIRTUAL_SACRIFICER.get().asItem().getDefaultInstance(),
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
            VirtualSacrificer.RECIPE_TYPE
        );
    }
}
