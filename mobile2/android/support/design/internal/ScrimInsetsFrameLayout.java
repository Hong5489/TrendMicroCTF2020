package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class ScrimInsetsFrameLayout extends FrameLayout {
  Drawable insetForeground;
  
  Rect insets;
  
  private Rect tempRect = new Rect();
  
  public ScrimInsetsFrameLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ScrimInsetsFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ScrimInsetsFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.ScrimInsetsFrameLayout, paramInt, R.style.Widget_Design_ScrimInsetsFrameLayout, new int[0]);
    this.insetForeground = typedArray.getDrawable(R.styleable.ScrimInsetsFrameLayout_insetForeground);
    typedArray.recycle();
    setWillNotDraw(true);
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            if (ScrimInsetsFrameLayout.this.insets == null)
              ScrimInsetsFrameLayout.this.insets = new Rect(); 
            ScrimInsetsFrameLayout.this.insets.set(param1WindowInsetsCompat.getSystemWindowInsetLeft(), param1WindowInsetsCompat.getSystemWindowInsetTop(), param1WindowInsetsCompat.getSystemWindowInsetRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom());
            ScrimInsetsFrameLayout.this.onInsetsChanged(param1WindowInsetsCompat);
            ScrimInsetsFrameLayout scrimInsetsFrameLayout = ScrimInsetsFrameLayout.this;
            if (!param1WindowInsetsCompat.hasSystemWindowInsets() || ScrimInsetsFrameLayout.this.insetForeground == null) {
              boolean bool1 = true;
              scrimInsetsFrameLayout.setWillNotDraw(bool1);
              ViewCompat.postInvalidateOnAnimation((View)ScrimInsetsFrameLayout.this);
              return param1WindowInsetsCompat.consumeSystemWindowInsets();
            } 
            boolean bool = false;
            scrimInsetsFrameLayout.setWillNotDraw(bool);
            ViewCompat.postInvalidateOnAnimation((View)ScrimInsetsFrameLayout.this);
            return param1WindowInsetsCompat.consumeSystemWindowInsets();
          }
        });
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    int i = getWidth();
    int j = getHeight();
    if (this.insets != null && this.insetForeground != null) {
      int k = paramCanvas.save();
      paramCanvas.translate(getScrollX(), getScrollY());
      this.tempRect.set(0, 0, i, this.insets.top);
      this.insetForeground.setBounds(this.tempRect);
      this.insetForeground.draw(paramCanvas);
      this.tempRect.set(0, j - this.insets.bottom, i, j);
      this.insetForeground.setBounds(this.tempRect);
      this.insetForeground.draw(paramCanvas);
      this.tempRect.set(0, this.insets.top, this.insets.left, j - this.insets.bottom);
      this.insetForeground.setBounds(this.tempRect);
      this.insetForeground.draw(paramCanvas);
      this.tempRect.set(i - this.insets.right, this.insets.top, i, j - this.insets.bottom);
      this.insetForeground.setBounds(this.tempRect);
      this.insetForeground.draw(paramCanvas);
      paramCanvas.restoreToCount(k);
    } 
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Drawable drawable = this.insetForeground;
    if (drawable != null)
      drawable.setCallback((Drawable.Callback)this); 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    Drawable drawable = this.insetForeground;
    if (drawable != null)
      drawable.setCallback(null); 
  }
  
  protected void onInsetsChanged(WindowInsetsCompat paramWindowInsetsCompat) {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/ScrimInsetsFrameLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */