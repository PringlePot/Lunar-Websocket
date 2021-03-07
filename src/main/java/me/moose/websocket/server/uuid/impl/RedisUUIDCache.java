package me.moose.websocket.server.uuid.impl;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.uuid.UUIDCache;
import me.moose.websocket.server.uuid.redis.RedisUtil;
import me.moose.websocket.utils.Config;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RedisUUIDCache implements UUIDCache {
    private static final Map<UUID, String> uuidToName = new ConcurrentHashMap<>();
    private static final Map<String, UUID> nameToUUID = new ConcurrentHashMap<>();

    public RedisUUIDCache() throws URISyntaxException {
        WebServer.getInstance().jedisPool = new JedisPool(new JedisPoolConfig(),
                Config.Redis.HOST,
                Config.Redis.PORT,
                Protocol.DEFAULT_TIMEOUT, null, Config.Redis.DBID);
        RedisUtil.runRedisCommand((redis) -> {
            final Map<String, String> cache = redis.hgetAll(Config.Redis.UUIDCACHE);
            for (Map.Entry<String, String> cacheEntry : cache.entrySet()) {
                final UUID uuid = UUID.fromString(cacheEntry.getKey());
                final String name = cacheEntry.getValue();
                uuidToName.put(uuid, name);
                nameToUUID.put(name.toLowerCase(), uuid);
            }
            return null;
        });
    }

    @Override
    public UUID uuid(String p0) {
        return nameToUUID.get(p0.toLowerCase());
    }

    @Override
    public String name(UUID p0) {
        return uuidToName.get(p0);
    }

    @Override
    public void ensure(UUID p0) {
    }

    @Override
    public void update(UUID p0, String p1) {
        uuidToName.put(p0, p1);
        for (Map.Entry<String, UUID> entry : new HashMap<>(nameToUUID).entrySet()) {
            if (entry.getValue().equals(p0)) {
                nameToUUID.remove(entry.getKey());
            }
        }

        nameToUUID.put(p1.toLowerCase(), p0);
        RedisUtil.runRedisCommand((redis) -> {
            redis.hset("WebsocketUUIDCache", p0.toString(), p1);
            return null;
        });
    }
}
