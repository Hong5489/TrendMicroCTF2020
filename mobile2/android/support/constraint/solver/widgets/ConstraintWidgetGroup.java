package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintWidgetGroup {
  public List<ConstraintWidget> mConstrainedGroup;
  
  public final int[] mGroupDimensions = new int[] { -1, -1 };
  
  int mGroupHeight = -1;
  
  int mGroupWidth = -1;
  
  public boolean mSkipSolver = false;
  
  List<ConstraintWidget> mStartHorizontalWidgets = new ArrayList<>();
  
  List<ConstraintWidget> mStartVerticalWidgets = new ArrayList<>();
  
  List<ConstraintWidget> mUnresolvedWidgets = new ArrayList<>();
  
  HashSet<ConstraintWidget> mWidgetsToSetHorizontal = new HashSet<>();
  
  HashSet<ConstraintWidget> mWidgetsToSetVertical = new HashSet<>();
  
  List<ConstraintWidget> mWidgetsToSolve = new ArrayList<>();
  
  ConstraintWidgetGroup(List<ConstraintWidget> paramList) {
    this.mConstrainedGroup = paramList;
  }
  
  ConstraintWidgetGroup(List<ConstraintWidget> paramList, boolean paramBoolean) {
    this.mConstrainedGroup = paramList;
    this.mSkipSolver = paramBoolean;
  }
  
  private void getWidgetsToSolveTraversal(ArrayList<ConstraintWidget> paramArrayList, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget.mGroupsToSolver)
      return; 
    paramArrayList.add(paramConstraintWidget);
    paramConstraintWidget.mGroupsToSolver = true;
    if (paramConstraintWidget.isFullyResolved())
      return; 
    if (paramConstraintWidget instanceof Helper) {
      Helper helper = (Helper)paramConstraintWidget;
      int j = helper.mWidgetsCount;
      for (byte b1 = 0; b1 < j; b1++)
        getWidgetsToSolveTraversal(paramArrayList, helper.mWidgets[b1]); 
    } 
    int i = paramConstraintWidget.mListAnchors.length;
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = (paramConstraintWidget.mListAnchors[b]).mTarget;
      if (constraintAnchor != null) {
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        if (constraintAnchor != null && constraintWidget != paramConstraintWidget.getParent())
          getWidgetsToSolveTraversal(paramArrayList, constraintWidget); 
      } 
    } 
  }
  
  private void updateResolvedDimension(ConstraintWidget paramConstraintWidget) {
    byte b = 0;
    if (paramConstraintWidget.mOptimizerMeasurable) {
      if (paramConstraintWidget.isFullyResolved())
        return; 
      ConstraintAnchor constraintAnchor = paramConstraintWidget.mRight.mTarget;
      boolean bool = false;
      if (constraintAnchor != null) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i) {
        constraintAnchor = paramConstraintWidget.mRight.mTarget;
      } else {
        constraintAnchor = paramConstraintWidget.mLeft.mTarget;
      } 
      int j = b;
      if (constraintAnchor != null) {
        if (!constraintAnchor.mOwner.mOptimizerMeasured)
          updateResolvedDimension(constraintAnchor.mOwner); 
        if (constraintAnchor.mType == ConstraintAnchor.Type.RIGHT) {
          j = constraintAnchor.mOwner.mX + constraintAnchor.mOwner.getWidth();
        } else {
          j = b;
          if (constraintAnchor.mType == ConstraintAnchor.Type.LEFT)
            j = constraintAnchor.mOwner.mX; 
        } 
      } 
      if (i) {
        j -= paramConstraintWidget.mRight.getMargin();
      } else {
        j += paramConstraintWidget.mLeft.getMargin() + paramConstraintWidget.getWidth();
      } 
      paramConstraintWidget.setHorizontalDimension(j - paramConstraintWidget.getWidth(), j);
      if (paramConstraintWidget.mBaseline.mTarget != null) {
        constraintAnchor = paramConstraintWidget.mBaseline.mTarget;
        if (!constraintAnchor.mOwner.mOptimizerMeasured)
          updateResolvedDimension(constraintAnchor.mOwner); 
        j = constraintAnchor.mOwner.mY + constraintAnchor.mOwner.mBaselineDistance - paramConstraintWidget.mBaselineDistance;
        paramConstraintWidget.setVerticalDimension(j, paramConstraintWidget.mHeight + j);
        paramConstraintWidget.mOptimizerMeasured = true;
        return;
      } 
      if (paramConstraintWidget.mBottom.mTarget != null)
        bool = true; 
      if (bool) {
        constraintAnchor = paramConstraintWidget.mBottom.mTarget;
      } else {
        constraintAnchor = paramConstraintWidget.mTop.mTarget;
      } 
      int i = j;
      if (constraintAnchor != null) {
        if (!constraintAnchor.mOwner.mOptimizerMeasured)
          updateResolvedDimension(constraintAnchor.mOwner); 
        if (constraintAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
          i = constraintAnchor.mOwner.mY + constraintAnchor.mOwner.getHeight();
        } else {
          i = j;
          if (constraintAnchor.mType == ConstraintAnchor.Type.TOP)
            i = constraintAnchor.mOwner.mY; 
        } 
      } 
      if (bool) {
        j = i - paramConstraintWidget.mBottom.getMargin();
      } else {
        j = i + paramConstraintWidget.mTop.getMargin() + paramConstraintWidget.getHeight();
      } 
      paramConstraintWidget.setVerticalDimension(j - paramConstraintWidget.getHeight(), j);
      paramConstraintWidget.mOptimizerMeasured = true;
    } 
  }
  
  void addWidgetsToSet(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      this.mWidgetsToSetHorizontal.add(paramConstraintWidget);
    } else if (paramInt == 1) {
      this.mWidgetsToSetVertical.add(paramConstraintWidget);
    } 
  }
  
  public List<ConstraintWidget> getStartWidgets(int paramInt) {
    return (paramInt == 0) ? this.mStartHorizontalWidgets : ((paramInt == 1) ? this.mStartVerticalWidgets : null);
  }
  
  Set<ConstraintWidget> getWidgetsToSet(int paramInt) {
    return (paramInt == 0) ? this.mWidgetsToSetHorizontal : ((paramInt == 1) ? this.mWidgetsToSetVertical : null);
  }
  
  List<ConstraintWidget> getWidgetsToSolve() {
    if (!this.mWidgetsToSolve.isEmpty())
      return this.mWidgetsToSolve; 
    int i = this.mConstrainedGroup.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mConstrainedGroup.get(b);
      if (!constraintWidget.mOptimizerMeasurable)
        getWidgetsToSolveTraversal((ArrayList<ConstraintWidget>)this.mWidgetsToSolve, constraintWidget); 
    } 
    this.mUnresolvedWidgets.clear();
    this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
    this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
    return this.mWidgetsToSolve;
  }
  
  void updateUnresolvedWidgets() {
    int i = this.mUnresolvedWidgets.size();
    for (byte b = 0; b < i; b++)
      updateResolvedDimension(this.mUnresolvedWidgets.get(b)); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/ConstraintWidgetGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */