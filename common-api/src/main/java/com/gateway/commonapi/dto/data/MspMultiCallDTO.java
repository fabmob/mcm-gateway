package com.gateway.commonapi.dto.data;

public class MspMultiCallDTO extends MspCallsDTO{
    /**
     * Copy constructor.
     * <p>
     * NOTE: Needed for recursive calls (as URL is modified when expanded).
     * It would be better to avoid this need.
     *
     * @param call Source object.
     */
    public MspMultiCallDTO (MspCallsDTO call) {
        super();

    }
}
