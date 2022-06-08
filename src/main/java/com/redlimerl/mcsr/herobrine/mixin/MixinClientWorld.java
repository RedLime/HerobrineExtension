package com.redlimerl.mcsr.herobrine.mixin;

import com.redlimerl.mcsr.herobrine.Herobrine;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends World {

    protected MixinClientWorld(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    @Inject(method = "updateListeners", at = @At("TAIL"))
    public void onBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        InGameTimer timer = InGameTimer.getInstance();

        if (timer.getCategory() == Herobrine.HEROBRINE && timer.getStatus() != TimerStatus.NONE) {

            loop:
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    for (int z = 0; z < 5; z++) {
                        BlockPos targetPos = pos.add(x - 2, y - 2, z - 2);
                        if (this.getBlockState(targetPos).getBlock() == Blocks.FIRE && checkHerobrinePortal(targetPos)) {
                            InGameTimer.complete();
                            break loop;
                        }
                    }
                }
            }

        }
    }

    private boolean checkHerobrinePortal(BlockPos firePos) {
        for (int x = -1; x < 2; x++) {
            for (int y = 0; y > -3; y--) {
                for (int z = -1; z < 2; z++) {
                    BlockPos targetPos = firePos.add(x, y, z);
                    Block targetBlock = this.getBlockState(targetPos).getBlock();

                    if (targetPos == firePos.add(0, -1, 0) && targetBlock != Blocks.NETHERRACK) {
                        return false;
                    }

                    if (targetPos == firePos.add(0, -2, 0) && targetBlock != Blocks.MOSSY_COBBLESTONE) {
                        return false;
                    }

                    int corner = Math.abs(x) + Math.abs(z);
                    if (y == -1 && corner == 1 && targetBlock != Blocks.REDSTONE_TORCH) {
                        return false;
                    }

                    if (y == -2 && corner != 0 && targetBlock != Blocks.GOLD_BLOCK) {
                        return false;
                    }

                    if (y == 0 && corner != 0 && targetBlock != Blocks.AIR) {
                        return false;
                    }

                    if (y == -1 && corner == 2 && targetBlock != Blocks.AIR) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}