package com.leo.snn.item;

import com.leo.snn.SanguineNeuralNetworks;
import com.leo.snn.block.entity.VSBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import wayoftime.bloodmagic.common.tile.TileAltar;

public class WrenchItem extends Item {
    public WrenchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();

        if(level.isClientSide) {
            return InteractionResult.CONSUME;
        }

        BlockPos pos = pContext.getClickedPos();
        ItemStack stack = pContext.getItemInHand();
        Player player = pContext.getPlayer();

        BlockEntity be = level.getBlockEntity(pos);
        CompoundTag tag = stack.getOrCreateTag();

        if(be instanceof TileAltar) {
            tag.put("altar", NbtUtils.writeBlockPos(pos));
            player.displayClientMessage(
                Component.translatable("item." + SanguineNeuralNetworks.MODID + ".savedAltar", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.DARK_RED),
                true
            );
            stack.setTag(tag);
            return InteractionResult.CONSUME;
        }

        if(be instanceof VSBlockEntity vs) {
            if(!tag.contains("altar")) {
                player.displayClientMessage(
                    Component.translatable("item." + SanguineNeuralNetworks.MODID + ".emptyAltar").withStyle(ChatFormatting.DARK_RED),
                    true
                );
                return InteractionResult.CONSUME;
            }

            BlockPos altarPos = NbtUtils.readBlockPos(tag.getCompound("altar"));
            vs.setBloodAltar(altarPos);
            player.displayClientMessage(
                Component.translatable("item." + SanguineNeuralNetworks.MODID + ".setSacrificer").withStyle(ChatFormatting.DARK_RED),
                true
            );
            return InteractionResult.CONSUME;
        }

        player.displayClientMessage(
            Component.translatable("item." + SanguineNeuralNetworks.MODID + ".invalidPos", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.DARK_RED),
            true
        );

        return InteractionResult.CONSUME;
    }
}
