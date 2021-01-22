package com.getsignature;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.getsignature.theme.SystemThemeDialog;
import com.getsignature.theme.Theme;
import com.getsignature.theme.ThemeHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private ProgressBar progress;
    private AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.listview);
        progress = findViewById(R.id.progress);
        initData();
    }

    public void initData() {
        adapter = new AppAdapter();
        list.setAdapter(adapter);
        new Thread(() -> {
            try {
                List<PackageInfo> applications = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    if (applications != null && applications.size() > 0) {
                        List<PackageInfo> apps = new ArrayList<>();
//                        List<PackageInfo> systemApps = new ArrayList<>();
                        for (PackageInfo p : applications) {
                            // 判断系统/非系统应用
                            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
                                apps.add(p);
                            } else {
//                                systemApps.add(p);
                            }
                        }
//                        apps.addAll(systemApps);
                        adapter.setApps(apps);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    list.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
        list.setOnItemClickListener((parent, view, position, id) -> {
            PackageInfo item = adapter.getItem(position);
            showPackage(item);
        });
    }

    private void showPackage(PackageInfo item) {
        if (item == null) {
            toast("无结果");
            return;
        }
        String sign = Utils.getSign(this, item.packageName);
        AlertDialog.Builder builder = SystemThemeDialog.AlertBuilder(this);
        builder.setTitle(item.applicationInfo.loadLabel(getPackageManager()) + "的签名")
                .setMessage(sign)
                .setPositiveButton("复制", (dialog, which) -> {
                    Utils.copyString(sign, this);
                    toast("复制成功");
                    dialog.dismiss();
                });
        builder.show();
    }

    private void showPackages(List<PackageInfo> items) {
        if (items.size() == 0) {
            toast("无结果");
            return;
        }
        if (items.size() == 1) {
            showPackage(items.get(0));
            return;
        }
        CharSequence[] itemArr = new CharSequence[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemArr[i] = items.get(i).applicationInfo.loadLabel(getPackageManager());
        }
        AlertDialog.Builder builder = SystemThemeDialog.AlertBuilder(this);
        builder.setTitle("搜索结果");
        builder.setItems(itemArr, (dialog, which) -> {
            showPackage(items.get(which));
            dialog.dismiss();
        }).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dark:
                String mode = Theme.get().getThemeMode();
                if (mode.equals(ThemeHelper.LIGHT_MODE)) {
                    Theme.get().putThemeMode(ThemeHelper.DARK_MODE);
                    ThemeHelper.applyTheme(ThemeHelper.DARK_MODE);
                    item.setTitle("白天模式");
                } else {
                    Theme.get().putThemeMode(ThemeHelper.LIGHT_MODE);
                    ThemeHelper.applyTheme(ThemeHelper.LIGHT_MODE);
                    item.setTitle("夜间模式");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_dark);
        item.setTitle(Theme.get().getThemeMode().equals(ThemeHelper.LIGHT_MODE) ? "夜间模式" : "白天模式");
        initSearcheView(menu);
        return true;
    }

    private void initSearcheView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchView.SearchAutoComplete edit = searchView.findViewById(R.id.search_src_text);
        edit.setHint("输入应用名称或包名");
        searchView.setSubmitButtonEnabled(true);//提交按钮是否可见
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showPackages(adapter.getItem(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void toast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}