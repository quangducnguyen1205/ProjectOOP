package com.projectoop.manager;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;

public class AnimationManager {
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;

    public AnimationManager() {
        stateTime = 0f;
    }

    // Phương thức để tạo và load animation từ các ảnh
    public void createAnimation(String folderPath, String baseName, int frameCount, float frameDuration) {
        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < frameCount; i++) {
            String fileName = folderPath + "/" + baseName + String.format("%02d", i) + ".png";
            Texture texture = new Texture(Gdx.files.internal(fileName));
            TextureRegion region = new TextureRegion(texture);
            frames.add(region);
        }

        currentAnimation = new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
        stateTime = 0f; // reset lại thời gian cho animation mới
    }

    // Phương thức để cập nhật animation
    public void update(float delta) {
        stateTime += delta;
    }

    // Phương thức để lấy frame hiện tại
    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime, true); // true: lặp lại animation
    }
}
