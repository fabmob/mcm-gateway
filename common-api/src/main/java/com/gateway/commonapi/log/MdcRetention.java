package com.gateway.commonapi.log;

import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;

public final class MdcRetention {

    private MdcRetention() {
    }

    public static Runnable wrap(final Runnable delegate) {
        return new MdcRetainingRunnable() {
            @Override
            protected void runInContext() {
                delegate.run();
            }
        };
    }

    private abstract static class MdcRetentionSupport {
        protected final Map<String, String> originalMdc;

        protected MdcRetentionSupport() {
            Map<String, String> origineMdc = MDC.getCopyOfContextMap();
            this.originalMdc = origineMdc == null ? Collections.emptyMap() : origineMdc;
        }
    }

    public abstract static class MdcRetainingRunnable extends MdcRetentionSupport implements Runnable {

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

        protected abstract void runInContext();
    }
}