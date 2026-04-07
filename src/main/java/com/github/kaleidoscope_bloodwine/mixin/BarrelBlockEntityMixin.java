package com.github.kaleidoscope_bloodwine.mixin;

import com.github.kaleidoscope_bloodwine.blockentity.BloodDrinkBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.brew.BarrelBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for BarrelBlockEntity.transform():
 * 
 * The original transform() method stores the filled ItemStack (with BrewLevel NBT) 
 * via "instanceof DrinkBlockEntity", which fails for BloodDrinkBlockEntity.
 * 
 * Strategy: HEAD captures brewLevel before possible reset, TAIL adds item after
 * the original method completes.
 */
@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin {

    @Shadow
    private int brewLevel;

    /**
     * Temporary storage for brewLevel, captured at HEAD before the original
     * transform() method may reset it.
     */
    @Unique
    private int bloodwine$savedBrewLevel;

    /**
     * At HEAD of transform(): save the current brewLevel.
     * This runs BEFORE the original method body, so brewLevel is still valid.
     */
    @Inject(method = "transform", at = @At("HEAD"), remap = false)
    private void bloodwine$captureBrewLevel(Level level, BlockPos below, BlockState belowState, BottleBlockItem result, CallbackInfo ci) {
        this.bloodwine$savedBrewLevel = this.brewLevel;
    }

    /**
     * At TAIL of transform(): if the block entity below is BloodDrinkBlockEntity
     * and still empty (original code's DrinkBlockEntity check failed), add the
     * filled ItemStack with the saved BrewLevel.
     */
    @Inject(method = "transform", at = @At("TAIL"), remap = false)
    private void bloodwine$addItemToBloodDrink(Level level, BlockPos below, BlockState belowState, BottleBlockItem result, CallbackInfo ci) {
        BlockEntity be = level.getBlockEntity(below);
        if (be instanceof BloodDrinkBlockEntity bloodDrink) {
            // Only add if no items were stored (i.e., the DrinkBlockEntity path failed)
            boolean allEmpty = true;
            for (ItemStack item : bloodDrink.getItems()) {
                if (!item.isEmpty()) {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) {
                ItemStack filledStack = result.getFilledStack(this.bloodwine$savedBrewLevel);
                bloodDrink.addItem(filledStack);
                bloodDrink.refresh();
            }
        }
    }
}
