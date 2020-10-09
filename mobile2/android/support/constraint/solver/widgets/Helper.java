package android.support.constraint.solver.widgets;

import java.util.Arrays;

public class Helper extends ConstraintWidget {
  protected ConstraintWidget[] mWidgets = new ConstraintWidget[4];
  
  protected int mWidgetsCount = 0;
  
  public void add(ConstraintWidget paramConstraintWidget) {
    int i = this.mWidgetsCount;
    ConstraintWidget[] arrayOfConstraintWidget = this.mWidgets;
    if (i + 1 > arrayOfConstraintWidget.length)
      this.mWidgets = Arrays.<ConstraintWidget>copyOf(arrayOfConstraintWidget, arrayOfConstraintWidget.length * 2); 
    arrayOfConstraintWidget = this.mWidgets;
    i = this.mWidgetsCount;
    arrayOfConstraintWidget[i] = paramConstraintWidget;
    this.mWidgetsCount = i + 1;
  }
  
  public void removeAllIds() {
    this.mWidgetsCount = 0;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/Helper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */