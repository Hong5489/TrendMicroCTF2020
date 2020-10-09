package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import java.util.Arrays;

public class ViewDragHelper {
  private static final int BASE_SETTLE_DURATION = 256;
  
  public static final int DIRECTION_ALL = 3;
  
  public static final int DIRECTION_HORIZONTAL = 1;
  
  public static final int DIRECTION_VERTICAL = 2;
  
  public static final int EDGE_ALL = 15;
  
  public static final int EDGE_BOTTOM = 8;
  
  public static final int EDGE_LEFT = 1;
  
  public static final int EDGE_RIGHT = 2;
  
  private static final int EDGE_SIZE = 20;
  
  public static final int EDGE_TOP = 4;
  
  public static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "ViewDragHelper";
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  private int mActivePointerId = -1;
  
  private final Callback mCallback;
  
  private View mCapturedView;
  
  private int mDragState;
  
  private int[] mEdgeDragsInProgress;
  
  private int[] mEdgeDragsLocked;
  
  private int mEdgeSize;
  
  private int[] mInitialEdgesTouched;
  
  private float[] mInitialMotionX;
  
  private float[] mInitialMotionY;
  
  private float[] mLastMotionX;
  
  private float[] mLastMotionY;
  
  private float mMaxVelocity;
  
  private float mMinVelocity;
  
  private final ViewGroup mParentView;
  
  private int mPointersDown;
  
  private boolean mReleaseInProgress;
  
  private OverScroller mScroller;
  
  private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() {
        ViewDragHelper.this.setDragState(0);
      }
    };
  
  private int mTouchSlop;
  
  private int mTrackingEdges;
  
  private VelocityTracker mVelocityTracker;
  
  private ViewDragHelper(Context paramContext, ViewGroup paramViewGroup, Callback paramCallback) {
    if (paramViewGroup != null) {
      if (paramCallback != null) {
        this.mParentView = paramViewGroup;
        this.mCallback = paramCallback;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
        this.mEdgeSize = (int)(20.0F * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(paramContext, sInterpolator);
        return;
      } 
      throw new IllegalArgumentException("Callback may not be null");
    } 
    throw new IllegalArgumentException("Parent view may not be null");
  }
  
  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    int i = this.mInitialEdgesTouched[paramInt1];
    boolean bool = false;
    if ((i & paramInt2) == paramInt2 && (this.mTrackingEdges & paramInt2) != 0 && (this.mEdgeDragsLocked[paramInt1] & paramInt2) != paramInt2 && (this.mEdgeDragsInProgress[paramInt1] & paramInt2) != paramInt2) {
      i = this.mTouchSlop;
      if (paramFloat1 > i || paramFloat2 > i) {
        if (paramFloat1 < 0.5F * paramFloat2 && this.mCallback.onEdgeLock(paramInt2)) {
          int[] arrayOfInt = this.mEdgeDragsLocked;
          arrayOfInt[paramInt1] = arrayOfInt[paramInt1] | paramInt2;
          return false;
        } 
        boolean bool1 = bool;
        if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0) {
          bool1 = bool;
          if (paramFloat1 > this.mTouchSlop)
            bool1 = true; 
        } 
        return bool1;
      } 
    } 
    return false;
  }
  
  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2) {
    int i;
    boolean bool4;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (paramView == null)
      return false; 
    if (this.mCallback.getViewHorizontalDragRange(paramView) > 0) {
      i = 1;
    } else {
      i = 0;
    } 
    if (this.mCallback.getViewVerticalDragRange(paramView) > 0) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if (i && bool4) {
      i = this.mTouchSlop;
      if (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 > (i * i))
        bool3 = true; 
      return bool3;
    } 
    if (i != 0) {
      bool3 = bool1;
      if (Math.abs(paramFloat1) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    if (bool4) {
      bool3 = bool2;
      if (Math.abs(paramFloat2) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    return false;
  }
  
  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = Math.abs(paramFloat1);
    if (f < paramFloat2)
      return 0.0F; 
    if (f > paramFloat3) {
      if (paramFloat1 > 0.0F) {
        paramFloat1 = paramFloat3;
      } else {
        paramFloat1 = -paramFloat3;
      } 
      return paramFloat1;
    } 
    return paramFloat1;
  }
  
  private int clampMag(int paramInt1, int paramInt2, int paramInt3) {
    int i = Math.abs(paramInt1);
    if (i < paramInt2)
      return 0; 
    if (i > paramInt3) {
      if (paramInt1 <= 0)
        paramInt3 = -paramInt3; 
      return paramInt3;
    } 
    return paramInt1;
  }
  
  private void clearMotionHistory() {
    float[] arrayOfFloat = this.mInitialMotionX;
    if (arrayOfFloat == null)
      return; 
    Arrays.fill(arrayOfFloat, 0.0F);
    Arrays.fill(this.mInitialMotionY, 0.0F);
    Arrays.fill(this.mLastMotionX, 0.0F);
    Arrays.fill(this.mLastMotionY, 0.0F);
    Arrays.fill(this.mInitialEdgesTouched, 0);
    Arrays.fill(this.mEdgeDragsInProgress, 0);
    Arrays.fill(this.mEdgeDragsLocked, 0);
    this.mPointersDown = 0;
  }
  
  private void clearMotionHistory(int paramInt) {
    if (this.mInitialMotionX == null || !isPointerDown(paramInt))
      return; 
    this.mInitialMotionX[paramInt] = 0.0F;
    this.mInitialMotionY[paramInt] = 0.0F;
    this.mLastMotionX[paramInt] = 0.0F;
    this.mLastMotionY[paramInt] = 0.0F;
    this.mInitialEdgesTouched[paramInt] = 0;
    this.mEdgeDragsInProgress[paramInt] = 0;
    this.mEdgeDragsLocked[paramInt] = 0;
    this.mPointersDown &= 1 << paramInt;
  }
  
  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 0)
      return 0; 
    int i = this.mParentView.getWidth();
    int j = i / 2;
    float f1 = Math.min(1.0F, Math.abs(paramInt1) / i);
    float f2 = j;
    float f3 = j;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0) {
      paramInt1 = Math.round(Math.abs((f2 + f3 * f1) / paramInt2) * 1000.0F) * 4;
    } else {
      paramInt1 = (int)((1.0F + Math.abs(paramInt1) / paramInt3) * 256.0F);
    } 
    return Math.min(paramInt1, 600);
  }
  
  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f2;
    paramInt3 = clampMag(paramInt3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    paramInt4 = clampMag(paramInt4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    int i = Math.abs(paramInt1);
    int j = Math.abs(paramInt2);
    int k = Math.abs(paramInt3);
    int m = Math.abs(paramInt4);
    int n = k + m;
    int i1 = i + j;
    if (paramInt3 != 0) {
      f1 = k;
      f2 = n;
    } else {
      f1 = i;
      f2 = i1;
    } 
    float f3 = f1 / f2;
    if (paramInt4 != 0) {
      f2 = m;
      f1 = n;
    } else {
      f2 = j;
      f1 = i1;
    } 
    float f1 = f2 / f1;
    paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
    paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
    return (int)(paramInt1 * f3 + paramInt2 * f1);
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, float paramFloat, Callback paramCallback) {
    ViewDragHelper viewDragHelper = create(paramViewGroup, paramCallback);
    viewDragHelper.mTouchSlop = (int)(viewDragHelper.mTouchSlop * 1.0F / paramFloat);
    return viewDragHelper;
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, Callback paramCallback) {
    return new ViewDragHelper(paramViewGroup.getContext(), paramViewGroup, paramCallback);
  }
  
  private void dispatchViewReleased(float paramFloat1, float paramFloat2) {
    this.mReleaseInProgress = true;
    this.mCallback.onViewReleased(this.mCapturedView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState == 1)
      setDragState(0); 
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F));
  }
  
  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt1;
    int j = paramInt2;
    int k = this.mCapturedView.getLeft();
    int m = this.mCapturedView.getTop();
    if (paramInt3 != 0) {
      paramInt1 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, paramInt1, paramInt3);
      ViewCompat.offsetLeftAndRight(this.mCapturedView, paramInt1 - k);
    } else {
      paramInt1 = i;
    } 
    if (paramInt4 != 0) {
      j = this.mCallback.clampViewPositionVertical(this.mCapturedView, paramInt2, paramInt4);
      ViewCompat.offsetTopAndBottom(this.mCapturedView, j - m);
    } 
    if (paramInt3 != 0 || paramInt4 != 0)
      this.mCallback.onViewPositionChanged(this.mCapturedView, paramInt1, j, paramInt1 - k, j - m); 
  }
  
  private void ensureMotionHistorySizeForId(int paramInt) {
    float[] arrayOfFloat = this.mInitialMotionX;
    if (arrayOfFloat == null || arrayOfFloat.length <= paramInt) {
      float[] arrayOfFloat1 = new float[paramInt + 1];
      float[] arrayOfFloat2 = new float[paramInt + 1];
      arrayOfFloat = new float[paramInt + 1];
      float[] arrayOfFloat3 = new float[paramInt + 1];
      int[] arrayOfInt1 = new int[paramInt + 1];
      int[] arrayOfInt2 = new int[paramInt + 1];
      int[] arrayOfInt3 = new int[paramInt + 1];
      float[] arrayOfFloat4 = this.mInitialMotionX;
      if (arrayOfFloat4 != null) {
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat1, 0, arrayOfFloat4.length);
        arrayOfFloat4 = this.mInitialMotionY;
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat2, 0, arrayOfFloat4.length);
        arrayOfFloat4 = this.mLastMotionX;
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat, 0, arrayOfFloat4.length);
        arrayOfFloat4 = this.mLastMotionY;
        System.arraycopy(arrayOfFloat4, 0, arrayOfFloat3, 0, arrayOfFloat4.length);
        int[] arrayOfInt = this.mInitialEdgesTouched;
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
        arrayOfInt = this.mEdgeDragsInProgress;
        System.arraycopy(arrayOfInt, 0, arrayOfInt2, 0, arrayOfInt.length);
        arrayOfInt = this.mEdgeDragsLocked;
        System.arraycopy(arrayOfInt, 0, arrayOfInt3, 0, arrayOfInt.length);
      } 
      this.mInitialMotionX = arrayOfFloat1;
      this.mInitialMotionY = arrayOfFloat2;
      this.mLastMotionX = arrayOfFloat;
      this.mLastMotionY = arrayOfFloat3;
      this.mInitialEdgesTouched = arrayOfInt1;
      this.mEdgeDragsInProgress = arrayOfInt2;
      this.mEdgeDragsLocked = arrayOfInt3;
    } 
  }
  
  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    paramInt1 -= i;
    paramInt2 -= j;
    if (paramInt1 == 0 && paramInt2 == 0) {
      this.mScroller.abortAnimation();
      setDragState(0);
      return false;
    } 
    paramInt3 = computeSettleDuration(this.mCapturedView, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mScroller.startScroll(i, j, paramInt1, paramInt2, paramInt3);
    setDragState(2);
    return true;
  }
  
  private int getEdgesTouched(int paramInt1, int paramInt2) {
    int i = 0;
    if (paramInt1 < this.mParentView.getLeft() + this.mEdgeSize)
      i = false | true; 
    int j = i;
    if (paramInt2 < this.mParentView.getTop() + this.mEdgeSize)
      j = i | 0x4; 
    i = j;
    if (paramInt1 > this.mParentView.getRight() - this.mEdgeSize)
      i = j | 0x2; 
    paramInt1 = i;
    if (paramInt2 > this.mParentView.getBottom() - this.mEdgeSize)
      paramInt1 = i | 0x8; 
    return paramInt1;
  }
  
  private boolean isValidPointerForActionMove(int paramInt) {
    if (!isPointerDown(paramInt)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Ignoring pointerId=");
      stringBuilder.append(paramInt);
      stringBuilder.append(" because ACTION_DOWN was not received ");
      stringBuilder.append("for this pointer before ACTION_MOVE. It likely happened because ");
      stringBuilder.append(" ViewDragHelper did not receive all the events in the event stream.");
      Log.e("ViewDragHelper", stringBuilder.toString());
      return false;
    } 
    return true;
  }
  
  private void releaseViewForPointerUp() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
    dispatchViewReleased(clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
  }
  
  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt) {
    int i = 0;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      i = false | true; 
    int j = i;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      j = i | 0x4; 
    i = j;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      i = j | 0x2; 
    j = i;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      j = i | 0x8; 
    if (j != 0) {
      int[] arrayOfInt = this.mEdgeDragsInProgress;
      arrayOfInt[paramInt] = arrayOfInt[paramInt] | j;
      this.mCallback.onEdgeDragStarted(j, paramInt);
    } 
  }
  
  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt) {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat[paramInt] = paramFloat1;
    arrayOfFloat = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat[paramInt] = paramFloat2;
    this.mInitialEdgesTouched[paramInt] = getEdgesTouched((int)paramFloat1, (int)paramFloat2);
    this.mPointersDown |= 1 << paramInt;
  }
  
  private void saveLastMotion(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getPointerCount();
    for (byte b = 0; b < i; b++) {
      int j = paramMotionEvent.getPointerId(b);
      if (isValidPointerForActionMove(j)) {
        float f1 = paramMotionEvent.getX(b);
        float f2 = paramMotionEvent.getY(b);
        this.mLastMotionX[j] = f1;
        this.mLastMotionY[j] = f2;
      } 
    } 
  }
  
  public void abort() {
    cancel();
    if (this.mDragState == 2) {
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, m, k - i, m - j);
    } 
    setDragState(0);
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool = paramView instanceof ViewGroup;
    boolean bool1 = true;
    if (bool) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = viewGroup.getChildCount() - 1; k >= 0; k--) {
        View view = viewGroup.getChildAt(k);
        if (paramInt3 + i >= view.getLeft() && paramInt3 + i < view.getRight() && paramInt4 + j >= view.getTop() && paramInt4 + j < view.getBottom() && canScroll(view, true, paramInt1, paramInt2, paramInt3 + i - view.getLeft(), paramInt4 + j - view.getTop()))
          return true; 
      } 
    } 
    if (paramBoolean)
      if (!paramView.canScrollHorizontally(-paramInt1)) {
        if (paramView.canScrollVertically(-paramInt2))
          return bool1; 
      } else {
        return bool1;
      }  
    return false;
  }
  
  public void cancel() {
    this.mActivePointerId = -1;
    clearMotionHistory();
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  public void captureChildView(View paramView, int paramInt) {
    if (paramView.getParent() == this.mParentView) {
      this.mCapturedView = paramView;
      this.mActivePointerId = paramInt;
      this.mCallback.onViewCaptured(paramView, paramInt);
      setDragState(1);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
    stringBuilder.append(this.mParentView);
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean checkTouchSlop(int paramInt) {
    int i = this.mInitialMotionX.length;
    for (byte b = 0; b < i; b++) {
      if (checkTouchSlop(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean checkTouchSlop(int paramInt1, int paramInt2) {
    boolean bool4;
    boolean bool = isPointerDown(paramInt2);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (!bool)
      return false; 
    if ((paramInt1 & 0x1) == 1) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if ((paramInt1 & 0x2) == 2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    float f1 = this.mLastMotionX[paramInt2] - this.mInitialMotionX[paramInt2];
    float f2 = this.mLastMotionY[paramInt2] - this.mInitialMotionY[paramInt2];
    if (bool4 && paramInt1 != 0) {
      paramInt1 = this.mTouchSlop;
      if (f1 * f1 + f2 * f2 > (paramInt1 * paramInt1))
        bool3 = true; 
      return bool3;
    } 
    if (bool4) {
      bool3 = bool1;
      if (Math.abs(f1) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    if (paramInt1 != 0) {
      bool3 = bool2;
      if (Math.abs(f2) > this.mTouchSlop)
        bool3 = true; 
      return bool3;
    } 
    return false;
  }
  
  public boolean continueSettling(boolean paramBoolean) {
    int i = this.mDragState;
    boolean bool = false;
    if (i == 2) {
      boolean bool1 = this.mScroller.computeScrollOffset();
      int j = this.mScroller.getCurrX();
      int k = this.mScroller.getCurrY();
      i = j - this.mCapturedView.getLeft();
      int m = k - this.mCapturedView.getTop();
      if (i != 0)
        ViewCompat.offsetLeftAndRight(this.mCapturedView, i); 
      if (m != 0)
        ViewCompat.offsetTopAndBottom(this.mCapturedView, m); 
      if (i != 0 || m != 0)
        this.mCallback.onViewPositionChanged(this.mCapturedView, j, k, i, m); 
      boolean bool2 = bool1;
      if (bool1) {
        bool2 = bool1;
        if (j == this.mScroller.getFinalX()) {
          bool2 = bool1;
          if (k == this.mScroller.getFinalY()) {
            this.mScroller.abortAnimation();
            bool2 = false;
          } 
        } 
      } 
      if (!bool2)
        if (paramBoolean) {
          this.mParentView.post(this.mSetIdleRunnable);
        } else {
          setDragState(0);
        }  
    } 
    paramBoolean = bool;
    if (this.mDragState == 2)
      paramBoolean = true; 
    return paramBoolean;
  }
  
  public View findTopChildUnder(int paramInt1, int paramInt2) {
    for (int i = this.mParentView.getChildCount() - 1; i >= 0; i--) {
      View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
      if (paramInt1 >= view.getLeft() && paramInt1 < view.getRight() && paramInt2 >= view.getTop() && paramInt2 < view.getBottom())
        return view; 
    } 
    return null;
  }
  
  public void flingCapturedView(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mReleaseInProgress) {
      this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), paramInt1, paramInt3, paramInt2, paramInt4);
      setDragState(2);
      return;
    } 
    throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
  }
  
  public int getActivePointerId() {
    return this.mActivePointerId;
  }
  
  public View getCapturedView() {
    return this.mCapturedView;
  }
  
  public int getEdgeSize() {
    return this.mEdgeSize;
  }
  
  public float getMinVelocity() {
    return this.mMinVelocity;
  }
  
  public int getTouchSlop() {
    return this.mTouchSlop;
  }
  
  public int getViewDragState() {
    return this.mDragState;
  }
  
  public boolean isCapturedViewUnder(int paramInt1, int paramInt2) {
    return isViewUnder(this.mCapturedView, paramInt1, paramInt2);
  }
  
  public boolean isEdgeTouched(int paramInt) {
    int i = this.mInitialEdgesTouched.length;
    for (byte b = 0; b < i; b++) {
      if (isEdgeTouched(paramInt, b))
        return true; 
    } 
    return false;
  }
  
  public boolean isEdgeTouched(int paramInt1, int paramInt2) {
    boolean bool;
    if (isPointerDown(paramInt2) && (this.mInitialEdgesTouched[paramInt2] & paramInt1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isPointerDown(int paramInt) {
    int i = this.mPointersDown;
    boolean bool = true;
    if ((i & 1 << paramInt) == 0)
      bool = false; 
    return bool;
  }
  
  public boolean isViewUnder(View paramView, int paramInt1, int paramInt2) {
    boolean bool = false;
    if (paramView == null)
      return false; 
    if (paramInt1 >= paramView.getLeft() && paramInt1 < paramView.getRight() && paramInt2 >= paramView.getTop() && paramInt2 < paramView.getBottom())
      bool = true; 
    return bool;
  }
  
  public void processTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (i == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 5) {
              if (i == 6) {
                int k = paramMotionEvent.getPointerId(j);
                if (this.mDragState == 1 && k == this.mActivePointerId) {
                  byte b = -1;
                  int m = paramMotionEvent.getPointerCount();
                  i = 0;
                  while (true) {
                    j = b;
                    if (i < m) {
                      j = paramMotionEvent.getPointerId(i);
                      if (j != this.mActivePointerId) {
                        float f1 = paramMotionEvent.getX(i);
                        float f2 = paramMotionEvent.getY(i);
                        View view1 = findTopChildUnder((int)f1, (int)f2);
                        View view2 = this.mCapturedView;
                        if (view1 == view2 && tryCaptureViewForDrag(view2, j)) {
                          j = this.mActivePointerId;
                          break;
                        } 
                      } 
                      i++;
                      continue;
                    } 
                    break;
                  } 
                  if (j == -1)
                    releaseViewForPointerUp(); 
                } 
                clearMotionHistory(k);
              } 
            } else {
              i = paramMotionEvent.getPointerId(j);
              float f2 = paramMotionEvent.getX(j);
              float f1 = paramMotionEvent.getY(j);
              saveInitialMotion(f2, f1, i);
              if (this.mDragState == 0) {
                tryCaptureViewForDrag(findTopChildUnder((int)f2, (int)f1), i);
                j = this.mInitialEdgesTouched[i];
                int k = this.mTrackingEdges;
                if ((j & k) != 0)
                  this.mCallback.onEdgeTouched(k & j, i); 
              } else if (isCapturedViewUnder((int)f2, (int)f1)) {
                tryCaptureViewForDrag(this.mCapturedView, i);
              } 
            } 
          } else {
            if (this.mDragState == 1)
              dispatchViewReleased(0.0F, 0.0F); 
            cancel();
          } 
        } else if (this.mDragState == 1) {
          if (isValidPointerForActionMove(this.mActivePointerId)) {
            i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
            float f2 = paramMotionEvent.getX(i);
            float f1 = paramMotionEvent.getY(i);
            float[] arrayOfFloat = this.mLastMotionX;
            j = this.mActivePointerId;
            i = (int)(f2 - arrayOfFloat[j]);
            j = (int)(f1 - this.mLastMotionY[j]);
            dragTo(this.mCapturedView.getLeft() + i, this.mCapturedView.getTop() + j, i, j);
            saveLastMotion(paramMotionEvent);
          } 
        } else {
          j = paramMotionEvent.getPointerCount();
          for (i = 0; i < j; i++) {
            int k = paramMotionEvent.getPointerId(i);
            if (isValidPointerForActionMove(k)) {
              float f2 = paramMotionEvent.getX(i);
              float f3 = paramMotionEvent.getY(i);
              float f4 = f2 - this.mInitialMotionX[k];
              float f1 = f3 - this.mInitialMotionY[k];
              reportNewEdgeDrags(f4, f1, k);
              if (this.mDragState == 1)
                break; 
              View view = findTopChildUnder((int)f2, (int)f3);
              if (checkTouchSlop(view, f4, f1) && tryCaptureViewForDrag(view, k))
                break; 
            } 
          } 
          saveLastMotion(paramMotionEvent);
        } 
      } else {
        if (this.mDragState == 1)
          releaseViewForPointerUp(); 
        cancel();
      } 
    } else {
      float f2 = paramMotionEvent.getX();
      float f1 = paramMotionEvent.getY();
      j = paramMotionEvent.getPointerId(0);
      View view = findTopChildUnder((int)f2, (int)f1);
      saveInitialMotion(f2, f1, j);
      tryCaptureViewForDrag(view, j);
      i = this.mInitialEdgesTouched[j];
      int k = this.mTrackingEdges;
      if ((i & k) != 0)
        this.mCallback.onEdgeTouched(k & i, j); 
    } 
  }
  
  void setDragState(int paramInt) {
    this.mParentView.removeCallbacks(this.mSetIdleRunnable);
    if (this.mDragState != paramInt) {
      this.mDragState = paramInt;
      this.mCallback.onViewDragStateChanged(paramInt);
      if (this.mDragState == 0)
        this.mCapturedView = null; 
    } 
  }
  
  public void setEdgeTrackingEnabled(int paramInt) {
    this.mTrackingEdges = paramInt;
  }
  
  public void setMinVelocity(float paramFloat) {
    this.mMinVelocity = paramFloat;
  }
  
  public boolean settleCapturedViewAt(int paramInt1, int paramInt2) {
    if (this.mReleaseInProgress)
      return forceSettleCapturedViewAt(paramInt1, paramInt2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId)); 
    throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
  }
  
  public boolean shouldInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getActionMasked : ()I
    //   4: istore_2
    //   5: aload_1
    //   6: invokevirtual getActionIndex : ()I
    //   9: istore_3
    //   10: iload_2
    //   11: ifne -> 18
    //   14: aload_0
    //   15: invokevirtual cancel : ()V
    //   18: aload_0
    //   19: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   22: ifnonnull -> 32
    //   25: aload_0
    //   26: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   29: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   32: aload_0
    //   33: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   36: aload_1
    //   37: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   40: iload_2
    //   41: ifeq -> 522
    //   44: iload_2
    //   45: iconst_1
    //   46: if_icmpeq -> 515
    //   49: iload_2
    //   50: iconst_2
    //   51: if_icmpeq -> 200
    //   54: iload_2
    //   55: iconst_3
    //   56: if_icmpeq -> 197
    //   59: iload_2
    //   60: iconst_5
    //   61: if_icmpeq -> 85
    //   64: iload_2
    //   65: bipush #6
    //   67: if_icmpeq -> 73
    //   70: goto -> 615
    //   73: aload_0
    //   74: aload_1
    //   75: iload_3
    //   76: invokevirtual getPointerId : (I)I
    //   79: invokespecial clearMotionHistory : (I)V
    //   82: goto -> 615
    //   85: aload_1
    //   86: iload_3
    //   87: invokevirtual getPointerId : (I)I
    //   90: istore #4
    //   92: aload_1
    //   93: iload_3
    //   94: invokevirtual getX : (I)F
    //   97: fstore #5
    //   99: aload_1
    //   100: iload_3
    //   101: invokevirtual getY : (I)F
    //   104: fstore #6
    //   106: aload_0
    //   107: fload #5
    //   109: fload #6
    //   111: iload #4
    //   113: invokespecial saveInitialMotion : (FFI)V
    //   116: aload_0
    //   117: getfield mDragState : I
    //   120: istore_3
    //   121: iload_3
    //   122: ifne -> 159
    //   125: aload_0
    //   126: getfield mInitialEdgesTouched : [I
    //   129: iload #4
    //   131: iaload
    //   132: istore_2
    //   133: aload_0
    //   134: getfield mTrackingEdges : I
    //   137: istore_3
    //   138: iload_2
    //   139: iload_3
    //   140: iand
    //   141: ifeq -> 194
    //   144: aload_0
    //   145: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   148: iload_3
    //   149: iload_2
    //   150: iand
    //   151: iload #4
    //   153: invokevirtual onEdgeTouched : (II)V
    //   156: goto -> 194
    //   159: iload_3
    //   160: iconst_2
    //   161: if_icmpne -> 194
    //   164: aload_0
    //   165: fload #5
    //   167: f2i
    //   168: fload #6
    //   170: f2i
    //   171: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   174: astore_1
    //   175: aload_1
    //   176: aload_0
    //   177: getfield mCapturedView : Landroid/view/View;
    //   180: if_acmpne -> 191
    //   183: aload_0
    //   184: aload_1
    //   185: iload #4
    //   187: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   190: pop
    //   191: goto -> 615
    //   194: goto -> 615
    //   197: goto -> 515
    //   200: aload_0
    //   201: getfield mInitialMotionX : [F
    //   204: ifnull -> 512
    //   207: aload_0
    //   208: getfield mInitialMotionY : [F
    //   211: ifnonnull -> 217
    //   214: goto -> 615
    //   217: aload_1
    //   218: invokevirtual getPointerCount : ()I
    //   221: istore #4
    //   223: iconst_0
    //   224: istore #7
    //   226: iload #7
    //   228: iload #4
    //   230: if_icmpge -> 504
    //   233: aload_1
    //   234: iload #7
    //   236: invokevirtual getPointerId : (I)I
    //   239: istore #8
    //   241: aload_0
    //   242: iload #8
    //   244: invokespecial isValidPointerForActionMove : (I)Z
    //   247: ifne -> 253
    //   250: goto -> 498
    //   253: aload_1
    //   254: iload #7
    //   256: invokevirtual getX : (I)F
    //   259: fstore #6
    //   261: aload_1
    //   262: iload #7
    //   264: invokevirtual getY : (I)F
    //   267: fstore #5
    //   269: fload #6
    //   271: aload_0
    //   272: getfield mInitialMotionX : [F
    //   275: iload #8
    //   277: faload
    //   278: fsub
    //   279: fstore #9
    //   281: fload #5
    //   283: aload_0
    //   284: getfield mInitialMotionY : [F
    //   287: iload #8
    //   289: faload
    //   290: fsub
    //   291: fstore #10
    //   293: aload_0
    //   294: fload #6
    //   296: f2i
    //   297: fload #5
    //   299: f2i
    //   300: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   303: astore #11
    //   305: aload #11
    //   307: ifnull -> 329
    //   310: aload_0
    //   311: aload #11
    //   313: fload #9
    //   315: fload #10
    //   317: invokespecial checkTouchSlop : (Landroid/view/View;FF)Z
    //   320: ifeq -> 329
    //   323: iconst_1
    //   324: istore #12
    //   326: goto -> 332
    //   329: iconst_0
    //   330: istore #12
    //   332: iload #12
    //   334: ifeq -> 458
    //   337: aload #11
    //   339: invokevirtual getLeft : ()I
    //   342: istore #13
    //   344: fload #9
    //   346: f2i
    //   347: istore #14
    //   349: aload_0
    //   350: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   353: aload #11
    //   355: iload #14
    //   357: iload #13
    //   359: iadd
    //   360: fload #9
    //   362: f2i
    //   363: invokevirtual clampViewPositionHorizontal : (Landroid/view/View;II)I
    //   366: istore #14
    //   368: aload #11
    //   370: invokevirtual getTop : ()I
    //   373: istore #15
    //   375: fload #10
    //   377: f2i
    //   378: istore #16
    //   380: aload_0
    //   381: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   384: aload #11
    //   386: iload #16
    //   388: iload #15
    //   390: iadd
    //   391: fload #10
    //   393: f2i
    //   394: invokevirtual clampViewPositionVertical : (Landroid/view/View;II)I
    //   397: istore #17
    //   399: aload_0
    //   400: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   403: aload #11
    //   405: invokevirtual getViewHorizontalDragRange : (Landroid/view/View;)I
    //   408: istore #18
    //   410: aload_0
    //   411: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   414: aload #11
    //   416: invokevirtual getViewVerticalDragRange : (Landroid/view/View;)I
    //   419: istore #16
    //   421: iload #18
    //   423: ifeq -> 438
    //   426: iload #18
    //   428: ifle -> 458
    //   431: iload #14
    //   433: iload #13
    //   435: if_icmpne -> 458
    //   438: iload #16
    //   440: ifeq -> 504
    //   443: iload #16
    //   445: ifle -> 458
    //   448: iload #17
    //   450: iload #15
    //   452: if_icmpne -> 458
    //   455: goto -> 504
    //   458: aload_0
    //   459: fload #9
    //   461: fload #10
    //   463: iload #8
    //   465: invokespecial reportNewEdgeDrags : (FFI)V
    //   468: aload_0
    //   469: getfield mDragState : I
    //   472: iconst_1
    //   473: if_icmpne -> 479
    //   476: goto -> 504
    //   479: iload #12
    //   481: ifeq -> 498
    //   484: aload_0
    //   485: aload #11
    //   487: iload #8
    //   489: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   492: ifeq -> 498
    //   495: goto -> 504
    //   498: iinc #7, 1
    //   501: goto -> 226
    //   504: aload_0
    //   505: aload_1
    //   506: invokespecial saveLastMotion : (Landroid/view/MotionEvent;)V
    //   509: goto -> 615
    //   512: goto -> 615
    //   515: aload_0
    //   516: invokevirtual cancel : ()V
    //   519: goto -> 615
    //   522: aload_1
    //   523: invokevirtual getX : ()F
    //   526: fstore #6
    //   528: aload_1
    //   529: invokevirtual getY : ()F
    //   532: fstore #5
    //   534: aload_1
    //   535: iconst_0
    //   536: invokevirtual getPointerId : (I)I
    //   539: istore_2
    //   540: aload_0
    //   541: fload #6
    //   543: fload #5
    //   545: iload_2
    //   546: invokespecial saveInitialMotion : (FFI)V
    //   549: aload_0
    //   550: fload #6
    //   552: f2i
    //   553: fload #5
    //   555: f2i
    //   556: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   559: astore_1
    //   560: aload_1
    //   561: aload_0
    //   562: getfield mCapturedView : Landroid/view/View;
    //   565: if_acmpne -> 583
    //   568: aload_0
    //   569: getfield mDragState : I
    //   572: iconst_2
    //   573: if_icmpne -> 583
    //   576: aload_0
    //   577: aload_1
    //   578: iload_2
    //   579: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   582: pop
    //   583: aload_0
    //   584: getfield mInitialEdgesTouched : [I
    //   587: iload_2
    //   588: iaload
    //   589: istore #4
    //   591: aload_0
    //   592: getfield mTrackingEdges : I
    //   595: istore_3
    //   596: iload #4
    //   598: iload_3
    //   599: iand
    //   600: ifeq -> 615
    //   603: aload_0
    //   604: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   607: iload_3
    //   608: iload #4
    //   610: iand
    //   611: iload_2
    //   612: invokevirtual onEdgeTouched : (II)V
    //   615: iconst_0
    //   616: istore #19
    //   618: aload_0
    //   619: getfield mDragState : I
    //   622: iconst_1
    //   623: if_icmpne -> 629
    //   626: iconst_1
    //   627: istore #19
    //   629: iload #19
    //   631: ireturn
  }
  
  public boolean smoothSlideViewTo(View paramView, int paramInt1, int paramInt2) {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    boolean bool = forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
    if (!bool && this.mDragState == 0 && this.mCapturedView != null)
      this.mCapturedView = null; 
    return bool;
  }
  
  boolean tryCaptureViewForDrag(View paramView, int paramInt) {
    if (paramView == this.mCapturedView && this.mActivePointerId == paramInt)
      return true; 
    if (paramView != null && this.mCallback.tryCaptureView(paramView, paramInt)) {
      this.mActivePointerId = paramInt;
      captureChildView(paramView, paramInt);
      return true;
    } 
    return false;
  }
  
  public static abstract class Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int getOrderedChildIndex(int param1Int) {
      return param1Int;
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return 0;
    }
    
    public int getViewVerticalDragRange(View param1View) {
      return 0;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {}
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {}
    
    public void onViewCaptured(View param1View, int param1Int) {}
    
    public void onViewDragStateChanged(int param1Int) {}
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {}
    
    public abstract boolean tryCaptureView(View param1View, int param1Int);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/widget/ViewDragHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */