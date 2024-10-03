package cy.jdkdigital.cobblegengalore;

import cy.jdkdigital.cobblegengalore.common.block.BlockGenBlock;
import cy.jdkdigital.cobblegengalore.common.block.entity.BlockGenBlockEntity;
import cy.jdkdigital.cobblegengalore.common.recipe.BlockGenRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CobbleGenGalore.MODID)
public class CobbleGenGalore
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblegengalore";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    public static final DeferredBlock<Block> BLOCKGEN_STONE_BLOCK = registerBlock("block_gen_stone", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion(), 1));
    public static final DeferredBlock<Block> BLOCKGEN_COPPER_BLOCK = registerBlock("block_gen_copper", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion(), 2));
    public static final DeferredBlock<Block> BLOCKGEN_IRON_BLOCK = registerBlock("block_gen_iron", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), 4));
    public static final DeferredBlock<Block> BLOCKGEN_GOLD_BLOCK = registerBlock("block_gen_gold", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion(), 8));
    public static final DeferredBlock<Block> BLOCKGEN_EMERALD_BLOCK = registerBlock("block_gen_emerald", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).noOcclusion(), 16));
    public static final DeferredBlock<Block> BLOCKGEN_DIAMOND_BLOCK = registerBlock("block_gen_diamond", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion(), 32));
    public static final DeferredBlock<Block> BLOCKGEN_NETHERITE_BLOCK = registerBlock("block_gen_netherite", () -> new BlockGenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion(), 64));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockGenBlockEntity>> BLOCKGEN_BLOCKENTITY = BLOCK_ENTITIES.register("blockgen", () -> BlockEntityType.Builder.of(BlockGenBlockEntity::new,
            BLOCKGEN_STONE_BLOCK.get(),
            BLOCKGEN_COPPER_BLOCK.get(),
            BLOCKGEN_IRON_BLOCK.get(),
            BLOCKGEN_GOLD_BLOCK.get(),
            BLOCKGEN_EMERALD_BLOCK.get(),
            BLOCKGEN_DIAMOND_BLOCK.get(),
            BLOCKGEN_NETHERITE_BLOCK.get()
    ).build(null));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> BLOCKGEN_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("blockgen", BlockGenRecipe.Serializer::new);
    public static DeferredHolder<RecipeType<?>, RecipeType<BlockGenRecipe>> BLOCKGEN_RECIPE_TYPE = RECIPE_TYPES.register("blockgen", () -> new RecipeType<>() {});

    public static final TagKey<Item> BLOCK_GENERATORS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "block_generators"));


    public CobbleGenGalore(IEventBus modEventBus, ModContainer modContainer)
    {
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            ITEMS.getEntries().forEach(itemDeferredHolder -> {
                event.accept(itemDeferredHolder.value());
            });
        }
    }

    private static DeferredBlock<Block> registerBlock(String name, Supplier<? extends Block> sup) {
        DeferredBlock<Block> block = BLOCKS.register(name, sup);
        ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }
}
