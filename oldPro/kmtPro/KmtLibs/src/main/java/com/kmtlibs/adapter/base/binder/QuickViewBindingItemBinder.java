package com.kmtlibs.adapter.base.binder;//package com.kmtlibs.adapter.base.binder;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
//
///**
// * 使用 ViewBinding 快速构建 Binder
// *
// * @ T item数据类型
// * @ VB : ViewBinding
// */
//public abstract class QuickViewBindingItemBinder<T, VB extends ViewBinding> extends BaseItemBinder<T, QuickViewBindingItemBinder.BinderVBHolder> {
//
//    /**
//     * 此 Holder 不适用于其他 BaseAdapter，仅针对[BaseBinderAdapter]
//     */
//    class BinderVBHolder<VB extends ViewBinding> extends BaseViewHolder {
//        public BinderVBHolder(VB viewBinding) {
//            super(viewBinding.getRoot());
//        }
//    }
//
//    @Override
//    public BinderVBHolder<VB> onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new BinderVBHolder(onCreateViewBinding(LayoutInflater.from(parent.getContext()), parent, viewType));
//    }
//
//    abstract VB onCreateViewBinding(LayoutInflater layoutInflater, ViewGroup parent, int viewType);
//}