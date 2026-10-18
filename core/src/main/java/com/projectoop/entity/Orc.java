package com.projectoop.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.state.IdleState;

import static com.projectoop.utils.Constants.*;

public class Orc extends Enemy {
    public Orc(World world, Vector2 startPosition) {
        super(world, startPosition);
        onInit();

        // Bắt đầu ở trạng thái IdleState
        changeState(IdleState.getInstance());
    }

    @Override
    protected void onBodyDefined() {
        // Nếu cần bổ sung logic đặc biệt sau khi `Body` được tạo, có thể thực hiện tại đây
        // Hiện tại để trống vì không có logic đặc biệt cho Orc
    }

    @Override
    public void onInit() {
        super.onInit();
        // Thêm animation riêng cho Orc
        addAnimation(ANIM_RUN, loadAnimation("assets/Animation/E_Orc/Walk/Walk", 8, 0.1f));
        addAnimation(ANIM_DEATH, loadAnimation("assets/Animation/E_Orc/Die/Die", 4, 0.1f));
        addAnimation(ANIM_ATTACK1, loadAnimation("assets/Animation/E_Orc/Attack/Attack",6, 0.1f));
        addAnimation(ANIM_IDLE, loadAnimation("assets/Animation/E_Orc/Walk/Walk", 4, 0.1f));
    }
    @Override
    public void defineBody() {
        super.defineBody();
    }

    @Override
    public void onDeath() {
        if (body != null) {
            world.destroyBody(body);
            body = null; // Đặt body về null để tránh hủy nhiều lần
        }
        // Animation chết và ngừng di chuyển
        changeAnim(ANIM_DEATH, false); // Sử dụng hằng số ANIM_DEATH để đồng bộ
        setVelocity(Vector2.Zero);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }
}
