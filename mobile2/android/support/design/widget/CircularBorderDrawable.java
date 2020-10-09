package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;

public class CircularBorderDrawable extends Drawable {
  private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333F;
  
  private ColorStateList borderTint;
  
  float borderWidth;
  
  private int bottomInnerStrokeColor;
  
  private int bottomOuterStrokeColor;
  
  private int currentBorderTintColor;
  
  private boolean invalidateShader = true;
  
  final Paint paint;
  
  final Rect rect = new Rect();
  
  final RectF rectF = new RectF();
  
  private float rotation;
  
  final CircularBorderState state = new CircularBorderState();
  
  private int topInnerStrokeColor;
  
  private int topOuterStrokeColor;
  
  public CircularBorderDrawable() {
    Paint paint = new Paint(1);
    this.paint = paint;
    paint.setStyle(Paint.Style.STROKE);
  }
  
  private Shader createGradientShader() {
    Rect rect = this.rect;
    copyBounds(rect);
    float f1 = this.borderWidth / rect.height();
    int i = ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor);
    int j = ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor);
    int k = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor);
    int m = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor);
    int n = ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor);
    int i1 = ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor);
    float f2 = rect.top;
    float f3 = rect.bottom;
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
    return (Shader)new LinearGradient(0.0F, f2, 0.0F, f3, new int[] { i, j, k, m, n, i1 }, new float[] { 0.0F, f1, 0.5F, 0.5F, 1.0F - f1, 1.0F }, tileMode);
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.invalidateShader) {
      this.paint.setShader(createGradientShader());
      this.invalidateShader = false;
    } 
    float f = this.paint.getStrokeWidth() / 2.0F;
    RectF rectF = this.rectF;
    copyBounds(this.rect);
    rectF.set(this.rect);
    rectF.left += f;
    rectF.top += f;
    rectF.right -= f;
    rectF.bottom -= f;
    paramCanvas.save();
    paramCanvas.rotate(this.rotation, rectF.centerX(), rectF.centerY());
    paramCanvas.drawOval(rectF, this.paint);
    paramCanvas.restore();
  }
  
  public Drawable.ConstantState getConstantState() {
    return this.state;
  }
  
  public int getOpacity() {
    byte b;
    if (this.borderWidth > 0.0F) {
      b = -3;
    } else {
      b = -2;
    } 
    return b;
  }
  
  public boolean getPadding(Rect paramRect) {
    int i = Math.round(this.borderWidth);
    paramRect.set(i, i, i, i);
    return true;
  }
  
  public boolean isStateful() {
    boolean bool;
    ColorStateList colorStateList = this.borderTint;
    if ((colorStateList != null && colorStateList.isStateful()) || super.isStateful()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    this.invalidateShader = true;
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    ColorStateList colorStateList = this.borderTint;
    if (colorStateList != null) {
      int i = colorStateList.getColorForState(paramArrayOfint, this.currentBorderTintColor);
      if (i != this.currentBorderTintColor) {
        this.invalidateShader = true;
        this.currentBorderTintColor = i;
      } 
    } 
    if (this.invalidateShader)
      invalidateSelf(); 
    return this.invalidateShader;
  }
  
  public void setAlpha(int paramInt) {
    this.paint.setAlpha(paramInt);
    invalidateSelf();
  }
  
  public void setBorderTint(ColorStateList paramColorStateList) {
    if (paramColorStateList != null)
      this.currentBorderTintColor = paramColorStateList.getColorForState(getState(), this.currentBorderTintColor); 
    this.borderTint = paramColorStateList;
    this.invalidateShader = true;
    invalidateSelf();
  }
  
  public void setBorderWidth(float paramFloat) {
    if (this.borderWidth != paramFloat) {
      this.borderWidth = paramFloat;
      this.paint.setStrokeWidth(1.3333F * paramFloat);
      this.invalidateShader = true;
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.paint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setGradientColors(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.topOuterStrokeColor = paramInt1;
    this.topInnerStrokeColor = paramInt2;
    this.bottomOuterStrokeColor = paramInt3;
    this.bottomInnerStrokeColor = paramInt4;
  }
  
  public final void setRotation(float paramFloat) {
    if (paramFloat != this.rotation) {
      this.rotation = paramFloat;
      invalidateSelf();
    } 
  }
  
  private class CircularBorderState extends Drawable.ConstantState {
    private CircularBorderState() {}
    
    public int getChangingConfigurations() {
      return 0;
    }
    
    public Drawable newDrawable() {
      return CircularBorderDrawable.this;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/CircularBorderDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */