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
import com.projectoop.manager.GameManager;
import com.projectoop.manager.LevelManager;
import com.projectoop.ui.MainMenuCanvas;
import com.projectoop.ui.UIManager;
import com.projectoop.utils.Constants;
import com.projectoop.utils.Hud;
import com.projectoop.utils.MapBuilder;

public class PlayScreen implements Screen {

    private SpriteBatch batch;
    private Hud hud;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private FitViewport viewport;
    private Box2DDebugRenderer b2dr;
    private World world;

    private LevelManager levelManager;
    private Player player;
    private MapBuilder mapBuilder;

    public PlayScreen() {}

    @Override
    public void show() {
        batch = new SpriteBatch();
        //hud = new Hud(batch);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIRTUAL_WIDTH / Constants.PPM, Constants.VIRTUAL_HEIGHT / Constants.PPM, camera);

        levelManager = LevelManager.getInstance(LevelManager.class);

        String currentLevel = GameManager.getInstance(GameManager.class).getPlayerData().getCurrentLevel();
        if (currentLevel == null || currentLevel.isEmpty()) {
            currentLevel = "level1"; // Mặc định Level 1
            GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(currentLevel);
        }

        levelManager.loadLevelByName(currentLevel);

        player = levelManager.getCurrentLevel().getPlayer();
        world = levelManager.getWorld();
        mapBuilder = levelManager.getCurrentLevel().getMapBuilder();
        renderer = mapBuilder.getRenderer();

        b2dr = new Box2DDebugRenderer();

        camera.position.set(Constants.VIRTUAL_WIDTH / 2f / Constants.PPM, Constants.VIRTUAL_HEIGHT / 2f / Constants.PPM, 0);
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
        //hud.update(delta);
    }

    private void renderGame() {
        renderer.render();
        b2dr.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (player != null) {
            player.render(batch);
        }
        batch.end();

        //batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();
    }

    private void onLevelCompleted() {
        String completedLevel = levelManager.getCurrentLevel().getLevelName();
        GameManager.getInstance(GameManager.class).getPlayerData().addCompletedLevel(completedLevel);

        int nextLevelIndex = levelManager.getNextLevelIndex(completedLevel);
        if (nextLevelIndex != -1) {
            levelManager.loadLevel(nextLevelIndex);
            GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(
                levelManager.getCurrentLevel().getLevelName()
            );
        } else {
            System.out.println("All levels completed!");
            UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class); // Mở MainMenu
        }
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
