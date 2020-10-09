package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private int tempLeftRightOffset = 0;
  
  private int tempTopBottomOffset = 0;
  
  private ViewOffsetHelper viewOffsetHelper;
  
  public ViewOffsetBehavior() {}
  
  public ViewOffsetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public int getLeftAndRightOffset() {
    boolean bool;
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null) {
      bool = viewOffsetHelper.getLeftAndRightOffset();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getTopAndBottomOffset() {
    boolean bool;
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null) {
      bool = viewOffsetHelper.getTopAndBottomOffset();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void layoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    paramCoordinatorLayout.onLayoutChild((View)paramV, paramInt);
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    layoutChild(paramCoordinatorLayout, paramV, paramInt);
    if (this.viewOffsetHelper == null)
      this.viewOffsetHelper = new ViewOffsetHelper((View)paramV); 
    this.viewOffsetHelper.onViewLayout();
    paramInt = this.tempTopBottomOffset;
    if (paramInt != 0) {
      this.viewOffsetHelper.setTopAndBottomOffset(paramInt);
      this.tempTopBottomOffset = 0;
    } 
    paramInt = this.tempLeftRightOffset;
    if (paramInt != 0) {
      this.viewOffsetHelper.setLeftAndRightOffset(paramInt);
      this.tempLeftRightOffset = 0;
    } 
    return true;
  }
  
  public boolean setLeftAndRightOffset(int paramInt) {
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null)
      return viewOffsetHelper.setLeftAndRightOffset(paramInt); 
    this.tempLeftRightOffset = paramInt;
    return false;
  }
  
  public boolean setTopAndBottomOffset(int paramInt) {
    ViewOffsetHelper viewOffsetHelper = this.viewOffsetHelper;
    if (viewOffsetHelper != null)
      return viewOffsetHelper.setTopAndBottomOffset(paramInt); 
    this.tempTopBottomOffset = paramInt;
    return false;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/ViewOffsetBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */