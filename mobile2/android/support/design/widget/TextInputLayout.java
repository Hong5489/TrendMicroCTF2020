package android.support.design.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.TintTypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TextInputLayout extends LinearLayout {
  public static final int BOX_BACKGROUND_FILLED = 1;
  
  public static final int BOX_BACKGROUND_NONE = 0;
  
  public static final int BOX_BACKGROUND_OUTLINE = 2;
  
  private static final int INVALID_MAX_LENGTH = -1;
  
  private static final int LABEL_SCALE_ANIMATION_DURATION = 167;
  
  private static final String LOG_TAG = "TextInputLayout";
  
  private ValueAnimator animator;
  
  private GradientDrawable boxBackground;
  
  private int boxBackgroundColor;
  
  private int boxBackgroundMode;
  
  private final int boxBottomOffsetPx;
  
  private final int boxCollapsedPaddingTopPx;
  
  private float boxCornerRadiusBottomEnd;
  
  private float boxCornerRadiusBottomStart;
  
  private float boxCornerRadiusTopEnd;
  
  private float boxCornerRadiusTopStart;
  
  private final int boxLabelCutoutPaddingPx;
  
  private int boxStrokeColor;
  
  private final int boxStrokeWidthDefaultPx;
  
  private final int boxStrokeWidthFocusedPx;
  
  private int boxStrokeWidthPx;
  
  final CollapsingTextHelper collapsingTextHelper = new CollapsingTextHelper((View)this);
  
  boolean counterEnabled;
  
  private int counterMaxLength;
  
  private final int counterOverflowTextAppearance;
  
  private boolean counterOverflowed;
  
  private final int counterTextAppearance;
  
  private TextView counterView;
  
  private ColorStateList defaultHintTextColor;
  
  private final int defaultStrokeColor;
  
  private final int disabledColor;
  
  EditText editText;
  
  private Drawable editTextOriginalDrawable;
  
  private int focusedStrokeColor;
  
  private ColorStateList focusedTextColor;
  
  private boolean hasPasswordToggleTintList;
  
  private boolean hasPasswordToggleTintMode;
  
  private boolean hasReconstructedEditTextBackground;
  
  private CharSequence hint;
  
  private boolean hintAnimationEnabled;
  
  private boolean hintEnabled;
  
  private boolean hintExpanded;
  
  private final int hoveredStrokeColor;
  
  private boolean inDrawableStateChanged;
  
  private final IndicatorViewController indicatorViewController = new IndicatorViewController(this);
  
  private final FrameLayout inputFrame;
  
  private boolean isProvidingHint;
  
  private Drawable originalEditTextEndDrawable;
  
  private CharSequence originalHint;
  
  private CharSequence passwordToggleContentDesc;
  
  private Drawable passwordToggleDrawable;
  
  private Drawable passwordToggleDummyDrawable;
  
  private boolean passwordToggleEnabled;
  
  private ColorStateList passwordToggleTintList;
  
  private PorterDuff.Mode passwordToggleTintMode;
  
  private CheckableImageButton passwordToggleView;
  
  private boolean passwordToggledVisible;
  
  private boolean restoringSavedState;
  
  private final Rect tmpRect = new Rect();
  
  private final RectF tmpRectF = new RectF();
  
  private Typeface typeface;
  
  public TextInputLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TextInputLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.textInputStyle);
  }
  
  public TextInputLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setOrientation(1);
    setWillNotDraw(false);
    setAddStatesFromChildren(true);
    FrameLayout frameLayout = new FrameLayout(paramContext);
    this.inputFrame = frameLayout;
    frameLayout.setAddStatesFromChildren(true);
    addView((View)this.inputFrame);
    this.collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    this.collapsingTextHelper.setPositionInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    this.collapsingTextHelper.setCollapsedTextGravity(8388659);
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(paramContext, paramAttributeSet, R.styleable.TextInputLayout, paramInt, R.style.Widget_Design_TextInputLayout, new int[0]);
    this.hintEnabled = tintTypedArray.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
    setHint(tintTypedArray.getText(R.styleable.TextInputLayout_android_hint));
    this.hintAnimationEnabled = tintTypedArray.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
    this.boxBottomOffsetPx = paramContext.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_bottom_offset);
    this.boxLabelCutoutPaddingPx = paramContext.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding);
    this.boxCollapsedPaddingTopPx = tintTypedArray.getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
    this.boxCornerRadiusTopStart = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, 0.0F);
    this.boxCornerRadiusTopEnd = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, 0.0F);
    this.boxCornerRadiusBottomEnd = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, 0.0F);
    this.boxCornerRadiusBottomStart = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, 0.0F);
    this.boxBackgroundColor = tintTypedArray.getColor(R.styleable.TextInputLayout_boxBackgroundColor, 0);
    this.focusedStrokeColor = tintTypedArray.getColor(R.styleable.TextInputLayout_boxStrokeColor, 0);
    this.boxStrokeWidthDefaultPx = paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default);
    this.boxStrokeWidthFocusedPx = paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused);
    this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
    setBoxBackgroundMode(tintTypedArray.getInt(R.styleable.TextInputLayout_boxBackgroundMode, 0));
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
      ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
      this.focusedTextColor = colorStateList;
      this.defaultHintTextColor = colorStateList;
    } 
    this.defaultStrokeColor = ContextCompat.getColor(paramContext, R.color.mtrl_textinput_default_box_stroke_color);
    this.disabledColor = ContextCompat.getColor(paramContext, R.color.mtrl_textinput_disabled_color);
    this.hoveredStrokeColor = ContextCompat.getColor(paramContext, R.color.mtrl_textinput_hovered_box_stroke_color);
    if (tintTypedArray.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1)
      setHintTextAppearance(tintTypedArray.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0)); 
    paramInt = tintTypedArray.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
    boolean bool1 = tintTypedArray.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
    int i = tintTypedArray.getResourceId(R.styleable.TextInputLayout_helperTextTextAppearance, 0);
    boolean bool2 = tintTypedArray.getBoolean(R.styleable.TextInputLayout_helperTextEnabled, false);
    CharSequence charSequence = tintTypedArray.getText(R.styleable.TextInputLayout_helperText);
    boolean bool3 = tintTypedArray.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
    setCounterMaxLength(tintTypedArray.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
    this.counterTextAppearance = tintTypedArray.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
    this.counterOverflowTextAppearance = tintTypedArray.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
    this.passwordToggleEnabled = tintTypedArray.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
    this.passwordToggleDrawable = tintTypedArray.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
    this.passwordToggleContentDesc = tintTypedArray.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
      this.hasPasswordToggleTintList = true;
      this.passwordToggleTintList = tintTypedArray.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
    } 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
      this.hasPasswordToggleTintMode = true;
      this.passwordToggleTintMode = ViewUtils.parseTintMode(tintTypedArray.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null);
    } 
    tintTypedArray.recycle();
    setHelperTextEnabled(bool2);
    setHelperText(charSequence);
    setHelperTextTextAppearance(i);
    setErrorEnabled(bool1);
    setErrorTextAppearance(paramInt);
    setCounterEnabled(bool3);
    applyPasswordToggleTint();
    ViewCompat.setImportantForAccessibility((View)this, 2);
  }
  
  private void applyBoxAttributes() {
    if (this.boxBackground == null)
      return; 
    setBoxAttributes();
    EditText editText = this.editText;
    if (editText != null && this.boxBackgroundMode == 2) {
      if (editText.getBackground() != null)
        this.editTextOriginalDrawable = this.editText.getBackground(); 
      ViewCompat.setBackground((View)this.editText, null);
    } 
    editText = this.editText;
    if (editText != null && this.boxBackgroundMode == 1) {
      Drawable drawable = this.editTextOriginalDrawable;
      if (drawable != null)
        ViewCompat.setBackground((View)editText, drawable); 
    } 
    int i = this.boxStrokeWidthPx;
    if (i > -1) {
      int j = this.boxStrokeColor;
      if (j != 0)
        this.boxBackground.setStroke(i, j); 
    } 
    this.boxBackground.setCornerRadii(getCornerRadiiAsArray());
    this.boxBackground.setColor(this.boxBackgroundColor);
    invalidate();
  }
  
  private void applyCutoutPadding(RectF paramRectF) {
    paramRectF.left -= this.boxLabelCutoutPaddingPx;
    paramRectF.top -= this.boxLabelCutoutPaddingPx;
    paramRectF.right += this.boxLabelCutoutPaddingPx;
    paramRectF.bottom += this.boxLabelCutoutPaddingPx;
  }
  
  private void applyPasswordToggleTint() {
    if (this.passwordToggleDrawable != null && (this.hasPasswordToggleTintList || this.hasPasswordToggleTintMode)) {
      Drawable drawable = DrawableCompat.wrap(this.passwordToggleDrawable).mutate();
      this.passwordToggleDrawable = drawable;
      if (this.hasPasswordToggleTintList)
        DrawableCompat.setTintList(drawable, this.passwordToggleTintList); 
      if (this.hasPasswordToggleTintMode)
        DrawableCompat.setTintMode(this.passwordToggleDrawable, this.passwordToggleTintMode); 
      CheckableImageButton checkableImageButton = this.passwordToggleView;
      if (checkableImageButton != null) {
        Drawable drawable2 = checkableImageButton.getDrawable();
        Drawable drawable1 = this.passwordToggleDrawable;
        if (drawable2 != drawable1)
          this.passwordToggleView.setImageDrawable(drawable1); 
      } 
    } 
  }
  
  private void assignBoxBackgroundByMode() {
    int i = this.boxBackgroundMode;
    if (i == 0) {
      this.boxBackground = null;
    } else if (i == 2 && this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable)) {
      this.boxBackground = new CutoutDrawable();
    } else if (!(this.boxBackground instanceof GradientDrawable)) {
      this.boxBackground = new GradientDrawable();
    } 
  }
  
  private int calculateBoxBackgroundTop() {
    EditText editText = this.editText;
    if (editText == null)
      return 0; 
    int i = this.boxBackgroundMode;
    return (i != 1) ? ((i != 2) ? 0 : (editText.getTop() + calculateLabelMarginTop())) : editText.getTop();
  }
  
  private int calculateCollapsedTextTopBounds() {
    int i = this.boxBackgroundMode;
    return (i != 1) ? ((i != 2) ? getPaddingTop() : ((getBoxBackground().getBounds()).top - calculateLabelMarginTop())) : ((getBoxBackground().getBounds()).top + this.boxCollapsedPaddingTopPx);
  }
  
  private int calculateLabelMarginTop() {
    if (!this.hintEnabled)
      return 0; 
    int i = this.boxBackgroundMode;
    return (i != 0 && i != 1) ? ((i != 2) ? 0 : (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0F)) : (int)this.collapsingTextHelper.getCollapsedTextHeight();
  }
  
  private void closeCutout() {
    if (cutoutEnabled())
      ((CutoutDrawable)this.boxBackground).removeCutout(); 
  }
  
  private void collapseHint(boolean paramBoolean) {
    ValueAnimator valueAnimator = this.animator;
    if (valueAnimator != null && valueAnimator.isRunning())
      this.animator.cancel(); 
    if (paramBoolean && this.hintAnimationEnabled) {
      animateToExpansionFraction(1.0F);
    } else {
      this.collapsingTextHelper.setExpansionFraction(1.0F);
    } 
    this.hintExpanded = false;
    if (cutoutEnabled())
      openCutout(); 
  }
  
  private boolean cutoutEnabled() {
    boolean bool;
    if (this.hintEnabled && !TextUtils.isEmpty(this.hint) && this.boxBackground instanceof CutoutDrawable) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void ensureBackgroundDrawableStateWorkaround() {
    int i = Build.VERSION.SDK_INT;
    if (i != 21 && i != 22)
      return; 
    Drawable drawable = this.editText.getBackground();
    if (drawable == null)
      return; 
    if (!this.hasReconstructedEditTextBackground) {
      Drawable drawable1 = drawable.getConstantState().newDrawable();
      if (drawable instanceof DrawableContainer)
        this.hasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)drawable, drawable1.getConstantState()); 
      if (!this.hasReconstructedEditTextBackground) {
        ViewCompat.setBackground((View)this.editText, drawable1);
        this.hasReconstructedEditTextBackground = true;
        onApplyBoxBackgroundMode();
      } 
    } 
  }
  
  private void expandHint(boolean paramBoolean) {
    ValueAnimator valueAnimator = this.animator;
    if (valueAnimator != null && valueAnimator.isRunning())
      this.animator.cancel(); 
    if (paramBoolean && this.hintAnimationEnabled) {
      animateToExpansionFraction(0.0F);
    } else {
      this.collapsingTextHelper.setExpansionFraction(0.0F);
    } 
    if (cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout())
      closeCutout(); 
    this.hintExpanded = true;
  }
  
  private Drawable getBoxBackground() {
    int i = this.boxBackgroundMode;
    if (i == 1 || i == 2)
      return (Drawable)this.boxBackground; 
    throw new IllegalStateException();
  }
  
  private float[] getCornerRadiiAsArray() {
    if (!ViewUtils.isLayoutRtl((View)this)) {
      float f5 = this.boxCornerRadiusTopStart;
      float f6 = this.boxCornerRadiusTopEnd;
      float f7 = this.boxCornerRadiusBottomEnd;
      float f8 = this.boxCornerRadiusBottomStart;
      return new float[] { f5, f5, f6, f6, f7, f7, f8, f8 };
    } 
    float f2 = this.boxCornerRadiusTopEnd;
    float f3 = this.boxCornerRadiusTopStart;
    float f4 = this.boxCornerRadiusBottomStart;
    float f1 = this.boxCornerRadiusBottomEnd;
    return new float[] { f2, f2, f3, f3, f4, f4, f1, f1 };
  }
  
  private boolean hasPasswordTransformation() {
    boolean bool;
    EditText editText = this.editText;
    if (editText != null && editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void onApplyBoxBackgroundMode() {
    assignBoxBackgroundByMode();
    if (this.boxBackgroundMode != 0)
      updateInputLayoutMargins(); 
    updateTextInputBoxBounds();
  }
  
  private void openCutout() {
    if (!cutoutEnabled())
      return; 
    RectF rectF = this.tmpRectF;
    this.collapsingTextHelper.getCollapsedTextActualBounds(rectF);
    applyCutoutPadding(rectF);
    ((CutoutDrawable)this.boxBackground).setCutout(rectF);
  }
  
  private static void recursiveSetEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {
    byte b = 0;
    int i = paramViewGroup.getChildCount();
    while (b < i) {
      View view = paramViewGroup.getChildAt(b);
      view.setEnabled(paramBoolean);
      if (view instanceof ViewGroup)
        recursiveSetEnabled((ViewGroup)view, paramBoolean); 
      b++;
    } 
  }
  
  private void setBoxAttributes() {
    int i = this.boxBackgroundMode;
    if (i != 1) {
      if (i == 2 && this.focusedStrokeColor == 0)
        this.focusedStrokeColor = this.focusedTextColor.getColorForState(getDrawableState(), this.focusedTextColor.getDefaultColor()); 
    } else {
      this.boxStrokeWidthPx = 0;
    } 
  }
  
  private void setEditText(EditText paramEditText) {
    if (this.editText == null) {
      if (!(paramEditText instanceof TextInputEditText))
        Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead."); 
      this.editText = paramEditText;
      onApplyBoxBackgroundMode();
      setTextInputAccessibilityDelegate(new AccessibilityDelegate(this));
      if (!hasPasswordTransformation())
        this.collapsingTextHelper.setTypefaces(this.editText.getTypeface()); 
      this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
      int i = this.editText.getGravity();
      this.collapsingTextHelper.setCollapsedTextGravity(i & 0xFFFFFF8F | 0x30);
      this.collapsingTextHelper.setExpandedTextGravity(i);
      this.editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable param1Editable) {
              TextInputLayout textInputLayout = TextInputLayout.this;
              textInputLayout.updateLabelState(textInputLayout.restoringSavedState ^ true);
              if (TextInputLayout.this.counterEnabled)
                TextInputLayout.this.updateCounter(param1Editable.length()); 
            }
            
            public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
            
            public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          });
      if (this.defaultHintTextColor == null)
        this.defaultHintTextColor = this.editText.getHintTextColors(); 
      if (this.hintEnabled) {
        if (TextUtils.isEmpty(this.hint)) {
          CharSequence charSequence = this.editText.getHint();
          this.originalHint = charSequence;
          setHint(charSequence);
          this.editText.setHint(null);
        } 
        this.isProvidingHint = true;
      } 
      if (this.counterView != null)
        updateCounter(this.editText.getText().length()); 
      this.indicatorViewController.adjustIndicatorPadding();
      updatePasswordToggleView();
      updateLabelState(false, true);
      return;
    } 
    throw new IllegalArgumentException("We already have an EditText, can only have one");
  }
  
  private void setHintInternal(CharSequence paramCharSequence) {
    if (!TextUtils.equals(paramCharSequence, this.hint)) {
      this.hint = paramCharSequence;
      this.collapsingTextHelper.setText(paramCharSequence);
      if (!this.hintExpanded)
        openCutout(); 
    } 
  }
  
  private boolean shouldShowPasswordIcon() {
    boolean bool;
    if (this.passwordToggleEnabled && (hasPasswordTransformation() || this.passwordToggledVisible)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void updateEditTextBackgroundBounds() {
    EditText editText = this.editText;
    if (editText == null)
      return; 
    Drawable drawable2 = editText.getBackground();
    if (drawable2 == null)
      return; 
    Drawable drawable1 = drawable2;
    if (DrawableUtils.canSafelyMutateDrawable(drawable2))
      drawable1 = drawable2.mutate(); 
    Rect rect = new Rect();
    DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)this.editText, rect);
    rect = drawable1.getBounds();
    if (rect.left != rect.right) {
      Rect rect1 = new Rect();
      drawable1.getPadding(rect1);
      int i = rect.left;
      int j = rect1.left;
      int k = rect.right;
      int m = rect1.right;
      drawable1.setBounds(i - j, rect.top, k + m * 2, this.editText.getBottom());
    } 
  }
  
  private void updateInputLayoutMargins() {
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.inputFrame.getLayoutParams();
    int i = calculateLabelMarginTop();
    if (i != layoutParams.topMargin) {
      layoutParams.topMargin = i;
      this.inputFrame.requestLayout();
    } 
  }
  
  private void updateLabelState(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isEnabled : ()Z
    //   4: istore_3
    //   5: aload_0
    //   6: getfield editText : Landroid/widget/EditText;
    //   9: astore #4
    //   11: iconst_1
    //   12: istore #5
    //   14: aload #4
    //   16: ifnull -> 36
    //   19: aload #4
    //   21: invokevirtual getText : ()Landroid/text/Editable;
    //   24: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   27: ifne -> 36
    //   30: iconst_1
    //   31: istore #6
    //   33: goto -> 39
    //   36: iconst_0
    //   37: istore #6
    //   39: aload_0
    //   40: getfield editText : Landroid/widget/EditText;
    //   43: astore #4
    //   45: aload #4
    //   47: ifnull -> 61
    //   50: aload #4
    //   52: invokevirtual hasFocus : ()Z
    //   55: ifeq -> 61
    //   58: goto -> 64
    //   61: iconst_0
    //   62: istore #5
    //   64: aload_0
    //   65: getfield indicatorViewController : Landroid/support/design/widget/IndicatorViewController;
    //   68: invokevirtual errorShouldBeShown : ()Z
    //   71: istore #7
    //   73: aload_0
    //   74: getfield defaultHintTextColor : Landroid/content/res/ColorStateList;
    //   77: astore #4
    //   79: aload #4
    //   81: ifnull -> 104
    //   84: aload_0
    //   85: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   88: aload #4
    //   90: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   93: aload_0
    //   94: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   97: aload_0
    //   98: getfield defaultHintTextColor : Landroid/content/res/ColorStateList;
    //   101: invokevirtual setExpandedTextColor : (Landroid/content/res/ColorStateList;)V
    //   104: iload_3
    //   105: ifne -> 139
    //   108: aload_0
    //   109: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   112: aload_0
    //   113: getfield disabledColor : I
    //   116: invokestatic valueOf : (I)Landroid/content/res/ColorStateList;
    //   119: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   122: aload_0
    //   123: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   126: aload_0
    //   127: getfield disabledColor : I
    //   130: invokestatic valueOf : (I)Landroid/content/res/ColorStateList;
    //   133: invokevirtual setExpandedTextColor : (Landroid/content/res/ColorStateList;)V
    //   136: goto -> 219
    //   139: iload #7
    //   141: ifeq -> 161
    //   144: aload_0
    //   145: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   148: aload_0
    //   149: getfield indicatorViewController : Landroid/support/design/widget/IndicatorViewController;
    //   152: invokevirtual getErrorViewTextColors : ()Landroid/content/res/ColorStateList;
    //   155: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   158: goto -> 219
    //   161: aload_0
    //   162: getfield counterOverflowed : Z
    //   165: ifeq -> 194
    //   168: aload_0
    //   169: getfield counterView : Landroid/widget/TextView;
    //   172: astore #4
    //   174: aload #4
    //   176: ifnull -> 194
    //   179: aload_0
    //   180: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   183: aload #4
    //   185: invokevirtual getTextColors : ()Landroid/content/res/ColorStateList;
    //   188: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   191: goto -> 219
    //   194: iload #5
    //   196: ifeq -> 219
    //   199: aload_0
    //   200: getfield focusedTextColor : Landroid/content/res/ColorStateList;
    //   203: astore #4
    //   205: aload #4
    //   207: ifnull -> 219
    //   210: aload_0
    //   211: getfield collapsingTextHelper : Landroid/support/design/widget/CollapsingTextHelper;
    //   214: aload #4
    //   216: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   219: iload #6
    //   221: ifne -> 263
    //   224: aload_0
    //   225: invokevirtual isEnabled : ()Z
    //   228: ifeq -> 244
    //   231: iload #5
    //   233: ifne -> 263
    //   236: iload #7
    //   238: ifeq -> 244
    //   241: goto -> 263
    //   244: iload_2
    //   245: ifne -> 255
    //   248: aload_0
    //   249: getfield hintExpanded : Z
    //   252: ifne -> 279
    //   255: aload_0
    //   256: iload_1
    //   257: invokespecial expandHint : (Z)V
    //   260: goto -> 279
    //   263: iload_2
    //   264: ifne -> 274
    //   267: aload_0
    //   268: getfield hintExpanded : Z
    //   271: ifeq -> 279
    //   274: aload_0
    //   275: iload_1
    //   276: invokespecial collapseHint : (Z)V
    //   279: return
  }
  
  private void updatePasswordToggleView() {
    if (this.editText == null)
      return; 
    if (shouldShowPasswordIcon()) {
      if (this.passwordToggleView == null) {
        CheckableImageButton checkableImageButton = (CheckableImageButton)LayoutInflater.from(getContext()).inflate(R.layout.design_text_input_password_icon, (ViewGroup)this.inputFrame, false);
        this.passwordToggleView = checkableImageButton;
        checkableImageButton.setImageDrawable(this.passwordToggleDrawable);
        this.passwordToggleView.setContentDescription(this.passwordToggleContentDesc);
        this.inputFrame.addView((View)this.passwordToggleView);
        this.passwordToggleView.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                TextInputLayout.this.passwordVisibilityToggleRequested(false);
              }
            });
      } 
      EditText editText = this.editText;
      if (editText != null && ViewCompat.getMinimumHeight((View)editText) <= 0)
        this.editText.setMinimumHeight(ViewCompat.getMinimumHeight((View)this.passwordToggleView)); 
      this.passwordToggleView.setVisibility(0);
      this.passwordToggleView.setChecked(this.passwordToggledVisible);
      if (this.passwordToggleDummyDrawable == null)
        this.passwordToggleDummyDrawable = (Drawable)new ColorDrawable(); 
      this.passwordToggleDummyDrawable.setBounds(0, 0, this.passwordToggleView.getMeasuredWidth(), 1);
      Drawable[] arrayOfDrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
      if (arrayOfDrawable[2] != this.passwordToggleDummyDrawable)
        this.originalEditTextEndDrawable = arrayOfDrawable[2]; 
      TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrayOfDrawable[0], arrayOfDrawable[1], this.passwordToggleDummyDrawable, arrayOfDrawable[3]);
      this.passwordToggleView.setPadding(this.editText.getPaddingLeft(), this.editText.getPaddingTop(), this.editText.getPaddingRight(), this.editText.getPaddingBottom());
    } else {
      CheckableImageButton checkableImageButton = this.passwordToggleView;
      if (checkableImageButton != null && checkableImageButton.getVisibility() == 0)
        this.passwordToggleView.setVisibility(8); 
      if (this.passwordToggleDummyDrawable != null) {
        Drawable[] arrayOfDrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
        if (arrayOfDrawable[2] == this.passwordToggleDummyDrawable) {
          TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrayOfDrawable[0], arrayOfDrawable[1], this.originalEditTextEndDrawable, arrayOfDrawable[3]);
          this.passwordToggleDummyDrawable = null;
        } 
      } 
    } 
  }
  
  private void updateTextInputBoxBounds() {
    if (this.boxBackgroundMode == 0 || this.boxBackground == null || this.editText == null || getRight() == 0)
      return; 
    int i = this.editText.getLeft();
    int j = calculateBoxBackgroundTop();
    int k = this.editText.getRight();
    int m = this.editText.getBottom() + this.boxBottomOffsetPx;
    int n = i;
    int i1 = j;
    int i2 = k;
    int i3 = m;
    if (this.boxBackgroundMode == 2) {
      i3 = this.boxStrokeWidthFocusedPx;
      n = i + i3 / 2;
      i1 = j - i3 / 2;
      i2 = k - i3 / 2;
      i3 = m + i3 / 2;
    } 
    this.boxBackground.setBounds(n, i1, i2, i3);
    applyBoxAttributes();
    updateEditTextBackgroundBounds();
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (paramView instanceof EditText) {
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(paramLayoutParams);
      layoutParams.gravity = layoutParams.gravity & 0xFFFFFF8F | 0x10;
      this.inputFrame.addView(paramView, (ViewGroup.LayoutParams)layoutParams);
      this.inputFrame.setLayoutParams(paramLayoutParams);
      updateInputLayoutMargins();
      setEditText((EditText)paramView);
    } else {
      super.addView(paramView, paramInt, paramLayoutParams);
    } 
  }
  
  void animateToExpansionFraction(float paramFloat) {
    if (this.collapsingTextHelper.getExpansionFraction() == paramFloat)
      return; 
    if (this.animator == null) {
      ValueAnimator valueAnimator = new ValueAnimator();
      this.animator = valueAnimator;
      valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      this.animator.setDuration(167L);
      this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
              TextInputLayout.this.collapsingTextHelper.setExpansionFraction(((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
            }
          });
    } 
    this.animator.setFloatValues(new float[] { this.collapsingTextHelper.getExpansionFraction(), paramFloat });
    this.animator.start();
  }
  
  boolean cutoutIsOpen() {
    boolean bool;
    if (cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void dispatchProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt) {
    if (this.originalHint != null) {
      EditText editText = this.editText;
      if (editText != null) {
        boolean bool = this.isProvidingHint;
        this.isProvidingHint = false;
        CharSequence charSequence = editText.getHint();
        this.editText.setHint(this.originalHint);
        try {
          super.dispatchProvideAutofillStructure(paramViewStructure, paramInt);
          return;
        } finally {
          this.editText.setHint(charSequence);
          this.isProvidingHint = bool;
        } 
      } 
    } 
    super.dispatchProvideAutofillStructure(paramViewStructure, paramInt);
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
    this.restoringSavedState = true;
    super.dispatchRestoreInstanceState(paramSparseArray);
    this.restoringSavedState = false;
  }
  
  public void draw(Canvas paramCanvas) {
    GradientDrawable gradientDrawable = this.boxBackground;
    if (gradientDrawable != null)
      gradientDrawable.draw(paramCanvas); 
    super.draw(paramCanvas);
    if (this.hintEnabled)
      this.collapsingTextHelper.draw(paramCanvas); 
  }
  
  protected void drawableStateChanged() {
    if (this.inDrawableStateChanged)
      return; 
    boolean bool = true;
    this.inDrawableStateChanged = true;
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    int i = 0;
    if (!ViewCompat.isLaidOut((View)this) || !isEnabled())
      bool = false; 
    updateLabelState(bool);
    updateEditTextBackground();
    updateTextInputBoxBounds();
    updateTextInputBoxState();
    CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
    if (collapsingTextHelper != null)
      i = false | collapsingTextHelper.setState(arrayOfInt); 
    if (i != 0)
      invalidate(); 
    this.inDrawableStateChanged = false;
  }
  
  public int getBoxBackgroundColor() {
    return this.boxBackgroundColor;
  }
  
  public float getBoxCornerRadiusBottomEnd() {
    return this.boxCornerRadiusBottomEnd;
  }
  
  public float getBoxCornerRadiusBottomStart() {
    return this.boxCornerRadiusBottomStart;
  }
  
  public float getBoxCornerRadiusTopEnd() {
    return this.boxCornerRadiusTopEnd;
  }
  
  public float getBoxCornerRadiusTopStart() {
    return this.boxCornerRadiusTopStart;
  }
  
  public int getBoxStrokeColor() {
    return this.focusedStrokeColor;
  }
  
  public int getCounterMaxLength() {
    return this.counterMaxLength;
  }
  
  CharSequence getCounterOverflowDescription() {
    if (this.counterEnabled && this.counterOverflowed) {
      TextView textView = this.counterView;
      if (textView != null)
        return textView.getContentDescription(); 
    } 
    return null;
  }
  
  public ColorStateList getDefaultHintTextColor() {
    return this.defaultHintTextColor;
  }
  
  public EditText getEditText() {
    return this.editText;
  }
  
  public CharSequence getError() {
    CharSequence charSequence;
    if (this.indicatorViewController.isErrorEnabled()) {
      charSequence = this.indicatorViewController.getErrorText();
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  public int getErrorCurrentTextColors() {
    return this.indicatorViewController.getErrorViewCurrentTextColor();
  }
  
  final int getErrorTextCurrentColor() {
    return this.indicatorViewController.getErrorViewCurrentTextColor();
  }
  
  public CharSequence getHelperText() {
    CharSequence charSequence;
    if (this.indicatorViewController.isHelperTextEnabled()) {
      charSequence = this.indicatorViewController.getHelperText();
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  public int getHelperTextCurrentTextColor() {
    return this.indicatorViewController.getHelperTextViewCurrentTextColor();
  }
  
  public CharSequence getHint() {
    CharSequence charSequence;
    if (this.hintEnabled) {
      charSequence = this.hint;
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  final float getHintCollapsedTextHeight() {
    return this.collapsingTextHelper.getCollapsedTextHeight();
  }
  
  final int getHintCurrentCollapsedTextColor() {
    return this.collapsingTextHelper.getCurrentCollapsedTextColor();
  }
  
  public CharSequence getPasswordVisibilityToggleContentDescription() {
    return this.passwordToggleContentDesc;
  }
  
  public Drawable getPasswordVisibilityToggleDrawable() {
    return this.passwordToggleDrawable;
  }
  
  public Typeface getTypeface() {
    return this.typeface;
  }
  
  public boolean isCounterEnabled() {
    return this.counterEnabled;
  }
  
  public boolean isErrorEnabled() {
    return this.indicatorViewController.isErrorEnabled();
  }
  
  final boolean isHelperTextDisplayed() {
    return this.indicatorViewController.helperTextIsDisplayed();
  }
  
  public boolean isHelperTextEnabled() {
    return this.indicatorViewController.isHelperTextEnabled();
  }
  
  public boolean isHintAnimationEnabled() {
    return this.hintAnimationEnabled;
  }
  
  public boolean isHintEnabled() {
    return this.hintEnabled;
  }
  
  final boolean isHintExpanded() {
    return this.hintExpanded;
  }
  
  public boolean isPasswordVisibilityToggleEnabled() {
    return this.passwordToggleEnabled;
  }
  
  boolean isProvidingHint() {
    return this.isProvidingHint;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.boxBackground != null)
      updateTextInputBoxBounds(); 
    if (this.hintEnabled) {
      EditText editText = this.editText;
      if (editText != null) {
        Rect rect = this.tmpRect;
        DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)editText, rect);
        paramInt3 = rect.left + this.editText.getCompoundPaddingLeft();
        paramInt1 = rect.right - this.editText.getCompoundPaddingRight();
        int i = calculateCollapsedTextTopBounds();
        this.collapsingTextHelper.setExpandedBounds(paramInt3, rect.top + this.editText.getCompoundPaddingTop(), paramInt1, rect.bottom - this.editText.getCompoundPaddingBottom());
        this.collapsingTextHelper.setCollapsedBounds(paramInt3, i, paramInt1, paramInt4 - paramInt2 - getPaddingBottom());
        this.collapsingTextHelper.recalculate();
        if (cutoutEnabled() && !this.hintExpanded)
          openCutout(); 
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    updatePasswordToggleView();
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    setError(savedState.error);
    if (savedState.isPasswordToggledVisible)
      passwordVisibilityToggleRequested(true); 
    requestLayout();
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (this.indicatorViewController.errorShouldBeShown())
      savedState.error = getError(); 
    savedState.isPasswordToggledVisible = this.passwordToggledVisible;
    return (Parcelable)savedState;
  }
  
  public void passwordVisibilityToggleRequested(boolean paramBoolean) {
    if (this.passwordToggleEnabled) {
      int i = this.editText.getSelectionEnd();
      if (hasPasswordTransformation()) {
        this.editText.setTransformationMethod(null);
        this.passwordToggledVisible = true;
      } else {
        this.editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
        this.passwordToggledVisible = false;
      } 
      this.passwordToggleView.setChecked(this.passwordToggledVisible);
      if (paramBoolean)
        this.passwordToggleView.jumpDrawablesToCurrentState(); 
      this.editText.setSelection(i);
    } 
  }
  
  public void setBoxBackgroundColor(int paramInt) {
    if (this.boxBackgroundColor != paramInt) {
      this.boxBackgroundColor = paramInt;
      applyBoxAttributes();
    } 
  }
  
  public void setBoxBackgroundColorResource(int paramInt) {
    setBoxBackgroundColor(ContextCompat.getColor(getContext(), paramInt));
  }
  
  public void setBoxBackgroundMode(int paramInt) {
    if (paramInt == this.boxBackgroundMode)
      return; 
    this.boxBackgroundMode = paramInt;
    onApplyBoxBackgroundMode();
  }
  
  public void setBoxCornerRadii(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (this.boxCornerRadiusTopStart != paramFloat1 || this.boxCornerRadiusTopEnd != paramFloat2 || this.boxCornerRadiusBottomEnd != paramFloat4 || this.boxCornerRadiusBottomStart != paramFloat3) {
      this.boxCornerRadiusTopStart = paramFloat1;
      this.boxCornerRadiusTopEnd = paramFloat2;
      this.boxCornerRadiusBottomEnd = paramFloat4;
      this.boxCornerRadiusBottomStart = paramFloat3;
      applyBoxAttributes();
    } 
  }
  
  public void setBoxCornerRadiiResources(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    setBoxCornerRadii(getContext().getResources().getDimension(paramInt1), getContext().getResources().getDimension(paramInt2), getContext().getResources().getDimension(paramInt3), getContext().getResources().getDimension(paramInt4));
  }
  
  public void setBoxStrokeColor(int paramInt) {
    if (this.focusedStrokeColor != paramInt) {
      this.focusedStrokeColor = paramInt;
      updateTextInputBoxState();
    } 
  }
  
  public void setCounterEnabled(boolean paramBoolean) {
    if (this.counterEnabled != paramBoolean) {
      if (paramBoolean) {
        AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
        this.counterView = (TextView)appCompatTextView;
        appCompatTextView.setId(R.id.textinput_counter);
        Typeface typeface = this.typeface;
        if (typeface != null)
          this.counterView.setTypeface(typeface); 
        this.counterView.setMaxLines(1);
        setTextAppearanceCompatWithErrorFallback(this.counterView, this.counterTextAppearance);
        this.indicatorViewController.addIndicator(this.counterView, 2);
        EditText editText = this.editText;
        if (editText == null) {
          updateCounter(0);
        } else {
          updateCounter(editText.getText().length());
        } 
      } else {
        this.indicatorViewController.removeIndicator(this.counterView, 2);
        this.counterView = null;
      } 
      this.counterEnabled = paramBoolean;
    } 
  }
  
  public void setCounterMaxLength(int paramInt) {
    if (this.counterMaxLength != paramInt) {
      if (paramInt > 0) {
        this.counterMaxLength = paramInt;
      } else {
        this.counterMaxLength = -1;
      } 
      if (this.counterEnabled) {
        EditText editText = this.editText;
        if (editText == null) {
          paramInt = 0;
        } else {
          paramInt = editText.getText().length();
        } 
        updateCounter(paramInt);
      } 
    } 
  }
  
  public void setDefaultHintTextColor(ColorStateList paramColorStateList) {
    this.defaultHintTextColor = paramColorStateList;
    this.focusedTextColor = paramColorStateList;
    if (this.editText != null)
      updateLabelState(false); 
  }
  
  public void setEnabled(boolean paramBoolean) {
    recursiveSetEnabled((ViewGroup)this, paramBoolean);
    super.setEnabled(paramBoolean);
  }
  
  public void setError(CharSequence paramCharSequence) {
    if (!this.indicatorViewController.isErrorEnabled()) {
      if (TextUtils.isEmpty(paramCharSequence))
        return; 
      setErrorEnabled(true);
    } 
    if (!TextUtils.isEmpty(paramCharSequence)) {
      this.indicatorViewController.showError(paramCharSequence);
    } else {
      this.indicatorViewController.hideError();
    } 
  }
  
  public void setErrorEnabled(boolean paramBoolean) {
    this.indicatorViewController.setErrorEnabled(paramBoolean);
  }
  
  public void setErrorTextAppearance(int paramInt) {
    this.indicatorViewController.setErrorTextAppearance(paramInt);
  }
  
  public void setErrorTextColor(ColorStateList paramColorStateList) {
    this.indicatorViewController.setErrorViewTextColor(paramColorStateList);
  }
  
  public void setHelperText(CharSequence paramCharSequence) {
    if (TextUtils.isEmpty(paramCharSequence)) {
      if (isHelperTextEnabled())
        setHelperTextEnabled(false); 
    } else {
      if (!isHelperTextEnabled())
        setHelperTextEnabled(true); 
      this.indicatorViewController.showHelper(paramCharSequence);
    } 
  }
  
  public void setHelperTextColor(ColorStateList paramColorStateList) {
    this.indicatorViewController.setHelperTextViewTextColor(paramColorStateList);
  }
  
  public void setHelperTextEnabled(boolean paramBoolean) {
    this.indicatorViewController.setHelperTextEnabled(paramBoolean);
  }
  
  public void setHelperTextTextAppearance(int paramInt) {
    this.indicatorViewController.setHelperTextAppearance(paramInt);
  }
  
  public void setHint(CharSequence paramCharSequence) {
    if (this.hintEnabled) {
      setHintInternal(paramCharSequence);
      sendAccessibilityEvent(2048);
    } 
  }
  
  public void setHintAnimationEnabled(boolean paramBoolean) {
    this.hintAnimationEnabled = paramBoolean;
  }
  
  public void setHintEnabled(boolean paramBoolean) {
    if (paramBoolean != this.hintEnabled) {
      this.hintEnabled = paramBoolean;
      if (!paramBoolean) {
        this.isProvidingHint = false;
        if (!TextUtils.isEmpty(this.hint) && TextUtils.isEmpty(this.editText.getHint()))
          this.editText.setHint(this.hint); 
        setHintInternal((CharSequence)null);
      } else {
        CharSequence charSequence = this.editText.getHint();
        if (!TextUtils.isEmpty(charSequence)) {
          if (TextUtils.isEmpty(this.hint))
            setHint(charSequence); 
          this.editText.setHint(null);
        } 
        this.isProvidingHint = true;
      } 
      if (this.editText != null)
        updateInputLayoutMargins(); 
    } 
  }
  
  public void setHintTextAppearance(int paramInt) {
    this.collapsingTextHelper.setCollapsedTextAppearance(paramInt);
    this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
    if (this.editText != null) {
      updateLabelState(false);
      updateInputLayoutMargins();
    } 
  }
  
  public void setPasswordVisibilityToggleContentDescription(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getResources().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setPasswordVisibilityToggleContentDescription(charSequence);
  }
  
  public void setPasswordVisibilityToggleContentDescription(CharSequence paramCharSequence) {
    this.passwordToggleContentDesc = paramCharSequence;
    CheckableImageButton checkableImageButton = this.passwordToggleView;
    if (checkableImageButton != null)
      checkableImageButton.setContentDescription(paramCharSequence); 
  }
  
  public void setPasswordVisibilityToggleDrawable(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = AppCompatResources.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setPasswordVisibilityToggleDrawable(drawable);
  }
  
  public void setPasswordVisibilityToggleDrawable(Drawable paramDrawable) {
    this.passwordToggleDrawable = paramDrawable;
    CheckableImageButton checkableImageButton = this.passwordToggleView;
    if (checkableImageButton != null)
      checkableImageButton.setImageDrawable(paramDrawable); 
  }
  
  public void setPasswordVisibilityToggleEnabled(boolean paramBoolean) {
    if (this.passwordToggleEnabled != paramBoolean) {
      this.passwordToggleEnabled = paramBoolean;
      if (!paramBoolean && this.passwordToggledVisible) {
        EditText editText = this.editText;
        if (editText != null)
          editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance()); 
      } 
      this.passwordToggledVisible = false;
      updatePasswordToggleView();
    } 
  }
  
  public void setPasswordVisibilityToggleTintList(ColorStateList paramColorStateList) {
    this.passwordToggleTintList = paramColorStateList;
    this.hasPasswordToggleTintList = true;
    applyPasswordToggleTint();
  }
  
  public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode paramMode) {
    this.passwordToggleTintMode = paramMode;
    this.hasPasswordToggleTintMode = true;
    applyPasswordToggleTint();
  }
  
  void setTextAppearanceCompatWithErrorFallback(TextView paramTextView, int paramInt) {
    boolean bool = false;
    try {
      TextViewCompat.setTextAppearance(paramTextView, paramInt);
      paramInt = bool;
      if (Build.VERSION.SDK_INT >= 23) {
        int i = paramTextView.getTextColors().getDefaultColor();
        paramInt = bool;
        if (i == -65281)
          paramInt = 1; 
      } 
    } catch (Exception exception) {
      paramInt = 1;
    } 
    if (paramInt != 0) {
      TextViewCompat.setTextAppearance(paramTextView, R.style.TextAppearance_AppCompat_Caption);
      paramTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.design_error));
    } 
  }
  
  public void setTextInputAccessibilityDelegate(AccessibilityDelegate paramAccessibilityDelegate) {
    EditText editText = this.editText;
    if (editText != null)
      ViewCompat.setAccessibilityDelegate((View)editText, paramAccessibilityDelegate); 
  }
  
  public void setTypeface(Typeface paramTypeface) {
    if (paramTypeface != this.typeface) {
      this.typeface = paramTypeface;
      this.collapsingTextHelper.setTypefaces(paramTypeface);
      this.indicatorViewController.setTypefaces(paramTypeface);
      TextView textView = this.counterView;
      if (textView != null)
        textView.setTypeface(paramTypeface); 
    } 
  }
  
  void updateCounter(int paramInt) {
    boolean bool = this.counterOverflowed;
    if (this.counterMaxLength == -1) {
      this.counterView.setText(String.valueOf(paramInt));
      this.counterView.setContentDescription(null);
      this.counterOverflowed = false;
    } else {
      boolean bool1;
      if (ViewCompat.getAccessibilityLiveRegion((View)this.counterView) == 1)
        ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 0); 
      if (paramInt > this.counterMaxLength) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.counterOverflowed = bool1;
      if (bool != bool1) {
        int i;
        TextView textView = this.counterView;
        if (bool1) {
          i = this.counterOverflowTextAppearance;
        } else {
          i = this.counterTextAppearance;
        } 
        setTextAppearanceCompatWithErrorFallback(textView, i);
        if (this.counterOverflowed)
          ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 1); 
      } 
      this.counterView.setText(getContext().getString(R.string.character_counter_pattern, new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.counterMaxLength) }));
      this.counterView.setContentDescription(getContext().getString(R.string.character_counter_content_description, new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.counterMaxLength) }));
    } 
    if (this.editText != null && bool != this.counterOverflowed) {
      updateLabelState(false);
      updateTextInputBoxState();
      updateEditTextBackground();
    } 
  }
  
  void updateEditTextBackground() {
    EditText editText = this.editText;
    if (editText == null)
      return; 
    Drawable drawable2 = editText.getBackground();
    if (drawable2 == null)
      return; 
    ensureBackgroundDrawableStateWorkaround();
    Drawable drawable1 = drawable2;
    if (DrawableUtils.canSafelyMutateDrawable(drawable2))
      drawable1 = drawable2.mutate(); 
    if (this.indicatorViewController.errorShouldBeShown()) {
      drawable1.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), PorterDuff.Mode.SRC_IN));
    } else {
      if (this.counterOverflowed) {
        TextView textView = this.counterView;
        if (textView != null) {
          drawable1.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(textView.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
          return;
        } 
      } 
      DrawableCompat.clearColorFilter(drawable1);
      this.editText.refreshDrawableState();
    } 
  }
  
  void updateLabelState(boolean paramBoolean) {
    updateLabelState(paramBoolean, false);
  }
  
  void updateTextInputBoxState() {
    // Byte code:
    //   0: aload_0
    //   1: getfield boxBackground : Landroid/graphics/drawable/GradientDrawable;
    //   4: ifnull -> 217
    //   7: aload_0
    //   8: getfield boxBackgroundMode : I
    //   11: ifne -> 17
    //   14: goto -> 217
    //   17: aload_0
    //   18: getfield editText : Landroid/widget/EditText;
    //   21: astore_1
    //   22: iconst_1
    //   23: istore_2
    //   24: aload_1
    //   25: ifnull -> 40
    //   28: aload_1
    //   29: invokevirtual hasFocus : ()Z
    //   32: ifeq -> 40
    //   35: iconst_1
    //   36: istore_3
    //   37: goto -> 42
    //   40: iconst_0
    //   41: istore_3
    //   42: aload_0
    //   43: getfield editText : Landroid/widget/EditText;
    //   46: astore_1
    //   47: aload_1
    //   48: ifnull -> 61
    //   51: aload_1
    //   52: invokevirtual isHovered : ()Z
    //   55: ifeq -> 61
    //   58: goto -> 63
    //   61: iconst_0
    //   62: istore_2
    //   63: aload_0
    //   64: getfield boxBackgroundMode : I
    //   67: iconst_2
    //   68: if_icmpne -> 216
    //   71: aload_0
    //   72: invokevirtual isEnabled : ()Z
    //   75: ifne -> 89
    //   78: aload_0
    //   79: aload_0
    //   80: getfield disabledColor : I
    //   83: putfield boxStrokeColor : I
    //   86: goto -> 178
    //   89: aload_0
    //   90: getfield indicatorViewController : Landroid/support/design/widget/IndicatorViewController;
    //   93: invokevirtual errorShouldBeShown : ()Z
    //   96: ifeq -> 113
    //   99: aload_0
    //   100: aload_0
    //   101: getfield indicatorViewController : Landroid/support/design/widget/IndicatorViewController;
    //   104: invokevirtual getErrorViewCurrentTextColor : ()I
    //   107: putfield boxStrokeColor : I
    //   110: goto -> 178
    //   113: aload_0
    //   114: getfield counterOverflowed : Z
    //   117: ifeq -> 140
    //   120: aload_0
    //   121: getfield counterView : Landroid/widget/TextView;
    //   124: astore_1
    //   125: aload_1
    //   126: ifnull -> 140
    //   129: aload_0
    //   130: aload_1
    //   131: invokevirtual getCurrentTextColor : ()I
    //   134: putfield boxStrokeColor : I
    //   137: goto -> 178
    //   140: iload_3
    //   141: ifeq -> 155
    //   144: aload_0
    //   145: aload_0
    //   146: getfield focusedStrokeColor : I
    //   149: putfield boxStrokeColor : I
    //   152: goto -> 178
    //   155: iload_2
    //   156: ifeq -> 170
    //   159: aload_0
    //   160: aload_0
    //   161: getfield hoveredStrokeColor : I
    //   164: putfield boxStrokeColor : I
    //   167: goto -> 178
    //   170: aload_0
    //   171: aload_0
    //   172: getfield defaultStrokeColor : I
    //   175: putfield boxStrokeColor : I
    //   178: iload_2
    //   179: ifne -> 186
    //   182: iload_3
    //   183: ifeq -> 204
    //   186: aload_0
    //   187: invokevirtual isEnabled : ()Z
    //   190: ifeq -> 204
    //   193: aload_0
    //   194: aload_0
    //   195: getfield boxStrokeWidthFocusedPx : I
    //   198: putfield boxStrokeWidthPx : I
    //   201: goto -> 212
    //   204: aload_0
    //   205: aload_0
    //   206: getfield boxStrokeWidthDefaultPx : I
    //   209: putfield boxStrokeWidthPx : I
    //   212: aload_0
    //   213: invokespecial applyBoxAttributes : ()V
    //   216: return
    //   217: return
  }
  
  public static class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final TextInputLayout layout;
    
    public AccessibilityDelegate(TextInputLayout param1TextInputLayout) {
      this.layout = param1TextInputLayout;
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool2;
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      EditText editText = this.layout.getEditText();
      if (editText != null) {
        Editable editable = editText.getText();
      } else {
        editText = null;
      } 
      CharSequence charSequence1 = this.layout.getHint();
      CharSequence charSequence2 = this.layout.getError();
      CharSequence charSequence3 = this.layout.getCounterOverflowDescription();
      int i = TextUtils.isEmpty((CharSequence)editText) ^ true;
      int j = TextUtils.isEmpty(charSequence1) ^ true;
      int k = TextUtils.isEmpty(charSequence2) ^ true;
      boolean bool1 = false;
      if (k != 0 || !TextUtils.isEmpty(charSequence3)) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (i != 0) {
        param1AccessibilityNodeInfoCompat.setText((CharSequence)editText);
      } else if (j != 0) {
        param1AccessibilityNodeInfoCompat.setText(charSequence1);
      } 
      if (j != 0) {
        param1AccessibilityNodeInfoCompat.setHintText(charSequence1);
        boolean bool = bool1;
        if (i == 0) {
          bool = bool1;
          if (j != 0)
            bool = true; 
        } 
        param1AccessibilityNodeInfoCompat.setShowingHintText(bool);
      } 
      if (bool2) {
        CharSequence charSequence;
        if (k != 0) {
          charSequence = charSequence2;
        } else {
          charSequence = charSequence3;
        } 
        param1AccessibilityNodeInfoCompat.setError(charSequence);
        param1AccessibilityNodeInfoCompat.setContentInvalid(true);
      } 
    }
    
    public void onPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      CharSequence charSequence;
      super.onPopulateAccessibilityEvent(param1View, param1AccessibilityEvent);
      EditText editText = this.layout.getEditText();
      if (editText != null) {
        Editable editable = editText.getText();
      } else {
        editText = null;
      } 
      if (TextUtils.isEmpty((CharSequence)editText))
        charSequence = this.layout.getHint(); 
      if (!TextUtils.isEmpty(charSequence))
        param1AccessibilityEvent.getText().add(charSequence); 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BoxBackgroundMode {}
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public TextInputLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new TextInputLayout.SavedState(param2Parcel, null);
        }
        
        public TextInputLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new TextInputLayout.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public TextInputLayout.SavedState[] newArray(int param2Int) {
          return new TextInputLayout.SavedState[param2Int];
        }
      };
    
    CharSequence error;
    
    boolean isPasswordToggledVisible;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      this.isPasswordToggledVisible = bool;
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("TextInputLayout.SavedState{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append(" error=");
      stringBuilder.append(this.error);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      TextUtils.writeToParcel(this.error, param1Parcel, param1Int);
      param1Parcel.writeInt(this.isPasswordToggledVisible);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public TextInputLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new TextInputLayout.SavedState(param1Parcel, null);
    }
    
    public TextInputLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new TextInputLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public TextInputLayout.SavedState[] newArray(int param1Int) {
      return new TextInputLayout.SavedState[param1Int];
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/TextInputLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */