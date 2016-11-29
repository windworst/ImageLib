package com.android.imagelib.util;

import android.app.Activity;
import android.view.View;

public class ViewUtil {
    public static <T extends View> T $(View view, int rId) {
        T v = (T) view.getTag(rId);
        if (null == v) {
            view.setTag(rId, v = (T) view.findViewById(rId));
        }
        return v;
    }

    public static <T extends View> T $(Activity activity, int rId) {
        return $(activity.getWindow().getDecorView(), rId);
    }
}
