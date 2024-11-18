package com.projectoop.state;

import com.badlogic.gdx.math.Vector2;
import com.projectoop.entity.Enemy;

public class PatrolState implements IState<Enemy> {
    @Override
    public void onEnter(Enemy enemy) {
        System.out.println("Enemy entered Patrol State");
        enemy.velocity = new Vector2(1, 0);
    }

    @Override
    public void onExecute(Enemy enemy) {
        if (enemy.b2body.getPosition().x < enemy.getX() - 2 ||
            enemy.b2body.getPosition().x > enemy.getX() + 2) {
            enemy.reverseVelocity(true, false);
        }
    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Enemy exited Patrol State");
    }
}
