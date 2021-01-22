package com.getsignature;

import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-21 17:09
 */
public class AppAdapter extends BaseAdapter {
    private List<PackageInfo> applicationInfos;
    private List<PackageInfo> datas;

    public AppAdapter() {
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder(convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, null));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PackageInfo info = datas.get(position);
        holder.icon.setImageDrawable(info.applicationInfo.loadIcon(parent.getContext().getPackageManager()));
        holder.appName.setText(info.applicationInfo.loadLabel(parent.getContext().getPackageManager()));
        holder.appPackage.setText(info.packageName);
        return convertView;
    }

    public void setApps(List<PackageInfo> datas) {
        this.applicationInfos = datas;
        this.datas = this.applicationInfos;
        notifyDataSetChanged();
    }

    public void setDatas(List<PackageInfo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<PackageInfo> getItem(String content) {
        List<PackageInfo> packageInfos = new ArrayList<>();
        if (this.datas != null) {
            for (PackageInfo p : datas) {
                if (p.packageName.equals(content) || p.applicationInfo.loadLabel(BaseApp.getInstance().getPackageManager()).toString().contains(content)) {
                    packageInfos.add(p);
                }
            }
        }
        return packageInfos;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView appName;
        public TextView appPackage;

        public ViewHolder(View view) {
            icon = view.findViewById(R.id.icon);
            appName = view.findViewById(R.id.appName);
            appPackage = view.findViewById(R.id.appPackage);
            view.setTag(this);
        }
    }
}