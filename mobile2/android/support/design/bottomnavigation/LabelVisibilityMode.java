package android.support.design.bottomnavigation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface LabelVisibilityMode {
  public static final int LABEL_VISIBILITY_AUTO = -1;
  
  public static final int LABEL_VISIBILITY_LABELED = 1;
  
  public static final int LABEL_VISIBILITY_SELECTED = 0;
  
  public static final int LABEL_VISIBILITY_UNLABELED = 2;
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/bottomnavigation/LabelVisibilityMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */