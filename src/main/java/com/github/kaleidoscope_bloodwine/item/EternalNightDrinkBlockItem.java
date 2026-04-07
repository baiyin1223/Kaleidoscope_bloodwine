package com.github.kaleidoscope_bloodwine.item;

import com.github.kaleidoscope_bloodwine.compat.VampirismCompat;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 永夜深红。
 * 对吸血鬼提供满饮血值恢复、生命提升和防晒效果。
 */
public class EternalNightDrinkBlockItem extends BloodDrinkBlockItem {

    public EternalNightDrinkBlockItem(Block block) {
        super(block);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 统计触发
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        // 所有人都获得 datamap 效果
        this.addDrinkEffect(stack, level, entity);

        // 吸血鬼额外效果
        if (VampirismCompat.isLoaded() && entity instanceof Player player && VampirismCompat.isVampire(player)) {
            int brewLevel = BottleBlockItem.getBrewLevel(stack);
            VampirismCompat.onDrinkEternalNight(player, stack, brewLevel);
        }

        if (entity instanceof Player player && !player.isCreative()) {
            stack.shrink(1);
        }
        return returnContainerToEntity(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (VampirismCompat.isLoaded()) {
            tooltip.add(Component.translatable("tooltip.kaleidoscope_bloodwine.eternal_night_crimson")
                    .withStyle(ChatFormatting.DARK_RED));
        }
    }
}
