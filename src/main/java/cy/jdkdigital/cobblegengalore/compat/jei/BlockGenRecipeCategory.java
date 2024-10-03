package cy.jdkdigital.cobblegengalore.compat.jei;

import cy.jdkdigital.cobblegengalore.CobbleGenGalore;
import cy.jdkdigital.cobblegengalore.common.recipe.BlockGenRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class BlockGenRecipeCategory implements IRecipeCategory<BlockGenRecipe>
{
    private final IDrawable background;
    private final IDrawable icon;

    public BlockGenRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(CobbleGenGalore.MODID, "textures/gui/jei/block_generator.png");
        this.background = guiHelper.createDrawable(location, 0, 0, 126, 70);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CobbleGenGalore.BLOCKGEN_IRON_BLOCK.get()));
    }

    @Override
    public RecipeType<BlockGenRecipe> getRecipeType() {
        return CobbleGenGaloreJeiPlugin.BLOCK_GEN_TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("jei." + CobbleGenGalore.MODID + ".blockgen");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlockGenRecipe recipe, IFocusGroup focuses) {
        addBlockStateInput(builder, recipe.left, 10, 27);
        addBlockStateInput(builder, recipe.right, 101, 27);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 27).addIngredient(VanillaTypes.ITEM_STACK, recipe.result);
        builder.addSlot(RecipeIngredientRole.CATALYST, 55, 27).addItemStacks(BuiltInRegistries.ITEM.getOrCreateTag(CobbleGenGalore.BLOCK_GENERATORS).stream().map(itemHolder -> new ItemStack(itemHolder.value())).toList());

        if (!recipe.modifier.isAir()) {
            addBlockStateInput(builder, recipe.modifier, 55, 46);
        }
    }

    private static void addBlockStateInput(IRecipeLayoutBuilder builder, BlockState state, int x, int y) {
        if (!state.getFluidState().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredient(NeoForgeTypes.FLUID_STACK, new FluidStack(state.getFluidState().getType(), 1000));
        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(state.getBlock().asItem()));
        }
    }
}
