package com.zxbear.base.manager;




import com.zxbear.base.hodel.ViewHodel;
import com.zxbear.base.listener.MyViewItem;

import androidx.collection.SparseArrayCompat;

/**
 * 多类型、样式item样式管理器
 *
 * @param <T>
 */
public class MoreViewItemManager<T> {

    //key = viewType , Value = MyViewItem
    private SparseArrayCompat<MyViewItem<T>> styles = new SparseArrayCompat<>();

    /**
     * 获取样式总数
     *
     * @return
     */
    public int getItemViewsCount() {
        return styles.size();
    }


    /**
     * 新增样式
     *
     * @param item
     */
    public void addStyle(MyViewItem<T> item) {
        if (item != null) {
            //每次加入放入末尾
            styles.put(styles.size(), item);
        }
    }

    /**
     * 根据数据源和位置返回item类型的ViewType
     *
     * @param entity
     * @param position
     * @return
     */
    public int getItemViewType(T entity, int position) {
        for (int i = styles.size() - 1; i >= 0; i--) {
            MyViewItem<T> item = styles.valueAt(i);
            if (item.isItemView(entity, position)) {
                return styles.keyAt(i);
            }
        }
        throw new IllegalArgumentException("位置：" + position + "该item无MyViewItem类型");
    }


    /**
     * 根据viewType获取 MyViewItem 对象
     *
     * @param viewType
     * @return
     */
    public MyViewItem getMyViewItem(int viewType) {
        return styles.get(viewType);
    }

    /**
     * 视图与数据绑定显示
     * @param hodel
     * @param entity
     * @param position
     */
    public void convert(ViewHodel hodel, T entity, int position) {
        for (int i = 0; i < styles.size(); i++) {
            MyViewItem<T> item = styles.valueAt(i);
            if (item.isItemView(entity,position)){
                item.convert(hodel,entity,position);
                return;
            }
        }
        throw new IllegalArgumentException("位置：" + position + "该item无MyViewItem类型");
    }
}

