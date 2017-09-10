# 自定义ViewGroup合集

## Android Z轴布局
> 如果需要在布局中创造一个层叠的概念,那么使用Android系统中的ViewGroup是不够的,但是可以通过改变ViewGroup的绘制顺序实现

API21以后有`android:elevation`属性,可以在代码中直接设置, 但是兼容低版本的话就需要自定义ViewGroup了
![这里写图片描述](http://img.blog.csdn.net/20170910220242375?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveTg3NDk2MTUyNA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 继承自FrameLayout
FrameLayout已经帮我们实现了子View的measure和layout过程,我们只需在它的基础上改变绘制顺序即可


### 自定义LayoutParams
layoutParams的作用是向父布局请求布局参数(MeasureSpec),这个参数会在View inflate时添加到布局中,我们如果使用LayoutParams将会得到很大的方便

```
// 这里继承FrameLayout的LayoutParams即可
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
```

我们自定义个Attribute,那么就可以在XML中进行使用了
```
<declare-styleable name="ZOrderLayout">
    <attr name="layout_zorder" format="integer"/>
</declare-styleable>
```
这样我们的View就可以这么使用
```
<!--layout_zorder 表示该View在第1层-->
<tianrui.viewgroup.MyTextView
    android:text="0"
    android:layout_width="50dp"
    android:layout_height="50dp"
    android:background="@android:color/holo_red_light"
    app:layout_zorder="1"/>

<!--layout_zorder=2 表示该View在第2层-->
<tianrui.viewgroup.MyTextView
    android:text="1"
    android:layout_width="50dp"
    android:layout_height="50dp"
    android:layout_marginLeft="20dp"
    android:background="@android:color/holo_blue_light"
    app:layout_zorder="2"/>
```

同时需要重写ViewGroup的`generateLayoutParams()`,让它生成我们的LayoutParams

###　初始化绘制顺序

在所有的子View加载完成后初始化需要绘制的顺序(根据我们的ZorderLayoutParams)
```
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
    
    // 根据Zorder属性,进行排序
    Collections.sort(list, new Comparator<Pair<View, Integer>>() {
        @Override
        public int compare(Pair<View, Integer> o1, Pair<View, Integer> o2) {
            return o1.second - o2.second;
        }
    });
}
```

获取所有的子View,然后根据他们的ZOrder进行排序,onFinishInflate()会在装载完所有的子View后进行回调

### 改变View的绘制顺序
这里使用排好序的View绘制顺序就可以了, 记得调用setChildrenDrawingOrderEnabled(true);
```
@Override
protected int getChildDrawingOrder(int childCount, int i) {
    return indexOfChild(list.get(i).first);
}
```

### Demo演示

```
<?xml version="1.0" encoding="utf-8"?>
<tianrui.viewgroup.view.ZOrderLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <tianrui.viewgroup.MyTextView
        android:text="0"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:background="@android:color/holo_red_light"
        app:layout_zorder="1"/>


    <tianrui.viewgroup.MyTextView
        android:text="1"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginLeft="20dp"
        android:background="@android:color/holo_blue_light"
        app:layout_zorder="2"/>


    <tianrui.viewgroup.MyTextView
        android:text="2"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginLeft="40dp"
        android:background="@android:color/holo_orange_light"
        app:layout_zorder="3"/>


    <tianrui.viewgroup.MyTextView
        android:text="3"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginLeft="60dp"
        android:background="@android:color/holo_green_light"
        app:layout_zorder="2"/>


    <tianrui.viewgroup.MyTextView
        android:text="4"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginLeft="80dp"
        android:background="@android:color/holo_purple"
        app:layout_zorder="1"/>

</tianrui.viewgroup.view.ZOrderLayout>
```

可以看出这个布局是中间的zorder最高,表示中间的会压在两边的上边,而最左(右)的绘制层级(zorder)为1, 表示会绘制在最下面
![](http://img.blog.csdn.net/20170910220242375?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveTg3NDk2MTUyNA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

