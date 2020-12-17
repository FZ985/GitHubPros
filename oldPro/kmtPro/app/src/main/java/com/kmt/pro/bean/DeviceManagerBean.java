package com.kmt.pro.bean;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-20 15:48
 **/
public class DeviceManagerBean implements Serializable {
    public String infoId;//系统主键,编辑与删除操作需要的参数
    public String deviceType;//设备类型，1、移动  2、虚拟
    public String deviceName;//设备名称
    public String deviceRemark;//用户设置备注，有值时覆盖名称
    public String deviceDescription;//设备描述
    public String bindingTime;//注册时间
    public String deviceLastTime;
}
