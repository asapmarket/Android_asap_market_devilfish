package com.joey.devilfish.ui.fragment.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joey.devilfish.R;
import com.joey.devilfish.utils.PromptUtils;
import com.joey.devilfish.utils.StringUtils;
import com.joey.devilfish.utils.Utils;
import com.joey.devilfish.widget.CircularProgress;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * 文件描述
 * Date: 2017/7/21
 *
 * @author xusheng
 */

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    protected View mRootView = null;

    protected WeakReference<Dialog> mNetDialog;

    protected CircularProgress mProgress;

    protected abstract int getContentLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.changeLanguage(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        mRootView = inflater.inflate(getContentLayout(), null);
        ButterKnife.bind(this, mRootView);
        getIntentData();

        initHeaderView();
        initContentView();
        initFooterView();

        initData();

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void getIntentData() {

    }

    protected void initContentView() {
        if (null == mRootView) {
            return;
        }
    }

    protected void initHeaderView() {
    }

    protected void initFooterView() {
    }

    protected void initData() {

    }

    public void showNetDialog(String loadingTv) {
        if (mNetDialog == null || mNetDialog.get() == null) {
            Dialog dialog = new Dialog(mContext, R.style.NromalDialog);
            RelativeLayout layout = (RelativeLayout) View.inflate(mContext,
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
        if (Utils.isChinese(getActivity())) {
            PromptUtils.getInstance().showShortPromptToast(getActivity(), msg_cn);
        } else {
            PromptUtils.getInstance().showShortPromptToast(getActivity(), msg_en);
        }
    }
}
