package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InternetNetworkNode extends NetworkNode {
    public static final ResourceLocation ID = new ResourceLocation("pundur", "internetblock");

    protected InternetNetworkNode(World world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public int getEnergyUsage() {
        return 100;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }


    @Override
    public void update() {
        super.update();
        //this.network.getItemStorageCache().addListener();
    }
}
