package com.kmt.pro.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.bean.KeFuBean;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.kmtlibs.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Create by JFZ
 * date: 2020-02-25 12:08
 **/
public class KeFuAdapter extends BaseQuickAdapter<KeFuBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, final KeFuBean item) {
        ImageView icon = helper.getView(R.id.kefu_icon);
        TextView kefu_name_title = helper.getView(R.id.kefu_name_title);
        TextView kefu_num = helper.getView(R.id.kefu_num);
        TextView kefu_copy = helper.getView(R.id.kefu_copy);
        LinearLayout kefu_root = helper.getView(R.id.kefu_root);
        if (item.type == 1) {
            icon.setImageResource(R.mipmap.icon_weixin_kefu);
            kefu_name_title.setText("官方微信客服");
            kefu_name_title.setTextColor(Color.parseColor("#489C91"));
            kefu_copy.setTextColor(Color.WHITE);
            kefu_copy.setBackgroundResource(R.drawable.shape_kefu_copy_wx);
            kefu_root.setBackgroundResource(R.drawable.kefu_weixin_shape);
        } else {
            icon.setImageResource(R.mipmap.icon_qq_kefu);
            kefu_name_title.setText("官方QQ客服");
            kefu_name_title.setTextColor(Color.parseColor("#73502D"));
            kefu_copy.setTextColor(Color.parseColor("#73502D"));
            kefu_copy.setBackgroundResource(R.drawable.shape_kefu_copy_qq);
            kefu_root.setBackgroundResource(R.drawable.kefu_qq_shape);
        }
        kefu_num.setText(item.name);
        kefu_copy.setOnClickListener(v -> {
            Utils.copyString(item.name, v.getContext());
            Tools.showToast("复制成功");
        });
    }

    public KeFuAdapter(String qq, String wx) {
        super(R.layout.item_kefu_list, new ArrayList<KeFuBean>());
        List<KeFuBean> data = new ArrayList<>();

        if (!TextUtils.isEmpty(wx)) {
            String[] wxs = wx.split(",");
            for (String w : wxs) {
                data.add(new KeFuBean(1, w));
            }
        }
        if (!TextUtils.isEmpty(qq)) {
            String[] qqs = qq.split(",");
            for (String w : qqs) {
                data.add(new KeFuBean(0, w));
            }
        }

        setNewData(data);
    }
}
