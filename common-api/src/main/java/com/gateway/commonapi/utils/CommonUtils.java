package com.gateway.commonapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
public class CommonUtils {

    private static final Pattern uuidPattern = Pattern.compile(GlobalConstants.UUID_REGEXP);

    private CommonUtils() {}

    public static boolean isUUID(String uuid) {
        Matcher matcher = uuidPattern.matcher(uuid);
        return matcher.matches();
    }

    /**
     * prepare context and headers for http call
     * @return httpHeaders
     */
    public static HttpHeaders setHttpHeaders(){
        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        return httpHeaders;
    }

    /**
     * Concert GenericError to json as string
     * @param error Error DTO
     * @return String with json inside
     */
    public static String stringifyGenericErrorDto(GenericError error) {
        String response = "";
        ObjectMapper objectMapper =  new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            log.error("Error parsing GenericError", e);
        }
        return response;
    }

    /**
     * Inject into a template using { } to delimiter variables the params passed. Params must be multiple of 2, first of couple is the name of the variable and the second one is the value to inject in the template.
     * @param template String of the template with {vars} with  brackets delimiters
     * @param params Parameters couple of variable to inject with associated value must be multiple of 2
     * @return the String resulting of the template formatting
     */
    public static String placeholderFormat(String template, String... params) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(toMap(params), "{", "}", '~');
        return stringSubstitutor.replace(template);
    }


    /**
     * Convert onto a map couple of params. First is the key and following the value. entries must be multiple of 2 otherwise raise IllegalArgumentException
      * @param entries couple of paramKey and value to transform into map
     * @return the map of key values
     */
    public static Map<String, String> toMap(String... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2).collect(HashMap::new,
                (m, i) -> m.put(entries[i], entries[i + 1]), Map::putAll);
    }

    public static HttpEntity<String> setHeader() {
        String correlationId = new ThreadLocalUserSession().get().getContextId();
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        return  new HttpEntity<>(httpHeaders);
    }

    /**
     * Allows you to apply a function to a value
     *
     * @param value           string representing the value to process
     * @param processFunction string representing the function to apply to the value
     * @return the processed value
     */
    public static String processFunction(String value, String processFunction) {
        String finalValue = value;
        if (processFunction.equals("BASE64")) {
            finalValue = Base64.getEncoder().encodeToString(value.getBytes());
        }
        return finalValue;
    }


    /**
     * Construct url with given params
     *
     * @param urlCall
     * @param params
     * @return
     */
    public static String constructUrlTemplate(String urlCall, Map<String,String> params) {

        String urlTemplate = urlCall;

        if (params != null && isValid(urlTemplate)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlTemplate = UriComponentsBuilder.fromHttpUrl(urlTemplate).queryParam(key, value).encode().toUriString();

            }
        }

        return urlTemplate;
    }


    /**
     * Returns true if url is valid
     * @param url
     * @return
     */
    private static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }


}
