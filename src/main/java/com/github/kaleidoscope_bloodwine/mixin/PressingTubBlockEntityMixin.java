package com.github.kaleidoscope_bloodwine.mixin;

import com.github.kaleidoscope_bloodwine.init.ModFluids;
import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.brew.PressingTubBlockEntity;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

/**
 * Mixin for PressingTubBlockEntity:
 * Replaces blue RAIN particles with dark-red DustParticleOptions
 * when the tub's fluid is "blood" (either from this addon or from Vampirism).
 */
@Mixin(PressingTubBlockEntity.class)
public abstract class PressingTubBlockEntityMixin extends BlockEntity {

    /** Shadow the private fluid field from the target class. */
    @Shadow
    private FluidTank fluid;

    // -------------------------------------------------------------------------
    // Dummy constructor required to satisfy the compiler (never actually called)
    // -------------------------------------------------------------------------
    private PressingTubBlockEntityMixin() {
        super(null, new BlockPos(0, 0, 0), null);
    }

    // -------------------------------------------------------------------------
    // Dark-red dust particle used to replace RAIN
    // -------------------------------------------------------------------------
    private static final DustParticleOptions RED_DUST = new DustParticleOptions(
            new Vector3f(0.8f, 0.05f, 0.05f),
            1.0f
    );

    // -------------------------------------------------------------------------
    // Helper: check whether the fluid currently held in the tub is "blood"
    // -------------------------------------------------------------------------
    private boolean kaleidoscope_bloodwine$isBloodFluid() {
        if (fluid == null) return false;
        FluidStack fluidStack = fluid.getFluid();
        if (fluidStack.isEmpty()) return false;

        // Check this addon's own blood fluid
        if (ModFluids.BLOOD.isPresent()
                && fluidStack.getFluid().isSame(ModFluids.BLOOD.get())) {
            return true;
        }

        // Check Vampirism's blood fluid (optional dependency)
        if (ModList.get().isLoaded("vampirism")) {
            net.minecraft.world.level.material.Fluid vampBlood =
                    ForgeRegistries.FLUIDS.getValue(new ResourceLocation("vampirism", "blood"));
            if (vampBlood != null && fluidStack.getFluid().isSame(vampBlood)) {
                return true;
            }
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // Inject into playSuccessPressEffect — only override when stack == null
    // (that branch is the one that uses RAIN)
    // -------------------------------------------------------------------------
    @Inject(
            method = "playSuccessPressEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void kaleidoscope_bloodwine$onPlaySuccessPressEffect(
            @Nullable ItemStack stack,
            CallbackInfo ci) {

        // Only intercept the stack == null branch (which emits RAIN particles)
        if (stack != null) return;

        if (!kaleidoscope_bloodwine$isBloodFluid()) return;

        if (this.level instanceof ServerLevel serverLevel) {
            // Play the same slime-fall sound as the original
            this.level.playSound(null,
                    this.worldPosition.getX() + 0.5,
                    this.worldPosition.getY() + 0.5,
                    this.worldPosition.getZ() + 0.5,
                    SoundEvents.SLIME_BLOCK_FALL,
                    SoundSource.BLOCKS,
                    0.5F + this.level.random.nextFloat(),
                    this.level.random.nextFloat() * 0.3F + 0.7F);

            // Red dust particles instead of RAIN
            serverLevel.sendParticles(RED_DUST,
                    this.worldPosition.getX() + 0.5,
                    this.worldPosition.getY() + 0.5,
                    this.worldPosition.getZ() + 0.5,
                    10, 0.25, 0.2, 0.25, 0.05);

            // Cancel the original method entirely
            ci.cancel();
        }
    }

    // -------------------------------------------------------------------------
    // Inject into playFinishedPressEffect — always uses RAIN, replace when blood
    // -------------------------------------------------------------------------
    @Inject(
            method = "playFinishedPressEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void kaleidoscope_bloodwine$onPlayFinishedPressEffect(CallbackInfo ci) {

        if (!kaleidoscope_bloodwine$isBloodFluid()) return;

        if (this.level instanceof ServerLevel serverLevel) {
            // Play the same honey-hit sound as the original
            this.level.playSound(null,
                    this.worldPosition.getX() + 0.5,
                    this.worldPosition.getY() + 0.5,
                    this.worldPosition.getZ() + 0.5,
                    SoundEvents.HONEY_BLOCK_HIT,
                    SoundSource.BLOCKS,
                    0.5F + this.level.random.nextFloat(),
                    this.level.random.nextFloat() * 0.3F + 0.7F);

            // Red dust particles instead of RAIN
            serverLevel.sendParticles(RED_DUST,
                    this.worldPosition.getX() + 0.5,
                    this.worldPosition.getY() + 0.5,
                    this.worldPosition.getZ() + 0.5,
                    10, 0.25, 0.2, 0.25, 0.05);

            // Cancel the original method entirely
            ci.cancel();
        }
    }
}
