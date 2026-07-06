package com.github.kaleidoscope_bloodwine;

import com.github.kaleidoscope_bloodwine.init.ModBlocks;
import com.github.kaleidoscope_bloodwine.init.ModFluids;
import com.github.kaleidoscope_bloodwine.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(KaleidoscopeBloodwine.MOD_ID)
public class KaleidoscopeBloodwine {
    public static final String MOD_ID = "kaleidoscope_bloodwine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public KaleidoscopeBloodwine(IEventBus modBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ENTITIES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModFluids.FLUID_TYPES.register(modBus);
        ModFluids.FLUIDS.register(modBus);

        LOGGER.info("Kaleidoscope Bloodwine initializing...");
    }
}
