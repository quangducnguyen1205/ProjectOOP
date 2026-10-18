package com.projectoop.entity;

import com.badlogic.gdx.graphics.Texture;
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
    private static final float DETECTED_RADIUS = 0.5f; // Phạm vi phát hiện Player
    private static final float ATTACK_RADIUS = 0.3f;
    private Vector2 velocity;
    private IState<Enemy> currentState;  // Trạng thái hiện tại
    protected Body body;
    protected World world; // Quản lý vật lý Box2D
    private Player player;
    private float width = 64f / PPM; // Kích thước cố định cho Enemy
    private float height = 64f / PPM; // Kích thước cố định cho Enemy
    private boolean isFacingRight = true; // Trạng thái hướng hiện tại
    private float moveSpeed = 0.5f;
    private boolean isDying = false; // Đánh dấu trạng thái đang chết
    private float deathTimer = 0f; // Bộ đếm thời gian chết
    private final float deathDuration = 0.4f; // Thời gian hiển thị anim chết


    // **Thêm thanh máu**
    private Texture healthBarBg;
    private Texture healthBarFg;
    private float maxHealth = 100;  // Máu tối đa

    public Enemy(World world, Vector2 startPosition) {
        super();
        this.world = world;
        this.velocity = new Vector2(moveSpeed, 0); // Tốc độ di chuyển mặc định
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

        // **Khởi tạo thanh máu**
        healthBarBg = new Texture("HealthBar/bg.png");
        healthBarFg = new Texture("HealthBar/green.png");
        hp = maxHealth;
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

        if (isDying) {
            deathTimer += delta;
            if (deathTimer >= deathDuration) {
                dispose(); // Hủy Enemy sau khi anim chết hoàn tất
            }
            return; // Không thực hiện các logic khác khi Enemy đang chết
        }

        // Cập nhật logic của trạng thái hiện tại
        if (currentState != null) {
            currentState.onExecute(this);
        }

        // Cập nhật vị trí từ Body
        if (body != null) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }
    public void move() {
        changeAnim(Constants.ANIM_RUN, true); // Animation di chuyển
        if (body != null) {
            body.setLinearVelocity(isFacingRight ? moveSpeed : -moveSpeed, body.getLinearVelocity().y);
        }
    }

    @Override
    public void onDeath() {
        if (!isDying) {
            isDying = true;
            changeAnim(Constants.ANIM_DEATH, false); // Hiển thị anim chết
            velocity.set(0, 0);
            System.out.println("Enemy is dying...");
        }
    }
    @Override
    public void dispose() {
        if (healthBarBg != null) healthBarBg.dispose();
        if (healthBarFg != null) healthBarFg.dispose();

        if (body != null) {
            assert world != null;
            if (!world.isLocked()) { // Chỉ hủy nếu World không bị khóa
                world.destroyBody(body);
            }
        }
        body = null;
        System.out.println("Enemy resources disposed.");
    }

    public void attack(){
        if (isPlayerAttacked() && player != null) {
            changeAnim(Constants.ANIM_ATTACK1, false);
            player.takeDamage(10); // Giảm máu của Player
            System.out.println("Enemy attacks Player!");
        }
    }

    public void stopMoving() {
        changeAnim(Constants.ANIM_IDLE, true);
        setVelocity(Vector2.Zero); // Ngừng di chuyển
    }

    public void changeDirection(boolean isRight) {
        if (isFacingRight != isRight) {
            isFacingRight = isRight;
            velocity.x = isRight ? moveSpeed : -moveSpeed; // Đặt hướng di chuyển
        }
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
        if (body != null) {
            body.setLinearVelocity(velocity);
        }
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
        return player != null && getDistanceToPlayer() <= DETECTED_RADIUS;
    }

    public boolean isPlayerAttacked() {
        return player != null && getDistanceToPlayer() <= ATTACK_RADIUS;
    }

    public void drawHealthBar(Batch batch) {
        float healthPercentage = hp / maxHealth;
        float barWidth = 50 / PPM;
        float barHeight = 8 / PPM;

        float barX = body.getPosition().x - barWidth / 2;
        float barY = body.getPosition().y + height / 2 - 20/PPM; // Thanh máu trên đầu enemy

        batch.draw(healthBarBg, barX, barY, barWidth, barHeight);  // Nền
        batch.draw(healthBarFg, barX, barY, barWidth * healthPercentage, barHeight);  // Máu
    }

    @Override
    public void draw(Batch batch) {
        //super.draw(batch);
        TextureRegion frame = getCurrentFrame(); // Lấy frame hiện tại
        if (frame != null) {
            if (isFacingRight) {
                // Vẽ hướng phải
                batch.draw(frame, getX(), getY(), getWidth(), getHeight());
            } else {
                // Vẽ hướng trái (đảo ngược frameWidth)
                batch.draw(frame, getX() + getWidth(), getY(), -getWidth(), getHeight());
            }
        } else {
            System.out.println("Frame not available for enemy!");
        }
        drawHealthBar(batch);
    }


    public void setOnGround(boolean isOnGround) {
        // Trạng thái tiếp đất
    }

    public Player getPlayer() {
        return player;
    }

    public float getDeltaTime() {
        return com.badlogic.gdx.Gdx.graphics.getDeltaTime();
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

    public Boolean getIsFacingRight() {
        return isFacingRight;
    }

    public IState<Enemy> getCurrentState() {
        return currentState;
    }
}
