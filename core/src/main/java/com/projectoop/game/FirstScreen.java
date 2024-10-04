package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class FirstScreen implements Screen {

    private SpriteBatch batch;
    private Player player;  // Quản lý player

    private OrthographicCamera camera;  // Camera để di chuyển trên map
    private TiledMap map;  // Bản đồ
    private OrthogonalTiledMapRenderer renderer;  // Render bản đồ

    @Override
    public void show() {
        // Chuẩn bị các tài nguyên hiển thị
        batch = new SpriteBatch();

        // Khởi tạo player với vị trí ban đầu
        player = new Player(new Vector2(50, 50));
        player.onInit(); // Khởi tạo các giá trị ban đầu cho player

        // Khởi tạo camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Load bản đồ .tmx
        map = new TmxMapLoader().load("assets/Map/untitled.tmx");  // Đặt đúng đường dẫn tới file .tmx
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(float delta) {
        // Dọn dẹp màn hình
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật trạng thái camera
        camera.update();
        renderer.setView(camera);

        // Render map
        renderer.render();

        // Cập nhật trạng thái của player
        player.update(delta);

        // Bắt đầu vẽ các frame của player
        batch.begin();
        player.render(batch);  // Vẽ player lên map
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

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
        map.dispose();
        renderer.dispose();
    }
}
