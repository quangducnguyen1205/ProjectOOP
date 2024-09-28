package com.projectoop.pooling;

import com.projectoop.utils.Transform;

/**
 * Class đại diện cho các đối tượng trong game, mỗi đối tượng thuộc một loại pool nhất định.
 */
public class GameUnit implements Cloneable {
    private Transform tf;  // Quản lý vị trí, rotation, và scale
    private PoolType poolType;  // Kiểu pool mà GameUnit này thuộc về
    private boolean active;  // Quản lý trạng thái hoạt động của đối tượng

    // Lấy đối tượng Transform
    public Transform getTransform() {
        if (tf == null) {
            tf = new Transform();  // Nếu chưa có, tạo mới Transform
        }
        return tf;
    }

    // Đặt kiểu pool cho GameUnit này
    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }

    public PoolType getPoolType() {
        return this.poolType;
    }

    // Đặt trạng thái hoạt động của đối tượng
    public void setActive(boolean active) {
        this.active = active;
    }

    // Kiểm tra xem đối tượng có đang hoạt động không
    public boolean isActive() {
        return this.active;
    }

    // Gọi hàm onDespawn sau một khoảng thời gian delay
    public void onDespawn(float delay) {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                onDespawn();
            }
        }, (long) (delay * 1000));  // Delay được tính bằng giây
    }

    // Hàm xử lý despawn - trả về pool
    private void onDespawn() {
        SimplePool.despawn(this);  // Gọi SimplePool để trả đối tượng này về pool
    }

    // Override phương thức clone để cho phép sao chép đối tượng
    @Override
    public GameUnit clone() {
        try {
            return (GameUnit) super.clone();  // Gọi clone từ Object và ép kiểu về GameUnit
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // Xử lý lỗi clone
        }
    }
}
