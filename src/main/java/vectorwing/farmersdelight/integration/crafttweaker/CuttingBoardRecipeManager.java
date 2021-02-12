package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.utils.IngredientUtils;
import vectorwing.farmersdelight.utils.ListUtils;

@ZenRegister
@ZenCodeType.Name("mods.farmersdelight.CuttingBoard")
public class CuttingBoardRecipeManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredient input,
                          IItemStack[] results,
                          String toolTypeName,
                          @ZenCodeType.OptionalString String soundEvent) {
        ToolType toolType;
        try {
            toolType = ToolType.get(toolTypeName);
        } catch (IllegalArgumentException e) {
            CraftTweakerAPI.logThrowing("Invalid tool type \"%s\"", e, toolTypeName);
            return;
        }
        Ingredient toolIngredient = IngredientUtils.getToolTypeIngredient(toolType);
        if (toolIngredient.hasNoMatchingItems()) {
            CraftTweakerAPI.logError("No tools of type \"%s\" for cutting recipe", toolTypeName);
            return;
        }

        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new CuttingBoardRecipe(new ResourceLocation(CraftTweaker.MODID, name),
                        "",
                        input.asVanillaIngredient(),
                        toolIngredient,
                        toolType,
                        ListUtils.mapArrayIndexSet(results,
                                IItemStack::getInternal,
                                NonNullList.withSize(results.length, ItemStack.EMPTY)),
                        soundEvent),
                ""));
    }

    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredient input,
                          IItemStack[] results,
                          IIngredient tool,
                          @ZenCodeType.OptionalString String soundEvent) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new CuttingBoardRecipe(new ResourceLocation(CraftTweaker.MODID, name),
                        "",
                        input.asVanillaIngredient(),
                        tool.asVanillaIngredient(),
                        null,
                        ListUtils.mapArrayIndexSet(results,
                                IItemStack::getInternal,
                                NonNullList.withSize(results.length, ItemStack.EMPTY)),
                        soundEvent),
                ""));
    }

    @Override
    public IRecipeType<CuttingBoardRecipe> getRecipeType() {
        return CuttingBoardRecipe.TYPE;
    }
}
