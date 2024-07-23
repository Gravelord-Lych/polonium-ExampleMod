package lych.polonium.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lych.polonium.Utils;
import lych.polonium.entity.monster.SkeletonWizard;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkeletonWizardEyesLayer extends RenderLayer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>> {
    private static final ResourceLocation SKELETON_WIZARD_EYES = Utils.prefix("textures/entity/skeleton_wizard/skeleton_wizard_eyes.png");
    private static final RenderType SKELETON_WIZARD_EYES_RENDER_TYPE = RenderType.entityCutoutNoCull(SKELETON_WIZARD_EYES);

    public SkeletonWizardEyesLayer(RenderLayerParent<AbstractSkeleton, SkeletonModel<AbstractSkeleton>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, AbstractSkeleton skeleton, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(skeleton instanceof SkeletonWizard wizard)) {
            throw new IllegalArgumentException("SkeletonWizardEyesLayer can only render SkeletonWizard, found: " + skeleton.getClass().getSimpleName());
        }
        if (wizard.isReinforced() && !wizard.isInvisible()) {
            VertexConsumer consumer = source.getBuffer(SKELETON_WIZARD_EYES_RENDER_TYPE);
            getParentModel().renderToBuffer(poseStack, consumer, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }
    }
}
