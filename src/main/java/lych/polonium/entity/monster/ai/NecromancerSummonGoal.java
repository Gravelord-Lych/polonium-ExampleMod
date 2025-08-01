package lych.polonium.entity.monster.ai;

import lych.polonium.entity.ModEntities;
import lych.polonium.entity.monster.Minion;
import lych.polonium.entity.monster.Necromancer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class NecromancerSummonGoal extends NecromancerGoal {
    public NecromancerSummonGoal(Necromancer necromancer) {
        super(necromancer);
    }

    @Override
    protected boolean meetExtraUseConditions() {
        return necromancer.getSummonCooldown() <= 0;
    }

    @Override
    protected int getSpellColor() {
        return 0x898F61;
    }

    @Override
    protected void performAttack(LivingEntity target) {
        RandomSource random = necromancer.getRandom();
        double randomRad = random.nextDouble() * Math.PI * 2;
        double distance = 1.5F + random.nextDouble() * 0.5F;
        double xOffset = Math.cos(randomRad) * distance;
        double zOffset = Math.sin(randomRad) * distance;
        Level level = necromancer.level();
        BlockPos pos1, pos2;
        for (int i = 0; i < 4; i++) {
            pos1 = new BlockPos((int) (necromancer.getX() + xOffset), (int) necromancer.getY(), (int) (necromancer.getZ() + zOffset));
            pos2 = new BlockPos((int) (necromancer.getX() - xOffset), (int) necromancer.getY(), (int) (necromancer.getZ() - zOffset));
            if (isBlocked(level, pos1) || isBlocked(level, pos2)) {
                xOffset /= 1.5;
                zOffset /= 1.5;
            } else {
                break;
            }
        }
        for (int i = -1; i <= 1; i += 2) {
            Minion minion = ModEntities.MINION.get().create(level);
            if (minion != null) {
                minion.moveTo(necromancer.getX() + xOffset * i, necromancer.getY(), necromancer.getZ() + zOffset * i, necromancer.getYRot(), 0);
                minion.setOwner(necromancer);
                necromancer.level().addFreshEntity(minion);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isBlocked(Level level, BlockPos pos1) {
        return level.getBlockState(pos1).blocksMotion();
    }

    @Override
    public void stop() {
        super.stop();
        necromancer.setSummonCooldown(Necromancer.MAX_SUMMON_COOLDOWN);
    }
}
