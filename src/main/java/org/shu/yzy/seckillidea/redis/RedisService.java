package org.shu.yzy.seckillidea.redis;

public interface RedisService {

    /** 获取缓存对象 */
    <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz);

    /** 设置缓存对象 */
    <T> T set(KeyPrefix keyPrefix, String key, T value);

    /** 判断缓存对象是否存在 */
    boolean exist(KeyPrefix keyPrefix, String key);

    /** 加一并返回当前值 */
    Long incr(KeyPrefix keyPrefix, String key);

    /** 减一并返回当前值 */
    Long decr(KeyPrefix keyPrefix, String key);
}
