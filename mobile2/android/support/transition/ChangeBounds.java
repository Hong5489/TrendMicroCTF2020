package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import org.xmlpull.v1.XmlPullParser;

public class ChangeBounds extends Transition {
  private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
  
  private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
  
  private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY;
  
  private static final Property<View, PointF> POSITION_PROPERTY;
  
  private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
  
  private static final String PROPNAME_CLIP = "android:changeBounds:clip";
  
  private static final String PROPNAME_PARENT = "android:changeBounds:parent";
  
  private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
  
  private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
  
  private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
  
  private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
  
  private static RectEvaluator sRectEvaluator;
  
  private static final String[] sTransitionProperties = new String[] { "android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY" };
  
  private boolean mReparent = false;
  
  private boolean mResizeClip = false;
  
  private int[] mTempLocation = new int[2];
  
  static {
    DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin") {
        private Rect mBounds = new Rect();
        
        public PointF get(Drawable param1Drawable) {
          param1Drawable.copyBounds(this.mBounds);
          return new PointF(this.mBounds.left, this.mBounds.top);
        }
        
        public void set(Drawable param1Drawable, PointF param1PointF) {
          param1Drawable.copyBounds(this.mBounds);
          this.mBounds.offsetTo(Math.round(param1PointF.x), Math.round(param1PointF.y));
          param1Drawable.setBounds(this.mBounds);
        }
      };
    TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft") {
        public PointF get(ChangeBounds.ViewBounds param1ViewBounds) {
          return null;
        }
        
        public void set(ChangeBounds.ViewBounds param1ViewBounds, PointF param1PointF) {
          param1ViewBounds.setTopLeft(param1PointF);
        }
      };
    BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight") {
        public PointF get(ChangeBounds.ViewBounds param1ViewBounds) {
          return null;
        }
        
        public void set(ChangeBounds.ViewBounds param1ViewBounds, PointF param1PointF) {
          param1ViewBounds.setBottomRight(param1PointF);
        }
      };
    BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          ViewUtils.setLeftTopRightBottom(param1View, param1View.getLeft(), param1View.getTop(), Math.round(param1PointF.x), Math.round(param1PointF.y));
        }
      };
    TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          ViewUtils.setLeftTopRightBottom(param1View, Math.round(param1PointF.x), Math.round(param1PointF.y), param1View.getRight(), param1View.getBottom());
        }
      };
    POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          int i = Math.round(param1PointF.x);
          int j = Math.round(param1PointF.y);
          ViewUtils.setLeftTopRightBottom(param1View, i, j, param1View.getWidth() + i, param1View.getHeight() + j);
        }
      };
    sRectEvaluator = new RectEvaluator();
  }
  
  public ChangeBounds() {}
  
  public ChangeBounds(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.CHANGE_BOUNDS);
    boolean bool = TypedArrayUtils.getNamedBoolean(typedArray, (XmlPullParser)paramAttributeSet, "resizeClip", 0, false);
    typedArray.recycle();
    setResizeClip(bool);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
      paramTransitionValues.values.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
      paramTransitionValues.values.put("android:changeBounds:parent", paramTransitionValues.view.getParent());
      if (this.mReparent) {
        paramTransitionValues.view.getLocationInWindow(this.mTempLocation);
        paramTransitionValues.values.put("android:changeBounds:windowX", Integer.valueOf(this.mTempLocation[0]));
        paramTransitionValues.values.put("android:changeBounds:windowY", Integer.valueOf(this.mTempLocation[1]));
      } 
      if (this.mResizeClip)
        paramTransitionValues.values.put("android:changeBounds:clip", ViewCompat.getClipBounds(view)); 
    } 
  }
  
  private boolean parentMatches(View paramView1, View paramView2) {
    boolean bool = true;
    if (this.mReparent) {
      boolean bool1 = true;
      bool = true;
      TransitionValues transitionValues = getMatchedTransitionValues(paramView1, true);
      if (transitionValues == null) {
        if (paramView1 != paramView2)
          bool = false; 
      } else if (paramView2 == transitionValues.view) {
        bool = bool1;
      } else {
        bool = false;
      } 
    } 
    return bool;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    // Byte code:
    //   0: aload_2
    //   1: ifnull -> 1117
    //   4: aload_3
    //   5: ifnonnull -> 11
    //   8: goto -> 1117
    //   11: aload_2
    //   12: getfield values : Ljava/util/Map;
    //   15: astore #4
    //   17: aload_3
    //   18: getfield values : Ljava/util/Map;
    //   21: astore #5
    //   23: aload #4
    //   25: ldc 'android:changeBounds:parent'
    //   27: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   32: checkcast android/view/ViewGroup
    //   35: astore #4
    //   37: aload #5
    //   39: ldc 'android:changeBounds:parent'
    //   41: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   46: checkcast android/view/ViewGroup
    //   49: astore #6
    //   51: aload #4
    //   53: ifnull -> 1115
    //   56: aload #6
    //   58: ifnonnull -> 64
    //   61: goto -> 1115
    //   64: aload_3
    //   65: getfield view : Landroid/view/View;
    //   68: astore #5
    //   70: aload_0
    //   71: aload #4
    //   73: aload #6
    //   75: invokespecial parentMatches : (Landroid/view/View;Landroid/view/View;)Z
    //   78: ifeq -> 863
    //   81: aload_2
    //   82: getfield values : Ljava/util/Map;
    //   85: ldc 'android:changeBounds:bounds'
    //   87: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   92: checkcast android/graphics/Rect
    //   95: astore #4
    //   97: aload_3
    //   98: getfield values : Ljava/util/Map;
    //   101: ldc 'android:changeBounds:bounds'
    //   103: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   108: checkcast android/graphics/Rect
    //   111: astore_1
    //   112: aload #4
    //   114: getfield left : I
    //   117: istore #7
    //   119: aload_1
    //   120: getfield left : I
    //   123: istore #8
    //   125: aload #4
    //   127: getfield top : I
    //   130: istore #9
    //   132: aload_1
    //   133: getfield top : I
    //   136: istore #10
    //   138: aload #4
    //   140: getfield right : I
    //   143: istore #11
    //   145: aload_1
    //   146: getfield right : I
    //   149: istore #12
    //   151: aload #4
    //   153: getfield bottom : I
    //   156: istore #13
    //   158: aload_1
    //   159: getfield bottom : I
    //   162: istore #14
    //   164: iload #11
    //   166: iload #7
    //   168: isub
    //   169: istore #15
    //   171: iload #13
    //   173: iload #9
    //   175: isub
    //   176: istore #16
    //   178: iload #12
    //   180: iload #8
    //   182: isub
    //   183: istore #17
    //   185: iload #14
    //   187: iload #10
    //   189: isub
    //   190: istore #18
    //   192: aload_2
    //   193: getfield values : Ljava/util/Map;
    //   196: ldc 'android:changeBounds:clip'
    //   198: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   203: checkcast android/graphics/Rect
    //   206: astore_2
    //   207: aload_3
    //   208: getfield values : Ljava/util/Map;
    //   211: ldc 'android:changeBounds:clip'
    //   213: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   218: checkcast android/graphics/Rect
    //   221: astore #6
    //   223: iconst_0
    //   224: istore #19
    //   226: iconst_0
    //   227: istore #20
    //   229: iload #15
    //   231: ifeq -> 239
    //   234: iload #16
    //   236: ifne -> 257
    //   239: iload #19
    //   241: istore #21
    //   243: iload #17
    //   245: ifeq -> 300
    //   248: iload #19
    //   250: istore #21
    //   252: iload #18
    //   254: ifeq -> 300
    //   257: iload #7
    //   259: iload #8
    //   261: if_icmpne -> 271
    //   264: iload #9
    //   266: iload #10
    //   268: if_icmpeq -> 276
    //   271: iconst_0
    //   272: iconst_1
    //   273: iadd
    //   274: istore #20
    //   276: iload #11
    //   278: iload #12
    //   280: if_icmpne -> 294
    //   283: iload #20
    //   285: istore #21
    //   287: iload #13
    //   289: iload #14
    //   291: if_icmpeq -> 300
    //   294: iload #20
    //   296: iconst_1
    //   297: iadd
    //   298: istore #21
    //   300: aload_2
    //   301: ifnull -> 313
    //   304: aload_2
    //   305: aload #6
    //   307: invokevirtual equals : (Ljava/lang/Object;)Z
    //   310: ifeq -> 330
    //   313: iload #21
    //   315: istore #20
    //   317: aload_2
    //   318: ifnonnull -> 336
    //   321: iload #21
    //   323: istore #20
    //   325: aload #6
    //   327: ifnull -> 336
    //   330: iload #21
    //   332: iconst_1
    //   333: iadd
    //   334: istore #20
    //   336: iload #20
    //   338: ifle -> 860
    //   341: aload_0
    //   342: getfield mResizeClip : Z
    //   345: ifne -> 607
    //   348: aload #5
    //   350: iload #7
    //   352: iload #9
    //   354: iload #11
    //   356: iload #13
    //   358: invokestatic setLeftTopRightBottom : (Landroid/view/View;IIII)V
    //   361: iload #20
    //   363: iconst_2
    //   364: if_icmpne -> 524
    //   367: iload #15
    //   369: iload #17
    //   371: if_icmpne -> 414
    //   374: iload #16
    //   376: iload #18
    //   378: if_icmpne -> 414
    //   381: aload_0
    //   382: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   385: iload #7
    //   387: i2f
    //   388: iload #9
    //   390: i2f
    //   391: iload #8
    //   393: i2f
    //   394: iload #10
    //   396: i2f
    //   397: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   400: astore_1
    //   401: aload #5
    //   403: getstatic android/support/transition/ChangeBounds.POSITION_PROPERTY : Landroid/util/Property;
    //   406: aload_1
    //   407: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   410: astore_1
    //   411: goto -> 819
    //   414: new android/support/transition/ChangeBounds$ViewBounds
    //   417: dup
    //   418: aload #5
    //   420: invokespecial <init> : (Landroid/view/View;)V
    //   423: astore_2
    //   424: aload_0
    //   425: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   428: iload #7
    //   430: i2f
    //   431: iload #9
    //   433: i2f
    //   434: iload #8
    //   436: i2f
    //   437: iload #10
    //   439: i2f
    //   440: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   443: astore_1
    //   444: aload_2
    //   445: getstatic android/support/transition/ChangeBounds.TOP_LEFT_PROPERTY : Landroid/util/Property;
    //   448: aload_1
    //   449: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   452: astore_3
    //   453: aload_0
    //   454: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   457: iload #11
    //   459: i2f
    //   460: iload #13
    //   462: i2f
    //   463: iload #12
    //   465: i2f
    //   466: iload #14
    //   468: i2f
    //   469: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   472: astore_1
    //   473: aload_2
    //   474: getstatic android/support/transition/ChangeBounds.BOTTOM_RIGHT_PROPERTY : Landroid/util/Property;
    //   477: aload_1
    //   478: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   481: astore #4
    //   483: new android/animation/AnimatorSet
    //   486: dup
    //   487: invokespecial <init> : ()V
    //   490: astore_1
    //   491: aload_1
    //   492: iconst_2
    //   493: anewarray android/animation/Animator
    //   496: dup
    //   497: iconst_0
    //   498: aload_3
    //   499: aastore
    //   500: dup
    //   501: iconst_1
    //   502: aload #4
    //   504: aastore
    //   505: invokevirtual playTogether : ([Landroid/animation/Animator;)V
    //   508: aload_1
    //   509: new android/support/transition/ChangeBounds$7
    //   512: dup
    //   513: aload_0
    //   514: aload_2
    //   515: invokespecial <init> : (Landroid/support/transition/ChangeBounds;Landroid/support/transition/ChangeBounds$ViewBounds;)V
    //   518: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   521: goto -> 819
    //   524: iload #7
    //   526: iload #8
    //   528: if_icmpne -> 574
    //   531: iload #9
    //   533: iload #10
    //   535: if_icmpeq -> 541
    //   538: goto -> 574
    //   541: aload_0
    //   542: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   545: iload #11
    //   547: i2f
    //   548: iload #13
    //   550: i2f
    //   551: iload #12
    //   553: i2f
    //   554: iload #14
    //   556: i2f
    //   557: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   560: astore_1
    //   561: aload #5
    //   563: getstatic android/support/transition/ChangeBounds.BOTTOM_RIGHT_ONLY_PROPERTY : Landroid/util/Property;
    //   566: aload_1
    //   567: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   570: astore_1
    //   571: goto -> 819
    //   574: aload_0
    //   575: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   578: iload #7
    //   580: i2f
    //   581: iload #9
    //   583: i2f
    //   584: iload #8
    //   586: i2f
    //   587: iload #10
    //   589: i2f
    //   590: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   593: astore_1
    //   594: aload #5
    //   596: getstatic android/support/transition/ChangeBounds.TOP_LEFT_ONLY_PROPERTY : Landroid/util/Property;
    //   599: aload_1
    //   600: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   603: astore_1
    //   604: goto -> 819
    //   607: aload #5
    //   609: astore #4
    //   611: iload #15
    //   613: iload #17
    //   615: invokestatic max : (II)I
    //   618: istore #21
    //   620: aload #4
    //   622: iload #7
    //   624: iload #9
    //   626: iload #7
    //   628: iload #21
    //   630: iadd
    //   631: iload #9
    //   633: iload #16
    //   635: iload #18
    //   637: invokestatic max : (II)I
    //   640: iadd
    //   641: invokestatic setLeftTopRightBottom : (Landroid/view/View;IIII)V
    //   644: iload #7
    //   646: iload #8
    //   648: if_icmpne -> 666
    //   651: iload #9
    //   653: iload #10
    //   655: if_icmpeq -> 661
    //   658: goto -> 666
    //   661: aconst_null
    //   662: astore_1
    //   663: goto -> 696
    //   666: aload_0
    //   667: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   670: iload #7
    //   672: i2f
    //   673: iload #9
    //   675: i2f
    //   676: iload #8
    //   678: i2f
    //   679: iload #10
    //   681: i2f
    //   682: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   685: astore_1
    //   686: aload #4
    //   688: getstatic android/support/transition/ChangeBounds.POSITION_PROPERTY : Landroid/util/Property;
    //   691: aload_1
    //   692: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   695: astore_1
    //   696: aload_2
    //   697: ifnonnull -> 717
    //   700: new android/graphics/Rect
    //   703: dup
    //   704: iconst_0
    //   705: iconst_0
    //   706: iload #15
    //   708: iload #16
    //   710: invokespecial <init> : (IIII)V
    //   713: astore_2
    //   714: goto -> 717
    //   717: aload #6
    //   719: ifnonnull -> 739
    //   722: new android/graphics/Rect
    //   725: dup
    //   726: iconst_0
    //   727: iconst_0
    //   728: iload #17
    //   730: iload #18
    //   732: invokespecial <init> : (IIII)V
    //   735: astore_3
    //   736: goto -> 742
    //   739: aload #6
    //   741: astore_3
    //   742: aconst_null
    //   743: astore #22
    //   745: aload_2
    //   746: aload_3
    //   747: invokevirtual equals : (Ljava/lang/Object;)Z
    //   750: ifne -> 810
    //   753: aload #4
    //   755: aload_2
    //   756: invokestatic setClipBounds : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   759: aload #4
    //   761: ldc_w 'clipBounds'
    //   764: getstatic android/support/transition/ChangeBounds.sRectEvaluator : Landroid/support/transition/RectEvaluator;
    //   767: iconst_2
    //   768: anewarray java/lang/Object
    //   771: dup
    //   772: iconst_0
    //   773: aload_2
    //   774: aastore
    //   775: dup
    //   776: iconst_1
    //   777: aload_3
    //   778: aastore
    //   779: invokestatic ofObject : (Ljava/lang/Object;Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/ObjectAnimator;
    //   782: astore_2
    //   783: aload_2
    //   784: new android/support/transition/ChangeBounds$8
    //   787: dup
    //   788: aload_0
    //   789: aload #4
    //   791: aload #6
    //   793: iload #8
    //   795: iload #10
    //   797: iload #12
    //   799: iload #14
    //   801: invokespecial <init> : (Landroid/support/transition/ChangeBounds;Landroid/view/View;Landroid/graphics/Rect;IIII)V
    //   804: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   807: goto -> 813
    //   810: aload #22
    //   812: astore_2
    //   813: aload_1
    //   814: aload_2
    //   815: invokestatic mergeAnimators : (Landroid/animation/Animator;Landroid/animation/Animator;)Landroid/animation/Animator;
    //   818: astore_1
    //   819: aload #5
    //   821: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   824: instanceof android/view/ViewGroup
    //   827: ifeq -> 858
    //   830: aload #5
    //   832: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   835: checkcast android/view/ViewGroup
    //   838: astore_2
    //   839: aload_2
    //   840: iconst_1
    //   841: invokestatic suppressLayout : (Landroid/view/ViewGroup;Z)V
    //   844: aload_0
    //   845: new android/support/transition/ChangeBounds$9
    //   848: dup
    //   849: aload_0
    //   850: aload_2
    //   851: invokespecial <init> : (Landroid/support/transition/ChangeBounds;Landroid/view/ViewGroup;)V
    //   854: invokevirtual addListener : (Landroid/support/transition/Transition$TransitionListener;)Landroid/support/transition/Transition;
    //   857: pop
    //   858: aload_1
    //   859: areturn
    //   860: goto -> 956
    //   863: aload_2
    //   864: getfield values : Ljava/util/Map;
    //   867: ldc 'android:changeBounds:windowX'
    //   869: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   874: checkcast java/lang/Integer
    //   877: invokevirtual intValue : ()I
    //   880: istore #9
    //   882: aload_2
    //   883: getfield values : Ljava/util/Map;
    //   886: ldc 'android:changeBounds:windowY'
    //   888: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   893: checkcast java/lang/Integer
    //   896: invokevirtual intValue : ()I
    //   899: istore #20
    //   901: aload_3
    //   902: getfield values : Ljava/util/Map;
    //   905: ldc 'android:changeBounds:windowX'
    //   907: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   912: checkcast java/lang/Integer
    //   915: invokevirtual intValue : ()I
    //   918: istore #19
    //   920: aload_3
    //   921: getfield values : Ljava/util/Map;
    //   924: ldc 'android:changeBounds:windowY'
    //   926: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   931: checkcast java/lang/Integer
    //   934: invokevirtual intValue : ()I
    //   937: istore #21
    //   939: iload #9
    //   941: iload #19
    //   943: if_icmpne -> 958
    //   946: iload #20
    //   948: iload #21
    //   950: if_icmpeq -> 956
    //   953: goto -> 958
    //   956: aconst_null
    //   957: areturn
    //   958: aload_1
    //   959: aload_0
    //   960: getfield mTempLocation : [I
    //   963: invokevirtual getLocationInWindow : ([I)V
    //   966: aload #5
    //   968: invokevirtual getWidth : ()I
    //   971: aload #5
    //   973: invokevirtual getHeight : ()I
    //   976: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
    //   979: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   982: astore_2
    //   983: aload #5
    //   985: new android/graphics/Canvas
    //   988: dup
    //   989: aload_2
    //   990: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   993: invokevirtual draw : (Landroid/graphics/Canvas;)V
    //   996: new android/graphics/drawable/BitmapDrawable
    //   999: dup
    //   1000: aload_2
    //   1001: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   1004: astore_2
    //   1005: aload #5
    //   1007: invokestatic getTransitionAlpha : (Landroid/view/View;)F
    //   1010: fstore #23
    //   1012: aload #5
    //   1014: fconst_0
    //   1015: invokestatic setTransitionAlpha : (Landroid/view/View;F)V
    //   1018: aload_1
    //   1019: invokestatic getOverlay : (Landroid/view/View;)Landroid/support/transition/ViewOverlayImpl;
    //   1022: aload_2
    //   1023: invokeinterface add : (Landroid/graphics/drawable/Drawable;)V
    //   1028: aload_0
    //   1029: invokevirtual getPathMotion : ()Landroid/support/transition/PathMotion;
    //   1032: astore_3
    //   1033: aload_0
    //   1034: getfield mTempLocation : [I
    //   1037: astore #4
    //   1039: aload_3
    //   1040: iload #9
    //   1042: aload #4
    //   1044: iconst_0
    //   1045: iaload
    //   1046: isub
    //   1047: i2f
    //   1048: iload #20
    //   1050: aload #4
    //   1052: iconst_1
    //   1053: iaload
    //   1054: isub
    //   1055: i2f
    //   1056: iload #19
    //   1058: aload #4
    //   1060: iconst_0
    //   1061: iaload
    //   1062: isub
    //   1063: i2f
    //   1064: iload #21
    //   1066: aload #4
    //   1068: iconst_1
    //   1069: iaload
    //   1070: isub
    //   1071: i2f
    //   1072: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   1075: astore_3
    //   1076: aload_2
    //   1077: iconst_1
    //   1078: anewarray android/animation/PropertyValuesHolder
    //   1081: dup
    //   1082: iconst_0
    //   1083: getstatic android/support/transition/ChangeBounds.DRAWABLE_ORIGIN_PROPERTY : Landroid/util/Property;
    //   1086: aload_3
    //   1087: invokestatic ofPointF : (Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;
    //   1090: aastore
    //   1091: invokestatic ofPropertyValuesHolder : (Ljava/lang/Object;[Landroid/animation/PropertyValuesHolder;)Landroid/animation/ObjectAnimator;
    //   1094: astore_3
    //   1095: aload_3
    //   1096: new android/support/transition/ChangeBounds$10
    //   1099: dup
    //   1100: aload_0
    //   1101: aload_1
    //   1102: aload_2
    //   1103: aload #5
    //   1105: fload #23
    //   1107: invokespecial <init> : (Landroid/support/transition/ChangeBounds;Landroid/view/ViewGroup;Landroid/graphics/drawable/BitmapDrawable;Landroid/view/View;F)V
    //   1110: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   1113: aload_3
    //   1114: areturn
    //   1115: aconst_null
    //   1116: areturn
    //   1117: aconst_null
    //   1118: areturn
  }
  
  public boolean getResizeClip() {
    return this.mResizeClip;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public void setResizeClip(boolean paramBoolean) {
    this.mResizeClip = paramBoolean;
  }
  
  private static class ViewBounds {
    private int mBottom;
    
    private int mBottomRightCalls;
    
    private int mLeft;
    
    private int mRight;
    
    private int mTop;
    
    private int mTopLeftCalls;
    
    private View mView;
    
    ViewBounds(View param1View) {
      this.mView = param1View;
    }
    
    private void setLeftTopRightBottom() {
      ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
      this.mTopLeftCalls = 0;
      this.mBottomRightCalls = 0;
    }
    
    void setBottomRight(PointF param1PointF) {
      this.mRight = Math.round(param1PointF.x);
      this.mBottom = Math.round(param1PointF.y);
      int i = this.mBottomRightCalls + 1;
      this.mBottomRightCalls = i;
      if (this.mTopLeftCalls == i)
        setLeftTopRightBottom(); 
    }
    
    void setTopLeft(PointF param1PointF) {
      this.mLeft = Math.round(param1PointF.x);
      this.mTop = Math.round(param1PointF.y);
      int i = this.mTopLeftCalls + 1;
      this.mTopLeftCalls = i;
      if (i == this.mBottomRightCalls)
        setLeftTopRightBottom(); 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/ChangeBounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */