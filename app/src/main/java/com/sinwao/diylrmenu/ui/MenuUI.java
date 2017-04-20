package com.sinwao.diylrmenu.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 描述：自定义左右滑动菜单布局
 * --------个人简介-------
 * QQ:710760186
 * Email：yibin479@yahoo.com
 * Created by 阿酷 on 2017/4/20.
 */

public class MenuUI extends RelativeLayout {

    private static final int TEST_DIS = 20;
    private Context mContext;

    private FrameLayout leftMenu;
    private FrameLayout middleMenu;
    private FrameLayout rightMenu;
    private Scroller mScroller;

    public static final int LEFT_ID = 1;
    public static final int MIDDLE_ID = 2;
    public static final int RIGHT_ID = 3;
    private FrameLayout layoutMask;

    public MenuUI(Context context) {
        super(context);
        initView(context);
    }

    public MenuUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化视图显示
     * @param context
     */
    private void initView(Context context) {
        this.mContext = context;
        /**
         * 实例化三个填充的view
         */
        leftMenu = new FrameLayout(context);
        middleMenu = new FrameLayout(context);
        rightMenu = new FrameLayout(context);
        layoutMask = new FrameLayout(context);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        leftMenu.setId(LEFT_ID);
        middleMenu.setId(MIDDLE_ID);
        rightMenu.setId(RIGHT_ID);
        leftMenu.setBackgroundColor(Color.GRAY);
        middleMenu.setBackgroundColor(Color.BLUE);
        rightMenu.setBackgroundColor(Color.CYAN);
        layoutMask.setBackgroundColor(0x88000000);
        addView(leftMenu);
        addView(middleMenu);
        addView(rightMenu);
        addView(layoutMask);
        layoutMask.setAlpha(0);
    }

    public float onLayoutMask(){
        System.out.println("透明度：" + layoutMask.getAlpha());
        return layoutMask.getAlpha();
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        onLayoutMask();
        int curX = Math.abs(getScrollX());
        float scale = curX / (float)leftMenu.getMeasuredWidth();
        layoutMask.setAlpha(scale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 测量每个布局的大小
         */
        middleMenu.measure(widthMeasureSpec,heightMeasureSpec);
        layoutMask.measure(widthMeasureSpec,heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        int temWidthMeasure = MeasureSpec.makeMeasureSpec((int) (realWidth * 0.6),MeasureSpec.EXACTLY);
        leftMenu.measure(temWidthMeasure,heightMeasureSpec);
        rightMenu.measure(temWidthMeasure,heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /**
         * 将三个view填充进布局
         */
        middleMenu.layout(l,t,r,b);
        layoutMask.layout(l,t,r,b);
        leftMenu.layout(l - leftMenu.getMeasuredWidth(),t,r,b);
        rightMenu.layout(l + middleMenu.getMeasuredWidth(),t,l + middleMenu.getMeasuredWidth() + rightMenu.getMeasuredWidth(),b);
    }

    private boolean isTestComplete = false;
    private boolean isLREvent = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (!isTestComplete) {
            getEventType(ev);
            return true;
        }
        if (isLREvent) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    int curScrollX = getScrollX();
                    int dis_x = (int) (ev.getX() - point.x);
                    int expectX = - dis_x + curScrollX;
                    int finalX = 0;
                    if (expectX < 0) {
                        finalX = Math.max(expectX,-leftMenu.getMeasuredWidth());
                    } else {
                        finalX = Math.min(expectX,rightMenu.getMeasuredWidth());
                    }
                    scrollTo(finalX,0);
                    point.x = (int) ev.getX();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    curScrollX = getScrollX();
                    if (Math.abs(curScrollX) > leftMenu.getMeasuredWidth() >> 1) {
                        if (curScrollX < 0) {
                            mScroller.startScroll(curScrollX,0,-leftMenu.getMeasuredWidth() - curScrollX,0);
                        } else {
                            mScroller.startScroll(curScrollX,0,leftMenu.getMeasuredWidth() - curScrollX,0);
                        }
                    } else {
                            mScroller.startScroll(curScrollX,0,-curScrollX,0);
                    }
                    invalidate();
                    isLREvent = false;
                    isTestComplete = false;
                    break;
            }
        }else {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                    isLREvent = false;
                    isTestComplete = false;
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        int tempX = mScroller.getCurrX();
        scrollTo(tempX,0);
    }

    private Point point = new Point();

    private void getEventType(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs((int) ev.getX() - point.x);
                int dy = Math.abs((int) ev.getY() - point.y);
                if (dx >= TEST_DIS && dx > dy) {
                    //左右滑动
                    isLREvent = true;
                    isTestComplete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                } else if (dy >= TEST_DIS && dy > dx) {
                    //上下滑动
                    isLREvent = false;
                    isTestComplete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                super.dispatchTouchEvent(ev);
                isLREvent = false;
                isTestComplete = false;
                break;
        }
    }
}
