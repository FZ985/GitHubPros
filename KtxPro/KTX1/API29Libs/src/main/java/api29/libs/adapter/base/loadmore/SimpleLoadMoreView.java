package api29.libs.adapter.base.loadmore;


import android.view.View;
import android.view.ViewGroup;

import api29.libs.R;
import api29.libs.adapter.base.util.AdapterUtils;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

public class SimpleLoadMoreView extends BaseLoadMoreView {

    @Override
    public View getRootView(ViewGroup parent) {
        return AdapterUtils.getItemView(R.layout.brvah_quick_view_load_more, parent);
    }

    @Override
    View getLoadingView(BaseViewHolder holder) {
        return holder.getView(R.id.load_more_loading_view);
    }

    @Override
    View getLoadComplete(BaseViewHolder holder) {
        return holder.getView(R.id.load_more_load_complete_view);
    }

    @Override
    View getLoadEndView(BaseViewHolder holder) {
        return holder.getView(R.id.load_more_load_end_view);
    }

    @Override
    View getLoadFailView(BaseViewHolder holder) {
        return holder.getView(R.id.load_more_load_fail_view);
    }
}