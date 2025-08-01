package lych.polonium.client;

import lych.polonium.Utils;
import lych.polonium.entity.ModEntityNames;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class ModModelLayers {
    public static final ModelLayerLocation DISPENSER_ZOMBIE = createMain(ModEntityNames.DISPENSER_ZOMBIE);
    public static final ModelLayerLocation DISPENSER_ZOMBIE_INNER_ARMOR = createInnerArmor(ModEntityNames.DISPENSER_ZOMBIE);
    public static final ModelLayerLocation DISPENSER_ZOMBIE_OUTER_ARMOR = createOuterArmor(ModEntityNames.DISPENSER_ZOMBIE);
    public static final ModelLayerLocation GHOUL = createMain(ModEntityNames.GHOUL);
    public static final ModelLayerLocation MINION = createMain(ModEntityNames.MINION);
    public static final ModelLayerLocation MINION_INNER_ARMOR = createInnerArmor(ModEntityNames.MINION);
    public static final ModelLayerLocation MINION_OUTER_ARMOR = createOuterArmor(ModEntityNames.MINION);
    public static final ModelLayerLocation NECROMANCER = createMain(ModEntityNames.NECROMANCER);
    public static final ModelLayerLocation REINFORCED_ZOMBIE = createMain(ModEntityNames.REINFORCED_ZOMBIE);
    public static final ModelLayerLocation REINFORCED_ZOMBIE_INNER_ARMOR = createInnerArmor(ModEntityNames.REINFORCED_ZOMBIE);
    public static final ModelLayerLocation REINFORCED_ZOMBIE_OUTER_ARMOR = createOuterArmor(ModEntityNames.REINFORCED_ZOMBIE);
    public static final ModelLayerLocation SKELETON_WIZARD = createMain(ModEntityNames.SKELETON_WIZARD);
    public static final ModelLayerLocation SKELETON_WIZARD_INNER_ARMOR = createInnerArmor(ModEntityNames.SKELETON_WIZARD);
    public static final ModelLayerLocation SKELETON_WIZARD_OUTER_ARMOR = createOuterArmor(ModEntityNames.SKELETON_WIZARD);

    private ModModelLayers() {}

    private static ModelLayerLocation createMain(String model) {
        return create(model, "main");
    }

    private static ModelLayerLocation createInnerArmor(String model) {
        return create(model, "inner_armor");
    }

    private static ModelLayerLocation createOuterArmor(String model) {
        return create(model, "outer_armor");
    }

    private static ModelLayerLocation create(String model, String layer) {
        return new ModelLayerLocation(Utils.prefix(model), layer);
    }
}
