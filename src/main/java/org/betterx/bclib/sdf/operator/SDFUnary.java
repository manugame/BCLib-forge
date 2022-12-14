package org.betterx.bclib.sdf.operator;

import org.betterx.bclib.sdf.SDF;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SDFUnary extends SDF {
    protected SDF source;

    public SDFUnary setSource(SDF source) {
        this.source = source;
        return this;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return source.getBlockState(pos);
    }
}
