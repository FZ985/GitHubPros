package api29.libs.adapter.base.entity;

/**
 * 仅供java使用
 *
 * 由于java无法实现{@link SectionEntity}中的默认接口实现，所以使用抽象类再封装一次，用于提供默认实现。
 */
public abstract class JSectionEntity extends SectionEntity {

    /**
     * 用于返回item类型，除了头布局外，默认只有 NORMAL_TYPE 一种布局
     * 如果需要实现 item 多布局，请重写此方法，返回自己的type
     */
    @Override
    public int itemType() {
        if (isHeader) {
            return SectionEntity.HEADER_TYPE;
        } else {
            // 拷贝 重写此处，返回自己的多布局类型
            return SectionEntity.NORMAL_TYPE;
        }
    }
}
