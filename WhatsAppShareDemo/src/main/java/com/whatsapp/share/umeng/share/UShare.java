package com.whatsapp.share.umeng.share;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

public class UShare {
    public static void shareText(Activity acivity, String text, UMShareListener listener) {
        if (acivity == null) return;
        ShareAction action = new ShareAction(acivity);
        action.setPlatform(SHARE_MEDIA.WHATSAPP)
                .withText(text + "")
                .setCallback(listener)
                .share();
    }

    public static void shareWebImage(Activity acivity, String imageUrl, UMShareListener listener) {
        if (acivity == null) return;
        ShareAction action = new ShareAction(acivity);
        action.setPlatform(SHARE_MEDIA.WHATSAPP)
                .withMedia(new UMImage(acivity, imageUrl))
                .setCallback(listener)
                .share();
    }

    public static void shareFileImage(Activity acivity, File file, UMShareListener listener) {
        if (acivity == null) return;
        ShareAction action = new ShareAction(acivity);
        action.setPlatform(SHARE_MEDIA.WHATSAPP)
                .withMedia(new UMImage(acivity, file))
                .setCallback(listener)
                .share();
    }
}
