package com.github.kaleidoscope_bloodwine.mixin;

import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.brew.BarrelBlockEntity;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor mixin for BarrelBlockEntity fields needed to reset brewing state
 * when blood wine fluid is fully drained via Create pipes.
 */
@Mixin(BarrelBlockEntity.class)
public interface BarrelBlockEntityAccessor {

    @Accessor("recipeId")
    void bloodwine$setRecipeId(ResourceLocation recipeId);

    @Accessor("brewLevel")
    void bloodwine$setBrewLevel(int brewLevel);

    @Accessor("brewTime")
    void bloodwine$setBrewTime(int brewTime);
}
