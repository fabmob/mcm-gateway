package com.gateway.commonapi.monitoring;

import com.gateway.commonapi.utils.enums.StandardEnum;

public class UserContext {
    private String contextId;
    private StandardEnum outputStandard;
    private String validCodes;

    public UserContext(String contextId) {
        this.contextId = contextId;
    }
    public UserContext(String contextId, StandardEnum outputStandard, String validCodes) {
        this.contextId = contextId;
        this.outputStandard = outputStandard;
        this.validCodes = validCodes;
    }

    public String getContextId() {
        return contextId;
    }
    public StandardEnum getOutPutStandard() {
        return outputStandard;
    }

    public String getValidCodes() {
        return validCodes;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public void setOutputStandard(StandardEnum outputStandard) {
        this.outputStandard = outputStandard;
    }

    public void setOutputStandard(String outputStandard) {
        this.outputStandard = StandardEnum.fromValue(outputStandard);
    }

    public void setValidCodes(String validCodes) {
        this.validCodes = validCodes;
    }

    @Override
    public String toString() {
        return String.format("UserContext [contextId=%s, Standard=%s, ValidCodes=%s]", contextId, this.outputStandard, this.validCodes);
    }
}