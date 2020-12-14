package okhttp.loadding;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by JFZ .
 * on 2018/1/15.
 */

public class DefLoad implements HttpLoad {

    private static DefLoad instance;
    private ProgressDialog progressDialog;

    private DefLoad(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public static DefLoad use(Context context) {
        if (instance == null) {
            instance = new DefLoad(context);
        }
        return instance;
    }

    @Override
    public void showLoadding() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void dismissLoadding() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public boolean isLoaddingShowing() {
        if (progressDialog != null)
            return progressDialog.isShowing();
        else
            return false;
    }
}


