package com.projectoop.base;

import com.badlogic.gdx.math.Vector2;

public abstract class Characters {

    protected float hp;
    protected String currentAnimName;
    //protected HealthBar healthBar;
    //protected CombatText combatTextPrefab;

    public boolean isDead() {
        return hp <= 0;
    }

    // Khởi tạo nhân vật
    public void start() {
        onInit();
    }

    // Phương thức abstract để các lớp con tự định nghĩa
    public abstract void onInit(); // Khởi tạp các giá trị ban đầu
    public abstract void onDespawn(); // Khởi tạo logic khi object bị huỷ
    public abstract void onDeath(); // Khởi tạo logic khi chết

    // Thay đổi animation
//    protected void changeAnim(String animName) {
//        if (!animName.equals(currentAnimName)) {
//            // Reset animation (mã chi tiết sẽ tùy thuộc vào cách bạn quản lý animation trong LibGDX)
//            resetTrigger(animName);
//            currentAnimName = animName;
//            setTrigger(currentAnimName);
//        }
//    }

    // Phương thức xử lý khi bị tấn công
    public void onHit(float damage) {
        if (!isDead()) {
            hp -= damage;
            //healthBar.setNewHp(hp);
            if (isDead()) {
                hp = 0;
                onDeath();
            }

            // Hiển thị combat text
            //combatTextPrefab.onInit(damage, new Vector2(0, 1));
        }
    }

    // Các phương thức giả định cho việc xử lý animation
    //protected void resetTrigger(String animName) {
        // Giả định hàm này sẽ reset animation (cần dựa vào cách bạn quản lý animation trong LibGDX)
    //}

    //protected void setTrigger(String animName) {
        // Giả định hàm này sẽ set animation (cần dựa vào cách bạn quản lý animation trong LibGDX)
    //}
}

