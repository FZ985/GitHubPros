package com.kmtlibs.okhttp.tool;

import android.app.Activity;
import android.app.ProgressDialog;

import com.kmtlibs.okhttp.callback.Loadding;

import java.lang.ref.WeakReference;


/**
 * Created by JFZ .
 * on 2018/1/15.
 */

public class OkDefLoad extends Loadding {

    private ProgressDialog progressDialog;

    private OkDefLoad(Activity context) {
        if (context != null) {
            progressDialog = new ProgressDialog(new WeakReference<Activity>(context).get(), 1);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static OkDefLoad use(Activity context) {
        return new OkDefLoad(context);
    }

    @Override
    public void show() {
        if (progressDialog != null) {
            Activity activity = scanForActivity(progressDialog.getContext());
            if (activity != null && !activity.isFinishing()) {
                progressDialog.show();
                OkhttpUtil.log("load", "show");
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
                OkhttpUtil.log("load", "dismiss");
            }
        }
        progressDialog = null;
    }

    @Override
    public boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

}


