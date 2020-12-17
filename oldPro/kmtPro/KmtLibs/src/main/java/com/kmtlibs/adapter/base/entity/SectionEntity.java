package com.kmtlibs.adapter.base.entity;


/**
 * 带头部布局的实体类接口
 * 实体类请继承此接口；如果使用java，请使用[JSectionEntity]抽象类
 */
public class SectionEntity implements MultiItemEntity {

    public boolean isHeader;

    /**
     * 用于返回item类型，除了头布局外，默认只有[NORMAL_TYPE]一种布局
     * 如果需要实现 item 多布局，请重写此方法，返回自己的type
     */
    @Override
    public int itemType() {
        return isHeader ? HEADER_TYPE : NORMAL_TYPE;
    }

    public static int NORMAL_TYPE = -100;
    public static int HEADER_TYPE = -99;
}
