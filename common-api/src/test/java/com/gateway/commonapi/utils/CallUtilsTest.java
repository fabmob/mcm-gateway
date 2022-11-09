package com.gateway.commonapi.utils;


import com.gateway.commonapi.tests.UTTestCase;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.gateway.commonapi.utils.CallUtils.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class CallUtilsTest extends UTTestCase {

    @Test
    public void testSaveOutputStandardInCallThread() {
        saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
        Assertions.assertEquals(StandardEnum.COVOITURAGE_STANDARD.toString(), CallUtils.getOutputStandardFromCallThread());
    }

    @Test
    public void testSaveValidCodesInCallThread() {
        saveValidCodesInCallThread("[200,400]");
        Assertions.assertEquals("[200,400]", getValidCodesFromCallThread());
    }

    @Test
    public void testSaveValidCodesInCallThreadList() {
        saveValidCodesInCallThread(Arrays.asList(200, 400));
        Assertions.assertEquals("[200, 400]", getValidCodesFromCallThread());
    }


}

