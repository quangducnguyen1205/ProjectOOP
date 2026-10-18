package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.entity.Enemy;
import com.projectoop.manager.GameManager;
import com.projectoop.manager.LevelManager;
import com.projectoop.scenes.PlayerHealthBar;
import com.projectoop.utils.CollisionHandler;
import com.projectoop.utils.Constants;

public abstract class BaseScreen implements Screen {

    public SpriteBatch batch;
    protected OrthographicCamera camera;
    protected OrthogonalTiledMapRenderer renderer;
    protected FitViewport viewport;
    protected Box2DDebugRenderer b2dr;
    protected World world;

    protected LevelManager levelManager;
    protected Player player;

    protected PlayerHealthBar playerHealthBar;

    protected CollisionHandler collisionHandler;

    public BaseScreen() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIRTUAL_WIDTH / Constants.PPM, Constants.VIRTUAL_HEIGHT / Constants.PPM, camera);

        levelManager = LevelManager.getInstance(LevelManager.class);
        b2dr = new Box2DDebugRenderer();

        camera.position.set(Constants.VIRTUAL_WIDTH / 2f / Constants.PPM, Constants.VIRTUAL_HEIGHT / 2f / Constants.PPM, 0);

        playerHealthBar = new PlayerHealthBar(this);
    }

    @Override
    public void show() {
        updateLevel();

    }

    protected void updateLevel() {
        String currentLevel = GameManager.getInstance(GameManager.class).getPlayerData().getCurrentLevel();
        if (currentLevel == null || currentLevel.isEmpty()) {
            currentLevel = "level1";
            GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(currentLevel);
        }

        levelManager.loadLevelByName(currentLevel);
        player = levelManager.getCurrentLevel().getPlayer();
        world = levelManager.getCurrentLevel().getWorld();
        renderer = levelManager.getCurrentLevel().getMapBuilder().getRenderer();
        collisionHandler = new CollisionHandler(world);
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

    protected void updateGame(float delta) {
        if (player != null) {
            player.update(delta);
            if (player.hasCompletedLevel()) {
                onLevelCompleted();
            }
        }
        if (world != null) {
            world.step(delta, 6, 2);
        }

        for (Enemy enemy : levelManager.getEnemies()) {
            enemy.update(delta);
            if (enemy.isDead() && enemy.getBody() != null) {
                enemy.onDeath();
            }
        }
    }

    protected void renderGame() {
        renderer.render();
        b2dr.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (player != null) {
            player.render(batch);
            playerHealthBar.draw();
        }
        for (Enemy enemy : levelManager.getEnemies()) {
            enemy.draw(batch);
        }
        batch.end();
    }

    protected abstract void onLevelCompleted();

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        if (world != null) {
            world.dispose(); // Giải phóng tất cả các body vật lý trong world
        }
        if (batch != null) {
            batch.dispose(); // Giải phóng tài nguyên đồ họa
        }
        if (b2dr != null) {
            b2dr.dispose(); // Giải phóng Box2D DebugRenderer
        }
        if (levelManager != null) {
            levelManager.dispose(); // Giải phóng tài nguyên của LevelManager nếu cần
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    public Camera getCamera() {
        return camera;
    }

    public Viewport getGamePort() {
        return viewport;
    }

    public Player getPlayer() {
        return player;
    }
}
