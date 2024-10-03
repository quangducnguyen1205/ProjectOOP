package com.projectoop.base;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class Characters {

    protected float hp;
    protected String currentAnimName;
    protected Animation<TextureRegion> currentAnimation;
    protected ObjectMap<String, Animation<TextureRegion>> animations;
    protected float stateTime;

    public Characters() {
        animations = new ObjectMap<>();
        stateTime = 0f;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    // Phương thức abstract cho lớp con tự định nghĩa
    public abstract void onInit();
    public abstract void onDespawn();
    public abstract void onDeath();

    // Thêm animation vào danh sách
    public void addAnimation(String name, Animation<TextureRegion> animation) {
        animations.put(name, animation);
    }

    // Thay đổi animation
    protected void changeAnim(String animName) {
        if (animations.containsKey(animName)) {
            currentAnimName = animName;
            setTrigger(currentAnimName);
        } else {
            System.out.println("Animation không tồn tại: " + animName);
        }
    }

    // Reset lại thời gian cho animation mới
    private void setTrigger(String animName) {
        currentAnimation = animations.get(animName);
        if (currentAnimation == null) {
            throw new IllegalArgumentException("Animation không tồn tại: " + animName);
        }
        stateTime = 0f;
    }


    // Cập nhật thời gian của animation
    public void update(float delta) {
        stateTime += delta;
    }

    // Lấy frame hiện tại của animation
    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime, true);
    }
}
