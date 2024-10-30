package com.projectoop.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MapBuilder implements Disposable {
    private World world;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public MapBuilder(World world, String mapFilePath) {
        this.world = world;
        try {
            // Thêm xử lý ngoại lệ khi tải file .tmx
            this.map = new TmxMapLoader().load(mapFilePath);
            // Khởi tạo OrthogonalTiledMapRenderer để render map
            this.renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        } catch (GdxRuntimeException e) {
            System.err.println("Error loading map: " + e.getMessage());
            this.map = null;
        }
    }

    // Phương thức để render map
    public void render(OrthographicCamera camera) {
        if (renderer != null) {
            renderer.setView(camera);
            renderer.render();
        }
    }

    // Phương thức tạo các Body tĩnh từ layer trong file .tmx
    public void createStaticBodiesFromLayer(String layerName) {
        if (map == null) return;

        MapLayer layer = map.getLayers().get(layerName);
        if (layer instanceof TiledMapTileLayer) {
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

            for (int x = 0; x < tileLayer.getWidth(); x++) {
                for (int y = 0; y < tileLayer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                    if (cell != null && cell.getTile() != null) {
                        // Log thông tin về vị trí ô đang tạo
                        System.out.println("Creating static body at (" + x + "," + y + ") in layer: " + layerName);
                        createStaticBody(x, y, layerName);
                    } else {
                        // Log các ô không có dữ liệu
                        System.out.println("No static body at (" + x + "," + y + ") in layer: " + layerName);
                    }
                }
            }
        }
    }


    // Tạo Body tĩnh từ các tọa độ x, y và tên layer
    private void createStaticBody(int x, int y, String layerName) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f, new Vector2(x + 0.5f, y + 0.5f), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.5f;

        if (layerName.equalsIgnoreCase("Ground")) {
            fixtureDef.filter.categoryBits = Constants.GROUND_BIT;
            fixtureDef.filter.maskBits = Constants.PLAYER_BIT;
        } else if (layerName.equalsIgnoreCase("Pipes") || layerName.equalsIgnoreCase("Bricks")) {
            fixtureDef.filter.categoryBits = Constants.OBSTACLE_BIT;
            fixtureDef.filter.maskBits = Constants.PLAYER_BIT;
        } else {
            fixtureDef.filter.categoryBits = Constants.GROUND_BIT;
            fixtureDef.filter.maskBits = Constants.PLAYER_BIT;
        }
        System.out.println("Static body created for layer " + layerName + " at (" + x + "," + y + ")");


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + 0.5f, y + 0.5f);  // Đảm bảo tọa độ đúng giữa các ô

        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(layerName);

        shape.dispose();
    }


    // Getter để lấy renderer
    public OrthogonalTiledMapRenderer getRenderer() {
        return this.renderer;
    }

    // Getter để lấy TiledMap
    public TiledMap getMap() {
        return this.map;
    }

    // Phương thức giải phóng tài nguyên
    @Override
    public void dispose() {
        if (renderer != null) {
            renderer.dispose();
        }
        if (map != null) {
            map.dispose();
        }
    }
}
