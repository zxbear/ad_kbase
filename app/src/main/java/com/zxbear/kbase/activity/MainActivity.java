package com.zxbear.kbase.activity;

import com.zxbear.annotation.MvpAct;
import com.zxbear.annotation.PARAS;
import com.zxbear.base.activty.BaseMvpActivity;
import com.zxbear.kbase.R;
import com.zxbear.kbase.constract.MainConstract;
import com.zxbear.kbase.presenter.MainPresenter;
import java.lang.Override;

@MvpAct(
    onCreate = PARAS.CREATED,
    path = "com.zxbear.kbase"
)
public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainConstract.View {
  @Override
  public int getView() {
    return R.layout.activity_main_layout;
  }

  @Override
  public MainPresenter getPresenter() {
    return new MainPresenter(this,this);
  }

  @Override
  public void setTitleBar() {
  }

  @Override
  public void initView() {
  }

  @Override
  public void initData() {
  }
}
