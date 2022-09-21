package com.zxbear.base.activty;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.zxbear.base.R;
import com.zxbear.base.views.MyTitleBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("unchecked")
public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends AppCompatActivity implements BaseMvpView {
    protected T mPresenter;
    private FrameLayout base_content;
    public MyTitleBar myTitle;
    public Bundle mBundle;
    //等待弹出框
    private ProgressDialog mDialog;
    public int TOAST_TIME = 3 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_mvp_layout);
        registerAndBind();
        initContentView();
        initPresenter();
        initTitleBar();
        initView();//初始化组件
        initData();//初始化数据
    }

    /**
     * 设置Activity的视图View
     */
    public abstract int getView();

    /**
     * 设置mPresenter
     *
     * @return
     */
    public abstract T getPresenter();

    /**
     * 设置titleBar
     */
    public abstract void setTitleBar();

    /**
     * 初始化组件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化ContentView
     */
    private void initContentView() {
        base_content = (FrameLayout) findViewById(R.id.base_content);
        base_content.addView(getLayoutInflater().inflate(getView(), null));
        mBundle = this.getIntent().getExtras();
    }

    /**
     * 初始化Presentrt
     */
    private void initPresenter() {
        mPresenter = getPresenter();
    }

    /**
     * 初始化titleBar
     */
    private void initTitleBar() {
        myTitle = (MyTitleBar) findViewById(R.id.title);
        setTitleBar();//初始化TitleBar
        if (myTitle.isSetDefautBackLisener()) {
            myTitle.backClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    /**
     * 注册和绑定
     */
    private void registerAndBind() {
        //IBinds.bind(this);//绑定ButterKife
    }

    @Override
    public void finish() {
        hideLoading();
        mDialog = null;
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 跳转
     *
     * @param context
     * @param mClass
     * @param bundle
     */
    public void openActivity(Activity context, Class mClass, Bundle... bundle) {
        Intent intent = new Intent(context, mClass);
        if (bundle.length > 0) {
            intent.putExtras(bundle[0]);
        }
        startActivity(intent);
    }

    public void openActivity(Class mClass) {
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
    }

    /**
     * 跳转页面获取简单数据
     *
     * @param key
     * @return
     */
    public Object getBundleValue(String key) {
        if (mBundle != null && !mBundle.isEmpty()) {
            return mBundle.get(key);
        } else {
            return "";
        }
    }

    /**
     * 跳转页面获取序列化数据
     *
     * @param key
     * @param mClass
     * @param <T>
     * @return
     */
    public <T> T getSerializableValue(String key, Class<T> mClass) {
        if (mBundle != null && !mBundle.isEmpty()) {
            return mClass.cast(mBundle.getSerializable(key));
        }
        return null;
    }

    @Override
    public void toastMsg(@NonNull Object msg) {
        Toast.makeText(this, msg.toString(), TOAST_TIME).show();
    }

    @Override
    public void showLoading(String... title) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("加载数据中...");
        }
        if (title.length > 0) {
            mDialog.setMessage(title[0]);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void activityFinish() {
        this.finish();
    }
}

