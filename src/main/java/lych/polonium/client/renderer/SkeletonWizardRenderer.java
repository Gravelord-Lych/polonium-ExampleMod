package lych.polonium.client.renderer;

import lych.polonium.Utils;
import lych.polonium.client.ModModelLayers;
import lych.polonium.client.layer.SkeletonWizardEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class SkeletonWizardRenderer extends SkeletonRenderer {
    private static final ResourceLocation SKELETON_WIZARD = Utils.prefix("textures/entity/skeleton_wizard/skeleton_wizard.png");

    public SkeletonWizardRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModModelLayers.SKELETON_WIZARD,
                ModModelLayers.SKELETON_WIZARD_INNER_ARMOR,
                ModModelLayers.SKELETON_WIZARD_OUTER_ARMOR);
        addLayer(new SkeletonWizardEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton skeleton) {
        return SKELETON_WIZARD;
    }
}
