package com.projectoop.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.Player;

public class CollisionHandler implements ContactListener {

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

        // Xử lý va chạm giữa `Player` và `KillZone`
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.KILLZONE_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.KILLZONE_BIT)) {
            handlePlayerKillZoneCollision(fixA, fixB);
        }

        // Xử lý va chạm giữa Player và SavePoint
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.SAVEPOINT_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.SAVEPOINT_BIT)) {
            handlePlayerSavePointCollision(fixA, fixB);
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


    private void handlePlayerSavePointCollision(Fixture fixA, Fixture fixB) {
        // Xác định Fixture của Player
        Fixture playerFixture = (fixA.getFilterData().categoryBits == Constants.PLAYER_BIT) ? fixA : fixB;

        if (playerFixture.getUserData() instanceof Player) {
            Player player = (Player) playerFixture.getUserData();
            Vector2 savePointPosition = getSavePointPosition(fixA, fixB);

            if (savePointPosition != null) {
                // Cập nhật SavePoint cho Player
                player.setSavePoint(savePointPosition);
                System.out.println("SavePoint updated to: " + savePointPosition);
            } else {
                // In log cảnh báo nếu không tìm thấy vị trí SavePoint
                System.out.println("Warning: Unable to update SavePoint. Position is null.");
            }
        }
    }

    private Vector2 getSavePointPosition(Fixture fixA, Fixture fixB) {
        Fixture savePointFixture = (fixA.getFilterData().categoryBits == Constants.SAVEPOINT_BIT) ? fixA : fixB;

        if (savePointFixture.getBody() != null) {
            return savePointFixture.getBody().getPosition().cpy().scl(Constants.PPM); // Nhân PPM khi lưu
        }

        if (savePointFixture.getUserData() instanceof Vector2) {
            return ((Vector2) savePointFixture.getUserData()).cpy();
        }

        System.out.println("Warning: SavePoint fixture does not contain a valid position or UserData: " + savePointFixture);
        return null;
    }


}
