package android.support.design.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Map;

public class TextScale extends Transition {
  private static final String PROPNAME_SCALE = "android:textscale:scale";
  
  private void captureValues(TransitionValues paramTransitionValues) {
    if (paramTransitionValues.view instanceof TextView) {
      TextView textView = (TextView)paramTransitionValues.view;
      paramTransitionValues.values.put("android:textscale:scale", Float.valueOf(textView.getScaleX()));
    } 
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    float f2;
    if (paramTransitionValues1 == null || paramTransitionValues2 == null || !(paramTransitionValues1.view instanceof TextView) || !(paramTransitionValues2.view instanceof TextView))
      return null; 
    final TextView view = (TextView)paramTransitionValues2.view;
    Map map1 = paramTransitionValues1.values;
    Map map2 = paramTransitionValues2.values;
    paramTransitionValues2 = (TransitionValues)map1.get("android:textscale:scale");
    float f1 = 1.0F;
    if (paramTransitionValues2 != null) {
      f2 = ((Float)map1.get("android:textscale:scale")).floatValue();
    } else {
      f2 = 1.0F;
    } 
    if (map2.get("android:textscale:scale") != null)
      f1 = ((Float)map2.get("android:textscale:scale")).floatValue(); 
    if (f2 == f1)
      return null; 
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { f2, f1 });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            view.setScaleX(f);
            view.setScaleY(f);
          }
        });
    return (Animator)valueAnimator;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/TextScale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */