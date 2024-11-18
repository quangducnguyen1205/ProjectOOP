package com.projectoop.state;

import com.badlogic.gdx.Gdx;
import com.projectoop.entity.Enemy;

public class PatrolState implements IState<Enemy> {
    private float patrolTime = 0; // Thời gian Enemy di chuyển
    private static final float MAX_PATROL_TIME = 3.0f; // Thời gian di chuyển tối đa

    @Override
    public void onEnter(Enemy enemy) {
        enemy.changeAnim("run", true); // Bắt đầu trạng thái chạy
        System.out.println("Entering Patrol State");
    }

    @Override
    public void onExecute(Enemy enemy) {
        patrolTime += Gdx.graphics.getDeltaTime(); // Sử dụng deltaTime toàn cục

        // Thực hiện di chuyển
        if (patrolTime > MAX_PATROL_TIME) {
            patrolTime = 0;
            enemy.setVelocity(enemy.getVelocity().scl(-1)); // Đảo hướng
        }

        // Chuyển sang AttackState nếu phát hiện Player (cần bổ sung logic kiểm tra Player)
        if (isPlayerInRange(enemy)) {
            enemy.getStateMachine().changeState(new AttackState());
        }
    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Exiting Patrol State");
    }

    private boolean isPlayerInRange(Enemy enemy) {
        // Kiểm tra Player trong phạm vi (thay thế bằng logic thực tế)
        return Math.random() > 0.9; // Ví dụ ngẫu nhiên
    }
}
