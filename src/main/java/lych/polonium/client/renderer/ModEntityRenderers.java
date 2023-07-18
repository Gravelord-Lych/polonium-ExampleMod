package lych.polonium.client.renderer;

import lych.polonium.Polonium;
import lych.polonium.client.ModModelLayers;
import lych.polonium.entity.ModEntities;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Polonium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.REINFORCED_ZOMBIE.get(), ReinforcedZombieRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        Supplier<LayerDefinition> zombie = () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);
        Supplier<LayerDefinition> innerArmor = () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32);
        Supplier<LayerDefinition> outerArmor = () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32);
        event.registerLayerDefinition(ModModelLayers.REINFORCED_ZOMBIE, zombie);
        event.registerLayerDefinition(ModModelLayers.REINFORCED_ZOMBIE_INNER_ARMOR, innerArmor);
        event.registerLayerDefinition(ModModelLayers.REINFORCED_ZOMBIE_OUTER_ARMOR, outerArmor);
    }
}
