package com.github.kaleidoscope_bloodwine.blockentity;

import com.github.kaleidoscope_bloodwine.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class BloodDrinkBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public BloodDrinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DRINK_BE.get(), pos, state);
    }

    public boolean addItem(ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack.copyWithCount(1));
                return true;
            }
        }
        return false;
    }

    public ItemStack removeItem() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (!items.get(i).isEmpty()) {
                ItemStack stack = items.get(i);
                items.set(i, ItemStack.EMPTY);
                return stack.copyWithCount(1);
            }
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    /**
     * Consume up to {@code count} items from the inventory, starting from the last slot.
     * @return the number of items actually consumed
     */
    public int consumeItems(int count) {
        int consumed = 0;
        for (int i = items.size() - 1; i >= 0 && consumed < count; i--) {
            if (!items.get(i).isEmpty()) {
                items.set(i, ItemStack.EMPTY);
                consumed++;
            }
        }
        if (consumed > 0) {
            refresh();
        }
        return consumed;
    }

    public void refresh() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }
}
