package api29.libs.adapter.base.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Create by JFZ
 * date: 2020-05-06 19:00
 **/
public class SlideInBottomAnimation implements BaseAnimation {
    @Override
    public Animator[] animators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
        };
    }
}
