package com.android.imagelib.data.baidu;

import com.android.imagelib.data.ImageItem;
import com.android.imagelib.data.ImageLoader;

import java.util.Collections;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaiduImageLoader implements ImageLoader {
    private BaiduImageService mBaiduImageService = new Retrofit.Builder().baseUrl(BaiduImageService.URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(FastJsonConverterFactory.create()).build()
            .create(BaiduImageService.class);

    public Observable<List<? extends ImageItem>> loadImage(final int index, final int page, String... args) {
        return mBaiduImageService.loadImage(index, page, args.length >= 1 ? args[0] : "", args.length >= 2 ? args[1] : "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(baiduResponse -> {
                    if (null == baiduResponse || baiduResponse.start_index != index) {
                        return Collections.EMPTY_LIST;
                    }
                    if (baiduResponse.data.size() > 0) {
                        baiduResponse.data.remove(baiduResponse.data.size() - 1);
                    }
                    return baiduResponse.data;
                });
    }
}
