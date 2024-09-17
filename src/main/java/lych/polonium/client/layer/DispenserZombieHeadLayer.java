package lych.polonium.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lych.polonium.entity.monster.DispenserZombie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DispenserZombieHeadLayer extends RenderLayer<DispenserZombie, ZombieModel<DispenserZombie>> {
    private final BlockRenderDispatcher blockRenderer;
    private final ItemRenderer itemRenderer;

    public DispenserZombieHeadLayer(RenderLayerParent<DispenserZombie, ZombieModel<DispenserZombie>> parent, BlockRenderDispatcher blockRenderer, ItemRenderer itemRenderer) {
        super(parent);
        this.blockRenderer = blockRenderer;
        this.itemRenderer = itemRenderer;
    }

    /**
     * [VanillaCopy]
     * <p>
     * {@link net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer#render(PoseStack, MultiBufferSource, int, SnowGolem, float, float, float, float, float, float)}
     */
    @SuppressWarnings("deprecation")
    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, DispenserZombie zombie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean invisibleButGlowing = Minecraft.getInstance().shouldEntityAppearGlowing(zombie) && zombie.isInvisible();
        if (!zombie.isInvisible() || invisibleButGlowing) {
            poseStack.pushPose();
            getParentModel().getHead().translateAndRotate(poseStack);
            poseStack.translate(0.0F, -0.34375F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(0.625F, -0.625F, -0.625F);
            Block dispenserHeadBlock = Blocks.DISPENSER;
            ItemStack itemStack = new ItemStack(dispenserHeadBlock);
            if (invisibleButGlowing) {
                BlockState dispenserHead = dispenserHeadBlock.defaultBlockState();
                BakedModel dispenserHeadModel = blockRenderer.getBlockModel(dispenserHead);
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(zombie, 0.0F);
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                blockRenderer.getModelRenderer().renderModel(poseStack.last(), source.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), dispenserHead, dispenserHeadModel, 0.0F, 0.0F, 0.0F, packedLight, overlayCoords);
            } else {
                itemRenderer.renderStatic(zombie, itemStack, ItemDisplayContext.HEAD, false, poseStack, source, zombie.level(), packedLight, LivingEntityRenderer.getOverlayCoords(zombie, 0.0F), zombie.getId());
            }
            poseStack.popPose();
        }
    }
}
