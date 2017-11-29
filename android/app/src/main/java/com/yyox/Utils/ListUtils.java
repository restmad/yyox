package com.yyox.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-05-17.
 */

public final class ListUtils {
    public static <T> List<T> filter(List<T> list, ListUtilsHook<T> hook) {
        ArrayList<T> r = new ArrayList<T>();
        for (T t : list) {
            if (hook.Result(t)) {
                r.add(t);
            }
        }
        r.trimToSize();
        return r;
    }
}
