package android.support.design.button;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.design.R;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

class MaterialButtonHelper {
  private static final float CORNER_RADIUS_ADJUSTMENT = 1.0E-5F;
  
  private static final int DEFAULT_BACKGROUND_COLOR = -1;
  
  private static final boolean IS_LOLLIPOP;
  
  private GradientDrawable backgroundDrawableLollipop;
  
  private boolean backgroundOverwritten = false;
  
  private ColorStateList backgroundTint;
  
  private PorterDuff.Mode backgroundTintMode;
  
  private final Rect bounds = new Rect();
  
  private final Paint buttonStrokePaint = new Paint(1);
  
  private GradientDrawable colorableBackgroundDrawableCompat;
  
  private int cornerRadius;
  
  private int insetBottom;
  
  private int insetLeft;
  
  private int insetRight;
  
  private int insetTop;
  
  private GradientDrawable maskDrawableLollipop;
  
  private final MaterialButton materialButton;
  
  private final RectF rectF = new RectF();
  
  private ColorStateList rippleColor;
  
  private GradientDrawable rippleDrawableCompat;
  
  private ColorStateList strokeColor;
  
  private GradientDrawable strokeDrawableLollipop;
  
  private int strokeWidth;
  
  private Drawable tintableBackgroundDrawableCompat;
  
  private Drawable tintableRippleDrawableCompat;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_LOLLIPOP = bool;
  }
  
  public MaterialButtonHelper(MaterialButton paramMaterialButton) {
    this.materialButton = paramMaterialButton;
  }
  
  private Drawable createBackgroundCompat() {
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    this.colorableBackgroundDrawableCompat = gradientDrawable2;
    gradientDrawable2.setCornerRadius(this.cornerRadius + 1.0E-5F);
    this.colorableBackgroundDrawableCompat.setColor(-1);
    Drawable drawable2 = DrawableCompat.wrap((Drawable)this.colorableBackgroundDrawableCompat);
    this.tintableBackgroundDrawableCompat = drawable2;
    DrawableCompat.setTintList(drawable2, this.backgroundTint);
    PorterDuff.Mode mode = this.backgroundTintMode;
    if (mode != null)
      DrawableCompat.setTintMode(this.tintableBackgroundDrawableCompat, mode); 
    GradientDrawable gradientDrawable1 = new GradientDrawable();
    this.rippleDrawableCompat = gradientDrawable1;
    gradientDrawable1.setCornerRadius(this.cornerRadius + 1.0E-5F);
    this.rippleDrawableCompat.setColor(-1);
    Drawable drawable1 = DrawableCompat.wrap((Drawable)this.rippleDrawableCompat);
    this.tintableRippleDrawableCompat = drawable1;
    DrawableCompat.setTintList(drawable1, this.rippleColor);
    return (Drawable)wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[] { this.tintableBackgroundDrawableCompat, this.tintableRippleDrawableCompat }));
  }
  
  private Drawable createBackgroundLollipop() {
    GradientDrawable gradientDrawable1 = new GradientDrawable();
    this.backgroundDrawableLollipop = gradientDrawable1;
    gradientDrawable1.setCornerRadius(this.cornerRadius + 1.0E-5F);
    this.backgroundDrawableLollipop.setColor(-1);
    updateTintAndTintModeLollipop();
    gradientDrawable1 = new GradientDrawable();
    this.strokeDrawableLollipop = gradientDrawable1;
    gradientDrawable1.setCornerRadius(this.cornerRadius + 1.0E-5F);
    this.strokeDrawableLollipop.setColor(0);
    this.strokeDrawableLollipop.setStroke(this.strokeWidth, this.strokeColor);
    InsetDrawable insetDrawable = wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[] { (Drawable)this.backgroundDrawableLollipop, (Drawable)this.strokeDrawableLollipop }));
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    this.maskDrawableLollipop = gradientDrawable2;
    gradientDrawable2.setCornerRadius(this.cornerRadius + 1.0E-5F);
    this.maskDrawableLollipop.setColor(-1);
    return (Drawable)new MaterialButtonBackgroundDrawable(RippleUtils.convertToRippleDrawableColor(this.rippleColor), insetDrawable, (Drawable)this.maskDrawableLollipop);
  }
  
  private GradientDrawable unwrapBackgroundDrawable() {
    return (IS_LOLLIPOP && this.materialButton.getBackground() != null) ? (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(0) : null;
  }
  
  private GradientDrawable unwrapStrokeDrawable() {
    return (IS_LOLLIPOP && this.materialButton.getBackground() != null) ? (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(1) : null;
  }
  
  private void updateStroke() {
    if (IS_LOLLIPOP && this.strokeDrawableLollipop != null) {
      this.materialButton.setInternalBackground(createBackgroundLollipop());
    } else if (!IS_LOLLIPOP) {
      this.materialButton.invalidate();
    } 
  }
  
  private void updateTintAndTintModeLollipop() {
    GradientDrawable gradientDrawable = this.backgroundDrawableLollipop;
    if (gradientDrawable != null) {
      DrawableCompat.setTintList((Drawable)gradientDrawable, this.backgroundTint);
      PorterDuff.Mode mode = this.backgroundTintMode;
      if (mode != null)
        DrawableCompat.setTintMode((Drawable)this.backgroundDrawableLollipop, mode); 
    } 
  }
  
  private InsetDrawable wrapDrawableWithInset(Drawable paramDrawable) {
    return new InsetDrawable(paramDrawable, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
  }
  
  void drawStroke(Canvas paramCanvas) {
    if (paramCanvas != null && this.strokeColor != null && this.strokeWidth > 0) {
      this.bounds.set(this.materialButton.getBackground().getBounds());
      this.rectF.set(this.bounds.left + this.strokeWidth / 2.0F + this.insetLeft, this.bounds.top + this.strokeWidth / 2.0F + this.insetTop, this.bounds.right - this.strokeWidth / 2.0F - this.insetRight, this.bounds.bottom - this.strokeWidth / 2.0F - this.insetBottom);
      float f = this.cornerRadius - this.strokeWidth / 2.0F;
      paramCanvas.drawRoundRect(this.rectF, f, f, this.buttonStrokePaint);
    } 
  }
  
  int getCornerRadius() {
    return this.cornerRadius;
  }
  
  ColorStateList getRippleColor() {
    return this.rippleColor;
  }
  
  ColorStateList getStrokeColor() {
    return this.strokeColor;
  }
  
  int getStrokeWidth() {
    return this.strokeWidth;
  }
  
  ColorStateList getSupportBackgroundTintList() {
    return this.backgroundTint;
  }
  
  PorterDuff.Mode getSupportBackgroundTintMode() {
    return this.backgroundTintMode;
  }
  
  boolean isBackgroundOverwritten() {
    return this.backgroundOverwritten;
  }
  
  public void loadFromAttributes(TypedArray paramTypedArray) {
    Drawable drawable;
    int i = R.styleable.MaterialButton_android_insetLeft;
    int j = 0;
    this.insetLeft = paramTypedArray.getDimensionPixelOffset(i, 0);
    this.insetRight = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
    this.insetTop = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
    this.insetBottom = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
    this.cornerRadius = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, 0);
    this.strokeWidth = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
    this.backgroundTintMode = ViewUtils.parseTintMode(paramTypedArray.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN);
    this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), paramTypedArray, R.styleable.MaterialButton_backgroundTint);
    this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), paramTypedArray, R.styleable.MaterialButton_strokeColor);
    this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), paramTypedArray, R.styleable.MaterialButton_rippleColor);
    this.buttonStrokePaint.setStyle(Paint.Style.STROKE);
    this.buttonStrokePaint.setStrokeWidth(this.strokeWidth);
    Paint paint = this.buttonStrokePaint;
    ColorStateList colorStateList = this.strokeColor;
    if (colorStateList != null)
      j = colorStateList.getColorForState(this.materialButton.getDrawableState(), 0); 
    paint.setColor(j);
    int k = ViewCompat.getPaddingStart((View)this.materialButton);
    i = this.materialButton.getPaddingTop();
    int m = ViewCompat.getPaddingEnd((View)this.materialButton);
    j = this.materialButton.getPaddingBottom();
    MaterialButton materialButton = this.materialButton;
    if (IS_LOLLIPOP) {
      drawable = createBackgroundLollipop();
    } else {
      drawable = createBackgroundCompat();
    } 
    materialButton.setInternalBackground(drawable);
    ViewCompat.setPaddingRelative((View)this.materialButton, this.insetLeft + k, this.insetTop + i, this.insetRight + m, this.insetBottom + j);
  }
  
  void setBackgroundColor(int paramInt) {
    if (IS_LOLLIPOP) {
      GradientDrawable gradientDrawable = this.backgroundDrawableLollipop;
      if (gradientDrawable != null) {
        gradientDrawable.setColor(paramInt);
        return;
      } 
    } 
    if (!IS_LOLLIPOP) {
      GradientDrawable gradientDrawable = this.colorableBackgroundDrawableCompat;
      if (gradientDrawable != null)
        gradientDrawable.setColor(paramInt); 
    } 
  }
  
  void setBackgroundOverwritten() {
    this.backgroundOverwritten = true;
    this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
    this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
  }
  
  void setCornerRadius(int paramInt) {
    if (this.cornerRadius != paramInt) {
      this.cornerRadius = paramInt;
      if (IS_LOLLIPOP && this.backgroundDrawableLollipop != null && this.strokeDrawableLollipop != null && this.maskDrawableLollipop != null) {
        if (Build.VERSION.SDK_INT == 21) {
          unwrapBackgroundDrawable().setCornerRadius(paramInt + 1.0E-5F);
          unwrapStrokeDrawable().setCornerRadius(paramInt + 1.0E-5F);
        } 
        this.backgroundDrawableLollipop.setCornerRadius(paramInt + 1.0E-5F);
        this.strokeDrawableLollipop.setCornerRadius(paramInt + 1.0E-5F);
        this.maskDrawableLollipop.setCornerRadius(paramInt + 1.0E-5F);
      } else if (!IS_LOLLIPOP) {
        GradientDrawable gradientDrawable = this.colorableBackgroundDrawableCompat;
        if (gradientDrawable != null && this.rippleDrawableCompat != null) {
          gradientDrawable.setCornerRadius(paramInt + 1.0E-5F);
          this.rippleDrawableCompat.setCornerRadius(paramInt + 1.0E-5F);
          this.materialButton.invalidate();
        } 
      } 
    } 
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    if (this.rippleColor != paramColorStateList) {
      this.rippleColor = paramColorStateList;
      if (IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawable) {
        ((RippleDrawable)this.materialButton.getBackground()).setColor(paramColorStateList);
      } else if (!IS_LOLLIPOP) {
        Drawable drawable = this.tintableRippleDrawableCompat;
        if (drawable != null)
          DrawableCompat.setTintList(drawable, paramColorStateList); 
      } 
    } 
  }
  
  void setStrokeColor(ColorStateList paramColorStateList) {
    if (this.strokeColor != paramColorStateList) {
      this.strokeColor = paramColorStateList;
      Paint paint = this.buttonStrokePaint;
      int i = 0;
      if (paramColorStateList != null)
        i = paramColorStateList.getColorForState(this.materialButton.getDrawableState(), 0); 
      paint.setColor(i);
      updateStroke();
    } 
  }
  
  void setStrokeWidth(int paramInt) {
    if (this.strokeWidth != paramInt) {
      this.strokeWidth = paramInt;
      this.buttonStrokePaint.setStrokeWidth(paramInt);
      updateStroke();
    } 
  }
  
  void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    if (this.backgroundTint != paramColorStateList) {
      this.backgroundTint = paramColorStateList;
      if (IS_LOLLIPOP) {
        updateTintAndTintModeLollipop();
      } else {
        Drawable drawable = this.tintableBackgroundDrawableCompat;
        if (drawable != null)
          DrawableCompat.setTintList(drawable, paramColorStateList); 
      } 
    } 
  }
  
  void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    if (this.backgroundTintMode != paramMode) {
      this.backgroundTintMode = paramMode;
      if (IS_LOLLIPOP) {
        updateTintAndTintModeLollipop();
      } else {
        Drawable drawable = this.tintableBackgroundDrawableCompat;
        if (drawable != null && paramMode != null)
          DrawableCompat.setTintMode(drawable, paramMode); 
      } 
    } 
  }
  
  void updateMaskBounds(int paramInt1, int paramInt2) {
    GradientDrawable gradientDrawable = this.maskDrawableLollipop;
    if (gradientDrawable != null)
      gradientDrawable.setBounds(this.insetLeft, this.insetTop, paramInt2 - this.insetRight, paramInt1 - this.insetBottom); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/button/MaterialButtonHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */