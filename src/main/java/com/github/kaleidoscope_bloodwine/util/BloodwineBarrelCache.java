package com.github.kaleidoscope_bloodwine.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Static cache for blood wine barrel outputs.
 * When a barrel finishes brewing blood wine, the result is stored here.
 * The BarrelFluidHandlerMixin reads from this cache to produce virtual fluids.
 */
public class BloodwineBarrelCache {

    // Key: "dimension|blockpos_long", Value: cached output data
    private static final ConcurrentHashMap<String, CachedOutput> CACHE = new ConcurrentHashMap<>();

    public record CachedOutput(ItemStack itemStack, ResourceLocation itemId, int brewLevel, int count) {}

    public static String makeKey(ResourceLocation dimension, BlockPos pos) {
        return dimension + "|" + pos.asLong();
    }

    public static void store(String key, ItemStack stack, ResourceLocation itemId, int brewLevel) {
        CACHE.put(key, new CachedOutput(stack.copy(), itemId, brewLevel, stack.getCount()));
    }

    public static CachedOutput get(String key) {
        return CACHE.get(key);
    }

    public static void consume(String key, int amount) {
        CachedOutput current = CACHE.get(key);
        if (current == null) return;
        int newCount = current.count() - amount;
        if (newCount <= 0) {
            CACHE.remove(key);
        } else {
            CACHE.put(key, new CachedOutput(current.itemStack(), current.itemId(), current.brewLevel(), newCount));
        }
    }

    public static void remove(String key) {
        CACHE.remove(key);
    }
}
