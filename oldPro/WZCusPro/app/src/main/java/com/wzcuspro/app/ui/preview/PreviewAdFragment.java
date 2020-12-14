package com.wzcuspro.app.ui.preview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.wzcuspro.R;
import com.wzcuspro.app.base.BaseFragment;
import com.wzcuspro.app.ui.fragment.HomeFragment;
import com.wzcuspro.app.utils.Logger;
import com.wzcuspro.app.widget.SwipeBackLayout;

import weiying.customlib.recycle.adapter.BaseQuickAdapter;
import weiying.customlib.recycle.adapter.listener.OnItemClickListener;

public class PreviewAdFragment extends BaseFragment {
    private RelativeLayout root;
    private SwipeBackLayout swipback;
    private RecyclerView recycle;
    private HomeFragment.HomeAdapter adapter;

    @Override
    public void initView() {
        swipback = findViewById(R.id.swipback);
        recycle = findViewById(R.id.recycle);
        root = findViewById(R.id.root);
        swipback.setDragEdge(SwipeBackLayout.DragEdge.TOP);
        recycle.setLayoutManager(new LinearLayoutManager(mContext));
        recycle.setAdapter(adapter = new HomeFragment.HomeAdapter());
        recycle.setNestedScrollingEnabled(false);
        recycle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                PreviewImageActivity.jump(mAct, position, adapter.getDatas());
            }
        });
        swipback.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                Logger.e("----onViewPositionChanged:" + fractionAnchor + ",fractionScreen:" + fractionScreen);
                root.getBackground().mutate().setAlpha((int) (((float) (1 - fractionScreen)) * 255));
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int layoutResId() {
        return R.layout.fragment_previewad;
    }
}
