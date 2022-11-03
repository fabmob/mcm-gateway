package com.gateway.commonapi.utils;

import com.gateway.commonapi.dto.data.SelectorDTO;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.util.Assert;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

/**
 * class grouping all json processing and operations
 */

@Slf4j
public class JsonUtils {

    private JsonUtils() {
    }

    /**
     * @param selector
     * @param responseBody
     * @return
     */
    public static JsonElement getJsonArray(SelectorDTO selector, String responseBody) throws JSONException {
        return getJsonElement(selector, responseBody);
    }

    /**
     * @param selector
     * @param responseBody
     * @return
     */
    public static JsonElement getJsonElement(SelectorDTO selector, String responseBody) throws JSONException {
        if (selector != null) {
            return findJsonElementBySelector(selector, responseBody);
        }
        log.debug("Selector is null!!");
        return new JsonParser().parse(responseBody);
    }

    /**
     * Find a JsonElement matching the given selector in a JSON object hierarchy.
     *
     * @param selector Selector (musn't be null).
     * @param jsonText JSON object hierarchy in text format.
     * @return JsonElement matching the selector.
     */
    public static JsonElement findJsonElementBySelector(SelectorDTO selector, String jsonText) throws JSONException {
        JsonElement responseElement = null;
        Assert.notNull(selector, "SelectorParam is null !!");
        try {
            JsonReader reader = new JsonReader(new StringReader(jsonText));
            reader.setLenient(true);
            responseElement = new JsonParser().parse(reader);
            return findJsonElementBySelector(responseElement, selector.getKey(), selector.getValue());
        } catch (JsonSyntaxException e) {
            log.error("Cannot parse response from MSP : {}", e.getMessage());
            log.debug(jsonText);
        } catch (JSONException e) {
            log.error(e.getMessage());
        }
        return responseElement;
    }

    /**
     * Find a JsonElement having a given key with a given value in a JSON object hierarchy.
     *
     * @param jsonElement Root element where the search is performed
     * @param key         Key to look for.
     * @param value       Optional value to look for in the key.
     * @return JsonElement matching the given criterion.
     */
    public static JsonElement findJsonElementBySelector(JsonElement jsonElement, String key, String value) throws JSONException {
        JsonElement result = null;
        if (jsonElement.isJsonArray()) {
            result = findInArray(jsonElement.getAsJsonArray(), key, value);
            if (result != null) {
                return result;
            }
        } else if (jsonElement.isJsonObject()) {
            result = findInObject(jsonElement.getAsJsonObject(), key, value);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    /**
     * Find a JsonElement having a given key with a given value in a JSON object hierarchy.
     *
     * @param jsonArray Array where the search is performed
     * @param key       Key to look for.
     * @param value     Optional value to look for in the key.
     * @return JsonElement matching the given criterion.
     */
    private static JsonElement findInArray(JsonArray jsonArray, String key, String value) throws JSONException {
        JsonElement result = null;
        for (JsonElement elm : jsonArray) {
            result = findJsonElementBySelector(elm, key, value);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    /**
     * Find a JsonElement having a given key with a given value in a JSON object hierarchy.
     *
     * @param jsonObject Object where the search is performed
     * @param key        Key to look for.
     * @param value      Optional value to look for in the key.
     * @return JsonElement matching the given criterion.
     */
    private static JsonElement findInObject(JsonObject jsonObject, String key, String value) throws JSONException {
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        JsonElement result = null;
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            if ((entry.getKey().equals(key) && entry.getValue().toString().equals(value))) {
                return jsonObject;
            }
            if (entry.getKey().equals(key) && StringUtils.isBlank(value)) {
                return jsonObject.get(key);
            }
            result = findJsonElementBySelector(entry.getValue(), key, value);
            if (result != null) {
                return result;
            }
        }
        return result;
    }


}
