package com.magneto.util;

import java.util.List;

public class MathUtil {

    public static Double max(List<Double> dataList) {
        double res = -1;
        for (Double v : dataList) {
            if (v > res) {
                res = v;
            }
        }
        return res;
    }

    public static Double min(List<Double> dataList) {
        double res = 1e18;
        for (Double v : dataList) {
            if (v < res) {
                res = v;
            }
        }
        return res;
    }
}
