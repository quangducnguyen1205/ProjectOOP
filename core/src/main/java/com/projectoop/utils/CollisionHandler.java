package com.projectoop.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.projectoop.entity.Enemy;
import com.projectoop.game.Player;
import com.projectoop.ui.LevelCompleteCanvas;
import com.projectoop.ui.UIManager;

public class CollisionHandler implements ContactListener {
    private static final float MIN_OBSTACLE_DISTANCE = 0.2f; // Khoảng cách tối thiểu trước khi đổi hướng
    private float lastObstacleCollisionTime = 0f;

    public CollisionHandler(World world) {
        world.setContactListener(this);
    }

    @Override
    public void beginContact(Contact contact) {
        // Lấy hai Fixture trong va chạm
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Xử lý va chạm
        handleCollision(fixA, fixB, true);
    }

    @Override
    public void endContact(Contact contact) {
        // Lấy hai Fixture trong va chạm
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Xử lý khi kết thúc va chạm
        handleCollision(fixA, fixB, false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Không cần xử lý
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Không cần xử lý
    }

    private void handleCollision(Fixture fixA, Fixture fixB, boolean isBeginning) {
        // Lấy `categoryBits` của các Fixture
        short categoryA = fixA.getFilterData().categoryBits;
        short categoryB = fixB.getFilterData().categoryBits;

        // Xử lý va chạm giữa `Player` và `Ground`
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.GROUND_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.GROUND_BIT)) {
            handlePlayerGroundCollision(fixA, fixB, isBeginning);
        }

        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.PIPES_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.PIPES_BIT)) {
            handlePlayerGroundCollision(fixA, fixB, isBeginning);
        }

        // Xử lý va chạm giữa `Player` và `KillZone`
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.KILLZONE_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.KILLZONE_BIT)) {
            handlePlayerKillZoneCollision(fixA, fixB);
        }

        // Xử lý va chạm giữa `Player` và `FinishZone`
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.FINISHZONE_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.FINISHZONE_BIT)) {
            handlePlayerFinishZoneCollision(fixA, fixB);
        }

        // Xử lý va chạm giữa `Enemy` và `Ground`
        if ((categoryA == Constants.ENEMY_BIT && categoryB == Constants.GROUND_BIT) ||
            (categoryB == Constants.ENEMY_BIT && categoryA == Constants.GROUND_BIT)) {
            handleEnemyGroundCollision(fixA, fixB, isBeginning);
        }

        // Xử lý va chạm giữa `Enemy` và `Player`
        if ((categoryA == Constants.ENEMY_BIT && categoryB == Constants.PLAYER_BIT) ||
            (categoryB == Constants.ENEMY_BIT && categoryA == Constants.PLAYER_BIT)) {
            handleEnemyPlayerCollision(fixA, fixB);
        }

        // Xử lý va chạm giữa `Enemy` và `Obstacle`
        if ((categoryA == Constants.ENEMY_BIT && categoryB == Constants.PIPES_BIT) ||
            (categoryB == Constants.ENEMY_BIT && categoryA == Constants.PIPES_BIT)) {
            handleEnemyObstacleCollision(fixA, fixB);
        }
    }

    private void handleEnemyGroundCollision(Fixture fixA, Fixture fixB, boolean isBeginning) {
        // Xác định Fixture nào là của Enemy
        Fixture enemyFixture = (fixA.getFilterData().categoryBits == Constants.ENEMY_BIT) ? fixA : fixB;

        if (enemyFixture.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) enemyFixture.getUserData();
            enemy.setOnGround(isBeginning); // Cập nhật trạng thái tiếp đất của Enemy
        }
    }

    private static final float DAMAGE_COOLDOWN = 1.0f; // Giới hạn thời gian giữa các lần tấn công
    private float lastAttackTime = 0f;

    private void handleEnemyPlayerCollision(Fixture fixA, Fixture fixB) {
        Fixture enemyFixture = (fixA.getFilterData().categoryBits == Constants.ENEMY_BIT) ? fixA : fixB;

        if (enemyFixture.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) enemyFixture.getUserData();
            Player player = enemy.getPlayer();

            if (player != null && !player.isDying() && (TimeUtils.nanoTime() - lastAttackTime) > DAMAGE_COOLDOWN * 1000000000) {
                lastAttackTime = TimeUtils.nanoTime();
                // player.takeDamage(10);  // Gây sát thương cho Player
                System.out.println("Player hit by Enemy!");
            }
        }
    }


    private void handleEnemyObstacleCollision(Fixture fixA, Fixture fixB) {
        Fixture enemyFixture = (fixA.getFilterData().categoryBits == Constants.ENEMY_BIT) ? fixA : fixB;

        if (enemyFixture.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy) enemyFixture.getUserData();
            Vector2 currentVelocity = enemy.getVelocity();
            float currentTime = TimeUtils.nanoTime() / 1e9f;

            // Chỉ đổi hướng nếu thời gian đủ lâu kể từ lần va chạm gần nhất
            if ((currentTime - lastObstacleCollisionTime > 0.5f) &&
                currentVelocity.len() > MIN_OBSTACLE_DISTANCE) {
                enemy.changeDirection(enemy.getPlayer().getPosition().x > enemy.getPosition().x);
                lastObstacleCollisionTime = currentTime; // Cập nhật thời gian
                System.out.println("Enemy changed direction: " + currentVelocity);
            }
        }
    }

    private void handlePlayerGroundCollision(Fixture fixA, Fixture fixB, boolean isBeginning) {
        // Xác định Fixture nào là của Player
        Fixture playerFixture = (fixA.getFilterData().categoryBits == Constants.PLAYER_BIT) ? fixA : fixB;

        // Kiểm tra nếu Fixture có UserData là Player
        if (playerFixture.getUserData() instanceof Player) {
            Player player = (Player) playerFixture.getUserData();
            player.setOnGround(isBeginning); // Cập nhật trạng thái tiếp đất
            //System.out.println(isBeginning ? "Player is now on the ground." : "Player has left the ground.");
        }
    }

    private void handlePlayerKillZoneCollision(Fixture fixA, Fixture fixB) {
        // Xác định Fixture nào là của Player
        Fixture playerFixture = (fixA.getFilterData().categoryBits == Constants.PLAYER_BIT) ? fixA : fixB;
        if (playerFixture.getUserData() instanceof Player) {
            Player player = (Player) playerFixture.getUserData();

            // Kiểm tra nếu Player chưa ở trạng thái chết
            if (!player.isDying()) {
                player.onDeath(); // Gọi logic chết của Player
            }
        }
    }

    private void handlePlayerFinishZoneCollision(Fixture fixA, Fixture fixB) {
        // Hiển thị UI hoàn thành màn chơi
        UIManager.getInstance(UIManager.class).openUI(LevelCompleteCanvas.class);
    }
}
