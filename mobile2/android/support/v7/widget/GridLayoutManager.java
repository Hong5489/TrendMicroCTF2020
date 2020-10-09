package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
  private static final boolean DEBUG = false;
  
  public static final int DEFAULT_SPAN_COUNT = -1;
  
  private static final String TAG = "GridLayoutManager";
  
  int[] mCachedBorders;
  
  final Rect mDecorInsets = new Rect();
  
  boolean mPendingSpanCountChange = false;
  
  final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
  
  final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
  
  View[] mSet;
  
  int mSpanCount = -1;
  
  SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
  
  public GridLayoutManager(Context paramContext, int paramInt) {
    super(paramContext);
    setSpanCount(paramInt);
  }
  
  public GridLayoutManager(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean) {
    super(paramContext, paramInt2, paramBoolean);
    setSpanCount(paramInt1);
  }
  
  public GridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setSpanCount((getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2)).spanCount);
  }
  
  private void assignSpans(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      boolean bool = false;
      paramInt2 = paramInt1;
      b = 1;
      paramInt1 = bool;
    } else {
      paramInt1--;
      paramInt2 = -1;
      b = -1;
    } 
    int i = 0;
    while (paramInt1 != paramInt2) {
      View view = this.mSet[paramInt1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      layoutParams.mSpanSize = getSpanSize(paramRecycler, paramState, getPosition(view));
      layoutParams.mSpanIndex = i;
      i += layoutParams.mSpanSize;
      paramInt1 += b;
    } 
  }
  
  private void cachePreLayoutSpanMapping() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.getViewLayoutPosition();
      this.mPreLayoutSpanSizeCache.put(j, layoutParams.getSpanSize());
      this.mPreLayoutSpanIndexCache.put(j, layoutParams.getSpanIndex());
    } 
  }
  
  private void calculateItemBorders(int paramInt) {
    this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, paramInt);
  }
  
  static int[] calculateItemBorders(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 24
    //   4: aload_0
    //   5: arraylength
    //   6: iload_1
    //   7: iconst_1
    //   8: iadd
    //   9: if_icmpne -> 24
    //   12: aload_0
    //   13: astore_3
    //   14: aload_0
    //   15: aload_0
    //   16: arraylength
    //   17: iconst_1
    //   18: isub
    //   19: iaload
    //   20: iload_2
    //   21: if_icmpeq -> 30
    //   24: iload_1
    //   25: iconst_1
    //   26: iadd
    //   27: newarray int
    //   29: astore_3
    //   30: aload_3
    //   31: iconst_0
    //   32: iconst_0
    //   33: iastore
    //   34: iload_2
    //   35: iload_1
    //   36: idiv
    //   37: istore #4
    //   39: iload_2
    //   40: iload_1
    //   41: irem
    //   42: istore #5
    //   44: iconst_0
    //   45: istore #6
    //   47: iconst_0
    //   48: istore_2
    //   49: iconst_1
    //   50: istore #7
    //   52: iload #7
    //   54: iload_1
    //   55: if_icmpgt -> 126
    //   58: iload #4
    //   60: istore #8
    //   62: iload_2
    //   63: iload #5
    //   65: iadd
    //   66: istore #9
    //   68: iload #9
    //   70: istore_2
    //   71: iload #8
    //   73: istore #10
    //   75: iload #9
    //   77: ifle -> 107
    //   80: iload #9
    //   82: istore_2
    //   83: iload #8
    //   85: istore #10
    //   87: iload_1
    //   88: iload #9
    //   90: isub
    //   91: iload #5
    //   93: if_icmpge -> 107
    //   96: iload #8
    //   98: iconst_1
    //   99: iadd
    //   100: istore #10
    //   102: iload #9
    //   104: iload_1
    //   105: isub
    //   106: istore_2
    //   107: iload #6
    //   109: iload #10
    //   111: iadd
    //   112: istore #6
    //   114: aload_3
    //   115: iload #7
    //   117: iload #6
    //   119: iastore
    //   120: iinc #7, 1
    //   123: goto -> 52
    //   126: aload_3
    //   127: areturn
  }
  
  private void clearPreLayoutSpanMappingCache() {
    this.mPreLayoutSpanSizeCache.clear();
    this.mPreLayoutSpanIndexCache.clear();
  }
  
  private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    int i;
    if (paramInt == 1) {
      i = 1;
    } else {
      i = 0;
    } 
    paramInt = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
    if (i) {
      while (paramInt > 0 && paramAnchorInfo.mPosition > 0) {
        paramAnchorInfo.mPosition--;
        paramInt = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
      } 
    } else {
      int j = paramState.getItemCount();
      i = paramAnchorInfo.mPosition;
      while (i < j - 1) {
        int k = getSpanIndex(paramRecycler, paramState, i + 1);
        if (k > paramInt) {
          i++;
          paramInt = k;
        } 
      } 
      paramAnchorInfo.mPosition = i;
    } 
  }
  
  private void ensureViewSet() {
    View[] arrayOfView = this.mSet;
    if (arrayOfView == null || arrayOfView.length != this.mSpanCount)
      this.mSet = new View[this.mSpanCount]; 
  }
  
  private int getSpanGroupIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanGroupIndex(paramInt, this.mSpanCount); 
    int i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. ");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getSpanGroupIndex(i, this.mSpanCount);
  }
  
  private int getSpanIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanIndex(paramInt, this.mSpanCount); 
    int i = this.mPreLayoutSpanIndexCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
  }
  
  private int getSpanSize(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanSize(paramInt); 
    int i = this.mPreLayoutSpanSizeCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 1;
    } 
    return this.mSpanSizeLookup.getSpanSize(i);
  }
  
  private void guessMeasurement(float paramFloat, int paramInt) {
    calculateItemBorders(Math.max(Math.round(this.mSpanCount * paramFloat), paramInt));
  }
  
  private void measureChild(View paramView, int paramInt, boolean paramBoolean) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    int i = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
    int j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
    int k = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
    if (this.mOrientation == 1) {
      paramInt = getChildMeasureSpec(k, paramInt, j, layoutParams.width, false);
      i = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i, layoutParams.height, true);
    } else {
      i = getChildMeasureSpec(k, paramInt, i, layoutParams.height, false);
      paramInt = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), j, layoutParams.width, true);
    } 
    measureChildWithDecorationsAndMargin(paramView, paramInt, i, paramBoolean);
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void updateMeasurements() {
    int i;
    if (getOrientation() == 1) {
      i = getWidth() - getPaddingRight() - getPaddingLeft();
    } else {
      i = getHeight() - getPaddingBottom() - getPaddingTop();
    } 
    calculateItemBorders(i);
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = this.mSpanCount;
    for (byte b = 0; b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0; b++) {
      int j = paramLayoutState.mCurrentPosition;
      paramLayoutPrefetchRegistry.addPosition(j, Math.max(0, paramLayoutState.mScrollingOffset));
      i -= this.mSpanSizeLookup.getSpanSize(j);
      paramLayoutState.mCurrentPosition += paramLayoutState.mItemDirection;
    } 
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    ensureLayoutState();
    View view1 = null;
    View view2 = null;
    int i = this.mOrientationHelper.getStartAfterPadding();
    int j = this.mOrientationHelper.getEndAfterPadding();
    if (paramInt2 > paramInt1) {
      b = 1;
    } else {
      b = -1;
    } 
    while (paramInt1 != paramInt2) {
      View view3 = getChildAt(paramInt1);
      int k = getPosition(view3);
      View view4 = view1;
      View view5 = view2;
      if (k >= 0) {
        view4 = view1;
        view5 = view2;
        if (k < paramInt3)
          if (getSpanIndex(paramRecycler, paramState, k) != 0) {
            view4 = view1;
            view5 = view2;
          } else if (((RecyclerView.LayoutParams)view3.getLayoutParams()).isItemRemoved()) {
            view4 = view1;
            view5 = view2;
            if (view1 == null) {
              view4 = view3;
              view5 = view2;
            } 
          } else if (this.mOrientationHelper.getDecoratedStart(view3) >= j || this.mOrientationHelper.getDecoratedEnd(view3) < i) {
            view4 = view1;
            view5 = view2;
            if (view2 == null) {
              view5 = view3;
              view4 = view1;
            } 
          } else {
            return view3;
          }  
      } 
      paramInt1 += b;
      view1 = view4;
      view2 = view5;
    } 
    if (view2 == null)
      view2 = view1; 
    return view2;
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
    return new LayoutParams(paramContext, paramAttributeSet);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams);
  }
  
  public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  int getSpaceForSpanRange(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1 && isLayoutRTL()) {
      int[] arrayOfInt1 = this.mCachedBorders;
      int i = this.mSpanCount;
      return arrayOfInt1[i - paramInt1] - arrayOfInt1[i - paramInt1 - paramInt2];
    } 
    int[] arrayOfInt = this.mCachedBorders;
    return arrayOfInt[paramInt1 + paramInt2] - arrayOfInt[paramInt1];
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  public SpanSizeLookup getSpanSizeLookup() {
    return this.mSpanSizeLookup;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, LinearLayoutManager.LayoutChunkResult paramLayoutChunkResult) {
    StringBuilder stringBuilder;
    int k;
    boolean bool;
    byte b;
    int i = this.mOrientationHelper.getModeInOther();
    if (i != 1073741824) {
      j = 1;
    } else {
      j = 0;
    } 
    if (getChildCount() > 0) {
      k = this.mCachedBorders[this.mSpanCount];
    } else {
      k = 0;
    } 
    if (j)
      updateMeasurements(); 
    if (paramLayoutState.mItemDirection == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int m = this.mSpanCount;
    if (!bool) {
      m = getSpanIndex(paramRecycler, paramState, paramLayoutState.mCurrentPosition) + getSpanSize(paramRecycler, paramState, paramLayoutState.mCurrentPosition);
      b = 0;
      n = 0;
    } else {
      b = 0;
      n = 0;
    } 
    while (true) {
      int i3 = m;
      if (b < this.mSpanCount) {
        i3 = m;
        if (paramLayoutState.hasMore(paramState)) {
          i3 = m;
          if (m > 0) {
            int i4 = paramLayoutState.mCurrentPosition;
            i3 = getSpanSize(paramRecycler, paramState, i4);
            if (i3 <= this.mSpanCount) {
              m -= i3;
              if (m < 0) {
                i3 = m;
                break;
              } 
              View view = paramLayoutState.next(paramRecycler);
              if (view == null) {
                i3 = m;
                break;
              } 
              n += i3;
              this.mSet[b] = view;
              b++;
              continue;
            } 
            stringBuilder = new StringBuilder();
            stringBuilder.append("Item at position ");
            stringBuilder.append(i4);
            stringBuilder.append(" requires ");
            stringBuilder.append(i3);
            stringBuilder.append(" spans but GridLayoutManager has only ");
            stringBuilder.append(this.mSpanCount);
            stringBuilder.append(" spans.");
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
        } 
      } 
      break;
    } 
    if (b == 0) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    assignSpans((RecyclerView.Recycler)stringBuilder, paramState, b, n, bool);
    int i2 = 0;
    m = 0;
    float f;
    for (f = 0.0F; i2 < b; f = f2) {
      View view = this.mSet[i2];
      if (paramLayoutState.mScrapList == null) {
        if (bool) {
          addView(view);
        } else {
          addView(view, 0);
        } 
      } else if (bool) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
      calculateItemDecorationsForChild(view, this.mDecorInsets);
      measureChild(view, i, false);
      int i3 = this.mOrientationHelper.getDecoratedMeasurement(view);
      n = m;
      if (i3 > m)
        n = i3; 
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      float f1 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0F / layoutParams.mSpanSize;
      float f2 = f;
      if (f1 > f)
        f2 = f1; 
      i2++;
      m = n;
    } 
    if (j) {
      guessMeasurement(f, k);
      m = 0;
      n = 0;
      while (n < b) {
        View view = this.mSet[n];
        measureChild(view, 1073741824, true);
        k = this.mOrientationHelper.getDecoratedMeasurement(view);
        j = m;
        if (k > m)
          j = k; 
        n++;
        m = j;
      } 
      k = m;
    } else {
      k = m;
    } 
    int n = 0;
    m = i;
    while (n < b) {
      View view = this.mSet[n];
      if (this.mOrientationHelper.getDecoratedMeasurement(view) != k) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        j = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        i2 = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        i = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
          i2 = getChildMeasureSpec(i, 1073741824, i2, layoutParams.width, false);
          j = View.MeasureSpec.makeMeasureSpec(k - j, 1073741824);
        } else {
          i2 = View.MeasureSpec.makeMeasureSpec(k - i2, 1073741824);
          j = getChildMeasureSpec(i, 1073741824, j, layoutParams.height, false);
        } 
        measureChildWithDecorationsAndMargin(view, i2, j, true);
      } 
      n++;
    } 
    paramLayoutChunkResult.mConsumed = k;
    n = 0;
    int j = 0;
    int i1 = 0;
    m = 0;
    if (this.mOrientation == 1) {
      if (paramLayoutState.mLayoutDirection == -1) {
        m = paramLayoutState.mOffset;
        i1 = m - k;
      } else {
        i1 = paramLayoutState.mOffset;
        m = i1 + k;
      } 
    } else if (paramLayoutState.mLayoutDirection == -1) {
      j = paramLayoutState.mOffset;
      n = j - k;
    } else {
      n = paramLayoutState.mOffset;
      j = n + k;
    } 
    for (i2 = 0; i2 < b; i2 = i3) {
      View view = this.mSet[i2];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (this.mOrientation == 1) {
        if (isLayoutRTL()) {
          n = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams.mSpanIndex];
          i = n - this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          j = m;
          m = i1;
          i1 = i;
        } else {
          j = getPaddingLeft() + this.mCachedBorders[layoutParams.mSpanIndex];
          i = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          n = j;
          i += j;
          j = m;
          m = i1;
          i1 = n;
          n = i;
        } 
      } else {
        i1 = n;
        n = j;
        j = getPaddingTop() + this.mCachedBorders[layoutParams.mSpanIndex];
        i = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
        m = j;
        j = i + j;
      } 
      layoutDecoratedWithMargins(view, i1, m, n, j);
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        paramLayoutChunkResult.mIgnoreConsumed = true; 
      paramLayoutChunkResult.mFocusable |= view.hasFocusable();
      int i3 = i2 + 1;
      i = n;
      i2 = j;
      n = i1;
      j = i;
      i1 = m;
      m = i2;
    } 
    Arrays.fill((Object[])this.mSet, (Object)null);
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    super.onAnchorReady(paramRecycler, paramState, paramAnchorInfo, paramInt);
    updateMeasurements();
    if (paramState.getItemCount() > 0 && !paramState.isPreLayout())
      ensureAnchorIsInCorrectSpan(paramRecycler, paramState, paramAnchorInfo, paramInt); 
    ensureViewSet();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   5: astore #5
    //   7: aload #5
    //   9: ifnonnull -> 14
    //   12: aconst_null
    //   13: areturn
    //   14: aload #5
    //   16: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   19: checkcast android/support/v7/widget/GridLayoutManager$LayoutParams
    //   22: astore #6
    //   24: aload #6
    //   26: getfield mSpanIndex : I
    //   29: istore #7
    //   31: aload #6
    //   33: getfield mSpanIndex : I
    //   36: aload #6
    //   38: getfield mSpanSize : I
    //   41: iadd
    //   42: istore #8
    //   44: aload_0
    //   45: aload_1
    //   46: iload_2
    //   47: aload_3
    //   48: aload #4
    //   50: invokespecial onFocusSearchFailed : (Landroid/view/View;ILandroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;)Landroid/view/View;
    //   53: ifnonnull -> 58
    //   56: aconst_null
    //   57: areturn
    //   58: aload_0
    //   59: iload_2
    //   60: invokevirtual convertFocusDirectionToLayoutDirection : (I)I
    //   63: iconst_1
    //   64: if_icmpne -> 73
    //   67: iconst_1
    //   68: istore #9
    //   70: goto -> 76
    //   73: iconst_0
    //   74: istore #9
    //   76: iload #9
    //   78: aload_0
    //   79: getfield mShouldReverseLayout : Z
    //   82: if_icmpeq -> 90
    //   85: iconst_1
    //   86: istore_2
    //   87: goto -> 92
    //   90: iconst_0
    //   91: istore_2
    //   92: iload_2
    //   93: ifeq -> 112
    //   96: aload_0
    //   97: invokevirtual getChildCount : ()I
    //   100: iconst_1
    //   101: isub
    //   102: istore_2
    //   103: iconst_m1
    //   104: istore #10
    //   106: iconst_m1
    //   107: istore #11
    //   109: goto -> 123
    //   112: iconst_0
    //   113: istore_2
    //   114: iconst_1
    //   115: istore #10
    //   117: aload_0
    //   118: invokevirtual getChildCount : ()I
    //   121: istore #11
    //   123: aload_0
    //   124: getfield mOrientation : I
    //   127: iconst_1
    //   128: if_icmpne -> 144
    //   131: aload_0
    //   132: invokevirtual isLayoutRTL : ()Z
    //   135: ifeq -> 144
    //   138: iconst_1
    //   139: istore #12
    //   141: goto -> 147
    //   144: iconst_0
    //   145: istore #12
    //   147: aconst_null
    //   148: astore #6
    //   150: aconst_null
    //   151: astore_1
    //   152: aload_0
    //   153: aload_3
    //   154: aload #4
    //   156: iload_2
    //   157: invokespecial getSpanGroupIndex : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;I)I
    //   160: istore #13
    //   162: iconst_m1
    //   163: istore #14
    //   165: iconst_0
    //   166: istore #15
    //   168: iconst_m1
    //   169: istore #16
    //   171: iconst_0
    //   172: istore #17
    //   174: iload_2
    //   175: istore #18
    //   177: iload_2
    //   178: istore #19
    //   180: iload #18
    //   182: iload #11
    //   184: if_icmpeq -> 553
    //   187: aload_0
    //   188: aload_3
    //   189: aload #4
    //   191: iload #18
    //   193: invokespecial getSpanGroupIndex : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;I)I
    //   196: istore_2
    //   197: aload_0
    //   198: iload #18
    //   200: invokevirtual getChildAt : (I)Landroid/view/View;
    //   203: astore #20
    //   205: aload #20
    //   207: aload #5
    //   209: if_acmpne -> 215
    //   212: goto -> 553
    //   215: aload #20
    //   217: invokevirtual hasFocusable : ()Z
    //   220: ifeq -> 240
    //   223: iload_2
    //   224: iload #13
    //   226: if_icmpeq -> 240
    //   229: aload #6
    //   231: ifnull -> 237
    //   234: goto -> 553
    //   237: goto -> 543
    //   240: aload #20
    //   242: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   245: checkcast android/support/v7/widget/GridLayoutManager$LayoutParams
    //   248: astore #21
    //   250: aload #21
    //   252: getfield mSpanIndex : I
    //   255: istore #22
    //   257: aload #21
    //   259: getfield mSpanIndex : I
    //   262: aload #21
    //   264: getfield mSpanSize : I
    //   267: iadd
    //   268: istore #23
    //   270: aload #20
    //   272: invokevirtual hasFocusable : ()Z
    //   275: ifeq -> 295
    //   278: iload #22
    //   280: iload #7
    //   282: if_icmpne -> 295
    //   285: iload #23
    //   287: iload #8
    //   289: if_icmpne -> 295
    //   292: aload #20
    //   294: areturn
    //   295: aload #20
    //   297: invokevirtual hasFocusable : ()Z
    //   300: ifeq -> 308
    //   303: aload #6
    //   305: ifnull -> 320
    //   308: aload #20
    //   310: invokevirtual hasFocusable : ()Z
    //   313: ifne -> 325
    //   316: aload_1
    //   317: ifnonnull -> 325
    //   320: iconst_1
    //   321: istore_2
    //   322: goto -> 458
    //   325: iload #22
    //   327: iload #7
    //   329: invokestatic max : (II)I
    //   332: istore_2
    //   333: iload #23
    //   335: iload #8
    //   337: invokestatic min : (II)I
    //   340: iload_2
    //   341: isub
    //   342: istore #24
    //   344: aload #20
    //   346: invokevirtual hasFocusable : ()Z
    //   349: ifeq -> 399
    //   352: iload #24
    //   354: iload #15
    //   356: if_icmple -> 364
    //   359: iconst_1
    //   360: istore_2
    //   361: goto -> 458
    //   364: iload #24
    //   366: iload #15
    //   368: if_icmpne -> 396
    //   371: iload #22
    //   373: iload #14
    //   375: if_icmple -> 383
    //   378: iconst_1
    //   379: istore_2
    //   380: goto -> 385
    //   383: iconst_0
    //   384: istore_2
    //   385: iload #12
    //   387: iload_2
    //   388: if_icmpne -> 396
    //   391: iconst_1
    //   392: istore_2
    //   393: goto -> 458
    //   396: goto -> 456
    //   399: aload #6
    //   401: ifnonnull -> 456
    //   404: iconst_0
    //   405: istore_2
    //   406: aload_0
    //   407: aload #20
    //   409: iconst_0
    //   410: iconst_1
    //   411: invokevirtual isViewPartiallyVisible : (Landroid/view/View;ZZ)Z
    //   414: ifeq -> 456
    //   417: iload #24
    //   419: iload #17
    //   421: if_icmple -> 429
    //   424: iconst_1
    //   425: istore_2
    //   426: goto -> 458
    //   429: iload #24
    //   431: iload #17
    //   433: if_icmpne -> 456
    //   436: iload #22
    //   438: iload #16
    //   440: if_icmple -> 445
    //   443: iconst_1
    //   444: istore_2
    //   445: iload #12
    //   447: iload_2
    //   448: if_icmpne -> 456
    //   451: iconst_1
    //   452: istore_2
    //   453: goto -> 458
    //   456: iconst_0
    //   457: istore_2
    //   458: iload_2
    //   459: ifeq -> 543
    //   462: aload #20
    //   464: invokevirtual hasFocusable : ()Z
    //   467: ifeq -> 507
    //   470: aload #21
    //   472: getfield mSpanIndex : I
    //   475: istore #14
    //   477: iload #23
    //   479: iload #8
    //   481: invokestatic min : (II)I
    //   484: istore_2
    //   485: iload #22
    //   487: iload #7
    //   489: invokestatic max : (II)I
    //   492: istore #15
    //   494: aload #20
    //   496: astore #6
    //   498: iload_2
    //   499: iload #15
    //   501: isub
    //   502: istore #15
    //   504: goto -> 543
    //   507: aload #21
    //   509: getfield mSpanIndex : I
    //   512: istore #16
    //   514: iload #23
    //   516: iload #8
    //   518: invokestatic min : (II)I
    //   521: istore_2
    //   522: iload #22
    //   524: iload #7
    //   526: invokestatic max : (II)I
    //   529: istore #17
    //   531: aload #20
    //   533: astore_1
    //   534: iload_2
    //   535: iload #17
    //   537: isub
    //   538: istore #17
    //   540: goto -> 543
    //   543: iload #18
    //   545: iload #10
    //   547: iadd
    //   548: istore #18
    //   550: goto -> 180
    //   553: aload #6
    //   555: ifnull -> 564
    //   558: aload #6
    //   560: astore_1
    //   561: goto -> 564
    //   564: aload_1
    //   565: areturn
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int i = getSpanGroupIndex(paramRecycler, paramState, layoutParams.getViewLayoutPosition());
    if (this.mOrientation == 0) {
      boolean bool;
      int j = layoutParams.getSpanIndex();
      int k = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(j, k, i, 1, bool, false));
    } else {
      boolean bool;
      int j = layoutParams.getSpanIndex();
      int k = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, j, k, bool, false));
    } 
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (paramState.isPreLayout())
      cachePreLayoutSpanMapping(); 
    super.onLayoutChildren(paramRecycler, paramState);
    clearPreLayoutSpanMappingCache();
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSpanCountChange = false;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollHorizontallyBy(paramInt, paramRecycler, paramState);
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollVerticallyBy(paramInt, paramRecycler, paramState);
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    int[] arrayOfInt;
    if (this.mCachedBorders == null)
      super.setMeasuredDimension(paramRect, paramInt1, paramInt2); 
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      paramInt2 = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      arrayOfInt = this.mCachedBorders;
      paramInt1 = chooseSize(paramInt1, arrayOfInt[arrayOfInt.length - 1] + i, getMinimumWidth());
    } else {
      paramInt1 = chooseSize(paramInt1, arrayOfInt.width() + i, getMinimumWidth());
      arrayOfInt = this.mCachedBorders;
      paramInt2 = chooseSize(paramInt2, arrayOfInt[arrayOfInt.length - 1] + j, getMinimumHeight());
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setSpanCount(int paramInt) {
    if (paramInt == this.mSpanCount)
      return; 
    this.mPendingSpanCountChange = true;
    if (paramInt >= 1) {
      this.mSpanCount = paramInt;
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      requestLayout();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Span count should be at least 1. Provided ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setSpanSizeLookup(SpanSizeLookup paramSpanSizeLookup) {
    this.mSpanSizeLookup = paramSpanSizeLookup;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    if (!paramBoolean) {
      super.setStackFromEnd(false);
      return;
    } 
    throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
    public int getSpanIndex(int param1Int1, int param1Int2) {
      return param1Int1 % param1Int2;
    }
    
    public int getSpanSize(int param1Int) {
      return 1;
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    int mSpanIndex = -1;
    
    int mSpanSize = 0;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public int getSpanIndex() {
      return this.mSpanIndex;
    }
    
    public int getSpanSize() {
      return this.mSpanSize;
    }
  }
  
  public static abstract class SpanSizeLookup {
    private boolean mCacheSpanIndices = false;
    
    final SparseIntArray mSpanIndexCache = new SparseIntArray();
    
    int findReferenceIndexFromCache(int param1Int) {
      int i = 0;
      for (int j = this.mSpanIndexCache.size() - 1; i <= j; j = k - 1) {
        int k = i + j >>> 1;
        if (this.mSpanIndexCache.keyAt(k) < param1Int) {
          i = k + 1;
          continue;
        } 
      } 
      param1Int = i - 1;
      return (param1Int >= 0 && param1Int < this.mSpanIndexCache.size()) ? this.mSpanIndexCache.keyAt(param1Int) : -1;
    }
    
    int getCachedSpanIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanIndices)
        return getSpanIndex(param1Int1, param1Int2); 
      int i = this.mSpanIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanIndex(param1Int1, param1Int2);
      this.mSpanIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    public int getSpanGroupIndex(int param1Int1, int param1Int2) {
      int i = 0;
      int j = 0;
      int k = getSpanSize(param1Int1);
      byte b = 0;
      while (b < param1Int1) {
        int i1;
        int m = getSpanSize(b);
        int n = i + m;
        if (n == param1Int2) {
          i = 0;
          i1 = j + 1;
        } else {
          i = n;
          i1 = j;
          if (n > param1Int2) {
            i = m;
            i1 = j + 1;
          } 
        } 
        b++;
        j = i1;
      } 
      param1Int1 = j;
      if (i + k > param1Int2)
        param1Int1 = j + 1; 
      return param1Int1;
    }
    
    public int getSpanIndex(int param1Int1, int param1Int2) {
      int i = getSpanSize(param1Int1);
      if (i == param1Int2)
        return 0; 
      int j = 0;
      int k = 0;
      int m = j;
      int n = k;
      if (this.mCacheSpanIndices) {
        m = j;
        n = k;
        if (this.mSpanIndexCache.size() > 0) {
          int i1 = findReferenceIndexFromCache(param1Int1);
          m = j;
          n = k;
          if (i1 >= 0) {
            m = this.mSpanIndexCache.get(i1) + getSpanSize(i1);
            n = i1 + 1;
          } 
        } 
      } 
      while (n < param1Int1) {
        k = getSpanSize(n);
        j = m + k;
        if (j == param1Int2) {
          m = 0;
        } else {
          m = j;
          if (j > param1Int2)
            m = k; 
        } 
        n++;
      } 
      return (m + i <= param1Int2) ? m : 0;
    }
    
    public abstract int getSpanSize(int param1Int);
    
    public void invalidateSpanIndexCache() {
      this.mSpanIndexCache.clear();
    }
    
    public boolean isSpanIndexCacheEnabled() {
      return this.mCacheSpanIndices;
    }
    
    public void setSpanIndexCacheEnabled(boolean param1Boolean) {
      this.mCacheSpanIndices = param1Boolean;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/GridLayoutManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */