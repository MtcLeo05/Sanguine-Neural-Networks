package com.leo.snn.compat.jei;

import dev.shadowsoffire.hostilenetworks.data.ModelTier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class VSRecipe implements Recipe<Container> {

    private final ItemStack model;
    private final int blood;
    private final int energy;

    public VSRecipe(ItemStack model, int blood, int energy) {
        this.model = model;
        this.blood = blood;
        this.energy = energy;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return model;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return model;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public ItemStack getOutput() {
        return model;
    }

    public int getBlood() {
        return blood;
    }

    public int getEnergy() {
        return energy;
    }

    public static class Type implements RecipeType<VSRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "output";
    }

}
