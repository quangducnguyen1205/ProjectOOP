package com.projectoop.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class NullScreen implements Screen {

    @Override
    public void show() {
        // Không có gì cần làm khi màn hình này hiển thị
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        // Không có gì để làm khi resize
    }

    @Override
    public void hide() {
        // Không có gì cần làm khi màn hình ẩn
    }

    @Override
    public void pause() {
        // Không có gì cần làm khi màn hình tạm dừng
    }

    @Override
    public void resume() {
        // Không có gì cần làm khi màn hình tiếp tục
    }

    @Override
    public void dispose() {
        // Giải phóng tài nguyên nếu cần
    }
}
