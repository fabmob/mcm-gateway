package com.gateway.commonapi.monitoring;

public class UserContext {
    private String contextId;

    public UserContext(String contextId) {
        this.contextId = contextId;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @Override
    public String toString() {
        return "UserContext [contextId=" + contextId + "]";
    }
}