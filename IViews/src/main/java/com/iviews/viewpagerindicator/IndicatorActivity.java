package com.iviews.viewpagerindicator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.iviews.R;
import com.iviews.utils.Utils;
import com.iviews.viewpagerindicator.view.CircleLineIndicator;

import java.util.ArrayList;
import java.util.List;

public class IndicatorActivity extends AppCompatActivity {

    private ViewPager vp;
    private CircleLineIndicator indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp_indicator);
        indicator = findViewById(R.id.ind1);
        vp = findViewById(R.id.vp);
        List<VpFragment> fs = new ArrayList<>();
        fs.add(new VpFragment());
        fs.add(new VpFragment());
        fs.add(new VpFragment());
        fs.add(new VpFragment());

        vp.setAdapter(new PagerFragAdapter(getSupportFragmentManager(), fs));
        indicator.setRadius(Utils.dip2px(this, 3))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setViewPager(vp);
    }

    private class PagerFragAdapter extends FragmentStatePagerAdapter {
        private List<VpFragment> list;

        public PagerFragAdapter(@NonNull FragmentManager fm, List<VpFragment> list) {
            super(fm);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
