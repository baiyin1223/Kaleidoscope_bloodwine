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
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 罪痕绯酒。
 * 所有人获得 datamap 效果（品级1也有幸运+夜视+速度）。
 * 吸血鬼额外获得：恢复饮血值 + 防晒 + 生命提升 + 力量。
 */
public class SinmarkCrimsonItem extends BloodDrinkBlockItem {

    public SinmarkCrimsonItem(Block block) {
        super(block);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        // 所有人都获得 datamap 效果
        this.addDrinkEffect(stack, level, entity);

        // 吸血鬼额外效果
        if (VampirismCompat.isLoaded() && entity instanceof Player player && VampirismCompat.isVampire(player)) {
            int brewLevel = BottleBlockItem.getBrewLevel(stack);
            VampirismCompat.onDrinkSinmarkCrimson(player, stack, brewLevel);
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
            tooltip.add(Component.translatable("tooltip.kaleidoscope_bloodwine.sinmark_crimson")
                    .withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
