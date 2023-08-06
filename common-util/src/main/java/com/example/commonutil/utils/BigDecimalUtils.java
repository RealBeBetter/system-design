package com.example.commonutil.utils;

import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author wei.song
 * @since 2023/08/06 12:41
 */
public class BigDecimalUtils {

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (a == null || b == null || b.floatValue() == 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(b, 6, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale) {
        if (a == null || b == null || b.floatValue() == 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return BigDecimal.ZERO;
        }
        return a.multiply(b);
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b, int scale) {
        if (a == null || b == null) {
            return BigDecimal.ZERO;
        }
        return a.multiply(b).setScale(scale, RoundingMode.HALF_UP);
    }


    public static BigDecimal multiply(BigDecimal a, BigDecimal b, BigDecimal c) {
        if (a == null || b == null | c == null) {
            return BigDecimal.ZERO;
        }
        return a.multiply(b).multiply(c);
    }


    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) {
            return BigDecimal.ZERO;
        } else if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            return a.add(b);
        }
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b, int scale) {
        return add(a, b).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) {
            return BigDecimal.ZERO;
        } else if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            return a.subtract(b);
        }
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b, int scale) {
        return subtract(a, b).setScale(scale, RoundingMode.HALF_UP);
    }

    public static String toString(BigDecimal a) {
        if (a == null) {
            return "0";
        }
        return a.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static String toPointString(BigDecimal a) {
        if (a == null) {
            return "0.00";
        }
        return a.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static String toFourScale(BigDecimal a) {
        if (a == null) {
            return "0.0000";
        }
        return a.setScale(4, RoundingMode.HALF_UP).toString();
    }

    public static BigDecimal ifNullZero(String str) {
        if (StringUtils.isEmpty(str)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(str);
    }

    public static boolean isNegative(BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) > 0;
    }

    public static boolean equal(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return false;
        }
        return a.compareTo(b) == 0;
    }

    public static boolean isZeroOrNull(BigDecimal a) {
        if (a == null) {
            return true;
        }
        return a.compareTo(BigDecimal.ZERO) == 0;
    }

    public static BigDecimal doubleValueOf(Double a) {
        if (ObjectUtil.isEmpty(a)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(a);
    }

}
