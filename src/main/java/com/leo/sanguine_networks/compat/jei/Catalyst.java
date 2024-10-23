package com.leo.sanguine_networks.compat.jei;

import com.leo.sanguine_networks.SanguineNeuralNetworks;
import com.leo.sanguine_networks.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Catalyst implements IRecipeCategory<CatalystRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(SanguineNeuralNetworks.MODID, "virtual_sacrificer/catalyst");
    public static final ResourceLocation TEXTURE = new ResourceLocation(SanguineNeuralNetworks.MODID, "textures/gui/catalyst_jei.png");
    public static final RecipeType<CatalystRecipe> RECIPE_TYPE = new RecipeType<>(UID, CatalystRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public Catalyst(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ModBlocks.VIRTUAL_SACRIFICER.get().asItem().getDefaultInstance());
    }

    @Override
    public RecipeType<CatalystRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(SanguineNeuralNetworks.MODID + ".container.vsacrificer.catalyst");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CatalystRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(
            RecipeIngredientRole.INPUT,
            50,
            1
        ).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(CatalystRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        Component energy = Component.translatable("jei."+ SanguineNeuralNetworks.MODID + ".mult", recipe.getMult());
        guiGraphics.drawString(font, energy, 2, 44, 0xFFFF0000);

        Component blood = Component.translatable("jei."+ SanguineNeuralNetworks.MODID + ".uses", recipe.getUses());
        guiGraphics.drawString(font, blood, 2, 26, 0xFFFF0000);
    }
}
