package com.github.kaleidoscope_bloodwine.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

/**
 * Vampirism 兼容层。
 * 所有 Vampirism 类引用都隔离在内部类 {@link Impl} 中，避免 Vampirism 未加载时触发 ClassNotFoundException。
 * 外部代码必须先调用 {@link #isLoaded()} 确认 Vampirism 已加载，再调用其他方法。
 */
public class VampirismCompat {

    /** Vampirism mod id */
    private static final String VAMPIRISM_MOD_ID = "vampirism";

    /** 延迟加载的实现实例，只有当 Vampirism 加载时才会创建 */
    @Nullable
    private static volatile Impl impl;

    /**
     * 判断 Vampirism 是否已加载。
     * 此方法不依赖任何 Vampirism 类，可以安全调用。
     *
     * @return true 表示 Vampirism 已在本次游戏实例中加载
     */
    public static boolean isLoaded() {
        return ModList.get().isLoaded(VAMPIRISM_MOD_ID);
    }

    /**
     * 获取实现实例。仅在确认 Vampirism 已加载后调用。
     */
    private static Impl getImpl() {
        if (impl == null) {
            synchronized (VampirismCompat.class) {
                if (impl == null) {
                    impl = new Impl();
                }
            }
        }
        return impl;
    }

    /**
     * 判断玩家是否为吸血鬼阵营（通过 Vampirism API 的 IFactionPlayerHandler）。
     * 必须在确认 {@link #isLoaded()} 返回 true 后调用。
     *
     * @param player 要检查的玩家，不能为 null
     * @return true 表示该玩家当前属于吸血鬼阵营
     */
    public static boolean isVampire(Player player) {
        return getImpl().isVampire(player);
    }

    /**
     * 判断玩家是否为猎人阵营（通过 Vampirism API 的 IFactionPlayerHandler）。
     * 必须在确认 {@link #isLoaded()} 返回 true 后调用。
     *
     * @param player 要检查的玩家，不能为 null
     * @return true 表示该玩家当前属于猎人阵营
     */
    public static boolean isHunter(Player player) {
        return getImpl().isHunter(player);
    }

    /**
     * 在玩家饮用血酒后，为吸血鬼玩家应用效果。
     * 仅在服务端调用有效；必须在确认 {@link #isLoaded()} 和 {@link #isVampire(Player)} 后调用。
     *
     * @param player    饮用血酒的吸血鬼玩家
     * @param stack     被饮用的 ItemStack（用作 drinkBlood 上下文）
     * @param brewLevel 酿造等级 (1-7)
     */
    public static void onDrinkBloodWine(Player player, ItemStack stack, int brewLevel) {
        getImpl().onDrinkBloodWine(player, stack, brewLevel);
    }

    /**
     * 在玩家饮用永夜血酒后，为吸血鬼玩家应用效果。
     * 仅在服务端调用有效；必须在确认 {@link #isLoaded()} 和 {@link #isVampire(Player)} 后调用。
     *
     * @param player    饮用永夜血酒的吸血鬼玩家
     * @param stack     被饮用的 ItemStack（用作 drinkBlood 上下文）
     * @param brewLevel 酿造等级 (1-7)
     */
    public static void onDrinkEternalNight(Player player, ItemStack stack, int brewLevel) {
        getImpl().onDrinkEternalNight(player, stack, brewLevel);
    }

    /**
     * 在玩家饮用幽庭血之秘酿后，为吸血鬼玩家应用效果。
     * 仅在服务端调用有效；必须在确认 {@link #isLoaded()} 和 {@link #isVampire(Player)} 后调用。
     *
     * @param player    饮用幽庭血之秘酿的吸血鬼玩家
     * @param stack     被饮用的 ItemStack（用作 drinkBlood 上下文）
     * @param brewLevel 酿造等级 (1-7)
     */
    public static void onDrinkCourtyardBrew(Player player, ItemStack stack, int brewLevel) {
        getImpl().onDrinkCourtyardBrew(player, stack, brewLevel);
    }

    /**
     * 在玩家饮用罪痕绯酒后，为吸血鬼玩家应用效果。
     * 仅在服务端调用有效；必须在确认 {@link #isLoaded()} 和 {@link #isVampire(Player)} 后调用。
     *
     * @param player    饮用罪痕绯酒的吸血鬼玩家
     * @param stack     被饮用的 ItemStack（用作 drinkBlood 上下文）
     * @param brewLevel 酿造等级 (1-7)
     */
    public static void onDrinkSinmarkCrimson(Player player, ItemStack stack, int brewLevel) {
        getImpl().onDrinkSinmarkCrimson(player, stack, brewLevel);
    }

    /**
     * 对吸血鬼玩家应用猎人饮品的负面效果。
     * 仅在服务端调用有效；必须在确认 {@link #isLoaded()} 和 {@link #isVampire(Player)} 后调用。
     *
     * @param player    饮用猎人饮品的吸血鬼玩家
     * @param brewLevel 酿造等级 (1-7)
     */
    public static void applyHunterDrinkVampireDebuff(Player player, int brewLevel) {
        getImpl().applyHunterDrinkVampireDebuff(player, brewLevel);
    }

    /**
     * 内部实现类。所有 Vampirism 类引用都放在这里。
     * 只有当 Vampirism 已加载时，这个类才会被创建和使用。
     * JVM 会延迟加载这个类，直到第一次使用时才解析其 import 语句。
     */
    private static class Impl {
        // 所有 Vampirism 相关的 import 都在这里
        // JVM 只有在创建 Impl 实例时才会加载这些类

        boolean isVampire(Player player) {
            return de.teamlapen.vampirism.api.VampirismAPI.getFactionPlayerHandler(player)
                    .map(handler -> handler.isInFaction(de.teamlapen.vampirism.api.VReference.VAMPIRE_FACTION))
                    .orElse(false);
        }

        boolean isHunter(Player player) {
            return de.teamlapen.vampirism.api.VampirismAPI.getFactionPlayerHandler(player)
                    .map(handler -> handler.isInFaction(de.teamlapen.vampirism.api.VReference.HUNTER_FACTION))
                    .orElse(false);
        }

        void onDrinkBloodWine(Player player, ItemStack stack, int brewLevel) {
            // 1. 恢复饮血值
            net.minecraftforge.common.util.LazyOptional<de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer> vampirePlayerOpt = 
                    de.teamlapen.vampirism.api.VampirismAPI.getVampirePlayer(player);
            vampirePlayerOpt.ifPresent(vampirePlayer -> {
                de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext context = buildItemContext(stack);
                vampirePlayer.drinkBlood(10, 0.5f, context);
            });

            // 2. 根据 brewLevel 给予防晒效果
            applySunscreenEffect(player, brewLevel);
        }

        void onDrinkEternalNight(Player player, ItemStack stack, int brewLevel) {
            // 1. 恢复满饮血值
            net.minecraftforge.common.util.LazyOptional<de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer> vampirePlayerOpt = 
                    de.teamlapen.vampirism.api.VampirismAPI.getVampirePlayer(player);
            vampirePlayerOpt.ifPresent(vampirePlayer -> {
                de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext context = buildItemContext(stack);
                // 使用较大值来模拟满血
                vampirePlayer.drinkBlood(100, 1.0f, context);
            });

            // 2. 根据 brewLevel 给予生命提升效果
            applyHealthBoostEffect(player, brewLevel);

            // 3. 根据 brewLevel 给予防晒效果
            applySunscreenEffect(player, brewLevel);
        }

        void onDrinkCourtyardBrew(Player player, ItemStack stack, int brewLevel) {
            // 1. 恢复饮血值
            net.minecraftforge.common.util.LazyOptional<de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer> vampirePlayerOpt = 
                    de.teamlapen.vampirism.api.VampirismAPI.getVampirePlayer(player);
            vampirePlayerOpt.ifPresent(vampirePlayer -> {
                de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext context = buildItemContext(stack);
                vampirePlayer.drinkBlood(10, 0.5f, context);
            });

            // 2. 防晒效果
            applySunscreenEffect(player, brewLevel);

            // 3. 力量效果
            applyStrengthEffect(player, brewLevel);
        }

        void onDrinkSinmarkCrimson(Player player, ItemStack stack, int brewLevel) {
            // 1. 恢复饮血值
            net.minecraftforge.common.util.LazyOptional<de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer> vampirePlayerOpt = 
                    de.teamlapen.vampirism.api.VampirismAPI.getVampirePlayer(player);
            vampirePlayerOpt.ifPresent(vampirePlayer -> {
                de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext context = buildItemContext(stack);
                vampirePlayer.drinkBlood(10, 0.5f, context);
            });

            // 2. 防晒效果
            applySunscreenEffect(player, brewLevel);

            // 3. 生命提升效果
            applyHealthBoostEffect(player, brewLevel);

            // 4. 力量效果
            applyStrengthEffect(player, brewLevel);
        }

        void applyHunterDrinkVampireDebuff(Player player, int brewLevel) {
            // 中毒效果：持续时间按 brewLevel 递增
            int poisonDuration = getPoisonDuration(brewLevel);
            if (poisonDuration > 0) {
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.POISON, poisonDuration * 20, 1, false, true));
            }

            // 反胃效果：持续时间按 brewLevel 递增
            int nauseaDuration = getNauseaDuration(brewLevel);
            if (nauseaDuration > 0) {
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.CONFUSION, nauseaDuration * 20, 0, false, true));
            }
        }

        /**
         * 根据 brewLevel 给予防晒效果。
         */
        private void applySunscreenEffect(Player player, int brewLevel) {
            if (brewLevel < 3) {
                return;
            }

            net.minecraft.world.effect.MobEffect sunscreen = net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getValue(
                    new net.minecraft.resources.ResourceLocation(VAMPIRISM_MOD_ID, "sunscreen"));
            if (sunscreen == null) {
                return;
            }

            int amplifier;
            int duration;

            switch (brewLevel) {
                case 3 -> { amplifier = 0; duration = 60; }
                case 4 -> { amplifier = 1; duration = 120; }
                case 5 -> { amplifier = 2; duration = 240; }
                case 6 -> { amplifier = 3; duration = 360; }
                case 7 -> { amplifier = 4; duration = 480; }
                default -> { return; }
            }

            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(sunscreen, duration * 20, amplifier, false, true));
        }

        /**
         * 根据 brewLevel 给予生命提升效果。
         */
        private void applyHealthBoostEffect(Player player, int brewLevel) {
            if (brewLevel < 3) {
                return;
            }

            int amplifier;
            int duration;

            switch (brewLevel) {
                case 3 -> { amplifier = 0; duration = 60; }
                case 4 -> { amplifier = 1; duration = 120; }
                case 5 -> { amplifier = 2; duration = 180; }
                case 6 -> { amplifier = 3; duration = 240; }
                case 7 -> { amplifier = 4; duration = 300; }
                default -> { return; }
            }

            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.HEALTH_BOOST, duration * 20, amplifier, false, true));
        }

        /**
         * 根据 brewLevel 给予力量效果。
         */
        private void applyStrengthEffect(Player player, int brewLevel) {
            if (brewLevel < 3) {
                return;
            }

            int amplifier;
            int duration;

            switch (brewLevel) {
                case 3 -> { amplifier = 0; duration = 60; }
                case 4 -> { amplifier = 1; duration = 120; }
                case 5 -> { amplifier = 2; duration = 180; }
                case 6 -> { amplifier = 3; duration = 240; }
                case 7 -> { amplifier = 4; duration = 300; }
                default -> { return; }
            }

            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.DAMAGE_BOOST, duration * 20, amplifier, false, true));
        }

        /**
         * 获取中毒效果持续时间（秒）。
         */
        private int getPoisonDuration(int brewLevel) {
            return switch (brewLevel) {
                case 1, 2 -> 5;
                case 3 -> 8;
                case 4 -> 12;
                case 5 -> 18;
                case 6 -> 25;
                case 7 -> 35;
                default -> 0;
            };
        }

        /**
         * 获取反胃效果持续时间（秒）。
         */
        private int getNauseaDuration(int brewLevel) {
            return switch (brewLevel) {
                case 1, 2 -> 10;
                case 3 -> 15;
                case 4 -> 20;
                case 5 -> 30;
                case 6 -> 45;
                case 7 -> 60;
                default -> 0;
            };
        }

        /**
         * 根据 ItemStack 构造 IDrinkBloodContext（血液来源上下文）。
         */
        private de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext buildItemContext(ItemStack stack) {
            return new de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext() {
                @Override
                public java.util.Optional<net.minecraft.world.entity.LivingEntity> getEntity() {
                    return java.util.Optional.empty();
                }

                @Override
                public java.util.Optional<ItemStack> getStack() {
                    return java.util.Optional.of(stack);
                }

                @Override
                public java.util.Optional<net.minecraft.world.level.block.state.BlockState> getBlockState() {
                    return java.util.Optional.empty();
                }

                @Override
                public java.util.Optional<net.minecraft.core.BlockPos> getBlockPos() {
                    return java.util.Optional.empty();
                }
            };
        }
    }
}
