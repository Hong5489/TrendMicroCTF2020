package android.support.design.animation;

import android.animation.TypeEvaluator;

public class ArgbEvaluatorCompat implements TypeEvaluator<Integer> {
  private static final ArgbEvaluatorCompat instance = new ArgbEvaluatorCompat();
  
  public static ArgbEvaluatorCompat getInstance() {
    return instance;
  }
  
  public Integer evaluate(float paramFloat, Integer paramInteger1, Integer paramInteger2) {
    int i = paramInteger1.intValue();
    float f1 = (i >> 24 & 0xFF) / 255.0F;
    float f2 = (i >> 16 & 0xFF) / 255.0F;
    float f3 = (i >> 8 & 0xFF) / 255.0F;
    float f4 = (i & 0xFF) / 255.0F;
    i = paramInteger2.intValue();
    float f5 = (i >> 24 & 0xFF) / 255.0F;
    float f6 = (i >> 16 & 0xFF) / 255.0F;
    float f7 = (i >> 8 & 0xFF) / 255.0F;
    float f8 = (i & 0xFF) / 255.0F;
    f2 = (float)Math.pow(f2, 2.2D);
    f3 = (float)Math.pow(f3, 2.2D);
    f4 = (float)Math.pow(f4, 2.2D);
    f6 = (float)Math.pow(f6, 2.2D);
    f7 = (float)Math.pow(f7, 2.2D);
    f8 = (float)Math.pow(f8, 2.2D);
    f2 = (float)Math.pow(((f6 - f2) * paramFloat + f2), 0.45454545454545453D);
    f3 = (float)Math.pow(((f7 - f3) * paramFloat + f3), 0.45454545454545453D);
    f4 = (float)Math.pow(((f8 - f4) * paramFloat + f4), 0.45454545454545453D);
    return Integer.valueOf(Math.round(((f5 - f1) * paramFloat + f1) * 255.0F) << 24 | Math.round(f2 * 255.0F) << 16 | Math.round(f3 * 255.0F) << 8 | Math.round(f4 * 255.0F));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/animation/ArgbEvaluatorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */