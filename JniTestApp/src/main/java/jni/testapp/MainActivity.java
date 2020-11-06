package jni.testapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jni.sdk.OnNativeConfig;
import com.jni.sdk.SdkConstant;
import com.jni.sdk.SdkNative;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("jjjj:" + SdkNative.getInstance().getStr());
        Log.e("调用", "赋值前:" + SdkConstant.BASE_URL);
        SdkNative.getInstance().updateValue();
        Log.e("调用", "赋值后:" + SdkConstant.BASE_URL);
        SdkNative.getInstance().onNativeConfig(new OnNativeConfig() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                System.out.println("----msg:" + msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
