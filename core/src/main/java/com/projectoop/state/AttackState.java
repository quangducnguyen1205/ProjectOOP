package com.projectoop.state;

import com.projectoop.entity.Enemy;
import com.projectoop.utils.Constants;

public class AttackState implements IState<Enemy> {
    private static final AttackState INSTANCE = new AttackState();

    private AttackState() {}

    public static AttackState getInstance() {
        return INSTANCE;
    }
    private float timer;

    @Override
    public void onEnter(Enemy enemy) {
        System.out.println("Entering Attack State");
        if (enemy.getPlayer() != null) {
            enemy.changeAnim(Constants.ANIM_ATTACK, false); // Chuyển sang animation Attack
            // Đổi hướng của Enemy về phía Player
            boolean isPlayerToRight = enemy.getPlayer().getPosition().x > enemy.getPosition().x;
            enemy.changeDirection(isPlayerToRight);

            // Dừng di chuyển và thực hiện tấn công
            enemy.stopMoving();
            enemy.attack();
        }
        timer = 0; // Đặt lại bộ đếm thời gian
    }

    @Override
    public void onExecute(Enemy enemy) {
        timer += enemy.getDeltaTime();

        // Kiểm tra nếu Player vẫn trong phạm vi tấn công
        if (enemy.isPlayerInRange()) {
            enemy.attack(); // Gây sát thương nếu Player trong phạm vi
        }

        // Quay lại PatrolState sau 1.5 giây
        if (timer > 1.5f) {
            enemy.changeState(PatrolState.getInstance());
        }
    }


    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Exiting Attack State");
    }
}
