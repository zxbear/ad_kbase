package com.zxbear.base.listener;


import com.zxbear.base.hodel.ViewHodel;

/**
 * 某一类的Item对象
 */

public interface MyViewItem<T> {
    // 获取item布局
    int getItemLayout();

    // 是否开启点击事件
    boolean openClick();

    // 是否为当前的item布局
    boolean isItemView(T entity, int position);

    //将item的控件与需要显示的数据绑定
    void convert(ViewHodel hodel, T entity, int position);
}
