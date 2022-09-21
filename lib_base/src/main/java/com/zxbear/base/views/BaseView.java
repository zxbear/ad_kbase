package com.zxbear.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.StyleableRes;


public abstract class BaseView extends RelativeLayout {
    public View mView;
    public Context mContext;
    private TypedArray ta;

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(getView(), this);
        mContext = context;
        initView();
        ta = context.obtainStyledAttributes(attrs, getStyleableIds() == null ? new int[0] : getStyleableIds());
        initData();
        if (ta != null) {
            ta.recycle();
        }
    }


    /**
     * 设置View的视图
     */
    public abstract int getView();

    /**
     * 初始化组件
     */
    public abstract void initView();


    /**
     * 初始化自定义属性
     */
    public abstract @StyleableRes
    int[] getStyleableIds();


    /**
     * 初始化组件数据
     */
    public abstract void initData();


    /**
     * findbyid
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById1(int id) {
        return mView.findViewById(id);
    }

    /**
     * @param index
     * @param defValue
     * @return
     */
    public boolean getStyleableBoolean(@StyleableRes int index, boolean defValue) {
        if (ta != null) {
            return ta.getBoolean(index, defValue);
        } else {
            return false;
        }
    }

    public int getStyleableInt(@StyleableRes int index, int defValue) {
        if (ta != null) {
            return ta.getInt(index, defValue);
        } else {
            return -1;
        }
    }

    public String getStyleableString(@StyleableRes int index) {
        if (ta != null) {
            return ta.getString(index);
        } else {
            return "";
        }
    }

    public Drawable getStyleableDrawable(@StyleableRes int index) {
        if (ta != null) {
            return ta.getDrawable(index);
        } else {
            return null;
        }
    }

    public double getStyleableDimension(@StyleableRes int index, int defValue) {
        if (ta != null) {
            return ta.getDimension(index,defValue);
        } else {
            return defValue;
        }
    }


    public int getStyleableResourceId(@StyleableRes int index, int defValue) {
        if (ta != null) {
            return ta.getResourceId(index, defValue);
        } else {
            return Color.WHITE;
        }
    }

}
