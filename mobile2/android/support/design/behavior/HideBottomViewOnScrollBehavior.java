package android.support.design.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class HideBottomViewOnScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  protected static final int ENTER_ANIMATION_DURATION = 225;
  
  protected static final int EXIT_ANIMATION_DURATION = 175;
  
  private static final int STATE_SCROLLED_DOWN = 1;
  
  private static final int STATE_SCROLLED_UP = 2;
  
  private ViewPropertyAnimator currentAnimator;
  
  private int currentState = 2;
  
  private int height = 0;
  
  public HideBottomViewOnScrollBehavior() {}
  
  public HideBottomViewOnScrollBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void animateChildTo(V paramV, int paramInt, long paramLong, TimeInterpolator paramTimeInterpolator) {
    this.currentAnimator = paramV.animate().translationY(paramInt).setInterpolator(paramTimeInterpolator).setDuration(paramLong).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            HideBottomViewOnScrollBehavior.access$002(HideBottomViewOnScrollBehavior.this, null);
          }
        });
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    this.height = paramV.getMeasuredHeight();
    return super.onLayoutChild(paramCoordinatorLayout, (View)paramV, paramInt);
  }
  
  public void onNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.currentState != 1 && paramInt2 > 0) {
      slideDown(paramV);
    } else if (this.currentState != 2 && paramInt2 < 0) {
      slideUp(paramV);
    } 
  }
  
  public boolean onStartNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView1, View paramView2, int paramInt) {
    boolean bool;
    if (paramInt == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void slideDown(V paramV) {
    ViewPropertyAnimator viewPropertyAnimator = this.currentAnimator;
    if (viewPropertyAnimator != null) {
      viewPropertyAnimator.cancel();
      paramV.clearAnimation();
    } 
    this.currentState = 1;
    animateChildTo(paramV, this.height, 175L, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
  }
  
  protected void slideUp(V paramV) {
    ViewPropertyAnimator viewPropertyAnimator = this.currentAnimator;
    if (viewPropertyAnimator != null) {
      viewPropertyAnimator.cancel();
      paramV.clearAnimation();
    } 
    this.currentState = 2;
    animateChildTo(paramV, 0, 225L, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/behavior/HideBottomViewOnScrollBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */