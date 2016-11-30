package com.android.imagelib.data.baidu;

import com.android.imagelib.data.ImageItem;
import com.android.imagelib.data.ImageLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class BaiduImageLoader implements ImageLoader {
    private BaiduImageService mBaiduImageService = new Retrofit.Builder().baseUrl(BaiduImageService.URL)
            .addConverterFactory(FastJsonConverterFactory.create()).build().create(BaiduImageService.class);

    public Observable<List<? extends ImageItem>> loadImage(final int index, final int page, String... args) {
        final PublishSubject<List<? extends ImageItem>> publishSubject = PublishSubject.create();
        mBaiduImageService.loadImage(index, page, args.length >= 1 ? args[0] : "", args.length >= 2 ? args[1] : "").enqueue(new Callback<BaiduResponse>() {
            @Override
            public void onResponse(Call<BaiduResponse> call, Response<BaiduResponse> response) {
                BaiduResponse baiduResponse = response.body();
                if (null == baiduResponse || baiduResponse.start_index != index) return;
                if (baiduResponse.data.size() > 0) {
                    baiduResponse.data.remove(baiduResponse.data.size() - 1);
                }
                Observable.just(baiduResponse.data).observeOn(AndroidSchedulers.mainThread()).subscribe(publishSubject);
            }

            @Override
            public void onFailure(Call<BaiduResponse> call, Throwable t) {

            }
        });
        return publishSubject;
    }
}
