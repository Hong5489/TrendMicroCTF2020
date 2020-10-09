package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
  static final int GENERATED_ITEM_PADDING = 4;
  
  static final int MIN_CELL_SIZE = 56;
  
  private static final String TAG = "ActionMenuView";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private boolean mFormatItems;
  
  private int mFormatItemsWidth;
  
  private int mGeneratedItemPadding;
  
  private MenuBuilder mMenu;
  
  MenuBuilder.Callback mMenuBuilderCallback;
  
  private int mMinCellSize;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ActionMenuPresenter mPresenter;
  
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mMinCellSize = (int)(56.0F * f);
    this.mGeneratedItemPadding = (int)(4.0F * f);
    this.mPopupContext = paramContext;
    this.mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   4: checkcast android/support/v7/widget/ActionMenuView$LayoutParams
    //   7: astore #5
    //   9: iload_3
    //   10: invokestatic getSize : (I)I
    //   13: iload #4
    //   15: isub
    //   16: iload_3
    //   17: invokestatic getMode : (I)I
    //   20: invokestatic makeMeasureSpec : (II)I
    //   23: istore #6
    //   25: aload_0
    //   26: instanceof android/support/v7/view/menu/ActionMenuItemView
    //   29: ifeq -> 41
    //   32: aload_0
    //   33: checkcast android/support/v7/view/menu/ActionMenuItemView
    //   36: astore #7
    //   38: goto -> 44
    //   41: aconst_null
    //   42: astore #7
    //   44: iconst_0
    //   45: istore #8
    //   47: aload #7
    //   49: ifnull -> 66
    //   52: aload #7
    //   54: invokevirtual hasText : ()Z
    //   57: ifeq -> 66
    //   60: iconst_1
    //   61: istore #4
    //   63: goto -> 69
    //   66: iconst_0
    //   67: istore #4
    //   69: iconst_0
    //   70: istore #9
    //   72: iload #9
    //   74: istore_3
    //   75: iload_2
    //   76: ifle -> 146
    //   79: iload #4
    //   81: ifeq -> 92
    //   84: iload #9
    //   86: istore_3
    //   87: iload_2
    //   88: iconst_2
    //   89: if_icmplt -> 146
    //   92: aload_0
    //   93: iload_1
    //   94: iload_2
    //   95: imul
    //   96: ldc -2147483648
    //   98: invokestatic makeMeasureSpec : (II)I
    //   101: iload #6
    //   103: invokevirtual measure : (II)V
    //   106: aload_0
    //   107: invokevirtual getMeasuredWidth : ()I
    //   110: istore #9
    //   112: iload #9
    //   114: iload_1
    //   115: idiv
    //   116: istore_3
    //   117: iload_3
    //   118: istore_2
    //   119: iload #9
    //   121: iload_1
    //   122: irem
    //   123: ifeq -> 130
    //   126: iload_3
    //   127: iconst_1
    //   128: iadd
    //   129: istore_2
    //   130: iload_2
    //   131: istore_3
    //   132: iload #4
    //   134: ifeq -> 146
    //   137: iload_2
    //   138: istore_3
    //   139: iload_2
    //   140: iconst_2
    //   141: if_icmpge -> 146
    //   144: iconst_2
    //   145: istore_3
    //   146: iload #8
    //   148: istore #10
    //   150: aload #5
    //   152: getfield isOverflowButton : Z
    //   155: ifne -> 170
    //   158: iload #8
    //   160: istore #10
    //   162: iload #4
    //   164: ifeq -> 170
    //   167: iconst_1
    //   168: istore #10
    //   170: aload #5
    //   172: iload #10
    //   174: putfield expandable : Z
    //   177: aload #5
    //   179: iload_3
    //   180: putfield cellsUsed : I
    //   183: aload_0
    //   184: iload_3
    //   185: iload_1
    //   186: imul
    //   187: ldc 1073741824
    //   189: invokestatic makeMeasureSpec : (II)I
    //   192: iload #6
    //   194: invokevirtual measure : (II)V
    //   197: iload_3
    //   198: ireturn
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2) {
    boolean bool;
    int i = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = getPaddingLeft();
    int m = getPaddingRight();
    int n = getPaddingTop() + getPaddingBottom();
    int i1 = getChildMeasureSpec(paramInt2, n, -2);
    int i2 = paramInt1 - k + m;
    paramInt1 = this.mMinCellSize;
    int i3 = i2 / paramInt1;
    int i4 = i2 % paramInt1;
    if (i3 == 0) {
      setMeasuredDimension(i2, 0);
      return;
    } 
    int i5 = paramInt1 + i4 / i3;
    paramInt1 = i3;
    m = 0;
    int i6 = 0;
    paramInt2 = 0;
    int i7 = 0;
    long l = 0L;
    int i8 = getChildCount();
    k = 0;
    int i9 = 0;
    while (i9 < i8) {
      int i12;
      View view = getChildAt(i9);
      if (view.getVisibility() == 8) {
        i12 = k;
      } else {
        boolean bool1 = view instanceof ActionMenuItemView;
        i12 = k + 1;
        if (bool1) {
          k = this.mGeneratedItemPadding;
          view.setPadding(k, 0, k, 0);
        } 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.expanded = false;
        layoutParams.extraPixels = 0;
        layoutParams.cellsUsed = 0;
        layoutParams.expandable = false;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        if (bool1 && ((ActionMenuItemView)view).hasText()) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        layoutParams.preventEdgeOffset = bool1;
        if (layoutParams.isOverflowButton) {
          k = 1;
        } else {
          k = paramInt1;
        } 
        bool = measureChildForCells(view, i5, k, i1, n);
        i6 = Math.max(i6, bool);
        k = paramInt2;
        if (layoutParams.expandable)
          k = paramInt2 + 1; 
        if (layoutParams.isOverflowButton)
          i7 = 1; 
        paramInt1 -= bool;
        m = Math.max(m, view.getMeasuredHeight());
        if (bool == true) {
          long l1 = (1 << i9);
          l |= l1;
          paramInt2 = k;
        } else {
          paramInt2 = k;
        } 
      } 
      i9++;
      k = i12;
    } 
    if (i7 && k == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    int i10 = 0;
    int i11 = paramInt1;
    paramInt1 = i10;
    i3 = i2;
    i9 = i;
    while (paramInt2 > 0 && i11 > 0) {
      i = Integer.MAX_VALUE;
      long l1 = 0L;
      i4 = 0;
      i2 = 0;
      i10 = paramInt1;
      paramInt1 = i4;
      while (i2 < i8) {
        long l2;
        int i12;
        LayoutParams layoutParams = (LayoutParams)getChildAt(i2).getLayoutParams();
        if (!layoutParams.expandable) {
          i4 = paramInt1;
          i12 = i;
          l2 = l1;
        } else if (layoutParams.cellsUsed < i) {
          i12 = layoutParams.cellsUsed;
          l2 = 1L << i2;
          i4 = 1;
        } else {
          i4 = paramInt1;
          i12 = i;
          l2 = l1;
          if (layoutParams.cellsUsed == i) {
            l2 = l1 | 1L << i2;
            i4 = paramInt1 + 1;
            i12 = i;
          } 
        } 
        i2++;
        paramInt1 = i4;
        i = i12;
        l1 = l2;
      } 
      l |= l1;
      if (paramInt1 > i11) {
        paramInt1 = i10;
        break;
      } 
      i10 = 0;
      while (i10 < i8) {
        long l2;
        View view = getChildAt(i10);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if ((l1 & (1 << i10)) == 0L) {
          i4 = i11;
          l2 = l;
          if (layoutParams.cellsUsed == i + 1) {
            l2 = l | (1 << i10);
            i4 = i11;
          } 
        } else {
          if (bool && layoutParams.preventEdgeOffset && i11 == 1) {
            i4 = this.mGeneratedItemPadding;
            view.setPadding(i4 + i5, 0, i4, 0);
          } 
          layoutParams.cellsUsed++;
          layoutParams.expanded = true;
          i4 = i11 - 1;
          l2 = l;
        } 
        i10++;
        i11 = i4;
        l = l2;
      } 
      paramInt1 = 1;
    } 
    if (!i7 && k == 1) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (i11 > 0 && l != 0L && (i11 < k - 1 || paramInt2 != 0 || i6 > 1)) {
      float f = Long.bitCount(l);
      if (paramInt2 == 0) {
        float f1;
        if ((l & 0x1L) != 0L) {
          f1 = f;
          if (!((LayoutParams)getChildAt(0).getLayoutParams()).preventEdgeOffset)
            f1 = f - 0.5F; 
        } else {
          f1 = f;
        } 
        f = f1;
        if ((l & (1 << i8 - 1)) != 0L) {
          f = f1;
          if (!((LayoutParams)getChildAt(i8 - 1).getLayoutParams()).preventEdgeOffset)
            f = f1 - 0.5F; 
        } 
      } 
      i10 = 0;
      if (f > 0.0F)
        i10 = (int)((i11 * i5) / f); 
      i4 = 0;
      for (i7 = paramInt1; i4 < i8; i7 = paramInt1) {
        if ((l & (1 << i4)) == 0L) {
          paramInt1 = i7;
        } else {
          View view = getChildAt(i4);
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (view instanceof ActionMenuItemView) {
            layoutParams.extraPixels = i10;
            layoutParams.expanded = true;
            if (i4 == 0 && !layoutParams.preventEdgeOffset)
              layoutParams.leftMargin = -i10 / 2; 
            paramInt1 = 1;
          } else if (layoutParams.isOverflowButton) {
            layoutParams.extraPixels = i10;
            layoutParams.expanded = true;
            layoutParams.rightMargin = -i10 / 2;
            paramInt1 = 1;
          } else {
            if (i4 != 0)
              layoutParams.leftMargin = i10 / 2; 
            paramInt1 = i7;
            if (i4 != i8 - 1) {
              layoutParams.rightMargin = i10 / 2;
              paramInt1 = i7;
            } 
          } 
        } 
        i4++;
      } 
      paramInt1 = i7;
    } 
    if (paramInt1 != 0)
      for (paramInt1 = 0; paramInt1 < i8; paramInt1++) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.expanded)
          view.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.cellsUsed * i5 + layoutParams.extraPixels, 1073741824), i1); 
      }  
    if (i9 != 1073741824) {
      paramInt1 = m;
    } else {
      paramInt1 = j;
    } 
    setMeasuredDimension(i3, paramInt1);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams != null && paramLayoutParams instanceof LayoutParams) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void dismissPopupMenus() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.dismissPopupMenus(); 
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    layoutParams.gravity = 16;
    return layoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (paramLayoutParams != null) {
      LayoutParams layoutParams;
      if (paramLayoutParams instanceof LayoutParams) {
        layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
      if (layoutParams.gravity <= 0)
        layoutParams.gravity = 16; 
      return layoutParams;
    } 
    return generateDefaultLayoutParams();
  }
  
  public LayoutParams generateOverflowButtonLayoutParams() {
    LayoutParams layoutParams = generateDefaultLayoutParams();
    layoutParams.isOverflowButton = true;
    return layoutParams;
  }
  
  public Menu getMenu() {
    if (this.mMenu == null) {
      Context context = getContext();
      MenuBuilder menuBuilder = new MenuBuilder(context);
      this.mMenu = menuBuilder;
      menuBuilder.setCallback(new MenuBuilderCallback());
      ActionMenuPresenter actionMenuPresenter2 = new ActionMenuPresenter(context);
      this.mPresenter = actionMenuPresenter2;
      actionMenuPresenter2.setReserveOverflow(true);
      ActionMenuPresenter actionMenuPresenter1 = this.mPresenter;
      MenuPresenter.Callback callback = this.mActionMenuPresenterCallback;
      if (callback == null)
        callback = new ActionMenuPresenterCallback(); 
      actionMenuPresenter1.setCallback(callback);
      this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter, this.mPopupContext);
      this.mPresenter.setMenuView(this);
    } 
    return (Menu)this.mMenu;
  }
  
  public Drawable getOverflowIcon() {
    getMenu();
    return this.mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  protected boolean hasSupportDividerBeforeChildAt(int paramInt) {
    boolean bool;
    if (paramInt == 0)
      return false; 
    View view1 = getChildAt(paramInt - 1);
    View view2 = getChildAt(paramInt);
    int i = 0;
    int j = i;
    if (paramInt < getChildCount()) {
      j = i;
      if (view1 instanceof ActionMenuChildView)
        j = false | ((ActionMenuChildView)view1).needsDividerAfter(); 
    } 
    i = j;
    if (paramInt > 0) {
      i = j;
      if (view2 instanceof ActionMenuChildView)
        bool = j | ((ActionMenuChildView)view2).needsDividerBefore(); 
    } 
    return bool;
  }
  
  public boolean hideOverflowMenu() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl) {
    return this.mMenu.performItemAction((MenuItem)paramMenuItemImpl, 0);
  }
  
  public boolean isOverflowMenuShowPending() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null) {
      actionMenuPresenter.updateMenuView(false);
      if (this.mPresenter.isOverflowMenuShowing()) {
        this.mPresenter.hideOverflowMenu();
        this.mPresenter.showOverflowMenu();
      } 
    } 
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    View view;
    ActionMenuView actionMenuView = this;
    if (!actionMenuView.mFormatItems) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    int i = getChildCount();
    int j = (paramInt4 - paramInt2) / 2;
    int k = getDividerWidth();
    paramInt2 = 0;
    int m = 0;
    int n = 0;
    paramInt4 = paramInt3 - paramInt1 - getPaddingRight() - getPaddingLeft();
    int i1 = 0;
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    int i2;
    for (i2 = 0; i2 < i; i2++) {
      View view1 = actionMenuView.getChildAt(i2);
      if (view1.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view1.getLayoutParams();
        if (layoutParams.isOverflowButton) {
          int i4;
          i1 = view1.getMeasuredWidth();
          paramInt2 = i1;
          if (actionMenuView.hasSupportDividerBeforeChildAt(i2))
            paramInt2 = i1 + k; 
          int i3 = view1.getMeasuredHeight();
          if (paramBoolean) {
            i1 = getPaddingLeft() + layoutParams.leftMargin;
            i4 = i1 + paramInt2;
          } else {
            i4 = getWidth() - getPaddingRight() - layoutParams.rightMargin;
            i1 = i4 - paramInt2;
          } 
          int i5 = j - i3 / 2;
          view1.layout(i1, i5, i4, i5 + i3);
          paramInt4 -= paramInt2;
          i1 = 1;
        } else {
          int i3 = view1.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
          int i4 = m + i3;
          paramInt4 -= i3;
          m = i4;
          if (actionMenuView.hasSupportDividerBeforeChildAt(i2))
            m = i4 + k; 
          n++;
        } 
      } 
    } 
    if (i == 1 && i1 == 0) {
      view = actionMenuView.getChildAt(0);
      paramInt4 = view.getMeasuredWidth();
      paramInt2 = view.getMeasuredHeight();
      paramInt1 = (paramInt3 - paramInt1) / 2 - paramInt4 / 2;
      paramInt3 = j - paramInt2 / 2;
      view.layout(paramInt1, paramInt3, paramInt1 + paramInt4, paramInt3 + paramInt2);
      return;
    } 
    paramInt1 = n - (i1 ^ 0x1);
    if (paramInt1 > 0) {
      paramInt1 = paramInt4 / paramInt1;
    } else {
      paramInt1 = 0;
    } 
    m = Math.max(0, paramInt1);
    if (paramBoolean) {
      paramInt4 = getWidth() - getPaddingRight();
      paramInt1 = 0;
      paramInt3 = k;
      while (paramInt1 < i) {
        View view1 = view.getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view1.getLayoutParams();
        if (view1.getVisibility() != 8 && !layoutParams.isOverflowButton) {
          i1 = paramInt4 - layoutParams.rightMargin;
          paramInt4 = view1.getMeasuredWidth();
          n = view1.getMeasuredHeight();
          i2 = j - n / 2;
          view1.layout(i1 - paramInt4, i2, i1, i2 + n);
          paramInt4 = i1 - layoutParams.leftMargin + paramInt4 + m;
        } 
        paramInt1++;
      } 
    } else {
      paramInt3 = getPaddingLeft();
      paramInt1 = 0;
      while (paramInt1 < i) {
        View view1 = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view1.getLayoutParams();
        paramInt2 = paramInt3;
        if (view1.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt2 = paramInt3;
          } else {
            paramInt4 = paramInt3 + layoutParams.leftMargin;
            i2 = view1.getMeasuredWidth();
            paramInt2 = view1.getMeasuredHeight();
            paramInt3 = j - paramInt2 / 2;
            view1.layout(paramInt4, paramInt3, paramInt4 + i2, paramInt3 + paramInt2);
            paramInt2 = paramInt4 + layoutParams.rightMargin + i2 + m;
          }  
        paramInt1++;
        paramInt3 = paramInt2;
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool2;
    boolean bool1 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.mFormatItems = bool2;
    if (bool1 != bool2)
      this.mFormatItemsWidth = 0; 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mFormatItems) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null && i != this.mFormatItemsWidth) {
        this.mFormatItemsWidth = i;
        menuBuilder.onItemsChanged(true);
      } 
    } 
    int j = getChildCount();
    if (this.mFormatItems && j > 0) {
      onMeasureExactFormat(paramInt1, paramInt2);
    } else {
      for (i = 0; i < j; i++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
        layoutParams.rightMargin = 0;
        layoutParams.leftMargin = 0;
      } 
      super.onMeasure(paramInt1, paramInt2);
    } 
  }
  
  public MenuBuilder peekMenu() {
    return this.mMenu;
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mPresenter.setExpandedActionViewsExclusive(paramBoolean);
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    getMenu();
    this.mPresenter.setOverflowIcon(paramDrawable);
  }
  
  public void setOverflowReserved(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
  }
  
  public void setPopupTheme(int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
      } else {
        this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
      } 
    } 
  }
  
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter) {
    this.mPresenter = paramActionMenuPresenter;
    paramActionMenuPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static interface ActionMenuChildView {
    boolean needsDividerAfter();
    
    boolean needsDividerBefore();
  }
  
  private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      return false;
    }
  }
  
  public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
    @ExportedProperty
    public int cellsUsed;
    
    @ExportedProperty
    public boolean expandable;
    
    boolean expanded;
    
    @ExportedProperty
    public int extraPixels;
    
    @ExportedProperty
    public boolean isOverflowButton;
    
    @ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = false;
    }
    
    LayoutParams(int param1Int1, int param1Int2, boolean param1Boolean) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = param1Boolean;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
      this.isOverflowButton = param1LayoutParams.isOverflowButton;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
  }
  
  private class MenuBuilderCallback implements MenuBuilder.Callback {
    public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
      boolean bool;
      if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onMenuModeChange(MenuBuilder param1MenuBuilder) {
      if (ActionMenuView.this.mMenuBuilderCallback != null)
        ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(param1MenuBuilder); 
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/ActionMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */