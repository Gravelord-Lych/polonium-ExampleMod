package lych.polonium.client.renderer;

import lych.polonium.client.ModModelLayers;
import lych.polonium.client.layer.DispenserZombieHeadLayer;
import lych.polonium.entity.monster.DispenserZombie;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DispenserZombieRenderer extends AbstractZombieRenderer<DispenserZombie, ZombieModel<DispenserZombie>> {
    public DispenserZombieRenderer(EntityRendererProvider.Context context) {
        super(context,
                new ZombieModel<>(context.bakeLayer(ModModelLayers.DISPENSER_ZOMBIE)),
                new ZombieModel<>(context.bakeLayer(ModModelLayers.DISPENSER_ZOMBIE_INNER_ARMOR)),
                new ZombieModel<>(context.bakeLayer(ModModelLayers.DISPENSER_ZOMBIE_OUTER_ARMOR)));
        addLayer(new DispenserZombieHeadLayer(this, context.getBlockRenderDispatcher(), context.getItemRenderer()));
    }
}
