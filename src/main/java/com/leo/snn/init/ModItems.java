package com.leo.snn.init;

import com.leo.snn.SanguineNeuralNetworks;
import com.leo.snn.item.WrenchItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SanguineNeuralNetworks.MODID);


    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
        () -> new WrenchItem(
            new Item.Properties()
        )
    );
}
