package com.projectoop.state;

import com.projectoop.entity.Enemy;

import java.util.Random;

public class PatrolState implements IState<Enemy> {
    private static final PatrolState INSTANCE = new PatrolState();

    private PatrolState() {}

    public static PatrolState getInstance() {
        return INSTANCE;
    }

    private float timer;
    private float randomTime;

    @Override
    public void onEnter(Enemy enemy) {
        timer = 0;
        randomTime = new Random().nextFloat() * (6f - 3f) + 4f; // Random từ 4 đến 7 giây
        System.out.println("Entering Idle State");
    }

    @Override
    public void onExecute(Enemy enemy) {
        timer += enemy.getDeltaTime();

        // Chuyển sang AttackState nếu Player trong phạm vi
        if (enemy.isPlayerInRange()) {
            enemy.changeState(AttackState.getInstance());
        } else {
            enemy.move();
        }

        // Chuyển sang IdleState khi hết thời gian tuần tra
        if (timer >= randomTime) {
            enemy.changeState(IdleState.getInstance());
        }

    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Exiting Patrol State");
    }
}
