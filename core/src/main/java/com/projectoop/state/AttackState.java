package com.projectoop.state;

import com.badlogic.gdx.Gdx;
import com.projectoop.entity.Enemy;

public class AttackState implements IState<Enemy> {
    private float attackCooldown = 0;
    private static final float ATTACK_INTERVAL = 1.0f; // Khoảng thời gian giữa các lần tấn công

    @Override
    public void onEnter(Enemy enemy) {
        enemy.changeAnim("attack", false); // Chuyển sang trạng thái tấn công
        System.out.println("Entering Attack State");
    }

    @Override
    public void onExecute(Enemy enemy) {
        attackCooldown += Gdx.graphics.getDeltaTime(); // Sử dụng deltaTime toàn cục

        if (attackCooldown > ATTACK_INTERVAL) {
            attackCooldown = 0;
            performAttack(enemy); // Tấn công
        }

        // Nếu Player rời khỏi phạm vi, quay lại PatrolState
        if (!isPlayerInRange(enemy)) {
            enemy.getStateMachine().changeState(new PatrolState());
        }
    }

    @Override
    public void onExit(Enemy enemy) {
        System.out.println("Exiting Attack State");
    }

    private void performAttack(Enemy enemy) {
        // Thực hiện logic tấn công (cần bổ sung logic thực tế)
        System.out.println("Enemy attacks!");
    }

    private boolean isPlayerInRange(Enemy enemy) {
        // Kiểm tra Player trong phạm vi (thay thế bằng logic thực tế)
        return Math.random() > 0.5; // Ví dụ ngẫu nhiên
    }
}
