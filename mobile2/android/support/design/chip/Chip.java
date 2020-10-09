package android.support.design.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.R;
import android.support.design.animation.MotionSpec;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.TextAppearance;
import android.support.design.ripple.RippleUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.text.BidiFormatter;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate {
  private static final int CLOSE_ICON_VIRTUAL_ID = 0;
  
  private static final Rect EMPTY_BOUNDS = new Rect();
  
  private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
  
  private static final int[] SELECTED_STATE = new int[] { 16842913 };
  
  private static final String TAG = "Chip";
  
  private ChipDrawable chipDrawable;
  
  private boolean closeIconFocused;
  
  private boolean closeIconHovered;
  
  private boolean closeIconPressed;
  
  private boolean deferredCheckedValue;
  
  private int focusedVirtualView = Integer.MIN_VALUE;
  
  private final ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback() {
      public void onFontRetrievalFailed(int param1Int) {}
      
      public void onFontRetrieved(Typeface param1Typeface) {
        Chip chip = Chip.this;
        chip.setText(chip.getText());
        Chip.this.requestLayout();
        Chip.this.invalidate();
      }
    };
  
  private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
  
  private View.OnClickListener onCloseIconClickListener;
  
  private final Rect rect = new Rect();
  
  private final RectF rectF = new RectF();
  
  private RippleDrawable ripple;
  
  private final ChipTouchHelper touchHelper;
  
  public Chip(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public Chip(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.chipStyle);
  }
  
  public Chip(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    validateAttributes(paramAttributeSet);
    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(paramContext, paramAttributeSet, paramInt, R.style.Widget_MaterialComponents_Chip_Action);
    setChipDrawable(chipDrawable);
    ChipTouchHelper chipTouchHelper = new ChipTouchHelper(this);
    this.touchHelper = chipTouchHelper;
    ViewCompat.setAccessibilityDelegate((View)this, (AccessibilityDelegateCompat)chipTouchHelper);
    initOutlineProvider();
    setChecked(this.deferredCheckedValue);
    chipDrawable.setShouldDrawText(false);
    setText(chipDrawable.getText());
    setEllipsize(chipDrawable.getEllipsize());
    setIncludeFontPadding(false);
    if (getTextAppearance() != null)
      updateTextPaintDrawState(getTextAppearance()); 
    setSingleLine();
    setGravity(8388627);
    updatePaddingInternal();
  }
  
  private void applyChipDrawable(ChipDrawable paramChipDrawable) {
    paramChipDrawable.setDelegate(this);
  }
  
  private float calculateTextOffsetFromStart(ChipDrawable paramChipDrawable) {
    float f = getChipStartPadding() + paramChipDrawable.calculateChipIconWidth() + getTextStartPadding();
    return (ViewCompat.getLayoutDirection((View)this) == 0) ? f : -f;
  }
  
  private int[] createCloseIconDrawableState() {
    int i = 0;
    if (isEnabled())
      i = 0 + 1; 
    int j = i;
    if (this.closeIconFocused)
      j = i + 1; 
    i = j;
    if (this.closeIconHovered)
      i = j + 1; 
    j = i;
    if (this.closeIconPressed)
      j = i + 1; 
    i = j;
    if (isChecked())
      i = j + 1; 
    int[] arrayOfInt = new int[i];
    i = 0;
    if (isEnabled()) {
      arrayOfInt[0] = 16842910;
      i = 0 + 1;
    } 
    j = i;
    if (this.closeIconFocused) {
      arrayOfInt[i] = 16842908;
      j = i + 1;
    } 
    i = j;
    if (this.closeIconHovered) {
      arrayOfInt[j] = 16843623;
      i = j + 1;
    } 
    j = i;
    if (this.closeIconPressed) {
      arrayOfInt[i] = 16842919;
      j = i + 1;
    } 
    if (isChecked())
      arrayOfInt[j] = 16842913; 
    return arrayOfInt;
  }
  
  private void ensureFocus() {
    if (this.focusedVirtualView == Integer.MIN_VALUE)
      setFocusedVirtualView(-1); 
  }
  
  private RectF getCloseIconTouchBounds() {
    this.rectF.setEmpty();
    if (hasCloseIcon())
      this.chipDrawable.getCloseIconTouchBounds(this.rectF); 
    return this.rectF;
  }
  
  private Rect getCloseIconTouchBoundsInt() {
    RectF rectF = getCloseIconTouchBounds();
    this.rect.set((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom);
    return this.rect;
  }
  
  private TextAppearance getTextAppearance() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      TextAppearance textAppearance = chipDrawable.getTextAppearance();
    } else {
      chipDrawable = null;
    } 
    return (TextAppearance)chipDrawable;
  }
  
  private boolean handleAccessibilityExit(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 10)
      try {
        Field field = ExploreByTouchHelper.class.getDeclaredField("mHoveredVirtualViewId");
        field.setAccessible(true);
        if (((Integer)field.get(this.touchHelper)).intValue() != Integer.MIN_VALUE) {
          Method method = ExploreByTouchHelper.class.getDeclaredMethod("updateHoveredVirtualView", new Class[] { int.class });
          method.setAccessible(true);
          method.invoke(this.touchHelper, new Object[] { Integer.valueOf(-2147483648) });
          return true;
        } 
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", noSuchMethodException);
      } catch (IllegalAccessException illegalAccessException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", illegalAccessException);
      } catch (InvocationTargetException invocationTargetException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", invocationTargetException);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", noSuchFieldException);
      }  
    return false;
  }
  
  private boolean hasCloseIcon() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.getCloseIcon() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void initOutlineProvider() {
    if (Build.VERSION.SDK_INT >= 21)
      setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View param1View, Outline param1Outline) {
              if (Chip.this.chipDrawable != null) {
                Chip.this.chipDrawable.getOutline(param1Outline);
              } else {
                param1Outline.setAlpha(0.0F);
              } 
            }
          }); 
  }
  
  private boolean moveFocus(boolean paramBoolean) {
    ensureFocus();
    boolean bool = false;
    if (paramBoolean) {
      paramBoolean = bool;
      if (this.focusedVirtualView == -1) {
        setFocusedVirtualView(0);
        paramBoolean = true;
      } 
    } else {
      paramBoolean = bool;
      if (this.focusedVirtualView == 0) {
        setFocusedVirtualView(-1);
        paramBoolean = true;
      } 
    } 
    return paramBoolean;
  }
  
  private void setCloseIconFocused(boolean paramBoolean) {
    if (this.closeIconFocused != paramBoolean) {
      this.closeIconFocused = paramBoolean;
      refreshDrawableState();
    } 
  }
  
  private void setCloseIconHovered(boolean paramBoolean) {
    if (this.closeIconHovered != paramBoolean) {
      this.closeIconHovered = paramBoolean;
      refreshDrawableState();
    } 
  }
  
  private void setCloseIconPressed(boolean paramBoolean) {
    if (this.closeIconPressed != paramBoolean) {
      this.closeIconPressed = paramBoolean;
      refreshDrawableState();
    } 
  }
  
  private void setFocusedVirtualView(int paramInt) {
    int i = this.focusedVirtualView;
    if (i != paramInt) {
      if (i == 0)
        setCloseIconFocused(false); 
      this.focusedVirtualView = paramInt;
      if (paramInt == 0)
        setCloseIconFocused(true); 
    } 
  }
  
  private void unapplyChipDrawable(ChipDrawable paramChipDrawable) {
    if (paramChipDrawable != null)
      paramChipDrawable.setDelegate((ChipDrawable.Delegate)null); 
  }
  
  private void updatePaddingInternal() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getText : ()Ljava/lang/CharSequence;
    //   4: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   7: ifne -> 209
    //   10: aload_0
    //   11: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   14: astore_1
    //   15: aload_1
    //   16: ifnonnull -> 22
    //   19: goto -> 209
    //   22: aload_1
    //   23: invokevirtual getChipStartPadding : ()F
    //   26: aload_0
    //   27: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   30: invokevirtual getChipEndPadding : ()F
    //   33: fadd
    //   34: aload_0
    //   35: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   38: invokevirtual getTextStartPadding : ()F
    //   41: fadd
    //   42: aload_0
    //   43: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   46: invokevirtual getTextEndPadding : ()F
    //   49: fadd
    //   50: fstore_2
    //   51: aload_0
    //   52: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   55: invokevirtual isChipIconVisible : ()Z
    //   58: ifeq -> 71
    //   61: aload_0
    //   62: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   65: invokevirtual getChipIcon : ()Landroid/graphics/drawable/Drawable;
    //   68: ifnonnull -> 104
    //   71: fload_2
    //   72: fstore_3
    //   73: aload_0
    //   74: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   77: invokevirtual getCheckedIcon : ()Landroid/graphics/drawable/Drawable;
    //   80: ifnull -> 130
    //   83: fload_2
    //   84: fstore_3
    //   85: aload_0
    //   86: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   89: invokevirtual isCheckedIconVisible : ()Z
    //   92: ifeq -> 130
    //   95: fload_2
    //   96: fstore_3
    //   97: aload_0
    //   98: invokevirtual isChecked : ()Z
    //   101: ifeq -> 130
    //   104: fload_2
    //   105: aload_0
    //   106: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   109: invokevirtual getIconStartPadding : ()F
    //   112: aload_0
    //   113: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   116: invokevirtual getIconEndPadding : ()F
    //   119: fadd
    //   120: aload_0
    //   121: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   124: invokevirtual getChipIconSize : ()F
    //   127: fadd
    //   128: fadd
    //   129: fstore_3
    //   130: fload_3
    //   131: fstore_2
    //   132: aload_0
    //   133: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   136: invokevirtual isCloseIconVisible : ()Z
    //   139: ifeq -> 180
    //   142: fload_3
    //   143: fstore_2
    //   144: aload_0
    //   145: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   148: invokevirtual getCloseIcon : ()Landroid/graphics/drawable/Drawable;
    //   151: ifnull -> 180
    //   154: fload_3
    //   155: aload_0
    //   156: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   159: invokevirtual getCloseIconStartPadding : ()F
    //   162: aload_0
    //   163: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   166: invokevirtual getCloseIconEndPadding : ()F
    //   169: fadd
    //   170: aload_0
    //   171: getfield chipDrawable : Landroid/support/design/chip/ChipDrawable;
    //   174: invokevirtual getCloseIconSize : ()F
    //   177: fadd
    //   178: fadd
    //   179: fstore_2
    //   180: aload_0
    //   181: invokestatic getPaddingEnd : (Landroid/view/View;)I
    //   184: i2f
    //   185: fload_2
    //   186: fcmpl
    //   187: ifeq -> 208
    //   190: aload_0
    //   191: aload_0
    //   192: invokestatic getPaddingStart : (Landroid/view/View;)I
    //   195: aload_0
    //   196: invokevirtual getPaddingTop : ()I
    //   199: fload_2
    //   200: f2i
    //   201: aload_0
    //   202: invokevirtual getPaddingBottom : ()I
    //   205: invokestatic setPaddingRelative : (Landroid/view/View;IIII)V
    //   208: return
    //   209: return
  }
  
  private void updateTextPaintDrawState(TextAppearance paramTextAppearance) {
    TextPaint textPaint = getPaint();
    textPaint.drawableState = this.chipDrawable.getState();
    paramTextAppearance.updateDrawState(getContext(), textPaint, this.fontCallback);
  }
  
  private void validateAttributes(AttributeSet paramAttributeSet) {
    if (paramAttributeSet == null)
      return; 
    if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") == null) {
      if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") == null) {
        if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") == null) {
          if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") == null) {
            if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") == null) {
              if (paramAttributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) && paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) == 1 && paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) == 1 && paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) == 1) {
                if (paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627)
                  Log.w("Chip", "Chip text must be vertically center and start aligned"); 
                return;
              } 
              throw new UnsupportedOperationException("Chip does not support multi-line text");
            } 
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
          } 
          throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        } 
        throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
      } 
      throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
    } 
    throw new UnsupportedOperationException("Do not set the background; Chip manages its own background drawable.");
  }
  
  protected boolean dispatchHoverEvent(MotionEvent paramMotionEvent) {
    return (handleAccessibilityExit(paramMotionEvent) || this.touchHelper.dispatchHoverEvent(paramMotionEvent) || super.dispatchHoverEvent(paramMotionEvent));
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (this.touchHelper.dispatchKeyEvent(paramKeyEvent) || super.dispatchKeyEvent(paramKeyEvent));
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    boolean bool1 = false;
    ChipDrawable chipDrawable = this.chipDrawable;
    boolean bool2 = bool1;
    if (chipDrawable != null) {
      bool2 = bool1;
      if (chipDrawable.isCloseIconStateful())
        bool2 = this.chipDrawable.setCloseIconState(createCloseIconDrawableState()); 
    } 
    if (bool2)
      invalidate(); 
  }
  
  public Drawable getCheckedIcon() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      Drawable drawable = chipDrawable.getCheckedIcon();
    } else {
      chipDrawable = null;
    } 
    return chipDrawable;
  }
  
  public ColorStateList getChipBackgroundColor() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getChipBackgroundColor();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public float getChipCornerRadius() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipCornerRadius();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public Drawable getChipDrawable() {
    return this.chipDrawable;
  }
  
  public float getChipEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public Drawable getChipIcon() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      Drawable drawable = chipDrawable.getChipIcon();
    } else {
      chipDrawable = null;
    } 
    return chipDrawable;
  }
  
  public float getChipIconSize() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipIconSize();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getChipIconTint() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getChipIconTint();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public float getChipMinHeight() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipMinHeight();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getChipStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getChipStrokeColor() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getChipStrokeColor();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public float getChipStrokeWidth() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipStrokeWidth();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  @Deprecated
  public CharSequence getChipText() {
    return getText();
  }
  
  public Drawable getCloseIcon() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      Drawable drawable = chipDrawable.getCloseIcon();
    } else {
      chipDrawable = null;
    } 
    return chipDrawable;
  }
  
  public CharSequence getCloseIconContentDescription() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      CharSequence charSequence = chipDrawable.getCloseIconContentDescription();
    } else {
      chipDrawable = null;
    } 
    return (CharSequence)chipDrawable;
  }
  
  public float getCloseIconEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getCloseIconEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getCloseIconSize() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getCloseIconSize();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getCloseIconStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getCloseIconStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getCloseIconTint() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getCloseIconTint();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public TextUtils.TruncateAt getEllipsize() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      TextUtils.TruncateAt truncateAt = chipDrawable.getEllipsize();
    } else {
      chipDrawable = null;
    } 
    return (TextUtils.TruncateAt)chipDrawable;
  }
  
  public void getFocusedRect(Rect paramRect) {
    if (this.focusedVirtualView == 0) {
      paramRect.set(getCloseIconTouchBoundsInt());
    } else {
      super.getFocusedRect(paramRect);
    } 
  }
  
  public MotionSpec getHideMotionSpec() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      MotionSpec motionSpec = chipDrawable.getHideMotionSpec();
    } else {
      chipDrawable = null;
    } 
    return (MotionSpec)chipDrawable;
  }
  
  public float getIconEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getIconEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getIconStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getIconStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getRippleColor() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getRippleColor();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public MotionSpec getShowMotionSpec() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      MotionSpec motionSpec = chipDrawable.getShowMotionSpec();
    } else {
      chipDrawable = null;
    } 
    return (MotionSpec)chipDrawable;
  }
  
  public CharSequence getText() {
    CharSequence charSequence;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      charSequence = chipDrawable.getText();
    } else {
      charSequence = "";
    } 
    return charSequence;
  }
  
  public float getTextEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getTextEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getTextStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getTextStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public boolean isCheckable() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isCheckable()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isCheckedIconEnabled() {
    return isCheckedIconVisible();
  }
  
  public boolean isCheckedIconVisible() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isCheckedIconVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isChipIconEnabled() {
    return isChipIconVisible();
  }
  
  public boolean isChipIconVisible() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isChipIconVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isCloseIconEnabled() {
    return isCloseIconVisible();
  }
  
  public boolean isCloseIconVisible() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isCloseIconVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onChipDrawableSizeChange() {
    updatePaddingInternal();
    requestLayout();
    if (Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, SELECTED_STATE); 
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (!TextUtils.isEmpty(getText())) {
      ChipDrawable chipDrawable = this.chipDrawable;
      if (chipDrawable != null && !chipDrawable.shouldDrawText()) {
        int i = paramCanvas.save();
        paramCanvas.translate(calculateTextOffsetFromStart(this.chipDrawable), 0.0F);
        super.onDraw(paramCanvas);
        paramCanvas.restoreToCount(i);
        return;
      } 
    } 
    super.onDraw(paramCanvas);
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
    if (paramBoolean) {
      setFocusedVirtualView(-1);
    } else {
      setFocusedVirtualView(-2147483648);
    } 
    invalidate();
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    this.touchHelper.onFocusChanged(paramBoolean, paramInt, paramRect);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionMasked();
    if (i != 7) {
      if (i == 10)
        setCloseIconHovered(false); 
    } else {
      setCloseIconHovered(getCloseIconTouchBounds().contains(paramMotionEvent.getX(), paramMotionEvent.getY()));
    } 
    return super.onHoverEvent(paramMotionEvent);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool2;
    boolean bool1 = false;
    int i = paramKeyEvent.getKeyCode();
    if (i != 61) {
      if (i != 66) {
        switch (i) {
          default:
            bool2 = bool1;
            break;
          case 22:
            bool2 = bool1;
            if (paramKeyEvent.hasNoModifiers())
              bool2 = moveFocus(ViewUtils.isLayoutRtl((View)this) ^ true); 
            break;
          case 21:
            bool2 = bool1;
            if (paramKeyEvent.hasNoModifiers())
              bool2 = moveFocus(ViewUtils.isLayoutRtl((View)this)); 
            break;
          case 23:
            i = this.focusedVirtualView;
            if (i != -1) {
              if (i != 0) {
                bool2 = bool1;
                break;
              } 
              performCloseIconClick();
              return true;
            } 
            performClick();
            return true;
        } 
      } else {
      
      } 
    } else {
      i = 0;
      if (paramKeyEvent.hasNoModifiers()) {
        i = 2;
      } else if (paramKeyEvent.hasModifiers(1)) {
        i = 1;
      } 
      bool2 = bool1;
      if (i != 0) {
        View view;
        ViewParent viewParent = getParent();
        Chip chip = this;
        while (true) {
          view = chip.focusSearch(i);
          if (view != null && view != this) {
            View view1 = view;
            if (view.getParent() != viewParent)
              break; 
            continue;
          } 
          break;
        } 
        bool2 = bool1;
        if (view != null) {
          view.requestFocus();
          return true;
        } 
      } 
    } 
    if (bool2) {
      invalidate();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt) {
    return (getCloseIconTouchBounds().contains(paramMotionEvent.getX(), paramMotionEvent.getY()) && isEnabled()) ? PointerIcon.getSystemIcon(getContext(), 1002) : null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_1
    //   5: invokevirtual getActionMasked : ()I
    //   8: istore #4
    //   10: aload_0
    //   11: invokespecial getCloseIconTouchBounds : ()Landroid/graphics/RectF;
    //   14: aload_1
    //   15: invokevirtual getX : ()F
    //   18: aload_1
    //   19: invokevirtual getY : ()F
    //   22: invokevirtual contains : (FF)Z
    //   25: istore #5
    //   27: iconst_0
    //   28: istore #6
    //   30: iload #4
    //   32: ifeq -> 104
    //   35: iload #4
    //   37: iconst_1
    //   38: if_icmpeq -> 82
    //   41: iload #4
    //   43: iconst_2
    //   44: if_icmpeq -> 58
    //   47: iload #4
    //   49: iconst_3
    //   50: if_icmpeq -> 96
    //   53: iload_2
    //   54: istore_3
    //   55: goto -> 118
    //   58: iload_2
    //   59: istore_3
    //   60: aload_0
    //   61: getfield closeIconPressed : Z
    //   64: ifeq -> 118
    //   67: iload #5
    //   69: ifne -> 77
    //   72: aload_0
    //   73: iconst_0
    //   74: invokespecial setCloseIconPressed : (Z)V
    //   77: iconst_1
    //   78: istore_3
    //   79: goto -> 118
    //   82: aload_0
    //   83: getfield closeIconPressed : Z
    //   86: ifeq -> 96
    //   89: aload_0
    //   90: invokevirtual performCloseIconClick : ()Z
    //   93: pop
    //   94: iconst_1
    //   95: istore_3
    //   96: aload_0
    //   97: iconst_0
    //   98: invokespecial setCloseIconPressed : (Z)V
    //   101: goto -> 118
    //   104: iload_2
    //   105: istore_3
    //   106: iload #5
    //   108: ifeq -> 118
    //   111: aload_0
    //   112: iconst_1
    //   113: invokespecial setCloseIconPressed : (Z)V
    //   116: iconst_1
    //   117: istore_3
    //   118: iload_3
    //   119: ifne -> 130
    //   122: aload_0
    //   123: aload_1
    //   124: invokespecial onTouchEvent : (Landroid/view/MotionEvent;)Z
    //   127: ifeq -> 133
    //   130: iconst_1
    //   131: istore #6
    //   133: iload #6
    //   135: ireturn
  }
  
  public boolean performCloseIconClick() {
    boolean bool;
    playSoundEffect(0);
    View.OnClickListener onClickListener = this.onCloseIconClickListener;
    if (onClickListener != null) {
      onClickListener.onClick((View)this);
      bool = true;
    } else {
      bool = false;
    } 
    this.touchHelper.sendEventForVirtualView(0, 1);
    return bool;
  }
  
  public void setBackground(Drawable paramDrawable) {
    if (paramDrawable == this.chipDrawable || paramDrawable == this.ripple) {
      super.setBackground(paramDrawable);
      return;
    } 
    throw new UnsupportedOperationException("Do not set the background; Chip manages its own background drawable.");
  }
  
  public void setBackgroundColor(int paramInt) {
    throw new UnsupportedOperationException("Do not set the background color; Chip manages its own background drawable.");
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.chipDrawable || paramDrawable == this.ripple) {
      super.setBackgroundDrawable(paramDrawable);
      return;
    } 
    throw new UnsupportedOperationException("Do not set the background drawable; Chip manages its own background drawable.");
  }
  
  public void setBackgroundResource(int paramInt) {
    throw new UnsupportedOperationException("Do not set the background resource; Chip manages its own background drawable.");
  }
  
  public void setBackgroundTintList(ColorStateList paramColorStateList) {
    throw new UnsupportedOperationException("Do not set the background tint list; Chip manages its own background drawable.");
  }
  
  public void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    throw new UnsupportedOperationException("Do not set the background tint mode; Chip manages its own background drawable.");
  }
  
  public void setCheckable(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckable(paramBoolean); 
  }
  
  public void setCheckableResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckableResource(paramInt); 
  }
  
  public void setChecked(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable == null) {
      this.deferredCheckedValue = paramBoolean;
    } else if (chipDrawable.isCheckable()) {
      boolean bool = isChecked();
      super.setChecked(paramBoolean);
      if (bool != paramBoolean) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListenerInternal;
        if (onCheckedChangeListener != null)
          onCheckedChangeListener.onCheckedChanged((CompoundButton)this, paramBoolean); 
      } 
    } 
  }
  
  public void setCheckedIcon(Drawable paramDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIcon(paramDrawable); 
  }
  
  @Deprecated
  public void setCheckedIconEnabled(boolean paramBoolean) {
    setCheckedIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setCheckedIconEnabledResource(int paramInt) {
    setCheckedIconVisible(paramInt);
  }
  
  public void setCheckedIconResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconResource(paramInt); 
  }
  
  public void setCheckedIconVisible(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconVisible(paramInt); 
  }
  
  public void setCheckedIconVisible(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconVisible(paramBoolean); 
  }
  
  public void setChipBackgroundColor(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipBackgroundColor(paramColorStateList); 
  }
  
  public void setChipBackgroundColorResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipBackgroundColorResource(paramInt); 
  }
  
  public void setChipCornerRadius(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipCornerRadius(paramFloat); 
  }
  
  public void setChipCornerRadiusResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipCornerRadiusResource(paramInt); 
  }
  
  public void setChipDrawable(ChipDrawable paramChipDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != paramChipDrawable) {
      unapplyChipDrawable(chipDrawable);
      this.chipDrawable = paramChipDrawable;
      applyChipDrawable(paramChipDrawable);
      if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
        this.ripple = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(this.chipDrawable.getRippleColor()), this.chipDrawable, null);
        this.chipDrawable.setUseCompatRipple(false);
        ViewCompat.setBackground((View)this, (Drawable)this.ripple);
      } else {
        this.chipDrawable.setUseCompatRipple(true);
        ViewCompat.setBackground((View)this, this.chipDrawable);
      } 
    } 
  }
  
  public void setChipEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipEndPadding(paramFloat); 
  }
  
  public void setChipEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipEndPaddingResource(paramInt); 
  }
  
  public void setChipIcon(Drawable paramDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIcon(paramDrawable); 
  }
  
  @Deprecated
  public void setChipIconEnabled(boolean paramBoolean) {
    setChipIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setChipIconEnabledResource(int paramInt) {
    setChipIconVisible(paramInt);
  }
  
  public void setChipIconResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconResource(paramInt); 
  }
  
  public void setChipIconSize(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconSize(paramFloat); 
  }
  
  public void setChipIconSizeResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconSizeResource(paramInt); 
  }
  
  public void setChipIconTint(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconTint(paramColorStateList); 
  }
  
  public void setChipIconTintResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconTintResource(paramInt); 
  }
  
  public void setChipIconVisible(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconVisible(paramInt); 
  }
  
  public void setChipIconVisible(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconVisible(paramBoolean); 
  }
  
  public void setChipMinHeight(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipMinHeight(paramFloat); 
  }
  
  public void setChipMinHeightResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipMinHeightResource(paramInt); 
  }
  
  public void setChipStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStartPadding(paramFloat); 
  }
  
  public void setChipStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStartPaddingResource(paramInt); 
  }
  
  public void setChipStrokeColor(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeColor(paramColorStateList); 
  }
  
  public void setChipStrokeColorResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeColorResource(paramInt); 
  }
  
  public void setChipStrokeWidth(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeWidth(paramFloat); 
  }
  
  public void setChipStrokeWidthResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeWidthResource(paramInt); 
  }
  
  @Deprecated
  public void setChipText(CharSequence paramCharSequence) {
    setText(paramCharSequence);
  }
  
  @Deprecated
  public void setChipTextResource(int paramInt) {
    setText(getResources().getString(paramInt));
  }
  
  public void setCloseIcon(Drawable paramDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIcon(paramDrawable); 
  }
  
  public void setCloseIconContentDescription(CharSequence paramCharSequence) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconContentDescription(paramCharSequence); 
  }
  
  @Deprecated
  public void setCloseIconEnabled(boolean paramBoolean) {
    setCloseIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setCloseIconEnabledResource(int paramInt) {
    setCloseIconVisible(paramInt);
  }
  
  public void setCloseIconEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconEndPadding(paramFloat); 
  }
  
  public void setCloseIconEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconEndPaddingResource(paramInt); 
  }
  
  public void setCloseIconResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconResource(paramInt); 
  }
  
  public void setCloseIconSize(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconSize(paramFloat); 
  }
  
  public void setCloseIconSizeResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconSizeResource(paramInt); 
  }
  
  public void setCloseIconStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconStartPadding(paramFloat); 
  }
  
  public void setCloseIconStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconStartPaddingResource(paramInt); 
  }
  
  public void setCloseIconTint(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconTint(paramColorStateList); 
  }
  
  public void setCloseIconTintResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconTintResource(paramInt); 
  }
  
  public void setCloseIconVisible(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconVisible(paramInt); 
  }
  
  public void setCloseIconVisible(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconVisible(paramBoolean); 
  }
  
  public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesRelative(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawablesRelative(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesRelativeWithIntrinsicBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt1 == 0) {
      if (paramInt3 == 0) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesWithIntrinsicBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt1 == 0) {
      if (paramInt3 == 0) {
        super.setCompoundDrawablesWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesWithIntrinsicBounds(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawablesWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
  }
  
  public void setEllipsize(TextUtils.TruncateAt paramTruncateAt) {
    if (this.chipDrawable == null)
      return; 
    if (paramTruncateAt != TextUtils.TruncateAt.MARQUEE) {
      super.setEllipsize(paramTruncateAt);
      ChipDrawable chipDrawable = this.chipDrawable;
      if (chipDrawable != null)
        chipDrawable.setEllipsize(paramTruncateAt); 
      return;
    } 
    throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
  }
  
  public void setGravity(int paramInt) {
    if (paramInt != 8388627) {
      Log.w("Chip", "Chip text must be vertically center and start aligned");
    } else {
      super.setGravity(paramInt);
    } 
  }
  
  public void setHideMotionSpec(MotionSpec paramMotionSpec) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setHideMotionSpec(paramMotionSpec); 
  }
  
  public void setHideMotionSpecResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setHideMotionSpecResource(paramInt); 
  }
  
  public void setIconEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconEndPadding(paramFloat); 
  }
  
  public void setIconEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconEndPaddingResource(paramInt); 
  }
  
  public void setIconStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconStartPadding(paramFloat); 
  }
  
  public void setIconStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconStartPaddingResource(paramInt); 
  }
  
  public void setLines(int paramInt) {
    if (paramInt <= 1) {
      super.setLines(paramInt);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  public void setMaxLines(int paramInt) {
    if (paramInt <= 1) {
      super.setMaxLines(paramInt);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  public void setMaxWidth(int paramInt) {
    super.setMaxWidth(paramInt);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setMaxWidth(paramInt); 
  }
  
  public void setMinLines(int paramInt) {
    if (paramInt <= 1) {
      super.setMinLines(paramInt);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  void setOnCheckedChangeListenerInternal(CompoundButton.OnCheckedChangeListener paramOnCheckedChangeListener) {
    this.onCheckedChangeListenerInternal = paramOnCheckedChangeListener;
  }
  
  public void setOnCloseIconClickListener(View.OnClickListener paramOnClickListener) {
    this.onCloseIconClickListener = paramOnClickListener;
  }
  
  public void setRippleColor(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setRippleColor(paramColorStateList); 
  }
  
  public void setRippleColorResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setRippleColorResource(paramInt); 
  }
  
  public void setShowMotionSpec(MotionSpec paramMotionSpec) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setShowMotionSpec(paramMotionSpec); 
  }
  
  public void setShowMotionSpecResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setShowMotionSpecResource(paramInt); 
  }
  
  public void setSingleLine(boolean paramBoolean) {
    if (paramBoolean) {
      super.setSingleLine(paramBoolean);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  public void setText(CharSequence paramCharSequence, TextView.BufferType paramBufferType) {
    if (this.chipDrawable == null)
      return; 
    CharSequence charSequence = paramCharSequence;
    if (paramCharSequence == null)
      charSequence = ""; 
    paramCharSequence = BidiFormatter.getInstance().unicodeWrap(charSequence);
    if (this.chipDrawable.shouldDrawText())
      paramCharSequence = null; 
    super.setText(paramCharSequence, paramBufferType);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setText(charSequence); 
  }
  
  public void setTextAppearance(int paramInt) {
    super.setTextAppearance(paramInt);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearanceResource(paramInt); 
    if (getTextAppearance() != null) {
      getTextAppearance().updateMeasureState(getContext(), getPaint(), this.fontCallback);
      updateTextPaintDrawState(getTextAppearance());
    } 
  }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearanceResource(paramInt); 
    if (getTextAppearance() != null) {
      getTextAppearance().updateMeasureState(paramContext, getPaint(), this.fontCallback);
      updateTextPaintDrawState(getTextAppearance());
    } 
  }
  
  public void setTextAppearance(TextAppearance paramTextAppearance) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearance(paramTextAppearance); 
    if (getTextAppearance() != null) {
      getTextAppearance().updateMeasureState(getContext(), getPaint(), this.fontCallback);
      updateTextPaintDrawState(paramTextAppearance);
    } 
  }
  
  public void setTextAppearanceResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearanceResource(paramInt); 
    setTextAppearance(getContext(), paramInt);
  }
  
  public void setTextEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextEndPadding(paramFloat); 
  }
  
  public void setTextEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextEndPaddingResource(paramInt); 
  }
  
  public void setTextStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextStartPadding(paramFloat); 
  }
  
  public void setTextStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextStartPaddingResource(paramInt); 
  }
  
  private class ChipTouchHelper extends ExploreByTouchHelper {
    ChipTouchHelper(Chip param1Chip1) {
      super((View)param1Chip1);
    }
    
    protected int getVirtualViewAt(float param1Float1, float param1Float2) {
      byte b;
      if (Chip.this.hasCloseIcon() && Chip.this.getCloseIconTouchBounds().contains(param1Float1, param1Float2)) {
        b = 0;
      } else {
        b = -1;
      } 
      return b;
    }
    
    protected void getVisibleVirtualViews(List<Integer> param1List) {
      if (Chip.this.hasCloseIcon())
        param1List.add(Integer.valueOf(0)); 
    }
    
    protected boolean onPerformActionForVirtualView(int param1Int1, int param1Int2, Bundle param1Bundle) {
      return (param1Int2 == 16 && param1Int1 == 0) ? Chip.this.performCloseIconClick() : false;
    }
    
    protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool;
      if (Chip.this.chipDrawable != null && Chip.this.chipDrawable.isCheckable()) {
        bool = true;
      } else {
        bool = false;
      } 
      param1AccessibilityNodeInfoCompat.setCheckable(bool);
      param1AccessibilityNodeInfoCompat.setClassName(Chip.class.getName());
      CharSequence charSequence = Chip.this.getText();
      if (Build.VERSION.SDK_INT >= 23) {
        param1AccessibilityNodeInfoCompat.setText(charSequence);
      } else {
        param1AccessibilityNodeInfoCompat.setContentDescription(charSequence);
      } 
    }
    
    protected void onPopulateNodeForVirtualView(int param1Int, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool = Chip.this.hasCloseIcon();
      String str = "";
      if (bool) {
        CharSequence charSequence = Chip.this.getCloseIconContentDescription();
        if (charSequence != null) {
          param1AccessibilityNodeInfoCompat.setContentDescription(charSequence);
        } else {
          CharSequence charSequence1;
          charSequence = Chip.this.getText();
          Context context = Chip.this.getContext();
          param1Int = R.string.mtrl_chip_close_icon_content_description;
          if (!TextUtils.isEmpty(charSequence))
            charSequence1 = charSequence; 
          param1AccessibilityNodeInfoCompat.setContentDescription(context.getString(param1Int, new Object[] { charSequence1 }).trim());
        } 
        param1AccessibilityNodeInfoCompat.setBoundsInParent(Chip.this.getCloseIconTouchBoundsInt());
        param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
        param1AccessibilityNodeInfoCompat.setEnabled(Chip.this.isEnabled());
      } else {
        param1AccessibilityNodeInfoCompat.setContentDescription("");
        param1AccessibilityNodeInfoCompat.setBoundsInParent(Chip.EMPTY_BOUNDS);
      } 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/chip/Chip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */