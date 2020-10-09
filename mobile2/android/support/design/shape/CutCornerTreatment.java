package android.support.design.shape;

public class CutCornerTreatment extends CornerTreatment {
  private final float size;
  
  public CutCornerTreatment(float paramFloat) {
    this.size = paramFloat;
  }
  
  public void getCornerPath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    paramShapePath.reset(0.0F, this.size * paramFloat2);
    paramShapePath.lineTo((float)(Math.sin(paramFloat1) * this.size * paramFloat2), (float)(Math.cos(paramFloat1) * this.size * paramFloat2));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/shape/CutCornerTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */