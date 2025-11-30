package com.example.securityapp.security;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private final Map<String, List<Long>> attempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_MS = 60_000; // 1 minute

    public boolean isAllowed(String key) {
        long now = System.currentTimeMillis();
        attempts.putIfAbsent(key, new ArrayList<>());
        List<Long> times = attempts.get(key);

        times.removeIf(t -> t < now - WINDOW_MS);
        if (times.size() >= MAX_ATTEMPTS) {
            return false;
        }

        times.add(now);
        return true;
    }
}
