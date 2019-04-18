
package com.longteng.framework.util;

/**
 * StringUtil description: ???
 *
 */
public class StringUtil {

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str)) {
            return true;
        }

        return false;
    }

}
