package com.gateway.commonapi.dto.data;

public class PartnerMultiCallDTO extends PartnerCallsDTO{
    /**
     * Copy constructor.
     * <p>
     * NOTE: Needed for recursive calls (as URL is modified when expanded).
     * It would be better to avoid this need.
     *
     * @param call Source object.
     */
    public PartnerMultiCallDTO(PartnerCallsDTO call) {
        super();

    }
}
