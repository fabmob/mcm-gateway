package com.gateway.commonapi.error;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.properties.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Replace the Spring basicErrorController (when no mapping handled the request) to mask the default Spring white label page in browsers
 * and format the json responses in the gateway way
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorMessages errorMessages;


    /**
     * Returns a human-readable message for bad API URLs in browsers
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> errorHtml(HttpServletRequest request) {
        return new ResponseEntity<>(errorMessages.getTechnicalBadRequestLabel(), HttpStatus.valueOf(Integer.parseInt(errorMessages.getTechnicalBadRequestCode())));
    }


    /**
     * Returns a json error via the ExceptionHandler
     */
    @GetMapping
    public ResponseEntity<GenericError> error(HttpServletRequest request) {
        throw new BadRequestException(errorMessages.getTechnicalBadRequestDescription());
    }

}