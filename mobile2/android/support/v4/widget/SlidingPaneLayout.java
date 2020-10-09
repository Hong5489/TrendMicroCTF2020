package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
  private static final int DEFAULT_FADE_COLOR = -858993460;
  
  private static final int DEFAULT_OVERHANG_SIZE = 32;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final String TAG = "SlidingPaneLayout";
  
  private boolean mCanSlide;
  
  private int mCoveredFadeColor;
  
  private boolean mDisplayListReflectionLoaded;
  
  final ViewDragHelper mDragHelper;
  
  private boolean mFirstLayout = true;
  
  private Method mGetDisplayList;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  boolean mIsUnableToDrag;
  
  private final int mOverhangSize;
  
  private PanelSlideListener mPanelSlideListener;
  
  private int mParallaxBy;
  
  private float mParallaxOffset;
  
  final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList<>();
  
  boolean mPreservedOpenState;
  
  private Field mRecreateDisplayList;
  
  private Drawable mShadowDrawableLeft;
  
  private Drawable mShadowDrawableRight;
  
  float mSlideOffset;
  
  int mSlideRange;
  
  View mSlideableView;
  
  private int mSliderFadeColor = -858993460;
  
  private final Rect mTmpRect = new Rect();
  
  public SlidingPaneLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mOverhangSize = (int)(32.0F * f + 0.5F);
    setWillNotDraw(false);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
    ViewCompat.setImportantForAccessibility((View)this, 1);
    ViewDragHelper viewDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback());
    this.mDragHelper = viewDragHelper;
    viewDragHelper.setMinVelocity(400.0F * f);
  }
  
  private boolean closePane(View paramView, int paramInt) {
    if (this.mFirstLayout || smoothSlideTo(0.0F, paramInt)) {
      this.mPreservedOpenState = false;
      return true;
    } 
    return false;
  }
  
  private void dimChildView(View paramView, float paramFloat, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat > 0.0F && paramInt != 0) {
      int i = (int)(((0xFF000000 & paramInt) >>> 24) * paramFloat);
      if (layoutParams.dimPaint == null)
        layoutParams.dimPaint = new Paint(); 
      layoutParams.dimPaint.setColorFilter((ColorFilter)new PorterDuffColorFilter(i << 24 | 0xFFFFFF & paramInt, PorterDuff.Mode.SRC_OVER));
      if (paramView.getLayerType() != 2)
        paramView.setLayerType(2, layoutParams.dimPaint); 
      invalidateChildRegion(paramView);
    } else if (paramView.getLayerType() != 0) {
      if (layoutParams.dimPaint != null)
        layoutParams.dimPaint.setColorFilter(null); 
      DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(paramView);
      this.mPostedRunnables.add(disableLayerRunnable);
      ViewCompat.postOnAnimation((View)this, disableLayerRunnable);
    } 
  }
  
  private boolean openPane(View paramView, int paramInt) {
    if (this.mFirstLayout || smoothSlideTo(1.0F, paramInt)) {
      this.mPreservedOpenState = true;
      return true;
    } 
    return false;
  }
  
  private void parallaxOtherViews(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore_2
    //   5: aload_0
    //   6: getfield mSlideableView : Landroid/view/View;
    //   9: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   12: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   15: astore_3
    //   16: aload_3
    //   17: getfield dimWhenOffset : Z
    //   20: ifeq -> 53
    //   23: iload_2
    //   24: ifeq -> 36
    //   27: aload_3
    //   28: getfield rightMargin : I
    //   31: istore #4
    //   33: goto -> 42
    //   36: aload_3
    //   37: getfield leftMargin : I
    //   40: istore #4
    //   42: iload #4
    //   44: ifgt -> 53
    //   47: iconst_1
    //   48: istore #4
    //   50: goto -> 56
    //   53: iconst_0
    //   54: istore #4
    //   56: aload_0
    //   57: invokevirtual getChildCount : ()I
    //   60: istore #5
    //   62: iconst_0
    //   63: istore #6
    //   65: iload #6
    //   67: iload #5
    //   69: if_icmpge -> 196
    //   72: aload_0
    //   73: iload #6
    //   75: invokevirtual getChildAt : (I)Landroid/view/View;
    //   78: astore_3
    //   79: aload_3
    //   80: aload_0
    //   81: getfield mSlideableView : Landroid/view/View;
    //   84: if_acmpne -> 90
    //   87: goto -> 190
    //   90: aload_0
    //   91: getfield mParallaxOffset : F
    //   94: fstore #7
    //   96: aload_0
    //   97: getfield mParallaxBy : I
    //   100: istore #8
    //   102: fconst_1
    //   103: fload #7
    //   105: fsub
    //   106: iload #8
    //   108: i2f
    //   109: fmul
    //   110: f2i
    //   111: istore #9
    //   113: aload_0
    //   114: fload_1
    //   115: putfield mParallaxOffset : F
    //   118: iload #9
    //   120: fconst_1
    //   121: fload_1
    //   122: fsub
    //   123: iload #8
    //   125: i2f
    //   126: fmul
    //   127: f2i
    //   128: isub
    //   129: istore #9
    //   131: iload_2
    //   132: ifeq -> 143
    //   135: iload #9
    //   137: ineg
    //   138: istore #9
    //   140: goto -> 143
    //   143: aload_3
    //   144: iload #9
    //   146: invokevirtual offsetLeftAndRight : (I)V
    //   149: iload #4
    //   151: ifeq -> 190
    //   154: aload_0
    //   155: getfield mParallaxOffset : F
    //   158: fstore #7
    //   160: iload_2
    //   161: ifeq -> 173
    //   164: fload #7
    //   166: fconst_1
    //   167: fsub
    //   168: fstore #7
    //   170: goto -> 179
    //   173: fconst_1
    //   174: fload #7
    //   176: fsub
    //   177: fstore #7
    //   179: aload_0
    //   180: aload_3
    //   181: fload #7
    //   183: aload_0
    //   184: getfield mCoveredFadeColor : I
    //   187: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   190: iinc #6, 1
    //   193: goto -> 65
    //   196: return
  }
  
  private static boolean viewIsOpaque(View paramView) {
    boolean bool = paramView.isOpaque();
    boolean bool1 = true;
    if (bool)
      return true; 
    if (Build.VERSION.SDK_INT >= 18)
      return false; 
    Drawable drawable = paramView.getBackground();
    if (drawable != null) {
      if (drawable.getOpacity() != -1)
        bool1 = false; 
      return bool1;
    } 
    return false;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = paramView instanceof ViewGroup;
    boolean bool1 = true;
    if (bool) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = viewGroup.getChildCount() - 1; k >= 0; k--) {
        View view = viewGroup.getChildAt(k);
        if (paramInt2 + i >= view.getLeft() && paramInt2 + i < view.getRight() && paramInt3 + j >= view.getTop() && paramInt3 + j < view.getBottom() && canScroll(view, true, paramInt1, paramInt2 + i - view.getLeft(), paramInt3 + j - view.getTop()))
          return true; 
      } 
    } 
    if (paramBoolean) {
      if (!isLayoutRtlSupport())
        paramInt1 = -paramInt1; 
      if (paramView.canScrollHorizontally(paramInt1))
        return bool1; 
    } 
    return false;
  }
  
  @Deprecated
  public boolean canSlide() {
    return this.mCanSlide;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean closePane() {
    return closePane(this.mSlideableView, 0);
  }
  
  public void computeScroll() {
    if (this.mDragHelper.continueSettling(true)) {
      if (!this.mCanSlide) {
        this.mDragHelper.abort();
        return;
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  void dispatchOnPanelClosed(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelClosed(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelOpened(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelOpened(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelSlide(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelSlide(paramView, this.mSlideOffset); 
  }
  
  public void draw(Canvas paramCanvas) {
    Drawable drawable;
    View view;
    int m;
    int n;
    super.draw(paramCanvas);
    if (isLayoutRtlSupport()) {
      drawable = this.mShadowDrawableRight;
    } else {
      drawable = this.mShadowDrawableLeft;
    } 
    if (getChildCount() > 1) {
      view = getChildAt(1);
    } else {
      view = null;
    } 
    if (view == null || drawable == null)
      return; 
    int i = view.getTop();
    int j = view.getBottom();
    int k = drawable.getIntrinsicWidth();
    if (isLayoutRtlSupport()) {
      m = view.getRight();
      n = m + k;
    } else {
      n = view.getLeft();
      m = n - k;
    } 
    drawable.setBounds(m, i, n, j);
    drawable.draw(paramCanvas);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save();
    if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
      paramCanvas.getClipBounds(this.mTmpRect);
      if (isLayoutRtlSupport()) {
        Rect rect = this.mTmpRect;
        rect.left = Math.max(rect.left, this.mSlideableView.getRight());
      } else {
        Rect rect = this.mTmpRect;
        rect.right = Math.min(rect.right, this.mSlideableView.getLeft());
      } 
      paramCanvas.clipRect(this.mTmpRect);
    } 
    boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(i);
    return bool;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    LayoutParams layoutParams;
    if (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) {
      layoutParams = new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    } else {
      layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
    return (ViewGroup.LayoutParams)layoutParams;
  }
  
  public int getCoveredFadeColor() {
    return this.mCoveredFadeColor;
  }
  
  public int getParallaxDistance() {
    return this.mParallaxBy;
  }
  
  public int getSliderFadeColor() {
    return this.mSliderFadeColor;
  }
  
  void invalidateChildRegion(View paramView) {
    // Byte code:
    //   0: getstatic android/os/Build$VERSION.SDK_INT : I
    //   3: bipush #17
    //   5: if_icmplt -> 23
    //   8: aload_1
    //   9: aload_1
    //   10: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   13: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   16: getfield dimPaint : Landroid/graphics/Paint;
    //   19: invokestatic setLayerPaint : (Landroid/view/View;Landroid/graphics/Paint;)V
    //   22: return
    //   23: getstatic android/os/Build$VERSION.SDK_INT : I
    //   26: bipush #16
    //   28: if_icmplt -> 166
    //   31: aload_0
    //   32: getfield mDisplayListReflectionLoaded : Z
    //   35: ifne -> 106
    //   38: aload_0
    //   39: ldc android/view/View
    //   41: ldc_w 'getDisplayList'
    //   44: aconst_null
    //   45: checkcast [Ljava/lang/Class;
    //   48: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   51: putfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   54: goto -> 68
    //   57: astore_2
    //   58: ldc 'SlidingPaneLayout'
    //   60: ldc_w 'Couldn't fetch getDisplayList method; dimming won't work right.'
    //   63: aload_2
    //   64: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   67: pop
    //   68: ldc android/view/View
    //   70: ldc_w 'mRecreateDisplayList'
    //   73: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   76: astore_2
    //   77: aload_0
    //   78: aload_2
    //   79: putfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   82: aload_2
    //   83: iconst_1
    //   84: invokevirtual setAccessible : (Z)V
    //   87: goto -> 101
    //   90: astore_2
    //   91: ldc 'SlidingPaneLayout'
    //   93: ldc_w 'Couldn't fetch mRecreateDisplayList field; dimming will be slow.'
    //   96: aload_2
    //   97: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   100: pop
    //   101: aload_0
    //   102: iconst_1
    //   103: putfield mDisplayListReflectionLoaded : Z
    //   106: aload_0
    //   107: getfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   110: ifnull -> 161
    //   113: aload_0
    //   114: getfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   117: astore_2
    //   118: aload_2
    //   119: ifnonnull -> 125
    //   122: goto -> 161
    //   125: aload_2
    //   126: aload_1
    //   127: iconst_1
    //   128: invokevirtual setBoolean : (Ljava/lang/Object;Z)V
    //   131: aload_0
    //   132: getfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   135: aload_1
    //   136: aconst_null
    //   137: checkcast [Ljava/lang/Object;
    //   140: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   143: pop
    //   144: goto -> 166
    //   147: astore_2
    //   148: ldc 'SlidingPaneLayout'
    //   150: ldc_w 'Error refreshing display list state'
    //   153: aload_2
    //   154: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   157: pop
    //   158: goto -> 166
    //   161: aload_1
    //   162: invokevirtual invalidate : ()V
    //   165: return
    //   166: aload_0
    //   167: aload_1
    //   168: invokevirtual getLeft : ()I
    //   171: aload_1
    //   172: invokevirtual getTop : ()I
    //   175: aload_1
    //   176: invokevirtual getRight : ()I
    //   179: aload_1
    //   180: invokevirtual getBottom : ()I
    //   183: invokestatic postInvalidateOnAnimation : (Landroid/view/View;IIII)V
    //   186: return
    // Exception table:
    //   from	to	target	type
    //   38	54	57	java/lang/NoSuchMethodException
    //   68	87	90	java/lang/NoSuchFieldException
    //   125	144	147	java/lang/Exception
  }
  
  boolean isDimmed(View paramView) {
    boolean bool1 = false;
    if (paramView == null)
      return false; 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    boolean bool2 = bool1;
    if (this.mCanSlide) {
      bool2 = bool1;
      if (layoutParams.dimWhenOffset) {
        bool2 = bool1;
        if (this.mSlideOffset > 0.0F)
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  boolean isLayoutRtlSupport() {
    int i = ViewCompat.getLayoutDirection((View)this);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isOpen() {
    return (!this.mCanSlide || this.mSlideOffset == 1.0F);
  }
  
  public boolean isSlideable() {
    return this.mCanSlide;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
    byte b = 0;
    int i = this.mPostedRunnables.size();
    while (b < i) {
      ((DisableLayerRunnable)this.mPostedRunnables.get(b)).run();
      b++;
    } 
    this.mPostedRunnables.clear();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    boolean bool = this.mCanSlide;
    boolean bool1 = true;
    if (!bool && i == 0 && getChildCount() > 1) {
      View view = getChildAt(1);
      if (view != null)
        this.mPreservedOpenState = this.mDragHelper.isViewUnder(view, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()) ^ true; 
    } 
    if (!this.mCanSlide || (this.mIsUnableToDrag && i != 0)) {
      this.mDragHelper.cancel();
      return super.onInterceptTouchEvent(paramMotionEvent);
    } 
    if (i == 3 || i == 1) {
      this.mDragHelper.cancel();
      return false;
    } 
    boolean bool2 = false;
    if (i != 0) {
      if (i != 2) {
        i = bool2;
      } else {
        float f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        f1 = Math.abs(f1 - this.mInitialMotionX);
        f2 = Math.abs(f2 - this.mInitialMotionY);
        i = bool2;
        if (f1 > this.mDragHelper.getTouchSlop()) {
          i = bool2;
          if (f2 > f1) {
            this.mDragHelper.cancel();
            this.mIsUnableToDrag = true;
            return false;
          } 
        } 
      } 
    } else {
      this.mIsUnableToDrag = false;
      float f2 = paramMotionEvent.getX();
      float f1 = paramMotionEvent.getY();
      this.mInitialMotionX = f2;
      this.mInitialMotionY = f1;
      i = bool2;
      if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)f2, (int)f1)) {
        i = bool2;
        if (isDimmed(this.mSlideableView))
          i = 1; 
      } 
    } 
    bool = bool1;
    if (!this.mDragHelper.shouldInterceptTouchEvent(paramMotionEvent))
      if (i != 0) {
        bool = bool1;
      } else {
        bool = false;
      }  
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore #6
    //   6: iload #6
    //   8: ifeq -> 22
    //   11: aload_0
    //   12: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   15: iconst_2
    //   16: invokevirtual setEdgeTrackingEnabled : (I)V
    //   19: goto -> 30
    //   22: aload_0
    //   23: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   26: iconst_1
    //   27: invokevirtual setEdgeTrackingEnabled : (I)V
    //   30: iload #4
    //   32: iload_2
    //   33: isub
    //   34: istore #7
    //   36: iload #6
    //   38: ifeq -> 49
    //   41: aload_0
    //   42: invokevirtual getPaddingRight : ()I
    //   45: istore_3
    //   46: goto -> 54
    //   49: aload_0
    //   50: invokevirtual getPaddingLeft : ()I
    //   53: istore_3
    //   54: iload #6
    //   56: ifeq -> 68
    //   59: aload_0
    //   60: invokevirtual getPaddingLeft : ()I
    //   63: istore #5
    //   65: goto -> 74
    //   68: aload_0
    //   69: invokevirtual getPaddingRight : ()I
    //   72: istore #5
    //   74: aload_0
    //   75: invokevirtual getPaddingTop : ()I
    //   78: istore #8
    //   80: aload_0
    //   81: invokevirtual getChildCount : ()I
    //   84: istore #9
    //   86: iload_3
    //   87: istore #4
    //   89: iload #4
    //   91: istore_2
    //   92: aload_0
    //   93: getfield mFirstLayout : Z
    //   96: ifeq -> 128
    //   99: aload_0
    //   100: getfield mCanSlide : Z
    //   103: ifeq -> 119
    //   106: aload_0
    //   107: getfield mPreservedOpenState : Z
    //   110: ifeq -> 119
    //   113: fconst_1
    //   114: fstore #10
    //   116: goto -> 122
    //   119: fconst_0
    //   120: fstore #10
    //   122: aload_0
    //   123: fload #10
    //   125: putfield mSlideOffset : F
    //   128: iconst_0
    //   129: istore #11
    //   131: iload #4
    //   133: istore #12
    //   135: iload_3
    //   136: istore #4
    //   138: iload #11
    //   140: iload #9
    //   142: if_icmpge -> 444
    //   145: aload_0
    //   146: iload #11
    //   148: invokevirtual getChildAt : (I)Landroid/view/View;
    //   151: astore #13
    //   153: aload #13
    //   155: invokevirtual getVisibility : ()I
    //   158: bipush #8
    //   160: if_icmpne -> 169
    //   163: iload #12
    //   165: istore_3
    //   166: goto -> 435
    //   169: aload #13
    //   171: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   174: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   177: astore #14
    //   179: aload #13
    //   181: invokevirtual getMeasuredWidth : ()I
    //   184: istore #15
    //   186: iconst_0
    //   187: istore #16
    //   189: aload #14
    //   191: getfield slideable : Z
    //   194: ifeq -> 332
    //   197: aload #14
    //   199: getfield leftMargin : I
    //   202: istore_3
    //   203: aload #14
    //   205: getfield rightMargin : I
    //   208: istore #17
    //   210: iload_2
    //   211: iload #7
    //   213: iload #5
    //   215: isub
    //   216: aload_0
    //   217: getfield mOverhangSize : I
    //   220: isub
    //   221: invokestatic min : (II)I
    //   224: iload #12
    //   226: isub
    //   227: iload_3
    //   228: iload #17
    //   230: iadd
    //   231: isub
    //   232: istore #17
    //   234: aload_0
    //   235: iload #17
    //   237: putfield mSlideRange : I
    //   240: iload #6
    //   242: ifeq -> 254
    //   245: aload #14
    //   247: getfield rightMargin : I
    //   250: istore_3
    //   251: goto -> 260
    //   254: aload #14
    //   256: getfield leftMargin : I
    //   259: istore_3
    //   260: iload #12
    //   262: iload_3
    //   263: iadd
    //   264: iload #17
    //   266: iadd
    //   267: iload #15
    //   269: iconst_2
    //   270: idiv
    //   271: iadd
    //   272: iload #7
    //   274: iload #5
    //   276: isub
    //   277: if_icmple -> 285
    //   280: iconst_1
    //   281: istore_1
    //   282: goto -> 287
    //   285: iconst_0
    //   286: istore_1
    //   287: aload #14
    //   289: iload_1
    //   290: putfield dimWhenOffset : Z
    //   293: iload #17
    //   295: i2f
    //   296: aload_0
    //   297: getfield mSlideOffset : F
    //   300: fmul
    //   301: f2i
    //   302: istore #17
    //   304: iload #12
    //   306: iload #17
    //   308: iload_3
    //   309: iadd
    //   310: iadd
    //   311: istore_3
    //   312: aload_0
    //   313: iload #17
    //   315: i2f
    //   316: aload_0
    //   317: getfield mSlideRange : I
    //   320: i2f
    //   321: fdiv
    //   322: putfield mSlideOffset : F
    //   325: iload #16
    //   327: istore #12
    //   329: goto -> 371
    //   332: aload_0
    //   333: getfield mCanSlide : Z
    //   336: ifeq -> 365
    //   339: aload_0
    //   340: getfield mParallaxBy : I
    //   343: istore_3
    //   344: iload_3
    //   345: ifeq -> 365
    //   348: fconst_1
    //   349: aload_0
    //   350: getfield mSlideOffset : F
    //   353: fsub
    //   354: iload_3
    //   355: i2f
    //   356: fmul
    //   357: f2i
    //   358: istore #12
    //   360: iload_2
    //   361: istore_3
    //   362: goto -> 371
    //   365: iload_2
    //   366: istore_3
    //   367: iload #16
    //   369: istore #12
    //   371: iload #6
    //   373: ifeq -> 395
    //   376: iload #7
    //   378: iload_3
    //   379: isub
    //   380: iload #12
    //   382: iadd
    //   383: istore #16
    //   385: iload #16
    //   387: iload #15
    //   389: isub
    //   390: istore #12
    //   392: goto -> 408
    //   395: iload_3
    //   396: iload #12
    //   398: isub
    //   399: istore #12
    //   401: iload #12
    //   403: iload #15
    //   405: iadd
    //   406: istore #16
    //   408: aload #13
    //   410: iload #12
    //   412: iload #8
    //   414: iload #16
    //   416: aload #13
    //   418: invokevirtual getMeasuredHeight : ()I
    //   421: iload #8
    //   423: iadd
    //   424: invokevirtual layout : (IIII)V
    //   427: iload_2
    //   428: aload #13
    //   430: invokevirtual getWidth : ()I
    //   433: iadd
    //   434: istore_2
    //   435: iinc #11, 1
    //   438: iload_3
    //   439: istore #12
    //   441: goto -> 138
    //   444: aload_0
    //   445: getfield mFirstLayout : Z
    //   448: ifeq -> 544
    //   451: aload_0
    //   452: getfield mCanSlide : Z
    //   455: ifeq -> 508
    //   458: aload_0
    //   459: getfield mParallaxBy : I
    //   462: ifeq -> 473
    //   465: aload_0
    //   466: aload_0
    //   467: getfield mSlideOffset : F
    //   470: invokespecial parallaxOtherViews : (F)V
    //   473: aload_0
    //   474: getfield mSlideableView : Landroid/view/View;
    //   477: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   480: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   483: getfield dimWhenOffset : Z
    //   486: ifeq -> 536
    //   489: aload_0
    //   490: aload_0
    //   491: getfield mSlideableView : Landroid/view/View;
    //   494: aload_0
    //   495: getfield mSlideOffset : F
    //   498: aload_0
    //   499: getfield mSliderFadeColor : I
    //   502: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   505: goto -> 536
    //   508: iconst_0
    //   509: istore_2
    //   510: iload_2
    //   511: iload #9
    //   513: if_icmpge -> 536
    //   516: aload_0
    //   517: aload_0
    //   518: iload_2
    //   519: invokevirtual getChildAt : (I)Landroid/view/View;
    //   522: fconst_0
    //   523: aload_0
    //   524: getfield mSliderFadeColor : I
    //   527: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   530: iinc #2, 1
    //   533: goto -> 510
    //   536: aload_0
    //   537: aload_0
    //   538: getfield mSlideableView : Landroid/view/View;
    //   541: invokevirtual updateObscuredViewsVisibility : (Landroid/view/View;)V
    //   544: aload_0
    //   545: iconst_0
    //   546: putfield mFirstLayout : Z
    //   549: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int m;
    int n;
    int i1;
    int i = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt2);
    if (i != 1073741824) {
      if (isInEditMode()) {
        if (i == Integer.MIN_VALUE) {
          paramInt2 = 1073741824;
          m = paramInt1;
          n = j;
          i1 = k;
        } else {
          paramInt2 = i;
          m = paramInt1;
          n = j;
          i1 = k;
          if (i == 0) {
            paramInt2 = 1073741824;
            m = 300;
            n = j;
            i1 = k;
          } 
        } 
      } else {
        throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
      } 
    } else {
      paramInt2 = i;
      m = paramInt1;
      n = j;
      i1 = k;
      if (j == 0)
        if (isInEditMode()) {
          paramInt2 = i;
          m = paramInt1;
          n = j;
          i1 = k;
          if (j == 0) {
            n = Integer.MIN_VALUE;
            i1 = 300;
            paramInt2 = i;
            m = paramInt1;
          } 
        } else {
          throw new IllegalStateException("Height must not be UNSPECIFIED");
        }  
    } 
    i = 0;
    paramInt1 = 0;
    if (n != Integer.MIN_VALUE) {
      if (n == 1073741824) {
        i = i1 - getPaddingTop() - getPaddingBottom();
        paramInt1 = i;
      } 
    } else {
      paramInt1 = i1 - getPaddingTop() - getPaddingBottom();
    } 
    float f = 0.0F;
    int i2 = 0;
    int i3 = m - getPaddingLeft() - getPaddingRight();
    j = i3;
    int i4 = getChildCount();
    if (i4 > 2)
      Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported."); 
    this.mSlideableView = null;
    k = 0;
    int i5 = i1;
    int i6 = paramInt2;
    while (k < i4) {
      int i8;
      View view = getChildAt(k);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (view.getVisibility() == 8) {
        layoutParams.dimWhenOffset = false;
        paramInt2 = i;
        continue;
      } 
      float f1 = f;
      if (layoutParams.weight > 0.0F) {
        f += layoutParams.weight;
        f1 = f;
        if (layoutParams.width == 0) {
          paramInt2 = i;
          continue;
        } 
      } 
      paramInt2 = layoutParams.leftMargin + layoutParams.rightMargin;
      if (layoutParams.width == -2) {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i3 - paramInt2, -2147483648);
      } else if (layoutParams.width == -1) {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i3 - paramInt2, 1073741824);
      } else {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
      } 
      if (layoutParams.height == -2) {
        i1 = View.MeasureSpec.makeMeasureSpec(paramInt1, -2147483648);
      } else if (layoutParams.height == -1) {
        i1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      } else {
        i1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
      } 
      view.measure(paramInt2, i1);
      i1 = view.getMeasuredWidth();
      int i7 = view.getMeasuredHeight();
      paramInt2 = i;
      if (n == Integer.MIN_VALUE) {
        paramInt2 = i;
        if (i7 > i)
          paramInt2 = Math.min(i7, paramInt1); 
      } 
      j -= i1;
      if (j < 0) {
        i8 = 1;
      } else {
        i8 = 0;
      } 
      layoutParams.slideable = i8;
      if (layoutParams.slideable)
        this.mSlideableView = view; 
      i2 = i8 | i2;
      f = f1;
      continue;
      k++;
      i = paramInt2;
    } 
    if (i2 != 0 || f > 0.0F) {
      k = i3 - this.mOverhangSize;
      i5 = 0;
      i1 = i4;
      paramInt2 = paramInt1;
      while (i5 < i1) {
        View view = getChildAt(i5);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (view.getVisibility() != 8) {
            if (layoutParams.width == 0 && layoutParams.weight > 0.0F) {
              paramInt1 = 1;
            } else {
              paramInt1 = 0;
            } 
            if (paramInt1 != 0) {
              i6 = 0;
            } else {
              i6 = view.getMeasuredWidth();
            } 
            if (i2 != 0 && view != this.mSlideableView) {
              if (layoutParams.width < 0 && (i6 > k || layoutParams.weight > 0.0F)) {
                if (paramInt1 != 0) {
                  if (layoutParams.height == -2) {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, -2147483648);
                  } else if (layoutParams.height == -1) {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
                  } else {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                  } 
                } else {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824);
                } 
                view.measure(View.MeasureSpec.makeMeasureSpec(k, 1073741824), paramInt1);
              } 
            } else if (layoutParams.weight > 0.0F) {
              if (layoutParams.width == 0) {
                if (layoutParams.height == -2) {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, -2147483648);
                } else if (layoutParams.height == -1) {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
                } else {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                } 
              } else {
                paramInt1 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824);
              } 
              if (i2 != 0) {
                int i7 = i3 - layoutParams.leftMargin + layoutParams.rightMargin;
                i4 = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
                if (i6 != i7)
                  view.measure(i4, paramInt1); 
              } else {
                i4 = Math.max(0, j);
                view.measure(View.MeasureSpec.makeMeasureSpec(i6 + (int)(layoutParams.weight * i4 / f), 1073741824), paramInt1);
              } 
            } 
          } 
        } 
        i5++;
      } 
    } 
    setMeasuredDimension(m, getPaddingTop() + i + getPaddingBottom());
    this.mCanSlide = i2;
    if (this.mDragHelper.getViewDragState() != 0 && i2 == 0)
      this.mDragHelper.abort(); 
  }
  
  void onPanelDragged(int paramInt) {
    int j;
    if (this.mSlideableView == null) {
      this.mSlideOffset = 0.0F;
      return;
    } 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i = this.mSlideableView.getWidth();
    if (bool)
      paramInt = getWidth() - paramInt - i; 
    if (bool) {
      i = getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = layoutParams.rightMargin;
    } else {
      j = layoutParams.leftMargin;
    } 
    float f = (paramInt - i + j) / this.mSlideRange;
    this.mSlideOffset = f;
    if (this.mParallaxBy != 0)
      parallaxOtherViews(f); 
    if (layoutParams.dimWhenOffset)
      dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor); 
    dispatchOnPanelSlide(this.mSlideableView);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.isOpen) {
      openPane();
    } else {
      closePane();
    } 
    this.mPreservedOpenState = savedState.isOpen;
  }
  
  protected Parcelable onSaveInstanceState() {
    boolean bool;
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (isSlideable()) {
      bool = isOpen();
    } else {
      bool = this.mPreservedOpenState;
    } 
    savedState.isOpen = bool;
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      this.mFirstLayout = true; 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!this.mCanSlide)
      return super.onTouchEvent(paramMotionEvent); 
    this.mDragHelper.processTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    if (i != 0) {
      if (i == 1 && isDimmed(this.mSlideableView)) {
        float f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        float f3 = f1 - this.mInitialMotionX;
        float f4 = f2 - this.mInitialMotionY;
        i = this.mDragHelper.getTouchSlop();
        if (f3 * f3 + f4 * f4 < (i * i) && this.mDragHelper.isViewUnder(this.mSlideableView, (int)f1, (int)f2))
          closePane(this.mSlideableView, 0); 
      } 
    } else {
      float f2 = paramMotionEvent.getX();
      float f1 = paramMotionEvent.getY();
      this.mInitialMotionX = f2;
      this.mInitialMotionY = f1;
    } 
    return true;
  }
  
  public boolean openPane() {
    return openPane(this.mSlideableView, 0);
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    super.requestChildFocus(paramView1, paramView2);
    if (!isInTouchMode() && !this.mCanSlide) {
      boolean bool;
      if (paramView1 == this.mSlideableView) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mPreservedOpenState = bool;
    } 
  }
  
  void setAllChildrenVisible() {
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      if (view.getVisibility() == 4)
        view.setVisibility(0); 
      b++;
    } 
  }
  
  public void setCoveredFadeColor(int paramInt) {
    this.mCoveredFadeColor = paramInt;
  }
  
  public void setPanelSlideListener(PanelSlideListener paramPanelSlideListener) {
    this.mPanelSlideListener = paramPanelSlideListener;
  }
  
  public void setParallaxDistance(int paramInt) {
    this.mParallaxBy = paramInt;
    requestLayout();
  }
  
  @Deprecated
  public void setShadowDrawable(Drawable paramDrawable) {
    setShadowDrawableLeft(paramDrawable);
  }
  
  public void setShadowDrawableLeft(Drawable paramDrawable) {
    this.mShadowDrawableLeft = paramDrawable;
  }
  
  public void setShadowDrawableRight(Drawable paramDrawable) {
    this.mShadowDrawableRight = paramDrawable;
  }
  
  @Deprecated
  public void setShadowResource(int paramInt) {
    setShadowDrawable(getResources().getDrawable(paramInt));
  }
  
  public void setShadowResourceLeft(int paramInt) {
    setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setShadowResourceRight(int paramInt) {
    setShadowDrawableRight(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setSliderFadeColor(int paramInt) {
    this.mSliderFadeColor = paramInt;
  }
  
  @Deprecated
  public void smoothSlideClosed() {
    closePane();
  }
  
  @Deprecated
  public void smoothSlideOpen() {
    openPane();
  }
  
  boolean smoothSlideTo(float paramFloat, int paramInt) {
    if (!this.mCanSlide)
      return false; 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    if (bool) {
      int i = getPaddingRight();
      paramInt = layoutParams.rightMargin;
      int j = this.mSlideableView.getWidth();
      paramInt = (int)(getWidth() - (i + paramInt) + this.mSlideRange * paramFloat + j);
    } else {
      paramInt = (int)((getPaddingLeft() + layoutParams.leftMargin) + this.mSlideRange * paramFloat);
    } 
    ViewDragHelper viewDragHelper = this.mDragHelper;
    View view = this.mSlideableView;
    if (viewDragHelper.smoothSlideViewTo(view, paramInt, view.getTop())) {
      setAllChildrenVisible();
      ViewCompat.postInvalidateOnAnimation((View)this);
      return true;
    } 
    return false;
  }
  
  void updateObscuredViewsVisibility(View paramView) {
    int i;
    int j;
    byte b1;
    byte b2;
    byte b3;
    byte b4;
    boolean bool = isLayoutRtlSupport();
    if (bool) {
      i = getWidth() - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = getPaddingLeft();
    } else {
      j = getWidth() - getPaddingRight();
    } 
    int k = getPaddingTop();
    int m = getHeight();
    int n = getPaddingBottom();
    if (paramView != null && viewIsOpaque(paramView)) {
      b1 = paramView.getLeft();
      b2 = paramView.getRight();
      b3 = paramView.getTop();
      b4 = paramView.getBottom();
    } else {
      b1 = 0;
      b4 = 0;
      b3 = 0;
      b2 = 0;
    } 
    byte b5 = 0;
    int i1 = getChildCount();
    while (b5 < i1) {
      View view = getChildAt(b5);
      if (view == paramView)
        break; 
      if (view.getVisibility() != 8) {
        if (bool) {
          i2 = j;
        } else {
          i2 = i;
        } 
        int i3 = Math.max(i2, view.getLeft());
        int i4 = Math.max(k, view.getTop());
        if (bool) {
          i2 = i;
        } else {
          i2 = j;
        } 
        int i5 = Math.min(i2, view.getRight());
        int i2 = Math.min(m - n, view.getBottom());
        if (i3 >= b1 && i4 >= b3 && i5 <= b2 && i2 <= b4) {
          i2 = 4;
        } else {
          i2 = 0;
        } 
        view.setVisibility(i2);
      } 
      b5++;
    } 
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat2) {
      Rect rect = this.mTmpRect;
      param1AccessibilityNodeInfoCompat2.getBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat2.getBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setVisibleToUser(param1AccessibilityNodeInfoCompat2.isVisibleToUser());
      param1AccessibilityNodeInfoCompat1.setPackageName(param1AccessibilityNodeInfoCompat2.getPackageName());
      param1AccessibilityNodeInfoCompat1.setClassName(param1AccessibilityNodeInfoCompat2.getClassName());
      param1AccessibilityNodeInfoCompat1.setContentDescription(param1AccessibilityNodeInfoCompat2.getContentDescription());
      param1AccessibilityNodeInfoCompat1.setEnabled(param1AccessibilityNodeInfoCompat2.isEnabled());
      param1AccessibilityNodeInfoCompat1.setClickable(param1AccessibilityNodeInfoCompat2.isClickable());
      param1AccessibilityNodeInfoCompat1.setFocusable(param1AccessibilityNodeInfoCompat2.isFocusable());
      param1AccessibilityNodeInfoCompat1.setFocused(param1AccessibilityNodeInfoCompat2.isFocused());
      param1AccessibilityNodeInfoCompat1.setAccessibilityFocused(param1AccessibilityNodeInfoCompat2.isAccessibilityFocused());
      param1AccessibilityNodeInfoCompat1.setSelected(param1AccessibilityNodeInfoCompat2.isSelected());
      param1AccessibilityNodeInfoCompat1.setLongClickable(param1AccessibilityNodeInfoCompat2.isLongClickable());
      param1AccessibilityNodeInfoCompat1.addAction(param1AccessibilityNodeInfoCompat2.getActions());
      param1AccessibilityNodeInfoCompat1.setMovementGranularities(param1AccessibilityNodeInfoCompat2.getMovementGranularities());
    }
    
    public boolean filter(View param1View) {
      return SlidingPaneLayout.this.isDimmed(param1View);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
      copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
      accessibilityNodeInfoCompat.recycle();
      param1AccessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setSource(param1View);
      ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
      if (viewParent instanceof View)
        param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
      int i = SlidingPaneLayout.this.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = SlidingPaneLayout.this.getChildAt(b);
        if (!filter(view) && view.getVisibility() == 0) {
          ViewCompat.setImportantForAccessibility(view, 1);
          param1AccessibilityNodeInfoCompat.addChild(view);
        } 
      } 
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      return !filter(param1View) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : false;
    }
  }
  
  private class DisableLayerRunnable implements Runnable {
    final View mChildView;
    
    DisableLayerRunnable(View param1View) {
      this.mChildView = param1View;
    }
    
    public void run() {
      if (this.mChildView.getParent() == SlidingPaneLayout.this) {
        this.mChildView.setLayerType(0, null);
        SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
      } 
      SlidingPaneLayout.this.mPostedRunnables.remove(this);
    }
  }
  
  private class DragHelperCallback extends ViewDragHelper.Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      SlidingPaneLayout.LayoutParams layoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
        param1Int2 = SlidingPaneLayout.this.getWidth() - SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth();
        int i = SlidingPaneLayout.this.mSlideRange;
        param1Int1 = Math.max(Math.min(param1Int1, param1Int2), param1Int2 - i);
      } else {
        int i = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
        param1Int2 = SlidingPaneLayout.this.mSlideRange;
        param1Int1 = Math.min(Math.max(param1Int1, i), param1Int2 + i);
      } 
      return param1Int1;
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return param1View.getTop();
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return SlidingPaneLayout.this.mSlideRange;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, param1Int2);
    }
    
    public void onViewCaptured(View param1View, int param1Int) {
      SlidingPaneLayout.this.setAllChildrenVisible();
    }
    
    public void onViewDragStateChanged(int param1Int) {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0)
        if (SlidingPaneLayout.this.mSlideOffset == 0.0F) {
          SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
          slidingPaneLayout.updateObscuredViewsVisibility(slidingPaneLayout.mSlideableView);
          slidingPaneLayout = SlidingPaneLayout.this;
          slidingPaneLayout.dispatchOnPanelClosed(slidingPaneLayout.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = false;
        } else {
          SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
          slidingPaneLayout.dispatchOnPanelOpened(slidingPaneLayout.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = true;
        }  
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      SlidingPaneLayout.this.onPanelDragged(param1Int1);
      SlidingPaneLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   4: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
      //   7: astore #4
      //   9: aload_0
      //   10: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   13: invokevirtual isLayoutRtlSupport : ()Z
      //   16: ifeq -> 109
      //   19: aload_0
      //   20: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   23: invokevirtual getPaddingRight : ()I
      //   26: aload #4
      //   28: getfield rightMargin : I
      //   31: iadd
      //   32: istore #5
      //   34: fload_2
      //   35: fconst_0
      //   36: fcmpg
      //   37: iflt -> 67
      //   40: iload #5
      //   42: istore #6
      //   44: fload_2
      //   45: fconst_0
      //   46: fcmpl
      //   47: ifne -> 79
      //   50: iload #5
      //   52: istore #6
      //   54: aload_0
      //   55: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   58: getfield mSlideOffset : F
      //   61: ldc 0.5
      //   63: fcmpl
      //   64: ifle -> 79
      //   67: iload #5
      //   69: aload_0
      //   70: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   73: getfield mSlideRange : I
      //   76: iadd
      //   77: istore #6
      //   79: aload_0
      //   80: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   83: getfield mSlideableView : Landroid/view/View;
      //   86: invokevirtual getWidth : ()I
      //   89: istore #5
      //   91: aload_0
      //   92: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   95: invokevirtual getWidth : ()I
      //   98: iload #6
      //   100: isub
      //   101: iload #5
      //   103: isub
      //   104: istore #6
      //   106: goto -> 167
      //   109: aload_0
      //   110: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   113: invokevirtual getPaddingLeft : ()I
      //   116: aload #4
      //   118: getfield leftMargin : I
      //   121: iadd
      //   122: istore #6
      //   124: fload_2
      //   125: fconst_0
      //   126: fcmpl
      //   127: ifgt -> 155
      //   130: fload_2
      //   131: fconst_0
      //   132: fcmpl
      //   133: ifne -> 152
      //   136: aload_0
      //   137: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   140: getfield mSlideOffset : F
      //   143: ldc 0.5
      //   145: fcmpl
      //   146: ifle -> 152
      //   149: goto -> 155
      //   152: goto -> 167
      //   155: iload #6
      //   157: aload_0
      //   158: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   161: getfield mSlideRange : I
      //   164: iadd
      //   165: istore #6
      //   167: aload_0
      //   168: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   171: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
      //   174: iload #6
      //   176: aload_1
      //   177: invokevirtual getTop : ()I
      //   180: invokevirtual settleCapturedViewAt : (II)Z
      //   183: pop
      //   184: aload_0
      //   185: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   188: invokevirtual invalidate : ()V
      //   191: return
    }
    
    public boolean tryCaptureView(View param1View, int param1Int) {
      return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)param1View.getLayoutParams()).slideable;
    }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int[] ATTRS = new int[] { 16843137 };
    
    Paint dimPaint;
    
    boolean dimWhenOffset;
    
    boolean slideable;
    
    public float weight = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ATTRS);
      this.weight = typedArray.getFloat(0, 0.0F);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  public static interface PanelSlideListener {
    void onPanelClosed(View param1View);
    
    void onPanelOpened(View param1View);
    
    void onPanelSlide(View param1View, float param1Float);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new SlidingPaneLayout.SavedState(param2Parcel, null);
        }
        
        public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new SlidingPaneLayout.SavedState(param2Parcel, null);
        }
        
        public SlidingPaneLayout.SavedState[] newArray(int param2Int) {
          return new SlidingPaneLayout.SavedState[param2Int];
        }
      };
    
    boolean isOpen;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOpen = bool;
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.isOpen);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new SlidingPaneLayout.SavedState(param1Parcel, null);
    }
    
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new SlidingPaneLayout.SavedState(param1Parcel, null);
    }
    
    public SlidingPaneLayout.SavedState[] newArray(int param1Int) {
      return new SlidingPaneLayout.SavedState[param1Int];
    }
  }
  
  public static class SimplePanelSlideListener implements PanelSlideListener {
    public void onPanelClosed(View param1View) {}
    
    public void onPanelOpened(View param1View) {}
    
    public void onPanelSlide(View param1View, float param1Float) {}
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/widget/SlidingPaneLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */