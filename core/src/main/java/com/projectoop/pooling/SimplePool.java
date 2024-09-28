package com.projectoop.pooling;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.projectoop.utils.Transform;
import java.util.*;

/**
 * SimplePool quản lý các đối tượng GameUnit, tái sử dụng chúng thông qua cơ chế Object Pooling.
 */
public class SimplePool {

    /**
     * Lớp Pool quản lý việc spawn và despawn các đối tượng GameUnit.
     */
    static class Pool {
        private Transform root;  // Đối tượng cha chứa các đơn vị trong pool
        private Queue<GameUnit> inactive;  // Các đối tượng chưa được sử dụng
        private List<GameUnit> active;     // Các đối tượng đang hoạt động
        private GameUnit prefab;           // Prefab cho GameUnit

        // Trả về danh sách các đối tượng đang hoạt động
        public List<GameUnit> getActive() {
            return active;
        }

        // Trả về tổng số đối tượng trong pool (bao gồm cả đối tượng chưa sử dụng)
        public int getCount() {
            return inactive.size() + active.size();
        }

        // Trả về đối tượng root
        public Transform getRoot() {
            return root;
        }

        // Constructor
        public Pool(GameUnit prefab, int initialQty, Transform parent) {
            this.root = parent;
            this.prefab = prefab;
            this.inactive = new LinkedList<>();
            this.active = new ArrayList<>();

            // Tạo sẵn số lượng đối tượng ban đầu trong pool
            for (int i = 0; i < initialQty; i++) {
                GameUnit obj = instantiateNewObject();
                inactive.add(obj);  // Thêm vào hàng đợi các đối tượng chưa sử dụng
            }
        }

        /**
         * Tạo đối tượng mới từ prefab.
         * @return Đối tượng GameUnit mới.
         */
        private GameUnit instantiateNewObject() {
            GameUnit obj = prefab.clone();  // Giả sử GameUnit có hàm clone() để sao chép đối tượng
            obj.setActive(false);           // Vô hiệu hóa đối tượng ngay khi tạo ra
            return obj;
        }

        /**
         * Sinh đối tượng từ pool với vị trí và rotation chỉ định.
         * @param pos Vị trí sinh đối tượng.
         * @param rot Rotation của đối tượng.
         * @return Đối tượng GameUnit được sinh ra.
         */
        public GameUnit spawn(Vector3 pos, Quaternion rot) {
            GameUnit obj;

            if (inactive.isEmpty()) {
                obj = instantiateNewObject();  // Nếu không còn đối tượng nào trong pool, tạo mới
            } else {
                obj = inactive.poll();  // Lấy đối tượng từ hàng đợi các đối tượng chưa sử dụng
            }

            active.add(obj);  // Đưa đối tượng vào danh sách các đối tượng đang hoạt động

            // Đặt vị trí và rotation cho đối tượng
            obj.getTransform().setPositionAndRotation(pos, rot);
            obj.setActive(true);  // Đánh dấu đối tượng là đang hoạt động

            return obj;
        }

        /**
         * Thu hồi đối tượng về pool.
         * @param obj Đối tượng cần thu hồi.
         */
        public void despawn(GameUnit obj) {
            obj.setActive(false);  // Đánh dấu đối tượng là không hoạt động
            inactive.offer(obj);   // Đưa đối tượng về hàng đợi các đối tượng chưa sử dụng
            active.remove(obj);    // Xóa đối tượng khỏi danh sách các đối tượng đang hoạt động
        }

        /**
         * Thu hồi tất cả các đối tượng đang hoạt động về pool.
         */
        public void collect() {
            while (!active.isEmpty()) {
                despawn(active.get(0));  // Thu hồi từng đối tượng đang hoạt động
            }
        }
    }

    // Lưu trữ tất cả các pool theo loại PoolType
    private static Map<PoolType, Pool> poolInstance = new HashMap<>();

    /**
     * Preload một số lượng đối tượng GameUnit vào pool.
     * @param prefab Đối tượng GameUnit mẫu.
     * @param amount Số lượng đối tượng được preload.
     * @param parent Đối tượng cha của pool.
     */
    public static void preload(GameUnit prefab, int amount, Transform parent) {
        if (!poolInstance.containsKey(prefab.getPoolType())) {
            poolInstance.put(prefab.getPoolType(), new Pool(prefab, amount, parent));
        }
    }

    /**
     * Sinh đối tượng GameUnit từ pool với vị trí và rotation chỉ định.
     * @param poolType Loại PoolType của đối tượng.
     * @param pos Vị trí sinh đối tượng.
     * @param rot Rotation của đối tượng.
     * @return Đối tượng GameUnit được sinh ra.
     */
    public static GameUnit spawn(PoolType poolType, Vector3 pos, Quaternion rot) {
        return poolInstance.get(poolType).spawn(pos, rot);
    }

    /**
     * Thu hồi đối tượng GameUnit về pool.
     * @param gameUnit Đối tượng GameUnit cần thu hồi.
     */
    public static void despawn(GameUnit gameUnit) {
        poolInstance.get(gameUnit.getPoolType()).despawn(gameUnit);
    }

    /**
     * Thu hồi tất cả các đối tượng từ tất cả các pool.
     */
    public static void collectAll() {
        for (Pool pool : poolInstance.values()) {
            pool.collect();
        }
    }
}
