package android.support.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;

public class ShadowDrawableWrapper extends DrawableWrapper {
  static final double COS_45 = Math.cos(Math.toRadians(45.0D));
  
  static final float SHADOW_BOTTOM_SCALE = 1.0F;
  
  static final float SHADOW_HORIZ_SCALE = 0.5F;
  
  static final float SHADOW_MULTIPLIER = 1.5F;
  
  static final float SHADOW_TOP_SCALE = 0.25F;
  
  private boolean addPaddingForCorners = true;
  
  final RectF contentBounds;
  
  float cornerRadius;
  
  final Paint cornerShadowPaint;
  
  Path cornerShadowPath;
  
  private boolean dirty = true;
  
  final Paint edgeShadowPaint;
  
  float maxShadowSize;
  
  private boolean printedShadowClipWarning = false;
  
  float rawMaxShadowSize;
  
  float rawShadowSize;
  
  private float rotation;
  
  private final int shadowEndColor;
  
  private final int shadowMiddleColor;
  
  float shadowSize;
  
  private final int shadowStartColor;
  
  public ShadowDrawableWrapper(Context paramContext, Drawable paramDrawable, float paramFloat1, float paramFloat2, float paramFloat3) {
    super(paramDrawable);
    this.shadowStartColor = ContextCompat.getColor(paramContext, R.color.design_fab_shadow_start_color);
    this.shadowMiddleColor = ContextCompat.getColor(paramContext, R.color.design_fab_shadow_mid_color);
    this.shadowEndColor = ContextCompat.getColor(paramContext, R.color.design_fab_shadow_end_color);
    Paint paint = new Paint(5);
    this.cornerShadowPaint = paint;
    paint.setStyle(Paint.Style.FILL);
    this.cornerRadius = Math.round(paramFloat1);
    this.contentBounds = new RectF();
    paint = new Paint(this.cornerShadowPaint);
    this.edgeShadowPaint = paint;
    paint.setAntiAlias(false);
    setShadowSize(paramFloat2, paramFloat3);
  }
  
  private void buildComponents(Rect paramRect) {
    float f = this.rawMaxShadowSize * 1.5F;
    this.contentBounds.set(paramRect.left + this.rawMaxShadowSize, paramRect.top + f, paramRect.right - this.rawMaxShadowSize, paramRect.bottom - f);
    getWrappedDrawable().setBounds((int)this.contentBounds.left, (int)this.contentBounds.top, (int)this.contentBounds.right, (int)this.contentBounds.bottom);
    buildShadowCorners();
  }
  
  private void buildShadowCorners() {
    float f1 = this.cornerRadius;
    RectF rectF1 = new RectF(-f1, -f1, f1, f1);
    RectF rectF2 = new RectF(rectF1);
    f1 = this.shadowSize;
    rectF2.inset(-f1, -f1);
    Path path = this.cornerShadowPath;
    if (path == null) {
      this.cornerShadowPath = new Path();
    } else {
      path.reset();
    } 
    this.cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
    this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0F);
    this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0F);
    this.cornerShadowPath.arcTo(rectF2, 180.0F, 90.0F, false);
    this.cornerShadowPath.arcTo(rectF1, 270.0F, -90.0F, false);
    this.cornerShadowPath.close();
    float f2 = -rectF2.top;
    if (f2 > 0.0F) {
      f1 = this.cornerRadius / f2;
      float f = (1.0F - f1) / 2.0F;
      Paint paint1 = this.cornerShadowPaint;
      int m = this.shadowStartColor;
      int n = this.shadowMiddleColor;
      int i1 = this.shadowEndColor;
      Shader.TileMode tileMode1 = Shader.TileMode.CLAMP;
      paint1.setShader((Shader)new RadialGradient(0.0F, 0.0F, f2, new int[] { 0, m, n, i1 }, new float[] { 0.0F, f1, f1 + f, 1.0F }, tileMode1));
    } 
    Paint paint = this.edgeShadowPaint;
    f1 = rectF1.top;
    f2 = rectF2.top;
    int k = this.shadowStartColor;
    int j = this.shadowMiddleColor;
    int i = this.shadowEndColor;
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
    paint.setShader((Shader)new LinearGradient(0.0F, f1, 0.0F, f2, new int[] { k, j, i }, new float[] { 0.0F, 0.5F, 1.0F }, tileMode));
    this.edgeShadowPaint.setAntiAlias(false);
  }
  
  public static float calculateHorizontalPadding(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return paramBoolean ? (float)(paramFloat1 + (1.0D - COS_45) * paramFloat2) : paramFloat1;
  }
  
  public static float calculateVerticalPadding(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return paramBoolean ? (float)((1.5F * paramFloat1) + (1.0D - COS_45) * paramFloat2) : (1.5F * paramFloat1);
  }
  
  private void drawShadow(Canvas paramCanvas) {
    int i = paramCanvas.save();
    paramCanvas.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
    float f1 = -this.cornerRadius - this.shadowSize;
    float f2 = this.cornerRadius;
    float f3 = this.contentBounds.width();
    boolean bool = true;
    if (f3 - f2 * 2.0F > 0.0F) {
      j = 1;
    } else {
      j = 0;
    } 
    if (this.contentBounds.height() - f2 * 2.0F <= 0.0F)
      bool = false; 
    float f4 = this.rawShadowSize;
    f3 = f2 / (f2 + f4 - 0.5F * f4);
    float f5 = f2 / (f2 + f4 - 0.25F * f4);
    f4 = f2 / (f2 + f4 - f4 * 1.0F);
    int k = paramCanvas.save();
    paramCanvas.translate(this.contentBounds.left + f2, this.contentBounds.top + f2);
    paramCanvas.scale(f3, f5);
    paramCanvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
    if (j) {
      paramCanvas.scale(1.0F / f3, 1.0F);
      paramCanvas.drawRect(0.0F, f1, this.contentBounds.width() - f2 * 2.0F, -this.cornerRadius, this.edgeShadowPaint);
    } 
    paramCanvas.restoreToCount(k);
    k = paramCanvas.save();
    paramCanvas.translate(this.contentBounds.right - f2, this.contentBounds.bottom - f2);
    paramCanvas.scale(f3, f4);
    paramCanvas.rotate(180.0F);
    paramCanvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
    if (j) {
      paramCanvas.scale(1.0F / f3, 1.0F);
      paramCanvas.drawRect(0.0F, f1, this.contentBounds.width() - f2 * 2.0F, -this.cornerRadius + this.shadowSize, this.edgeShadowPaint);
    } 
    paramCanvas.restoreToCount(k);
    int j = paramCanvas.save();
    paramCanvas.translate(this.contentBounds.left + f2, this.contentBounds.bottom - f2);
    paramCanvas.scale(f3, f4);
    paramCanvas.rotate(270.0F);
    paramCanvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
    if (bool) {
      paramCanvas.scale(1.0F / f4, 1.0F);
      paramCanvas.drawRect(0.0F, f1, this.contentBounds.height() - f2 * 2.0F, -this.cornerRadius, this.edgeShadowPaint);
    } 
    paramCanvas.restoreToCount(j);
    j = paramCanvas.save();
    paramCanvas.translate(this.contentBounds.right - f2, this.contentBounds.top + f2);
    paramCanvas.scale(f3, f5);
    paramCanvas.rotate(90.0F);
    paramCanvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
    if (bool) {
      paramCanvas.scale(1.0F / f5, 1.0F);
      paramCanvas.drawRect(0.0F, f1, this.contentBounds.height() - 2.0F * f2, -this.cornerRadius, this.edgeShadowPaint);
    } 
    paramCanvas.restoreToCount(j);
    paramCanvas.restoreToCount(i);
  }
  
  private static int toEven(float paramFloat) {
    int i = Math.round(paramFloat);
    if (i % 2 == 1)
      i--; 
    return i;
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.dirty) {
      buildComponents(getBounds());
      this.dirty = false;
    } 
    drawShadow(paramCanvas);
    super.draw(paramCanvas);
  }
  
  public float getCornerRadius() {
    return this.cornerRadius;
  }
  
  public float getMaxShadowSize() {
    return this.rawMaxShadowSize;
  }
  
  public float getMinHeight() {
    float f = this.rawMaxShadowSize;
    f = Math.max(f, this.cornerRadius + f * 1.5F / 2.0F);
    return this.rawMaxShadowSize * 1.5F * 2.0F + f * 2.0F;
  }
  
  public float getMinWidth() {
    float f = this.rawMaxShadowSize;
    f = Math.max(f, this.cornerRadius + f / 2.0F);
    return this.rawMaxShadowSize * 2.0F + f * 2.0F;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public boolean getPadding(Rect paramRect) {
    int i = (int)Math.ceil(calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
    int j = (int)Math.ceil(calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
    paramRect.set(j, i, j, i);
    return true;
  }
  
  public float getShadowSize() {
    return this.rawShadowSize;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    this.dirty = true;
  }
  
  public void setAddPaddingForCorners(boolean paramBoolean) {
    this.addPaddingForCorners = paramBoolean;
    invalidateSelf();
  }
  
  public void setAlpha(int paramInt) {
    super.setAlpha(paramInt);
    this.cornerShadowPaint.setAlpha(paramInt);
    this.edgeShadowPaint.setAlpha(paramInt);
  }
  
  public void setCornerRadius(float paramFloat) {
    paramFloat = Math.round(paramFloat);
    if (this.cornerRadius == paramFloat)
      return; 
    this.cornerRadius = paramFloat;
    this.dirty = true;
    invalidateSelf();
  }
  
  public void setMaxShadowSize(float paramFloat) {
    setShadowSize(this.rawShadowSize, paramFloat);
  }
  
  public final void setRotation(float paramFloat) {
    if (this.rotation != paramFloat) {
      this.rotation = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setShadowSize(float paramFloat) {
    setShadowSize(paramFloat, this.rawMaxShadowSize);
  }
  
  public void setShadowSize(float paramFloat1, float paramFloat2) {
    if (paramFloat1 >= 0.0F && paramFloat2 >= 0.0F) {
      float f1 = toEven(paramFloat1);
      float f2 = toEven(paramFloat2);
      paramFloat1 = f1;
      if (f1 > f2) {
        paramFloat2 = f2;
        paramFloat1 = paramFloat2;
        if (!this.printedShadowClipWarning) {
          this.printedShadowClipWarning = true;
          paramFloat1 = paramFloat2;
        } 
      } 
      if (this.rawShadowSize == paramFloat1 && this.rawMaxShadowSize == f2)
        return; 
      this.rawShadowSize = paramFloat1;
      this.rawMaxShadowSize = f2;
      this.shadowSize = Math.round(1.5F * paramFloat1);
      this.maxShadowSize = f2;
      this.dirty = true;
      invalidateSelf();
      return;
    } 
    throw new IllegalArgumentException("invalid shadow size");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/ShadowDrawableWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */