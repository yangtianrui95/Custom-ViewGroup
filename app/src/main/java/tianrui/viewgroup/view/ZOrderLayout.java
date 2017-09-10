package tianrui.viewgroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tianrui.viewgroup.R;

/**
 * Created by yangtianrui on 17-9-6.
 * Zorder framelayout
 */

public class ZOrderLayout extends FrameLayout {


    private List<Pair<View, Integer>> list = new ArrayList<>();


    public ZOrderLayout(@NonNull Context context) {
        this(context, null);
    }

    public ZOrderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZOrderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return indexOfChild(list.get(i).first);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initialZOrder();
    }



    private void initialZOrder() {
        final int childCount = getChildCount();
        View view;
        ZOrderLayout.LayoutParams params;
        for (int i = 0; i < childCount; i++) {
            view = getChildAt(i);
            params = (LayoutParams) view.getLayoutParams();

            Pair<View, Integer> pair = new Pair<>(view, params.zOrder);
            list.add(pair);
        }

        Collections.sort(list, new Comparator<Pair<View, Integer>>() {
            @Override
            public int compare(Pair<View, Integer> o1, Pair<View, Integer> o2) {
                return o1.second - o2.second;
            }
        });
    }

    /**
     * 在解析xml时,会解析每个跟布局的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        public final static int DEFAULT_ZORDER = 1;

        public int zOrder;

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ZOrderLayout);
            zOrder = a.getInt(R.styleable.ZOrderLayout_layout_zorder, DEFAULT_ZORDER);
            a.recycle();
        }
    }
}
