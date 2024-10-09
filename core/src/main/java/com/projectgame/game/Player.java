package com.projectgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.projectgame.base.Characters;
import com.projectgame.utils.Constants;

public class Player extends Characters {

    private Vector2 position;
    private float speed = 200f; // Tốc độ di chuyển
    private float frameWidth = 64f; // Chiều rộng của khung hình (giả định)
    private float frameHeight = 64f; // Chiều cao của khung hình (giả định)
    private boolean facingRight = true; // Để biết nhân vật đang quay hướng nào

    public Player(Vector2 startPosition) {
        this.position = startPosition;
    }

    @Override
    public void onInit() {
        hp = 100;
        currentAnimName = Constants.ANIM_IDLE;

        addAnimation(Constants.ANIM_RUN, loadAnimation("assets/Ninja/Run/Run", 10, 0.1f));
        addAnimation(Constants.ANIM_IDLE, loadAnimation("assets/Ninja/Idle/Idle", 10, 0.1f));
        changeAnim(Constants.ANIM_IDLE);
    }

    private Animation<TextureRegion> loadAnimation(String folderPath, int frameCount, float frameDuration) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            String fileName = folderPath + "__00" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(fileName));
            frames.add(new TextureRegion(texture));
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    @Override
    public void onDespawn() {}

    @Override
    public void onDeath() {}

    public void update(float delta) {

        super.update(delta);

        // Kiểm tra hướng di chuyển và thay đổi animation nếu cần
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !currentAnimName.equals(Constants.ANIM_RUN)) {
            changeAnim(Constants.ANIM_RUN);
        }
        else if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !currentAnimName.equals(Constants.ANIM_IDLE)) {
            changeAnim(Constants.ANIM_IDLE);
        }

        // Di chuyển nhân vật
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * delta;
            facingRight = true; // Quay về bên phải
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * delta;
            facingRight = false; // Quay về bên trái
        }
    }

    public void render(Batch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        if (currentFrame != null) {
            if (facingRight) {
                batch.draw(currentFrame, position.x, position.y, frameWidth, frameHeight);
            } else {
                // Vẽ hình ảnh lật theo trục x khi di chuyển sang trái
                batch.draw(currentFrame, position.x + frameWidth, position.y, -frameWidth, frameHeight);
            }
        }

        // Debug thông tin frame hiện tại
        System.out.println("Current frame: " + currentFrame);
    }

    public Vector2 getPosition() {
        return position;
    }
}
