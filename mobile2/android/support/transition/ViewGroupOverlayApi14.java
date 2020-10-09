package android.support.transition;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

class ViewGroupOverlayApi14 extends ViewOverlayApi14 implements ViewGroupOverlayImpl {
  ViewGroupOverlayApi14(Context paramContext, ViewGroup paramViewGroup, View paramView) {
    super(paramContext, paramViewGroup, paramView);
  }
  
  static ViewGroupOverlayApi14 createFrom(ViewGroup paramViewGroup) {
    return (ViewGroupOverlayApi14)ViewOverlayApi14.createFrom((View)paramViewGroup);
  }
  
  public void add(View paramView) {
    this.mOverlayViewGroup.add(paramView);
  }
  
  public void remove(View paramView) {
    this.mOverlayViewGroup.remove(paramView);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/ViewGroupOverlayApi14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */