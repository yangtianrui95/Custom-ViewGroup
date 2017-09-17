package tianrui.viewgroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// only test
public class TestActivity extends AppCompatActivity {

    private TextView mTvText;

    private static final String TAG = "ytr";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_test);
        mTvText = (TextView) findViewById(R.id.tv_text);
        // measure(0, 0) 是将View的内容进行Measure, 获取的高度与View LayoutParams中的高度不同
        measureView(mTvText);
        Log.d(TAG, "onCreate: tvText " + mTvText.getMeasuredWidth());
        mTvText.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: tvWidth " + mTvText.getWidth() + " tVMeasureWidth " + mTvText.getMeasuredWidth());
            }
        });
    }


    //http://blog.csdn.net/wood_water_peng/article/details/47982971
    private static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int lpHeight = p.height;
        int lpWidth = p.width;
        int childHeightSpec;
        int childWidthSpec;
        if (lpHeight > 0) {   //如果Height是一个定值，那么我们测量的时候就使用这个定值
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {   //否则，我们将mode设置为不指定，size设置为0
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        if (lpWidth > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
}
