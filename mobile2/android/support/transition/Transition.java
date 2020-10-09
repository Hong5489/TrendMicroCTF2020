package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParser;

public abstract class Transition implements Cloneable {
  static final boolean DBG = false;
  
  private static final int[] DEFAULT_MATCH_ORDER = new int[] { 2, 1, 3, 4 };
  
  private static final String LOG_TAG = "Transition";
  
  private static final int MATCH_FIRST = 1;
  
  public static final int MATCH_ID = 3;
  
  private static final String MATCH_ID_STR = "id";
  
  public static final int MATCH_INSTANCE = 1;
  
  private static final String MATCH_INSTANCE_STR = "instance";
  
  public static final int MATCH_ITEM_ID = 4;
  
  private static final String MATCH_ITEM_ID_STR = "itemId";
  
  private static final int MATCH_LAST = 4;
  
  public static final int MATCH_NAME = 2;
  
  private static final String MATCH_NAME_STR = "name";
  
  private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
      public Path getPath(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
        Path path = new Path();
        path.moveTo(param1Float1, param1Float2);
        path.lineTo(param1Float3, param1Float4);
        return path;
      }
    };
  
  private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<>();
  
  private ArrayList<Animator> mAnimators = new ArrayList<>();
  
  boolean mCanRemoveViews = false;
  
  ArrayList<Animator> mCurrentAnimators = new ArrayList<>();
  
  long mDuration = -1L;
  
  private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
  
  private ArrayList<TransitionValues> mEndValuesList;
  
  private boolean mEnded = false;
  
  private EpicenterCallback mEpicenterCallback;
  
  private TimeInterpolator mInterpolator = null;
  
  private ArrayList<TransitionListener> mListeners = null;
  
  private int[] mMatchOrder = DEFAULT_MATCH_ORDER;
  
  private String mName = getClass().getName();
  
  private ArrayMap<String, String> mNameOverrides;
  
  private int mNumInstances = 0;
  
  TransitionSet mParent = null;
  
  private PathMotion mPathMotion = STRAIGHT_PATH_MOTION;
  
  private boolean mPaused = false;
  
  TransitionPropagation mPropagation;
  
  private ViewGroup mSceneRoot = null;
  
  private long mStartDelay = -1L;
  
  private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
  
  private ArrayList<TransitionValues> mStartValuesList;
  
  private ArrayList<View> mTargetChildExcludes = null;
  
  private ArrayList<View> mTargetExcludes = null;
  
  private ArrayList<Integer> mTargetIdChildExcludes = null;
  
  private ArrayList<Integer> mTargetIdExcludes = null;
  
  ArrayList<Integer> mTargetIds = new ArrayList<>();
  
  private ArrayList<String> mTargetNameExcludes = null;
  
  private ArrayList<String> mTargetNames = null;
  
  private ArrayList<Class> mTargetTypeChildExcludes = null;
  
  private ArrayList<Class> mTargetTypeExcludes = null;
  
  private ArrayList<Class> mTargetTypes = null;
  
  ArrayList<View> mTargets = new ArrayList<>();
  
  public Transition() {}
  
  public Transition(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION);
    XmlResourceParser xmlResourceParser = (XmlResourceParser)paramAttributeSet;
    long l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)xmlResourceParser, "duration", 1, -1);
    if (l >= 0L)
      setDuration(l); 
    l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)xmlResourceParser, "startDelay", 2, -1);
    if (l > 0L)
      setStartDelay(l); 
    int i = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)xmlResourceParser, "interpolator", 0, 0);
    if (i > 0)
      setInterpolator((TimeInterpolator)AnimationUtils.loadInterpolator(paramContext, i)); 
    String str = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)xmlResourceParser, "matchOrder", 3);
    if (str != null)
      setMatchOrder(parseMatchOrder(str)); 
    typedArray.recycle();
  }
  
  private void addUnmatched(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2) {
    byte b;
    for (b = 0; b < paramArrayMap1.size(); b++) {
      TransitionValues transitionValues = (TransitionValues)paramArrayMap1.valueAt(b);
      if (isValidTarget(transitionValues.view)) {
        this.mStartValuesList.add(transitionValues);
        this.mEndValuesList.add(null);
      } 
    } 
    for (b = 0; b < paramArrayMap2.size(); b++) {
      TransitionValues transitionValues = (TransitionValues)paramArrayMap2.valueAt(b);
      if (isValidTarget(transitionValues.view)) {
        this.mEndValuesList.add(transitionValues);
        this.mStartValuesList.add(null);
      } 
    } 
  }
  
  private static void addViewValues(TransitionValuesMaps paramTransitionValuesMaps, View paramView, TransitionValues paramTransitionValues) {
    paramTransitionValuesMaps.mViewValues.put(paramView, paramTransitionValues);
    int i = paramView.getId();
    if (i >= 0)
      if (paramTransitionValuesMaps.mIdValues.indexOfKey(i) >= 0) {
        paramTransitionValuesMaps.mIdValues.put(i, null);
      } else {
        paramTransitionValuesMaps.mIdValues.put(i, paramView);
      }  
    String str = ViewCompat.getTransitionName(paramView);
    if (str != null)
      if (paramTransitionValuesMaps.mNameValues.containsKey(str)) {
        paramTransitionValuesMaps.mNameValues.put(str, null);
      } else {
        paramTransitionValuesMaps.mNameValues.put(str, paramView);
      }  
    if (paramView.getParent() instanceof ListView) {
      ListView listView = (ListView)paramView.getParent();
      if (listView.getAdapter().hasStableIds()) {
        long l = listView.getItemIdAtPosition(listView.getPositionForView(paramView));
        if (paramTransitionValuesMaps.mItemIdValues.indexOfKey(l) >= 0) {
          paramView = (View)paramTransitionValuesMaps.mItemIdValues.get(l);
          if (paramView != null) {
            ViewCompat.setHasTransientState(paramView, false);
            paramTransitionValuesMaps.mItemIdValues.put(l, null);
          } 
        } else {
          ViewCompat.setHasTransientState(paramView, true);
          paramTransitionValuesMaps.mItemIdValues.put(l, paramView);
        } 
      } 
    } 
  }
  
  private static boolean alreadyContains(int[] paramArrayOfint, int paramInt) {
    int i = paramArrayOfint[paramInt];
    for (byte b = 0; b < paramInt; b++) {
      if (paramArrayOfint[b] == i)
        return true; 
    } 
    return false;
  }
  
  private void captureHierarchy(View paramView, boolean paramBoolean) {
    if (paramView == null)
      return; 
    int i = paramView.getId();
    ArrayList<Integer> arrayList2 = this.mTargetIdExcludes;
    if (arrayList2 != null && arrayList2.contains(Integer.valueOf(i)))
      return; 
    ArrayList<View> arrayList1 = this.mTargetExcludes;
    if (arrayList1 != null && arrayList1.contains(paramView))
      return; 
    ArrayList<Class> arrayList = this.mTargetTypeExcludes;
    if (arrayList != null) {
      int j = arrayList.size();
      for (byte b = 0; b < j; b++) {
        if (((Class)this.mTargetTypeExcludes.get(b)).isInstance(paramView))
          return; 
      } 
    } 
    if (paramView.getParent() instanceof ViewGroup) {
      TransitionValues transitionValues = new TransitionValues();
      transitionValues.view = paramView;
      if (paramBoolean) {
        captureStartValues(transitionValues);
      } else {
        captureEndValues(transitionValues);
      } 
      transitionValues.mTargetedTransitions.add(this);
      capturePropagationValues(transitionValues);
      if (paramBoolean) {
        addViewValues(this.mStartValues, paramView, transitionValues);
      } else {
        addViewValues(this.mEndValues, paramView, transitionValues);
      } 
    } 
    if (paramView instanceof ViewGroup) {
      ArrayList<Integer> arrayList5 = this.mTargetIdChildExcludes;
      if (arrayList5 != null && arrayList5.contains(Integer.valueOf(i)))
        return; 
      ArrayList<View> arrayList4 = this.mTargetChildExcludes;
      if (arrayList4 != null && arrayList4.contains(paramView))
        return; 
      ArrayList<Class> arrayList3 = this.mTargetTypeChildExcludes;
      if (arrayList3 != null) {
        int j = arrayList3.size();
        for (byte b1 = 0; b1 < j; b1++) {
          if (((Class)this.mTargetTypeChildExcludes.get(b1)).isInstance(paramView))
            return; 
        } 
      } 
      ViewGroup viewGroup = (ViewGroup)paramView;
      for (byte b = 0; b < viewGroup.getChildCount(); b++)
        captureHierarchy(viewGroup.getChildAt(b), paramBoolean); 
    } 
  }
  
  private ArrayList<Integer> excludeId(ArrayList<Integer> paramArrayList, int paramInt, boolean paramBoolean) {
    ArrayList<Integer> arrayList = paramArrayList;
    if (paramInt > 0)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, Integer.valueOf(paramInt));
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, Integer.valueOf(paramInt));
      }  
    return arrayList;
  }
  
  private static <T> ArrayList<T> excludeObject(ArrayList<T> paramArrayList, T paramT, boolean paramBoolean) {
    ArrayList<T> arrayList = paramArrayList;
    if (paramT != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramT);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramT);
      }  
    return arrayList;
  }
  
  private ArrayList<Class> excludeType(ArrayList<Class> paramArrayList, Class<?> paramClass, boolean paramBoolean) {
    ArrayList<Class> arrayList = paramArrayList;
    if (paramClass != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramClass);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramClass);
      }  
    return arrayList;
  }
  
  private ArrayList<View> excludeView(ArrayList<View> paramArrayList, View paramView, boolean paramBoolean) {
    ArrayList<View> arrayList = paramArrayList;
    if (paramView != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramView);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramView);
      }  
    return arrayList;
  }
  
  private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
    ArrayMap<Animator, AnimationInfo> arrayMap1 = sRunningAnimators.get();
    ArrayMap<Animator, AnimationInfo> arrayMap2 = arrayMap1;
    if (arrayMap1 == null) {
      arrayMap2 = new ArrayMap();
      sRunningAnimators.set(arrayMap2);
    } 
    return arrayMap2;
  }
  
  private static boolean isValidMatch(int paramInt) {
    boolean bool = true;
    if (paramInt < 1 || paramInt > 4)
      bool = false; 
    return bool;
  }
  
  private static boolean isValueChanged(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2, String paramString) {
    int i;
    paramTransitionValues1 = (TransitionValues)paramTransitionValues1.values.get(paramString);
    paramTransitionValues2 = (TransitionValues)paramTransitionValues2.values.get(paramString);
    if (paramTransitionValues1 == null && paramTransitionValues2 == null) {
      i = 0;
    } else {
      if (paramTransitionValues1 == null || paramTransitionValues2 == null)
        return true; 
      i = paramTransitionValues1.equals(paramTransitionValues2) ^ true;
    } 
    return i;
  }
  
  private void matchIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, SparseArray<View> paramSparseArray1, SparseArray<View> paramSparseArray2) {
    int i = paramSparseArray1.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramSparseArray1.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramSparseArray2.get(paramSparseArray1.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues1 != null && transitionValues2 != null) {
            this.mStartValuesList.add(transitionValues1);
            this.mEndValuesList.add(transitionValues2);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchInstances(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2) {
    for (int i = paramArrayMap1.size() - 1; i >= 0; i--) {
      View view = (View)paramArrayMap1.keyAt(i);
      if (view != null && isValidTarget(view)) {
        TransitionValues transitionValues = (TransitionValues)paramArrayMap2.remove(view);
        if (transitionValues != null && transitionValues.view != null && isValidTarget(transitionValues.view)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.removeAt(i);
          this.mStartValuesList.add(transitionValues1);
          this.mEndValuesList.add(transitionValues);
        } 
      } 
    } 
  }
  
  private void matchItemIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, LongSparseArray<View> paramLongSparseArray1, LongSparseArray<View> paramLongSparseArray2) {
    int i = paramLongSparseArray1.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramLongSparseArray1.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramLongSparseArray2.get(paramLongSparseArray1.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues1 != null && transitionValues2 != null) {
            this.mStartValuesList.add(transitionValues1);
            this.mEndValuesList.add(transitionValues2);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchNames(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, ArrayMap<String, View> paramArrayMap3, ArrayMap<String, View> paramArrayMap4) {
    int i = paramArrayMap3.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramArrayMap3.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramArrayMap4.get(paramArrayMap3.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues1 != null && transitionValues2 != null) {
            this.mStartValuesList.add(transitionValues1);
            this.mEndValuesList.add(transitionValues2);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchStartAndEnd(TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2) {
    ArrayMap<View, TransitionValues> arrayMap1 = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps1.mViewValues);
    ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps2.mViewValues);
    byte b = 0;
    while (true) {
      int[] arrayOfInt = this.mMatchOrder;
      if (b < arrayOfInt.length) {
        int i = arrayOfInt[b];
        if (i != 1) {
          if (i != 2) {
            if (i != 3) {
              if (i == 4)
                matchItemIds(arrayMap1, arrayMap2, paramTransitionValuesMaps1.mItemIdValues, paramTransitionValuesMaps2.mItemIdValues); 
            } else {
              matchIds(arrayMap1, arrayMap2, paramTransitionValuesMaps1.mIdValues, paramTransitionValuesMaps2.mIdValues);
            } 
          } else {
            matchNames(arrayMap1, arrayMap2, paramTransitionValuesMaps1.mNameValues, paramTransitionValuesMaps2.mNameValues);
          } 
        } else {
          matchInstances(arrayMap1, arrayMap2);
        } 
        b++;
        continue;
      } 
      addUnmatched(arrayMap1, arrayMap2);
      return;
    } 
  }
  
  private static int[] parseMatchOrder(String paramString) {
    StringBuilder stringBuilder;
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ",");
    int[] arrayOfInt = new int[stringTokenizer.countTokens()];
    for (byte b = 0; stringTokenizer.hasMoreTokens(); b++) {
      String str = stringTokenizer.nextToken().trim();
      if ("id".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 3;
      } else if ("instance".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 1;
      } else if ("name".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 2;
      } else if ("itemId".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 4;
      } else {
        int[] arrayOfInt1;
        if (str.isEmpty()) {
          arrayOfInt1 = new int[arrayOfInt.length - 1];
          System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, b);
          arrayOfInt = arrayOfInt1;
          b--;
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown match type in matchOrder: '");
          stringBuilder.append((String)arrayOfInt1);
          stringBuilder.append("'");
          throw new InflateException(stringBuilder.toString());
        } 
      } 
    } 
    return (int[])stringBuilder;
  }
  
  private void runAnimator(Animator paramAnimator, final ArrayMap<Animator, AnimationInfo> runningAnimators) {
    if (paramAnimator != null) {
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              runningAnimators.remove(param1Animator);
              Transition.this.mCurrentAnimators.remove(param1Animator);
            }
            
            public void onAnimationStart(Animator param1Animator) {
              Transition.this.mCurrentAnimators.add(param1Animator);
            }
          });
      animate(paramAnimator);
    } 
  }
  
  public Transition addListener(TransitionListener paramTransitionListener) {
    if (this.mListeners == null)
      this.mListeners = new ArrayList<>(); 
    this.mListeners.add(paramTransitionListener);
    return this;
  }
  
  public Transition addTarget(int paramInt) {
    if (paramInt != 0)
      this.mTargetIds.add(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public Transition addTarget(View paramView) {
    this.mTargets.add(paramView);
    return this;
  }
  
  public Transition addTarget(Class paramClass) {
    if (this.mTargetTypes == null)
      this.mTargetTypes = new ArrayList<>(); 
    this.mTargetTypes.add(paramClass);
    return this;
  }
  
  public Transition addTarget(String paramString) {
    if (this.mTargetNames == null)
      this.mTargetNames = new ArrayList<>(); 
    this.mTargetNames.add(paramString);
    return this;
  }
  
  protected void animate(Animator paramAnimator) {
    if (paramAnimator == null) {
      end();
    } else {
      if (getDuration() >= 0L)
        paramAnimator.setDuration(getDuration()); 
      if (getStartDelay() >= 0L)
        paramAnimator.setStartDelay(getStartDelay()); 
      if (getInterpolator() != null)
        paramAnimator.setInterpolator(getInterpolator()); 
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              Transition.this.end();
              param1Animator.removeListener((Animator.AnimatorListener)this);
            }
          });
      paramAnimator.start();
    } 
  }
  
  protected void cancel() {
    int i;
    for (i = this.mCurrentAnimators.size() - 1; i >= 0; i--)
      ((Animator)this.mCurrentAnimators.get(i)).cancel(); 
    ArrayList<TransitionListener> arrayList = this.mListeners;
    if (arrayList != null && arrayList.size() > 0) {
      arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
      int j = arrayList.size();
      for (i = 0; i < j; i++)
        ((TransitionListener)arrayList.get(i)).onTransitionCancel(this); 
    } 
  }
  
  public abstract void captureEndValues(TransitionValues paramTransitionValues);
  
  void capturePropagationValues(TransitionValues paramTransitionValues) {
    if (this.mPropagation != null && !paramTransitionValues.values.isEmpty()) {
      boolean bool2;
      String[] arrayOfString = this.mPropagation.getPropagationProperties();
      if (arrayOfString == null)
        return; 
      boolean bool1 = true;
      byte b = 0;
      while (true) {
        bool2 = bool1;
        if (b < arrayOfString.length) {
          if (!paramTransitionValues.values.containsKey(arrayOfString[b])) {
            bool2 = false;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (!bool2)
        this.mPropagation.captureValues(paramTransitionValues); 
    } 
  }
  
  public abstract void captureStartValues(TransitionValues paramTransitionValues);
  
  void captureValues(ViewGroup paramViewGroup, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: invokevirtual clearValues : (Z)V
    //   5: aload_0
    //   6: getfield mTargetIds : Ljava/util/ArrayList;
    //   9: invokevirtual size : ()I
    //   12: ifgt -> 25
    //   15: aload_0
    //   16: getfield mTargets : Ljava/util/ArrayList;
    //   19: invokevirtual size : ()I
    //   22: ifle -> 60
    //   25: aload_0
    //   26: getfield mTargetNames : Ljava/util/ArrayList;
    //   29: astore_3
    //   30: aload_3
    //   31: ifnull -> 41
    //   34: aload_3
    //   35: invokevirtual isEmpty : ()Z
    //   38: ifeq -> 60
    //   41: aload_0
    //   42: getfield mTargetTypes : Ljava/util/ArrayList;
    //   45: astore_3
    //   46: aload_3
    //   47: ifnull -> 69
    //   50: aload_3
    //   51: invokevirtual isEmpty : ()Z
    //   54: ifeq -> 60
    //   57: goto -> 69
    //   60: aload_0
    //   61: aload_1
    //   62: iload_2
    //   63: invokespecial captureHierarchy : (Landroid/view/View;Z)V
    //   66: goto -> 294
    //   69: iconst_0
    //   70: istore #4
    //   72: iload #4
    //   74: aload_0
    //   75: getfield mTargetIds : Ljava/util/ArrayList;
    //   78: invokevirtual size : ()I
    //   81: if_icmpge -> 191
    //   84: aload_1
    //   85: aload_0
    //   86: getfield mTargetIds : Ljava/util/ArrayList;
    //   89: iload #4
    //   91: invokevirtual get : (I)Ljava/lang/Object;
    //   94: checkcast java/lang/Integer
    //   97: invokevirtual intValue : ()I
    //   100: invokevirtual findViewById : (I)Landroid/view/View;
    //   103: astore_3
    //   104: aload_3
    //   105: ifnull -> 185
    //   108: new android/support/transition/TransitionValues
    //   111: dup
    //   112: invokespecial <init> : ()V
    //   115: astore #5
    //   117: aload #5
    //   119: aload_3
    //   120: putfield view : Landroid/view/View;
    //   123: iload_2
    //   124: ifeq -> 136
    //   127: aload_0
    //   128: aload #5
    //   130: invokevirtual captureStartValues : (Landroid/support/transition/TransitionValues;)V
    //   133: goto -> 142
    //   136: aload_0
    //   137: aload #5
    //   139: invokevirtual captureEndValues : (Landroid/support/transition/TransitionValues;)V
    //   142: aload #5
    //   144: getfield mTargetedTransitions : Ljava/util/ArrayList;
    //   147: aload_0
    //   148: invokevirtual add : (Ljava/lang/Object;)Z
    //   151: pop
    //   152: aload_0
    //   153: aload #5
    //   155: invokevirtual capturePropagationValues : (Landroid/support/transition/TransitionValues;)V
    //   158: iload_2
    //   159: ifeq -> 175
    //   162: aload_0
    //   163: getfield mStartValues : Landroid/support/transition/TransitionValuesMaps;
    //   166: aload_3
    //   167: aload #5
    //   169: invokestatic addViewValues : (Landroid/support/transition/TransitionValuesMaps;Landroid/view/View;Landroid/support/transition/TransitionValues;)V
    //   172: goto -> 185
    //   175: aload_0
    //   176: getfield mEndValues : Landroid/support/transition/TransitionValuesMaps;
    //   179: aload_3
    //   180: aload #5
    //   182: invokestatic addViewValues : (Landroid/support/transition/TransitionValuesMaps;Landroid/view/View;Landroid/support/transition/TransitionValues;)V
    //   185: iinc #4, 1
    //   188: goto -> 72
    //   191: iconst_0
    //   192: istore #4
    //   194: iload #4
    //   196: aload_0
    //   197: getfield mTargets : Ljava/util/ArrayList;
    //   200: invokevirtual size : ()I
    //   203: if_icmpge -> 294
    //   206: aload_0
    //   207: getfield mTargets : Ljava/util/ArrayList;
    //   210: iload #4
    //   212: invokevirtual get : (I)Ljava/lang/Object;
    //   215: checkcast android/view/View
    //   218: astore_3
    //   219: new android/support/transition/TransitionValues
    //   222: dup
    //   223: invokespecial <init> : ()V
    //   226: astore_1
    //   227: aload_1
    //   228: aload_3
    //   229: putfield view : Landroid/view/View;
    //   232: iload_2
    //   233: ifeq -> 244
    //   236: aload_0
    //   237: aload_1
    //   238: invokevirtual captureStartValues : (Landroid/support/transition/TransitionValues;)V
    //   241: goto -> 249
    //   244: aload_0
    //   245: aload_1
    //   246: invokevirtual captureEndValues : (Landroid/support/transition/TransitionValues;)V
    //   249: aload_1
    //   250: getfield mTargetedTransitions : Ljava/util/ArrayList;
    //   253: aload_0
    //   254: invokevirtual add : (Ljava/lang/Object;)Z
    //   257: pop
    //   258: aload_0
    //   259: aload_1
    //   260: invokevirtual capturePropagationValues : (Landroid/support/transition/TransitionValues;)V
    //   263: iload_2
    //   264: ifeq -> 279
    //   267: aload_0
    //   268: getfield mStartValues : Landroid/support/transition/TransitionValuesMaps;
    //   271: aload_3
    //   272: aload_1
    //   273: invokestatic addViewValues : (Landroid/support/transition/TransitionValuesMaps;Landroid/view/View;Landroid/support/transition/TransitionValues;)V
    //   276: goto -> 288
    //   279: aload_0
    //   280: getfield mEndValues : Landroid/support/transition/TransitionValuesMaps;
    //   283: aload_3
    //   284: aload_1
    //   285: invokestatic addViewValues : (Landroid/support/transition/TransitionValuesMaps;Landroid/view/View;Landroid/support/transition/TransitionValues;)V
    //   288: iinc #4, 1
    //   291: goto -> 194
    //   294: iload_2
    //   295: ifne -> 427
    //   298: aload_0
    //   299: getfield mNameOverrides : Landroid/support/v4/util/ArrayMap;
    //   302: astore_1
    //   303: aload_1
    //   304: ifnull -> 427
    //   307: aload_1
    //   308: invokevirtual size : ()I
    //   311: istore #6
    //   313: new java/util/ArrayList
    //   316: dup
    //   317: iload #6
    //   319: invokespecial <init> : (I)V
    //   322: astore_1
    //   323: iconst_0
    //   324: istore #4
    //   326: iload #4
    //   328: iload #6
    //   330: if_icmpge -> 368
    //   333: aload_0
    //   334: getfield mNameOverrides : Landroid/support/v4/util/ArrayMap;
    //   337: iload #4
    //   339: invokevirtual keyAt : (I)Ljava/lang/Object;
    //   342: checkcast java/lang/String
    //   345: astore_3
    //   346: aload_1
    //   347: aload_0
    //   348: getfield mStartValues : Landroid/support/transition/TransitionValuesMaps;
    //   351: getfield mNameValues : Landroid/support/v4/util/ArrayMap;
    //   354: aload_3
    //   355: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   358: invokevirtual add : (Ljava/lang/Object;)Z
    //   361: pop
    //   362: iinc #4, 1
    //   365: goto -> 326
    //   368: iconst_0
    //   369: istore #4
    //   371: iload #4
    //   373: iload #6
    //   375: if_icmpge -> 427
    //   378: aload_1
    //   379: iload #4
    //   381: invokevirtual get : (I)Ljava/lang/Object;
    //   384: checkcast android/view/View
    //   387: astore #5
    //   389: aload #5
    //   391: ifnull -> 421
    //   394: aload_0
    //   395: getfield mNameOverrides : Landroid/support/v4/util/ArrayMap;
    //   398: iload #4
    //   400: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   403: checkcast java/lang/String
    //   406: astore_3
    //   407: aload_0
    //   408: getfield mStartValues : Landroid/support/transition/TransitionValuesMaps;
    //   411: getfield mNameValues : Landroid/support/v4/util/ArrayMap;
    //   414: aload_3
    //   415: aload #5
    //   417: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   420: pop
    //   421: iinc #4, 1
    //   424: goto -> 371
    //   427: return
  }
  
  void clearValues(boolean paramBoolean) {
    if (paramBoolean) {
      this.mStartValues.mViewValues.clear();
      this.mStartValues.mIdValues.clear();
      this.mStartValues.mItemIdValues.clear();
    } else {
      this.mEndValues.mViewValues.clear();
      this.mEndValues.mIdValues.clear();
      this.mEndValues.mItemIdValues.clear();
    } 
  }
  
  public Transition clone() {
    try {
      Transition transition = (Transition)super.clone();
      ArrayList<Animator> arrayList = new ArrayList();
      this();
      transition.mAnimators = arrayList;
      TransitionValuesMaps transitionValuesMaps = new TransitionValuesMaps();
      this();
      transition.mStartValues = transitionValuesMaps;
      transitionValuesMaps = new TransitionValuesMaps();
      this();
      transition.mEndValues = transitionValuesMaps;
      transition.mStartValuesList = null;
      transition.mEndValuesList = null;
      return transition;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      return null;
    } 
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2, ArrayList<TransitionValues> paramArrayList1, ArrayList<TransitionValues> paramArrayList2) {
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    long l = Long.MAX_VALUE;
    SparseIntArray sparseIntArray = new SparseIntArray();
    int i = paramArrayList1.size();
    int j = 0;
    while (j < i) {
      long l1;
      TransitionValues transitionValues1 = paramArrayList1.get(j);
      TransitionValues transitionValues2 = paramArrayList2.get(j);
      if (transitionValues1 != null && !transitionValues1.mTargetedTransitions.contains(this))
        transitionValues1 = null; 
      if (transitionValues2 != null && !transitionValues2.mTargetedTransitions.contains(this))
        transitionValues2 = null; 
      if (transitionValues1 == null && transitionValues2 == null) {
        l1 = l;
      } else {
        int k;
        if (transitionValues1 == null || transitionValues2 == null || isTransitionRequired(transitionValues1, transitionValues2)) {
          k = 1;
        } else {
          k = 0;
        } 
        if (k) {
          Animator animator = createAnimator(paramViewGroup, transitionValues1, transitionValues2);
          if (animator != null) {
            TransitionValues transitionValues3;
            View view;
            TransitionValues transitionValues4 = null;
            if (transitionValues2 != null) {
              View view1 = transitionValues2.view;
              String[] arrayOfString = getTransitionProperties();
              if (view1 != null && arrayOfString != null && arrayOfString.length > 0) {
                transitionValues4 = new TransitionValues();
                transitionValues4.view = view1;
                TransitionValues transitionValues5 = (TransitionValues)paramTransitionValuesMaps2.mViewValues.get(view1);
                if (transitionValues5 != null)
                  for (k = 0; k < arrayOfString.length; k++)
                    transitionValues4.values.put(arrayOfString[k], transitionValues5.values.get(arrayOfString[k]));  
                k = arrayMap.size();
                for (byte b = 0; b < k; b++) {
                  AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(arrayMap.keyAt(b));
                  if (animationInfo.mValues != null && animationInfo.mView == view1 && animationInfo.mName.equals(getName()) && animationInfo.mValues.equals(transitionValues4)) {
                    animator = null;
                    break;
                  } 
                } 
              } 
              TransitionValues transitionValues = transitionValues4;
              view = view1;
              Animator animator1 = animator;
              transitionValues3 = transitionValues;
              k = j;
            } else {
              view = transitionValues1.view;
              TransitionValues transitionValues = null;
              transitionValues4 = transitionValues3;
              k = j;
              transitionValues3 = transitionValues;
            } 
            l1 = l;
            j = k;
            if (transitionValues4 != null) {
              TransitionPropagation transitionPropagation = this.mPropagation;
              if (transitionPropagation != null) {
                l1 = transitionPropagation.getStartDelay(paramViewGroup, this, transitionValues1, transitionValues2);
                sparseIntArray.put(this.mAnimators.size(), (int)l1);
                l = Math.min(l1, l);
              } 
              arrayMap.put(transitionValues4, new AnimationInfo(view, getName(), this, ViewUtils.getWindowId((View)paramViewGroup), transitionValues3));
              this.mAnimators.add(transitionValues4);
              l1 = l;
              j = k;
            } 
          } else {
            l1 = l;
          } 
        } else {
          l1 = l;
        } 
      } 
      j++;
      l = l1;
    } 
    if (l != 0L)
      for (j = 0; j < sparseIntArray.size(); j++) {
        int k = sparseIntArray.keyAt(j);
        Animator animator = this.mAnimators.get(k);
        animator.setStartDelay(sparseIntArray.valueAt(j) - l + animator.getStartDelay());
      }  
  }
  
  protected void end() {
    int i = this.mNumInstances - 1;
    this.mNumInstances = i;
    if (i == 0) {
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionEnd(this); 
      } 
      for (i = 0; i < this.mStartValues.mItemIdValues.size(); i++) {
        View view = (View)this.mStartValues.mItemIdValues.valueAt(i);
        if (view != null)
          ViewCompat.setHasTransientState(view, false); 
      } 
      for (i = 0; i < this.mEndValues.mItemIdValues.size(); i++) {
        View view = (View)this.mEndValues.mItemIdValues.valueAt(i);
        if (view != null)
          ViewCompat.setHasTransientState(view, false); 
      } 
      this.mEnded = true;
    } 
  }
  
  public Transition excludeChildren(int paramInt, boolean paramBoolean) {
    this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(View paramView, boolean paramBoolean) {
    this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(Class paramClass, boolean paramBoolean) {
    this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(int paramInt, boolean paramBoolean) {
    this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(View paramView, boolean paramBoolean) {
    this.mTargetExcludes = excludeView(this.mTargetExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(Class paramClass, boolean paramBoolean) {
    this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(String paramString, boolean paramBoolean) {
    this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, paramString, paramBoolean);
    return this;
  }
  
  void forceToEnd(ViewGroup paramViewGroup) {
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    int i = arrayMap.size();
    if (paramViewGroup != null) {
      WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)paramViewGroup);
      while (--i >= 0) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
        if (animationInfo.mView != null && windowIdImpl != null && windowIdImpl.equals(animationInfo.mWindowId))
          ((Animator)arrayMap.keyAt(i)).end(); 
        i--;
      } 
    } 
  }
  
  public long getDuration() {
    return this.mDuration;
  }
  
  public Rect getEpicenter() {
    EpicenterCallback epicenterCallback = this.mEpicenterCallback;
    return (epicenterCallback == null) ? null : epicenterCallback.onGetEpicenter(this);
  }
  
  public EpicenterCallback getEpicenterCallback() {
    return this.mEpicenterCallback;
  }
  
  public TimeInterpolator getInterpolator() {
    return this.mInterpolator;
  }
  
  TransitionValues getMatchedTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValues transitionValues;
    ArrayList<TransitionValues> arrayList;
    byte b2;
    TransitionSet transitionSet = this.mParent;
    if (transitionSet != null)
      return transitionSet.getMatchedTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      arrayList = this.mStartValuesList;
    } else {
      arrayList = this.mEndValuesList;
    } 
    if (arrayList == null)
      return null; 
    int i = arrayList.size();
    byte b1 = -1;
    byte b = 0;
    while (true) {
      b2 = b1;
      if (b < i) {
        TransitionValues transitionValues1 = arrayList.get(b);
        if (transitionValues1 == null)
          return null; 
        if (transitionValues1.view == paramView) {
          b2 = b;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    paramView = null;
    if (b2 >= 0) {
      ArrayList<TransitionValues> arrayList1;
      if (paramBoolean) {
        arrayList1 = this.mEndValuesList;
      } else {
        arrayList1 = this.mStartValuesList;
      } 
      transitionValues = arrayList1.get(b2);
    } 
    return transitionValues;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public PathMotion getPathMotion() {
    return this.mPathMotion;
  }
  
  public TransitionPropagation getPropagation() {
    return this.mPropagation;
  }
  
  public long getStartDelay() {
    return this.mStartDelay;
  }
  
  public List<Integer> getTargetIds() {
    return this.mTargetIds;
  }
  
  public List<String> getTargetNames() {
    return this.mTargetNames;
  }
  
  public List<Class> getTargetTypes() {
    return this.mTargetTypes;
  }
  
  public List<View> getTargets() {
    return this.mTargets;
  }
  
  public String[] getTransitionProperties() {
    return null;
  }
  
  public TransitionValues getTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValuesMaps transitionValuesMaps;
    TransitionSet transitionSet = this.mParent;
    if (transitionSet != null)
      return transitionSet.getTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      transitionValuesMaps = this.mStartValues;
    } else {
      transitionValuesMaps = this.mEndValues;
    } 
    return (TransitionValues)transitionValuesMaps.mViewValues.get(paramView);
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = bool1;
    if (paramTransitionValues1 != null) {
      bool3 = bool1;
      if (paramTransitionValues2 != null) {
        String[] arrayOfString = getTransitionProperties();
        if (arrayOfString != null) {
          int i = arrayOfString.length;
          byte b = 0;
          while (true) {
            bool3 = bool2;
            if (b < i) {
              if (isValueChanged(paramTransitionValues1, paramTransitionValues2, arrayOfString[b])) {
                bool3 = true;
                break;
              } 
              b++;
              continue;
            } 
            break;
          } 
        } else {
          Iterator<String> iterator = paramTransitionValues1.values.keySet().iterator();
          while (true) {
            bool3 = bool1;
            if (iterator.hasNext()) {
              if (isValueChanged(paramTransitionValues1, paramTransitionValues2, iterator.next())) {
                bool3 = true;
                break;
              } 
              continue;
            } 
            break;
          } 
        } 
      } 
    } 
    return bool3;
  }
  
  boolean isValidTarget(View paramView) {
    int i = paramView.getId();
    ArrayList<Integer> arrayList3 = this.mTargetIdExcludes;
    if (arrayList3 != null && arrayList3.contains(Integer.valueOf(i)))
      return false; 
    ArrayList<View> arrayList2 = this.mTargetExcludes;
    if (arrayList2 != null && arrayList2.contains(paramView))
      return false; 
    ArrayList<Class> arrayList1 = this.mTargetTypeExcludes;
    if (arrayList1 != null) {
      int j = arrayList1.size();
      for (byte b = 0; b < j; b++) {
        if (((Class)this.mTargetTypeExcludes.get(b)).isInstance(paramView))
          return false; 
      } 
    } 
    if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(paramView) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(paramView)))
      return false; 
    if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
      arrayList1 = this.mTargetTypes;
      if (arrayList1 == null || arrayList1.isEmpty()) {
        ArrayList<String> arrayList4 = this.mTargetNames;
        if (arrayList4 == null || arrayList4.isEmpty())
          return true; 
      } 
    } 
    if (this.mTargetIds.contains(Integer.valueOf(i)) || this.mTargets.contains(paramView))
      return true; 
    ArrayList<String> arrayList = this.mTargetNames;
    if (arrayList != null && arrayList.contains(ViewCompat.getTransitionName(paramView)))
      return true; 
    if (this.mTargetTypes != null)
      for (byte b = 0; b < this.mTargetTypes.size(); b++) {
        if (((Class)this.mTargetTypes.get(b)).isInstance(paramView))
          return true; 
      }  
    return false;
  }
  
  public void pause(View paramView) {
    if (!this.mEnded) {
      ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
      int i = arrayMap.size();
      WindowIdImpl windowIdImpl = ViewUtils.getWindowId(paramView);
      while (--i >= 0) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
        if (animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId))
          AnimatorUtils.pause((Animator)arrayMap.keyAt(i)); 
        i--;
      } 
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionPause(this); 
      } 
      this.mPaused = true;
    } 
  }
  
  void playTransition(ViewGroup paramViewGroup) {
    this.mStartValuesList = new ArrayList<>();
    this.mEndValuesList = new ArrayList<>();
    matchStartAndEnd(this.mStartValues, this.mEndValues);
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    int i = arrayMap.size();
    WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)paramViewGroup);
    while (--i >= 0) {
      Animator animator = (Animator)arrayMap.keyAt(i);
      if (animator != null) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(animator);
        if (animationInfo != null && animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId)) {
          TransitionValues transitionValues1 = animationInfo.mValues;
          View view = animationInfo.mView;
          boolean bool = true;
          TransitionValues transitionValues3 = getTransitionValues(view, true);
          TransitionValues transitionValues2 = getMatchedTransitionValues(view, true);
          if ((transitionValues3 == null && transitionValues2 == null) || !animationInfo.mTransition.isTransitionRequired(transitionValues1, transitionValues2))
            bool = false; 
          if (bool)
            if (animator.isRunning() || animator.isStarted()) {
              animator.cancel();
            } else {
              arrayMap.remove(animator);
            }  
        } 
      } 
      i--;
    } 
    createAnimators(paramViewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
    runAnimators();
  }
  
  public Transition removeListener(TransitionListener paramTransitionListener) {
    ArrayList<TransitionListener> arrayList = this.mListeners;
    if (arrayList == null)
      return this; 
    arrayList.remove(paramTransitionListener);
    if (this.mListeners.size() == 0)
      this.mListeners = null; 
    return this;
  }
  
  public Transition removeTarget(int paramInt) {
    if (paramInt != 0)
      this.mTargetIds.remove(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public Transition removeTarget(View paramView) {
    this.mTargets.remove(paramView);
    return this;
  }
  
  public Transition removeTarget(Class paramClass) {
    ArrayList<Class> arrayList = this.mTargetTypes;
    if (arrayList != null)
      arrayList.remove(paramClass); 
    return this;
  }
  
  public Transition removeTarget(String paramString) {
    ArrayList<String> arrayList = this.mTargetNames;
    if (arrayList != null)
      arrayList.remove(paramString); 
    return this;
  }
  
  public void resume(View paramView) {
    if (this.mPaused) {
      if (!this.mEnded) {
        ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
        int i = arrayMap.size();
        WindowIdImpl windowIdImpl = ViewUtils.getWindowId(paramView);
        while (--i >= 0) {
          AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
          if (animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId))
            AnimatorUtils.resume((Animator)arrayMap.keyAt(i)); 
          i--;
        } 
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
          arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
          int j = arrayList.size();
          for (i = 0; i < j; i++)
            ((TransitionListener)arrayList.get(i)).onTransitionResume(this); 
        } 
      } 
      this.mPaused = false;
    } 
  }
  
  protected void runAnimators() {
    start();
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    for (Animator animator : this.mAnimators) {
      if (arrayMap.containsKey(animator)) {
        start();
        runAnimator(animator, arrayMap);
      } 
    } 
    this.mAnimators.clear();
    end();
  }
  
  void setCanRemoveViews(boolean paramBoolean) {
    this.mCanRemoveViews = paramBoolean;
  }
  
  public Transition setDuration(long paramLong) {
    this.mDuration = paramLong;
    return this;
  }
  
  public void setEpicenterCallback(EpicenterCallback paramEpicenterCallback) {
    this.mEpicenterCallback = paramEpicenterCallback;
  }
  
  public Transition setInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.mInterpolator = paramTimeInterpolator;
    return this;
  }
  
  public void setMatchOrder(int... paramVarArgs) {
    if (paramVarArgs == null || paramVarArgs.length == 0) {
      this.mMatchOrder = DEFAULT_MATCH_ORDER;
      return;
    } 
    byte b = 0;
    while (b < paramVarArgs.length) {
      if (isValidMatch(paramVarArgs[b])) {
        if (!alreadyContains(paramVarArgs, b)) {
          b++;
          continue;
        } 
        throw new IllegalArgumentException("matches contains a duplicate value");
      } 
      throw new IllegalArgumentException("matches contains invalid value");
    } 
    this.mMatchOrder = (int[])paramVarArgs.clone();
  }
  
  public void setPathMotion(PathMotion paramPathMotion) {
    if (paramPathMotion == null) {
      this.mPathMotion = STRAIGHT_PATH_MOTION;
    } else {
      this.mPathMotion = paramPathMotion;
    } 
  }
  
  public void setPropagation(TransitionPropagation paramTransitionPropagation) {
    this.mPropagation = paramTransitionPropagation;
  }
  
  Transition setSceneRoot(ViewGroup paramViewGroup) {
    this.mSceneRoot = paramViewGroup;
    return this;
  }
  
  public Transition setStartDelay(long paramLong) {
    this.mStartDelay = paramLong;
    return this;
  }
  
  protected void start() {
    if (this.mNumInstances == 0) {
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int i = arrayList.size();
        for (byte b = 0; b < i; b++)
          ((TransitionListener)arrayList.get(b)).onTransitionStart(this); 
      } 
      this.mEnded = false;
    } 
    this.mNumInstances++;
  }
  
  public String toString() {
    return toString("");
  }
  
  String toString(String paramString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_2
    //   8: aload_2
    //   9: aload_1
    //   10: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   13: pop
    //   14: aload_2
    //   15: aload_0
    //   16: invokevirtual getClass : ()Ljava/lang/Class;
    //   19: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   22: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   25: pop
    //   26: aload_2
    //   27: ldc_w '@'
    //   30: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: pop
    //   34: aload_2
    //   35: aload_0
    //   36: invokevirtual hashCode : ()I
    //   39: invokestatic toHexString : (I)Ljava/lang/String;
    //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: aload_2
    //   47: ldc_w ': '
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_2
    //   55: invokevirtual toString : ()Ljava/lang/String;
    //   58: astore_1
    //   59: aload_1
    //   60: astore_2
    //   61: aload_0
    //   62: getfield mDuration : J
    //   65: ldc2_w -1
    //   68: lcmp
    //   69: ifeq -> 116
    //   72: new java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial <init> : ()V
    //   79: astore_2
    //   80: aload_2
    //   81: aload_1
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_2
    //   87: ldc_w 'dur('
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: aload_2
    //   95: aload_0
    //   96: getfield mDuration : J
    //   99: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: aload_2
    //   104: ldc_w ') '
    //   107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload_2
    //   112: invokevirtual toString : ()Ljava/lang/String;
    //   115: astore_2
    //   116: aload_2
    //   117: astore_1
    //   118: aload_0
    //   119: getfield mStartDelay : J
    //   122: ldc2_w -1
    //   125: lcmp
    //   126: ifeq -> 173
    //   129: new java/lang/StringBuilder
    //   132: dup
    //   133: invokespecial <init> : ()V
    //   136: astore_1
    //   137: aload_1
    //   138: aload_2
    //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload_1
    //   144: ldc_w 'dly('
    //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload_1
    //   152: aload_0
    //   153: getfield mStartDelay : J
    //   156: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: aload_1
    //   161: ldc_w ') '
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: pop
    //   168: aload_1
    //   169: invokevirtual toString : ()Ljava/lang/String;
    //   172: astore_1
    //   173: aload_1
    //   174: astore_2
    //   175: aload_0
    //   176: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   179: ifnull -> 226
    //   182: new java/lang/StringBuilder
    //   185: dup
    //   186: invokespecial <init> : ()V
    //   189: astore_2
    //   190: aload_2
    //   191: aload_1
    //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: pop
    //   196: aload_2
    //   197: ldc_w 'interp('
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_2
    //   205: aload_0
    //   206: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   209: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload_2
    //   214: ldc_w ') '
    //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: pop
    //   221: aload_2
    //   222: invokevirtual toString : ()Ljava/lang/String;
    //   225: astore_2
    //   226: aload_0
    //   227: getfield mTargetIds : Ljava/util/ArrayList;
    //   230: invokevirtual size : ()I
    //   233: ifgt -> 248
    //   236: aload_2
    //   237: astore_1
    //   238: aload_0
    //   239: getfield mTargets : Ljava/util/ArrayList;
    //   242: invokevirtual size : ()I
    //   245: ifle -> 503
    //   248: new java/lang/StringBuilder
    //   251: dup
    //   252: invokespecial <init> : ()V
    //   255: astore_1
    //   256: aload_1
    //   257: aload_2
    //   258: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload_1
    //   263: ldc_w 'tgts('
    //   266: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload_1
    //   271: invokevirtual toString : ()Ljava/lang/String;
    //   274: astore_1
    //   275: aload_1
    //   276: astore_2
    //   277: aload_0
    //   278: getfield mTargetIds : Ljava/util/ArrayList;
    //   281: invokevirtual size : ()I
    //   284: ifle -> 373
    //   287: iconst_0
    //   288: istore_3
    //   289: aload_1
    //   290: astore_2
    //   291: iload_3
    //   292: aload_0
    //   293: getfield mTargetIds : Ljava/util/ArrayList;
    //   296: invokevirtual size : ()I
    //   299: if_icmpge -> 373
    //   302: aload_1
    //   303: astore_2
    //   304: iload_3
    //   305: ifle -> 335
    //   308: new java/lang/StringBuilder
    //   311: dup
    //   312: invokespecial <init> : ()V
    //   315: astore_2
    //   316: aload_2
    //   317: aload_1
    //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: pop
    //   322: aload_2
    //   323: ldc_w ', '
    //   326: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: pop
    //   330: aload_2
    //   331: invokevirtual toString : ()Ljava/lang/String;
    //   334: astore_2
    //   335: new java/lang/StringBuilder
    //   338: dup
    //   339: invokespecial <init> : ()V
    //   342: astore_1
    //   343: aload_1
    //   344: aload_2
    //   345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   348: pop
    //   349: aload_1
    //   350: aload_0
    //   351: getfield mTargetIds : Ljava/util/ArrayList;
    //   354: iload_3
    //   355: invokevirtual get : (I)Ljava/lang/Object;
    //   358: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   361: pop
    //   362: aload_1
    //   363: invokevirtual toString : ()Ljava/lang/String;
    //   366: astore_1
    //   367: iinc #3, 1
    //   370: goto -> 289
    //   373: aload_2
    //   374: astore #4
    //   376: aload_0
    //   377: getfield mTargets : Ljava/util/ArrayList;
    //   380: invokevirtual size : ()I
    //   383: ifle -> 475
    //   386: iconst_0
    //   387: istore_3
    //   388: aload_2
    //   389: astore_1
    //   390: aload_1
    //   391: astore #4
    //   393: iload_3
    //   394: aload_0
    //   395: getfield mTargets : Ljava/util/ArrayList;
    //   398: invokevirtual size : ()I
    //   401: if_icmpge -> 475
    //   404: aload_1
    //   405: astore_2
    //   406: iload_3
    //   407: ifle -> 437
    //   410: new java/lang/StringBuilder
    //   413: dup
    //   414: invokespecial <init> : ()V
    //   417: astore_2
    //   418: aload_2
    //   419: aload_1
    //   420: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   423: pop
    //   424: aload_2
    //   425: ldc_w ', '
    //   428: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   431: pop
    //   432: aload_2
    //   433: invokevirtual toString : ()Ljava/lang/String;
    //   436: astore_2
    //   437: new java/lang/StringBuilder
    //   440: dup
    //   441: invokespecial <init> : ()V
    //   444: astore_1
    //   445: aload_1
    //   446: aload_2
    //   447: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   450: pop
    //   451: aload_1
    //   452: aload_0
    //   453: getfield mTargets : Ljava/util/ArrayList;
    //   456: iload_3
    //   457: invokevirtual get : (I)Ljava/lang/Object;
    //   460: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   463: pop
    //   464: aload_1
    //   465: invokevirtual toString : ()Ljava/lang/String;
    //   468: astore_1
    //   469: iinc #3, 1
    //   472: goto -> 390
    //   475: new java/lang/StringBuilder
    //   478: dup
    //   479: invokespecial <init> : ()V
    //   482: astore_1
    //   483: aload_1
    //   484: aload #4
    //   486: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   489: pop
    //   490: aload_1
    //   491: ldc_w ')'
    //   494: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   497: pop
    //   498: aload_1
    //   499: invokevirtual toString : ()Ljava/lang/String;
    //   502: astore_1
    //   503: aload_1
    //   504: areturn
  }
  
  private static class AnimationInfo {
    String mName;
    
    Transition mTransition;
    
    TransitionValues mValues;
    
    View mView;
    
    WindowIdImpl mWindowId;
    
    AnimationInfo(View param1View, String param1String, Transition param1Transition, WindowIdImpl param1WindowIdImpl, TransitionValues param1TransitionValues) {
      this.mView = param1View;
      this.mName = param1String;
      this.mValues = param1TransitionValues;
      this.mWindowId = param1WindowIdImpl;
      this.mTransition = param1Transition;
    }
  }
  
  private static class ArrayListManager {
    static <T> ArrayList<T> add(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList == null)
        arrayList = new ArrayList<>(); 
      if (!arrayList.contains(param1T))
        arrayList.add(param1T); 
      return arrayList;
    }
    
    static <T> ArrayList<T> remove(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList != null) {
        param1ArrayList.remove(param1T);
        arrayList = param1ArrayList;
        if (param1ArrayList.isEmpty())
          arrayList = null; 
      } 
      return arrayList;
    }
  }
  
  public static abstract class EpicenterCallback {
    public abstract Rect onGetEpicenter(Transition param1Transition);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MatchOrder {}
  
  public static interface TransitionListener {
    void onTransitionCancel(Transition param1Transition);
    
    void onTransitionEnd(Transition param1Transition);
    
    void onTransitionPause(Transition param1Transition);
    
    void onTransitionResume(Transition param1Transition);
    
    void onTransitionStart(Transition param1Transition);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/Transition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */