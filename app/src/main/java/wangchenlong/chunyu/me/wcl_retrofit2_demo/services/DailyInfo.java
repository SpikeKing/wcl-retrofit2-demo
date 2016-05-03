package wangchenlong.chunyu.me.wcl_retrofit2_demo.services;

import java.util.List;

/**
 * 日常信息的返回
 * <p>
 * Created by wangchenlong on 16/5/3.
 */
public class DailyInfo {
    public String new_version; // 新版本
    public String new_updates; // 新更新
    public List<AdvertBean> advert; // 广告

    public static class AdvertBean {
        public int stay_time;
        public String ad_type;
        public String share_icon;
        public String url;
        public boolean share;
        public boolean full_screen;
        public int number;
        public String image_url;
        public String share_title;
        public String share_content;
    }
}
