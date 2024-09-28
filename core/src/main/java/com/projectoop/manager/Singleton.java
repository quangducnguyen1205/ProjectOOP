package com.projectoop.manager;

public class Singleton<T> {

    // T là lớp con kế thừa từ Singleton
    private static Singleton instance;

    // Constructor private để ngăn việc tạo đối tượng từ bên ngoài
    protected Singleton() {}

    // Lấy instance của Singleton (đảm bảo chỉ có một instance)
    @SuppressWarnings("unchecked")
    public static <T extends Singleton> T getInstance(Class<T> clazz) {
        if (instance == null) {
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating Singleton instance");
            }
        }
        return (T) instance;
    }
}
