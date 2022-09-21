package com.zxbear.base.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.zxbear.base.hodel.ViewHodel;
import com.zxbear.base.listener.ItemListener;
import com.zxbear.base.listener.MyViewItem;
import com.zxbear.base.manager.MoreViewItemManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BaseMoreAdapter<T> extends RecyclerView.Adapter<ViewHodel> {

    private MoreViewItemManager<T> itemStyle;//类型（样式）管理器
    private ItemListener<T> itemListener;//条目点击监听
    private List<T> datas; //数据源

    /**
     * 单样式
     *
     * @param datas
     */
    public BaseMoreAdapter(List<T> datas) {
        if (datas == null) datas = new ArrayList();
        this.datas = datas;
        itemStyle = new MoreViewItemManager();
    }

    /**
     * 多样式
     *
     * @param datas
     * @param item
     */
    public BaseMoreAdapter(List<T> datas, MyViewItem<T> item) {
        if (datas == null) datas = new ArrayList();
        this.datas = datas;
        itemStyle = new MoreViewItemManager();
        //将Item加入
        addItemStyle(item);
    }

    /**
     * 判断是否为多样式
     *
     * @return
     */
    private boolean hasMultiStyle() {
        return itemStyle.getItemViewsCount() > 0;
    }

    /**
     * 多样式item
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (hasMultiStyle()) {
            return itemStyle.getItemViewType(datas.get(position), position);
        }
        return super.getItemViewType(position);
    }

    /**
     * 根据布局类型创建不同的MyViewHodel
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHodel onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewItem item = itemStyle.getMyViewItem(viewType);
        int layoutId = item.getItemLayout();
        ViewHodel hodel = ViewHodel.createViewHodel(parent.getContext(), parent, layoutId);
        //点击监听
        if (item.openClick()) setListener(hodel);
        return hodel;
    }


    @Override
    public void onBindViewHolder(ViewHodel hodel, int position) {
        convert(hodel,datas.get(position));
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addItemStyle(MyViewItem<T> item) {
        itemStyle.addStyle(item);
    }

    public void setItemListener(ItemListener<T> itemListener) {
        this.itemListener = itemListener;
    }

    private void setListener(final ViewHodel hodel) {
        //阻塞事件。。。

        //点击监听
        hodel.getmConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    int position = hodel.getAdapterPosition();
                    itemListener.onItemClick(view, datas.get(position), position);
                }
            }
        });
        //长按监听
        hodel.getmConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemListener != null) {
                    int position = hodel.getAdapterPosition();
                    itemListener.onItemLongClick(view, datas.get(position), position);
                }
                return false;
            }
        });
    }


    private void convert(ViewHodel hodel, T entity) {
        itemStyle.convert(hodel,entity,hodel.getAdapterPosition());
    }
}
