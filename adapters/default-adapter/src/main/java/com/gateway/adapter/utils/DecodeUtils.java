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

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;

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
        String champExterne = "";
        String champInterne = "";
        String champInterneRoot = "";
        for (DataMapperDTO mapperDTO : dataMappers) {
            champExterne = mapperDTO.getChampExterne();
            champInterne = mapperDTO.getChampInterne();


            //decode according if it is an Array or not
            if ((mapperDTO.getIsArray() != null) && (mapperDTO.getIsArray() != 0)) {
                decodeArrayElements(mapperDTO, dataResponse, champInterneRoot, champInterne, champExterne);

            } else {
                //verify that champExterne is in MSP response, else pass to next data_mapper
                if(Boolean.TRUE.equals(verifyChampExterneValid(dataResponse,champExterne))){
                    decodingFields(mapperDTO, champExterne, champInterne, dataResponse);
                }

            }


        }
        return dataResponse;
    }

    /**
     * Verify if champExterne is in MSP response
     * @param dataResponse
     * @param champExterne
     * @return
     */
    private static Boolean verifyChampExterneValid (JSONObject dataResponse, String champExterne)  {

        boolean valid = false;
        if (StringUtils.isNotBlank(champExterne)) {
            if (champExterne.contains(".")) {
                String[] champExternetab = champExterne.split("\\.");
                if (!dataResponse.isNull(champExternetab[0])) {
                    valid = true;
                }
            } else {
                if (!dataResponse.isNull(champExterne)) {
                    valid =  true;
                }
            }
        } else {
            valid =  true;
        }

        return valid;


    }



        /** Select array field to loop on in response
         * @param mapperDTO
         * @param champInterne
         * @param dataResponse
         * @return
         * @throws JSONException
         */
    private static Object defineElementTOLoopOn(DataMapperDTO mapperDTO, String champInterne, JSONObject dataResponse) throws JSONException {
        String champInterneRoot = null;
        if(champInterne.contains(".")){
            champInterneRoot = champInterne.substring(0, champInterne.indexOf('.'));
        } else {
            champInterneRoot = champInterne;
        }

        Object jsonArray = null;

        if (dataResponse.has(mapperDTO.getChampExterne())) {
            jsonArray = dataResponse.get(mapperDTO.getChampExterne());
        }
        if (dataResponse.has(champInterneRoot)) {
            jsonArray = dataResponse.get(champInterneRoot);
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
     * @param champInterneRoot
     * @param champInterne
     * @param champExterne
     * @throws JSONException
     */

    private static void decodeArrayElements(DataMapperDTO mapperDTO, JSONObject dataResponse, String champInterneRoot, String champInterne, String champExterne) throws JSONException {
        String champInterneSub = "";
        Object jsonArrayResonse = defineElementTOLoopOn(mapperDTO, champInterne, dataResponse);

        //decode array with object elements
        if (jsonArrayResonse instanceof JSONArray && ((JSONArray) jsonArrayResonse).get(0) instanceof JSONObject) {
            if (champInterne.contains(".")) {
                champInterneSub = champInterne.substring(champInterne.indexOf('.') + 1, champInterne.length());
                //decode subObject Arrays
                champInterneRoot = decodingSubObjectsInterne(champExterne, champInterne, dataResponse);
            }

            //verify that champExterne is in MSP response, else pass to next data_mapper
            if(Boolean.TRUE.equals(verifyChampExterneValid(dataResponse,champInterneRoot))){
                decodeDataResponseArray(mapperDTO, dataResponse, champInterneRoot, champInterneSub, mapperDTO.getContainedValue());
            }


        }

        //decode array of String to boolean
        else {

            //verify that champExterne is in MSP response, else pass to next data_mapper
            if(Boolean.TRUE.equals(verifyChampExterneValid(dataResponse,champExterne))){
                decodeStringArraysToBoolean(mapperDTO, dataResponse, champInterne, champExterne, null);
            }

        }
    }

    /**
     * Decode array with object elements
     *
     * @param mapperDTO
     * @param dataResponse
     * @param champInterneRoot
     * @param champInterneSub
     * @return
     * @throws JSONException
     */
    private static JSONObject decodeDataResponseArray(DataMapperDTO mapperDTO, JSONObject dataResponse, String champInterneRoot, String champInterneSub, String containedValue) throws JSONException {

        JSONArray responseArray = dataResponse.getJSONArray(champInterneRoot);
        if (dataResponse.get(champInterneRoot) != null && (containedValue != null || mapperDTO.getDefaultValue() != null)) {

            //loop on the array's elements
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject dataObjet = dataResponse.getJSONArray(champInterneRoot).getJSONObject(i);

                //if ContainedValue != null we select the corresponding field
                if(StringUtils.isNotBlank(containedValue)) {
                    for (int j = 0; j < dataObjet.names().length(); j++) {
                        String containedValueRoot = "";
                        if(containedValue.contains(".")){
                            containedValueRoot = containedValue.substring(0, containedValue.indexOf('.'));
                        } else {
                            containedValueRoot = containedValue;
                        }

                        if (containedValueRoot.equals(dataObjet.names().getString(j))) {
                            // select the value of field containedValue and assign it to new champInterne
                            decodingFields(mapperDTO, containedValue, champInterneSub, dataObjet);
                        }
                    }

                //if ContainedValue == null we decode fields considering extern field null
                } else {
                    // select the defaultValue and assign it to new champInterne
                    decodingFields(mapperDTO, null, champInterneSub, dataObjet);
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
     * @param champInterne
     * @param champExterne
     * @throws JSONException
     */
    private static void decodeStringArraysToBoolean(DataMapperDTO mapperDTO, JSONObject dataResponse, String champInterne, String champExterne, String DeductedContainedValue) throws JSONException {
        //get the value to assign
        String value = String.valueOf(false);
        JSONArray array = null;
        String containedValue = DeductedContainedValue != null ? DeductedContainedValue : mapperDTO.getContainedValue();

        // get the array of string in response
        if (champExterne.contains(".")) {
            String[] champExternetab = champExterne.split("\\.");
            JSONObject object = (JSONObject) dataResponse.get(champExternetab[0]);
            for (int i = 1; i < champExternetab.length; i++) {
                if (i == champExternetab.length - 1) {
                    array = object.getJSONArray(champExternetab[i]);
                } else {
                    object = (JSONObject) object.get(champExternetab[i]);
                }

            }

        } else {
            array = dataResponse.getJSONArray(champExterne);
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

        //creation of the corresponding boolean field "champInterne"
        assignChampInterne(mapperDTO, champInterne, dataResponse, value);

    }





    /**
     * Decoding SubObjects Interne
     *
     * @param champExterne
     * @param champInterne
     * @param dataResponse
     * @return
     * @throws JSONException
     */

    private static String decodingSubObjectsInterne(String champExterne, String champInterne, JSONObject dataResponse) throws JSONException {
        String champInterneRoot = "";
        if (champInterne.contains(".")) {
            champInterneRoot = champInterne.substring(0, champInterne.indexOf('.'));
            dataResponse.putOpt(champInterneRoot, dataResponse.remove(champExterne));
        }
        return champInterneRoot;
    }



    /**
     * Looks for the value of the field in MSP response (champExterne) and assign it to corresponding champInterne
     * @param mapperDTO
     * @param champExterne
     * @param champInterne
     * @param dataResponse
     * @throws JSONException
     */
    private static void decodingFields(DataMapperDTO mapperDTO, String champExterne, String champInterne, JSONObject dataResponse) throws JSONException {
        boolean isNestedArray = false;

        //get the value to assign
        Object value = null;
        if (StringUtils.isBlank(champExterne) && StringUtils.isNotBlank(mapperDTO.getDefaultValue())) {

            value = mapperDTO.getDefaultValue();

        } else if (StringUtils.isNotBlank(champExterne)) {
            if (champExterne.contains(".")) {
                String[] champExternetab = champExterne.split("\\.");
                JSONObject object = (JSONObject) dataResponse.get(champExternetab[0]);
                for (int i = 1; i < champExternetab.length; i++) {
                    if (i == champExternetab.length - 1) {
                        value = object.get(champExternetab[i]);
                    } else {
                        //case object = array of String
                        if(i == champExternetab.length - 2 && object.get(champExternetab[i]) instanceof JSONArray) {
                            if(((JSONArray) object.get(champExternetab[i])).get(0) instanceof String){
                                String subChampExterne = champExterne.replace("."+champExternetab[i+1], "");
                                decodeStringArraysToBoolean(mapperDTO, dataResponse, champInterne, subChampExterne, champExternetab[i+1]);
                                break;

                            // case of nested Array of Objects
                            } else if (((JSONArray) object.get(champExternetab[i])).get(0) instanceof JSONObject){
                                isNestedArray = true;

                                // apply function decodeDataResponseArray on nested array
                                String champInterneRoot = decodingSubObjectsInterne(champExternetab[i],champInterne,object);
                                String champInterneSub = champInterne.replace(champInterneRoot+".","");
                                String containedValue = "";
                                for(int j= i+1; j < champExternetab.length; j++ ){
                                    containedValue += "."+champExternetab[j];
                                }
                                containedValue = containedValue.substring(1);

                                decodeDataResponseArray(mapperDTO,object,champInterneRoot,champInterneSub, containedValue);

                                //assign the array with tranformed values to champInterne
                                String[] champInternetab = champInterne.split("\\.");
                                Object nestedArray = object.get(champExternetab[i]);

                                assignChampInterne(mapperDTO, champInterne.replace("."+champInternetab[champInternetab.length-1],""), dataResponse, nestedArray);
                                break;
                            }


                        }

                        object = (JSONObject) object.get(champExternetab[i]);
                    }

                }

            } else {
                value = dataResponse.get(champExterne);
            }
        } else {

            throw new InternalException(CommonUtils.placeholderFormat(MISSING_CHAMP_EXTERNE, FIRST_PLACEHOLDER, mapperDTO.getDataMapperId().toString()));
        }


        if (value!=null && !isNestedArray){

            if(StringUtils.isNotBlank(mapperDTO.getFormat())){
                String format = mapperDTO.getFormat();
                String timezone = mapperDTO.getTimezone();

                Map<String,Object> formattedData = FormatUtils.formatValue(value,format,champInterne,timezone, mapperDTO);
                value = formattedData.get("value");
                champInterne = (String) formattedData.get("champInterne");
            }

            //creation of the corresponding field "champInterne"
            if(value != null && StringUtils.isNotBlank(champInterne)){
                assignChampInterne(mapperDTO, champInterne, dataResponse, value);
            }

        }



    }


    /**
     * Create the corresponding champInterne and assign the found value to it
     * @param mapperDTO
     * @param champInterne
     * @param dataResponse
     * @param value
     * @throws JSONException
     */
    private static void assignChampInterne (DataMapperDTO mapperDTO, String champInterne, JSONObject dataResponse, Object value) throws JSONException {

        if (champInterne.contains(".")) {
            String[] champInternetab = champInterne.split("\\.");
            JSONObject object = new JSONObject();
            // assign the value of champExterne to the corresponding intern field
            object.put(champInternetab[champInternetab.length - 1], value);

            //case where root of champInterne not created yet
            if (dataResponse.isNull(champInternetab[0])) {

                // creation of the full object with subObjects indicated in champInterne to create
                for (int i = 2; i <= champInternetab.length; i++) {
                    JSONObject parent = new JSONObject();
                    parent.put(champInternetab[champInternetab.length - i], object);
                    object = parent;
                }
                dataResponse.put(champInternetab[0], object.get(champInternetab[0]));

            // champInterne root has already been created through a previous iteration on data_mappers
            } else {
                JSONObject responseObject = dataResponse.getJSONObject(champInternetab[0]);
                if (champInternetab.length > 2) {

                    //loop on different levels of existing champInterne  to reach the level were new object needs to be added
                    for (int i = 1; i < champInternetab.length - 1; i++) {
                        if (!responseObject.isNull(champInternetab[i])) {
                            responseObject = responseObject.getJSONObject(champInternetab[i]);
                            if (i == champInternetab.length - 2) {
                                responseObject.put(champInternetab[i + 1], object.get(champInternetab[i + 1]));
                            }
                        } else {
                            if(i == champInternetab.length - 2){
                                responseObject.put(champInternetab[i],object);
                            } else {
                                for (int j = i; j <= champInternetab.length - i; j++) {
                                    JSONObject parent = new JSONObject();
                                    parent.put(champInternetab[champInternetab.length - j], object);
                                    object = parent;
                                }

                                responseObject.put(champInternetab[i], object.get(champInternetab[i]));
                            }

                        }

                    }
                } else {
                    responseObject.put(champInternetab[1], object.get(champInternetab[1]));
                }


            }

        } else {
            dataResponse.putOpt(champInterne, value);
        }

    }
}