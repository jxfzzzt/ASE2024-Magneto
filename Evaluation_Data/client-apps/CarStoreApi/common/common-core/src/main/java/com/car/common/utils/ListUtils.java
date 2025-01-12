package com.car.common.utils;

import java.util.HashSet;
import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/3/12 16:43
 */
public class ListUtils {

    /**
     * List数据去重
     * @param list
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }
}
