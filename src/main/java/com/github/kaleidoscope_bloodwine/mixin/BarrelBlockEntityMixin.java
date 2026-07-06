package com.github.kaleidoscope_bloodwine.mixin;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import com.github.kaleidoscope_bloodwine.blockentity.BloodDrinkBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.brew.BarrelBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.item.BottleBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for BarrelBlockEntity.doTapExtract():
 *
 * KT 1.2.0 rewrote doTapExtract/transform with Ingredient-based carrier
 * matching and split transformPlacedCarrier/transformItemCarrier flows.
 * The new placeBottleResult() stores into DrinkBlockEntity only,
 * so BloodDrinkBlockEntity (not a DrinkBlockEntity subclass) is skipped.
 *
 * Strategy:
 * - HEAD captures brewLevel and output slot before extraction
 * - TAIL checks if the block below is a BloodDrinkBlockEntity with empty
 *   items, and if so, stores the saved item with brew level.
 */
@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin {

    @Shadow(remap = false)
    private int brewLevel;

    @Unique
    private int bloodwine$savedBrewLevel;

    @Unique
    private ItemStack bloodwine$savedOutput = ItemStack.EMPTY;

    @Inject(method = "doTapExtract", at = @At("HEAD"), remap = false)
    private void bloodwine$captureState(Level level, BlockPos pos, CallbackInfo ci) {
        this.bloodwine$savedBrewLevel = this.brewLevel;
        BarrelBlockEntity self = (BarrelBlockEntity) (Object) this;
        ItemStack output = self.getOutput().getStackInSlot(0);
        this.bloodwine$savedOutput = output.isEmpty() ? ItemStack.EMPTY : output.copy();
    }

    @Inject(method = "doTapExtract", at = @At("TAIL"), remap = false)
    private void bloodwine$fillBloodDrinkBlockEntity(Level level, BlockPos pos, CallbackInfo ci) {
        BlockPos below = pos.below();
        BlockEntity be = level.getBlockEntity(below);
        if (!(be instanceof BloodDrinkBlockEntity bloodDrink)) return;

        boolean allEmpty = true;
        for (ItemStack item : bloodDrink.getItems()) {
            if (!item.isEmpty()) {
                allEmpty = false;
                break;
            }
        }
        if (!allEmpty) return;

        ItemStack outputStack = this.bloodwine$savedOutput;
        if (outputStack.isEmpty()) return;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(outputStack.getItem());
        if (itemId == null || !KaleidoscopeBloodwine.MOD_ID.equals(itemId.getNamespace())) return;

        BottleBlockItem.setBrewLevel(outputStack, this.bloodwine$savedBrewLevel);
        bloodDrink.addItem(outputStack.copy());
        bloodDrink.refresh();
    }
}
