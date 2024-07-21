package lych.polonium.client.renderer;

import lych.polonium.Utils;
import lych.polonium.client.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReinforcedZombieRenderer extends ZombieRenderer {
    private static final ResourceLocation REINFORCED_ZOMBIE = Utils.prefix("textures/entity/reinforced_zombie.png");

    public ReinforcedZombieRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModModelLayers.REINFORCED_ZOMBIE,
                ModModelLayers.REINFORCED_ZOMBIE_INNER_ARMOR,
                ModModelLayers.REINFORCED_ZOMBIE_OUTER_ARMOR);
    }
/*
    @Override
    public void setupAnim(T skeleton, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(skeleton, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack stack = skeleton.getMainHandItem();
        if (skeleton.isAggressive() && (stack.isEmpty() || !stack.is(Items.BOW))) {
            float f = Mth.sin(attackTime * (float) Math.PI);
            float f1 = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float) Math.PI);
            rightArm.zRot = 0.0F;
            leftArm.zRot = 0.0F;
            rightArm.yRot = -(0.1F - f * 0.6F);
            leftArm.yRot = 0.1F - f * 0.6F;
            rightArm.xRot = -(float) Math.PI / 2F;
            leftArm.xRot = -(float) Math.PI / 2F;
            rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            AnimationUtils.bobArms(rightArm, leftArm, ageInTicks);
        }
    }
*/
    /*
    private void poseRightArm(T entity) {
        case BOW_AND_ARROW:
            rightArm.yRot = -0.1F + head.yRot;
            leftArm.yRot = 0.1F + head.yRot + 0.4F;
            rightArm.xRot = (-(float) Math.PI / 2F) + head.xRot;
            leftArm.xRot = (-(float) Math.PI / 2F) + head.xRot;
        break;
    }

    private void poseLeftArm(T entity) {
        case BOW_AND_ARROW:
            rightArm.yRot = -0.1F + head.yRot - 0.4F;
            leftArm.yRot = 0.1F + head.yRot;
            rightArm.xRot = (-(float) Math.PI / 2F) + head.xRot;
            leftArm.xRot = (-(float) Math.PI / 2F) + head.xRot;
        break;
    }
*/
    @Override
    public ResourceLocation getTextureLocation(Zombie zombie) {
        return REINFORCED_ZOMBIE;
    }
}
