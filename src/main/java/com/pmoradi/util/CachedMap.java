package com.pmoradi.util;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import java.util.Map;
import java.util.Collections;

public class CachedMap {

    //Soft Refrence for the returned cached map?

    public static <K,V> Map<K,V> getCachedMap(int timeout){
        return Collections.synchronizedMap(new PassiveExpiringMap<K, V>(timeout));
    }
}
