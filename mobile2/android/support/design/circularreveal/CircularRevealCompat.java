package android.support.design.circularreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

public final class CircularRevealCompat {
  public static Animator createCircularReveal(CircularRevealWidget paramCircularRevealWidget, float paramFloat1, float paramFloat2, float paramFloat3) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofObject(paramCircularRevealWidget, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, (Object[])new CircularRevealWidget.RevealInfo[] { new CircularRevealWidget.RevealInfo(paramFloat1, paramFloat2, paramFloat3) });
    if (Build.VERSION.SDK_INT >= 21) {
      CircularRevealWidget.RevealInfo revealInfo = paramCircularRevealWidget.getRevealInfo();
      if (revealInfo != null) {
        float f = revealInfo.radius;
        Animator animator = ViewAnimationUtils.createCircularReveal((View)paramCircularRevealWidget, (int)paramFloat1, (int)paramFloat2, f, paramFloat3);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[] { (Animator)objectAnimator, animator });
        return (Animator)animatorSet;
      } 
      throw new IllegalStateException("Caller must set a non-null RevealInfo before calling this.");
    } 
    return (Animator)objectAnimator;
  }
  
  public static Animator createCircularReveal(CircularRevealWidget paramCircularRevealWidget, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofObject(paramCircularRevealWidget, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, (Object[])new CircularRevealWidget.RevealInfo[] { new CircularRevealWidget.RevealInfo(paramFloat1, paramFloat2, paramFloat3), new CircularRevealWidget.RevealInfo(paramFloat1, paramFloat2, paramFloat4) });
    if (Build.VERSION.SDK_INT >= 21) {
      Animator animator = ViewAnimationUtils.createCircularReveal((View)paramCircularRevealWidget, (int)paramFloat1, (int)paramFloat2, paramFloat3, paramFloat4);
      AnimatorSet animatorSet = new AnimatorSet();
      animatorSet.playTogether(new Animator[] { (Animator)objectAnimator, animator });
      return (Animator)animatorSet;
    } 
    return (Animator)objectAnimator;
  }
  
  public static Animator.AnimatorListener createCircularRevealListener(final CircularRevealWidget view) {
    return (Animator.AnimatorListener)new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator param1Animator) {
          view.destroyCircularRevealCache();
        }
        
        public void onAnimationStart(Animator param1Animator) {
          view.buildCircularRevealCache();
        }
      };
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/circularreveal/CircularRevealCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */