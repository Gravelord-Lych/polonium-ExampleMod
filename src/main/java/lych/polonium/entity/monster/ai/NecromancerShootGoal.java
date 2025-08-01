package lych.polonium.entity.monster.ai;

import lych.polonium.entity.monster.Necromancer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;

public class NecromancerShootGoal extends NecromancerGoal {
    private static final int ATTACK_INTERVAL_SMALL = 12;
    private static final int ATTACK_INTERVAL_BIG = 40;
    private static final int MAX_ATTACK_COUNT = 3;
    private int attackCount;

    public NecromancerShootGoal(Necromancer necromancer) {
        super(necromancer);
    }

    @Override
    protected boolean meetExtraUseConditions() {
        return true;
    }

    @Override
    protected int getSpellColor() {
        return 0x101010;
    }

    @Override
    protected void performAttack(LivingEntity target) {
        double dx = target.getX() - necromancer.getX();
        double dy = target.getY(0.5) - necromancer.getEyeY();
        double dz = target.getZ() - necromancer.getZ();
        boolean dangerous = attackCount == MAX_ATTACK_COUNT - 1 && necromancer.getRandom().nextDouble() < getDangerousProbability();
        for (int i = -30; i <= 30; i += 30) {
            Vec3 direction = new Vec3(dx, dy, dz).yRot((float) Math.toRadians(i));
            WitherSkull skull = new WitherSkull(necromancer.level(), necromancer, direction.x, direction.y, direction.z);
            skull.setPos(necromancer.getX(), necromancer.getEyeY(), necromancer.getZ());
            if (dangerous) {
                skull.setDangerous(true);
            }
            necromancer.level().addFreshEntity(skull);
        }
        attackCount++;
        if (attackCount < MAX_ATTACK_COUNT) {
            ticksRemaining = ATTACK_INTERVAL_SMALL;
        } else {
            attackCount = 0;
            ticksRemaining = ATTACK_INTERVAL_BIG;
        }
    }

    private double getDangerousProbability() {
        return switch (necromancer.level().getDifficulty()) {
            case PEACEFUL, EASY -> 0;
            case NORMAL -> 0.01;
            case HARD -> 0.2;
        };
    }
}
