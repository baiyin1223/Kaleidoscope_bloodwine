package com.github.kaleidoscope_bloodwine.event;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import com.github.kaleidoscope_bloodwine.compat.VampirismCompat;
import com.github.kaleidoscope_bloodwine.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

/**
 * 监听物品使用完成事件，处理血酒的 Vampirism 兼容效果。
 * 注册在 FORGE 事件总线上（运行时事件） */
@Mod.EventBusSubscriber(modid = KaleidoscopeBloodwine.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DrinkEventHandler {

    /**
     * 血酒物品集合（懒加载）。
     * 包含所有标准血酒，但不含 hunter_drink（其逻辑在 HunterDrinkBlockItem 中处理）
     * 和 eternal_night_crimson（其特殊逻辑在 EternalNightDrinkBlockItem 中处理）。
     * 延迟初始化以避免在类加载时调用 RegistryObject.get()，防止注册表未填充时的 NullPointerException。
     */
    private static Set<Item> BLOOD_WINE_ITEMS;

    private static Set<Item> getBloodWineItems() {
        if (BLOOD_WINE_ITEMS == null) {
            BLOOD_WINE_ITEMS = Set.of(
                    ModItems.BLOOD_GRAPE_WINE.get(),
                    ModItems.BLOOD_WHISKEY.get(),
                    ModItems.BLOOD_SAKURA_WINE.get(),
                    ModItems.BLOOD_CHAMPAGNE.get(),
                    ModItems.BLOOD_VODKA.get(),
                    ModItems.BLOOD_BRANDY.get(),
                    ModItems.BLOOD_CARIGNAN.get()
            );
        }
        return BLOOD_WINE_ITEMS;
    }

    /**
     * 当玩家完成饮用物品时触发。
     * 若饮用的是血酒，且 Vampirism 已加载且玩家为吸血鬼，
     * 则为其应用吸血鬼饮血效果和防晒效果。
     *
     * @param event LivingEntityUseItemEvent.Finish 事件
     */
    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        // 仅服务端处理，且实体必须为 Player
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        // 仅对血酒生效
        if (!getBloodWineItems().contains(item)) {
            return;
        }

        // Vampirism 兼容：仅在 Vampirism 已加载且玩家为吸血鬼时触发
        if (VampirismCompat.isLoaded() && VampirismCompat.isVampire(player)) {
            int brewLevel = BottleBlockItem.getBrewLevel(stack);
            VampirismCompat.onDrinkBloodWine(player, stack, brewLevel);
        }
    }
}
