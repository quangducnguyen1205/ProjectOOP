package com.projectoop.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Level {

    private String levelName;
    private boolean isInitialized;

    // Constructor cho level
    public Level(String name) {
        this.levelName = name;
        this.isInitialized = false;
    }

    // Khởi tạo level
    public void onInit() {
        Gdx.app.log("Level", "Initializing level: " + levelName);
        isInitialized = true;

        // Khởi tạo các thành phần của level (ví dụ: tạo môi trường, thiết lập đối tượng)
        // Bạn có thể thêm các đoạn mã cần thiết để khởi tạo các đối tượng trong level
    }

    // Xử lý dọn dẹp level khi chuyển sang level khác hoặc kết thúc
    public void onDespawn() {
        Gdx.app.log("Level", "Disposing level: " + levelName);
        isInitialized = false;

        // Dọn dẹp các tài nguyên của level (ví dụ: xóa đối tượng, giải phóng bộ nhớ)
        // Ví dụ: background.onDespawn(); hoặc các hình ảnh, âm thanh không còn cần thiết.
    }

    // Xử lý render cho level
    public void render(Stage stage) {
        if (!isInitialized) {
            Gdx.app.log("Level", "Attempted to render uninitialized level: " + levelName);
            return;
        }

        // Render các thành phần của level
        // Stage render các actor trong level hoặc render các đối tượng khác bằng SpriteBatch
        stage.act(Gdx.graphics.getDeltaTime());  // Cập nhật stage theo thời gian
        stage.draw();  // Vẽ các actor trong stage
    }
}
