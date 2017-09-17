package tianrui.viewgroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickZorderLayout(View view) {
        startActivity(new Intent(this, ZOrderActivity.class));
    }

    public void onClickFrameLayout(View view) {
        startActivity(new Intent(this, CircleLayoutActivity.class));
    }
}
