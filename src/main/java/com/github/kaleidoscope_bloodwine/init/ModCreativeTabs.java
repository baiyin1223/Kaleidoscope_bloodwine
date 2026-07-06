package com.github.kaleidoscope_bloodwine.init;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * 创造模式物品栏配置。
 * 使用 BuildCreativeModeTabContentsEvent 将附属模组物品追加到主模组的 TAVERN_MAIN_TAB。
 */
@EventBusSubscriber(modid = KaleidoscopeBloodwine.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModCreativeTabs {

    /**
     * 主模组 TAVERN_MAIN_TAB 的 ResourceKey。
     */
    public static final ResourceKey<CreativeModeTab> TAVERN_MAIN_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath("kaleidoscope_tavern", "tavern_main")
    );

    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(TAVERN_MAIN_TAB_KEY)) {
            // 血液桶
            event.accept(ModItems.BLOOD_BUCKET.get());

            // 血酒系列（按 brewLevel 排序）
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_GRAPE_WINE));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_WHISKEY));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_SAKURA_WINE));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_CHAMPAGNE));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_VODKA));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_BRANDY));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.BLOOD_CARIGNAN));

            // 特殊酒
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.HUNTER_DRINK));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.ETERNAL_NIGHT_CRIMSON));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.COURTYARD_BLOOD_BREW));
            event.accept(BottleBlockItem.getMaxLevelDrink(ModItems.SINMARK_CRIMSON));
        }
    }
}
