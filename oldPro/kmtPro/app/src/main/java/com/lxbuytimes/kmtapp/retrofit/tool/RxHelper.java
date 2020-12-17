package com.lxbuytimes.kmtapp.retrofit.tool;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by JFZ
 * date: 2019-10-16 15:08
 * 绑定Activity与fragment的生命周期的帮助类，防止内存泄露，
 * 并切换io与主线程
 * <p>
 * Observable.xxx().compose(RxHelper.observableIO2Main())
 **/
public class RxHelper {
//    public static <T> ObservableTransformer<T, T> observableIO2Main(final Context context) {
//        return upstream -> {
//            Observable<T> observable = upstream.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread());
//            return composeContext(context, observable);
//        };
//    }

    public static <T> ObservableTransformer<T, T> observableIO2Main() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> flowableIO2Main() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    private static <T> ObservableSource<T> composeContext(Context context, Observable<T> observable) {
//        if (context instanceof RxActivity) {
//            return observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
//        } else if (context instanceof RxFragmentActivity) {
//            return observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
//        } else if (context instanceof RxAppCompatActivity) {
//            return observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
//        } else {
//            return observable;
//        }
//    }

}
