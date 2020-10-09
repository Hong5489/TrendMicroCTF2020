package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflaterCompat {
  private static final boolean DBG_ANIMATOR_INFLATER = false;
  
  private static final int MAX_NUM_POINTS = 100;
  
  private static final String TAG = "AnimatorInflater";
  
  private static final int TOGETHER = 0;
  
  private static final int VALUE_TYPE_COLOR = 3;
  
  private static final int VALUE_TYPE_FLOAT = 0;
  
  private static final int VALUE_TYPE_INT = 1;
  
  private static final int VALUE_TYPE_PATH = 2;
  
  private static final int VALUE_TYPE_UNDEFINED = 4;
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, float paramFloat) throws XmlPullParserException, IOException {
    return createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser), null, 0, paramFloat);
  }
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, AnimatorSet paramAnimatorSet, int paramInt, float paramFloat) throws XmlPullParserException, IOException {
    AnimatorSet animatorSet;
    ArrayList<AnimatorSet> arrayList;
    int i = paramXmlPullParser.getDepth();
    ObjectAnimator objectAnimator = null;
    String str = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        ArrayList<AnimatorSet> arrayList1;
        if (j != 2)
          continue; 
        String str1 = paramXmlPullParser.getName();
        j = 0;
        if (str1.equals("objectAnimator")) {
          objectAnimator = loadObjectAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, paramFloat, paramXmlPullParser);
        } else if (str1.equals("animator")) {
          ValueAnimator valueAnimator = loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, null, paramFloat, paramXmlPullParser);
        } else {
          TypedArray typedArray;
          if (str1.equals("set")) {
            animatorSet = new AnimatorSet();
            typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
            int k = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "ordering", 0, 0);
            createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, paramAttributeSet, animatorSet, k, paramFloat);
            typedArray.recycle();
          } else if (typedArray.equals("propertyValuesHolder")) {
            PropertyValuesHolder[] arrayOfPropertyValuesHolder = loadValues(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser));
            if (arrayOfPropertyValuesHolder != null && animatorSet != null && animatorSet instanceof ValueAnimator)
              ((ValueAnimator)animatorSet).setValues(arrayOfPropertyValuesHolder); 
            j = 1;
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown animator name: ");
            stringBuilder.append(paramXmlPullParser.getName());
            throw new RuntimeException(stringBuilder.toString());
          } 
        } 
        str1 = str;
        if (paramAnimatorSet != null) {
          str1 = str;
          if (j == 0) {
            str1 = str;
            if (str == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(animatorSet);
          } 
        } 
        arrayList = arrayList1;
        continue;
      } 
      break;
    } 
    if (paramAnimatorSet != null && arrayList != null) {
      Animator[] arrayOfAnimator = new Animator[arrayList.size()];
      byte b = 0;
      Iterator<AnimatorSet> iterator = arrayList.iterator();
      while (iterator.hasNext()) {
        arrayOfAnimator[b] = (Animator)iterator.next();
        b++;
      } 
      if (paramInt == 0) {
        paramAnimatorSet.playTogether(arrayOfAnimator);
      } else {
        paramAnimatorSet.playSequentially(arrayOfAnimator);
      } 
    } 
    return (Animator)animatorSet;
  }
  
  private static Keyframe createNewKeyframe(Keyframe paramKeyframe, float paramFloat) {
    if (paramKeyframe.getType() == float.class) {
      paramKeyframe = Keyframe.ofFloat(paramFloat);
    } else if (paramKeyframe.getType() == int.class) {
      paramKeyframe = Keyframe.ofInt(paramFloat);
    } else {
      paramKeyframe = Keyframe.ofObject(paramFloat);
    } 
    return paramKeyframe;
  }
  
  private static void distributeKeyframes(Keyframe[] paramArrayOfKeyframe, float paramFloat, int paramInt1, int paramInt2) {
    paramFloat /= (paramInt2 - paramInt1 + 2);
    while (paramInt1 <= paramInt2) {
      paramArrayOfKeyframe[paramInt1].setFraction(paramArrayOfKeyframe[paramInt1 - 1].getFraction() + paramFloat);
      paramInt1++;
    } 
  }
  
  private static void dumpKeyframes(Object[] paramArrayOfObject, String paramString) {
    if (paramArrayOfObject == null || paramArrayOfObject.length == 0)
      return; 
    Log.d("AnimatorInflater", paramString);
    int i = paramArrayOfObject.length;
    for (byte b = 0; b < i; b++) {
      Float float_;
      Keyframe keyframe = (Keyframe)paramArrayOfObject[b];
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Keyframe ");
      stringBuilder.append(b);
      stringBuilder.append(": fraction ");
      float f = keyframe.getFraction();
      String str = "null";
      if (f < 0.0F) {
        paramString = "null";
      } else {
        float_ = Float.valueOf(keyframe.getFraction());
      } 
      stringBuilder.append(float_);
      stringBuilder.append(", ");
      stringBuilder.append(", value : ");
      Object object = str;
      if (keyframe.hasValue())
        object = keyframe.getValue(); 
      stringBuilder.append(object);
      Log.d("AnimatorInflater", stringBuilder.toString());
    } 
  }
  
  private static PropertyValuesHolder getPVH(TypedArray paramTypedArray, int paramInt1, int paramInt2, int paramInt3, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   5: astore #5
    //   7: aload #5
    //   9: ifnull -> 18
    //   12: iconst_1
    //   13: istore #6
    //   15: goto -> 21
    //   18: iconst_0
    //   19: istore #6
    //   21: iload #6
    //   23: ifeq -> 36
    //   26: aload #5
    //   28: getfield type : I
    //   31: istore #7
    //   33: goto -> 39
    //   36: iconst_0
    //   37: istore #7
    //   39: aload_0
    //   40: iload_3
    //   41: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   44: astore #5
    //   46: aload #5
    //   48: ifnull -> 57
    //   51: iconst_1
    //   52: istore #8
    //   54: goto -> 60
    //   57: iconst_0
    //   58: istore #8
    //   60: iload #8
    //   62: ifeq -> 75
    //   65: aload #5
    //   67: getfield type : I
    //   70: istore #9
    //   72: goto -> 78
    //   75: iconst_0
    //   76: istore #9
    //   78: iload_1
    //   79: iconst_4
    //   80: if_icmpne -> 119
    //   83: iload #6
    //   85: ifeq -> 96
    //   88: iload #7
    //   90: invokestatic isColorType : (I)Z
    //   93: ifne -> 109
    //   96: iload #8
    //   98: ifeq -> 114
    //   101: iload #9
    //   103: invokestatic isColorType : (I)Z
    //   106: ifeq -> 114
    //   109: iconst_3
    //   110: istore_1
    //   111: goto -> 119
    //   114: iconst_0
    //   115: istore_1
    //   116: goto -> 119
    //   119: iload_1
    //   120: ifne -> 129
    //   123: iconst_1
    //   124: istore #10
    //   126: goto -> 132
    //   129: iconst_0
    //   130: istore #10
    //   132: iload_1
    //   133: iconst_2
    //   134: if_icmpne -> 346
    //   137: aload_0
    //   138: iload_2
    //   139: invokevirtual getString : (I)Ljava/lang/String;
    //   142: astore #5
    //   144: aload_0
    //   145: iload_3
    //   146: invokevirtual getString : (I)Ljava/lang/String;
    //   149: astore_0
    //   150: aload #5
    //   152: invokestatic createNodesFromPathData : (Ljava/lang/String;)[Landroid/support/v4/graphics/PathParser$PathDataNode;
    //   155: astore #11
    //   157: aload_0
    //   158: invokestatic createNodesFromPathData : (Ljava/lang/String;)[Landroid/support/v4/graphics/PathParser$PathDataNode;
    //   161: astore #12
    //   163: aload #11
    //   165: ifnonnull -> 179
    //   168: aload #12
    //   170: ifnull -> 176
    //   173: goto -> 179
    //   176: goto -> 338
    //   179: aload #11
    //   181: ifnull -> 308
    //   184: new android/support/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator
    //   187: dup
    //   188: invokespecial <init> : ()V
    //   191: astore #13
    //   193: aload #12
    //   195: ifnull -> 288
    //   198: aload #11
    //   200: aload #12
    //   202: invokestatic canMorph : ([Landroid/support/v4/graphics/PathParser$PathDataNode;[Landroid/support/v4/graphics/PathParser$PathDataNode;)Z
    //   205: ifeq -> 233
    //   208: aload #4
    //   210: aload #13
    //   212: iconst_2
    //   213: anewarray java/lang/Object
    //   216: dup
    //   217: iconst_0
    //   218: aload #11
    //   220: aastore
    //   221: dup
    //   222: iconst_1
    //   223: aload #12
    //   225: aastore
    //   226: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   229: astore_0
    //   230: goto -> 305
    //   233: new java/lang/StringBuilder
    //   236: dup
    //   237: invokespecial <init> : ()V
    //   240: astore #4
    //   242: aload #4
    //   244: ldc_w ' Can't morph from '
    //   247: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   250: pop
    //   251: aload #4
    //   253: aload #5
    //   255: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: pop
    //   259: aload #4
    //   261: ldc_w ' to '
    //   264: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   267: pop
    //   268: aload #4
    //   270: aload_0
    //   271: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   274: pop
    //   275: new android/view/InflateException
    //   278: dup
    //   279: aload #4
    //   281: invokevirtual toString : ()Ljava/lang/String;
    //   284: invokespecial <init> : (Ljava/lang/String;)V
    //   287: athrow
    //   288: aload #4
    //   290: aload #13
    //   292: iconst_1
    //   293: anewarray java/lang/Object
    //   296: dup
    //   297: iconst_0
    //   298: aload #11
    //   300: aastore
    //   301: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   304: astore_0
    //   305: goto -> 340
    //   308: aload #12
    //   310: ifnull -> 338
    //   313: aload #4
    //   315: new android/support/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator
    //   318: dup
    //   319: invokespecial <init> : ()V
    //   322: iconst_1
    //   323: anewarray java/lang/Object
    //   326: dup
    //   327: iconst_0
    //   328: aload #12
    //   330: aastore
    //   331: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   334: astore_0
    //   335: goto -> 340
    //   338: aconst_null
    //   339: astore_0
    //   340: aload_0
    //   341: astore #4
    //   343: goto -> 724
    //   346: aconst_null
    //   347: astore #5
    //   349: iload_1
    //   350: iconst_3
    //   351: if_icmpne -> 359
    //   354: invokestatic getInstance : ()Landroid/support/graphics/drawable/ArgbEvaluator;
    //   357: astore #5
    //   359: iload #10
    //   361: ifeq -> 505
    //   364: iload #6
    //   366: ifeq -> 463
    //   369: iload #7
    //   371: iconst_5
    //   372: if_icmpne -> 386
    //   375: aload_0
    //   376: iload_2
    //   377: fconst_0
    //   378: invokevirtual getDimension : (IF)F
    //   381: fstore #14
    //   383: goto -> 394
    //   386: aload_0
    //   387: iload_2
    //   388: fconst_0
    //   389: invokevirtual getFloat : (IF)F
    //   392: fstore #14
    //   394: iload #8
    //   396: ifeq -> 446
    //   399: iload #9
    //   401: iconst_5
    //   402: if_icmpne -> 416
    //   405: aload_0
    //   406: iload_3
    //   407: fconst_0
    //   408: invokevirtual getDimension : (IF)F
    //   411: fstore #15
    //   413: goto -> 424
    //   416: aload_0
    //   417: iload_3
    //   418: fconst_0
    //   419: invokevirtual getFloat : (IF)F
    //   422: fstore #15
    //   424: aload #4
    //   426: iconst_2
    //   427: newarray float
    //   429: dup
    //   430: iconst_0
    //   431: fload #14
    //   433: fastore
    //   434: dup
    //   435: iconst_1
    //   436: fload #15
    //   438: fastore
    //   439: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   442: astore_0
    //   443: goto -> 502
    //   446: aload #4
    //   448: iconst_1
    //   449: newarray float
    //   451: dup
    //   452: iconst_0
    //   453: fload #14
    //   455: fastore
    //   456: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   459: astore_0
    //   460: goto -> 502
    //   463: iload #9
    //   465: iconst_5
    //   466: if_icmpne -> 480
    //   469: aload_0
    //   470: iload_3
    //   471: fconst_0
    //   472: invokevirtual getDimension : (IF)F
    //   475: fstore #14
    //   477: goto -> 488
    //   480: aload_0
    //   481: iload_3
    //   482: fconst_0
    //   483: invokevirtual getFloat : (IF)F
    //   486: fstore #14
    //   488: aload #4
    //   490: iconst_1
    //   491: newarray float
    //   493: dup
    //   494: iconst_0
    //   495: fload #14
    //   497: fastore
    //   498: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   501: astore_0
    //   502: goto -> 700
    //   505: iload #6
    //   507: ifeq -> 635
    //   510: iload #7
    //   512: iconst_5
    //   513: if_icmpne -> 527
    //   516: aload_0
    //   517: iload_2
    //   518: fconst_0
    //   519: invokevirtual getDimension : (IF)F
    //   522: f2i
    //   523: istore_1
    //   524: goto -> 552
    //   527: iload #7
    //   529: invokestatic isColorType : (I)Z
    //   532: ifeq -> 545
    //   535: aload_0
    //   536: iload_2
    //   537: iconst_0
    //   538: invokevirtual getColor : (II)I
    //   541: istore_1
    //   542: goto -> 552
    //   545: aload_0
    //   546: iload_2
    //   547: iconst_0
    //   548: invokevirtual getInt : (II)I
    //   551: istore_1
    //   552: iload #8
    //   554: ifeq -> 619
    //   557: iload #9
    //   559: iconst_5
    //   560: if_icmpne -> 574
    //   563: aload_0
    //   564: iload_3
    //   565: fconst_0
    //   566: invokevirtual getDimension : (IF)F
    //   569: f2i
    //   570: istore_2
    //   571: goto -> 599
    //   574: iload #9
    //   576: invokestatic isColorType : (I)Z
    //   579: ifeq -> 592
    //   582: aload_0
    //   583: iload_3
    //   584: iconst_0
    //   585: invokevirtual getColor : (II)I
    //   588: istore_2
    //   589: goto -> 599
    //   592: aload_0
    //   593: iload_3
    //   594: iconst_0
    //   595: invokevirtual getInt : (II)I
    //   598: istore_2
    //   599: aload #4
    //   601: iconst_2
    //   602: newarray int
    //   604: dup
    //   605: iconst_0
    //   606: iload_1
    //   607: iastore
    //   608: dup
    //   609: iconst_1
    //   610: iload_2
    //   611: iastore
    //   612: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   615: astore_0
    //   616: goto -> 700
    //   619: aload #4
    //   621: iconst_1
    //   622: newarray int
    //   624: dup
    //   625: iconst_0
    //   626: iload_1
    //   627: iastore
    //   628: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   631: astore_0
    //   632: goto -> 700
    //   635: iload #8
    //   637: ifeq -> 698
    //   640: iload #9
    //   642: iconst_5
    //   643: if_icmpne -> 657
    //   646: aload_0
    //   647: iload_3
    //   648: fconst_0
    //   649: invokevirtual getDimension : (IF)F
    //   652: f2i
    //   653: istore_1
    //   654: goto -> 682
    //   657: iload #9
    //   659: invokestatic isColorType : (I)Z
    //   662: ifeq -> 675
    //   665: aload_0
    //   666: iload_3
    //   667: iconst_0
    //   668: invokevirtual getColor : (II)I
    //   671: istore_1
    //   672: goto -> 682
    //   675: aload_0
    //   676: iload_3
    //   677: iconst_0
    //   678: invokevirtual getInt : (II)I
    //   681: istore_1
    //   682: aload #4
    //   684: iconst_1
    //   685: newarray int
    //   687: dup
    //   688: iconst_0
    //   689: iload_1
    //   690: iastore
    //   691: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   694: astore_0
    //   695: goto -> 700
    //   698: aconst_null
    //   699: astore_0
    //   700: aload_0
    //   701: astore #4
    //   703: aload_0
    //   704: ifnull -> 724
    //   707: aload_0
    //   708: astore #4
    //   710: aload #5
    //   712: ifnull -> 724
    //   715: aload_0
    //   716: aload #5
    //   718: invokevirtual setEvaluator : (Landroid/animation/TypeEvaluator;)V
    //   721: aload_0
    //   722: astore #4
    //   724: aload #4
    //   726: areturn
  }
  
  private static int inferValueTypeFromValues(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    boolean bool2;
    TypedValue typedValue2 = paramTypedArray.peekValue(paramInt1);
    boolean bool1 = true;
    int i = 0;
    if (typedValue2 != null) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 != 0) {
      bool2 = typedValue2.type;
    } else {
      bool2 = false;
    } 
    TypedValue typedValue1 = paramTypedArray.peekValue(paramInt2);
    if (typedValue1 != null) {
      paramInt2 = bool1;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt2 != 0)
      i = typedValue1.type; 
    if ((paramInt1 != 0 && isColorType(bool2)) || (paramInt2 != 0 && isColorType(i))) {
      paramInt1 = 3;
    } else {
      paramInt1 = 0;
    } 
    return paramInt1;
  }
  
  private static int inferValueTypeOfKeyframe(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    byte b = 0;
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null)
      b = 1; 
    if (b && isColorType(typedValue.type)) {
      b = 3;
    } else {
      b = 0;
    } 
    typedArray.recycle();
    return b;
  }
  
  private static boolean isColorType(int paramInt) {
    boolean bool;
    if (paramInt >= 28 && paramInt <= 31) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static Animator loadAnimator(Context paramContext, int paramInt) throws Resources.NotFoundException {
    Animator animator;
    if (Build.VERSION.SDK_INT >= 24) {
      animator = AnimatorInflater.loadAnimator(paramContext, paramInt);
    } else {
      animator = loadAnimator((Context)animator, animator.getResources(), animator.getTheme(), paramInt);
    } 
    return animator;
  }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, int paramInt) throws Resources.NotFoundException {
    return loadAnimator(paramContext, paramResources, paramTheme, paramInt, 1.0F);
  }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, int paramInt, float paramFloat) throws Resources.NotFoundException {
    XmlResourceParser xmlResourceParser1 = null;
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    try {
      XmlResourceParser xmlResourceParser = paramResources.getAnimation(paramInt);
      xmlResourceParser3 = xmlResourceParser;
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      Animator animator = createAnimatorFromXml(paramContext, paramResources, paramTheme, (XmlPullParser)xmlResourceParser, paramFloat);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return animator;
    } catch (XmlPullParserException xmlPullParserException) {
      xmlResourceParser3 = xmlResourceParser2;
      Resources.NotFoundException notFoundException = new Resources.NotFoundException();
      xmlResourceParser3 = xmlResourceParser2;
      StringBuilder stringBuilder = new StringBuilder();
      xmlResourceParser3 = xmlResourceParser2;
      this();
      xmlResourceParser3 = xmlResourceParser2;
      stringBuilder.append("Can't load animation resource ID #0x");
      xmlResourceParser3 = xmlResourceParser2;
      stringBuilder.append(Integer.toHexString(paramInt));
      xmlResourceParser3 = xmlResourceParser2;
      this(stringBuilder.toString());
      xmlResourceParser3 = xmlResourceParser2;
      notFoundException.initCause((Throwable)xmlPullParserException);
      xmlResourceParser3 = xmlResourceParser2;
      throw notFoundException;
    } catch (IOException iOException) {
      xmlResourceParser3 = xmlResourceParser1;
      Resources.NotFoundException notFoundException = new Resources.NotFoundException();
      xmlResourceParser3 = xmlResourceParser1;
      StringBuilder stringBuilder = new StringBuilder();
      xmlResourceParser3 = xmlResourceParser1;
      this();
      xmlResourceParser3 = xmlResourceParser1;
      stringBuilder.append("Can't load animation resource ID #0x");
      xmlResourceParser3 = xmlResourceParser1;
      stringBuilder.append(Integer.toHexString(paramInt));
      xmlResourceParser3 = xmlResourceParser1;
      this(stringBuilder.toString());
      xmlResourceParser3 = xmlResourceParser1;
      notFoundException.initCause(iOException);
      xmlResourceParser3 = xmlResourceParser1;
      throw notFoundException;
    } finally {}
    if (xmlResourceParser3 != null)
      xmlResourceParser3.close(); 
    throw paramContext;
  }
  
  private static ValueAnimator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, ValueAnimator paramValueAnimator, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    TypedArray typedArray2 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR);
    TypedArray typedArray1 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
    ValueAnimator valueAnimator = paramValueAnimator;
    if (paramValueAnimator == null)
      valueAnimator = new ValueAnimator(); 
    parseAnimatorFromTypeArray(valueAnimator, typedArray2, typedArray1, paramFloat, paramXmlPullParser);
    int i = TypedArrayUtils.getNamedResourceId(typedArray2, paramXmlPullParser, "interpolator", 0, 0);
    if (i > 0)
      valueAnimator.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(paramContext, i)); 
    typedArray2.recycle();
    if (typedArray1 != null)
      typedArray1.recycle(); 
    return valueAnimator;
  }
  
  private static Keyframe loadKeyframe(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    Keyframe keyframe;
    boolean bool;
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    paramResources = null;
    float f = TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "fraction", 3, -1.0F);
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = paramInt;
    if (paramInt == 4)
      if (bool && isColorType(typedValue.type)) {
        i = 3;
      } else {
        i = 0;
      }  
    if (bool) {
      if (i != 0) {
        if (i == 1 || i == 3)
          keyframe = Keyframe.ofInt(f, TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "value", 0, 0)); 
      } else {
        keyframe = Keyframe.ofFloat(f, TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "value", 0, 0.0F));
      } 
    } else if (i == 0) {
      keyframe = Keyframe.ofFloat(f);
    } else {
      keyframe = Keyframe.ofInt(f);
    } 
    paramInt = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "interpolator", 1, 0);
    if (paramInt > 0)
      keyframe.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(paramContext, paramInt)); 
    typedArray.recycle();
    return keyframe;
  }
  
  private static ObjectAnimator loadObjectAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    ObjectAnimator objectAnimator = new ObjectAnimator();
    loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, (ValueAnimator)objectAnimator, paramFloat, paramXmlPullParser);
    return objectAnimator;
  }
  
  private static PropertyValuesHolder loadPvh(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, String paramString, int paramInt) throws XmlPullParserException, IOException {
    int j;
    boolean bool = false;
    ArrayList<Keyframe> arrayList = null;
    int i = paramInt;
    while (true) {
      paramInt = paramXmlPullParser.next();
      j = paramInt;
      if (paramInt != 3 && j != 1) {
        if (paramXmlPullParser.getName().equals("keyframe")) {
          if (i == 4)
            i = inferValueTypeOfKeyframe(paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), paramXmlPullParser); 
          Keyframe keyframe = loadKeyframe(paramContext, paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), i, paramXmlPullParser);
          ArrayList<Keyframe> arrayList1 = arrayList;
          if (keyframe != null) {
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(keyframe);
          } 
          paramXmlPullParser.next();
          arrayList = arrayList1;
        } 
        continue;
      } 
      break;
    } 
    if (arrayList != null) {
      paramInt = arrayList.size();
      int k = paramInt;
      if (paramInt > 0) {
        Keyframe keyframe2 = arrayList.get(0);
        Keyframe keyframe1 = arrayList.get(k - 1);
        float f1 = keyframe1.getFraction();
        float f2 = 0.0F;
        paramInt = k;
        if (f1 < 1.0F)
          if (f1 < 0.0F) {
            keyframe1.setFraction(1.0F);
            paramInt = k;
          } else {
            arrayList.add(arrayList.size(), createNewKeyframe(keyframe1, 1.0F));
            paramInt = k + 1;
          }  
        f1 = keyframe2.getFraction();
        int m = paramInt;
        if (f1 != 0.0F)
          if (f1 < 0.0F) {
            keyframe2.setFraction(0.0F);
            m = paramInt;
          } else {
            arrayList.add(0, createNewKeyframe(keyframe2, 0.0F));
            m = paramInt + 1;
          }  
        Keyframe[] arrayOfKeyframe = new Keyframe[m];
        arrayList.toArray(arrayOfKeyframe);
        k = 0;
        paramInt = j;
        while (k < m) {
          keyframe2 = arrayOfKeyframe[k];
          if (keyframe2.getFraction() < f2)
            if (k == 0) {
              keyframe2.setFraction(f2);
            } else if (k == m - 1) {
              keyframe2.setFraction(1.0F);
              f2 = 0.0F;
            } else {
              int n = k + 1;
              int i1 = k;
              j = paramInt;
              for (paramInt = n; paramInt < m - 1 && arrayOfKeyframe[paramInt].getFraction() < 0.0F; paramInt++)
                i1 = paramInt; 
              f2 = 0.0F;
              distributeKeyframes(arrayOfKeyframe, arrayOfKeyframe[i1 + 1].getFraction() - arrayOfKeyframe[k - 1].getFraction(), k, i1);
              paramInt = j;
            }  
          k++;
        } 
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe(paramString, arrayOfKeyframe);
        PropertyValuesHolder propertyValuesHolder1 = propertyValuesHolder2;
        if (i == 3) {
          propertyValuesHolder2.setEvaluator(ArgbEvaluator.getInstance());
          propertyValuesHolder1 = propertyValuesHolder2;
        } 
        return propertyValuesHolder1;
      } 
    } 
    return null;
  }
  
  private static PropertyValuesHolder[] loadValues(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder;
    ArrayList<PropertyValuesHolder> arrayList;
    String str = null;
    while (true) {
      int i = paramXmlPullParser.getEventType();
      if (i != 3 && i != 1) {
        if (i != 2) {
          paramXmlPullParser.next();
          continue;
        } 
        if (paramXmlPullParser.getName().equals("propertyValuesHolder")) {
          ArrayList<PropertyValuesHolder> arrayList1;
          TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
          String str1 = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "propertyName", 3);
          i = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "valueType", 2, 4);
          PropertyValuesHolder propertyValuesHolder = loadPvh(paramContext, paramResources, paramTheme, paramXmlPullParser, str1, i);
          if (propertyValuesHolder == null)
            propertyValuesHolder = getPVH(typedArray, i, 0, 1, str1); 
          str1 = str;
          if (propertyValuesHolder != null) {
            str1 = str;
            if (str == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(propertyValuesHolder);
          } 
          typedArray.recycle();
          arrayList = arrayList1;
        } 
        paramXmlPullParser.next();
        continue;
      } 
      break;
    } 
    paramContext = null;
    if (arrayList != null) {
      int i = arrayList.size();
      PropertyValuesHolder[] arrayOfPropertyValuesHolder1 = new PropertyValuesHolder[i];
      byte b = 0;
      while (true) {
        arrayOfPropertyValuesHolder = arrayOfPropertyValuesHolder1;
        if (b < i) {
          arrayOfPropertyValuesHolder1[b] = arrayList.get(b);
          b++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfPropertyValuesHolder;
  }
  
  private static void parseAnimatorFromTypeArray(ValueAnimator paramValueAnimator, TypedArray paramTypedArray1, TypedArray paramTypedArray2, float paramFloat, XmlPullParser paramXmlPullParser) {
    long l1 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "duration", 1, 300);
    long l2 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "startOffset", 2, 0);
    int i = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "valueType", 7, 4);
    int j = i;
    if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueFrom")) {
      j = i;
      if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueTo")) {
        int k = i;
        if (i == 4)
          k = inferValueTypeFromValues(paramTypedArray1, 5, 6); 
        PropertyValuesHolder propertyValuesHolder = getPVH(paramTypedArray1, k, 5, 6, "");
        j = k;
        if (propertyValuesHolder != null) {
          paramValueAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder });
          j = k;
        } 
      } 
    } 
    paramValueAnimator.setDuration(l1);
    paramValueAnimator.setStartDelay(l2);
    paramValueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatCount", 3, 0));
    paramValueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatMode", 4, 1));
    if (paramTypedArray2 != null)
      setupObjectAnimator(paramValueAnimator, paramTypedArray2, j, paramFloat, paramXmlPullParser); 
  }
  
  private static void setupObjectAnimator(ValueAnimator paramValueAnimator, TypedArray paramTypedArray, int paramInt, float paramFloat, XmlPullParser paramXmlPullParser) {
    // Byte code:
    //   0: aload_0
    //   1: checkcast android/animation/ObjectAnimator
    //   4: astore_0
    //   5: aload_1
    //   6: aload #4
    //   8: ldc_w 'pathData'
    //   11: iconst_1
    //   12: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   15: astore #5
    //   17: aload #5
    //   19: ifnull -> 127
    //   22: aload_1
    //   23: aload #4
    //   25: ldc_w 'propertyXName'
    //   28: iconst_2
    //   29: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   32: astore #6
    //   34: aload_1
    //   35: aload #4
    //   37: ldc_w 'propertyYName'
    //   40: iconst_3
    //   41: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   44: astore #4
    //   46: iload_2
    //   47: iconst_2
    //   48: if_icmpeq -> 56
    //   51: iload_2
    //   52: iconst_4
    //   53: if_icmpne -> 56
    //   56: aload #6
    //   58: ifnonnull -> 106
    //   61: aload #4
    //   63: ifnull -> 69
    //   66: goto -> 106
    //   69: new java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial <init> : ()V
    //   76: astore_0
    //   77: aload_0
    //   78: aload_1
    //   79: invokevirtual getPositionDescription : ()Ljava/lang/String;
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_0
    //   87: ldc_w ' propertyXName or propertyYName is needed for PathData'
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: new android/view/InflateException
    //   97: dup
    //   98: aload_0
    //   99: invokevirtual toString : ()Ljava/lang/String;
    //   102: invokespecial <init> : (Ljava/lang/String;)V
    //   105: athrow
    //   106: aload #5
    //   108: invokestatic createPathFromPathData : (Ljava/lang/String;)Landroid/graphics/Path;
    //   111: aload_0
    //   112: ldc_w 0.5
    //   115: fload_3
    //   116: fmul
    //   117: aload #6
    //   119: aload #4
    //   121: invokestatic setupPathMotion : (Landroid/graphics/Path;Landroid/animation/ObjectAnimator;FLjava/lang/String;Ljava/lang/String;)V
    //   124: goto -> 141
    //   127: aload_0
    //   128: aload_1
    //   129: aload #4
    //   131: ldc_w 'propertyName'
    //   134: iconst_0
    //   135: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   138: invokevirtual setPropertyName : (Ljava/lang/String;)V
    //   141: return
  }
  
  private static void setupPathMotion(Path paramPath, ObjectAnimator paramObjectAnimator, float paramFloat, String paramString1, String paramString2) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    float f = 0.0F;
    ArrayList<Float> arrayList = new ArrayList();
    arrayList.add(Float.valueOf(0.0F));
    while (true) {
      f += pathMeasure.getLength();
      arrayList.add(Float.valueOf(f));
      if (!pathMeasure.nextContour()) {
        PropertyValuesHolder propertyValuesHolder1;
        PropertyValuesHolder propertyValuesHolder2;
        PathMeasure pathMeasure1 = new PathMeasure(paramPath, false);
        int i = Math.min(100, (int)(f / paramFloat) + 1);
        float[] arrayOfFloat2 = new float[i];
        float[] arrayOfFloat1 = new float[i];
        float[] arrayOfFloat3 = new float[2];
        int j = 0;
        f /= (i - 1);
        paramFloat = 0.0F;
        byte b = 0;
        while (b < i) {
          pathMeasure1.getPosTan(paramFloat - ((Float)arrayList.get(j)).floatValue(), arrayOfFloat3, null);
          arrayOfFloat2[b] = arrayOfFloat3[0];
          arrayOfFloat1[b] = arrayOfFloat3[1];
          paramFloat += f;
          int k = j;
          if (j + 1 < arrayList.size()) {
            k = j;
            if (paramFloat > ((Float)arrayList.get(j + 1)).floatValue()) {
              k = j + 1;
              pathMeasure1.nextContour();
            } 
          } 
          b++;
          j = k;
        } 
        pathMeasure1 = null;
        arrayList = null;
        if (paramString1 != null)
          propertyValuesHolder1 = PropertyValuesHolder.ofFloat(paramString1, arrayOfFloat2); 
        ArrayList<Float> arrayList1 = arrayList;
        if (paramString2 != null)
          propertyValuesHolder2 = PropertyValuesHolder.ofFloat(paramString2, arrayOfFloat1); 
        if (propertyValuesHolder1 == null) {
          paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder2 });
        } else if (propertyValuesHolder2 == null) {
          paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder1 });
        } else {
          paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder1, propertyValuesHolder2 });
        } 
        return;
      } 
    } 
  }
  
  private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]> {
    private PathParser.PathDataNode[] mNodeArray;
    
    PathDataEvaluator() {}
    
    PathDataEvaluator(PathParser.PathDataNode[] param1ArrayOfPathDataNode) {
      this.mNodeArray = param1ArrayOfPathDataNode;
    }
    
    public PathParser.PathDataNode[] evaluate(float param1Float, PathParser.PathDataNode[] param1ArrayOfPathDataNode1, PathParser.PathDataNode[] param1ArrayOfPathDataNode2) {
      if (PathParser.canMorph(param1ArrayOfPathDataNode1, param1ArrayOfPathDataNode2)) {
        PathParser.PathDataNode[] arrayOfPathDataNode = this.mNodeArray;
        if (arrayOfPathDataNode == null || !PathParser.canMorph(arrayOfPathDataNode, param1ArrayOfPathDataNode1))
          this.mNodeArray = PathParser.deepCopyNodes(param1ArrayOfPathDataNode1); 
        for (byte b = 0; b < param1ArrayOfPathDataNode1.length; b++)
          this.mNodeArray[b].interpolatePathDataNode(param1ArrayOfPathDataNode1[b], param1ArrayOfPathDataNode2[b], param1Float); 
        return this.mNodeArray;
      } 
      throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/graphics/drawable/AnimatorInflaterCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */