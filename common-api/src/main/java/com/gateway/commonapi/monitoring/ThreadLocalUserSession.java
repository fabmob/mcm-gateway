package com.gateway.commonapi.monitoring;

import com.gateway.commonapi.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class ThreadLocalUserSession implements Runnable {

    private static final ThreadLocal<UserContext> local = new ThreadLocal<>();

    public UserContext get() {
        UserContext session = local.get();
        if (session == null) {
            UserContext userContext = new UserContext(UUID.randomUUID().toString());
            local.set(userContext);
            log.info("User is not authenticated");
        }
        return local.get();
    }

    public void set(UserContext session) {
        local.set(session);
    }

    public void unload() {
        local.remove();
    }

    @Override
    public void run() {
        saveContext();
        getAndRemoveContext();
    }
    public void saveContext() {
        UserContext userContext = new UserContext(Thread.currentThread().getName());
        local.set(userContext); // each thread saves a context to the shared ThreadLocal
        log.debug("save to thread name: {}" , Thread.currentThread().getName());
    }

    public void getAndRemoveContext() {
        UserContext userContext = local.get(); // each thread gets its own copy
        log.debug("{} context string: {}",Thread.currentThread().getName(), userContext);
        local.remove();
    }
}