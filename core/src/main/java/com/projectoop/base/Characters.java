package com.projectoop.base;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class Characters extends Sprite {

    protected float hp;
    protected String currentAnimName;
    protected Animation<TextureRegion> currentAnimation;
    protected ObjectMap<String, Animation<TextureRegion>> animations;
    protected float stateTime;
    protected boolean loopAnimation; // Biến để kiểm soát việc loop animation

    public Characters() {
        super();
        animations = new ObjectMap<>();
        stateTime = 0f;
        loopAnimation = true; // Mặc định là loop animation
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public abstract void onInit();
    public abstract void onDespawn();
    public abstract void onDeath();

    // Thêm animation vào danh sách
    public void addAnimation(String name, Animation<TextureRegion> animation) {
        animations.put(name, animation);
    }

    // Thay đổi animation hiện tại với kiểm soát việc loop
    protected void changeAnim(String animName, boolean loop) {
        if (animations.containsKey(animName)) {
            if (!animName.equals(currentAnimName)) {  // Chỉ thay đổi nếu animation khác với hiện tại
                currentAnimName = animName;
                loopAnimation = loop;  // Gán giá trị loop theo biến truyền vào
                setTrigger(currentAnimName);
                //System.out.println("animation: " + currentAnimName);
            }
        } else {
            System.out.println("Animation không tồn tại: " + animName);
        }
    }

    private void setTrigger(String animName) {
        currentAnimation = animations.get(animName);
        if (currentAnimation == null) {
            throw new IllegalArgumentException("Animation không tồn tại: " + animName);
        }
        stateTime = 0f;
    }

    public void update(float delta) {
        stateTime += delta;

        // Cập nhật TextureRegion của Sprite theo animation
        TextureRegion frame = getCurrentFrame();
        if (frame != null) {
            setRegion(frame); // Set khung hình hiện tại làm texture
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    // Lấy khung hình hiện tại của animation, kiểm soát việc loop
    public TextureRegion getCurrentFrame() {
        if (currentAnimation != null) {
            TextureRegion frame = currentAnimation.getKeyFrame(stateTime, loopAnimation);
            if (frame == null) {
                System.out.println("Current frame is null for animation: " + currentAnimName);
            }
            return frame;
        } else {
            System.out.println("Current animation is null. Animation name: " + currentAnimName);
        }
        return null; // Trường hợp không có animation
    }
}
