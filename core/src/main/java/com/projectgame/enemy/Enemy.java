package com.projectgame.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Characters {
    private Vector2 position;  // Vị trí của enemy
    private ShapeRenderer shapeRenderer;  // Đối tượng để vẽ hình dạng

    public Enemy(float x, float y) {
        super();
        position = new Vector2(x, y);  // Khởi tạo vị trí
        shapeRenderer = new ShapeRenderer();  // Khởi tạo ShapeRenderer
        onInit();  // Khởi tạo enemy
    }

    @Override
    public void onInit() {
        hp = 100;  // Thiết lập điểm HP ban đầu
    }

    @Override
    public void onDespawn() {
        // Logic khi enemy bị loại bỏ
    }

    @Override
    public void onDeath() {
        // Logic khi enemy chết
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Logic di chuyển enemy (nếu cần)
    }

    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);  // Bắt đầu vẽ
        shapeRenderer.setColor(Color.RED);  // Màu sắc của enemy
        shapeRenderer.rect(position.x, position.y, 50, 50);  // Vẽ ô vuông (50x50 pixels)
        shapeRenderer.end();  // Kết thúc vẽ
    }

    public Vector2 getPosition() {
        return position;
    }
}
