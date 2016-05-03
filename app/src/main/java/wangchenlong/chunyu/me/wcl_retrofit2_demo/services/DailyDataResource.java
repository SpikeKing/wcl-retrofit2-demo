package wangchenlong.chunyu.me.wcl_retrofit2_demo.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 日常数据资源
 * <p>
 * Created by wangchenlong on 16/5/3.
 */
public class DailyDataResource {
    private final DailyRequestService mService; // 服务

    public DailyDataResource(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        ChunyuInterceptor chunyuInterceptor = new ChunyuInterceptor(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chunyuInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();

        Gson customGsonInstance = new GsonBuilder().create();

        Retrofit dailyApiAdapter = new Retrofit.Builder()
                .baseUrl(DailyRequestService.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        mService = dailyApiAdapter.create(DailyRequestService.class);
    }

    public Observable<DailyInfo> getDailyInfo() {
        return mService.getDailyInfo();
    }
}
