package com.github.kaleidoscope_bloodwine;

import com.github.kaleidoscope_bloodwine.compat.GearsandTavernCompat;
import com.github.kaleidoscope_bloodwine.init.ModBlocks;
import com.github.kaleidoscope_bloodwine.init.ModCreativeTabs;
import com.github.kaleidoscope_bloodwine.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(KaleidoscopeBloodwine.MOD_ID)
public class KaleidoscopeBloodwine {
    public static final String MOD_ID = "kaleidoscope_bloodwine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public KaleidoscopeBloodwine() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ENTITIES.register(modBus);
        ModItems.ITEMS.register(modBus);

        // 注册 CGT 兼容的 Capability 监听器（FORGE 事件总线）
        // 内部会检查 CGT 是否加载，只有加载时才附加 Capability
        GearsandTavernCompat.registerCapabilityListener(
                net.minecraftforge.common.MinecraftForge.EVENT_BUS);

        LOGGER.info("Kaleidoscope Bloodwine initializing...");
    }
}
