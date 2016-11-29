package com.android.imagelib;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.imagelib.data.BaiduImageService;
import com.android.imagelib.data.BaiduResponse;
import com.android.imagelib.util.ViewUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

import static com.android.imagelib.R.id.recyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPreviewRecyclerView;
    private List<DisplayItem> mDisplayItemList = new ArrayList<>();
    private int mMaxIndex = 0, mPage = 100;
    private PreviewItemAdapter mPreviewItemAdapter = new PreviewItemAdapter();
    private BaiduImageService mBaiduImageService = new Retrofit.Builder().baseUrl(BaiduImageService.URL)
            .addConverterFactory(FastJsonConverterFactory.create()).build().create(BaiduImageService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadImageList();
    }

    private void initView() {
        mPreviewRecyclerView = ViewUtil.$(this, recyclerView);
        mPreviewRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mPreviewRecyclerView.setAdapter(mPreviewItemAdapter);

        mPreviewRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    loadImageList();
                }
            }
        });
    }

    private void loadImageList() {
        mBaiduImageService.loadImage(mMaxIndex, mPage, "美女", "性感").enqueue(new Callback<BaiduResponse>() {
            @Override
            public void onResponse(Call<BaiduResponse> call, Response<BaiduResponse> response) {
                BaiduResponse baiduResponse = response.body();
                if (baiduResponse.start_index != mMaxIndex) return;
                mMaxIndex = baiduResponse.start_index + baiduResponse.return_number;
                int oldSize = mDisplayItemList.size();
                if (baiduResponse.data.size() > 0) {
                    baiduResponse.data.remove(baiduResponse.data.size() - 1);
                }
                mDisplayItemList.addAll(baiduResponse.data);
                mPreviewItemAdapter.notifyItemRangeChanged(oldSize, mDisplayItemList.size() - oldSize);
            }

            @Override
            public void onFailure(Call<BaiduResponse> call, Throwable t) {

            }
        });
    }

    private class PreviewItemAdapter extends RecyclerView.Adapter<PreviewItemAdapter.ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.display_item, null));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Picasso.with(MainActivity.this).load(mDisplayItemList.get(position).getThumbImageUrl()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mDisplayItemList.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ItemHolder(View itemView) {
                super(itemView);
                imageView = ViewUtil.$(itemView, R.id.imageView);
            }
        }
    }
}
