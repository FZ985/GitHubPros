package api29.libs.app.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import api29.libs.R;
import api29.libs.app.utils.Utils;


public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    protected Window window;
    protected WindowManager.LayoutParams lp;
    protected Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
        int resId = resLayoutId();
        if (resId != 0) {
            setContentView(resId);
        } else setContentView(createView());
        window = getWindow();
        lp = window.getAttributes();
        gravity(Gravity.CENTER);
        width(Utils.getScreenWidth(context));
        initView();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        int resId = resLayoutId();
        if (resId != 0) {
            setContentView(resId);
        } else setContentView(createView());
        window = getWindow();
        lp = window.getAttributes();
        gravity(Gravity.CENTER);
        width(Utils.getScreenWidth(context));
        initView();
    }


    public abstract int resLayoutId();

    public abstract void initView();

    public abstract void initData();

    public View createView() {
        return new View(getContext());
    }

    @Override
    public void onClick(View v) {

    }

    public void gravity(int gravity) {
        if (lp != null && window != null) {
            lp.gravity = gravity;
            window.setAttributes(lp);
        }
    }

    public void alpha(float alpha) {
        if (lp != null && window != null) {
            lp.dimAmount = alpha;
            window.setAttributes(lp);
        }
    }

    public void width(int width) {
        if (lp != null && window != null) {
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    public void height(int height) {
        if (lp != null && window != null) {
            lp.height = height;
            window.setAttributes(lp);
        }
    }

    public void screenSize(int width, int height) {
        if (lp != null && window != null) {
            lp.width = width;
            lp.height = height;
            window.setAttributes(lp);
        }
    }

    public void animStyle(int styleRes) {
        if (lp != null && window != null) {
            window.setWindowAnimations(styleRes);
            lp.windowAnimations = styleRes;
            window.setAttributes(lp);
        }
    }
}
