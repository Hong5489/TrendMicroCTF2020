package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class AlertDialogLayout extends LinearLayoutCompat {
  public AlertDialogLayout(Context paramContext) {
    super(paramContext);
  }
  
  public AlertDialogLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private static int resolveMinimumHeight(View paramView) {
    int i = ViewCompat.getMinimumHeight(paramView);
    if (i > 0)
      return i; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      if (viewGroup.getChildCount() == 1)
        return resolveMinimumHeight(viewGroup.getChildAt(0)); 
    } 
    return 0;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2) {
    View view1 = null;
    View view2 = null;
    View view3 = null;
    int i = getChildCount();
    int j;
    for (j = 0; j < i; j++) {
      View view = getChildAt(j);
      if (view.getVisibility() != 8) {
        int i8 = view.getId();
        if (i8 == R.id.topPanel) {
          view1 = view;
        } else if (i8 == R.id.buttonPanel) {
          view2 = view;
        } else if (i8 == R.id.contentPanel || i8 == R.id.customPanel) {
          if (view3 != null)
            return false; 
          view3 = view;
        } else {
          return false;
        } 
      } 
    } 
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt2);
    int i1 = View.MeasureSpec.getMode(paramInt1);
    int i2 = 0;
    j = getPaddingTop() + getPaddingBottom();
    int i3 = j;
    if (view1 != null) {
      view1.measure(paramInt1, 0);
      i3 = j + view1.getMeasuredHeight();
      i2 = View.combineMeasuredStates(0, view1.getMeasuredState());
    } 
    j = 0;
    int i4 = 0;
    int k = i2;
    int i5 = i3;
    if (view2 != null) {
      view2.measure(paramInt1, 0);
      j = resolveMinimumHeight(view2);
      i4 = view2.getMeasuredHeight() - j;
      i5 = i3 + j;
      k = View.combineMeasuredStates(i2, view2.getMeasuredState());
    } 
    int i6 = 0;
    if (view3 != null) {
      if (m == 0) {
        i3 = 0;
      } else {
        i3 = View.MeasureSpec.makeMeasureSpec(Math.max(0, n - i5), m);
      } 
      view3.measure(paramInt1, i3);
      i6 = view3.getMeasuredHeight();
      i5 += i6;
      k = View.combineMeasuredStates(k, view3.getMeasuredState());
    } 
    int i7 = n - i5;
    n = i7;
    i3 = k;
    i2 = i5;
    if (view2 != null) {
      n = Math.min(i7, i4);
      i3 = i7;
      i2 = j;
      if (n > 0) {
        i3 = i7 - n;
        i2 = j + n;
      } 
      view2.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
      i2 = i5 - j + view2.getMeasuredHeight();
      j = View.combineMeasuredStates(k, view2.getMeasuredState());
      n = i3;
      i3 = j;
    } 
    i5 = n;
    k = i3;
    j = i2;
    if (view3 != null) {
      i5 = n;
      k = i3;
      j = i2;
      if (n > 0) {
        view3.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i6 + n, m));
        j = i2 - i6 + view3.getMeasuredHeight();
        k = View.combineMeasuredStates(i3, view3.getMeasuredState());
        i5 = n - n;
      } 
    } 
    i3 = 0;
    i5 = 0;
    while (i5 < i) {
      View view = getChildAt(i5);
      i2 = i3;
      if (view.getVisibility() != 8)
        i2 = Math.max(i3, view.getMeasuredWidth()); 
      i5++;
      i3 = i2;
    } 
    setMeasuredDimension(View.resolveSizeAndState(i3 + getPaddingLeft() + getPaddingRight(), paramInt1, k), View.resolveSizeAndState(j, paramInt2, 0));
    if (i1 != 1073741824)
      forceUniformWidth(i, paramInt2); 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    paramInt1 = getMeasuredHeight();
    int n = getChildCount();
    int i1 = getGravity();
    paramInt3 = i1 & 0x70;
    if (paramInt3 != 16) {
      if (paramInt3 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - paramInt1;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - paramInt1) / 2;
    } 
    Drawable drawable = getDividerDrawable();
    if (drawable == null) {
      paramInt3 = 0;
    } else {
      paramInt3 = drawable.getIntrinsicHeight();
    } 
    paramInt4 = 0;
    while (true) {
      AlertDialogLayout alertDialogLayout = this;
      if (paramInt4 < n) {
        View view = alertDialogLayout.getChildAt(paramInt4);
        if (view != null && view.getVisibility() != 8) {
          int i2 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
          paramInt2 = layoutParams.gravity;
          if (paramInt2 < 0)
            paramInt2 = i1 & 0x800007; 
          paramInt2 = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt2 != 1) {
            if (paramInt2 != 5) {
              paramInt2 = layoutParams.leftMargin + i;
            } else {
              paramInt2 = j - k - i2 - layoutParams.rightMargin;
            } 
          } else {
            paramInt2 = (j - i - m - i2) / 2 + i + layoutParams.leftMargin - layoutParams.rightMargin;
          } 
          int i4 = paramInt1;
          if (alertDialogLayout.hasDividerBeforeChildAt(paramInt4))
            i4 = paramInt1 + paramInt3; 
          paramInt1 = i4 + layoutParams.topMargin;
          setChildFrame(view, paramInt2, paramInt1, i2, i3);
          paramInt1 += i3 + layoutParams.bottomMargin;
        } 
        paramInt4++;
        continue;
      } 
      break;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (!tryOnMeasure(paramInt1, paramInt2))
      super.onMeasure(paramInt1, paramInt2); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/AlertDialogLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */