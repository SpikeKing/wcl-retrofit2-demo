package wangchenlong.chunyu.me.wcl_retrofit2_demo.services;


import retrofit2.http.GET;
import rx.Observable;

/**
 * 日常服务的请求
 * <p>
 * 例如:
 * http://steps.summer2.chunyu.me/api/daily_request/
 * ?app=7&platform=android&systemVer=5.0.1
 * &version=2.0.3&app_ver=2.0.3
 * &imei=869511022668418&device_id=869511022668418
 * &mac=98%3Ae7%3Af5%3Aec%3A24%3A23&secureId=aaeec93530106e5f'
 * &installId=1461750494597&phoneType=HUAWEI+GRA-TL00_by_HUAWEI
 * &vendor=ziyou&u=NA
 * <p>
 * Created by wangchenlong on 16/5/3.
 */
public interface DailyRequestService {
    String END_POINT = "http://steps.summer2.chunyu.me/";

    @GET("/api/daily_request/") Observable<DailyInfo> getDailyInfo();
}
