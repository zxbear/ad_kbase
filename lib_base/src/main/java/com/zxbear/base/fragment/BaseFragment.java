package com.zxbear.base.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    public Context mContext;
    private View mView;
    public Bundle mBundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getFView(), null);
        mContext = getActivity();
        mBundle = getArguments();
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
     * 初始化view组件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

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

}
