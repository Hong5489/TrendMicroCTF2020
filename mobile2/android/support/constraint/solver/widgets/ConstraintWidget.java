package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
  protected static final int ANCHOR_BASELINE = 4;
  
  protected static final int ANCHOR_BOTTOM = 3;
  
  protected static final int ANCHOR_LEFT = 0;
  
  protected static final int ANCHOR_RIGHT = 1;
  
  protected static final int ANCHOR_TOP = 2;
  
  private static final boolean AUTOTAG_CENTER = false;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  public static float DEFAULT_BIAS = 0.5F;
  
  static final int DIMENSION_HORIZONTAL = 0;
  
  static final int DIMENSION_VERTICAL = 1;
  
  protected static final int DIRECT = 2;
  
  public static final int GONE = 8;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVISIBLE = 4;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_RATIO = 3;
  
  public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  protected static final int SOLVER = 1;
  
  public static final int UNKNOWN = -1;
  
  public static final int VERTICAL = 1;
  
  public static final int VISIBLE = 0;
  
  private static final int WRAP = -2;
  
  protected ArrayList<ConstraintAnchor> mAnchors;
  
  ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
  
  int mBaselineDistance;
  
  ConstraintWidgetGroup mBelongingGroup = null;
  
  ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
  
  boolean mBottomHasCentered;
  
  ConstraintAnchor mCenter;
  
  ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
  
  ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
  
  private float mCircleConstraintAngle = 0.0F;
  
  private Object mCompanionWidget;
  
  private int mContainerItemSkip;
  
  private String mDebugName;
  
  protected float mDimensionRatio;
  
  protected int mDimensionRatioSide;
  
  int mDistToBottom;
  
  int mDistToLeft;
  
  int mDistToRight;
  
  int mDistToTop;
  
  private int mDrawHeight;
  
  private int mDrawWidth;
  
  private int mDrawX;
  
  private int mDrawY;
  
  boolean mGroupsToSolver;
  
  int mHeight;
  
  float mHorizontalBiasPercent;
  
  boolean mHorizontalChainFixedPosition;
  
  int mHorizontalChainStyle;
  
  ConstraintWidget mHorizontalNextWidget;
  
  public int mHorizontalResolution = -1;
  
  boolean mHorizontalWrapVisited;
  
  boolean mIsHeightWrapContent;
  
  boolean mIsWidthWrapContent;
  
  ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
  
  boolean mLeftHasCentered;
  
  protected ConstraintAnchor[] mListAnchors;
  
  protected DimensionBehaviour[] mListDimensionBehaviors;
  
  protected ConstraintWidget[] mListNextMatchConstraintsWidget;
  
  int mMatchConstraintDefaultHeight = 0;
  
  int mMatchConstraintDefaultWidth = 0;
  
  int mMatchConstraintMaxHeight = 0;
  
  int mMatchConstraintMaxWidth = 0;
  
  int mMatchConstraintMinHeight = 0;
  
  int mMatchConstraintMinWidth = 0;
  
  float mMatchConstraintPercentHeight = 1.0F;
  
  float mMatchConstraintPercentWidth = 1.0F;
  
  private int[] mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
  
  protected int mMinHeight;
  
  protected int mMinWidth;
  
  protected ConstraintWidget[] mNextChainWidget;
  
  protected int mOffsetX;
  
  protected int mOffsetY;
  
  boolean mOptimizerMeasurable;
  
  boolean mOptimizerMeasured;
  
  ConstraintWidget mParent;
  
  int mRelX;
  
  int mRelY;
  
  ResolutionDimension mResolutionHeight;
  
  ResolutionDimension mResolutionWidth;
  
  float mResolvedDimensionRatio = 1.0F;
  
  int mResolvedDimensionRatioSide = -1;
  
  int[] mResolvedMatchConstraintDefault = new int[2];
  
  ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
  
  boolean mRightHasCentered;
  
  ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
  
  boolean mTopHasCentered;
  
  private String mType;
  
  float mVerticalBiasPercent;
  
  boolean mVerticalChainFixedPosition;
  
  int mVerticalChainStyle;
  
  ConstraintWidget mVerticalNextWidget;
  
  public int mVerticalResolution = -1;
  
  boolean mVerticalWrapVisited;
  
  private int mVisibility;
  
  float[] mWeight;
  
  int mWidth;
  
  private int mWrapHeight;
  
  private int mWrapWidth;
  
  protected int mX;
  
  protected int mY;
  
  public ConstraintWidget() {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<>();
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    addAnchors();
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2) {
    this(0, 0, paramInt1, paramInt2);
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<>();
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.mX = paramInt1;
    this.mY = paramInt2;
    this.mWidth = paramInt3;
    this.mHeight = paramInt4;
    addAnchors();
    forceUpdateDrawPosition();
  }
  
  private void addAnchors() {
    this.mAnchors.add(this.mLeft);
    this.mAnchors.add(this.mTop);
    this.mAnchors.add(this.mRight);
    this.mAnchors.add(this.mBottom);
    this.mAnchors.add(this.mCenterX);
    this.mAnchors.add(this.mCenterY);
    this.mAnchors.add(this.mCenter);
    this.mAnchors.add(this.mBaseline);
  }
  
  private void applyConstraints(LinearSystem paramLinearSystem, boolean paramBoolean1, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, DimensionBehaviour paramDimensionBehaviour, boolean paramBoolean2, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, boolean paramBoolean3, boolean paramBoolean4, int paramInt5, int paramInt6, int paramInt7, float paramFloat2, boolean paramBoolean5) {
    // Byte code:
    //   0: aload_1
    //   1: aload #7
    //   3: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   6: astore #21
    //   8: aload_1
    //   9: aload #8
    //   11: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   14: astore #22
    //   16: aload #22
    //   18: astore #23
    //   20: aload_1
    //   21: aload #7
    //   23: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   26: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   29: astore #24
    //   31: aload_1
    //   32: aload #8
    //   34: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   37: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   40: astore #25
    //   42: aload_1
    //   43: getfield graphOptimizer : Z
    //   46: ifeq -> 132
    //   49: aload #7
    //   51: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   54: getfield state : I
    //   57: iconst_1
    //   58: if_icmpne -> 132
    //   61: aload #8
    //   63: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   66: getfield state : I
    //   69: iconst_1
    //   70: if_icmpne -> 132
    //   73: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   76: ifnull -> 93
    //   79: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   82: astore_3
    //   83: aload_3
    //   84: aload_3
    //   85: getfield resolvedWidgets : J
    //   88: lconst_1
    //   89: ladd
    //   90: putfield resolvedWidgets : J
    //   93: aload #7
    //   95: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   98: aload_1
    //   99: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   102: aload #8
    //   104: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   107: aload_1
    //   108: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   111: iload #15
    //   113: ifne -> 131
    //   116: iload_2
    //   117: ifeq -> 131
    //   120: aload_1
    //   121: aload #4
    //   123: aload #23
    //   125: iconst_0
    //   126: bipush #6
    //   128: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   131: return
    //   132: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   135: ifnull -> 155
    //   138: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   141: astore #26
    //   143: aload #26
    //   145: aload #26
    //   147: getfield nonresolvedWidgets : J
    //   150: lconst_1
    //   151: ladd
    //   152: putfield nonresolvedWidgets : J
    //   155: aload #7
    //   157: invokevirtual isConnected : ()Z
    //   160: istore #27
    //   162: aload #8
    //   164: invokevirtual isConnected : ()Z
    //   167: istore #28
    //   169: aload_0
    //   170: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   173: invokevirtual isConnected : ()Z
    //   176: istore #29
    //   178: iconst_0
    //   179: istore #30
    //   181: iload #27
    //   183: ifeq -> 191
    //   186: iconst_0
    //   187: iconst_1
    //   188: iadd
    //   189: istore #30
    //   191: iload #30
    //   193: istore #31
    //   195: iload #28
    //   197: ifeq -> 206
    //   200: iload #30
    //   202: iconst_1
    //   203: iadd
    //   204: istore #31
    //   206: iload #31
    //   208: istore #30
    //   210: iload #29
    //   212: ifeq -> 221
    //   215: iload #31
    //   217: iconst_1
    //   218: iadd
    //   219: istore #30
    //   221: iload #14
    //   223: ifeq -> 232
    //   226: iconst_3
    //   227: istore #31
    //   229: goto -> 236
    //   232: iload #16
    //   234: istore #31
    //   236: getstatic android/support/constraint/solver/widgets/ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintWidget$DimensionBehaviour : [I
    //   239: aload #5
    //   241: invokevirtual ordinal : ()I
    //   244: iaload
    //   245: istore #16
    //   247: iload #16
    //   249: iconst_1
    //   250: if_icmpeq -> 307
    //   253: iload #16
    //   255: iconst_2
    //   256: if_icmpeq -> 301
    //   259: iload #16
    //   261: iconst_3
    //   262: if_icmpeq -> 295
    //   265: iload #16
    //   267: iconst_4
    //   268: if_icmpeq -> 277
    //   271: iconst_0
    //   272: istore #16
    //   274: goto -> 310
    //   277: iload #31
    //   279: iconst_4
    //   280: if_icmpne -> 289
    //   283: iconst_0
    //   284: istore #16
    //   286: goto -> 310
    //   289: iconst_1
    //   290: istore #16
    //   292: goto -> 310
    //   295: iconst_0
    //   296: istore #16
    //   298: goto -> 310
    //   301: iconst_0
    //   302: istore #16
    //   304: goto -> 310
    //   307: iconst_0
    //   308: istore #16
    //   310: aload_0
    //   311: getfield mVisibility : I
    //   314: bipush #8
    //   316: if_icmpne -> 328
    //   319: iconst_0
    //   320: istore #10
    //   322: iconst_0
    //   323: istore #16
    //   325: goto -> 328
    //   328: iload #20
    //   330: ifeq -> 385
    //   333: iload #27
    //   335: ifne -> 359
    //   338: iload #28
    //   340: ifne -> 359
    //   343: iload #29
    //   345: ifne -> 359
    //   348: aload_1
    //   349: aload #21
    //   351: iload #9
    //   353: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;I)V
    //   356: goto -> 385
    //   359: iload #27
    //   361: ifeq -> 385
    //   364: iload #28
    //   366: ifne -> 385
    //   369: aload_1
    //   370: aload #21
    //   372: aload #24
    //   374: aload #7
    //   376: invokevirtual getMargin : ()I
    //   379: bipush #6
    //   381: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   384: pop
    //   385: iload #16
    //   387: ifne -> 472
    //   390: iload #6
    //   392: ifeq -> 448
    //   395: aload_1
    //   396: aload #23
    //   398: aload #21
    //   400: iconst_0
    //   401: iconst_3
    //   402: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   405: pop
    //   406: iload #11
    //   408: ifle -> 426
    //   411: aload_1
    //   412: aload #23
    //   414: aload #21
    //   416: iload #11
    //   418: bipush #6
    //   420: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   423: goto -> 426
    //   426: iload #12
    //   428: ldc 2147483647
    //   430: if_icmpge -> 461
    //   433: aload_1
    //   434: aload #23
    //   436: aload #21
    //   438: iload #12
    //   440: bipush #6
    //   442: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   445: goto -> 461
    //   448: aload_1
    //   449: aload #23
    //   451: aload #21
    //   453: iload #10
    //   455: bipush #6
    //   457: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   460: pop
    //   461: iload #17
    //   463: istore #12
    //   465: iload #18
    //   467: istore #10
    //   469: goto -> 821
    //   472: iload #17
    //   474: istore #9
    //   476: iload #17
    //   478: bipush #-2
    //   480: if_icmpne -> 487
    //   483: iload #10
    //   485: istore #9
    //   487: iload #18
    //   489: istore #12
    //   491: iload #18
    //   493: bipush #-2
    //   495: if_icmpne -> 502
    //   498: iload #10
    //   500: istore #12
    //   502: iload #9
    //   504: ifle -> 531
    //   507: aload_1
    //   508: aload #23
    //   510: aload #21
    //   512: iload #9
    //   514: bipush #6
    //   516: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   519: iload #10
    //   521: iload #9
    //   523: invokestatic max : (II)I
    //   526: istore #10
    //   528: goto -> 531
    //   531: iload #10
    //   533: istore #17
    //   535: iload #12
    //   537: ifle -> 561
    //   540: aload_1
    //   541: aload #23
    //   543: aload #21
    //   545: iload #12
    //   547: bipush #6
    //   549: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   552: iload #10
    //   554: iload #12
    //   556: invokestatic min : (II)I
    //   559: istore #17
    //   561: iload #31
    //   563: iconst_1
    //   564: if_icmpne -> 622
    //   567: iload_2
    //   568: ifeq -> 587
    //   571: aload_1
    //   572: aload #23
    //   574: aload #21
    //   576: iload #17
    //   578: bipush #6
    //   580: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   583: pop
    //   584: goto -> 747
    //   587: iload #15
    //   589: ifeq -> 607
    //   592: aload_1
    //   593: aload #23
    //   595: aload #21
    //   597: iload #17
    //   599: iconst_4
    //   600: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   603: pop
    //   604: goto -> 747
    //   607: aload_1
    //   608: aload #23
    //   610: aload #21
    //   612: iload #17
    //   614: iconst_1
    //   615: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   618: pop
    //   619: goto -> 747
    //   622: iload #31
    //   624: iconst_2
    //   625: if_icmpne -> 747
    //   628: aload #7
    //   630: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   633: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   636: if_acmpeq -> 688
    //   639: aload #7
    //   641: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   644: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   647: if_acmpne -> 653
    //   650: goto -> 688
    //   653: aload_1
    //   654: aload_0
    //   655: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   658: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   661: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   664: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   667: astore #5
    //   669: aload_1
    //   670: aload_0
    //   671: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   674: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   677: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   680: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   683: astore #26
    //   685: goto -> 720
    //   688: aload_1
    //   689: aload_0
    //   690: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   693: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   696: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   699: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   702: astore #5
    //   704: aload_1
    //   705: aload_0
    //   706: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   709: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   712: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   715: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   718: astore #26
    //   720: aload_1
    //   721: aload_1
    //   722: invokevirtual createRow : ()Landroid/support/constraint/solver/ArrayRow;
    //   725: aload #23
    //   727: aload #21
    //   729: aload #26
    //   731: aload #5
    //   733: fload #19
    //   735: invokevirtual createRowDimensionRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;F)Landroid/support/constraint/solver/ArrayRow;
    //   738: invokevirtual addConstraint : (Landroid/support/constraint/solver/ArrayRow;)V
    //   741: iconst_0
    //   742: istore #16
    //   744: goto -> 747
    //   747: iload #12
    //   749: istore #10
    //   751: iload #16
    //   753: ifeq -> 817
    //   756: iload #30
    //   758: iconst_2
    //   759: if_icmpeq -> 817
    //   762: iload #14
    //   764: ifne -> 817
    //   767: iconst_0
    //   768: istore #16
    //   770: iload #9
    //   772: iload #17
    //   774: invokestatic max : (II)I
    //   777: istore #17
    //   779: iload #17
    //   781: istore #12
    //   783: iload #10
    //   785: ifle -> 797
    //   788: iload #10
    //   790: iload #17
    //   792: invokestatic min : (II)I
    //   795: istore #12
    //   797: aload_1
    //   798: aload #23
    //   800: aload #21
    //   802: iload #12
    //   804: bipush #6
    //   806: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   809: pop
    //   810: iload #9
    //   812: istore #12
    //   814: goto -> 821
    //   817: iload #9
    //   819: istore #12
    //   821: aload #24
    //   823: astore #5
    //   825: iload #20
    //   827: ifeq -> 1508
    //   830: iload #15
    //   832: ifeq -> 838
    //   835: goto -> 1508
    //   838: iload #27
    //   840: ifne -> 873
    //   843: iload #28
    //   845: ifne -> 873
    //   848: iload #29
    //   850: ifne -> 873
    //   853: iload_2
    //   854: ifeq -> 870
    //   857: aload_1
    //   858: aload #4
    //   860: aload #23
    //   862: iconst_0
    //   863: iconst_5
    //   864: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   867: goto -> 1492
    //   870: goto -> 1492
    //   873: iload #27
    //   875: ifeq -> 903
    //   878: iload #28
    //   880: ifne -> 903
    //   883: iload_2
    //   884: ifeq -> 900
    //   887: aload_1
    //   888: aload #4
    //   890: aload #23
    //   892: iconst_0
    //   893: iconst_5
    //   894: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   897: goto -> 1492
    //   900: goto -> 1492
    //   903: iload #27
    //   905: ifne -> 949
    //   908: iload #28
    //   910: ifeq -> 949
    //   913: aload_1
    //   914: aload #23
    //   916: aload #25
    //   918: aload #8
    //   920: invokevirtual getMargin : ()I
    //   923: ineg
    //   924: bipush #6
    //   926: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   929: pop
    //   930: iload_2
    //   931: ifeq -> 946
    //   934: aload_1
    //   935: aload #21
    //   937: aload_3
    //   938: iconst_0
    //   939: iconst_5
    //   940: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   943: goto -> 1492
    //   946: goto -> 1492
    //   949: iload #27
    //   951: ifeq -> 1492
    //   954: iload #28
    //   956: ifeq -> 1492
    //   959: iconst_0
    //   960: istore #9
    //   962: iconst_0
    //   963: istore #17
    //   965: iload #16
    //   967: ifeq -> 1202
    //   970: iload_2
    //   971: ifeq -> 993
    //   974: iload #11
    //   976: ifne -> 993
    //   979: aload_1
    //   980: aload #23
    //   982: aload #21
    //   984: iconst_0
    //   985: bipush #6
    //   987: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   990: goto -> 993
    //   993: iload #31
    //   995: ifne -> 1080
    //   998: iload #10
    //   1000: ifgt -> 1018
    //   1003: iload #12
    //   1005: ifle -> 1011
    //   1008: goto -> 1018
    //   1011: bipush #6
    //   1013: istore #11
    //   1015: goto -> 1024
    //   1018: iconst_1
    //   1019: istore #9
    //   1021: iconst_4
    //   1022: istore #11
    //   1024: aload_1
    //   1025: aload #21
    //   1027: aload #5
    //   1029: aload #7
    //   1031: invokevirtual getMargin : ()I
    //   1034: iload #11
    //   1036: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1039: pop
    //   1040: aload_1
    //   1041: aload #23
    //   1043: aload #25
    //   1045: aload #8
    //   1047: invokevirtual getMargin : ()I
    //   1050: ineg
    //   1051: iload #11
    //   1053: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1056: pop
    //   1057: iload #10
    //   1059: ifgt -> 1071
    //   1062: iload #17
    //   1064: istore #10
    //   1066: iload #12
    //   1068: ifle -> 1074
    //   1071: iconst_1
    //   1072: istore #10
    //   1074: iconst_5
    //   1075: istore #11
    //   1077: goto -> 1211
    //   1080: iload #31
    //   1082: iconst_1
    //   1083: if_icmpne -> 1099
    //   1086: iconst_1
    //   1087: istore #9
    //   1089: iconst_1
    //   1090: istore #10
    //   1092: bipush #6
    //   1094: istore #11
    //   1096: goto -> 1211
    //   1099: iload #31
    //   1101: iconst_3
    //   1102: if_icmpne -> 1190
    //   1105: iconst_4
    //   1106: istore #11
    //   1108: iload #14
    //   1110: ifne -> 1141
    //   1113: iload #11
    //   1115: istore #9
    //   1117: aload_0
    //   1118: getfield mResolvedDimensionRatioSide : I
    //   1121: iconst_m1
    //   1122: if_icmpeq -> 1145
    //   1125: iload #11
    //   1127: istore #9
    //   1129: iload #10
    //   1131: ifgt -> 1145
    //   1134: bipush #6
    //   1136: istore #9
    //   1138: goto -> 1145
    //   1141: iload #11
    //   1143: istore #9
    //   1145: aload_1
    //   1146: aload #21
    //   1148: aload #5
    //   1150: aload #7
    //   1152: invokevirtual getMargin : ()I
    //   1155: iload #9
    //   1157: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1160: pop
    //   1161: aload_1
    //   1162: aload #23
    //   1164: aload #25
    //   1166: aload #8
    //   1168: invokevirtual getMargin : ()I
    //   1171: ineg
    //   1172: iload #9
    //   1174: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1177: pop
    //   1178: iconst_1
    //   1179: istore #9
    //   1181: iconst_1
    //   1182: istore #10
    //   1184: iconst_5
    //   1185: istore #11
    //   1187: goto -> 1211
    //   1190: iconst_0
    //   1191: istore #9
    //   1193: iconst_0
    //   1194: istore #10
    //   1196: iconst_5
    //   1197: istore #11
    //   1199: goto -> 1211
    //   1202: iconst_0
    //   1203: istore #9
    //   1205: iconst_1
    //   1206: istore #10
    //   1208: iconst_5
    //   1209: istore #11
    //   1211: iconst_5
    //   1212: istore #12
    //   1214: iconst_5
    //   1215: istore #17
    //   1217: iload_2
    //   1218: istore #15
    //   1220: iload_2
    //   1221: istore #14
    //   1223: iload #10
    //   1225: ifeq -> 1368
    //   1228: aload_1
    //   1229: aload #21
    //   1231: aload #5
    //   1233: aload #7
    //   1235: invokevirtual getMargin : ()I
    //   1238: fload #13
    //   1240: aload #25
    //   1242: aload #23
    //   1244: aload #8
    //   1246: invokevirtual getMargin : ()I
    //   1249: iload #11
    //   1251: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1254: aload #7
    //   1256: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1259: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1262: instanceof android/support/constraint/solver/widgets/Barrier
    //   1265: istore #29
    //   1267: aload #8
    //   1269: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1272: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1275: instanceof android/support/constraint/solver/widgets/Barrier
    //   1278: istore #28
    //   1280: iload #29
    //   1282: ifeq -> 1308
    //   1285: iload #28
    //   1287: ifne -> 1308
    //   1290: bipush #6
    //   1292: istore #11
    //   1294: iconst_1
    //   1295: istore #20
    //   1297: iload #12
    //   1299: istore #10
    //   1301: iload #15
    //   1303: istore #6
    //   1305: goto -> 1384
    //   1308: iload #12
    //   1310: istore #10
    //   1312: iload #17
    //   1314: istore #11
    //   1316: iload #15
    //   1318: istore #6
    //   1320: iload #14
    //   1322: istore #20
    //   1324: iload #29
    //   1326: ifne -> 1384
    //   1329: iload #12
    //   1331: istore #10
    //   1333: iload #17
    //   1335: istore #11
    //   1337: iload #15
    //   1339: istore #6
    //   1341: iload #14
    //   1343: istore #20
    //   1345: iload #28
    //   1347: ifeq -> 1384
    //   1350: bipush #6
    //   1352: istore #10
    //   1354: iconst_1
    //   1355: istore #6
    //   1357: iload #17
    //   1359: istore #11
    //   1361: iload #14
    //   1363: istore #20
    //   1365: goto -> 1384
    //   1368: iload #14
    //   1370: istore #20
    //   1372: iload #15
    //   1374: istore #6
    //   1376: iload #17
    //   1378: istore #11
    //   1380: iload #12
    //   1382: istore #10
    //   1384: iload #9
    //   1386: ifeq -> 1400
    //   1389: bipush #6
    //   1391: istore #10
    //   1393: bipush #6
    //   1395: istore #11
    //   1397: goto -> 1400
    //   1400: iload #16
    //   1402: ifne -> 1410
    //   1405: iload #6
    //   1407: ifne -> 1415
    //   1410: iload #9
    //   1412: ifeq -> 1430
    //   1415: aload_1
    //   1416: aload #21
    //   1418: aload #5
    //   1420: aload #7
    //   1422: invokevirtual getMargin : ()I
    //   1425: iload #10
    //   1427: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1430: iload #16
    //   1432: ifne -> 1440
    //   1435: iload #20
    //   1437: ifne -> 1445
    //   1440: iload #9
    //   1442: ifeq -> 1464
    //   1445: aload_1
    //   1446: aload #22
    //   1448: aload #25
    //   1450: aload #8
    //   1452: invokevirtual getMargin : ()I
    //   1455: ineg
    //   1456: iload #11
    //   1458: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1461: goto -> 1464
    //   1464: iload_2
    //   1465: ifeq -> 1485
    //   1468: aload_1
    //   1469: aload #21
    //   1471: aload_3
    //   1472: iconst_0
    //   1473: bipush #6
    //   1475: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1478: aload #22
    //   1480: astore #23
    //   1482: goto -> 1492
    //   1485: aload #22
    //   1487: astore #23
    //   1489: goto -> 1492
    //   1492: iload_2
    //   1493: ifeq -> 1507
    //   1496: aload_1
    //   1497: aload #4
    //   1499: aload #23
    //   1501: iconst_0
    //   1502: bipush #6
    //   1504: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1507: return
    //   1508: iload #30
    //   1510: iconst_2
    //   1511: if_icmpge -> 1539
    //   1514: iload_2
    //   1515: ifeq -> 1539
    //   1518: aload_1
    //   1519: aload #21
    //   1521: aload_3
    //   1522: iconst_0
    //   1523: bipush #6
    //   1525: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1528: aload_1
    //   1529: aload #4
    //   1531: aload #23
    //   1533: iconst_0
    //   1534: bipush #6
    //   1536: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1539: return
  }
  
  private boolean isChainHead(int paramInt) {
    paramInt *= 2;
    if ((this.mListAnchors[paramInt]).mTarget != null) {
      ConstraintAnchor constraintAnchor = (this.mListAnchors[paramInt]).mTarget.mTarget;
      ConstraintAnchor[] arrayOfConstraintAnchor = this.mListAnchors;
      if (constraintAnchor != arrayOfConstraintAnchor[paramInt] && (arrayOfConstraintAnchor[paramInt + 1]).mTarget != null && (this.mListAnchors[paramInt + 1]).mTarget.mTarget == this.mListAnchors[paramInt + 1])
        return true; 
    } 
    return false;
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   8: astore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   17: astore_3
    //   18: aload_1
    //   19: aload_0
    //   20: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   23: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   26: astore #4
    //   28: aload_1
    //   29: aload_0
    //   30: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   36: astore #5
    //   38: aload_1
    //   39: aload_0
    //   40: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   43: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   46: astore #6
    //   48: aload_0
    //   49: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   52: astore #7
    //   54: aload #7
    //   56: ifnull -> 314
    //   59: aload #7
    //   61: ifnull -> 83
    //   64: aload #7
    //   66: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   69: iconst_0
    //   70: aaload
    //   71: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   74: if_acmpne -> 83
    //   77: iconst_1
    //   78: istore #8
    //   80: goto -> 86
    //   83: iconst_0
    //   84: istore #8
    //   86: aload_0
    //   87: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   90: astore #7
    //   92: aload #7
    //   94: ifnull -> 116
    //   97: aload #7
    //   99: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   102: iconst_1
    //   103: aaload
    //   104: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   107: if_acmpne -> 116
    //   110: iconst_1
    //   111: istore #9
    //   113: goto -> 119
    //   116: iconst_0
    //   117: istore #9
    //   119: aload_0
    //   120: iconst_0
    //   121: invokespecial isChainHead : (I)Z
    //   124: ifeq -> 145
    //   127: aload_0
    //   128: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   131: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   134: aload_0
    //   135: iconst_0
    //   136: invokevirtual addChain : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)V
    //   139: iconst_1
    //   140: istore #10
    //   142: goto -> 151
    //   145: aload_0
    //   146: invokevirtual isInHorizontalChain : ()Z
    //   149: istore #10
    //   151: aload_0
    //   152: iconst_1
    //   153: invokespecial isChainHead : (I)Z
    //   156: ifeq -> 177
    //   159: aload_0
    //   160: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   163: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   166: aload_0
    //   167: iconst_1
    //   168: invokevirtual addChain : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)V
    //   171: iconst_1
    //   172: istore #11
    //   174: goto -> 183
    //   177: aload_0
    //   178: invokevirtual isInVerticalChain : ()Z
    //   181: istore #11
    //   183: iload #8
    //   185: ifeq -> 235
    //   188: aload_0
    //   189: getfield mVisibility : I
    //   192: bipush #8
    //   194: if_icmpeq -> 235
    //   197: aload_0
    //   198: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   201: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   204: ifnonnull -> 235
    //   207: aload_0
    //   208: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   211: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   214: ifnonnull -> 235
    //   217: aload_1
    //   218: aload_1
    //   219: aload_0
    //   220: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   223: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   226: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   229: aload_3
    //   230: iconst_0
    //   231: iconst_1
    //   232: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   235: iload #9
    //   237: ifeq -> 295
    //   240: aload_0
    //   241: getfield mVisibility : I
    //   244: bipush #8
    //   246: if_icmpeq -> 295
    //   249: aload_0
    //   250: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   253: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   256: ifnonnull -> 295
    //   259: aload_0
    //   260: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   263: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   266: ifnonnull -> 295
    //   269: aload_0
    //   270: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   273: ifnonnull -> 295
    //   276: aload_1
    //   277: aload_1
    //   278: aload_0
    //   279: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   282: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   285: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   288: aload #5
    //   290: iconst_0
    //   291: iconst_1
    //   292: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   295: iload #10
    //   297: istore #12
    //   299: iload #11
    //   301: istore #10
    //   303: iload #8
    //   305: istore #13
    //   307: iload #12
    //   309: istore #11
    //   311: goto -> 326
    //   314: iconst_0
    //   315: istore #11
    //   317: iconst_0
    //   318: istore #10
    //   320: iconst_0
    //   321: istore #13
    //   323: iconst_0
    //   324: istore #9
    //   326: aload_0
    //   327: getfield mWidth : I
    //   330: istore #14
    //   332: iload #14
    //   334: istore #15
    //   336: iload #14
    //   338: aload_0
    //   339: getfield mMinWidth : I
    //   342: if_icmpge -> 351
    //   345: aload_0
    //   346: getfield mMinWidth : I
    //   349: istore #15
    //   351: aload_0
    //   352: getfield mHeight : I
    //   355: istore #14
    //   357: iload #14
    //   359: istore #16
    //   361: iload #14
    //   363: aload_0
    //   364: getfield mMinHeight : I
    //   367: if_icmpge -> 376
    //   370: aload_0
    //   371: getfield mMinHeight : I
    //   374: istore #16
    //   376: aload_0
    //   377: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   380: iconst_0
    //   381: aaload
    //   382: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   385: if_acmpeq -> 394
    //   388: iconst_1
    //   389: istore #8
    //   391: goto -> 397
    //   394: iconst_0
    //   395: istore #8
    //   397: aload_0
    //   398: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   401: iconst_1
    //   402: aaload
    //   403: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   406: if_acmpeq -> 415
    //   409: iconst_1
    //   410: istore #12
    //   412: goto -> 418
    //   415: iconst_0
    //   416: istore #12
    //   418: iconst_0
    //   419: istore #17
    //   421: aload_0
    //   422: aload_0
    //   423: getfield mDimensionRatioSide : I
    //   426: putfield mResolvedDimensionRatioSide : I
    //   429: aload_0
    //   430: getfield mDimensionRatio : F
    //   433: fstore #18
    //   435: aload_0
    //   436: fload #18
    //   438: putfield mResolvedDimensionRatio : F
    //   441: aload_0
    //   442: getfield mMatchConstraintDefaultWidth : I
    //   445: istore #19
    //   447: aload_0
    //   448: getfield mMatchConstraintDefaultHeight : I
    //   451: istore #20
    //   453: fload #18
    //   455: fconst_0
    //   456: fcmpl
    //   457: ifle -> 859
    //   460: aload_0
    //   461: getfield mVisibility : I
    //   464: bipush #8
    //   466: if_icmpeq -> 859
    //   469: iconst_1
    //   470: istore #21
    //   472: iload #19
    //   474: istore #14
    //   476: aload_0
    //   477: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   480: iconst_0
    //   481: aaload
    //   482: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   485: if_acmpne -> 500
    //   488: iload #19
    //   490: istore #14
    //   492: iload #19
    //   494: ifne -> 500
    //   497: iconst_3
    //   498: istore #14
    //   500: iload #20
    //   502: istore #22
    //   504: aload_0
    //   505: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   508: iconst_1
    //   509: aaload
    //   510: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   513: if_acmpne -> 528
    //   516: iload #20
    //   518: istore #22
    //   520: iload #20
    //   522: ifne -> 528
    //   525: iconst_3
    //   526: istore #22
    //   528: aload_0
    //   529: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   532: iconst_0
    //   533: aaload
    //   534: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   537: if_acmpne -> 591
    //   540: aload_0
    //   541: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   544: iconst_1
    //   545: aaload
    //   546: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   549: if_acmpne -> 591
    //   552: iload #14
    //   554: iconst_3
    //   555: if_icmpne -> 591
    //   558: iload #22
    //   560: iconst_3
    //   561: if_icmpne -> 591
    //   564: aload_0
    //   565: iload #13
    //   567: iload #9
    //   569: iload #8
    //   571: iload #12
    //   573: invokevirtual setupDimensionRatio : (ZZZZ)V
    //   576: iload #22
    //   578: istore #20
    //   580: iload #14
    //   582: istore #19
    //   584: iload #21
    //   586: istore #17
    //   588: goto -> 859
    //   591: aload_0
    //   592: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   595: iconst_0
    //   596: aaload
    //   597: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   600: if_acmpne -> 702
    //   603: iload #14
    //   605: iconst_3
    //   606: if_icmpne -> 702
    //   609: aload_0
    //   610: iconst_0
    //   611: putfield mResolvedDimensionRatioSide : I
    //   614: aload_0
    //   615: getfield mResolvedDimensionRatio : F
    //   618: aload_0
    //   619: getfield mHeight : I
    //   622: i2f
    //   623: fmul
    //   624: f2i
    //   625: istore #17
    //   627: aload_0
    //   628: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   631: iconst_1
    //   632: aaload
    //   633: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   636: if_acmpeq -> 668
    //   639: iload #17
    //   641: istore #14
    //   643: iload #16
    //   645: istore #15
    //   647: iload #22
    //   649: istore #16
    //   651: iconst_4
    //   652: istore #19
    //   654: iconst_0
    //   655: istore #17
    //   657: iload #15
    //   659: istore #22
    //   661: iload #19
    //   663: istore #15
    //   665: goto -> 875
    //   668: iload #16
    //   670: istore #15
    //   672: iload #22
    //   674: istore #16
    //   676: iload #14
    //   678: istore #19
    //   680: iconst_1
    //   681: istore #20
    //   683: iload #17
    //   685: istore #14
    //   687: iload #15
    //   689: istore #22
    //   691: iload #19
    //   693: istore #15
    //   695: iload #20
    //   697: istore #17
    //   699: goto -> 875
    //   702: iload #22
    //   704: istore #20
    //   706: iload #14
    //   708: istore #19
    //   710: iload #21
    //   712: istore #17
    //   714: aload_0
    //   715: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   718: iconst_1
    //   719: aaload
    //   720: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   723: if_acmpne -> 859
    //   726: iload #22
    //   728: istore #20
    //   730: iload #14
    //   732: istore #19
    //   734: iload #21
    //   736: istore #17
    //   738: iload #22
    //   740: iconst_3
    //   741: if_icmpne -> 859
    //   744: aload_0
    //   745: iconst_1
    //   746: putfield mResolvedDimensionRatioSide : I
    //   749: aload_0
    //   750: getfield mDimensionRatioSide : I
    //   753: iconst_m1
    //   754: if_icmpne -> 767
    //   757: aload_0
    //   758: fconst_1
    //   759: aload_0
    //   760: getfield mResolvedDimensionRatio : F
    //   763: fdiv
    //   764: putfield mResolvedDimensionRatio : F
    //   767: aload_0
    //   768: getfield mResolvedDimensionRatio : F
    //   771: aload_0
    //   772: getfield mWidth : I
    //   775: i2f
    //   776: fmul
    //   777: f2i
    //   778: istore #17
    //   780: aload_0
    //   781: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   784: iconst_0
    //   785: aaload
    //   786: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   789: if_acmpeq -> 821
    //   792: iload #15
    //   794: istore #16
    //   796: iload #17
    //   798: istore #22
    //   800: iconst_4
    //   801: istore #19
    //   803: iload #14
    //   805: istore #15
    //   807: iconst_0
    //   808: istore #17
    //   810: iload #16
    //   812: istore #14
    //   814: iload #19
    //   816: istore #16
    //   818: goto -> 875
    //   821: iload #17
    //   823: istore #16
    //   825: iload #22
    //   827: istore #17
    //   829: iload #14
    //   831: istore #19
    //   833: iconst_1
    //   834: istore #20
    //   836: iload #15
    //   838: istore #14
    //   840: iload #16
    //   842: istore #22
    //   844: iload #17
    //   846: istore #16
    //   848: iload #19
    //   850: istore #15
    //   852: iload #20
    //   854: istore #17
    //   856: goto -> 875
    //   859: iload #15
    //   861: istore #14
    //   863: iload #16
    //   865: istore #22
    //   867: iload #20
    //   869: istore #16
    //   871: iload #19
    //   873: istore #15
    //   875: aload_0
    //   876: getfield mResolvedMatchConstraintDefault : [I
    //   879: astore #7
    //   881: aload #7
    //   883: iconst_0
    //   884: iload #15
    //   886: iastore
    //   887: aload #7
    //   889: iconst_1
    //   890: iload #16
    //   892: iastore
    //   893: iload #17
    //   895: ifeq -> 924
    //   898: aload_0
    //   899: getfield mResolvedDimensionRatioSide : I
    //   902: istore #19
    //   904: iload #19
    //   906: ifeq -> 918
    //   909: iload #19
    //   911: iconst_m1
    //   912: if_icmpne -> 924
    //   915: goto -> 918
    //   918: iconst_1
    //   919: istore #12
    //   921: goto -> 927
    //   924: iconst_0
    //   925: istore #12
    //   927: aload_0
    //   928: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   931: iconst_0
    //   932: aaload
    //   933: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   936: if_acmpne -> 952
    //   939: aload_0
    //   940: instanceof android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   943: ifeq -> 952
    //   946: iconst_1
    //   947: istore #23
    //   949: goto -> 955
    //   952: iconst_0
    //   953: istore #23
    //   955: aload_0
    //   956: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   959: invokevirtual isConnected : ()Z
    //   962: ifeq -> 971
    //   965: iconst_0
    //   966: istore #8
    //   968: goto -> 974
    //   971: iconst_1
    //   972: istore #8
    //   974: aload_0
    //   975: getfield mHorizontalResolution : I
    //   978: iconst_2
    //   979: if_icmpeq -> 1108
    //   982: aload_0
    //   983: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   986: astore #7
    //   988: aload #7
    //   990: ifnull -> 1007
    //   993: aload_1
    //   994: aload #7
    //   996: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   999: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1002: astore #7
    //   1004: goto -> 1010
    //   1007: aconst_null
    //   1008: astore #7
    //   1010: aload_0
    //   1011: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1014: astore #24
    //   1016: aload #24
    //   1018: ifnull -> 1035
    //   1021: aload_1
    //   1022: aload #24
    //   1024: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1027: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1030: astore #24
    //   1032: goto -> 1038
    //   1035: aconst_null
    //   1036: astore #24
    //   1038: aload_0
    //   1039: aload_1
    //   1040: iload #13
    //   1042: aload #24
    //   1044: aload #7
    //   1046: aload_0
    //   1047: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1050: iconst_0
    //   1051: aaload
    //   1052: iload #23
    //   1054: aload_0
    //   1055: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1058: aload_0
    //   1059: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1062: aload_0
    //   1063: getfield mX : I
    //   1066: iload #14
    //   1068: aload_0
    //   1069: getfield mMinWidth : I
    //   1072: aload_0
    //   1073: getfield mMaxDimension : [I
    //   1076: iconst_0
    //   1077: iaload
    //   1078: aload_0
    //   1079: getfield mHorizontalBiasPercent : F
    //   1082: iload #12
    //   1084: iload #11
    //   1086: iload #15
    //   1088: aload_0
    //   1089: getfield mMatchConstraintMinWidth : I
    //   1092: aload_0
    //   1093: getfield mMatchConstraintMaxWidth : I
    //   1096: aload_0
    //   1097: getfield mMatchConstraintPercentWidth : F
    //   1100: iload #8
    //   1102: invokespecial applyConstraints : (Landroid/support/constraint/solver/LinearSystem;ZLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroid/support/constraint/solver/widgets/ConstraintAnchor;Landroid/support/constraint/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1105: goto -> 1108
    //   1108: aload_3
    //   1109: astore #24
    //   1111: aload #4
    //   1113: astore #7
    //   1115: aload #5
    //   1117: astore #4
    //   1119: aload_0
    //   1120: getfield mVerticalResolution : I
    //   1123: iconst_2
    //   1124: if_icmpne -> 1128
    //   1127: return
    //   1128: aload_0
    //   1129: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1132: iconst_1
    //   1133: aaload
    //   1134: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1137: if_acmpne -> 1153
    //   1140: aload_0
    //   1141: instanceof android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   1144: ifeq -> 1153
    //   1147: iconst_1
    //   1148: istore #11
    //   1150: goto -> 1156
    //   1153: iconst_0
    //   1154: istore #11
    //   1156: iload #17
    //   1158: ifeq -> 1185
    //   1161: aload_0
    //   1162: getfield mResolvedDimensionRatioSide : I
    //   1165: istore #14
    //   1167: iload #14
    //   1169: iconst_1
    //   1170: if_icmpeq -> 1179
    //   1173: iload #14
    //   1175: iconst_m1
    //   1176: if_icmpne -> 1185
    //   1179: iconst_1
    //   1180: istore #13
    //   1182: goto -> 1188
    //   1185: iconst_0
    //   1186: istore #13
    //   1188: aload_0
    //   1189: getfield mBaselineDistance : I
    //   1192: ifle -> 1281
    //   1195: aload_0
    //   1196: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1199: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1202: getfield state : I
    //   1205: iconst_1
    //   1206: if_icmpne -> 1223
    //   1209: aload_0
    //   1210: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1213: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1216: aload_1
    //   1217: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1220: goto -> 1281
    //   1223: aload_1
    //   1224: astore #5
    //   1226: aload #5
    //   1228: aload #6
    //   1230: aload #7
    //   1232: aload_0
    //   1233: invokevirtual getBaselineDistance : ()I
    //   1236: bipush #6
    //   1238: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1241: pop
    //   1242: aload_0
    //   1243: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1246: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1249: ifnull -> 1281
    //   1252: aload #5
    //   1254: aload #6
    //   1256: aload #5
    //   1258: aload_0
    //   1259: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1262: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1265: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1268: iconst_0
    //   1269: bipush #6
    //   1271: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1274: pop
    //   1275: iconst_0
    //   1276: istore #8
    //   1278: goto -> 1281
    //   1281: aload #7
    //   1283: astore_3
    //   1284: aload_1
    //   1285: astore #6
    //   1287: aload_0
    //   1288: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1291: astore #5
    //   1293: aload #5
    //   1295: ifnull -> 1313
    //   1298: aload #6
    //   1300: aload #5
    //   1302: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1305: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1308: astore #5
    //   1310: goto -> 1316
    //   1313: aconst_null
    //   1314: astore #5
    //   1316: aload_0
    //   1317: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1320: astore #7
    //   1322: aload #7
    //   1324: ifnull -> 1342
    //   1327: aload #6
    //   1329: aload #7
    //   1331: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1334: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1337: astore #7
    //   1339: goto -> 1345
    //   1342: aconst_null
    //   1343: astore #7
    //   1345: aload_0
    //   1346: aload_1
    //   1347: iload #9
    //   1349: aload #7
    //   1351: aload #5
    //   1353: aload_0
    //   1354: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1357: iconst_1
    //   1358: aaload
    //   1359: iload #11
    //   1361: aload_0
    //   1362: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1365: aload_0
    //   1366: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1369: aload_0
    //   1370: getfield mY : I
    //   1373: iload #22
    //   1375: aload_0
    //   1376: getfield mMinHeight : I
    //   1379: aload_0
    //   1380: getfield mMaxDimension : [I
    //   1383: iconst_1
    //   1384: iaload
    //   1385: aload_0
    //   1386: getfield mVerticalBiasPercent : F
    //   1389: iload #13
    //   1391: iload #10
    //   1393: iload #16
    //   1395: aload_0
    //   1396: getfield mMatchConstraintMinHeight : I
    //   1399: aload_0
    //   1400: getfield mMatchConstraintMaxHeight : I
    //   1403: aload_0
    //   1404: getfield mMatchConstraintPercentHeight : F
    //   1407: iload #8
    //   1409: invokespecial applyConstraints : (Landroid/support/constraint/solver/LinearSystem;ZLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroid/support/constraint/solver/widgets/ConstraintAnchor;Landroid/support/constraint/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1412: iload #17
    //   1414: ifeq -> 1460
    //   1417: aload_0
    //   1418: getfield mResolvedDimensionRatioSide : I
    //   1421: iconst_1
    //   1422: if_icmpne -> 1444
    //   1425: aload_1
    //   1426: aload #4
    //   1428: aload_3
    //   1429: aload #24
    //   1431: aload_2
    //   1432: aload_0
    //   1433: getfield mResolvedDimensionRatio : F
    //   1436: bipush #6
    //   1438: invokevirtual addRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;FI)V
    //   1441: goto -> 1460
    //   1444: aload_1
    //   1445: aload #24
    //   1447: aload_2
    //   1448: aload #4
    //   1450: aload_3
    //   1451: aload_0
    //   1452: getfield mResolvedDimensionRatio : F
    //   1455: bipush #6
    //   1457: invokevirtual addRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;FI)V
    //   1460: aload_0
    //   1461: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1464: invokevirtual isConnected : ()Z
    //   1467: ifeq -> 1506
    //   1470: aload #6
    //   1472: aload_0
    //   1473: aload_0
    //   1474: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1477: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1480: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1483: aload_0
    //   1484: getfield mCircleConstraintAngle : F
    //   1487: ldc_w 90.0
    //   1490: fadd
    //   1491: f2d
    //   1492: invokestatic toRadians : (D)D
    //   1495: d2f
    //   1496: aload_0
    //   1497: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1500: invokevirtual getMargin : ()I
    //   1503: invokevirtual addCenterPoint : (Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidget;FI)V
    //   1506: return
  }
  
  public boolean allowedInBarrier() {
    boolean bool;
    if (this.mVisibility != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void analyze(int paramInt) {
    Optimizer.analyze(paramInt, this);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2) {
    connect(paramType1, paramConstraintWidget, paramType2, 0, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt, ConstraintAnchor.Strength paramStrength) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, paramStrength, 0);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    // Byte code:
    //   0: aload_1
    //   1: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   4: if_acmpne -> 420
    //   7: aload_3
    //   8: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   11: if_acmpne -> 289
    //   14: aload_0
    //   15: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   18: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   21: astore #7
    //   23: aload_0
    //   24: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   27: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   30: astore #8
    //   32: aload_0
    //   33: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   36: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   39: astore_3
    //   40: aload_0
    //   41: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   44: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   47: astore_1
    //   48: iconst_0
    //   49: istore #9
    //   51: iconst_0
    //   52: istore #10
    //   54: aload #7
    //   56: ifnull -> 71
    //   59: iload #9
    //   61: istore #4
    //   63: aload #7
    //   65: invokevirtual isConnected : ()Z
    //   68: ifne -> 126
    //   71: aload #8
    //   73: ifnull -> 91
    //   76: aload #8
    //   78: invokevirtual isConnected : ()Z
    //   81: ifeq -> 91
    //   84: iload #9
    //   86: istore #4
    //   88: goto -> 126
    //   91: aload_0
    //   92: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   95: aload_2
    //   96: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   99: iconst_0
    //   100: aload #5
    //   102: iload #6
    //   104: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   107: aload_0
    //   108: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   111: aload_2
    //   112: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   115: iconst_0
    //   116: aload #5
    //   118: iload #6
    //   120: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   123: iconst_1
    //   124: istore #4
    //   126: aload_3
    //   127: ifnull -> 141
    //   130: iload #10
    //   132: istore #9
    //   134: aload_3
    //   135: invokevirtual isConnected : ()Z
    //   138: ifne -> 194
    //   141: aload_1
    //   142: ifnull -> 159
    //   145: aload_1
    //   146: invokevirtual isConnected : ()Z
    //   149: ifeq -> 159
    //   152: iload #10
    //   154: istore #9
    //   156: goto -> 194
    //   159: aload_0
    //   160: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   163: aload_2
    //   164: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   167: iconst_0
    //   168: aload #5
    //   170: iload #6
    //   172: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   175: aload_0
    //   176: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   179: aload_2
    //   180: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   183: iconst_0
    //   184: aload #5
    //   186: iload #6
    //   188: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   191: iconst_1
    //   192: istore #9
    //   194: iload #4
    //   196: ifeq -> 228
    //   199: iload #9
    //   201: ifeq -> 228
    //   204: aload_0
    //   205: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   208: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   211: aload_2
    //   212: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   215: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   218: iconst_0
    //   219: iload #6
    //   221: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   224: pop
    //   225: goto -> 286
    //   228: iload #4
    //   230: ifeq -> 257
    //   233: aload_0
    //   234: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   237: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   240: aload_2
    //   241: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   244: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   247: iconst_0
    //   248: iload #6
    //   250: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   253: pop
    //   254: goto -> 286
    //   257: iload #9
    //   259: ifeq -> 286
    //   262: aload_0
    //   263: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   266: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   269: aload_2
    //   270: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   273: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   276: iconst_0
    //   277: iload #6
    //   279: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   282: pop
    //   283: goto -> 286
    //   286: goto -> 988
    //   289: aload_3
    //   290: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   293: if_acmpeq -> 370
    //   296: aload_3
    //   297: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   300: if_acmpne -> 306
    //   303: goto -> 370
    //   306: aload_3
    //   307: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   310: if_acmpeq -> 320
    //   313: aload_3
    //   314: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   317: if_acmpne -> 417
    //   320: aload_0
    //   321: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   324: aload_2
    //   325: aload_3
    //   326: iconst_0
    //   327: aload #5
    //   329: iload #6
    //   331: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   334: aload_0
    //   335: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   338: aload_2
    //   339: aload_3
    //   340: iconst_0
    //   341: aload #5
    //   343: iload #6
    //   345: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   348: aload_0
    //   349: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   352: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   355: aload_2
    //   356: aload_3
    //   357: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   360: iconst_0
    //   361: iload #6
    //   363: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   366: pop
    //   367: goto -> 988
    //   370: aload_0
    //   371: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   374: aload_2
    //   375: aload_3
    //   376: iconst_0
    //   377: aload #5
    //   379: iload #6
    //   381: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   384: aload_0
    //   385: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   388: aload_2
    //   389: aload_3
    //   390: iconst_0
    //   391: aload #5
    //   393: iload #6
    //   395: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   398: aload_0
    //   399: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   402: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   405: aload_2
    //   406: aload_3
    //   407: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   410: iconst_0
    //   411: iload #6
    //   413: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   416: pop
    //   417: goto -> 988
    //   420: aload_1
    //   421: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   424: if_acmpne -> 499
    //   427: aload_3
    //   428: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   431: if_acmpeq -> 441
    //   434: aload_3
    //   435: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   438: if_acmpne -> 499
    //   441: aload_0
    //   442: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   445: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   448: astore_1
    //   449: aload_2
    //   450: aload_3
    //   451: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   454: astore_2
    //   455: aload_0
    //   456: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   459: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   462: astore_3
    //   463: aload_1
    //   464: aload_2
    //   465: iconst_0
    //   466: iload #6
    //   468: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   471: pop
    //   472: aload_3
    //   473: aload_2
    //   474: iconst_0
    //   475: iload #6
    //   477: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   480: pop
    //   481: aload_0
    //   482: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   485: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   488: aload_2
    //   489: iconst_0
    //   490: iload #6
    //   492: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   495: pop
    //   496: goto -> 988
    //   499: aload_1
    //   500: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   503: if_acmpne -> 574
    //   506: aload_3
    //   507: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   510: if_acmpeq -> 520
    //   513: aload_3
    //   514: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   517: if_acmpne -> 574
    //   520: aload_2
    //   521: aload_3
    //   522: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   525: astore_1
    //   526: aload_0
    //   527: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   530: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   533: aload_1
    //   534: iconst_0
    //   535: iload #6
    //   537: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   540: pop
    //   541: aload_0
    //   542: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   545: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   548: aload_1
    //   549: iconst_0
    //   550: iload #6
    //   552: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   555: pop
    //   556: aload_0
    //   557: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   560: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   563: aload_1
    //   564: iconst_0
    //   565: iload #6
    //   567: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   570: pop
    //   571: goto -> 988
    //   574: aload_1
    //   575: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   578: if_acmpne -> 652
    //   581: aload_3
    //   582: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   585: if_acmpne -> 652
    //   588: aload_0
    //   589: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   592: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   595: aload_2
    //   596: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   599: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   602: iconst_0
    //   603: iload #6
    //   605: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   608: pop
    //   609: aload_0
    //   610: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   613: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   616: aload_2
    //   617: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   620: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   623: iconst_0
    //   624: iload #6
    //   626: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   629: pop
    //   630: aload_0
    //   631: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   634: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   637: aload_2
    //   638: aload_3
    //   639: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   642: iconst_0
    //   643: iload #6
    //   645: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   648: pop
    //   649: goto -> 988
    //   652: aload_1
    //   653: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   656: if_acmpne -> 730
    //   659: aload_3
    //   660: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   663: if_acmpne -> 730
    //   666: aload_0
    //   667: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   670: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   673: aload_2
    //   674: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   677: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   680: iconst_0
    //   681: iload #6
    //   683: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   686: pop
    //   687: aload_0
    //   688: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   691: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   694: aload_2
    //   695: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   698: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   701: iconst_0
    //   702: iload #6
    //   704: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   707: pop
    //   708: aload_0
    //   709: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   712: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   715: aload_2
    //   716: aload_3
    //   717: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   720: iconst_0
    //   721: iload #6
    //   723: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   726: pop
    //   727: goto -> 988
    //   730: aload_0
    //   731: aload_1
    //   732: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   735: astore #8
    //   737: aload_2
    //   738: aload_3
    //   739: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   742: astore_2
    //   743: aload #8
    //   745: aload_2
    //   746: invokevirtual isValidConnection : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;)Z
    //   749: ifeq -> 988
    //   752: aload_1
    //   753: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   756: if_acmpne -> 797
    //   759: aload_0
    //   760: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   763: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   766: astore_3
    //   767: aload_0
    //   768: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   771: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   774: astore_1
    //   775: aload_3
    //   776: ifnull -> 783
    //   779: aload_3
    //   780: invokevirtual reset : ()V
    //   783: aload_1
    //   784: ifnull -> 791
    //   787: aload_1
    //   788: invokevirtual reset : ()V
    //   791: iconst_0
    //   792: istore #4
    //   794: goto -> 960
    //   797: aload_1
    //   798: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   801: if_acmpeq -> 889
    //   804: aload_1
    //   805: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   808: if_acmpne -> 814
    //   811: goto -> 889
    //   814: aload_1
    //   815: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   818: if_acmpeq -> 834
    //   821: aload_1
    //   822: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   825: if_acmpne -> 831
    //   828: goto -> 834
    //   831: goto -> 960
    //   834: aload_0
    //   835: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   838: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   841: astore_3
    //   842: aload_3
    //   843: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   846: aload_2
    //   847: if_acmpeq -> 854
    //   850: aload_3
    //   851: invokevirtual reset : ()V
    //   854: aload_0
    //   855: aload_1
    //   856: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   859: invokevirtual getOpposite : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   862: astore_3
    //   863: aload_0
    //   864: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   867: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   870: astore_1
    //   871: aload_1
    //   872: invokevirtual isConnected : ()Z
    //   875: ifeq -> 960
    //   878: aload_3
    //   879: invokevirtual reset : ()V
    //   882: aload_1
    //   883: invokevirtual reset : ()V
    //   886: goto -> 960
    //   889: aload_0
    //   890: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   893: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   896: astore_3
    //   897: aload_3
    //   898: ifnull -> 905
    //   901: aload_3
    //   902: invokevirtual reset : ()V
    //   905: aload_0
    //   906: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   909: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   912: astore_3
    //   913: aload_3
    //   914: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   917: aload_2
    //   918: if_acmpeq -> 925
    //   921: aload_3
    //   922: invokevirtual reset : ()V
    //   925: aload_0
    //   926: aload_1
    //   927: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   930: invokevirtual getOpposite : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   933: astore_3
    //   934: aload_0
    //   935: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   938: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   941: astore_1
    //   942: aload_1
    //   943: invokevirtual isConnected : ()Z
    //   946: ifeq -> 831
    //   949: aload_3
    //   950: invokevirtual reset : ()V
    //   953: aload_1
    //   954: invokevirtual reset : ()V
    //   957: goto -> 831
    //   960: aload #8
    //   962: aload_2
    //   963: iload #4
    //   965: aload #5
    //   967: iload #6
    //   969: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)Z
    //   972: pop
    //   973: aload_2
    //   974: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   977: aload #8
    //   979: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   982: invokevirtual connectedTo : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   985: goto -> 988
    //   988: return
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt, ConstraintAnchor.Strength.STRONG, 0);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt1, ConstraintAnchor.Strength.STRONG, paramInt2);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    if (paramConstraintAnchor1.getOwner() == this)
      connect(paramConstraintAnchor1.getType(), paramConstraintAnchor2.getOwner(), paramConstraintAnchor2.getType(), paramInt1, paramStrength, paramInt2); 
  }
  
  public void connectCircularConstraint(ConstraintWidget paramConstraintWidget, float paramFloat, int paramInt) {
    immediateConnect(ConstraintAnchor.Type.CENTER, paramConstraintWidget, ConstraintAnchor.Type.CENTER, paramInt, 0);
    this.mCircleConstraintAngle = paramFloat;
  }
  
  public void connectedTo(ConstraintWidget paramConstraintWidget) {}
  
  public void createObjectVariables(LinearSystem paramLinearSystem) {
    paramLinearSystem.createObjectVariable(this.mLeft);
    paramLinearSystem.createObjectVariable(this.mTop);
    paramLinearSystem.createObjectVariable(this.mRight);
    paramLinearSystem.createObjectVariable(this.mBottom);
    if (this.mBaselineDistance > 0)
      paramLinearSystem.createObjectVariable(this.mBaseline); 
  }
  
  public void disconnectUnlockedWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget && constraintAnchor.getConnectionCreator() == 2)
        constraintAnchor.reset(); 
      b++;
    } 
  }
  
  public void disconnectWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget)
        constraintAnchor.reset(); 
      b++;
    } 
  }
  
  public void forceUpdateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mX;
    int m = this.mWidth;
    int n = this.mY;
    int i1 = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + m - i;
    this.mDrawHeight = n + i1 - j;
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        return null;
      case null:
        return this.mCenterY;
      case null:
        return this.mCenterX;
      case null:
        return this.mCenter;
      case null:
        return this.mBaseline;
      case null:
        return this.mBottom;
      case null:
        return this.mRight;
      case null:
        return this.mTop;
      case null:
        break;
    } 
    return this.mLeft;
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() {
    return this.mAnchors;
  }
  
  public int getBaselineDistance() {
    return this.mBaselineDistance;
  }
  
  public float getBiasPercent(int paramInt) {
    return (paramInt == 0) ? this.mHorizontalBiasPercent : ((paramInt == 1) ? this.mVerticalBiasPercent : -1.0F);
  }
  
  public int getBottom() {
    return getY() + this.mHeight;
  }
  
  public Object getCompanionWidget() {
    return this.mCompanionWidget;
  }
  
  public int getContainerItemSkip() {
    return this.mContainerItemSkip;
  }
  
  public String getDebugName() {
    return this.mDebugName;
  }
  
  public DimensionBehaviour getDimensionBehaviour(int paramInt) {
    return (paramInt == 0) ? getHorizontalDimensionBehaviour() : ((paramInt == 1) ? getVerticalDimensionBehaviour() : null);
  }
  
  public float getDimensionRatio() {
    return this.mDimensionRatio;
  }
  
  public int getDimensionRatioSide() {
    return this.mDimensionRatioSide;
  }
  
  public int getDrawBottom() {
    return getDrawY() + this.mDrawHeight;
  }
  
  public int getDrawHeight() {
    return this.mDrawHeight;
  }
  
  public int getDrawRight() {
    return getDrawX() + this.mDrawWidth;
  }
  
  public int getDrawWidth() {
    return this.mDrawWidth;
  }
  
  public int getDrawX() {
    return this.mDrawX + this.mOffsetX;
  }
  
  public int getDrawY() {
    return this.mDrawY + this.mOffsetY;
  }
  
  public int getHeight() {
    return (this.mVisibility == 8) ? 0 : this.mHeight;
  }
  
  public float getHorizontalBiasPercent() {
    return this.mHorizontalBiasPercent;
  }
  
  public ConstraintWidget getHorizontalChainControlWidget() {
    ConstraintWidget constraintWidget;
    ConstraintAnchor constraintAnchor1 = null;
    ConstraintAnchor constraintAnchor2 = null;
    if (isInHorizontalChain()) {
      ConstraintWidget constraintWidget1 = this;
      while (true) {
        constraintAnchor1 = constraintAnchor2;
        if (constraintAnchor2 == null) {
          constraintAnchor1 = constraintAnchor2;
          if (constraintWidget1 != null) {
            constraintAnchor1 = constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor constraintAnchor = null;
            if (constraintAnchor1 == null) {
              constraintAnchor1 = null;
            } else {
              constraintAnchor1 = constraintAnchor1.getTarget();
            } 
            if (constraintAnchor1 == null) {
              constraintAnchor1 = null;
            } else {
              constraintWidget = constraintAnchor1.getOwner();
            } 
            if (constraintWidget == getParent()) {
              constraintWidget = constraintWidget1;
              break;
            } 
            if (constraintWidget != null)
              constraintAnchor = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget(); 
            if (constraintAnchor != null && constraintAnchor.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget2 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget;
            continue;
          } 
        } 
        break;
      } 
    } 
    return constraintWidget;
  }
  
  public int getHorizontalChainStyle() {
    return this.mHorizontalChainStyle;
  }
  
  public DimensionBehaviour getHorizontalDimensionBehaviour() {
    return this.mListDimensionBehaviors[0];
  }
  
  public int getInternalDrawBottom() {
    return this.mDrawY + this.mDrawHeight;
  }
  
  public int getInternalDrawRight() {
    return this.mDrawX + this.mDrawWidth;
  }
  
  int getInternalDrawX() {
    return this.mDrawX;
  }
  
  int getInternalDrawY() {
    return this.mDrawY;
  }
  
  public int getLeft() {
    return getX();
  }
  
  public int getLength(int paramInt) {
    return (paramInt == 0) ? getWidth() : ((paramInt == 1) ? getHeight() : 0);
  }
  
  public int getMaxHeight() {
    return this.mMaxDimension[1];
  }
  
  public int getMaxWidth() {
    return this.mMaxDimension[0];
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizerWrapHeight() {
    int i = this.mHeight;
    int j = i;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultHeight == 1) {
        i = Math.max(this.mMatchConstraintMinHeight, i);
      } else if (this.mMatchConstraintMinHeight > 0) {
        i = this.mMatchConstraintMinHeight;
        this.mHeight = i;
      } else {
        i = 0;
      } 
      int k = this.mMatchConstraintMaxHeight;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = this.mMatchConstraintMaxHeight; 
      } 
    } 
    return j;
  }
  
  public int getOptimizerWrapWidth() {
    int i = this.mWidth;
    int j = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultWidth == 1) {
        i = Math.max(this.mMatchConstraintMinWidth, i);
      } else if (this.mMatchConstraintMinWidth > 0) {
        i = this.mMatchConstraintMinWidth;
        this.mWidth = i;
      } else {
        i = 0;
      } 
      int k = this.mMatchConstraintMaxWidth;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = this.mMatchConstraintMaxWidth; 
      } 
    } 
    return j;
  }
  
  public ConstraintWidget getParent() {
    return this.mParent;
  }
  
  int getRelativePositioning(int paramInt) {
    return (paramInt == 0) ? this.mRelX : ((paramInt == 1) ? this.mRelY : 0);
  }
  
  public ResolutionDimension getResolutionHeight() {
    if (this.mResolutionHeight == null)
      this.mResolutionHeight = new ResolutionDimension(); 
    return this.mResolutionHeight;
  }
  
  public ResolutionDimension getResolutionWidth() {
    if (this.mResolutionWidth == null)
      this.mResolutionWidth = new ResolutionDimension(); 
    return this.mResolutionWidth;
  }
  
  public int getRight() {
    return getX() + this.mWidth;
  }
  
  public WidgetContainer getRootWidgetContainer() {
    ConstraintWidget constraintWidget;
    for (constraintWidget = this; constraintWidget.getParent() != null; constraintWidget = constraintWidget.getParent());
    return (constraintWidget instanceof WidgetContainer) ? (WidgetContainer)constraintWidget : null;
  }
  
  protected int getRootX() {
    return this.mX + this.mOffsetX;
  }
  
  protected int getRootY() {
    return this.mY + this.mOffsetY;
  }
  
  public int getTop() {
    return getY();
  }
  
  public String getType() {
    return this.mType;
  }
  
  public float getVerticalBiasPercent() {
    return this.mVerticalBiasPercent;
  }
  
  public ConstraintWidget getVerticalChainControlWidget() {
    ConstraintWidget constraintWidget;
    ConstraintAnchor constraintAnchor1 = null;
    ConstraintAnchor constraintAnchor2 = null;
    if (isInVerticalChain()) {
      ConstraintWidget constraintWidget1 = this;
      while (true) {
        constraintAnchor1 = constraintAnchor2;
        if (constraintAnchor2 == null) {
          constraintAnchor1 = constraintAnchor2;
          if (constraintWidget1 != null) {
            constraintAnchor1 = constraintWidget1.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor constraintAnchor = null;
            if (constraintAnchor1 == null) {
              constraintAnchor1 = null;
            } else {
              constraintAnchor1 = constraintAnchor1.getTarget();
            } 
            if (constraintAnchor1 == null) {
              constraintAnchor1 = null;
            } else {
              constraintWidget = constraintAnchor1.getOwner();
            } 
            if (constraintWidget == getParent()) {
              constraintWidget = constraintWidget1;
              break;
            } 
            if (constraintWidget != null)
              constraintAnchor = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget(); 
            if (constraintAnchor != null && constraintAnchor.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget2 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget;
            continue;
          } 
        } 
        break;
      } 
    } 
    return constraintWidget;
  }
  
  public int getVerticalChainStyle() {
    return this.mVerticalChainStyle;
  }
  
  public DimensionBehaviour getVerticalDimensionBehaviour() {
    return this.mListDimensionBehaviors[1];
  }
  
  public int getVisibility() {
    return this.mVisibility;
  }
  
  public int getWidth() {
    return (this.mVisibility == 8) ? 0 : this.mWidth;
  }
  
  public int getWrapHeight() {
    return this.mWrapHeight;
  }
  
  public int getWrapWidth() {
    return this.mWrapWidth;
  }
  
  public int getX() {
    return this.mX;
  }
  
  public int getY() {
    return this.mY;
  }
  
  public boolean hasAncestor(ConstraintWidget paramConstraintWidget) {
    ConstraintWidget constraintWidget1 = getParent();
    if (constraintWidget1 == paramConstraintWidget)
      return true; 
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (constraintWidget1 == paramConstraintWidget.getParent())
      return false; 
    while (constraintWidget2 != null) {
      if (constraintWidget2 == paramConstraintWidget)
        return true; 
      if (constraintWidget2 == paramConstraintWidget.getParent())
        return true; 
      constraintWidget2 = constraintWidget2.getParent();
    } 
    return false;
  }
  
  public boolean hasBaseline() {
    boolean bool;
    if (this.mBaselineDistance > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void immediateConnect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, int paramInt2) {
    getAnchor(paramType1).connect(paramConstraintWidget.getAnchor(paramType2), paramInt1, paramInt2, ConstraintAnchor.Strength.STRONG, 0, true);
  }
  
  public boolean isFullyResolved() {
    return ((this.mLeft.getResolutionNode()).state == 1 && (this.mRight.getResolutionNode()).state == 1 && (this.mTop.getResolutionNode()).state == 1 && (this.mBottom.getResolutionNode()).state == 1);
  }
  
  public boolean isHeightWrapContent() {
    return this.mIsHeightWrapContent;
  }
  
  public boolean isInHorizontalChain() {
    return ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight));
  }
  
  public boolean isInVerticalChain() {
    return ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom));
  }
  
  public boolean isInsideConstraintLayout() {
    ConstraintWidget constraintWidget1 = getParent();
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (constraintWidget1 == null)
      return false; 
    while (constraintWidget2 != null) {
      if (constraintWidget2 instanceof ConstraintWidgetContainer)
        return true; 
      constraintWidget2 = constraintWidget2.getParent();
    } 
    return false;
  }
  
  public boolean isRoot() {
    boolean bool;
    if (this.mParent == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isRootContainer() {
    if (this instanceof ConstraintWidgetContainer) {
      ConstraintWidget constraintWidget = this.mParent;
      if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer))
        return true; 
    } 
    return false;
  }
  
  public boolean isSpreadHeight() {
    int i = this.mMatchConstraintDefaultHeight;
    boolean bool = true;
    if (i != 0 || this.mDimensionRatio != 0.0F || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT)
      bool = false; 
    return bool;
  }
  
  public boolean isSpreadWidth() {
    int i = this.mMatchConstraintDefaultWidth;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (i == 0) {
      bool2 = bool1;
      if (this.mDimensionRatio == 0.0F) {
        bool2 = bool1;
        if (this.mMatchConstraintMinWidth == 0) {
          bool2 = bool1;
          if (this.mMatchConstraintMaxWidth == 0) {
            bool2 = bool1;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT)
              bool2 = true; 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public boolean isWidthWrapContent() {
    return this.mIsWidthWrapContent;
  }
  
  public void reset() {
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mParent = null;
    this.mCircleConstraintAngle = 0.0F;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    this.mMinWidth = 0;
    this.mMinHeight = 0;
    this.mWrapWidth = 0;
    this.mWrapHeight = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
    this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
    this.mCompanionWidget = null;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mType = null;
    this.mHorizontalWrapVisited = false;
    this.mVerticalWrapVisited = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mHorizontalChainFixedPosition = false;
    this.mVerticalChainFixedPosition = false;
    float[] arrayOfFloat = this.mWeight;
    arrayOfFloat[0] = -1.0F;
    arrayOfFloat[1] = -1.0F;
    this.mHorizontalResolution = -1;
    this.mVerticalResolution = -1;
    int[] arrayOfInt = this.mMaxDimension;
    arrayOfInt[0] = Integer.MAX_VALUE;
    arrayOfInt[1] = Integer.MAX_VALUE;
    this.mMatchConstraintDefaultWidth = 0;
    this.mMatchConstraintDefaultHeight = 0;
    this.mMatchConstraintPercentWidth = 1.0F;
    this.mMatchConstraintPercentHeight = 1.0F;
    this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
    this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
    this.mMatchConstraintMinWidth = 0;
    this.mMatchConstraintMinHeight = 0;
    this.mResolvedDimensionRatioSide = -1;
    this.mResolvedDimensionRatio = 1.0F;
    ResolutionDimension resolutionDimension = this.mResolutionWidth;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    resolutionDimension = this.mResolutionHeight;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    this.mBelongingGroup = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
  }
  
  public void resetAllConstraints() {
    resetAnchors();
    setVerticalBiasPercent(DEFAULT_BIAS);
    setHorizontalBiasPercent(DEFAULT_BIAS);
    if (this instanceof ConstraintWidgetContainer)
      return; 
    if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getWidth() == getWrapWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getWidth() > getMinWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
    if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getHeight() == getWrapHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getHeight() > getMinHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
  }
  
  public void resetAnchor(ConstraintAnchor paramConstraintAnchor) {
    if (getParent() != null && getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
    ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor constraintAnchor5 = getAnchor(ConstraintAnchor.Type.CENTER);
    ConstraintAnchor constraintAnchor6 = getAnchor(ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor constraintAnchor7 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
    if (paramConstraintAnchor == constraintAnchor5) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor6) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget().getOwner() == constraintAnchor2.getTarget().getOwner()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor7) {
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget().getOwner() == constraintAnchor4.getTarget().getOwner()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor1 || paramConstraintAnchor == constraintAnchor2) {
      if (constraintAnchor1.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget())
        constraintAnchor5.reset(); 
    } else if ((paramConstraintAnchor == constraintAnchor3 || paramConstraintAnchor == constraintAnchor4) && constraintAnchor3.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
      constraintAnchor5.reset();
    } 
    paramConstraintAnchor.reset();
  }
  
  public void resetAnchors() {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).reset();
      b++;
    } 
  }
  
  public void resetAnchors(int paramInt) {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = this.mAnchors.get(b);
      if (paramInt == constraintAnchor.getConnectionCreator()) {
        if (constraintAnchor.isVerticalAnchor()) {
          setVerticalBiasPercent(DEFAULT_BIAS);
        } else {
          setHorizontalBiasPercent(DEFAULT_BIAS);
        } 
        constraintAnchor.reset();
      } 
      b++;
    } 
  }
  
  public void resetResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().reset(); 
  }
  
  public void resetSolverVariables(Cache paramCache) {
    this.mLeft.resetSolverVariable(paramCache);
    this.mTop.resetSolverVariable(paramCache);
    this.mRight.resetSolverVariable(paramCache);
    this.mBottom.resetSolverVariable(paramCache);
    this.mBaseline.resetSolverVariable(paramCache);
    this.mCenter.resetSolverVariable(paramCache);
    this.mCenterX.resetSolverVariable(paramCache);
    this.mCenterY.resetSolverVariable(paramCache);
  }
  
  public void resolve() {}
  
  public void setBaselineDistance(int paramInt) {
    this.mBaselineDistance = paramInt;
  }
  
  public void setCompanionWidget(Object paramObject) {
    this.mCompanionWidget = paramObject;
  }
  
  public void setContainerItemSkip(int paramInt) {
    if (paramInt >= 0) {
      this.mContainerItemSkip = paramInt;
    } else {
      this.mContainerItemSkip = 0;
    } 
  }
  
  public void setDebugName(String paramString) {
    this.mDebugName = paramString;
  }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.mDebugName = paramString;
    SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mLeft);
    SolverVariable solverVariable2 = paramLinearSystem.createObjectVariable(this.mTop);
    SolverVariable solverVariable3 = paramLinearSystem.createObjectVariable(this.mRight);
    SolverVariable solverVariable4 = paramLinearSystem.createObjectVariable(this.mBottom);
    StringBuilder stringBuilder4 = new StringBuilder();
    stringBuilder4.append(paramString);
    stringBuilder4.append(".left");
    solverVariable1.setName(stringBuilder4.toString());
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramString);
    stringBuilder1.append(".top");
    solverVariable2.setName(stringBuilder1.toString());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(".right");
    solverVariable3.setName(stringBuilder2.toString());
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".bottom");
    solverVariable4.setName(stringBuilder3.toString());
    if (this.mBaselineDistance > 0) {
      solverVariable4 = paramLinearSystem.createObjectVariable(this.mBaseline);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".baseline");
      solverVariable4.setName(stringBuilder.toString());
    } 
  }
  
  public void setDimension(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    int i = this.mMinWidth;
    if (paramInt1 < i)
      this.mWidth = i; 
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
  }
  
  public void setDimensionRatio(float paramFloat, int paramInt) {
    this.mDimensionRatio = paramFloat;
    this.mDimensionRatioSide = paramInt;
  }
  
  public void setDimensionRatio(String paramString) {
    float f4;
    if (paramString == null || paramString.length() == 0) {
      this.mDimensionRatio = 0.0F;
      return;
    } 
    byte b = -1;
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    int i = paramString.length();
    int j = paramString.indexOf(',');
    if (j > 0 && j < i - 1) {
      String str = paramString.substring(0, j);
      if (str.equalsIgnoreCase("W")) {
        b = 0;
      } else if (str.equalsIgnoreCase("H")) {
        b = 1;
      } 
      j++;
    } else {
      j = 0;
    } 
    int k = paramString.indexOf(':');
    if (k >= 0 && k < i - 1) {
      String str = paramString.substring(j, k);
      paramString = paramString.substring(k + 1);
      f4 = f1;
      if (str.length() > 0) {
        f4 = f1;
        if (paramString.length() > 0)
          try {
            float f = Float.parseFloat(str);
            f2 = Float.parseFloat(paramString);
            f4 = f3;
            if (f > 0.0F) {
              f4 = f3;
              if (f2 > 0.0F)
                if (b == 1) {
                  f4 = Math.abs(f2 / f);
                } else {
                  f4 = Math.abs(f / f2);
                }  
            } 
          } catch (NumberFormatException numberFormatException) {
            f4 = f1;
          }  
      } 
    } else {
      String str = numberFormatException.substring(j);
      f4 = f2;
      if (str.length() > 0)
        try {
          f4 = Float.parseFloat(str);
        } catch (NumberFormatException numberFormatException1) {
          f4 = f2;
        }  
    } 
    if (f4 > 0.0F) {
      this.mDimensionRatio = f4;
      this.mDimensionRatioSide = b;
    } 
  }
  
  public void setDrawHeight(int paramInt) {
    this.mDrawHeight = paramInt;
  }
  
  public void setDrawOrigin(int paramInt1, int paramInt2) {
    paramInt1 -= this.mOffsetX;
    this.mDrawX = paramInt1;
    paramInt2 -= this.mOffsetY;
    this.mDrawY = paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setDrawWidth(int paramInt) {
    this.mDrawWidth = paramInt;
  }
  
  public void setDrawX(int paramInt) {
    paramInt -= this.mOffsetX;
    this.mDrawX = paramInt;
    this.mX = paramInt;
  }
  
  public void setDrawY(int paramInt) {
    paramInt -= this.mOffsetY;
    this.mDrawY = paramInt;
    this.mY = paramInt;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 0) {
      setHorizontalDimension(paramInt1, paramInt2);
    } else if (paramInt3 == 1) {
      setVerticalDimension(paramInt1, paramInt2);
    } 
    this.mOptimizerMeasured = true;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt3 - paramInt1;
    paramInt3 = paramInt4 - paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
    if (this.mVisibility == 8) {
      this.mWidth = 0;
      this.mHeight = 0;
      return;
    } 
    paramInt1 = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
      paramInt1 = i;
      if (i < this.mWidth)
        paramInt1 = this.mWidth; 
    } 
    paramInt2 = paramInt3;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
      paramInt2 = paramInt3;
      if (paramInt3 < this.mHeight)
        paramInt2 = this.mHeight; 
    } 
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
    paramInt2 = this.mWidth;
    paramInt1 = this.mMinWidth;
    if (paramInt2 < paramInt1)
      this.mWidth = paramInt1; 
    this.mOptimizerMeasured = true;
  }
  
  public void setGoneMargin(ConstraintAnchor.Type paramType, int paramInt) {
    int i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramType.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            this.mBottom.mGoneMargin = paramInt; 
        } else {
          this.mRight.mGoneMargin = paramInt;
        } 
      } else {
        this.mTop.mGoneMargin = paramInt;
      } 
    } else {
      this.mLeft.mGoneMargin = paramInt;
    } 
  }
  
  public void setHeight(int paramInt) {
    this.mHeight = paramInt;
    int i = this.mMinHeight;
    if (paramInt < i)
      this.mHeight = i; 
  }
  
  public void setHeightWrapContent(boolean paramBoolean) {
    this.mIsHeightWrapContent = paramBoolean;
  }
  
  public void setHorizontalBiasPercent(float paramFloat) {
    this.mHorizontalBiasPercent = paramFloat;
  }
  
  public void setHorizontalChainStyle(int paramInt) {
    this.mHorizontalChainStyle = paramInt;
  }
  
  public void setHorizontalDimension(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    paramInt1 = paramInt2 - paramInt1;
    this.mWidth = paramInt1;
    paramInt2 = this.mMinWidth;
    if (paramInt1 < paramInt2)
      this.mWidth = paramInt2; 
  }
  
  public void setHorizontalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[0] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setWidth(this.mWrapWidth); 
  }
  
  public void setHorizontalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultWidth = paramInt1;
    this.mMatchConstraintMinWidth = paramInt2;
    this.mMatchConstraintMaxWidth = paramInt3;
    this.mMatchConstraintPercentWidth = paramFloat;
    if (paramFloat < 1.0F && paramInt1 == 0)
      this.mMatchConstraintDefaultWidth = 2; 
  }
  
  public void setHorizontalWeight(float paramFloat) {
    this.mWeight[0] = paramFloat;
  }
  
  public void setLength(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      setWidth(paramInt1);
    } else if (paramInt2 == 1) {
      setHeight(paramInt1);
    } 
  }
  
  public void setMaxHeight(int paramInt) {
    this.mMaxDimension[1] = paramInt;
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxDimension[0] = paramInt;
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt < 0) {
      this.mMinHeight = 0;
    } else {
      this.mMinHeight = paramInt;
    } 
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt < 0) {
      this.mMinWidth = 0;
    } else {
      this.mMinWidth = paramInt;
    } 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
  
  public void setOrigin(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setParent(ConstraintWidget paramConstraintWidget) {
    this.mParent = paramConstraintWidget;
  }
  
  void setRelativePositioning(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      this.mRelX = paramInt1;
    } else if (paramInt2 == 1) {
      this.mRelY = paramInt1;
    } 
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setVerticalBiasPercent(float paramFloat) {
    this.mVerticalBiasPercent = paramFloat;
  }
  
  public void setVerticalChainStyle(int paramInt) {
    this.mVerticalChainStyle = paramInt;
  }
  
  public void setVerticalDimension(int paramInt1, int paramInt2) {
    this.mY = paramInt1;
    paramInt2 -= paramInt1;
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
  }
  
  public void setVerticalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[1] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setHeight(this.mWrapHeight); 
  }
  
  public void setVerticalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultHeight = paramInt1;
    this.mMatchConstraintMinHeight = paramInt2;
    this.mMatchConstraintMaxHeight = paramInt3;
    this.mMatchConstraintPercentHeight = paramFloat;
    if (paramFloat < 1.0F && paramInt1 == 0)
      this.mMatchConstraintDefaultHeight = 2; 
  }
  
  public void setVerticalWeight(float paramFloat) {
    this.mWeight[1] = paramFloat;
  }
  
  public void setVisibility(int paramInt) {
    this.mVisibility = paramInt;
  }
  
  public void setWidth(int paramInt) {
    this.mWidth = paramInt;
    int i = this.mMinWidth;
    if (paramInt < i)
      this.mWidth = i; 
  }
  
  public void setWidthWrapContent(boolean paramBoolean) {
    this.mIsWidthWrapContent = paramBoolean;
  }
  
  public void setWrapHeight(int paramInt) {
    this.mWrapHeight = paramInt;
  }
  
  public void setWrapWidth(int paramInt) {
    this.mWrapWidth = paramInt;
  }
  
  public void setX(int paramInt) {
    this.mX = paramInt;
  }
  
  public void setY(int paramInt) {
    this.mY = paramInt;
  }
  
  public void setupDimensionRatio(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean3 && !paramBoolean4) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean3 && paramBoolean4) {
        this.mResolvedDimensionRatioSide = 1;
        if (this.mDimensionRatioSide == -1)
          this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio; 
      }  
    if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
      this.mResolvedDimensionRatioSide = 1;
    } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
      this.mResolvedDimensionRatioSide = 0;
    } 
    if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected()))
      if (this.mTop.isConnected() && this.mBottom.isConnected()) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean1 && !paramBoolean2) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean1 && paramBoolean2) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1 && paramBoolean1 && paramBoolean2) {
      this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
      this.mResolvedDimensionRatioSide = 1;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    String str1 = this.mType;
    String str2 = "";
    if (str1 != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("type: ");
      stringBuilder1.append(this.mType);
      stringBuilder1.append(" ");
      String str = stringBuilder1.toString();
    } else {
      str1 = "";
    } 
    stringBuilder.append(str1);
    str1 = str2;
    if (this.mDebugName != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("id: ");
      stringBuilder1.append(this.mDebugName);
      stringBuilder1.append(" ");
      str1 = stringBuilder1.toString();
    } 
    stringBuilder.append(str1);
    stringBuilder.append("(");
    stringBuilder.append(this.mX);
    stringBuilder.append(", ");
    stringBuilder.append(this.mY);
    stringBuilder.append(") - (");
    stringBuilder.append(this.mWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mHeight);
    stringBuilder.append(") wrap: (");
    stringBuilder.append(this.mWrapWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mWrapHeight);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void updateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mX;
    int m = this.mWidth;
    int n = this.mY;
    int i1 = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + m - i;
    this.mDrawHeight = n + i1 - j;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   8: istore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   17: istore_3
    //   18: aload_1
    //   19: aload_0
    //   20: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   23: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   26: istore #4
    //   28: aload_1
    //   29: aload_0
    //   30: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   36: istore #5
    //   38: iload #4
    //   40: iload_2
    //   41: isub
    //   42: iflt -> 112
    //   45: iload #5
    //   47: iload_3
    //   48: isub
    //   49: iflt -> 112
    //   52: iload_2
    //   53: ldc_w -2147483648
    //   56: if_icmpeq -> 112
    //   59: iload_2
    //   60: ldc 2147483647
    //   62: if_icmpeq -> 112
    //   65: iload_3
    //   66: ldc_w -2147483648
    //   69: if_icmpeq -> 112
    //   72: iload_3
    //   73: ldc 2147483647
    //   75: if_icmpeq -> 112
    //   78: iload #4
    //   80: ldc_w -2147483648
    //   83: if_icmpeq -> 112
    //   86: iload #4
    //   88: ldc 2147483647
    //   90: if_icmpeq -> 112
    //   93: iload #5
    //   95: ldc_w -2147483648
    //   98: if_icmpeq -> 112
    //   101: iload #5
    //   103: istore #6
    //   105: iload #5
    //   107: ldc 2147483647
    //   109: if_icmpne -> 122
    //   112: iconst_0
    //   113: istore_2
    //   114: iconst_0
    //   115: istore_3
    //   116: iconst_0
    //   117: istore #4
    //   119: iconst_0
    //   120: istore #6
    //   122: aload_0
    //   123: iload_2
    //   124: iload_3
    //   125: iload #4
    //   127: iload #6
    //   129: invokevirtual setFrame : (IIII)V
    //   132: return
  }
  
  public void updateResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().update(); 
  }
  
  public enum ContentAlignment {
    BEGIN, BOTTOM, END, LEFT, MIDDLE, RIGHT, TOP, VERTICAL_MIDDLE;
    
    static {
      BOTTOM = new ContentAlignment("BOTTOM", 5);
      LEFT = new ContentAlignment("LEFT", 6);
      ContentAlignment contentAlignment = new ContentAlignment("RIGHT", 7);
      RIGHT = contentAlignment;
      $VALUES = new ContentAlignment[] { BEGIN, MIDDLE, END, TOP, VERTICAL_MIDDLE, BOTTOM, LEFT, contentAlignment };
    }
  }
  
  public enum DimensionBehaviour {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP_CONTENT;
    
    static {
      DimensionBehaviour dimensionBehaviour = new DimensionBehaviour("MATCH_PARENT", 3);
      MATCH_PARENT = dimensionBehaviour;
      $VALUES = new DimensionBehaviour[] { FIXED, WRAP_CONTENT, MATCH_CONSTRAINT, dimensionBehaviour };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/ConstraintWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */