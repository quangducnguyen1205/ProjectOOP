package com.projectoop.state;

import com.badlogic.gdx.math.Vector2;
import com.projectoop.entity.Enemy;
import com.projectoop.utils.Constants;

import java.util.Random;

public class PatrolState implements IState<Enemy> {
    private static final PatrolState INSTANCE = new PatrolState();

    private PatrolState() {}

    public static PatrolState getInstance() {
        return INSTANCE;
    }

    private float timer;
    private float randomTime;
    private float lastObstacleTime = 0f; // Thời gian va chạm obstacle lần cuối
    private static final float MIN_OBSTACLE_INTERVAL = 1.0f; // Khoảng cách thời gian giữa các lần đổi hướng

    @Override
    public void onEnter(Enemy enemy) {
        timer = 0;
        randomTime = new Random().nextFloat() * (6f - 3f) + 4f; // Random từ 4 đến 7 giây
        enemy.changeAnim(Constants.ANIM_RUN, true);
        enemy.setVelocity(new Vector2(0.5f, 0)); // Tốc độ di chuyển
        System.out.println("Entering Patrol State");
    }

    @Override
    public void onExecute(Enemy enemy) {
        timer += enemy.getDeltaTime();

        // Chuyển sang AttackState nếu Player trong phạm vi
        if (enemy.isPlayerInRange()) {
            enemy.changeState(AttackState.getInstance());
            return;
        }

        // Chuyển sang IdleState khi hết thời gian tuần tra
        if (timer >= randomTime) {
            enemy.changeState(IdleState.getInstance());
            return;
        }

        handleMovement(enemy);
    }

    private void handleMovement(Enemy enemy) {
        float currentTime = enemy.getDeltaTime();

        if (enemy.isTouchingObstacle() && currentTime - lastObstacleTime > MIN_OBSTACLE_INTERVAL) {
            // Đổi hướng nếu Enemy va chạm obstacle và đủ thời gian từ lần đổi hướng trước
            Vector2 velocity = enemy.getVelocity();
            velocity.x = -velocity.x; // Đảo hướng
            enemy.setVelocity(velocity);
            lastObstacleTime = currentTime;
            System.out.println("Enemy changed direction in PatrolState.");
        }

        // Cập nhật vận tốc cho Body
        enemy.getBody().setLinearVelocity(enemy.getVelocity());
    }

    @Override
    public void onExit(Enemy enemy) {
        enemy.setVelocity(Vector2.Zero); // Dừng di chuyển khi thoát khỏi PatrolState
        System.out.println("Exiting Patrol State");
    }
}
