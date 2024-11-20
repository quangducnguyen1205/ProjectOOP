package com.projectoop.state;

import com.projectoop.entity.Enemy;

import java.util.Random;

public class IdleState implements IState<Enemy> {
    private static final IdleState INSTANCE = new IdleState();

    private IdleState() {}

    public static IdleState getInstance() {
        return INSTANCE;
    }

    private float timer;
    private float randomTime;

    @Override
    public void onEnter(Enemy enemy) {
        timer = 0;
        randomTime = new Random().nextFloat() * (4f - 2f) + 2f; // Random từ 2 đến 4 giây
        enemy.stopMoving();
        System.out.println("Entering Idle State");
    }

    @Override
    public void onExecute(Enemy enemy) {
        timer += enemy.getDeltaTime(); // Cập nhật thời gian trong trạng thái Idle

        // Nếu Player trong phạm vi, chuyển sang AttackState
        if (enemy.isPlayerInRange()) {
            enemy.changeState(AttackState.getInstance());
            return;
        }

        // Chuyển sang PatrolState sau khi hết thời gian chờ
        if (timer >= randomTime) {
            enemy.changeState(PatrolState.getInstance());
        }
    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Exiting Idle State");
    }
}
