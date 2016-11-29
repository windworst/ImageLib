package com.android.imagelib.data;

import com.android.imagelib.DisplayItem;

public class BaiduDataItem implements DisplayItem {
    public String download_url, thumbnail_url;

    @Override
    public String getThumbImageUrl() {
        return thumbnail_url;
    }

    @Override
    public String getImageUrl() {
        return download_url;
    }
}
