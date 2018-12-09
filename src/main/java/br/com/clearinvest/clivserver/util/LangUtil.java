package br.com.clearinvest.clivserver.util;

import java.util.Collection;

public class LangUtil {

    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;

        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;

        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();

        } else {
            return false;
        }
    }

    public static <T> T valueOrElse(T originalValue, T defaultValue) {
        return originalValue == null? defaultValue: originalValue;
    }

}
