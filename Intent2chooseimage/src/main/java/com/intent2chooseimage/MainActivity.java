package com.intent2chooseimage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.intent2chooseimage.choose.FileChooser;

public class MainActivity extends AppCompatActivity {
    private ImageView iamgeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iamgeIv = findViewById(R.id.iamgeIv);
        if (!PermissionUtils.checkReadWritePermission(this)) {
            PermissionUtils.reqPermission(this, PermissionUtils.PERMISSIONS_READ_AND_WRITE, 100);
        }
    }

    public void image(View view) {
        FileChooser.getInstance()
                .setCall(uris -> {
                    System.out.println("ddd_uris:" + uris.length);
                    System.out.println("ddd_path:" + uris[0].toString());
//                    System.out.println("ddd_string:" + BaseImage64Utils.getRealFilePath(this, uris[0]));
//                    String base64 = BaseImage64Utils.fileToBase64(new File(BaseImage64Utils.getRealFilePath(this, uris[0])));
//                    System.out.println("ddd_base64:" + base64);
//                    File image = getExternalFilesDir("image");
//                    System.out.println("ddd_file:" + image.getPath());
//                    File imageFile = new File(image, System.currentTimeMillis() + ".jpg");
//                    try {
//                        imageFile.createNewFile();
//                        BaseImage64Utils.base64ToFile(base64, imageFile.getPath());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    Glide.with(iamgeIv)
                            .load(uris[0])
                            .into(iamgeIv);
                })
                .chooseImage(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileChooser.getInstance().onActivityResult(requestCode, resultCode, data);
    }


}