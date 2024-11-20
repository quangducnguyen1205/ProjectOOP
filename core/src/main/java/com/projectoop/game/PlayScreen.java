package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.projectoop.entity.Enemy;
import com.projectoop.manager.GameManager;
import com.projectoop.manager.LevelManager;
import com.projectoop.ui.MainMenuCanvas;
import com.projectoop.ui.UIManager;
import com.projectoop.utils.Constants;
import com.projectoop.utils.MapBuilder;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private FitViewport viewport;
    private Box2DDebugRenderer b2dr;
    private World world;

    private LevelManager levelManager;
    private Player player;
    private MapBuilder mapBuilder;
    // Danh sách Enemy trong PlayScreen
    private final List<Enemy> enemies = new ArrayList<>();

    public PlayScreen() {

    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        //hud = new Hud(batch);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIRTUAL_WIDTH / Constants.PPM, Constants.VIRTUAL_HEIGHT / Constants.PPM, camera);

        levelManager = LevelManager.getInstance(LevelManager.class);

        updateLevel();

        b2dr = new Box2DDebugRenderer();

        camera.position.set(Constants.VIRTUAL_WIDTH / 2f / Constants.PPM, Constants.VIRTUAL_HEIGHT / 2f / Constants.PPM, 0);
    }

    private void updateLevel() {
        String currentLevel = GameManager.getInstance(GameManager.class).getPlayerData().getCurrentLevel();
        if (currentLevel == null || currentLevel.isEmpty()) {
            currentLevel = "level1";
            GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(currentLevel);
        }

        levelManager.loadLevelByName(currentLevel);

        player = levelManager.getCurrentLevel().getPlayer();
        world = levelManager.getWorld();
        mapBuilder = levelManager.getCurrentLevel().getMapBuilder();
        renderer = mapBuilder.getRenderer();

        // Lấy danh sách Enemy từ MapBuilder
        enemies.clear(); // Xóa danh sách Enemy trước khi thêm mới
        enemies.addAll(mapBuilder.getEnemies());
    }



    @Override
    public void render(float delta) {
        updateGame(delta);
        updateCamera();
        clearScreen();
        renderGame();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void updateCamera() {
        if (player != null) {
            Vector2 playerPos = player.getPosition();
            camera.position.set(playerPos.x, playerPos.y, 0);
        }
        camera.update();
        viewport.apply();
        renderer.setView(camera);
    }

    private void updateGame(float delta) {
        if (player != null) {
            player.update(delta);

            if (player.hasCompletedLevel()) {
                onLevelCompleted();
            }
        }
        if (world != null) {
            world.step(delta, 6, 2);
        }
        // Cập nhật Enemy
        for (Enemy enemy : enemies) {
            enemy.update(delta);
            // Kiểm tra nếu Enemy đã chết và cần xóa
            if (enemy.isDead() && enemy.getBody() != null) {
                enemy.onDeath();
            }
        }
    }

    private void renderGame() {
        renderer.render();
        b2dr.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (player != null) {
            player.render(batch);
        }
        // Vẽ tất cả các Enemy
        for (Enemy enemy : enemies) {
            enemy.draw(batch); // Gọi phương thức draw cho mỗi Enemy
        }

        batch.end();
    }

    private void onLevelCompleted() {
        String completedLevel = levelManager.getCurrentLevel().getLevelName();
        GameManager.getInstance(GameManager.class).getPlayerData().addCompletedLevel(completedLevel);

        int nextLevelIndex = levelManager.getNextLevelIndex(completedLevel);
        if (nextLevelIndex != -1) {
            String nextLevelName = levelManager.getCurrentLevel().getLevelName();
            GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(nextLevelName);

            // Tải màn chơi tiếp theo
            updateLevel();
        } else {
            System.out.println("All levels completed!");
            UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
        }
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
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
