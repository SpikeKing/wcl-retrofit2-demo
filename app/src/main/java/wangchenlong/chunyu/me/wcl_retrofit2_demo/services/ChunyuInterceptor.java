package wangchenlong.chunyu.me.wcl_retrofit2_demo.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import wangchenlong.chunyu.me.wcl_retrofit2_demo.BuildConfig;
import wangchenlong.chunyu.me.wcl_retrofit2_demo.consts.PrefsConsts;

/**
 * 自定义拦截器
 * <p>
 * 目标:
 * http://steps.summer2.chunyu.me/api/daily_request/
 * ?app=7&platform=android&systemVer=5.0.1
 * &version=2.0.3&app_ver=2.0.3
 * &imei=869511022668418&device_id=869511022668418
 * &mac=98%3Ae7%3Af5%3Aec%3A24%3A23&secureId=aaeec93530106e5f'
 * &installId=1461750494597&phoneType=HUAWEI+GRA-TL00_by_HUAWEI
 * &vendor=ziyou&u=NA
 * <p>
 * 结果:
 * http://steps.summer2.chunyu.me/api/daily_request/
 * ?app=7&platform=android&systemVer=5.0.2
 * &version=1.0&app_ver=1.0
 * &imei=460078115450528&device_id=869677020412198
 * &mac=10%253A2a%253Ab3%253A65%253A6a%253A1f
 * &secureId=234221834dfb16cb
 * &installId=1462264409240&phoneType=Redmi%20Note%203_by_Xiaomi
 * &vendor=ziyou&u=NA
 * <p>
 * Created by wangchenlong on 16/5/3.
 */
public class ChunyuInterceptor implements Interceptor {
    private static final String APP = "app";
    private static final String PLATFORM = "platform";
    private static final String SYSTEM_VER = "systemVer";
    private static final String VERSION = "version";
    private static final String APP_VER = "app_ver";
    private static final String IMEI = "imei";
    private static final String DEVICE_ID = "device_id";
    private static final String MAC = "mac";
    private static final String SECURE_ID = "secureId";
    private static final String INSTALL_ID = "installId";
    private static final String PHONE_TYPE = "phoneType";
    private static final String VENDOR = "vendor";
    private static final String U = "u";

    private static final String APP_VALUE = "7";
    private static final String PLATFORM_VALUE = "android";

    private final Context mContext;
    private final SharedPreferences mPrefs;
    private final TelephonyManager mTelephonyManager;
    private final WifiManager mWifiManager;


    public ChunyuInterceptor(Context context) {
        mContext = context.getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mTelephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        mWifiManager = (WifiManager) (mContext.getSystemService(Context.WIFI_SERVICE));
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter(APP, APP_VALUE)
                .addQueryParameter(PLATFORM, PLATFORM_VALUE)
                .addQueryParameter(SYSTEM_VER, Build.VERSION.RELEASE)
                .addQueryParameter(VERSION, BuildConfig.VERSION_NAME)
                .addQueryParameter(APP_VER, BuildConfig.VERSION_NAME)
                .addQueryParameter(IMEI, getImei())
                .addQueryParameter(DEVICE_ID, getDeviceId())
                .addQueryParameter(MAC, getMac())
                .addQueryParameter(SECURE_ID, getSecureId())
                .addQueryParameter(INSTALL_ID, getInstallId())
                .addQueryParameter(PHONE_TYPE, getDeviceInfo())
                .addQueryParameter(VENDOR, "ziyou")
                .addQueryParameter(U, "NA");

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(builder.build())
                .build();

        return chain.proceed(newRequest);
    }

    // 获取设备ID, 需要权限READ_PHONE_STATE
    private String getDeviceId() {
        if (mPrefs.contains(PrefsConsts.PHONE_INFO_DEVICE_ID_PREFS)) {
            return mPrefs.getString(PrefsConsts.PHONE_INFO_DEVICE_ID_PREFS, "");
        } else {
            String deviceId = null;
            try {
                deviceId = mTelephonyManager.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = "";
            }
            mPrefs.edit().putString(PrefsConsts.PHONE_INFO_DEVICE_ID_PREFS, deviceId)
                    .apply();
            return mPrefs.getString(PrefsConsts.PHONE_INFO_DEVICE_ID_PREFS, "");
        }
    }

    // 获取手机的IMEI
    private String getImei() {
        if (mPrefs.contains(PrefsConsts.PHONE_INFO_IMEI_PREFS)) {
            return mPrefs.getString(PrefsConsts.PHONE_INFO_IMEI_PREFS, "");
        } else {
            String imei = null;
            try {
                imei = mTelephonyManager.getSubscriberId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(imei)) {
                imei = "";
            }
            mPrefs.edit().putString(PrefsConsts.PHONE_INFO_IMEI_PREFS, imei)
                    .apply();
            return mPrefs.getString(PrefsConsts.PHONE_INFO_IMEI_PREFS, "");
        }
    }

    // 获取Mac地址, 需要权限ACCESS_WIFI_STATE
    private String getMac() {
        if (mPrefs.contains(PrefsConsts.PHONE_INFO_MAC_ADDRESS_PREFS)) {
            return mPrefs.getString(PrefsConsts.PHONE_INFO_MAC_ADDRESS_PREFS, "");
        } else {
            String mac = null;
            try {
                mac = mWifiManager.getConnectionInfo().getMacAddress();
                //noinspection deprecation
                mac = URLEncoder.encode(mac);
            } catch (Error e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(mac)) {
                mac = "";
            }
            mPrefs.edit().putString(PrefsConsts.PHONE_INFO_MAC_ADDRESS_PREFS, mac)
                    .apply();
            return mPrefs.getString(PrefsConsts.PHONE_INFO_MAC_ADDRESS_PREFS, "");
        }
    }

    // 获取安全ID
    private String getSecureId() {
        if (mPrefs.contains(PrefsConsts.PHONE_INFO_SECURE_ID_PREFS)) {
            return mPrefs.getString(PrefsConsts.PHONE_INFO_SECURE_ID_PREFS, "");
        } else {
            String secure = null;
            try {
                secure = Settings.Secure.getString(
                        mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(secure)) {
                secure = "";
            }
            mPrefs.edit().putString(PrefsConsts.PHONE_INFO_SECURE_ID_PREFS, secure)
                    .apply();
            return mPrefs.getString(PrefsConsts.PHONE_INFO_SECURE_ID_PREFS, "");
        }
    }

    // 获取安装时的系统时间
    private String getInstallId() {
        if (mPrefs.contains(PrefsConsts.PHONE_INFO_INSTALL_ID_PREFS)) {
            return mPrefs.getString(PrefsConsts.PHONE_INFO_INSTALL_ID_PREFS, "");
        } else {
            String time = String.format(Locale.ENGLISH, "%d", System.currentTimeMillis());
            mPrefs.edit().putString(PrefsConsts.PHONE_INFO_INSTALL_ID_PREFS, time)
                    .apply();
            return mPrefs.getString(PrefsConsts.PHONE_INFO_INSTALL_ID_PREFS, "");
        }
    }

    private String getDeviceInfo() {
        return Build.MODEL + "_by_" + Build.MANUFACTURER;
    }
}
