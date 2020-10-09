package android.support.design.widget;

import android.graphics.Outline;

public class CircularBorderDrawableLollipop extends CircularBorderDrawable {
  public void getOutline(Outline paramOutline) {
    copyBounds(this.rect);
    paramOutline.setOval(this.rect);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/CircularBorderDrawableLollipop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */