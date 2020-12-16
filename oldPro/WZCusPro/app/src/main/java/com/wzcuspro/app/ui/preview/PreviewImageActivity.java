package com.wzcuspro.app.ui.preview;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.wzcuspro.R;
import com.wzcuspro.app.base.BaseActivity;
import com.wzcuspro.app.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import weiying.customlib.barlib.ImmersionBar;

public class PreviewImageActivity extends BaseActivity {
    private LinearLayout preview_root;
    private ViewPager preview_vp;
    private ArrayList<String> imgList;
    private int index = 0;

    @Override
    public void initView() {
        preview_root = findViewById(R.id.preview_root);
        preview_vp = findViewById(R.id.preview_vp);
        List<BaseFragment> list = new ArrayList<>();
        for (String url : imgList) {
            list.add(PreviewImageFragment.instance(url));
        }
        list.add(new PreviewAdFragment());
        preview_vp.setAdapter(new PreviewViewPagerAdapter(getSupportFragmentManager(), list));
        preview_vp.setCurrentItem(index);
    }

    @Override
    public void initData() {

    }

    @Override
    public int layoutResId() {
        index = getIntent().getIntExtra("index", 0);
        imgList = getIntent().getStringArrayListExtra("imgs");
        return R.layout.activity_preview_image;
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.navigationBarColor(R.color.black).init();
    }

    public static void jump(Activity activity, int index, ArrayList<String> list) {
        activity.startActivity(new Intent(activity, PreviewImageActivity.class).putExtra("index", index).putStringArrayListExtra("imgs", list));
        activity.overridePendingTransition(R.anim.wechat_act_enter, R.anim.wechat_act_exit);
    }

    class PreviewViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<BaseFragment> list;

        public PreviewViewPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.wechat_act_enter, R.anim.wechat_act_exit);
    }
}
