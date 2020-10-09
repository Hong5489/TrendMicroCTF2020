package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead {
  private boolean mDefined;
  
  protected ConstraintWidget mFirst;
  
  protected ConstraintWidget mFirstMatchConstraintWidget;
  
  protected ConstraintWidget mFirstVisibleWidget;
  
  protected boolean mHasComplexMatchWeights;
  
  protected boolean mHasDefinedWeights;
  
  protected boolean mHasUndefinedWeights;
  
  protected ConstraintWidget mHead;
  
  private boolean mIsRtl = false;
  
  protected ConstraintWidget mLast;
  
  protected ConstraintWidget mLastMatchConstraintWidget;
  
  protected ConstraintWidget mLastVisibleWidget;
  
  private int mOrientation;
  
  protected float mTotalWeight = 0.0F;
  
  protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
  
  protected int mWidgetsCount;
  
  protected int mWidgetsMatchCount;
  
  public ChainHead(ConstraintWidget paramConstraintWidget, int paramInt, boolean paramBoolean) {
    this.mFirst = paramConstraintWidget;
    this.mOrientation = paramInt;
    this.mIsRtl = paramBoolean;
  }
  
  private void defineChainProperties() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mOrientation : I
    //   4: iconst_2
    //   5: imul
    //   6: istore_1
    //   7: aload_0
    //   8: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   11: astore_2
    //   12: aload_0
    //   13: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   16: astore_3
    //   17: aload_0
    //   18: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   21: astore #4
    //   23: iconst_0
    //   24: istore #5
    //   26: iconst_1
    //   27: istore #6
    //   29: iload #5
    //   31: ifne -> 388
    //   34: aload_0
    //   35: aload_0
    //   36: getfield mWidgetsCount : I
    //   39: iconst_1
    //   40: iadd
    //   41: putfield mWidgetsCount : I
    //   44: aload_3
    //   45: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   48: aload_0
    //   49: getfield mOrientation : I
    //   52: aconst_null
    //   53: aastore
    //   54: aload_3
    //   55: getfield mListNextMatchConstraintsWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   58: aload_0
    //   59: getfield mOrientation : I
    //   62: aconst_null
    //   63: aastore
    //   64: aload_3
    //   65: invokevirtual getVisibility : ()I
    //   68: bipush #8
    //   70: if_icmpeq -> 286
    //   73: aload_0
    //   74: getfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   77: ifnonnull -> 85
    //   80: aload_0
    //   81: aload_3
    //   82: putfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   85: aload_0
    //   86: aload_3
    //   87: putfield mLastVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   90: aload_3
    //   91: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   94: aload_0
    //   95: getfield mOrientation : I
    //   98: aaload
    //   99: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   102: if_acmpne -> 286
    //   105: aload_3
    //   106: getfield mResolvedMatchConstraintDefault : [I
    //   109: aload_0
    //   110: getfield mOrientation : I
    //   113: iaload
    //   114: ifeq -> 143
    //   117: aload_3
    //   118: getfield mResolvedMatchConstraintDefault : [I
    //   121: aload_0
    //   122: getfield mOrientation : I
    //   125: iaload
    //   126: iconst_3
    //   127: if_icmpeq -> 143
    //   130: aload_3
    //   131: getfield mResolvedMatchConstraintDefault : [I
    //   134: aload_0
    //   135: getfield mOrientation : I
    //   138: iaload
    //   139: iconst_2
    //   140: if_icmpne -> 286
    //   143: aload_0
    //   144: aload_0
    //   145: getfield mWidgetsMatchCount : I
    //   148: iconst_1
    //   149: iadd
    //   150: putfield mWidgetsMatchCount : I
    //   153: aload_3
    //   154: getfield mWeight : [F
    //   157: aload_0
    //   158: getfield mOrientation : I
    //   161: faload
    //   162: fstore #7
    //   164: fload #7
    //   166: fconst_0
    //   167: fcmpl
    //   168: ifle -> 189
    //   171: aload_0
    //   172: aload_0
    //   173: getfield mTotalWeight : F
    //   176: aload_3
    //   177: getfield mWeight : [F
    //   180: aload_0
    //   181: getfield mOrientation : I
    //   184: faload
    //   185: fadd
    //   186: putfield mTotalWeight : F
    //   189: aload_3
    //   190: aload_0
    //   191: getfield mOrientation : I
    //   194: invokestatic isMatchConstraintEqualityCandidate : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)Z
    //   197: ifeq -> 247
    //   200: fload #7
    //   202: fconst_0
    //   203: fcmpg
    //   204: ifge -> 215
    //   207: aload_0
    //   208: iconst_1
    //   209: putfield mHasUndefinedWeights : Z
    //   212: goto -> 220
    //   215: aload_0
    //   216: iconst_1
    //   217: putfield mHasDefinedWeights : Z
    //   220: aload_0
    //   221: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   224: ifnonnull -> 238
    //   227: aload_0
    //   228: new java/util/ArrayList
    //   231: dup
    //   232: invokespecial <init> : ()V
    //   235: putfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   238: aload_0
    //   239: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   242: aload_3
    //   243: invokevirtual add : (Ljava/lang/Object;)Z
    //   246: pop
    //   247: aload_0
    //   248: getfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   251: ifnonnull -> 259
    //   254: aload_0
    //   255: aload_3
    //   256: putfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   259: aload_0
    //   260: getfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   263: astore #4
    //   265: aload #4
    //   267: ifnull -> 281
    //   270: aload #4
    //   272: getfield mListNextMatchConstraintsWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   275: aload_0
    //   276: getfield mOrientation : I
    //   279: aload_3
    //   280: aastore
    //   281: aload_0
    //   282: aload_3
    //   283: putfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   286: aload_2
    //   287: aload_3
    //   288: if_acmpeq -> 301
    //   291: aload_2
    //   292: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   295: aload_0
    //   296: getfield mOrientation : I
    //   299: aload_3
    //   300: aastore
    //   301: aload_3
    //   302: astore_2
    //   303: aload_3
    //   304: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   307: iload_1
    //   308: iconst_1
    //   309: iadd
    //   310: aaload
    //   311: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   314: astore #4
    //   316: aload #4
    //   318: ifnull -> 368
    //   321: aload #4
    //   323: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   326: astore #8
    //   328: aload #8
    //   330: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   333: iload_1
    //   334: aaload
    //   335: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   338: ifnull -> 362
    //   341: aload #8
    //   343: astore #4
    //   345: aload #8
    //   347: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   350: iload_1
    //   351: aaload
    //   352: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   355: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   358: aload_3
    //   359: if_acmpeq -> 371
    //   362: aconst_null
    //   363: astore #4
    //   365: goto -> 371
    //   368: aconst_null
    //   369: astore #4
    //   371: aload #4
    //   373: ifnull -> 382
    //   376: aload #4
    //   378: astore_3
    //   379: goto -> 385
    //   382: iconst_1
    //   383: istore #5
    //   385: goto -> 26
    //   388: aload_0
    //   389: aload_3
    //   390: putfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   393: aload_0
    //   394: getfield mOrientation : I
    //   397: ifne -> 415
    //   400: aload_0
    //   401: getfield mIsRtl : Z
    //   404: ifeq -> 415
    //   407: aload_0
    //   408: aload_3
    //   409: putfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   412: goto -> 423
    //   415: aload_0
    //   416: aload_0
    //   417: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   420: putfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   423: aload_0
    //   424: getfield mHasDefinedWeights : Z
    //   427: ifeq -> 440
    //   430: aload_0
    //   431: getfield mHasUndefinedWeights : Z
    //   434: ifeq -> 440
    //   437: goto -> 443
    //   440: iconst_0
    //   441: istore #6
    //   443: aload_0
    //   444: iload #6
    //   446: putfield mHasComplexMatchWeights : Z
    //   449: return
  }
  
  private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget paramConstraintWidget, int paramInt) {
    boolean bool;
    if (paramConstraintWidget.getVisibility() != 8 && paramConstraintWidget.mListDimensionBehaviors[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 0 || paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 3)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void define() {
    if (!this.mDefined)
      defineChainProperties(); 
    this.mDefined = true;
  }
  
  public ConstraintWidget getFirst() {
    return this.mFirst;
  }
  
  public ConstraintWidget getFirstMatchConstraintWidget() {
    return this.mFirstMatchConstraintWidget;
  }
  
  public ConstraintWidget getFirstVisibleWidget() {
    return this.mFirstVisibleWidget;
  }
  
  public ConstraintWidget getHead() {
    return this.mHead;
  }
  
  public ConstraintWidget getLast() {
    return this.mLast;
  }
  
  public ConstraintWidget getLastMatchConstraintWidget() {
    return this.mLastMatchConstraintWidget;
  }
  
  public ConstraintWidget getLastVisibleWidget() {
    return this.mLastVisibleWidget;
  }
  
  public float getTotalWeight() {
    return this.mTotalWeight;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/ChainHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */