package android.support.v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
  static final Interpolator ACCELERATE_CUBIC;
  
  static final Interpolator ACCELERATE_QUINT;
  
  static final int ANIM_DUR = 220;
  
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  
  static boolean DEBUG = false;
  
  static final Interpolator DECELERATE_CUBIC;
  
  static final Interpolator DECELERATE_QUINT;
  
  static final String TAG = "FragmentManager";
  
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  static final String TARGET_STATE_TAG = "android:target_state";
  
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  static final String VIEW_STATE_TAG = "android:view_state";
  
  static Field sAnimationListenerField = null;
  
  SparseArray<Fragment> mActive;
  
  final ArrayList<Fragment> mAdded = new ArrayList<>();
  
  ArrayList<Integer> mAvailBackStackIndices;
  
  ArrayList<BackStackRecord> mBackStack;
  
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  
  ArrayList<BackStackRecord> mBackStackIndices;
  
  FragmentContainer mContainer;
  
  ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = 0;
  
  boolean mDestroyed;
  
  Runnable mExecCommit = new Runnable() {
      public void run() {
        FragmentManagerImpl.this.execPendingActions();
      }
    };
  
  boolean mExecutingActions;
  
  boolean mHavePendingDeferredStart;
  
  FragmentHostCallback mHost;
  
  private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<>();
  
  boolean mNeedMenuInvalidate;
  
  int mNextFragmentIndex = 0;
  
  String mNoTransactionsBecause;
  
  Fragment mParent;
  
  ArrayList<OpGenerator> mPendingActions;
  
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  
  Fragment mPrimaryNav;
  
  FragmentManagerNonConfig mSavedNonConfig;
  
  SparseArray<Parcelable> mStateArray = null;
  
  Bundle mStateBundle = null;
  
  boolean mStateSaved;
  
  boolean mStopped;
  
  ArrayList<Fragment> mTmpAddedFragments;
  
  ArrayList<Boolean> mTmpIsPop;
  
  ArrayList<BackStackRecord> mTmpRecords;
  
  static {
    DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5F);
    DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5F);
    ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5F);
    ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5F);
  }
  
  private void addAddedFragments(ArraySet<Fragment> paramArraySet) {
    int i = this.mCurState;
    if (i < 1)
      return; 
    int j = Math.min(i, 3);
    int k = this.mAdded.size();
    for (i = 0; i < k; i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment.mState < j) {
        moveToState(fragment, j, fragment.getNextAnim(), fragment.getNextTransition(), false);
        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded)
          paramArraySet.add(fragment); 
      } 
    } 
  }
  
  private void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator paramAnimationOrAnimator, int paramInt) {
    final View viewToAnimate = fragment.mView;
    final ViewGroup container = fragment.mContainer;
    viewGroup.startViewTransition(view);
    fragment.setStateAfterAnimating(paramInt);
    if (paramAnimationOrAnimator.animation != null) {
      EndViewTransitionAnimator endViewTransitionAnimator = new EndViewTransitionAnimator(paramAnimationOrAnimator.animation, viewGroup, view);
      fragment.setAnimatingAway(fragment.mView);
      endViewTransitionAnimator.setAnimationListener(new AnimationListenerWrapper(getAnimationListener((Animation)endViewTransitionAnimator)) {
            public void onAnimationEnd(Animation param1Animation) {
              super.onAnimationEnd(param1Animation);
              container.post(new Runnable() {
                    public void run() {
                      if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                      } 
                    }
                  });
            }
          });
      setHWLayerAnimListenerIfAlpha(view, paramAnimationOrAnimator);
      fragment.mView.startAnimation((Animation)endViewTransitionAnimator);
    } else {
      Animator animator = paramAnimationOrAnimator.animator;
      fragment.setAnimator(paramAnimationOrAnimator.animator);
      animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              container.endViewTransition(viewToAnimate);
              param1Animator = fragment.getAnimator();
              fragment.setAnimator(null);
              if (param1Animator != null && container.indexOfChild(viewToAnimate) < 0) {
                FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
                Fragment fragment = fragment;
                fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
              } 
            }
          });
      animator.setTarget(fragment.mView);
      setHWLayerAnimListenerIfAlpha(fragment.mView, paramAnimationOrAnimator);
      animator.start();
    } 
  }
  
  private void burpActive() {
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray != null)
      for (int i = sparseArray.size() - 1; i >= 0; i--) {
        if (this.mActive.valueAt(i) == null) {
          sparseArray = this.mActive;
          sparseArray.delete(sparseArray.keyAt(i));
        } 
      }  
  }
  
  private void checkStateLoss() {
    if (!isStateSaved()) {
      if (this.mNoTransactionsBecause == null)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can not perform this action inside of ");
      stringBuilder.append(this.mNoTransactionsBecause);
      throw new IllegalStateException(stringBuilder.toString());
    } 
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  private void cleanupExec() {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }
  
  private void dispatchStateChange(int paramInt) {
    try {
      this.mExecutingActions = true;
      moveToState(paramInt, false);
      this.mExecutingActions = false;
      return;
    } finally {
      this.mExecutingActions = false;
    } 
  }
  
  private void endAnimatingAwayFragments() {
    int i;
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray == null) {
      i = 0;
    } else {
      i = sparseArray.size();
    } 
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)this.mActive.valueAt(b);
      if (fragment != null)
        if (fragment.getAnimatingAway() != null) {
          int j = fragment.getStateAfterAnimating();
          View view = fragment.getAnimatingAway();
          Animation animation = view.getAnimation();
          if (animation != null) {
            animation.cancel();
            view.clearAnimation();
          } 
          fragment.setAnimatingAway(null);
          moveToState(fragment, j, 0, 0, false);
        } else if (fragment.getAnimator() != null) {
          fragment.getAnimator().end();
        }  
    } 
  }
  
  private void ensureExecReady(boolean paramBoolean) {
    if (!this.mExecutingActions) {
      if (this.mHost != null) {
        if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
          if (!paramBoolean)
            checkStateLoss(); 
          if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList<>();
            this.mTmpIsPop = new ArrayList<>();
          } 
          this.mExecutingActions = true;
          try {
            executePostponedTransaction(null, null);
            return;
          } finally {
            this.mExecutingActions = false;
          } 
        } 
        throw new IllegalStateException("Must be called from main thread of fragment host");
      } 
      throw new IllegalStateException("Fragment host has been destroyed");
    } 
    throw new IllegalStateException("FragmentManager is already executing transactions");
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
      boolean bool = ((Boolean)paramArrayList1.get(paramInt1)).booleanValue();
      boolean bool1 = true;
      if (bool) {
        backStackRecord.bumpBackStackNesting(-1);
        if (paramInt1 != paramInt2 - 1)
          bool1 = false; 
        backStackRecord.executePopOps(bool1);
      } else {
        backStackRecord.bumpBackStackNesting(1);
        backStackRecord.executeOps();
      } 
      paramInt1++;
    } 
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    boolean bool = ((BackStackRecord)paramArrayList.get(paramInt1)).mReorderingAllowed;
    ArrayList<Fragment> arrayList = this.mTmpAddedFragments;
    if (arrayList == null) {
      this.mTmpAddedFragments = new ArrayList<>();
    } else {
      arrayList.clear();
    } 
    this.mTmpAddedFragments.addAll(this.mAdded);
    Fragment fragment = getPrimaryNavigationFragment();
    int i = paramInt1;
    boolean bool1 = false;
    while (true) {
      boolean bool2 = true;
      if (i < paramInt2) {
        BackStackRecord backStackRecord = paramArrayList.get(i);
        if (!((Boolean)paramArrayList1.get(i)).booleanValue()) {
          fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
        } else {
          fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
        } 
        boolean bool3 = bool2;
        if (!bool1)
          if (backStackRecord.mAddToBackStack) {
            bool3 = bool2;
          } else {
            bool3 = false;
          }  
        i++;
        bool1 = bool3;
        continue;
      } 
      this.mTmpAddedFragments.clear();
      if (!bool)
        FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, paramInt2, false); 
      executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
      int j = paramInt2;
      if (bool) {
        ArraySet<Fragment> arraySet = new ArraySet();
        addAddedFragments(arraySet);
        j = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2, arraySet);
        makeRemovedFragmentsInvisible(arraySet);
      } 
      if (j != paramInt1 && bool) {
        FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, j, true);
        moveToState(this.mCurState, true);
      } 
      while (paramInt1 < paramInt2) {
        BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
        if (((Boolean)paramArrayList1.get(paramInt1)).booleanValue() && backStackRecord.mIndex >= 0) {
          freeBackStackIndex(backStackRecord.mIndex);
          backStackRecord.mIndex = -1;
        } 
        backStackRecord.runOnCommitRunnables();
        paramInt1++;
      } 
      if (bool1)
        reportBackStackChanged(); 
      return;
    } 
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnonnull -> 15
    //   9: iconst_0
    //   10: istore #4
    //   12: goto -> 21
    //   15: aload_3
    //   16: invokevirtual size : ()I
    //   19: istore #4
    //   21: iconst_0
    //   22: istore #5
    //   24: iload #5
    //   26: iload #4
    //   28: if_icmpge -> 232
    //   31: aload_0
    //   32: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   35: iload #5
    //   37: invokevirtual get : (I)Ljava/lang/Object;
    //   40: checkcast android/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener
    //   43: astore_3
    //   44: aload_1
    //   45: ifnull -> 101
    //   48: aload_3
    //   49: getfield mIsBack : Z
    //   52: ifne -> 101
    //   55: aload_1
    //   56: aload_3
    //   57: getfield mRecord : Landroid/support/v4/app/BackStackRecord;
    //   60: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   63: istore #6
    //   65: iload #6
    //   67: iconst_m1
    //   68: if_icmpeq -> 101
    //   71: aload_2
    //   72: iload #6
    //   74: invokevirtual get : (I)Ljava/lang/Object;
    //   77: checkcast java/lang/Boolean
    //   80: invokevirtual booleanValue : ()Z
    //   83: ifeq -> 101
    //   86: aload_3
    //   87: invokevirtual cancelTransaction : ()V
    //   90: iload #4
    //   92: istore #6
    //   94: iload #5
    //   96: istore #7
    //   98: goto -> 219
    //   101: aload_3
    //   102: invokevirtual isReady : ()Z
    //   105: ifne -> 144
    //   108: iload #4
    //   110: istore #6
    //   112: iload #5
    //   114: istore #7
    //   116: aload_1
    //   117: ifnull -> 219
    //   120: iload #4
    //   122: istore #6
    //   124: iload #5
    //   126: istore #7
    //   128: aload_3
    //   129: getfield mRecord : Landroid/support/v4/app/BackStackRecord;
    //   132: aload_1
    //   133: iconst_0
    //   134: aload_1
    //   135: invokevirtual size : ()I
    //   138: invokevirtual interactsWith : (Ljava/util/ArrayList;II)Z
    //   141: ifeq -> 219
    //   144: aload_0
    //   145: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   148: iload #5
    //   150: invokevirtual remove : (I)Ljava/lang/Object;
    //   153: pop
    //   154: iload #5
    //   156: iconst_1
    //   157: isub
    //   158: istore #7
    //   160: iload #4
    //   162: iconst_1
    //   163: isub
    //   164: istore #6
    //   166: aload_1
    //   167: ifnull -> 215
    //   170: aload_3
    //   171: getfield mIsBack : Z
    //   174: ifne -> 215
    //   177: aload_1
    //   178: aload_3
    //   179: getfield mRecord : Landroid/support/v4/app/BackStackRecord;
    //   182: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   185: istore #5
    //   187: iload #5
    //   189: iconst_m1
    //   190: if_icmpeq -> 215
    //   193: aload_2
    //   194: iload #5
    //   196: invokevirtual get : (I)Ljava/lang/Object;
    //   199: checkcast java/lang/Boolean
    //   202: invokevirtual booleanValue : ()Z
    //   205: ifeq -> 215
    //   208: aload_3
    //   209: invokevirtual cancelTransaction : ()V
    //   212: goto -> 219
    //   215: aload_3
    //   216: invokevirtual completeTransaction : ()V
    //   219: iload #7
    //   221: iconst_1
    //   222: iadd
    //   223: istore #5
    //   225: iload #6
    //   227: istore #4
    //   229: goto -> 24
    //   232: return
  }
  
  private Fragment findFragmentUnder(Fragment paramFragment) {
    ViewGroup viewGroup = paramFragment.mContainer;
    View view = paramFragment.mView;
    if (viewGroup == null || view == null)
      return null; 
    for (int i = this.mAdded.indexOf(paramFragment) - 1; i >= 0; i--) {
      paramFragment = this.mAdded.get(i);
      if (paramFragment.mContainer == viewGroup && paramFragment.mView != null)
        return paramFragment; 
    } 
    return null;
  }
  
  private void forcePostponedTransactions() {
    if (this.mPostponedTransactions != null)
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();  
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield mPendingActions : Ljava/util/ArrayList;
    //   8: ifnull -> 96
    //   11: aload_0
    //   12: getfield mPendingActions : Ljava/util/ArrayList;
    //   15: invokevirtual size : ()I
    //   18: ifne -> 24
    //   21: goto -> 96
    //   24: aload_0
    //   25: getfield mPendingActions : Ljava/util/ArrayList;
    //   28: invokevirtual size : ()I
    //   31: istore #4
    //   33: iconst_0
    //   34: istore #5
    //   36: iload #5
    //   38: iload #4
    //   40: if_icmpge -> 71
    //   43: iload_3
    //   44: aload_0
    //   45: getfield mPendingActions : Ljava/util/ArrayList;
    //   48: iload #5
    //   50: invokevirtual get : (I)Ljava/lang/Object;
    //   53: checkcast android/support/v4/app/FragmentManagerImpl$OpGenerator
    //   56: aload_1
    //   57: aload_2
    //   58: invokeinterface generateOps : (Ljava/util/ArrayList;Ljava/util/ArrayList;)Z
    //   63: ior
    //   64: istore_3
    //   65: iinc #5, 1
    //   68: goto -> 36
    //   71: aload_0
    //   72: getfield mPendingActions : Ljava/util/ArrayList;
    //   75: invokevirtual clear : ()V
    //   78: aload_0
    //   79: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   82: invokevirtual getHandler : ()Landroid/os/Handler;
    //   85: aload_0
    //   86: getfield mExecCommit : Ljava/lang/Runnable;
    //   89: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   92: aload_0
    //   93: monitorexit
    //   94: iload_3
    //   95: ireturn
    //   96: aload_0
    //   97: monitorexit
    //   98: iconst_0
    //   99: ireturn
    //   100: astore_1
    //   101: aload_0
    //   102: monitorexit
    //   103: aload_1
    //   104: athrow
    // Exception table:
    //   from	to	target	type
    //   4	21	100	finally
    //   24	33	100	finally
    //   43	65	100	finally
    //   71	94	100	finally
    //   96	98	100	finally
    //   101	103	100	finally
  }
  
  private static Animation.AnimationListener getAnimationListener(Animation paramAnimation) {
    IllegalAccessException illegalAccessException2 = null;
    NoSuchFieldException noSuchFieldException = null;
    try {
      if (sAnimationListenerField == null) {
        Field field = Animation.class.getDeclaredField("mListener");
        sAnimationListenerField = field;
        field.setAccessible(true);
      } 
      Animation.AnimationListener animationListener = (Animation.AnimationListener)sAnimationListenerField.get(paramAnimation);
    } catch (NoSuchFieldException noSuchFieldException1) {
      Log.e("FragmentManager", "No field with the name mListener is found in Animation class", noSuchFieldException1);
      noSuchFieldException1 = noSuchFieldException;
    } catch (IllegalAccessException illegalAccessException1) {
      Log.e("FragmentManager", "Cannot access Animation's mListener field", illegalAccessException1);
      illegalAccessException1 = illegalAccessException2;
    } 
    return (Animation.AnimationListener)illegalAccessException1;
  }
  
  static AnimationOrAnimator makeFadeAnimation(Context paramContext, float paramFloat1, float paramFloat2) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    return new AnimationOrAnimator((Animation)alphaAnimation);
  }
  
  static AnimationOrAnimator makeOpenCloseAnimation(Context paramContext, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    AnimationSet animationSet = new AnimationSet(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setInterpolator(DECELERATE_QUINT);
    scaleAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)scaleAnimation);
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)alphaAnimation);
    return new AnimationOrAnimator((Animation)animationSet);
  }
  
  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet) {
    int i = paramArraySet.size();
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)paramArraySet.valueAt(b);
      if (!fragment.mAdded) {
        View view = fragment.getView();
        fragment.mPostponedAlpha = view.getAlpha();
        view.setAlpha(0.0F);
      } 
    } 
  }
  
  static boolean modifiesAlpha(Animator paramAnimator) {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder;
    if (paramAnimator == null)
      return false; 
    if (paramAnimator instanceof ValueAnimator) {
      arrayOfPropertyValuesHolder = ((ValueAnimator)paramAnimator).getValues();
      for (byte b = 0; b < arrayOfPropertyValuesHolder.length; b++) {
        if ("alpha".equals(arrayOfPropertyValuesHolder[b].getPropertyName()))
          return true; 
      } 
    } else if (arrayOfPropertyValuesHolder instanceof AnimatorSet) {
      ArrayList<Animator> arrayList = ((AnimatorSet)arrayOfPropertyValuesHolder).getChildAnimations();
      for (byte b = 0; b < arrayList.size(); b++) {
        if (modifiesAlpha(arrayList.get(b)))
          return true; 
      } 
    } 
    return false;
  }
  
  static boolean modifiesAlpha(AnimationOrAnimator paramAnimationOrAnimator) {
    List list;
    if (paramAnimationOrAnimator.animation instanceof AlphaAnimation)
      return true; 
    if (paramAnimationOrAnimator.animation instanceof AnimationSet) {
      list = ((AnimationSet)paramAnimationOrAnimator.animation).getAnimations();
      for (byte b = 0; b < list.size(); b++) {
        if (list.get(b) instanceof AlphaAnimation)
          return true; 
      } 
      return false;
    } 
    return modifiesAlpha(((AnimationOrAnimator)list).animator);
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2) {
    execPendingActions();
    ensureExecReady(true);
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null && paramInt1 < 0 && paramString == null) {
      FragmentManager fragmentManager = fragment.peekChildFragmentManager();
      if (fragmentManager != null && fragmentManager.popBackStackImmediate())
        return true; 
    } 
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet) {
    int i = paramInt2;
    int j = paramInt2 - 1;
    while (j >= paramInt1) {
      boolean bool1;
      BackStackRecord backStackRecord = paramArrayList.get(j);
      boolean bool = ((Boolean)paramArrayList1.get(j)).booleanValue();
      if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(paramArrayList, j + 1, paramInt2)) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      int k = i;
      if (bool1) {
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList<>(); 
        StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bool);
        this.mPostponedTransactions.add(startEnterTransitionListener);
        backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
        if (bool) {
          backStackRecord.executeOps();
        } else {
          backStackRecord.executePopOps(false);
        } 
        k = i - 1;
        if (j != k) {
          paramArrayList.remove(j);
          paramArrayList.add(k, backStackRecord);
        } 
        addAddedFragments(paramArraySet);
      } 
      j--;
      i = k;
    } 
    return i;
  }
  
  private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    if (paramArrayList == null || paramArrayList.isEmpty())
      return; 
    if (paramArrayList1 != null && paramArrayList.size() == paramArrayList1.size()) {
      executePostponedTransaction(paramArrayList, paramArrayList1);
      int i = paramArrayList.size();
      int j = 0;
      int k = 0;
      while (k < i) {
        int m = j;
        int n = k;
        if (!((BackStackRecord)paramArrayList.get(k)).mReorderingAllowed) {
          if (j != k)
            executeOpsTogether(paramArrayList, paramArrayList1, j, k); 
          j = k + 1;
          n = j;
          if (((Boolean)paramArrayList1.get(k)).booleanValue())
            while (true) {
              n = j;
              if (j < i) {
                n = j;
                if (((Boolean)paramArrayList1.get(j)).booleanValue()) {
                  n = j;
                  if (!((BackStackRecord)paramArrayList.get(j)).mReorderingAllowed) {
                    j++;
                    continue;
                  } 
                } 
              } 
              break;
            }  
          executeOpsTogether(paramArrayList, paramArrayList1, k, n);
          m = n;
          n--;
        } 
        k = n + 1;
        j = m;
      } 
      if (j != i)
        executeOpsTogether(paramArrayList, paramArrayList1, j, i); 
      return;
    } 
    throw new IllegalStateException("Internal error with the back stack records");
  }
  
  public static int reverseTransit(int paramInt) {
    boolean bool = false;
    if (paramInt != 4097) {
      if (paramInt != 4099) {
        if (paramInt != 8194) {
          paramInt = bool;
        } else {
          paramInt = 4097;
        } 
      } else {
        paramInt = 4099;
      } 
    } else {
      paramInt = 8194;
    } 
    return paramInt;
  }
  
  private static void setHWLayerAnimListenerIfAlpha(View paramView, AnimationOrAnimator paramAnimationOrAnimator) {
    if (paramView == null || paramAnimationOrAnimator == null)
      return; 
    if (shouldRunOnHWLayer(paramView, paramAnimationOrAnimator))
      if (paramAnimationOrAnimator.animator != null) {
        paramAnimationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorOnHWLayerIfNeededListener(paramView));
      } else {
        Animation.AnimationListener animationListener = getAnimationListener(paramAnimationOrAnimator.animation);
        paramView.setLayerType(2, null);
        paramAnimationOrAnimator.animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(paramView, animationListener));
      }  
  }
  
  private static void setRetaining(FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    if (paramFragmentManagerNonConfig == null)
      return; 
    List<Fragment> list1 = paramFragmentManagerNonConfig.getFragments();
    if (list1 != null) {
      Iterator<Fragment> iterator = list1.iterator();
      while (iterator.hasNext())
        ((Fragment)iterator.next()).mRetaining = true; 
    } 
    List<FragmentManagerNonConfig> list = paramFragmentManagerNonConfig.getChildNonConfigs();
    if (list != null) {
      Iterator<FragmentManagerNonConfig> iterator = list.iterator();
      while (iterator.hasNext())
        setRetaining(iterator.next()); 
    } 
  }
  
  static boolean shouldRunOnHWLayer(View paramView, AnimationOrAnimator paramAnimationOrAnimator) {
    boolean bool = false;
    if (paramView == null || paramAnimationOrAnimator == null)
      return false; 
    if (Build.VERSION.SDK_INT >= 19 && paramView.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(paramView) && modifiesAlpha(paramAnimationOrAnimator))
      bool = true; 
    return bool;
  }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter printWriter = new PrintWriter((Writer)new LogWriter("FragmentManager"));
    FragmentHostCallback fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      try {
        fragmentHostCallback.onDump("  ", null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
    } else {
      try {
        dump("  ", null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
    } 
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean) {
    byte b = -1;
    if (paramInt != 4097) {
      if (paramInt != 4099) {
        if (paramInt != 8194) {
          paramInt = b;
        } else if (paramBoolean) {
          paramInt = 3;
        } else {
          paramInt = 4;
        } 
      } else if (paramBoolean) {
        paramInt = 5;
      } else {
        paramInt = 6;
      } 
    } else if (paramBoolean) {
      paramInt = 1;
    } else {
      paramInt = 2;
    } 
    return paramInt;
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList<>(); 
    this.mBackStack.add(paramBackStackRecord);
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("add: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    makeActive(paramFragment);
    if (!paramFragment.mDetached)
      if (!this.mAdded.contains(paramFragment)) {
        synchronized (this.mAdded) {
          this.mAdded.add(paramFragment);
          paramFragment.mAdded = true;
          paramFragment.mRemoving = false;
          if (paramFragment.mView == null)
            paramFragment.mHiddenChanged = false; 
          if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
            this.mNeedMenuInvalidate = true; 
          if (paramBoolean)
            moveToState(paramFragment); 
        } 
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment already added: ");
        stringBuilder.append(paramFragment);
        throw new IllegalStateException(stringBuilder.toString());
      }  
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList<>(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnull -> 111
    //   9: aload_0
    //   10: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifgt -> 22
    //   19: goto -> 111
    //   22: aload_0
    //   23: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   26: aload_0
    //   27: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: invokevirtual size : ()I
    //   33: iconst_1
    //   34: isub
    //   35: invokevirtual remove : (I)Ljava/lang/Object;
    //   38: checkcast java/lang/Integer
    //   41: invokevirtual intValue : ()I
    //   44: istore_2
    //   45: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   48: ifeq -> 97
    //   51: new java/lang/StringBuilder
    //   54: astore_3
    //   55: aload_3
    //   56: invokespecial <init> : ()V
    //   59: aload_3
    //   60: ldc_w 'Adding back stack index '
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_3
    //   68: iload_2
    //   69: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_3
    //   74: ldc_w ' with '
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_3
    //   82: aload_1
    //   83: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: ldc 'FragmentManager'
    //   89: aload_3
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   101: iload_2
    //   102: aload_1
    //   103: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   106: pop
    //   107: aload_0
    //   108: monitorexit
    //   109: iload_2
    //   110: ireturn
    //   111: aload_0
    //   112: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   115: ifnonnull -> 131
    //   118: new java/util/ArrayList
    //   121: astore_3
    //   122: aload_3
    //   123: invokespecial <init> : ()V
    //   126: aload_0
    //   127: aload_3
    //   128: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   131: aload_0
    //   132: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   135: invokevirtual size : ()I
    //   138: istore_2
    //   139: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   142: ifeq -> 191
    //   145: new java/lang/StringBuilder
    //   148: astore_3
    //   149: aload_3
    //   150: invokespecial <init> : ()V
    //   153: aload_3
    //   154: ldc_w 'Setting back stack index '
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: aload_3
    //   162: iload_2
    //   163: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   166: pop
    //   167: aload_3
    //   168: ldc_w ' to '
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_3
    //   176: aload_1
    //   177: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: ldc 'FragmentManager'
    //   183: aload_3
    //   184: invokevirtual toString : ()Ljava/lang/String;
    //   187: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   190: pop
    //   191: aload_0
    //   192: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   195: aload_1
    //   196: invokevirtual add : (Ljava/lang/Object;)Z
    //   199: pop
    //   200: aload_0
    //   201: monitorexit
    //   202: iload_2
    //   203: ireturn
    //   204: astore_1
    //   205: aload_0
    //   206: monitorexit
    //   207: aload_1
    //   208: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	204	finally
    //   22	97	204	finally
    //   97	109	204	finally
    //   111	131	204	finally
    //   131	191	204	finally
    //   191	202	204	finally
    //   205	207	204	finally
  }
  
  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment) {
    if (this.mHost == null) {
      this.mHost = paramFragmentHostCallback;
      this.mContainer = paramFragmentContainer;
      this.mParent = paramFragment;
      return;
    } 
    throw new IllegalStateException("Already attached");
  }
  
  public void attachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("attach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded)
        if (!this.mAdded.contains(paramFragment)) {
          if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add from attach: ");
            stringBuilder.append(paramFragment);
            Log.v("FragmentManager", stringBuilder.toString());
          } 
          synchronized (this.mAdded) {
            this.mAdded.add(paramFragment);
            paramFragment.mAdded = true;
            if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
              this.mNeedMenuInvalidate = true; 
          } 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Fragment already added: ");
          stringBuilder.append(paramFragment);
          throw new IllegalStateException(stringBuilder.toString());
        }  
    } 
  }
  
  public FragmentTransaction beginTransaction() {
    return new BackStackRecord(this);
  }
  
  void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if (paramBoolean1) {
      paramBackStackRecord.executePopOps(paramBoolean3);
    } else {
      paramBackStackRecord.executeOps();
    } 
    ArrayList<BackStackRecord> arrayList = new ArrayList(1);
    ArrayList<Boolean> arrayList1 = new ArrayList(1);
    arrayList.add(paramBackStackRecord);
    arrayList1.add(Boolean.valueOf(paramBoolean1));
    if (paramBoolean2)
      FragmentTransition.startTransitions(this, arrayList, arrayList1, 0, 1, true); 
    if (paramBoolean3)
      moveToState(this.mCurState, true); 
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray != null) {
      int i = sparseArray.size();
      for (byte b = 0; b < i; b++) {
        Fragment fragment = (Fragment)this.mActive.valueAt(b);
        if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && paramBackStackRecord.interactsWith(fragment.mContainerId)) {
          if (fragment.mPostponedAlpha > 0.0F)
            fragment.mView.setAlpha(fragment.mPostponedAlpha); 
          if (paramBoolean3) {
            fragment.mPostponedAlpha = 0.0F;
          } else {
            fragment.mPostponedAlpha = -1.0F;
            fragment.mIsNewlyAdded = false;
          } 
        } 
      } 
    } 
  }
  
  void completeShowHideFragment(final Fragment fragment) {
    if (fragment.mView != null) {
      AnimationOrAnimator animationOrAnimator = loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
      if (animationOrAnimator != null && animationOrAnimator.animator != null) {
        animationOrAnimator.animator.setTarget(fragment.mView);
        if (fragment.mHidden) {
          if (fragment.isHideReplaced()) {
            fragment.setHideReplaced(false);
          } else {
            final ViewGroup container = fragment.mContainer;
            final View animatingView = fragment.mView;
            viewGroup.startViewTransition(view);
            animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator param1Animator) {
                    container.endViewTransition(animatingView);
                    param1Animator.removeListener((Animator.AnimatorListener)this);
                    if (fragment.mView != null)
                      fragment.mView.setVisibility(8); 
                  }
                });
          } 
        } else {
          fragment.mView.setVisibility(0);
        } 
        setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
        animationOrAnimator.animator.start();
      } else {
        boolean bool;
        if (animationOrAnimator != null) {
          setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
          fragment.mView.startAnimation(animationOrAnimator.animation);
          animationOrAnimator.animation.start();
        } 
        if (fragment.mHidden && !fragment.isHideReplaced()) {
          bool = true;
        } else {
          bool = false;
        } 
        fragment.mView.setVisibility(bool);
        if (fragment.isHideReplaced())
          fragment.setHideReplaced(false); 
      } 
    } 
    if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible)
      this.mNeedMenuInvalidate = true; 
    fragment.mHiddenChanged = false;
    fragment.onHiddenChanged(fragment.mHidden);
  }
  
  public void detachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("detach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("remove from detach: ");
          stringBuilder.append(paramFragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        synchronized (this.mAdded) {
          this.mAdded.remove(paramFragment);
          if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
            this.mNeedMenuInvalidate = true; 
          paramFragment.mAdded = false;
        } 
      } 
    } 
  }
  
  public void dispatchActivityCreated() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(2);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration) {
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.performConfigurationChanged(paramConfiguration); 
    } 
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null && fragment.performContextItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchCreate() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(1);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    if (this.mCurState < 1)
      return false; 
    boolean bool = false;
    ArrayList<Fragment> arrayList = null;
    byte b = 0;
    while (b < this.mAdded.size()) {
      Fragment fragment = this.mAdded.get(b);
      boolean bool1 = bool;
      ArrayList<Fragment> arrayList1 = arrayList;
      if (fragment != null) {
        bool1 = bool;
        arrayList1 = arrayList;
        if (fragment.performCreateOptionsMenu(paramMenu, paramMenuInflater)) {
          bool1 = true;
          arrayList1 = arrayList;
          if (arrayList == null)
            arrayList1 = new ArrayList(); 
          arrayList1.add(fragment);
        } 
      } 
      b++;
      bool = bool1;
      arrayList = arrayList1;
    } 
    if (this.mCreatedMenus != null)
      for (b = 0; b < this.mCreatedMenus.size(); b++) {
        Fragment fragment = this.mCreatedMenus.get(b);
        if (arrayList == null || !arrayList.contains(fragment))
          fragment.onDestroyOptionsMenu(); 
      }  
    this.mCreatedMenus = arrayList;
    return bool;
  }
  
  public void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions();
    dispatchStateChange(0);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
  }
  
  public void dispatchDestroyView() {
    dispatchStateChange(1);
  }
  
  public void dispatchLowMemory() {
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.performLowMemory(); 
    } 
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performMultiWindowModeChanged(paramBoolean); 
    } 
  }
  
  void dispatchOnFragmentActivityCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentDestroyed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentDetached(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPaused(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPreAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentPreCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentResumed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentSaveInstanceState(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentStarted(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentStopped(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentViewCreated(Fragment paramFragment, View paramView, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, paramFragment, paramView, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentViewDestroyed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, paramFragment); 
    } 
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null && fragment.performOptionsItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mCurState < 1)
      return; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.performOptionsMenuClosed(paramMenu); 
    } 
  }
  
  public void dispatchPause() {
    dispatchStateChange(3);
  }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performPictureInPictureModeChanged(paramBoolean); 
    } 
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    if (this.mCurState < 1)
      return false; 
    boolean bool = false;
    byte b = 0;
    while (b < this.mAdded.size()) {
      Fragment fragment = this.mAdded.get(b);
      boolean bool1 = bool;
      if (fragment != null) {
        bool1 = bool;
        if (fragment.performPrepareOptionsMenu(paramMenu))
          bool1 = true; 
      } 
      b++;
      bool = bool1;
    } 
    return bool;
  }
  
  public void dispatchResume() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(4);
  }
  
  public void dispatchStart() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(3);
  }
  
  public void dispatchStop() {
    this.mStopped = true;
    dispatchStateChange(2);
  }
  
  void doPendingDeferredStart() {
    if (this.mHavePendingDeferredStart) {
      this.mHavePendingDeferredStart = false;
      startPendingDeferredFragments();
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #5
    //   9: aload #5
    //   11: aload_1
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload #5
    //   18: ldc_w '    '
    //   21: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload #5
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: astore #5
    //   32: aload_0
    //   33: getfield mActive : Landroid/util/SparseArray;
    //   36: astore #6
    //   38: aload #6
    //   40: ifnull -> 162
    //   43: aload #6
    //   45: invokevirtual size : ()I
    //   48: istore #7
    //   50: iload #7
    //   52: ifle -> 162
    //   55: aload_3
    //   56: aload_1
    //   57: invokevirtual print : (Ljava/lang/String;)V
    //   60: aload_3
    //   61: ldc_w 'Active Fragments in '
    //   64: invokevirtual print : (Ljava/lang/String;)V
    //   67: aload_3
    //   68: aload_0
    //   69: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   72: invokestatic toHexString : (I)Ljava/lang/String;
    //   75: invokevirtual print : (Ljava/lang/String;)V
    //   78: aload_3
    //   79: ldc_w ':'
    //   82: invokevirtual println : (Ljava/lang/String;)V
    //   85: iconst_0
    //   86: istore #8
    //   88: iload #8
    //   90: iload #7
    //   92: if_icmpge -> 162
    //   95: aload_0
    //   96: getfield mActive : Landroid/util/SparseArray;
    //   99: iload #8
    //   101: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   104: checkcast android/support/v4/app/Fragment
    //   107: astore #6
    //   109: aload_3
    //   110: aload_1
    //   111: invokevirtual print : (Ljava/lang/String;)V
    //   114: aload_3
    //   115: ldc_w '  #'
    //   118: invokevirtual print : (Ljava/lang/String;)V
    //   121: aload_3
    //   122: iload #8
    //   124: invokevirtual print : (I)V
    //   127: aload_3
    //   128: ldc_w ': '
    //   131: invokevirtual print : (Ljava/lang/String;)V
    //   134: aload_3
    //   135: aload #6
    //   137: invokevirtual println : (Ljava/lang/Object;)V
    //   140: aload #6
    //   142: ifnull -> 156
    //   145: aload #6
    //   147: aload #5
    //   149: aload_2
    //   150: aload_3
    //   151: aload #4
    //   153: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   156: iinc #8, 1
    //   159: goto -> 88
    //   162: aload_0
    //   163: getfield mAdded : Ljava/util/ArrayList;
    //   166: invokevirtual size : ()I
    //   169: istore #7
    //   171: iload #7
    //   173: ifle -> 252
    //   176: aload_3
    //   177: aload_1
    //   178: invokevirtual print : (Ljava/lang/String;)V
    //   181: aload_3
    //   182: ldc_w 'Added Fragments:'
    //   185: invokevirtual println : (Ljava/lang/String;)V
    //   188: iconst_0
    //   189: istore #8
    //   191: iload #8
    //   193: iload #7
    //   195: if_icmpge -> 252
    //   198: aload_0
    //   199: getfield mAdded : Ljava/util/ArrayList;
    //   202: iload #8
    //   204: invokevirtual get : (I)Ljava/lang/Object;
    //   207: checkcast android/support/v4/app/Fragment
    //   210: astore #6
    //   212: aload_3
    //   213: aload_1
    //   214: invokevirtual print : (Ljava/lang/String;)V
    //   217: aload_3
    //   218: ldc_w '  #'
    //   221: invokevirtual print : (Ljava/lang/String;)V
    //   224: aload_3
    //   225: iload #8
    //   227: invokevirtual print : (I)V
    //   230: aload_3
    //   231: ldc_w ': '
    //   234: invokevirtual print : (Ljava/lang/String;)V
    //   237: aload_3
    //   238: aload #6
    //   240: invokevirtual toString : ()Ljava/lang/String;
    //   243: invokevirtual println : (Ljava/lang/String;)V
    //   246: iinc #8, 1
    //   249: goto -> 191
    //   252: aload_0
    //   253: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   256: astore #6
    //   258: aload #6
    //   260: ifnull -> 351
    //   263: aload #6
    //   265: invokevirtual size : ()I
    //   268: istore #7
    //   270: iload #7
    //   272: ifle -> 351
    //   275: aload_3
    //   276: aload_1
    //   277: invokevirtual print : (Ljava/lang/String;)V
    //   280: aload_3
    //   281: ldc_w 'Fragments Created Menus:'
    //   284: invokevirtual println : (Ljava/lang/String;)V
    //   287: iconst_0
    //   288: istore #8
    //   290: iload #8
    //   292: iload #7
    //   294: if_icmpge -> 351
    //   297: aload_0
    //   298: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   301: iload #8
    //   303: invokevirtual get : (I)Ljava/lang/Object;
    //   306: checkcast android/support/v4/app/Fragment
    //   309: astore #6
    //   311: aload_3
    //   312: aload_1
    //   313: invokevirtual print : (Ljava/lang/String;)V
    //   316: aload_3
    //   317: ldc_w '  #'
    //   320: invokevirtual print : (Ljava/lang/String;)V
    //   323: aload_3
    //   324: iload #8
    //   326: invokevirtual print : (I)V
    //   329: aload_3
    //   330: ldc_w ': '
    //   333: invokevirtual print : (Ljava/lang/String;)V
    //   336: aload_3
    //   337: aload #6
    //   339: invokevirtual toString : ()Ljava/lang/String;
    //   342: invokevirtual println : (Ljava/lang/String;)V
    //   345: iinc #8, 1
    //   348: goto -> 290
    //   351: aload_0
    //   352: getfield mBackStack : Ljava/util/ArrayList;
    //   355: astore #6
    //   357: aload #6
    //   359: ifnull -> 461
    //   362: aload #6
    //   364: invokevirtual size : ()I
    //   367: istore #7
    //   369: iload #7
    //   371: ifle -> 461
    //   374: aload_3
    //   375: aload_1
    //   376: invokevirtual print : (Ljava/lang/String;)V
    //   379: aload_3
    //   380: ldc_w 'Back Stack:'
    //   383: invokevirtual println : (Ljava/lang/String;)V
    //   386: iconst_0
    //   387: istore #8
    //   389: iload #8
    //   391: iload #7
    //   393: if_icmpge -> 461
    //   396: aload_0
    //   397: getfield mBackStack : Ljava/util/ArrayList;
    //   400: iload #8
    //   402: invokevirtual get : (I)Ljava/lang/Object;
    //   405: checkcast android/support/v4/app/BackStackRecord
    //   408: astore #6
    //   410: aload_3
    //   411: aload_1
    //   412: invokevirtual print : (Ljava/lang/String;)V
    //   415: aload_3
    //   416: ldc_w '  #'
    //   419: invokevirtual print : (Ljava/lang/String;)V
    //   422: aload_3
    //   423: iload #8
    //   425: invokevirtual print : (I)V
    //   428: aload_3
    //   429: ldc_w ': '
    //   432: invokevirtual print : (Ljava/lang/String;)V
    //   435: aload_3
    //   436: aload #6
    //   438: invokevirtual toString : ()Ljava/lang/String;
    //   441: invokevirtual println : (Ljava/lang/String;)V
    //   444: aload #6
    //   446: aload #5
    //   448: aload_2
    //   449: aload_3
    //   450: aload #4
    //   452: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   455: iinc #8, 1
    //   458: goto -> 389
    //   461: aload_0
    //   462: monitorenter
    //   463: aload_0
    //   464: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   467: ifnull -> 555
    //   470: aload_0
    //   471: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   474: invokevirtual size : ()I
    //   477: istore #7
    //   479: iload #7
    //   481: ifle -> 555
    //   484: aload_3
    //   485: aload_1
    //   486: invokevirtual print : (Ljava/lang/String;)V
    //   489: aload_3
    //   490: ldc_w 'Back Stack Indices:'
    //   493: invokevirtual println : (Ljava/lang/String;)V
    //   496: iconst_0
    //   497: istore #8
    //   499: iload #8
    //   501: iload #7
    //   503: if_icmpge -> 555
    //   506: aload_0
    //   507: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   510: iload #8
    //   512: invokevirtual get : (I)Ljava/lang/Object;
    //   515: checkcast android/support/v4/app/BackStackRecord
    //   518: astore_2
    //   519: aload_3
    //   520: aload_1
    //   521: invokevirtual print : (Ljava/lang/String;)V
    //   524: aload_3
    //   525: ldc_w '  #'
    //   528: invokevirtual print : (Ljava/lang/String;)V
    //   531: aload_3
    //   532: iload #8
    //   534: invokevirtual print : (I)V
    //   537: aload_3
    //   538: ldc_w ': '
    //   541: invokevirtual print : (Ljava/lang/String;)V
    //   544: aload_3
    //   545: aload_2
    //   546: invokevirtual println : (Ljava/lang/Object;)V
    //   549: iinc #8, 1
    //   552: goto -> 499
    //   555: aload_0
    //   556: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   559: ifnull -> 598
    //   562: aload_0
    //   563: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   566: invokevirtual size : ()I
    //   569: ifle -> 598
    //   572: aload_3
    //   573: aload_1
    //   574: invokevirtual print : (Ljava/lang/String;)V
    //   577: aload_3
    //   578: ldc_w 'mAvailBackStackIndices: '
    //   581: invokevirtual print : (Ljava/lang/String;)V
    //   584: aload_3
    //   585: aload_0
    //   586: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   589: invokevirtual toArray : ()[Ljava/lang/Object;
    //   592: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   595: invokevirtual println : (Ljava/lang/String;)V
    //   598: aload_0
    //   599: monitorexit
    //   600: aload_0
    //   601: getfield mPendingActions : Ljava/util/ArrayList;
    //   604: astore_2
    //   605: aload_2
    //   606: ifnull -> 691
    //   609: aload_2
    //   610: invokevirtual size : ()I
    //   613: istore #7
    //   615: iload #7
    //   617: ifle -> 691
    //   620: aload_3
    //   621: aload_1
    //   622: invokevirtual print : (Ljava/lang/String;)V
    //   625: aload_3
    //   626: ldc_w 'Pending Actions:'
    //   629: invokevirtual println : (Ljava/lang/String;)V
    //   632: iconst_0
    //   633: istore #8
    //   635: iload #8
    //   637: iload #7
    //   639: if_icmpge -> 691
    //   642: aload_0
    //   643: getfield mPendingActions : Ljava/util/ArrayList;
    //   646: iload #8
    //   648: invokevirtual get : (I)Ljava/lang/Object;
    //   651: checkcast android/support/v4/app/FragmentManagerImpl$OpGenerator
    //   654: astore_2
    //   655: aload_3
    //   656: aload_1
    //   657: invokevirtual print : (Ljava/lang/String;)V
    //   660: aload_3
    //   661: ldc_w '  #'
    //   664: invokevirtual print : (Ljava/lang/String;)V
    //   667: aload_3
    //   668: iload #8
    //   670: invokevirtual print : (I)V
    //   673: aload_3
    //   674: ldc_w ': '
    //   677: invokevirtual print : (Ljava/lang/String;)V
    //   680: aload_3
    //   681: aload_2
    //   682: invokevirtual println : (Ljava/lang/Object;)V
    //   685: iinc #8, 1
    //   688: goto -> 635
    //   691: aload_3
    //   692: aload_1
    //   693: invokevirtual print : (Ljava/lang/String;)V
    //   696: aload_3
    //   697: ldc_w 'FragmentManager misc state:'
    //   700: invokevirtual println : (Ljava/lang/String;)V
    //   703: aload_3
    //   704: aload_1
    //   705: invokevirtual print : (Ljava/lang/String;)V
    //   708: aload_3
    //   709: ldc_w '  mHost='
    //   712: invokevirtual print : (Ljava/lang/String;)V
    //   715: aload_3
    //   716: aload_0
    //   717: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   720: invokevirtual println : (Ljava/lang/Object;)V
    //   723: aload_3
    //   724: aload_1
    //   725: invokevirtual print : (Ljava/lang/String;)V
    //   728: aload_3
    //   729: ldc_w '  mContainer='
    //   732: invokevirtual print : (Ljava/lang/String;)V
    //   735: aload_3
    //   736: aload_0
    //   737: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   740: invokevirtual println : (Ljava/lang/Object;)V
    //   743: aload_0
    //   744: getfield mParent : Landroid/support/v4/app/Fragment;
    //   747: ifnull -> 770
    //   750: aload_3
    //   751: aload_1
    //   752: invokevirtual print : (Ljava/lang/String;)V
    //   755: aload_3
    //   756: ldc_w '  mParent='
    //   759: invokevirtual print : (Ljava/lang/String;)V
    //   762: aload_3
    //   763: aload_0
    //   764: getfield mParent : Landroid/support/v4/app/Fragment;
    //   767: invokevirtual println : (Ljava/lang/Object;)V
    //   770: aload_3
    //   771: aload_1
    //   772: invokevirtual print : (Ljava/lang/String;)V
    //   775: aload_3
    //   776: ldc_w '  mCurState='
    //   779: invokevirtual print : (Ljava/lang/String;)V
    //   782: aload_3
    //   783: aload_0
    //   784: getfield mCurState : I
    //   787: invokevirtual print : (I)V
    //   790: aload_3
    //   791: ldc_w ' mStateSaved='
    //   794: invokevirtual print : (Ljava/lang/String;)V
    //   797: aload_3
    //   798: aload_0
    //   799: getfield mStateSaved : Z
    //   802: invokevirtual print : (Z)V
    //   805: aload_3
    //   806: ldc_w ' mStopped='
    //   809: invokevirtual print : (Ljava/lang/String;)V
    //   812: aload_3
    //   813: aload_0
    //   814: getfield mStopped : Z
    //   817: invokevirtual print : (Z)V
    //   820: aload_3
    //   821: ldc_w ' mDestroyed='
    //   824: invokevirtual print : (Ljava/lang/String;)V
    //   827: aload_3
    //   828: aload_0
    //   829: getfield mDestroyed : Z
    //   832: invokevirtual println : (Z)V
    //   835: aload_0
    //   836: getfield mNeedMenuInvalidate : Z
    //   839: ifeq -> 862
    //   842: aload_3
    //   843: aload_1
    //   844: invokevirtual print : (Ljava/lang/String;)V
    //   847: aload_3
    //   848: ldc_w '  mNeedMenuInvalidate='
    //   851: invokevirtual print : (Ljava/lang/String;)V
    //   854: aload_3
    //   855: aload_0
    //   856: getfield mNeedMenuInvalidate : Z
    //   859: invokevirtual println : (Z)V
    //   862: aload_0
    //   863: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   866: ifnull -> 889
    //   869: aload_3
    //   870: aload_1
    //   871: invokevirtual print : (Ljava/lang/String;)V
    //   874: aload_3
    //   875: ldc_w '  mNoTransactionsBecause='
    //   878: invokevirtual print : (Ljava/lang/String;)V
    //   881: aload_3
    //   882: aload_0
    //   883: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   886: invokevirtual println : (Ljava/lang/String;)V
    //   889: return
    //   890: astore_1
    //   891: aload_0
    //   892: monitorexit
    //   893: aload_1
    //   894: athrow
    // Exception table:
    //   from	to	target	type
    //   463	479	890	finally
    //   484	496	890	finally
    //   506	549	890	finally
    //   555	598	890	finally
    //   598	600	890	finally
    //   891	893	890	finally
  }
  
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: ifne -> 8
    //   4: aload_0
    //   5: invokespecial checkStateLoss : ()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield mDestroyed : Z
    //   14: ifne -> 63
    //   17: aload_0
    //   18: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   21: ifnonnull -> 27
    //   24: goto -> 63
    //   27: aload_0
    //   28: getfield mPendingActions : Ljava/util/ArrayList;
    //   31: ifnonnull -> 47
    //   34: new java/util/ArrayList
    //   37: astore_3
    //   38: aload_3
    //   39: invokespecial <init> : ()V
    //   42: aload_0
    //   43: aload_3
    //   44: putfield mPendingActions : Ljava/util/ArrayList;
    //   47: aload_0
    //   48: getfield mPendingActions : Ljava/util/ArrayList;
    //   51: aload_1
    //   52: invokevirtual add : (Ljava/lang/Object;)Z
    //   55: pop
    //   56: aload_0
    //   57: invokevirtual scheduleCommit : ()V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: iload_2
    //   64: ifeq -> 70
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: new java/lang/IllegalStateException
    //   73: astore_1
    //   74: aload_1
    //   75: ldc_w 'Activity has been destroyed'
    //   78: invokespecial <init> : (Ljava/lang/String;)V
    //   81: aload_1
    //   82: athrow
    //   83: astore_1
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_1
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   10	24	83	finally
    //   27	47	83	finally
    //   47	62	83	finally
    //   67	69	83	finally
    //   70	83	83	finally
    //   84	86	83	finally
  }
  
  void ensureInflatedFragmentView(Fragment paramFragment) {
    if (paramFragment.mFromLayout && !paramFragment.mPerformedCreateView) {
      paramFragment.performCreateView(paramFragment.performGetLayoutInflater(paramFragment.mSavedFragmentState), null, paramFragment.mSavedFragmentState);
      if (paramFragment.mView != null) {
        paramFragment.mInnerView = paramFragment.mView;
        paramFragment.mView.setSaveFromParentEnabled(false);
        if (paramFragment.mHidden)
          paramFragment.mView.setVisibility(8); 
        paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
        dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
      } else {
        paramFragment.mInnerView = null;
      } 
    } 
  }
  
  public boolean execPendingActions() {
    ensureExecReady(true);
    boolean bool = false;
    while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    if (paramBoolean && (this.mHost == null || this.mDestroyed))
      return; 
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    burpActive();
  }
  
  public boolean executePendingTransactions() {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }
  
  public Fragment findFragmentById(int paramInt) {
    int i;
    for (i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray != null)
      for (i = sparseArray.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mActive.valueAt(i);
        if (fragment != null && fragment.mFragmentId == paramInt)
          return fragment; 
      }  
    return null;
  }
  
  public Fragment findFragmentByTag(String paramString) {
    if (paramString != null)
      for (int i = this.mAdded.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray != null && paramString != null)
      for (int i = sparseArray.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mActive.valueAt(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    return null;
  }
  
  public Fragment findFragmentByWho(String paramString) {
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray != null && paramString != null)
      for (int i = sparseArray.size() - 1; i >= 0; i--) {
        Fragment fragment = (Fragment)this.mActive.valueAt(i);
        if (fragment != null) {
          fragment = fragment.findFragmentByWho(paramString);
          if (fragment != null)
            return fragment; 
        } 
      }  
    return null;
  }
  
  public void freeBackStackIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: iload_1
    //   7: aconst_null
    //   8: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   16: ifnonnull -> 32
    //   19: new java/util/ArrayList
    //   22: astore_2
    //   23: aload_2
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: aload_2
    //   29: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   32: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   35: ifeq -> 70
    //   38: new java/lang/StringBuilder
    //   41: astore_2
    //   42: aload_2
    //   43: invokespecial <init> : ()V
    //   46: aload_2
    //   47: ldc_w 'Freeing back stack index '
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_2
    //   55: iload_1
    //   56: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: ldc 'FragmentManager'
    //   62: aload_2
    //   63: invokevirtual toString : ()Ljava/lang/String;
    //   66: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   74: iload_1
    //   75: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   78: invokevirtual add : (Ljava/lang/Object;)Z
    //   81: pop
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: astore_2
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	85	finally
    //   32	70	85	finally
    //   70	84	85	finally
    //   86	88	85	finally
  }
  
  int getActiveFragmentCount() {
    SparseArray<Fragment> sparseArray = this.mActive;
    return (sparseArray == null) ? 0 : sparseArray.size();
  }
  
  List<Fragment> getActiveFragments() {
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray == null)
      return null; 
    int i = sparseArray.size();
    ArrayList<Object> arrayList = new ArrayList(i);
    for (byte b = 0; b < i; b++)
      arrayList.add(this.mActive.valueAt(b)); 
    return arrayList;
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt) {
    return this.mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount() {
    boolean bool;
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    if (arrayList != null) {
      bool = arrayList.size();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    int i = paramBundle.getInt(paramString, -1);
    if (i == -1)
      return null; 
    Fragment fragment = (Fragment)this.mActive.get(i);
    if (fragment == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment no longer exists for key ");
      stringBuilder.append(paramString);
      stringBuilder.append(": index ");
      stringBuilder.append(i);
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    return fragment;
  }
  
  public List<Fragment> getFragments() {
    if (this.mAdded.isEmpty())
      return Collections.emptyList(); 
    synchronized (this.mAdded) {
      return (List)this.mAdded.clone();
    } 
  }
  
  LayoutInflater.Factory2 getLayoutInflaterFactory() {
    return this;
  }
  
  public Fragment getPrimaryNavigationFragment() {
    return this.mPrimaryNav;
  }
  
  public void hideFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("hide: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      paramFragment.mHiddenChanged = true ^ paramFragment.mHiddenChanged;
    } 
  }
  
  public boolean isDestroyed() {
    return this.mDestroyed;
  }
  
  boolean isStateAtLeast(int paramInt) {
    boolean bool;
    if (this.mCurState >= paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStateSaved() {
    return (this.mStateSaved || this.mStopped);
  }
  
  AnimationOrAnimator loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2) {
    int i = paramFragment.getNextAnim();
    Animation animation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, i);
    if (animation != null)
      return new AnimationOrAnimator(animation); 
    Animator animator = paramFragment.onCreateAnimator(paramInt1, paramBoolean, i);
    if (animator != null)
      return new AnimationOrAnimator(animator); 
    if (i != 0) {
      boolean bool = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(i));
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (bool)
        try {
          Animation animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
          if (animation1 != null)
            return new AnimationOrAnimator(animation1); 
          bool2 = true;
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          throw notFoundException;
        } catch (RuntimeException runtimeException) {
          bool2 = bool1;
        }  
      if (!bool2)
        try {
          animator = AnimatorInflater.loadAnimator(this.mHost.getContext(), i);
          if (animator != null)
            return new AnimationOrAnimator(animator); 
        } catch (RuntimeException runtimeException) {
          Animation animation1;
          if (!bool) {
            animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
            if (animation1 != null)
              return new AnimationOrAnimator(animation1); 
          } else {
            throw animation1;
          } 
        }  
    } 
    if (paramInt1 == 0)
      return null; 
    paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
    if (paramInt1 < 0)
      return null; 
    switch (paramInt1) {
      default:
        paramInt1 = paramInt2;
        if (paramInt2 == 0) {
          paramInt1 = paramInt2;
          if (this.mHost.onHasWindowAnimations())
            paramInt1 = this.mHost.onGetWindowAnimations(); 
        } 
        break;
      case 6:
        return makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
      case 5:
        return makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
      case 4:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
      case 3:
        return makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
      case 2:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
      case 1:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
    } 
    return (AnimationOrAnimator)((paramInt1 == 0) ? null : null);
  }
  
  void makeActive(Fragment paramFragment) {
    if (paramFragment.mIndex >= 0)
      return; 
    int i = this.mNextFragmentIndex;
    this.mNextFragmentIndex = i + 1;
    paramFragment.setIndex(i, this.mParent);
    if (this.mActive == null)
      this.mActive = new SparseArray(); 
    this.mActive.put(paramFragment.mIndex, paramFragment);
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Allocated fragment index ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  void makeInactive(Fragment paramFragment) {
    if (paramFragment.mIndex < 0)
      return; 
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Freeing fragment index ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    this.mActive.put(paramFragment.mIndex, null);
    paramFragment.initState();
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment) {
    if (paramFragment == null)
      return; 
    int i = this.mCurState;
    int j = i;
    if (paramFragment.mRemoving)
      if (paramFragment.isInBackStack()) {
        j = Math.min(i, 1);
      } else {
        j = Math.min(i, 0);
      }  
    moveToState(paramFragment, j, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
    if (paramFragment.mView != null) {
      Fragment fragment = findFragmentUnder(paramFragment);
      if (fragment != null) {
        View view = fragment.mView;
        ViewGroup viewGroup = paramFragment.mContainer;
        i = viewGroup.indexOfChild(view);
        j = viewGroup.indexOfChild(paramFragment.mView);
        if (j < i) {
          viewGroup.removeViewAt(j);
          viewGroup.addView(paramFragment.mView, i);
        } 
      } 
      if (paramFragment.mIsNewlyAdded && paramFragment.mContainer != null) {
        if (paramFragment.mPostponedAlpha > 0.0F)
          paramFragment.mView.setAlpha(paramFragment.mPostponedAlpha); 
        paramFragment.mPostponedAlpha = 0.0F;
        paramFragment.mIsNewlyAdded = false;
        AnimationOrAnimator animationOrAnimator = loadAnimation(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
        if (animationOrAnimator != null) {
          setHWLayerAnimListenerIfAlpha(paramFragment.mView, animationOrAnimator);
          if (animationOrAnimator.animation != null) {
            paramFragment.mView.startAnimation(animationOrAnimator.animation);
          } else {
            animationOrAnimator.animator.setTarget(paramFragment.mView);
            animationOrAnimator.animator.start();
          } 
        } 
      } 
    } 
    if (paramFragment.mHiddenChanged)
      completeShowHideFragment(paramFragment); 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    if (this.mHost != null || paramInt == 0) {
      if (!paramBoolean && paramInt == this.mCurState)
        return; 
      this.mCurState = paramInt;
      if (this.mActive != null) {
        int i = this.mAdded.size();
        for (paramInt = 0; paramInt < i; paramInt++)
          moveFragmentToExpectedState(this.mAdded.get(paramInt)); 
        i = this.mActive.size();
        for (paramInt = 0; paramInt < i; paramInt++) {
          Fragment fragment = (Fragment)this.mActive.valueAt(paramInt);
          if (fragment != null && (fragment.mRemoving || fragment.mDetached) && !fragment.mIsNewlyAdded)
            moveFragmentToExpectedState(fragment); 
        } 
        startPendingDeferredFragments();
        if (this.mNeedMenuInvalidate) {
          FragmentHostCallback fragmentHostCallback = this.mHost;
          if (fragmentHostCallback != null && this.mCurState == 4) {
            fragmentHostCallback.onSupportInvalidateOptionsMenu();
            this.mNeedMenuInvalidate = false;
          } 
        } 
      } 
      return;
    } 
    throw new IllegalStateException("No activity");
  }
  
  void moveToState(Fragment paramFragment) {
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mAdded : Z
    //   4: istore #6
    //   6: iconst_1
    //   7: istore #7
    //   9: iload #6
    //   11: ifeq -> 27
    //   14: aload_1
    //   15: getfield mDetached : Z
    //   18: ifeq -> 24
    //   21: goto -> 27
    //   24: goto -> 41
    //   27: iload_2
    //   28: istore #8
    //   30: iload #8
    //   32: istore_2
    //   33: iload #8
    //   35: iconst_1
    //   36: if_icmple -> 41
    //   39: iconst_1
    //   40: istore_2
    //   41: iload_2
    //   42: istore #8
    //   44: aload_1
    //   45: getfield mRemoving : Z
    //   48: ifeq -> 88
    //   51: iload_2
    //   52: istore #8
    //   54: iload_2
    //   55: aload_1
    //   56: getfield mState : I
    //   59: if_icmple -> 88
    //   62: aload_1
    //   63: getfield mState : I
    //   66: ifne -> 82
    //   69: aload_1
    //   70: invokevirtual isInBackStack : ()Z
    //   73: ifeq -> 82
    //   76: iconst_1
    //   77: istore #8
    //   79: goto -> 88
    //   82: aload_1
    //   83: getfield mState : I
    //   86: istore #8
    //   88: iload #8
    //   90: istore_2
    //   91: aload_1
    //   92: getfield mDeferStart : Z
    //   95: ifeq -> 120
    //   98: iload #8
    //   100: istore_2
    //   101: aload_1
    //   102: getfield mState : I
    //   105: iconst_3
    //   106: if_icmpge -> 120
    //   109: iload #8
    //   111: istore_2
    //   112: iload #8
    //   114: iconst_2
    //   115: if_icmple -> 120
    //   118: iconst_2
    //   119: istore_2
    //   120: aload_1
    //   121: getfield mState : I
    //   124: iload_2
    //   125: if_icmpgt -> 1341
    //   128: aload_1
    //   129: getfield mFromLayout : Z
    //   132: ifeq -> 143
    //   135: aload_1
    //   136: getfield mInLayout : Z
    //   139: ifne -> 143
    //   142: return
    //   143: aload_1
    //   144: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   147: ifnonnull -> 157
    //   150: aload_1
    //   151: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   154: ifnull -> 179
    //   157: aload_1
    //   158: aconst_null
    //   159: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   162: aload_1
    //   163: aconst_null
    //   164: invokevirtual setAnimator : (Landroid/animation/Animator;)V
    //   167: aload_0
    //   168: aload_1
    //   169: aload_1
    //   170: invokevirtual getStateAfterAnimating : ()I
    //   173: iconst_0
    //   174: iconst_0
    //   175: iconst_1
    //   176: invokevirtual moveToState : (Landroid/support/v4/app/Fragment;IIIZ)V
    //   179: aload_1
    //   180: getfield mState : I
    //   183: istore #8
    //   185: iload #8
    //   187: ifeq -> 216
    //   190: iload #8
    //   192: iconst_1
    //   193: if_icmpeq -> 754
    //   196: iload_2
    //   197: istore #4
    //   199: iload #8
    //   201: iconst_2
    //   202: if_icmpeq -> 1201
    //   205: iload_2
    //   206: istore_3
    //   207: iload #8
    //   209: iconst_3
    //   210: if_icmpeq -> 1265
    //   213: goto -> 1336
    //   216: iload_2
    //   217: ifle -> 754
    //   220: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   223: ifeq -> 262
    //   226: new java/lang/StringBuilder
    //   229: dup
    //   230: invokespecial <init> : ()V
    //   233: astore #9
    //   235: aload #9
    //   237: ldc_w 'moveto CREATED: '
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: pop
    //   244: aload #9
    //   246: aload_1
    //   247: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   250: pop
    //   251: ldc 'FragmentManager'
    //   253: aload #9
    //   255: invokevirtual toString : ()Ljava/lang/String;
    //   258: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   261: pop
    //   262: iload_2
    //   263: istore_3
    //   264: aload_1
    //   265: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   268: ifnull -> 399
    //   271: aload_1
    //   272: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   275: aload_0
    //   276: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   279: invokevirtual getContext : ()Landroid/content/Context;
    //   282: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   285: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   288: aload_1
    //   289: aload_1
    //   290: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   293: ldc 'android:view_state'
    //   295: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   298: putfield mSavedViewState : Landroid/util/SparseArray;
    //   301: aload_1
    //   302: aload_0
    //   303: aload_1
    //   304: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   307: ldc 'android:target_state'
    //   309: invokevirtual getFragment : (Landroid/os/Bundle;Ljava/lang/String;)Landroid/support/v4/app/Fragment;
    //   312: putfield mTarget : Landroid/support/v4/app/Fragment;
    //   315: aload_1
    //   316: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   319: ifnull -> 336
    //   322: aload_1
    //   323: aload_1
    //   324: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   327: ldc 'android:target_req_state'
    //   329: iconst_0
    //   330: invokevirtual getInt : (Ljava/lang/String;I)I
    //   333: putfield mTargetRequestCode : I
    //   336: aload_1
    //   337: getfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   340: ifnull -> 362
    //   343: aload_1
    //   344: aload_1
    //   345: getfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   348: invokevirtual booleanValue : ()Z
    //   351: putfield mUserVisibleHint : Z
    //   354: aload_1
    //   355: aconst_null
    //   356: putfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   359: goto -> 376
    //   362: aload_1
    //   363: aload_1
    //   364: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   367: ldc 'android:user_visible_hint'
    //   369: iconst_1
    //   370: invokevirtual getBoolean : (Ljava/lang/String;Z)Z
    //   373: putfield mUserVisibleHint : Z
    //   376: iload_2
    //   377: istore_3
    //   378: aload_1
    //   379: getfield mUserVisibleHint : Z
    //   382: ifne -> 399
    //   385: aload_1
    //   386: iconst_1
    //   387: putfield mDeferStart : Z
    //   390: iload_2
    //   391: istore_3
    //   392: iload_2
    //   393: iconst_2
    //   394: if_icmple -> 399
    //   397: iconst_2
    //   398: istore_3
    //   399: aload_1
    //   400: aload_0
    //   401: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   404: putfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   407: aload_1
    //   408: aload_0
    //   409: getfield mParent : Landroid/support/v4/app/Fragment;
    //   412: putfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   415: aload_0
    //   416: getfield mParent : Landroid/support/v4/app/Fragment;
    //   419: astore #9
    //   421: aload #9
    //   423: ifnull -> 436
    //   426: aload #9
    //   428: getfield mChildFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   431: astore #9
    //   433: goto -> 445
    //   436: aload_0
    //   437: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   440: invokevirtual getFragmentManagerImpl : ()Landroid/support/v4/app/FragmentManagerImpl;
    //   443: astore #9
    //   445: aload_1
    //   446: aload #9
    //   448: putfield mFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   451: aload_1
    //   452: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   455: ifnull -> 571
    //   458: aload_0
    //   459: getfield mActive : Landroid/util/SparseArray;
    //   462: aload_1
    //   463: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   466: getfield mIndex : I
    //   469: invokevirtual get : (I)Ljava/lang/Object;
    //   472: aload_1
    //   473: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   476: if_acmpne -> 505
    //   479: aload_1
    //   480: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   483: getfield mState : I
    //   486: iconst_1
    //   487: if_icmpge -> 571
    //   490: aload_0
    //   491: aload_1
    //   492: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   495: iconst_1
    //   496: iconst_0
    //   497: iconst_0
    //   498: iconst_1
    //   499: invokevirtual moveToState : (Landroid/support/v4/app/Fragment;IIIZ)V
    //   502: goto -> 571
    //   505: new java/lang/StringBuilder
    //   508: dup
    //   509: invokespecial <init> : ()V
    //   512: astore #9
    //   514: aload #9
    //   516: ldc_w 'Fragment '
    //   519: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   522: pop
    //   523: aload #9
    //   525: aload_1
    //   526: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   529: pop
    //   530: aload #9
    //   532: ldc_w ' declared target fragment '
    //   535: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   538: pop
    //   539: aload #9
    //   541: aload_1
    //   542: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   545: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   548: pop
    //   549: aload #9
    //   551: ldc_w ' that does not belong to this FragmentManager!'
    //   554: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   557: pop
    //   558: new java/lang/IllegalStateException
    //   561: dup
    //   562: aload #9
    //   564: invokevirtual toString : ()Ljava/lang/String;
    //   567: invokespecial <init> : (Ljava/lang/String;)V
    //   570: athrow
    //   571: aload_0
    //   572: aload_1
    //   573: aload_0
    //   574: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   577: invokevirtual getContext : ()Landroid/content/Context;
    //   580: iconst_0
    //   581: invokevirtual dispatchOnFragmentPreAttached : (Landroid/support/v4/app/Fragment;Landroid/content/Context;Z)V
    //   584: aload_1
    //   585: iconst_0
    //   586: putfield mCalled : Z
    //   589: aload_1
    //   590: aload_0
    //   591: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   594: invokevirtual getContext : ()Landroid/content/Context;
    //   597: invokevirtual onAttach : (Landroid/content/Context;)V
    //   600: aload_1
    //   601: getfield mCalled : Z
    //   604: ifeq -> 707
    //   607: aload_1
    //   608: getfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   611: ifnonnull -> 625
    //   614: aload_0
    //   615: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   618: aload_1
    //   619: invokevirtual onAttachFragment : (Landroid/support/v4/app/Fragment;)V
    //   622: goto -> 633
    //   625: aload_1
    //   626: getfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   629: aload_1
    //   630: invokevirtual onAttachFragment : (Landroid/support/v4/app/Fragment;)V
    //   633: aload_0
    //   634: aload_1
    //   635: aload_0
    //   636: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   639: invokevirtual getContext : ()Landroid/content/Context;
    //   642: iconst_0
    //   643: invokevirtual dispatchOnFragmentAttached : (Landroid/support/v4/app/Fragment;Landroid/content/Context;Z)V
    //   646: aload_1
    //   647: getfield mIsCreated : Z
    //   650: ifne -> 684
    //   653: aload_0
    //   654: aload_1
    //   655: aload_1
    //   656: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   659: iconst_0
    //   660: invokevirtual dispatchOnFragmentPreCreated : (Landroid/support/v4/app/Fragment;Landroid/os/Bundle;Z)V
    //   663: aload_1
    //   664: aload_1
    //   665: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   668: invokevirtual performCreate : (Landroid/os/Bundle;)V
    //   671: aload_0
    //   672: aload_1
    //   673: aload_1
    //   674: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   677: iconst_0
    //   678: invokevirtual dispatchOnFragmentCreated : (Landroid/support/v4/app/Fragment;Landroid/os/Bundle;Z)V
    //   681: goto -> 697
    //   684: aload_1
    //   685: aload_1
    //   686: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   689: invokevirtual restoreChildFragmentState : (Landroid/os/Bundle;)V
    //   692: aload_1
    //   693: iconst_1
    //   694: putfield mState : I
    //   697: aload_1
    //   698: iconst_0
    //   699: putfield mRetaining : Z
    //   702: iload_3
    //   703: istore_2
    //   704: goto -> 754
    //   707: new java/lang/StringBuilder
    //   710: dup
    //   711: invokespecial <init> : ()V
    //   714: astore #9
    //   716: aload #9
    //   718: ldc_w 'Fragment '
    //   721: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   724: pop
    //   725: aload #9
    //   727: aload_1
    //   728: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   731: pop
    //   732: aload #9
    //   734: ldc_w ' did not call through to super.onAttach()'
    //   737: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   740: pop
    //   741: new android/support/v4/app/SuperNotCalledException
    //   744: dup
    //   745: aload #9
    //   747: invokevirtual toString : ()Ljava/lang/String;
    //   750: invokespecial <init> : (Ljava/lang/String;)V
    //   753: athrow
    //   754: aload_0
    //   755: aload_1
    //   756: invokevirtual ensureInflatedFragmentView : (Landroid/support/v4/app/Fragment;)V
    //   759: iload_2
    //   760: iconst_1
    //   761: if_icmple -> 1198
    //   764: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   767: ifeq -> 806
    //   770: new java/lang/StringBuilder
    //   773: dup
    //   774: invokespecial <init> : ()V
    //   777: astore #9
    //   779: aload #9
    //   781: ldc_w 'moveto ACTIVITY_CREATED: '
    //   784: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   787: pop
    //   788: aload #9
    //   790: aload_1
    //   791: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   794: pop
    //   795: ldc 'FragmentManager'
    //   797: aload #9
    //   799: invokevirtual toString : ()Ljava/lang/String;
    //   802: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   805: pop
    //   806: aload_1
    //   807: getfield mFromLayout : Z
    //   810: ifne -> 1160
    //   813: aconst_null
    //   814: astore #9
    //   816: aload_1
    //   817: getfield mContainerId : I
    //   820: ifeq -> 1016
    //   823: aload_1
    //   824: getfield mContainerId : I
    //   827: iconst_m1
    //   828: if_icmpne -> 881
    //   831: new java/lang/StringBuilder
    //   834: dup
    //   835: invokespecial <init> : ()V
    //   838: astore #9
    //   840: aload #9
    //   842: ldc_w 'Cannot create fragment '
    //   845: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   848: pop
    //   849: aload #9
    //   851: aload_1
    //   852: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   855: pop
    //   856: aload #9
    //   858: ldc_w ' for a container view with no id'
    //   861: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   864: pop
    //   865: aload_0
    //   866: new java/lang/IllegalArgumentException
    //   869: dup
    //   870: aload #9
    //   872: invokevirtual toString : ()Ljava/lang/String;
    //   875: invokespecial <init> : (Ljava/lang/String;)V
    //   878: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   881: aload_0
    //   882: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   885: aload_1
    //   886: getfield mContainerId : I
    //   889: invokevirtual onFindViewById : (I)Landroid/view/View;
    //   892: checkcast android/view/ViewGroup
    //   895: astore #10
    //   897: aload #10
    //   899: ifnonnull -> 1012
    //   902: aload_1
    //   903: getfield mRestored : Z
    //   906: ifne -> 1012
    //   909: aload_1
    //   910: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   913: aload_1
    //   914: getfield mContainerId : I
    //   917: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   920: astore #9
    //   922: goto -> 932
    //   925: astore #9
    //   927: ldc_w 'unknown'
    //   930: astore #9
    //   932: new java/lang/StringBuilder
    //   935: dup
    //   936: invokespecial <init> : ()V
    //   939: astore #11
    //   941: aload #11
    //   943: ldc_w 'No view found for id 0x'
    //   946: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   949: pop
    //   950: aload #11
    //   952: aload_1
    //   953: getfield mContainerId : I
    //   956: invokestatic toHexString : (I)Ljava/lang/String;
    //   959: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   962: pop
    //   963: aload #11
    //   965: ldc_w ' ('
    //   968: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   971: pop
    //   972: aload #11
    //   974: aload #9
    //   976: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   979: pop
    //   980: aload #11
    //   982: ldc_w ') for fragment '
    //   985: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   988: pop
    //   989: aload #11
    //   991: aload_1
    //   992: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   995: pop
    //   996: aload_0
    //   997: new java/lang/IllegalArgumentException
    //   1000: dup
    //   1001: aload #11
    //   1003: invokevirtual toString : ()Ljava/lang/String;
    //   1006: invokespecial <init> : (Ljava/lang/String;)V
    //   1009: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   1012: aload #10
    //   1014: astore #9
    //   1016: aload_1
    //   1017: aload #9
    //   1019: putfield mContainer : Landroid/view/ViewGroup;
    //   1022: aload_1
    //   1023: aload_1
    //   1024: aload_1
    //   1025: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1028: invokevirtual performGetLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   1031: aload #9
    //   1033: aload_1
    //   1034: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1037: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)V
    //   1040: aload_1
    //   1041: getfield mView : Landroid/view/View;
    //   1044: ifnull -> 1155
    //   1047: aload_1
    //   1048: aload_1
    //   1049: getfield mView : Landroid/view/View;
    //   1052: putfield mInnerView : Landroid/view/View;
    //   1055: aload_1
    //   1056: getfield mView : Landroid/view/View;
    //   1059: iconst_0
    //   1060: invokevirtual setSaveFromParentEnabled : (Z)V
    //   1063: aload #9
    //   1065: ifnull -> 1077
    //   1068: aload #9
    //   1070: aload_1
    //   1071: getfield mView : Landroid/view/View;
    //   1074: invokevirtual addView : (Landroid/view/View;)V
    //   1077: aload_1
    //   1078: getfield mHidden : Z
    //   1081: ifeq -> 1093
    //   1084: aload_1
    //   1085: getfield mView : Landroid/view/View;
    //   1088: bipush #8
    //   1090: invokevirtual setVisibility : (I)V
    //   1093: aload_1
    //   1094: aload_1
    //   1095: getfield mView : Landroid/view/View;
    //   1098: aload_1
    //   1099: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1102: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   1105: aload_0
    //   1106: aload_1
    //   1107: aload_1
    //   1108: getfield mView : Landroid/view/View;
    //   1111: aload_1
    //   1112: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1115: iconst_0
    //   1116: invokevirtual dispatchOnFragmentViewCreated : (Landroid/support/v4/app/Fragment;Landroid/view/View;Landroid/os/Bundle;Z)V
    //   1119: aload_1
    //   1120: getfield mView : Landroid/view/View;
    //   1123: invokevirtual getVisibility : ()I
    //   1126: ifne -> 1143
    //   1129: aload_1
    //   1130: getfield mContainer : Landroid/view/ViewGroup;
    //   1133: ifnull -> 1143
    //   1136: iload #7
    //   1138: istore #5
    //   1140: goto -> 1146
    //   1143: iconst_0
    //   1144: istore #5
    //   1146: aload_1
    //   1147: iload #5
    //   1149: putfield mIsNewlyAdded : Z
    //   1152: goto -> 1160
    //   1155: aload_1
    //   1156: aconst_null
    //   1157: putfield mInnerView : Landroid/view/View;
    //   1160: aload_1
    //   1161: aload_1
    //   1162: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1165: invokevirtual performActivityCreated : (Landroid/os/Bundle;)V
    //   1168: aload_0
    //   1169: aload_1
    //   1170: aload_1
    //   1171: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1174: iconst_0
    //   1175: invokevirtual dispatchOnFragmentActivityCreated : (Landroid/support/v4/app/Fragment;Landroid/os/Bundle;Z)V
    //   1178: aload_1
    //   1179: getfield mView : Landroid/view/View;
    //   1182: ifnull -> 1193
    //   1185: aload_1
    //   1186: aload_1
    //   1187: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1190: invokevirtual restoreViewState : (Landroid/os/Bundle;)V
    //   1193: aload_1
    //   1194: aconst_null
    //   1195: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1198: iload_2
    //   1199: istore #4
    //   1201: iload #4
    //   1203: istore_3
    //   1204: iload #4
    //   1206: iconst_2
    //   1207: if_icmple -> 1265
    //   1210: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1213: ifeq -> 1252
    //   1216: new java/lang/StringBuilder
    //   1219: dup
    //   1220: invokespecial <init> : ()V
    //   1223: astore #9
    //   1225: aload #9
    //   1227: ldc_w 'moveto STARTED: '
    //   1230: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1233: pop
    //   1234: aload #9
    //   1236: aload_1
    //   1237: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1240: pop
    //   1241: ldc 'FragmentManager'
    //   1243: aload #9
    //   1245: invokevirtual toString : ()Ljava/lang/String;
    //   1248: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1251: pop
    //   1252: aload_1
    //   1253: invokevirtual performStart : ()V
    //   1256: aload_0
    //   1257: aload_1
    //   1258: iconst_0
    //   1259: invokevirtual dispatchOnFragmentStarted : (Landroid/support/v4/app/Fragment;Z)V
    //   1262: iload #4
    //   1264: istore_3
    //   1265: iload_3
    //   1266: istore_2
    //   1267: iload_3
    //   1268: iconst_3
    //   1269: if_icmple -> 1336
    //   1272: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1275: ifeq -> 1314
    //   1278: new java/lang/StringBuilder
    //   1281: dup
    //   1282: invokespecial <init> : ()V
    //   1285: astore #9
    //   1287: aload #9
    //   1289: ldc_w 'moveto RESUMED: '
    //   1292: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1295: pop
    //   1296: aload #9
    //   1298: aload_1
    //   1299: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1302: pop
    //   1303: ldc 'FragmentManager'
    //   1305: aload #9
    //   1307: invokevirtual toString : ()Ljava/lang/String;
    //   1310: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1313: pop
    //   1314: aload_1
    //   1315: invokevirtual performResume : ()V
    //   1318: aload_0
    //   1319: aload_1
    //   1320: iconst_0
    //   1321: invokevirtual dispatchOnFragmentResumed : (Landroid/support/v4/app/Fragment;Z)V
    //   1324: aload_1
    //   1325: aconst_null
    //   1326: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1329: aload_1
    //   1330: aconst_null
    //   1331: putfield mSavedViewState : Landroid/util/SparseArray;
    //   1334: iload_3
    //   1335: istore_2
    //   1336: iload_2
    //   1337: istore_3
    //   1338: goto -> 1954
    //   1341: aload_1
    //   1342: getfield mState : I
    //   1345: iload_2
    //   1346: if_icmple -> 1952
    //   1349: aload_1
    //   1350: getfield mState : I
    //   1353: istore #8
    //   1355: iload #8
    //   1357: iconst_1
    //   1358: if_icmpeq -> 1741
    //   1361: iload #8
    //   1363: iconst_2
    //   1364: if_icmpeq -> 1498
    //   1367: iload #8
    //   1369: iconst_3
    //   1370: if_icmpeq -> 1441
    //   1373: iload #8
    //   1375: iconst_4
    //   1376: if_icmpeq -> 1384
    //   1379: iload_2
    //   1380: istore_3
    //   1381: goto -> 1954
    //   1384: iload_2
    //   1385: iconst_4
    //   1386: if_icmpge -> 1441
    //   1389: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1392: ifeq -> 1431
    //   1395: new java/lang/StringBuilder
    //   1398: dup
    //   1399: invokespecial <init> : ()V
    //   1402: astore #9
    //   1404: aload #9
    //   1406: ldc_w 'movefrom RESUMED: '
    //   1409: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1412: pop
    //   1413: aload #9
    //   1415: aload_1
    //   1416: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1419: pop
    //   1420: ldc 'FragmentManager'
    //   1422: aload #9
    //   1424: invokevirtual toString : ()Ljava/lang/String;
    //   1427: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1430: pop
    //   1431: aload_1
    //   1432: invokevirtual performPause : ()V
    //   1435: aload_0
    //   1436: aload_1
    //   1437: iconst_0
    //   1438: invokevirtual dispatchOnFragmentPaused : (Landroid/support/v4/app/Fragment;Z)V
    //   1441: iload_2
    //   1442: iconst_3
    //   1443: if_icmpge -> 1498
    //   1446: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1449: ifeq -> 1488
    //   1452: new java/lang/StringBuilder
    //   1455: dup
    //   1456: invokespecial <init> : ()V
    //   1459: astore #9
    //   1461: aload #9
    //   1463: ldc_w 'movefrom STARTED: '
    //   1466: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1469: pop
    //   1470: aload #9
    //   1472: aload_1
    //   1473: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1476: pop
    //   1477: ldc 'FragmentManager'
    //   1479: aload #9
    //   1481: invokevirtual toString : ()Ljava/lang/String;
    //   1484: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1487: pop
    //   1488: aload_1
    //   1489: invokevirtual performStop : ()V
    //   1492: aload_0
    //   1493: aload_1
    //   1494: iconst_0
    //   1495: invokevirtual dispatchOnFragmentStopped : (Landroid/support/v4/app/Fragment;Z)V
    //   1498: iload_2
    //   1499: iconst_2
    //   1500: if_icmpge -> 1738
    //   1503: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1506: ifeq -> 1545
    //   1509: new java/lang/StringBuilder
    //   1512: dup
    //   1513: invokespecial <init> : ()V
    //   1516: astore #9
    //   1518: aload #9
    //   1520: ldc_w 'movefrom ACTIVITY_CREATED: '
    //   1523: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1526: pop
    //   1527: aload #9
    //   1529: aload_1
    //   1530: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1533: pop
    //   1534: ldc 'FragmentManager'
    //   1536: aload #9
    //   1538: invokevirtual toString : ()Ljava/lang/String;
    //   1541: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1544: pop
    //   1545: aload_1
    //   1546: getfield mView : Landroid/view/View;
    //   1549: ifnull -> 1575
    //   1552: aload_0
    //   1553: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   1556: aload_1
    //   1557: invokevirtual onShouldSaveFragmentState : (Landroid/support/v4/app/Fragment;)Z
    //   1560: ifeq -> 1575
    //   1563: aload_1
    //   1564: getfield mSavedViewState : Landroid/util/SparseArray;
    //   1567: ifnonnull -> 1575
    //   1570: aload_0
    //   1571: aload_1
    //   1572: invokevirtual saveFragmentViewState : (Landroid/support/v4/app/Fragment;)V
    //   1575: aload_1
    //   1576: invokevirtual performDestroyView : ()V
    //   1579: aload_0
    //   1580: aload_1
    //   1581: iconst_0
    //   1582: invokevirtual dispatchOnFragmentViewDestroyed : (Landroid/support/v4/app/Fragment;Z)V
    //   1585: aload_1
    //   1586: getfield mView : Landroid/view/View;
    //   1589: ifnull -> 1702
    //   1592: aload_1
    //   1593: getfield mContainer : Landroid/view/ViewGroup;
    //   1596: ifnull -> 1702
    //   1599: aload_1
    //   1600: getfield mContainer : Landroid/view/ViewGroup;
    //   1603: aload_1
    //   1604: getfield mView : Landroid/view/View;
    //   1607: invokevirtual endViewTransition : (Landroid/view/View;)V
    //   1610: aload_1
    //   1611: getfield mView : Landroid/view/View;
    //   1614: invokevirtual clearAnimation : ()V
    //   1617: aconst_null
    //   1618: astore #9
    //   1620: aload_0
    //   1621: getfield mCurState : I
    //   1624: ifle -> 1670
    //   1627: aload_0
    //   1628: getfield mDestroyed : Z
    //   1631: ifne -> 1670
    //   1634: aload_1
    //   1635: getfield mView : Landroid/view/View;
    //   1638: invokevirtual getVisibility : ()I
    //   1641: ifne -> 1667
    //   1644: aload_1
    //   1645: getfield mPostponedAlpha : F
    //   1648: fconst_0
    //   1649: fcmpl
    //   1650: iflt -> 1667
    //   1653: aload_0
    //   1654: aload_1
    //   1655: iload_3
    //   1656: iconst_0
    //   1657: iload #4
    //   1659: invokevirtual loadAnimation : (Landroid/support/v4/app/Fragment;IZI)Landroid/support/v4/app/FragmentManagerImpl$AnimationOrAnimator;
    //   1662: astore #9
    //   1664: goto -> 1670
    //   1667: goto -> 1670
    //   1670: aload_1
    //   1671: fconst_0
    //   1672: putfield mPostponedAlpha : F
    //   1675: aload #9
    //   1677: ifnull -> 1688
    //   1680: aload_0
    //   1681: aload_1
    //   1682: aload #9
    //   1684: iload_2
    //   1685: invokespecial animateRemoveFragment : (Landroid/support/v4/app/Fragment;Landroid/support/v4/app/FragmentManagerImpl$AnimationOrAnimator;I)V
    //   1688: aload_1
    //   1689: getfield mContainer : Landroid/view/ViewGroup;
    //   1692: aload_1
    //   1693: getfield mView : Landroid/view/View;
    //   1696: invokevirtual removeView : (Landroid/view/View;)V
    //   1699: goto -> 1702
    //   1702: aload_1
    //   1703: aconst_null
    //   1704: putfield mContainer : Landroid/view/ViewGroup;
    //   1707: aload_1
    //   1708: aconst_null
    //   1709: putfield mView : Landroid/view/View;
    //   1712: aload_1
    //   1713: aconst_null
    //   1714: putfield mViewLifecycleOwner : Landroid/arch/lifecycle/LifecycleOwner;
    //   1717: aload_1
    //   1718: getfield mViewLifecycleOwnerLiveData : Landroid/arch/lifecycle/MutableLiveData;
    //   1721: aconst_null
    //   1722: invokevirtual setValue : (Ljava/lang/Object;)V
    //   1725: aload_1
    //   1726: aconst_null
    //   1727: putfield mInnerView : Landroid/view/View;
    //   1730: aload_1
    //   1731: iconst_0
    //   1732: putfield mInLayout : Z
    //   1735: goto -> 1741
    //   1738: goto -> 1741
    //   1741: iload_2
    //   1742: istore_3
    //   1743: iload_2
    //   1744: iconst_1
    //   1745: if_icmpge -> 1954
    //   1748: aload_0
    //   1749: getfield mDestroyed : Z
    //   1752: ifeq -> 1804
    //   1755: aload_1
    //   1756: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1759: ifnull -> 1781
    //   1762: aload_1
    //   1763: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1766: astore #9
    //   1768: aload_1
    //   1769: aconst_null
    //   1770: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   1773: aload #9
    //   1775: invokevirtual clearAnimation : ()V
    //   1778: goto -> 1804
    //   1781: aload_1
    //   1782: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1785: ifnull -> 1804
    //   1788: aload_1
    //   1789: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1792: astore #9
    //   1794: aload_1
    //   1795: aconst_null
    //   1796: invokevirtual setAnimator : (Landroid/animation/Animator;)V
    //   1799: aload #9
    //   1801: invokevirtual cancel : ()V
    //   1804: aload_1
    //   1805: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1808: ifnonnull -> 1942
    //   1811: aload_1
    //   1812: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1815: ifnull -> 1821
    //   1818: goto -> 1942
    //   1821: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1824: ifeq -> 1863
    //   1827: new java/lang/StringBuilder
    //   1830: dup
    //   1831: invokespecial <init> : ()V
    //   1834: astore #9
    //   1836: aload #9
    //   1838: ldc_w 'movefrom CREATED: '
    //   1841: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1844: pop
    //   1845: aload #9
    //   1847: aload_1
    //   1848: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1851: pop
    //   1852: ldc 'FragmentManager'
    //   1854: aload #9
    //   1856: invokevirtual toString : ()Ljava/lang/String;
    //   1859: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1862: pop
    //   1863: aload_1
    //   1864: getfield mRetaining : Z
    //   1867: ifne -> 1883
    //   1870: aload_1
    //   1871: invokevirtual performDestroy : ()V
    //   1874: aload_0
    //   1875: aload_1
    //   1876: iconst_0
    //   1877: invokevirtual dispatchOnFragmentDestroyed : (Landroid/support/v4/app/Fragment;Z)V
    //   1880: goto -> 1888
    //   1883: aload_1
    //   1884: iconst_0
    //   1885: putfield mState : I
    //   1888: aload_1
    //   1889: invokevirtual performDetach : ()V
    //   1892: aload_0
    //   1893: aload_1
    //   1894: iconst_0
    //   1895: invokevirtual dispatchOnFragmentDetached : (Landroid/support/v4/app/Fragment;Z)V
    //   1898: iload_2
    //   1899: istore_3
    //   1900: iload #5
    //   1902: ifne -> 1954
    //   1905: aload_1
    //   1906: getfield mRetaining : Z
    //   1909: ifne -> 1922
    //   1912: aload_0
    //   1913: aload_1
    //   1914: invokevirtual makeInactive : (Landroid/support/v4/app/Fragment;)V
    //   1917: iload_2
    //   1918: istore_3
    //   1919: goto -> 1954
    //   1922: aload_1
    //   1923: aconst_null
    //   1924: putfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   1927: aload_1
    //   1928: aconst_null
    //   1929: putfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   1932: aload_1
    //   1933: aconst_null
    //   1934: putfield mFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   1937: iload_2
    //   1938: istore_3
    //   1939: goto -> 1954
    //   1942: aload_1
    //   1943: iload_2
    //   1944: invokevirtual setStateAfterAnimating : (I)V
    //   1947: iconst_1
    //   1948: istore_3
    //   1949: goto -> 1954
    //   1952: iload_2
    //   1953: istore_3
    //   1954: aload_1
    //   1955: getfield mState : I
    //   1958: iload_3
    //   1959: if_icmpeq -> 2047
    //   1962: new java/lang/StringBuilder
    //   1965: dup
    //   1966: invokespecial <init> : ()V
    //   1969: astore #9
    //   1971: aload #9
    //   1973: ldc_w 'moveToState: Fragment state for '
    //   1976: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1979: pop
    //   1980: aload #9
    //   1982: aload_1
    //   1983: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1986: pop
    //   1987: aload #9
    //   1989: ldc_w ' not updated inline; '
    //   1992: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1995: pop
    //   1996: aload #9
    //   1998: ldc_w 'expected state '
    //   2001: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2004: pop
    //   2005: aload #9
    //   2007: iload_3
    //   2008: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2011: pop
    //   2012: aload #9
    //   2014: ldc_w ' found '
    //   2017: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2020: pop
    //   2021: aload #9
    //   2023: aload_1
    //   2024: getfield mState : I
    //   2027: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2030: pop
    //   2031: ldc 'FragmentManager'
    //   2033: aload #9
    //   2035: invokevirtual toString : ()Ljava/lang/String;
    //   2038: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   2041: pop
    //   2042: aload_1
    //   2043: iload_3
    //   2044: putfield mState : I
    //   2047: return
    // Exception table:
    //   from	to	target	type
    //   909	922	925	android/content/res/Resources$NotFoundException
  }
  
  public void noteStateNotSaved() {
    this.mSavedNonConfig = null;
    this.mStateSaved = false;
    this.mStopped = false;
    int i = this.mAdded.size();
    for (byte b = 0; b < i; b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.noteStateNotSaved(); 
    } 
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    if (!"fragment".equals(paramString))
      return null; 
    String str1 = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
    int i = 0;
    if (str1 == null)
      str1 = typedArray.getString(0); 
    int j = typedArray.getResourceId(1, -1);
    String str2 = typedArray.getString(2);
    typedArray.recycle();
    if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), str1))
      return null; 
    if (paramView != null)
      i = paramView.getId(); 
    if (i != -1 || j != -1 || str2 != null) {
      Fragment fragment2;
      if (j != -1) {
        Fragment fragment = findFragmentById(j);
      } else {
        paramView = null;
      } 
      View view = paramView;
      if (paramView == null) {
        view = paramView;
        if (str2 != null)
          fragment2 = findFragmentByTag(str2); 
      } 
      Fragment fragment1 = fragment2;
      if (fragment2 == null) {
        fragment1 = fragment2;
        if (i != -1)
          fragment1 = findFragmentById(i); 
      } 
      if (DEBUG) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("onCreateView: id=0x");
        stringBuilder2.append(Integer.toHexString(j));
        stringBuilder2.append(" fname=");
        stringBuilder2.append(str1);
        stringBuilder2.append(" existing=");
        stringBuilder2.append(fragment1);
        Log.v("FragmentManager", stringBuilder2.toString());
      } 
      if (fragment1 == null) {
        int k;
        fragment1 = this.mContainer.instantiate(paramContext, str1, null);
        fragment1.mFromLayout = true;
        if (j != 0) {
          k = j;
        } else {
          k = i;
        } 
        fragment1.mFragmentId = k;
        fragment1.mContainerId = i;
        fragment1.mTag = str2;
        fragment1.mInLayout = true;
        fragment1.mFragmentManager = this;
        fragment1.mHost = this.mHost;
        fragment1.onInflate(this.mHost.getContext(), paramAttributeSet, fragment1.mSavedFragmentState);
        addFragment(fragment1, true);
      } else if (!fragment1.mInLayout) {
        fragment1.mInLayout = true;
        fragment1.mHost = this.mHost;
        if (!fragment1.mRetaining)
          fragment1.onInflate(this.mHost.getContext(), paramAttributeSet, fragment1.mSavedFragmentState); 
      } else {
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append(paramAttributeSet.getPositionDescription());
        stringBuilder1.append(": Duplicate id 0x");
        stringBuilder1.append(Integer.toHexString(j));
        stringBuilder1.append(", tag ");
        stringBuilder1.append(str2);
        stringBuilder1.append(", or parent id 0x");
        stringBuilder1.append(Integer.toHexString(i));
        stringBuilder1.append(" with another fragment for ");
        stringBuilder1.append(str1);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      if (this.mCurState < 1 && ((Fragment)stringBuilder1).mFromLayout) {
        moveToState((Fragment)stringBuilder1, 1, 0, 0, false);
      } else {
        moveToState((Fragment)stringBuilder1);
      } 
      if (((Fragment)stringBuilder1).mView != null) {
        if (j != 0)
          ((Fragment)stringBuilder1).mView.setId(j); 
        if (((Fragment)stringBuilder1).mView.getTag() == null)
          ((Fragment)stringBuilder1).mView.setTag(str2); 
        return ((Fragment)stringBuilder1).mView;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Fragment ");
      stringBuilder1.append(str1);
      stringBuilder1.append(" did not create a view.");
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramAttributeSet.getPositionDescription());
    stringBuilder.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
    stringBuilder.append(str1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return onCreateView(null, paramString, paramContext, paramAttributeSet);
  }
  
  public void performPendingDeferredStart(Fragment paramFragment) {
    if (paramFragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
      paramFragment.mDeferStart = false;
      moveToState(paramFragment, this.mCurState, 0, 0, false);
    } 
  }
  
  public void popBackStack() {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }
  
  public void popBackStack(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void popBackStack(String paramString, int paramInt) {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }
  
  public boolean popBackStackImmediate() {
    checkStateLoss();
    return popBackStackImmediate(null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    checkStateLoss();
    execPendingActions();
    if (paramInt1 >= 0)
      return popBackStackImmediate(null, paramInt1, paramInt2); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt) {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2) {
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    if (arrayList == null)
      return false; 
    if (paramString == null && paramInt1 < 0 && (paramInt2 & 0x1) == 0) {
      paramInt1 = arrayList.size() - 1;
      if (paramInt1 < 0)
        return false; 
      paramArrayList.add(this.mBackStack.remove(paramInt1));
      paramArrayList1.add(Boolean.valueOf(true));
    } else {
      int i = -1;
      if (paramString != null || paramInt1 >= 0) {
        int j;
        for (j = this.mBackStack.size() - 1; j >= 0; j--) {
          BackStackRecord backStackRecord = this.mBackStack.get(j);
          if ((paramString != null && paramString.equals(backStackRecord.getName())) || (paramInt1 >= 0 && paramInt1 == backStackRecord.mIndex))
            break; 
        } 
        if (j < 0)
          return false; 
        i = j;
        if ((paramInt2 & 0x1) != 0)
          for (paramInt2 = j - 1;; paramInt2--) {
            i = paramInt2;
            if (paramInt2 >= 0) {
              BackStackRecord backStackRecord = this.mBackStack.get(paramInt2);
              if (paramString == null || !paramString.equals(backStackRecord.getName())) {
                i = paramInt2;
                if (paramInt1 >= 0) {
                  i = paramInt2;
                  if (paramInt1 == backStackRecord.mIndex)
                    continue; 
                } 
                break;
              } 
              continue;
            } 
            break;
          }  
      } 
      if (i == this.mBackStack.size() - 1)
        return false; 
      for (paramInt1 = this.mBackStack.size() - 1; paramInt1 > i; paramInt1--) {
        paramArrayList.add(this.mBackStack.remove(paramInt1));
        paramArrayList1.add(Boolean.valueOf(true));
      } 
    } 
    return true;
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mIndex < 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    paramBundle.putInt(paramString, paramFragment.mIndex);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean) {
    this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(paramFragmentLifecycleCallbacks, paramBoolean));
  }
  
  public void removeFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("remove: ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" nesting=");
      stringBuilder.append(paramFragment.mBackStackNesting);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    boolean bool = paramFragment.isInBackStack();
    if (!paramFragment.mDetached || (bool ^ true) != 0)
      synchronized (this.mAdded) {
        this.mAdded.remove(paramFragment);
        if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
          this.mNeedMenuInvalidate = true; 
        paramFragment.mAdded = false;
        paramFragment.mRemoving = true;
        return;
      }  
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
    if (arrayList != null)
      arrayList.remove(paramOnBackStackChangedListener); 
  }
  
  void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (byte b = 0; b < this.mBackStackChangeListeners.size(); b++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(b)).onBackStackChanged();  
  }
  
  void restoreAllState(Parcelable<FragmentManagerNonConfig> paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    List<ViewModelStore> list;
    if (paramParcelable == null)
      return; 
    FragmentManagerState fragmentManagerState = (FragmentManagerState)paramParcelable;
    if (fragmentManagerState.mActive == null)
      return; 
    if (paramFragmentManagerNonConfig != null) {
      byte b1;
      List<Fragment> list2 = paramFragmentManagerNonConfig.getFragments();
      List<FragmentManagerNonConfig> list1 = paramFragmentManagerNonConfig.getChildNonConfigs();
      list = paramFragmentManagerNonConfig.getViewModelStores();
      if (list2 != null) {
        b1 = list2.size();
      } else {
        b1 = 0;
      } 
      for (byte b2 = 0; b2 < b1; b2++) {
        Fragment fragment = list2.get(b2);
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("restoreAllState: re-attaching retained ");
          stringBuilder.append(fragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        byte b;
        for (b = 0; b < fragmentManagerState.mActive.length && (fragmentManagerState.mActive[b]).mIndex != fragment.mIndex; b++);
        if (b == fragmentManagerState.mActive.length) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Could not find active fragment with index ");
          stringBuilder.append(fragment.mIndex);
          throwException(new IllegalStateException(stringBuilder.toString()));
        } 
        FragmentState fragmentState = fragmentManagerState.mActive[b];
        fragmentState.mInstance = fragment;
        fragment.mSavedViewState = null;
        fragment.mBackStackNesting = 0;
        fragment.mInLayout = false;
        fragment.mAdded = false;
        fragment.mTarget = null;
        if (fragmentState.mSavedFragmentState != null) {
          fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
          fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
          fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
        } 
      } 
    } else {
      list = null;
      paramParcelable = null;
    } 
    this.mActive = new SparseArray(fragmentManagerState.mActive.length);
    int i;
    for (i = 0; i < fragmentManagerState.mActive.length; i++) {
      FragmentState fragmentState = fragmentManagerState.mActive[i];
      if (fragmentState != null) {
        ViewModelStore viewModelStore;
        if (paramParcelable != null && i < paramParcelable.size()) {
          fragment = (Fragment)paramParcelable.get(i);
        } else {
          fragment = null;
        } 
        if (list != null && i < list.size()) {
          viewModelStore = list.get(i);
        } else {
          viewModelStore = null;
        } 
        Fragment fragment = fragmentState.instantiate(this.mHost, this.mContainer, this.mParent, (FragmentManagerNonConfig)fragment, viewModelStore);
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("restoreAllState: active #");
          stringBuilder.append(i);
          stringBuilder.append(": ");
          stringBuilder.append(fragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        this.mActive.put(fragment.mIndex, fragment);
        fragmentState.mInstance = null;
      } 
    } 
    if (paramFragmentManagerNonConfig != null) {
      List<Fragment> list1 = paramFragmentManagerNonConfig.getFragments();
      if (list1 != null) {
        i = list1.size();
      } else {
        i = 0;
      } 
      for (byte b = 0; b < i; b++) {
        Fragment fragment = list1.get(b);
        if (fragment.mTargetIndex >= 0) {
          fragment.mTarget = (Fragment)this.mActive.get(fragment.mTargetIndex);
          if (fragment.mTarget == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Re-attaching retained fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" target no longer exists: ");
            stringBuilder.append(fragment.mTargetIndex);
            Log.w("FragmentManager", stringBuilder.toString());
          } 
        } 
      } 
    } 
    this.mAdded.clear();
    if (fragmentManagerState.mAdded != null) {
      i = 0;
      while (i < fragmentManagerState.mAdded.length) {
        Fragment fragment = (Fragment)this.mActive.get(fragmentManagerState.mAdded[i]);
        if (fragment == null) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("No instantiated fragment for index #");
          stringBuilder.append(fragmentManagerState.mAdded[i]);
          throwException(new IllegalStateException(stringBuilder.toString()));
        } 
        fragment.mAdded = true;
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("restoreAllState: added #");
          stringBuilder.append(i);
          stringBuilder.append(": ");
          stringBuilder.append(fragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        if (!this.mAdded.contains(fragment)) {
          synchronized (this.mAdded) {
            this.mAdded.add(fragment);
            i++;
          } 
          continue;
        } 
        throw new IllegalStateException("Already added!");
      } 
    } 
    if (fragmentManagerState.mBackStack != null) {
      this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
      for (i = 0; i < fragmentManagerState.mBackStack.length; i++) {
        BackStackRecord backStackRecord = fragmentManagerState.mBackStack[i].instantiate(this);
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("restoreAllState: back stack #");
          stringBuilder.append(i);
          stringBuilder.append(" (index ");
          stringBuilder.append(backStackRecord.mIndex);
          stringBuilder.append("): ");
          stringBuilder.append(backStackRecord);
          Log.v("FragmentManager", stringBuilder.toString());
          PrintWriter printWriter = new PrintWriter((Writer)new LogWriter("FragmentManager"));
          backStackRecord.dump("  ", printWriter, false);
          printWriter.close();
        } 
        this.mBackStack.add(backStackRecord);
        if (backStackRecord.mIndex >= 0)
          setBackStackIndex(backStackRecord.mIndex, backStackRecord); 
      } 
    } else {
      this.mBackStack = null;
    } 
    if (fragmentManagerState.mPrimaryNavActiveIndex >= 0)
      this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex); 
    this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
  }
  
  FragmentManagerNonConfig retainNonConfig() {
    setRetaining(this.mSavedNonConfig);
    return this.mSavedNonConfig;
  }
  
  Parcelable saveAllState() {
    StringBuilder stringBuilder1;
    StringBuilder stringBuilder2;
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    this.mStateSaved = true;
    this.mSavedNonConfig = null;
    SparseArray<Fragment> sparseArray = this.mActive;
    if (sparseArray == null || sparseArray.size() <= 0)
      return null; 
    int i = this.mActive.size();
    FragmentState[] arrayOfFragmentState = new FragmentState[i];
    int j = 0;
    byte b;
    for (b = 0; b < i; b++) {
      Fragment fragment1 = (Fragment)this.mActive.valueAt(b);
      if (fragment1 != null) {
        if (fragment1.mIndex < 0) {
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Failure saving state: active ");
          stringBuilder2.append(fragment1);
          stringBuilder2.append(" has cleared index: ");
          stringBuilder2.append(fragment1.mIndex);
          throwException(new IllegalStateException(stringBuilder2.toString()));
        } 
        byte b1 = 1;
        FragmentState fragmentState = new FragmentState(fragment1);
        arrayOfFragmentState[b] = fragmentState;
        if (fragment1.mState > 0 && fragmentState.mSavedFragmentState == null) {
          fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment1);
          if (fragment1.mTarget != null) {
            if (fragment1.mTarget.mIndex < 0) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Failure saving state: ");
              stringBuilder.append(fragment1);
              stringBuilder.append(" has target not in fragment manager: ");
              stringBuilder.append(fragment1.mTarget);
              throwException(new IllegalStateException(stringBuilder.toString()));
            } 
            if (fragmentState.mSavedFragmentState == null)
              fragmentState.mSavedFragmentState = new Bundle(); 
            putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment1.mTarget);
            if (fragment1.mTargetRequestCode != 0)
              fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment1.mTargetRequestCode); 
          } 
        } else {
          fragmentState.mSavedFragmentState = fragment1.mSavedFragmentState;
        } 
        j = b1;
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Saved state of ");
          stringBuilder.append(fragment1);
          stringBuilder.append(": ");
          stringBuilder.append(fragmentState.mSavedFragmentState);
          Log.v("FragmentManager", stringBuilder.toString());
          j = b1;
        } 
      } 
    } 
    if (!j) {
      if (DEBUG)
        Log.v("FragmentManager", "saveAllState: no fragments!"); 
      return null;
    } 
    sparseArray = null;
    BackStackState[] arrayOfBackStackState2 = null;
    j = this.mAdded.size();
    if (j > 0) {
      int[] arrayOfInt = new int[j];
      b = 0;
      while (true) {
        int[] arrayOfInt1 = arrayOfInt;
        if (b < j) {
          arrayOfInt[b] = ((Fragment)this.mAdded.get(b)).mIndex;
          if (arrayOfInt[b] < 0) {
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append("Failure saving state: active ");
            stringBuilder1.append(this.mAdded.get(b));
            stringBuilder1.append(" has cleared index: ");
            stringBuilder1.append(arrayOfInt[b]);
            throwException(new IllegalStateException(stringBuilder1.toString()));
          } 
          if (DEBUG) {
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append("saveAllState: adding fragment #");
            stringBuilder1.append(b);
            stringBuilder1.append(": ");
            stringBuilder1.append(this.mAdded.get(b));
            Log.v("FragmentManager", stringBuilder1.toString());
          } 
          b++;
          continue;
        } 
        break;
      } 
    } 
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    BackStackState[] arrayOfBackStackState1 = arrayOfBackStackState2;
    if (arrayList != null) {
      j = arrayList.size();
      arrayOfBackStackState1 = arrayOfBackStackState2;
      if (j > 0) {
        arrayOfBackStackState2 = new BackStackState[j];
        b = 0;
        while (true) {
          arrayOfBackStackState1 = arrayOfBackStackState2;
          if (b < j) {
            arrayOfBackStackState2[b] = new BackStackState(this.mBackStack.get(b));
            if (DEBUG) {
              stringBuilder2 = new StringBuilder();
              stringBuilder2.append("saveAllState: adding back stack #");
              stringBuilder2.append(b);
              stringBuilder2.append(": ");
              stringBuilder2.append(this.mBackStack.get(b));
              Log.v("FragmentManager", stringBuilder2.toString());
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    FragmentManagerState fragmentManagerState = new FragmentManagerState();
    fragmentManagerState.mActive = arrayOfFragmentState;
    fragmentManagerState.mAdded = (int[])stringBuilder1;
    fragmentManagerState.mBackStack = (BackStackState[])stringBuilder2;
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null)
      fragmentManagerState.mPrimaryNavActiveIndex = fragment.mIndex; 
    fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
    saveNonConfig();
    return fragmentManagerState;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment) {
    Bundle bundle1 = null;
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle(); 
    paramFragment.performSaveInstanceState(this.mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, this.mStateBundle, false);
    if (!this.mStateBundle.isEmpty()) {
      bundle1 = this.mStateBundle;
      this.mStateBundle = null;
    } 
    if (paramFragment.mView != null)
      saveFragmentViewState(paramFragment); 
    Bundle bundle2 = bundle1;
    if (paramFragment.mSavedViewState != null) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
    } 
    bundle1 = bundle2;
    if (!paramFragment.mUserVisibleHint) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
    } 
    return bundle1;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    if (paramFragment.mIndex < 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    int i = paramFragment.mState;
    Fragment fragment = null;
    if (i > 0) {
      Fragment.SavedState savedState;
      Bundle bundle = saveFragmentBasicState(paramFragment);
      paramFragment = fragment;
      if (bundle != null)
        savedState = new Fragment.SavedState(bundle); 
      return savedState;
    } 
    return null;
  }
  
  void saveFragmentViewState(Fragment paramFragment) {
    if (paramFragment.mInnerView == null)
      return; 
    SparseArray<Parcelable> sparseArray = this.mStateArray;
    if (sparseArray == null) {
      this.mStateArray = new SparseArray();
    } else {
      sparseArray.clear();
    } 
    paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
    if (this.mStateArray.size() > 0) {
      paramFragment.mSavedViewState = this.mStateArray;
      this.mStateArray = null;
    } 
  }
  
  void saveNonConfig() {
    ArrayList<Fragment> arrayList1 = null;
    ArrayList<Fragment> arrayList2 = null;
    ArrayList<Fragment> arrayList3 = null;
    ArrayList<Fragment> arrayList4 = null;
    ArrayList<Fragment> arrayList5 = null;
    ArrayList<Fragment> arrayList6 = null;
    if (this.mActive != null) {
      byte b = 0;
      while (true) {
        arrayList1 = arrayList2;
        arrayList3 = arrayList4;
        arrayList5 = arrayList6;
        if (b < this.mActive.size()) {
          Fragment fragment = (Fragment)this.mActive.valueAt(b);
          arrayList3 = arrayList2;
          arrayList5 = arrayList4;
          ArrayList<Fragment> arrayList = arrayList6;
          if (fragment != null) {
            FragmentManagerNonConfig fragmentManagerNonConfig;
            arrayList1 = arrayList2;
            if (fragment.mRetainInstance) {
              byte b1;
              arrayList3 = arrayList2;
              if (arrayList2 == null)
                arrayList3 = new ArrayList(); 
              arrayList3.add(fragment);
              if (fragment.mTarget != null) {
                b1 = fragment.mTarget.mIndex;
              } else {
                b1 = -1;
              } 
              fragment.mTargetIndex = b1;
              arrayList1 = arrayList3;
              if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("retainNonConfig: keeping retained ");
                stringBuilder.append(fragment);
                Log.v("FragmentManager", stringBuilder.toString());
                arrayList1 = arrayList3;
              } 
            } 
            if (fragment.mChildFragmentManager != null) {
              fragment.mChildFragmentManager.saveNonConfig();
              fragmentManagerNonConfig = fragment.mChildFragmentManager.mSavedNonConfig;
            } else {
              fragmentManagerNonConfig = fragment.mChildNonConfig;
            } 
            arrayList2 = arrayList4;
            if (arrayList4 == null) {
              arrayList2 = arrayList4;
              if (fragmentManagerNonConfig != null) {
                arrayList4 = new ArrayList<>(this.mActive.size());
                byte b1 = 0;
                while (true) {
                  arrayList2 = arrayList4;
                  if (b1 < b) {
                    arrayList4.add(null);
                    b1++;
                    continue;
                  } 
                  break;
                } 
              } 
            } 
            if (arrayList2 != null)
              arrayList2.add(fragmentManagerNonConfig); 
            arrayList4 = arrayList6;
            if (arrayList6 == null) {
              arrayList4 = arrayList6;
              if (fragment.mViewModelStore != null) {
                arrayList6 = new ArrayList<>(this.mActive.size());
                byte b1 = 0;
                while (true) {
                  arrayList4 = arrayList6;
                  if (b1 < b) {
                    arrayList6.add(null);
                    b1++;
                    continue;
                  } 
                  break;
                } 
              } 
            } 
            arrayList3 = arrayList1;
            arrayList5 = arrayList2;
            arrayList = arrayList4;
            if (arrayList4 != null) {
              arrayList4.add(fragment.mViewModelStore);
              arrayList = arrayList4;
              arrayList5 = arrayList2;
              arrayList3 = arrayList1;
            } 
          } 
          b++;
          arrayList2 = arrayList3;
          arrayList4 = arrayList5;
          arrayList6 = arrayList;
          continue;
        } 
        break;
      } 
    } 
    if (arrayList1 == null && arrayList3 == null && arrayList5 == null) {
      this.mSavedNonConfig = null;
    } else {
      this.mSavedNonConfig = new FragmentManagerNonConfig(arrayList1, (List)arrayList3, (List)arrayList5);
    } 
  }
  
  void scheduleCommit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   6: astore_1
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_1
    //   10: ifnull -> 28
    //   13: aload_0
    //   14: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   17: invokevirtual isEmpty : ()Z
    //   20: ifne -> 28
    //   23: iconst_1
    //   24: istore_3
    //   25: goto -> 30
    //   28: iconst_0
    //   29: istore_3
    //   30: iload_2
    //   31: istore #4
    //   33: aload_0
    //   34: getfield mPendingActions : Ljava/util/ArrayList;
    //   37: ifnull -> 57
    //   40: iload_2
    //   41: istore #4
    //   43: aload_0
    //   44: getfield mPendingActions : Ljava/util/ArrayList;
    //   47: invokevirtual size : ()I
    //   50: iconst_1
    //   51: if_icmpne -> 57
    //   54: iconst_1
    //   55: istore #4
    //   57: iload_3
    //   58: ifne -> 66
    //   61: iload #4
    //   63: ifeq -> 95
    //   66: aload_0
    //   67: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   70: invokevirtual getHandler : ()Landroid/os/Handler;
    //   73: aload_0
    //   74: getfield mExecCommit : Ljava/lang/Runnable;
    //   77: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   80: aload_0
    //   81: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   84: invokevirtual getHandler : ()Landroid/os/Handler;
    //   87: aload_0
    //   88: getfield mExecCommit : Ljava/lang/Runnable;
    //   91: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   94: pop
    //   95: aload_0
    //   96: monitorexit
    //   97: return
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	98	finally
    //   13	23	98	finally
    //   33	40	98	finally
    //   43	54	98	finally
    //   66	95	98	finally
    //   95	97	98	finally
    //   99	101	98	finally
  }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnonnull -> 22
    //   9: new java/util/ArrayList
    //   12: astore_3
    //   13: aload_3
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: aload_3
    //   19: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   22: aload_0
    //   23: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   26: invokevirtual size : ()I
    //   29: istore #4
    //   31: iload #4
    //   33: istore #5
    //   35: iload_1
    //   36: iload #4
    //   38: if_icmpge -> 106
    //   41: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   44: ifeq -> 93
    //   47: new java/lang/StringBuilder
    //   50: astore_3
    //   51: aload_3
    //   52: invokespecial <init> : ()V
    //   55: aload_3
    //   56: ldc_w 'Setting back stack index '
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_3
    //   64: iload_1
    //   65: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload_3
    //   70: ldc_w ' to '
    //   73: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: pop
    //   77: aload_3
    //   78: aload_2
    //   79: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   82: pop
    //   83: ldc 'FragmentManager'
    //   85: aload_3
    //   86: invokevirtual toString : ()Ljava/lang/String;
    //   89: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   92: pop
    //   93: aload_0
    //   94: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   97: iload_1
    //   98: aload_2
    //   99: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   102: pop
    //   103: goto -> 260
    //   106: iload #5
    //   108: iload_1
    //   109: if_icmpge -> 199
    //   112: aload_0
    //   113: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   116: aconst_null
    //   117: invokevirtual add : (Ljava/lang/Object;)Z
    //   120: pop
    //   121: aload_0
    //   122: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   125: ifnonnull -> 141
    //   128: new java/util/ArrayList
    //   131: astore_3
    //   132: aload_3
    //   133: invokespecial <init> : ()V
    //   136: aload_0
    //   137: aload_3
    //   138: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   141: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   144: ifeq -> 180
    //   147: new java/lang/StringBuilder
    //   150: astore_3
    //   151: aload_3
    //   152: invokespecial <init> : ()V
    //   155: aload_3
    //   156: ldc_w 'Adding available back stack index '
    //   159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: aload_3
    //   164: iload #5
    //   166: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   169: pop
    //   170: ldc 'FragmentManager'
    //   172: aload_3
    //   173: invokevirtual toString : ()Ljava/lang/String;
    //   176: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   179: pop
    //   180: aload_0
    //   181: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   184: iload #5
    //   186: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   189: invokevirtual add : (Ljava/lang/Object;)Z
    //   192: pop
    //   193: iinc #5, 1
    //   196: goto -> 106
    //   199: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   202: ifeq -> 251
    //   205: new java/lang/StringBuilder
    //   208: astore_3
    //   209: aload_3
    //   210: invokespecial <init> : ()V
    //   213: aload_3
    //   214: ldc_w 'Adding back stack index '
    //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: pop
    //   221: aload_3
    //   222: iload_1
    //   223: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   226: pop
    //   227: aload_3
    //   228: ldc_w ' with '
    //   231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload_3
    //   236: aload_2
    //   237: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   240: pop
    //   241: ldc 'FragmentManager'
    //   243: aload_3
    //   244: invokevirtual toString : ()Ljava/lang/String;
    //   247: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   250: pop
    //   251: aload_0
    //   252: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   255: aload_2
    //   256: invokevirtual add : (Ljava/lang/Object;)Z
    //   259: pop
    //   260: aload_0
    //   261: monitorexit
    //   262: return
    //   263: astore_2
    //   264: aload_0
    //   265: monitorexit
    //   266: aload_2
    //   267: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	263	finally
    //   22	31	263	finally
    //   41	93	263	finally
    //   93	103	263	finally
    //   112	141	263	finally
    //   141	180	263	finally
    //   180	193	263	finally
    //   199	251	263	finally
    //   251	260	263	finally
    //   260	262	263	finally
    //   264	266	263	finally
  }
  
  public void setPrimaryNavigationFragment(Fragment paramFragment) {
    if (paramFragment == null || (this.mActive.get(paramFragment.mIndex) == paramFragment && (paramFragment.mHost == null || paramFragment.getFragmentManager() == this))) {
      this.mPrimaryNav = paramFragment;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Fragment ");
    stringBuilder.append(paramFragment);
    stringBuilder.append(" is not an active fragment of FragmentManager ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void showFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("show: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      paramFragment.mHiddenChanged ^= 0x1;
    } 
  }
  
  void startPendingDeferredFragments() {
    if (this.mActive == null)
      return; 
    for (byte b = 0; b < this.mActive.size(); b++) {
      Fragment fragment = (Fragment)this.mActive.valueAt(b);
      if (fragment != null)
        performPendingDeferredStart(fragment); 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    Fragment fragment = this.mParent;
    if (fragment != null) {
      DebugUtils.buildShortClassTag(fragment, stringBuilder);
    } else {
      DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
    } 
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: iconst_0
    //   8: istore_3
    //   9: aload_0
    //   10: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   13: invokevirtual size : ()I
    //   16: istore #4
    //   18: iload_3
    //   19: iload #4
    //   21: if_icmpge -> 60
    //   24: aload_0
    //   25: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   28: iload_3
    //   29: invokevirtual get : (I)Ljava/lang/Object;
    //   32: checkcast android/support/v4/app/FragmentManagerImpl$FragmentLifecycleCallbacksHolder
    //   35: getfield mCallback : Landroid/support/v4/app/FragmentManager$FragmentLifecycleCallbacks;
    //   38: aload_1
    //   39: if_acmpne -> 54
    //   42: aload_0
    //   43: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   46: iload_3
    //   47: invokevirtual remove : (I)Ljava/lang/Object;
    //   50: pop
    //   51: goto -> 60
    //   54: iinc #3, 1
    //   57: goto -> 18
    //   60: aload_2
    //   61: monitorexit
    //   62: return
    //   63: astore_1
    //   64: aload_2
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   9	18	63	finally
    //   24	51	63	finally
    //   60	62	63	finally
    //   64	66	63	finally
  }
  
  private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper {
    View mView;
    
    AnimateOnHWLayerIfNeededListener(View param1View, Animation.AnimationListener param1AnimationListener) {
      super(param1AnimationListener);
      this.mView = param1View;
    }
    
    public void onAnimationEnd(Animation param1Animation) {
      if (ViewCompat.isAttachedToWindow(this.mView) || Build.VERSION.SDK_INT >= 24) {
        this.mView.post(new Runnable() {
              public void run() {
                FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, null);
              }
            });
      } else {
        this.mView.setLayerType(0, null);
      } 
      super.onAnimationEnd(param1Animation);
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$0.mView.setLayerType(0, null);
    }
  }
  
  private static class AnimationListenerWrapper implements Animation.AnimationListener {
    private final Animation.AnimationListener mWrapped;
    
    AnimationListenerWrapper(Animation.AnimationListener param1AnimationListener) {
      this.mWrapped = param1AnimationListener;
    }
    
    public void onAnimationEnd(Animation param1Animation) {
      Animation.AnimationListener animationListener = this.mWrapped;
      if (animationListener != null)
        animationListener.onAnimationEnd(param1Animation); 
    }
    
    public void onAnimationRepeat(Animation param1Animation) {
      Animation.AnimationListener animationListener = this.mWrapped;
      if (animationListener != null)
        animationListener.onAnimationRepeat(param1Animation); 
    }
    
    public void onAnimationStart(Animation param1Animation) {
      Animation.AnimationListener animationListener = this.mWrapped;
      if (animationListener != null)
        animationListener.onAnimationStart(param1Animation); 
    }
  }
  
  private static class AnimationOrAnimator {
    public final Animation animation = null;
    
    public final Animator animator;
    
    AnimationOrAnimator(Animator param1Animator) {
      this.animator = param1Animator;
      if (param1Animator != null)
        return; 
      throw new IllegalStateException("Animator cannot be null");
    }
    
    AnimationOrAnimator(Animation param1Animation) {
      this.animator = null;
      if (param1Animation != null)
        return; 
      throw new IllegalStateException("Animation cannot be null");
    }
  }
  
  private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
    View mView;
    
    AnimatorOnHWLayerIfNeededListener(View param1View) {
      this.mView = param1View;
    }
    
    public void onAnimationEnd(Animator param1Animator) {
      this.mView.setLayerType(0, null);
      param1Animator.removeListener((Animator.AnimatorListener)this);
    }
    
    public void onAnimationStart(Animator param1Animator) {
      this.mView.setLayerType(2, null);
    }
  }
  
  private static class EndViewTransitionAnimator extends AnimationSet implements Runnable {
    private boolean mAnimating = true;
    
    private final View mChild;
    
    private boolean mEnded;
    
    private final ViewGroup mParent;
    
    private boolean mTransitionEnded;
    
    EndViewTransitionAnimator(Animation param1Animation, ViewGroup param1ViewGroup, View param1View) {
      super(false);
      this.mParent = param1ViewGroup;
      this.mChild = param1View;
      addAnimation(param1Animation);
      this.mParent.post(this);
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation) {
      this.mAnimating = true;
      if (this.mEnded)
        return true ^ this.mTransitionEnded; 
      if (!super.getTransformation(param1Long, param1Transformation)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation, float param1Float) {
      this.mAnimating = true;
      if (this.mEnded)
        return true ^ this.mTransitionEnded; 
      if (!super.getTransformation(param1Long, param1Transformation, param1Float)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public void run() {
      if (!this.mEnded && this.mAnimating) {
        this.mAnimating = false;
        this.mParent.post(this);
      } else {
        this.mParent.endViewTransition(this.mChild);
        this.mTransitionEnded = true;
      } 
    }
  }
  
  private static final class FragmentLifecycleCallbacksHolder {
    final FragmentManager.FragmentLifecycleCallbacks mCallback;
    
    final boolean mRecursive;
    
    FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks param1FragmentLifecycleCallbacks, boolean param1Boolean) {
      this.mCallback = param1FragmentLifecycleCallbacks;
      this.mRecursive = param1Boolean;
    }
  }
  
  static class FragmentTag {
    public static final int[] Fragment = new int[] { 16842755, 16842960, 16842961 };
    
    public static final int Fragment_id = 1;
    
    public static final int Fragment_name = 0;
    
    public static final int Fragment_tag = 2;
  }
  
  static interface OpGenerator {
    boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1);
  }
  
  private class PopBackStackState implements OpGenerator {
    final int mFlags;
    
    final int mId;
    
    final String mName;
    
    PopBackStackState(String param1String, int param1Int1, int param1Int2) {
      this.mName = param1String;
      this.mId = param1Int1;
      this.mFlags = param1Int2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1) {
      if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
        FragmentManager fragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
        if (fragmentManager != null && fragmentManager.popBackStackImmediate())
          return false; 
      } 
      return FragmentManagerImpl.this.popBackStackState(param1ArrayList, param1ArrayList1, this.mName, this.mId, this.mFlags);
    }
  }
  
  static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
    final boolean mIsBack;
    
    private int mNumPostponed;
    
    final BackStackRecord mRecord;
    
    StartEnterTransitionListener(BackStackRecord param1BackStackRecord, boolean param1Boolean) {
      this.mIsBack = param1Boolean;
      this.mRecord = param1BackStackRecord;
    }
    
    public void cancelTransaction() {
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
    }
    
    public void completeTransaction() {
      int i = this.mNumPostponed;
      boolean bool = false;
      if (i > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      FragmentManagerImpl fragmentManagerImpl1 = this.mRecord.mManager;
      int j = fragmentManagerImpl1.mAdded.size();
      for (byte b = 0; b < j; b++) {
        Fragment fragment = fragmentManagerImpl1.mAdded.get(b);
        fragment.setOnStartEnterTransitionListener(null);
        if (i != 0 && fragment.isPostponed())
          fragment.startPostponedEnterTransition(); 
      } 
      FragmentManagerImpl fragmentManagerImpl2 = this.mRecord.mManager;
      BackStackRecord backStackRecord = this.mRecord;
      boolean bool1 = this.mIsBack;
      if (i == 0)
        bool = true; 
      fragmentManagerImpl2.completeExecute(backStackRecord, bool1, bool, true);
    }
    
    public boolean isReady() {
      boolean bool;
      if (this.mNumPostponed == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onStartEnterTransition() {
      int i = this.mNumPostponed - 1;
      this.mNumPostponed = i;
      if (i != 0)
        return; 
      this.mRecord.mManager.scheduleCommit();
    }
    
    public void startListening() {
      this.mNumPostponed++;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/app/FragmentManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */