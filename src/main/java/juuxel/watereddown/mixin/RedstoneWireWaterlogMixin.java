package juuxel.watereddown.mixin;/* This file is a part of the This & That project
 * by Juuxel, licensed under the MIT license.
 * Full code and license: https://github.com/Juuxel/ThisAndThat
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
@Implements(@Interface(iface = Waterloggable.class, prefix = "waterlog$"))
public abstract class RedstoneWireWaterlogMixin extends BlockShadowMixin {
    @Inject(at = @At("RETURN"), method = "<init>")
    private void onConstruct(Block.Settings var1, CallbackInfo info) {
        setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
    }

    @Inject(at = @At("RETURN"), method = "appendProperties", cancellable = true)
    private void onAppendProperties(StateFactory.Builder<Block, BlockState> var1, CallbackInfo info) {
        var1.with(Properties.WATERLOGGED);
    }

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    private void replacePlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info) {
        try {
            FluidState state = context.getWorld().getFluidState(context.getPos());
            info.setReturnValue(info.getReturnValue().with(Properties.WATERLOGGED, state.matches(FluidTags.WATER)));
        } catch (NullPointerException e) {}
    }
}
