package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static de.rfeoi.pundur.PundurMod.internetCodes;

public class InternetNetworkNode extends NetworkNode {
    public static String NBT_INTERNET_ID = "Internet_ID";

    public static final ResourceLocation ID = new ResourceLocation("pundur", "internetblock");

    public String internetId;

    public boolean registered = false;

    protected InternetNetworkNode(World world, BlockPos pos) {
        super(world, pos);
    }

    private void writeSelfToIdRegistry() {
        if (!world.isClientSide) {
            if (internetId != null) {
                internetCodes.put(internetId, this);
                registered = true;
            }
        } else {
            registered = true;
        }
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
    public CompoundNBT writeConfiguration(CompoundNBT tag) {
        tag = super.writeConfiguration(tag);
        tag.putString(NBT_INTERNET_ID, internetId);
        return tag;
    }

    @Override
    public void readConfiguration(CompoundNBT tag) {
        super.readConfiguration(tag);
        if (tag.contains(NBT_INTERNET_ID)) {
            internetId = tag.getString(NBT_INTERNET_ID);
        }
    }



    @Override
    public void update() {
        super.update();
        //this.network.getItemStorageCache().addListener();
        if (!registered) writeSelfToIdRegistry();
    }

    @Override
    public void onConnected(INetwork network) {
        super.onConnected(network);

        if (internetId == null && !world.isClientSide) {
            internetId = UUID.randomUUID().toString().substring(0,8);
            while (internetCodes.containsKey(internetId)) { // to avoid duplicates
                internetId = UUID.randomUUID().toString().substring(0,8);
            }
            markDirty();
            writeSelfToIdRegistry();
        }
    }
}
