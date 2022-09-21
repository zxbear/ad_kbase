package com.zxbear.base.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.zxbear.base.R;

import androidx.fragment.app.Fragment;

public abstract class BaseMvpFragment<T extends BaseMvpFragmentPresenter> extends Fragment implements BaseMvpFragmentView {
    protected T mPresenter;
    private LayoutInflater mInflater;
    public Context mContext;
    private View mView;
    public Bundle mBundle;
    //等待弹出框
    private ProgressDialog mDialog;
    public int TOAST_TIME = 3 * 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        initContentView();
        initPresenter();
        registerAndBind(mView); //注册EventBus
        initView();//初始化组件
        initData();//初始化数据
        return mView;
    }

    /**
     * 获取Fragment的视图
     *
     * @return
     */
    public abstract int getFView();

    /**
     * 设置mPresenter
     *
     * @return
     */
    public abstract T getPrensent();


    /**
     * 初始化view组件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();


    /**
     * 初始化ContentView
     */
    private void initContentView() {
        mView = mInflater.inflate(getFView(), null);
        mContext = getActivity();
        mBundle = getArguments();
    }

    /**
     * 初始化Presentrt
     */
    private void initPresenter() {
        mPresenter = getPrensent();
    }


    /**
     * 注册和绑定
     */
    private void registerAndBind(View mView) {
    }

    /**
     * findbyid
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int id) {
        return (T) mView.findViewById(id);
    }


    /**
     * 跳转页面获取简单数据
     *
     * @param key
     * @return
     */
    public Object getBundleValue(String key) {
        if (mBundle != null && !mBundle.isEmpty()) {
            return mBundle.get(key);
        } else {
            return "";
        }
    }

    /**
     * 跳转页面获取序列化数据
     *
     * @param key
     * @param mClass
     * @param <T>
     * @return
     */
    public <T> T getSerializableValue(String key, Class<T> mClass) {
        if (mBundle != null && !mBundle.isEmpty()) {
            return mClass.cast(mBundle.getSerializable(key));
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * toat消息
     *
     * @param msg
     * @param deplay
     */
    public void ToastMsg(Object msg, int... deplay) {
        if (deplay != null && deplay.length > 0) {
            TOAST_TIME = deplay[0];
        }
        if (msg != null) {
            Toast.makeText(mContext, msg.toString(), TOAST_TIME).show();
        }
    }


    /**
     * 默认对话框
     */
    @Override
    public void showLoading(String... title) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("加载数据中...");
        }
        if (title.length > 0) {
            mDialog.setMessage(title[0]);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 隐藏对话框
     */
    @Override
    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }


    /**
     * 跳转
     *
     * @param mClass
     * @param bundle
     */
    public void openActivity(Class mClass, Bundle... bundle) {
        Intent intent = new Intent(getActivity(), mClass);
        if (bundle.length > 0) {
            intent.putExtras(bundle[0]);
        }
        startActivity(intent);
    }

}
