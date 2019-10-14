package com.dizan.mlicxapp.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 该类是实现图片轮播的核心类
 */
public class ImageBannerViewGroup extends ViewGroup {

    private int children;  // ViewGroup子视图总个数
    private int childwidth;  // ViewGroup子视图宽度
    private int childheight;  // ViewGroup子视图高度

    private int x; // 此时的x的值，代表的是第一次按下的位置横坐标，每一次移动过程中，移动之前的位置横坐标
    private int index = 0; //代表的是每张图片的索引


    public ImageBannerViewGroup(Context context) {
        super(context);
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 我们在自定义viewGroup中，我们必须要实现的方法有：测量--> 布局-->绘制
     *
     * 因为我们是自定义ViewGroup容器，针对于容器的绘制，
     * 其实就是子控件的绘制过程，我们只需要调用系统自带的绘制即可，
     * 也就说，对于ViewGroup绘制过程，不需要重写该方法，调用系统自带的即可。
     *
     * 测量：onMeasure()
     * 布局：onLayout()
     * 绘制：
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 由于我们要实现的是一个ViewGroup的容器，
         * 我们就应该需要知道该容器中的所有子视图，
         * 我们想要测量我们的ViewGroup的宽度和高度，
         * 我们就必须先要去测量子视图的宽度和高度之和，
         * 才能知道我们的ViewGroup的宽度和高度是多少
         *
         */

        // 1、求出子视图的个数
        children = getChildCount();  //获取子视图的个数
        if(0 == children) {
            setMeasuredDimension(0,0);
        } else {
            // 2、测量子视图的宽度和高度
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            // 此时以第一个子视图为基准，也就是我们的ViewGroup的高度就是我们第一个子视图的高度，宽度就是我们第一个子视图的宽度 * 子视图的个数
            View view = getChildAt(0); // 此时第一个视图绝对存在
            childwidth = view.getMeasuredWidth();
            // 3、根据子视图的宽度和高度，求出该ViewGroup的宽度和高度
            childheight = view.getMeasuredHeight();
            int width = view.getMeasuredWidth() * children;  // 宽度是所有子视图的宽度总和
            setMeasuredDimension(width,childheight);
        }
    }

    /**
     * 事件的传递过程的调用方法：我们需要调用 容器的拦截方法 onInterceptTouchEvent
     * 针对于该方法，我们可以理解为，如果说：
     * 该方法的返回值为true的时候，那么我们自定义的ViewGroup容器就会处理此次拦截事件
     * 该方法的返回值为false的时候，那么我们自定义的ViewGroup容器将不会接受此次事件的处理过程，将会继续向下传递该事件
     * 针对于自定义的ViewGroup，我们当然是希望我们的ViewGroup容器处理接受事件，那么我们的返回值就是true,
     * 若为true,真正处理该事件的方法是 onTouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 用两种方式来实现轮播图的手动轮播
     * 1、利用scrollTo、scrollBy 完成轮播图的手动轮播
     * 2、利用Scroller对象 完成轮播图的手动轮播
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 表示的是用户按下的一瞬间
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE: // 表示的是用户按下之后在屏幕移动的过程
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance,0);
                x = moveX;
                break;
            case MotionEvent.ACTION_UP: //表示的是用户抬起的一瞬间
                int scrollX = getScrollX();
                index = (scrollX + childwidth / 2) / childwidth;
                if(index < 0) {  //说明此时已经滑动到了最左边的第一张图片
                    index = 0;
                } else if(index > children - 1) {  //说明此时已经滑动到了最右最后一张图片
                    index = children - 1;
                }
                scrollTo(index * childwidth, 0);
                break;
                default:
                    break;
        }
        return true;  // 返回true，目的：告诉该ViewGroup容器的父View，我们已经处理好了该事件
    }

    /**
     * 继承ViewGroup必须要实现布局onLayout方法
     * @param change 代表当我们的ViewGroup布局位置发生改变的时为true,没发生改变时为false
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        if(change) {
            int leftMargin = 0;
            for(int i = 0;i < children; i++) {
                View view = getChildAt(i);
                view.layout(leftMargin, 0, leftMargin + childwidth, childheight);
                leftMargin += childwidth;
            }
        }
    }
}
