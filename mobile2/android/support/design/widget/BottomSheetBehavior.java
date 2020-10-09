package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.R;
import android.support.v4.math.MathUtils;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final float HIDE_FRICTION = 0.1F;
  
  private static final float HIDE_THRESHOLD = 0.5F;
  
  public static final int PEEK_HEIGHT_AUTO = -1;
  
  public static final int STATE_COLLAPSED = 4;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_EXPANDED = 3;
  
  public static final int STATE_HALF_EXPANDED = 6;
  
  public static final int STATE_HIDDEN = 5;
  
  public static final int STATE_SETTLING = 2;
  
  int activePointerId;
  
  private BottomSheetCallback callback;
  
  int collapsedOffset;
  
  private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        return param1View.getLeft();
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        int i = BottomSheetBehavior.this.getExpandedOffset();
        if (BottomSheetBehavior.this.hideable) {
          param1Int2 = BottomSheetBehavior.this.parentHeight;
        } else {
          param1Int2 = BottomSheetBehavior.this.collapsedOffset;
        } 
        return MathUtils.clamp(param1Int1, i, param1Int2);
      }
      
      public int getViewVerticalDragRange(View param1View) {
        return BottomSheetBehavior.this.hideable ? BottomSheetBehavior.this.parentHeight : BottomSheetBehavior.this.collapsedOffset;
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (param1Int == 1)
          BottomSheetBehavior.this.setStateInternal(1); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        BottomSheetBehavior.this.dispatchOnSlide(param1Int2);
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        int i;
        byte b;
        if (param1Float2 < 0.0F) {
          if (BottomSheetBehavior.this.fitToContents) {
            i = BottomSheetBehavior.this.fitToContentsOffset;
            b = 3;
          } else if (param1View.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
            i = BottomSheetBehavior.this.halfExpandedOffset;
            b = 6;
          } else {
            i = 0;
            b = 3;
          } 
        } else if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(param1View, param1Float2) && (param1View.getTop() > BottomSheetBehavior.this.collapsedOffset || Math.abs(param1Float1) < Math.abs(param1Float2))) {
          i = BottomSheetBehavior.this.parentHeight;
          b = 5;
        } else if (param1Float2 == 0.0F || Math.abs(param1Float1) > Math.abs(param1Float2)) {
          i = param1View.getTop();
          if (BottomSheetBehavior.this.fitToContents) {
            if (Math.abs(i - BottomSheetBehavior.this.fitToContentsOffset) < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
              i = BottomSheetBehavior.this.fitToContentsOffset;
              b = 3;
            } else {
              i = BottomSheetBehavior.this.collapsedOffset;
              b = 4;
            } 
          } else if (i < BottomSheetBehavior.this.halfExpandedOffset) {
            if (i < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
              i = 0;
              b = 3;
            } else {
              i = BottomSheetBehavior.this.halfExpandedOffset;
              b = 6;
            } 
          } else if (Math.abs(i - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(i - BottomSheetBehavior.this.collapsedOffset)) {
            i = BottomSheetBehavior.this.halfExpandedOffset;
            b = 6;
          } else {
            i = BottomSheetBehavior.this.collapsedOffset;
            b = 4;
          } 
        } else {
          i = BottomSheetBehavior.this.collapsedOffset;
          b = 4;
        } 
        if (BottomSheetBehavior.this.viewDragHelper.settleCapturedViewAt(param1View.getLeft(), i)) {
          BottomSheetBehavior.this.setStateInternal(2);
          ViewCompat.postOnAnimation(param1View, new BottomSheetBehavior.SettleRunnable(param1View, b));
        } else {
          BottomSheetBehavior.this.setStateInternal(b);
        } 
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        int i = BottomSheetBehavior.this.state;
        boolean bool = true;
        if (i == 1)
          return false; 
        if (BottomSheetBehavior.this.touchingScrollingChild)
          return false; 
        if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == param1Int) {
          View view = BottomSheetBehavior.this.nestedScrollingChildRef.get();
          if (view != null && view.canScrollVertically(-1))
            return false; 
        } 
        if (BottomSheetBehavior.this.viewRef == null || BottomSheetBehavior.this.viewRef.get() != param1View)
          bool = false; 
        return bool;
      }
    };
  
  private boolean fitToContents = true;
  
  int fitToContentsOffset;
  
  int halfExpandedOffset;
  
  boolean hideable;
  
  private boolean ignoreEvents;
  
  private Map<View, Integer> importantForAccessibilityMap;
  
  private int initialY;
  
  private int lastNestedScrollDy;
  
  private int lastPeekHeight;
  
  private float maximumVelocity;
  
  private boolean nestedScrolled;
  
  WeakReference<View> nestedScrollingChildRef;
  
  int parentHeight;
  
  private int peekHeight;
  
  private boolean peekHeightAuto;
  
  private int peekHeightMin;
  
  private boolean skipCollapsed;
  
  int state = 4;
  
  boolean touchingScrollingChild;
  
  private VelocityTracker velocityTracker;
  
  ViewDragHelper viewDragHelper;
  
  WeakReference<V> viewRef;
  
  public BottomSheetBehavior() {}
  
  public BottomSheetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.BottomSheetBehavior_Layout);
    TypedValue typedValue = typedArray.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
    if (typedValue != null && typedValue.data == -1) {
      setPeekHeight(typedValue.data);
    } else {
      setPeekHeight(typedArray.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
    } 
    setHideable(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
    setFitToContents(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
    setSkipCollapsed(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
    typedArray.recycle();
    this.maximumVelocity = ViewConfiguration.get(paramContext).getScaledMaximumFlingVelocity();
  }
  
  private void calculateCollapsedOffset() {
    if (this.fitToContents) {
      this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
    } else {
      this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
    } 
  }
  
  public static <V extends View> BottomSheetBehavior<V> from(V paramV) {
    ViewGroup.LayoutParams layoutParams = paramV.getLayoutParams();
    if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior();
      if (behavior instanceof BottomSheetBehavior)
        return (BottomSheetBehavior<V>)behavior; 
      throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    } 
    throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
  }
  
  private int getExpandedOffset() {
    boolean bool;
    if (this.fitToContents) {
      bool = this.fitToContentsOffset;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private float getYVelocity() {
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker == null)
      return 0.0F; 
    velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
    return this.velocityTracker.getYVelocity(this.activePointerId);
  }
  
  private void reset() {
    this.activePointerId = -1;
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.velocityTracker = null;
    } 
  }
  
  private void updateImportantForAccessibility(boolean paramBoolean) {
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null)
      return; 
    ViewParent viewParent = ((View)weakReference.get()).getParent();
    if (!(viewParent instanceof CoordinatorLayout))
      return; 
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)viewParent;
    int i = coordinatorLayout.getChildCount();
    if (Build.VERSION.SDK_INT >= 16 && paramBoolean)
      if (this.importantForAccessibilityMap == null) {
        this.importantForAccessibilityMap = new HashMap<>(i);
      } else {
        return;
      }  
    for (byte b = 0; b < i; b++) {
      View view = coordinatorLayout.getChildAt(b);
      if (view != this.viewRef.get())
        if (!paramBoolean) {
          Map<View, Integer> map = this.importantForAccessibilityMap;
          if (map != null && map.containsKey(view))
            ViewCompat.setImportantForAccessibility(view, ((Integer)this.importantForAccessibilityMap.get(view)).intValue()); 
        } else {
          if (Build.VERSION.SDK_INT >= 16)
            this.importantForAccessibilityMap.put(view, Integer.valueOf(view.getImportantForAccessibility())); 
          ViewCompat.setImportantForAccessibility(view, 4);
        }  
    } 
    if (!paramBoolean)
      this.importantForAccessibilityMap = null; 
  }
  
  void dispatchOnSlide(int paramInt) {
    View view = (View)this.viewRef.get();
    if (view != null) {
      BottomSheetCallback bottomSheetCallback = this.callback;
      if (bottomSheetCallback != null) {
        int i = this.collapsedOffset;
        if (paramInt > i) {
          bottomSheetCallback.onSlide(view, (i - paramInt) / (this.parentHeight - i));
        } else {
          bottomSheetCallback.onSlide(view, (i - paramInt) / (i - getExpandedOffset()));
        } 
      } 
    } 
  }
  
  View findScrollingChild(View paramView) {
    if (ViewCompat.isNestedScrollingEnabled(paramView))
      return paramView; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      byte b = 0;
      int i = viewGroup.getChildCount();
      while (b < i) {
        View view = findScrollingChild(viewGroup.getChildAt(b));
        if (view != null)
          return view; 
        b++;
      } 
    } 
    return null;
  }
  
  public final int getPeekHeight() {
    int i;
    if (this.peekHeightAuto) {
      i = -1;
    } else {
      i = this.peekHeight;
    } 
    return i;
  }
  
  int getPeekHeightMin() {
    return this.peekHeightMin;
  }
  
  public boolean getSkipCollapsed() {
    return this.skipCollapsed;
  }
  
  public final int getState() {
    return this.state;
  }
  
  public boolean isFitToContents() {
    return this.fitToContents;
  }
  
  public boolean isHideable() {
    return this.hideable;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    View view;
    boolean bool = paramV.isShown();
    boolean bool1 = false;
    if (!bool) {
      this.ignoreEvents = true;
      return false;
    } 
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      reset(); 
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
    this.velocityTracker.addMovement(paramMotionEvent);
    V v = null;
    if (i != 0) {
      if (i == 1 || i == 3) {
        this.touchingScrollingChild = false;
        this.activePointerId = -1;
        if (this.ignoreEvents) {
          this.ignoreEvents = false;
          return false;
        } 
      } 
    } else {
      int j = (int)paramMotionEvent.getX();
      this.initialY = (int)paramMotionEvent.getY();
      WeakReference<View> weakReference1 = this.nestedScrollingChildRef;
      if (weakReference1 != null) {
        View view1 = weakReference1.get();
      } else {
        weakReference1 = null;
      } 
      if (weakReference1 != null && paramCoordinatorLayout.isPointInChildBounds((View)weakReference1, j, this.initialY)) {
        this.activePointerId = paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
        this.touchingScrollingChild = true;
      } 
      if (this.activePointerId == -1 && !paramCoordinatorLayout.isPointInChildBounds((View)paramV, j, this.initialY)) {
        bool = true;
      } else {
        bool = false;
      } 
      this.ignoreEvents = bool;
    } 
    if (!this.ignoreEvents) {
      ViewDragHelper viewDragHelper = this.viewDragHelper;
      if (viewDragHelper != null && viewDragHelper.shouldInterceptTouchEvent(paramMotionEvent))
        return true; 
    } 
    WeakReference<View> weakReference = this.nestedScrollingChildRef;
    paramV = v;
    if (weakReference != null)
      view = weakReference.get(); 
    if (i == 2 && view != null && !this.ignoreEvents && this.state != 1 && !paramCoordinatorLayout.isPointInChildBounds(view, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()) && this.viewDragHelper != null && Math.abs(this.initialY - paramMotionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
      bool = true;
    } else {
      bool = bool1;
    } 
    return bool;
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    if (ViewCompat.getFitsSystemWindows((View)paramCoordinatorLayout) && !ViewCompat.getFitsSystemWindows((View)paramV))
      paramV.setFitsSystemWindows(true); 
    int i = paramV.getTop();
    paramCoordinatorLayout.onLayoutChild((View)paramV, paramInt);
    this.parentHeight = paramCoordinatorLayout.getHeight();
    if (this.peekHeightAuto) {
      if (this.peekHeightMin == 0)
        this.peekHeightMin = paramCoordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min); 
      this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - paramCoordinatorLayout.getWidth() * 9 / 16);
    } else {
      this.lastPeekHeight = this.peekHeight;
    } 
    this.fitToContentsOffset = Math.max(0, this.parentHeight - paramV.getHeight());
    this.halfExpandedOffset = this.parentHeight / 2;
    calculateCollapsedOffset();
    paramInt = this.state;
    if (paramInt == 3) {
      ViewCompat.offsetTopAndBottom((View)paramV, getExpandedOffset());
    } else if (paramInt == 6) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.halfExpandedOffset);
    } else if (this.hideable && paramInt == 5) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.parentHeight);
    } else {
      paramInt = this.state;
      if (paramInt == 4) {
        ViewCompat.offsetTopAndBottom((View)paramV, this.collapsedOffset);
      } else if (paramInt == 1 || paramInt == 2) {
        ViewCompat.offsetTopAndBottom((View)paramV, i - paramV.getTop());
      } 
    } 
    if (this.viewDragHelper == null)
      this.viewDragHelper = ViewDragHelper.create(paramCoordinatorLayout, this.dragCallback); 
    this.viewRef = new WeakReference<>(paramV);
    this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild((View)paramV));
    return true;
  }
  
  public boolean onNestedPreFling(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, float paramFloat1, float paramFloat2) {
    boolean bool;
    if (paramView == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(paramCoordinatorLayout, paramV, paramView, paramFloat1, paramFloat2))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onNestedPreScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    if (paramInt3 == 1)
      return; 
    if (paramView != (View)this.nestedScrollingChildRef.get())
      return; 
    paramInt1 = paramV.getTop();
    int i = paramInt1 - paramInt2;
    if (paramInt2 > 0) {
      if (i < getExpandedOffset()) {
        paramArrayOfint[1] = paramInt1 - getExpandedOffset();
        ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
        setStateInternal(3);
      } else {
        paramArrayOfint[1] = paramInt2;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
        setStateInternal(1);
      } 
    } else if (paramInt2 < 0 && !paramView.canScrollVertically(-1)) {
      paramInt3 = this.collapsedOffset;
      if (i <= paramInt3 || this.hideable) {
        paramArrayOfint[1] = paramInt2;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
        setStateInternal(1);
      } else {
        paramArrayOfint[1] = paramInt1 - paramInt3;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
        setStateInternal(4);
      } 
    } 
    dispatchOnSlide(paramV.getTop());
    this.lastNestedScrollDy = paramInt2;
    this.nestedScrolled = true;
  }
  
  public void onRestoreInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV, Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramCoordinatorLayout, paramV, savedState.getSuperState());
    if (savedState.state == 1 || savedState.state == 2) {
      this.state = 4;
      return;
    } 
    this.state = savedState.state;
  }
  
  public Parcelable onSaveInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV) {
    return (Parcelable)new SavedState(super.onSaveInstanceState(paramCoordinatorLayout, paramV), this.state);
  }
  
  public boolean onStartNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    boolean bool = false;
    this.lastNestedScrollDy = 0;
    this.nestedScrolled = false;
    if ((paramInt1 & 0x2) != 0)
      bool = true; 
    return bool;
  }
  
  public void onStopNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt) {
    byte b;
    if (paramV.getTop() == getExpandedOffset()) {
      setStateInternal(3);
      return;
    } 
    if (paramView != this.nestedScrollingChildRef.get() || !this.nestedScrolled)
      return; 
    if (this.lastNestedScrollDy > 0) {
      paramInt = getExpandedOffset();
      b = 3;
    } else if (this.hideable && shouldHide((View)paramV, getYVelocity())) {
      paramInt = this.parentHeight;
      b = 5;
    } else if (this.lastNestedScrollDy == 0) {
      paramInt = paramV.getTop();
      if (this.fitToContents) {
        if (Math.abs(paramInt - this.fitToContentsOffset) < Math.abs(paramInt - this.collapsedOffset)) {
          paramInt = this.fitToContentsOffset;
          b = 3;
        } else {
          paramInt = this.collapsedOffset;
          b = 4;
        } 
      } else {
        b = this.halfExpandedOffset;
        if (paramInt < b) {
          if (paramInt < Math.abs(paramInt - this.collapsedOffset)) {
            paramInt = 0;
            b = 3;
          } else {
            paramInt = this.halfExpandedOffset;
            b = 6;
          } 
        } else if (Math.abs(paramInt - b) < Math.abs(paramInt - this.collapsedOffset)) {
          paramInt = this.halfExpandedOffset;
          b = 6;
        } else {
          paramInt = this.collapsedOffset;
          b = 4;
        } 
      } 
    } else {
      paramInt = this.collapsedOffset;
      b = 4;
    } 
    if (this.viewDragHelper.smoothSlideViewTo((View)paramV, paramV.getLeft(), paramInt)) {
      setStateInternal(2);
      ViewCompat.postOnAnimation((View)paramV, new SettleRunnable((View)paramV, b));
    } else {
      setStateInternal(b);
    } 
    this.nestedScrolled = false;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (!paramV.isShown())
      return false; 
    int i = paramMotionEvent.getActionMasked();
    if (this.state == 1 && i == 0)
      return true; 
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null)
      viewDragHelper.processTouchEvent(paramMotionEvent); 
    if (i == 0)
      reset(); 
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
    this.velocityTracker.addMovement(paramMotionEvent);
    if (i == 2 && !this.ignoreEvents && Math.abs(this.initialY - paramMotionEvent.getY()) > this.viewDragHelper.getTouchSlop())
      this.viewDragHelper.captureChildView((View)paramV, paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex())); 
    return this.ignoreEvents ^ true;
  }
  
  public void setBottomSheetCallback(BottomSheetCallback paramBottomSheetCallback) {
    this.callback = paramBottomSheetCallback;
  }
  
  public void setFitToContents(boolean paramBoolean) {
    int i;
    if (this.fitToContents == paramBoolean)
      return; 
    this.fitToContents = paramBoolean;
    if (this.viewRef != null)
      calculateCollapsedOffset(); 
    if (this.fitToContents && this.state == 6) {
      i = 3;
    } else {
      i = this.state;
    } 
    setStateInternal(i);
  }
  
  public void setHideable(boolean paramBoolean) {
    this.hideable = paramBoolean;
  }
  
  public final void setPeekHeight(int paramInt) {
    boolean bool = false;
    if (paramInt == -1) {
      if (!this.peekHeightAuto) {
        this.peekHeightAuto = true;
        bool = true;
      } 
    } else if (this.peekHeightAuto || this.peekHeight != paramInt) {
      this.peekHeightAuto = false;
      this.peekHeight = Math.max(0, paramInt);
      this.collapsedOffset = this.parentHeight - paramInt;
      bool = true;
    } 
    if (bool && this.state == 4) {
      WeakReference<V> weakReference = this.viewRef;
      if (weakReference != null) {
        View view = (View)weakReference.get();
        if (view != null)
          view.requestLayout(); 
      } 
    } 
  }
  
  public void setSkipCollapsed(boolean paramBoolean) {
    this.skipCollapsed = paramBoolean;
  }
  
  public final void setState(final int finalState) {
    if (finalState == this.state)
      return; 
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null) {
      if (finalState == 4 || finalState == 3 || finalState == 6 || (this.hideable && finalState == 5))
        this.state = finalState; 
      return;
    } 
    final View child = (View)weakReference.get();
    if (view == null)
      return; 
    ViewParent viewParent = view.getParent();
    if (viewParent != null && viewParent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
      view.post(new Runnable() {
            public void run() {
              BottomSheetBehavior.this.startSettlingAnimation(child, finalState);
            }
          });
    } else {
      startSettlingAnimation(view, finalState);
    } 
  }
  
  void setStateInternal(int paramInt) {
    if (this.state == paramInt)
      return; 
    this.state = paramInt;
    if (paramInt == 6 || paramInt == 3) {
      updateImportantForAccessibility(true);
    } else if (paramInt == 5 || paramInt == 4) {
      updateImportantForAccessibility(false);
    } 
    View view = (View)this.viewRef.get();
    if (view != null) {
      BottomSheetCallback bottomSheetCallback = this.callback;
      if (bottomSheetCallback != null)
        bottomSheetCallback.onStateChanged(view, paramInt); 
    } 
  }
  
  boolean shouldHide(View paramView, float paramFloat) {
    boolean bool = this.skipCollapsed;
    boolean bool1 = true;
    if (bool)
      return true; 
    if (paramView.getTop() < this.collapsedOffset)
      return false; 
    if (Math.abs(paramView.getTop() + 0.1F * paramFloat - this.collapsedOffset) / this.peekHeight <= 0.5F)
      bool1 = false; 
    return bool1;
  }
  
  void startSettlingAnimation(View paramView, int paramInt) {
    StringBuilder stringBuilder;
    int i;
    int j;
    if (paramInt == 4) {
      i = this.collapsedOffset;
      j = paramInt;
    } else if (paramInt == 6) {
      int k = this.halfExpandedOffset;
      i = k;
      j = paramInt;
      if (this.fitToContents) {
        i = k;
        j = paramInt;
        if (k <= this.fitToContentsOffset) {
          j = 3;
          i = this.fitToContentsOffset;
        } 
      } 
    } else if (paramInt == 3) {
      i = getExpandedOffset();
      j = paramInt;
    } else if (this.hideable && paramInt == 5) {
      i = this.parentHeight;
      j = paramInt;
    } else {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Illegal state argument: ");
      stringBuilder.append(paramInt);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    if (this.viewDragHelper.smoothSlideViewTo((View)stringBuilder, stringBuilder.getLeft(), i)) {
      setStateInternal(2);
      ViewCompat.postOnAnimation((View)stringBuilder, new SettleRunnable((View)stringBuilder, j));
    } else {
      setStateInternal(j);
    } 
  }
  
  public static abstract class BottomSheetCallback {
    public abstract void onSlide(View param1View, float param1Float);
    
    public abstract void onStateChanged(View param1View, int param1Int);
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomSheetBehavior.SavedState(param2Parcel, null);
        }
        
        public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new BottomSheetBehavior.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public BottomSheetBehavior.SavedState[] newArray(int param2Int) {
          return new BottomSheetBehavior.SavedState[param2Int];
        }
      };
    
    final int state;
    
    public SavedState(Parcel param1Parcel) {
      this(param1Parcel, (ClassLoader)null);
    }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.state = param1Parcel.readInt();
    }
    
    public SavedState(Parcelable param1Parcelable, int param1Int) {
      super(param1Parcelable);
      this.state = param1Int;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.state);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomSheetBehavior.SavedState(param1Parcel, null);
    }
    
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomSheetBehavior.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomSheetBehavior.SavedState[] newArray(int param1Int) {
      return new BottomSheetBehavior.SavedState[param1Int];
    }
  }
  
  private class SettleRunnable implements Runnable {
    private final int targetState;
    
    private final View view;
    
    SettleRunnable(View param1View, int param1Int) {
      this.view = param1View;
      this.targetState = param1Int;
    }
    
    public void run() {
      if (BottomSheetBehavior.this.viewDragHelper != null && BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.view, this);
      } else {
        BottomSheetBehavior.this.setStateInternal(this.targetState);
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/BottomSheetBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */