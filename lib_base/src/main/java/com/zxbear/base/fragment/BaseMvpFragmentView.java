package com.zxbear.base.fragment;

public interface BaseMvpFragmentView {
    /**
     * 显示加载中
     */
    void showLoading(String... title);

    /**
     * 隐藏加载
     */
    void hideLoading();


    /**
     * toastMsg
     */
    void toastMsg(Object msg);
}
