package android.support.design.shape;

public class TriangleEdgeTreatment extends EdgeTreatment {
  private final boolean inside;
  
  private final float size;
  
  public TriangleEdgeTreatment(float paramFloat, boolean paramBoolean) {
    this.size = paramFloat;
    this.inside = paramBoolean;
  }
  
  public void getEdgePath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    float f2;
    paramShapePath.lineTo(paramFloat1 / 2.0F - this.size * paramFloat2, 0.0F);
    float f1 = paramFloat1 / 2.0F;
    if (this.inside) {
      f2 = this.size;
    } else {
      f2 = -this.size;
    } 
    paramShapePath.lineTo(f1, f2 * paramFloat2);
    paramShapePath.lineTo(paramFloat1 / 2.0F + this.size * paramFloat2, 0.0F);
    paramShapePath.lineTo(paramFloat1, 0.0F);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/shape/TriangleEdgeTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */