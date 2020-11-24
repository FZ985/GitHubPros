package com.app.api29.ui.fragment;

import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.api29.Adapter3;
import com.app.api29.R;
import com.app.api29.SortBean;
import com.app.api29.base.BaseFragment;
import com.app.api29.chinese2pinyin.PinYin;
import com.app.api29.chinese2pinyin.PinyinComparator;
import com.app.api29.chinese2pinyin.SortObject;
import com.app.api29.sticky.StickyRecyclerHeadersDecoration;
import com.app.api29.utils.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-05-06 11:28
 **/
public class HomeFragment extends BaseFragment {
    private RecyclerView recycler;

    private Adapter3 adapter;
    private LinearLayoutManager manager;
    private StickyRecyclerHeadersDecoration headersDecor;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        recycler = mRootView.findViewById(R.id.recycler);
        manager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(manager);
        List<SortBean> datas = new ArrayList<>();
        for (String s : items) {
            datas.add(new SortBean(s));
        }
//        Collections.sort(datas, new PinyinComparator());
        datas = Sort.getSort(datas,Sort.AZ_09_OTHER);
        adapter = new Adapter3(datas);
        recycler.setAdapter(adapter);
        headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recycler.addItemDecoration(headersDecor);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                headersDecor.invalidateHeaders();
            }
        });
    }

    @Override
    public void initData() {
    }


    private String[] items = {"aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh",
            "2222",
            "jjjj",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "@#$%^",
            "#@$%^",
            "$#@%^",
            "99999",
            "99999",
            "oooo", "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh",
            "99999",
            "99999",
            "jjjj",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "oooo", "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh",
            "jjjj",
            "11111",
            "99999",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "oooo",
            "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh", "421",
            "123",
            "jjjj",
            "77",
            "adggg",
            "hhhjk",
            "2323",
            "yyy",
            "uuuu",
            "iii",
            "oooo",
            "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "1111",
            "dgad",
            "gggg",
            "hhhh",
            "jjjj",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "oooo"};

}
