package dev.squiggles.snowballspleef.mixin;

import dev.squiggles.snowballspleef.SnowballBreakableBlocksCommand;
import net.minecraft.block.Block;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballEntity.class)
public abstract class SnowballMixin {

    @Inject(method = "onCollision", at = @At("HEAD"))
    private void onCollision(HitResult hitResult, CallbackInfo info) {
        if (!((SnowballEntity) (Object) this).getWorld().isClient) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                World world = ((SnowballEntity) (Object) this).getWorld();
                Block block = world.getBlockState(blockHitResult.getBlockPos()).getBlock();

                if (SnowballBreakableBlocksCommand.getBreakableBlocks().contains(block)) {
                    world.breakBlock(blockHitResult.getBlockPos(), false);
                }
            }
        }
    }
}
