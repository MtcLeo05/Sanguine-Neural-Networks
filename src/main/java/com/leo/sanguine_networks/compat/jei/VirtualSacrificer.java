package com.leo.sanguine_networks.compat.jei;

import com.leo.sanguine_networks.Config;
import com.leo.sanguine_networks.SanguineNeuralNetworks;
import com.leo.sanguine_networks.init.ModBlocks;
import dev.shadowsoffire.hostilenetworks.data.ModelTier;
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

public class VirtualSacrificer implements IRecipeCategory<VSRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(SanguineNeuralNetworks.MODID, "virtual_sacrificer/recipe");
    public static final ResourceLocation TEXTURE = new ResourceLocation(SanguineNeuralNetworks.MODID, "textures/gui/virtual_sacrificer_jei.png");
    public static final RecipeType<VSRecipe> RECIPE_TYPE = new RecipeType<>(UID, VSRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private ModelTier currentTier;

    public VirtualSacrificer(IGuiHelper guiHelper) {
        this.currentTier = ModelTier.FAULTY;
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ModBlocks.VIRTUAL_SACRIFICER.get().asItem().getDefaultInstance());
    }

    @Override
    public RecipeType<VSRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(SanguineNeuralNetworks.MODID + ".container.virtual_sacrificer");
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
    public void setRecipe(IRecipeLayoutBuilder builder, VSRecipe vsRecipe, IFocusGroup iFocusGroup) {
        builder.addSlot(
            RecipeIngredientRole.INPUT,
            61,
            1
        ).addItemStack(vsRecipe.getOutput());
    }

    private int changeCD = 0;
    private final int maxChangeCD = 120;

    @Override
    public void draw(VSRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        Component energy = Component.translatable("jei."+ SanguineNeuralNetworks.MODID + ".energy", recipe.getEnergy());
        guiGraphics.drawString(font, energy, 2, 44, 0xFFFF0000);

        changeCD++;
        if(changeCD >= maxChangeCD) {
            changeCD = 0;

            if(!currentTier.next().equals(currentTier)) {
                currentTier = currentTier.next();
            } else {
                currentTier = ModelTier.FAULTY;
            }
        }

        Component tier = currentTier.getComponent();
        float mult = Config.tiers.get(currentTier.ordinal());

        Component blood = Component.translatable("jei."+ SanguineNeuralNetworks.MODID + ".blood", recipe.getBlood() * mult);
        guiGraphics.drawString(font, blood, 2, 26, 0xFFFF0000);
        guiGraphics.drawString(font, tier, 2, 7, currentTier.color());
    }
}
