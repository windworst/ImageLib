package com.android.imagelib.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaiduImageService {
    String URL = "https://image.baidu.com/";

    @GET("/channel/listjson?ie=utf8")
    Call<BaiduResponse> loadImage(@Query("pn") int index, @Query("rn") int number, @Query("tag1") String tag1, @Query("tag2") String tag2);
}
