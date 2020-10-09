package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.TooltipCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class BottomNavigationItemView extends FrameLayout implements MenuView.ItemView {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  public static final int INVALID_ITEM_POSITION = -1;
  
  private final int defaultMargin;
  
  private ImageView icon;
  
  private ColorStateList iconTint;
  
  private boolean isShifting;
  
  private MenuItemImpl itemData;
  
  private int itemPosition = -1;
  
  private int labelVisibilityMode;
  
  private final TextView largeLabel;
  
  private float scaleDownFactor;
  
  private float scaleUpFactor;
  
  private float shiftAmount;
  
  private final TextView smallLabel;
  
  public BottomNavigationItemView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationItemView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public BottomNavigationItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Resources resources = getResources();
    LayoutInflater.from(paramContext).inflate(R.layout.design_bottom_navigation_item, (ViewGroup)this, true);
    setBackgroundResource(R.drawable.design_bottom_navigation_item_background);
    this.defaultMargin = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin);
    this.icon = (ImageView)findViewById(R.id.icon);
    this.smallLabel = (TextView)findViewById(R.id.smallLabel);
    this.largeLabel = (TextView)findViewById(R.id.largeLabel);
    ViewCompat.setImportantForAccessibility((View)this.smallLabel, 2);
    ViewCompat.setImportantForAccessibility((View)this.largeLabel, 2);
    setFocusable(true);
    calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
  }
  
  private void calculateTextScaleFactors(float paramFloat1, float paramFloat2) {
    this.shiftAmount = paramFloat1 - paramFloat2;
    this.scaleUpFactor = paramFloat2 * 1.0F / paramFloat1;
    this.scaleDownFactor = 1.0F * paramFloat1 / paramFloat2;
  }
  
  private void setViewLayoutParams(View paramView, int paramInt1, int paramInt2) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)paramView.getLayoutParams();
    layoutParams.topMargin = paramInt1;
    layoutParams.gravity = paramInt2;
    paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  private void setViewValues(View paramView, float paramFloat1, float paramFloat2, int paramInt) {
    paramView.setScaleX(paramFloat1);
    paramView.setScaleY(paramFloat2);
    paramView.setVisibility(paramInt);
  }
  
  public MenuItemImpl getItemData() {
    return this.itemData;
  }
  
  public int getItemPosition() {
    return this.itemPosition;
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
    this.itemData = paramMenuItemImpl;
    setCheckable(paramMenuItemImpl.isCheckable());
    setChecked(paramMenuItemImpl.isChecked());
    setEnabled(paramMenuItemImpl.isEnabled());
    setIcon(paramMenuItemImpl.getIcon());
    setTitle(paramMenuItemImpl.getTitle());
    setId(paramMenuItemImpl.getItemId());
    if (!TextUtils.isEmpty(paramMenuItemImpl.getContentDescription()))
      setContentDescription(paramMenuItemImpl.getContentDescription()); 
    TooltipCompat.setTooltipText((View)this, paramMenuItemImpl.getTooltipText());
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    } 
    setVisibility(paramInt);
  }
  
  public int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  public boolean prefersCondensedTitle() {
    return false;
  }
  
  public void setCheckable(boolean paramBoolean) {
    refreshDrawableState();
  }
  
  public void setChecked(boolean paramBoolean) {
    TextView textView = this.largeLabel;
    textView.setPivotX((textView.getWidth() / 2));
    textView = this.largeLabel;
    textView.setPivotY(textView.getBaseline());
    textView = this.smallLabel;
    textView.setPivotX((textView.getWidth() / 2));
    textView = this.smallLabel;
    textView.setPivotY(textView.getBaseline());
    int i = this.labelVisibilityMode;
    if (i != -1) {
      if (i != 0) {
        if (i != 1) {
          if (i == 2) {
            setViewLayoutParams((View)this.icon, this.defaultMargin, 17);
            this.largeLabel.setVisibility(8);
            this.smallLabel.setVisibility(8);
          } 
        } else if (paramBoolean) {
          setViewLayoutParams((View)this.icon, (int)(this.defaultMargin + this.shiftAmount), 49);
          setViewValues((View)this.largeLabel, 1.0F, 1.0F, 0);
          textView = this.smallLabel;
          float f = this.scaleUpFactor;
          setViewValues((View)textView, f, f, 4);
        } else {
          setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
          textView = this.largeLabel;
          float f = this.scaleDownFactor;
          setViewValues((View)textView, f, f, 4);
          setViewValues((View)this.smallLabel, 1.0F, 1.0F, 0);
        } 
      } else {
        if (paramBoolean) {
          setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
          setViewValues((View)this.largeLabel, 1.0F, 1.0F, 0);
        } else {
          setViewLayoutParams((View)this.icon, this.defaultMargin, 17);
          setViewValues((View)this.largeLabel, 0.5F, 0.5F, 4);
        } 
        this.smallLabel.setVisibility(4);
      } 
    } else if (this.isShifting) {
      if (paramBoolean) {
        setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
        setViewValues((View)this.largeLabel, 1.0F, 1.0F, 0);
      } else {
        setViewLayoutParams((View)this.icon, this.defaultMargin, 17);
        setViewValues((View)this.largeLabel, 0.5F, 0.5F, 4);
      } 
      this.smallLabel.setVisibility(4);
    } else if (paramBoolean) {
      setViewLayoutParams((View)this.icon, (int)(this.defaultMargin + this.shiftAmount), 49);
      setViewValues((View)this.largeLabel, 1.0F, 1.0F, 0);
      textView = this.smallLabel;
      float f = this.scaleUpFactor;
      setViewValues((View)textView, f, f, 4);
    } else {
      setViewLayoutParams((View)this.icon, this.defaultMargin, 49);
      textView = this.largeLabel;
      float f = this.scaleDownFactor;
      setViewValues((View)textView, f, f, 4);
      setViewValues((View)this.smallLabel, 1.0F, 1.0F, 0);
    } 
    refreshDrawableState();
    setSelected(paramBoolean);
  }
  
  public void setEnabled(boolean paramBoolean) {
    super.setEnabled(paramBoolean);
    this.smallLabel.setEnabled(paramBoolean);
    this.largeLabel.setEnabled(paramBoolean);
    this.icon.setEnabled(paramBoolean);
    if (paramBoolean) {
      ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(getContext(), 1002));
    } else {
      ViewCompat.setPointerIcon((View)this, null);
    } 
  }
  
  public void setIcon(Drawable paramDrawable) {
    Drawable drawable = paramDrawable;
    if (paramDrawable != null) {
      Drawable.ConstantState constantState = paramDrawable.getConstantState();
      if (constantState != null)
        paramDrawable = constantState.newDrawable(); 
      drawable = DrawableCompat.wrap(paramDrawable).mutate();
      DrawableCompat.setTintList(drawable, this.iconTint);
    } 
    this.icon.setImageDrawable(drawable);
  }
  
  public void setIconSize(int paramInt) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.icon.getLayoutParams();
    layoutParams.width = paramInt;
    layoutParams.height = paramInt;
    this.icon.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  public void setIconTintList(ColorStateList paramColorStateList) {
    this.iconTint = paramColorStateList;
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl != null)
      setIcon(menuItemImpl.getIcon()); 
  }
  
  public void setItemBackground(int paramInt) {
    Drawable drawable;
    if (paramInt == 0) {
      drawable = null;
    } else {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } 
    setItemBackground(drawable);
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    ViewCompat.setBackground((View)this, paramDrawable);
  }
  
  public void setItemPosition(int paramInt) {
    this.itemPosition = paramInt;
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    if (this.labelVisibilityMode != paramInt) {
      this.labelVisibilityMode = paramInt;
      if (this.itemData != null) {
        paramInt = 1;
      } else {
        paramInt = 0;
      } 
      if (paramInt != 0)
        setChecked(this.itemData.isChecked()); 
    } 
  }
  
  public void setShifting(boolean paramBoolean) {
    if (this.isShifting != paramBoolean) {
      boolean bool;
      this.isShifting = paramBoolean;
      if (this.itemData != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool)
        setChecked(this.itemData.isChecked()); 
    } 
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar) {}
  
  public void setTextAppearanceActive(int paramInt) {
    TextViewCompat.setTextAppearance(this.largeLabel, paramInt);
    calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
  }
  
  public void setTextAppearanceInactive(int paramInt) {
    TextViewCompat.setTextAppearance(this.smallLabel, paramInt);
    calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
  }
  
  public void setTextColor(ColorStateList paramColorStateList) {
    if (paramColorStateList != null) {
      this.smallLabel.setTextColor(paramColorStateList);
      this.largeLabel.setTextColor(paramColorStateList);
    } 
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.smallLabel.setText(paramCharSequence);
    this.largeLabel.setText(paramCharSequence);
    MenuItemImpl menuItemImpl = this.itemData;
    if (menuItemImpl == null || TextUtils.isEmpty(menuItemImpl.getContentDescription()))
      setContentDescription(paramCharSequence); 
  }
  
  public boolean showsIcon() {
    return true;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/BottomNavigationItemView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */