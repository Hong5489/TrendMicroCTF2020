package android.support.design.shape;

public class RoundedCornerTreatment extends CornerTreatment {
  private final float radius;
  
  public RoundedCornerTreatment(float paramFloat) {
    this.radius = paramFloat;
  }
  
  public void getCornerPath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    paramShapePath.reset(0.0F, this.radius * paramFloat2);
    float f = this.radius;
    paramShapePath.addArc(0.0F, 0.0F, f * 2.0F * paramFloat2, f * 2.0F * paramFloat2, paramFloat1 + 180.0F, 90.0F);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/shape/RoundedCornerTreatment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */