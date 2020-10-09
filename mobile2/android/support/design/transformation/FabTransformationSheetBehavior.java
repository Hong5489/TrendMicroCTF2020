package android.support.design.transformation;

import android.content.Context;
import android.os.Build;
import android.support.design.R;
import android.support.design.animation.MotionSpec;
import android.support.design.animation.Positioning;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import java.util.HashMap;
import java.util.Map;

public class FabTransformationSheetBehavior extends FabTransformationBehavior {
  private Map<View, Integer> importantForAccessibilityMap;
  
  public FabTransformationSheetBehavior() {}
  
  public FabTransformationSheetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void updateImportantForAccessibility(View paramView, boolean paramBoolean) {
    ViewParent viewParent = paramView.getParent();
    if (!(viewParent instanceof CoordinatorLayout))
      return; 
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)viewParent;
    int i = coordinatorLayout.getChildCount();
    if (Build.VERSION.SDK_INT >= 16 && paramBoolean)
      this.importantForAccessibilityMap = new HashMap<>(i); 
    for (byte b = 0; b < i; b++) {
      boolean bool;
      View view = coordinatorLayout.getChildAt(b);
      if (view.getLayoutParams() instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams)view.getLayoutParams()).getBehavior() instanceof FabTransformationScrimBehavior) {
        bool = true;
      } else {
        bool = false;
      } 
      if (view != paramView && !bool)
        if (!paramBoolean) {
          Map<View, Integer> map = this.importantForAccessibilityMap;
          if (map != null && map.containsKey(view))
            ViewCompat.setImportantForAccessibility(view, ((Integer)this.importantForAccessibilityMap.get(view)).intValue()); 
        } else {
          if (Build.VERSION.SDK_INT >= 16)
            this.importantForAccessibilityMap.put(view, Integer.valueOf(view.getImportantForAccessibility())); 
          ViewCompat.setImportantForAccessibility(view, 4);
        }  
    } 
    if (!paramBoolean)
      this.importantForAccessibilityMap = null; 
  }
  
  protected FabTransformationBehavior.FabTransformationSpec onCreateMotionSpec(Context paramContext, boolean paramBoolean) {
    int i;
    if (paramBoolean) {
      i = R.animator.mtrl_fab_transformation_sheet_expand_spec;
    } else {
      i = R.animator.mtrl_fab_transformation_sheet_collapse_spec;
    } 
    FabTransformationBehavior.FabTransformationSpec fabTransformationSpec = new FabTransformationBehavior.FabTransformationSpec();
    fabTransformationSpec.timings = MotionSpec.createFromResource(paramContext, i);
    fabTransformationSpec.positioning = new Positioning(17, 0.0F, 0.0F);
    return fabTransformationSpec;
  }
  
  protected boolean onExpandedStateChange(View paramView1, View paramView2, boolean paramBoolean1, boolean paramBoolean2) {
    updateImportantForAccessibility(paramView2, paramBoolean1);
    return super.onExpandedStateChange(paramView1, paramView2, paramBoolean1, paramBoolean2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/transformation/FabTransformationSheetBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */