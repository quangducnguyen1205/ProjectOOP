package com.projectoop.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.PlayerData;

public class CollisionHandler implements ContactListener {

    private World world;

    // Constructor nhận World từ bên ngoài
    public CollisionHandler(World world) {
        this.world = world;
        // Gán ContactListener cho World để xử lý va chạm
        this.world.setContactListener(this);
    }

    @Override
    public void beginContact(Contact contact) {
        // Lấy hai Fixture va chạm
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Log chi tiết về va chạm, kể cả categoryBits của từng Fixture
        System.out.println("Collision detected between: " + fixA.getUserData() + " (categoryBits: "
            + fixA.getFilterData().categoryBits + ") and " + fixB.getUserData()
            + " (categoryBits: " + fixB.getFilterData().categoryBits + ")");

        // Kiểm tra loại va chạm và xử lý
        handleCollision(fixA, fixB, true); // Thêm tham số isBeginning để biết là bắt đầu va chạm
    }

    @Override
    public void endContact(Contact contact) {
        // Lấy hai Fixture va chạm
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Log chi tiết về kết thúc va chạm, kể cả categoryBits của từng Fixture
        System.out.println("Collision ended between: " + fixA.getUserData() + " (categoryBits: "
            + fixA.getFilterData().categoryBits + ") and " + fixB.getUserData()
            + " (categoryBits: " + fixB.getFilterData().categoryBits + ")");

        // Xử lý logic khi va chạm kết thúc
        handleCollision(fixA, fixB, false); // Thêm tham số isBeginning để biết là kết thúc va chạm
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        // Xử lý trước khi va chạm được giải quyết nếu cần
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        // Xử lý sau khi va chạm được giải quyết nếu cần
    }

    // Phương thức để xử lý va chạm giữa hai Fixture
    private void handleCollision(Fixture fixA, Fixture fixB, boolean isBeginning) {
        // Lấy `categoryBits` của hai Fixture
        short categoryA = fixA.getFilterData().categoryBits;
        short categoryB = fixB.getFilterData().categoryBits;

        // Kiểm tra va chạm giữa `Player` và `Ground`
        if ((categoryA == Constants.PLAYER_BIT && categoryB == Constants.GROUND_BIT) ||
            (categoryB == Constants.PLAYER_BIT && categoryA == Constants.GROUND_BIT)) {

            // Xử lý va chạm giữa Player và Ground
            handlePlayerGroundCollision(fixA, fixB, isBeginning);
        }

        // Bạn có thể thêm các logic xử lý va chạm khác ở đây
        // Ví dụ: Player với Obstacle, Weapon với Enemy, etc.
    }

    // Phương thức xử lý va chạm giữa Player và Ground
    private void handlePlayerGroundCollision(Fixture fixA, Fixture fixB, boolean isBeginning) {
        // Lấy Fixture tương ứng với Player
        Fixture playerFixture = (fixA.getFilterData().categoryBits == Constants.PLAYER_BIT) ? fixA : fixB;

        // Lấy UserData từ Player Fixture
        if (playerFixture.getUserData() instanceof PlayerData) {
            PlayerData playerData = (PlayerData) playerFixture.getUserData();
            playerData.setOnGround(isBeginning);
            System.out.println(isBeginning ? "Player started touching the ground." : "Player left the ground.");
        }
    }
}
