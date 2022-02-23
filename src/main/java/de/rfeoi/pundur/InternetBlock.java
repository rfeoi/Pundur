package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class InternetBlock extends NetworkNodeBlock {
    protected InternetBlock() {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
    }

    @Override
    public BlockDirection getDirection() {
        return BlockDirection.HORIZONTAL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InternetBlockTileEntity();
    }


    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            InternetNetworkNode internetNetworkNode = (InternetNetworkNode) NetworkUtils.getNodeFromTile(world.getBlockEntity(pos));
            if (internetNetworkNode != null && internetNetworkNode.internetId != null) {
                player.sendMessage(new StringTextComponent(internetNetworkNode.internetId), UUID.randomUUID());
            } else {
                player.sendMessage(new StringTextComponent("Please connect to storage system."), UUID.randomUUID());
            }
        }
        return ActionResultType.SUCCESS;
    }
}
