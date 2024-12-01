package com.projectoop.game.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.FourthMapScreen;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.sprites.PlayerData;
import com.projectoop.game.tools.AudioManager;
import com.projectoop.game.ui.MainMenuCanvas;
import com.projectoop.game.ui.UIManager;

public class GameManager extends Singleton<GameManager> {

    private Game game;
    private PlayerData playerData;
    private ScreenManager screenManager;  // Khai báo ScreenManager
    public GameWorld gameWorld;
    public FourthMapScreen lastScreen;

    public void initialize(Game game) {
        this.game = game;

        AudioManager.setUp();
        // Khởi tạo GameWorld
        gameWorld = new GameWorld();

        playerData = new PlayerData(); // Khởi tạo PlayerData

        // Tạo một ScreenManager mới
        screenManager = new ScreenManager(game);

        // Thiết lập MainMenu làm màn hình đầu tiên
        UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
    }

    public void setScreen(Screen screen) {
        if (game != null) {
            screenManager.setScreen(screen);  // Dùng ScreenManager để chuyển màn
        } else  {
            throw new IllegalStateException("Game instance is not initialized. Call initialize() first.");
        }
        if (screen instanceof FourthMapScreen) {
            lastScreen = (FourthMapScreen) screen;
        }
    }

    public void update() {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Xóa màn hình với màu nền
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.getScreen() != null) {
            game.getScreen().render(Gdx.graphics.getDeltaTime()); // Cập nhật màn hình hiện tại
        }

        if (UIManager.getInstance(UIManager.class).getStage().getActors().size > 0) {
            UIManager.getInstance(UIManager.class).getStage().act(Gdx.graphics.getDeltaTime());
            UIManager.getInstance(UIManager.class).getStage().draw();
        }
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public FourthMapScreen getLastScreen () {
        return lastScreen;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    // Lưu dữ liệu người chơi
    public void savePlayerData() {
        // Thực hiện logic lưu dữ liệu (vd: ghi vào file hoặc database)
        System.out.println("PlayerData saved!");
    }

    // Tải dữ liệu người chơi
    public void loadPlayerData() {
        // Thực hiện logic tải dữ liệu
        System.out.println("PlayerData loaded!");
    }

    public void dispose() {
        if (UIManager.getInstance(UIManager.class) != null) {
            UIManager.getInstance(UIManager.class).getStage().dispose();
        }
    }
}
