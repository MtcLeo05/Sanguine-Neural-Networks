package com.leo.snn.compat.jei;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CatalystRecipe implements Recipe<Container> {

    private final Item item;
    private final float mult;
    private final int uses;

    public CatalystRecipe(Item item, float mult, int uses) {
        this.item = item;
        this.mult = mult;
        this.uses = uses;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return new ItemStack(item);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(item);
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

    public Item getItem() {
        return item;
    }

    public float getMult() {
        return mult;
    }

    public int getUses() {
        return uses;
    }

    public ItemStack getOutput(){
        return new ItemStack(item);
    }

    public static class Type implements RecipeType<CatalystRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "output";
    }

}
