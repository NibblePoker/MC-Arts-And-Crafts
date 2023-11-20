package com.nibblepoker.artsandcrafts;

import com.mojang.logging.LogUtils;
import com.nibblepoker.artsandcrafts.items.BrokenNeonTubeItem;
import com.nibblepoker.artsandcrafts.items.DebuggingItem;
import com.nibblepoker.artsandcrafts.items.GlueItem;
import com.nibblepoker.artsandcrafts.items.NeonTubeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ArtsAndCraftsMod.MOD_ID)
public class ArtsAndCraftsMod {
    // Misc Stuff
    public static final String MOD_ID = "np_arts_and_crafts";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Registers
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

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
                            .strength(1.0F, 1.5F).sound(SoundType.WOOD).ignitedByLava()
            )
    );
    public static final RegistryObject<Item> PAPER_MACHE_BLOCK_ITEM = ITEMS.register(
            "paper_mache", () -> new BlockItem(
                    PAPER_MACHE_BLOCK.get(), new Item.Properties()
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

    // Mod's code
    public ArtsAndCraftsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //BlockIndex.register(modEventBus);

        // Register the Deferred Register instances to the mod event bus.
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);

        //MenuIndex.registerMenus(modEventBus);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);

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
}
