package com.zxbear.base.activty;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("unchecked")
public class BaseMvpPresenter<V extends BaseMvpView> {
    protected V mView;
    protected Context mContext;
    private CompositeDisposable compositeDisposable;

    public BaseMvpPresenter(V mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }


    public void setContext(Context context) {
        this.mContext = context;
    }


    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        this.mView = null;
        this.mContext = null;
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        return mView != null;
    }



    public void addDisPosable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 销毁异步监听
     */
    public void dispose() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

}