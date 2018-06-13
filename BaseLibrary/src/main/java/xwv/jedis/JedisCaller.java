package xwv.jedis;

import redis.clients.jedis.Jedis;

public interface JedisCaller<T> {
    T call(Jedis jedis);

}
