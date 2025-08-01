package lych.polonium.client.renderer;

import lych.polonium.Utils;
import lych.polonium.client.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class MinionRenderer extends ZombieRenderer {
    private static final ResourceLocation MINION = Utils.prefix("textures/entity/minion.png");

    public MinionRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModModelLayers.MINION,
                ModModelLayers.MINION_INNER_ARMOR,
                ModModelLayers.MINION_OUTER_ARMOR);
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie zombie) {
        return MINION;
    }
}
