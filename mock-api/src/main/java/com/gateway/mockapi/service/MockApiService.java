package com.gateway.mockapi.service;

public interface MockApiService {

    /**
     * Checks that the mockPath exists and returns the last subdirectory name
     *
     * @param mockPath the relative mock path
     * @return the last directory value as an int response code
     */
    int getMockedResponseCode(String mockPath);

    /**
     * Checks that the mockPath exists and if it lasts with a file, returns the file content
     *
     * @param mockPath the relative mock path
     * @return the content of the required file or null
     */
    String getMockedBody(String mockPath);

}
