package android.support.design.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.MathUtils;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CircularRevealHelper {
  public static final int BITMAP_SHADER = 0;
  
  public static final int CLIP_PATH = 1;
  
  private static final boolean DEBUG = false;
  
  public static final int REVEAL_ANIMATOR = 2;
  
  public static final int STRATEGY;
  
  private boolean buildingCircularRevealCache;
  
  private Paint debugPaint;
  
  private final Delegate delegate;
  
  private boolean hasCircularRevealCache;
  
  private Drawable overlayDrawable;
  
  private CircularRevealWidget.RevealInfo revealInfo;
  
  private final Paint revealPaint;
  
  private final Path revealPath;
  
  private final Paint scrimPaint;
  
  private final View view;
  
  static {
    if (Build.VERSION.SDK_INT >= 21) {
      STRATEGY = 2;
    } else if (Build.VERSION.SDK_INT >= 18) {
      STRATEGY = 1;
    } else {
      STRATEGY = 0;
    } 
  }
  
  public CircularRevealHelper(Delegate paramDelegate) {
    this.delegate = paramDelegate;
    View view = (View)paramDelegate;
    this.view = view;
    view.setWillNotDraw(false);
    this.revealPath = new Path();
    this.revealPaint = new Paint(7);
    Paint paint = new Paint(1);
    this.scrimPaint = paint;
    paint.setColor(0);
  }
  
  private void drawDebugCircle(Canvas paramCanvas, int paramInt, float paramFloat) {
    this.debugPaint.setColor(paramInt);
    this.debugPaint.setStrokeWidth(paramFloat);
    paramCanvas.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius - paramFloat / 2.0F, this.debugPaint);
  }
  
  private void drawDebugMode(Canvas paramCanvas) {
    this.delegate.actualDraw(paramCanvas);
    if (shouldDrawScrim())
      paramCanvas.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.scrimPaint); 
    if (shouldDrawCircularReveal()) {
      drawDebugCircle(paramCanvas, -16777216, 10.0F);
      drawDebugCircle(paramCanvas, -65536, 5.0F);
    } 
    drawOverlayDrawable(paramCanvas);
  }
  
  private void drawOverlayDrawable(Canvas paramCanvas) {
    if (shouldDrawOverlayDrawable()) {
      Rect rect = this.overlayDrawable.getBounds();
      float f1 = this.revealInfo.centerX - rect.width() / 2.0F;
      float f2 = this.revealInfo.centerY - rect.height() / 2.0F;
      paramCanvas.translate(f1, f2);
      this.overlayDrawable.draw(paramCanvas);
      paramCanvas.translate(-f1, -f2);
    } 
  }
  
  private float getDistanceToFurthestCorner(CircularRevealWidget.RevealInfo paramRevealInfo) {
    return MathUtils.distanceToFurthestCorner(paramRevealInfo.centerX, paramRevealInfo.centerY, 0.0F, 0.0F, this.view.getWidth(), this.view.getHeight());
  }
  
  private void invalidateRevealInfo() {
    if (STRATEGY == 1) {
      this.revealPath.rewind();
      CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
      if (revealInfo != null)
        this.revealPath.addCircle(revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, Path.Direction.CW); 
    } 
    this.view.invalidate();
  }
  
  private boolean shouldDrawCircularReveal() {
    boolean bool3;
    CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
    boolean bool1 = false;
    boolean bool2 = false;
    if (revealInfo == null || revealInfo.isInvalid()) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (STRATEGY == 0) {
      bool1 = bool2;
      if (!bool3) {
        bool1 = bool2;
        if (this.hasCircularRevealCache)
          bool1 = true; 
      } 
      return bool1;
    } 
    if (!bool3)
      bool1 = true; 
    return bool1;
  }
  
  private boolean shouldDrawOverlayDrawable() {
    boolean bool;
    if (!this.buildingCircularRevealCache && this.overlayDrawable != null && this.revealInfo != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean shouldDrawScrim() {
    boolean bool;
    if (!this.buildingCircularRevealCache && Color.alpha(this.scrimPaint.getColor()) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void buildCircularRevealCache() {
    if (STRATEGY == 0) {
      this.buildingCircularRevealCache = true;
      this.hasCircularRevealCache = false;
      this.view.buildDrawingCache();
      Bitmap bitmap1 = this.view.getDrawingCache();
      Bitmap bitmap2 = bitmap1;
      if (bitmap1 == null) {
        bitmap2 = bitmap1;
        if (this.view.getWidth() != 0) {
          bitmap2 = bitmap1;
          if (this.view.getHeight() != 0) {
            bitmap2 = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap2);
            this.view.draw(canvas);
          } 
        } 
      } 
      if (bitmap2 != null)
        this.revealPaint.setShader((Shader)new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)); 
      this.buildingCircularRevealCache = false;
      this.hasCircularRevealCache = true;
    } 
  }
  
  public void destroyCircularRevealCache() {
    if (STRATEGY == 0) {
      this.hasCircularRevealCache = false;
      this.view.destroyDrawingCache();
      this.revealPaint.setShader(null);
      this.view.invalidate();
    } 
  }
  
  public void draw(Canvas paramCanvas) {
    StringBuilder stringBuilder;
    if (shouldDrawCircularReveal()) {
      int i = STRATEGY;
      if (i != 0) {
        if (i != 1) {
          if (i == 2) {
            this.delegate.actualDraw(paramCanvas);
            if (shouldDrawScrim())
              paramCanvas.drawRect(0.0F, 0.0F, this.view.getWidth(), this.view.getHeight(), this.scrimPaint); 
          } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported strategy ");
            stringBuilder.append(STRATEGY);
            throw new IllegalStateException(stringBuilder.toString());
          } 
        } else {
          i = stringBuilder.save();
          stringBuilder.clipPath(this.revealPath);
          this.delegate.actualDraw((Canvas)stringBuilder);
          if (shouldDrawScrim())
            stringBuilder.drawRect(0.0F, 0.0F, this.view.getWidth(), this.view.getHeight(), this.scrimPaint); 
          stringBuilder.restoreToCount(i);
        } 
      } else {
        stringBuilder.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.revealPaint);
        if (shouldDrawScrim())
          stringBuilder.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.scrimPaint); 
      } 
    } else {
      this.delegate.actualDraw((Canvas)stringBuilder);
      if (shouldDrawScrim())
        stringBuilder.drawRect(0.0F, 0.0F, this.view.getWidth(), this.view.getHeight(), this.scrimPaint); 
    } 
    drawOverlayDrawable((Canvas)stringBuilder);
  }
  
  public Drawable getCircularRevealOverlayDrawable() {
    return this.overlayDrawable;
  }
  
  public int getCircularRevealScrimColor() {
    return this.scrimPaint.getColor();
  }
  
  public CircularRevealWidget.RevealInfo getRevealInfo() {
    if (this.revealInfo == null)
      return null; 
    CircularRevealWidget.RevealInfo revealInfo = new CircularRevealWidget.RevealInfo(this.revealInfo);
    if (revealInfo.isInvalid())
      revealInfo.radius = getDistanceToFurthestCorner(revealInfo); 
    return revealInfo;
  }
  
  public boolean isOpaque() {
    boolean bool;
    if (this.delegate.actualIsOpaque() && !shouldDrawCircularReveal()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setCircularRevealOverlayDrawable(Drawable paramDrawable) {
    this.overlayDrawable = paramDrawable;
    this.view.invalidate();
  }
  
  public void setCircularRevealScrimColor(int paramInt) {
    this.scrimPaint.setColor(paramInt);
    this.view.invalidate();
  }
  
  public void setRevealInfo(CircularRevealWidget.RevealInfo paramRevealInfo) {
    if (paramRevealInfo == null) {
      this.revealInfo = null;
    } else {
      CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
      if (revealInfo == null) {
        this.revealInfo = new CircularRevealWidget.RevealInfo(paramRevealInfo);
      } else {
        revealInfo.set(paramRevealInfo);
      } 
      if (MathUtils.geq(paramRevealInfo.radius, getDistanceToFurthestCorner(paramRevealInfo), 1.0E-4F))
        this.revealInfo.radius = Float.MAX_VALUE; 
    } 
    invalidateRevealInfo();
  }
  
  static interface Delegate {
    void actualDraw(Canvas param1Canvas);
    
    boolean actualIsOpaque();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Strategy {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/circularreveal/CircularRevealHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */