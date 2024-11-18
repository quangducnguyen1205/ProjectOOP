package com.projectoop.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.projectoop.entity.Enemy;
import com.projectoop.entity.Goomba;
import com.projectoop.entity.Orc;

import java.util.ArrayList;
import java.util.List;

public class MapBuilder implements Disposable {
    private World world;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Danh sách các enemy được tạo
    private final List<Enemy> enemies;

    public MapBuilder(World world, String mapFilePath) {
        this.world = world;
        System.out.println("Loading map from: " + mapFilePath);
        this.map = new TmxMapLoader().load(mapFilePath);

        // Thêm hệ số tỷ lệ để đảm bảo các tile layer hiển thị đúng kích thước
        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM, new SpriteBatch());
        this.enemies = new ArrayList<>();
        System.out.println("Map and renderer initialized successfully.");
    }

    public void createStaticBodies() {
        if (map == null) return;

        for (MapLayer layer : map.getLayers()) {
            String layerName = layer.getName();

            // Duyệt qua các đối tượng trong layer
            for (RectangleMapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = object.getRectangle();

                if (layerName.equalsIgnoreCase("Ground") || layerName.equalsIgnoreCase("Bricks")) {
                    createStaticBody(rect, Constants.GROUND_BIT, Constants.PLAYER_BIT, false, layerName);
                } else if (layerName.equalsIgnoreCase("Pipes")) {
                    createStaticBody(rect, Constants.OBSTACLE_BIT, Constants.PLAYER_BIT, false, layerName);
                } else if (layerName.equalsIgnoreCase("KillZone")) {
                    createStaticBody(rect, Constants.KILLZONE_BIT, Constants.PLAYER_BIT, false, layerName);
                } else if (layerName.equalsIgnoreCase("FinishZone")) {
                    createStaticBody(rect, Constants.FINISHZONE_BIT, Constants.PLAYER_BIT, true, layerName);
                } else if (layerName.equalsIgnoreCase("Goomba")) {
                    createGoomba(rect);
                } else if (layerName.equalsIgnoreCase("Orc")) {
                    createOrc(rect);
                }
            }
        }
    }

    private void createStaticBody(Rectangle rect, short categoryBits, short maskBits, boolean isSensor, String layerName) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
            (rect.getX() + rect.getWidth() / 2) / Constants.PPM,
            (rect.getY() + rect.getHeight() / 2) / Constants.PPM
        );

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef).setUserData(layerName);
        shape.dispose();
    }

    private void createGoomba(Rectangle rect) {
        float x = (rect.getX() + rect.getWidth() / 2) / Constants.PPM;
        float y = (rect.getY() + rect.getHeight() / 2) / Constants.PPM;
        Goomba goomba = new Goomba(world, new Vector2(x,y));
        enemies.add(goomba);
    }

    private void createOrc(Rectangle rect) {
        float x = (rect.getX() + rect.getWidth() / 2) / Constants.PPM;
        float y = (rect.getY() + rect.getHeight() / 2) / Constants.PPM;
        Orc orc = new Orc(world, new Vector2(x,y));
        enemies.add(orc);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return this.renderer;
    }

    public TiledMap getMap() {
        return this.map;
    }

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
