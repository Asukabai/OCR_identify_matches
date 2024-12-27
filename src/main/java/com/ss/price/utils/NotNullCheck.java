package com.ss.price.utils;

import java.util.Collection;
import java.util.Map;

public class NotNullCheck {
    public NotNullCheck() {
    }

    public static boolean str(String str) {
        return str != null && !str.isEmpty() && str.length() >= 1 && !"".equals(str.replaceAll(" ", ""));
    }

    public static boolean array(Collection list) {
        return list != null && !list.isEmpty() && list.size() != 0;
    }

    public static boolean map(Map map) {
        return map != null && !map.isEmpty() && map.size() != 0;
    }
}