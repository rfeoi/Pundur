package de.rfeoi.pundur;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;


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

    public static final HashMap<String, InternetNetworkNode> internetCodes = new HashMap<>();

    @RSAPIInject
    public static IRSAPI RSAPI;

    public PundurMod() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
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
        try {
            startPundurServer();
            System.out.println("Started Pundur Web Server on Port 8080");
        } catch (IOException e) {
            LOGGER.error("Can not start Pundur Web Server");
            e.printStackTrace();
        }
    }

    private void startPundurServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Config.SERVER.port.get()), 0);
        server.createContext("/", (t) -> {
            String internetCode = t.getRequestHeaders().getFirst("X-Internet-Code");
            if (internetCodes.containsKey(internetCode)) {
                INetwork network = NetworkUtils.getNetworkFromNode(internetCodes.get(internetCode));
                if (network != null) {
                    RSResponseFactory factory = new RSResponseFactory(network);
                    sendReponse(t, factory.make(), 200);
                } else {
                    sendReponse(t,"{\"error\": \"Can not access Network! Is the system chunk-loaded?\"}", 500);
                }
            } else {
                sendReponse(t,"{\"error\": \"Can not find a system under specified code\"}", 401);
            }
        });
        server.setExecutor(null);
        server.start();
    }

    private void sendReponse(HttpExchange t, String response, int code) throws IOException {
        t.getResponseHeaders().set("Content-Type", "application/json");
        t.sendResponseHeaders(code, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
