package com.zoradev.websocket.server.uuid;

import com.google.common.base.Preconditions;

import java.util.UUID;

public class WebsocketUUIDCache {
    private static UUIDCache impl;
    private static boolean initialized;

    private WebsocketUUIDCache() {}

    public static void init() {
        Preconditions.checkState(!WebsocketUUIDCache.initialized);
        WebsocketUUIDCache.initialized = true;

        try {
            WebsocketUUIDCache.impl = (UUIDCache) Class.forName("com.zoradev.websocket.server.uuid.impl.RedisUUIDCache").newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static UUID uuid(final String name) {
        return WebsocketUUIDCache.impl.uuid(name);
    }

    public static String name(final UUID uuid) {
        return WebsocketUUIDCache.impl.name(uuid);
    }

    public static void ensure(final UUID uuid) {
        WebsocketUUIDCache.impl.ensure(uuid);
    }

    public static void update(final UUID uuid, final String name) {
        WebsocketUUIDCache.impl.update(uuid, name);
    }

    static {
        impl = null;
        initialized = false;
    }
}
