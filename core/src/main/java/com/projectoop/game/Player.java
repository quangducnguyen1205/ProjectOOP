package com.projectoop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.base.Characters;
import com.projectoop.utils.Constants;
import com.projectoop.weapons.Kunai;

public class Player extends Characters {

    private final Vector2 position;
    private final float speed;
    private final float jumpForce;
    private final float frameWidth = 16f;
    private final float frameHeight = 16f;
    private boolean facingRight = true;
    private boolean isJumping = false;
    private boolean isFalling = false;

    private Vector2 instantiatePoint;
    private final float throwSpeed = 3f;

    private final World world;
    private Body body;
    private Array<Kunai> kunaiArray;

    // Thêm biến để lưu trữ UserData
    private PlayerData playerData;

    public Player(World world, Vector2 startPosition, float speed, float jumpForce) {
        this.world = world;
        this.position = startPosition;
        this.speed = speed;
        this.jumpForce = jumpForce;
        this.kunaiArray = new Array<>();
        this.playerData = new PlayerData(this); // Khởi tạo PlayerData
        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / Constants.PPM, position.y / Constants.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(frameWidth / 2 / Constants.PPM, frameHeight / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f; // Điều chỉnh mật độ để cải thiện phản hồi
        fixtureDef.friction = 0.4f; // Tăng ma sát để tránh trượt
        fixtureDef.restitution = 0f;

        fixtureDef.filter.categoryBits = Constants.PLAYER_BIT;
        fixtureDef.filter.maskBits = Constants.GROUND_BIT | Constants.OBSTACLE_BIT;

        // Log chi tiết về Player Fixture
        System.out.println("Player Fixture created: categoryBits = " +
            fixtureDef.filter.categoryBits + ", maskBits = " + fixtureDef.filter.maskBits);

        if (body != null) {
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(playerData); // Gán PlayerData làm UserData

            // Log chi tiết về fixture của Player
            System.out.println("Player body created at: " + body.getPosition());
            System.out.println("Player Fixture categoryBits = " + fixtureDef.filter.categoryBits);
            System.out.println("Player Fixture maskBits = " + fixtureDef.filter.maskBits);
        }
        shape.dispose();
    }


    @Override
    public void onInit() {
        hp = 100;
        currentAnimName = Constants.ANIM_IDLE;

        // Sử dụng lại logic addAnimation như phiên bản trước
        addAnimation(Constants.ANIM_RUN, loadAnimation("assets/Ninja/Run/Run", 10, 0.1f));
        addAnimation(Constants.ANIM_IDLE, loadAnimation("assets/Ninja/Idle/Idle", 10, 0.1f));
        addAnimation(Constants.ANIM_ATTACK, loadAnimation("assets/Ninja/Attack/Attack", 10, 0.1f));
        addAnimation(Constants.ANIM_JUMP, loadAnimation("assets/Ninja/Jump/Jump", 6, 0.1f));
        addAnimation(Constants.ANIM_FALL, loadAnimation("assets/Ninja/Fall/Fall", 4, 0.1f));
        changeAnim(Constants.ANIM_IDLE, true);

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
    public void onDespawn() {
        System.out.println("Player despawned.");
    }

    @Override
    public void onDeath() {
        System.out.println("Player died.");
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Kiểm tra trạng thái tiếp đất từ PlayerData
        boolean isOnGround = playerData.isOnGround();

        // Log chi tiết nếu Player không tiếp xúc mặt đất
        if (!isOnGround) {
            System.out.println("Player is in the air, position: " + body.getPosition().x + ", " + body.getPosition().y);
        } else {
            System.out.println("Player is on the ground, position: " + body.getPosition().x + ", " + body.getPosition().y);
        }

        // Kiểm tra nhảy
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && isOnGround) {
            body.applyLinearImpulse(new Vector2(0, jumpForce), body.getWorldCenter(), true);
            changeAnim(Constants.ANIM_JUMP, false);
        }

        // Kiểm tra trạng thái rơi
        if (!isOnGround && body.getLinearVelocity().y < 0) {
            changeAnim(Constants.ANIM_FALL, false);
        }

        // Kiểm tra trạng thái tiếp đất
        if (isOnGround && body.getLinearVelocity().y == 0) {
            changeAnim(Constants.ANIM_IDLE, true);
        }

        // Di chuyển trái/phải và cập nhật animation
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(new Vector2(speed / Constants.PPM, body.getLinearVelocity().y));
            facingRight = true;
            if (isOnGround) {
                changeAnim(Constants.ANIM_RUN, true);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.setLinearVelocity(new Vector2(-speed / Constants.PPM, body.getLinearVelocity().y));
            facingRight = false;
            if (isOnGround) {
                changeAnim(Constants.ANIM_RUN, true);
            }
        } else if (isOnGround) {
            body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
            changeAnim(Constants.ANIM_IDLE, true);
        }

        // Cập nhật vị trí Player
        position.set(body.getPosition().x * Constants.PPM - frameWidth / 2,
            body.getPosition().y * Constants.PPM - frameHeight / 2);

        // Xử lý ném Kunai
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            throwKunai();
        }

        // Cập nhật Kunai
        for (Kunai kunai : kunaiArray) {
            kunai.update(delta);
        }
        System.out.println(position.y);
    }

    private void throwKunai() {
        Vector2 kunaiStartPosition = new Vector2(
            position.x + (facingRight ? Constants.KUNAI_OFFSET_X : -Constants.KUNAI_OFFSET_X),
            position.y + Constants.KUNAI_OFFSET_Y
        );
        Kunai kunai = new Kunai(world, kunaiStartPosition, facingRight ? throwSpeed : -throwSpeed, 20);
        kunaiArray.add(kunai);
        System.out.println("Throwing Kunai.");
    }

    public void render(Batch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        if (currentFrame != null) {
            batch.begin();
            if (facingRight) {
                batch.draw(currentFrame, position.x, position.y, frameWidth, frameHeight);
            } else {
                batch.draw(currentFrame, position.x + frameWidth, position.y, -frameWidth, frameHeight);
            }
            batch.end();
        }

        // Vẽ tất cả Kunai
        for (Kunai kunai : kunaiArray) {
            kunai.render(batch);
        }
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return position;
    }
}
