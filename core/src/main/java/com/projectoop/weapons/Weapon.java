package com.projectoop.weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.utils.Constants;

public abstract class Weapon {

    protected World world;
    protected Body body;
    protected Vector2 position;
    protected float speed;
    protected int damage;
    protected TextureRegion texture;

    public Weapon(World world, Vector2 startPosition, float speed, int damage) {
        this.world = world;
        this.position = startPosition;
        this.speed = speed;
        this.damage = damage;
        createBody();
    }

    protected abstract void createBody();  // Phương thức trừu tượng để các lớp con hiện thực

    public void update(float delta) {
        // Cập nhật vị trí vũ khí dựa trên vật lý
        if (body != null) {
            position.set(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        }
    }

    public void render(Batch batch) {
        if (texture != null) {
            batch.draw(texture, position.x, position.y, Constants.WEAPON_WIDTH, Constants.WEAPON_HEIGHT);
        }
    }

    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
        }
    }
}

