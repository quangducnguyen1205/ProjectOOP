package com.projectoop.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.utils.Constants;

public class Goomba extends Enemy {

    public Goomba(World world, Vector2 startPosition) {
        super(world, startPosition);
    }

    @Override
    public void onInit() {
        super.onInit();

        // Thêm các animation của Goomba
        addAnimation("run", loadAnimation("path/to/goomba/run", 4, 0.2f));
        addAnimation("death", loadAnimation("path/to/goomba/death", 4, 0.1f));

        changeAnim("run", true); // Animation mặc định là "run"
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 16 / 2 / Constants.PPM, (float) 16 / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f;
        fixtureDef.filter.categoryBits = Constants.ENEMY_BIT;
        fixtureDef.filter.maskBits = Constants.GROUND_BIT | Constants.PLAYER_BIT;

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }
}
