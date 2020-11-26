package api29.libs.adapter.base.entity.node;

/**
 * 如果需要，可以实现此接口，返回脚部节点
 */
public interface NodeFooterImp {
    /**
     * 返回脚部节点
     *
     * @return BaseNode? 如果返回 null，则代表没有脚部节点
     */
    public BaseNode footerNode();
}