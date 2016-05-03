package wangchenlong.chunyu.me.wcl_retrofit2_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wangchenlong.chunyu.me.wcl_retrofit2_demo.services.DailyDataResource;

public class MainActivity extends AppCompatActivity {
    private DailyDataResource mDailyDataResource;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDailyDataResource = new DailyDataResource(getApplicationContext());
        loadData();
    }

    private void loadData() {
        mSubscription = mDailyDataResource.getDailyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dailyInfo -> {
                    Log.e("DEBUG-WCL: ", dailyInfo.new_version);
                    Toast.makeText(getApplicationContext(),
                            "新版本: " + dailyInfo.new_version, Toast.LENGTH_SHORT).show();
                });
    }

    @Override protected void onDestroy() {
        mSubscription.unsubscribe();
        super.onDestroy();
    }
}
