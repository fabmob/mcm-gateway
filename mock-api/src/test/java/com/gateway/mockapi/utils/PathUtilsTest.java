package com.gateway.mockapi.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

class PathUtilsTest {

    @Test
    void testIsAChild() {
        // Null values
        assertFalse(PathUtils.isAChild(null, null));
        assertFalse(PathUtils.isAChild(Paths.get("."), null));
        assertFalse(PathUtils.isAChild(null, Paths.get(".")));

        // Test if subdirectory or subfile of the parent directory
        assertTrue(PathUtils.isAChild(Paths.get("."), Paths.get(".")));
        assertTrue(PathUtils.isAChild(Paths.get("/mnt/share/sub/file.json"), Paths.get("/mnt/share")));
        assertTrue(PathUtils.isAChild(Paths.get("/mnt/share/sub/"), Paths.get("/mnt/share")));
        assertFalse(PathUtils.isAChild(Paths.get("/mnt/other/sub/"), Paths.get("/mnt/share")));

        // Deal with path traversal
        assertTrue(PathUtils.isAChild(Paths.get("/mnt/share/sub/../file.json"), Paths.get("/mnt/share")));
        assertFalse(PathUtils.isAChild(Paths.get("/mnt/share/sub/../../../file.json"), Paths.get("/mnt/share")));
    }

    @Test
    void testGetResponseCode() {
        //Test when response code is correct
        assertEquals("201", PathUtils.getResponseCode(Paths.get("/mnt/sub/201/file.json")));
        assertEquals("403", PathUtils.getResponseCode(Paths.get("/mnt/sub/403")));
    }
}