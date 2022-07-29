package com.gateway.commonapi.log;

import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;

public final class MdcRetention {

    public static Runnable wrap(final Runnable delegate) {
        return new MdcRetainingRunnable() {
            @Override
            protected void runInContext() {
                delegate.run();
            }
        };
    }

    private static abstract class MdcRetentionSupport {
        protected final Map<String, String> originalMdc;

        protected MdcRetentionSupport() {
            Map<String, String> originalMdc = MDC.getCopyOfContextMap();
            this.originalMdc = originalMdc == null ? Collections.emptyMap() : originalMdc;
        }
    }

    public static abstract class MdcRetainingRunnable extends MdcRetentionSupport implements Runnable {

        @Override
        public final void run() {
            Map<String, String> currentMdc = MDC.getCopyOfContextMap();
            MDC.setContextMap(originalMdc);
            try {
                runInContext();
            } finally {
                MDC.setContextMap(currentMdc);
            }
        }

        abstract protected void runInContext();
    }}