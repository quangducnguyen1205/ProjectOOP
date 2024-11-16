package com.projectoop.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.Player;
import com.projectoop.ui.LevelCompleteCanvas;
import com.projectoop.ui.UIManager;

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

        // Xử lý va chạm giữa `Player` và `FinishZone`
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.FINISHZONE_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.FINISHZONE_BIT)) {
            handlePlayerFinishZoneCollision(fixA, fixB);
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
