package com.projectoop.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.entity.Enemy;
import com.projectoop.game.Player;
import com.projectoop.utils.MapBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {

    protected String levelName;
    protected TiledMap map;
    protected MapBuilder mapBuilder;
    protected World world;
    protected Player player;
    private List<Enemy> enemies;// Danh sách Enemy

    // Constructor chung cho Level
    public Level(String levelName) {
        this.levelName = levelName;
        this.mapBuilder = null; // Khởi tạo MapBuilder sau khi có thông tin
        this.enemies = new ArrayList<>();
    }

    // Phương thức khởi tạo level, mỗi level con sẽ định nghĩa riêng
    public abstract void onInit();

    // Dọn dẹp khi level kết thúc
    public void onDespawn() {
        if (mapBuilder != null) {
            mapBuilder.dispose();  // Giải phóng tài nguyên từ MapBuilder
        }
        for (Enemy enemy : enemies) {
            enemy.dispose(); // Giải phóng tài nguyên của Enemy
        }
        enemies.clear();
    }

    // Trả về player cho LevelManager quản lý
    public Player getPlayer() {
        return player;
    }

    // Thiết lập MapBuilder và tải map
    protected void setupMap(String mapFilePath) {
        mapBuilder = new MapBuilder(world, mapFilePath);
        if (mapBuilder.getMap() == null) {
            System.err.println("Error loading map for level: " + levelName);
        } else {
            this.map = mapBuilder.getMap();
            System.out.println("Map loaded successfully for level: " + levelName);
        }
    }
    // Phương thức tạo tất cả các body tĩnh từ RectangleMapObject trong map
    protected void createStaticBodies() {
        System.out.println("Creating static bodies for Level...");

        // Sử dụng createStaticBodies để tự động tạo body từ tất cả RectangleMapObject trong map
        mapBuilder.createStaticBodies();

        enemies.addAll(mapBuilder.getEnemies());

        System.out.println("Static bodies created for all layers in the map.");
    }
    public void setWorld(World world) {
        this.world = world;
    }
    public World getWorld() {
        return world;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public String getLevelName() {
        return levelName;
    }

    public MapBuilder getMapBuilder() {
        return mapBuilder;
    }
}
