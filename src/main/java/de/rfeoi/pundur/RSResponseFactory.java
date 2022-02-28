package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.api.autocrafting.task.ICraftingTask;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.storage.IStorage;
import net.minecraft.item.ItemStack;

/*
  I have no Idea how to do factories.
  But I heard Java people love them!
 */
public class RSResponseFactory {

    private final INetwork network;

    public RSResponseFactory(INetwork network) {
        this.network = network;
    }

    private String getItemsString() {
        String items = "[";
        for (IStorage<ItemStack> storage : network.getItemStorageCache().getStorages()) {
            for (ItemStack item : storage.getStacks()) {
                items += "{" +
                        "\"name\": \"" + item.getDisplayName().getString() + "\"," +
                        "\"item\": \"" + item.getItem().getRegistryName().toString() + "\"," +
                        "\"count\": " + item.getCount() +
                        "},";
            }
        }
        if (!items.equals("[")) items = items.substring(0, items.length() - 1);
        items += "]";
        return items;
    }

    private String getCraftingStatusString() {
        String craftingsTasks = "[";
        for (ICraftingTask task : network.getCraftingManager().getTasks()) {
            craftingsTasks += "{" +
                    "\"item\": \"" + task.getRequested().getItem().getItem().getRegistryName().toString() + "\"," +
                    "\"percentage\": " + task.getCompletionPercentage() + "},";
        }
        if (!craftingsTasks.equals("[")) craftingsTasks = craftingsTasks.substring(0, craftingsTasks.length() - 1);
        craftingsTasks += "]";
        return craftingsTasks;
    }

    public String make() {
        return "{" +
                "\"energyStorage\": " + network.getEnergyStorage().getMaxEnergyStored() + "," +
                "\"energyStored\": " + network.getEnergyStorage().getEnergyStored() + "," +
                "\"energyUsage\": " + network.getEnergyUsage() + "," +
                "\"posX\": " + network.getPosition().getX() + "," +
                "\"posY\": " + network.getPosition().getY() + "," +
                "\"posZ\": " + network.getPosition().getZ() + "," +
                "\"craftingTasks\": " + getCraftingStatusString() + "," +
                "\"items\": " + getItemsString() +
                "}";
    }
}
