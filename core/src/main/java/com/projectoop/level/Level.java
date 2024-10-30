package com.projectoop.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.Player;
import com.projectoop.utils.MapBuilder;

public abstract class Level {

    protected String levelName;
    protected TiledMap map;
    protected MapBuilder mapBuilder;
    protected World world;
    protected Player player;

    // Constructor chung cho Level
    public Level(String levelName, World world) {
        this.levelName = levelName;
        this.world = world;
        this.mapBuilder = null;  // Khởi tạo MapBuilder sau khi có thông tin
    }

    // Phương thức khởi tạo level, mỗi level con sẽ định nghĩa riêng
    public abstract void onInit();

    // Dọn dẹp khi level kết thúc
    public void onDespawn() {
        if (mapBuilder != null) {
            mapBuilder.dispose();  // Giải phóng tài nguyên từ MapBuilder
        }
    }

    // Render level hiện tại
    public void render(OrthographicCamera camera) {
        if (mapBuilder != null) {
            // Cập nhật camera để render các lớp trong bản đồ
            mapBuilder.render(camera);

//            if (player != null) {
//                player.render();  // Giả định player có phương thức render() không cần batch
//            }
        }
    }

    // Cập nhật trạng thái của level hiện tại
    public void update(float delta) {
        if (world != null) {
            // Cập nhật World trong mỗi frame
            world.step(1 / 60f, 6, 2);
        }
        if (player != null) {
            player.update(delta);
        }
    }

    // Trả về player cho LevelManager quản lý
    public Player getPlayer() {
        return player;
    }

    // Thiết lập MapBuilder và tải map
    protected void setupMap(String mapFilePath) {
        System.out.println("Loading map from: " + mapFilePath);
        mapBuilder = new MapBuilder(world, mapFilePath);
        if (mapBuilder.getMap() == null) {
            System.err.println("Error loading map for level: " + levelName);
        } else {
            this.map = mapBuilder.getMap();
        }
    }

    public String getLevelName() {
        return levelName;
    }
}
