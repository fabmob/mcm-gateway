package com.gateway.commonapi.utils;


import com.gateway.commonapi.tests.UTTestCase;
import com.gateway.commonapi.utils.enums.TypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ExceptionUtilsTest extends UTTestCase {


    @Test
    public void testConvertToSingleLine() {
        String msg = ExceptionUtils.getBadEnumValueMessage(TypeEnum.class, "bad_value");
        Assertions.assertTrue(StringUtils.isNotEmpty(msg));
        Assertions.assertTrue(msg.contains("TypeEnum"));
        Assertions.assertTrue(msg.contains("bad_value"));
        Assertions.assertTrue(msg.contains(TypeEnum.CAR_SHARING.toString()));
        Assertions.assertTrue(msg.contains(TypeEnum.TAXI_VTC.toString()));

    }

    @org.junit.Test
    public void testGetRootCause() {
        // Null exception
        Assertions.assertNull(ExceptionUtils.getRootException(null, null));

        // IllegalException
        Throwable illegalException = new IllegalArgumentException();
        Assertions.assertEquals(illegalException, ExceptionUtils.getRootException(illegalException, null));
        Assertions.assertEquals(illegalException, ExceptionUtils.getRootException(illegalException, IllegalArgumentException.class));

        // IllegalException > CallerException > StateException
        Throwable callerException = new IllegalCallerException();
        Throwable stateException = new IllegalStateException();
        illegalException.initCause(callerException.initCause(stateException));
        Assertions.assertEquals(stateException, ExceptionUtils.getRootException(illegalException, null));
        Assertions.assertEquals(callerException, ExceptionUtils.getRootException(illegalException, IllegalCallerException.class));
        Assertions.assertNull(ExceptionUtils.getRootException(illegalException, ArrayIndexOutOfBoundsException.class));
    }
}

