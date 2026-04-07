package com.github.kaleidoscope_bloodwine.init;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import com.github.kaleidoscope_bloodwine.block.BloodDrinkBlock;
import com.github.kaleidoscope_bloodwine.blockentity.BloodDrinkBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KaleidoscopeBloodwine.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KaleidoscopeBloodwine.MOD_ID);

    // 流体方块
    public static final RegistryObject<LiquidBlock> BLOOD_BLOCK = BLOCKS.register("blood",
            () -> new LiquidBlock(ModFluids.BLOOD, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .pushReaction(PushReaction.DESTROY)));

    // ==================== 标准形状 ====================
    // WINE/WHISKEY/SAKURA_WINE 形状：maxCount=4
    private static final VoxelShape[] WINE_SHAPES = new VoxelShape[]{
            Block.box(6, 0, 6, 10, 16, 10),
            Block.box(2, 0, 6, 14, 16, 10),
            Shapes.or(
                    Block.box(2, 0, 10, 14, 16, 14),
                    Block.box(6, 0, 2, 10, 16, 14)
            ),
            Block.box(2, 0, 2, 14, 16, 14)
    };

    // VODKA 形状：maxCount=4
    private static final VoxelShape[] VODKA_SHAPES = new VoxelShape[]{
            Block.box(4, 0, 4, 12, 15, 12),
            Block.box(0, 0, 4, 16, 15, 12),
            Shapes.or(
                    Block.box(0, 0, 8, 16, 15, 16),
                    Block.box(4, 0, 0, 12, 15, 16)
            ),
            Block.box(0, 0, 0, 16, 16, 16)
    };

    // BRANDY 形状（不规则）：maxCount=3
    private static final VoxelShape[] BRANDY_SHAPES = new VoxelShape[]{
            Block.box(3, 0, 6, 13, 13, 10),
            Block.box(1, 0, 3, 15, 12, 12),
            Block.box(1, 0, 1, 16, 12, 13)
    };

    // CARIGNAN 形状（不规则）：maxCount=3
    private static final VoxelShape[] CARIGNAN_SHAPES = new VoxelShape[]{
            Block.box(3, 0, 6, 13, 12, 10),
            Block.box(1, 0, 3, 15, 12, 12),
            Block.box(0, 0, 1, 16, 12, 13)
    };

    // FLASK 形状：maxCount=4（圆底/锥形烧瓶，长颈烧瓶比标准酒瓶略宽底部略矮）
    private static final VoxelShape[] FLASK_SHAPES = new VoxelShape[]{
            Block.box(5, 0, 5, 11, 16, 11),
            Block.box(1, 0, 5, 15, 16, 11),
            Shapes.or(
                    Block.box(1, 0, 9, 15, 16, 15),
                    Block.box(5, 0, 1, 11, 16, 15)
            ),
            Block.box(1, 0, 1, 15, 16, 15)
    };

    // ==================== 血酒方块 ====================

    // 鲜血葡萄酒 - 使用 WINE 形状
    public static final RegistryObject<Block> BLOOD_GRAPE_WINE = BLOCKS.register("blood_grape_wine",
            () -> new BloodDrinkBlock(4, WINE_SHAPES));

    // 血威士忌 - 使用 WHISKEY 形状（与 WINE 相同）
    public static final RegistryObject<Block> BLOOD_WHISKEY = BLOCKS.register("blood_whiskey",
            () -> new BloodDrinkBlock(4, WINE_SHAPES));

    // 血樱花酒 - 使用 SAKURA_WINE 形状（与 WINE 相同）
    public static final RegistryObject<Block> BLOOD_SAKURA_WINE = BLOCKS.register("blood_sakura_wine",
            () -> new BloodDrinkBlock(4, WINE_SHAPES));

    // 血香槟 - 使用 CHAMPAGNE 形状（与 WINE 相同）
    public static final RegistryObject<Block> BLOOD_CHAMPAGNE = BLOCKS.register("blood_champagne",
            () -> new BloodDrinkBlock(4, WINE_SHAPES));

    // 血伏特加 - 使用 VODKA 形状
    public static final RegistryObject<Block> BLOOD_VODKA = BLOCKS.register("blood_vodka",
            () -> new BloodDrinkBlock(4, VODKA_SHAPES));

    // 血白兰地 - 使用 BRANDY 形状（不规则）
    public static final RegistryObject<Block> BLOOD_BRANDY = BLOCKS.register("blood_brandy",
            () -> new BloodDrinkBlock(true, 3, BRANDY_SHAPES));

    // 血佳丽酿 - 使用 CARIGNAN 形状（不规则）
    public static final RegistryObject<Block> BLOOD_CARIGNAN = BLOCKS.register("blood_carignan",
            () -> new BloodDrinkBlock(true, 3, CARIGNAN_SHAPES));

    // 猎人饮品 - 使用 WINE 形状
    public static final RegistryObject<Block> HUNTER_DRINK = BLOCKS.register("hunter_drink",
            () -> new BloodDrinkBlock(4, WINE_SHAPES));

    // 永夜深红 - 使用 WINE 形状
    public static final RegistryObject<Block> ETERNAL_NIGHT_CRIMSON = BLOCKS.register("eternal_night_crimson",
            () -> new BloodDrinkBlock(4, WINE_SHAPES));

    // 幽庭血之秘酿 - 使用 FLASK 形状
    public static final RegistryObject<Block> COURTYARD_BLOOD_BREW = BLOCKS.register("courtyard_blood_brew",
            () -> new BloodDrinkBlock(4, FLASK_SHAPES));

    // 罪痕绯酒 - 使用 FLASK 形状
    public static final RegistryObject<Block> SINMARK_CRIMSON = BLOCKS.register("sinmark_crimson",
            () -> new BloodDrinkBlock(4, FLASK_SHAPES));

    // BlockEntity - 包含所有11个血酒方块
    public static final RegistryObject<BlockEntityType<BloodDrinkBlockEntity>> DRINK_BE = BLOCK_ENTITIES.register(
            "drink", () -> BlockEntityType.Builder
                    .of(BloodDrinkBlockEntity::new,
                            BLOOD_GRAPE_WINE.get(),
                            BLOOD_WHISKEY.get(),
                            BLOOD_SAKURA_WINE.get(),
                            BLOOD_CHAMPAGNE.get(),
                            BLOOD_VODKA.get(),
                            BLOOD_BRANDY.get(),
                            BLOOD_CARIGNAN.get(),
                            HUNTER_DRINK.get(),
                            ETERNAL_NIGHT_CRIMSON.get(),
                            COURTYARD_BLOOD_BREW.get(),
                            SINMARK_CRIMSON.get()
                    ).build(null));
}
