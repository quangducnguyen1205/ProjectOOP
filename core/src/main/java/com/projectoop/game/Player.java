package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        IDLE, RUNNING, JUMPING, FALLING, ATTACKING1, ATTACKING2, ATTACKING3, DYING
    }

    private final Vector2 position;
    private final float speed;
    private final float jumpForce;
    private final float frameWidth = 64f / PPM;
    private final float frameHeight = 64f / PPM;
    private boolean facingRight = true;

    private PlayerState currentState;
    private PlayerState previousState;

    private boolean isOnGround;

    private final World world;
    private Body body;
    private Array<Kunai> kunaiArray;
    private Vector2 currentSavePoint;

    private boolean isDying = false;
    private float deathTimer = 0f;
    private final float deathDuration = 1.0f;

    private boolean levelCompleted;

    private float maxHp = 100; // Máu tối đa
    private float currentHp; // Máu hiện tại
//
//    // **Thêm texture cho thanh máu**
//    private Texture healthBarBg;
//    private Texture healthBarFg;

    public Player(World world, Vector2 startPosition, float speed, float jumpForce) {
        this.world = world;
        this.position = startPosition;
        this.currentSavePoint = startPosition.cpy();
        this.speed = speed;
        this.jumpForce = jumpForce;
        this.kunaiArray = new Array<>();
        this.isOnGround = false;

        this.currentState = PlayerState.IDLE;
        this.previousState = PlayerState.IDLE;

        this.currentHp = maxHp; // Khởi tạo máu đầy đủ

        // **Khởi tạo texture cho thanh máu**
//        healthBarBg = new Texture("HealthBar/bg.png");
//        healthBarFg = new Texture("HealthBar/green.png");

        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(currentSavePoint.x / PPM, currentSavePoint.y / PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(frameWidth / 16 , frameHeight / 16);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits = GROUND_BIT | PIPES_BIT;

        assert body != null;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void onInit() {
        addAnimation(ANIM_ATTACK1,loadAnimation("assets/Animation/KnightAsset/Attack01/Attack01",6,0.1f));
        addAnimation(ANIM_RUN, loadAnimation("assets/Animation/KnightAsset/Run/Run", 8, 0.1f));
        addAnimation(ANIM_IDLE, loadAnimation("assets/Animation/KnightAsset/Idle/Idle", 6, 0.1f));
        addAnimation(ANIM_JUMP, loadAnimation("assets/Animation/KnightAsset/Idle/Idle", 3, 0.1f));
        addAnimation(ANIM_FALL, loadAnimation("assets/Animation/KnightAsset/Idle/Idle", 4, 0.1f));
        addAnimation(ANIM_DEATH, loadAnimation("assets/Animation/KnightAsset/Die/Die", 4, 0.1f));
        changeAnim(ANIM_IDLE, true);
    }

    public void heal(float amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public float getCurrentHealth() {
        return currentHp;
    }

    public float getMaxHealth() {
        return maxHp;
    }

    @Override
    public void onDeath() {
        if (!isDying) {
            changeAnim(ANIM_DEATH, false);
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
            isDying = true;
            deathTimer = 0f;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isDying) {
            deathTimer += delta;
            if (deathTimer >= deathDuration) {
                dispose(); // Chuyển sang gọi dispose thay vì onDespawn
            }
            return;
        }

        handleInput();
        updateState();

        position.set(body.getPosition().x - frameWidth / 2, body.getPosition().y - frameHeight / 2);

        for (Kunai kunai : kunaiArray) {
            kunai.update(delta);
        }

        if (currentState != previousState) {
            updateAnimation();
            previousState = currentState;
        }

    }

    @Override
    public void dispose() {
        // Giải phóng tài nguyên liên quan
//        if (healthBarBg != null) {
//            healthBarBg.dispose();
//        }
//        if (healthBarFg != null) {
//            healthBarFg.dispose();
//        }

        if (body != null && world != null) {
            world.destroyBody(body); // Hủy body khỏi world
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.setLinearVelocity(new Vector2(speed, body.getLinearVelocity().y));
            facingRight = true;
            if (currentState != PlayerState.JUMPING && currentState != PlayerState.FALLING) {
                currentState = PlayerState.RUNNING;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.setLinearVelocity(new Vector2(-speed, body.getLinearVelocity().y));
            facingRight = false;
            if (currentState != PlayerState.JUMPING && currentState != PlayerState.FALLING) {
                currentState = PlayerState.RUNNING;
            }
        } else if (currentState != PlayerState.JUMPING && currentState != PlayerState.FALLING) {
            body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
            currentState = PlayerState.IDLE;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && isOnGround) {
            body.applyLinearImpulse(new Vector2(0, jumpForce), body.getWorldCenter(), true);
            currentState = PlayerState.JUMPING;
        }

        // Tấn công
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            attack1();
        } else if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            attack2();
        } else if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            attack3();
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
            case ATTACKING1:
                changeAnim(ANIM_ATTACK1, false);
                break;
            case ATTACKING2:
                changeAnim(ANIM_ATTACK2, false);
                break;
            case ATTACKING3:
                changeAnim(ANIM_ATTACK3, false);
                break;
            case IDLE:
            default:
                changeAnim(ANIM_IDLE, true);
                break;
        }
    }
    // Các phương thức tấn công
    private void attack1() {
        if (currentState != PlayerState.ATTACKING1) {
            currentState = PlayerState.ATTACKING1;
            // Thực hiện logic tấn công 1
        }
    }

    private void attack2() {
        if (currentState != PlayerState.ATTACKING2) {
            currentState = PlayerState.ATTACKING2;
            // Thực hiện logic tấn công 2
        }
    }

    private void attack3() {
        if (currentState != PlayerState.ATTACKING3) {
            currentState = PlayerState.ATTACKING3;
            // Thực hiện logic tấn công 3 (ví dụ: bắn kunai)
            Kunai kunai = new Kunai(world, body.getPosition(), 5f/PPM, 10);
            kunaiArray.add(kunai);
        }
    }

    public void render(Batch batch) {
        // Vẽ nhân vật
        TextureRegion currentFrame = getCurrentFrame();
        if (currentFrame != null) {
            if (facingRight) {
                batch.draw(currentFrame, position.x, position.y, frameWidth, frameHeight);
            } else {
                batch.draw(currentFrame, position.x + frameWidth, position.y, -frameWidth, frameHeight);
            }
        }

        // Vẽ thanh máu
        //drawHealthBar(batch);

        // Vẽ Kunai
        for (Kunai kunai : kunaiArray) {
            kunai.render(batch);
        }
    }

    public boolean hasCompletedLevel() {
        return levelCompleted;
    }

    public void resetLevelCompletion() {
        this.levelCompleted = false;
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
