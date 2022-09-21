package com.zxbear.base.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.Toast;

import com.zxbear.base.tools.ActivityCollector;
import com.zxbear.base.views.MyTitleBar;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("unchecked")
public abstract class BaseActivity extends AppCompatActivity {
    public MyTitleBar myTitle;
    public Bundle mBundle;
    public int TOAST_TIME = 3 * 1000;
    private static BaseActivity baseActivity;
    public static final int FINISH_ACTIVITY = 1000 * 60 * 60;//activity会话超时时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
        ActivityCollector.addActivity(this);
        registerAndBind();
        setTitle();//初始化TitleBar
        mBundle = this.getIntent().getExtras();
        initView();//初始化组件
        initData();//初始化数据
    }

    /**
     * 设置Activity的视图View
     */
    public abstract int getView();

    /**
     * 设置titleBar
     */
    public abstract void setTitle();

    /**
     * 初始化组件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 注册和绑定
     */
    private void registerAndBind() {
      //  IBinds.bind(this);//绑定ButterKife
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销eventBus
        ActivityCollector.removeActivity(this);
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


    /**
     * toat消息
     *
     * @param msg
     * @param deplay
     */
    public void ToastMsg(Object msg, int... deplay) {
        if (deplay != null && deplay.length > 0) {
            TOAST_TIME = deplay[0];
        }
        if (msg != null) {
            Toast.makeText(this, msg.toString(), TOAST_TIME).show();
        }
    }


    /* 以下添加MIP回话超时机制 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: { // 手指下来的时候,取消之前绑定的Runnable
                handler.removeCallbacks(runnable);
                break;
            }
            case MotionEvent.ACTION_UP: { // 手指离开屏幕，发送延迟消息 ，5分钟后执行
                handler.postDelayed(runnable, FINISH_ACTIVITY);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseActivity = this;
        handler.postDelayed(runnable, FINISH_ACTIVITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        baseActivity = null;
        handler.removeCallbacks(runnable);
    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }
    };

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        //Platform.getCurrent().logout(baseActivity);
                        ActivityCollector.finishAll();
                        startActivity(new Intent(baseActivity, Class.forName("com.sh.ydzy.login.LoginActivity")));
                        Toast.makeText(getApplicationContext(), (FINISH_ACTIVITY / (1000 * 60)) + "分钟内无操作用户被注销", Toast.LENGTH_SHORT).show();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}

