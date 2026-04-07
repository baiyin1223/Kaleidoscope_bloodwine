package com.github.kaleidoscope_bloodwine.init;

import com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine;
import com.github.kaleidoscope_bloodwine.fluid.BloodFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine.MOD_ID;
import static com.github.kaleidoscope_bloodwine.KaleidoscopeBloodwine.modLoc;
import static net.minecraftforge.registries.ForgeRegistries.FLUIDS;
import static net.minecraftforge.registries.ForgeRegistries.Keys.FLUID_TYPES;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFluids {
    // ID
    public static final ResourceLocation BLOOD_ID = modLoc("blood");
    public static final ResourceLocation FLOWING_BLOOD_ID = modLoc("flowing_blood");

    // FluidType
    public static final RegistryObject<FluidType> BLOOD_TYPE = RegistryObject.create(BLOOD_ID, FLUID_TYPES.location(), MOD_ID);

    // Fluid
    public static final RegistryObject<FlowingFluid> BLOOD = RegistryObject.create(BLOOD_ID, FLUIDS);
    public static final RegistryObject<FlowingFluid> FLOWING_BLOOD = RegistryObject.create(FLOWING_BLOOD_ID, FLUIDS);

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Keys.FLUID_TYPES, helper -> {
            helper.register(BLOOD_ID, new BloodFluidType(BLOOD_ID, 0));
        });

        event.register(Keys.FLUIDS, helper -> {
            Properties blood = new Properties(BLOOD_TYPE, BLOOD, FLOWING_BLOOD).bucket(ModItems.BLOOD_BUCKET);

            helper.register(BLOOD_ID, new Source(blood));
            helper.register(FLOWING_BLOOD_ID, new Flowing(blood));
        });
    }
}
