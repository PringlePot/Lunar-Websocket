package me.moose.websocket.server.uuid.redis;

import me.moose.websocket.server.WebServer;
import redis.clients.jedis.Jedis;

public class RedisUtil {
    public static  <T> T runRedisCommand(final RedisCommand<T> redisCommand) {
        Jedis jedis = WebServer.getInstance().getJedisPool().getResource();
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (jedis != null) {
                WebServer.getInstance().getJedisPool().returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                WebServer.getInstance().getJedisPool().returnResource(jedis);
            }
        }
        return result;
    }
}
