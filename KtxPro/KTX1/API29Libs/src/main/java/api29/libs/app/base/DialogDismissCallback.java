package api29.libs.app.base;


public interface DialogDismissCallback {

    interface BaseDialogCall {
        void dismissCall(BaseDialog dialog, int current);
    }

    interface BaseDecorDialogCall {
        void dismissCall(BaseDecorDialog dialog, int current);
    }

}
