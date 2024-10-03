package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FirstScreen implements Screen {

    private SpriteBatch batch;
    private Player player;  // Quản lý player

    @Override
    public void show() {
        // Chuẩn bị các tài nguyên hiển thị
        batch = new SpriteBatch();

        // Khởi tạo player với vị trí ban đầu
        player = new Player(new Vector2(50, 50));
        player.onInit(); // Khởi tạo các giá trị ban đầu cho player
    }

    @Override
    public void render(float delta) {
        // Dọn dẹp màn hình
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật trạng thái của player
        player.update(delta);

        // Bắt đầu vẽ các frame của player
        batch.begin();
        player.render(batch); // Sử dụng phương thức render của player để vẽ animation hiện tại
        batch.end();

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Giải phóng tài nguyên
        batch.dispose();
    }

}
