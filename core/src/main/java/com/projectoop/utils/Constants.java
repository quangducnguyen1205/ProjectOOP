package com.projectoop.utils;

public class Constants {

    // Bitmask for collision detection
    public static final short PLAYER_BIT = 1;
    public static final short ENEMY_BIT = 2;
    public static final short GROUND_BIT = 3;
    public static final short PIPES_BIT = 4;
    public static final short KILLZONE_BIT = 5;
    public static final short FINISHZONE_BIT = 6;

    // Pixels per meter (PPM) - used for scaling between the game world and the physical world in Box2D
    public static final float PPM = 100f; // 100 pixels per meter, keeps physics and map scaled properly

    // Virtual resolution for the game screen
    public static final float VIRTUAL_WIDTH = 400f;  // Width of the game screen
    public static final float VIRTUAL_HEIGHT = 300f;  // Height of the game screen

    // Animation constants
    public static final String ANIM_ATTACK1 = "attack1";
    public static final String ANIM_ATTACK2 = "attack2";
    public static final String ANIM_ATTACK3 = "attack3";
    public static final String ANIM_RUN = "run";
    public static final String ANIM_IDLE = "idle";
    public static final String ANIM_DEATH = "death";
    public static final String ANIM_JUMP = "jump";
    public static final String ANIM_FALL = "fall";
    public static final String ANIM_HURT = "hurt";
    public static final String ANIM_DANCE = "dance";
    public static final String ANIM_GLIDE = "glide";
    public static final String ANIM_CLIMB = "climb";
    public static final String ANIM_SLEEPIDLE = "sleepidle";
    public static final String ANIM_SLIDE = "slide";
    public static final String ANIM_THROW = "throw";

    public static final float WEAPON_WIDTH = 10f;
    public static final float WEAPON_HEIGHT = 10f;

    // Kunai
    public static final float KUNAI_OFFSET_X = 15f; // Độ lệch từ player
    public static final float KUNAI_OFFSET_Y = 5f;  // Độ lệch từ player
    public static final short WEAPON_BIT = 0x0004;
}
