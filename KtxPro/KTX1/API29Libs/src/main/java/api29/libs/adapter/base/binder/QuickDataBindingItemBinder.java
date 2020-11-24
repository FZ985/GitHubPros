//package api29.libs.adapter.base.binder;
//
//
//import android.databinding.ViewDataBinding;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import api29.libs.adapter.base.viewholder.BaseViewHolder;
//
//
///**
// * 使用 DataBinding 快速构建 Binder
// *
// * @ T item数据类型
// * @ DB : ViewDataBinding
// */
//public abstract class QuickDataBindingItemBinder<T, DB extends ViewDataBinding> extends BaseItemBinder<T, QuickDataBindingItemBinder.BinderDataBindingHolder> {
//
//    /**
//     * 此 Holder 不适用于其他 BaseAdapter，仅针对[BaseBinderAdapter]
//     */
//    public class BinderDataBindingHolder<DB extends ViewDataBinding> extends BaseViewHolder {
//        public BinderDataBindingHolder(DB dataBinding) {
//            super(dataBinding.getRoot());
//        }
//    }
//
//    @Override
//    public BinderDataBindingHolder<DB> onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new BinderDataBindingHolder(onCreateDataBinding(LayoutInflater.from(parent.getContext()), parent, viewType));
//    }
//
//    abstract DB onCreateDataBinding(LayoutInflater layoutInflater, ViewGroup parent, int viewType);
//}