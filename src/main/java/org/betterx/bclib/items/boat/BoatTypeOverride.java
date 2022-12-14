package org.betterx.bclib.items.boat;

import org.betterx.bclib.BCLib;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class BoatTypeOverride {
    private static final String DEFAULT_LAYER = "main";

    private static final List<BoatTypeOverride> values = new ArrayList<>(8);
    private final String name;
    private final Block planks;
    private final int ordinal;
    public final ResourceLocation id;
    public final ResourceLocation boatTexture;
    public final ResourceLocation chestBoatTexture;
    public final ModelLayerLocation boatModelName;
    public final ModelLayerLocation chestBoatModelName;
    private BoatModel boatModel, chestBoatModel;
    private BoatItem boat, chestBoat;

    BoatTypeOverride(String modID, String name, Block planks) {
        this.id = new ResourceLocation(modID, name);
        this.name = name;
        this.planks = planks;
        int nr = Objects.hash(name);
        if (nr >= 0 && nr <= 1000) nr += 1000;
        while (byId(nr) != null) {
            BCLib.LOGGER.warning("Boat Type Ordinal " + nr + " is already used, searching for another one");
            nr++;
            if (nr >= 0 && nr <= 1000) nr += 1000;
        }
        this.ordinal = nr;
        if (BCLib.isClient()) {
            this.boatModelName = createBoatModelName(id.getNamespace(), id.getPath());
            this.chestBoatModelName = createChestBoatModelName(id.getNamespace(), id.getPath());
            this.boatTexture = getTextureLocation(modID, name, false);
            this.chestBoatTexture = getTextureLocation(modID, name, true);
        } else {
            this.boatModelName = null;
            this.chestBoatModelName = null;
            this.boatTexture = null;
            this.chestBoatTexture = null;
        }

        values.add(this);
    }

    public BoatModel getBoatModel(boolean chest) {
        return chest ? chestBoatModel : boatModel;
    }

    public void createBoatModels(EntityRendererProvider.Context context) {
        if (boatModel == null) {
            boatModel = new BoatModel(context.bakeLayer(boatModelName), false);
            chestBoatModel = new BoatModel(context.bakeLayer(chestBoatModelName), true);
        }
    }

    public Block getPlanks() {
        return planks;
    }

    public void setBoatItem(BoatItem item) {
        this.boat = item;
    }

    public BoatItem getBoatItem() {
        return boat;
    }

    public void setChestBoatItem(BoatItem item) {
        this.chestBoat = item;
    }

    public BoatItem getChestBoatItem() {
        return chestBoat;
    }

    public static Stream<BoatTypeOverride> values() {
        return values.stream();
    }

    private static ModelLayerLocation createBoatModelName(String modID, String name) {
        return new ModelLayerLocation(new ResourceLocation(modID, "boat/" + name), DEFAULT_LAYER);
    }

    private static ModelLayerLocation createChestBoatModelName(String modID, String name) {
        return new ModelLayerLocation(new ResourceLocation(modID, "chest_boat/" + name), DEFAULT_LAYER);
    }

    private static ResourceLocation getTextureLocation(String modID, String name, boolean chest) {
        if (chest) {
            return new ResourceLocation(modID, "textures/entity/chest_boat/" + name + ".png");
        }
        return new ResourceLocation(modID, "textures/entity/boat/" + name + ".png");
    }

    public static BoatTypeOverride create(String modID, String name, Block planks) {
        BoatTypeOverride t = new BoatTypeOverride(modID, name, planks);

        return t;
    }

    public BoatItem createItem(boolean hasChest) {
        return createItem(hasChest, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION));
    }

    public BoatItem createItem(boolean hasChest, Item.Properties itemSettings) {
        BoatItem item = new BaseBoatItem(hasChest, this, itemSettings);

        if (hasChest) this.setChestBoatItem(item);
        else this.setBoatItem(item);

        return item;
    }

    public static BoatTypeOverride byId(int i) {
        for (BoatTypeOverride t : values) {
            if (t.ordinal == i) return t;
        }
        return null;
    }

    public static BoatTypeOverride byName(String string) {
        for (BoatTypeOverride t : values) {
            if (!t.name().equals(string)) continue;
            return t;
        }
        return null;
    }

    public String name() {
        return name;
    }

    public int ordinal() {
        return ordinal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BoatTypeOverride) obj;
        return Objects.equals(this.name, that.name) &&
                this.ordinal == that.ordinal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ordinal);
    }

    @Override
    public String toString() {
        return "BoatTypeOverride[" +
                "name=" + name + ", " +
                "ordinal=" + ordinal + ']';
    }

}