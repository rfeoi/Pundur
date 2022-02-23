package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.block.GridBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("pundur")
public class PundurMod {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "pundur");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "pundur");
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "pundur");

    private static final RegistryObject<Block> INTERNET_BLOCK = BLOCKS.register("internet_block", InternetBlock::new);
    private static final RegistryObject<Item> INTERNET_BLOCK_ITEM = ITEMS.register("internet_block", () -> new BlockItem(INTERNET_BLOCK.get(), new Item.Properties().tab(RS.MAIN_GROUP)));
    public static final RegistryObject<TileEntityType<InternetBlockTileEntity>> INTERNET_BLOCK_TILE = TILES.register("internet_block", () -> TileEntityType.Builder.of(InternetBlockTileEntity::new, INTERNET_BLOCK.get()).build(null));

    @RSAPIInject
    public static IRSAPI RSAPI;

    public PundurMod() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("WE ARE ALIVE! PUNDUR FOREVER!");
        RSAPI.getNetworkNodeRegistry().add(InternetNetworkNode.ID, (tag, world, pos) -> {
            InternetNetworkNode internetBlock = new InternetNetworkNode(world, pos);
            internetBlock.read(tag);
            return internetBlock;
        });
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("Pundur is getting ready!");
    }
}
