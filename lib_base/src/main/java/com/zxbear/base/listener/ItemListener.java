package com.zxbear.base.listener;

import android.view.View;

/**
 * 条目点击事件
 * @param <T>
 */
public interface ItemListener<T> {
    /**
     * Item点击事件监听
     * @param view
     * @param entity
     * @param position
     */
    void onItemClick(View view, T entity, int position);

    /**
     * 长按点击事件
     * @param view
     * @param entity
     * @param position
     */
    void onItemLongClick(View view, T entity, int position);
}
