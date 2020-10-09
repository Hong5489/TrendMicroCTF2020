package android.support.v7.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;

public class PagerSnapHelper extends SnapHelper {
  private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
  
  private OrientationHelper mHorizontalHelper;
  
  private OrientationHelper mVerticalHelper;
  
  private int distanceToCenter(RecyclerView.LayoutManager paramLayoutManager, View paramView, OrientationHelper paramOrientationHelper) {
    int k;
    int i = paramOrientationHelper.getDecoratedStart(paramView);
    int j = paramOrientationHelper.getDecoratedMeasurement(paramView) / 2;
    if (paramLayoutManager.getClipToPadding()) {
      k = paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
    } else {
      k = paramOrientationHelper.getEnd() / 2;
    } 
    return i + j - k;
  }
  
  private View findCenterView(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    int j;
    int i = paramLayoutManager.getChildCount();
    if (i == 0)
      return null; 
    View view = null;
    if (paramLayoutManager.getClipToPadding()) {
      j = paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
    } else {
      j = paramOrientationHelper.getEnd() / 2;
    } 
    int k = Integer.MAX_VALUE;
    byte b = 0;
    while (b < i) {
      View view1 = paramLayoutManager.getChildAt(b);
      int m = Math.abs(paramOrientationHelper.getDecoratedStart(view1) + paramOrientationHelper.getDecoratedMeasurement(view1) / 2 - j);
      int n = k;
      if (m < k) {
        n = m;
        view = view1;
      } 
      b++;
      k = n;
    } 
    return view;
  }
  
  private View findStartView(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    int i = paramLayoutManager.getChildCount();
    if (i == 0)
      return null; 
    View view = null;
    int j = Integer.MAX_VALUE;
    byte b = 0;
    while (b < i) {
      View view1 = paramLayoutManager.getChildAt(b);
      int k = paramOrientationHelper.getDecoratedStart(view1);
      int m = j;
      if (k < j) {
        m = k;
        view = view1;
      } 
      b++;
      j = m;
    } 
    return view;
  }
  
  private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mHorizontalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(paramLayoutManager); 
    return this.mHorizontalHelper;
  }
  
  private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mVerticalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mVerticalHelper = OrientationHelper.createVerticalHelper(paramLayoutManager); 
    return this.mVerticalHelper;
  }
  
  public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager paramLayoutManager, View paramView) {
    int[] arrayOfInt = new int[2];
    if (paramLayoutManager.canScrollHorizontally()) {
      arrayOfInt[0] = distanceToCenter(paramLayoutManager, paramView, getHorizontalHelper(paramLayoutManager));
    } else {
      arrayOfInt[0] = 0;
    } 
    if (paramLayoutManager.canScrollVertically()) {
      arrayOfInt[1] = distanceToCenter(paramLayoutManager, paramView, getVerticalHelper(paramLayoutManager));
    } else {
      arrayOfInt[1] = 0;
    } 
    return arrayOfInt;
  }
  
  protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager paramLayoutManager) {
    return !(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
        protected float calculateSpeedPerPixel(DisplayMetrics param1DisplayMetrics) {
          return 100.0F / param1DisplayMetrics.densityDpi;
        }
        
        protected int calculateTimeForScrolling(int param1Int) {
          return Math.min(100, super.calculateTimeForScrolling(param1Int));
        }
        
        protected void onTargetFound(View param1View, RecyclerView.State param1State, RecyclerView.SmoothScroller.Action param1Action) {
          PagerSnapHelper pagerSnapHelper = PagerSnapHelper.this;
          int[] arrayOfInt = pagerSnapHelper.calculateDistanceToFinalSnap(pagerSnapHelper.mRecyclerView.getLayoutManager(), param1View);
          int i = arrayOfInt[0];
          int j = arrayOfInt[1];
          int k = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(j)));
          if (k > 0)
            param1Action.update(i, j, k, (Interpolator)this.mDecelerateInterpolator); 
        }
      };
  }
  
  public View findSnapView(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? findCenterView(paramLayoutManager, getVerticalHelper(paramLayoutManager)) : (paramLayoutManager.canScrollHorizontally() ? findCenterView(paramLayoutManager, getHorizontalHelper(paramLayoutManager)) : null);
  }
  
  public int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getItemCount : ()I
    //   4: istore #4
    //   6: iload #4
    //   8: ifne -> 13
    //   11: iconst_m1
    //   12: ireturn
    //   13: aconst_null
    //   14: astore #5
    //   16: aload_1
    //   17: invokevirtual canScrollVertically : ()Z
    //   20: ifeq -> 38
    //   23: aload_0
    //   24: aload_1
    //   25: aload_0
    //   26: aload_1
    //   27: invokespecial getVerticalHelper : (Landroid/support/v7/widget/RecyclerView$LayoutManager;)Landroid/support/v7/widget/OrientationHelper;
    //   30: invokespecial findStartView : (Landroid/support/v7/widget/RecyclerView$LayoutManager;Landroid/support/v7/widget/OrientationHelper;)Landroid/view/View;
    //   33: astore #5
    //   35: goto -> 57
    //   38: aload_1
    //   39: invokevirtual canScrollHorizontally : ()Z
    //   42: ifeq -> 57
    //   45: aload_0
    //   46: aload_1
    //   47: aload_0
    //   48: aload_1
    //   49: invokespecial getHorizontalHelper : (Landroid/support/v7/widget/RecyclerView$LayoutManager;)Landroid/support/v7/widget/OrientationHelper;
    //   52: invokespecial findStartView : (Landroid/support/v7/widget/RecyclerView$LayoutManager;Landroid/support/v7/widget/OrientationHelper;)Landroid/view/View;
    //   55: astore #5
    //   57: aload #5
    //   59: ifnonnull -> 64
    //   62: iconst_m1
    //   63: ireturn
    //   64: aload_1
    //   65: aload #5
    //   67: invokevirtual getPosition : (Landroid/view/View;)I
    //   70: istore #6
    //   72: iload #6
    //   74: iconst_m1
    //   75: if_icmpne -> 80
    //   78: iconst_m1
    //   79: ireturn
    //   80: aload_1
    //   81: invokevirtual canScrollHorizontally : ()Z
    //   84: istore #7
    //   86: iconst_0
    //   87: istore #8
    //   89: iload #7
    //   91: ifeq -> 108
    //   94: iload_2
    //   95: ifle -> 103
    //   98: iconst_1
    //   99: istore_2
    //   100: goto -> 105
    //   103: iconst_0
    //   104: istore_2
    //   105: goto -> 119
    //   108: iload_3
    //   109: ifle -> 117
    //   112: iconst_1
    //   113: istore_2
    //   114: goto -> 119
    //   117: iconst_0
    //   118: istore_2
    //   119: iconst_0
    //   120: istore #9
    //   122: iload #9
    //   124: istore_3
    //   125: aload_1
    //   126: instanceof android/support/v7/widget/RecyclerView$SmoothScroller$ScrollVectorProvider
    //   129: ifeq -> 176
    //   132: aload_1
    //   133: checkcast android/support/v7/widget/RecyclerView$SmoothScroller$ScrollVectorProvider
    //   136: iload #4
    //   138: iconst_1
    //   139: isub
    //   140: invokeinterface computeScrollVectorForPosition : (I)Landroid/graphics/PointF;
    //   145: astore_1
    //   146: iload #9
    //   148: istore_3
    //   149: aload_1
    //   150: ifnull -> 176
    //   153: aload_1
    //   154: getfield x : F
    //   157: fconst_0
    //   158: fcmpg
    //   159: iflt -> 174
    //   162: iload #8
    //   164: istore_3
    //   165: aload_1
    //   166: getfield y : F
    //   169: fconst_0
    //   170: fcmpg
    //   171: ifge -> 176
    //   174: iconst_1
    //   175: istore_3
    //   176: iload_3
    //   177: ifeq -> 192
    //   180: iload_2
    //   181: ifeq -> 204
    //   184: iload #6
    //   186: iconst_1
    //   187: isub
    //   188: istore_2
    //   189: goto -> 207
    //   192: iload_2
    //   193: ifeq -> 204
    //   196: iload #6
    //   198: iconst_1
    //   199: iadd
    //   200: istore_2
    //   201: goto -> 207
    //   204: iload #6
    //   206: istore_2
    //   207: iload_2
    //   208: ireturn
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/PagerSnapHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */