package com.projectoop.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.utils.Constants;

public class Kunai extends Weapon {

    public Kunai(World world, Vector2 startPosition, float speed, int damage) {
        super(world, startPosition, speed, damage);
        texture = new TextureRegion(new Texture("assets/Ninja/Kunai.png")); // Giả sử hình ảnh Kunai nằm trong thư mục assets
    }

    @Override
    protected void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / Constants.PPM, position.y / Constants.PPM);
        bodyDef.linearVelocity.set(speed, 0);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.WEAPON_WIDTH / 2 / Constants.PPM, Constants.WEAPON_HEIGHT / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = Constants.WEAPON_BIT;
        fixtureDef.filter.maskBits = Constants.ENEMY_BIT | Constants.PIPES_BIT;

        body.createFixture(fixtureDef).setUserData("Kunai");

        shape.dispose();
    }
}
