package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Analyzer {
  public static void determineGroups(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    if ((paramConstraintWidgetContainer.getOptimizationLevel() & 0x20) != 32) {
      singleGroup(paramConstraintWidgetContainer);
      return;
    } 
    paramConstraintWidgetContainer.mSkipSolver = true;
    paramConstraintWidgetContainer.mGroupsWrapOptimized = false;
    paramConstraintWidgetContainer.mHorizontalWrapOptimized = false;
    paramConstraintWidgetContainer.mVerticalWrapOptimized = false;
    ArrayList<ConstraintWidget> arrayList = paramConstraintWidgetContainer.mChildren;
    List<ConstraintWidgetGroup> list = paramConstraintWidgetContainer.mWidgetGroups;
    if (paramConstraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (paramConstraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool1 || bool2) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    list.clear();
    for (ConstraintWidget constraintWidget : arrayList) {
      constraintWidget.mBelongingGroup = null;
      constraintWidget.mGroupsToSolver = false;
      constraintWidget.resetResolutionNodes();
    } 
    for (ConstraintWidget constraintWidget : arrayList) {
      if (constraintWidget.mBelongingGroup == null && !determineGroups(constraintWidget, list, bool3)) {
        singleGroup(paramConstraintWidgetContainer);
        paramConstraintWidgetContainer.mSkipSolver = false;
        return;
      } 
    } 
    int i = 0;
    int j = 0;
    for (ConstraintWidgetGroup constraintWidgetGroup : list) {
      i = Math.max(i, getMaxDimension(constraintWidgetGroup, 0));
      j = Math.max(j, getMaxDimension(constraintWidgetGroup, 1));
    } 
    if (bool1) {
      paramConstraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      paramConstraintWidgetContainer.setWidth(i);
      paramConstraintWidgetContainer.mGroupsWrapOptimized = true;
      paramConstraintWidgetContainer.mHorizontalWrapOptimized = true;
      paramConstraintWidgetContainer.mWrapFixedWidth = i;
    } 
    if (bool2) {
      paramConstraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      paramConstraintWidgetContainer.setHeight(j);
      paramConstraintWidgetContainer.mGroupsWrapOptimized = true;
      paramConstraintWidgetContainer.mVerticalWrapOptimized = true;
      paramConstraintWidgetContainer.mWrapFixedHeight = j;
    } 
    setPosition(list, 0, paramConstraintWidgetContainer.getWidth());
    setPosition(list, 1, paramConstraintWidgetContainer.getHeight());
  }
  
  private static boolean determineGroups(ConstraintWidget paramConstraintWidget, List<ConstraintWidgetGroup> paramList, boolean paramBoolean) {
    ConstraintWidgetGroup constraintWidgetGroup = new ConstraintWidgetGroup(new ArrayList<>(), true);
    paramList.add(constraintWidgetGroup);
    return traverse(paramConstraintWidget, constraintWidgetGroup, paramList, paramBoolean);
  }
  
  private static int getMaxDimension(ConstraintWidgetGroup paramConstraintWidgetGroup, int paramInt) {
    int i = 0;
    int j = paramInt * 2;
    List<ConstraintWidget> list = paramConstraintWidgetGroup.getStartWidgets(paramInt);
    int k = list.size();
    for (byte b = 0; b < k; b++) {
      boolean bool;
      ConstraintWidget constraintWidget = list.get(b);
      if ((constraintWidget.mListAnchors[j + 1]).mTarget == null || ((constraintWidget.mListAnchors[j]).mTarget != null && (constraintWidget.mListAnchors[j + 1]).mTarget != null)) {
        bool = true;
      } else {
        bool = false;
      } 
      i = Math.max(i, getMaxDimensionTraversal(constraintWidget, paramInt, bool, 0));
    } 
    paramConstraintWidgetGroup.mGroupDimensions[paramInt] = i;
    return i;
  }
  
  private static int getMaxDimensionTraversal(ConstraintWidget paramConstraintWidget, int paramInt1, boolean paramBoolean, int paramInt2) {
    byte b;
    int i4;
    boolean bool = paramConstraintWidget.mOptimizerMeasurable;
    int i = 0;
    if (!bool)
      return 0; 
    int j = 0;
    int k = 0;
    int m = i;
    if (paramConstraintWidget.mBaseline.mTarget != null) {
      m = i;
      if (paramInt1 == 1)
        m = 1; 
    } 
    if (paramBoolean) {
      n = paramConstraintWidget.getBaselineDistance();
      i1 = paramConstraintWidget.getHeight() - paramConstraintWidget.getBaselineDistance();
      i = paramInt1 * 2;
      i2 = i + 1;
    } else {
      n = paramConstraintWidget.getHeight() - paramConstraintWidget.getBaselineDistance();
      i1 = paramConstraintWidget.getBaselineDistance();
      i2 = paramInt1 * 2;
      i = i2 + 1;
    } 
    if ((paramConstraintWidget.mListAnchors[i2]).mTarget != null && (paramConstraintWidget.mListAnchors[i]).mTarget == null) {
      b = -1;
      int i9 = i2;
      i2 = i;
      i = i9;
    } else {
      b = 1;
    } 
    if (m != 0) {
      i4 = paramInt2 - n;
    } else {
      i4 = paramInt2;
    } 
    int i5 = paramConstraintWidget.mListAnchors[i].getMargin() * b + getParentBiasOffset(paramConstraintWidget, paramInt1);
    int i6 = i5 + i4;
    if (paramInt1 == 0) {
      paramInt2 = paramConstraintWidget.getWidth();
    } else {
      paramInt2 = paramConstraintWidget.getHeight();
    } 
    int i7 = paramInt2 * b;
    Iterator<ResolutionNode> iterator = (paramConstraintWidget.mListAnchors[i].getResolutionNode()).dependents.iterator();
    int i3 = k;
    for (paramInt2 = j; iterator.hasNext(); paramInt2 = Math.max(paramInt2, getMaxDimensionTraversal(((ResolutionAnchor)iterator.next()).myAnchor.mOwner, paramInt1, paramBoolean, i6)));
    iterator = (paramConstraintWidget.mListAnchors[i2].getResolutionNode()).dependents.iterator();
    j = i3;
    i3 = i2;
    int i2;
    for (i2 = j; iterator.hasNext(); i2 = Math.max(i2, getMaxDimensionTraversal(((ResolutionAnchor)iterator.next()).myAnchor.mOwner, paramInt1, paramBoolean, i7 + i6)));
    if (m != 0) {
      j = paramInt2 - n;
      k = i2 + i1;
    } else {
      if (paramInt1 == 0) {
        j = paramConstraintWidget.getWidth();
      } else {
        j = paramConstraintWidget.getHeight();
      } 
      k = i2 + j * b;
      j = paramInt2;
    } 
    paramInt2 = 0;
    int i8 = 0;
    if (paramInt1 == 1) {
      iterator = (paramConstraintWidget.mBaseline.getResolutionNode()).dependents.iterator();
      paramInt2 = i7;
      i2 = i;
      for (i = i8; iterator.hasNext(); i = Math.max(i, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, paramInt1, paramBoolean, i1 * b + i6))) {
        ResolutionAnchor resolutionAnchor = (ResolutionAnchor)iterator.next();
        if (b == 1) {
          i = Math.max(i, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, paramInt1, paramBoolean, n + i6));
          continue;
        } 
      } 
      i8 = i2;
      i6 = paramInt2;
      paramInt2 = i;
      i2 = i8;
      i7 = i6;
      if ((paramConstraintWidget.mBaseline.getResolutionNode()).dependents.size() > 0) {
        paramInt2 = i;
        i2 = i8;
        i7 = i6;
        if (m == 0)
          if (b == 1) {
            paramInt2 = i + n;
            i2 = i8;
            i7 = i6;
          } else {
            paramInt2 = i - i1;
            i2 = i8;
            i7 = i6;
          }  
      } 
    } else {
      i2 = i;
    } 
    int n = Math.max(j, Math.max(k, paramInt2));
    i = i4 + i5;
    paramInt2 = i + i7;
    int i1 = i;
    m = paramInt2;
    if (b == -1) {
      m = i;
      i1 = paramInt2;
    } 
    if (paramBoolean) {
      Optimizer.setOptimizedWidget(paramConstraintWidget, paramInt1, i1);
      paramConstraintWidget.setFrame(i1, m, paramInt1);
    } else {
      paramConstraintWidget.mBelongingGroup.addWidgetsToSet(paramConstraintWidget, paramInt1);
      paramConstraintWidget.setRelativePositioning(i1, paramInt1);
    } 
    if (paramConstraintWidget.getDimensionBehaviour(paramInt1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && paramConstraintWidget.mDimensionRatio != 0.0F)
      paramConstraintWidget.mBelongingGroup.addWidgetsToSet(paramConstraintWidget, paramInt1); 
    if ((paramConstraintWidget.mListAnchors[i2]).mTarget != null && (paramConstraintWidget.mListAnchors[i3]).mTarget != null) {
      ConstraintWidget constraintWidget = paramConstraintWidget.getParent();
      if ((paramConstraintWidget.mListAnchors[i2]).mTarget.mOwner == constraintWidget && (paramConstraintWidget.mListAnchors[i3]).mTarget.mOwner == constraintWidget)
        paramConstraintWidget.mBelongingGroup.addWidgetsToSet(paramConstraintWidget, paramInt1); 
    } 
    return i5 + n;
  }
  
  private static int getParentBiasOffset(ConstraintWidget paramConstraintWidget, int paramInt) {
    int i = paramInt * 2;
    ConstraintAnchor constraintAnchor1 = paramConstraintWidget.mListAnchors[i];
    ConstraintAnchor constraintAnchor2 = paramConstraintWidget.mListAnchors[i + 1];
    if (constraintAnchor1.mTarget != null && constraintAnchor1.mTarget.mOwner == paramConstraintWidget.mParent && constraintAnchor2.mTarget != null && constraintAnchor2.mTarget.mOwner == paramConstraintWidget.mParent) {
      float f;
      i = paramConstraintWidget.mParent.getLength(paramInt);
      if (paramInt == 0) {
        f = paramConstraintWidget.mHorizontalBiasPercent;
      } else {
        f = paramConstraintWidget.mVerticalBiasPercent;
      } 
      paramInt = paramConstraintWidget.getLength(paramInt);
      return (int)((i - constraintAnchor1.getMargin() - constraintAnchor2.getMargin() - paramInt) * f);
    } 
    return 0;
  }
  
  private static void invalidate(ConstraintWidgetContainer paramConstraintWidgetContainer, ConstraintWidget paramConstraintWidget, ConstraintWidgetGroup paramConstraintWidgetGroup) {
    paramConstraintWidgetGroup.mSkipSolver = false;
    paramConstraintWidgetContainer.mSkipSolver = false;
    paramConstraintWidget.mOptimizerMeasurable = false;
  }
  
  private static int resolveDimensionRatio(ConstraintWidget paramConstraintWidget) {
    int i = -1;
    if (paramConstraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      if (paramConstraintWidget.mDimensionRatioSide == 0) {
        i = (int)(paramConstraintWidget.getHeight() * paramConstraintWidget.mDimensionRatio);
      } else {
        i = (int)(paramConstraintWidget.getHeight() / paramConstraintWidget.mDimensionRatio);
      } 
      paramConstraintWidget.setWidth(i);
    } else if (paramConstraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      if (paramConstraintWidget.mDimensionRatioSide == 1) {
        i = (int)(paramConstraintWidget.getWidth() * paramConstraintWidget.mDimensionRatio);
      } else {
        i = (int)(paramConstraintWidget.getWidth() / paramConstraintWidget.mDimensionRatio);
      } 
      paramConstraintWidget.setHeight(i);
    } 
    return i;
  }
  
  private static void setConnection(ConstraintAnchor paramConstraintAnchor) {
    ResolutionAnchor resolutionAnchor = paramConstraintAnchor.getResolutionNode();
    if (paramConstraintAnchor.mTarget != null && paramConstraintAnchor.mTarget.mTarget != paramConstraintAnchor)
      paramConstraintAnchor.mTarget.getResolutionNode().addDependent(resolutionAnchor); 
  }
  
  public static void setPosition(List<ConstraintWidgetGroup> paramList, int paramInt1, int paramInt2) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      for (ConstraintWidget constraintWidget : ((ConstraintWidgetGroup)paramList.get(b)).getWidgetsToSet(paramInt1)) {
        if (constraintWidget.mOptimizerMeasurable)
          updateSizeDependentWidgets(constraintWidget, paramInt1, paramInt2); 
      } 
    } 
  }
  
  private static void singleGroup(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    paramConstraintWidgetContainer.mWidgetGroups.clear();
    paramConstraintWidgetContainer.mWidgetGroups.add(0, new ConstraintWidgetGroup(paramConstraintWidgetContainer.mChildren));
  }
  
  private static boolean traverse(ConstraintWidget paramConstraintWidget, ConstraintWidgetGroup paramConstraintWidgetGroup, List<ConstraintWidgetGroup> paramList, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 6
    //   4: iconst_1
    //   5: ireturn
    //   6: aload_0
    //   7: iconst_0
    //   8: putfield mOptimizerMeasured : Z
    //   11: aload_0
    //   12: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   15: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   18: astore #4
    //   20: aload_0
    //   21: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   24: ifnonnull -> 938
    //   27: aload_0
    //   28: iconst_1
    //   29: putfield mOptimizerMeasurable : Z
    //   32: aload_1
    //   33: getfield mConstrainedGroup : Ljava/util/List;
    //   36: aload_0
    //   37: invokeinterface add : (Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_0
    //   44: aload_1
    //   45: putfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   48: aload_0
    //   49: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   52: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   55: ifnonnull -> 121
    //   58: aload_0
    //   59: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   62: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   65: ifnonnull -> 121
    //   68: aload_0
    //   69: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   72: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   75: ifnonnull -> 121
    //   78: aload_0
    //   79: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   82: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   85: ifnonnull -> 121
    //   88: aload_0
    //   89: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   92: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   95: ifnonnull -> 121
    //   98: aload_0
    //   99: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   102: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   105: ifnonnull -> 121
    //   108: aload #4
    //   110: aload_0
    //   111: aload_1
    //   112: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   115: iload_3
    //   116: ifeq -> 121
    //   119: iconst_0
    //   120: ireturn
    //   121: aload_0
    //   122: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   125: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   128: ifnull -> 209
    //   131: aload_0
    //   132: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   135: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   138: ifnull -> 209
    //   141: aload #4
    //   143: invokevirtual getVerticalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   146: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   149: if_acmpne -> 155
    //   152: goto -> 155
    //   155: iload_3
    //   156: ifeq -> 168
    //   159: aload #4
    //   161: aload_0
    //   162: aload_1
    //   163: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   166: iconst_0
    //   167: ireturn
    //   168: aload_0
    //   169: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   172: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   175: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   178: aload_0
    //   179: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   182: if_acmpne -> 202
    //   185: aload_0
    //   186: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   189: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   192: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   195: aload_0
    //   196: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   199: if_acmpeq -> 209
    //   202: aload #4
    //   204: aload_0
    //   205: aload_1
    //   206: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   209: aload_0
    //   210: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   213: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   216: ifnull -> 297
    //   219: aload_0
    //   220: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   223: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   226: ifnull -> 297
    //   229: aload #4
    //   231: invokevirtual getHorizontalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   234: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   237: if_acmpne -> 243
    //   240: goto -> 243
    //   243: iload_3
    //   244: ifeq -> 256
    //   247: aload #4
    //   249: aload_0
    //   250: aload_1
    //   251: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   254: iconst_0
    //   255: ireturn
    //   256: aload_0
    //   257: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   260: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   263: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   266: aload_0
    //   267: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   270: if_acmpne -> 290
    //   273: aload_0
    //   274: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   277: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   280: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   283: aload_0
    //   284: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   287: if_acmpeq -> 297
    //   290: aload #4
    //   292: aload_0
    //   293: aload_1
    //   294: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   297: aload_0
    //   298: invokevirtual getHorizontalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   301: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   304: if_acmpne -> 313
    //   307: iconst_1
    //   308: istore #5
    //   310: goto -> 316
    //   313: iconst_0
    //   314: istore #5
    //   316: aload_0
    //   317: invokevirtual getVerticalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   320: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   323: if_acmpne -> 332
    //   326: iconst_1
    //   327: istore #6
    //   329: goto -> 335
    //   332: iconst_0
    //   333: istore #6
    //   335: iload #5
    //   337: iload #6
    //   339: ixor
    //   340: ifeq -> 360
    //   343: aload_0
    //   344: getfield mDimensionRatio : F
    //   347: fconst_0
    //   348: fcmpl
    //   349: ifeq -> 360
    //   352: aload_0
    //   353: invokestatic resolveDimensionRatio : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)I
    //   356: pop
    //   357: goto -> 393
    //   360: aload_0
    //   361: invokevirtual getHorizontalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   364: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   367: if_acmpeq -> 380
    //   370: aload_0
    //   371: invokevirtual getVerticalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   374: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   377: if_acmpne -> 393
    //   380: aload #4
    //   382: aload_0
    //   383: aload_1
    //   384: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   387: iload_3
    //   388: ifeq -> 393
    //   391: iconst_0
    //   392: ireturn
    //   393: aload_0
    //   394: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   397: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   400: ifnonnull -> 413
    //   403: aload_0
    //   404: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   407: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   410: ifnull -> 541
    //   413: aload_0
    //   414: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   417: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   420: ifnull -> 450
    //   423: aload_0
    //   424: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   427: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   430: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   433: aload_0
    //   434: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   437: if_acmpne -> 450
    //   440: aload_0
    //   441: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   444: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   447: ifnull -> 541
    //   450: aload_0
    //   451: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   454: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   457: ifnull -> 487
    //   460: aload_0
    //   461: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   464: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   467: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   470: aload_0
    //   471: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   474: if_acmpne -> 487
    //   477: aload_0
    //   478: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   481: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   484: ifnull -> 541
    //   487: aload_0
    //   488: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   491: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   494: ifnull -> 576
    //   497: aload_0
    //   498: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   501: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   504: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   507: aload_0
    //   508: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   511: if_acmpne -> 576
    //   514: aload_0
    //   515: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   518: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   521: ifnull -> 576
    //   524: aload_0
    //   525: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   528: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   531: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   534: aload_0
    //   535: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   538: if_acmpne -> 576
    //   541: aload_0
    //   542: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   545: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   548: ifnonnull -> 576
    //   551: aload_0
    //   552: instanceof android/support/constraint/solver/widgets/Guideline
    //   555: ifne -> 576
    //   558: aload_0
    //   559: instanceof android/support/constraint/solver/widgets/Helper
    //   562: ifne -> 576
    //   565: aload_1
    //   566: getfield mStartHorizontalWidgets : Ljava/util/List;
    //   569: aload_0
    //   570: invokeinterface add : (Ljava/lang/Object;)Z
    //   575: pop
    //   576: aload_0
    //   577: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   580: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   583: ifnonnull -> 596
    //   586: aload_0
    //   587: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   590: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   593: ifnull -> 724
    //   596: aload_0
    //   597: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   600: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   603: ifnull -> 633
    //   606: aload_0
    //   607: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   610: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   613: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   616: aload_0
    //   617: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   620: if_acmpne -> 633
    //   623: aload_0
    //   624: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   627: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   630: ifnull -> 724
    //   633: aload_0
    //   634: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   637: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   640: ifnull -> 670
    //   643: aload_0
    //   644: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   647: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   650: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   653: aload_0
    //   654: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   657: if_acmpne -> 670
    //   660: aload_0
    //   661: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   664: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   667: ifnull -> 724
    //   670: aload_0
    //   671: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   674: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   677: ifnull -> 769
    //   680: aload_0
    //   681: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   684: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   687: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   690: aload_0
    //   691: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   694: if_acmpne -> 769
    //   697: aload_0
    //   698: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   701: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   704: ifnull -> 769
    //   707: aload_0
    //   708: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   711: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   714: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   717: aload_0
    //   718: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   721: if_acmpne -> 769
    //   724: aload_0
    //   725: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   728: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   731: ifnonnull -> 769
    //   734: aload_0
    //   735: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   738: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   741: ifnonnull -> 769
    //   744: aload_0
    //   745: instanceof android/support/constraint/solver/widgets/Guideline
    //   748: ifne -> 769
    //   751: aload_0
    //   752: instanceof android/support/constraint/solver/widgets/Helper
    //   755: ifne -> 769
    //   758: aload_1
    //   759: getfield mStartVerticalWidgets : Ljava/util/List;
    //   762: aload_0
    //   763: invokeinterface add : (Ljava/lang/Object;)Z
    //   768: pop
    //   769: aload_0
    //   770: instanceof android/support/constraint/solver/widgets/Helper
    //   773: ifeq -> 833
    //   776: aload #4
    //   778: aload_0
    //   779: aload_1
    //   780: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   783: iload_3
    //   784: ifeq -> 789
    //   787: iconst_0
    //   788: ireturn
    //   789: aload_0
    //   790: checkcast android/support/constraint/solver/widgets/Helper
    //   793: astore #7
    //   795: iconst_0
    //   796: istore #5
    //   798: iload #5
    //   800: aload #7
    //   802: getfield mWidgetsCount : I
    //   805: if_icmpge -> 833
    //   808: aload #7
    //   810: getfield mWidgets : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   813: iload #5
    //   815: aaload
    //   816: aload_1
    //   817: aload_2
    //   818: iload_3
    //   819: invokestatic traverse : (Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;Ljava/util/List;Z)Z
    //   822: ifne -> 827
    //   825: iconst_0
    //   826: ireturn
    //   827: iinc #5, 1
    //   830: goto -> 798
    //   833: aload_0
    //   834: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   837: arraylength
    //   838: istore #6
    //   840: iconst_0
    //   841: istore #5
    //   843: iload #5
    //   845: iload #6
    //   847: if_icmpge -> 936
    //   850: aload_0
    //   851: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   854: iload #5
    //   856: aaload
    //   857: astore #7
    //   859: aload #7
    //   861: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   864: ifnull -> 930
    //   867: aload #7
    //   869: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   872: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   875: aload_0
    //   876: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   879: if_acmpeq -> 930
    //   882: aload #7
    //   884: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   887: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   890: if_acmpne -> 906
    //   893: aload #4
    //   895: aload_0
    //   896: aload_1
    //   897: invokestatic invalidate : (Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;)V
    //   900: iload_3
    //   901: ifeq -> 911
    //   904: iconst_0
    //   905: ireturn
    //   906: aload #7
    //   908: invokestatic setConnection : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;)V
    //   911: aload #7
    //   913: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   916: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   919: aload_1
    //   920: aload_2
    //   921: iload_3
    //   922: invokestatic traverse : (Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;Ljava/util/List;Z)Z
    //   925: ifne -> 930
    //   928: iconst_0
    //   929: ireturn
    //   930: iinc #5, 1
    //   933: goto -> 843
    //   936: iconst_1
    //   937: ireturn
    //   938: aload_0
    //   939: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   942: aload_1
    //   943: if_acmpeq -> 1061
    //   946: aload_1
    //   947: getfield mConstrainedGroup : Ljava/util/List;
    //   950: aload_0
    //   951: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   954: getfield mConstrainedGroup : Ljava/util/List;
    //   957: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   962: pop
    //   963: aload_1
    //   964: getfield mStartHorizontalWidgets : Ljava/util/List;
    //   967: aload_0
    //   968: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   971: getfield mStartHorizontalWidgets : Ljava/util/List;
    //   974: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   979: pop
    //   980: aload_1
    //   981: getfield mStartVerticalWidgets : Ljava/util/List;
    //   984: aload_0
    //   985: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   988: getfield mStartVerticalWidgets : Ljava/util/List;
    //   991: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   996: pop
    //   997: aload_0
    //   998: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   1001: getfield mSkipSolver : Z
    //   1004: ifne -> 1012
    //   1007: aload_1
    //   1008: iconst_0
    //   1009: putfield mSkipSolver : Z
    //   1012: aload_2
    //   1013: aload_0
    //   1014: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   1017: invokeinterface remove : (Ljava/lang/Object;)Z
    //   1022: pop
    //   1023: aload_0
    //   1024: getfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   1027: getfield mConstrainedGroup : Ljava/util/List;
    //   1030: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1035: astore_0
    //   1036: aload_0
    //   1037: invokeinterface hasNext : ()Z
    //   1042: ifeq -> 1061
    //   1045: aload_0
    //   1046: invokeinterface next : ()Ljava/lang/Object;
    //   1051: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   1054: aload_1
    //   1055: putfield mBelongingGroup : Landroid/support/constraint/solver/widgets/ConstraintWidgetGroup;
    //   1058: goto -> 1036
    //   1061: iconst_1
    //   1062: ireturn
  }
  
  private static void updateSizeDependentWidgets(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int i = paramInt1 * 2;
    ConstraintAnchor constraintAnchor1 = paramConstraintWidget.mListAnchors[i];
    ConstraintAnchor constraintAnchor2 = paramConstraintWidget.mListAnchors[i + 1];
    if (constraintAnchor1.mTarget != null && constraintAnchor2.mTarget != null) {
      j = 1;
    } else {
      j = 0;
    } 
    if (j) {
      Optimizer.setOptimizedWidget(paramConstraintWidget, paramInt1, getParentBiasOffset(paramConstraintWidget, paramInt1) + constraintAnchor1.getMargin());
      return;
    } 
    if (paramConstraintWidget.mDimensionRatio != 0.0F && paramConstraintWidget.getDimensionBehaviour(paramInt1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      paramInt2 = resolveDimensionRatio(paramConstraintWidget);
      j = (int)(paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedOffset;
      (constraintAnchor2.getResolutionNode()).resolvedTarget = constraintAnchor1.getResolutionNode();
      (constraintAnchor2.getResolutionNode()).resolvedOffset = paramInt2;
      (constraintAnchor2.getResolutionNode()).state = 1;
      paramConstraintWidget.setFrame(j, j + paramInt2, paramInt1);
      return;
    } 
    int j = paramInt2 - paramConstraintWidget.getRelativePositioning(paramInt1);
    paramInt2 = j - paramConstraintWidget.getLength(paramInt1);
    paramConstraintWidget.setFrame(paramInt2, j, paramInt1);
    Optimizer.setOptimizedWidget(paramConstraintWidget, paramInt1, paramInt2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/Analyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */