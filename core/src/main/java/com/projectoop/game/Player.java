package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.base.Characters;
import com.projectoop.weapons.Kunai;

import static com.projectoop.utils.Constants.*;

public class Player extends Characters {

    private enum PlayerState {
        IDLE, RUNNING, JUMPING, FALLING, ATTACKING
    }

    private final Vector2 position;
    private final float speed;
    private final float jumpForce;
    private final float frameWidth = 16f / PPM;
    private final float frameHeight = 16f / PPM;
    private boolean facingRight = true;

    private PlayerState currentState;
    private PlayerState previousState;

    private boolean isOnGround; // Trạng thái tiếp đất

    private final World world;
    private Body body;
    private Array<Kunai> kunaiArray;
    private Vector2 currentSavePoint;

    private boolean isDying = false; // Kiểm tra xem Player đang trong trạng thái chết
    private float deathTimer = 0f;  // Bộ đếm thời gian cho hoạt ảnh chết
    private final float deathDuration = 1.0f; // Thời gian hoạt ảnh chết (giây)

    public Player(World world, Vector2 startPosition, float speed, float jumpForce) {
        this.world = world;
        this.position = startPosition;
        this.currentSavePoint = startPosition.cpy();
        this.speed = speed;
        this.jumpForce = jumpForce;
        this.kunaiArray = new Array<>();
        this.isOnGround = false; // Ban đầu không tiếp đất

        this.currentState = PlayerState.IDLE;
        this.previousState = PlayerState.IDLE;

        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(currentSavePoint.x / PPM, currentSavePoint.y / PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(frameWidth / 2, frameHeight / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits = GROUND_BIT | OBSTACLE_BIT;

        assert body != null;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void onInit() {
        hp = 100;
        addAnimation(ANIM_RUN, loadAnimation("assets/Ninja/Run/Run", 10, 0.1f));
        addAnimation(ANIM_IDLE, loadAnimation("assets/Ninja/Idle/Idle", 10, 0.1f));
        addAnimation(ANIM_JUMP, loadAnimation("assets/Ninja/Jump/Jump", 6, 0.1f));
        addAnimation(ANIM_FALL, loadAnimation("assets/Ninja/Fall/Fall", 4, 0.1f));
        addAnimation(ANIM_DEATH, loadAnimation("assets/Ninja/Dead/Dead", 10, 0.1f));
        changeAnim(ANIM_IDLE, true);
    }

    @Override
    public void onDespawn() {
        // Đặt lại vị trí Body tới SavePoint
        body.setTransform(currentSavePoint.x / PPM, currentSavePoint.y / PPM, 0);

        // Reset trạng thái Player
        currentState = PlayerState.IDLE;
        previousState = PlayerState.IDLE;

        // Đặt lại hoạt ảnh về IDLE
        changeAnim(ANIM_IDLE, true);

        isDying = false; // Reset trạng thái chết
    }

    @Override
    public void onDeath() {
        if (!isDying) {
            System.out.println("1");
            changeAnim(ANIM_DEATH, false); // Chạy hoạt ảnh chết

            // Dừng tất cả chuyển động vật lý
            body.setLinearVelocity(0, 0); // Ngừng di chuyển
            body.setAngularVelocity(0); // Ngừng xoay

            isDying = true;
            deathTimer = 0f; // Reset bộ đếm thời gian
        }
    }

    private Animation<TextureRegion> loadAnimation(String folderPath, int frameCount, float frameDuration) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            String fileName = folderPath + "__00" + i + ".png";
            frames.add(new TextureRegion(new Texture(Gdx.files.internal(fileName))));
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isDying) {
            deathTimer += delta; // Tăng bộ đếm thời gian theo delta
            if (deathTimer >= deathDuration) { // Nếu đủ thời gian hoạt ảnh chết
                onDespawn(); // Chạy logic hồi sinh tại SavePoint
                System.out.println("2");
            }
            return; // Không cập nhật gì thêm khi Player đang chết
        }

        handleInput();
        updateState();

        // Update position based on Box2D physics
        position.set(body.getPosition().x - frameWidth / 2, body.getPosition().y - frameHeight / 2);

        // Update Kunai logic
        for (Kunai kunai : kunaiArray) {
            kunai.update(delta);
        }

        // Update animation if state changes
        if (currentState != previousState) {
            updateAnimation();
            previousState = currentState;
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(new Vector2(speed, body.getLinearVelocity().y));
            facingRight = true;
            if (currentState != PlayerState.JUMPING && currentState != PlayerState.FALLING) {
                currentState = PlayerState.RUNNING;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.setLinearVelocity(new Vector2(-speed, body.getLinearVelocity().y));
            facingRight = false;
            if (currentState != PlayerState.JUMPING && currentState != PlayerState.FALLING) {
                currentState = PlayerState.RUNNING;
            }
        } else if (currentState != PlayerState.JUMPING && currentState != PlayerState.FALLING) {
            body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
            currentState = PlayerState.IDLE;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && isOnGround) {
            body.applyLinearImpulse(new Vector2(0, jumpForce), body.getWorldCenter(), true);
            currentState = PlayerState.JUMPING;
        }
    }

    private void updateState() {
        if (!isOnGround && body.getLinearVelocity().y < 0) {
            currentState = PlayerState.FALLING;
        } else if (isOnGround && currentState == PlayerState.FALLING) {
            currentState = PlayerState.IDLE;
        }
    }

    private void updateAnimation() {
        switch (currentState) {
            case RUNNING:
                changeAnim(ANIM_RUN, true);
                break;
            case JUMPING:
                changeAnim(ANIM_JUMP, false);
                break;
            case FALLING:
                changeAnim(ANIM_FALL, false);
                break;
            case IDLE:
            default:
                changeAnim(ANIM_IDLE, true);
                break;
        }
    }

    public void render(Batch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        if (currentFrame != null) {
            if (facingRight) {
                batch.draw(currentFrame, position.x, position.y, frameWidth, frameHeight);
            } else {
                batch.draw(currentFrame, position.x + frameWidth, position.y, -frameWidth, frameHeight);
            }
        }
        for (Kunai kunai : kunaiArray) {
            kunai.render(batch);
        }
    }

    public void setSavePoint(Vector2 savePoint) {
        if (savePoint != null) {
            this.currentSavePoint = savePoint.cpy(); // Cập nhật SavePoint
            System.out.println("SavePoint updated to: " + currentSavePoint);
        } else {
            System.out.println("Warning: Attempted to set SavePoint to null.");
        }
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return new Vector2(body.getPosition().x, body.getPosition().y);
    }

    public void setOnGround(boolean onGround) {
        this.isOnGround = onGround;
    }

    public boolean isOnGround() {
        return isOnGround;
    }
    public boolean isDying() {
        return isDying;
    }

}
