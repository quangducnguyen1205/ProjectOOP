package com.projectoop.state;

import com.badlogic.gdx.math.Vector2;
import com.projectoop.entity.Enemy;

import static com.projectoop.utils.Constants.ANIM_ATTACK;

public class AttackState implements IState<Enemy> {
    private static final AttackState INSTANCE = new AttackState();

    private AttackState() {}

    public static AttackState getInstance() {
        return INSTANCE;
    }

    private float attackCooldown = 0;
    private static final float ATTACK_INTERVAL = 2.0f; // Khoảng thời gian giữa các lần tấn công
    private static final float ATTACK_RADIUS = 0.3f; // Phạm vi tấn công
    private static final float SAFETY_MARGIN = 0.1f; // Ngưỡng an toàn để tránh chuyển đổi liên tục

    @Override
    public void onEnter(Enemy enemy) {
        enemy.changeAnim(ANIM_ATTACK, false);
        attackCooldown = 0;
        System.out.println("Entering Attack State");
    }

    @Override
    public void onExecute(Enemy enemy) {
        float distanceToPlayer = enemy.getDistanceToPlayer();

        // Nếu Enemy trong phạm vi tấn công, thực hiện tấn công
        if (distanceToPlayer <= ATTACK_RADIUS) {
            attackCooldown += enemy.getDeltaTime();
            if (attackCooldown >= ATTACK_INTERVAL) {
                attackCooldown = 0;
                enemy.performAttack();
            }
        } else if (distanceToPlayer > ATTACK_RADIUS + SAFETY_MARGIN) {
            // Nếu Player ra khỏi phạm vi tấn công, quay về PatrolState
            enemy.changeState(PatrolState.getInstance());
        } else {
            // Enemy di chuyển về phía Player
            Vector2 direction = new Vector2(enemy.getPlayer().getPosition()).sub(enemy.getPosition()).nor();
            enemy.setVelocity(direction.scl(0.5f)); // Vận tốc hướng về phía Player
        }
    }

    @Override
    public void onExit(Enemy enemy) {
        enemy.setVelocity(Vector2.Zero); // Dừng Enemy khi thoát trạng thái
        System.out.println("Exiting Attack State");
    }
}
