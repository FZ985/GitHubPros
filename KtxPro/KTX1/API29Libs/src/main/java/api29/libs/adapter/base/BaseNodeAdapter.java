package api29.libs.adapter.base;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import api29.libs.adapter.base.entity.node.BaseExpandNode;
import api29.libs.adapter.base.entity.node.BaseNode;
import api29.libs.adapter.base.entity.node.NodeFooterImp;
import api29.libs.adapter.base.provider.BaseItemProvider;
import api29.libs.adapter.base.provider.BaseNodeProvider;

public abstract class BaseNodeAdapter extends BaseProviderMultiAdapter<BaseNode> {

    public BaseNodeAdapter() {
        this(new ArrayList<>());
    }

    public BaseNodeAdapter(List<BaseNode> nodeList) {
        super(nodeList);
        if (nodeList != null && !nodeList.isEmpty()) {
            List<BaseNode> flatData = flatData(nodeList);
            this.data.addAll(flatData);
        }
    }

    public BaseNodeAdapter(List<BaseNode> nodeList, boolean isExpanded) {
        super(nodeList);
        if (nodeList != null && !nodeList.isEmpty()) {
            List<BaseNode> flatData = flatData(nodeList, isExpanded);
            this.data.addAll(flatData);
        }
    }

    private HashSet<Integer> fullSpanNodeTypeSet = new HashSet<>();

    /**
     * 添加 node provider
     *
     * @param provider BaseItemProvider
     */
    public void addNodeProvider(BaseNodeProvider provider) {
        addItemProvider(provider);
    }

    /**
     * 添加需要铺满的 node provider
     *
     * @param provider BaseItemProvider
     */
    public void addFullSpanNodeProvider(BaseNodeProvider provider) {
        fullSpanNodeTypeSet.add(provider.itemViewType());
        addItemProvider(provider);
    }

    /**
     * 添加脚部 node provider
     * 铺满一行或者一列
     *
     * @param provider BaseItemProvider
     */
    public void addFooterNodeProvider(BaseNodeProvider provider) {
        addFullSpanNodeProvider(provider);
    }

    /**
     * 请勿直接通过此方法添加 node provider！
     *
     * @param provider BaseItemProvider<BaseNode, VH>
     */
    @Override
    public void addItemProvider(BaseItemProvider<BaseNode> provider) {
        if (provider instanceof BaseNodeProvider) {
            super.addItemProvider(provider);
        } else {
            throw new IllegalStateException("Please add BaseNodeProvider, no BaseItemProvider!");
        }
    }

    @Override
    protected boolean isFixedViewType(int type) {
        return super.isFixedViewType(type) || fullSpanNodeTypeSet.contains(type);
    }

    @Override
    public void setNewInstance(List<BaseNode> list) {
        super.setNewInstance(flatData(list != null ? list : new ArrayList<>()));
    }

    /**
     * 替换整个列表数据，如果需要对某节点下的子节点进行替换，请使用[nodeReplaceChildData]！
     */
    @Override
    public void setList(Collection<BaseNode> list) {
        super.setList(flatData(list != null ? list : new ArrayList<>()));
    }

    /**
     * 如果需要对某节点下的子节点进行数据操作，请使用[nodeAddData]！
     *
     * @param position Int 整个 data 的 index
     * @param data     BaseNode
     */
    @Override
    public void addData(int position, @NonNull BaseNode data) {
        addData(position, Arrays.asList(data));
    }

    @Override
    public void addData(@NonNull BaseNode data) {
        addData(Arrays.asList(data));
    }

    @Override
    public void addData(int position, Collection<BaseNode> newData) {
        List<BaseNode> nodes = flatData(newData);
        super.addData(position, nodes);
    }

    @Override
    public void addData(Collection<BaseNode> newData) {
        List<BaseNode> nodes = flatData(newData);
        super.addData(nodes);
    }

    /**
     * 如果需要对某节点下的子节点进行数据操作，请使用[nodeRemoveData]！
     *
     * @param position Int 整个 data 的 index
     */
    @Override
    public void removeAt(int position) {
        int removeCount = removeNodesAt(position);
        notifyItemRangeRemoved(position + getHeaderLayoutCount(), removeCount);
        compatibilityDataSizeChanged(0);
    }

    /**
     * 如果需要对某节点下的子节点进行数据操作，请使用[nodeSetData]！
     *
     * @param index Int
     * @param data  BaseNode
     */
    @Override
    public void setData(int index, BaseNode data) {
        // 先移除，再添加
        int removeCount = removeNodesAt(index);
        List<BaseNode> newFlatData = flatData(Arrays.asList(data));
        this.data.addAll(index, newFlatData);
        if (removeCount == newFlatData.size()) {
            notifyItemRangeChanged(index + getHeaderLayoutCount(), removeCount);
        } else {
            notifyItemRangeRemoved(index + getHeaderLayoutCount(), removeCount);
            notifyItemRangeInserted(index + getHeaderLayoutCount(), newFlatData.size());
//        notifyItemRangeChanged(index + getHeaderLayoutCount(), max(removeCount, newFlatData.size)
        }
    }

    @Override
    public void setDiffNewData(List<BaseNode> list) {
        if (hasEmptyView()) {
            setNewInstance(list);
            return;
        }
        super.setDiffNewData(flatData(list != null ? list : new ArrayList<>()));
    }

    @Override
    public void setDiffNewData(DiffUtil.DiffResult diffResult, List<BaseNode> list) {
        if (hasEmptyView()) {
            setNewInstance(list);
            return;
        }
        super.setDiffNewData(diffResult, flatData(list));
    }

    /**
     * 从数组中移除
     *
     * @param position Int
     * @return Int 被移除的数量
     */
    private int removeNodesAt(int position) {
        if (position >= data.size()) {
            return 0;
        }
        // 记录被移除的item数量
        int removeCount = 0;
        // 先移除子项
        removeCount = removeChildAt(position);
        // 移除node自己
        this.data.remove(position);
        removeCount += 1;
        BaseNode node = this.data.get(position);
        // 移除脚部
        if (node instanceof NodeFooterImp && ((NodeFooterImp) node).footerNode() != null) {
            this.data.remove(position);
            removeCount += 1;
        }
        return removeCount;
    }

    private int removeChildAt(int position) {
        if (position >= data.size()) {
            return 0;
        }
        // 记录被移除的item数量
        int removeCount = 0;
        BaseNode node = this.data.get(position);
        // 移除子项
        if (node.childNode() != null && !node.childNode().isEmpty()) {
            if (node instanceof BaseExpandNode) {
                if (((BaseExpandNode) node).isExpanded) {
                    List<BaseNode> items = flatData(node.childNode());
                    this.data.removeAll(items);
                    removeCount = items.size();
                }
            } else {
                List<BaseNode> items = flatData(node.childNode());
                this.data.removeAll(items);
                removeCount = items.size();
            }
        }
        return removeCount;
    }

    /*************************** 重写数据设置方法 END ***************************/


    /*************************** Node 数据操作 ***************************/

    /**
     * 对指定的父node，添加子node
     *
     * @param parentNode BaseNode 父node
     * @param data       BaseNode 子node
     */
    public void nodeAddData(BaseNode parentNode, BaseNode data) {
        if (parentNode.childNode() != null) {
            parentNode.childNode().add(data);
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                return;
            }
            int parentIndex = this.data.indexOf(parentNode);
            int childIndex = parentNode.childNode().size();
            addData(parentIndex + childIndex, data);
        }
    }

    /**
     * 对指定的父node，在指定位置添加子node
     *
     * @param parentNode BaseNode 父node
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     * @param data       BaseNode 添加的数据
     */
    public void nodeAddData(BaseNode parentNode, int childIndex, BaseNode data) {
        if (parentNode.childNode() != null) {
            parentNode.childNode().add(childIndex, data);
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                return;
            }
            int parentIndex = this.data.indexOf(parentNode);
            int pos = parentIndex + 1 + childIndex;
            addData(pos, data);
        }
    }

    /**
     * 对指定的父node，在指定位置添加子node集合
     *
     * @param parentNode BaseNode 父node
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     * @param newData    添加的数据集合
     */
    public void nodeAddData(BaseNode parentNode, int childIndex, Collection<BaseNode> newData) {
        if (parentNode.childNode() != null) {
            parentNode.childNode().addAll(childIndex, newData);
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                return;
            }
            int parentIndex = this.data.indexOf(parentNode);
            int pos = parentIndex + 1 + childIndex;
            addData(pos, newData);
        }
    }

    /**
     * 对指定的父node下对子node进行移除
     *
     * @param parentNode BaseNode 父node
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     */
    public void nodeRemoveData(BaseNode parentNode, int childIndex) {
        if (parentNode.childNode() != null) {
            if (childIndex >= parentNode.childNode().size()) {
                return;
            }
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                parentNode.childNode().remove(childIndex);
                return;
            }
            int parentIndex = this.data.indexOf(parentNode);
            int pos = parentIndex + 1 + childIndex;
            remove(pos);
            parentNode.childNode().remove(childIndex);
        }
    }

    /**
     * 对指定的父node下对子node进行移除
     *
     * @param parentNode BaseNode 父node
     * @param childNode  BaseNode 子node
     */
    public void nodeRemoveData(BaseNode parentNode, BaseNode childNode) {
        if ((parentNode.childNode() != null)) {
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                parentNode.childNode().remove(childNode);
                return;
            }
            remove(childNode);
            parentNode.childNode().remove(childNode);
        }
    }

    /**
     * 改变指定的父node下的子node数据
     *
     * @param parentNode BaseNode
     * @param childIndex Int 此位置是相对于其childNodes数据的位置！并不是整个data
     * @param data       BaseNode 新数据
     */
    public void nodeSetData(BaseNode parentNode, int childIndex, BaseNode data) {
        if (parentNode.childNode() != null) {
            if (childIndex >= parentNode.childNode().size()) {
                return;
            }
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                parentNode.childNode().set(childIndex, data);
                return;
            }

            int parentIndex = this.data.indexOf(parentNode);
            int pos = parentIndex + 1 + childIndex;
            setData(pos, data);

            parentNode.childNode().set(childIndex, data);
        }
    }

    /**
     * 替换父节点下的子节点集合
     *
     * @param parentNode BaseNode
     * @param newData    Collection<BaseNode>
     */
    public void nodeReplaceChildData(BaseNode parentNode, Collection<BaseNode> newData) {
        if (parentNode.childNode() != null) {
            if (parentNode instanceof BaseExpandNode && !((BaseExpandNode) parentNode).isExpanded) {
                parentNode.childNode().clear();
                parentNode.childNode().addAll(newData);
                return;
            }

            int parentIndex = this.data.indexOf(parentNode);
            int removeCount = removeChildAt(parentIndex);

            parentNode.childNode().clear();
            parentNode.childNode().addAll(newData);

            List<BaseNode> newFlatData = flatData(newData);
            this.data.addAll(parentIndex + 1, newFlatData);

            int positionStart = parentIndex + 1 + getHeaderLayoutCount();
            if (removeCount == newFlatData.size()) {
                notifyItemRangeChanged(positionStart, removeCount);
            } else {
                notifyItemRangeRemoved(positionStart, removeCount);
                notifyItemRangeInserted(positionStart, newFlatData.size());
            }
//            notifyItemRangeChanged(parentIndex + 1 + getHeaderLayoutCount(), max(removeCount, newFlatData.size))
        }
    }

    /*************************** Node 数据操作 END ***************************/

    /**
     * 将输入的嵌套类型数组循环递归，在扁平化数据的同时，设置展开状态
     *
     * @param list       Collection<BaseNode>
     * @param isExpanded Boolean? 如果不需要改变状态，设置为null。true 为展开，false 为收起
     * @return MutableList<BaseNode>
     */
    private List<BaseNode> flatData(Collection<BaseNode> list, Boolean isExpanded) {
        List<BaseNode> newList = new ArrayList<>();
        for (BaseNode element : list) {
            newList.add(element);
            if (element instanceof BaseExpandNode) {
                // 如果是展开状态 或者需要设置为展开状态
                boolean elementIsExpanded = ((BaseExpandNode) element).isExpanded;
                if (isExpanded || elementIsExpanded) {
                    List<BaseNode> childNode = element.childNode();
                    if (childNode != null && !childNode.isEmpty()) {
                        List<BaseNode> items = flatData(childNode, isExpanded);
                        newList.addAll(items);
                    }
                }
                if (isExpanded != null) {
                    ((BaseExpandNode) element).isExpanded = isExpanded;
                }
            } else {
                List<BaseNode> childNode = element.childNode();
                if (childNode != null && !childNode.isEmpty()) {
                    List<BaseNode> items = flatData(childNode, isExpanded);
                    newList.addAll(items);
                }
            }
            if (element instanceof NodeFooterImp) {
                if (((NodeFooterImp) element).footerNode() != null) {
                    newList.add(((NodeFooterImp) element).footerNode());
                }
            }
        }
        return newList;
    }

    private List<BaseNode> flatData(Collection<BaseNode> list) {
        return flatData(list, true);
    }

    /**
     * 收起Node
     * 私有方法，为减少递归复杂度，不对外暴露 isChangeChildExpand 参数，防止错误设置
     *
     * @param position              Int
     * @param isChangeChildCollapse Boolean 是否改变子 node 的状态为收起，true 为跟随变为收起，false 表示保持原状态。
     * @param animate               Boolean
     * @param notify                Boolean
     */
    private int collapse(@IntRange(from = 0) int position, boolean isChangeChildCollapse, boolean animate, boolean notify, Object parentPayload) {
        BaseNode node = this.data.get(position);
        if (node instanceof BaseExpandNode && ((BaseExpandNode) node).isExpanded) {
            int adapterPosition = position + getHeaderLayoutCount();
            ((BaseExpandNode) node).isExpanded = false;
            if (node.childNode() != null && node.childNode().isEmpty()) {
                notifyItemChanged(adapterPosition, parentPayload);
                return 0;
            }
            List<BaseNode> items = flatData(node.childNode(), (isChangeChildCollapse) ? false : null);
            int size = items.size();
            this.data.removeAll(items);
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition, parentPayload);
                    notifyItemRangeRemoved(adapterPosition + 1, size);
                } else {
                    notifyDataSetChanged();
                }
            }
            return size;
        }
        return 0;
    }

    /**
     * 展开Node
     * 私有方法，为减少递归复杂度，不对外暴露 isChangeChildExpand 参数，防止错误设置
     *
     * @param position            Int
     * @param isChangeChildExpand Boolean 是否改变子 node 的状态为展开，true 为跟随变为展开，false 表示保持原状态。
     * @param animate             Boolean
     * @param notify              Boolean
     */
    private int expand(@IntRange(from = 0) int position, boolean isChangeChildExpand, boolean animate, boolean notify, Object parentPayload) {
        BaseNode node = this.data.get(position);
        if (node instanceof BaseExpandNode && !((BaseExpandNode) node).isExpanded) {
            int adapterPosition = position + getHeaderLayoutCount();
            ((BaseExpandNode) node).isExpanded = true;
            if (node.childNode() != null && node.childNode().isEmpty()) {
                notifyItemChanged(adapterPosition, parentPayload);
                return 0;
            }
            List<BaseNode> items = flatData(node.childNode(), (isChangeChildExpand) ? true : false);
            int size = items.size();
            this.data.addAll(position + 1, items);
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition, parentPayload);
                    notifyItemRangeInserted(adapterPosition + 1, size);
                } else {
                    notifyDataSetChanged();
                }
            }
            return size;
        }
        return 0;
    }

    /**
     * 收起 node
     *
     * @param position Int
     * @param animate  Boolean
     * @param notify   Boolean
     */
    public int collapse(@IntRange(from = 0) int position, boolean animate, boolean notify, Object parentPayload) {
        return collapse(position, false, animate, notify, parentPayload);
    }

    /**
     * 展开 node
     *
     * @param position Int
     * @param animate  Boolean
     * @param notify   Boolean
     */
    public int expand(@IntRange(from = 0) int position, boolean animate, boolean notify, Object parentPayload) {
        return expand(position, false, animate, notify, parentPayload);
    }

    /**
     * 收起或展开Node
     *
     * @param position Int
     * @param animate  Boolean
     * @param notify   Boolean
     */

    public int expandOrCollapse(@IntRange(from = 0) int position, boolean animate, boolean notify, Object parentPayload) {
        BaseNode node = this.data.get(position);
        if (node instanceof BaseExpandNode) {
            if (((BaseExpandNode) node).isExpanded) {
                return collapse(position, false, animate, notify, parentPayload);
            } else {
                return expand(position, false, animate, notify, parentPayload);
            }
        }
        return 0;
    }

    public int expandOrCollapse(@IntRange(from = 0) int position) {
        return expandOrCollapse(position, true, true, null);
    }

    public int expandAndChild(@IntRange(from = 0) int position) {
        return expandAndChild(position, true, true, null);
    }

    public int expandAndChild(@IntRange(from = 0) int position, boolean animate, boolean notify, Object parentPayload) {
        return expand(position, true, animate, notify, parentPayload);
    }

    public int collapseAndChild(@IntRange(from = 0) int position) {
        return collapseAndChild(position, true, true, null);
    }

    public int collapseAndChild(@IntRange(from = 0) int position, boolean animate, boolean notify, Object parentPayload) {
        return collapse(position, true, animate, notify, parentPayload);
    }

    /**
     * 展开某一个node的时候，折叠其他node
     *
     * @param position        Int
     * @param isExpandedChild Boolean 展开的时候，是否展开子项目
     * @param isCollapseChild Boolean 折叠其他node的时候，是否折叠子项目
     * @param animate         Boolean
     * @param notify          Boolean
     */
    public void expandAndCollapseOther(@IntRange(from = 0) int position,
                                       boolean isExpandedChild, boolean isCollapseChild,
                                       boolean animate, boolean notify,
                                       Object expandPayload, Object collapsePayload) {

        int expandCount = expand(position, isExpandedChild, animate, notify, expandPayload);
        if (expandCount == 0) {
            return;
        }

        int parentPosition = findParentNode(position);
        // 当前层级顶部开始位置
        int firstPosition = (parentPosition == -1) ? 0 // 如果没有父节点，则为最外层，从0开始
                : parentPosition + 1; // 如果有父节点，则为子节点，从 父节点+1 的位置开始

        // 当前 position 之前有 node 收起以后，position的位置就会变化
        int newPosition = position;
        // 在此之前的 node 总数
        int beforeAllSize = position - firstPosition;
        // 如果此 position 之前有 node
        if (beforeAllSize > 0) {
            // 从顶部开始位置循环
            int i = firstPosition;
            do {
                int collapseSize = collapse(i, isCollapseChild, animate, notify, collapsePayload);
                i++;
                // 每次折叠后，重新计算新的 Position
                newPosition -= collapseSize;
            } while (i < newPosition);
        }

        // 当前层级最后的位置
        int lastPosition = 0;
        if (parentPosition == -1) {
            lastPosition = (data.size() - 1);// 如果没有父节点，则为最外层
        } else {
            int dataSize = data.get(parentPosition).childNode() != null ? data.get(parentPosition).childNode().size() : 0;
            lastPosition = parentPosition + dataSize + expandCount; // 如果有父节点，则为子节点，父节点 + 子节点数量 + 展开的数量
        }

        //如果此 position 之后有 node
        if ((newPosition + expandCount) < lastPosition) {
            int i = newPosition + expandCount + 1;
            while (i <= lastPosition) {
                int collapseSize = collapse(i, isCollapseChild, animate, notify, collapsePayload);
                i++;
                lastPosition -= collapseSize;
            }
        }
    }


    /**
     * 查找父节点。如果不存在，则返回-1
     *
     * @param node BaseNode
     * @return Int 父节点的position
     */
    private int findParentNode(BaseNode node) {
        int pos = this.data.indexOf(node);
        if (pos == -1 || pos == 0) {
            return -1;
        }
        for (int i = 0; i < (pos - 1); i++) {
            BaseNode tempNode = this.data.get(i);
            if (tempNode.childNode() != null && tempNode.childNode().contains(node)) {
                return i;
            }
        }
        return -1;
    }

    private int findParentNode(@IntRange(from = 0) int position) {
        if (position == 0) {
            return -1;
        }
        BaseNode node = this.data.get(position);
        for (int i = 0; i < (position - 1); i++) {
            BaseNode tempNode = this.data.get(i);
            if (tempNode.childNode() != null && tempNode.childNode().contains(node)) {
                return i;
            }
        }
        return -1;
    }
}