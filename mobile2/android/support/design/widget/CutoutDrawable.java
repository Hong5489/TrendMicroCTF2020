package android.support.design.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;

class CutoutDrawable extends GradientDrawable {
  private final RectF cutoutBounds;
  
  private final Paint cutoutPaint = new Paint(1);
  
  private int savedLayer;
  
  CutoutDrawable() {
    setPaintStyles();
    this.cutoutBounds = new RectF();
  }
  
  private void postDraw(Canvas paramCanvas) {
    if (!useHardwareLayer(getCallback()))
      paramCanvas.restoreToCount(this.savedLayer); 
  }
  
  private void preDraw(Canvas paramCanvas) {
    Drawable.Callback callback = getCallback();
    if (useHardwareLayer(callback)) {
      ((View)callback).setLayerType(2, null);
    } else {
      saveCanvasLayer(paramCanvas);
    } 
  }
  
  private void saveCanvasLayer(Canvas paramCanvas) {
    if (Build.VERSION.SDK_INT >= 21) {
      this.savedLayer = paramCanvas.saveLayer(0.0F, 0.0F, paramCanvas.getWidth(), paramCanvas.getHeight(), null);
    } else {
      this.savedLayer = paramCanvas.saveLayer(0.0F, 0.0F, paramCanvas.getWidth(), paramCanvas.getHeight(), null, 31);
    } 
  }
  
  private void setPaintStyles() {
    this.cutoutPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    this.cutoutPaint.setColor(-1);
    this.cutoutPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
  }
  
  private boolean useHardwareLayer(Drawable.Callback paramCallback) {
    return paramCallback instanceof View;
  }
  
  public void draw(Canvas paramCanvas) {
    preDraw(paramCanvas);
    super.draw(paramCanvas);
    paramCanvas.drawRect(this.cutoutBounds, this.cutoutPaint);
    postDraw(paramCanvas);
  }
  
  boolean hasCutout() {
    return this.cutoutBounds.isEmpty() ^ true;
  }
  
  void removeCutout() {
    setCutout(0.0F, 0.0F, 0.0F, 0.0F);
  }
  
  void setCutout(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (paramFloat1 != this.cutoutBounds.left || paramFloat2 != this.cutoutBounds.top || paramFloat3 != this.cutoutBounds.right || paramFloat4 != this.cutoutBounds.bottom) {
      this.cutoutBounds.set(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      invalidateSelf();
    } 
  }
  
  void setCutout(RectF paramRectF) {
    setCutout(paramRectF.left, paramRectF.top, paramRectF.right, paramRectF.bottom);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/CutoutDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */