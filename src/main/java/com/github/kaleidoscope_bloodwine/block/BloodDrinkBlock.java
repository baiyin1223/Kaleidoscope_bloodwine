package com.github.kaleidoscope_bloodwine.block;

import com.github.kaleidoscope_bloodwine.blockentity.BloodDrinkBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.DrinkBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import com.github.ysbbbbbb.kaleidoscopetavern.item.DrinkBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 血酒方块。由于 BloodDrinkBlockEntity 不继承 DrinkBlockEntity，
 * 需要覆写所有涉及 instanceof DrinkBlockEntity 检查的方法。
 */
public class BloodDrinkBlock extends DrinkBlock {

    public BloodDrinkBlock(boolean irregular, int maxCount, VoxelShape... shapes) {
        super(irregular, maxCount, shapes);
    }

    public BloodDrinkBlock(int maxCount, VoxelShape... shapes) {
        this(false, maxCount, shapes);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BloodDrinkBlockEntity(pos, state);
    }

    /**
     * 堆叠酒瓶时存储物品到 BloodDrinkBlockEntity。
     */
    @Override
    public boolean tryIncreaseCount(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        int count = state.getValue(this.countProperty);
        if (count < this.maxCount) {
            if (level.getBlockEntity(pos) instanceof BloodDrinkBlockEntity be) {
                if (be.addItem(stack)) {
                    be.refresh();
                }
            }
            level.setBlockAndUpdate(pos, state.cycle(this.countProperty));
            return true;
        }
        return false;
    }

    /**
     * 空手右键取出物品。
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        // 手中有物品时，走父类逻辑（BottleBlock.use）
        if (!player.getItemInHand(hand).isEmpty()) {
            return super.use(state, level, pos, player, hand, hitResult);
        }

        // 空手：从 BloodDrinkBlockEntity 取出物品
        if (level.getBlockEntity(pos) instanceof BloodDrinkBlockEntity be) {
            ItemStack stack = be.removeItem();
            if (!stack.isEmpty()) {
                be.refresh();
                ItemHandlerHelper.giveItemToPlayer(player, stack);
                level.playSound(null, pos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS);
            }
        }

        int count = state.getValue(this.countProperty);
        if (count > 1) {
            level.setBlockAndUpdate(pos, state.setValue(this.countProperty, count - 1));
        } else {
            level.removeBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 投射物命中时获取最高酿造等级并生成药水云。
     */
    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (level.isClientSide) {
            super.onProjectileHit(level, state, hit, projectile);
            return;
        }

        BlockPos pos = hit.getBlockPos();
        if (level.getBlockEntity(pos) instanceof BloodDrinkBlockEntity be) {
            int maxBrewLevel = 0;
            for (ItemStack item : be.getItems()) {
                if (!item.isEmpty()) {
                    int brewLevel = BottleBlockItem.getBrewLevel(item);
                    if (brewLevel > maxBrewLevel) {
                        maxBrewLevel = brewLevel;
                    }
                }
            }

            if (maxBrewLevel > 0 && this.asItem() instanceof DrinkBlockItem drinkItem) {
                drinkItem.makeThrownPotion(level, pos.getX(), pos.getY(), pos.getZ(), maxBrewLevel, projectile.getOwner());
            }

            super.onProjectileHit(level, state, hit, projectile);
            return;
        }

        // 非血酒 BlockEntity，回退到父类
        super.onProjectileHit(level, state, hit, projectile);
    }

    /**
     * 方块破坏时掉落 BlockEntity 中存储的所有物品。
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BloodDrinkBlockEntity be) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : be.getItems()) {
                if (!stack.isEmpty()) {
                    stacks.add(stack.copy());
                }
            }
            return stacks;
        }
        // 非血酒 BlockEntity，回退到父类
        return super.getDrops(state, params);
    }
}
