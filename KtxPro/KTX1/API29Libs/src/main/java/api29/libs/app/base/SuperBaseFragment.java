package api29.libs.app.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import api29.libs.app.handler.HandlerHelperAdapter;
import api29.libs.app.receive.IBroadcastRecvHandler;
import api29.libs.app.receive.RecvCallBack;


/**
 * Created by JFZ
 * on 2018/5/20.
 */

public abstract class SuperBaseFragment extends Fragment implements BaseInitCallback, View.OnClickListener, Handler.Callback, RecvCallBack {

    private String TAG = getClass().getSimpleName();
    protected View mRootView;
    protected AppCompatActivity mAct;
    protected Context mContext;
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
    protected HandlerHelperAdapter<AppCompatActivity> mHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mAct = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new HandlerHelperAdapter<AppCompatActivity>(mAct, this);
    }

    @Override
    public View createView() {
        return new View(mContext);
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
        LocalBroadcastManager.getInstance(mAct.getApplicationContext()).registerReceiver(
                mBroadcastReceiv, mReceivFilter);
    }

    @Override
    public void onDetach() {
        try {
            if (mBroadcastReceiv != null) {
                LocalBroadcastManager.getInstance(mAct.getApplicationContext()).unregisterReceiver(
                        mBroadcastReceiv);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mContext = null;
        Log.i(TAG, "#视图销毁#");
        super.onDetach();
    }

    /**
     * 广播处理器
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String act = intent.getAction();
            onSafeReceiv(context, intent, act);
        }
    }

    /**
     * 广播处理器
     */
    protected void onSafeReceiv(Context context, Intent intent, String action) {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return true;
    }

}
