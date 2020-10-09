package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

public class ContentFrameLayout extends FrameLayout {
  private OnAttachListener mAttachListener;
  
  private final Rect mDecorPadding = new Rect();
  
  private TypedValue mFixedHeightMajor;
  
  private TypedValue mFixedHeightMinor;
  
  private TypedValue mFixedWidthMajor;
  
  private TypedValue mFixedWidthMinor;
  
  private TypedValue mMinWidthMajor;
  
  private TypedValue mMinWidthMinor;
  
  public ContentFrameLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void dispatchFitSystemWindows(Rect paramRect) {
    fitSystemWindows(paramRect);
  }
  
  public TypedValue getFixedHeightMajor() {
    if (this.mFixedHeightMajor == null)
      this.mFixedHeightMajor = new TypedValue(); 
    return this.mFixedHeightMajor;
  }
  
  public TypedValue getFixedHeightMinor() {
    if (this.mFixedHeightMinor == null)
      this.mFixedHeightMinor = new TypedValue(); 
    return this.mFixedHeightMinor;
  }
  
  public TypedValue getFixedWidthMajor() {
    if (this.mFixedWidthMajor == null)
      this.mFixedWidthMajor = new TypedValue(); 
    return this.mFixedWidthMajor;
  }
  
  public TypedValue getFixedWidthMinor() {
    if (this.mFixedWidthMinor == null)
      this.mFixedWidthMinor = new TypedValue(); 
    return this.mFixedWidthMinor;
  }
  
  public TypedValue getMinWidthMajor() {
    if (this.mMinWidthMajor == null)
      this.mMinWidthMajor = new TypedValue(); 
    return this.mMinWidthMajor;
  }
  
  public TypedValue getMinWidthMinor() {
    if (this.mMinWidthMinor == null)
      this.mMinWidthMinor = new TypedValue(); 
    return this.mMinWidthMinor;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    OnAttachListener onAttachListener = this.mAttachListener;
    if (onAttachListener != null)
      onAttachListener.onAttachedFromWindow(); 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    OnAttachListener onAttachListener = this.mAttachListener;
    if (onAttachListener != null)
      onAttachListener.onDetachedFromWindow(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i;
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = 0;
    int n = m;
    int i1 = paramInt1;
    if (j == Integer.MIN_VALUE) {
      TypedValue typedValue;
      if (i) {
        typedValue = this.mFixedWidthMinor;
      } else {
        typedValue = this.mFixedWidthMajor;
      } 
      n = m;
      i1 = paramInt1;
      if (typedValue != null) {
        n = m;
        i1 = paramInt1;
        if (typedValue.type != 0) {
          int i3 = 0;
          if (typedValue.type == 5) {
            i3 = (int)typedValue.getDimension(displayMetrics);
          } else if (typedValue.type == 6) {
            i3 = (int)typedValue.getFraction(displayMetrics.widthPixels, displayMetrics.widthPixels);
          } 
          n = m;
          i1 = paramInt1;
          if (i3 > 0) {
            n = this.mDecorPadding.left;
            i1 = this.mDecorPadding.right;
            paramInt1 = View.MeasureSpec.getSize(paramInt1);
            i1 = View.MeasureSpec.makeMeasureSpec(Math.min(i3 - n + i1, paramInt1), 1073741824);
            n = 1;
          } 
        } 
      } 
    } 
    int i2 = paramInt2;
    if (k == Integer.MIN_VALUE) {
      TypedValue typedValue;
      if (i) {
        typedValue = this.mFixedHeightMajor;
      } else {
        typedValue = this.mFixedHeightMinor;
      } 
      i2 = paramInt2;
      if (typedValue != null) {
        i2 = paramInt2;
        if (typedValue.type != 0) {
          paramInt1 = 0;
          if (typedValue.type == 5) {
            paramInt1 = (int)typedValue.getDimension(displayMetrics);
          } else if (typedValue.type == 6) {
            paramInt1 = (int)typedValue.getFraction(displayMetrics.heightPixels, displayMetrics.heightPixels);
          } 
          i2 = paramInt2;
          if (paramInt1 > 0) {
            i2 = this.mDecorPadding.top;
            m = this.mDecorPadding.bottom;
            paramInt2 = View.MeasureSpec.getSize(paramInt2);
            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(paramInt1 - i2 + m, paramInt2), 1073741824);
          } 
        } 
      } 
    } 
    super.onMeasure(i1, i2);
    k = getMeasuredWidth();
    m = 0;
    i1 = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
    paramInt2 = m;
    paramInt1 = i1;
    if (n == 0) {
      paramInt2 = m;
      paramInt1 = i1;
      if (j == Integer.MIN_VALUE) {
        TypedValue typedValue;
        if (i) {
          typedValue = this.mMinWidthMinor;
        } else {
          typedValue = this.mMinWidthMajor;
        } 
        paramInt2 = m;
        paramInt1 = i1;
        if (typedValue != null) {
          paramInt2 = m;
          paramInt1 = i1;
          if (typedValue.type != 0) {
            paramInt1 = 0;
            if (typedValue.type == 5) {
              paramInt1 = (int)typedValue.getDimension(displayMetrics);
            } else if (typedValue.type == 6) {
              paramInt1 = (int)typedValue.getFraction(displayMetrics.widthPixels, displayMetrics.widthPixels);
            } 
            i = paramInt1;
            if (paramInt1 > 0)
              i = paramInt1 - this.mDecorPadding.left + this.mDecorPadding.right; 
            paramInt2 = m;
            paramInt1 = i1;
            if (k < i) {
              paramInt1 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
              paramInt2 = 1;
            } 
          } 
        } 
      } 
    } 
    if (paramInt2 != 0)
      super.onMeasure(paramInt1, i2); 
  }
  
  public void setAttachListener(OnAttachListener paramOnAttachListener) {
    this.mAttachListener = paramOnAttachListener;
  }
  
  public void setDecorPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mDecorPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    if (ViewCompat.isLaidOut((View)this))
      requestLayout(); 
  }
  
  public static interface OnAttachListener {
    void onAttachedFromWindow();
    
    void onDetachedFromWindow();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/ContentFrameLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */