package android.support.design.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;

public class ShapePath {
  public float endX;
  
  public float endY;
  
  private final List<PathOperation> operations = new ArrayList<>();
  
  public float startX;
  
  public float startY;
  
  public ShapePath() {
    reset(0.0F, 0.0F);
  }
  
  public ShapePath(float paramFloat1, float paramFloat2) {
    reset(paramFloat1, paramFloat2);
  }
  
  public void addArc(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    PathArcOperation pathArcOperation = new PathArcOperation(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    pathArcOperation.startAngle = paramFloat5;
    pathArcOperation.sweepAngle = paramFloat6;
    this.operations.add(pathArcOperation);
    this.endX = (paramFloat1 + paramFloat3) * 0.5F + (paramFloat3 - paramFloat1) / 2.0F * (float)Math.cos(Math.toRadians((paramFloat5 + paramFloat6)));
    this.endY = (paramFloat2 + paramFloat4) * 0.5F + (paramFloat4 - paramFloat2) / 2.0F * (float)Math.sin(Math.toRadians((paramFloat5 + paramFloat6)));
  }
  
  public void applyToPath(Matrix paramMatrix, Path paramPath) {
    byte b = 0;
    int i = this.operations.size();
    while (b < i) {
      ((PathOperation)this.operations.get(b)).applyToPath(paramMatrix, paramPath);
      b++;
    } 
  }
  
  public void lineTo(float paramFloat1, float paramFloat2) {
    PathLineOperation pathLineOperation = new PathLineOperation();
    PathLineOperation.access$002(pathLineOperation, paramFloat1);
    PathLineOperation.access$102(pathLineOperation, paramFloat2);
    this.operations.add(pathLineOperation);
    this.endX = paramFloat1;
    this.endY = paramFloat2;
  }
  
  public void quadToPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    PathQuadOperation pathQuadOperation = new PathQuadOperation();
    pathQuadOperation.controlX = paramFloat1;
    pathQuadOperation.controlY = paramFloat2;
    pathQuadOperation.endX = paramFloat3;
    pathQuadOperation.endY = paramFloat4;
    this.operations.add(pathQuadOperation);
    this.endX = paramFloat3;
    this.endY = paramFloat4;
  }
  
  public void reset(float paramFloat1, float paramFloat2) {
    this.startX = paramFloat1;
    this.startY = paramFloat2;
    this.endX = paramFloat1;
    this.endY = paramFloat2;
    this.operations.clear();
  }
  
  public static class PathArcOperation extends PathOperation {
    private static final RectF rectF = new RectF();
    
    public float bottom;
    
    public float left;
    
    public float right;
    
    public float startAngle;
    
    public float sweepAngle;
    
    public float top;
    
    public PathArcOperation(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      this.left = param1Float1;
      this.top = param1Float2;
      this.right = param1Float3;
      this.bottom = param1Float4;
    }
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      rectF.set(this.left, this.top, this.right, this.bottom);
      param1Path.arcTo(rectF, this.startAngle, this.sweepAngle, false);
      param1Path.transform(param1Matrix);
    }
  }
  
  public static class PathLineOperation extends PathOperation {
    private float x;
    
    private float y;
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      param1Path.lineTo(this.x, this.y);
      param1Path.transform(param1Matrix);
    }
  }
  
  public static abstract class PathOperation {
    protected final Matrix matrix = new Matrix();
    
    public abstract void applyToPath(Matrix param1Matrix, Path param1Path);
  }
  
  public static class PathQuadOperation extends PathOperation {
    public float controlX;
    
    public float controlY;
    
    public float endX;
    
    public float endY;
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      param1Path.quadTo(this.controlX, this.controlY, this.endX, this.endY);
      param1Path.transform(param1Matrix);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/shape/ShapePath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */