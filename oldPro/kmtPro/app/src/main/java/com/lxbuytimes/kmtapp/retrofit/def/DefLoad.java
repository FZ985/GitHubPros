package com.lxbuytimes.kmtapp.retrofit.def;

import android.app.Activity;
import android.app.ProgressDialog;

import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import java.lang.ref.WeakReference;


/**
 * Created by JFZ .
 * on 2018/1/15.
 */

public class DefLoad extends Loadding {

    private ProgressDialog progressDialog;

    private DefLoad(Activity context) {
        if (context != null) {
            progressDialog = new ProgressDialog(new WeakReference<Activity>(context).get(), 1);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static DefLoad use(Activity context) {
        return new DefLoad(context);
    }

    @Override
    public void show() {
        if (progressDialog != null) {
            Activity activity = scanForActivity(progressDialog.getContext());
            if (activity != null && !activity.isFinishing()) {
                progressDialog.show();
                HttpUtils.log("load", "show");
            }
        }
    }

    @Override
    public void dismiss() {
        if (progressDialog != null) {
            Activity activity = scanForActivity(progressDialog.getContext());
            if (activity != null && !activity.isFinishing()) {
                progressDialog.dismiss();
                progressDialog = null;
                HttpUtils.log("load", "dismiss");
            }
        }
        progressDialog = null;
    }

    @Override
    public boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

}


