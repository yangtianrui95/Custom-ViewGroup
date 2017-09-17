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
        radius = getMeasuredWidth() / 2;
        Log.d(TAG, "onLayout: radiusFactor " + radiusFactor);
        final int count = getChildCount();
        final double angleOffset = CIRCLE_RADIUS / count;
        double angle;
        View child;
        int left, top, right, bottom;
        int height, width;
        for (int i = 0; i < count; i++) {
            angle = angleOffset * i;
            child = getChildAt(i);
            width = child.getMeasuredWidth();
            height = child.getMeasuredHeight();
            left = clamp(0, (int) (radius * 2), (int) (radius - radius * Math.sin(angle) - width / 2), width);
            top = clamp(0, (int) (radius * 2), (int) (radius - radius * Math.cos(angle) - height / 2), height);
            right = left + width;
            bottom = top + height;
            Log.d(TAG, "onLayout: " + String.format("left: %d, top: %d, right: %d, bottom: %d"
                    , left, top, right, bottom));
            child.layout(left, top, right, bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private static int clampBound(int width, int height) {
        return width > height ? height : width;
    }


    // 固定left和top的值
    private static int clamp(int min, int max, int position, int size) {
        if (position - size < min) {
            return min;
        } else if (position + size > max) {
            return max - size;
        }
        return position;
    }


    public static class LayoutParams extends ViewGroup.LayoutParams {

        public float radius;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray arr = c.obtainStyledAttributes(attrs, R.styleable.CircleLayout);
            radius = arr.getFloat(R.styleable.CircleLayout_layout_radius, 0f);
            arr.recycle();
        }
    }
}
