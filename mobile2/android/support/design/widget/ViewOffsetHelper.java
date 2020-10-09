package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.view.View;

class ViewOffsetHelper {
  private int layoutLeft;
  
  private int layoutTop;
  
  private int offsetLeft;
  
  private int offsetTop;
  
  private final View view;
  
  public ViewOffsetHelper(View paramView) {
    this.view = paramView;
  }
  
  private void updateOffsets() {
    View view = this.view;
    ViewCompat.offsetTopAndBottom(view, this.offsetTop - view.getTop() - this.layoutTop);
    view = this.view;
    ViewCompat.offsetLeftAndRight(view, this.offsetLeft - view.getLeft() - this.layoutLeft);
  }
  
  public int getLayoutLeft() {
    return this.layoutLeft;
  }
  
  public int getLayoutTop() {
    return this.layoutTop;
  }
  
  public int getLeftAndRightOffset() {
    return this.offsetLeft;
  }
  
  public int getTopAndBottomOffset() {
    return this.offsetTop;
  }
  
  public void onViewLayout() {
    this.layoutTop = this.view.getTop();
    this.layoutLeft = this.view.getLeft();
    updateOffsets();
  }
  
  public boolean setLeftAndRightOffset(int paramInt) {
    if (this.offsetLeft != paramInt) {
      this.offsetLeft = paramInt;
      updateOffsets();
      return true;
    } 
    return false;
  }
  
  public boolean setTopAndBottomOffset(int paramInt) {
    if (this.offsetTop != paramInt) {
      this.offsetTop = paramInt;
      updateOffsets();
      return true;
    } 
    return false;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/ViewOffsetHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */