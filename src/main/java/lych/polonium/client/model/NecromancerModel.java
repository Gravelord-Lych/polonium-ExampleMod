package lych.polonium.client.model;

import lych.polonium.entity.monster.Necromancer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class NecromancerModel extends HumanoidModel<Necromancer> {
    public NecromancerModel(ModelPart part) {
        super(part);
    }

    @Override
    public void setupAnim(Necromancer necromancer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(necromancer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (necromancer.isCastingSpell()) {
            if (necromancer.isLeftHanded()) {
                leftArm.z = 0.0F;
                leftArm.x = 5.0F;
                leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
                leftArm.zRot = -2.3561945F;
                leftArm.yRot = 0.0F;
            } else {
                rightArm.z = 0.0F;
                rightArm.x = -5.0F;
                rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
                rightArm.zRot = 2.3561945F;
                rightArm.yRot = 0.0F;
            }
        }
    }
}
