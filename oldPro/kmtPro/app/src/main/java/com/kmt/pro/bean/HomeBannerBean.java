package com.kmt.pro.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-01 15:37
 **/
public class HomeBannerBean implements Serializable {
    public List<BannerBean> bannerList;

    public List<BannerBean> getBannerList() {
        if (bannerList == null) {
            bannerList = new ArrayList<>();
        }
        return bannerList;
    }

    public static class BannerBean implements Serializable {
        public int id;
        public String title;
        public String bannerUrl;
        public String remark;
        public String imageUrl;
        public String description;
        public int type;
        public String pub;
        public String channel;
        public String vedioUrl;
        public int zoneId;
        public String investorsName;
        public String investorsCode;
    }
}
