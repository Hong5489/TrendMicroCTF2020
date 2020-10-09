package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
  protected ArrayList<ConstraintWidget> mChildren = new ArrayList<>();
  
  public WidgetContainer() {}
  
  public WidgetContainer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public WidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static Rectangle getBounds(ArrayList<ConstraintWidget> paramArrayList) {
    Rectangle rectangle = new Rectangle();
    if (paramArrayList.size() == 0)
      return rectangle; 
    int i = Integer.MAX_VALUE;
    int j = 0;
    int k = Integer.MAX_VALUE;
    int m = 0;
    byte b = 0;
    int n = paramArrayList.size();
    while (b < n) {
      ConstraintWidget constraintWidget = paramArrayList.get(b);
      int i1 = i;
      if (constraintWidget.getX() < i)
        i1 = constraintWidget.getX(); 
      int i2 = k;
      if (constraintWidget.getY() < k)
        i2 = constraintWidget.getY(); 
      k = j;
      if (constraintWidget.getRight() > j)
        k = constraintWidget.getRight(); 
      int i3 = m;
      if (constraintWidget.getBottom() > m)
        i3 = constraintWidget.getBottom(); 
      b++;
      i = i1;
      j = k;
      k = i2;
      m = i3;
    } 
    rectangle.setBounds(i, k, j - i, m - k);
    return rectangle;
  }
  
  public void add(ConstraintWidget paramConstraintWidget) {
    this.mChildren.add(paramConstraintWidget);
    if (paramConstraintWidget.getParent() != null)
      ((WidgetContainer)paramConstraintWidget.getParent()).remove(paramConstraintWidget); 
    paramConstraintWidget.setParent(this);
  }
  
  public void add(ConstraintWidget... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      add(paramVarArgs[b]); 
  }
  
  public ConstraintWidget findWidget(float paramFloat1, float paramFloat2) {
    WidgetContainer widgetContainer = null;
    int i = getDrawX();
    int j = getDrawY();
    int k = getWidth();
    int m = getHeight();
    ConstraintWidget constraintWidget = widgetContainer;
    if (paramFloat1 >= i) {
      constraintWidget = widgetContainer;
      if (paramFloat1 <= (k + i)) {
        constraintWidget = widgetContainer;
        if (paramFloat2 >= j) {
          constraintWidget = widgetContainer;
          if (paramFloat2 <= (m + j))
            constraintWidget = this; 
        } 
      } 
    } 
    k = 0;
    j = this.mChildren.size();
    while (k < j) {
      ConstraintWidget constraintWidget1 = this.mChildren.get(k);
      if (constraintWidget1 instanceof WidgetContainer) {
        constraintWidget1 = ((WidgetContainer)constraintWidget1).findWidget(paramFloat1, paramFloat2);
        if (constraintWidget1 != null)
          ConstraintWidget constraintWidget2 = constraintWidget1; 
      } else {
        m = constraintWidget1.getDrawX();
        int n = constraintWidget1.getDrawY();
        i = constraintWidget1.getWidth();
        int i1 = constraintWidget1.getHeight();
        if (paramFloat1 >= m && paramFloat1 <= (i + m) && paramFloat2 >= n && paramFloat2 <= (i1 + n))
          constraintWidget = constraintWidget1; 
      } 
      k++;
    } 
    return constraintWidget;
  }
  
  public ArrayList<ConstraintWidget> findWidgets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    Rectangle rectangle = new Rectangle();
    rectangle.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = 0;
    paramInt2 = this.mChildren.size();
    while (paramInt1 < paramInt2) {
      ConstraintWidget constraintWidget = this.mChildren.get(paramInt1);
      Rectangle rectangle1 = new Rectangle();
      rectangle1.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
      if (rectangle.intersects(rectangle1))
        arrayList.add(constraintWidget); 
      paramInt1++;
    } 
    return arrayList;
  }
  
  public ArrayList<ConstraintWidget> getChildren() {
    return this.mChildren;
  }
  
  public ConstraintWidgetContainer getRootConstraintContainer() {
    ConstraintWidget constraintWidget1 = getParent();
    ConstraintWidgetContainer constraintWidgetContainer = null;
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (this instanceof ConstraintWidgetContainer) {
      constraintWidgetContainer = (ConstraintWidgetContainer)this;
      constraintWidget2 = constraintWidget1;
    } 
    while (true) {
      ConstraintWidget constraintWidget = constraintWidget2;
      if (constraintWidget != null) {
        constraintWidget1 = constraintWidget.getParent();
        constraintWidget2 = constraintWidget1;
        if (constraintWidget instanceof ConstraintWidgetContainer) {
          constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget;
          constraintWidget2 = constraintWidget1;
        } 
        continue;
      } 
      return constraintWidgetContainer;
    } 
  }
  
  public void layout() {
    updateDrawPosition();
    ArrayList<ConstraintWidget> arrayList = this.mChildren;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof WidgetContainer)
        ((WidgetContainer)constraintWidget).layout(); 
    } 
  }
  
  public void remove(ConstraintWidget paramConstraintWidget) {
    this.mChildren.remove(paramConstraintWidget);
    paramConstraintWidget.setParent(null);
  }
  
  public void removeAllChildren() {
    this.mChildren.clear();
  }
  
  public void reset() {
    this.mChildren.clear();
    super.reset();
  }
  
  public void resetSolverVariables(Cache paramCache) {
    super.resetSolverVariables(paramCache);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetSolverVariables(paramCache); 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    super.setOffset(paramInt1, paramInt2);
    paramInt2 = this.mChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
      ((ConstraintWidget)this.mChildren.get(paramInt1)).setOffset(getRootX(), getRootY()); 
  }
  
  public void updateDrawPosition() {
    super.updateDrawPosition();
    ArrayList<ConstraintWidget> arrayList = this.mChildren;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      constraintWidget.setOffset(getDrawX(), getDrawY());
      if (!(constraintWidget instanceof ConstraintWidgetContainer))
        constraintWidget.updateDrawPosition(); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/WidgetContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */