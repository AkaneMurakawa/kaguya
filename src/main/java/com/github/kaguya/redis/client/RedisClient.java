package com.github.kaguya.redis.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * RedisClient
 */
@Log4j2
@Repository
public class RedisClient {

    private static final String REDIS_ASTERISK = "*";

    private static final int REDIS_SCAN_COUNT = 20000;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                DataType type = redisTemplate.type(key);
                switch (type) {
                    case STRING: {
                        log.info("删除String类型key:{},删除前key的值为:{}", key, get(key));
                        redisTemplate.delete(key);
                        break;
                    }
                    case LIST: {
                        long len = lListSize(key);
                        if (len > 500) {
                            log.warn("该key对应的队列包含数据太多,不能删除");
                            break;
                        }
                        log.info("删除list类型key:{},删除前key的值为:{}", key, lGet(key, 0, len));
                        redisTemplate.delete(key);
                        break;
                    }
                    case HASH: {
                        log.info("删除map类型key:{},删除前key的值为:{}", key, hGet(key));
                        redisTemplate.delete(key);
                        break;
                    }
                    case SET: {
                        log.info("删除set类型key:{},删除前key的值为:{}", key, sGet(key));
                        redisTemplate.delete(key);
                        break;
                    }
                    case ZSET: {
                        log.info("删除zset类型key:{},删除前key的值为:{}", key, sGet(key));
                        redisTemplate.delete(key);
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hSet(String key, Map<Object, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hSet(String key, Map<Object, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * HashSet 清除缓存项
     *
     * @param key     键
     * @param itemKey 项
     * @return true成功 false失败
     */
    public boolean hDel(final String key, final String itemKey) {
        try {
            if (StringUtils.isBlank(itemKey)) {
                return true;
            }
            final String delOrderIvSign = "$const@oiv@del$";
            final String oivKey = "ful:order:inventory";
            if (!itemKey.startsWith(delOrderIvSign) || !key.equals(oivKey)) {
                Object[] args = itemKey.split(",");
                redisTemplate.opsForHash().delete(key, args);
                return true;
            }
            boolean isDelOivAll = itemKey.equals(delOrderIvSign);
            String perfix = itemKey.startsWith(delOrderIvSign) ? itemKey.replace(delOrderIvSign, "") : "";
            redisTemplate.opsForHash().entries(key).forEach((k, v) -> {
                if (isDelOivAll || k.toString().startsWith(perfix)) {
                    redisTemplate.opsForHash().delete(key, k);
                }
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hSetIfAbsent(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().putIfAbsent(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key   键 不能为null
     * @param items 项 可以使多个 不能为null
     */
    public void hDel(String key, String... items) {
        for (String item : items) {
            log.warn("删除hash值, key:{},item:{},value:{}", key, item, redisTemplate.opsForHash().get(key, item));
            redisTemplate.opsForHash().delete(key, item);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     */
    public long sSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取set
     * @param key
     * @return
     */
    public Set<Object> zGet(String key) {
        try {
            return redisTemplate.opsForZSet().range(key, 0, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取set
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set zRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 获取zset的权重
     * @param key
     * @return
     */
    public Map<Object, Double> zGetWithScore(String key) {
        try {
            Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
            Map<Object, Double> map = new HashMap<>();
            for (ZSetOperations.TypedTuple<Object> set : typedTuples) {
                map.put(set.getValue(), set.getScore());
            }
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 是否包含key
     * @param key
     * @param value
     * @return
     */
    public boolean zHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForZSet().rank(key, value) > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * zset
     * @param key
     * @param values
     * @param score
     * @return
     */
    public boolean zSet(String key, Object values, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, values, score);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * zSetSize
     * @param key
     * @return
     */
    public long zSetSize(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * zSetRemove
     * @param key
     * @param values
     * @return
     */
    public long zSetRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public long lListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            log.warn("更新队列前数据: key:{},index:{},value:{}", key, index, redisTemplate.opsForList().index(key, index));
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 删除key
     *
     * @param keys
     * @return
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 如果key不存在缓存放入并设置时间，否则直接返回false
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setIfAbsent(String key, Object value, long time) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection ->
                BooleanUtils.isTrue(redisConnection.set(
                        key.getBytes(),
                        value.toString().getBytes(),
                        Expiration.seconds(time),
                        RedisStringCommands.SetOption.SET_IF_ABSENT)
                ));
    }

    /**
     * 查询key 可模糊匹配
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        //禁止keys *
        if (REDIS_ASTERISK.equals(pattern)) {
            return Sets.newHashSet();
        }
        List<String> keys = exeKeys(pattern);
        return Sets.newHashSet(keys);
    }

    /**
     * 执行keys
     * @param pattern
     * @return
     */
    private List<String> exeKeys(String pattern) {
        Set<byte[]> result = redisTemplate.execute(connection -> {
            Object nativeConnection = connection.getNativeConnection();
            // 集群
            if (nativeConnection instanceof JedisCluster) {
                return connection.keys(pattern.getBytes(StandardCharsets.UTF_8));
            }
            // 单机模式
            if (nativeConnection instanceof JedisSentinelPool || nativeConnection instanceof Jedis) {
                Set<byte[]> keySet = Sets.newHashSet();
                Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(REDIS_SCAN_COUNT).match(pattern).build());
                while (cursor.hasNext()) {
                    keySet.add(cursor.next());
                }
                return keySet;
            }
            return Sets.newHashSet();
        }, true);
        List<String> list = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(result)) {
            Set<String> keysSet = SerializationUtils.deserialize(result, new StringRedisSerializer());
            list = Lists.newArrayList(keysSet);
        }
        return list;
    }

    /**
     * 执行lua脚本
     * @param script
     * @param keys
     * @param args
     * @return
     */
    public long evalLua(String script, List<String> keys, List<String> args) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            // 集群模式
            if (nativeConnection instanceof JedisCluster) {
                return (Long) ((JedisCluster) nativeConnection).eval(script, keys, args);
            }
            // 单机模式
            else if (nativeConnection instanceof RedisProperties.Jedis) {
                return (Long) ((Jedis) nativeConnection).eval(script, keys, args);
            }
            // Lettuce
            else if (nativeConnection instanceof RedisAsyncCommands) {
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script);
                redisScript.setResultType(Long.class);
                return redisTemplate.execute(redisScript, new StringRedisSerializer(), new Jackson2JsonRedisSerializer<>(Long.class), keys, args.toArray());
            }
            return 0L;
        });
    }

}