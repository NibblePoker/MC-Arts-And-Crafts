package com.nibblepoker.artsandcrafts;

import com.mojang.logging.LogUtils;
import com.nibblepoker.artsandcrafts.items.BrokenNeonTubeItem;
import com.nibblepoker.artsandcrafts.items.DebuggingItem;
import com.nibblepoker.artsandcrafts.items.NeonTubeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    // Items
    public static final RegistryObject<Item> DEBUG_JOYSTICK = ITEMS.register(
            "debug_joystick", () -> new DebuggingItem(
                    new Item.Properties().fireResistant().rarity(Rarity.EPIC)
            )
    );
    public static final RegistryObject<Item> NEON_TUBE = ITEMS.register(
            "neon_tube", () -> new NeonTubeItem(
                    new Item.Properties()
            )
    );
    public static final RegistryObject<Item> BROKEN_NEON_TUBE = ITEMS.register(
            "broken_neon_tube", () -> new BrokenNeonTubeItem(
                    new Item.Properties().durability(6)
            )
    );

    // Sounds
    public static final RegistryObject<SoundEvent> NEON_BREAKING_SOUND = SOUNDS.register(
            "neon_tube_break", () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(MOD_ID, "neon_tube_break")
            )
    );

    // Mod's code
    public ArtsAndCraftsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //BlockIndex.register(modEventBus);

        // Register the Deferred Register to the mod event bus so items get registered
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
