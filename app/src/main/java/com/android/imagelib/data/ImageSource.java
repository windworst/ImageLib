package com.android.imagelib.data;

import com.android.imagelib.data.baidu.BaiduImageLoader;

public interface ImageSource {
    ImageLoader BAIDU = new BaiduImageLoader();
}
