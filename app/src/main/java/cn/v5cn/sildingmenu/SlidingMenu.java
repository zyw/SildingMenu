package cn.v5cn.sildingmenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.reflect.Type;

/**
 * Created by ZYW on 2015/2/4.
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    //屏幕宽度
    private int mScreenWidth;

    //菜单距离右边的距离
    private int mMenuRightPadding;

    //菜单的宽度
    private int mMenuWidth;
    private int mHalfMenuWidth;

    //防止重复渲染
    private boolean once;

    private boolean isOpen;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(outMetrics);

        mScreenWidth = outMetrics.widthPixels;
        //把dp转换成功px
        mMenuRightPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(!once){
            mWapper = (LinearLayout)getChildAt(0);
            mMenu = (ViewGroup)mWapper.getChildAt(0);
            mContent = (ViewGroup)mWapper.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth =  mMenuWidth / 2;

            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed){
            this.scrollTo(mMenuWidth,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if(scrollX > mHalfMenuWidth){
                    this.smoothScrollTo(mMenuWidth,0);
                    isOpen = false;
                }else{
                    this.smoothScrollTo(0,0);
                    isOpen = true;
                }
                return true;
        }

        return super.onTouchEvent(ev);
    }

    public void openMenu(){
        if(isOpen) return;
        this.smoothScrollTo(0,0);
        isOpen = true;
    }

    public void closeMenu(){
        if(!isOpen) return;

        this.smoothScrollTo(mMenuWidth,0);
        isOpen = false;
    }

    public void toggleMenu(){
        if(isOpen)
            closeMenu();
        else
            openMenu();
    }
}
