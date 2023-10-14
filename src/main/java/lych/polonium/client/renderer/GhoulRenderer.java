package lych.polonium.client.renderer;

import lych.polonium.Utils;
import lych.polonium.client.ModModelLayers;
import lych.polonium.entity.monster.Ghoul;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhoulRenderer extends HumanoidMobRenderer<Ghoul, HumanoidModel<Ghoul>> {
    private static final ResourceLocation GHOUL = Utils.prefix("textures/entity/ghoul.png");

    public GhoulRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModModelLayers.GHOUL)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Ghoul ghoul) {
        return GHOUL;
    }
}
