package com.kotlin.k1.adapter2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kotlin.k1.R;
import com.kotlin.k1.Utils;
import com.kotlin.k1.chinese2pinyin.PinyinComparator;
import com.kotlin.k1.sticky.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.main_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<SortBean> datas = new ArrayList<>();
        for (String s : Utils.INSTANCE.getItems()) {
            datas.add(new SortBean(s));
        }
        Collections.sort(datas, new PinyinComparator());
        Adapter3 adapter3 = new Adapter3(datas);
        recyclerView.setAdapter(adapter3);
        final StickyRecyclerHeadersDecoration headDecor = new StickyRecyclerHeadersDecoration(adapter3);
        recyclerView.addItemDecoration(headDecor);
        adapter3.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                headDecor.invalidateHeaders();
            }
        });

    }

}