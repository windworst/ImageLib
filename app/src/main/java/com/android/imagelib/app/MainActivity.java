package com.android.imagelib.app;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.imagelib.R;
import com.android.imagelib.data.ImageItem;
import com.android.imagelib.data.ImageSource;
import com.android.imagelib.util.ViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerViewPreview;
    private RelativeLayout mLayoutMain;
    private List<ImageItem> mImageItemList = new ArrayList<>();
    private int mPage = 100;
    private PreviewItemAdapter mPreviewItemAdapter = new PreviewItemAdapter();
    private RequestManager mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGlide = Glide.with(this);
        initView();
        loadBaiduImageList().subscribe(mPreviewItemAdapter);
    }

    private void initView() {
        mLayoutMain = ViewUtil.$(this, R.id.activity_main);
        mRecyclerViewPreview = ViewUtil.$(this, R.id.recyclerView);
        mRecyclerViewPreview.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerViewPreview.setAdapter(mPreviewItemAdapter);
        mLayoutMain.setOnTouchListener((v, event) -> {
            if (!ViewCompat.canScrollVertically(mRecyclerViewPreview, 1)) {
                loadBaiduImageList().subscribe(mPreviewItemAdapter);
            }
            return false;
        });
        mRecyclerViewPreview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    loadBaiduImageList().subscribe(mPreviewItemAdapter);
                }
            }
        });
    }

    private void showPicture(ImageItem imageItem) {
        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        View view = LayoutInflater.from(this).inflate(R.layout.display_image, null);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(mLayoutMain, Gravity.CENTER, 0, 0);
        ImageView imageView = ViewUtil.$(view, R.id.imageViewShowPicture);
        View.OnClickListener onClickListener = v -> mGlide.load(imageItem.getImageUrl()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setImageDrawable(resource);
            }
        });
        onClickListener.onClick(imageView);
        imageView.setOnClickListener(onClickListener);
    }

    private Observable<List<ImageItem>> loadBaiduImageList() {
        return ImageSource.BAIDU.loadImage(mImageItemList.size(), mPage, "美女", "全部").compose(bindToLifecycle()).map(imageItems -> {
            mImageItemList.addAll(imageItems);
            return mImageItemList;
        });
    }

    private class PreviewItemAdapter extends RecyclerView.Adapter<PreviewItemAdapter.ItemHolder> implements Observer<List<ImageItem>> {
        private List<ImageItem> imageItemList = Collections.EMPTY_LIST;
        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.display_item, null));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            final ImageItem imageItem = imageItemList.get(position);
            String url = imageItem.getThumbImageUrl();
            if (url != null && !url.equals(holder.url)) {
                holder.url = imageItem.getThumbImageUrl();
                holder.imageView.setOnClickListener(v -> showPicture(imageItem));
                mGlide.load(imageItem.getThumbImageUrl()).into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            return imageItemList.size();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(List<ImageItem> imageItems) {
            imageItemList = imageItems;
            notifyItemRangeInserted(imageItemList.size(), 1);
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            String url;

            ItemHolder(View itemView) {
                super(itemView);
                imageView = ViewUtil.$(itemView, R.id.imageView);
            }
        }
    }
}
