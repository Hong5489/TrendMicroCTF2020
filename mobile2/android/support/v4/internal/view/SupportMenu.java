package android.support.v4.internal.view;

import android.view.Menu;

public interface SupportMenu extends Menu {
  public static final int CATEGORY_MASK = -65536;
  
  public static final int CATEGORY_SHIFT = 16;
  
  public static final int FLAG_KEEP_OPEN_ON_SUBMENU_OPENED = 4;
  
  public static final int SUPPORTED_MODIFIERS_MASK = 69647;
  
  public static final int USER_MASK = 65535;
  
  public static final int USER_SHIFT = 0;
  
  void setGroupDividerEnabled(boolean paramBoolean);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/internal/view/SupportMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */