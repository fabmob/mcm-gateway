package com.gateway.adapter.utils;

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.FIRST_PLACEHOLDER;
import static com.gateway.adapter.utils.constant.AdapterMessageDict.MISSING_EXTERNAL_FIELD;

/**
 * class to process all decoding operations and transformation of the response of msp
 */
public class DecodeUtils {
    /**
     * Default constructor.
     *
     * @throws IllegalStateException Utility class, constructor should not be used.
     */
    private DecodeUtils() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * decode data in the jsonObject and apply the transformation
     *
     * @param dataResponse Data about the vehicle.
     * @param dataMappers  Rules to convert the data.
     * @return Vehicle object filled by the method.
     */
    public static JSONObject decodeDataResponse(JSONObject dataResponse, List<DataMapperDTO> dataMappers) throws JSONException {
        String externalField;
        String internalField;
        String internalFieldRoot = "";
        for (DataMapperDTO mapperDTO : dataMappers) {
            externalField = mapperDTO.getExternalField();
            internalField = mapperDTO.getInternalField();


            //decode according if it is an Array or not
            if ((mapperDTO.getIsArray() != null) && (mapperDTO.getIsArray() != 0)) {
                decodeArrayElements(mapperDTO, dataResponse, internalFieldRoot, internalField, externalField);

            } else {
                //verify that externalField is in MSP response, else pass to next data_mapper
                if (Boolean.TRUE.equals(verifyExternalFieldValid(dataResponse, externalField))) {
                    decodingFields(mapperDTO, externalField, internalField, dataResponse);
                }

            }


        }
        return dataResponse;
    }

    /**
     * Verify if externalField is in MSP response
     *
     * @param dataResponse
     * @param externalField
     * @return
     */
    private static Boolean verifyExternalFieldValid(JSONObject dataResponse, String externalField) {

        boolean valid = false;
        if (StringUtils.isNotBlank(externalField)) {
            if (externalField.contains(".")) {
                String[] externalFieldTab = externalField.split("\\.");
                if (!dataResponse.isNull(externalFieldTab[0])) {
                    valid = true;
                }
            } else {
                if (!dataResponse.isNull(externalField)) {
                    valid = true;
                }
            }
        } else {
            valid = true;
        }

        return valid;


    }


    /**
     * Select array field to loop on in response
     *
     * @param mapperDTO
     * @param internalField
     * @param dataResponse
     * @return
     * @throws JSONException
     */
    private static Object defineElementTOLoopOn(DataMapperDTO mapperDTO, String internalField, JSONObject dataResponse) throws JSONException {
        String internalFieldRoot;
        if (internalField.contains(".")) {
            internalFieldRoot = internalField.substring(0, internalField.indexOf('.'));
        } else {
            internalFieldRoot = internalField;
        }

        Object jsonArray = null;

        if (dataResponse.has(mapperDTO.getExternalField())) {
            jsonArray = dataResponse.get(mapperDTO.getExternalField());
        }
        if (dataResponse.has(internalFieldRoot)) {
            jsonArray = dataResponse.get(internalFieldRoot);
        }
        if (jsonArray instanceof JSONArray) {
            return jsonArray;
        }
        return null;
    }


    /**
     * Decode Array Elements
     *
     * @param mapperDTO
     * @param dataResponse
     * @param internalFieldRoot
     * @param internalField
     * @param externalField
     * @throws JSONException
     */

    private static void decodeArrayElements(DataMapperDTO mapperDTO, JSONObject dataResponse, String internalFieldRoot, String internalField, String externalField) throws JSONException {
        String internalFieldSub = "";
        Object jsonArrayResponse = defineElementTOLoopOn(mapperDTO, internalField, dataResponse);

        //decode array with object elements
        if (jsonArrayResponse instanceof JSONArray && ((JSONArray) jsonArrayResponse).get(0) instanceof JSONObject) {
            if (internalField.contains(".")) {
                internalFieldSub = internalField.substring(internalField.indexOf('.') + 1, internalField.length());
                //decode subObject Arrays
                internalFieldRoot = decodingSubObjectsIntern(externalField, internalField, dataResponse);
            }

            //verify that externalField is in MSP response, else pass to next data_mapper
            if (Boolean.TRUE.equals(verifyExternalFieldValid(dataResponse, internalFieldRoot))) {
                decodeDataResponseArray(mapperDTO, dataResponse, internalFieldRoot, internalFieldSub, mapperDTO.getContainedValue());
            }


        }

        //decode array of String to boolean
        else {

            //verify that externalField is in MSP response, else pass to next data_mapper
            if (Boolean.TRUE.equals(verifyExternalFieldValid(dataResponse, externalField))) {
                decodeStringArraysToBoolean(mapperDTO, dataResponse, internalField, externalField, null);
            }

        }
    }

    /**
     * Decode array with object elements
     *
     * @param mapperDTO
     * @param dataResponse
     * @param internalFieldRoot
     * @param internalFieldSub
     * @return
     * @throws JSONException
     */
    private static JSONObject decodeDataResponseArray(DataMapperDTO mapperDTO, JSONObject dataResponse, String internalFieldRoot, String internalFieldSub, String containedValue) throws JSONException {

        JSONArray responseArray = dataResponse.getJSONArray(internalFieldRoot);
        if (dataResponse.get(internalFieldRoot) != null && (containedValue != null || mapperDTO.getDefaultValue() != null)) {

            //loop on the array's elements
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject dataObjet = dataResponse.getJSONArray(internalFieldRoot).getJSONObject(i);

                //if ContainedValue != null we select the corresponding field
                if (StringUtils.isNotBlank(containedValue)) {
                    for (int j = 0; j < dataObjet.names().length(); j++) {
                        String containedValueRoot;
                        if (containedValue.contains(".")) {
                            containedValueRoot = containedValue.substring(0, containedValue.indexOf('.'));
                        } else {
                            containedValueRoot = containedValue;
                        }

                        if (containedValueRoot.equals(dataObjet.names().getString(j))) {
                            // select the value of field containedValue and assign it to new internalField
                            decodingFields(mapperDTO, containedValue, internalFieldSub, dataObjet);
                        }
                    }

                    //if ContainedValue == null we decode fields considering extern field null
                } else {
                    // select the defaultValue and assign it to new internalField
                    decodingFields(mapperDTO, null, internalFieldSub, dataObjet);
                }

            }
        }
        return dataResponse;
    }


    /**
     * Decode array of String to boolean
     *
     * @param mapperDTO
     * @param dataResponse
     * @param internalField
     * @param externalField
     * @throws JSONException
     */
    private static void decodeStringArraysToBoolean(DataMapperDTO mapperDTO, JSONObject dataResponse, String internalField, String externalField, String deductedContainedValue) throws JSONException {
        //get the value to assign
        String value = String.valueOf(false);
        JSONArray array = null;
        String containedValue = deductedContainedValue != null ? deductedContainedValue : mapperDTO.getContainedValue();

        // get the array of string in response
        if (externalField.contains(".")) {
            String[] externalFieldTab = externalField.split("\\.");
            JSONObject object = (JSONObject) dataResponse.get(externalFieldTab[0]);
            for (int i = 1; i < externalFieldTab.length; i++) {
                if (i == externalFieldTab.length - 1) {
                    array = object.getJSONArray(externalFieldTab[i]);
                } else {
                    object = (JSONObject) object.get(externalFieldTab[i]);
                }

            }

        } else {
            array = dataResponse.getJSONArray(externalField);
        }

        // decode boolean value from array's data
        if (array != null && containedValue != null) {
            for (int i = 0; i < array.length(); i++) {
                String elm = array.getString(i);
                if (containedValue.equals(elm)) {
                    value = String.valueOf(true);
                }
            }

        }

        //creation of the corresponding boolean field "internalField"
        assignInternalField(internalField, dataResponse, value);

    }


    /**
     * Decoding SubObjects Intern
     *
     * @param externalField
     * @param internalField
     * @param dataResponse
     * @return
     * @throws JSONException
     */

    private static String decodingSubObjectsIntern(String externalField, String internalField, JSONObject dataResponse) throws JSONException {
        String internalFieldRoot = "";
        if (internalField.contains(".")) {
            internalFieldRoot = internalField.substring(0, internalField.indexOf('.'));
            dataResponse.putOpt(internalFieldRoot, dataResponse.remove(externalField));
        }
        return internalFieldRoot;
    }


    /**
     * Looks for the value of the field in MSP response (externalField) and assign it to corresponding internalField
     *
     * @param mapperDTO
     * @param externalField
     * @param internalField
     * @param dataResponse
     * @throws JSONException
     */
    private static void decodingFields(DataMapperDTO mapperDTO, String externalField, String internalField, JSONObject dataResponse) throws JSONException {
        boolean isNestedArray = false;

        //get the value to assign
        Object value = null;
        if (StringUtils.isBlank(externalField) && StringUtils.isNotBlank(mapperDTO.getDefaultValue())) {

            value = mapperDTO.getDefaultValue();

        } else if (StringUtils.isNotBlank(externalField)) {
            if (externalField.contains(".")) {
                String[] externalFieldTab = externalField.split("\\.");
                JSONObject object = (JSONObject) dataResponse.get(externalFieldTab[0]);
                for (int i = 1; i < externalFieldTab.length; i++) {
                    if (i == externalFieldTab.length - 1) {
                        value = object.get(externalFieldTab[i]);
                    } else {
                        //case object = array of String
                        if (i == externalFieldTab.length - 2 && object.get(externalFieldTab[i]) instanceof JSONArray) {
                            if (((JSONArray) object.get(externalFieldTab[i])).get(0) instanceof String) {
                                String subExternalField = externalField.replace("." + externalFieldTab[i + 1], "");
                                decodeStringArraysToBoolean(mapperDTO, dataResponse, internalField, subExternalField, externalFieldTab[i + 1]);
                                break;

                                // case of nested Array of Objects
                            } else if (((JSONArray) object.get(externalFieldTab[i])).get(0) instanceof JSONObject) {
                                isNestedArray = true;

                                // apply function decodeDataResponseArray on nested array
                                String internalFieldRoot = decodingSubObjectsIntern(externalFieldTab[i], internalField, object);
                                String internalFieldSub = internalField.replace(internalFieldRoot + ".", "");
                                StringBuilder containedValue = new StringBuilder();
                                for (int j = i + 1; j < externalFieldTab.length; j++) {
                                    containedValue.append(".").append(externalFieldTab[j]);
                                }
                                containedValue = new StringBuilder(containedValue.substring(1));

                                decodeDataResponseArray(mapperDTO, object, internalFieldRoot, internalFieldSub, containedValue.toString());

                                //assign the array with transformed values to internalField
                                String[] internalFieldTab = internalField.split("\\.");
                                Object nestedArray = object.get(externalFieldTab[i]);

                                assignInternalField(internalField.replace("." + internalFieldTab[internalFieldTab.length - 1], ""), dataResponse, nestedArray);
                                break;
                            }


                        }

                        object = (JSONObject) object.get(externalFieldTab[i]);
                    }

                }

            } else {
                value = dataResponse.get(externalField);
            }
        } else {

            throw new InternalException(CommonUtils.placeholderFormat(MISSING_EXTERNAL_FIELD, FIRST_PLACEHOLDER, mapperDTO.getDataMapperId().toString()));
        }


        if (value != null && !isNestedArray) {

            if (StringUtils.isNotBlank(mapperDTO.getFormat())) {
                String format = mapperDTO.getFormat();
                String timezone = mapperDTO.getTimezone();

                Map<String, Object> formattedData = FormatUtils.formatValue(value, format, internalField, timezone, mapperDTO);
                value = formattedData.get("value");
                internalField = (String) formattedData.get("internalField");
            }

            //creation of the corresponding field "internalField"
            if (value != null && StringUtils.isNotBlank(internalField)) {
                assignInternalField(internalField, dataResponse, value);
            }

        }


    }


    /**
     * Create the corresponding internalField and assign the found value to it
     *
     * @param internalField
     * @param dataResponse
     * @param value
     * @throws JSONException
     */
    private static void assignInternalField(String internalField, JSONObject dataResponse, Object value) throws JSONException {

        if (internalField.contains(".")) {
            String[] internalFieldTab = internalField.split("\\.");
            JSONObject object = new JSONObject();
            // assign the value of externalField to the corresponding internal field
            object.put(internalFieldTab[internalFieldTab.length - 1], value);

            //case where root of internalField not created yet
            if (dataResponse.isNull(internalFieldTab[0])) {

                // creation of the full object with subObjects indicated in internalField to create
                for (int i = 2; i <= internalFieldTab.length; i++) {
                    JSONObject parent = new JSONObject();
                    parent.put(internalFieldTab[internalFieldTab.length - i], object);
                    object = parent;
                }
                dataResponse.put(internalFieldTab[0], object.get(internalFieldTab[0]));

                // internalField root has already been created through a previous iteration on data_mappers
            } else {
                JSONObject responseObject = dataResponse.getJSONObject(internalFieldTab[0]);
                if (internalFieldTab.length > 2) {

                    //loop on different levels of existing internalField  to reach the level where the new object needs to be added
                    for (int i = 1; i < internalFieldTab.length - 1; i++) {
                        if (!responseObject.isNull(internalFieldTab[i])) {
                            responseObject = responseObject.getJSONObject(internalFieldTab[i]);
                            if (i == internalFieldTab.length - 2) {
                                responseObject.put(internalFieldTab[i + 1], object.get(internalFieldTab[i + 1]));
                            }
                        } else {
                            if (i == internalFieldTab.length - 2) {
                                responseObject.put(internalFieldTab[i], object);
                            } else {
                                for (int j = i; j <= internalFieldTab.length - i; j++) {
                                    JSONObject parent = new JSONObject();
                                    parent.put(internalFieldTab[internalFieldTab.length - j], object);
                                    object = parent;
                                }

                                responseObject.put(internalFieldTab[i], object.get(internalFieldTab[i]));
                            }

                        }

                    }
                } else {
                    responseObject.put(internalFieldTab[1], object.get(internalFieldTab[1]));
                }


            }

        } else {
            dataResponse.putOpt(internalField, value);
        }

    }
}