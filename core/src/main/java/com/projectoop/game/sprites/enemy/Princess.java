package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.NullScreen;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.ui.UIManager;
import com.projectoop.game.ui.lastlevel.CrushStoryCanvas;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Princess extends GroundEnemy {
    public Princess(PlayScreen screen, float x, float y) {
        super(screen, x, y, 0.3f, 2, 10);
    }

    @Override
    protected void prepareAnimation(){
        atlasWalking = new TextureAtlas("Congchua/Pack/Idle.pack");
        atlasAttacking = new TextureAtlas("Congchua/Pack/Run.pack");

        walkAnimation = new Animation<TextureRegion>(0.6f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.6f, atlasAttacking.getRegions());
    }
    @Override
    protected void defineEnemy(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX()+15/GameWorld.PPM, getY()+25/GameWorld.PPM); //(2506.8,60.18799)
        bdef.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6/GameWorld.PPM, 15/GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.PRINCESS_BIT;
        //Collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.CHEST_BIT |
            GameWorld.PILAR_BIT | GameWorld.ARROW_BIT |
            GameWorld.KNIGHT_SWORD_LEFT | GameWorld.KNIGHT_SWORD_RIGHT| GameWorld.KNIGHT_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;


        currentState = State.WALKING;
        TextureRegion frame;

        frame = walkAnimation.getKeyFrame(stateTime, true);

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !frame.isFlipX()) {
            frame.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && frame.isFlipX()) {
            frame.flip(true, false);
            runningRight = true;
        }

        setBounds(getX(), getY(), frame.getRegionWidth() / GameWorld.PPM, frame.getRegionHeight() / GameWorld.PPM);
        setRegion(frame);
    }

    @Override
    public State getState() {
        return State.WALKING;
    }

    @Override
    public void hurtingCallBack() {
        System.out.print("AYE");
        //screen.game.setScreen(new WinScreen(screen.game));
        // Tạo một Timer để trì hoãn
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Đảm bảo việc chuyển màn hình diễn ra trên thread chính
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        GameManager.getInstance(GameManager.class).setScreen(new NullScreen());
                        UIManager.getInstance(UIManager.class).openUI(CrushStoryCanvas.class);
                    }
                });
            }
        }, 2000); // 5000 milliseconds = 5 seconds
    }

    @Override
    public void attackingCallBack() {

    }

    @Override
    public void destroy() {

        setToDestroy = true;
    }

    @Override
    public void hitOnHead() {

    }
    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            TextureRegion frame = getFrame(stateTime);
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        }
    }
    @Override
    public void dispose() {

    }


}
