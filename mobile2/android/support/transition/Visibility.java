package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public abstract class Visibility extends Transition {
  public static final int MODE_IN = 1;
  
  public static final int MODE_OUT = 2;
  
  private static final String PROPNAME_PARENT = "android:visibility:parent";
  
  private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
  
  static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
  
  private static final String[] sTransitionProperties = new String[] { "android:visibility:visibility", "android:visibility:parent" };
  
  private int mMode = 3;
  
  public Visibility() {}
  
  public Visibility(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.VISIBILITY_TRANSITION);
    int i = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)paramAttributeSet, "transitionVisibilityMode", 0, 0);
    typedArray.recycle();
    if (i != 0)
      setMode(i); 
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    int i = paramTransitionValues.view.getVisibility();
    paramTransitionValues.values.put("android:visibility:visibility", Integer.valueOf(i));
    paramTransitionValues.values.put("android:visibility:parent", paramTransitionValues.view.getParent());
    int[] arrayOfInt = new int[2];
    paramTransitionValues.view.getLocationOnScreen(arrayOfInt);
    paramTransitionValues.values.put("android:visibility:screenLocation", arrayOfInt);
  }
  
  private VisibilityInfo getVisibilityChangeInfo(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = new VisibilityInfo();
    visibilityInfo.mVisibilityChange = false;
    visibilityInfo.mFadeIn = false;
    if (paramTransitionValues1 != null && paramTransitionValues1.values.containsKey("android:visibility:visibility")) {
      visibilityInfo.mStartVisibility = ((Integer)paramTransitionValues1.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.mStartParent = (ViewGroup)paramTransitionValues1.values.get("android:visibility:parent");
    } else {
      visibilityInfo.mStartVisibility = -1;
      visibilityInfo.mStartParent = null;
    } 
    if (paramTransitionValues2 != null && paramTransitionValues2.values.containsKey("android:visibility:visibility")) {
      visibilityInfo.mEndVisibility = ((Integer)paramTransitionValues2.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.mEndParent = (ViewGroup)paramTransitionValues2.values.get("android:visibility:parent");
    } else {
      visibilityInfo.mEndVisibility = -1;
      visibilityInfo.mEndParent = null;
    } 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null) {
      if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent)
        return visibilityInfo; 
      if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
        if (visibilityInfo.mStartVisibility == 0) {
          visibilityInfo.mFadeIn = false;
          visibilityInfo.mVisibilityChange = true;
        } else if (visibilityInfo.mEndVisibility == 0) {
          visibilityInfo.mFadeIn = true;
          visibilityInfo.mVisibilityChange = true;
        } 
      } else if (visibilityInfo.mEndParent == null) {
        visibilityInfo.mFadeIn = false;
        visibilityInfo.mVisibilityChange = true;
      } else if (visibilityInfo.mStartParent == null) {
        visibilityInfo.mFadeIn = true;
        visibilityInfo.mVisibilityChange = true;
      } 
    } else if (paramTransitionValues1 == null && visibilityInfo.mEndVisibility == 0) {
      visibilityInfo.mFadeIn = true;
      visibilityInfo.mVisibilityChange = true;
    } else if (paramTransitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
      visibilityInfo.mFadeIn = false;
      visibilityInfo.mVisibilityChange = true;
    } 
    return visibilityInfo;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    return (visibilityInfo.mVisibilityChange && (visibilityInfo.mStartParent != null || visibilityInfo.mEndParent != null)) ? (visibilityInfo.mFadeIn ? onAppear(paramViewGroup, paramTransitionValues1, visibilityInfo.mStartVisibility, paramTransitionValues2, visibilityInfo.mEndVisibility) : onDisappear(paramViewGroup, paramTransitionValues1, visibilityInfo.mStartVisibility, paramTransitionValues2, visibilityInfo.mEndVisibility)) : null;
  }
  
  public int getMode() {
    return this.mMode;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool = false;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return false; 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null && paramTransitionValues2.values.containsKey("android:visibility:visibility") != paramTransitionValues1.values.containsKey("android:visibility:visibility"))
      return false; 
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    null = bool;
    if (visibilityInfo.mVisibilityChange) {
      if (visibilityInfo.mStartVisibility != 0) {
        null = bool;
        return (visibilityInfo.mEndVisibility == 0) ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues) {
    boolean bool1 = false;
    if (paramTransitionValues == null)
      return false; 
    int i = ((Integer)paramTransitionValues.values.get("android:visibility:visibility")).intValue();
    View view = (View)paramTransitionValues.values.get("android:visibility:parent");
    boolean bool2 = bool1;
    if (i == 0) {
      bool2 = bool1;
      if (view != null)
        bool2 = true; 
    } 
    return bool2;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    if ((this.mMode & 0x1) != 1 || paramTransitionValues2 == null)
      return null; 
    if (paramTransitionValues1 == null) {
      View view = (View)paramTransitionValues2.view.getParent();
      TransitionValues transitionValues2 = getMatchedTransitionValues(view, false);
      TransitionValues transitionValues1 = getTransitionValues(view, false);
      if ((getVisibilityChangeInfo(transitionValues2, transitionValues1)).mVisibilityChange)
        return null; 
    } 
    return onAppear(paramViewGroup, paramTransitionValues2.view, paramTransitionValues1, paramTransitionValues2);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    Animator animator;
    View view1;
    final View finalOverlayView;
    TransitionValues transitionValues2;
    if ((this.mMode & 0x2) != 2)
      return null; 
    if (paramTransitionValues1 != null) {
      view1 = paramTransitionValues1.view;
    } else {
      view1 = null;
    } 
    if (paramTransitionValues2 != null) {
      view2 = paramTransitionValues2.view;
    } else {
      view2 = null;
    } 
    View view3 = null;
    TransitionValues transitionValues1 = null;
    if (view2 == null || view2.getParent() == null) {
      if (view2 != null) {
        transitionValues2 = transitionValues1;
      } else {
        view2 = view3;
        transitionValues2 = transitionValues1;
        if (view1 != null)
          if (view1.getParent() == null) {
            view2 = view1;
            transitionValues2 = transitionValues1;
          } else {
            view2 = view3;
            transitionValues2 = transitionValues1;
            if (view1.getParent() instanceof View) {
              View view = (View)view1.getParent();
              transitionValues2 = getTransitionValues(view, true);
              TransitionValues transitionValues = getMatchedTransitionValues(view, true);
              if (!(getVisibilityChangeInfo(transitionValues2, transitionValues)).mVisibilityChange) {
                View view4 = TransitionUtils.copyViewImage(paramViewGroup, view1, view);
                transitionValues2 = transitionValues1;
              } else {
                view2 = view3;
                transitionValues2 = transitionValues1;
                if (view.getParent() == null) {
                  paramInt1 = view.getId();
                  view2 = view3;
                  transitionValues2 = transitionValues1;
                  if (paramInt1 != -1) {
                    view2 = view3;
                    transitionValues2 = transitionValues1;
                    if (paramViewGroup.findViewById(paramInt1) != null) {
                      view2 = view3;
                      transitionValues2 = transitionValues1;
                      if (this.mCanRemoveViews) {
                        view2 = view1;
                        transitionValues2 = transitionValues1;
                      } 
                    } 
                  } 
                } 
              } 
            } 
          }  
      } 
    } else if (paramInt2 == 4) {
      View view = view2;
      view2 = view3;
    } else if (view1 == view2) {
      View view = view2;
      view2 = view3;
    } else if (this.mCanRemoveViews) {
      view2 = view1;
      transitionValues2 = transitionValues1;
    } else {
      view2 = TransitionUtils.copyViewImage(paramViewGroup, view1, (View)view1.getParent());
      transitionValues2 = transitionValues1;
    } 
    if (view2 != null && paramTransitionValues1 != null) {
      int[] arrayOfInt = (int[])paramTransitionValues1.values.get("android:visibility:screenLocation");
      paramInt2 = arrayOfInt[0];
      paramInt1 = arrayOfInt[1];
      arrayOfInt = new int[2];
      paramViewGroup.getLocationOnScreen(arrayOfInt);
      view2.offsetLeftAndRight(paramInt2 - arrayOfInt[0] - view2.getLeft());
      view2.offsetTopAndBottom(paramInt1 - arrayOfInt[1] - view2.getTop());
      final ViewGroupOverlayImpl overlay = ViewGroupUtils.getOverlay(paramViewGroup);
      viewGroupOverlayImpl.add(view2);
      animator = onDisappear(paramViewGroup, view2, paramTransitionValues1, paramTransitionValues2);
      if (animator == null) {
        viewGroupOverlayImpl.remove(view2);
      } else {
        animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
              public void onAnimationEnd(Animator param1Animator) {
                overlay.remove(finalOverlayView);
              }
            });
      } 
      return animator;
    } 
    if (transitionValues2 != null) {
      paramInt1 = transitionValues2.getVisibility();
      ViewUtils.setTransitionVisibility((View)transitionValues2, 0);
      animator = onDisappear((ViewGroup)animator, (View)transitionValues2, paramTransitionValues1, paramTransitionValues2);
      if (animator != null) {
        DisappearListener disappearListener = new DisappearListener((View)transitionValues2, paramInt2, true);
        animator.addListener((Animator.AnimatorListener)disappearListener);
        AnimatorUtils.addPauseListener(animator, disappearListener);
        addListener(disappearListener);
      } else {
        ViewUtils.setTransitionVisibility((View)transitionValues2, paramInt1);
      } 
      return animator;
    } 
    return null;
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  public void setMode(int paramInt) {
    if ((paramInt & 0xFFFFFFFC) == 0) {
      this.mMode = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
  }
  
  private static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
    boolean mCanceled = false;
    
    private final int mFinalVisibility;
    
    private boolean mLayoutSuppressed;
    
    private final ViewGroup mParent;
    
    private final boolean mSuppressLayout;
    
    private final View mView;
    
    DisappearListener(View param1View, int param1Int, boolean param1Boolean) {
      this.mView = param1View;
      this.mFinalVisibility = param1Int;
      this.mParent = (ViewGroup)param1View.getParent();
      this.mSuppressLayout = param1Boolean;
      suppressLayout(true);
    }
    
    private void hideViewWhenNotCanceled() {
      if (!this.mCanceled) {
        ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
        ViewGroup viewGroup = this.mParent;
        if (viewGroup != null)
          viewGroup.invalidate(); 
      } 
      suppressLayout(false);
    }
    
    private void suppressLayout(boolean param1Boolean) {
      if (this.mSuppressLayout && this.mLayoutSuppressed != param1Boolean) {
        ViewGroup viewGroup = this.mParent;
        if (viewGroup != null) {
          this.mLayoutSuppressed = param1Boolean;
          ViewGroupUtils.suppressLayout(viewGroup, param1Boolean);
        } 
      } 
    }
    
    public void onAnimationCancel(Animator param1Animator) {
      this.mCanceled = true;
    }
    
    public void onAnimationEnd(Animator param1Animator) {
      hideViewWhenNotCanceled();
    }
    
    public void onAnimationPause(Animator param1Animator) {
      if (!this.mCanceled)
        ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility); 
    }
    
    public void onAnimationRepeat(Animator param1Animator) {}
    
    public void onAnimationResume(Animator param1Animator) {
      if (!this.mCanceled)
        ViewUtils.setTransitionVisibility(this.mView, 0); 
    }
    
    public void onAnimationStart(Animator param1Animator) {}
    
    public void onTransitionCancel(Transition param1Transition) {}
    
    public void onTransitionEnd(Transition param1Transition) {
      hideViewWhenNotCanceled();
      param1Transition.removeListener(this);
    }
    
    public void onTransitionPause(Transition param1Transition) {
      suppressLayout(false);
    }
    
    public void onTransitionResume(Transition param1Transition) {
      suppressLayout(true);
    }
    
    public void onTransitionStart(Transition param1Transition) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
  
  private static class VisibilityInfo {
    ViewGroup mEndParent;
    
    int mEndVisibility;
    
    boolean mFadeIn;
    
    ViewGroup mStartParent;
    
    int mStartVisibility;
    
    boolean mVisibilityChange;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/Visibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */