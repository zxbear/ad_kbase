package com.zxbear.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {
    public Context myContext;
    public List<T> mData;
    public onClickmItemLisener mLisener;
    private boolean clickItemEnable = true;
    private int itemWidth = 0;
    public ViewGroup viewGroup;
    public int viewType;

    public BaseAdapter(Context myContext, List<T> mData) {
        this.myContext = myContext;
        if (mData != null) {
            this.mData = mData;
        } else {
            this.mData = new ArrayList<>();
        }
    }

    //**********************************************定义抽象方法************************************************

    /**
     * 加载item布局
     *
     * @return
     */
    public abstract int getItemLayout();

    public abstract void onBindMViewHolder(ViewHolder holder, T item, int position);

    /**
     * 获取ViewHolder
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        this.viewGroup = viewGroup;
        this.viewType = viewType;
        View rowHolder = LayoutInflater.from(viewGroup.getContext()).inflate(getItemLayout(), viewGroup, false);
        return new ViewHolder(rowHolder, itemWidth);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = mData.get(position);
        onBindMViewHolder(holder, item, position);
        if (clickItemEnable) {
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLisener != null) {
                        mLisener.onClickItem(v);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    /**
     * 更新数据
     *
     * @param mDatas
     */
    public void notifyData(List<T> mDatas) {
        this.mData.clear();
        if (mDatas != null && mDatas.size() > 0) {
            this.mData = mDatas;
        }
        notifyDataSetChanged();
    }

    /**
     * 设置item的宽
     *
     * @param width
     */
    public void setItemWidth(int width) {
        if (width > 0) {
            itemWidth = width;
        }
    }

    /**
     * 定义点击事件
     */
    public interface onClickmItemLisener {
        void onClickItem(View v);
    }

    public void setOnItemClickLisener(onClickmItemLisener lisener) {
        mLisener = lisener;
    }

    public void clickItemEnable(boolean enable) {
        clickItemEnable = enable;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //---如何定义
        public ViewHolder(View itemView, int itemWidth) {
            super(itemView);
            if (itemWidth > 0) {
                itemView.getLayoutParams().width = itemWidth;
                itemView.requestLayout();
            }
        }

        public View getView(int id) {
            return itemView.findViewById(id);
        }

        public LinearLayout getLinearLayout(int id) {
            return (LinearLayout) itemView.findViewById(id);
        }

        public RelativeLayout getRelativeLayout(int id) {
            return (RelativeLayout) itemView.findViewById(id);
        }

        public TextView getTextView(int id) {
            return (TextView) itemView.findViewById(id);
        }

        public EditText getEditText(int id) {
            return (EditText) itemView.findViewById(id);
        }

        public ImageView getImageView(int id) {
            return (ImageView) itemView.findViewById(id);
        }

        public ImageButton getImageButton(int id) {
            return (ImageButton) itemView.findViewById(id);
        }

        public Button getButton(int id) {
            return (Button) itemView.findViewById(id);
        }

        public CheckBox getCheckBox(int id) {
            return (CheckBox) itemView.findViewById(id);
        }

        public RadioButton getRadioButton(int id) {
            return (RadioButton) itemView.findViewById(id);
        }

        public GridLayout getGridLayout(int id) {
            return (GridLayout) itemView.findViewById(id);
        }

        public GridView getGridView(int id) {
            return (GridView) itemView.findViewById(id);
        }
    }
}
