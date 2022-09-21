package com.zxbear.base.views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxbear.base.R;
import com.zxbear.base.hodel.MyViewHodel;


/**
 * @create_time: 2021/3/4 17:04
 * @Description: MyTitleBar
 * @author: zz
 * @version: v1.0.1
 */

public class MyTitleBar extends BaseView {
    private RelativeLayout rl_mtitle;//title整体的layout
    private boolean isHavebackLisener = false;//是否设置的自定义返回事件
    private LinearLayout back;//title的返回布局layout
    private TextView tv_title;//标题的title
    private boolean isHaveSelfRightView = false;//标题右侧是否为自定义布局
    private FrameLayout rightView;//标题右侧的layout
    private MyViewHodel rightViewHolder;///标题右侧的view封装类
    private ImageView imgLeft;

    public MyTitleBar(Context context) {
        super(context);
    }

    public MyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getView() {
        return R.layout.base_title_layout;
    }

    @Override
    public void initView() {
        rl_mtitle = findViewById1(R.id.rl_mtitle);
        back = findViewById1(R.id.ll_back);
        tv_title = findViewById1(R.id.tv_title);
        rightView = findViewById1(R.id.fl_title_right);
        imgLeft = findViewById(R.id.imgLeft);
    }

    @Override
    public int[] getStyleableIds() {
        return R.styleable.TitleBar;
    }

    @Override
    public void initData() {
        String Title = getStyleableString(R.styleable.TitleBar_title);
        boolean backEnable = getStyleableBoolean(R.styleable.TitleBar_backEnabke, true);
        //获取背景
        int resourceId = getStyleableResourceId(R.styleable.TitleBar_titleBg, R.color.base_title_bg_nomor);
        rl_mtitle.setBackgroundResource(resourceId);
        //赋值title
        setTitle(Title);
        setBackEnable(backEnable);
    }


    /**
     * 设置标题
     *
     * @param Title
     */
    public void setTitle(String Title) {
        if (!TextUtils.isEmpty(Title)) {
            tv_title.setText(Title);
        } else {
            tv_title.setText("标 题");
        }
    }


    public void setTitleSize(int size) {
        tv_title.setTextSize(size);
    }


    public void setTitleBacgraound(int resourceId) {
        if (rl_mtitle != null)
            rl_mtitle.setBackgroundResource(resourceId);
    }

    /**
     * 设置返回按钮
     *
     * @param Enable
     */
    public void setBackEnable(boolean Enable) {
        if (Enable) {
            back.setVisibility(VISIBLE);
        } else {
            back.setVisibility(GONE);
        }
    }

    /**
     * 设置左边图片
     *
     * @param id
     */
    public void setLeftImg(int id) {
        imgLeft.setImageResource(id);
    }

    public boolean isSetDefautBackLisener() {
        return (back.getVisibility() == VISIBLE && !isHavebackLisener);
    }

    /**
     * 设置右侧按钮
     *
     * @param Enable    是否显示title右侧按钮
     * @param resources -1为默认布局，否则自定义布局
     */
    public void setRightEnable(boolean Enable, int resources) {
        if (!Enable) {
            rightView.setVisibility(GONE);
            return;
        } else {
            rightViewHolder = null;
            rightView.setVisibility(VISIBLE);
        }
        //是否为默认右侧布局
        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.base_title_right_layout, null);
        if (resources != -1) {
            isHaveSelfRightView = true;
            view = ((Activity) mContext).getLayoutInflater().inflate(resources, null);
        }
        rightView.addView(view);
    }

    /**
     * 设置默认右侧的image样式
     */
    public void setDefautRightBg(int resources) {
        if (rightView.getVisibility() == VISIBLE && !isHaveSelfRightView) {
            getRightViewHolder().getImageView(R.id.iv_right).setImageResource(resources);
        }
    }

    /**
     * 获取标题右侧布局view
     *
     * @return
     */
    public MyViewHodel getRightViewHolder() {
        if (rightViewHolder == null) {
            rightViewHolder = new MyViewHodel(rightView);
        }
        return rightViewHolder;
    }

    /**
     * 监听返回事件
     *
     * @param bacListener
     */
    public void backClickListener(OnClickListener bacListener) {
        isHavebackLisener = true;
        back.setOnClickListener(bacListener);
    }

    /**
     * 设置默认右侧布局的点击事件
     *
     * @param rListener
     */
    public void setRightDefautClickListener(OnClickListener rListener) {
        if (rightView.getVisibility() == VISIBLE && !isHaveSelfRightView) {
            getRightViewHolder().getImageView(R.id.iv_right).setOnClickListener(rListener);
        }
    }


}
