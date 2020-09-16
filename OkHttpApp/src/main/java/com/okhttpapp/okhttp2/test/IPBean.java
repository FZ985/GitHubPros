package com.okhttpapp.okhttp2.test;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-09-16 11:13
 **/
public class IPBean implements Serializable {
//    {
//        "resultcode": "200",
//            "reason": "查询成功",
//            "result": {
//        "Country": "中国",
//                "Province": "江苏省",
//                "City": "无锡市",
//                "Isp": "电信"
//    },
//        "error_code": 0
//    }

    public String resultcode;
    public String reason;
    public IPCountry result;
    public String error_code;

    public static class IPCountry implements Serializable {
        String Country;
        String Province;
        String City;
        String Isp;

        @Override
        public String toString() {
            return "IPCountry{" +
                    "Country='" + Country + '\'' +
                    ", Province='" + Province + '\'' +
                    ", City='" + City + '\'' +
                    ", Isp='" + Isp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "IPBean{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result='" + ((result == null) ? "null" : result.toString()) + '\'' +
                ", error_code='" + error_code + '\'' +
                '}';
    }
}
