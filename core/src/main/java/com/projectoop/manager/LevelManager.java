package com.projectoop.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.projectoop.entity.Enemy;
import com.projectoop.level.Level;
import com.projectoop.level.Level1;
import com.projectoop.level.Level2;

import java.util.ArrayList;
import java.util.List;

public class LevelManager extends Singleton<LevelManager> {

    //private World world;
    private Level currentLevel;
    private Array<Level> levels;

    // Khởi tạo LevelManager
    protected LevelManager() {
        levels = new Array<>();
    }

    // Bắt đầu game và tải danh sách các level
    public void start() {
        if (levels.isEmpty()) {
               addLevel(new Level1("level1"));
               addLevel(new Level2("level2"));
        }
    }

    // Tải level dựa trên tên
    public void loadLevelByName(String levelName) {
        int levelIndex = getLevelIndexByName(levelName);
        if (levelIndex != -1) {
            loadLevel(levelIndex);
        } else {
            Gdx.app.log("LevelManager", "Level not found: " + levelName);
        }
    }

    // Tải level dựa trên chỉ số
    public void loadLevel(int levelIndex) {
        if (levelIndex >= 0 && levelIndex < levels.size) {
            if (currentLevel != null) {
                currentLevel.onDespawn();
            }
            currentLevel = levels.get(levelIndex);
            currentLevel.onInit();  // Khởi tạo lại level mới
        } else {
            Gdx.app.log("LevelManager", "Invalid level index: " + levelIndex);
        }
    }

    // Thêm level vào danh sách
    public void addLevel(Level level) {
        if (level != null) {
            levels.add(level);
        }
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    // Lấy chỉ số level theo tên
    public int getLevelIndexByName(String levelName) {
        for (int i = 0; i < levels.size; i++) {
            if (levels.get(i).getLevelName().equals(levelName)) {
                return i;
            }
        }
        return -1;
    }

    // Lấy chỉ số của level tiếp theo
    public int getNextLevelIndex(String currentLevelName) {
        int currentIndex = getLevelIndexByName(currentLevelName);
        if (currentIndex != -1 && currentIndex < levels.size - 1) {
            return currentIndex + 1;
        }
        return -1;
    }

    public List<Enemy> getEnemies() {
        if (currentLevel != null) {
            return currentLevel.getEnemies();
        }
        return new ArrayList<>();
    }

    // Giải phóng tài nguyên
    public void dispose() {
        if (currentLevel != null) {
            currentLevel.onDespawn();
        }
    }
}
