package com.github.kaleidoscope_bloodwine.init;

import com.github.kaleidoscope_bloodwine.fluid.BloodFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine.MOD_ID;
import static com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine.modLoc;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, MOD_ID);

    // ID
    public static final ResourceLocation BLOOD_ID = modLoc("blood");
    public static final ResourceLocation FLOWING_BLOOD_ID = modLoc("flowing_blood");

    // FluidType
    public static final Supplier<FluidType> BLOOD_TYPE = FLUID_TYPES.register("blood", () -> new BloodFluidType(BLOOD_ID, 0));

    // Fluid
    public static final Supplier<BaseFlowingFluid.Source> BLOOD = FLUIDS.register("blood", () -> new BaseFlowingFluid.Source(bloodProperties()));
    public static final Supplier<BaseFlowingFluid.Flowing> FLOWING_BLOOD = FLUIDS.register("flowing_blood", () -> new BaseFlowingFluid.Flowing(bloodProperties()));

    private static BaseFlowingFluid.Properties bloodProperties() {
        return new BaseFlowingFluid.Properties(BLOOD_TYPE, BLOOD, FLOWING_BLOOD).bucket(ModItems.BLOOD_BUCKET);
    }
}
