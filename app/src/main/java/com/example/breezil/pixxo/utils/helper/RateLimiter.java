package com.example.breezil.pixxo.utils.helper;

import android.os.SystemClock;
import android.util.ArrayMap;

import java.util.concurrent.TimeUnit;

public final class RateLimiter {
    long timeout;
    private ArrayMap timestamps;


    public RateLimiter(int timeout, TimeUnit timeUnit) {
        this.timestamps = new ArrayMap();
        this.timeout = timeUnit.toMillis((long)timeout);
    }

    public synchronized boolean shouldFetch(Object key){
        Long lastFetched = (Long) this.timestamps.get(key);
        long now = this.now();

        if(lastFetched == null){
            timestamps.put(key,now);
            return true;
        }else if( now - lastFetched > timeout){
            timestamps.put(key, now);
        }
        else{
            return false;
        }
        return true;
    }

    private final long now() {
        return SystemClock.uptimeMillis();
    }

    public final synchronized void reset(Object key) {
        this.timestamps.remove(key);
    }




}
