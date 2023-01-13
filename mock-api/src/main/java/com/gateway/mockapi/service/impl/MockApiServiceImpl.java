package com.gateway.mockapi.service.impl;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnauthorizedException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.mockapi.service.MockApiService;
import com.gateway.mockapi.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import static com.gateway.mockapi.utils.constant.MockApiMessageDict.*;

@Slf4j
@Service
public class MockApiServiceImpl implements MockApiService {

    @Value("${gateway.service.mockapi.basedir}")
    private String mockDirectory;

    @Autowired
    private ErrorMessages errorMessages;

    @Override
    public int getMockedResponseCode(String mockPath) {

        Path responsePath = buildMockPath(mockPath);
        String responseCode = PathUtils.getResponseCode(responsePath);

        try {
            return Integer.parseInt(responseCode);
        } catch (NumberFormatException nfe) {
            log.warn(ERROR_MOCKS_NO_RESPONSE_CODE);
            throw new BadRequestException(ERROR_MOCKS_NO_RESPONSE_CODE);
        }
    }

    @Override
    public String getMockedBody(String mockPath) {

        Path responsePath = buildMockPath(mockPath);
        String body = null;

        if (Files.isRegularFile(responsePath)) {
            try {
                body = Files.readString(responsePath);
            } catch (IOException e) {
                log.error(MessageFormat.format(ERROR_MOCKS_DURING_FILE_READ,
                        CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER), e.getMessage()), e);
                throw new InternalException(e.getMessage());
            }
        }
        return body;
    }

    /**
     * Checks that the required mockPath exists in mock directory and returns a full path to it.
     *
     * @param mockPath
     * @return the full path of the mock response
     * @throws BadRequestException if root directory is not set or mockPath is invalid
     * @throws NotFoundException   if mock directory or file is not found or not readable
     */
    protected Path buildMockPath(String mockPath) {
        if (StringUtils.isEmpty(mockDirectory)) {
            log.error(ERROR_MOCKS_NO_ROOT_DIR);
            throw new InternalException(ERROR_MOCKS_NO_ROOT_DIR);
        } else if (StringUtils.isEmpty(mockPath)) {
            log.warn(ERROR_MOCKS_NO_HEADER);
            throw new BadRequestException(ERROR_MOCKS_NO_HEADER);
        } else if ( !mockPath.matches("^[0-9a-zA-Z].*")) {
            log.warn(ERROR_MOCKS_INCORRECT_PATH);
            throw new BadRequestException(ERROR_MOCKS_INCORRECT_PATH);
        }

        Path responsePath = null;
        try {
            responsePath = Paths.get(mockDirectory, mockPath.replace("\\", "/"));

            if (!PathUtils.isAChild(responsePath, Paths.get(mockDirectory))) {
                log.error(ERROR_MOCKS_PATH_TRAVERSAL);
                throw new UnauthorizedException(ERROR_MOCKS_PATH_TRAVERSAL);
            }
            if (!Files.isReadable(responsePath)) {
                log.warn(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), ""));
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), ""));
            }
        } catch (InvalidPathException ipe) {
            log.error(ERROR_MOCKS_HEADER_UNUSABLE);
            throw new BadRequestException(ERROR_MOCKS_HEADER_UNUSABLE);
        }

        return responsePath;
    }
}
