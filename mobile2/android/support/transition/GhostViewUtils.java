package android.support.transition;

import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
  static GhostViewImpl addGhost(View paramView, ViewGroup paramViewGroup, Matrix paramMatrix) {
    return (Build.VERSION.SDK_INT >= 21) ? GhostViewApi21.addGhost(paramView, paramViewGroup, paramMatrix) : GhostViewApi14.addGhost(paramView, paramViewGroup);
  }
  
  static void removeGhost(View paramView) {
    if (Build.VERSION.SDK_INT >= 21) {
      GhostViewApi21.removeGhost(paramView);
    } else {
      GhostViewApi14.removeGhost(paramView);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/GhostViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */