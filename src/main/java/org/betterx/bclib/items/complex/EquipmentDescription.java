package org.betterx.bclib.items.complex;

import org.betterx.bclib.items.BaseArmorItem;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.bclib.recipes.GridRecipe;
import org.betterx.bclib.registry.ItemRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;

import java.util.function.Function;

public class EquipmentDescription<I extends Item> {
    private final Function<Tier, I> creator;
    private I item;

    public EquipmentDescription(Function<Tier, I> creator) {
        this.creator = creator;
    }

    public void init(ResourceLocation id, ItemRegistry itemsRegistry, Tier material, ItemLike stick) {
        item = creator.apply(material);
        itemsRegistry.registerTool(id, item);

        addRecipe(id, item, material, stick);
    }

    public void addRecipe(ResourceLocation id, Item tool, Tier material, ItemLike stick) {
        if (material == null) return;
        var repair = material.getRepairIngredient();
        if (repair == null) return;
        var repairItems = repair.getItems();
        if (repairItems == null || repairItems.length == 0) return;
        final ItemLike ingot = repairItems[0].getItem();

        var builder = BCLRecipeBuilder.crafting(id, tool)
                                      .addMaterial('#', ingot);

        if (buildRecipe(tool, stick, builder)) return;
        builder
                .setGroup(id.getPath())
                .build();

    }

    protected boolean buildRecipe(Item tool, ItemLike stick, GridRecipe builder) {
        if (tool instanceof ShearsItem) {
            builder.setShape(" #", "# ");
        } else if (tool instanceof BaseArmorItem bai) {
            if (bai.getSlot() == EquipmentSlot.FEET) {
                builder.setShape("# #", "# #");
            } else if (bai.getSlot() == EquipmentSlot.HEAD) {
                builder.setShape("###", "# #");
            } else if (bai.getSlot() == EquipmentSlot.CHEST) {
                builder.setShape("# #", "###", "###");
            } else if (bai.getSlot() == EquipmentSlot.LEGS) {
                builder.setShape("###", "# #", "# #");
            } else return true;
        } else {
            builder.addMaterial('I', stick);
            if (tool instanceof PickaxeItem) {
                builder.setShape("###", " I ", " I ");
            } else if (tool instanceof AxeItem) {
                builder.setShape("##", "#I", " I");
            } else if (tool instanceof HoeItem) {
                builder.setShape("##", " I", " I");
            } else if (tool instanceof ShovelItem) {
                builder.setShape("#", "I", "I");
            } else if (tool instanceof SwordItem) {
                builder.setShape("#", "#", "I");
            } else return true;
        }
        return false;
    }

    public I getItem() {
        return item;
    }
}
