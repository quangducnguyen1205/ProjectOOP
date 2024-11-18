package com.projectoop.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.base.Characters;
import com.projectoop.state.StateMachine;

public abstract class Enemy extends Characters {
    public World world; // Thay đổi phạm vi thành public
    public Body b2body; // Thay đổi phạm vi thành public
    public Vector2 velocity; // Thay đổi phạm vi thành public
    public StateMachine<Enemy> stateMachine; // Thay đổi phạm vi thành public
    public boolean isFacingRight = true; // Thay đổi phạm vi thành public

    public Enemy(World world, float x, float y) {
        this.world = world;
        setPosition(x, y);
        velocity = new Vector2(1, 0); // Tốc độ mặc định
        stateMachine = new StateMachine<>(this);
        onInit();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateMachine.update();
        b2body.setLinearVelocity(velocity);

        setPosition(
            b2body.getPosition().x - getWidth() / 2,
            b2body.getPosition().y - getHeight() / 2
        );
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public StateMachine<Enemy> getStateMachine() {
        return stateMachine;
    }

    public void reverseVelocity(boolean reverseX, boolean reverseY) {
        if (reverseX) velocity.x = -velocity.x;
        if (reverseY) velocity.y = -velocity.y;
        isFacingRight = velocity.x > 0;
    }

    public abstract void defineEnemy();

    @Override
    public void onInit() {
        defineEnemy();
    }

    @Override
    public void onDeath() {}

    @Override
    public void onDespawn() {
        world.destroyBody(b2body);
    }

    public abstract void attack();

    public abstract void patrol();
}
