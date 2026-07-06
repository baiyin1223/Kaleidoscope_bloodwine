package com.github.kaleidoscope_bloodwine.init;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import com.github.kaleidoscope_bloodwine.item.BloodDrinkBlockItem;
import com.github.kaleidoscope_bloodwine.item.CourtyardBloodBrewItem;
import com.github.kaleidoscope_bloodwine.item.EternalNightDrinkBlockItem;
import com.github.kaleidoscope_bloodwine.item.HunterDrinkBlockItem;
import com.github.kaleidoscope_bloodwine.item.SinmarkCrimsonItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeBloodwine.MOD_ID);

    // 流体桶 - 使用标准 BucketItem（血液不可饮用）
    public static final DeferredItem<Item> BLOOD_BUCKET = ITEMS.register("blood_bucket",
            () -> new BucketItem(ModFluids.BLOOD.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    // ==================== 血酒物品 ====================

    // 鲜血葡萄酒
    public static final DeferredItem<Item> BLOOD_GRAPE_WINE = ITEMS.register("blood_grape_wine",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_GRAPE_WINE.get()));

    // 血威士忌
    public static final DeferredItem<Item> BLOOD_WHISKEY = ITEMS.register("blood_whiskey",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_WHISKEY.get()));

    // 血樱花酒
    public static final DeferredItem<Item> BLOOD_SAKURA_WINE = ITEMS.register("blood_sakura_wine",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_SAKURA_WINE.get()));

    // 血香槟
    public static final DeferredItem<Item> BLOOD_CHAMPAGNE = ITEMS.register("blood_champagne",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_CHAMPAGNE.get()));

    // 血伏特加
    public static final DeferredItem<Item> BLOOD_VODKA = ITEMS.register("blood_vodka",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_VODKA.get()));

    // 血白兰地
    public static final DeferredItem<Item> BLOOD_BRANDY = ITEMS.register("blood_brandy",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_BRANDY.get()));

    // 血佳丽酿
    public static final DeferredItem<Item> BLOOD_CARIGNAN = ITEMS.register("blood_carignan",
            () -> new BloodDrinkBlockItem(ModBlocks.BLOOD_CARIGNAN.get()));

    // ==================== 特殊酒 ====================

    // 猎人饮品 - 使用自定义 HunterDrinkBlockItem
    public static final DeferredItem<Item> HUNTER_DRINK = ITEMS.register("hunter_drink",
            () -> new HunterDrinkBlockItem(ModBlocks.HUNTER_DRINK.get()));

    // 永夜深红 - 使用自定义 EternalNightDrinkBlockItem
    public static final DeferredItem<Item> ETERNAL_NIGHT_CRIMSON = ITEMS.register("eternal_night_crimson",
            () -> new EternalNightDrinkBlockItem(ModBlocks.ETERNAL_NIGHT_CRIMSON.get()));

    // 幽庭血之秘酿
    public static final DeferredItem<Item> COURTYARD_BLOOD_BREW = ITEMS.register("courtyard_blood_brew",
            () -> new CourtyardBloodBrewItem(ModBlocks.COURTYARD_BLOOD_BREW.get()));

    // 罪痕绯酒
    public static final DeferredItem<Item> SINMARK_CRIMSON = ITEMS.register("sinmark_crimson",
            () -> new SinmarkCrimsonItem(ModBlocks.SINMARK_CRIMSON.get()));
}
