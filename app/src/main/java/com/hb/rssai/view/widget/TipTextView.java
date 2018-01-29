package com.hb.rssai.view.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * 顶部小提示
 * Created by Administrator on 2018/1/29.
 */

public class TipTextView extends android.support.v7.widget.AppCompatTextView {
    private int titleHeight = 100;
    private static final int START_TIME = 400;
    private static final int END_TIME = 400;
    private static final int SHOW_TIME = 1000;

    public TipTextView(Context context) {
        super(context);
    }

    public TipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TipTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public void showTips() {
        setVisibility(VISIBLE);
        //向下移动动画
        TranslateAnimation downTranslateAnimation = new TranslateAnimation(0, 0, 0, titleHeight);
        downTranslateAnimation.setDuration(START_TIME);
        downTranslateAnimation.setFillAfter(true);
        //开始动画
        startAnimation(downTranslateAnimation);

        //监听
        downTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {//向下移动结束，开始上下移动
                topTranslateAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void topTranslateAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //向上移动动画
                TranslateAnimation topTranslateAnimation = new TranslateAnimation(0, 0, titleHeight, 0);
                topTranslateAnimation.setDuration(END_TIME);
                topTranslateAnimation.setFillAfter(true);

                //改变透明度
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(END_TIME);

                //两个动画添加到动画集合中
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(topTranslateAnimation);
                animationSet.addAnimation(alphaAnimation);

                //开启动画
                startAnimation(animationSet);

                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }, SHOW_TIME);
    }
}
