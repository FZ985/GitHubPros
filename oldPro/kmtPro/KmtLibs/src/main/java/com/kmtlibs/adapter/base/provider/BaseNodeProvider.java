package com.kmtlibs.adapter.base.provider;


import com.kmtlibs.adapter.base.BaseNodeAdapter;
import com.kmtlibs.adapter.base.entity.node.BaseNode;

public abstract class BaseNodeProvider extends BaseItemProvider<BaseNode> {

    @Override
    public BaseNodeAdapter getAdapter() {
        return (BaseNodeAdapter) super.getAdapter();
    }
}