package com.android.imagelib.data.baidu;

import com.android.imagelib.data.ImageItem;

public class BaiduDataItem implements ImageItem {
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
