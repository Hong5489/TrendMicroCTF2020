package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;

public class Optimizer {
  static final int FLAG_CHAIN_DANGLING = 1;
  
  static final int FLAG_RECOMPUTE_BOUNDS = 2;
  
  static final int FLAG_USE_OPTIMIZE = 0;
  
  public static final int OPTIMIZATION_BARRIER = 2;
  
  public static final int OPTIMIZATION_CHAIN = 4;
  
  public static final int OPTIMIZATION_DIMENSIONS = 8;
  
  public static final int OPTIMIZATION_DIRECT = 1;
  
  public static final int OPTIMIZATION_GROUPS = 32;
  
  public static final int OPTIMIZATION_NONE = 0;
  
  public static final int OPTIMIZATION_RATIO = 16;
  
  public static final int OPTIMIZATION_STANDARD = 7;
  
  static boolean[] flags = new boolean[3];
  
  static void analyze(int paramInt, ConstraintWidget paramConstraintWidget) {
    int i;
    paramConstraintWidget.updateResolutionNodes();
    ResolutionAnchor resolutionAnchor1 = paramConstraintWidget.mLeft.getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = paramConstraintWidget.mTop.getResolutionNode();
    ResolutionAnchor resolutionAnchor3 = paramConstraintWidget.mRight.getResolutionNode();
    ResolutionAnchor resolutionAnchor4 = paramConstraintWidget.mBottom.getResolutionNode();
    if ((paramInt & 0x8) == 8) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 0)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor1.type != 4 && resolutionAnchor3.type != 4)
      if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || (i && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(2);
          resolutionAnchor3.setType(2);
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.setOpposite(resolutionAnchor3, -paramConstraintWidget.getWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } 
      } else if (i) {
        i = paramConstraintWidget.getWidth();
        resolutionAnchor1.setType(1);
        resolutionAnchor3.setType(1);
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor1.setType(3);
            resolutionAnchor3.setType(3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, 0.0F);
            resolutionAnchor3.setOpposite(resolutionAnchor1, 0.0F);
          } else {
            resolutionAnchor1.setType(2);
            resolutionAnchor3.setType(2);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -i);
            resolutionAnchor3.setOpposite(resolutionAnchor1, i);
            paramConstraintWidget.setWidth(i);
          } 
        } 
      }  
    if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 1)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor2.type != 4 && resolutionAnchor4.type != 4) {
      if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || (i != 0 && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaseline.mTarget != null) {
            paramConstraintWidget.mBaseline.getResolutionNode().setType(1);
            resolutionAnchor2.dependsOn(1, paramConstraintWidget.mBaseline.getResolutionNode(), -paramConstraintWidget.mBaselineDistance);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(2);
          resolutionAnchor4.setType(2);
          if (paramInt != 0) {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } else {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -paramConstraintWidget.getHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } 
        return;
      } 
      if (i != 0) {
        i = paramConstraintWidget.getHeight();
        resolutionAnchor2.setType(1);
        resolutionAnchor4.setType(1);
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor2.setType(3);
            resolutionAnchor4.setType(3);
            resolutionAnchor2.setOpposite(resolutionAnchor4, 0.0F);
            resolutionAnchor4.setOpposite(resolutionAnchor2, 0.0F);
          } else {
            resolutionAnchor2.setType(2);
            resolutionAnchor4.setType(2);
            resolutionAnchor2.setOpposite(resolutionAnchor4, -i);
            resolutionAnchor4.setOpposite(resolutionAnchor2, i);
            paramConstraintWidget.setHeight(i);
            if (paramConstraintWidget.mBaselineDistance > 0)
              paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
          } 
        } 
      } 
    } 
  }
  
  static boolean applyChainOptimized(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    ConstraintAnchor constraintAnchor;
    boolean bool1;
    boolean bool2;
    boolean bool3;
    ConstraintWidget constraintWidget2 = paramChainHead.mFirst;
    ConstraintWidget constraintWidget3 = paramChainHead.mLast;
    ConstraintWidget constraintWidget4 = paramChainHead.mFirstVisibleWidget;
    ConstraintWidget constraintWidget5 = paramChainHead.mLastVisibleWidget;
    ConstraintWidget constraintWidget6 = paramChainHead.mHead;
    float f1 = paramChainHead.mTotalWeight;
    ConstraintWidget constraintWidget7 = paramChainHead.mFirstMatchConstraintWidget;
    ConstraintWidget constraintWidget1 = paramChainHead.mLastMatchConstraintWidget;
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[paramInt1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      boolean bool = true;
    } else {
      boolean bool = false;
    } 
    if (paramInt1 == 0) {
      boolean bool5;
      if (constraintWidget6.mHorizontalChainStyle == 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (constraintWidget6.mHorizontalChainStyle == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (constraintWidget6.mHorizontalChainStyle == 2) {
        bool5 = true;
      } else {
        bool5 = false;
      } 
      boolean bool6 = bool1;
      bool1 = bool5;
      bool3 = bool2;
      bool2 = bool6;
    } else {
      boolean bool;
      if (constraintWidget6.mVerticalChainStyle == 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (constraintWidget6.mVerticalChainStyle == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (constraintWidget6.mVerticalChainStyle == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      bool3 = bool2;
      bool2 = bool1;
      bool1 = bool;
    } 
    float f2 = 0.0F;
    float f3 = 0.0F;
    int i = 0;
    boolean bool4 = false;
    constraintWidget6 = constraintWidget2;
    int j = 0;
    while (!bool4) {
      int k = j;
      float f7 = f2;
      float f8 = f3;
      if (constraintWidget6.getVisibility() != 8) {
        k = j + 1;
        if (paramInt1 == 0) {
          f7 = f2 + constraintWidget6.getWidth();
        } else {
          f7 = f2 + constraintWidget6.getHeight();
        } 
        f8 = f7;
        if (constraintWidget6 != constraintWidget4)
          f8 = f7 + constraintWidget6.mListAnchors[paramInt2].getMargin(); 
        f7 = f8;
        if (constraintWidget6 != constraintWidget5)
          f7 = f8 + constraintWidget6.mListAnchors[paramInt2 + 1].getMargin(); 
        f8 = f3 + constraintWidget6.mListAnchors[paramInt2].getMargin() + constraintWidget6.mListAnchors[paramInt2 + 1].getMargin();
      } 
      ConstraintAnchor constraintAnchor1 = constraintWidget6.mListAnchors[paramInt2];
      j = i;
      if (constraintWidget6.getVisibility() != 8) {
        j = i;
        if (constraintWidget6.mListDimensionBehaviors[paramInt1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          j = i + 1;
          if (paramInt1 == 0) {
            if (constraintWidget6.mMatchConstraintDefaultWidth != 0)
              return false; 
            if (constraintWidget6.mMatchConstraintMinWidth != 0 || constraintWidget6.mMatchConstraintMaxWidth != 0)
              return false; 
          } else {
            if (constraintWidget6.mMatchConstraintDefaultHeight != 0)
              return false; 
            if (constraintWidget6.mMatchConstraintMinHeight != 0 || constraintWidget6.mMatchConstraintMaxHeight != 0)
              return false; 
          } 
          if (constraintWidget6.mDimensionRatio != 0.0F)
            return false; 
        } 
      } 
      constraintAnchor1 = (constraintWidget6.mListAnchors[paramInt2 + 1]).mTarget;
      if (constraintAnchor1 != null) {
        ConstraintWidget constraintWidget = constraintAnchor1.mOwner;
        if ((constraintWidget.mListAnchors[paramInt2]).mTarget == null || (constraintWidget.mListAnchors[paramInt2]).mTarget.mOwner != constraintWidget6)
          constraintWidget = null; 
      } else {
        constraintAnchor1 = null;
      } 
      if (constraintAnchor1 != null) {
        constraintAnchor = constraintAnchor1;
      } else {
        bool4 = true;
      } 
      i = j;
      j = k;
      f2 = f7;
      f3 = f8;
    } 
    ResolutionAnchor resolutionAnchor2 = constraintWidget2.mListAnchors[paramInt2].getResolutionNode();
    ResolutionAnchor resolutionAnchor1 = constraintWidget3.mListAnchors[paramInt2 + 1].getResolutionNode();
    if (resolutionAnchor2.target == null || resolutionAnchor1.target == null)
      return false; 
    if (resolutionAnchor2.target.state != 1 || resolutionAnchor1.target.state != 1)
      return false; 
    if (i > 0 && i != j)
      return false; 
    float f4 = 0.0F;
    float f5 = 0.0F;
    if (bool1 || bool2 || bool3) {
      if (constraintWidget4 != null)
        f5 = constraintWidget4.mListAnchors[paramInt2].getMargin(); 
      f4 = f5;
      if (constraintWidget5 != null)
        f4 = f5 + constraintWidget5.mListAnchors[paramInt2 + 1].getMargin(); 
    } 
    float f6 = resolutionAnchor2.target.resolvedOffset;
    f5 = resolutionAnchor1.target.resolvedOffset;
    if (f6 < f5) {
      f5 = f5 - f6 - f2;
    } else {
      f5 = f6 - f5 - f2;
    } 
    if (i > 0 && i == j) {
      if (constraintAnchor.getParent() != null && (constraintAnchor.getParent()).mListDimensionBehaviors[paramInt1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
        return false; 
      f3 = f5 + f2 - f3;
      ConstraintWidget constraintWidget = constraintWidget2;
      f5 = f6;
      while (constraintWidget != null) {
        if (LinearSystem.sMetrics != null) {
          Metrics metrics = LinearSystem.sMetrics;
          metrics.nonresolvedWidgets--;
          metrics = LinearSystem.sMetrics;
          metrics.resolvedWidgets++;
          metrics = LinearSystem.sMetrics;
          metrics.chainConnectionResolved++;
        } 
        constraintWidget1 = constraintWidget.mNextChainWidget[paramInt1];
        if (constraintWidget1 != null || constraintWidget == constraintWidget3) {
          f4 = f3 / i;
          if (f1 > 0.0F)
            if (constraintWidget.mWeight[paramInt1] == -1.0F) {
              f4 = 0.0F;
            } else {
              f4 = constraintWidget.mWeight[paramInt1] * f3 / f1;
            }  
          if (constraintWidget.getVisibility() == 8)
            f4 = 0.0F; 
          f5 += constraintWidget.mListAnchors[paramInt2].getMargin();
          constraintWidget.mListAnchors[paramInt2].getResolutionNode().resolve(resolutionAnchor2.resolvedTarget, f5);
          constraintWidget.mListAnchors[paramInt2 + 1].getResolutionNode().resolve(resolutionAnchor2.resolvedTarget, f5 + f4);
          constraintWidget.mListAnchors[paramInt2].getResolutionNode().addResolvedValue(paramLinearSystem);
          constraintWidget.mListAnchors[paramInt2 + 1].getResolutionNode().addResolvedValue(paramLinearSystem);
          f5 = f5 + f4 + constraintWidget.mListAnchors[paramInt2 + 1].getMargin();
        } 
        constraintWidget = constraintWidget1;
      } 
      return true;
    } 
    if (f5 < 0.0F) {
      bool2 = true;
      i = 0;
      bool1 = false;
    } else {
      i = bool2;
      bool2 = bool1;
      bool1 = bool3;
    } 
    if (bool2) {
      f3 = constraintWidget2.getBiasPercent(paramInt1);
      constraintWidget1 = constraintWidget2;
      f5 = f3 * (f5 - f4) + f6;
      f4 = f1;
      while (constraintWidget1 != null) {
        if (LinearSystem.sMetrics != null) {
          Metrics metrics = LinearSystem.sMetrics;
          metrics.nonresolvedWidgets--;
          metrics = LinearSystem.sMetrics;
          metrics.resolvedWidgets++;
          metrics = LinearSystem.sMetrics;
          metrics.chainConnectionResolved++;
        } 
        constraintWidget2 = constraintWidget1.mNextChainWidget[paramInt1];
        if (constraintWidget2 != null || constraintWidget1 == constraintWidget3) {
          if (paramInt1 == 0) {
            f3 = constraintWidget1.getWidth();
          } else {
            f3 = constraintWidget1.getHeight();
          } 
          f5 += constraintWidget1.mListAnchors[paramInt2].getMargin();
          constraintWidget1.mListAnchors[paramInt2].getResolutionNode().resolve(resolutionAnchor2.resolvedTarget, f5);
          constraintWidget1.mListAnchors[paramInt2 + 1].getResolutionNode().resolve(resolutionAnchor2.resolvedTarget, f5 + f3);
          constraintWidget1.mListAnchors[paramInt2].getResolutionNode().addResolvedValue(paramLinearSystem);
          constraintWidget1.mListAnchors[paramInt2 + 1].getResolutionNode().addResolvedValue(paramLinearSystem);
          f5 = f5 + f3 + constraintWidget1.mListAnchors[paramInt2 + 1].getMargin();
        } 
        constraintWidget1 = constraintWidget2;
      } 
    } else if (i != 0 || bool1) {
      ConstraintWidget constraintWidget;
      if (i != 0) {
        f2 = f5 - f4;
      } else {
        f2 = f5;
        if (bool1)
          f2 = f5 - f4; 
      } 
      f3 = f2 / (j + 1);
      if (bool1)
        if (j > 1) {
          f3 = f2 / (j - 1);
        } else {
          f3 = f2 / 2.0F;
        }  
      f2 = f6;
      f5 = f2;
      if (constraintWidget2.getVisibility() != 8)
        f5 = f2 + f3; 
      f2 = f5;
      if (bool1) {
        f2 = f5;
        if (j > 1)
          f2 = f6 + constraintWidget4.mListAnchors[paramInt2].getMargin(); 
      } 
      if (i != 0 && constraintWidget4 != null) {
        f5 = constraintWidget4.mListAnchors[paramInt2].getMargin();
        constraintWidget = constraintWidget2;
        f5 = f2 + f5;
      } else {
        constraintWidget = constraintWidget2;
        f5 = f2;
      } 
      while (constraintWidget != null) {
        if (LinearSystem.sMetrics != null) {
          Metrics metrics = LinearSystem.sMetrics;
          metrics.nonresolvedWidgets--;
          metrics = LinearSystem.sMetrics;
          metrics.resolvedWidgets++;
          metrics = LinearSystem.sMetrics;
          metrics.chainConnectionResolved++;
        } 
        constraintWidget1 = constraintWidget.mNextChainWidget[paramInt1];
        if (constraintWidget1 != null || constraintWidget == constraintWidget3) {
          if (paramInt1 == 0) {
            f2 = constraintWidget.getWidth();
          } else {
            f2 = constraintWidget.getHeight();
          } 
          if (constraintWidget != constraintWidget4)
            f5 += constraintWidget.mListAnchors[paramInt2].getMargin(); 
          constraintWidget.mListAnchors[paramInt2].getResolutionNode().resolve(resolutionAnchor2.resolvedTarget, f5);
          constraintWidget.mListAnchors[paramInt2 + 1].getResolutionNode().resolve(resolutionAnchor2.resolvedTarget, f5 + f2);
          constraintWidget.mListAnchors[paramInt2].getResolutionNode().addResolvedValue(paramLinearSystem);
          constraintWidget.mListAnchors[paramInt2 + 1].getResolutionNode().addResolvedValue(paramLinearSystem);
          f2 = f5 + constraintWidget.mListAnchors[paramInt2 + 1].getMargin() + f2;
          if (constraintWidget1 != null) {
            f5 = f2;
            if (constraintWidget1.getVisibility() != 8)
              f5 = f2 + f3; 
          } else {
            f5 = f2;
          } 
        } 
        constraintWidget = constraintWidget1;
      } 
    } 
    return true;
  }
  
  static void checkMatchParent(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mLeft.mMargin;
      int j = paramConstraintWidgetContainer.getWidth() - paramConstraintWidget.mRight.mMargin;
      paramConstraintWidget.mLeft.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mLeft);
      paramConstraintWidget.mRight.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mRight);
      paramLinearSystem.addEquality(paramConstraintWidget.mLeft.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mRight.mSolverVariable, j);
      paramConstraintWidget.mHorizontalResolution = 2;
      paramConstraintWidget.setHorizontalDimension(i, j);
    } 
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int j = paramConstraintWidget.mTop.mMargin;
      int i = paramConstraintWidgetContainer.getHeight() - paramConstraintWidget.mBottom.mMargin;
      paramConstraintWidget.mTop.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mTop);
      paramConstraintWidget.mBottom.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBottom);
      paramLinearSystem.addEquality(paramConstraintWidget.mTop.mSolverVariable, j);
      paramLinearSystem.addEquality(paramConstraintWidget.mBottom.mSolverVariable, i);
      if (paramConstraintWidget.mBaselineDistance > 0 || paramConstraintWidget.getVisibility() == 8) {
        paramConstraintWidget.mBaseline.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBaseline);
        paramLinearSystem.addEquality(paramConstraintWidget.mBaseline.mSolverVariable, paramConstraintWidget.mBaselineDistance + j);
      } 
      paramConstraintWidget.mVerticalResolution = 2;
      paramConstraintWidget.setVerticalDimension(j, i);
    } 
  }
  
  private static boolean optimizableMatchConstraint(ConstraintWidget paramConstraintWidget, int paramInt) {
    ConstraintWidget.DimensionBehaviour[] arrayOfDimensionBehaviour;
    if (paramConstraintWidget.mListDimensionBehaviors[paramInt] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      return false; 
    float f = paramConstraintWidget.mDimensionRatio;
    boolean bool = true;
    if (f != 0.0F) {
      arrayOfDimensionBehaviour = paramConstraintWidget.mListDimensionBehaviors;
      if (paramInt == 0) {
        paramInt = bool;
      } else {
        paramInt = 0;
      } 
      return (arrayOfDimensionBehaviour[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? false : false;
    } 
    if (paramInt == 0) {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultWidth != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinWidth != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxWidth != 0)
        return false; 
    } else {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultHeight != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinHeight != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxHeight != 0)
        return false; 
    } 
    return true;
  }
  
  static void setOptimizedWidget(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int i = paramInt1 * 2;
    int j = i + 1;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedTarget = (paramConstraintWidget.getParent()).mLeft.getResolutionNode();
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedOffset = paramInt2;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).state = 1;
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedTarget = paramConstraintWidget.mListAnchors[i].getResolutionNode();
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedOffset = paramConstraintWidget.getLength(paramInt1);
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).state = 1;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/Optimizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */