package com.github.kaleidoscope_bloodwine.init;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 创造模式物品栏配置。
 * 使用 BuildCreativeModeTabContentsEvent 将附属模组物品追加到主模组的 TAVERN_MAIN_TAB。
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeBloodwine.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModCreativeTabs {

    /**
     * 主模组 TAVERN_MAIN_TAB 的 ResourceKey。
     */
    public static final ResourceKey<CreativeModeTab> TAVERN_MAIN_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            new ResourceLocation("kaleidoscope_tavern", "tavern_main")
    );

    @SubscribeEvent
    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(TAVERN_MAIN_TAB_KEY)) {
            // 血液桶
            event.accept(ModItems.BLOOD_BUCKET.get());

            // 血酒系列（按 brewLevel 排序）
            event.accept(ModItems.BLOOD_GRAPE_WINE.get());
            event.accept(ModItems.BLOOD_WHISKEY.get());
            event.accept(ModItems.BLOOD_SAKURA_WINE.get());
            event.accept(ModItems.BLOOD_CHAMPAGNE.get());
            event.accept(ModItems.BLOOD_VODKA.get());
            event.accept(ModItems.BLOOD_BRANDY.get());
            event.accept(ModItems.BLOOD_CARIGNAN.get());

            // 特殊酒
            event.accept(ModItems.HUNTER_DRINK.get());
            event.accept(ModItems.ETERNAL_NIGHT_CRIMSON.get());
            event.accept(ModItems.COURTYARD_BLOOD_BREW.get());
            event.accept(ModItems.SINMARK_CRIMSON.get());
        }
    }
}
