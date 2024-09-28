package com.projectoop.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameManager extends Singleton<GameManager> {

    // Constructor riêng để ngăn việc tạo nhiều instance của GameManager
    private GameManager() {
        // Các thiết lập ban đầu có thể cần
    }

    public void onInit() {
        // Khởi tạo GameManager
        Gdx.app.log("GameManager", "GameManager initialized");

        // Đặt frame rate cho game (nếu cần)
        Gdx.graphics.setVSync(true);  // Bật VSync để đồng bộ với tần số quét màn hình
    }

    public void onDespawn() {
        // Dọn dẹp các tài nguyên hoặc logic khi GameManager bị hủy
        Gdx.app.log("GameManager", "GameManager destroyed");
    }

    // Hàm xử lý render cho game
    public void render() {
        // Xóa màn hình với màu đen (hoặc bạn có thể chọn màu khác nếu muốn)
        Gdx.gl.glClearColor(0, 0, 0, 1);  // Màu nền là đen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Các lệnh cập nhật và render khác (ví dụ: cập nhật logic game, render đối tượng)
    }

    // Hàm xử lý khi nhấn phím ESC để thoát game
    public void handleInput() {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit();  // Thoát game
        }
    }
}
