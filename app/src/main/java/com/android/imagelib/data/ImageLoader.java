package com.android.imagelib.data;

import java.util.List;

import rx.Observable;

public interface ImageLoader {
    /**
     * 加载图片信息
     *
     * @param index 图片信息索引
     * @param page  期望返回的图片信息个数
     * @param args  其他参数
     * @return rx响应对象, 返回一个包含图片信息的List, 有图片Url等信息
     */
    Observable<List<? extends ImageItem>> loadImage(final int index, final int page, String... args);
}
