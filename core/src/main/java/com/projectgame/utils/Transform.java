package com.projectgame.utils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;

/**
 * Lớp Transform đại diện cho vị trí, rotation và scale của một đối tượng trong không gian 3D.
 */
public class Transform {
    private Vector3 position;    // Vị trí của đối tượng
    private Quaternion rotation; // Rotation của đối tượng
    private Vector3 scale;       // Scale của đối tượng

    // Khởi tạo Transform mặc định
    public Transform() {
        position = new Vector3(0, 0, 0);     // Vị trí mặc định là (0,0,0)
        rotation = new Quaternion();         // Rotation mặc định là không quay
        scale = new Vector3(1, 1, 1);        // Scale mặc định là 1 ở tất cả các trục
    }

    // Lấy vị trí hiện tại của đối tượng
    public Vector3 getPosition() {
        return position;
    }

    // Đặt vị trí mới cho đối tượng
    public void setPosition(Vector3 position) {
        this.position = position;
    }

    // Lấy rotation hiện tại của đối tượng
    public Quaternion getRotation() {
        return rotation;
    }

    // Đặt rotation mới cho đối tượng
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    // Lấy scale hiện tại của đối tượng
    public Vector3 getScale() {
        return scale;
    }

    // Đặt scale mới cho đối tượng
    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    // Đặt vị trí và rotation cùng lúc
    public void setPositionAndRotation(Vector3 position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }
}
