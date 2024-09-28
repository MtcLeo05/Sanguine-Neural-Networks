package com.leo.snn.datagen;

import com.leo.snn.SanguineNeuralNetworks;
import com.leo.snn.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SanguineNeuralNetworks.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(
            ModBlocks.V_SACRIFICER.get()
        );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            ModBlocks.V_SACRIFICER.get()
        );
    }
}
