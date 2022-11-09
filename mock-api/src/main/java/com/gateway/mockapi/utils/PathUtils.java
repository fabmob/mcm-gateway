package com.gateway.mockapi.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Pattern;

@Slf4j
public class PathUtils {

    /**
     * Default constructor.
     *
     * @throws IllegalStateException Utility class, constructor should not be used.
     */
    private PathUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if a path is really a child of a parent
     *
     * @param potentialChild the path to check
     * @param parent         the parent path that may contain the child
     * @return true if the path is a child of the parent
     */
    public static boolean isAChild(Path potentialChild, Path parent) {
        boolean child = false;
        if (potentialChild != null && parent != null) {
            child = normalizePath(potentialChild).startsWith(normalizePath(parent));
        }
        return child;
    }

    /**
     * Extracts from a path the last directory name which is supposed to be the response code to use
     *
     * @param path a directory or a file in a directory
     * @return the last directory name
     */
    public static String getResponseCode(Path path) {
        String responseCode = null;
        if (path != null) {
            String[] pathElements = normalizePath(path).toString().split(Pattern.quote(File.separator));
            boolean isLastElementAFile = pathElements[pathElements.length - 1].contains(".");

            if (pathElements.length > 0 && !isLastElementAFile) {
                responseCode = pathElements[pathElements.length - 1];
            } else if (pathElements.length > 1 && isLastElementAFile) {
                responseCode = pathElements[pathElements.length - 2];
            }
        }
        return responseCode;
    }

    /**
     * Normalizes (resolve . and ..) and removes windows specific backslashes
     *
     * @param original
     * @return an absolute normalized path
     */
    private static Path normalizePath(Path original) {
        Path normalized = null;
        if (original != null) {
            normalized = original.toAbsolutePath().normalize();
        }
        return normalized;
    }
}
