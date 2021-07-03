package com.iridium.iridiumskyblock;

public class Cache<T> {

    private T cache;
    private long lastCache;
    private final long refreshTimeInMilliseconds;

    public Cache(long refreshTimeInMilliseconds) {
        this.refreshTimeInMilliseconds = refreshTimeInMilliseconds;
        this.lastCache = 0;
    }

    public T getCache(CacheGetter<T> cacheGetter) {
        long currentTime = System.currentTimeMillis();
        if (lastCache + refreshTimeInMilliseconds < currentTime || cache == null) {
            this.cache = cacheGetter.getObject();
            this.lastCache = currentTime;
        }
        return cache;
    }

    public void clearCache() {
        this.cache = null;
    }

    public interface CacheGetter<T> {
        T getObject();
    }
}
