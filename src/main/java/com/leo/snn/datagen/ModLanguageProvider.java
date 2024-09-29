package com.leo.snn.datagen;

import com.leo.snn.SanguineNeuralNetworks;
import com.leo.snn.init.ModBlocks;
import com.leo.snn.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, SanguineNeuralNetworks.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(SanguineNeuralNetworks.MODID + ".itemGroup.main", "Sanguine Neural Networks");

        this.add(
            ModBlocks.V_SACRIFICER.get(), "Virtual Sacrificer"
        );

        this.add(
            ModItems.WRENCH.get(), "Wrench"
        );

        this.add(
            SanguineNeuralNetworks.MODID + ".container.vsacrificer",
            "Virtual Sacrificer"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".energy",
            "Energy: %d / %d"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".consume",
            "Consuming: %d RF/t"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".uses",
            "Catalyst Uses: %d / %d"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".produce",
            "Blood / Operation: %d mb"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".noAltar",
            "Blood altar is missing!"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".noModel",
            "Missing data model!"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".cModifier",
            "Mult: %dx"
        );

        this.add(
            "gui." + SanguineNeuralNetworks.MODID + ".aModifier",
            "Altar: %dx"
        );

        this.add(
            "item." + SanguineNeuralNetworks.MODID + ".savedAltar",
            "Saved Blood Altar Pos: %d x, %d y, %d z"
        );

        this.add(
            "item." + SanguineNeuralNetworks.MODID + ".emptyAltar",
            "Blood Altar Pos is empty, select an altar before"
        );

        this.add(
            "item." + SanguineNeuralNetworks.MODID + ".setSacrificer",
            "Successfully saved blood altar in the virtual sacrificer"
        );

        this.add(
            "item." + SanguineNeuralNetworks.MODID + ".invalidPos",
            "Position is not a valid selection: %d x, %d y, %d z"
        );

        this.add(
            "item." + SanguineNeuralNetworks.MODID + ".wandUse",
            "First shift r-click on the Blood Altar, then shift r-click on the Virtual Sacrificer"
        );

        this.add(
            "jei."+ SanguineNeuralNetworks.MODID + ".blood",
            "Blood: %dmb"
        );

        this.add(
            "jei."+ SanguineNeuralNetworks.MODID + ".energy",
            "Energy: %dRF/t"
        );

        this.add(
            "jei."+ SanguineNeuralNetworks.MODID + ".mult",
            "Mult: %dx"
        );

        this.add(
            "jei."+ SanguineNeuralNetworks.MODID + ".uses",
            "Uses: %d"
        );

        this.add(
            "snn.container.vsacrificer.catalyst",
            "Virtual Sacrificer Catalysts"
        );
    }


    /**
     * Capitalizes first letter of a string
     *
     * @param input the string to capitalize e.g. "alpha"
     * @return the string capitalized e.g. "Alpha"
     */
    public static String cFL(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}