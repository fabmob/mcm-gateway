package com.gateway.commonapi.utils;

import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static com.gateway.commonapi.constants.GatewayApiPathDict.CARPOOLING_PATH;
import static com.gateway.commonapi.constants.GatewayApiPathDict.GET_PARTNERS_PATH;

@Slf4j
public class CallUtils {

    private CallUtils() {
    }

    /**
     * save in the call thread the standard to respond to the call
     *
     * @param standard a string representation of a StandardEnum
     */
    public static void saveOutputStandardInCallThread(String standard) {
        saveOutputStandardInCallThread(StandardEnum.fromValue(standard));
    }

    /**
     * save in the call thread the standard to respond to the call
     *
     * @param standard a StandardEnum
     */
    public static void saveOutputStandardInCallThread(StandardEnum standard) {
        new ThreadLocalUserSession().get().setOutputStandard(standard);
    }

    /**
     * save in the call thread the standard to respond to the call according to passed request / endpoint
     *
     * @param request
     */
    public static void saveOutputStandardInCallThread(WebRequest request) {
        if (request.toString().contains(CARPOOLING_PATH)) {
            saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
        } else if (request.toString().contains(GET_PARTNERS_PATH)) {
            saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        } else {
            saveOutputStandardInCallThread(StandardEnum.GATEWAY);
        }

    }

    /**
     * save in the call thread the list of HTTP responses codes that can be forwarded as received, from the partner to the caller
     *
     * @param validCodes a string representation of a list of HTTP status integer codes
     */
    public static void saveValidCodesInCallThread(String validCodes) {
        new ThreadLocalUserSession().get().setValidCodes(validCodes);
    }

    /**
     * save in the call thread the list of HTTP responses codes that can be forwarded as received, from the partner to the caller
     *
     * @param validCodes a list of HTTP status integer codes
     */
    public static void saveValidCodesInCallThread(List<Integer> validCodes) {
        new ThreadLocalUserSession().get().setValidCodes(String.valueOf(new ArrayList<>(validCodes)));
    }

    /**
     * @return a string representation of the standard to respond to the call
     */
    public static String getOutputStandardFromCallThread() {
        StandardEnum outputStandard = new ThreadLocalUserSession().get().getOutPutStandard();
        return (outputStandard != null) ? outputStandard.toString() : null;
    }

    /**
     * @return the list of HTTP responses codes that can be forwarded as received, from the partner to the caller
     */
    public static String getValidCodesFromCallThread() {
        return new ThreadLocalUserSession().get().getValidCodes();
    }
}
