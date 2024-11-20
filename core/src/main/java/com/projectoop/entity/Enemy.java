package com.projectoop.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.base.Characters;
import com.projectoop.game.Player;
import com.projectoop.manager.LevelManager;
import com.projectoop.state.IState;
import com.projectoop.utils.Constants;

import static com.projectoop.utils.Constants.PPM;

public abstract class Enemy extends Characters {
    private static final float DETECTION_RADIUS = 0.3f; // Phạm vi phát hiện Player
    private Vector2 velocity;
    private IState<Enemy> currentState;  // Trạng thái hiện tại
    protected Body body;
    protected World world; // Quản lý vật lý Box2D
    private Player player;
    private boolean onGround = false; // Trạng thái tiếp đất
    private float width = 64f / PPM; // Kích thước cố định cho Enemy
    private float height = 64f / PPM; // Kích thước cố định cho Enemy

    public Enemy(World world, Vector2 startPosition) {
        super();
        this.world = world;
        this.velocity = new Vector2(0.5f, 0); // Tốc độ di chuyển mặc định
        this.currentState = new IState<Enemy>() {
            @Override
            public void onEnter(Enemy enemy) {

            }

            @Override
            public void onExecute(Enemy enemy) {

            }

            @Override
            public void onExit(Enemy enemy) {

            }
        }; // Trạng thái khởi tạo mặc định

        // Lấy Player từ LevelManager
        this.player = LevelManager.getInstance(LevelManager.class).getCurrentLevel().getPlayer();

        // Đặt vị trí ban đầu và tạo Body
        setPosition(startPosition.x, startPosition.y);
        defineBody();

        // Khởi động trạng thái đầu tiên
        currentState.onEnter(this);
    }

    // Định nghĩa Body cho Enemy
    public void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / PPM, getY() / PPM);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(8 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.ENEMY_BIT;
        fixtureDef.filter.maskBits = Constants.PLAYER_BIT | Constants.GROUND_BIT | Constants.PIPES_BIT;

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        onBodyDefined();
    }

    // Phương thức tùy chỉnh cho lớp con
    protected void onBodyDefined() {
        // Các lớp con ghi đè nếu cần
    }

    @Override
    public void onInit() {
        hp = 100; // Máu mặc định
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Cập nhật logic của trạng thái hiện tại
        if (currentState != null) {
            currentState.onExecute(this);
        }

        // Cập nhật vị trí từ Body
        if (body != null) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }

    @Override
    public void onDeath() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
        changeAnim("death", false);
        velocity.set(0, 0);
        System.out.println("Enemy has died.");
    }

    @Override
    public void onDespawn() {
        System.out.println("Enemy has been despawned.");
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
        if (body != null) {
            body.setLinearVelocity(velocity);
        }

        // Lật hình ảnh dựa trên hướng di chuyển
        if (velocity.x != 0) {
            setFlip(velocity.x < 0); // Lật hình ảnh nếu vận tốc âm
        }
    }

    // Phương thức lật hình ảnh
    private void setFlip(boolean flipX) {
        setScale(flipX ? -1 : 1, 1); // Đảo hướng trên trục X
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    // Kiểm tra khoảng cách giữa Player và Enemy
    public float getDistanceToPlayer() {
        if (player != null) {
            return getPosition().dst(player.getPosition());
        }
        return Float.MAX_VALUE;
    }

    public boolean isPlayerInRange() {
        return getDistanceToPlayer() <= DETECTION_RADIUS;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        TextureRegion frame = getCurrentFrame();
        if (frame != null) {
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        } else {
            System.out.println("Frame not available for enemy!");
        }
    }

    public void setOnGround(boolean isOnGround) {
        this.onGround = isOnGround;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public Player getPlayer() {
        return player;
    }

    public float getDeltaTime() {
        return com.badlogic.gdx.Gdx.graphics.getDeltaTime();
    }

    public boolean isTouchingObstacle() {
        // Logic kiểm tra va chạm obstacle (sử dụng user data hoặc filter từ Box2D)
        return false; // Thay thế bằng logic cụ thể
    }

    public void performAttack() {
        if (isPlayerInRange()) {
            System.out.println("Attacking...");
            // Gọi logic gây sát thương tại đây
        } else {
            System.out.println("Player out of range, cannot attack.");
        }
    }

    // Thay đổi trạng thái
    public void changeState(IState<Enemy> newState) {
        if (newState == null || currentState == newState) {
            return; // Không thay đổi nếu trạng thái giống nhau hoặc null
        }

        // Thoát trạng thái hiện tại
        if (currentState != null) {
            currentState.onExit(this);
        }

        // Chuyển sang trạng thái mới
        currentState = newState;

        // Kích hoạt trạng thái mới
        currentState.onEnter(this);
    }

    public IState<Enemy> getCurrentState() {
        return currentState;
    }
}
