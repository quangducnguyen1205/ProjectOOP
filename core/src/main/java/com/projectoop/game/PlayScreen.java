package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.projectoop.manager.LevelManager;
import com.projectoop.utils.Constants;
import com.projectoop.utils.Hud;

public class PlayScreen implements Screen {

    private SpriteBatch batch;
    private Hud hud;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Box2DDebugRenderer b2dr;

    // Khởi tạo LevelManager để quản lý các level
    private LevelManager levelManager;

    PlayScreen(){

    }

    @Override
    public void show() {
        // Khởi tạo SpriteBatch và Hud
        batch = new SpriteBatch();
        hud = new Hud(batch);

        // Tạo camera và viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIRTUAL_WIDTH / Constants.PPM, Constants.VIRTUAL_HEIGHT / Constants.PPM, camera);

        // Khởi tạo LevelManager và tải level đầu tiên
        levelManager = LevelManager.getInstance(LevelManager.class);
        levelManager.start();

        // Sử dụng World từ LevelManager
        b2dr = new Box2DDebugRenderer();

        // Thiết lập camera ban đầu
        camera.position.set(Constants.VIRTUAL_WIDTH / 2f / Constants.PPM, Constants.VIRTUAL_HEIGHT / 2f / Constants.PPM, 0);

        //camera.update();
    }

    @Override
    public void render(float delta) {
        // Xóa màn hình
        clearScreen();

        // Cập nhật camera trước khi render
        updateCamera();

        // Cập nhật trạng thái game
        updateGame(delta);

        // Render game
        renderGame();
    }

    // Phương thức để xóa màn hình
    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // Phương thức để cập nhật camera
    private void updateCamera() {
        if (levelManager.getCurrentLevel().getPlayer() != null) {
            Vector2 playerPos = levelManager.getCurrentLevel().getPlayer().getPosition();
            if (playerPos.x != camera.position.x || playerPos.y != camera.position.y) {
                camera.position.set(playerPos.x, playerPos.y, 0);
                System.out.println("Camera position updated to player position: " + camera.position);
            }
        }
        viewport.apply();
        camera.update();
    }

    // Phương thức để cập nhật trạng thái game
    private void updateGame(float delta) {
        if (levelManager != null) {
            levelManager.update(delta);
        }
        hud.update(delta);
    }

    // Phương thức để render game
    private void renderGame() {
        if (levelManager != null) {
            // Render bản đồ qua LevelManager
            levelManager.render(camera);
        }

        // Render các yếu tố HUD
        hud.stage.draw();

        // Hiển thị debug Box2D (tùy chọn)
        b2dr.render(levelManager.getWorld(), camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        hud.dispose();
        b2dr.dispose();
        levelManager.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
