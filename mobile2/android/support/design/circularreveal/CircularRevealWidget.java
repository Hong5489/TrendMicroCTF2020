package android.support.design.circularreveal;

import android.animation.TypeEvaluator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.design.widget.MathUtils;
import android.util.Property;

public interface CircularRevealWidget extends CircularRevealHelper.Delegate {
  void buildCircularRevealCache();
  
  void destroyCircularRevealCache();
  
  void draw(Canvas paramCanvas);
  
  Drawable getCircularRevealOverlayDrawable();
  
  int getCircularRevealScrimColor();
  
  RevealInfo getRevealInfo();
  
  boolean isOpaque();
  
  void setCircularRevealOverlayDrawable(Drawable paramDrawable);
  
  void setCircularRevealScrimColor(int paramInt);
  
  void setRevealInfo(RevealInfo paramRevealInfo);
  
  public static class CircularRevealEvaluator implements TypeEvaluator<RevealInfo> {
    public static final TypeEvaluator<CircularRevealWidget.RevealInfo> CIRCULAR_REVEAL = new CircularRevealEvaluator();
    
    private final CircularRevealWidget.RevealInfo revealInfo = new CircularRevealWidget.RevealInfo();
    
    public CircularRevealWidget.RevealInfo evaluate(float param1Float, CircularRevealWidget.RevealInfo param1RevealInfo1, CircularRevealWidget.RevealInfo param1RevealInfo2) {
      this.revealInfo.set(MathUtils.lerp(param1RevealInfo1.centerX, param1RevealInfo2.centerX, param1Float), MathUtils.lerp(param1RevealInfo1.centerY, param1RevealInfo2.centerY, param1Float), MathUtils.lerp(param1RevealInfo1.radius, param1RevealInfo2.radius, param1Float));
      return this.revealInfo;
    }
  }
  
  public static class CircularRevealProperty extends Property<CircularRevealWidget, RevealInfo> {
    public static final Property<CircularRevealWidget, CircularRevealWidget.RevealInfo> CIRCULAR_REVEAL = new CircularRevealProperty("circularReveal");
    
    private CircularRevealProperty(String param1String) {
      super(CircularRevealWidget.RevealInfo.class, param1String);
    }
    
    public CircularRevealWidget.RevealInfo get(CircularRevealWidget param1CircularRevealWidget) {
      return param1CircularRevealWidget.getRevealInfo();
    }
    
    public void set(CircularRevealWidget param1CircularRevealWidget, CircularRevealWidget.RevealInfo param1RevealInfo) {
      param1CircularRevealWidget.setRevealInfo(param1RevealInfo);
    }
  }
  
  public static class CircularRevealScrimColorProperty extends Property<CircularRevealWidget, Integer> {
    public static final Property<CircularRevealWidget, Integer> CIRCULAR_REVEAL_SCRIM_COLOR = new CircularRevealScrimColorProperty("circularRevealScrimColor");
    
    private CircularRevealScrimColorProperty(String param1String) {
      super(Integer.class, param1String);
    }
    
    public Integer get(CircularRevealWidget param1CircularRevealWidget) {
      return Integer.valueOf(param1CircularRevealWidget.getCircularRevealScrimColor());
    }
    
    public void set(CircularRevealWidget param1CircularRevealWidget, Integer param1Integer) {
      param1CircularRevealWidget.setCircularRevealScrimColor(param1Integer.intValue());
    }
  }
  
  public static class RevealInfo {
    public static final float INVALID_RADIUS = 3.4028235E38F;
    
    public float centerX;
    
    public float centerY;
    
    public float radius;
    
    private RevealInfo() {}
    
    public RevealInfo(float param1Float1, float param1Float2, float param1Float3) {
      this.centerX = param1Float1;
      this.centerY = param1Float2;
      this.radius = param1Float3;
    }
    
    public RevealInfo(RevealInfo param1RevealInfo) {
      this(param1RevealInfo.centerX, param1RevealInfo.centerY, param1RevealInfo.radius);
    }
    
    public boolean isInvalid() {
      boolean bool;
      if (this.radius == Float.MAX_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void set(float param1Float1, float param1Float2, float param1Float3) {
      this.centerX = param1Float1;
      this.centerY = param1Float2;
      this.radius = param1Float3;
    }
    
    public void set(RevealInfo param1RevealInfo) {
      set(param1RevealInfo.centerX, param1RevealInfo.centerY, param1RevealInfo.radius);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/circularreveal/CircularRevealWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */