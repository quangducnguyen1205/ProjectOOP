package com.projectoop.state;

import com.projectoop.entity.Enemy;

public class AttackState implements IState<Enemy> {
    @Override
    public void onEnter(Enemy enemy) {
        System.out.println("Enemy entered Attack State");
        enemy.velocity.set(0, 0);
    }

    @Override
    public void onExecute(Enemy enemy) {
        enemy.attack();
    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Enemy exited Attack State");
    }
}
