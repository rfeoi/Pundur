package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InternetBlockTileEntity extends NetworkNodeTile<InternetNetworkNode> {
    protected InternetBlockTileEntity() {
        super(PundurMod.INTERNET_BLOCK_TILE.get());
    }

    @Override
    public InternetNetworkNode createNode(World world, BlockPos pos) {
        return new InternetNetworkNode(world, pos);
    }
}
