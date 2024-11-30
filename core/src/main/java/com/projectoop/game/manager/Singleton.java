package com.projectoop.game.manager;

import java.util.HashMap;
import java.util.Map;

public class Singleton<T> {

    // Lưu trữ các instance của các class khác nhau
    private static Map<Class<?>, Singleton<?>> instances = new HashMap<>();

    // Constructor protected để ngăn việc tạo đối tượng từ bên ngoài
    protected Singleton() {}

    // Phương thức lấy instance, đảm bảo chỉ có một instance cho mỗi class
    @SuppressWarnings("unchecked")
    public static <T extends Singleton<T>> T getInstance(Class<T> clazz) {
        // Kiểm tra xem instance của class này đã tồn tại chưa
        if (!instances.containsKey(clazz)) {
            try {
                // Sử dụng Reflection để khởi tạo instance mới
                T instance = clazz.getDeclaredConstructor().newInstance();
                instances.put(clazz, instance);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating Singleton instance for class: " + clazz.getName());
            }
        }
        // Trả về instance đã tồn tại hoặc mới tạo
        return (T) instances.get(clazz);
    }
}
