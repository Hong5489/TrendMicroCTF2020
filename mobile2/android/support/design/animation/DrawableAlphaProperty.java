package android.support.design.animation;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import java.util.WeakHashMap;

public class DrawableAlphaProperty extends Property<Drawable, Integer> {
  public static final Property<Drawable, Integer> DRAWABLE_ALPHA_COMPAT = new DrawableAlphaProperty();
  
  private final WeakHashMap<Drawable, Integer> alphaCache = new WeakHashMap<>();
  
  private DrawableAlphaProperty() {
    super(Integer.class, "drawableAlphaCompat");
  }
  
  public Integer get(Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 19) ? Integer.valueOf(paramDrawable.getAlpha()) : (this.alphaCache.containsKey(paramDrawable) ? this.alphaCache.get(paramDrawable) : Integer.valueOf(255));
  }
  
  public void set(Drawable paramDrawable, Integer paramInteger) {
    if (Build.VERSION.SDK_INT < 19)
      this.alphaCache.put(paramDrawable, paramInteger); 
    paramDrawable.setAlpha(paramInteger.intValue());
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/animation/DrawableAlphaProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */