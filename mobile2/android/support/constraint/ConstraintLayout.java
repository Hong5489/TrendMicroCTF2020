package android.support.constraint;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.Analyzer;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Guideline;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
  static final boolean ALLOWS_EMBEDDED = false;
  
  private static final boolean CACHE_MEASURED_DIMENSION = false;
  
  private static final boolean DEBUG = false;
  
  public static final int DESIGN_INFO_ID = 0;
  
  private static final String TAG = "ConstraintLayout";
  
  private static final boolean USE_CONSTRAINTS_HELPER = true;
  
  public static final String VERSION = "ConstraintLayout-1.1.3";
  
  SparseArray<View> mChildrenByIds = new SparseArray();
  
  private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<>(4);
  
  private ConstraintSet mConstraintSet = null;
  
  private int mConstraintSetId = -1;
  
  private HashMap<String, Integer> mDesignIds = new HashMap<>();
  
  private boolean mDirtyHierarchy = true;
  
  private int mLastMeasureHeight = -1;
  
  int mLastMeasureHeightMode = 0;
  
  int mLastMeasureHeightSize = -1;
  
  private int mLastMeasureWidth = -1;
  
  int mLastMeasureWidthMode = 0;
  
  int mLastMeasureWidthSize = -1;
  
  ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
  
  private int mMaxHeight = Integer.MAX_VALUE;
  
  private int mMaxWidth = Integer.MAX_VALUE;
  
  private Metrics mMetrics;
  
  private int mMinHeight = 0;
  
  private int mMinWidth = 0;
  
  private int mOptimizationLevel = 7;
  
  private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>(100);
  
  public ConstraintLayout(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private final ConstraintWidget getTargetWidget(int paramInt) {
    ConstraintWidget constraintWidget;
    if (paramInt == 0)
      return (ConstraintWidget)this.mLayoutWidget; 
    View view1 = (View)this.mChildrenByIds.get(paramInt);
    View view2 = view1;
    if (view1 == null) {
      view1 = findViewById(paramInt);
      view2 = view1;
      if (view1 != null) {
        view2 = view1;
        if (view1 != this) {
          view2 = view1;
          if (view1.getParent() == this) {
            onViewAdded(view1);
            view2 = view1;
          } 
        } 
      } 
    } 
    if (view2 == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (view2 == null) {
      view2 = null;
    } else {
      constraintWidget = ((LayoutParams)view2.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  private void init(AttributeSet paramAttributeSet) {
    this.mLayoutWidget.setCompanionWidget(this);
    this.mChildrenByIds.put(getId(), this);
    this.mConstraintSet = null;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_android_minWidth) {
          this.mMinWidth = typedArray.getDimensionPixelOffset(j, this.mMinWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_minHeight) {
          this.mMinHeight = typedArray.getDimensionPixelOffset(j, this.mMinHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
          this.mMaxWidth = typedArray.getDimensionPixelOffset(j, this.mMaxWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
          this.mMaxHeight = typedArray.getDimensionPixelOffset(j, this.mMaxHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
          this.mOptimizationLevel = typedArray.getInt(j, this.mOptimizationLevel);
        } else if (j == R.styleable.ConstraintLayout_Layout_constraintSet) {
          j = typedArray.getResourceId(j, 0);
          try {
            ConstraintSet constraintSet = new ConstraintSet();
            this();
            this.mConstraintSet = constraintSet;
            constraintSet.load(getContext(), j);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            this.mConstraintSet = null;
          } 
          this.mConstraintSetId = j;
        } 
      } 
      typedArray.recycle();
    } 
    this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
  }
  
  private void internalMeasureChildren(int paramInt1, int paramInt2) {
    int i = getPaddingTop() + getPaddingBottom();
    int j = getPaddingLeft() + getPaddingRight();
    int k = getChildCount();
    byte b = 0;
    while (true) {
      int m = paramInt1;
      ConstraintLayout constraintLayout = this;
      if (b < k) {
        View view = constraintLayout.getChildAt(b);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          ConstraintWidget constraintWidget = layoutParams.widget;
          if (!layoutParams.isGuideline && !layoutParams.isHelper) {
            int i2;
            constraintWidget.setVisibility(view.getVisibility());
            int n = layoutParams.width;
            int i1 = layoutParams.height;
            if (layoutParams.horizontalDimensionFixed || layoutParams.verticalDimensionFixed || (!layoutParams.horizontalDimensionFixed && layoutParams.matchConstraintDefaultWidth == 1) || layoutParams.width == -1 || (!layoutParams.verticalDimensionFixed && (layoutParams.matchConstraintDefaultHeight == 1 || layoutParams.height == -1))) {
              i2 = 1;
            } else {
              i2 = 0;
            } 
            boolean bool1 = false;
            int i3 = 0;
            boolean bool2 = false;
            byte b1 = 0;
            int i4 = 0;
            byte b2 = 0;
            int i5 = n;
            int i6 = i1;
            if (i2) {
              boolean bool;
              if (n == 0) {
                i4 = getChildMeasureSpec(m, j, -2);
                i6 = 1;
              } else if (n == -1) {
                i4 = getChildMeasureSpec(m, j, -1);
                i6 = bool1;
              } else {
                i6 = bool2;
                if (n == -2)
                  i6 = 1; 
                i4 = getChildMeasureSpec(m, j, n);
              } 
              if (i1 == 0) {
                i2 = getChildMeasureSpec(paramInt2, i, -2);
                i3 = 1;
              } else if (i1 == -1) {
                i2 = getChildMeasureSpec(paramInt2, i, -1);
                i3 = b1;
              } else {
                i3 = b2;
                if (i1 == -2)
                  i3 = 1; 
                i2 = getChildMeasureSpec(paramInt2, i, i1);
              } 
              view.measure(i4, i2);
              Metrics metrics = constraintLayout.mMetrics;
              if (metrics != null)
                metrics.measures++; 
              if (n == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              constraintWidget.setWidthWrapContent(bool);
              if (i1 == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              constraintWidget.setHeightWrapContent(bool);
              i5 = view.getMeasuredWidth();
              i2 = view.getMeasuredHeight();
              i4 = i3;
              i3 = i6;
              i6 = i2;
            } 
            constraintWidget.setWidth(i5);
            constraintWidget.setHeight(i6);
            if (i3 != 0)
              constraintWidget.setWrapWidth(i5); 
            if (i4 != 0)
              constraintWidget.setWrapHeight(i6); 
            if (layoutParams.needsBaseline) {
              i6 = view.getBaseline();
              if (i6 != -1)
                constraintWidget.setBaselineDistance(i6); 
            } 
          } 
        } 
        b++;
        continue;
      } 
      break;
    } 
  }
  
  private void internalMeasureDimensions(int paramInt1, int paramInt2) {
    ConstraintLayout constraintLayout = this;
    int i = getPaddingTop() + getPaddingBottom();
    int j = getPaddingLeft() + getPaddingRight();
    int k = getChildCount();
    int m;
    for (m = 0; m < k; m++) {
      View view = constraintLayout.getChildAt(m);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        ConstraintWidget constraintWidget = layoutParams.widget;
        if (!layoutParams.isGuideline && !layoutParams.isHelper) {
          constraintWidget.setVisibility(view.getVisibility());
          int n = layoutParams.width;
          int i1 = layoutParams.height;
          if (n == 0 || i1 == 0) {
            constraintWidget.getResolutionWidth().invalidate();
            constraintWidget.getResolutionHeight().invalidate();
          } else {
            boolean bool2;
            int i2 = 0;
            boolean bool1 = false;
            if (n == -2)
              i2 = 1; 
            int i3 = getChildMeasureSpec(paramInt1, j, n);
            if (i1 == -2)
              bool1 = true; 
            view.measure(i3, getChildMeasureSpec(paramInt2, i, i1));
            Metrics metrics = constraintLayout.mMetrics;
            if (metrics != null)
              metrics.measures++; 
            if (n == -2) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            constraintWidget.setWidthWrapContent(bool2);
            if (i1 == -2) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            constraintWidget.setHeightWrapContent(bool2);
            i1 = view.getMeasuredWidth();
            n = view.getMeasuredHeight();
            constraintWidget.setWidth(i1);
            constraintWidget.setHeight(n);
            if (i2)
              constraintWidget.setWrapWidth(i1); 
            if (bool1)
              constraintWidget.setWrapHeight(n); 
            if (layoutParams.needsBaseline) {
              i2 = view.getBaseline();
              if (i2 != -1)
                constraintWidget.setBaselineDistance(i2); 
            } 
            if (layoutParams.horizontalDimensionFixed && layoutParams.verticalDimensionFixed) {
              constraintWidget.getResolutionWidth().resolve(i1);
              constraintWidget.getResolutionHeight().resolve(n);
            } 
          } 
        } 
      } 
    } 
    constraintLayout.mLayoutWidget.solveGraph();
    byte b = 0;
    while (true) {
      int n = paramInt1;
      if (b < k) {
        ConstraintLayout constraintLayout1;
        View view = constraintLayout.getChildAt(b);
        if (view.getVisibility() == 8) {
          constraintLayout1 = constraintLayout;
        } else {
          LayoutParams layoutParams = (LayoutParams)constraintLayout1.getLayoutParams();
          ConstraintWidget constraintWidget = layoutParams.widget;
          if (!layoutParams.isGuideline) {
            if (layoutParams.isHelper) {
              constraintLayout1 = constraintLayout;
            } else {
              constraintWidget.setVisibility(constraintLayout1.getVisibility());
              int i1 = layoutParams.width;
              int i2 = layoutParams.height;
              if (i1 != 0 && i2 != 0) {
                constraintLayout1 = constraintLayout;
              } else {
                int i3;
                ResolutionAnchor resolutionAnchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
                ResolutionAnchor resolutionAnchor3 = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getResolutionNode();
                if (constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).getTarget() != null && constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget() != null) {
                  m = 1;
                } else {
                  m = 0;
                } 
                ResolutionAnchor resolutionAnchor4 = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
                ResolutionAnchor resolutionAnchor1 = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getResolutionNode();
                if (constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).getTarget() != null && constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget() != null) {
                  i3 = 1;
                } else {
                  i3 = 0;
                } 
                if (i1 == 0 && i2 == 0 && m != 0 && i3) {
                  constraintLayout1 = constraintLayout;
                } else {
                  boolean bool1;
                  boolean bool3;
                  boolean bool2 = false;
                  boolean bool4 = false;
                  boolean bool5 = false;
                  boolean bool6 = false;
                  if (constraintLayout.mLayoutWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    bool1 = true;
                  } else {
                    bool1 = false;
                  } 
                  if (constraintLayout.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    i4 = 1;
                  } else {
                    i4 = 0;
                  } 
                  if (!bool1)
                    constraintWidget.getResolutionWidth().invalidate(); 
                  if (!i4)
                    constraintWidget.getResolutionHeight().invalidate(); 
                  if (i1 == 0) {
                    if (bool1 && constraintWidget.isSpreadWidth() && m != 0 && resolutionAnchor2.isResolved() && resolutionAnchor3.isResolved()) {
                      i1 = (int)(resolutionAnchor3.getResolvedValue() - resolutionAnchor2.getResolvedValue());
                      constraintWidget.getResolutionWidth().resolve(i1);
                      m = getChildMeasureSpec(n, j, i1);
                      n = bool1;
                    } else {
                      m = getChildMeasureSpec(n, j, -2);
                      bool2 = true;
                      n = 0;
                    } 
                  } else if (i1 == -1) {
                    m = getChildMeasureSpec(n, j, -1);
                    n = bool1;
                  } else {
                    bool2 = bool4;
                    if (i1 == -2)
                      bool2 = true; 
                    m = getChildMeasureSpec(n, j, i1);
                    n = bool1;
                  } 
                  if (i2 == 0) {
                    if (i4 && constraintWidget.isSpreadHeight() && i3 && resolutionAnchor4.isResolved() && resolutionAnchor1.isResolved()) {
                      float f1 = resolutionAnchor1.getResolvedValue();
                      float f2 = resolutionAnchor4.getResolvedValue();
                      i3 = i4;
                      i2 = (int)(f1 - f2);
                      constraintWidget.getResolutionHeight().resolve(i2);
                      i4 = getChildMeasureSpec(paramInt2, i, i2);
                      bool1 = bool5;
                    } else {
                      i4 = getChildMeasureSpec(paramInt2, i, -2);
                      bool1 = true;
                      i3 = 0;
                    } 
                  } else {
                    i3 = i4;
                    if (i2 == -1) {
                      i4 = getChildMeasureSpec(paramInt2, i, -1);
                      bool1 = bool5;
                    } else {
                      bool1 = bool6;
                      if (i2 == -2)
                        bool1 = true; 
                      i4 = getChildMeasureSpec(paramInt2, i, i2);
                    } 
                  } 
                  constraintLayout1.measure(m, i4);
                  constraintLayout = this;
                  Metrics metrics = constraintLayout.mMetrics;
                  if (metrics != null)
                    metrics.measures++; 
                  if (i1 == -2) {
                    bool3 = true;
                  } else {
                    bool3 = false;
                  } 
                  constraintWidget.setWidthWrapContent(bool3);
                  if (i2 == -2) {
                    bool3 = true;
                  } else {
                    bool3 = false;
                  } 
                  constraintWidget.setHeightWrapContent(bool3);
                  m = constraintLayout1.getMeasuredWidth();
                  int i4 = constraintLayout1.getMeasuredHeight();
                  constraintWidget.setWidth(m);
                  constraintWidget.setHeight(i4);
                  if (bool2)
                    constraintWidget.setWrapWidth(m); 
                  if (bool1)
                    constraintWidget.setWrapHeight(i4); 
                  if (n != 0) {
                    constraintWidget.getResolutionWidth().resolve(m);
                  } else {
                    constraintWidget.getResolutionWidth().remove();
                  } 
                  if (i3 != 0) {
                    constraintWidget.getResolutionHeight().resolve(i4);
                  } else {
                    constraintWidget.getResolutionHeight().remove();
                  } 
                  if (layoutParams.needsBaseline) {
                    m = constraintLayout1.getBaseline();
                    constraintLayout1 = constraintLayout;
                    if (m != -1) {
                      constraintWidget.setBaselineDistance(m);
                      constraintLayout1 = constraintLayout;
                    } 
                  } else {
                    constraintLayout1 = constraintLayout;
                  } 
                } 
              } 
            } 
          } else {
            constraintLayout1 = constraintLayout;
          } 
        } 
        b++;
        constraintLayout = constraintLayout1;
        continue;
      } 
      break;
    } 
  }
  
  private void setChildrenConstraints() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isInEditMode : ()Z
    //   4: istore_1
    //   5: aload_0
    //   6: invokevirtual getChildCount : ()I
    //   9: istore_2
    //   10: iconst_0
    //   11: istore_3
    //   12: iconst_m1
    //   13: istore #4
    //   15: iload_1
    //   16: ifeq -> 120
    //   19: iconst_0
    //   20: istore #5
    //   22: iload #5
    //   24: iload_2
    //   25: if_icmpge -> 120
    //   28: aload_0
    //   29: iload #5
    //   31: invokevirtual getChildAt : (I)Landroid/view/View;
    //   34: astore #6
    //   36: aload_0
    //   37: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   40: aload #6
    //   42: invokevirtual getId : ()I
    //   45: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   48: astore #7
    //   50: aload_0
    //   51: iconst_0
    //   52: aload #7
    //   54: aload #6
    //   56: invokevirtual getId : ()I
    //   59: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   62: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   65: aload #7
    //   67: bipush #47
    //   69: invokevirtual indexOf : (I)I
    //   72: istore #8
    //   74: aload #7
    //   76: astore #9
    //   78: iload #8
    //   80: iconst_m1
    //   81: if_icmpeq -> 95
    //   84: aload #7
    //   86: iload #8
    //   88: iconst_1
    //   89: iadd
    //   90: invokevirtual substring : (I)Ljava/lang/String;
    //   93: astore #9
    //   95: aload_0
    //   96: aload #6
    //   98: invokevirtual getId : ()I
    //   101: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   104: aload #9
    //   106: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   109: goto -> 114
    //   112: astore #9
    //   114: iinc #5, 1
    //   117: goto -> 22
    //   120: iconst_0
    //   121: istore #5
    //   123: iload #5
    //   125: iload_2
    //   126: if_icmpge -> 160
    //   129: aload_0
    //   130: aload_0
    //   131: iload #5
    //   133: invokevirtual getChildAt : (I)Landroid/view/View;
    //   136: invokevirtual getViewWidget : (Landroid/view/View;)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   139: astore #9
    //   141: aload #9
    //   143: ifnonnull -> 149
    //   146: goto -> 154
    //   149: aload #9
    //   151: invokevirtual reset : ()V
    //   154: iinc #5, 1
    //   157: goto -> 123
    //   160: aload_0
    //   161: getfield mConstraintSetId : I
    //   164: iconst_m1
    //   165: if_icmpeq -> 223
    //   168: iconst_0
    //   169: istore #5
    //   171: iload #5
    //   173: iload_2
    //   174: if_icmpge -> 223
    //   177: aload_0
    //   178: iload #5
    //   180: invokevirtual getChildAt : (I)Landroid/view/View;
    //   183: astore #9
    //   185: aload #9
    //   187: invokevirtual getId : ()I
    //   190: aload_0
    //   191: getfield mConstraintSetId : I
    //   194: if_icmpne -> 217
    //   197: aload #9
    //   199: instanceof android/support/constraint/Constraints
    //   202: ifeq -> 217
    //   205: aload_0
    //   206: aload #9
    //   208: checkcast android/support/constraint/Constraints
    //   211: invokevirtual getConstraintSet : ()Landroid/support/constraint/ConstraintSet;
    //   214: putfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   217: iinc #5, 1
    //   220: goto -> 171
    //   223: aload_0
    //   224: getfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   227: astore #9
    //   229: aload #9
    //   231: ifnull -> 240
    //   234: aload #9
    //   236: aload_0
    //   237: invokevirtual applyToInternal : (Landroid/support/constraint/ConstraintLayout;)V
    //   240: aload_0
    //   241: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   244: invokevirtual removeAllChildren : ()V
    //   247: aload_0
    //   248: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   251: invokevirtual size : ()I
    //   254: istore #10
    //   256: iload #10
    //   258: ifle -> 293
    //   261: iconst_0
    //   262: istore #5
    //   264: iload #5
    //   266: iload #10
    //   268: if_icmpge -> 293
    //   271: aload_0
    //   272: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   275: iload #5
    //   277: invokevirtual get : (I)Ljava/lang/Object;
    //   280: checkcast android/support/constraint/ConstraintHelper
    //   283: aload_0
    //   284: invokevirtual updatePreLayout : (Landroid/support/constraint/ConstraintLayout;)V
    //   287: iinc #5, 1
    //   290: goto -> 264
    //   293: iconst_0
    //   294: istore #5
    //   296: iload #5
    //   298: iload_2
    //   299: if_icmpge -> 333
    //   302: aload_0
    //   303: iload #5
    //   305: invokevirtual getChildAt : (I)Landroid/view/View;
    //   308: astore #9
    //   310: aload #9
    //   312: instanceof android/support/constraint/Placeholder
    //   315: ifeq -> 327
    //   318: aload #9
    //   320: checkcast android/support/constraint/Placeholder
    //   323: aload_0
    //   324: invokevirtual updatePreLayout : (Landroid/support/constraint/ConstraintLayout;)V
    //   327: iinc #5, 1
    //   330: goto -> 296
    //   333: iconst_0
    //   334: istore #11
    //   336: iload #4
    //   338: istore #5
    //   340: iload #11
    //   342: iload_2
    //   343: if_icmpge -> 2107
    //   346: aload_0
    //   347: iload #11
    //   349: invokevirtual getChildAt : (I)Landroid/view/View;
    //   352: astore #7
    //   354: aload_0
    //   355: aload #7
    //   357: invokevirtual getViewWidget : (Landroid/view/View;)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   360: astore #6
    //   362: aload #6
    //   364: ifnonnull -> 370
    //   367: goto -> 2101
    //   370: aload #7
    //   372: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   375: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   378: astore #9
    //   380: aload #9
    //   382: invokevirtual validate : ()V
    //   385: aload #9
    //   387: getfield helped : Z
    //   390: ifeq -> 402
    //   393: aload #9
    //   395: iload_3
    //   396: putfield helped : Z
    //   399: goto -> 471
    //   402: iload_1
    //   403: ifeq -> 471
    //   406: aload_0
    //   407: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   410: aload #7
    //   412: invokevirtual getId : ()I
    //   415: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   418: astore #12
    //   420: aload_0
    //   421: iload_3
    //   422: aload #12
    //   424: aload #7
    //   426: invokevirtual getId : ()I
    //   429: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   432: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   435: aload #12
    //   437: aload #12
    //   439: ldc_w 'id/'
    //   442: invokevirtual indexOf : (Ljava/lang/String;)I
    //   445: iconst_3
    //   446: iadd
    //   447: invokevirtual substring : (I)Ljava/lang/String;
    //   450: astore #12
    //   452: aload_0
    //   453: aload #7
    //   455: invokevirtual getId : ()I
    //   458: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   461: aload #12
    //   463: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   466: goto -> 471
    //   469: astore #12
    //   471: aload #6
    //   473: aload #7
    //   475: invokevirtual getVisibility : ()I
    //   478: invokevirtual setVisibility : (I)V
    //   481: aload #9
    //   483: getfield isInPlaceholder : Z
    //   486: ifeq -> 496
    //   489: aload #6
    //   491: bipush #8
    //   493: invokevirtual setVisibility : (I)V
    //   496: aload #6
    //   498: aload #7
    //   500: invokevirtual setCompanionWidget : (Ljava/lang/Object;)V
    //   503: aload_0
    //   504: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   507: aload #6
    //   509: invokevirtual add : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   512: aload #9
    //   514: getfield verticalDimensionFixed : Z
    //   517: ifeq -> 528
    //   520: aload #9
    //   522: getfield horizontalDimensionFixed : Z
    //   525: ifne -> 538
    //   528: aload_0
    //   529: getfield mVariableDimensionsWidgets : Ljava/util/ArrayList;
    //   532: aload #6
    //   534: invokevirtual add : (Ljava/lang/Object;)Z
    //   537: pop
    //   538: aload #9
    //   540: getfield isGuideline : Z
    //   543: ifeq -> 656
    //   546: aload #6
    //   548: checkcast android/support/constraint/solver/widgets/Guideline
    //   551: astore #7
    //   553: aload #9
    //   555: getfield resolvedGuideBegin : I
    //   558: istore #4
    //   560: aload #9
    //   562: getfield resolvedGuideEnd : I
    //   565: istore #8
    //   567: aload #9
    //   569: getfield resolvedGuidePercent : F
    //   572: fstore #13
    //   574: getstatic android/os/Build$VERSION.SDK_INT : I
    //   577: bipush #17
    //   579: if_icmpge -> 603
    //   582: aload #9
    //   584: getfield guideBegin : I
    //   587: istore #4
    //   589: aload #9
    //   591: getfield guideEnd : I
    //   594: istore #8
    //   596: aload #9
    //   598: getfield guidePercent : F
    //   601: fstore #13
    //   603: fload #13
    //   605: ldc_w -1.0
    //   608: fcmpl
    //   609: ifeq -> 622
    //   612: aload #7
    //   614: fload #13
    //   616: invokevirtual setGuidePercent : (F)V
    //   619: goto -> 653
    //   622: iload #4
    //   624: iload #5
    //   626: if_icmpeq -> 639
    //   629: aload #7
    //   631: iload #4
    //   633: invokevirtual setGuideBegin : (I)V
    //   636: goto -> 653
    //   639: iload #8
    //   641: iload #5
    //   643: if_icmpeq -> 653
    //   646: aload #7
    //   648: iload #8
    //   650: invokevirtual setGuideEnd : (I)V
    //   653: goto -> 2101
    //   656: aload #9
    //   658: getfield leftToLeft : I
    //   661: iload #5
    //   663: if_icmpne -> 836
    //   666: aload #9
    //   668: getfield leftToRight : I
    //   671: iload #5
    //   673: if_icmpne -> 836
    //   676: aload #9
    //   678: getfield rightToLeft : I
    //   681: iload #5
    //   683: if_icmpne -> 836
    //   686: aload #9
    //   688: getfield rightToRight : I
    //   691: iload #5
    //   693: if_icmpne -> 836
    //   696: aload #9
    //   698: getfield startToStart : I
    //   701: iload #5
    //   703: if_icmpne -> 836
    //   706: aload #9
    //   708: getfield startToEnd : I
    //   711: iload #5
    //   713: if_icmpne -> 836
    //   716: aload #9
    //   718: getfield endToStart : I
    //   721: iload #5
    //   723: if_icmpne -> 836
    //   726: aload #9
    //   728: getfield endToEnd : I
    //   731: iload #5
    //   733: if_icmpne -> 836
    //   736: aload #9
    //   738: getfield topToTop : I
    //   741: iload #5
    //   743: if_icmpne -> 836
    //   746: aload #9
    //   748: getfield topToBottom : I
    //   751: iload #5
    //   753: if_icmpne -> 836
    //   756: aload #9
    //   758: getfield bottomToTop : I
    //   761: iload #5
    //   763: if_icmpne -> 836
    //   766: aload #9
    //   768: getfield bottomToBottom : I
    //   771: iload #5
    //   773: if_icmpne -> 836
    //   776: aload #9
    //   778: getfield baselineToBaseline : I
    //   781: iload #5
    //   783: if_icmpne -> 836
    //   786: aload #9
    //   788: getfield editorAbsoluteX : I
    //   791: iload #5
    //   793: if_icmpne -> 836
    //   796: aload #9
    //   798: getfield editorAbsoluteY : I
    //   801: iload #5
    //   803: if_icmpne -> 836
    //   806: aload #9
    //   808: getfield circleConstraint : I
    //   811: iload #5
    //   813: if_icmpne -> 836
    //   816: aload #9
    //   818: getfield width : I
    //   821: iload #5
    //   823: if_icmpeq -> 836
    //   826: aload #9
    //   828: getfield height : I
    //   831: iload #5
    //   833: if_icmpne -> 653
    //   836: aload #9
    //   838: getfield resolvedLeftToLeft : I
    //   841: istore #4
    //   843: aload #9
    //   845: getfield resolvedLeftToRight : I
    //   848: istore #8
    //   850: aload #9
    //   852: getfield resolvedRightToLeft : I
    //   855: istore_3
    //   856: aload #9
    //   858: getfield resolvedRightToRight : I
    //   861: istore #5
    //   863: aload #9
    //   865: getfield resolveGoneLeftMargin : I
    //   868: istore #14
    //   870: aload #9
    //   872: getfield resolveGoneRightMargin : I
    //   875: istore #15
    //   877: aload #9
    //   879: getfield resolvedHorizontalBias : F
    //   882: fstore #13
    //   884: getstatic android/os/Build$VERSION.SDK_INT : I
    //   887: bipush #17
    //   889: if_icmpge -> 1152
    //   892: aload #9
    //   894: getfield leftToLeft : I
    //   897: istore #5
    //   899: aload #9
    //   901: getfield leftToRight : I
    //   904: istore_3
    //   905: aload #9
    //   907: getfield rightToLeft : I
    //   910: istore #15
    //   912: aload #9
    //   914: getfield rightToRight : I
    //   917: istore #14
    //   919: aload #9
    //   921: getfield goneLeftMargin : I
    //   924: istore #4
    //   926: aload #9
    //   928: getfield goneRightMargin : I
    //   931: istore #16
    //   933: aload #9
    //   935: getfield horizontalBias : F
    //   938: fstore #13
    //   940: iload #5
    //   942: iconst_m1
    //   943: if_icmpne -> 1002
    //   946: iload_3
    //   947: iconst_m1
    //   948: if_icmpne -> 1002
    //   951: aload #9
    //   953: getfield startToStart : I
    //   956: iconst_m1
    //   957: if_icmpeq -> 976
    //   960: aload #9
    //   962: getfield startToStart : I
    //   965: istore #8
    //   967: iload_3
    //   968: istore #5
    //   970: iload #8
    //   972: istore_3
    //   973: goto -> 1012
    //   976: aload #9
    //   978: getfield startToEnd : I
    //   981: iconst_m1
    //   982: if_icmpeq -> 1002
    //   985: aload #9
    //   987: getfield startToEnd : I
    //   990: istore #8
    //   992: iload #5
    //   994: istore_3
    //   995: iload #8
    //   997: istore #5
    //   999: goto -> 1012
    //   1002: iload_3
    //   1003: istore #8
    //   1005: iload #5
    //   1007: istore_3
    //   1008: iload #8
    //   1010: istore #5
    //   1012: iload #15
    //   1014: iconst_m1
    //   1015: if_icmpne -> 1119
    //   1018: iload #14
    //   1020: iconst_m1
    //   1021: if_icmpne -> 1119
    //   1024: iload_3
    //   1025: istore #8
    //   1027: aload #9
    //   1029: getfield endToStart : I
    //   1032: iconst_m1
    //   1033: if_icmpeq -> 1073
    //   1036: aload #9
    //   1038: getfield endToStart : I
    //   1041: istore_3
    //   1042: iload #8
    //   1044: istore #15
    //   1046: iload #4
    //   1048: istore #17
    //   1050: iload #5
    //   1052: istore #8
    //   1054: iload #14
    //   1056: istore #5
    //   1058: iload #15
    //   1060: istore #4
    //   1062: iload #16
    //   1064: istore #15
    //   1066: iload #17
    //   1068: istore #14
    //   1070: goto -> 1152
    //   1073: aload #9
    //   1075: getfield endToEnd : I
    //   1078: iconst_m1
    //   1079: if_icmpeq -> 1119
    //   1082: aload #9
    //   1084: getfield endToEnd : I
    //   1087: istore #18
    //   1089: iload #8
    //   1091: istore #17
    //   1093: iload #4
    //   1095: istore #14
    //   1097: iload #5
    //   1099: istore #8
    //   1101: iload #15
    //   1103: istore_3
    //   1104: iload #18
    //   1106: istore #5
    //   1108: iload #17
    //   1110: istore #4
    //   1112: iload #16
    //   1114: istore #15
    //   1116: goto -> 1152
    //   1119: iload #4
    //   1121: istore #17
    //   1123: iload #5
    //   1125: istore #8
    //   1127: iload #15
    //   1129: istore #18
    //   1131: iload #14
    //   1133: istore #5
    //   1135: iload_3
    //   1136: istore #4
    //   1138: iload #16
    //   1140: istore #15
    //   1142: iload #18
    //   1144: istore_3
    //   1145: iload #17
    //   1147: istore #14
    //   1149: goto -> 1152
    //   1152: aload #9
    //   1154: getfield circleConstraint : I
    //   1157: iconst_m1
    //   1158: if_icmpeq -> 1200
    //   1161: aload_0
    //   1162: aload #9
    //   1164: getfield circleConstraint : I
    //   1167: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1170: astore #7
    //   1172: aload #7
    //   1174: ifnull -> 1197
    //   1177: aload #6
    //   1179: aload #7
    //   1181: aload #9
    //   1183: getfield circleAngle : F
    //   1186: aload #9
    //   1188: getfield circleRadius : I
    //   1191: invokevirtual connectCircularConstraint : (Landroid/support/constraint/solver/widgets/ConstraintWidget;FI)V
    //   1194: goto -> 1197
    //   1197: goto -> 1761
    //   1200: iload #4
    //   1202: iconst_m1
    //   1203: if_icmpeq -> 1245
    //   1206: aload_0
    //   1207: iload #4
    //   1209: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1212: astore #7
    //   1214: aload #7
    //   1216: ifnull -> 1242
    //   1219: aload #6
    //   1221: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1224: aload #7
    //   1226: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1229: aload #9
    //   1231: getfield leftMargin : I
    //   1234: iload #14
    //   1236: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1239: goto -> 1287
    //   1242: goto -> 1287
    //   1245: iload #8
    //   1247: iconst_m1
    //   1248: if_icmpeq -> 1287
    //   1251: aload_0
    //   1252: iload #8
    //   1254: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1257: astore #7
    //   1259: aload #7
    //   1261: ifnull -> 1287
    //   1264: aload #6
    //   1266: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1269: aload #7
    //   1271: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1274: aload #9
    //   1276: getfield leftMargin : I
    //   1279: iload #14
    //   1281: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1284: goto -> 1287
    //   1287: aload #9
    //   1289: astore #7
    //   1291: iload_3
    //   1292: iconst_m1
    //   1293: if_icmpeq -> 1331
    //   1296: aload_0
    //   1297: iload_3
    //   1298: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1301: astore #12
    //   1303: aload #12
    //   1305: ifnull -> 1373
    //   1308: aload #6
    //   1310: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1313: aload #12
    //   1315: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1318: aload #7
    //   1320: getfield rightMargin : I
    //   1323: iload #15
    //   1325: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1328: goto -> 1373
    //   1331: iload #5
    //   1333: iconst_m1
    //   1334: if_icmpeq -> 1373
    //   1337: aload_0
    //   1338: iload #5
    //   1340: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1343: astore #12
    //   1345: aload #12
    //   1347: ifnull -> 1373
    //   1350: aload #6
    //   1352: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1355: aload #12
    //   1357: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1360: aload #7
    //   1362: getfield rightMargin : I
    //   1365: iload #15
    //   1367: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1370: goto -> 1373
    //   1373: aload #7
    //   1375: getfield topToTop : I
    //   1378: iconst_m1
    //   1379: if_icmpeq -> 1424
    //   1382: aload_0
    //   1383: aload #7
    //   1385: getfield topToTop : I
    //   1388: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1391: astore #12
    //   1393: aload #12
    //   1395: ifnull -> 1475
    //   1398: aload #6
    //   1400: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1403: aload #12
    //   1405: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1408: aload #7
    //   1410: getfield topMargin : I
    //   1413: aload #7
    //   1415: getfield goneTopMargin : I
    //   1418: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1421: goto -> 1475
    //   1424: aload #7
    //   1426: getfield topToBottom : I
    //   1429: iconst_m1
    //   1430: if_icmpeq -> 1475
    //   1433: aload_0
    //   1434: aload #7
    //   1436: getfield topToBottom : I
    //   1439: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1442: astore #12
    //   1444: aload #12
    //   1446: ifnull -> 1475
    //   1449: aload #6
    //   1451: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1454: aload #12
    //   1456: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1459: aload #7
    //   1461: getfield topMargin : I
    //   1464: aload #7
    //   1466: getfield goneTopMargin : I
    //   1469: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1472: goto -> 1475
    //   1475: aload #7
    //   1477: getfield bottomToTop : I
    //   1480: iconst_m1
    //   1481: if_icmpeq -> 1526
    //   1484: aload_0
    //   1485: aload #7
    //   1487: getfield bottomToTop : I
    //   1490: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1493: astore #12
    //   1495: aload #12
    //   1497: ifnull -> 1577
    //   1500: aload #6
    //   1502: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1505: aload #12
    //   1507: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1510: aload #7
    //   1512: getfield bottomMargin : I
    //   1515: aload #7
    //   1517: getfield goneBottomMargin : I
    //   1520: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1523: goto -> 1577
    //   1526: aload #7
    //   1528: getfield bottomToBottom : I
    //   1531: iconst_m1
    //   1532: if_icmpeq -> 1577
    //   1535: aload_0
    //   1536: aload #7
    //   1538: getfield bottomToBottom : I
    //   1541: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1544: astore #12
    //   1546: aload #12
    //   1548: ifnull -> 1577
    //   1551: aload #6
    //   1553: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1556: aload #12
    //   1558: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1561: aload #7
    //   1563: getfield bottomMargin : I
    //   1566: aload #7
    //   1568: getfield goneBottomMargin : I
    //   1571: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1574: goto -> 1577
    //   1577: aload #7
    //   1579: getfield baselineToBaseline : I
    //   1582: iconst_m1
    //   1583: if_icmpeq -> 1706
    //   1586: aload_0
    //   1587: getfield mChildrenByIds : Landroid/util/SparseArray;
    //   1590: aload #7
    //   1592: getfield baselineToBaseline : I
    //   1595: invokevirtual get : (I)Ljava/lang/Object;
    //   1598: checkcast android/view/View
    //   1601: astore #19
    //   1603: aload_0
    //   1604: aload #7
    //   1606: getfield baselineToBaseline : I
    //   1609: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1612: astore #12
    //   1614: aload #12
    //   1616: ifnull -> 1706
    //   1619: aload #19
    //   1621: ifnull -> 1706
    //   1624: aload #19
    //   1626: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1629: instanceof android/support/constraint/ConstraintLayout$LayoutParams
    //   1632: ifeq -> 1706
    //   1635: aload #19
    //   1637: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1640: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   1643: astore #19
    //   1645: aload #7
    //   1647: iconst_1
    //   1648: putfield needsBaseline : Z
    //   1651: aload #19
    //   1653: iconst_1
    //   1654: putfield needsBaseline : Z
    //   1657: aload #6
    //   1659: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1662: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1665: aload #12
    //   1667: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1670: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1673: iconst_0
    //   1674: iconst_m1
    //   1675: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Strength.STRONG : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;
    //   1678: iconst_0
    //   1679: iconst_1
    //   1680: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;IILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;IZ)Z
    //   1683: pop
    //   1684: aload #6
    //   1686: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1689: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1692: invokevirtual reset : ()V
    //   1695: aload #6
    //   1697: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1700: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1703: invokevirtual reset : ()V
    //   1706: fload #13
    //   1708: fconst_0
    //   1709: fcmpl
    //   1710: iflt -> 1729
    //   1713: fload #13
    //   1715: ldc_w 0.5
    //   1718: fcmpl
    //   1719: ifeq -> 1729
    //   1722: aload #6
    //   1724: fload #13
    //   1726: invokevirtual setHorizontalBiasPercent : (F)V
    //   1729: aload #7
    //   1731: getfield verticalBias : F
    //   1734: fconst_0
    //   1735: fcmpl
    //   1736: iflt -> 1761
    //   1739: aload #7
    //   1741: getfield verticalBias : F
    //   1744: ldc_w 0.5
    //   1747: fcmpl
    //   1748: ifeq -> 1761
    //   1751: aload #6
    //   1753: aload #7
    //   1755: getfield verticalBias : F
    //   1758: invokevirtual setVerticalBiasPercent : (F)V
    //   1761: iload_1
    //   1762: ifeq -> 1798
    //   1765: aload #9
    //   1767: getfield editorAbsoluteX : I
    //   1770: iconst_m1
    //   1771: if_icmpne -> 1783
    //   1774: aload #9
    //   1776: getfield editorAbsoluteY : I
    //   1779: iconst_m1
    //   1780: if_icmpeq -> 1798
    //   1783: aload #6
    //   1785: aload #9
    //   1787: getfield editorAbsoluteX : I
    //   1790: aload #9
    //   1792: getfield editorAbsoluteY : I
    //   1795: invokevirtual setOrigin : (II)V
    //   1798: aload #9
    //   1800: getfield horizontalDimensionFixed : Z
    //   1803: ifne -> 1875
    //   1806: aload #9
    //   1808: getfield width : I
    //   1811: iconst_m1
    //   1812: if_icmpne -> 1858
    //   1815: aload #6
    //   1817: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1820: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1823: aload #6
    //   1825: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1828: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1831: aload #9
    //   1833: getfield leftMargin : I
    //   1836: putfield mMargin : I
    //   1839: aload #6
    //   1841: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1844: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1847: aload #9
    //   1849: getfield rightMargin : I
    //   1852: putfield mMargin : I
    //   1855: goto -> 1893
    //   1858: aload #6
    //   1860: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1863: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1866: aload #6
    //   1868: iconst_0
    //   1869: invokevirtual setWidth : (I)V
    //   1872: goto -> 1893
    //   1875: aload #6
    //   1877: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1880: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1883: aload #6
    //   1885: aload #9
    //   1887: getfield width : I
    //   1890: invokevirtual setWidth : (I)V
    //   1893: aload #9
    //   1895: getfield verticalDimensionFixed : Z
    //   1898: ifne -> 1970
    //   1901: aload #9
    //   1903: getfield height : I
    //   1906: iconst_m1
    //   1907: if_icmpne -> 1953
    //   1910: aload #6
    //   1912: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1915: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1918: aload #6
    //   1920: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1923: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1926: aload #9
    //   1928: getfield topMargin : I
    //   1931: putfield mMargin : I
    //   1934: aload #6
    //   1936: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1939: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1942: aload #9
    //   1944: getfield bottomMargin : I
    //   1947: putfield mMargin : I
    //   1950: goto -> 1988
    //   1953: aload #6
    //   1955: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1958: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1961: aload #6
    //   1963: iconst_0
    //   1964: invokevirtual setHeight : (I)V
    //   1967: goto -> 1988
    //   1970: aload #6
    //   1972: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1975: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1978: aload #6
    //   1980: aload #9
    //   1982: getfield height : I
    //   1985: invokevirtual setHeight : (I)V
    //   1988: iconst_m1
    //   1989: istore #5
    //   1991: iconst_0
    //   1992: istore_3
    //   1993: aload #9
    //   1995: getfield dimensionRatio : Ljava/lang/String;
    //   1998: ifnull -> 2011
    //   2001: aload #6
    //   2003: aload #9
    //   2005: getfield dimensionRatio : Ljava/lang/String;
    //   2008: invokevirtual setDimensionRatio : (Ljava/lang/String;)V
    //   2011: aload #6
    //   2013: aload #9
    //   2015: getfield horizontalWeight : F
    //   2018: invokevirtual setHorizontalWeight : (F)V
    //   2021: aload #6
    //   2023: aload #9
    //   2025: getfield verticalWeight : F
    //   2028: invokevirtual setVerticalWeight : (F)V
    //   2031: aload #6
    //   2033: aload #9
    //   2035: getfield horizontalChainStyle : I
    //   2038: invokevirtual setHorizontalChainStyle : (I)V
    //   2041: aload #6
    //   2043: aload #9
    //   2045: getfield verticalChainStyle : I
    //   2048: invokevirtual setVerticalChainStyle : (I)V
    //   2051: aload #6
    //   2053: aload #9
    //   2055: getfield matchConstraintDefaultWidth : I
    //   2058: aload #9
    //   2060: getfield matchConstraintMinWidth : I
    //   2063: aload #9
    //   2065: getfield matchConstraintMaxWidth : I
    //   2068: aload #9
    //   2070: getfield matchConstraintPercentWidth : F
    //   2073: invokevirtual setHorizontalMatchStyle : (IIIF)V
    //   2076: aload #6
    //   2078: aload #9
    //   2080: getfield matchConstraintDefaultHeight : I
    //   2083: aload #9
    //   2085: getfield matchConstraintMinHeight : I
    //   2088: aload #9
    //   2090: getfield matchConstraintMaxHeight : I
    //   2093: aload #9
    //   2095: getfield matchConstraintPercentHeight : F
    //   2098: invokevirtual setVerticalMatchStyle : (IIIF)V
    //   2101: iinc #11, 1
    //   2104: goto -> 340
    //   2107: return
    // Exception table:
    //   from	to	target	type
    //   36	74	112	android/content/res/Resources$NotFoundException
    //   84	95	112	android/content/res/Resources$NotFoundException
    //   95	109	112	android/content/res/Resources$NotFoundException
    //   406	466	469	android/content/res/Resources$NotFoundException
  }
  
  private void setSelfDimensionBehaviour(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    int k = getPaddingTop();
    int m = getPaddingBottom();
    int n = getPaddingLeft();
    int i1 = getPaddingRight();
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.FIXED;
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
    boolean bool1 = false;
    boolean bool2 = false;
    getLayoutParams();
    if (i != Integer.MIN_VALUE) {
      if (i != 0) {
        if (i != 1073741824) {
          paramInt1 = bool1;
        } else {
          paramInt1 = Math.min(this.mMaxWidth, paramInt1) - n + i1;
        } 
      } else {
        dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        paramInt1 = bool1;
      } 
    } else {
      dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
    } 
    if (j != Integer.MIN_VALUE) {
      if (j != 0) {
        if (j != 1073741824) {
          paramInt2 = bool2;
        } else {
          paramInt2 = Math.min(this.mMaxHeight, paramInt2) - k + m;
        } 
      } else {
        dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        paramInt2 = bool2;
      } 
    } else {
      dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
    } 
    this.mLayoutWidget.setMinWidth(0);
    this.mLayoutWidget.setMinHeight(0);
    this.mLayoutWidget.setHorizontalDimensionBehaviour(dimensionBehaviour1);
    this.mLayoutWidget.setWidth(paramInt1);
    this.mLayoutWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
    this.mLayoutWidget.setHeight(paramInt2);
    this.mLayoutWidget.setMinWidth(this.mMinWidth - getPaddingLeft() - getPaddingRight());
    this.mLayoutWidget.setMinHeight(this.mMinHeight - getPaddingTop() - getPaddingBottom());
  }
  
  private void updateHierarchy() {
    boolean bool2;
    int i = getChildCount();
    boolean bool1 = false;
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < i) {
        if (getChildAt(b).isLayoutRequested()) {
          bool2 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool2) {
      this.mVariableDimensionsWidgets.clear();
      setChildrenConstraints();
    } 
  }
  
  private void updatePostMeasures() {
    int i = getChildCount();
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view instanceof Placeholder)
        ((Placeholder)view).updatePostMeasure(this); 
    } 
    i = this.mConstraintHelpers.size();
    if (i > 0)
      for (b = 0; b < i; b++)
        ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePostMeasure(this);  
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (Build.VERSION.SDK_INT < 14)
      onViewAdded(paramView); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dispatchDraw(Canvas paramCanvas) {
    super.dispatchDraw(paramCanvas);
    if (isInEditMode()) {
      int i = getChildCount();
      float f1 = getWidth();
      float f2 = getHeight();
      float f3 = 1080.0F;
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getVisibility() != 8) {
          Object object = view.getTag();
          if (object != null && object instanceof String) {
            object = ((String)object).split(",");
            if (object.length == 4) {
              int j = Integer.parseInt((String)object[0]);
              int k = Integer.parseInt((String)object[1]);
              int m = Integer.parseInt((String)object[2]);
              int n = Integer.parseInt((String)object[3]);
              j = (int)(j / f3 * f1);
              k = (int)(k / 1920.0F * f2);
              m = (int)(m / f3 * f1);
              n = (int)(n / 1920.0F * f2);
              object = new Paint();
              object.setColor(-65536);
              paramCanvas.drawLine(j, k, (j + m), k, (Paint)object);
              paramCanvas.drawLine((j + m), k, (j + m), (k + n), (Paint)object);
              paramCanvas.drawLine((j + m), (k + n), j, (k + n), (Paint)object);
              paramCanvas.drawLine(j, (k + n), j, k, (Paint)object);
              object.setColor(-16711936);
              paramCanvas.drawLine(j, k, (j + m), (k + n), (Paint)object);
              paramCanvas.drawLine(j, (k + n), (j + m), k, (Paint)object);
            } 
          } 
        } 
      } 
    } 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mLayoutWidget.fillMetrics(paramMetrics);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)new LayoutParams(paramLayoutParams);
  }
  
  public Object getDesignInformation(int paramInt, Object<String, Integer> paramObject) {
    if (paramInt == 0 && paramObject instanceof String) {
      String str = (String)paramObject;
      paramObject = (Object<String, Integer>)this.mDesignIds;
      if (paramObject != null && paramObject.containsKey(str))
        return this.mDesignIds.get(str); 
    } 
    return null;
  }
  
  public int getMaxHeight() {
    return this.mMaxHeight;
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizationLevel() {
    return this.mLayoutWidget.getOptimizationLevel();
  }
  
  public View getViewById(int paramInt) {
    return (View)this.mChildrenByIds.get(paramInt);
  }
  
  public final ConstraintWidget getViewWidget(View paramView) {
    ConstraintWidget constraintWidget;
    if (paramView == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (paramView == null) {
      paramView = null;
    } else {
      constraintWidget = ((LayoutParams)paramView.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = getChildCount();
    paramBoolean = isInEditMode();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      View view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      ConstraintWidget constraintWidget = layoutParams.widget;
      if ((view.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || paramBoolean) && !layoutParams.isInPlaceholder) {
        int i = constraintWidget.getDrawX();
        paramInt4 = constraintWidget.getDrawY();
        paramInt3 = constraintWidget.getWidth() + i;
        int j = constraintWidget.getHeight() + paramInt4;
        view.layout(i, paramInt4, paramInt3, j);
        if (view instanceof Placeholder) {
          view = ((Placeholder)view).getContent();
          if (view != null) {
            view.setVisibility(0);
            view.layout(i, paramInt4, paramInt3, j);
          } 
        } 
      } 
    } 
    paramInt2 = this.mConstraintHelpers.size();
    if (paramInt2 > 0)
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
        ((ConstraintHelper)this.mConstraintHelpers.get(paramInt1)).updatePostLayout(this);  
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool1;
    System.currentTimeMillis();
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = getPaddingLeft();
    int i1 = getPaddingTop();
    this.mLayoutWidget.setX(n);
    this.mLayoutWidget.setY(i1);
    this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
    this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
    if (Build.VERSION.SDK_INT >= 17) {
      boolean bool;
      ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
      if (getLayoutDirection() == 1) {
        bool = true;
      } else {
        bool = false;
      } 
      constraintWidgetContainer.setRtl(bool);
    } 
    setSelfDimensionBehaviour(paramInt1, paramInt2);
    int i2 = this.mLayoutWidget.getWidth();
    int i3 = this.mLayoutWidget.getHeight();
    int i4 = 0;
    if (this.mDirtyHierarchy) {
      this.mDirtyHierarchy = false;
      updateHierarchy();
      i4 = 1;
    } 
    if ((this.mOptimizationLevel & 0x8) == 8) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1) {
      this.mLayoutWidget.preOptimize();
      this.mLayoutWidget.optimizeForDimensions(i2, i3);
      internalMeasureDimensions(paramInt1, paramInt2);
    } else {
      internalMeasureChildren(paramInt1, paramInt2);
    } 
    updatePostMeasures();
    if (getChildCount() > 0 && i4)
      Analyzer.determineGroups(this.mLayoutWidget); 
    if (this.mLayoutWidget.mGroupsWrapOptimized) {
      if (this.mLayoutWidget.mHorizontalWrapOptimized && i == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedWidth < j) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setWidth(constraintWidgetContainer.mWrapFixedWidth);
        } 
        this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
      if (this.mLayoutWidget.mVerticalWrapOptimized && k == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedHeight < m) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setHeight(constraintWidgetContainer.mWrapFixedHeight);
        } 
        this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
    } 
    i4 = this.mOptimizationLevel;
    int i5 = 0;
    if ((i4 & 0x20) == 32) {
      int i8 = this.mLayoutWidget.getWidth();
      i4 = this.mLayoutWidget.getHeight();
      if (this.mLastMeasureWidth != i8 && i == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, i8); 
      if (this.mLastMeasureHeight != i4 && k == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, i4); 
      if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > j)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, j); 
      if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > m)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, m); 
    } 
    boolean bool2 = false;
    if (getChildCount() > 0)
      solveLinearSystem("First pass"); 
    j = this.mVariableDimensionsWidgets.size();
    int i6 = getPaddingBottom() + i1;
    int i7 = n + getPaddingRight();
    if (j > 0) {
      boolean bool;
      if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        i = 1;
      } else {
        i = 0;
      } 
      if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        bool = true;
      } else {
        bool = false;
      } 
      n = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
      i4 = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
      m = 0;
      k = 0;
      byte b = 0;
      while (b < j) {
        int i8;
        int i9;
        int i10;
        int i11;
        ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(b);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view == null) {
          i8 = m;
          i9 = n;
          i10 = i4;
          i11 = k;
        } else {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (!layoutParams.isHelper) {
            if (layoutParams.isGuideline) {
              i8 = m;
              i9 = n;
              i10 = i4;
              i11 = k;
            } else if (view.getVisibility() == 8) {
              i8 = m;
              i9 = n;
              i10 = i4;
              i11 = k;
            } else if (bool1 && constraintWidget.getResolutionWidth().isResolved() && constraintWidget.getResolutionHeight().isResolved()) {
              i8 = m;
              i9 = n;
              i10 = i4;
              i11 = k;
            } else {
              if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                i8 = getChildMeasureSpec(paramInt1, i7, layoutParams.width);
              } else {
                i8 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
              } 
              if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                i11 = getChildMeasureSpec(paramInt2, i6, layoutParams.height);
              } else {
                i11 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
              } 
              view.measure(i8, i11);
              Metrics metrics = this.mMetrics;
              if (metrics != null)
                metrics.additionalMeasures++; 
              int i12 = i5 + 1;
              i5 = view.getMeasuredWidth();
              i8 = view.getMeasuredHeight();
              if (i5 != constraintWidget.getWidth()) {
                constraintWidget.setWidth(i5);
                if (bool1)
                  constraintWidget.getResolutionWidth().resolve(i5); 
                if (i != 0 && constraintWidget.getRight() > n)
                  n = Math.max(n, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
                i5 = 1;
              } else {
                i5 = k;
              } 
              k = i4;
              if (i8 != constraintWidget.getHeight()) {
                constraintWidget.setHeight(i8);
                if (bool1)
                  constraintWidget.getResolutionHeight().resolve(i8); 
                k = i4;
                if (bool) {
                  k = i4;
                  if (constraintWidget.getBottom() > i4)
                    k = Math.max(i4, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
                } 
                i5 = 1;
              } 
              i4 = i5;
              if (layoutParams.needsBaseline) {
                i8 = view.getBaseline();
                i4 = i5;
                if (i8 != -1) {
                  i4 = i5;
                  if (i8 != constraintWidget.getBaselineDistance()) {
                    constraintWidget.setBaselineDistance(i8);
                    i4 = 1;
                  } 
                } 
              } 
              i8 = m;
              i9 = n;
              i10 = k;
              i11 = i4;
              i5 = i12;
              if (Build.VERSION.SDK_INT >= 11) {
                i8 = combineMeasuredStates(m, view.getMeasuredState());
                i9 = n;
                i10 = k;
                i11 = i4;
                i5 = i12;
              } 
            } 
          } else {
            i11 = k;
            i10 = i4;
            i9 = n;
            i8 = m;
          } 
        } 
        b++;
        m = i8;
        n = i9;
        i4 = i10;
        k = i11;
      } 
      i5 = j;
      if (k != 0) {
        this.mLayoutWidget.setWidth(i2);
        this.mLayoutWidget.setHeight(i3);
        if (bool1)
          this.mLayoutWidget.solveGraph(); 
        solveLinearSystem("2nd pass");
        j = 0;
        if (this.mLayoutWidget.getWidth() < n) {
          this.mLayoutWidget.setWidth(n);
          j = 1;
        } 
        if (this.mLayoutWidget.getHeight() < i4) {
          this.mLayoutWidget.setHeight(i4);
          j = 1;
        } 
        if (j != 0)
          solveLinearSystem("3rd pass"); 
      } 
      i4 = i2;
      n = 0;
      i2 = bool2;
      while (n < i5) {
        ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(n);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view != null && (view.getMeasuredWidth() != constraintWidget.getWidth() || view.getMeasuredHeight() != constraintWidget.getHeight()) && constraintWidget.getVisibility() != 8) {
          view.measure(View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824));
          Metrics metrics = this.mMetrics;
          if (metrics != null)
            metrics.additionalMeasures++; 
          i2++;
        } 
        n++;
      } 
      i4 = m;
    } else {
      i4 = 0;
    } 
    n = this.mLayoutWidget.getWidth() + i7;
    i = this.mLayoutWidget.getHeight() + i6;
    if (Build.VERSION.SDK_INT >= 11) {
      paramInt1 = resolveSizeAndState(n, paramInt1, i4);
      i4 = resolveSizeAndState(i, paramInt2, i4 << 16);
      paramInt2 = Math.min(this.mMaxWidth, paramInt1 & 0xFFFFFF);
      i4 = Math.min(this.mMaxHeight, i4 & 0xFFFFFF);
      paramInt1 = paramInt2;
      if (this.mLayoutWidget.isWidthMeasuredTooSmall())
        paramInt1 = paramInt2 | 0x1000000; 
      paramInt2 = i4;
      if (this.mLayoutWidget.isHeightMeasuredTooSmall())
        paramInt2 = i4 | 0x1000000; 
      setMeasuredDimension(paramInt1, paramInt2);
      this.mLastMeasureWidth = paramInt1;
      this.mLastMeasureHeight = paramInt2;
    } else {
      setMeasuredDimension(n, i);
      this.mLastMeasureWidth = n;
      this.mLastMeasureHeight = i;
    } 
  }
  
  public void onViewAdded(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewAdded(paramView); 
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    if (paramView instanceof Guideline && !(constraintWidget instanceof Guideline)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      layoutParams.widget = (ConstraintWidget)new Guideline();
      layoutParams.isGuideline = true;
      ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
    } 
    if (paramView instanceof ConstraintHelper) {
      ConstraintHelper constraintHelper = (ConstraintHelper)paramView;
      constraintHelper.validateParams();
      ((LayoutParams)paramView.getLayoutParams()).isHelper = true;
      if (!this.mConstraintHelpers.contains(constraintHelper))
        this.mConstraintHelpers.add(constraintHelper); 
    } 
    this.mChildrenByIds.put(paramView.getId(), paramView);
    this.mDirtyHierarchy = true;
  }
  
  public void onViewRemoved(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewRemoved(paramView); 
    this.mChildrenByIds.remove(paramView.getId());
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    this.mLayoutWidget.remove(constraintWidget);
    this.mConstraintHelpers.remove(paramView);
    this.mVariableDimensionsWidgets.remove(constraintWidget);
    this.mDirtyHierarchy = true;
  }
  
  public void removeView(View paramView) {
    super.removeView(paramView);
    if (Build.VERSION.SDK_INT < 14)
      onViewRemoved(paramView); 
  }
  
  public void requestLayout() {
    super.requestLayout();
    this.mDirtyHierarchy = true;
    this.mLastMeasureWidth = -1;
    this.mLastMeasureHeight = -1;
    this.mLastMeasureWidthSize = -1;
    this.mLastMeasureHeightSize = -1;
    this.mLastMeasureWidthMode = 0;
    this.mLastMeasureHeightMode = 0;
  }
  
  public void setConstraintSet(ConstraintSet paramConstraintSet) {
    this.mConstraintSet = paramConstraintSet;
  }
  
  public void setDesignInformation(int paramInt, Object paramObject1, Object paramObject2) {
    if (paramInt == 0 && paramObject1 instanceof String && paramObject2 instanceof Integer) {
      if (this.mDesignIds == null)
        this.mDesignIds = new HashMap<>(); 
      String str = (String)paramObject1;
      paramInt = str.indexOf("/");
      paramObject1 = str;
      if (paramInt != -1)
        paramObject1 = str.substring(paramInt + 1); 
      paramInt = ((Integer)paramObject2).intValue();
      this.mDesignIds.put(paramObject1, Integer.valueOf(paramInt));
    } 
  }
  
  public void setId(int paramInt) {
    this.mChildrenByIds.remove(getId());
    super.setId(paramInt);
    this.mChildrenByIds.put(getId(), this);
  }
  
  public void setMaxHeight(int paramInt) {
    if (paramInt == this.mMaxHeight)
      return; 
    this.mMaxHeight = paramInt;
    requestLayout();
  }
  
  public void setMaxWidth(int paramInt) {
    if (paramInt == this.mMaxWidth)
      return; 
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt == this.mMinHeight)
      return; 
    this.mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt == this.mMinWidth)
      return; 
    this.mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mLayoutWidget.setOptimizationLevel(paramInt);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  protected void solveLinearSystem(String paramString) {
    this.mLayoutWidget.layout();
    Metrics metrics = this.mMetrics;
    if (metrics != null)
      metrics.resolutions++; 
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int BASELINE = 5;
    
    public static final int BOTTOM = 4;
    
    public static final int CHAIN_PACKED = 2;
    
    public static final int CHAIN_SPREAD = 0;
    
    public static final int CHAIN_SPREAD_INSIDE = 1;
    
    public static final int END = 7;
    
    public static final int HORIZONTAL = 0;
    
    public static final int LEFT = 1;
    
    public static final int MATCH_CONSTRAINT = 0;
    
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    
    public static final int PARENT_ID = 0;
    
    public static final int RIGHT = 2;
    
    public static final int START = 6;
    
    public static final int TOP = 3;
    
    public static final int UNSET = -1;
    
    public static final int VERTICAL = 1;
    
    public int baselineToBaseline;
    
    public int bottomToBottom;
    
    public int bottomToTop;
    
    public float circleAngle;
    
    public int circleConstraint;
    
    public int circleRadius;
    
    public boolean constrainedHeight;
    
    public boolean constrainedWidth;
    
    public String dimensionRatio;
    
    int dimensionRatioSide;
    
    float dimensionRatioValue;
    
    public int editorAbsoluteX;
    
    public int editorAbsoluteY;
    
    public int endToEnd;
    
    public int endToStart;
    
    public int goneBottomMargin;
    
    public int goneEndMargin;
    
    public int goneLeftMargin;
    
    public int goneRightMargin;
    
    public int goneStartMargin;
    
    public int goneTopMargin;
    
    public int guideBegin;
    
    public int guideEnd;
    
    public float guidePercent;
    
    public boolean helped;
    
    public float horizontalBias;
    
    public int horizontalChainStyle;
    
    boolean horizontalDimensionFixed;
    
    public float horizontalWeight;
    
    boolean isGuideline;
    
    boolean isHelper;
    
    boolean isInPlaceholder;
    
    public int leftToLeft;
    
    public int leftToRight;
    
    public int matchConstraintDefaultHeight;
    
    public int matchConstraintDefaultWidth;
    
    public int matchConstraintMaxHeight;
    
    public int matchConstraintMaxWidth;
    
    public int matchConstraintMinHeight;
    
    public int matchConstraintMinWidth;
    
    public float matchConstraintPercentHeight;
    
    public float matchConstraintPercentWidth;
    
    boolean needsBaseline;
    
    public int orientation;
    
    int resolveGoneLeftMargin;
    
    int resolveGoneRightMargin;
    
    int resolvedGuideBegin;
    
    int resolvedGuideEnd;
    
    float resolvedGuidePercent;
    
    float resolvedHorizontalBias;
    
    int resolvedLeftToLeft;
    
    int resolvedLeftToRight;
    
    int resolvedRightToLeft;
    
    int resolvedRightToRight;
    
    public int rightToLeft;
    
    public int rightToRight;
    
    public int startToEnd;
    
    public int startToStart;
    
    public int topToBottom;
    
    public int topToTop;
    
    public float verticalBias;
    
    public int verticalChainStyle;
    
    boolean verticalDimensionFixed;
    
    public float verticalWeight;
    
    ConstraintWidget widget;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.guideBegin = -1;
      this.guideEnd = -1;
      this.guidePercent = -1.0F;
      this.leftToLeft = -1;
      this.leftToRight = -1;
      this.rightToLeft = -1;
      this.rightToRight = -1;
      this.topToTop = -1;
      this.topToBottom = -1;
      this.bottomToTop = -1;
      this.bottomToBottom = -1;
      this.baselineToBaseline = -1;
      this.circleConstraint = -1;
      this.circleRadius = 0;
      this.circleAngle = 0.0F;
      this.startToEnd = -1;
      this.startToStart = -1;
      this.endToStart = -1;
      this.endToEnd = -1;
      this.goneLeftMargin = -1;
      this.goneTopMargin = -1;
      this.goneRightMargin = -1;
      this.goneBottomMargin = -1;
      this.goneStartMargin = -1;
      this.goneEndMargin = -1;
      this.horizontalBias = 0.5F;
      this.verticalBias = 0.5F;
      this.dimensionRatio = null;
      this.dimensionRatioValue = 0.0F;
      this.dimensionRatioSide = 1;
      this.horizontalWeight = -1.0F;
      this.verticalWeight = -1.0F;
      this.horizontalChainStyle = 0;
      this.verticalChainStyle = 0;
      this.matchConstraintDefaultWidth = 0;
      this.matchConstraintDefaultHeight = 0;
      this.matchConstraintMinWidth = 0;
      this.matchConstraintMinHeight = 0;
      this.matchConstraintMaxWidth = 0;
      this.matchConstraintMaxHeight = 0;
      this.matchConstraintPercentWidth = 1.0F;
      this.matchConstraintPercentHeight = 1.0F;
      this.editorAbsoluteX = -1;
      this.editorAbsoluteY = -1;
      this.orientation = -1;
      this.constrainedWidth = false;
      this.constrainedHeight = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      this.needsBaseline = false;
      this.isGuideline = false;
      this.isHelper = false;
      this.isInPlaceholder = false;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolvedHorizontalBias = 0.5F;
      this.widget = new ConstraintWidget();
      this.helped = false;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      int i = -1;
      this.guideBegin = -1;
      this.guideEnd = -1;
      this.guidePercent = -1.0F;
      this.leftToLeft = -1;
      this.leftToRight = -1;
      this.rightToLeft = -1;
      this.rightToRight = -1;
      this.topToTop = -1;
      this.topToBottom = -1;
      this.bottomToTop = -1;
      this.bottomToBottom = -1;
      this.baselineToBaseline = -1;
      this.circleConstraint = -1;
      int j = 0;
      this.circleRadius = 0;
      this.circleAngle = 0.0F;
      this.startToEnd = -1;
      this.startToStart = -1;
      this.endToStart = -1;
      this.endToEnd = -1;
      this.goneLeftMargin = -1;
      this.goneTopMargin = -1;
      this.goneRightMargin = -1;
      this.goneBottomMargin = -1;
      this.goneStartMargin = -1;
      this.goneEndMargin = -1;
      this.horizontalBias = 0.5F;
      this.verticalBias = 0.5F;
      this.dimensionRatio = null;
      this.dimensionRatioValue = 0.0F;
      this.dimensionRatioSide = 1;
      this.horizontalWeight = -1.0F;
      this.verticalWeight = -1.0F;
      this.horizontalChainStyle = 0;
      this.verticalChainStyle = 0;
      this.matchConstraintDefaultWidth = 0;
      this.matchConstraintDefaultHeight = 0;
      this.matchConstraintMinWidth = 0;
      this.matchConstraintMinHeight = 0;
      this.matchConstraintMaxWidth = 0;
      this.matchConstraintMaxHeight = 0;
      this.matchConstraintPercentWidth = 1.0F;
      this.matchConstraintPercentHeight = 1.0F;
      this.editorAbsoluteX = -1;
      this.editorAbsoluteY = -1;
      this.orientation = -1;
      this.constrainedWidth = false;
      this.constrainedHeight = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      this.needsBaseline = false;
      this.isGuideline = false;
      this.isHelper = false;
      this.isInPlaceholder = false;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolvedHorizontalBias = 0.5F;
      this.widget = new ConstraintWidget();
      this.helped = false;
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ConstraintLayout_Layout);
      int k = typedArray.getIndexCount();
      int m;
      for (m = 0; m < k; m = i1) {
        String str;
        float f;
        int i2;
        int i3;
        int n = typedArray.getIndex(m);
        int i1 = Table.map.get(n);
        switch (i1) {
          default:
            switch (i1) {
              default:
                i1 = i;
                i = j;
                j = i1;
                break;
              case 50:
                this.editorAbsoluteY = typedArray.getDimensionPixelOffset(n, this.editorAbsoluteY);
                i1 = i;
                i = j;
                j = i1;
                break;
              case 49:
                this.editorAbsoluteX = typedArray.getDimensionPixelOffset(n, this.editorAbsoluteX);
                i1 = i;
                i = j;
                j = i1;
                break;
              case 48:
                this.verticalChainStyle = typedArray.getInt(n, j);
                i1 = i;
                i = j;
                j = i1;
                break;
              case 47:
                this.horizontalChainStyle = typedArray.getInt(n, j);
                i1 = i;
                i = j;
                j = i1;
                break;
              case 46:
                this.verticalWeight = typedArray.getFloat(n, this.verticalWeight);
                i1 = i;
                i = j;
                j = i1;
                break;
              case 45:
                this.horizontalWeight = typedArray.getFloat(n, this.horizontalWeight);
                i1 = i;
                i = j;
                j = i1;
                break;
              case 44:
                break;
            } 
            str = typedArray.getString(n);
            this.dimensionRatio = str;
            this.dimensionRatioValue = Float.NaN;
            this.dimensionRatioSide = i;
            if (str != null) {
              i = str.length();
              i1 = this.dimensionRatio.indexOf(',');
              if (i1 > 0 && i1 < i - 1) {
                str = this.dimensionRatio.substring(j, i1);
                if (str.equalsIgnoreCase("W")) {
                  this.dimensionRatioSide = j;
                } else if (str.equalsIgnoreCase("H")) {
                  this.dimensionRatioSide = 1;
                } 
                j = i1 + 1;
              } else {
                j = 0;
              } 
              i1 = this.dimensionRatio.indexOf(':');
              if (i1 >= 0 && i1 < i - 1) {
                String str1 = this.dimensionRatio.substring(j, i1);
                str = this.dimensionRatio.substring(i1 + 1);
                if (str1.length() > 0 && str.length() > 0)
                  try {
                    float f1 = Float.parseFloat(str1);
                    float f2 = Float.parseFloat(str);
                    if (f1 > 0.0F && f2 > 0.0F)
                      if (this.dimensionRatioSide == 1) {
                        this.dimensionRatioValue = Math.abs(f2 / f1);
                      } else {
                        this.dimensionRatioValue = Math.abs(f1 / f2);
                      }  
                  } catch (NumberFormatException numberFormatException) {} 
              } else {
                str = this.dimensionRatio.substring(j);
                if (str.length() > 0)
                  try {
                    this.dimensionRatioValue = Float.parseFloat(str);
                  } catch (NumberFormatException numberFormatException) {} 
              } 
              i = 0;
              j = -1;
              break;
            } 
            i = 0;
            j = -1;
            break;
          case 38:
            this.matchConstraintPercentHeight = Math.max(0.0F, typedArray.getFloat(n, this.matchConstraintPercentHeight));
            i = 0;
            j = -1;
            break;
          case 37:
            try {
              this.matchConstraintMaxHeight = typedArray.getDimensionPixelSize(n, this.matchConstraintMaxHeight);
              i = 0;
              j = -1;
            } catch (Exception exception) {
              if (typedArray.getInt(n, this.matchConstraintMaxHeight) == -2)
                this.matchConstraintMaxHeight = -2; 
              i = 0;
              j = -1;
            } 
            break;
          case 36:
            try {
              this.matchConstraintMinHeight = typedArray.getDimensionPixelSize(n, this.matchConstraintMinHeight);
              i = 0;
              j = -1;
            } catch (Exception exception) {
              if (typedArray.getInt(n, this.matchConstraintMinHeight) == -2)
                this.matchConstraintMinHeight = -2; 
              i = 0;
              j = -1;
            } 
            break;
          case 35:
            this.matchConstraintPercentWidth = Math.max(0.0F, typedArray.getFloat(n, this.matchConstraintPercentWidth));
            i = 0;
            j = -1;
            break;
          case 34:
            try {
              this.matchConstraintMaxWidth = typedArray.getDimensionPixelSize(n, this.matchConstraintMaxWidth);
              i = 0;
              j = -1;
            } catch (Exception exception) {
              if (typedArray.getInt(n, this.matchConstraintMaxWidth) == -2)
                this.matchConstraintMaxWidth = -2; 
              i = 0;
              j = -1;
            } 
            break;
          case 33:
            try {
              this.matchConstraintMinWidth = typedArray.getDimensionPixelSize(n, this.matchConstraintMinWidth);
              i = 0;
              j = -1;
            } catch (Exception exception) {
              if (typedArray.getInt(n, this.matchConstraintMinWidth) == -2)
                this.matchConstraintMinWidth = -2; 
              i = 0;
              j = -1;
            } 
            break;
          case 32:
            i = 0;
            j = typedArray.getInt(n, 0);
            this.matchConstraintDefaultHeight = j;
            if (j == 1) {
              Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
              j = -1;
              break;
            } 
            j = -1;
            break;
          case 31:
            i = typedArray.getInt(n, j);
            this.matchConstraintDefaultWidth = i;
            if (i == 1) {
              Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
              i1 = -1;
              i = j;
              j = i1;
              break;
            } 
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 30:
            this.verticalBias = typedArray.getFloat(n, this.verticalBias);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 29:
            this.horizontalBias = typedArray.getFloat(n, this.horizontalBias);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 28:
            this.constrainedHeight = typedArray.getBoolean(n, this.constrainedHeight);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 27:
            this.constrainedWidth = typedArray.getBoolean(n, this.constrainedWidth);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 26:
            this.goneEndMargin = typedArray.getDimensionPixelSize(n, this.goneEndMargin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 25:
            this.goneStartMargin = typedArray.getDimensionPixelSize(n, this.goneStartMargin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 24:
            this.goneBottomMargin = typedArray.getDimensionPixelSize(n, this.goneBottomMargin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 23:
            this.goneRightMargin = typedArray.getDimensionPixelSize(n, this.goneRightMargin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 22:
            this.goneTopMargin = typedArray.getDimensionPixelSize(n, this.goneTopMargin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 21:
            this.goneLeftMargin = typedArray.getDimensionPixelSize(n, this.goneLeftMargin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 20:
            i1 = j;
            i2 = typedArray.getResourceId(n, this.endToEnd);
            this.endToEnd = i2;
            i3 = -1;
            i = i1;
            j = i3;
            if (i2 == -1) {
              this.endToEnd = typedArray.getInt(n, -1);
              i = i1;
              j = i3;
            } 
            break;
          case 19:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.endToStart);
            this.endToStart = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.endToStart = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 18:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.startToStart);
            this.startToStart = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.startToStart = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 17:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.startToEnd);
            this.startToEnd = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.startToEnd = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 16:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.baselineToBaseline);
            this.baselineToBaseline = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.baselineToBaseline = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 15:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.bottomToBottom);
            this.bottomToBottom = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.bottomToBottom = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 14:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.bottomToTop);
            this.bottomToTop = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.bottomToTop = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 13:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.topToBottom);
            this.topToBottom = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.topToBottom = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 12:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.topToTop);
            this.topToTop = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.topToTop = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 11:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.rightToRight);
            this.rightToRight = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.rightToRight = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 10:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.rightToLeft);
            this.rightToLeft = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.rightToLeft = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 9:
            i1 = i;
            i3 = j;
            i2 = typedArray.getResourceId(n, this.leftToRight);
            this.leftToRight = i2;
            i = i3;
            j = i1;
            if (i2 == i1) {
              this.leftToRight = typedArray.getInt(n, i1);
              i = i3;
              j = i1;
            } 
            break;
          case 8:
            i1 = typedArray.getResourceId(n, this.leftToLeft);
            this.leftToLeft = i1;
            if (i1 == i) {
              this.leftToLeft = typedArray.getInt(n, i);
              i1 = -1;
              i = j;
              j = i1;
              break;
            } 
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 7:
            this.guidePercent = typedArray.getFloat(n, this.guidePercent);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 6:
            this.guideEnd = typedArray.getDimensionPixelOffset(n, this.guideEnd);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 5:
            this.guideBegin = typedArray.getDimensionPixelOffset(n, this.guideBegin);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 4:
            f = typedArray.getFloat(n, this.circleAngle) % 360.0F;
            this.circleAngle = f;
            if (f < 0.0F) {
              this.circleAngle = (360.0F - f) % 360.0F;
              i1 = -1;
              i = j;
              j = i1;
              break;
            } 
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 3:
            this.circleRadius = typedArray.getDimensionPixelSize(n, this.circleRadius);
            i1 = -1;
            i = j;
            j = i1;
            break;
          case 2:
            i1 = j;
            i2 = typedArray.getResourceId(n, this.circleConstraint);
            this.circleConstraint = i2;
            i3 = -1;
            i = i1;
            j = i3;
            if (i2 == -1) {
              this.circleConstraint = typedArray.getInt(n, -1);
              i = i1;
              j = i3;
            } 
            break;
          case 1:
            i1 = i;
            i = j;
            this.orientation = typedArray.getInt(n, this.orientation);
            j = i1;
            break;
        } 
        i1 = m + 1;
        m = i;
        i = j;
        j = m;
      } 
      typedArray.recycle();
      validate();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = -1;
      this.guideEnd = -1;
      this.guidePercent = -1.0F;
      this.leftToLeft = -1;
      this.leftToRight = -1;
      this.rightToLeft = -1;
      this.rightToRight = -1;
      this.topToTop = -1;
      this.topToBottom = -1;
      this.bottomToTop = -1;
      this.bottomToBottom = -1;
      this.baselineToBaseline = -1;
      this.circleConstraint = -1;
      this.circleRadius = 0;
      this.circleAngle = 0.0F;
      this.startToEnd = -1;
      this.startToStart = -1;
      this.endToStart = -1;
      this.endToEnd = -1;
      this.goneLeftMargin = -1;
      this.goneTopMargin = -1;
      this.goneRightMargin = -1;
      this.goneBottomMargin = -1;
      this.goneStartMargin = -1;
      this.goneEndMargin = -1;
      this.horizontalBias = 0.5F;
      this.verticalBias = 0.5F;
      this.dimensionRatio = null;
      this.dimensionRatioValue = 0.0F;
      this.dimensionRatioSide = 1;
      this.horizontalWeight = -1.0F;
      this.verticalWeight = -1.0F;
      this.horizontalChainStyle = 0;
      this.verticalChainStyle = 0;
      this.matchConstraintDefaultWidth = 0;
      this.matchConstraintDefaultHeight = 0;
      this.matchConstraintMinWidth = 0;
      this.matchConstraintMinHeight = 0;
      this.matchConstraintMaxWidth = 0;
      this.matchConstraintMaxHeight = 0;
      this.matchConstraintPercentWidth = 1.0F;
      this.matchConstraintPercentHeight = 1.0F;
      this.editorAbsoluteX = -1;
      this.editorAbsoluteY = -1;
      this.orientation = -1;
      this.constrainedWidth = false;
      this.constrainedHeight = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      this.needsBaseline = false;
      this.isGuideline = false;
      this.isHelper = false;
      this.isInPlaceholder = false;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolvedHorizontalBias = 0.5F;
      this.widget = new ConstraintWidget();
      this.helped = false;
      this.guideBegin = param1LayoutParams.guideBegin;
      this.guideEnd = param1LayoutParams.guideEnd;
      this.guidePercent = param1LayoutParams.guidePercent;
      this.leftToLeft = param1LayoutParams.leftToLeft;
      this.leftToRight = param1LayoutParams.leftToRight;
      this.rightToLeft = param1LayoutParams.rightToLeft;
      this.rightToRight = param1LayoutParams.rightToRight;
      this.topToTop = param1LayoutParams.topToTop;
      this.topToBottom = param1LayoutParams.topToBottom;
      this.bottomToTop = param1LayoutParams.bottomToTop;
      this.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.circleConstraint = param1LayoutParams.circleConstraint;
      this.circleRadius = param1LayoutParams.circleRadius;
      this.circleAngle = param1LayoutParams.circleAngle;
      this.startToEnd = param1LayoutParams.startToEnd;
      this.startToStart = param1LayoutParams.startToStart;
      this.endToStart = param1LayoutParams.endToStart;
      this.endToEnd = param1LayoutParams.endToEnd;
      this.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.horizontalBias = param1LayoutParams.horizontalBias;
      this.verticalBias = param1LayoutParams.verticalBias;
      this.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.dimensionRatioValue = param1LayoutParams.dimensionRatioValue;
      this.dimensionRatioSide = param1LayoutParams.dimensionRatioSide;
      this.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.verticalWeight = param1LayoutParams.verticalWeight;
      this.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.matchConstraintDefaultWidth = param1LayoutParams.matchConstraintDefaultWidth;
      this.matchConstraintDefaultHeight = param1LayoutParams.matchConstraintDefaultHeight;
      this.matchConstraintMinWidth = param1LayoutParams.matchConstraintMinWidth;
      this.matchConstraintMaxWidth = param1LayoutParams.matchConstraintMaxWidth;
      this.matchConstraintMinHeight = param1LayoutParams.matchConstraintMinHeight;
      this.matchConstraintMaxHeight = param1LayoutParams.matchConstraintMaxHeight;
      this.matchConstraintPercentWidth = param1LayoutParams.matchConstraintPercentWidth;
      this.matchConstraintPercentHeight = param1LayoutParams.matchConstraintPercentHeight;
      this.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.orientation = param1LayoutParams.orientation;
      this.horizontalDimensionFixed = param1LayoutParams.horizontalDimensionFixed;
      this.verticalDimensionFixed = param1LayoutParams.verticalDimensionFixed;
      this.needsBaseline = param1LayoutParams.needsBaseline;
      this.isGuideline = param1LayoutParams.isGuideline;
      this.resolvedLeftToLeft = param1LayoutParams.resolvedLeftToLeft;
      this.resolvedLeftToRight = param1LayoutParams.resolvedLeftToRight;
      this.resolvedRightToLeft = param1LayoutParams.resolvedRightToLeft;
      this.resolvedRightToRight = param1LayoutParams.resolvedRightToRight;
      this.resolveGoneLeftMargin = param1LayoutParams.resolveGoneLeftMargin;
      this.resolveGoneRightMargin = param1LayoutParams.resolveGoneRightMargin;
      this.resolvedHorizontalBias = param1LayoutParams.resolvedHorizontalBias;
      this.widget = param1LayoutParams.widget;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = -1;
      this.guideEnd = -1;
      this.guidePercent = -1.0F;
      this.leftToLeft = -1;
      this.leftToRight = -1;
      this.rightToLeft = -1;
      this.rightToRight = -1;
      this.topToTop = -1;
      this.topToBottom = -1;
      this.bottomToTop = -1;
      this.bottomToBottom = -1;
      this.baselineToBaseline = -1;
      this.circleConstraint = -1;
      this.circleRadius = 0;
      this.circleAngle = 0.0F;
      this.startToEnd = -1;
      this.startToStart = -1;
      this.endToStart = -1;
      this.endToEnd = -1;
      this.goneLeftMargin = -1;
      this.goneTopMargin = -1;
      this.goneRightMargin = -1;
      this.goneBottomMargin = -1;
      this.goneStartMargin = -1;
      this.goneEndMargin = -1;
      this.horizontalBias = 0.5F;
      this.verticalBias = 0.5F;
      this.dimensionRatio = null;
      this.dimensionRatioValue = 0.0F;
      this.dimensionRatioSide = 1;
      this.horizontalWeight = -1.0F;
      this.verticalWeight = -1.0F;
      this.horizontalChainStyle = 0;
      this.verticalChainStyle = 0;
      this.matchConstraintDefaultWidth = 0;
      this.matchConstraintDefaultHeight = 0;
      this.matchConstraintMinWidth = 0;
      this.matchConstraintMinHeight = 0;
      this.matchConstraintMaxWidth = 0;
      this.matchConstraintMaxHeight = 0;
      this.matchConstraintPercentWidth = 1.0F;
      this.matchConstraintPercentHeight = 1.0F;
      this.editorAbsoluteX = -1;
      this.editorAbsoluteY = -1;
      this.orientation = -1;
      this.constrainedWidth = false;
      this.constrainedHeight = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      this.needsBaseline = false;
      this.isGuideline = false;
      this.isHelper = false;
      this.isInPlaceholder = false;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolvedHorizontalBias = 0.5F;
      this.widget = new ConstraintWidget();
      this.helped = false;
    }
    
    public void reset() {
      ConstraintWidget constraintWidget = this.widget;
      if (constraintWidget != null)
        constraintWidget.reset(); 
    }
    
    public void resolveLayoutDirection(int param1Int) {
      int i = this.leftMargin;
      int j = this.rightMargin;
      super.resolveLayoutDirection(param1Int);
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolveGoneLeftMargin = this.goneLeftMargin;
      this.resolveGoneRightMargin = this.goneRightMargin;
      this.resolvedHorizontalBias = this.horizontalBias;
      this.resolvedGuideBegin = this.guideBegin;
      this.resolvedGuideEnd = this.guideEnd;
      this.resolvedGuidePercent = this.guidePercent;
      if (1 == getLayoutDirection()) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      if (param1Int != 0) {
        param1Int = 0;
        int k = this.startToEnd;
        if (k != -1) {
          this.resolvedRightToLeft = k;
          param1Int = 1;
        } else {
          k = this.startToStart;
          if (k != -1) {
            this.resolvedRightToRight = k;
            param1Int = 1;
          } 
        } 
        k = this.endToStart;
        if (k != -1) {
          this.resolvedLeftToRight = k;
          param1Int = 1;
        } 
        k = this.endToEnd;
        if (k != -1) {
          this.resolvedLeftToLeft = k;
          param1Int = 1;
        } 
        k = this.goneStartMargin;
        if (k != -1)
          this.resolveGoneRightMargin = k; 
        k = this.goneEndMargin;
        if (k != -1)
          this.resolveGoneLeftMargin = k; 
        if (param1Int != 0)
          this.resolvedHorizontalBias = 1.0F - this.horizontalBias; 
        if (this.isGuideline && this.orientation == 1) {
          float f = this.guidePercent;
          if (f != -1.0F) {
            this.resolvedGuidePercent = 1.0F - f;
            this.resolvedGuideBegin = -1;
            this.resolvedGuideEnd = -1;
          } else {
            param1Int = this.guideBegin;
            if (param1Int != -1) {
              this.resolvedGuideEnd = param1Int;
              this.resolvedGuideBegin = -1;
              this.resolvedGuidePercent = -1.0F;
            } else {
              param1Int = this.guideEnd;
              if (param1Int != -1) {
                this.resolvedGuideBegin = param1Int;
                this.resolvedGuideEnd = -1;
                this.resolvedGuidePercent = -1.0F;
              } 
            } 
          } 
        } 
      } else {
        param1Int = this.startToEnd;
        if (param1Int != -1)
          this.resolvedLeftToRight = param1Int; 
        param1Int = this.startToStart;
        if (param1Int != -1)
          this.resolvedLeftToLeft = param1Int; 
        param1Int = this.endToStart;
        if (param1Int != -1)
          this.resolvedRightToLeft = param1Int; 
        param1Int = this.endToEnd;
        if (param1Int != -1)
          this.resolvedRightToRight = param1Int; 
        param1Int = this.goneStartMargin;
        if (param1Int != -1)
          this.resolveGoneLeftMargin = param1Int; 
        param1Int = this.goneEndMargin;
        if (param1Int != -1)
          this.resolveGoneRightMargin = param1Int; 
      } 
      if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
        param1Int = this.rightToLeft;
        if (param1Int != -1) {
          this.resolvedRightToLeft = param1Int;
          if (this.rightMargin <= 0 && j > 0)
            this.rightMargin = j; 
        } else {
          param1Int = this.rightToRight;
          if (param1Int != -1) {
            this.resolvedRightToRight = param1Int;
            if (this.rightMargin <= 0 && j > 0)
              this.rightMargin = j; 
          } 
        } 
        param1Int = this.leftToLeft;
        if (param1Int != -1) {
          this.resolvedLeftToLeft = param1Int;
          if (this.leftMargin <= 0 && i > 0)
            this.leftMargin = i; 
        } else {
          param1Int = this.leftToRight;
          if (param1Int != -1) {
            this.resolvedLeftToRight = param1Int;
            if (this.leftMargin <= 0 && i > 0)
              this.leftMargin = i; 
          } 
        } 
      } 
    }
    
    public void validate() {
      this.isGuideline = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      if (this.width == -2 && this.constrainedWidth) {
        this.horizontalDimensionFixed = false;
        this.matchConstraintDefaultWidth = 1;
      } 
      if (this.height == -2 && this.constrainedHeight) {
        this.verticalDimensionFixed = false;
        this.matchConstraintDefaultHeight = 1;
      } 
      if (this.width == 0 || this.width == -1) {
        this.horizontalDimensionFixed = false;
        if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
          this.width = -2;
          this.constrainedWidth = true;
        } 
      } 
      if (this.height == 0 || this.height == -1) {
        this.verticalDimensionFixed = false;
        if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
          this.height = -2;
          this.constrainedHeight = true;
        } 
      } 
      if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
        this.isGuideline = true;
        this.horizontalDimensionFixed = true;
        this.verticalDimensionFixed = true;
        if (!(this.widget instanceof Guideline))
          this.widget = (ConstraintWidget)new Guideline(); 
        ((Guideline)this.widget).setOrientation(this.orientation);
      } 
    }
    
    private static class Table {
      public static final int ANDROID_ORIENTATION = 1;
      
      public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
      
      public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
      
      public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
      
      public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
      
      public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
      
      public static final int LAYOUT_GONE_MARGIN_END = 26;
      
      public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
      
      public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
      
      public static final int LAYOUT_GONE_MARGIN_START = 25;
      
      public static final int LAYOUT_GONE_MARGIN_TOP = 22;
      
      public static final int UNUSED = 0;
      
      public static final SparseIntArray map;
      
      static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        map = sparseIntArray;
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
        map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
      }
    }
  }
  
  private static class Table {
    public static final int ANDROID_ORIENTATION = 1;
    
    public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
    
    public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
    
    public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
    
    public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
    
    public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
    
    public static final int LAYOUT_GONE_MARGIN_END = 26;
    
    public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
    
    public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
    
    public static final int LAYOUT_GONE_MARGIN_START = 25;
    
    public static final int LAYOUT_GONE_MARGIN_TOP = 22;
    
    public static final int UNUSED = 0;
    
    public static final SparseIntArray map;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      map = sparseIntArray;
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
      map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/ConstraintLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */