package com.zoradev.websocket.server.uuid;

import java.util.UUID;

public interface UUIDCache {
    UUID uuid(final String p0);
    String name(final UUID p0);
    void ensure(final UUID p0);
    void update(final UUID p0, final String p1);
}
