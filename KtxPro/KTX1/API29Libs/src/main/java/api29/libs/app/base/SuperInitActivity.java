package api29.libs.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import api29.libs.app.ActivityManager;
import api29.libs.app.HomeKeyWatcher;
import api29.libs.app.handler.HandlerHelperAdapter;
import api29.libs.app.receive.IBroadcastRecvHandler;
import api29.libs.app.receive.RecvCallBack;

/**
 * Create by JFZ
 * date: 2020-04-30 15:58
 **/
public abstract class SuperInitActivity extends AppCompatActivity implements BaseInitCallback, Handler.Callback, View.OnClickListener, RecvCallBack, HomeKeyWatcher.OnHomePressedListener {
    protected String TAG = getClass().getSimpleName();
    protected Activity mContext;
    //    private boolean pressedHome;
//    private HomeKeyWatcher mHomeKeyWatcher;

    /**
     * 广播
     */
    protected IBroadcastRecvHandler mBroadcastReceiv;
    /**
     * 广播过滤
     */
    protected IntentFilter mReceivFilter;

    /**
     * 安全的handler
     */
    protected HandlerHelperAdapter<Activity> mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onCreateBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        onCreateAfter(savedInstanceState);
        mContext = this;
        mHandler = new HandlerHelperAdapter<Activity>(mContext, this);
        ActivityManager.getAppInstance().addActivity(this);//将当前activity添加进入管理栈
//        if (mHomeKeyWatcher == null) {
//            mHomeKeyWatcher = new HomeKeyWatcher(this);
//            mHomeKeyWatcher.setOnHomePressedListener(this);
//            pressedHome = false;
//            if (!mHomeKeyWatcher.isRegRecevier()) {
//                mHomeKeyWatcher.startWatch();
//            }
//        }
        printActivityLife("onCreate");
    }

    protected void onCreateBefore(Bundle savedInstanceState) {

    }

    protected void onCreateAfter(Bundle savedInstanceState) {

    }

    @Override
    public View createView() {
        return new View(this);
    }

    public void styleBar(Activity activity) {

    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        printActivityLife("handleMessage===>拦截");
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        printActivityLife(TAG + ":onReceive=======================");
        if (intent != null) {
            String action = intent.getAction();
            onSafeReceive(intent, action);
        }
    }

    /**
     * 广播回调.空实现
     */
    public void onSafeReceive(Intent intent, String action) {
        printActivityLife(TAG + ":onSafeReceive=====广播==================");
    }

    @Override
    protected void onRestart() {
//        mHomeKeyWatcher.startWatch();
//        pressedHome = false;
        super.onRestart();
        printActivityLife("onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        printActivityLife("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        printActivityLife("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mHomeKeyWatcher.stopWatch();
    }

    @Override
    protected void onDestroy() {
        printActivityLife("onDestroy");
        ActivityManager.getAppInstance().removeActivity(this);//将当前activity移除管理栈
        if (mBroadcastReceiv != null)
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiv);
//        if (mHomeKeyWatcher != null && mHomeKeyWatcher.isRegRecevier()) {
//            mHomeKeyWatcher.stopWatch();
//        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
        mContext = null;
    }

    /**
     * 注册广播
     */
    public void regBroadcastRecv(String... actions) {
        if (mBroadcastReceiv == null || mReceivFilter == null) {
            mBroadcastReceiv = new IBroadcastRecvHandler(this);
            mReceivFilter = new IntentFilter();
        }
        if (actions != null) {
            for (String act : actions) {
                mReceivFilter.addAction(act);
            }
        }
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mBroadcastReceiv, mReceivFilter);
    }

    @Override
    public void onHomePressed() {
//        pressedHome = true;
    }


    private InputMethodManager imm;

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null) {
            Configuration config = res.getConfiguration();
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        }
        return res;
    }

    protected void printActivityLife(String method) {
        Log.i(TAG, String.format("######Activity:%s #####执行:%s", TAG, method));
    }
}
