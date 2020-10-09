package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.R;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
  private int itemSpacing;
  
  private int lineSpacing;
  
  private boolean singleLine = false;
  
  public FlowLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    loadFromAttributes(paramContext, paramAttributeSet);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    loadFromAttributes(paramContext, paramAttributeSet);
  }
  
  private static int getMeasuredDimension(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt2 != Integer.MIN_VALUE) ? ((paramInt2 != 1073741824) ? paramInt3 : paramInt1) : Math.min(paramInt3, paramInt1);
  }
  
  private void loadFromAttributes(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.FlowLayout, 0, 0);
    this.lineSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
    this.itemSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_itemSpacing, 0);
    typedArray.recycle();
  }
  
  protected int getItemSpacing() {
    return this.itemSpacing;
  }
  
  protected int getLineSpacing() {
    return this.lineSpacing;
  }
  
  protected boolean isSingleLine() {
    return this.singleLine;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (getChildCount() == 0)
      return; 
    paramInt2 = ViewCompat.getLayoutDirection((View)this);
    boolean bool = true;
    if (paramInt2 != 1)
      bool = false; 
    if (bool) {
      paramInt2 = getPaddingRight();
    } else {
      paramInt2 = getPaddingLeft();
    } 
    if (bool) {
      i = getPaddingLeft();
    } else {
      i = getPaddingRight();
    } 
    paramInt4 = paramInt2;
    int j = getPaddingTop();
    int k = j;
    int m = paramInt3 - paramInt1 - i;
    int i = 0;
    paramInt1 = j;
    paramInt3 = paramInt4;
    while (i < getChildCount()) {
      View view = getChildAt(i);
      if (view.getVisibility() != 8) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        j = 0;
        int n = 0;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
          j = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
          n = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
        } 
        int i1 = view.getMeasuredWidth();
        int i2 = paramInt3;
        paramInt4 = paramInt1;
        if (!this.singleLine) {
          i2 = paramInt3;
          paramInt4 = paramInt1;
          if (paramInt3 + j + i1 > m) {
            i2 = paramInt2;
            paramInt4 = k + this.lineSpacing;
          } 
        } 
        paramInt1 = i2 + j + view.getMeasuredWidth();
        k = view.getMeasuredHeight() + paramInt4;
        if (bool) {
          view.layout(m - paramInt1, paramInt4, m - i2 - j, k);
        } else {
          view.layout(i2 + j, paramInt4, paramInt1, k);
        } 
        paramInt3 = i2 + j + n + view.getMeasuredWidth() + this.itemSpacing;
        paramInt1 = paramInt4;
      } 
      i++;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int n;
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getSize(paramInt2);
    int m = View.MeasureSpec.getMode(paramInt2);
    if (j == Integer.MIN_VALUE || j == 1073741824) {
      n = i;
    } else {
      n = Integer.MAX_VALUE;
    } 
    int i1 = getPaddingLeft();
    int i2 = getPaddingTop();
    int i3 = i2;
    int i4 = 0;
    int i5 = getPaddingRight();
    byte b = 0;
    int i6 = n;
    while (b < getChildCount()) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        measureChild(view, paramInt1, paramInt2);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i7 = 0;
        int i8 = 0;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
          i7 = 0 + marginLayoutParams.leftMargin;
          i8 = 0 + marginLayoutParams.rightMargin;
        } 
        if (i1 + i7 + view.getMeasuredWidth() > n - i5 && !isSingleLine()) {
          i1 = getPaddingLeft();
          i2 = this.lineSpacing + i3;
          i3 = i1;
        } else {
          i3 = i1;
        } 
        int i9 = i3 + i7 + view.getMeasuredWidth();
        int i10 = view.getMeasuredHeight();
        i1 = i4;
        if (i9 > i4)
          i1 = i9; 
        i7 = i3 + i7 + i8 + view.getMeasuredWidth() + this.itemSpacing;
        i3 = i10 + i2;
        i4 = i1;
        i1 = i7;
      } 
      b++;
    } 
    setMeasuredDimension(getMeasuredDimension(i, j, i4), getMeasuredDimension(k, m, i3));
  }
  
  protected void setItemSpacing(int paramInt) {
    this.itemSpacing = paramInt;
  }
  
  protected void setLineSpacing(int paramInt) {
    this.lineSpacing = paramInt;
  }
  
  public void setSingleLine(boolean paramBoolean) {
    this.singleLine = paramBoolean;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/FlowLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */