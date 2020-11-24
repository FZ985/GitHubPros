package api29.libs.adapter.base.provider;


import api29.libs.adapter.base.BaseNodeAdapter;
import api29.libs.adapter.base.entity.node.BaseNode;

public abstract class BaseNodeProvider extends BaseItemProvider<BaseNode> {

    @Override
    public BaseNodeAdapter getAdapter() {
        return (BaseNodeAdapter) super.getAdapter();
    }
}