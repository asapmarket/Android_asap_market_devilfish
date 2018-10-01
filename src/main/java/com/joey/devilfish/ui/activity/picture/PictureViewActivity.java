package com.joey.devilfish.ui.activity.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.joey.devilfish.R;
import com.joey.devilfish.manager.MediaManager;
import com.joey.devilfish.ui.adapter.ImagePagerAdapter;
import com.joey.devilfish.utils.ExtendUtils;
import com.joey.devilfish.utils.PromptUtils;
import com.joey.devilfish.widget.viewpager.HackyViewPager;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 浏览图片页面
 * Date: 2017/5/14
 *
 * @author xusheng
 */

public class PictureViewActivity extends Activity {

    private HackyViewPager mPictureView;

    private List<String> mImageList;

    private int mCurrentPosition = 0;

    private MediaManager mMediaManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch (what) {
                case MediaManager.SAVE_FAIL: {
                    PromptUtils.getInstance().showShortPromptToast(PictureViewActivity.this, "图片保存失败！");
                    break;
                }

                case MediaManager.SAVE_SUCCESS: {
                    PromptUtils.getInstance().showShortPromptToast(PictureViewActivity.this, "图片保存成功！");
                    ExtendUtils.getInstance().updateAlbum(PictureViewActivity.this, new File(mMediaManager.getLocalPath()));
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        mPictureView = (HackyViewPager) findViewById(R.id.hvp_picture);
        initData();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        mImageList = (List<String>) getIntent().getSerializableExtra("images");
        mCurrentPosition = getIntent().getIntExtra("position", 0);

        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(this);

        imageAdapter.setOperateListener(new ImagePagerAdapter.OperateListener() {
            @Override
            public void onImageTap() {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onDownload(String url) {
                download(url);
            }
        });
        imageAdapter.setData(mImageList);
        mPictureView.setAdapter(imageAdapter);
        mPictureView.setVerticalFadingEdgeEnabled(false);
        mPictureView.setHorizontalFadingEdgeEnabled(false);
        mPictureView.setCurrentItem(mCurrentPosition);
        imageAdapter.notifyDataSetChanged();
    }

    private void download(String url) {
        mMediaManager = new MediaManager();
        mMediaManager.setMediaPath(url);
        mMediaManager.saveMedia(mHandler);
    }

    /**
     * 图片浏览
     *
     * @param context
     * @param imageList
     */
    public static void forwardPictureView(Context context, List<String> imageList, int position) {
        Intent intent = new Intent(context, PictureViewActivity.class);
        intent.putExtra("images", (Serializable) imageList);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

}
