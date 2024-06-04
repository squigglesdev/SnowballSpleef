package dev.squiggles.snowballspleef.mixin;

import dev.squiggles.snowballspleef.BlockListStorage;
import dev.squiggles.snowballspleef.SnowballBreakableBlocksCommand;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowballEntity.class)
public abstract class SnowballMixin {

    @Inject(method = "onCollision", at = @At("HEAD"))
    private void onCollision(HitResult hitResult, CallbackInfo info) {
        if (!((SnowballEntity) (Object) this).getWorld().isClient) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                World world = ((SnowballEntity) (Object) this).getWorld();
                BlockPos blockPos = blockHitResult.getBlockPos();

                // Check the block directly hit
                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock();

                // Check the block above in case the pressure plate is above
                BlockPos abovePos = blockPos.up();
                BlockState aboveBlockState = world.getBlockState(abovePos);
                Block aboveBlock = aboveBlockState.getBlock();

                if (SnowballBreakableBlocksCommand.getIgnitesTNT()) {

                    // Handle Pressure plate-block-tnt situations
                    if (aboveBlock instanceof PressurePlateBlock) {
                        BlockPos tntBlockPos = blockPos.down();
                        BlockState tntBlockState = world.getBlockState(tntBlockPos);
                        Block potentialTntBlock = tntBlockState.getBlock();
                        if (potentialTntBlock instanceof TntBlock) {
                            TntBlock.primeTnt(world, tntBlockPos);
                            world.removeBlock(tntBlockPos, false);
                            world.removeBlock(abovePos, false);
                        }
                    }

                    // Handle snowball-tnt situations
                    if (block instanceof TntBlock) {
                        TntBlock.primeTnt(world, blockPos);
                        world.removeBlock(blockPos, false);
                    }
                }

                if (SnowballBreakableBlocksCommand.getBreakableBlocks().contains(block)) {
                    world.breakBlock(blockPos, false);
                }
            }
        }
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ThrownItemEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V"))
    private void onEntityHit(ThrownItemEntity instance, EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        int i = entity instanceof BlazeEntity ? 3 : 0;
        entity.damage(instance.getDamageSources().thrown((SnowballEntity) (Object) this, ((SnowballEntity) (Object) this).getOwner()), (float) i);

        if (entity instanceof PlayerEntity) {
            // Replace the arguments with appropriate values
            ((LivingEntity) entity).takeKnockback(0.5, ((SnowballEntity) (Object) this).getX() - entity.getX(), ((SnowballEntity) (Object) this).getZ() - entity.getZ());
            entity.damage(instance.getDamageSources().thrown((SnowballEntity) (Object) this, ((SnowballEntity) (Object) this).getOwner()), 1.0F);
        }
    }
}
