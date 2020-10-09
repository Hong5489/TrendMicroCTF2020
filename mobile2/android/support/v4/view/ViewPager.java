package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
  private static final int CLOSE_ENOUGH = 2;
  
  private static final Comparator<ItemInfo> COMPARATOR;
  
  private static final boolean DEBUG = false;
  
  private static final int DEFAULT_GUTTER_SIZE = 16;
  
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  
  private static final int DRAW_ORDER_DEFAULT = 0;
  
  private static final int DRAW_ORDER_FORWARD = 1;
  
  private static final int DRAW_ORDER_REVERSE = 2;
  
  private static final int INVALID_POINTER = -1;
  
  static final int[] LAYOUT_ATTRS = new int[] { 16842931 };
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  private static final String TAG = "ViewPager";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator;
  
  private static final ViewPositionComparator sPositionComparator;
  
  private int mActivePointerId = -1;
  
  PagerAdapter mAdapter;
  
  private List<OnAdapterChangeListener> mAdapterChangeListeners;
  
  private int mBottomPageBounds;
  
  private boolean mCalledSuper;
  
  private int mChildHeightMeasureSpec;
  
  private int mChildWidthMeasureSpec;
  
  private int mCloseEnough;
  
  int mCurItem;
  
  private int mDecorChildCount;
  
  private int mDefaultGutterSize;
  
  private int mDrawingOrder;
  
  private ArrayList<View> mDrawingOrderedChildren;
  
  private final Runnable mEndScrollRunnable = new Runnable() {
      public void run() {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
  
  private int mExpectedAdapterCount;
  
  private long mFakeDragBeginTime;
  
  private boolean mFakeDragging;
  
  private boolean mFirstLayout = true;
  
  private float mFirstOffset = -3.4028235E38F;
  
  private int mFlingDistance;
  
  private int mGutterSize;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsScrollStarted;
  
  private boolean mIsUnableToDrag;
  
  private final ArrayList<ItemInfo> mItems = new ArrayList<>();
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private float mLastOffset = Float.MAX_VALUE;
  
  private EdgeEffect mLeftEdge;
  
  private Drawable mMarginDrawable;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private boolean mNeedCalculatePageOffsets = false;
  
  private PagerObserver mObserver;
  
  private int mOffscreenPageLimit = 1;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private List<OnPageChangeListener> mOnPageChangeListeners;
  
  private int mPageMargin;
  
  private PageTransformer mPageTransformer;
  
  private int mPageTransformerLayerType;
  
  private boolean mPopulatePending;
  
  private Parcelable mRestoredAdapterState = null;
  
  private ClassLoader mRestoredClassLoader = null;
  
  private int mRestoredCurItem = -1;
  
  private EdgeEffect mRightEdge;
  
  private int mScrollState = 0;
  
  private Scroller mScroller;
  
  private boolean mScrollingCacheEnabled;
  
  private final ItemInfo mTempItem = new ItemInfo();
  
  private final Rect mTempRect = new Rect();
  
  private int mTopPageBounds;
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  static {
    COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ViewPager.ItemInfo param1ItemInfo1, ViewPager.ItemInfo param1ItemInfo2) {
          return param1ItemInfo1.position - param1ItemInfo2.position;
        }
      };
    sInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
    sPositionComparator = new ViewPositionComparator();
  }
  
  public ViewPager(Context paramContext) {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2) {
    float f1;
    int i = this.mAdapter.getCount();
    int j = getClientWidth();
    if (j > 0) {
      f1 = this.mPageMargin / j;
    } else {
      f1 = 0.0F;
    } 
    if (paramItemInfo2 != null) {
      j = paramItemInfo2.position;
      if (j < paramItemInfo1.position) {
        byte b = 0;
        f2 = paramItemInfo2.offset + paramItemInfo2.widthFactor + f1;
        while (++j <= paramItemInfo1.position && b < this.mItems.size()) {
          float f;
          int n;
          paramItemInfo2 = this.mItems.get(b);
          while (true) {
            f = f2;
            n = j;
            if (j > paramItemInfo2.position) {
              f = f2;
              n = j;
              if (b < this.mItems.size() - 1) {
                paramItemInfo2 = this.mItems.get(++b);
                continue;
              } 
            } 
            break;
          } 
          while (n < paramItemInfo2.position) {
            f += this.mAdapter.getPageWidth(n) + f1;
            n++;
          } 
          paramItemInfo2.offset = f;
          f2 = f + paramItemInfo2.widthFactor + f1;
          j = n + 1;
        } 
      } else if (j > paramItemInfo1.position) {
        int n = this.mItems.size() - 1;
        f2 = paramItemInfo2.offset;
        while (--j >= paramItemInfo1.position && n >= 0) {
          float f;
          int i1;
          paramItemInfo2 = this.mItems.get(n);
          while (true) {
            f = f2;
            i1 = j;
            if (j < paramItemInfo2.position) {
              f = f2;
              i1 = j;
              if (n > 0) {
                paramItemInfo2 = this.mItems.get(--n);
                continue;
              } 
            } 
            break;
          } 
          while (i1 > paramItemInfo2.position) {
            f -= this.mAdapter.getPageWidth(i1) + f1;
            i1--;
          } 
          f2 = f - paramItemInfo2.widthFactor + f1;
          paramItemInfo2.offset = f2;
          j = i1 - 1;
        } 
      } 
    } 
    int m = this.mItems.size();
    float f3 = paramItemInfo1.offset;
    j = paramItemInfo1.position - 1;
    if (paramItemInfo1.position == 0) {
      f2 = paramItemInfo1.offset;
    } else {
      f2 = -3.4028235E38F;
    } 
    this.mFirstOffset = f2;
    if (paramItemInfo1.position == i - 1) {
      f2 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
    } else {
      f2 = Float.MAX_VALUE;
    } 
    this.mLastOffset = f2;
    int k = paramInt - 1;
    float f2 = f3;
    while (k >= 0) {
      paramItemInfo2 = this.mItems.get(k);
      while (j > paramItemInfo2.position) {
        f2 -= this.mAdapter.getPageWidth(j) + f1;
        j--;
      } 
      f2 -= paramItemInfo2.widthFactor + f1;
      paramItemInfo2.offset = f2;
      if (paramItemInfo2.position == 0)
        this.mFirstOffset = f2; 
      k--;
      j--;
    } 
    f2 = paramItemInfo1.offset + paramItemInfo1.widthFactor + f1;
    k = paramItemInfo1.position + 1;
    j = paramInt + 1;
    for (paramInt = k; j < m; paramInt++) {
      paramItemInfo1 = this.mItems.get(j);
      while (paramInt < paramItemInfo1.position) {
        f2 += this.mAdapter.getPageWidth(paramInt) + f1;
        paramInt++;
      } 
      if (paramItemInfo1.position == i - 1)
        this.mLastOffset = paramItemInfo1.widthFactor + f2 - 1.0F; 
      paramItemInfo1.offset = f2;
      f2 += paramItemInfo1.widthFactor + f1;
      j++;
    } 
    this.mNeedCalculatePageOffsets = false;
  }
  
  private void completeScroll(boolean paramBoolean) {
    boolean bool;
    if (this.mScrollState == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      setScrollingCacheEnabled(false);
      if ((true ^ this.mScroller.isFinished()) != 0) {
        this.mScroller.abortAnimation();
        int i = getScrollX();
        int j = getScrollY();
        int k = this.mScroller.getCurrX();
        int m = this.mScroller.getCurrY();
        if (i != k || j != m) {
          scrollTo(k, m);
          if (k != i)
            pageScrolled(k); 
        } 
      } 
    } 
    this.mPopulatePending = false;
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (itemInfo.scrolling) {
        bool = true;
        itemInfo.scrolling = false;
      } 
    } 
    if (bool)
      if (paramBoolean) {
        ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
      } else {
        this.mEndScrollRunnable.run();
      }  
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
    if (Math.abs(paramInt3) > this.mFlingDistance && Math.abs(paramInt2) > this.mMinimumVelocity) {
      if (paramInt2 <= 0)
        paramInt1++; 
    } else {
      float f;
      if (paramInt1 >= this.mCurItem) {
        f = 0.4F;
      } else {
        f = 0.6F;
      } 
      paramInt1 = (int)(paramFloat + f) + paramInt1;
    } 
    paramInt2 = paramInt1;
    if (this.mItems.size() > 0) {
      ItemInfo itemInfo1 = this.mItems.get(0);
      ArrayList<ItemInfo> arrayList = this.mItems;
      ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
      paramInt2 = Math.max(itemInfo1.position, Math.min(paramInt1, itemInfo2.position));
    } 
    return paramInt2;
  }
  
  private void dispatchOnPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrolled(paramInt1, paramFloat, paramInt2); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  private void dispatchOnPageSelected(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageSelected(paramInt); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageSelected(paramInt); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageSelected(paramInt); 
  }
  
  private void dispatchOnScrollStateChanged(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrollStateChanged(paramInt); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrollStateChanged(paramInt); 
        b++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrollStateChanged(paramInt); 
  }
  
  private void enableLayers(boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      boolean bool;
      if (paramBoolean) {
        bool = this.mPageTransformerLayerType;
      } else {
        bool = false;
      } 
      getChildAt(b).setLayerType(bool, null);
    } 
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView) {
    Rect rect = paramRect;
    if (paramRect == null)
      rect = new Rect(); 
    if (paramView == null) {
      rect.set(0, 0, 0, 0);
      return rect;
    } 
    rect.left = paramView.getLeft();
    rect.right = paramView.getRight();
    rect.top = paramView.getTop();
    rect.bottom = paramView.getBottom();
    ViewParent viewParent = paramView.getParent();
    while (viewParent instanceof ViewGroup && viewParent != this) {
      ViewGroup viewGroup = (ViewGroup)viewParent;
      rect.left += viewGroup.getLeft();
      rect.right += viewGroup.getRight();
      rect.top += viewGroup.getTop();
      rect.bottom += viewGroup.getBottom();
      ViewParent viewParent1 = viewGroup.getParent();
    } 
    return rect;
  }
  
  private int getClientWidth() {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }
  
  private ItemInfo infoForCurrentScrollPosition() {
    float f2;
    int i = getClientWidth();
    float f1 = 0.0F;
    if (i > 0) {
      f2 = getScrollX() / i;
    } else {
      f2 = 0.0F;
    } 
    if (i > 0)
      f1 = this.mPageMargin / i; 
    int j = -1;
    float f3 = 0.0F;
    float f4 = 0.0F;
    boolean bool = true;
    ItemInfo itemInfo = null;
    i = 0;
    while (i < this.mItems.size()) {
      ItemInfo itemInfo1 = this.mItems.get(i);
      int k = i;
      ItemInfo itemInfo2 = itemInfo1;
      if (!bool) {
        k = i;
        itemInfo2 = itemInfo1;
        if (itemInfo1.position != j + 1) {
          itemInfo2 = this.mTempItem;
          itemInfo2.offset = f3 + f4 + f1;
          itemInfo2.position = j + 1;
          itemInfo2.widthFactor = this.mAdapter.getPageWidth(itemInfo2.position);
          k = i - 1;
        } 
      } 
      f3 = itemInfo2.offset;
      f4 = itemInfo2.widthFactor;
      if (bool || f2 >= f3) {
        if (f2 < f4 + f3 + f1 || k == this.mItems.size() - 1)
          return itemInfo2; 
        bool = false;
        j = itemInfo2.position;
        f4 = itemInfo2.widthFactor;
        i = k + 1;
        itemInfo = itemInfo2;
        continue;
      } 
      return itemInfo;
    } 
    return itemInfo;
  }
  
  private static boolean isDecorView(View paramView) {
    boolean bool;
    if (paramView.getClass().getAnnotation(DecorView.class) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2) {
    boolean bool;
    if ((paramFloat1 < this.mGutterSize && paramFloat2 > 0.0F) || (paramFloat1 > (getWidth() - this.mGutterSize) && paramFloat2 < 0.0F)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionX = paramMotionEvent.getX(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    } 
  }
  
  private boolean pageScrolled(int paramInt) {
    if (this.mItems.size() == 0) {
      if (this.mFirstLayout)
        return false; 
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (this.mCalledSuper)
        return false; 
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    } 
    ItemInfo itemInfo = infoForCurrentScrollPosition();
    int i = getClientWidth();
    int j = this.mPageMargin;
    float f = j / i;
    int k = itemInfo.position;
    f = (paramInt / i - itemInfo.offset) / (itemInfo.widthFactor + f);
    paramInt = (int)((i + j) * f);
    this.mCalledSuper = false;
    onPageScrolled(k, f, paramInt);
    if (this.mCalledSuper)
      return true; 
    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
  }
  
  private boolean performDrag(float paramFloat) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    float f1 = this.mLastMotionX;
    this.mLastMotionX = paramFloat;
    float f2 = getScrollX() + f1 - paramFloat;
    int i = getClientWidth();
    paramFloat = i * this.mFirstOffset;
    f1 = i * this.mLastOffset;
    boolean bool4 = true;
    boolean bool5 = true;
    ItemInfo itemInfo1 = this.mItems.get(0);
    ArrayList<ItemInfo> arrayList = this.mItems;
    ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
    if (itemInfo1.position != 0) {
      bool4 = false;
      paramFloat = itemInfo1.offset * i;
    } 
    if (itemInfo2.position != this.mAdapter.getCount() - 1) {
      bool5 = false;
      f1 = itemInfo2.offset * i;
    } 
    if (f2 < paramFloat) {
      if (bool4) {
        this.mLeftEdge.onPull(Math.abs(paramFloat - f2) / i);
        bool3 = true;
      } 
    } else {
      bool3 = bool2;
      paramFloat = f2;
      if (f2 > f1) {
        bool3 = bool1;
        if (bool5) {
          this.mRightEdge.onPull(Math.abs(f2 - f1) / i);
          bool3 = true;
        } 
        paramFloat = f1;
      } 
    } 
    this.mLastMotionX += paramFloat - (int)paramFloat;
    scrollTo((int)paramFloat, getScrollY());
    pageScrolled((int)paramFloat);
    return bool3;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt2 > 0 && !this.mItems.isEmpty()) {
      if (!this.mScroller.isFinished()) {
        this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
      } else {
        int i = getPaddingLeft();
        int j = getPaddingRight();
        int k = getPaddingLeft();
        int m = getPaddingRight();
        float f = getScrollX() / (paramInt2 - k - m + paramInt4);
        scrollTo((int)((paramInt1 - i - j + paramInt3) * f), getScrollY());
      } 
    } else {
      float f;
      ItemInfo itemInfo = infoForPosition(this.mCurItem);
      if (itemInfo != null) {
        f = Math.min(itemInfo.offset, this.mLastOffset);
      } else {
        f = 0.0F;
      } 
      paramInt1 = (int)((paramInt1 - getPaddingLeft() - getPaddingRight()) * f);
      if (paramInt1 != getScrollX()) {
        completeScroll(false);
        scrollTo(paramInt1, getScrollY());
      } 
    } 
  }
  
  private void removeNonDecorViews() {
    for (int i = 0; i < getChildCount(); i = j + 1) {
      int j = i;
      if (!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor) {
        removeViewAt(i);
        j = i - 1;
      } 
    } 
  }
  
  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean) {
    ViewParent viewParent = getParent();
    if (viewParent != null)
      viewParent.requestDisallowInterceptTouchEvent(paramBoolean); 
  }
  
  private boolean resetTouch() {
    this.mActivePointerId = -1;
    endDrag();
    this.mLeftEdge.onRelease();
    this.mRightEdge.onRelease();
    return (this.mLeftEdge.isFinished() || this.mRightEdge.isFinished());
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) {
    ItemInfo itemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (itemInfo != null)
      i = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset))); 
    if (paramBoolean1) {
      smoothScrollTo(i, 0, paramInt2);
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
    } else {
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
      completeScroll(false);
      scrollTo(i, 0);
      pageScrolled(i);
    } 
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  private void sortChildDrawingOrder() {
    if (this.mDrawingOrder != 0) {
      ArrayList<View> arrayList = this.mDrawingOrderedChildren;
      if (arrayList == null) {
        this.mDrawingOrderedChildren = new ArrayList<>();
      } else {
        arrayList.clear();
      } 
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        this.mDrawingOrderedChildren.add(view);
      } 
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    } 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216)
      for (byte b = 0; b < getChildCount(); b++) {
        View view = getChildAt(b);
        if (view.getVisibility() == 0) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null && itemInfo.position == this.mCurItem)
            view.addFocusables(paramArrayList, paramInt1, paramInt2); 
        } 
      }  
    if (j != 262144 || i == paramArrayList.size()) {
      if (!isFocusable())
        return; 
      if ((paramInt2 & 0x1) == 1 && isInTouchMode() && !isFocusableInTouchMode())
        return; 
      if (paramArrayList != null)
        paramArrayList.add(this); 
    } 
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.position = paramInt1;
    itemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    itemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if (paramInt2 < 0 || paramInt2 >= this.mItems.size()) {
      this.mItems.add(itemInfo);
      return itemInfo;
    } 
    this.mItems.add(paramInt2, itemInfo);
    return itemInfo;
  }
  
  public void addOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    if (this.mAdapterChangeListeners == null)
      this.mAdapterChangeListeners = new ArrayList<>(); 
    this.mAdapterChangeListeners.add(paramOnAdapterChangeListener);
  }
  
  public void addOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    if (this.mOnPageChangeListeners == null)
      this.mOnPageChangeListeners = new ArrayList<>(); 
    this.mOnPageChangeListeners.add(paramOnPageChangeListener);
  }
  
  public void addTouchables(ArrayList<View> paramArrayList) {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem)
          view.addTouchables(paramArrayList); 
      } 
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    ViewGroup.LayoutParams layoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams))
      layoutParams = generateLayoutParams(paramLayoutParams); 
    paramLayoutParams = layoutParams;
    ((LayoutParams)paramLayoutParams).isDecor |= isDecorView(paramView);
    if (this.mInLayout) {
      if (paramLayoutParams == null || !((LayoutParams)paramLayoutParams).isDecor) {
        ((LayoutParams)paramLayoutParams).needsMeasure = true;
        addViewInLayout(paramView, paramInt, layoutParams);
        return;
      } 
      throw new IllegalStateException("Cannot add pager decor view during layout");
    } 
    super.addView(paramView, paramInt, layoutParams);
  }
  
  public boolean arrowScroll(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual findFocus : ()Landroid/view/View;
    //   4: astore_2
    //   5: aload_2
    //   6: aload_0
    //   7: if_acmpne -> 15
    //   10: aconst_null
    //   11: astore_3
    //   12: goto -> 172
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: ifnull -> 172
    //   21: iconst_0
    //   22: istore #4
    //   24: aload_2
    //   25: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   28: astore_3
    //   29: iload #4
    //   31: istore #5
    //   33: aload_3
    //   34: instanceof android/view/ViewGroup
    //   37: ifeq -> 61
    //   40: aload_3
    //   41: aload_0
    //   42: if_acmpne -> 51
    //   45: iconst_1
    //   46: istore #5
    //   48: goto -> 61
    //   51: aload_3
    //   52: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   57: astore_3
    //   58: goto -> 29
    //   61: aload_2
    //   62: astore_3
    //   63: iload #5
    //   65: ifne -> 172
    //   68: new java/lang/StringBuilder
    //   71: dup
    //   72: invokespecial <init> : ()V
    //   75: astore #6
    //   77: aload #6
    //   79: aload_2
    //   80: invokevirtual getClass : ()Ljava/lang/Class;
    //   83: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   86: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: pop
    //   90: aload_2
    //   91: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   94: astore_3
    //   95: aload_3
    //   96: instanceof android/view/ViewGroup
    //   99: ifeq -> 134
    //   102: aload #6
    //   104: ldc_w ' => '
    //   107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload #6
    //   113: aload_3
    //   114: invokevirtual getClass : ()Ljava/lang/Class;
    //   117: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload_3
    //   125: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   130: astore_3
    //   131: goto -> 95
    //   134: new java/lang/StringBuilder
    //   137: dup
    //   138: invokespecial <init> : ()V
    //   141: astore_3
    //   142: aload_3
    //   143: ldc_w 'arrowScroll tried to find focus based on non-child current focused view '
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_3
    //   151: aload #6
    //   153: invokevirtual toString : ()Ljava/lang/String;
    //   156: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: ldc 'ViewPager'
    //   162: aload_3
    //   163: invokevirtual toString : ()Ljava/lang/String;
    //   166: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   169: pop
    //   170: aconst_null
    //   171: astore_3
    //   172: iconst_0
    //   173: istore #7
    //   175: iconst_0
    //   176: istore #8
    //   178: invokestatic getInstance : ()Landroid/view/FocusFinder;
    //   181: aload_0
    //   182: aload_3
    //   183: iload_1
    //   184: invokevirtual findNextFocus : (Landroid/view/ViewGroup;Landroid/view/View;I)Landroid/view/View;
    //   187: astore_2
    //   188: aload_2
    //   189: ifnull -> 323
    //   192: aload_2
    //   193: aload_3
    //   194: if_acmpeq -> 323
    //   197: iload_1
    //   198: bipush #17
    //   200: if_icmpne -> 260
    //   203: aload_0
    //   204: aload_0
    //   205: getfield mTempRect : Landroid/graphics/Rect;
    //   208: aload_2
    //   209: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   212: getfield left : I
    //   215: istore #4
    //   217: aload_0
    //   218: aload_0
    //   219: getfield mTempRect : Landroid/graphics/Rect;
    //   222: aload_3
    //   223: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   226: getfield left : I
    //   229: istore #5
    //   231: aload_3
    //   232: ifnull -> 251
    //   235: iload #4
    //   237: iload #5
    //   239: if_icmplt -> 251
    //   242: aload_0
    //   243: invokevirtual pageLeft : ()Z
    //   246: istore #8
    //   248: goto -> 257
    //   251: aload_2
    //   252: invokevirtual requestFocus : ()Z
    //   255: istore #8
    //   257: goto -> 367
    //   260: iload_1
    //   261: bipush #66
    //   263: if_icmpne -> 257
    //   266: aload_0
    //   267: aload_0
    //   268: getfield mTempRect : Landroid/graphics/Rect;
    //   271: aload_2
    //   272: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   275: getfield left : I
    //   278: istore #5
    //   280: aload_0
    //   281: aload_0
    //   282: getfield mTempRect : Landroid/graphics/Rect;
    //   285: aload_3
    //   286: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   289: getfield left : I
    //   292: istore #4
    //   294: aload_3
    //   295: ifnull -> 314
    //   298: iload #5
    //   300: iload #4
    //   302: if_icmpgt -> 314
    //   305: aload_0
    //   306: invokevirtual pageRight : ()Z
    //   309: istore #8
    //   311: goto -> 320
    //   314: aload_2
    //   315: invokevirtual requestFocus : ()Z
    //   318: istore #8
    //   320: goto -> 367
    //   323: iload_1
    //   324: bipush #17
    //   326: if_icmpeq -> 361
    //   329: iload_1
    //   330: iconst_1
    //   331: if_icmpne -> 337
    //   334: goto -> 361
    //   337: iload_1
    //   338: bipush #66
    //   340: if_icmpeq -> 352
    //   343: iload #7
    //   345: istore #8
    //   347: iload_1
    //   348: iconst_2
    //   349: if_icmpne -> 367
    //   352: aload_0
    //   353: invokevirtual pageRight : ()Z
    //   356: istore #8
    //   358: goto -> 367
    //   361: aload_0
    //   362: invokevirtual pageLeft : ()Z
    //   365: istore #8
    //   367: iload #8
    //   369: ifeq -> 380
    //   372: aload_0
    //   373: iload_1
    //   374: invokestatic getContantForFocusDirection : (I)I
    //   377: invokevirtual playSoundEffect : (I)V
    //   380: iload #8
    //   382: ireturn
  }
  
  public boolean beginFakeDrag() {
    if (this.mIsBeingDragged)
      return false; 
    this.mFakeDragging = true;
    setScrollState(1);
    this.mLastMotionX = 0.0F;
    this.mInitialMotionX = 0.0F;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    } else {
      velocityTracker.clear();
    } 
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
    this.mVelocityTracker.addMovement(motionEvent);
    motionEvent.recycle();
    this.mFakeDragBeginTime = l;
    return true;
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
    if (paramBoolean && paramView.canScrollHorizontally(-paramInt1)) {
      paramBoolean = bool1;
    } else {
      paramBoolean = false;
    } 
    return paramBoolean;
  }
  
  public boolean canScrollHorizontally(int paramInt) {
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool1 = false;
    boolean bool2 = false;
    if (pagerAdapter == null)
      return false; 
    int i = getClientWidth();
    int j = getScrollX();
    if (paramInt < 0) {
      if (j > (int)(i * this.mFirstOffset))
        bool2 = true; 
      return bool2;
    } 
    if (paramInt > 0) {
      bool2 = bool1;
      if (j < (int)(i * this.mLastOffset))
        bool2 = true; 
      return bool2;
    } 
    return false;
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
  
  public void clearOnPageChangeListeners() {
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null)
      list.clear(); 
  }
  
  public void computeScroll() {
    this.mIsScrollStarted = true;
    if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m) {
        scrollTo(k, m);
        if (!pageScrolled(k)) {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        } 
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
      return;
    } 
    completeScroll(true);
  }
  
  void dataSetChanged() {
    byte b;
    int i = this.mAdapter.getCount();
    this.mExpectedAdapterCount = i;
    if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < i) {
      b = 1;
    } else {
      b = 0;
    } 
    int j = this.mCurItem;
    int k = 0;
    int m = 0;
    while (m < this.mItems.size()) {
      int i1;
      int i2;
      int i3;
      ItemInfo itemInfo = this.mItems.get(m);
      int n = this.mAdapter.getItemPosition(itemInfo.object);
      if (n == -1) {
        i1 = j;
        i2 = k;
        i3 = m;
      } else if (n == -2) {
        this.mItems.remove(m);
        n = m - 1;
        m = k;
        if (!k) {
          this.mAdapter.startUpdate(this);
          m = 1;
        } 
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
        b = 1;
        i1 = j;
        i2 = m;
        i3 = n;
        if (this.mCurItem == itemInfo.position) {
          i1 = Math.max(0, Math.min(this.mCurItem, i - 1));
          b = 1;
          i2 = m;
          i3 = n;
        } 
      } else {
        i1 = j;
        i2 = k;
        i3 = m;
        if (itemInfo.position != n) {
          if (itemInfo.position == this.mCurItem)
            j = n; 
          itemInfo.position = n;
          b = 1;
          i3 = m;
          i2 = k;
          i1 = j;
        } 
      } 
      m = i3 + 1;
      j = i1;
      k = i2;
    } 
    if (k != 0)
      this.mAdapter.finishUpdate(this); 
    Collections.sort(this.mItems, COMPARATOR);
    if (b) {
      k = getChildCount();
      for (b = 0; b < k; b++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
        if (!layoutParams.isDecor)
          layoutParams.widthFactor = 0.0F; 
      } 
      setCurrentItemInternal(j, false, true);
      requestLayout();
    } 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (paramAccessibilityEvent.getEventType() == 4096)
      return super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent); 
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))
          return true; 
      } 
    } 
    return false;
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F));
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial draw : (Landroid/graphics/Canvas;)V
    //   5: iconst_0
    //   6: istore_2
    //   7: iconst_0
    //   8: istore_3
    //   9: aload_0
    //   10: invokevirtual getOverScrollMode : ()I
    //   13: istore #4
    //   15: iload #4
    //   17: ifeq -> 66
    //   20: iload #4
    //   22: iconst_1
    //   23: if_icmpne -> 49
    //   26: aload_0
    //   27: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   30: astore #5
    //   32: aload #5
    //   34: ifnull -> 49
    //   37: aload #5
    //   39: invokevirtual getCount : ()I
    //   42: iconst_1
    //   43: if_icmple -> 49
    //   46: goto -> 66
    //   49: aload_0
    //   50: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   53: invokevirtual finish : ()V
    //   56: aload_0
    //   57: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   60: invokevirtual finish : ()V
    //   63: goto -> 257
    //   66: aload_0
    //   67: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   70: invokevirtual isFinished : ()Z
    //   73: ifne -> 155
    //   76: aload_1
    //   77: invokevirtual save : ()I
    //   80: istore_2
    //   81: aload_0
    //   82: invokevirtual getHeight : ()I
    //   85: aload_0
    //   86: invokevirtual getPaddingTop : ()I
    //   89: isub
    //   90: aload_0
    //   91: invokevirtual getPaddingBottom : ()I
    //   94: isub
    //   95: istore #4
    //   97: aload_0
    //   98: invokevirtual getWidth : ()I
    //   101: istore_3
    //   102: aload_1
    //   103: ldc_w 270.0
    //   106: invokevirtual rotate : (F)V
    //   109: aload_1
    //   110: iload #4
    //   112: ineg
    //   113: aload_0
    //   114: invokevirtual getPaddingTop : ()I
    //   117: iadd
    //   118: i2f
    //   119: aload_0
    //   120: getfield mFirstOffset : F
    //   123: iload_3
    //   124: i2f
    //   125: fmul
    //   126: invokevirtual translate : (FF)V
    //   129: aload_0
    //   130: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   133: iload #4
    //   135: iload_3
    //   136: invokevirtual setSize : (II)V
    //   139: iconst_0
    //   140: aload_0
    //   141: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   144: aload_1
    //   145: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   148: ior
    //   149: istore_3
    //   150: aload_1
    //   151: iload_2
    //   152: invokevirtual restoreToCount : (I)V
    //   155: iload_3
    //   156: istore_2
    //   157: aload_0
    //   158: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   161: invokevirtual isFinished : ()Z
    //   164: ifne -> 257
    //   167: aload_1
    //   168: invokevirtual save : ()I
    //   171: istore #4
    //   173: aload_0
    //   174: invokevirtual getWidth : ()I
    //   177: istore #6
    //   179: aload_0
    //   180: invokevirtual getHeight : ()I
    //   183: istore_2
    //   184: aload_0
    //   185: invokevirtual getPaddingTop : ()I
    //   188: istore #7
    //   190: aload_0
    //   191: invokevirtual getPaddingBottom : ()I
    //   194: istore #8
    //   196: aload_1
    //   197: ldc_w 90.0
    //   200: invokevirtual rotate : (F)V
    //   203: aload_1
    //   204: aload_0
    //   205: invokevirtual getPaddingTop : ()I
    //   208: ineg
    //   209: i2f
    //   210: aload_0
    //   211: getfield mLastOffset : F
    //   214: fconst_1
    //   215: fadd
    //   216: fneg
    //   217: iload #6
    //   219: i2f
    //   220: fmul
    //   221: invokevirtual translate : (FF)V
    //   224: aload_0
    //   225: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   228: iload_2
    //   229: iload #7
    //   231: isub
    //   232: iload #8
    //   234: isub
    //   235: iload #6
    //   237: invokevirtual setSize : (II)V
    //   240: iload_3
    //   241: aload_0
    //   242: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   245: aload_1
    //   246: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   249: ior
    //   250: istore_2
    //   251: aload_1
    //   252: iload #4
    //   254: invokevirtual restoreToCount : (I)V
    //   257: iload_2
    //   258: ifeq -> 265
    //   261: aload_0
    //   262: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   265: return
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    Drawable drawable = this.mMarginDrawable;
    if (drawable != null && drawable.isStateful())
      drawable.setState(getDrawableState()); 
  }
  
  public void endFakeDrag() {
    if (this.mFakeDragging) {
      if (this.mAdapter != null) {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int i = (int)velocityTracker.getXVelocity(this.mActivePointerId);
        this.mPopulatePending = true;
        int j = getClientWidth();
        int k = getScrollX();
        ItemInfo itemInfo = infoForCurrentScrollPosition();
        setCurrentItemInternal(determineTargetPage(itemInfo.position, (k / j - itemInfo.offset) / itemInfo.widthFactor, i, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, i);
      } 
      endDrag();
      this.mFakeDragging = false;
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramKeyEvent.getAction() == 0) {
      int i = paramKeyEvent.getKeyCode();
      if (i != 21) {
        if (i != 22) {
          if (i != 61) {
            bool2 = bool1;
          } else if (paramKeyEvent.hasNoModifiers()) {
            bool2 = arrowScroll(2);
          } else {
            bool2 = bool1;
            if (paramKeyEvent.hasModifiers(1))
              bool2 = arrowScroll(1); 
          } 
        } else if (paramKeyEvent.hasModifiers(2)) {
          bool2 = pageRight();
        } else {
          bool2 = arrowScroll(66);
        } 
      } else if (paramKeyEvent.hasModifiers(2)) {
        bool2 = pageLeft();
      } else {
        bool2 = arrowScroll(17);
      } 
    } 
    return bool2;
  }
  
  public void fakeDragBy(float paramFloat) {
    if (this.mFakeDragging) {
      if (this.mAdapter == null)
        return; 
      this.mLastMotionX += paramFloat;
      float f1 = getScrollX() - paramFloat;
      int i = getClientWidth();
      paramFloat = i * this.mFirstOffset;
      float f2 = i * this.mLastOffset;
      ItemInfo itemInfo1 = this.mItems.get(0);
      ArrayList<ItemInfo> arrayList = this.mItems;
      ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
      if (itemInfo1.position != 0)
        paramFloat = itemInfo1.offset * i; 
      if (itemInfo2.position != this.mAdapter.getCount() - 1)
        f2 = itemInfo2.offset * i; 
      if (f1 >= paramFloat) {
        paramFloat = f1;
        if (f1 > f2)
          paramFloat = f2; 
      } 
      this.mLastMotionX += paramFloat - (int)paramFloat;
      scrollTo((int)paramFloat, getScrollY());
      pageScrolled((int)paramFloat);
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
      this.mVelocityTracker.addMovement(motionEvent);
      motionEvent.recycle();
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter() {
    return this.mAdapter;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    if (this.mDrawingOrder == 2)
      paramInt2 = paramInt1 - 1 - paramInt2; 
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(paramInt2)).getLayoutParams()).childIndex;
  }
  
  public int getCurrentItem() {
    return this.mCurItem;
  }
  
  public int getOffscreenPageLimit() {
    return this.mOffscreenPageLimit;
  }
  
  public int getPageMargin() {
    return this.mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView) {
    while (true) {
      ViewParent viewParent = paramView.getParent();
      if (viewParent != this) {
        if (viewParent != null) {
          if (!(viewParent instanceof View))
            return null; 
          paramView = (View)viewParent;
          continue;
        } 
        continue;
      } 
      return infoForChild(paramView);
    } 
  }
  
  ItemInfo infoForChild(View paramView) {
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (this.mAdapter.isViewFromObject(paramView, itemInfo.object))
        return itemInfo; 
    } 
    return null;
  }
  
  ItemInfo infoForPosition(int paramInt) {
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (itemInfo.position == paramInt)
        return itemInfo; 
    } 
    return null;
  }
  
  void initViewPager() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    float f = (context.getResources().getDisplayMetrics()).density;
    this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    this.mMinimumVelocity = (int)(400.0F * f);
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffect(context);
    this.mRightEdge = new EdgeEffect(context);
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(16.0F * f);
    ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          private final Rect mTempRect = new Rect();
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            WindowInsetsCompat windowInsetsCompat = ViewCompat.onApplyWindowInsets(param1View, param1WindowInsetsCompat);
            if (windowInsetsCompat.isConsumed())
              return windowInsetsCompat; 
            Rect rect = this.mTempRect;
            rect.left = windowInsetsCompat.getSystemWindowInsetLeft();
            rect.top = windowInsetsCompat.getSystemWindowInsetTop();
            rect.right = windowInsetsCompat.getSystemWindowInsetRight();
            rect.bottom = windowInsetsCompat.getSystemWindowInsetBottom();
            byte b = 0;
            int i = ViewPager.this.getChildCount();
            while (b < i) {
              param1WindowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(b), windowInsetsCompat);
              rect.left = Math.min(param1WindowInsetsCompat.getSystemWindowInsetLeft(), rect.left);
              rect.top = Math.min(param1WindowInsetsCompat.getSystemWindowInsetTop(), rect.top);
              rect.right = Math.min(param1WindowInsetsCompat.getSystemWindowInsetRight(), rect.right);
              rect.bottom = Math.min(param1WindowInsetsCompat.getSystemWindowInsetBottom(), rect.bottom);
              b++;
            } 
            return windowInsetsCompat.replaceSystemWindowInsets(rect.left, rect.top, rect.right, rect.bottom);
          }
        });
  }
  
  public boolean isFakeDragging() {
    return this.mFakeDragging;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mEndScrollRunnable);
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished())
      this.mScroller.abortAnimation(); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
      int i = getScrollX();
      int j = getWidth();
      float f1 = this.mPageMargin / j;
      byte b = 0;
      ItemInfo itemInfo = this.mItems.get(0);
      float f2 = itemInfo.offset;
      int k = this.mItems.size();
      int m = itemInfo.position;
      int n = ((ItemInfo)this.mItems.get(k - 1)).position;
      while (m < n) {
        ItemInfo itemInfo1;
        float f;
        while (m > itemInfo.position && b < k) {
          ArrayList<ItemInfo> arrayList = this.mItems;
          itemInfo1 = arrayList.get(++b);
        } 
        if (m == itemInfo1.position) {
          f = (itemInfo1.offset + itemInfo1.widthFactor) * j;
          f2 = itemInfo1.offset + itemInfo1.widthFactor + f1;
        } else {
          f = this.mAdapter.getPageWidth(m);
          float f3 = j;
          float f4 = f2 + f + f1;
          f = (f2 + f) * f3;
          f2 = f4;
        } 
        if (this.mPageMargin + f > i) {
          this.mMarginDrawable.setBounds(Math.round(f), this.mTopPageBounds, Math.round(this.mPageMargin + f), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        } 
        if (f > (i + j))
          break; 
        m++;
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 3 || i == 1) {
      resetTouch();
      return false;
    } 
    if (i != 0) {
      if (this.mIsBeingDragged)
        return true; 
      if (this.mIsUnableToDrag)
        return false; 
    } 
    if (i != 0) {
      if (i != 2) {
        if (i == 6)
          onSecondaryPointerUp(paramMotionEvent); 
      } else {
        i = this.mActivePointerId;
        if (i != -1) {
          i = paramMotionEvent.findPointerIndex(i);
          float f1 = paramMotionEvent.getX(i);
          float f2 = f1 - this.mLastMotionX;
          float f3 = Math.abs(f2);
          float f4 = paramMotionEvent.getY(i);
          float f5 = Math.abs(f4 - this.mInitialMotionY);
          if (f2 != 0.0F && !isGutterDrag(this.mLastMotionX, f2) && canScroll((View)this, false, (int)f2, (int)f1, (int)f4)) {
            this.mLastMotionX = f1;
            this.mLastMotionY = f4;
            this.mIsUnableToDrag = true;
            return false;
          } 
          if (f3 > this.mTouchSlop && 0.5F * f3 > f5) {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
            if (f2 > 0.0F) {
              f3 = this.mInitialMotionX + this.mTouchSlop;
            } else {
              f3 = this.mInitialMotionX - this.mTouchSlop;
            } 
            this.mLastMotionX = f3;
            this.mLastMotionY = f4;
            setScrollingCacheEnabled(true);
          } else if (f5 > this.mTouchSlop) {
            this.mIsUnableToDrag = true;
          } 
          if (this.mIsBeingDragged && performDrag(f1))
            ViewCompat.postInvalidateOnAnimation((View)this); 
        } 
      } 
    } else {
      float f = paramMotionEvent.getX();
      this.mInitialMotionX = f;
      this.mLastMotionX = f;
      f = paramMotionEvent.getY();
      this.mInitialMotionY = f;
      this.mLastMotionY = f;
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsUnableToDrag = false;
      this.mIsScrollStarted = true;
      this.mScroller.computeScrollOffset();
      if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        this.mIsBeingDragged = true;
        requestParentDisallowInterceptTouchEvent(true);
        setScrollState(1);
      } else {
        completeScroll(false);
        this.mIsBeingDragged = false;
      } 
    } 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    return this.mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    paramInt1 = getPaddingLeft();
    paramInt2 = getPaddingTop();
    int m = getPaddingRight();
    paramInt4 = getPaddingBottom();
    int n = getScrollX();
    int i1 = 0;
    int i2 = 0;
    while (i2 < i) {
      View view = getChildAt(i2);
      paramInt3 = paramInt1;
      int i4 = paramInt2;
      int i5 = m;
      int i6 = paramInt4;
      int i7 = i1;
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isDecor) {
          paramInt3 = layoutParams.gravity & 0x7;
          i5 = layoutParams.gravity & 0x70;
          if (paramInt3 != 1) {
            if (paramInt3 != 3) {
              if (paramInt3 != 5) {
                paramInt3 = paramInt1;
                i4 = paramInt1;
              } else {
                paramInt3 = j - m - view.getMeasuredWidth();
                m += view.getMeasuredWidth();
                i4 = paramInt1;
              } 
            } else {
              paramInt3 = paramInt1;
              i4 = paramInt1 + view.getMeasuredWidth();
            } 
          } else {
            paramInt3 = Math.max((j - view.getMeasuredWidth()) / 2, paramInt1);
            i4 = paramInt1;
          } 
          if (i5 != 16) {
            if (i5 != 48) {
              if (i5 != 80) {
                paramInt1 = paramInt2;
              } else {
                paramInt1 = k - paramInt4 - view.getMeasuredHeight();
                paramInt4 += view.getMeasuredHeight();
              } 
            } else {
              paramInt1 = paramInt2;
              paramInt2 += view.getMeasuredHeight();
            } 
          } else {
            paramInt1 = Math.max((k - view.getMeasuredHeight()) / 2, paramInt2);
          } 
          paramInt3 += n;
          view.layout(paramInt3, paramInt1, view.getMeasuredWidth() + paramInt3, paramInt1 + view.getMeasuredHeight());
          i7 = i1 + 1;
          paramInt3 = i4;
          i4 = paramInt2;
          i5 = m;
          i6 = paramInt4;
        } else {
          i7 = i1;
          i6 = paramInt4;
          i5 = m;
          i4 = paramInt2;
          paramInt3 = paramInt1;
        } 
      } 
      i2++;
      paramInt1 = paramInt3;
      paramInt2 = i4;
      m = i5;
      paramInt4 = i6;
      i1 = i7;
    } 
    i2 = j - paramInt1 - m;
    m = 0;
    paramInt3 = j;
    int i3 = i;
    while (m < i3) {
      View view = getChildAt(m);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null) {
            int i4 = paramInt1 + (int)(i2 * itemInfo.offset);
            if (layoutParams.needsMeasure) {
              layoutParams.needsMeasure = false;
              view.measure(View.MeasureSpec.makeMeasureSpec((int)(i2 * layoutParams.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(k - paramInt2 - paramInt4, 1073741824));
            } 
            view.layout(i4, paramInt2, view.getMeasuredWidth() + i4, view.getMeasuredHeight() + paramInt2);
          } 
        } 
      } 
      m++;
    } 
    this.mTopPageBounds = paramInt2;
    this.mBottomPageBounds = k - paramInt4;
    this.mDecorChildCount = i1;
    if (this.mFirstLayout)
      scrollToItem(this.mCurItem, false, 0, false); 
    this.mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int i = getMeasuredWidth();
    int j = i / 10;
    this.mGutterSize = Math.min(j, this.mDefaultGutterSize);
    paramInt1 = i - getPaddingLeft() - getPaddingRight();
    paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int k = getChildCount();
    byte b = 0;
    while (b < k) {
      int n;
      int i1;
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams != null && layoutParams.isDecor) {
          boolean bool;
          int i4;
          n = layoutParams.gravity & 0x7;
          int i2 = layoutParams.gravity & 0x70;
          int i3 = Integer.MIN_VALUE;
          i1 = Integer.MIN_VALUE;
          if (i2 == 48 || i2 == 80) {
            i2 = 1;
          } else {
            i2 = 0;
          } 
          if (n == 3 || n == 5) {
            bool = true;
          } else {
            bool = false;
          } 
          if (i2 != 0) {
            n = 1073741824;
          } else {
            n = i3;
            if (bool) {
              i1 = 1073741824;
              n = i3;
            } 
          } 
          if (layoutParams.width != -2) {
            i4 = 1073741824;
            if (layoutParams.width != -1) {
              n = layoutParams.width;
            } else {
              n = paramInt1;
            } 
          } else {
            i3 = paramInt1;
            i4 = n;
            n = i3;
          } 
          if (layoutParams.height != -2) {
            if (layoutParams.height != -1) {
              i3 = layoutParams.height;
              i1 = 1073741824;
            } else {
              i1 = 1073741824;
              i3 = paramInt2;
            } 
          } else {
            i3 = paramInt2;
          } 
          view.measure(View.MeasureSpec.makeMeasureSpec(n, i4), View.MeasureSpec.makeMeasureSpec(i3, i1));
          if (i2 != 0) {
            i1 = paramInt2 - view.getMeasuredHeight();
            n = paramInt1;
          } else {
            n = paramInt1;
            i1 = paramInt2;
            if (bool) {
              n = paramInt1 - view.getMeasuredWidth();
              i1 = paramInt2;
            } 
          } 
        } else {
          n = paramInt1;
          i1 = paramInt2;
        } 
      } else {
        i1 = paramInt2;
        n = paramInt1;
      } 
      b++;
      paramInt1 = n;
      paramInt2 = i1;
    } 
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int m = getChildCount();
    for (paramInt2 = 0; paramInt2 < m; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams == null || !layoutParams.isDecor)
          view.measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * layoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec); 
      } 
    } 
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    if (this.mDecorChildCount > 0) {
      int i = getScrollX();
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = getWidth();
      int n = getChildCount();
      byte b = 0;
      while (b < n) {
        int i1;
        int i2;
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          i1 = j;
          i2 = k;
        } else {
          i2 = layoutParams.gravity & 0x7;
          if (i2 != 1) {
            if (i2 != 3) {
              if (i2 != 5) {
                i2 = j;
              } else {
                i2 = m - k - view.getMeasuredWidth();
                k += view.getMeasuredWidth();
              } 
            } else {
              i2 = j;
              j += view.getWidth();
            } 
          } else {
            i2 = Math.max((m - view.getMeasuredWidth()) / 2, j);
          } 
          int i3 = i2 + i - view.getLeft();
          i1 = j;
          i2 = k;
          if (i3 != 0) {
            view.offsetLeftAndRight(i3);
            i2 = k;
            i1 = j;
          } 
        } 
        b++;
        j = i1;
        k = i2;
      } 
    } 
    dispatchOnPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mPageTransformer != null) {
      int i = getScrollX();
      paramInt2 = getChildCount();
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        View view = getChildAt(paramInt1);
        if (!((LayoutParams)view.getLayoutParams()).isDecor) {
          paramFloat = (view.getLeft() - i) / getClientWidth();
          this.mPageTransformer.transformPage(view, paramFloat);
        } 
      } 
    } 
    this.mCalledSuper = true;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    int j;
    byte b;
    int i = getChildCount();
    if ((paramInt & 0x2) != 0) {
      j = 0;
      b = 1;
    } else {
      j = i - 1;
      b = -1;
      i = -1;
    } 
    while (j != i) {
      View view = getChildAt(j);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.requestFocus(paramInt, paramRect))
          return true; 
      } 
      j += b;
    } 
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    paramParcelable = paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      pagerAdapter.restoreState(((SavedState)paramParcelable).adapterState, ((SavedState)paramParcelable).loader);
      setCurrentItemInternal(((SavedState)paramParcelable).position, false, true);
    } else {
      this.mRestoredCurItem = ((SavedState)paramParcelable).position;
      this.mRestoredAdapterState = ((SavedState)paramParcelable).adapterState;
      this.mRestoredClassLoader = ((SavedState)paramParcelable).loader;
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.position = this.mCurItem;
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null)
      savedState.adapterState = pagerAdapter.saveState(); 
    return savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      paramInt2 = this.mPageMargin;
      recomputeScrollPosition(paramInt1, paramInt3, paramInt2, paramInt2);
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mFakeDragging : Z
    //   4: ifeq -> 9
    //   7: iconst_1
    //   8: ireturn
    //   9: aload_1
    //   10: invokevirtual getAction : ()I
    //   13: ifne -> 25
    //   16: aload_1
    //   17: invokevirtual getEdgeFlags : ()I
    //   20: ifeq -> 25
    //   23: iconst_0
    //   24: ireturn
    //   25: aload_0
    //   26: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   29: astore_2
    //   30: aload_2
    //   31: ifnull -> 606
    //   34: aload_2
    //   35: invokevirtual getCount : ()I
    //   38: ifne -> 44
    //   41: goto -> 606
    //   44: aload_0
    //   45: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   48: ifnonnull -> 58
    //   51: aload_0
    //   52: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   55: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   58: aload_0
    //   59: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   62: aload_1
    //   63: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   66: aload_1
    //   67: invokevirtual getAction : ()I
    //   70: istore_3
    //   71: iconst_0
    //   72: istore #4
    //   74: iload_3
    //   75: sipush #255
    //   78: iand
    //   79: istore_3
    //   80: iload_3
    //   81: ifeq -> 534
    //   84: iload_3
    //   85: iconst_1
    //   86: if_icmpeq -> 400
    //   89: iload_3
    //   90: iconst_2
    //   91: if_icmpeq -> 193
    //   94: iload_3
    //   95: iconst_3
    //   96: if_icmpeq -> 163
    //   99: iload_3
    //   100: iconst_5
    //   101: if_icmpeq -> 137
    //   104: iload_3
    //   105: bipush #6
    //   107: if_icmpeq -> 113
    //   110: goto -> 595
    //   113: aload_0
    //   114: aload_1
    //   115: invokespecial onSecondaryPointerUp : (Landroid/view/MotionEvent;)V
    //   118: aload_0
    //   119: aload_1
    //   120: aload_1
    //   121: aload_0
    //   122: getfield mActivePointerId : I
    //   125: invokevirtual findPointerIndex : (I)I
    //   128: invokevirtual getX : (I)F
    //   131: putfield mLastMotionX : F
    //   134: goto -> 595
    //   137: aload_1
    //   138: invokevirtual getActionIndex : ()I
    //   141: istore_3
    //   142: aload_0
    //   143: aload_1
    //   144: iload_3
    //   145: invokevirtual getX : (I)F
    //   148: putfield mLastMotionX : F
    //   151: aload_0
    //   152: aload_1
    //   153: iload_3
    //   154: invokevirtual getPointerId : (I)I
    //   157: putfield mActivePointerId : I
    //   160: goto -> 595
    //   163: aload_0
    //   164: getfield mIsBeingDragged : Z
    //   167: ifeq -> 190
    //   170: aload_0
    //   171: aload_0
    //   172: getfield mCurItem : I
    //   175: iconst_1
    //   176: iconst_0
    //   177: iconst_0
    //   178: invokespecial scrollToItem : (IZIZ)V
    //   181: aload_0
    //   182: invokespecial resetTouch : ()Z
    //   185: istore #4
    //   187: goto -> 595
    //   190: goto -> 595
    //   193: aload_0
    //   194: getfield mIsBeingDragged : Z
    //   197: ifne -> 367
    //   200: aload_1
    //   201: aload_0
    //   202: getfield mActivePointerId : I
    //   205: invokevirtual findPointerIndex : (I)I
    //   208: istore_3
    //   209: iload_3
    //   210: iconst_m1
    //   211: if_icmpne -> 223
    //   214: aload_0
    //   215: invokespecial resetTouch : ()Z
    //   218: istore #4
    //   220: goto -> 595
    //   223: aload_1
    //   224: iload_3
    //   225: invokevirtual getX : (I)F
    //   228: fstore #5
    //   230: fload #5
    //   232: aload_0
    //   233: getfield mLastMotionX : F
    //   236: fsub
    //   237: invokestatic abs : (F)F
    //   240: fstore #6
    //   242: aload_1
    //   243: iload_3
    //   244: invokevirtual getY : (I)F
    //   247: fstore #7
    //   249: fload #7
    //   251: aload_0
    //   252: getfield mLastMotionY : F
    //   255: fsub
    //   256: invokestatic abs : (F)F
    //   259: fstore #8
    //   261: fload #6
    //   263: aload_0
    //   264: getfield mTouchSlop : I
    //   267: i2f
    //   268: fcmpl
    //   269: ifle -> 367
    //   272: fload #6
    //   274: fload #8
    //   276: fcmpl
    //   277: ifle -> 367
    //   280: aload_0
    //   281: iconst_1
    //   282: putfield mIsBeingDragged : Z
    //   285: aload_0
    //   286: iconst_1
    //   287: invokespecial requestParentDisallowInterceptTouchEvent : (Z)V
    //   290: aload_0
    //   291: getfield mInitialMotionX : F
    //   294: fstore #8
    //   296: fload #5
    //   298: fload #8
    //   300: fsub
    //   301: fconst_0
    //   302: fcmpl
    //   303: ifle -> 319
    //   306: fload #8
    //   308: aload_0
    //   309: getfield mTouchSlop : I
    //   312: i2f
    //   313: fadd
    //   314: fstore #5
    //   316: goto -> 329
    //   319: fload #8
    //   321: aload_0
    //   322: getfield mTouchSlop : I
    //   325: i2f
    //   326: fsub
    //   327: fstore #5
    //   329: aload_0
    //   330: fload #5
    //   332: putfield mLastMotionX : F
    //   335: aload_0
    //   336: fload #7
    //   338: putfield mLastMotionY : F
    //   341: aload_0
    //   342: iconst_1
    //   343: invokevirtual setScrollState : (I)V
    //   346: aload_0
    //   347: iconst_1
    //   348: invokespecial setScrollingCacheEnabled : (Z)V
    //   351: aload_0
    //   352: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   355: astore_2
    //   356: aload_2
    //   357: ifnull -> 367
    //   360: aload_2
    //   361: iconst_1
    //   362: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   367: aload_0
    //   368: getfield mIsBeingDragged : Z
    //   371: ifeq -> 397
    //   374: iconst_0
    //   375: aload_0
    //   376: aload_1
    //   377: aload_1
    //   378: aload_0
    //   379: getfield mActivePointerId : I
    //   382: invokevirtual findPointerIndex : (I)I
    //   385: invokevirtual getX : (I)F
    //   388: invokespecial performDrag : (F)Z
    //   391: ior
    //   392: istore #4
    //   394: goto -> 595
    //   397: goto -> 595
    //   400: aload_0
    //   401: getfield mIsBeingDragged : Z
    //   404: ifeq -> 531
    //   407: aload_0
    //   408: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   411: astore_2
    //   412: aload_2
    //   413: sipush #1000
    //   416: aload_0
    //   417: getfield mMaximumVelocity : I
    //   420: i2f
    //   421: invokevirtual computeCurrentVelocity : (IF)V
    //   424: aload_2
    //   425: aload_0
    //   426: getfield mActivePointerId : I
    //   429: invokevirtual getXVelocity : (I)F
    //   432: f2i
    //   433: istore #9
    //   435: aload_0
    //   436: iconst_1
    //   437: putfield mPopulatePending : Z
    //   440: aload_0
    //   441: invokespecial getClientWidth : ()I
    //   444: istore #10
    //   446: aload_0
    //   447: invokevirtual getScrollX : ()I
    //   450: istore_3
    //   451: aload_0
    //   452: invokespecial infoForCurrentScrollPosition : ()Landroid/support/v4/view/ViewPager$ItemInfo;
    //   455: astore_2
    //   456: aload_0
    //   457: getfield mPageMargin : I
    //   460: i2f
    //   461: iload #10
    //   463: i2f
    //   464: fdiv
    //   465: fstore #5
    //   467: aload_0
    //   468: aload_0
    //   469: aload_2
    //   470: getfield position : I
    //   473: iload_3
    //   474: i2f
    //   475: iload #10
    //   477: i2f
    //   478: fdiv
    //   479: aload_2
    //   480: getfield offset : F
    //   483: fsub
    //   484: aload_2
    //   485: getfield widthFactor : F
    //   488: fload #5
    //   490: fadd
    //   491: fdiv
    //   492: iload #9
    //   494: aload_1
    //   495: aload_1
    //   496: aload_0
    //   497: getfield mActivePointerId : I
    //   500: invokevirtual findPointerIndex : (I)I
    //   503: invokevirtual getX : (I)F
    //   506: aload_0
    //   507: getfield mInitialMotionX : F
    //   510: fsub
    //   511: f2i
    //   512: invokespecial determineTargetPage : (IFII)I
    //   515: iconst_1
    //   516: iconst_1
    //   517: iload #9
    //   519: invokevirtual setCurrentItemInternal : (IZZI)V
    //   522: aload_0
    //   523: invokespecial resetTouch : ()Z
    //   526: istore #4
    //   528: goto -> 595
    //   531: goto -> 595
    //   534: aload_0
    //   535: getfield mScroller : Landroid/widget/Scroller;
    //   538: invokevirtual abortAnimation : ()V
    //   541: aload_0
    //   542: iconst_0
    //   543: putfield mPopulatePending : Z
    //   546: aload_0
    //   547: invokevirtual populate : ()V
    //   550: aload_1
    //   551: invokevirtual getX : ()F
    //   554: fstore #5
    //   556: aload_0
    //   557: fload #5
    //   559: putfield mInitialMotionX : F
    //   562: aload_0
    //   563: fload #5
    //   565: putfield mLastMotionX : F
    //   568: aload_1
    //   569: invokevirtual getY : ()F
    //   572: fstore #5
    //   574: aload_0
    //   575: fload #5
    //   577: putfield mInitialMotionY : F
    //   580: aload_0
    //   581: fload #5
    //   583: putfield mLastMotionY : F
    //   586: aload_0
    //   587: aload_1
    //   588: iconst_0
    //   589: invokevirtual getPointerId : (I)I
    //   592: putfield mActivePointerId : I
    //   595: iload #4
    //   597: ifeq -> 604
    //   600: aload_0
    //   601: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   604: iconst_1
    //   605: ireturn
    //   606: iconst_0
    //   607: ireturn
  }
  
  boolean pageLeft() {
    int i = this.mCurItem;
    if (i > 0) {
      setCurrentItem(i - 1, true);
      return true;
    } 
    return false;
  }
  
  boolean pageRight() {
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null && this.mCurItem < pagerAdapter.getCount() - 1) {
      setCurrentItem(this.mCurItem + 1, true);
      return true;
    } 
    return false;
  }
  
  void populate() {
    populate(this.mCurItem);
  }
  
  void populate(int paramInt) {
    ItemInfo itemInfo;
    String str;
    int i = this.mCurItem;
    if (i != paramInt) {
      itemInfo = infoForPosition(i);
      this.mCurItem = paramInt;
    } else {
      itemInfo = null;
    } 
    if (this.mAdapter == null) {
      sortChildDrawingOrder();
      return;
    } 
    if (this.mPopulatePending) {
      sortChildDrawingOrder();
      return;
    } 
    if (getWindowToken() == null)
      return; 
    this.mAdapter.startUpdate(this);
    int j = this.mOffscreenPageLimit;
    int k = Math.max(0, this.mCurItem - j);
    int m = this.mAdapter.getCount();
    int n = Math.min(m - 1, this.mCurItem + j);
    if (m == this.mExpectedAdapterCount) {
      ItemInfo itemInfo2;
      ItemInfo itemInfo1 = null;
      paramInt = 0;
      while (true) {
        itemInfo2 = itemInfo1;
        if (paramInt < this.mItems.size()) {
          ItemInfo itemInfo3 = this.mItems.get(paramInt);
          if (itemInfo3.position >= this.mCurItem) {
            itemInfo2 = itemInfo1;
            if (itemInfo3.position == this.mCurItem)
              itemInfo2 = itemInfo3; 
            break;
          } 
          paramInt++;
          continue;
        } 
        break;
      } 
      itemInfo1 = itemInfo2;
      if (itemInfo2 == null) {
        itemInfo1 = itemInfo2;
        if (m > 0)
          itemInfo1 = addNewItem(this.mCurItem, paramInt); 
      } 
      if (itemInfo1 != null) {
        float f2;
        float f1 = 0.0F;
        int i1 = paramInt - 1;
        if (i1 >= 0) {
          itemInfo2 = this.mItems.get(i1);
        } else {
          itemInfo2 = null;
        } 
        int i2 = getClientWidth();
        if (i2 <= 0) {
          f2 = 0.0F;
        } else {
          f2 = 2.0F - itemInfo1.widthFactor + getPaddingLeft() / i2;
        } 
        int i3 = this.mCurItem - 1;
        ItemInfo itemInfo3 = itemInfo2;
        int i4 = paramInt;
        while (i3 >= 0) {
          float f;
          if (f1 >= f2 && i3 < k) {
            if (itemInfo3 == null)
              break; 
            paramInt = i4;
            f = f1;
            i = i1;
            itemInfo2 = itemInfo3;
            if (i3 == itemInfo3.position) {
              paramInt = i4;
              f = f1;
              i = i1;
              itemInfo2 = itemInfo3;
              if (!itemInfo3.scrolling) {
                this.mItems.remove(i1);
                this.mAdapter.destroyItem(this, i3, itemInfo3.object);
                i = i1 - 1;
                paramInt = i4 - 1;
                if (i >= 0) {
                  itemInfo2 = this.mItems.get(i);
                } else {
                  itemInfo2 = null;
                } 
                f = f1;
              } 
            } 
          } else if (itemInfo3 != null && i3 == itemInfo3.position) {
            f = f1 + itemInfo3.widthFactor;
            i = i1 - 1;
            if (i >= 0) {
              itemInfo2 = this.mItems.get(i);
            } else {
              itemInfo2 = null;
            } 
            paramInt = i4;
          } else {
            f = f1 + (addNewItem(i3, i1 + 1)).widthFactor;
            paramInt = i4 + 1;
            if (i1 >= 0) {
              itemInfo2 = this.mItems.get(i1);
            } else {
              itemInfo2 = null;
            } 
            i = i1;
          } 
          i3--;
          i4 = paramInt;
          f1 = f;
          i1 = i;
          itemInfo3 = itemInfo2;
        } 
        float f3 = itemInfo1.widthFactor;
        paramInt = i4 + 1;
        if (f3 < 2.0F) {
          if (paramInt < this.mItems.size()) {
            itemInfo2 = this.mItems.get(paramInt);
          } else {
            itemInfo2 = null;
          } 
          if (i2 <= 0) {
            f2 = 0.0F;
          } else {
            f2 = getPaddingRight() / i2 + 2.0F;
          } 
          i3 = this.mCurItem + 1;
          i = k;
          i1 = j;
          while (i3 < m) {
            if (f3 >= f2 && i3 > n) {
              if (itemInfo2 == null)
                break; 
              if (i3 == itemInfo2.position && !itemInfo2.scrolling) {
                this.mItems.remove(paramInt);
                this.mAdapter.destroyItem(this, i3, itemInfo2.object);
                if (paramInt < this.mItems.size()) {
                  itemInfo2 = this.mItems.get(paramInt);
                } else {
                  itemInfo2 = null;
                } 
              } 
            } else if (itemInfo2 != null && i3 == itemInfo2.position) {
              f3 += itemInfo2.widthFactor;
              if (++paramInt < this.mItems.size()) {
                itemInfo2 = this.mItems.get(paramInt);
              } else {
                itemInfo2 = null;
              } 
            } else {
              itemInfo2 = addNewItem(i3, paramInt);
              paramInt++;
              f3 += itemInfo2.widthFactor;
              if (paramInt < this.mItems.size()) {
                itemInfo2 = this.mItems.get(paramInt);
              } else {
                itemInfo2 = null;
              } 
            } 
            i3++;
          } 
        } 
        calculatePageOffsets(itemInfo1, i4, itemInfo);
        this.mAdapter.setPrimaryItem(this, this.mCurItem, itemInfo1.object);
      } 
      this.mAdapter.finishUpdate(this);
      i = getChildCount();
      for (paramInt = 0; paramInt < i; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.childIndex = paramInt;
        if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0F) {
          ItemInfo itemInfo3 = infoForChild(view);
          if (itemInfo3 != null) {
            layoutParams.widthFactor = itemInfo3.widthFactor;
            layoutParams.position = itemInfo3.position;
          } 
        } 
      } 
      sortChildDrawingOrder();
      if (hasFocus()) {
        View view = findFocus();
        if (view != null) {
          ItemInfo itemInfo3 = infoForAnyChild(view);
        } else {
          view = null;
        } 
        if (view == null || ((ItemInfo)view).position != this.mCurItem)
          for (paramInt = 0; paramInt < getChildCount(); paramInt++) {
            view = getChildAt(paramInt);
            itemInfo = infoForChild(view);
            if (itemInfo != null && itemInfo.position == this.mCurItem && view.requestFocus(2))
              break; 
          }  
      } 
      return;
    } 
    try {
      str = getResources().getResourceName(getId());
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      str = Integer.toHexString(getId());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
    stringBuilder.append(this.mExpectedAdapterCount);
    stringBuilder.append(", found: ");
    stringBuilder.append(m);
    stringBuilder.append(" Pager id: ");
    stringBuilder.append(str);
    stringBuilder.append(" Pager class: ");
    stringBuilder.append(getClass());
    stringBuilder.append(" Problematic adapter: ");
    stringBuilder.append(this.mAdapter.getClass());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void removeOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
    if (list != null)
      list.remove(paramOnAdapterChangeListener); 
  }
  
  public void removeOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null)
      list.remove(paramOnPageChangeListener); 
  }
  
  public void removeView(View paramView) {
    if (this.mInLayout) {
      removeViewInLayout(paramView);
    } else {
      super.removeView(paramView);
    } 
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter) {
    PagerAdapter pagerAdapter1 = this.mAdapter;
    if (pagerAdapter1 != null) {
      pagerAdapter1.setViewPagerObserver(null);
      this.mAdapter.startUpdate(this);
      for (byte b = 0; b < this.mItems.size(); b++) {
        ItemInfo itemInfo = this.mItems.get(b);
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
      } 
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    } 
    PagerAdapter pagerAdapter2 = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    this.mExpectedAdapterCount = 0;
    if (paramPagerAdapter != null) {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(); 
      this.mAdapter.setViewPagerObserver(this.mObserver);
      this.mPopulatePending = false;
      boolean bool = this.mFirstLayout;
      this.mFirstLayout = true;
      this.mExpectedAdapterCount = this.mAdapter.getCount();
      if (this.mRestoredCurItem >= 0) {
        this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
        setCurrentItemInternal(this.mRestoredCurItem, false, true);
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
      } else if (!bool) {
        populate();
      } else {
        requestLayout();
      } 
    } 
    List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
    if (list != null && !list.isEmpty()) {
      byte b = 0;
      int i = this.mAdapterChangeListeners.size();
      while (b < i) {
        ((OnAdapterChangeListener)this.mAdapterChangeListeners.get(b)).onAdapterChanged(this, pagerAdapter2, paramPagerAdapter);
        b++;
      } 
    } 
  }
  
  public void setCurrentItem(int paramInt) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, this.mFirstLayout ^ true, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2) {
    int i;
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool = false;
    if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (!paramBoolean2 && this.mCurItem == paramInt1 && this.mItems.size() != 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (paramInt1 < 0) {
      i = 0;
    } else {
      i = paramInt1;
      if (paramInt1 >= this.mAdapter.getCount())
        i = this.mAdapter.getCount() - 1; 
    } 
    int j = this.mOffscreenPageLimit;
    paramInt1 = this.mCurItem;
    if (i > paramInt1 + j || i < paramInt1 - j)
      for (paramInt1 = 0; paramInt1 < this.mItems.size(); paramInt1++)
        ((ItemInfo)this.mItems.get(paramInt1)).scrolling = true;  
    paramBoolean2 = bool;
    if (this.mCurItem != i)
      paramBoolean2 = true; 
    if (this.mFirstLayout) {
      this.mCurItem = i;
      if (paramBoolean2)
        dispatchOnPageSelected(i); 
      requestLayout();
    } else {
      populate(i);
      scrollToItem(i, paramBoolean1, paramInt2, paramBoolean2);
    } 
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    int i = paramInt;
    if (paramInt < 1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Requested offscreen page limit ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" too small; defaulting to ");
      stringBuilder.append(1);
      Log.w("ViewPager", stringBuilder.toString());
      i = 1;
    } 
    if (i != this.mOffscreenPageLimit) {
      this.mOffscreenPageLimit = i;
      populate();
    } 
  }
  
  @Deprecated
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt) {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt) {
    setPageMarginDrawable(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable) {
    boolean bool;
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState(); 
    if (paramDrawable == null) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    invalidate();
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer) {
    setPageTransformer(paramBoolean, paramPageTransformer, 2);
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer, int paramInt) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    byte b = 1;
    if (paramPageTransformer != null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (this.mPageTransformer != null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool1 != bool2) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    this.mPageTransformer = paramPageTransformer;
    setChildrenDrawingOrderEnabled(bool1);
    if (bool1) {
      if (paramBoolean)
        b = 2; 
      this.mDrawingOrder = b;
      this.mPageTransformerLayerType = paramInt;
    } else {
      this.mDrawingOrder = 0;
    } 
    if (bool3)
      populate(); 
  }
  
  void setScrollState(int paramInt) {
    if (this.mScrollState == paramInt)
      return; 
    this.mScrollState = paramInt;
    if (this.mPageTransformer != null) {
      boolean bool;
      if (paramInt != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      enableLayers(bool);
    } 
    dispatchOnScrollStateChanged(paramInt);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      if (this.mIsScrollStarted) {
        i = this.mScroller.getCurrX();
      } else {
        i = this.mScroller.getStartX();
      } 
      this.mScroller.abortAnimation();
      setScrollingCacheEnabled(false);
    } else {
      i = getScrollX();
    } 
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if (k == 0 && paramInt2 == 0) {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    } 
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getClientWidth();
    int m = paramInt1 / 2;
    float f1 = Math.min(1.0F, Math.abs(k) * 1.0F / paramInt1);
    float f2 = m;
    float f3 = m;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {
      paramInt1 = Math.round(Math.abs((f2 + f3 * f1) / paramInt3) * 1000.0F) * 4;
    } else {
      f2 = paramInt1;
      f3 = this.mAdapter.getPageWidth(this.mCurItem);
      paramInt1 = (int)((1.0F + Math.abs(k) / (this.mPageMargin + f2 * f3)) * 100.0F);
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mIsScrollStarted = false;
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mMarginDrawable);
  }
  
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface DecorView {}
  
  static class ItemInfo {
    Object object;
    
    float offset;
    
    int position;
    
    boolean scrolling;
    
    float widthFactor;
  }
  
  public static class LayoutParams extends ViewGroup.LayoutParams {
    int childIndex;
    
    public int gravity;
    
    public boolean isDecor;
    
    boolean needsMeasure;
    
    int position;
    
    float widthFactor = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = typedArray.getInteger(0, 48);
      typedArray.recycle();
    }
  }
  
  class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
    private boolean canScroll() {
      PagerAdapter pagerAdapter = ViewPager.this.mAdapter;
      boolean bool = true;
      if (pagerAdapter == null || ViewPager.this.mAdapter.getCount() <= 1)
        bool = false; 
      return bool;
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ViewPager.class.getName());
      param1AccessibilityEvent.setScrollable(canScroll());
      if (param1AccessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
        param1AccessibilityEvent.setItemCount(ViewPager.this.mAdapter.getCount());
        param1AccessibilityEvent.setFromIndex(ViewPager.this.mCurItem);
        param1AccessibilityEvent.setToIndex(ViewPager.this.mCurItem);
      } 
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      param1AccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      param1AccessibilityNodeInfoCompat.setScrollable(canScroll());
      if (ViewPager.this.canScrollHorizontally(1))
        param1AccessibilityNodeInfoCompat.addAction(4096); 
      if (ViewPager.this.canScrollHorizontally(-1))
        param1AccessibilityNodeInfoCompat.addAction(8192); 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (super.performAccessibilityAction(param1View, param1Int, param1Bundle))
        return true; 
      if (param1Int != 4096) {
        if (param1Int != 8192)
          return false; 
        if (ViewPager.this.canScrollHorizontally(-1)) {
          ViewPager viewPager = ViewPager.this;
          viewPager.setCurrentItem(viewPager.mCurItem - 1);
          return true;
        } 
        return false;
      } 
      if (ViewPager.this.canScrollHorizontally(1)) {
        ViewPager viewPager = ViewPager.this;
        viewPager.setCurrentItem(viewPager.mCurItem + 1);
        return true;
      } 
      return false;
    }
  }
  
  public static interface OnAdapterChangeListener {
    void onAdapterChanged(ViewPager param1ViewPager, PagerAdapter param1PagerAdapter1, PagerAdapter param1PagerAdapter2);
  }
  
  public static interface OnPageChangeListener {
    void onPageScrollStateChanged(int param1Int);
    
    void onPageScrolled(int param1Int1, float param1Float, int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  public static interface PageTransformer {
    void transformPage(View param1View, float param1Float);
  }
  
  private class PagerObserver extends DataSetObserver {
    public void onChanged() {
      ViewPager.this.dataSetChanged();
    }
    
    public void onInvalidated() {
      ViewPager.this.dataSetChanged();
    }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel) {
          return new ViewPager.SavedState(param2Parcel, null);
        }
        
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new ViewPager.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public ViewPager.SavedState[] newArray(int param2Int) {
          return new ViewPager.SavedState[param2Int];
        }
      };
    
    Parcelable adapterState;
    
    ClassLoader loader;
    
    int position;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      ClassLoader classLoader = param1ClassLoader;
      if (param1ClassLoader == null)
        classLoader = getClass().getClassLoader(); 
      this.position = param1Parcel.readInt();
      this.adapterState = param1Parcel.readParcelable(classLoader);
      this.loader = classLoader;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("FragmentPager.SavedState{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append(" position=");
      stringBuilder.append(this.position);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.position);
      param1Parcel.writeParcelable(this.adapterState, param1Int);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel) {
      return new ViewPager.SavedState(param1Parcel, null);
    }
    
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new ViewPager.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public ViewPager.SavedState[] newArray(int param1Int) {
      return new ViewPager.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
  
  static class ViewPositionComparator implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      ViewPager.LayoutParams layoutParams1 = (ViewPager.LayoutParams)param1View1.getLayoutParams();
      ViewPager.LayoutParams layoutParams2 = (ViewPager.LayoutParams)param1View2.getLayoutParams();
      if (layoutParams1.isDecor != layoutParams2.isDecor) {
        byte b;
        if (layoutParams1.isDecor) {
          b = 1;
        } else {
          b = -1;
        } 
        return b;
      } 
      return layoutParams1.position - layoutParams2.position;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/view/ViewPager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */