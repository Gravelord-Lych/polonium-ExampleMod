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

    @Override
    public ResourceLocation getTextureLocation(Zombie zombie) {
        return REINFORCED_ZOMBIE;
    }
}
