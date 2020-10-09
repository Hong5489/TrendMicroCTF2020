package android.support.design.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.animation.AnimatorSetCompat;
import android.support.design.animation.MotionTiming;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class FabTransformationScrimBehavior extends ExpandableTransformationBehavior {
  public static final long COLLAPSE_DELAY = 0L;
  
  public static final long COLLAPSE_DURATION = 150L;
  
  public static final long EXPAND_DELAY = 75L;
  
  public static final long EXPAND_DURATION = 150L;
  
  private final MotionTiming collapseTiming = new MotionTiming(0L, 150L);
  
  private final MotionTiming expandTiming = new MotionTiming(75L, 150L);
  
  public FabTransformationScrimBehavior() {}
  
  public FabTransformationScrimBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void createScrimAnimation(View paramView, boolean paramBoolean1, boolean paramBoolean2, List<Animator> paramList, List<Animator.AnimatorListener> paramList1) {
    ObjectAnimator objectAnimator;
    MotionTiming motionTiming;
    if (paramBoolean1) {
      motionTiming = this.expandTiming;
    } else {
      motionTiming = this.collapseTiming;
    } 
    if (paramBoolean1) {
      if (!paramBoolean2)
        paramView.setAlpha(0.0F); 
      objectAnimator = ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { 1.0F });
    } else {
      objectAnimator = ObjectAnimator.ofFloat(objectAnimator, View.ALPHA, new float[] { 0.0F });
    } 
    motionTiming.apply((Animator)objectAnimator);
    paramList.add(objectAnimator);
  }
  
  public boolean layoutDependsOn(CoordinatorLayout paramCoordinatorLayout, View paramView1, View paramView2) {
    return paramView2 instanceof android.support.design.widget.FloatingActionButton;
  }
  
  protected AnimatorSet onCreateExpandedStateChangeAnimation(View paramView1, final View child, final boolean expanded, boolean paramBoolean2) {
    ArrayList<Animator> arrayList = new ArrayList();
    createScrimAnimation(child, expanded, paramBoolean2, arrayList, new ArrayList<>());
    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSetCompat.playTogether(animatorSet, arrayList);
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            if (!expanded)
              child.setVisibility(4); 
          }
          
          public void onAnimationStart(Animator param1Animator) {
            if (expanded)
              child.setVisibility(0); 
          }
        });
    return animatorSet;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, View paramView, MotionEvent paramMotionEvent) {
    return super.onTouchEvent(paramCoordinatorLayout, paramView, paramMotionEvent);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/transformation/FabTransformationScrimBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */