package android.support.design.internal;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BottomNavigationMenuView extends ViewGroup implements MenuView {
  private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private final int activeItemMaxWidth;
  
  private final int activeItemMinWidth;
  
  private BottomNavigationItemView[] buttons;
  
  private final int inactiveItemMaxWidth;
  
  private final int inactiveItemMinWidth;
  
  private Drawable itemBackground;
  
  private int itemBackgroundRes;
  
  private final int itemHeight;
  
  private boolean itemHorizontalTranslationEnabled;
  
  private int itemIconSize;
  
  private ColorStateList itemIconTint;
  
  private final Pools.Pool<BottomNavigationItemView> itemPool = (Pools.Pool<BottomNavigationItemView>)new Pools.SynchronizedPool(5);
  
  private int itemTextAppearanceActive;
  
  private int itemTextAppearanceInactive;
  
  private final ColorStateList itemTextColorDefault;
  
  private ColorStateList itemTextColorFromUser;
  
  private int labelVisibilityMode;
  
  private MenuBuilder menu;
  
  private final View.OnClickListener onClickListener;
  
  private BottomNavigationPresenter presenter;
  
  private int selectedItemId = 0;
  
  private int selectedItemPosition = 0;
  
  private final TransitionSet set;
  
  private int[] tempChildWidths;
  
  public BottomNavigationMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    Resources resources = getResources();
    this.inactiveItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
    this.inactiveItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
    this.activeItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
    this.activeItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_min_width);
    this.itemHeight = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
    this.itemTextColorDefault = createDefaultColorStateList(16842808);
    AutoTransition autoTransition = new AutoTransition();
    this.set = (TransitionSet)autoTransition;
    autoTransition.setOrdering(0);
    this.set.setDuration(115L);
    this.set.setInterpolator((TimeInterpolator)new FastOutSlowInInterpolator());
    this.set.addTransition(new TextScale());
    this.onClickListener = new View.OnClickListener() {
        public void onClick(View param1View) {
          MenuItemImpl menuItemImpl = ((BottomNavigationItemView)param1View).getItemData();
          if (!BottomNavigationMenuView.this.menu.performItemAction((MenuItem)menuItemImpl, BottomNavigationMenuView.this.presenter, 0))
            menuItemImpl.setChecked(true); 
        }
      };
    this.tempChildWidths = new int[5];
  }
  
  private BottomNavigationItemView getNewItem() {
    BottomNavigationItemView bottomNavigationItemView1 = (BottomNavigationItemView)this.itemPool.acquire();
    BottomNavigationItemView bottomNavigationItemView2 = bottomNavigationItemView1;
    if (bottomNavigationItemView1 == null)
      bottomNavigationItemView2 = new BottomNavigationItemView(getContext()); 
    return bottomNavigationItemView2;
  }
  
  private boolean isShifting(int paramInt1, int paramInt2) {
    boolean bool = true;
    if ((paramInt1 == -1) ? (paramInt2 > 3) : (paramInt1 == 0))
      bool = false; 
    return bool;
  }
  
  public void buildMenuView() {
    removeAllViews();
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int j = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < j; b++) {
        BottomNavigationItemView bottomNavigationItemView = arrayOfBottomNavigationItemView[b];
        if (bottomNavigationItemView != null)
          this.itemPool.release(bottomNavigationItemView); 
      } 
    } 
    if (this.menu.size() == 0) {
      this.selectedItemId = 0;
      this.selectedItemPosition = 0;
      this.buttons = null;
      return;
    } 
    this.buttons = new BottomNavigationItemView[this.menu.size()];
    boolean bool = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
    int i;
    for (i = 0; i < this.menu.size(); i++) {
      this.presenter.setUpdateSuspended(true);
      this.menu.getItem(i).setCheckable(true);
      this.presenter.setUpdateSuspended(false);
      BottomNavigationItemView bottomNavigationItemView = getNewItem();
      this.buttons[i] = bottomNavigationItemView;
      bottomNavigationItemView.setIconTintList(this.itemIconTint);
      bottomNavigationItemView.setIconSize(this.itemIconSize);
      bottomNavigationItemView.setTextColor(this.itemTextColorDefault);
      bottomNavigationItemView.setTextAppearanceInactive(this.itemTextAppearanceInactive);
      bottomNavigationItemView.setTextAppearanceActive(this.itemTextAppearanceActive);
      bottomNavigationItemView.setTextColor(this.itemTextColorFromUser);
      Drawable drawable = this.itemBackground;
      if (drawable != null) {
        bottomNavigationItemView.setItemBackground(drawable);
      } else {
        bottomNavigationItemView.setItemBackground(this.itemBackgroundRes);
      } 
      bottomNavigationItemView.setShifting(bool);
      bottomNavigationItemView.setLabelVisibilityMode(this.labelVisibilityMode);
      bottomNavigationItemView.initialize((MenuItemImpl)this.menu.getItem(i), 0);
      bottomNavigationItemView.setItemPosition(i);
      bottomNavigationItemView.setOnClickListener(this.onClickListener);
      addView((View)bottomNavigationItemView);
    } 
    i = Math.min(this.menu.size() - 1, this.selectedItemPosition);
    this.selectedItemPosition = i;
    this.menu.getItem(i).setChecked(true);
  }
  
  public ColorStateList createDefaultColorStateList(int paramInt) {
    TypedValue typedValue = new TypedValue();
    if (!getContext().getTheme().resolveAttribute(paramInt, typedValue, true))
      return null; 
    ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
    if (!getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true))
      return null; 
    paramInt = typedValue.data;
    int i = colorStateList.getDefaultColor();
    int[] arrayOfInt1 = DISABLED_STATE_SET;
    int[] arrayOfInt2 = CHECKED_STATE_SET;
    int[] arrayOfInt3 = EMPTY_STATE_SET;
    int j = colorStateList.getColorForState(DISABLED_STATE_SET, i);
    return new ColorStateList(new int[][] { arrayOfInt1, arrayOfInt2, arrayOfInt3 }, new int[] { j, paramInt, i });
  }
  
  public ColorStateList getIconTintList() {
    return this.itemIconTint;
  }
  
  public Drawable getItemBackground() {
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    return (arrayOfBottomNavigationItemView != null && arrayOfBottomNavigationItemView.length > 0) ? arrayOfBottomNavigationItemView[0].getBackground() : this.itemBackground;
  }
  
  @Deprecated
  public int getItemBackgroundRes() {
    return this.itemBackgroundRes;
  }
  
  public int getItemIconSize() {
    return this.itemIconSize;
  }
  
  public int getItemTextAppearanceActive() {
    return this.itemTextAppearanceActive;
  }
  
  public int getItemTextAppearanceInactive() {
    return this.itemTextAppearanceInactive;
  }
  
  public ColorStateList getItemTextColor() {
    return this.itemTextColorFromUser;
  }
  
  public int getLabelVisibilityMode() {
    return this.labelVisibilityMode;
  }
  
  public int getSelectedItemId() {
    return this.selectedItemId;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.menu = paramMenuBuilder;
  }
  
  public boolean isItemHorizontalTranslationEnabled() {
    return this.itemHorizontalTranslationEnabled;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    paramInt3 -= paramInt1;
    paramInt4 -= paramInt2;
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
          view.layout(paramInt3 - paramInt2 - view.getMeasuredWidth(), 0, paramInt3 - paramInt2, paramInt4);
        } else {
          view.layout(paramInt2, 0, view.getMeasuredWidth() + paramInt2, paramInt4);
        } 
        paramInt2 += view.getMeasuredWidth();
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = this.menu.getVisibleItems().size();
    int k = getChildCount();
    int m = View.MeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824);
    if (isShifting(this.labelVisibilityMode, j) && this.itemHorizontalTranslationEnabled) {
      View view = getChildAt(this.selectedItemPosition);
      paramInt2 = this.activeItemMinWidth;
      paramInt1 = paramInt2;
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.activeItemMaxWidth, -2147483648), m);
        paramInt1 = Math.max(paramInt2, view.getMeasuredWidth());
      } 
      if (view.getVisibility() != 8) {
        paramInt2 = 1;
      } else {
        paramInt2 = 0;
      } 
      paramInt2 = j - paramInt2;
      int n = Math.min(i - this.inactiveItemMinWidth * paramInt2, Math.min(paramInt1, this.activeItemMaxWidth));
      if (paramInt2 == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = paramInt2;
      } 
      int i1 = Math.min((i - n) / paramInt1, this.inactiveItemMaxWidth);
      paramInt1 = i - n - i1 * paramInt2;
      paramInt2 = 0;
      while (paramInt2 < k) {
        if (getChildAt(paramInt2).getVisibility() != 8) {
          int[] arrayOfInt = this.tempChildWidths;
          if (paramInt2 == this.selectedItemPosition) {
            j = n;
          } else {
            j = i1;
          } 
          arrayOfInt[paramInt2] = j;
          j = paramInt1;
          if (paramInt1 > 0) {
            arrayOfInt = this.tempChildWidths;
            arrayOfInt[paramInt2] = arrayOfInt[paramInt2] + 1;
            j = paramInt1 - 1;
          } 
        } else {
          this.tempChildWidths[paramInt2] = 0;
          j = paramInt1;
        } 
        paramInt2++;
        paramInt1 = j;
      } 
    } else {
      if (j == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = j;
      } 
      int n = Math.min(i / paramInt1, this.activeItemMaxWidth);
      j = i - n * j;
      paramInt1 = 0;
      while (paramInt1 < k) {
        if (getChildAt(paramInt1).getVisibility() != 8) {
          int[] arrayOfInt = this.tempChildWidths;
          arrayOfInt[paramInt1] = n;
          paramInt2 = j;
          if (j > 0) {
            arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
            paramInt2 = j - 1;
          } 
        } else {
          this.tempChildWidths[paramInt1] = 0;
          paramInt2 = j;
        } 
        paramInt1++;
        j = paramInt2;
      } 
    } 
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < k; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.tempChildWidths[paramInt1], 1073741824), m);
        (view.getLayoutParams()).width = view.getMeasuredWidth();
        paramInt2 += view.getMeasuredWidth();
      } 
    } 
    setMeasuredDimension(View.resolveSizeAndState(paramInt2, View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824), 0), View.resolveSizeAndState(this.itemHeight, m, 0));
  }
  
  public void setIconTintList(ColorStateList paramColorStateList) {
    this.itemIconTint = paramColorStateList;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setIconTintList(paramColorStateList); 
    } 
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.itemBackground = paramDrawable;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setItemBackground(paramDrawable); 
    } 
  }
  
  public void setItemBackgroundRes(int paramInt) {
    this.itemBackgroundRes = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setItemBackground(paramInt); 
    } 
  }
  
  public void setItemHorizontalTranslationEnabled(boolean paramBoolean) {
    this.itemHorizontalTranslationEnabled = paramBoolean;
  }
  
  public void setItemIconSize(int paramInt) {
    this.itemIconSize = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setIconSize(paramInt); 
    } 
  }
  
  public void setItemTextAppearanceActive(int paramInt) {
    this.itemTextAppearanceActive = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++) {
        BottomNavigationItemView bottomNavigationItemView = arrayOfBottomNavigationItemView[b];
        bottomNavigationItemView.setTextAppearanceActive(paramInt);
        ColorStateList colorStateList = this.itemTextColorFromUser;
        if (colorStateList != null)
          bottomNavigationItemView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  public void setItemTextAppearanceInactive(int paramInt) {
    this.itemTextAppearanceInactive = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++) {
        BottomNavigationItemView bottomNavigationItemView = arrayOfBottomNavigationItemView[b];
        bottomNavigationItemView.setTextAppearanceInactive(paramInt);
        ColorStateList colorStateList = this.itemTextColorFromUser;
        if (colorStateList != null)
          bottomNavigationItemView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.itemTextColorFromUser = paramColorStateList;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setTextColor(paramColorStateList); 
    } 
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    this.labelVisibilityMode = paramInt;
  }
  
  public void setPresenter(BottomNavigationPresenter paramBottomNavigationPresenter) {
    this.presenter = paramBottomNavigationPresenter;
  }
  
  void tryRestoreSelectedItemId(int paramInt) {
    int i = this.menu.size();
    for (byte b = 0; b < i; b++) {
      MenuItem menuItem = this.menu.getItem(b);
      if (paramInt == menuItem.getItemId()) {
        this.selectedItemId = paramInt;
        this.selectedItemPosition = b;
        menuItem.setChecked(true);
        break;
      } 
    } 
  }
  
  public void updateMenuView() {
    MenuBuilder menuBuilder = this.menu;
    if (menuBuilder == null || this.buttons == null)
      return; 
    int i = menuBuilder.size();
    if (i != this.buttons.length) {
      buildMenuView();
      return;
    } 
    int j = this.selectedItemId;
    byte b;
    for (b = 0; b < i; b++) {
      MenuItem menuItem = this.menu.getItem(b);
      if (menuItem.isChecked()) {
        this.selectedItemId = menuItem.getItemId();
        this.selectedItemPosition = b;
      } 
    } 
    if (j != this.selectedItemId)
      TransitionManager.beginDelayedTransition(this, (Transition)this.set); 
    boolean bool = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
    for (b = 0; b < i; b++) {
      this.presenter.setUpdateSuspended(true);
      this.buttons[b].setLabelVisibilityMode(this.labelVisibilityMode);
      this.buttons[b].setShifting(bool);
      this.buttons[b].initialize((MenuItemImpl)this.menu.getItem(b), 0);
      this.presenter.setUpdateSuspended(false);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/BottomNavigationMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */