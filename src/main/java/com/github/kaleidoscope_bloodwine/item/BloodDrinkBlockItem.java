package com.github.kaleidoscope_bloodwine.item;

import com.github.kaleidoscope_bloodwine.blockentity.BloodDrinkBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.item.DrinkBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 覆写 updateCustomBlockEntityTag() 处理首次放置时物品存储。
 * 主模组的 DrinkBlockItem 只检查 instanceof DrinkBlockEntity，
 * 而 BloodDrinkBlockEntity 不是其子类，需要在此补充处理。
 */
public class BloodDrinkBlockItem extends DrinkBlockItem {

    public BloodDrinkBlockItem(Block block) {
        super(block);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BloodDrinkBlockEntity bloodDrink) {
            if (bloodDrink.addItem(stack)) {
                bloodDrink.refresh();
            }
            // 调用 BlockItem 的 updateCustomBlockEntityTag（跳过 DrinkBlockItem 中对 DrinkBlockEntity 的重复检查）
            return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        }
        // 非血酒方块，回退到父类处理
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}
