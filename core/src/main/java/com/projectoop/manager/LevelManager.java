package com.projectoop.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.projectoop.entity.Enemy;
import com.projectoop.level.Level;
import com.projectoop.level.Level1;
import com.projectoop.level.Level2;
import com.projectoop.ui.MainMenuCanvas;
import com.projectoop.utils.CollisionHandler;

import java.util.ArrayList;
import java.util.List;

public class LevelManager extends Singleton<LevelManager> {

    private World world; // Lưu trữ World toàn cục
    private Level currentLevel;
    private Array<Level> levels;
    private CollisionHandler collisionHandler; // Sử dụng CollisionHandler
    private final List<Enemy> enemies = new ArrayList<>(); // Danh sách Enemy

    // Khởi tạo LevelManager
    protected LevelManager() {
        levels = new Array<>();
        world = new World(new Vector2(0, -9.8f), true); // Khởi tạo World
        collisionHandler = new CollisionHandler(world); // Khởi tạo CollisionHandler
        System.out.println("CollisionHandler has been set for the world.");
    }

    // Bắt đầu game và tải danh sách các level
    public void start() {
        if (levels.isEmpty()) {  // Chỉ khởi tạo khi danh sách level chưa có phần tử nào
            addLevel(new Level1("level1", world));
            addLevel(new Level2("level2", world));
        }

        // Lấy cấp độ hiện tại từ PlayerData
        String currentLevel = GameManager.getInstance(GameManager.class).getPlayerData().getCurrentLevel();

        // Tải cấp độ tương ứng
        int levelIndex = getLevelIndexByName(currentLevel);
        if (levelIndex == -1) {
            levelIndex = 0; // Mặc định là level đầu tiên nếu không tìm thấy
        }
        loadLevel(levelIndex);
    }

    // Tải level dựa trên tên
    public void loadLevelByName(String levelName) {
        int levelIndex = getLevelIndexByName(levelName);
        System.out.println("Loading level: " + levelIndex);
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
                System.out.println("Level despawned: " + currentLevel.getLevelName());
            }
            currentLevel = levels.get(levelIndex);
            Gdx.app.log("LevelManager", "Loading level: " + currentLevel.getLevelName());
            currentLevel.onInit();
        } else {
            Gdx.app.log("LevelManager", "Invalid level index: " + levelIndex);
        }
    }

    // Xử lý khi hoàn thành cấp độ
    public void onLevelCompleted() {
        if (currentLevel != null) {
            String completedLevel = currentLevel.getLevelName();
            GameManager.getInstance(GameManager.class).getPlayerData().addCompletedLevel(completedLevel);

            // Tải cấp độ tiếp theo nếu có
            int nextLevelIndex = getNextLevelIndex(completedLevel);
            if (nextLevelIndex != -1) {
                loadLevel(nextLevelIndex);
                GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(
                    levels.get(nextLevelIndex).getLevelName()
                );
            } else {
                System.out.println("All levels completed!");
                GameManager.getInstance(GameManager.class).setScreen((Screen) new MainMenuCanvas());
            }
        }
    }

    // Thêm level vào danh sách
    public void addLevel(Level level) {
        if (level != null) {
            levels.add(level);
            System.out.println("Level added: " + level.getLevelName());
        }
    }

    // Lấy World hiện tại
    public World getWorld() {
        return world;
    }

    // Trả về level hiện tại
    public Level getCurrentLevel() {
        System.out.println("Getting current level: " + currentLevel.getLevelName());
        return currentLevel;
    }

    // Giải phóng tài nguyên
    public void dispose() {
        if (currentLevel != null) {
            currentLevel.onDespawn();
        }
    }

    // Lấy chỉ số level theo tên
    public int getLevelIndexByName(String levelName) {
        System.out.println("Getting level index: " + levelName);
        for (int i = 0; i < levels.size; i++) {
            System.out.println(levels.get(i).getLevelName());
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

}
