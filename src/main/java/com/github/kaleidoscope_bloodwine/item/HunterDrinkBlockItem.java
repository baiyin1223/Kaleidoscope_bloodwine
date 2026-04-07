package com.github.kaleidoscope_bloodwine.item;

import com.github.kaleidoscope_bloodwine.compat.VampirismCompat;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 猎人饮品。
 * 对吸血鬼造成负面效果，对猎人提供额外抗性加成。
 */
public class HunterDrinkBlockItem extends BloodDrinkBlockItem {

    public HunterDrinkBlockItem(Block block) {
        super(block);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 统计触发
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        // 根据 Vampirism 阵营决定效果
        if (VampirismCompat.isLoaded() && entity instanceof Player player) {
            int brewLevel = BottleBlockItem.getBrewLevel(stack);

            if (VampirismCompat.isVampire(player)) {
                // 吸血鬼：不给正常效果，改为中毒+反胃
                VampirismCompat.applyHunterDrinkVampireDebuff(player, brewLevel);
            } else if (VampirismCompat.isHunter(player)) {
                // 猎人：正常效果 + 额外抗性提升
                this.addDrinkEffect(stack, level, entity);
                applyHunterBonus(level, entity, brewLevel);
            } else {
                // 普通人类：正常效果
                this.addDrinkEffect(stack, level, entity);
            }
        } else {
            // Vampirism 未加载：正常效果
            this.addDrinkEffect(stack, level, entity);
        }

        if (entity instanceof Player player && !player.isCreative()) {
            stack.shrink(1);
        }
        return returnContainerToEntity(stack, level, entity);
    }

    /**
     * 给猎人玩家应用额外的抗性提升效果。
     */
    private void applyHunterBonus(Level level, LivingEntity entity, int brewLevel) {
        if (brewLevel < 3 || level.isClientSide) {
            return;
        }

        int amplifier;
        int duration;

        switch (brewLevel) {
            case 3 -> {
                amplifier = 0;
                duration = 10;
            }
            case 4 -> {
                amplifier = 1;
                duration = 30;
            }
            case 5 -> {
                amplifier = 2;
                duration = 60;
            }
            case 6 -> {
                amplifier = 3;
                duration = 100;
            }
            case 7 -> {
                amplifier = 5;
                duration = 150;
            }
            default -> {
                return;
            }
        }

        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration * 20, amplifier, false, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        // 仅在 Vampirism 加载时显示提示
        if (VampirismCompat.isLoaded()) {
            tooltip.add(Component.translatable("tooltip.kaleidoscope_bloodwine.hunter_drink")
                    .withStyle(ChatFormatting.DARK_BLUE));
        }
    }
}
