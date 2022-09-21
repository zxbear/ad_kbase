package com.zxbear.base.activty;


import androidx.annotation.NonNull;

public interface BaseMvpView {

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
    void toastMsg(@NonNull Object msg);


    /**
     * finish
     */
    void activityFinish();

}
