package android.support.design.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
  private int baseline = -1;
  
  public BaselineLayout(Context paramContext) {
    super(paramContext, null, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public int getBaseline() {
    return this.baseline;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    int j = getPaddingLeft();
    int k = getPaddingRight();
    int m = getPaddingTop();
    for (paramInt2 = 0; paramInt2 < i; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        int n = view.getMeasuredWidth();
        int i1 = view.getMeasuredHeight();
        int i2 = (paramInt3 - paramInt1 - k - j - n) / 2 + j;
        if (this.baseline != -1 && view.getBaseline() != -1) {
          paramInt4 = this.baseline + m - view.getBaseline();
        } else {
          paramInt4 = m;
        } 
        view.layout(i2, paramInt4, i2 + n, paramInt4 + i1);
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = -1;
    int n = -1;
    int i1 = 0;
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        measureChild(view, paramInt1, paramInt2);
        int i3 = view.getBaseline();
        int i4 = m;
        int i5 = n;
        if (i3 != -1) {
          i4 = Math.max(m, i3);
          i5 = Math.max(n, view.getMeasuredHeight() - i3);
        } 
        j = Math.max(j, view.getMeasuredWidth());
        k = Math.max(k, view.getMeasuredHeight());
        i1 = View.combineMeasuredStates(i1, view.getMeasuredState());
        n = i5;
        m = i4;
      } 
    } 
    int i2 = k;
    if (m != -1) {
      i2 = Math.max(k, m + Math.max(n, getPaddingBottom()));
      this.baseline = m;
    } 
    k = Math.max(i2, getSuggestedMinimumHeight());
    n = Math.max(j, getSuggestedMinimumWidth());
    setMeasuredDimension(View.resolveSizeAndState(n, paramInt1, i1), View.resolveSizeAndState(k, paramInt2, i1 << 16));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/BaselineLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */