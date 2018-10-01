package com.joey.devilfish.ui.activity.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joey.devilfish.R;
import com.joey.devilfish.application.BaseApplication;
import com.joey.devilfish.utils.PromptUtils;
import com.joey.devilfish.utils.StringUtils;
import com.joey.devilfish.utils.Utils;
import com.joey.devilfish.widget.CircularProgress;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * 文件描述
 * Date: 2017/5/14
 *
 * @author xusheng
 */

public abstract class BaseActivity extends FragmentActivity {

    protected View mRootView;

    protected WeakReference<Dialog> mNetDialog;

    protected CircularProgress mProgress;

    protected abstract int getContentLayout();

    protected void getIntentData() {

    }

    protected void getIntentData(Bundle outState) {
    }

    protected void initHeaderView(Bundle savedInstanceState) {

    }

    protected void initContentView(Bundle savedInstanceState) {

    }

    protected void initFooterView(Bundle savedInstanceState) {
    }

    protected void initData(Bundle savedInstanceState) {

    }

    protected void initialData(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.changeLanguage(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initialData(savedInstanceState);

        mRootView = LayoutInflater.from(this).inflate(getContentLayout(), null);
        setContentView(mRootView);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // 5.0及以上
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.WHITE);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 4.4~5.0
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
//                    localLayoutParams.flags);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //android6.0以后可以对状态栏文字颜色和图标进行修改
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

        ButterKnife.bind(BaseActivity.this);

        getIntentData(savedInstanceState);
        getIntentData();
        initHeaderView(savedInstanceState);
        initContentView(savedInstanceState);
        initFooterView(savedInstanceState);
        initData(savedInstanceState);

        BaseApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(this);
    }

    public void showNetDialog(String loadingTv) {
        if (mNetDialog == null || mNetDialog.get() == null) {
            Dialog dialog = new Dialog(BaseActivity.this, R.style.NromalDialog);
            RelativeLayout layout = (RelativeLayout) View.inflate(BaseActivity.this,
                    R.layout.layout_indeterminate_dialog, null);
            TextView tv = (TextView) layout.findViewById(R.id.loading_tv);
            mProgress = (CircularProgress) layout.findViewById(R.id.progress);
            if (!StringUtils.getInstance().isNullOrEmpty(loadingTv)) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(loadingTv);
            }

            mProgress.startSpinning();
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            mNetDialog = new WeakReference<Dialog>(dialog);
        } else {
            if (!mNetDialog.get().isShowing()) {
                mNetDialog.get().show();
            }
        }
    }

    public void closeNetDialog() {
        if (mNetDialog != null
                && mNetDialog.get() != null
                && mNetDialog.get().isShowing()) {
            mNetDialog.get().dismiss();

            if (null != mProgress) {
                mProgress.stopSpinning();
            }
        }
    }

    public void responseError(String msg_cn, String msg_en) {
        if (Utils.isChinese(this)) {
            if (!StringUtils.getInstance().isNullOrEmpty(msg_cn)) {
                PromptUtils.getInstance().showShortPromptToast(this, msg_cn);
            }
        } else {
            if (!StringUtils.getInstance().isNullOrEmpty(msg_en)) {
                PromptUtils.getInstance().showShortPromptToast(this, msg_en);
            }
        }
    }

}
