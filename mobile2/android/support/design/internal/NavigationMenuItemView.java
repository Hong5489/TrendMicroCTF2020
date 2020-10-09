package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.design.R;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

public class NavigationMenuItemView extends ForegroundLinearLayout implements MenuView.ItemView {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private final AccessibilityDelegateCompat accessibilityDelegate = new AccessibilityDelegateCompat() {
      public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
        param1AccessibilityNodeInfoCompat.setCheckable(NavigationMenuItemView.this.checkable);
      }
    };
  
  private FrameLayout actionArea;
  
  boolean checkable;
  
  private Drawable emptyDrawable;
  
  private boolean hasIconTintList;
  
  private final int iconSize;
  
  private ColorStateList iconTintList;
  
  private MenuItemImpl itemData;
  
  private boolean needsEmptyIcon;
  
  private final CheckedTextView textView;
  
  public NavigationMenuItemView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NavigationMenuItemView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public NavigationMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setOrientation(0);
    LayoutInflater.from(paramContext).inflate(R.layout.design_navigation_menu_item, (ViewGroup)this, true);
    this.iconSize = paramContext.getResources().getDimensionPixelSize(R.dimen.design_navigation_icon_size);
    CheckedTextView checkedTextView = (CheckedTextView)findViewById(R.id.design_menu_item_text);
    this.textView = checkedTextView;
    checkedTextView.setDuplicateParentStateEnabled(true);
    ViewCompat.setAccessibilityDelegate((View)this.textView, this.accessibilityDelegate);
  }
  
  private void adjustAppearance() {
    if (shouldExpandActionArea()) {
      this.textView.setVisibility(8);
      FrameLayout frameLayout = this.actionArea;
      if (frameLayout != null) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)frameLayout.getLayoutParams();
        layoutParams.width = -1;
        this.actionArea.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
    } else {
      this.textView.setVisibility(0);
      FrameLayout frameLayout = this.actionArea;
      if (frameLayout != null) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)frameLayout.getLayoutParams();
        layoutParams.width = -2;
        this.actionArea.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
    } 
  }
  
  private StateListDrawable createDefaultBackground() {
    TypedValue typedValue = new TypedValue();
    if (getContext().getTheme().resolveAttribute(R.attr.colorControlHighlight, typedValue, true)) {
      StateListDrawable stateListDrawable = new StateListDrawable();
      stateListDrawable.addState(CHECKED_STATE_SET, (Drawable)new ColorDrawable(typedValue.data));
      stateListDrawable.addState(EMPTY_STATE_SET, (Drawable)new ColorDrawable(0));
      return stateListDrawable;
    } 
    return null;
  }
  
  private void setActionView(View paramView) {
    if (paramView != null) {
      if (this.actionArea == null)
        this.actionArea = (FrameLayout)((ViewStub)findViewById(R.id.design_menu_item_action_area_stub)).inflate(); 
      this.actionArea.removeAllViews();
      this.actionArea.addView(paramView);
    } 
  }
  
  private boolean shouldExpandActionArea() {
    boolean bool;
    if (this.itemData.getTitle() == null && this.itemData.getIcon() == null && this.itemData.getActionView() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public MenuItemImpl getItemData() {
    return this.itemData;
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
    this.itemData = paramMenuItemImpl;
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    } 
    setVisibility(paramInt);
    if (getBackground() == null)
      ViewCompat.setBackground((View)this, (Drawable)createDefaultBackground()); 
    setCheckable(paramMenuItemImpl.isCheckable());
    setChecked(paramMenuItemImpl.isChecked());
    setEnabled(paramMenuItemImpl.isEnabled());
    setTitle(paramMenuItemImpl.getTitle());
    setIcon(paramMenuItemImpl.getIcon());
    setActionView(paramMenuItemImpl.getActionView());
    setContentDescription(paramMenuItemImpl.getContentDescription());
    TooltipCompat.setTooltipText((View)this, paramMenuItemImpl.getTooltipText());
    adjustAppearance();
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  public boolean prefersCondensedTitle() {
    return false;
  }
  
  public void recycle() {
    FrameLayout frameLayout = this.actionArea;
    if (frameLayout != null)
      frameLayout.removeAllViews(); 
    this.textView.setCompoundDrawables(null, null, null, null);
  }
  
  public void setCheckable(boolean paramBoolean) {
    refreshDrawableState();
    if (this.checkable != paramBoolean) {
      this.checkable = paramBoolean;
      this.accessibilityDelegate.sendAccessibilityEvent((View)this.textView, 2048);
    } 
  }
  
  public void setChecked(boolean paramBoolean) {
    refreshDrawableState();
    this.textView.setChecked(paramBoolean);
  }
  
  public void setHorizontalPadding(int paramInt) {
    setPadding(paramInt, 0, paramInt, 0);
  }
  
  public void setIcon(Drawable paramDrawable) {
    if (paramDrawable != null) {
      Drawable drawable = paramDrawable;
      if (this.hasIconTintList) {
        Drawable.ConstantState constantState = paramDrawable.getConstantState();
        if (constantState != null)
          paramDrawable = constantState.newDrawable(); 
        drawable = DrawableCompat.wrap(paramDrawable).mutate();
        DrawableCompat.setTintList(drawable, this.iconTintList);
      } 
      int i = this.iconSize;
      drawable.setBounds(0, 0, i, i);
      paramDrawable = drawable;
    } else if (this.needsEmptyIcon) {
      if (this.emptyDrawable == null) {
        paramDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.navigation_empty_icon, getContext().getTheme());
        this.emptyDrawable = paramDrawable;
        if (paramDrawable != null) {
          int i = this.iconSize;
          paramDrawable.setBounds(0, 0, i, i);
        } 
      } 
      paramDrawable = this.emptyDrawable;
    } 
    TextViewCompat.setCompoundDrawablesRelative((TextView)this.textView, paramDrawable, null, null, null);
  }
  
  public void setIconPadding(int paramInt) {
    this.textView.setCompoundDrawablePadding(paramInt);
  }
  
  void setIconTintList(ColorStateList paramColorStateList) {
    boolean bool;
    this.iconTintList = paramColorStateList;
    if (paramColorStateList != null) {
      bool = true;
    } else {
      bool = false;
    } 
    this.hasIconTintList = bool;
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl != null)
      setIcon(menuItemImpl.getIcon()); 
  }
  
  public void setNeedsEmptyIcon(boolean paramBoolean) {
    this.needsEmptyIcon = paramBoolean;
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar) {}
  
  public void setTextAppearance(int paramInt) {
    TextViewCompat.setTextAppearance((TextView)this.textView, paramInt);
  }
  
  public void setTextColor(ColorStateList paramColorStateList) {
    this.textView.setTextColor(paramColorStateList);
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.textView.setText(paramCharSequence);
  }
  
  public boolean showsIcon() {
    return true;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/NavigationMenuItemView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */