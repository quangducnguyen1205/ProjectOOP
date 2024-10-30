package com.projectoop.game;

import com.badlogic.gdx.physics.box2d.Body;

// Lớp này chứa dữ liệu về Player, bao gồm việc lưu Body và trạng thái va chạm
public class PlayerData {

    private final Player player;
    private boolean isOnGround; // Biến để xác định nếu Player đang tiếp xúc với mặt đất

    public PlayerData(Player player) {
        this.player = player;
        this.isOnGround = false;
    }

    // Getter cho Player
    public Player getPlayer() {
        return player;
    }

    // Kiểm tra xem Player có đang tiếp đất không
    public boolean isOnGround() {
        return isOnGround;
    }

    // Setter khi Player tiếp đất
    public void setOnGround(boolean onGround) {
        this.isOnGround = onGround;

        // Log để kiểm tra trạng thái cập nhật
        if (onGround) {
            System.out.println("Player is now on the ground.");
        } else {
            System.out.println("Player is now in the air.");
        }
    }

    // Lấy Body của Player để xử lý logic vật lý
    public Body getBody() {
        return player.getBody();
    }
}
