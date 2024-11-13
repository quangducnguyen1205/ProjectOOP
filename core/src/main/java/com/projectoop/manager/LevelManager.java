package com.projectoop.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.projectoop.level.Level;
import com.projectoop.level.Level1;
import com.projectoop.utils.CollisionHandler;

public class LevelManager extends Singleton<LevelManager> {

    private World world; // Lưu trữ World toàn cục
    private Level currentLevel;
    private Array<Level> levels;
    private CollisionHandler collisionHandler; // Sử dụng CollisionHandler

    // Khởi tạo LevelManager
    protected LevelManager() {
        levels = new Array<>();
        world = new World(new Vector2(0, -9.8f), true); // Khởi tạo World ở đây

        // Khởi tạo CollisionHandler với World hiện tại
        collisionHandler = new CollisionHandler(world);
        System.out.println("CollisionHandler has been set for the world.");
    }
    private void onInit() {

    }

    // Bắt đầu game và tải level đầu tiên
    public void start() {
        if (levels.isEmpty()) {  // Chỉ khởi tạo khi danh sách level chưa có phần tử nào
            addLevel(new Level1("level1", world));
            loadLevel(0);
        }
    }
    // Tải level mới và thiết lập logic liên quan
    public void loadLevel(int levelIndex) {
        if (levelIndex >= 0 && levelIndex < levels.size) {
            if (currentLevel != null) {
                currentLevel.onDespawn();
                System.out.println("Level despawned: " + currentLevel.getLevelName());
            }
            currentLevel = levels.get(levelIndex);
            Gdx.app.log("LevelManager", "Loading level: " + currentLevel.getLevelName());
            currentLevel.onInit();
            //onInit();
        } else {
            Gdx.app.log("LevelManager", "Invalid level index: " + levelIndex);
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
        return currentLevel;
    }

    // Phương thức giải phóng tài nguyên
    public void dispose() {
        if (world != null) {
            world.dispose();
            System.out.println("World disposed.");
        }
        if (currentLevel != null) {
            currentLevel.onDespawn();
        }
        levels.clear(); // Xóa danh sách các level để giải phóng tài nguyên
    }
}
