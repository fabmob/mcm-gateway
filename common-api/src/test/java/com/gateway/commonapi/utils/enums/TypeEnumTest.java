package com.gateway.commonapi.utils.enums;

import com.gateway.commonapi.tests.UTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TypeEnumTest extends UTTestCase {

    @Test
    public void testMaas() {
        Assertions.assertTrue(TypeEnum.isMaasType("MAAS_APPLICATION"));
        Assertions.assertTrue(TypeEnum.isMaasType("MAAS_EDITOR"));
    }

    @Test
    public void testNotMaas() {
        Assertions.assertFalse(TypeEnum.isMaasType("TAXI_VTC"));
        Assertions.assertFalse(TypeEnum.isMaasType("CARPOOLING"));
        Assertions.assertFalse(TypeEnum.isMaasType(""));
        Assertions.assertFalse(TypeEnum.isMaasType(null));
    }

}
