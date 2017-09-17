package tianrui.viewgroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import tianrui.viewgroup.R;

/**
 * Created by yangtianrui on 17-9-14.
 * 圆形布局
 */

public class CircleLayout extends ViewGroup {

    private static final String TAG = "ytr";
    public static double CIRCLE_RADIUS = 2 * Math.PI;

    private float radius;
    private float radiusFactor;

    private int childMaxWidth;
    private int childMaxHeight;

    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleLayout);
        radiusFactor = array.getFloat(R.styleable.CircleLayout_radius_factor, 1f);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = getSuggestedMinimumWidth();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = getSuggestedMinimumHeight();
        }
        // 保证宽高相等,才是圆形布局
        width = clampBound(widthSize, heightSize);
        height = clampBound(widthSize, heightSize);
        setMeasuredDimension(width, height);

        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        radius = (r - l) / 2 * radiusFactor * getScaleFactor();
        final int count = getChildCount();
        final double angleOffset = CIRCLE_RADIUS / count;
        final int centerX = (r - l) / 2;
        final int centerY = (b - t) / 2;
        double angle;
        View child;
        int left, top, right, bottom;
        int height, width;
        for (int i = 0; i < count; i++) {
            angle = angleOffset * i;
            child = getChildAt(i);
            width = child.getMeasuredWidth();
            height = child.getMeasuredHeight();

            right = (int) (centerX + (radius * Math.sin(angle)) + (width / 2));
            left = right - width;
            bottom = (int) (centerY + (radius * Math.cos(angle)) + (height / 2));
            top = bottom - height;
            Log.d(TAG, "onLayout: " + String.format("left: %d, top: %d, right: %d, bottom: %d"
                    , left, top, right, bottom));
            child.layout(left, top, right, bottom);
        }
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        childMaxWidth = Math.max(childMaxWidth, child.getMeasuredWidth());
        childMaxHeight = Math.max(childMaxHeight, child.getMeasuredHeight());
    }

    private float getScaleFactor() {
        final int size = Math.max(childMaxWidth, childMaxHeight);
        return 1 - (float) size / getMeasuredWidth();
    }


    private static int clampBound(int width, int height) {
        return width > height ? height : width;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


}
