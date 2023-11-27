package com.nibblepoker.artsandcrafts;

import com.mojang.logging.LogUtils;
import com.nibblepoker.artsandcrafts.blocks.DevTestBlockOne;
import com.nibblepoker.artsandcrafts.blocks.entities.DevTestOneBlockEntity;
import com.nibblepoker.artsandcrafts.blocks.renderers.DevTestOneBlockRenderer;
import com.nibblepoker.artsandcrafts.items.*;
import com.nibblepoker.artsandcrafts.logic.managers.ArtManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Paths;

@Mod(ArtsAndCraftsMod.MOD_ID)
public class ArtsAndCraftsMod {
    // Misc Stuff
    public static final String MOD_ID = "np_arts_and_crafts";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Public globals
    public static boolean dataFolderAvailable = false;
    public static File dataFolderPath = null;
    public static ArtManager artManager = new ArtManager();

    // Registers
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // Blocks
    public static final RegistryObject<Block> WET_PAPER_MACHE_BLOCK = BLOCKS.register(
            "wet_paper_mache", () -> new Block(
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY)
                            .strength(0.5F).sound(SoundType.SLIME_BLOCK)
            )
    );
    public static final RegistryObject<Item> WET_PAPER_MACHE_BLOCK_ITEM = ITEMS.register(
            "wet_paper_mache", () -> new BlockItem(
                    WET_PAPER_MACHE_BLOCK.get(), new Item.Properties()
            )
    );

    public static final RegistryObject<Block> PAPER_MACHE_BLOCK = BLOCKS.register(
            "paper_mache", () -> new Block(
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY)
                            .strength(0.8F).sound(SoundType.WOOD).ignitedByLava()
            )
    );
    public static final RegistryObject<Item> PAPER_MACHE_BLOCK_ITEM = ITEMS.register(
            "paper_mache", () -> new BlockItem(
                    PAPER_MACHE_BLOCK.get(), new Item.Properties()
            )
    );

    public static final RegistryObject<Block> DEV_TEST_1_BLOCK = BLOCKS.register(
            "dev_test_block_1", () -> new DevTestBlockOne(
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK)
                            .strength(0.0F).sound(SoundType.STONE)
            )
    );
    public static final RegistryObject<Item> DEV_TEST_1_ITEM = ITEMS.register(
            "dev_test_block_1", () -> new BlockItem(
                    DEV_TEST_1_BLOCK.get(), new Item.Properties()
            )
    );

    // Items
    public static final RegistryObject<Item> DEBUG_JOYSTICK = ITEMS.register(
            "debug_joystick", () -> new DebuggingItem(
                    new Item.Properties().fireResistant().rarity(Rarity.EPIC)
            )
    );

    public static final RegistryObject<Item> BROKEN_NEON_TUBE_YELLOW = ITEMS.register(
            "broken_neon_tube_yellow", () -> new BrokenNeonTubeItem(
                    new Item.Properties().durability(6)
            )
    );
    public static final RegistryObject<Item> BROKEN_NEON_TUBE_BLUE = ITEMS.register(
            "broken_neon_tube_blue", () -> new BrokenNeonTubeItem(
                    new Item.Properties().durability(6)
            )
    );

    public static final RegistryObject<Item> NEON_TUBE_YELLOW = ITEMS.register(
            "neon_tube_yellow", () -> new NeonTubeItem(
                    BROKEN_NEON_TUBE_YELLOW.get(), new Item.Properties()
            )
    );
    public static final RegistryObject<Item> NEON_TUBE_BLUE = ITEMS.register(
            "neon_tube_blue", () -> new NeonTubeItem(
                    BROKEN_NEON_TUBE_BLUE.get(), new Item.Properties()
            )
    );

    public static final RegistryObject<Item> POTATO_STARCH_ITEM = ITEMS.register(
            "potato_starch", () -> new Item(new Item.Properties().stacksTo(1))
    );
    public static final RegistryObject<Item> POTATO_GLUE_ITEM = ITEMS.register(
            "potato_glue", () -> new GlueItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> DESIGNER_TAB_ITEM = ITEMS.register(
            "designer_tab", () -> new DesignerTabItem(new Item.Properties().stacksTo(1))
    );
    public static final RegistryObject<Item> DESIGNER_BLUEPRINT_ITEM = ITEMS.register(
            "designer_blueprint", () -> new DesignerBlueprintItem(new Item.Properties().stacksTo(1))
    );

    // Sounds
    public static final RegistryObject<SoundEvent> NEON_BREAKING_SOUND = SOUNDS.register(
            "neon_tube_break", () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(MOD_ID, "neon_tube_break")
            )
    );
    public static final RegistryObject<SoundEvent> GLUE_SLURP_SOUND = SOUNDS.register(
            "glue_slurp", () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(MOD_ID, "glue_slurp")
            )
    );

    // Block entities
    public static final RegistryObject<BlockEntityType<DevTestOneBlockEntity>> DEV_TEST_1_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register(
            "be_test_zero",
            () -> BlockEntityType.Builder.of(
                    DevTestOneBlockEntity::new,
                    DEV_TEST_1_BLOCK.get()
            ).build(null)
    );

    // Creative tabs
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register(
            "main_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(DESIGNER_TAB_ITEM.get()))
                    .title(Component.translatable("creative_tab."+MOD_ID+".main_tab"))
                    .displayItems((itemDisplayParameters, outputTab) -> {
                        ITEMS.getEntries().forEach(itemRegistryObject -> {
                            outputTab.accept(((RegistryObject<Item>) itemRegistryObject).get());
                        });
                    }).build()
    );

    // Mod's code
    public ArtsAndCraftsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Preparing the data directory in "NibblePokerData/np_arts_and_crafts".
        LOGGER.info("Preparing the data folders...");
        ArtsAndCraftsMod.dataFolderPath = Paths.get(Minecraft.getInstance().gameDirectory.getAbsolutePath(),
                "NibblePokerData", MOD_ID).toFile();

        // Checking if we need to create the directory tree.
        if(!ArtsAndCraftsMod.dataFolderPath.exists()) {
            try {
                LOGGER.info("Attempting to create the data directories...");
                ArtsAndCraftsMod.dataFolderPath.mkdirs();
            } catch (Exception e) {
                LOGGER.error("Unable to create data directory !");
                LOGGER.error(e.getMessage());
            }
        }

        // Checking if we actually have a data directory and not some file with the same name.
        if(ArtsAndCraftsMod.dataFolderPath.exists()) {
            if(ArtsAndCraftsMod.dataFolderPath.isDirectory()) {
                LOGGER.debug("The data directory is available :)");
                ArtsAndCraftsMod.dataFolderAvailable = true;
            } else {
                LOGGER.error("The data directory is a file !");
            }
        }

        // Preparing the ArtManager.
        LOGGER.info("Preparing the ArtManager...");
        ArtsAndCraftsMod.artManager.init(ArtsAndCraftsMod.dataFolderPath);

        // Register the commonSetup method for mod
        LOGGER.info("Doing modEventBus stuff...");
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register instances to the mod event bus.
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        //MenuIndex.registerMenus(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Registering the network messages that can be sent back and forth.
        //event.enqueueWork(MessageIndex::register);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            //ItemBlockRenderTypes.setRenderLayer(WALL_VENDING_TEST_BLOCK, RenderType.CompositeState);
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            LOGGER.info("Registering renderers...");
            event.registerBlockEntityRenderer(DEV_TEST_1_BLOCK_ENTITY_TYPE.get(),
                    DevTestOneBlockRenderer::new);
        }
    }
}
