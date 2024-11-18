package com.projectoop.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.base.Characters;
import com.projectoop.state.PatrolState;
import com.projectoop.state.StateMachine;

public abstract class Enemy extends Characters {
    private Vector2 velocity;
    private final StateMachine<Enemy> stateMachine;
    protected Body body;
    protected World world; // Quản lý vật lý Box2D

    public Enemy(World world, Vector2 startPosition) {
        super();
        this.world = world; // Nhận `world` từ constructor
        this.velocity = new Vector2(1f, 0); // Tốc độ di chuyển mặc định
        this.stateMachine = new StateMachine<>(this); // Tạo StateMachine

        setPosition(startPosition.x, startPosition.y); // Đặt vị trí ban đầu cho Enemy
        defineEnemy(); // Tạo Body dựa trên vị trí ban đầu
    }

    protected abstract void defineEnemy(); // Phương thức trừu tượng để định nghĩa Body

    @Override
    public void onInit() {
        hp = 100; // Đặt máu mặc định
        stateMachine.changeState(new PatrolState()); // Đặt trạng thái ban đầu
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Cập nhật logic StateMachine
        stateMachine.update();

        // Cập nhật vị trí từ Body (Box2D)
        if (body != null) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }

    @Override
    public void onDeath() {
        // Xử lý khi Enemy chết
        changeAnim("death", false); // Thay đổi anim thành "death"
        velocity.set(0, 0); // Ngừng di chuyển
    }

    @Override
    public void onDespawn() {
        System.out.println("Enemy has been despawned.");
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public StateMachine<Enemy> getStateMachine() {
        return stateMachine;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        // Hiển thị animation hiện tại
        TextureRegion frame = getCurrentFrame();
        if (frame != null) {
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        }
    }
}
