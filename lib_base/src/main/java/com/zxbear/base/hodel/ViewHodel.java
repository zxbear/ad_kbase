package com.zxbear.base.hodel;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
@SuppressWarnings("unchecked")
public class ViewHodel extends RecyclerView.ViewHolder {
    //所有的控件集合
    private SparseArray<View> mViews;
    //当前对象的view
    private View mConvertView;

    private ViewHodel(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    //创建一个viewHodel对象
    public static ViewHodel createViewHodel(Context mContext, ViewGroup parent, int layoutId) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new ViewHodel(view);
    }

    //获取当前的view
    public View getmConvertView() {
        return mConvertView;
    }

    //通过id获取某个View
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            //初始化view控件
            view = mConvertView.findViewById(viewId);
            //存放至view集合中
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
