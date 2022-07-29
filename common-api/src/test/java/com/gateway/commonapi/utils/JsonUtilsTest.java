package com.gateway.commonapi.utils;

import com.gateway.commonapi.dto.data.SelectorDTO;
import com.gateway.commonapi.tests.WsTestUtil;
import com.google.gson.JsonElement;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    public static final String MOCK_JSON = "./src/test/resources/mock/Json_mock.json";

    @InjectMocks
    JsonUtils jsonUtils;

    private String createMockResponse() throws IOException {
        return WsTestUtil.readJsonFromFilePath(MOCK_JSON);
    }

    @Test
    void testgetJsonArray() throws IOException, JSONException {
        SelectorDTO selector = new SelectorDTO();
        String responseBody = this.createMockResponse();
        JsonElement jsonElement = JsonUtils.getJsonArray(selector, responseBody);
        assertEquals(jsonElement, null);

    }

}