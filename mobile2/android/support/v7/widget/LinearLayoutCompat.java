package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        if (bool) {
          j = getPaddingLeft();
        } else {
          j = getWidth() - getPaddingRight() - this.mDividerWidth;
        } 
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          j = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } else {
          j = view.getRight() + layoutParams.rightMargin;
        } 
      } 
      drawVerticalDivider(paramCanvas, j);
    } 
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        j = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        j = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, j);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    int i = this.mOrientation;
    return (i == 0) ? new LayoutParams(-2, -2) : ((i == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    int i = getChildCount();
    int j = this.mBaselineAlignedChildIndex;
    if (i > j) {
      View view = getChildAt(j);
      int k = view.getBaseline();
      if (k == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      i = this.mBaselineChildTop;
      j = i;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        j = i;
        if (m != 48)
          if (m != 16) {
            if (m != 80) {
              j = i;
            } else {
              j = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
            } 
          } else {
            j = i + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
          }  
      } 
      return ((LayoutParams)view.getLayoutParams()).topMargin + j + k;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramInt == 0) {
      if ((this.mShowDividers & 0x1) != 0)
        bool2 = true; 
      return bool2;
    } 
    if (paramInt == getChildCount()) {
      bool2 = bool1;
      if ((this.mShowDividers & 0x4) != 0)
        bool2 = true; 
      return bool2;
    } 
    if ((this.mShowDividers & 0x2) != 0) {
      bool1 = false;
      paramInt--;
      while (true) {
        bool2 = bool1;
        if (paramInt >= 0) {
          if (getChildAt(paramInt).getVisibility() != 8) {
            bool2 = true;
            break;
          } 
          paramInt--;
          continue;
        } 
        break;
      } 
      return bool2;
    } 
    return false;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    byte b1;
    byte b2;
    boolean bool1 = ViewUtils.isLayoutRtl((View)this);
    int i = getPaddingTop();
    int j = paramInt4 - paramInt2;
    int k = getPaddingBottom();
    int m = getPaddingBottom();
    int n = getVirtualChildCount();
    int i1 = this.mGravity;
    boolean bool2 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    int i2 = ViewCompat.getLayoutDirection((View)this);
    paramInt2 = GravityCompat.getAbsoluteGravity(i1 & 0x800007, i2);
    if (paramInt2 != 1) {
      if (paramInt2 != 5) {
        paramInt1 = getPaddingLeft();
      } else {
        paramInt1 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingLeft() + (paramInt3 - paramInt1 - this.mTotalLength) / 2;
    } 
    if (bool1) {
      b1 = n - 1;
      b2 = -1;
    } else {
      b1 = 0;
      b2 = 1;
    } 
    paramInt2 = 0;
    int i3 = j;
    paramInt3 = i;
    paramInt4 = paramInt1;
    while (paramInt2 < n) {
      int i4 = b1 + b2 * paramInt2;
      View view = getVirtualChildAt(i4);
      if (view == null) {
        paramInt4 += measureNullChild(i4);
      } else if (view.getVisibility() != 8) {
        int i5 = view.getMeasuredWidth();
        int i6 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool2 && layoutParams.height != -1) {
          paramInt1 = view.getBaseline();
        } else {
          paramInt1 = -1;
        } 
        int i7 = layoutParams.gravity;
        if (i7 < 0)
          i7 = i1 & 0x70; 
        i7 &= 0x70;
        if (i7 != 16) {
          if (i7 != 48) {
            if (i7 != 80) {
              paramInt1 = paramInt3;
            } else {
              i7 = j - k - i6 - layoutParams.bottomMargin;
              if (paramInt1 != -1) {
                int i8 = view.getMeasuredHeight();
                paramInt1 = i7 - arrayOfInt2[2] - i8 - paramInt1;
              } else {
                paramInt1 = i7;
              } 
            } 
          } else {
            i7 = layoutParams.topMargin + paramInt3;
            if (paramInt1 != -1) {
              paramInt1 = i7 + arrayOfInt1[1] - paramInt1;
            } else {
              paramInt1 = i7;
            } 
          } 
        } else {
          paramInt1 = (j - i - m - i6) / 2 + paramInt3 + layoutParams.topMargin - layoutParams.bottomMargin;
        } 
        i7 = paramInt4;
        if (hasDividerBeforeChildAt(i4))
          i7 = paramInt4 + this.mDividerWidth; 
        paramInt4 = i7 + layoutParams.leftMargin;
        setChildFrame(view, paramInt4 + getLocationOffset(view), paramInt1, i5, i6);
        i7 = layoutParams.rightMargin;
        paramInt1 = getNextLocationOffset(view);
        paramInt2 += getChildrenSkipCount(view, i4);
        paramInt4 += i5 + i7 + paramInt1;
      } 
      paramInt2++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    int n = getVirtualChildCount();
    int i1 = this.mGravity;
    paramInt1 = i1 & 0x70;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - this.mTotalLength) / 2;
    } 
    paramInt2 = 0;
    paramInt3 = i;
    while (true) {
      paramInt4 = paramInt3;
      if (paramInt2 < n) {
        View view = getVirtualChildAt(paramInt2);
        if (view == null) {
          paramInt1 += measureNullChild(paramInt2);
        } else if (view.getVisibility() != 8) {
          int i2 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt3 = layoutParams.gravity;
          if (paramInt3 < 0)
            paramInt3 = i1 & 0x800007; 
          paramInt3 = GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt3 != 1) {
            if (paramInt3 != 5) {
              paramInt3 = layoutParams.leftMargin + paramInt4;
            } else {
              paramInt3 = j - k - i2 - layoutParams.rightMargin;
            } 
          } else {
            paramInt3 = (j - i - m - i2) / 2 + paramInt4 + layoutParams.leftMargin - layoutParams.rightMargin;
          } 
          int i4 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            i4 = paramInt1 + this.mDividerHeight; 
          paramInt1 = i4 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i2, i3);
          paramInt3 = layoutParams.bottomMargin;
          i4 = getNextLocationOffset(view);
          paramInt2 += getChildrenSkipCount(view, paramInt2);
          paramInt1 += i3 + paramInt3 + i4;
        } 
        paramInt2++;
        paramInt3 = paramInt4;
        continue;
      } 
      break;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    boolean bool3;
    this.mTotalLength = 0;
    int i = getVirtualChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    if (this.mMaxAscent == null || this.mMaxDescent == null) {
      this.mMaxAscent = new int[4];
      this.mMaxDescent = new int[4];
    } 
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    arrayOfInt1[3] = -1;
    arrayOfInt1[2] = -1;
    arrayOfInt1[1] = -1;
    arrayOfInt1[0] = -1;
    arrayOfInt2[3] = -1;
    arrayOfInt2[2] = -1;
    arrayOfInt2[1] = -1;
    arrayOfInt2[0] = -1;
    boolean bool1 = this.mBaselineAligned;
    boolean bool2 = this.mUseLargestChild;
    if (j == 1073741824) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    int m = 0;
    int n = 0;
    float f = 0.0F;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    boolean bool4 = true;
    int i5 = 0;
    int i6 = 0;
    while (m < i) {
      View view = getVirtualChildAt(m);
      if (view == null) {
        this.mTotalLength += measureNullChild(m);
      } else if (view.getVisibility() == 8) {
        m += getChildrenSkipCount(view, m);
      } else {
        if (hasDividerBeforeChildAt(m))
          this.mTotalLength += this.mDividerWidth; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (j == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0F) {
          if (bool3) {
            this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
          } else {
            int i13 = this.mTotalLength;
            this.mTotalLength = Math.max(i13, layoutParams.leftMargin + i13 + layoutParams.rightMargin);
          } 
          if (bool1) {
            int i13 = View.MeasureSpec.makeMeasureSpec(0, 0);
            view.measure(i13, i13);
          } else {
            i3 = 1;
          } 
        } else {
          int i13;
          if (layoutParams.width == 0 && layoutParams.weight > 0.0F) {
            layoutParams.width = -2;
            i13 = 0;
          } else {
            i13 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i14 = this.mTotalLength;
          } else {
            i14 = 0;
          } 
          measureChildBeforeLayout(view, m, paramInt1, i14, paramInt2, 0);
          if (i13 != Integer.MIN_VALUE)
            layoutParams.width = i13; 
          LayoutParams layoutParams1 = layoutParams;
          int i14 = view.getMeasuredWidth();
          if (bool3) {
            this.mTotalLength += layoutParams1.leftMargin + i14 + layoutParams1.rightMargin + getNextLocationOffset(view);
          } else {
            i13 = this.mTotalLength;
            this.mTotalLength = Math.max(i13, i13 + i14 + layoutParams1.leftMargin + layoutParams1.rightMargin + getNextLocationOffset(view));
          } 
          if (bool2)
            i2 = Math.max(i14, i2); 
        } 
        int i9 = i6;
        int i11 = 0;
        int i10 = i11;
        i6 = i4;
        if (k != 1073741824) {
          i10 = i11;
          i6 = i4;
          if (layoutParams.height == -1) {
            i6 = 1;
            i10 = 1;
          } 
        } 
        int i12 = layoutParams.topMargin + layoutParams.bottomMargin;
        i11 = view.getMeasuredHeight() + i12;
        i1 = View.combineMeasuredStates(i1, view.getMeasuredState());
        if (bool1) {
          int i13 = view.getBaseline();
          if (i13 != -1) {
            if (layoutParams.gravity < 0) {
              i4 = this.mGravity;
            } else {
              i4 = layoutParams.gravity;
            } 
            i4 = ((i4 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
            arrayOfInt1[i4] = Math.max(arrayOfInt1[i4], i13);
            arrayOfInt2[i4] = Math.max(arrayOfInt2[i4], i11 - i13);
          } 
        } 
        i4 = i12;
        n = Math.max(n, i11);
        if (bool4 && layoutParams.height == -1) {
          bool4 = true;
        } else {
          bool4 = false;
        } 
        if (layoutParams.weight > 0.0F) {
          if (i10 == 0)
            i4 = i11; 
          i4 = Math.max(i9, i4);
        } else {
          if (i10 == 0)
            i4 = i11; 
          i5 = Math.max(i5, i4);
          i4 = i9;
        } 
        m += getChildrenSkipCount(view, m);
        i9 = i4;
        i4 = i6;
        i6 = i9;
      } 
      m++;
    } 
    m = i6;
    int i7 = i2;
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i))
      this.mTotalLength += this.mDividerWidth; 
    if (arrayOfInt1[1] != -1 || arrayOfInt1[0] != -1 || arrayOfInt1[2] != -1 || arrayOfInt1[3] != -1) {
      i2 = Math.max(n, Math.max(arrayOfInt1[3], Math.max(arrayOfInt1[0], Math.max(arrayOfInt1[1], arrayOfInt1[2]))) + Math.max(arrayOfInt2[3], Math.max(arrayOfInt2[0], Math.max(arrayOfInt2[1], arrayOfInt2[2]))));
    } else {
      i2 = n;
    } 
    if (bool2) {
      i6 = j;
      if (i6 == Integer.MIN_VALUE || i6 == 0) {
        this.mTotalLength = 0;
        for (i6 = 0; i6 < i; i6++) {
          View view = getVirtualChildAt(i6);
          if (view == null) {
            this.mTotalLength += measureNullChild(i6);
          } else if (view.getVisibility() == 8) {
            i6 += getChildrenSkipCount(view, i6);
          } else {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (bool3) {
              this.mTotalLength += layoutParams.leftMargin + i7 + layoutParams.rightMargin + getNextLocationOffset(view);
            } else {
              n = this.mTotalLength;
              this.mTotalLength = Math.max(n, n + i7 + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
            } 
          } 
        } 
      } 
    } 
    this.mTotalLength += getPaddingLeft() + getPaddingRight();
    i6 = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), paramInt1, 0);
    n = i6 & 0xFFFFFF;
    int i8 = n - this.mTotalLength;
    if (i3 || (i8 != 0 && f > 0.0F)) {
      i7 = i5;
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      arrayOfInt1[3] = -1;
      arrayOfInt1[2] = -1;
      arrayOfInt1[1] = -1;
      arrayOfInt1[0] = -1;
      arrayOfInt2[3] = -1;
      arrayOfInt2[2] = -1;
      arrayOfInt2[1] = -1;
      arrayOfInt2[0] = -1;
      this.mTotalLength = 0;
      int i9 = 0;
      i2 = i8;
      n = -1;
      i5 = i1;
      i3 = m;
      m = n;
      n = j;
      j = i7;
      for (i1 = i9; i1 < i; i1++) {
        View view = getVirtualChildAt(i1);
        if (view != null && view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i9 = (int)(i2 * f1 / f);
            int i11 = getChildMeasureSpec(paramInt2, getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height);
            if (layoutParams.width != 0 || n != 1073741824) {
              i8 = view.getMeasuredWidth() + i9;
              i7 = i8;
              if (i8 < 0)
                i7 = 0; 
              view.measure(View.MeasureSpec.makeMeasureSpec(i7, 1073741824), i11);
            } else {
              if (i9 > 0) {
                i7 = i9;
              } else {
                i7 = 0;
              } 
              view.measure(View.MeasureSpec.makeMeasureSpec(i7, 1073741824), i11);
            } 
            i5 = View.combineMeasuredStates(i5, view.getMeasuredState() & 0xFF000000);
            f -= f1;
            i2 -= i9;
          } 
          if (bool3) {
            this.mTotalLength += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view);
          } else {
            i7 = this.mTotalLength;
            this.mTotalLength = Math.max(i7, view.getMeasuredWidth() + i7 + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
          } 
          if (k != 1073741824 && layoutParams.height == -1) {
            i7 = 1;
          } else {
            i7 = 0;
          } 
          int i10 = layoutParams.topMargin + layoutParams.bottomMargin;
          i8 = view.getMeasuredHeight() + i10;
          i9 = Math.max(m, i8);
          if (i7 != 0) {
            m = i10;
          } else {
            m = i8;
          } 
          m = Math.max(j, m);
          if (bool4 && layoutParams.height == -1) {
            bool4 = true;
          } else {
            bool4 = false;
          } 
          if (bool1) {
            i7 = view.getBaseline();
            if (i7 != -1) {
              if (layoutParams.gravity < 0) {
                j = this.mGravity;
              } else {
                j = layoutParams.gravity;
              } 
              j = ((j & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
              arrayOfInt1[j] = Math.max(arrayOfInt1[j], i7);
              arrayOfInt2[j] = Math.max(arrayOfInt2[j], i8 - i7);
            } 
          } 
          j = m;
          m = i9;
        } 
      } 
      this.mTotalLength += getPaddingLeft() + getPaddingRight();
      if (arrayOfInt1[1] != -1 || arrayOfInt1[0] != -1 || arrayOfInt1[2] != -1 || arrayOfInt1[3] != -1) {
        i2 = Math.max(m, Math.max(arrayOfInt1[3], Math.max(arrayOfInt1[0], Math.max(arrayOfInt1[1], arrayOfInt1[2]))) + Math.max(arrayOfInt2[3], Math.max(arrayOfInt2[0], Math.max(arrayOfInt2[1], arrayOfInt2[2]))));
      } else {
        i2 = m;
      } 
      m = j;
      i3 = i5;
      j = i6;
      i5 = i2;
      i6 = i3;
      i2 = m;
    } else {
      i5 = Math.max(i5, m);
      if (bool2 && j != 1073741824) {
        m = 0;
        j = n;
        while (m < i) {
          View view = getVirtualChildAt(m);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(i7, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824)); 
          m++;
        } 
      } 
      j = i6;
      i6 = i5;
      i5 = i2;
      i2 = i6;
      i6 = i1;
    } 
    m = i5;
    if (!bool4) {
      m = i5;
      if (k != 1073741824)
        m = i2; 
    } 
    setMeasuredDimension(j | 0xFF000000 & i6, View.resolveSizeAndState(Math.max(m + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), paramInt2, i6 << 16));
    if (i4 != 0)
      forceUniformHeight(i, paramInt1); 
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i = getVirtualChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = this.mBaselineAlignedChildIndex;
    boolean bool = this.mUseLargestChild;
    int n = 0;
    int i1 = 0;
    float f = 0.0F;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 1;
    while (i3 < i) {
      View view = getVirtualChildAt(i3);
      if (view == null) {
        this.mTotalLength += measureNullChild(i3);
      } else if (view.getVisibility() == 8) {
        i3 += getChildrenSkipCount(view, i3);
      } else {
        if (hasDividerBeforeChildAt(i3))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (k == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          n = this.mTotalLength;
          this.mTotalLength = Math.max(n, layoutParams.topMargin + n + layoutParams.bottomMargin);
          n = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i14 = 0;
          } else {
            i14 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i15 = this.mTotalLength;
          } else {
            i15 = 0;
          } 
          LayoutParams layoutParams1 = layoutParams;
          measureChildBeforeLayout(view, i3, paramInt1, 0, paramInt2, i15);
          if (i14 != Integer.MIN_VALUE)
            layoutParams1.height = i14; 
          int i15 = view.getMeasuredHeight();
          int i14 = this.mTotalLength;
          this.mTotalLength = Math.max(i14, i14 + i15 + layoutParams1.topMargin + layoutParams1.bottomMargin + getNextLocationOffset(view));
          if (bool)
            i7 = Math.max(i15, i7); 
        } 
        int i12 = i3;
        i3 = i6;
        if (m >= 0 && m == i12 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        int i13 = i12;
        if (i13 >= m || layoutParams.weight <= 0.0F) {
          int i14 = 0;
          i12 = i14;
          i6 = i4;
          if (j != 1073741824) {
            i12 = i14;
            i6 = i4;
            if (layoutParams.width == -1) {
              i6 = 1;
              i12 = 1;
            } 
          } 
          i14 = layoutParams.leftMargin + layoutParams.rightMargin;
          i4 = view.getMeasuredWidth() + i14;
          int i15 = Math.max(i1, i4);
          int i16 = View.combineMeasuredStates(i5, view.getMeasuredState());
          if (i8 && layoutParams.width == -1) {
            i5 = 1;
          } else {
            i5 = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (i12 != 0)
              i4 = i14; 
            i1 = Math.max(i3, i4);
            i8 = i2;
          } else {
            i1 = i3;
            if (i12 != 0) {
              i8 = i14;
            } else {
              i8 = i4;
            } 
            i8 = Math.max(i2, i8);
          } 
          i4 = getChildrenSkipCount(view, i13);
          i3 = i1;
          i2 = i8;
          i14 = i4 + i13;
          i1 = i15;
          i12 = i16;
          i4 = i6;
          i8 = i5;
          i5 = i12;
          i6 = i3;
          i3 = i14;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i3++;
    } 
    i3 = i6;
    int i9 = i2;
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i))
      this.mTotalLength += this.mDividerHeight; 
    int i10 = i;
    if (bool) {
      i6 = k;
      if (i6 == Integer.MIN_VALUE || i6 == 0) {
        this.mTotalLength = 0;
        for (i6 = 0; i6 < i10; i6++) {
          View view = getVirtualChildAt(i6);
          if (view == null) {
            this.mTotalLength += measureNullChild(i6);
          } else if (view.getVisibility() == 8) {
            i6 += getChildrenSkipCount(view, i6);
          } else {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            i2 = this.mTotalLength;
            this.mTotalLength = Math.max(i2, i2 + i7 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          } 
        } 
        i6 = i5;
      } else {
        i6 = i5;
      } 
    } else {
      i6 = i5;
    } 
    i2 = k;
    this.mTotalLength += getPaddingTop() + getPaddingBottom();
    k = Math.max(this.mTotalLength, getSuggestedMinimumHeight());
    i5 = i7;
    int i11 = View.resolveSizeAndState(k, paramInt2, 0);
    k = i11 & 0xFFFFFF;
    i7 = k - this.mTotalLength;
    if (n != 0 || (i7 != 0 && f > 0.0F)) {
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      this.mTotalLength = 0;
      i = 0;
      i3 = i7;
      i7 = i6;
      k = m;
      i6 = i3;
      i3 = i5;
      i5 = i9;
      while (i < i10) {
        View view = getVirtualChildAt(i);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i9 = (int)(i6 * f1 / f);
            int i14 = getPaddingLeft();
            m = getPaddingRight();
            int i12 = layoutParams.leftMargin;
            int i13 = layoutParams.rightMargin;
            int i15 = layoutParams.width;
            n = i6 - i9;
            m = getChildMeasureSpec(paramInt1, i14 + m + i12 + i13, i15);
            if (layoutParams.height != 0 || i2 != 1073741824) {
              i9 = view.getMeasuredHeight() + i9;
              i6 = i9;
              if (i9 < 0)
                i6 = 0; 
              view.measure(m, View.MeasureSpec.makeMeasureSpec(i6, 1073741824));
            } else {
              if (i9 > 0) {
                i6 = i9;
              } else {
                i6 = 0;
              } 
              view.measure(m, View.MeasureSpec.makeMeasureSpec(i6, 1073741824));
            } 
            i7 = View.combineMeasuredStates(i7, view.getMeasuredState() & 0xFFFFFF00);
            f -= f1;
            i6 = n;
          } 
          m = layoutParams.leftMargin + layoutParams.rightMargin;
          i9 = view.getMeasuredWidth() + m;
          n = Math.max(i1, i9);
          if (j != 1073741824 && layoutParams.width == -1) {
            i1 = 1;
          } else {
            i1 = 0;
          } 
          if (i1 != 0) {
            i1 = m;
          } else {
            i1 = i9;
          } 
          i1 = Math.max(i5, i1);
          if (i8 != 0 && layoutParams.width == -1) {
            i5 = 1;
          } else {
            i5 = 0;
          } 
          i8 = this.mTotalLength;
          this.mTotalLength = Math.max(i8, i8 + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          i8 = i5;
          i5 = i1;
          i1 = n;
        } 
        i++;
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
    } else {
      i7 = Math.max(i9, i3);
      if (bool && i2 != 1073741824)
        for (i2 = 0; i2 < i10; i2++) {
          View view = getVirtualChildAt(i2);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i5, 1073741824)); 
        }  
      i5 = i7;
      i7 = i6;
    } 
    i6 = i1;
    if (i8 == 0) {
      i6 = i1;
      if (j != 1073741824)
        i6 = i5; 
    } 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, i7), i11);
    if (i4 != 0)
      forceUniformWidth(i10, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
    } else {
      drawDividersHorizontal(paramCanvas);
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
    } else {
      measureHorizontal(paramInt1, paramInt2);
    } 
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("base aligned child index out of range (0, ");
    stringBuilder.append(getChildCount());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    int i = paramInt & 0x800007;
    paramInt = this.mGravity;
    if ((0x800007 & paramInt) != i) {
      this.mGravity = 0xFF7FFFF8 & paramInt | i;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    int i = paramInt & 0x70;
    paramInt = this.mGravity;
    if ((paramInt & 0x70) != i) {
      this.mGravity = paramInt & 0xFFFFFF8F | i;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OrientationMode {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/LinearLayoutCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */