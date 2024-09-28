package com.projectoop.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.projectoop.level.Level;

public class LevelManager extends Singleton<LevelManager> {

    private Level currentLevel;
    private Array<Level> levels;  // Danh sách các level

    // Khởi tạo LevelManager
    private LevelManager() {
        levels = new Array<>();  // Khởi tạo danh sách các level
    }

    // Bắt đầu và tải level đầu tiên
    public void start() {
        onLoadLevel(0);  // Tải level đầu tiên
        onInit();
    }

    // Khởi tạo trạng thái game
    public void onInit() {
        //Gdx.app.log("LevelManager", "Game initialized");
        // Khởi tạo player hoặc các yếu tố khác trong level
    }

    // Reset trạng thái game
    public void onReset() {
        //Gdx.app.log("LevelManager", "Game reset");
        // Dọn dẹp và reset player hoặc các yếu tố khác nếu cần
    }

    // Tải một level mới
    public void onLoadLevel(int levelIndex) {
        //Gdx.app.log("LevelManager", "Loading level " + levelIndex);

        // Nếu có level hiện tại, dọn dẹp trước khi tải level mới
        if (currentLevel != null) {
            currentLevel.onDespawn();  // Dọn dẹp level hiện tại
        }

        // Tải level mới từ danh sách level
        if (levelIndex >= 0 && levelIndex < levels.size) {
            currentLevel = levels.get(levelIndex);
            currentLevel.onInit();  // Khởi tạo level mới
        } else {
            Gdx.app.log("LevelManager", "Invalid level index");
        }
    }

    // Render level hiện tại
    public void render(Stage stage) {
        if (currentLevel != null) {
            currentLevel.render(stage);  // Render level hiện tại
        }
    }
}
