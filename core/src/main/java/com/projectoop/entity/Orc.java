package com.projectoop.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.projectoop.state.PatrolState;
import com.projectoop.utils.Constants;

public class Orc extends Enemy {
    private Animation<TextureRegion> walkAnimation;

    public Orc(World world, float x, float y) {
        super(world, x, y);
        stateMachine.changeState(new PatrolState());
        prepareAnimations();
    }

    private void prepareAnimations() {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(new TextureRegion(/* Tải texture Goomba */), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation<>(0.1f, frames);
        addAnimation("walk", walkAnimation);
        changeAnim("walk", true);
    }

    @Override
    public void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(8 / Constants.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.ENEMY_BIT;
        fdef.filter.maskBits = Constants.PLAYER_BIT | Constants.GROUND_BIT;
        b2body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void patrol() {
        // Logic đi tuần tra của Orc
        reverseVelocity(true, false);
    }

    @Override
    public void attack() {
        // Logic tấn công của Orc
    }

    @Override
    public void onDeath() {
        // Logic khi Orc chết
    }
}
