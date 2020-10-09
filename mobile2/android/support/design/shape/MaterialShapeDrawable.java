package android.support.design.shape;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.TintAwareDrawable;

public class MaterialShapeDrawable extends Drawable implements TintAwareDrawable {
  private int alpha = 255;
  
  private final ShapePath[] cornerPaths = new ShapePath[4];
  
  private final Matrix[] cornerTransforms = new Matrix[4];
  
  private final Matrix[] edgeTransforms = new Matrix[4];
  
  private float interpolation = 1.0F;
  
  private final Matrix matrix = new Matrix();
  
  private final Paint paint = new Paint();
  
  private Paint.Style paintStyle = Paint.Style.FILL_AND_STROKE;
  
  private final Path path = new Path();
  
  private final PointF pointF = new PointF();
  
  private float scale = 1.0F;
  
  private final float[] scratch = new float[2];
  
  private final float[] scratch2 = new float[2];
  
  private final Region scratchRegion = new Region();
  
  private int shadowColor = -16777216;
  
  private int shadowElevation = 5;
  
  private boolean shadowEnabled = false;
  
  private int shadowRadius = 10;
  
  private final ShapePath shapePath = new ShapePath();
  
  private ShapePathModel shapedViewModel = null;
  
  private float strokeWidth = 0.0F;
  
  private PorterDuffColorFilter tintFilter;
  
  private ColorStateList tintList = null;
  
  private PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;
  
  private final Region transparentRegion = new Region();
  
  private boolean useTintColorForShadow = false;
  
  public MaterialShapeDrawable() {
    this((ShapePathModel)null);
  }
  
  public MaterialShapeDrawable(ShapePathModel paramShapePathModel) {
    this.shapedViewModel = paramShapePathModel;
    for (byte b = 0; b < 4; b++) {
      this.cornerTransforms[b] = new Matrix();
      this.edgeTransforms[b] = new Matrix();
      this.cornerPaths[b] = new ShapePath();
    } 
  }
  
  private float angleOfCorner(int paramInt1, int paramInt2, int paramInt3) {
    getCoordinatesOfCorner((paramInt1 - 1 + 4) % 4, paramInt2, paramInt3, this.pointF);
    float f1 = this.pointF.x;
    float f2 = this.pointF.y;
    getCoordinatesOfCorner((paramInt1 + 1) % 4, paramInt2, paramInt3, this.pointF);
    float f3 = this.pointF.x;
    float f4 = this.pointF.y;
    getCoordinatesOfCorner(paramInt1, paramInt2, paramInt3, this.pointF);
    float f5 = this.pointF.x;
    float f6 = this.pointF.y;
    f4 = (float)Math.atan2((f2 - f6), (f1 - f5)) - (float)Math.atan2((f4 - f6), (f3 - f5));
    f3 = f4;
    if (f4 < 0.0F)
      f3 = (float)(f4 + 6.283185307179586D); 
    return f3;
  }
  
  private float angleOfEdge(int paramInt1, int paramInt2, int paramInt3) {
    getCoordinatesOfCorner(paramInt1, paramInt2, paramInt3, this.pointF);
    float f1 = this.pointF.x;
    float f2 = this.pointF.y;
    getCoordinatesOfCorner((paramInt1 + 1) % 4, paramInt2, paramInt3, this.pointF);
    float f3 = this.pointF.x;
    return (float)Math.atan2((this.pointF.y - f2), (f3 - f1));
  }
  
  private void appendCornerPath(int paramInt, Path paramPath) {
    this.scratch[0] = (this.cornerPaths[paramInt]).startX;
    this.scratch[1] = (this.cornerPaths[paramInt]).startY;
    this.cornerTransforms[paramInt].mapPoints(this.scratch);
    if (paramInt == 0) {
      float[] arrayOfFloat = this.scratch;
      paramPath.moveTo(arrayOfFloat[0], arrayOfFloat[1]);
    } else {
      float[] arrayOfFloat = this.scratch;
      paramPath.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
    } 
    this.cornerPaths[paramInt].applyToPath(this.cornerTransforms[paramInt], paramPath);
  }
  
  private void appendEdgePath(int paramInt, Path paramPath) {
    int i = (paramInt + 1) % 4;
    this.scratch[0] = (this.cornerPaths[paramInt]).endX;
    this.scratch[1] = (this.cornerPaths[paramInt]).endY;
    this.cornerTransforms[paramInt].mapPoints(this.scratch);
    this.scratch2[0] = (this.cornerPaths[i]).startX;
    this.scratch2[1] = (this.cornerPaths[i]).startY;
    this.cornerTransforms[i].mapPoints(this.scratch2);
    float[] arrayOfFloat1 = this.scratch;
    float f = arrayOfFloat1[0];
    float[] arrayOfFloat2 = this.scratch2;
    f = (float)Math.hypot((f - arrayOfFloat2[0]), (arrayOfFloat1[1] - arrayOfFloat2[1]));
    this.shapePath.reset(0.0F, 0.0F);
    getEdgeTreatmentForIndex(paramInt).getEdgePath(f, this.interpolation, this.shapePath);
    this.shapePath.applyToPath(this.edgeTransforms[paramInt], paramPath);
  }
  
  private void getCoordinatesOfCorner(int paramInt1, int paramInt2, int paramInt3, PointF paramPointF) {
    if (paramInt1 != 1) {
      if (paramInt1 != 2) {
        if (paramInt1 != 3) {
          paramPointF.set(0.0F, 0.0F);
        } else {
          paramPointF.set(0.0F, paramInt3);
        } 
      } else {
        paramPointF.set(paramInt2, paramInt3);
      } 
    } else {
      paramPointF.set(paramInt2, 0.0F);
    } 
  }
  
  private CornerTreatment getCornerTreatmentForIndex(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? this.shapedViewModel.getTopLeftCorner() : this.shapedViewModel.getBottomLeftCorner()) : this.shapedViewModel.getBottomRightCorner()) : this.shapedViewModel.getTopRightCorner();
  }
  
  private EdgeTreatment getEdgeTreatmentForIndex(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? this.shapedViewModel.getTopEdge() : this.shapedViewModel.getLeftEdge()) : this.shapedViewModel.getBottomEdge()) : this.shapedViewModel.getRightEdge();
  }
  
  private void getPath(int paramInt1, int paramInt2, Path paramPath) {
    getPathForSize(paramInt1, paramInt2, paramPath);
    if (this.scale == 1.0F)
      return; 
    this.matrix.reset();
    Matrix matrix = this.matrix;
    float f = this.scale;
    matrix.setScale(f, f, (paramInt1 / 2), (paramInt2 / 2));
    paramPath.transform(this.matrix);
  }
  
  private static int modulateAlpha(int paramInt1, int paramInt2) {
    return paramInt1 * ((paramInt2 >>> 7) + paramInt2) >>> 8;
  }
  
  private void setCornerPathAndTransform(int paramInt1, int paramInt2, int paramInt3) {
    getCoordinatesOfCorner(paramInt1, paramInt2, paramInt3, this.pointF);
    float f = angleOfCorner(paramInt1, paramInt2, paramInt3);
    getCornerTreatmentForIndex(paramInt1).getCornerPath(f, this.interpolation, this.cornerPaths[paramInt1]);
    f = angleOfEdge((paramInt1 - 1 + 4) % 4, paramInt2, paramInt3);
    this.cornerTransforms[paramInt1].reset();
    this.cornerTransforms[paramInt1].setTranslate(this.pointF.x, this.pointF.y);
    this.cornerTransforms[paramInt1].preRotate((float)Math.toDegrees((f + 1.5707964F)));
  }
  
  private void setEdgeTransform(int paramInt1, int paramInt2, int paramInt3) {
    this.scratch[0] = (this.cornerPaths[paramInt1]).endX;
    this.scratch[1] = (this.cornerPaths[paramInt1]).endY;
    this.cornerTransforms[paramInt1].mapPoints(this.scratch);
    float f = angleOfEdge(paramInt1, paramInt2, paramInt3);
    this.edgeTransforms[paramInt1].reset();
    Matrix matrix = this.edgeTransforms[paramInt1];
    float[] arrayOfFloat = this.scratch;
    matrix.setTranslate(arrayOfFloat[0], arrayOfFloat[1]);
    this.edgeTransforms[paramInt1].preRotate((float)Math.toDegrees(f));
  }
  
  private void updateTintFilter() {
    ColorStateList colorStateList = this.tintList;
    if (colorStateList == null || this.tintMode == null) {
      this.tintFilter = null;
      return;
    } 
    int i = colorStateList.getColorForState(getState(), 0);
    this.tintFilter = new PorterDuffColorFilter(i, this.tintMode);
    if (this.useTintColorForShadow)
      this.shadowColor = i; 
  }
  
  public void draw(Canvas paramCanvas) {
    this.paint.setColorFilter((ColorFilter)this.tintFilter);
    int i = this.paint.getAlpha();
    this.paint.setAlpha(modulateAlpha(i, this.alpha));
    this.paint.setStrokeWidth(this.strokeWidth);
    this.paint.setStyle(this.paintStyle);
    int j = this.shadowElevation;
    if (j > 0 && this.shadowEnabled)
      this.paint.setShadowLayer(this.shadowRadius, 0.0F, j, this.shadowColor); 
    if (this.shapedViewModel != null) {
      getPath(paramCanvas.getWidth(), paramCanvas.getHeight(), this.path);
      paramCanvas.drawPath(this.path, this.paint);
    } else {
      paramCanvas.drawRect(0.0F, 0.0F, paramCanvas.getWidth(), paramCanvas.getHeight(), this.paint);
    } 
    this.paint.setAlpha(i);
  }
  
  public float getInterpolation() {
    return this.interpolation;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public Paint.Style getPaintStyle() {
    return this.paintStyle;
  }
  
  public void getPathForSize(int paramInt1, int paramInt2, Path paramPath) {
    paramPath.rewind();
    if (this.shapedViewModel == null)
      return; 
    for (byte b = 0; b < 4; b++) {
      setCornerPathAndTransform(b, paramInt1, paramInt2);
      setEdgeTransform(b, paramInt1, paramInt2);
    } 
    for (paramInt1 = 0; paramInt1 < 4; paramInt1++) {
      appendCornerPath(paramInt1, paramPath);
      appendEdgePath(paramInt1, paramPath);
    } 
    paramPath.close();
  }
  
  public float getScale() {
    return this.scale;
  }
  
  public int getShadowElevation() {
    return this.shadowElevation;
  }
  
  public int getShadowRadius() {
    return this.shadowRadius;
  }
  
  public ShapePathModel getShapedViewModel() {
    return this.shapedViewModel;
  }
  
  public float getStrokeWidth() {
    return this.strokeWidth;
  }
  
  public ColorStateList getTintList() {
    return this.tintList;
  }
  
  public Region getTransparentRegion() {
    Rect rect = getBounds();
    this.transparentRegion.set(rect);
    getPath(rect.width(), rect.height(), this.path);
    this.scratchRegion.setPath(this.path, this.transparentRegion);
    this.transparentRegion.op(this.scratchRegion, Region.Op.DIFFERENCE);
    return this.transparentRegion;
  }
  
  public boolean isPointInTransparentRegion(int paramInt1, int paramInt2) {
    return getTransparentRegion().contains(paramInt1, paramInt2);
  }
  
  public boolean isShadowEnabled() {
    return this.shadowEnabled;
  }
  
  public void setAlpha(int paramInt) {
    this.alpha = paramInt;
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.paint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setInterpolation(float paramFloat) {
    this.interpolation = paramFloat;
    invalidateSelf();
  }
  
  public void setPaintStyle(Paint.Style paramStyle) {
    this.paintStyle = paramStyle;
    invalidateSelf();
  }
  
  public void setScale(float paramFloat) {
    this.scale = paramFloat;
    invalidateSelf();
  }
  
  public void setShadowColor(int paramInt) {
    this.shadowColor = paramInt;
    this.useTintColorForShadow = false;
    invalidateSelf();
  }
  
  public void setShadowElevation(int paramInt) {
    this.shadowElevation = paramInt;
    invalidateSelf();
  }
  
  public void setShadowEnabled(boolean paramBoolean) {
    this.shadowEnabled = paramBoolean;
    invalidateSelf();
  }
  
  public void setShadowRadius(int paramInt) {
    this.shadowRadius = paramInt;
    invalidateSelf();
  }
  
  public void setShapedViewModel(ShapePathModel paramShapePathModel) {
    this.shapedViewModel = paramShapePathModel;
    invalidateSelf();
  }
  
  public void setStrokeWidth(float paramFloat) {
    this.strokeWidth = paramFloat;
    invalidateSelf();
  }
  
  public void setTint(int paramInt) {
    setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    this.tintList = paramColorStateList;
    updateTintFilter();
    invalidateSelf();
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    this.tintMode = paramMode;
    updateTintFilter();
    invalidateSelf();
  }
  
  public void setUseTintColorForShadow(boolean paramBoolean) {
    this.useTintColorForShadow = paramBoolean;
    invalidateSelf();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/shape/MaterialShapeDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */