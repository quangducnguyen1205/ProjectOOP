package com.projectoop.state;

import com.projectoop.entity.Enemy;

public class AttackState implements IState<Enemy> {
    private static final AttackState INSTANCE = new AttackState();

    private AttackState() {}

    public static AttackState getInstance() {
        return INSTANCE;
    }
    private float timer;

    @Override
    public void onEnter(Enemy enemy) {
        if (enemy.getPlayer() != null) {
            // Đổi hướng của Enemy về phía Player
            boolean isPlayerToRight = enemy.getPlayer().getPosition().x > enemy.getPosition().x;
            enemy.changeDirection(isPlayerToRight);

            // Dừng di chuyển và thực hiện tấn công
            enemy.stopMoving();
            enemy.attack();
        }
        timer = 0; // Đặt lại bộ đếm thời gian
        System.out.println("Entering Attack State");
    }

    @Override
    public void onExecute(Enemy enemy) {

        timer += enemy.getDeltaTime();
        if (timer > 1.5f) { // Quay lại PatrolState sau 1.5 giây
            enemy.changeState(PatrolState.getInstance());
        }
    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Exiting Attack State");
    }
}
