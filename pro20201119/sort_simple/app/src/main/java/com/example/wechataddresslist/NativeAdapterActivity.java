package com.example.wechataddresslist;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechataddresslist.adapter.NativeAdapter;
import com.example.wechataddresslist.bean.SortBean;
import com.example.wechataddresslist.callback.OnRecyclerItemListener;
import com.example.wechataddresslist.utils.Utils;
import com.example.wechataddresslist.widget.SideBar;
import com.lib.chinese2pinyin.PinYin;
import com.lib.chinese2pinyin.PinyinComparator;
import com.lib.sticky.StickyRecyclerHeadersDecoration;

import java.util.Collections;
import java.util.List;

public class NativeAdapterActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private StickyRecyclerHeadersDecoration headersDecor;
    private NativeAdapter adapter;
    private SideBar sideBar;

    private LinearLayoutManager manager;
    private int pyType = Utils.PY_For_lib;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pyType = getIntent().getIntExtra("py", Utils.PY_For_lib);
        setContentView(R.layout.activity_recycler);
        manager = new LinearLayoutManager(this);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);
        sideBar = findViewById(R.id.side);
        List<SortBean> datas = Utils.datas(pyType);
        Collections.sort(datas, new PinyinComparator());
        datas.add(0, new SortBean("公众号", PinYin.getPinYin("公众号"), Utils.HEAD));
        datas.add(0, new SortBean("标签", PinYin.getPinYin("标签"), Utils.HEAD));
        datas.add(0, new SortBean("群聊", PinYin.getPinYin("群聊"), Utils.HEAD));
        datas.add(0, new SortBean("新的朋友", PinYin.getPinYin("新的朋友"), Utils.HEAD));
        adapter = new NativeAdapter(datas);
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
        adapter.setItemListener((OnRecyclerItemListener<SortBean>) (adapter, view, position, data) -> {
            Toast.makeText(this, "pos:" + position + "," + data.getDisplayInfo(), Toast.LENGTH_SHORT).show();
        });
        sideBar.setOnTouchingLetterChangedListener(s -> {
            int index = adapter.getIndex(s);
            if (index != -1 && recycler != null) {
                manager.scrollToPositionWithOffset(index, 0);
            }
        });
    }
}