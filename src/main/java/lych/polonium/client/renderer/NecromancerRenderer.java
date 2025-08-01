package lych.polonium.client.renderer;

import lych.polonium.Utils;
import lych.polonium.client.ModModelLayers;
import lych.polonium.client.model.NecromancerModel;
import lych.polonium.entity.monster.Necromancer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NecromancerRenderer extends HumanoidMobRenderer<Necromancer, NecromancerModel> {
    private static final ResourceLocation NECROMANCER = Utils.prefix("textures/entity/necromancer.png");

    public NecromancerRenderer(EntityRendererProvider.Context context) {
        super(context, new NecromancerModel(context.bakeLayer(ModModelLayers.NECROMANCER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Necromancer necromancer) {
        return NECROMANCER;
    }
}
