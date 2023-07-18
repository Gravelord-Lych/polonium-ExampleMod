package lych.polonium.client;

import lych.polonium.Utils;
import lych.polonium.entity.ModEntityNames;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ModModelLayers {
    public static final ModelLayerLocation REINFORCED_ZOMBIE = createMain(ModEntityNames.REINFORCED_ZOMBIE);
    public static final ModelLayerLocation REINFORCED_ZOMBIE_INNER_ARMOR = createInnerArmor(ModEntityNames.REINFORCED_ZOMBIE);
    public static final ModelLayerLocation REINFORCED_ZOMBIE_OUTER_ARMOR = createOuterArmor(ModEntityNames.REINFORCED_ZOMBIE);

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
