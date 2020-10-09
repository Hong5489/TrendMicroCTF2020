package android.support.transition;

import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

public class SidePropagation extends VisibilityPropagation {
  private float mPropagationSpeed = 3.0F;
  
  private int mSide = 80;
  
  private int distance(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
    int i = this.mSide;
    byte b = 0;
    int j = 0;
    if (i == 8388611) {
      if (ViewCompat.getLayoutDirection(paramView) == 1)
        j = 1; 
      if (j) {
        j = 5;
      } else {
        j = 3;
      } 
    } else if (i == 8388613) {
      j = b;
      if (ViewCompat.getLayoutDirection(paramView) == 1)
        j = 1; 
      if (j != 0) {
        j = 3;
      } else {
        j = 5;
      } 
    } else {
      j = this.mSide;
    } 
    b = 0;
    if (j != 3) {
      if (j != 5) {
        if (j != 48) {
          if (j != 80) {
            paramInt1 = b;
          } else {
            paramInt1 = paramInt2 - paramInt6 + Math.abs(paramInt3 - paramInt1);
          } 
        } else {
          paramInt1 = paramInt8 - paramInt2 + Math.abs(paramInt3 - paramInt1);
        } 
      } else {
        paramInt1 = paramInt1 - paramInt5 + Math.abs(paramInt4 - paramInt2);
      } 
    } else {
      paramInt1 = paramInt7 - paramInt1 + Math.abs(paramInt4 - paramInt2);
    } 
    return paramInt1;
  }
  
  private int getMaxDistance(ViewGroup paramViewGroup) {
    int i = this.mSide;
    return (i != 3 && i != 5 && i != 8388611 && i != 8388613) ? paramViewGroup.getHeight() : paramViewGroup.getWidth();
  }
  
  public long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool;
    int i2;
    int i3;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return 0L; 
    Rect rect = paramTransition.getEpicenter();
    if (paramTransitionValues2 == null || getViewVisibility(paramTransitionValues1) == 0) {
      bool = true;
    } else {
      bool = true;
      paramTransitionValues1 = paramTransitionValues2;
    } 
    int i = getViewX(paramTransitionValues1);
    int j = getViewY(paramTransitionValues1);
    int[] arrayOfInt = new int[2];
    paramViewGroup.getLocationOnScreen(arrayOfInt);
    int k = arrayOfInt[0] + Math.round(paramViewGroup.getTranslationX());
    int m = arrayOfInt[1] + Math.round(paramViewGroup.getTranslationY());
    int n = k + paramViewGroup.getWidth();
    int i1 = m + paramViewGroup.getHeight();
    if (rect != null) {
      i2 = rect.centerX();
      i3 = rect.centerY();
    } else {
      i2 = (k + n) / 2;
      i3 = (m + i1) / 2;
    } 
    float f = distance((View)paramViewGroup, i, j, i2, i3, k, m, n, i1) / getMaxDistance(paramViewGroup);
    long l1 = paramTransition.getDuration();
    long l2 = l1;
    if (l1 < 0L)
      l2 = 300L; 
    return Math.round((float)(bool * l2) / this.mPropagationSpeed * f);
  }
  
  public void setPropagationSpeed(float paramFloat) {
    if (paramFloat != 0.0F) {
      this.mPropagationSpeed = paramFloat;
      return;
    } 
    throw new IllegalArgumentException("propagationSpeed may not be 0");
  }
  
  public void setSide(int paramInt) {
    this.mSide = paramInt;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/SidePropagation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */