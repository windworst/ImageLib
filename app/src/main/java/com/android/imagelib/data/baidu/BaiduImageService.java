package com.android.imagelib.data.baidu;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BaiduImageService {
    String URL = "https://image.baidu.com/";

    @GET("/channel/listjson?ie=utf8")
    Observable<BaiduResponse> loadImage(@Query("pn") int index, @Query("rn") int number, @Query("tag1") String tag1, @Query("tag2") String tag2);
}
