package android.support.design.animation;

import android.support.design.R;
import android.util.Property;
import android.view.ViewGroup;

public class ChildrenAlphaProperty extends Property<ViewGroup, Float> {
  public static final Property<ViewGroup, Float> CHILDREN_ALPHA = new ChildrenAlphaProperty("childrenAlpha");
  
  private ChildrenAlphaProperty(String paramString) {
    super(Float.class, paramString);
  }
  
  public Float get(ViewGroup paramViewGroup) {
    Float float_ = (Float)paramViewGroup.getTag(R.id.mtrl_internal_children_alpha_tag);
    return (float_ != null) ? float_ : Float.valueOf(1.0F);
  }
  
  public void set(ViewGroup paramViewGroup, Float paramFloat) {
    float f = paramFloat.floatValue();
    paramViewGroup.setTag(R.id.mtrl_internal_children_alpha_tag, Float.valueOf(f));
    byte b = 0;
    int i = paramViewGroup.getChildCount();
    while (b < i) {
      paramViewGroup.getChildAt(b).setAlpha(f);
      b++;
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/animation/ChildrenAlphaProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */