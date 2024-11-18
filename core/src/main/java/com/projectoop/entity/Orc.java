package com.projectoop.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.state.PatrolState;
import com.projectoop.utils.Constants;

public class Orc extends Enemy {
    public Orc(World world, Vector2 startPosition) {
        super(world, startPosition);
        // Bắt đầu ở trạng thái tuần tra
        getStateMachine().changeState(new PatrolState());
    }

    @Override
    protected void defineEnemy() {
        // Định nghĩa Body cho Orc
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(8 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.ENEMY_BIT;
        fixtureDef.filter.maskBits = Constants.PLAYER_BIT | Constants.GROUND_BIT;

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void onInit() {
        // Thêm animation riêng cho Orc
        addAnimation("run", loadAnimation("path/to/orc/run", 6, 0.1f));
        addAnimation("death", loadAnimation("path/to/orc/death", 4, 0.1f));
        changeAnim("run", true); // Animation mặc định
    }

    @Override
    public void onDespawn() {
        // Logic khi Orc bị hủy
        System.out.println("Orc has been despawned.");
    }

    @Override
    public void onDeath() {
        // Animation chết và ngừng di chuyển
        changeAnim("death", false);
        setVelocity(Vector2.Zero);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Cập nhật logic StateMachine
        getStateMachine().update();
    }
}
