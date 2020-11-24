package api29.libs.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Stack;


public class ActivityManager {
    private static Stack<Activity> activityStack;

    private ActivityManager() {
    }

    private static class Holder {
        private static volatile ActivityManager INSTANCE = new ActivityManager();
    }

    /**
     * 单一实例
     */
    public static synchronized ActivityManager getAppInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 添加Activity到堆栈
     */
    public synchronized void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
        Log.i("TAG", "ActivityManager添加了：" + activity.getClass().getName());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(activityStack.lastElement());
    }

    /**
     * 移除最后一个Activity
     */
    public void removeActivity(Activity activity) {
        try {
            if (activity != null && activityStack != null && activityStack.contains(activity)) {
                activityStack.remove(activity);
                Log.i("TAG", "ActivityManager移除了：" + activity.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            activity.finish();
            Log.i("TAG", "ActivityManager关闭了：" + activity.getClass().getName());
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                if (activityStack.get(i).getClass().equals(cls)) {
                    finishActivity(activityStack.get(i));
                    return;
                }
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 除了当前activity，其他activity全部关闭
     */
    public void finishActivityOnlyCurrent() {
        try {
            Activity currentAct = currentActivity();
            if (activityStack != null && currentAct != null) {
                for (Activity activity : activityStack) {
                    if (activity != null && !activity.getClass().getName().equals(currentAct.getClass().getName())) {
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保留指定activity
    public void keepActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                if (!activityStack.get(i).getClass().equals(cls)) {
                    finishActivity(activityStack.get(i));
                    return;
                }
            }
        }
    }

    public void hideSoftKeyBoard(Activity activity) {
        View localView = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (localView != null && imm != null) {
            imm.hideSoftInputFromWindow(localView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
