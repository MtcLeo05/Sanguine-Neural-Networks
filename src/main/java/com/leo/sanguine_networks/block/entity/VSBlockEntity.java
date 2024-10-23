package com.leo.sanguine_networks.block.entity;

import com.leo.sanguine_networks.Config;
import com.leo.sanguine_networks.SanguineNeuralNetworks;
import com.leo.sanguine_networks.init.ModBlockEntities;
import com.leo.sanguine_networks.menu.VSacrificerMenu;
import com.leo.sanguine_networks.util.Pair;
import dev.shadowsoffire.hostilenetworks.data.DataModel;
import dev.shadowsoffire.hostilenetworks.data.ModelTier;
import dev.shadowsoffire.hostilenetworks.item.DataModelItem;
import dev.shadowsoffire.placebo.cap.ModifiableEnergyStorage;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.tile.TileAltar;

public class VSBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            VSBlockEntity.this.sync();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch(slot){
                case 0 -> stack.getItem() instanceof DataModelItem && Config.models.containsKey(stack.getOrCreateTagElement("data_model").getString("id"));
                case 1 -> Config.catalysts.containsKey(stack.getItem());
                default -> true;
            };

        }
    };
    private ModifiableEnergyStorage energyStorage;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.empty();

    private int catalystUses = 0, maxCatalystUses = 0;
    private int progress = 0, maxProgress = 0;
    private int toProduce = 0;
    private float catalystMult = 0f, altarMultiplier;
    private boolean hasModel = false;

    private BlockPos altarPos;
    TileAltar bloodAltar;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex){
                case 0 -> VSBlockEntity.this.energyStorage.getEnergyStored();
                case 1 -> VSBlockEntity.this.energyStorage.getMaxEnergyStored();
                case 2 -> catalystUses;
                case 3 -> maxCatalystUses;
                case 4 -> progress;
                case 5 -> maxProgress;
                case 6 -> toProduce;
                case 7 -> (int) (catalystMult * 1000);
                case 8 -> bloodAltar != null ? 1: 0;
                case 9 -> hasModel ? 1: 0;
                case 10 -> (int) (altarMultiplier * 1000);
                default -> -1;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {}

        @Override
        public int getCount() {
            return 11;
        }
    };

    public VSBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.V_SACRIFICER_BE.get(), pPos, pBlockState);
        energyStorage  = new ModifiableEnergyStorage(Config.sacrificerEnergy, Config.sacrificerEnergy, 0, 0);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(SanguineNeuralNetworks.MODID + ".container.vsacrificer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new VSacrificerMenu(i, inventory, this, containerData);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        if(cap == ForgeCapabilities.ENERGY){
            return lazyEnergyStorage.cast();
        }

        return super.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        if(cap == ForgeCapabilities.ENERGY){
            return lazyEnergyStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyStorage = LazyOptional.of(() -> energyStorage);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemHandler.serializeNBT());

        pTag.putInt("energy", energyStorage.getEnergyStored());

        pTag.putInt("catalystUses", catalystUses);
        pTag.putInt("maxCatalystUses", maxCatalystUses);

        pTag.putFloat("catalystMult", catalystMult);
        pTag.putInt("toProduce", toProduce);

        pTag.putInt("progress", progress);
        pTag.putInt("maxProgress", maxProgress);

        if(altarPos != null){
            pTag.put("altarPos", NbtUtils.writeBlockPos(altarPos));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        CompoundTag inv = pTag.getCompound("inventory");
        itemHandler.deserializeNBT(inv);

        energyStorage.setEnergy(pTag.getInt("energy"));

        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyStorage = LazyOptional.of(() -> energyStorage);

        catalystUses = pTag.getInt("catalystUses");
        maxCatalystUses = pTag.getInt("maxCatalystUses");

        catalystMult = pTag.getFloat("catalystMult");
        toProduce = pTag.getInt("toProduce");

        progress = pTag.getInt("progress");
        maxProgress = pTag.getInt("maxProgress");

        if(pTag.contains("altarPos")){
            altarPos = NbtUtils.readBlockPos(pTag.getCompound("altarPos"));
        }
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyStorage.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(getLevel(), getBlockPos(), container);
    }

    public void tick() {
        if(altarPos != null && level.getBlockEntity(altarPos) instanceof TileAltar altar){
            bloodAltar = altar;
        } else {
            bloodAltar = null;
        }

        hasModel = !getModelStack().isEmpty();

        if(maxProgress != Config.sacrificerSpeed) maxProgress = Config.sacrificerSpeed;

        boolean hasCatalyst = catalystUses > 0;

        if(!getCatalystStack().isEmpty() && !hasCatalyst) {
            maxCatalystUses = getCatalystFromStack(getCatalystStack()).second;
            catalystMult = getCatalystFromStack(getCatalystStack()).first;
            catalystUses = maxCatalystUses;
            getCatalystStack().shrink(1);
            sync();
        }

        if(!hasModel) {
            progress = 0;
            toProduce = 0;
            sync();
            return;
        }

        if (!hasCatalyst && catalystUses <= 0) {
            catalystMult = 1;
        }

        toProduce = (int) (getBloodFromStack(getModelStack()) * catalystMult);

        if(energyStorage.getEnergyStored() < getRFTick()) {
            sync();
            return;
        }

        if(bloodAltar == null) {
            sync();
            return;
        }

        altarMultiplier = 1 + bloodAltar.getSacrificeMultiplier();
        toProduce = (int) (toProduce * altarMultiplier);

        if(bloodAltar.getCurrentBlood() + toProduce >= bloodAltar.getCapacity()) {
            sync();
            return;
        }

        energyStorage.setEnergy(energyStorage.getEnergyStored() - getRFTick());
        progress++;

        if(progress < maxProgress) {
            sync();
            return;
        }

        if(hasCatalyst) {
            catalystUses--;
        }

        progress = 0;

        bloodAltar.fillMainTank(toProduce);

        int data = DataModelItem.getData(getModelStack());

        DataModel model = DataModelItem.getStoredModel(getModelStack()).get();
        ModelTier tier = ModelTier.getByData(model, data);

        if(tier != ModelTier.FAULTY || Config.faultyData) {
            data += Config.sacrificerData;
        }

        DataModelItem.setData(getModelStack(), data);
        sync();
    }

    public void sync(){
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public Pair<Float, Integer> getCatalystFromStack(ItemStack stack){
        return Config.catalysts.get(stack.getItem());
    }

    public ItemStack getModelStack(){
        return itemHandler.getStackInSlot(0);
    }

    public Pair<Integer, Integer> getModelFromStack(ItemStack stack){
        String mobId = stack.getOrCreateTagElement("data_model").getString("id");

        return getModelStack().isEmpty() || !Config.models.containsKey(mobId) ? Pair.of(0, 0): Config.models.get(mobId);
    }

    public int getBloodFromStack(ItemStack stack){
        int blood = getModelFromStack(stack).first;

        DynamicHolder<DataModel> model = DataModelItem.getStoredModel(stack);
        ModelTier tier = ModelTier.getByData(model, DataModelItem.getData(stack));

        blood *= Config.tiers.get(tier.ordinal());

        return blood;
    }

    public ItemStack getCatalystStack(){
        return itemHandler.getStackInSlot(1);
    }

    public ItemStackHandler getInventory() {
        return itemHandler;
    }

    public int getRFTick(){
        return getModelFromStack(getModelStack()).second;
    }

    public void setBloodAltar(BlockPos pos){
        TileAltar altar = (TileAltar) level.getBlockEntity(pos);
        altarPos = pos;
        bloodAltar = altar;
    }
}
