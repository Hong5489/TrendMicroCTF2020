package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.R;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.design.internal.ThemeEnforcement;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class BottomNavigationView extends FrameLayout {
  private static final int MENU_PRESENTER_ID = 1;
  
  private final MenuBuilder menu;
  
  private MenuInflater menuInflater;
  
  private final BottomNavigationMenuView menuView;
  
  private final BottomNavigationPresenter presenter = new BottomNavigationPresenter();
  
  private OnNavigationItemReselectedListener reselectedListener;
  
  private OnNavigationItemSelectedListener selectedListener;
  
  public BottomNavigationView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.bottomNavigationStyle);
  }
  
  public BottomNavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.menu = (MenuBuilder)new BottomNavigationMenu(paramContext);
    this.menuView = new BottomNavigationMenuView(paramContext);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
    layoutParams.gravity = 17;
    this.menuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    this.presenter.setBottomNavigationMenuView(this.menuView);
    this.presenter.setId(1);
    this.menuView.setPresenter(this.presenter);
    this.menu.addMenuPresenter((MenuPresenter)this.presenter);
    this.presenter.initForMenu(getContext(), this.menu);
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(paramContext, paramAttributeSet, R.styleable.BottomNavigationView, paramInt, R.style.Widget_Design_BottomNavigationView, new int[] { R.styleable.BottomNavigationView_itemTextAppearanceInactive, R.styleable.BottomNavigationView_itemTextAppearanceActive });
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
      this.menuView.setIconTintList(tintTypedArray.getColorStateList(R.styleable.BottomNavigationView_itemIconTint));
    } else {
      BottomNavigationMenuView bottomNavigationMenuView = this.menuView;
      bottomNavigationMenuView.setIconTintList(bottomNavigationMenuView.createDefaultColorStateList(16842808));
    } 
    setItemIconSize(tintTypedArray.getDimensionPixelSize(R.styleable.BottomNavigationView_itemIconSize, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_icon_size)));
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_itemTextAppearanceInactive))
      setItemTextAppearanceInactive(tintTypedArray.getResourceId(R.styleable.BottomNavigationView_itemTextAppearanceInactive, 0)); 
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_itemTextAppearanceActive))
      setItemTextAppearanceActive(tintTypedArray.getResourceId(R.styleable.BottomNavigationView_itemTextAppearanceActive, 0)); 
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_itemTextColor))
      setItemTextColor(tintTypedArray.getColorStateList(R.styleable.BottomNavigationView_itemTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_elevation))
      ViewCompat.setElevation((View)this, tintTypedArray.getDimensionPixelSize(R.styleable.BottomNavigationView_elevation, 0)); 
    setLabelVisibilityMode(tintTypedArray.getInteger(R.styleable.BottomNavigationView_labelVisibilityMode, -1));
    setItemHorizontalTranslationEnabled(tintTypedArray.getBoolean(R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
    paramInt = tintTypedArray.getResourceId(R.styleable.BottomNavigationView_itemBackground, 0);
    this.menuView.setItemBackgroundRes(paramInt);
    if (tintTypedArray.hasValue(R.styleable.BottomNavigationView_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.BottomNavigationView_menu, 0)); 
    tintTypedArray.recycle();
    addView((View)this.menuView, (ViewGroup.LayoutParams)layoutParams);
    if (Build.VERSION.SDK_INT < 21)
      addCompatibilityTopDivider(paramContext); 
    this.menu.setCallback(new MenuBuilder.Callback() {
          public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
            BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener = BottomNavigationView.this.reselectedListener;
            boolean bool = true;
            if (onNavigationItemReselectedListener != null && param1MenuItem.getItemId() == BottomNavigationView.this.getSelectedItemId()) {
              BottomNavigationView.this.reselectedListener.onNavigationItemReselected(param1MenuItem);
              return true;
            } 
            if (BottomNavigationView.this.selectedListener == null || BottomNavigationView.this.selectedListener.onNavigationItemSelected(param1MenuItem))
              bool = false; 
            return bool;
          }
          
          public void onMenuModeChange(MenuBuilder param1MenuBuilder) {}
        });
  }
  
  private void addCompatibilityTopDivider(Context paramContext) {
    View view = new View(paramContext);
    view.setBackgroundColor(ContextCompat.getColor(paramContext, R.color.design_bottom_navigation_shadow_color));
    view.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
    addView(view);
  }
  
  private MenuInflater getMenuInflater() {
    if (this.menuInflater == null)
      this.menuInflater = (MenuInflater)new SupportMenuInflater(getContext()); 
    return this.menuInflater;
  }
  
  public Drawable getItemBackground() {
    return this.menuView.getItemBackground();
  }
  
  @Deprecated
  public int getItemBackgroundResource() {
    return this.menuView.getItemBackgroundRes();
  }
  
  public int getItemIconSize() {
    return this.menuView.getItemIconSize();
  }
  
  public ColorStateList getItemIconTintList() {
    return this.menuView.getIconTintList();
  }
  
  public int getItemTextAppearanceActive() {
    return this.menuView.getItemTextAppearanceActive();
  }
  
  public int getItemTextAppearanceInactive() {
    return this.menuView.getItemTextAppearanceInactive();
  }
  
  public ColorStateList getItemTextColor() {
    return this.menuView.getItemTextColor();
  }
  
  public int getLabelVisibilityMode() {
    return this.menuView.getLabelVisibilityMode();
  }
  
  public int getMaxItemCount() {
    return 5;
  }
  
  public Menu getMenu() {
    return (Menu)this.menu;
  }
  
  public int getSelectedItemId() {
    return this.menuView.getSelectedItemId();
  }
  
  public void inflateMenu(int paramInt) {
    this.presenter.setUpdateSuspended(true);
    getMenuInflater().inflate(paramInt, (Menu)this.menu);
    this.presenter.setUpdateSuspended(false);
    this.presenter.updateMenuView(true);
  }
  
  public boolean isItemHorizontalTranslationEnabled() {
    return this.menuView.isItemHorizontalTranslationEnabled();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.menu.restorePresenterStates(savedState.menuPresenterState);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.menuPresenterState = new Bundle();
    this.menu.savePresenterStates(savedState.menuPresenterState);
    return (Parcelable)savedState;
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.menuView.setItemBackground(paramDrawable);
  }
  
  public void setItemBackgroundResource(int paramInt) {
    this.menuView.setItemBackgroundRes(paramInt);
  }
  
  public void setItemHorizontalTranslationEnabled(boolean paramBoolean) {
    if (this.menuView.isItemHorizontalTranslationEnabled() != paramBoolean) {
      this.menuView.setItemHorizontalTranslationEnabled(paramBoolean);
      this.presenter.updateMenuView(false);
    } 
  }
  
  public void setItemIconSize(int paramInt) {
    this.menuView.setItemIconSize(paramInt);
  }
  
  public void setItemIconSizeRes(int paramInt) {
    setItemIconSize(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setItemIconTintList(ColorStateList paramColorStateList) {
    this.menuView.setIconTintList(paramColorStateList);
  }
  
  public void setItemTextAppearanceActive(int paramInt) {
    this.menuView.setItemTextAppearanceActive(paramInt);
  }
  
  public void setItemTextAppearanceInactive(int paramInt) {
    this.menuView.setItemTextAppearanceInactive(paramInt);
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.menuView.setItemTextColor(paramColorStateList);
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    if (this.menuView.getLabelVisibilityMode() != paramInt) {
      this.menuView.setLabelVisibilityMode(paramInt);
      this.presenter.updateMenuView(false);
    } 
  }
  
  public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener paramOnNavigationItemReselectedListener) {
    this.reselectedListener = paramOnNavigationItemReselectedListener;
  }
  
  public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener paramOnNavigationItemSelectedListener) {
    this.selectedListener = paramOnNavigationItemSelectedListener;
  }
  
  public void setSelectedItemId(int paramInt) {
    MenuItem menuItem = this.menu.findItem(paramInt);
    if (menuItem != null && !this.menu.performItemAction(menuItem, (MenuPresenter)this.presenter, 0))
      menuItem.setChecked(true); 
  }
  
  public static interface OnNavigationItemReselectedListener {
    void onNavigationItemReselected(MenuItem param1MenuItem);
  }
  
  public static interface OnNavigationItemSelectedListener {
    boolean onNavigationItemSelected(MenuItem param1MenuItem);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public BottomNavigationView.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomNavigationView.SavedState(param2Parcel, null);
        }
        
        public BottomNavigationView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new BottomNavigationView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public BottomNavigationView.SavedState[] newArray(int param2Int) {
          return new BottomNavigationView.SavedState[param2Int];
        }
      };
    
    Bundle menuPresenterState;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      readFromParcel(param1Parcel, param1ClassLoader);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    private void readFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      this.menuPresenterState = param1Parcel.readBundle(param1ClassLoader);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeBundle(this.menuPresenterState);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public BottomNavigationView.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomNavigationView.SavedState(param1Parcel, null);
    }
    
    public BottomNavigationView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomNavigationView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomNavigationView.SavedState[] newArray(int param1Int) {
      return new BottomNavigationView.SavedState[param1Int];
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/BottomNavigationView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */