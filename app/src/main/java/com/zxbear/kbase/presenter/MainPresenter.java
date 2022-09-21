package com.zxbear.kbase.presenter;

import android.content.Context;
import com.zxbear.base.activty.BaseMvpPresenter;
import com.zxbear.kbase.constract.MainConstract;

public class MainPresenter extends BaseMvpPresenter<MainConstract.View> implements MainConstract.Presenter {
  public MainPresenter(MainConstract.View mView, Context mContext) {
    super(mView, mContext);
  }
}
