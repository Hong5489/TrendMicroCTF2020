package android.support.design.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.design.animation.MotionSpec;
import android.support.design.canvas.CanvasCompat;
import android.support.design.drawable.DrawableUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.resources.MaterialResources;
import android.support.design.resources.TextAppearance;
import android.support.design.ripple.RippleUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.support.v4.text.BidiFormatter;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ChipDrawable extends Drawable implements TintAwareDrawable, Drawable.Callback {
  private static final boolean DEBUG = false;
  
  private static final int[] DEFAULT_STATE = new int[] { 16842910 };
  
  private static final String NAMESPACE_APP = "http://schemas.android.com/apk/res-auto";
  
  private int alpha = 255;
  
  private boolean checkable;
  
  private Drawable checkedIcon;
  
  private boolean checkedIconVisible;
  
  private ColorStateList chipBackgroundColor;
  
  private float chipCornerRadius;
  
  private float chipEndPadding;
  
  private Drawable chipIcon;
  
  private float chipIconSize;
  
  private ColorStateList chipIconTint;
  
  private boolean chipIconVisible;
  
  private float chipMinHeight;
  
  private final Paint chipPaint = new Paint(1);
  
  private float chipStartPadding;
  
  private ColorStateList chipStrokeColor;
  
  private float chipStrokeWidth;
  
  private Drawable closeIcon;
  
  private CharSequence closeIconContentDescription;
  
  private float closeIconEndPadding;
  
  private float closeIconSize;
  
  private float closeIconStartPadding;
  
  private int[] closeIconStateSet;
  
  private ColorStateList closeIconTint;
  
  private boolean closeIconVisible;
  
  private ColorFilter colorFilter;
  
  private ColorStateList compatRippleColor;
  
  private final Context context;
  
  private boolean currentChecked;
  
  private int currentChipBackgroundColor;
  
  private int currentChipStrokeColor;
  
  private int currentCompatRippleColor;
  
  private int currentTextColor;
  
  private int currentTint;
  
  private final Paint debugPaint;
  
  private WeakReference<Delegate> delegate = new WeakReference<>(null);
  
  private final ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback() {
      public void onFontRetrievalFailed(int param1Int) {}
      
      public void onFontRetrieved(Typeface param1Typeface) {
        ChipDrawable.access$002(ChipDrawable.this, true);
        ChipDrawable.this.onSizeChange();
        ChipDrawable.this.invalidateSelf();
      }
    };
  
  private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
  
  private MotionSpec hideMotionSpec;
  
  private float iconEndPadding;
  
  private float iconStartPadding;
  
  private int maxWidth;
  
  private final PointF pointF = new PointF();
  
  private CharSequence rawText;
  
  private final RectF rectF = new RectF();
  
  private ColorStateList rippleColor;
  
  private boolean shouldDrawText;
  
  private MotionSpec showMotionSpec;
  
  private TextAppearance textAppearance;
  
  private float textEndPadding;
  
  private final TextPaint textPaint = new TextPaint(1);
  
  private float textStartPadding;
  
  private float textWidth;
  
  private boolean textWidthDirty = true;
  
  private ColorStateList tint;
  
  private PorterDuffColorFilter tintFilter;
  
  private PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;
  
  private TextUtils.TruncateAt truncateAt;
  
  private CharSequence unicodeWrappedText;
  
  private boolean useCompatRipple;
  
  private ChipDrawable(Context paramContext) {
    this.context = paramContext;
    this.rawText = "";
    this.textPaint.density = (paramContext.getResources().getDisplayMetrics()).density;
    this.debugPaint = null;
    if (false) {
      Paint.Style style = Paint.Style.STROKE;
      throw new NullPointerException();
    } 
    setState(DEFAULT_STATE);
    setCloseIconState(DEFAULT_STATE);
    this.shouldDrawText = true;
  }
  
  private void applyChildDrawable(Drawable paramDrawable) {
    if (paramDrawable != null) {
      paramDrawable.setCallback(this);
      DrawableCompat.setLayoutDirection(paramDrawable, DrawableCompat.getLayoutDirection(this));
      paramDrawable.setLevel(getLevel());
      paramDrawable.setVisible(isVisible(), false);
      if (paramDrawable == this.closeIcon) {
        if (paramDrawable.isStateful())
          paramDrawable.setState(getCloseIconState()); 
        DrawableCompat.setTintList(paramDrawable, this.closeIconTint);
      } else if (paramDrawable.isStateful()) {
        paramDrawable.setState(getState());
      } 
    } 
  }
  
  private void calculateChipIconBounds(Rect paramRect, RectF paramRectF) {
    paramRectF.setEmpty();
    if (showsChipIcon() || showsCheckedIcon()) {
      float f = this.chipStartPadding + this.iconStartPadding;
      if (DrawableCompat.getLayoutDirection(this) == 0) {
        paramRectF.left = paramRect.left + f;
        paramRectF.right = paramRectF.left + this.chipIconSize;
      } else {
        paramRectF.right = paramRect.right - f;
        paramRectF.left = paramRectF.right - this.chipIconSize;
      } 
      paramRectF.top = paramRect.exactCenterY() - this.chipIconSize / 2.0F;
      paramRectF.bottom = paramRectF.top + this.chipIconSize;
    } 
  }
  
  private void calculateChipTouchBounds(Rect paramRect, RectF paramRectF) {
    paramRectF.set(paramRect);
    if (showsCloseIcon()) {
      float f = this.chipEndPadding + this.closeIconEndPadding + this.closeIconSize + this.closeIconStartPadding + this.textEndPadding;
      if (DrawableCompat.getLayoutDirection(this) == 0) {
        paramRectF.right = paramRect.right - f;
      } else {
        paramRectF.left = paramRect.left + f;
      } 
    } 
  }
  
  private void calculateCloseIconBounds(Rect paramRect, RectF paramRectF) {
    paramRectF.setEmpty();
    if (showsCloseIcon()) {
      float f = this.chipEndPadding + this.closeIconEndPadding;
      if (DrawableCompat.getLayoutDirection(this) == 0) {
        paramRectF.right = paramRect.right - f;
        paramRectF.left = paramRectF.right - this.closeIconSize;
      } else {
        paramRectF.left = paramRect.left + f;
        paramRectF.right = paramRectF.left + this.closeIconSize;
      } 
      paramRectF.top = paramRect.exactCenterY() - this.closeIconSize / 2.0F;
      paramRectF.bottom = paramRectF.top + this.closeIconSize;
    } 
  }
  
  private void calculateCloseIconTouchBounds(Rect paramRect, RectF paramRectF) {
    paramRectF.setEmpty();
    if (showsCloseIcon()) {
      float f = this.chipEndPadding + this.closeIconEndPadding + this.closeIconSize + this.closeIconStartPadding + this.textEndPadding;
      if (DrawableCompat.getLayoutDirection(this) == 0) {
        paramRectF.right = paramRect.right;
        paramRectF.left = paramRectF.right - f;
      } else {
        paramRectF.left = paramRect.left;
        paramRectF.right = paramRect.left + f;
      } 
      paramRectF.top = paramRect.top;
      paramRectF.bottom = paramRect.bottom;
    } 
  }
  
  private float calculateCloseIconWidth() {
    return showsCloseIcon() ? (this.closeIconStartPadding + this.closeIconSize + this.closeIconEndPadding) : 0.0F;
  }
  
  private void calculateTextBounds(Rect paramRect, RectF paramRectF) {
    paramRectF.setEmpty();
    if (this.unicodeWrappedText != null) {
      float f1 = this.chipStartPadding + calculateChipIconWidth() + this.textStartPadding;
      float f2 = this.chipEndPadding + calculateCloseIconWidth() + this.textEndPadding;
      if (DrawableCompat.getLayoutDirection(this) == 0) {
        paramRectF.left = paramRect.left + f1;
        paramRectF.right = paramRect.right - f2;
      } else {
        paramRectF.left = paramRect.left + f2;
        paramRectF.right = paramRect.right - f1;
      } 
      paramRectF.top = paramRect.top;
      paramRectF.bottom = paramRect.bottom;
    } 
  }
  
  private float calculateTextCenterFromBaseline() {
    this.textPaint.getFontMetrics(this.fontMetrics);
    return (this.fontMetrics.descent + this.fontMetrics.ascent) / 2.0F;
  }
  
  private float calculateTextWidth(CharSequence paramCharSequence) {
    return (paramCharSequence == null) ? 0.0F : this.textPaint.measureText(paramCharSequence, 0, paramCharSequence.length());
  }
  
  private boolean canShowCheckedIcon() {
    boolean bool;
    if (this.checkedIconVisible && this.checkedIcon != null && this.checkable) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static ChipDrawable createFromAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    ChipDrawable chipDrawable = new ChipDrawable(paramContext);
    chipDrawable.loadFromAttributes(paramAttributeSet, paramInt1, paramInt2);
    return chipDrawable;
  }
  
  public static ChipDrawable createFromResource(Context paramContext, int paramInt) {
    try {
      int i;
      XmlResourceParser xmlResourceParser = paramContext.getResources().getXml(paramInt);
      do {
        i = xmlResourceParser.next();
      } while (i != 2 && i != 1);
      if (i == 2) {
        if (TextUtils.equals(xmlResourceParser.getName(), "chip")) {
          AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
          int j = attributeSet.getStyleAttribute();
          i = j;
          if (j == 0)
            i = R.style.Widget_MaterialComponents_Chip_Entry; 
          return createFromAttributes(paramContext, attributeSet, R.attr.chipStandaloneStyle, i);
        } 
        XmlPullParserException xmlPullParserException1 = new XmlPullParserException();
        this("Must have a <chip> start tag");
        throw xmlPullParserException1;
      } 
      XmlPullParserException xmlPullParserException = new XmlPullParserException();
      this("No start tag found");
      throw xmlPullParserException;
    } catch (XmlPullParserException xmlPullParserException) {
    
    } catch (IOException iOException) {}
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Can't load chip resource ID #0x");
    stringBuilder.append(Integer.toHexString(paramInt));
    Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
    notFoundException.initCause(iOException);
    throw notFoundException;
  }
  
  private void drawCheckedIcon(Canvas paramCanvas, Rect paramRect) {
    if (showsCheckedIcon()) {
      calculateChipIconBounds(paramRect, this.rectF);
      float f1 = this.rectF.left;
      float f2 = this.rectF.top;
      paramCanvas.translate(f1, f2);
      this.checkedIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
      this.checkedIcon.draw(paramCanvas);
      paramCanvas.translate(-f1, -f2);
    } 
  }
  
  private void drawChipBackground(Canvas paramCanvas, Rect paramRect) {
    this.chipPaint.setColor(this.currentChipBackgroundColor);
    this.chipPaint.setStyle(Paint.Style.FILL);
    this.chipPaint.setColorFilter(getTintColorFilter());
    this.rectF.set(paramRect);
    RectF rectF = this.rectF;
    float f = this.chipCornerRadius;
    paramCanvas.drawRoundRect(rectF, f, f, this.chipPaint);
  }
  
  private void drawChipIcon(Canvas paramCanvas, Rect paramRect) {
    if (showsChipIcon()) {
      calculateChipIconBounds(paramRect, this.rectF);
      float f1 = this.rectF.left;
      float f2 = this.rectF.top;
      paramCanvas.translate(f1, f2);
      this.chipIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
      this.chipIcon.draw(paramCanvas);
      paramCanvas.translate(-f1, -f2);
    } 
  }
  
  private void drawChipStroke(Canvas paramCanvas, Rect paramRect) {
    if (this.chipStrokeWidth > 0.0F) {
      this.chipPaint.setColor(this.currentChipStrokeColor);
      this.chipPaint.setStyle(Paint.Style.STROKE);
      this.chipPaint.setColorFilter(getTintColorFilter());
      this.rectF.set(paramRect.left + this.chipStrokeWidth / 2.0F, paramRect.top + this.chipStrokeWidth / 2.0F, paramRect.right - this.chipStrokeWidth / 2.0F, paramRect.bottom - this.chipStrokeWidth / 2.0F);
      float f = this.chipCornerRadius - this.chipStrokeWidth / 2.0F;
      paramCanvas.drawRoundRect(this.rectF, f, f, this.chipPaint);
    } 
  }
  
  private void drawCloseIcon(Canvas paramCanvas, Rect paramRect) {
    if (showsCloseIcon()) {
      calculateCloseIconBounds(paramRect, this.rectF);
      float f1 = this.rectF.left;
      float f2 = this.rectF.top;
      paramCanvas.translate(f1, f2);
      this.closeIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
      this.closeIcon.draw(paramCanvas);
      paramCanvas.translate(-f1, -f2);
    } 
  }
  
  private void drawCompatRipple(Canvas paramCanvas, Rect paramRect) {
    this.chipPaint.setColor(this.currentCompatRippleColor);
    this.chipPaint.setStyle(Paint.Style.FILL);
    this.rectF.set(paramRect);
    RectF rectF = this.rectF;
    float f = this.chipCornerRadius;
    paramCanvas.drawRoundRect(rectF, f, f, this.chipPaint);
  }
  
  private void drawDebug(Canvas paramCanvas, Rect paramRect) {
    Paint paint = this.debugPaint;
    if (paint != null) {
      paint.setColor(ColorUtils.setAlphaComponent(-16777216, 127));
      paramCanvas.drawRect(paramRect, this.debugPaint);
      if (showsChipIcon() || showsCheckedIcon()) {
        calculateChipIconBounds(paramRect, this.rectF);
        paramCanvas.drawRect(this.rectF, this.debugPaint);
      } 
      if (this.unicodeWrappedText != null)
        paramCanvas.drawLine(paramRect.left, paramRect.exactCenterY(), paramRect.right, paramRect.exactCenterY(), this.debugPaint); 
      if (showsCloseIcon()) {
        calculateCloseIconBounds(paramRect, this.rectF);
        paramCanvas.drawRect(this.rectF, this.debugPaint);
      } 
      this.debugPaint.setColor(ColorUtils.setAlphaComponent(-65536, 127));
      calculateChipTouchBounds(paramRect, this.rectF);
      paramCanvas.drawRect(this.rectF, this.debugPaint);
      this.debugPaint.setColor(ColorUtils.setAlphaComponent(-16711936, 127));
      calculateCloseIconTouchBounds(paramRect, this.rectF);
      paramCanvas.drawRect(this.rectF, this.debugPaint);
    } 
  }
  
  private void drawText(Canvas paramCanvas, Rect paramRect) {
    if (this.unicodeWrappedText != null) {
      boolean bool;
      Paint.Align align = calculateTextOriginAndAlignment(paramRect, this.pointF);
      calculateTextBounds(paramRect, this.rectF);
      if (this.textAppearance != null) {
        this.textPaint.drawableState = getState();
        this.textAppearance.updateDrawState(this.context, this.textPaint, this.fontCallback);
      } 
      this.textPaint.setTextAlign(align);
      if (Math.round(getTextWidth()) > Math.round(this.rectF.width())) {
        bool = true;
      } else {
        bool = false;
      } 
      int i = 0;
      if (bool) {
        i = paramCanvas.save();
        paramCanvas.clipRect(this.rectF);
      } 
      CharSequence charSequence2 = this.unicodeWrappedText;
      CharSequence charSequence1 = charSequence2;
      if (bool) {
        charSequence1 = charSequence2;
        if (this.truncateAt != null)
          charSequence1 = TextUtils.ellipsize(this.unicodeWrappedText, this.textPaint, this.rectF.width(), this.truncateAt); 
      } 
      paramCanvas.drawText(charSequence1, 0, charSequence1.length(), this.pointF.x, this.pointF.y, (Paint)this.textPaint);
      if (bool)
        paramCanvas.restoreToCount(i); 
    } 
  }
  
  private float getTextWidth() {
    if (!this.textWidthDirty)
      return this.textWidth; 
    float f = calculateTextWidth(this.unicodeWrappedText);
    this.textWidth = f;
    this.textWidthDirty = false;
    return f;
  }
  
  private ColorFilter getTintColorFilter() {
    PorterDuffColorFilter porterDuffColorFilter;
    ColorFilter colorFilter = this.colorFilter;
    if (colorFilter == null)
      porterDuffColorFilter = this.tintFilter; 
    return (ColorFilter)porterDuffColorFilter;
  }
  
  private static boolean hasState(int[] paramArrayOfint, int paramInt) {
    if (paramArrayOfint == null)
      return false; 
    int i = paramArrayOfint.length;
    for (byte b = 0; b < i; b++) {
      if (paramArrayOfint[b] == paramInt)
        return true; 
    } 
    return false;
  }
  
  private static boolean isStateful(ColorStateList paramColorStateList) {
    boolean bool;
    if (paramColorStateList != null && paramColorStateList.isStateful()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isStateful(Drawable paramDrawable) {
    boolean bool;
    if (paramDrawable != null && paramDrawable.isStateful()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isStateful(TextAppearance paramTextAppearance) {
    boolean bool;
    if (paramTextAppearance != null && paramTextAppearance.textColor != null && paramTextAppearance.textColor.isStateful()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(this.context, paramAttributeSet, R.styleable.Chip, paramInt1, paramInt2, new int[0]);
    setChipBackgroundColor(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_chipBackgroundColor));
    setChipMinHeight(typedArray.getDimension(R.styleable.Chip_chipMinHeight, 0.0F));
    setChipCornerRadius(typedArray.getDimension(R.styleable.Chip_chipCornerRadius, 0.0F));
    setChipStrokeColor(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_chipStrokeColor));
    setChipStrokeWidth(typedArray.getDimension(R.styleable.Chip_chipStrokeWidth, 0.0F));
    setRippleColor(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_rippleColor));
    setText(typedArray.getText(R.styleable.Chip_android_text));
    setTextAppearance(MaterialResources.getTextAppearance(this.context, typedArray, R.styleable.Chip_android_textAppearance));
    paramInt1 = typedArray.getInt(R.styleable.Chip_android_ellipsize, 0);
    if (paramInt1 != 1) {
      if (paramInt1 != 2) {
        if (paramInt1 == 3)
          setEllipsize(TextUtils.TruncateAt.END); 
      } else {
        setEllipsize(TextUtils.TruncateAt.MIDDLE);
      } 
    } else {
      setEllipsize(TextUtils.TruncateAt.START);
    } 
    setChipIconVisible(typedArray.getBoolean(R.styleable.Chip_chipIconVisible, false));
    if (paramAttributeSet != null && paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconEnabled") != null && paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconVisible") == null)
      setChipIconVisible(typedArray.getBoolean(R.styleable.Chip_chipIconEnabled, false)); 
    setChipIcon(MaterialResources.getDrawable(this.context, typedArray, R.styleable.Chip_chipIcon));
    setChipIconTint(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_chipIconTint));
    setChipIconSize(typedArray.getDimension(R.styleable.Chip_chipIconSize, 0.0F));
    setCloseIconVisible(typedArray.getBoolean(R.styleable.Chip_closeIconVisible, false));
    if (paramAttributeSet != null && paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconEnabled") != null && paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconVisible") == null)
      setCloseIconVisible(typedArray.getBoolean(R.styleable.Chip_closeIconEnabled, false)); 
    setCloseIcon(MaterialResources.getDrawable(this.context, typedArray, R.styleable.Chip_closeIcon));
    setCloseIconTint(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_closeIconTint));
    setCloseIconSize(typedArray.getDimension(R.styleable.Chip_closeIconSize, 0.0F));
    setCheckable(typedArray.getBoolean(R.styleable.Chip_android_checkable, false));
    setCheckedIconVisible(typedArray.getBoolean(R.styleable.Chip_checkedIconVisible, false));
    if (paramAttributeSet != null && paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconEnabled") != null && paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconVisible") == null)
      setCheckedIconVisible(typedArray.getBoolean(R.styleable.Chip_checkedIconEnabled, false)); 
    setCheckedIcon(MaterialResources.getDrawable(this.context, typedArray, R.styleable.Chip_checkedIcon));
    setShowMotionSpec(MotionSpec.createFromAttribute(this.context, typedArray, R.styleable.Chip_showMotionSpec));
    setHideMotionSpec(MotionSpec.createFromAttribute(this.context, typedArray, R.styleable.Chip_hideMotionSpec));
    setChipStartPadding(typedArray.getDimension(R.styleable.Chip_chipStartPadding, 0.0F));
    setIconStartPadding(typedArray.getDimension(R.styleable.Chip_iconStartPadding, 0.0F));
    setIconEndPadding(typedArray.getDimension(R.styleable.Chip_iconEndPadding, 0.0F));
    setTextStartPadding(typedArray.getDimension(R.styleable.Chip_textStartPadding, 0.0F));
    setTextEndPadding(typedArray.getDimension(R.styleable.Chip_textEndPadding, 0.0F));
    setCloseIconStartPadding(typedArray.getDimension(R.styleable.Chip_closeIconStartPadding, 0.0F));
    setCloseIconEndPadding(typedArray.getDimension(R.styleable.Chip_closeIconEndPadding, 0.0F));
    setChipEndPadding(typedArray.getDimension(R.styleable.Chip_chipEndPadding, 0.0F));
    setMaxWidth(typedArray.getDimensionPixelSize(R.styleable.Chip_android_maxWidth, 2147483647));
    typedArray.recycle();
  }
  
  private boolean onStateChange(int[] paramArrayOfint1, int[] paramArrayOfint2) {
    boolean bool3;
    boolean bool1 = super.onStateChange(paramArrayOfint1);
    boolean bool = false;
    ColorStateList colorStateList2 = this.chipBackgroundColor;
    int i = 0;
    if (colorStateList2 != null) {
      j = colorStateList2.getColorForState(paramArrayOfint1, this.currentChipBackgroundColor);
    } else {
      j = 0;
    } 
    if (this.currentChipBackgroundColor != j) {
      this.currentChipBackgroundColor = j;
      bool1 = true;
    } 
    colorStateList2 = this.chipStrokeColor;
    if (colorStateList2 != null) {
      j = colorStateList2.getColorForState(paramArrayOfint1, this.currentChipStrokeColor);
    } else {
      j = 0;
    } 
    if (this.currentChipStrokeColor != j) {
      this.currentChipStrokeColor = j;
      bool1 = true;
    } 
    colorStateList2 = this.compatRippleColor;
    if (colorStateList2 != null) {
      j = colorStateList2.getColorForState(paramArrayOfint1, this.currentCompatRippleColor);
    } else {
      j = 0;
    } 
    boolean bool2 = bool1;
    if (this.currentCompatRippleColor != j) {
      this.currentCompatRippleColor = j;
      bool2 = bool1;
      if (this.useCompatRipple)
        bool2 = true; 
    } 
    TextAppearance textAppearance = this.textAppearance;
    if (textAppearance != null && textAppearance.textColor != null) {
      j = this.textAppearance.textColor.getColorForState(paramArrayOfint1, this.currentTextColor);
    } else {
      j = 0;
    } 
    bool1 = bool2;
    if (this.currentTextColor != j) {
      this.currentTextColor = j;
      bool1 = true;
    } 
    if (hasState(getState(), 16842912) && this.checkable) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    bool2 = bool1;
    int j = bool;
    if (this.currentChecked != bool3) {
      bool2 = bool1;
      j = bool;
      if (this.checkedIcon != null) {
        float f1 = calculateChipIconWidth();
        this.currentChecked = bool3;
        float f2 = calculateChipIconWidth();
        bool1 = true;
        bool2 = bool1;
        j = bool;
        if (f1 != f2) {
          j = 1;
          bool2 = bool1;
        } 
      } 
    } 
    ColorStateList colorStateList1 = this.tint;
    if (colorStateList1 != null)
      i = colorStateList1.getColorForState(paramArrayOfint1, this.currentTint); 
    bool1 = bool2;
    if (this.currentTint != i) {
      this.currentTint = i;
      this.tintFilter = DrawableUtils.updateTintFilter(this, this.tint, this.tintMode);
      bool1 = true;
    } 
    bool2 = bool1;
    if (isStateful(this.chipIcon))
      bool2 = bool1 | this.chipIcon.setState(paramArrayOfint1); 
    bool1 = bool2;
    if (isStateful(this.checkedIcon))
      bool1 = bool2 | this.checkedIcon.setState(paramArrayOfint1); 
    bool2 = bool1;
    if (isStateful(this.closeIcon))
      bool2 = bool1 | this.closeIcon.setState(paramArrayOfint2); 
    if (bool2)
      invalidateSelf(); 
    if (j != 0)
      onSizeChange(); 
    return bool2;
  }
  
  private boolean showsCheckedIcon() {
    boolean bool;
    if (this.checkedIconVisible && this.checkedIcon != null && this.currentChecked) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean showsChipIcon() {
    boolean bool;
    if (this.chipIconVisible && this.chipIcon != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean showsCloseIcon() {
    boolean bool;
    if (this.closeIconVisible && this.closeIcon != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void unapplyChildDrawable(Drawable paramDrawable) {
    if (paramDrawable != null)
      paramDrawable.setCallback(null); 
  }
  
  private void updateCompatRippleColor() {
    ColorStateList colorStateList;
    if (this.useCompatRipple) {
      colorStateList = RippleUtils.convertToRippleDrawableColor(this.rippleColor);
    } else {
      colorStateList = null;
    } 
    this.compatRippleColor = colorStateList;
  }
  
  float calculateChipIconWidth() {
    return (showsChipIcon() || showsCheckedIcon()) ? (this.iconStartPadding + this.chipIconSize + this.iconEndPadding) : 0.0F;
  }
  
  Paint.Align calculateTextOriginAndAlignment(Rect paramRect, PointF paramPointF) {
    paramPointF.set(0.0F, 0.0F);
    Paint.Align align = Paint.Align.LEFT;
    if (this.unicodeWrappedText != null) {
      float f = this.chipStartPadding + calculateChipIconWidth() + this.textStartPadding;
      if (DrawableCompat.getLayoutDirection(this) == 0) {
        paramPointF.x = paramRect.left + f;
        align = Paint.Align.LEFT;
      } else {
        paramPointF.x = paramRect.right - f;
        align = Paint.Align.RIGHT;
      } 
      paramPointF.y = paramRect.centerY() - calculateTextCenterFromBaseline();
    } 
    return align;
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect = getBounds();
    if (rect.isEmpty() || getAlpha() == 0)
      return; 
    int i = 0;
    if (this.alpha < 255)
      i = CanvasCompat.saveLayerAlpha(paramCanvas, rect.left, rect.top, rect.right, rect.bottom, this.alpha); 
    drawChipBackground(paramCanvas, rect);
    drawChipStroke(paramCanvas, rect);
    drawCompatRipple(paramCanvas, rect);
    drawChipIcon(paramCanvas, rect);
    drawCheckedIcon(paramCanvas, rect);
    if (this.shouldDrawText)
      drawText(paramCanvas, rect); 
    drawCloseIcon(paramCanvas, rect);
    drawDebug(paramCanvas, rect);
    if (this.alpha < 255)
      paramCanvas.restoreToCount(i); 
  }
  
  public int getAlpha() {
    return this.alpha;
  }
  
  public Drawable getCheckedIcon() {
    return this.checkedIcon;
  }
  
  public ColorStateList getChipBackgroundColor() {
    return this.chipBackgroundColor;
  }
  
  public float getChipCornerRadius() {
    return this.chipCornerRadius;
  }
  
  public float getChipEndPadding() {
    return this.chipEndPadding;
  }
  
  public Drawable getChipIcon() {
    Drawable drawable = this.chipIcon;
    if (drawable != null) {
      drawable = DrawableCompat.unwrap(drawable);
    } else {
      drawable = null;
    } 
    return drawable;
  }
  
  public float getChipIconSize() {
    return this.chipIconSize;
  }
  
  public ColorStateList getChipIconTint() {
    return this.chipIconTint;
  }
  
  public float getChipMinHeight() {
    return this.chipMinHeight;
  }
  
  public float getChipStartPadding() {
    return this.chipStartPadding;
  }
  
  public ColorStateList getChipStrokeColor() {
    return this.chipStrokeColor;
  }
  
  public float getChipStrokeWidth() {
    return this.chipStrokeWidth;
  }
  
  public void getChipTouchBounds(RectF paramRectF) {
    calculateChipTouchBounds(getBounds(), paramRectF);
  }
  
  public Drawable getCloseIcon() {
    Drawable drawable = this.closeIcon;
    if (drawable != null) {
      drawable = DrawableCompat.unwrap(drawable);
    } else {
      drawable = null;
    } 
    return drawable;
  }
  
  public CharSequence getCloseIconContentDescription() {
    return this.closeIconContentDescription;
  }
  
  public float getCloseIconEndPadding() {
    return this.closeIconEndPadding;
  }
  
  public float getCloseIconSize() {
    return this.closeIconSize;
  }
  
  public float getCloseIconStartPadding() {
    return this.closeIconStartPadding;
  }
  
  public int[] getCloseIconState() {
    return this.closeIconStateSet;
  }
  
  public ColorStateList getCloseIconTint() {
    return this.closeIconTint;
  }
  
  public void getCloseIconTouchBounds(RectF paramRectF) {
    calculateCloseIconTouchBounds(getBounds(), paramRectF);
  }
  
  public ColorFilter getColorFilter() {
    return this.colorFilter;
  }
  
  public TextUtils.TruncateAt getEllipsize() {
    return this.truncateAt;
  }
  
  public MotionSpec getHideMotionSpec() {
    return this.hideMotionSpec;
  }
  
  public float getIconEndPadding() {
    return this.iconEndPadding;
  }
  
  public float getIconStartPadding() {
    return this.iconStartPadding;
  }
  
  public int getIntrinsicHeight() {
    return (int)this.chipMinHeight;
  }
  
  public int getIntrinsicWidth() {
    return Math.min(Math.round(this.chipStartPadding + calculateChipIconWidth() + this.textStartPadding + getTextWidth() + this.textEndPadding + calculateCloseIconWidth() + this.chipEndPadding), this.maxWidth);
  }
  
  public int getMaxWidth() {
    return this.maxWidth;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public void getOutline(Outline paramOutline) {
    Rect rect = getBounds();
    if (!rect.isEmpty()) {
      paramOutline.setRoundRect(rect, this.chipCornerRadius);
    } else {
      paramOutline.setRoundRect(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), this.chipCornerRadius);
    } 
    paramOutline.setAlpha(getAlpha() / 255.0F);
  }
  
  public ColorStateList getRippleColor() {
    return this.rippleColor;
  }
  
  public MotionSpec getShowMotionSpec() {
    return this.showMotionSpec;
  }
  
  public CharSequence getText() {
    return this.rawText;
  }
  
  public TextAppearance getTextAppearance() {
    return this.textAppearance;
  }
  
  public float getTextEndPadding() {
    return this.textEndPadding;
  }
  
  public float getTextStartPadding() {
    return this.textStartPadding;
  }
  
  public boolean getUseCompatRipple() {
    return this.useCompatRipple;
  }
  
  public void invalidateDrawable(Drawable paramDrawable) {
    Drawable.Callback callback = getCallback();
    if (callback != null)
      callback.invalidateDrawable(this); 
  }
  
  public boolean isCheckable() {
    return this.checkable;
  }
  
  @Deprecated
  public boolean isCheckedIconEnabled() {
    return isCheckedIconVisible();
  }
  
  public boolean isCheckedIconVisible() {
    return this.checkedIconVisible;
  }
  
  @Deprecated
  public boolean isChipIconEnabled() {
    return isChipIconVisible();
  }
  
  public boolean isChipIconVisible() {
    return this.chipIconVisible;
  }
  
  @Deprecated
  public boolean isCloseIconEnabled() {
    return isCloseIconVisible();
  }
  
  public boolean isCloseIconStateful() {
    return isStateful(this.closeIcon);
  }
  
  public boolean isCloseIconVisible() {
    return this.closeIconVisible;
  }
  
  public boolean isStateful() {
    return (isStateful(this.chipBackgroundColor) || isStateful(this.chipStrokeColor) || (this.useCompatRipple && isStateful(this.compatRippleColor)) || isStateful(this.textAppearance) || canShowCheckedIcon() || isStateful(this.chipIcon) || isStateful(this.checkedIcon) || isStateful(this.tint));
  }
  
  public boolean onLayoutDirectionChanged(int paramInt) {
    boolean bool1 = super.onLayoutDirectionChanged(paramInt);
    boolean bool2 = bool1;
    if (showsChipIcon())
      bool2 = bool1 | this.chipIcon.setLayoutDirection(paramInt); 
    bool1 = bool2;
    if (showsCheckedIcon())
      bool1 = bool2 | this.checkedIcon.setLayoutDirection(paramInt); 
    bool2 = bool1;
    if (showsCloseIcon())
      bool2 = bool1 | this.closeIcon.setLayoutDirection(paramInt); 
    if (bool2)
      invalidateSelf(); 
    return true;
  }
  
  protected boolean onLevelChange(int paramInt) {
    boolean bool1 = super.onLevelChange(paramInt);
    boolean bool2 = bool1;
    if (showsChipIcon())
      bool2 = bool1 | this.chipIcon.setLevel(paramInt); 
    bool1 = bool2;
    if (showsCheckedIcon())
      bool1 = bool2 | this.checkedIcon.setLevel(paramInt); 
    bool2 = bool1;
    if (showsCloseIcon())
      bool2 = bool1 | this.closeIcon.setLevel(paramInt); 
    if (bool2)
      invalidateSelf(); 
    return bool2;
  }
  
  protected void onSizeChange() {
    Delegate delegate = this.delegate.get();
    if (delegate != null)
      delegate.onChipDrawableSizeChange(); 
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    return onStateChange(paramArrayOfint, getCloseIconState());
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong) {
    Drawable.Callback callback = getCallback();
    if (callback != null)
      callback.scheduleDrawable(this, paramRunnable, paramLong); 
  }
  
  public void setAlpha(int paramInt) {
    if (this.alpha != paramInt) {
      this.alpha = paramInt;
      invalidateSelf();
    } 
  }
  
  public void setCheckable(boolean paramBoolean) {
    if (this.checkable != paramBoolean) {
      this.checkable = paramBoolean;
      float f1 = calculateChipIconWidth();
      if (!paramBoolean && this.currentChecked)
        this.currentChecked = false; 
      float f2 = calculateChipIconWidth();
      invalidateSelf();
      if (f1 != f2)
        onSizeChange(); 
    } 
  }
  
  public void setCheckableResource(int paramInt) {
    setCheckable(this.context.getResources().getBoolean(paramInt));
  }
  
  public void setCheckedIcon(Drawable paramDrawable) {
    if (this.checkedIcon != paramDrawable) {
      float f1 = calculateChipIconWidth();
      this.checkedIcon = paramDrawable;
      float f2 = calculateChipIconWidth();
      unapplyChildDrawable(this.checkedIcon);
      applyChildDrawable(this.checkedIcon);
      invalidateSelf();
      if (f1 != f2)
        onSizeChange(); 
    } 
  }
  
  @Deprecated
  public void setCheckedIconEnabled(boolean paramBoolean) {
    setCheckedIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setCheckedIconEnabledResource(int paramInt) {
    setCheckedIconVisible(this.context.getResources().getBoolean(paramInt));
  }
  
  public void setCheckedIconResource(int paramInt) {
    setCheckedIcon(AppCompatResources.getDrawable(this.context, paramInt));
  }
  
  public void setCheckedIconVisible(int paramInt) {
    setCheckedIconVisible(this.context.getResources().getBoolean(paramInt));
  }
  
  public void setCheckedIconVisible(boolean paramBoolean) {
    if (this.checkedIconVisible != paramBoolean) {
      boolean bool1;
      boolean bool = showsCheckedIcon();
      this.checkedIconVisible = paramBoolean;
      paramBoolean = showsCheckedIcon();
      if (bool != paramBoolean) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1) {
        if (paramBoolean) {
          applyChildDrawable(this.checkedIcon);
        } else {
          unapplyChildDrawable(this.checkedIcon);
        } 
        invalidateSelf();
        onSizeChange();
      } 
    } 
  }
  
  public void setChipBackgroundColor(ColorStateList paramColorStateList) {
    if (this.chipBackgroundColor != paramColorStateList) {
      this.chipBackgroundColor = paramColorStateList;
      onStateChange(getState());
    } 
  }
  
  public void setChipBackgroundColorResource(int paramInt) {
    setChipBackgroundColor(AppCompatResources.getColorStateList(this.context, paramInt));
  }
  
  public void setChipCornerRadius(float paramFloat) {
    if (this.chipCornerRadius != paramFloat) {
      this.chipCornerRadius = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setChipCornerRadiusResource(int paramInt) {
    setChipCornerRadius(this.context.getResources().getDimension(paramInt));
  }
  
  public void setChipEndPadding(float paramFloat) {
    if (this.chipEndPadding != paramFloat) {
      this.chipEndPadding = paramFloat;
      invalidateSelf();
      onSizeChange();
    } 
  }
  
  public void setChipEndPaddingResource(int paramInt) {
    setChipEndPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setChipIcon(Drawable paramDrawable) {
    Drawable drawable = getChipIcon();
    if (drawable != paramDrawable) {
      float f1 = calculateChipIconWidth();
      if (paramDrawable != null) {
        paramDrawable = DrawableCompat.wrap(paramDrawable).mutate();
      } else {
        paramDrawable = null;
      } 
      this.chipIcon = paramDrawable;
      float f2 = calculateChipIconWidth();
      unapplyChildDrawable(drawable);
      if (showsChipIcon())
        applyChildDrawable(this.chipIcon); 
      invalidateSelf();
      if (f1 != f2)
        onSizeChange(); 
    } 
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
    setChipIcon(AppCompatResources.getDrawable(this.context, paramInt));
  }
  
  public void setChipIconSize(float paramFloat) {
    if (this.chipIconSize != paramFloat) {
      float f = calculateChipIconWidth();
      this.chipIconSize = paramFloat;
      paramFloat = calculateChipIconWidth();
      invalidateSelf();
      if (f != paramFloat)
        onSizeChange(); 
    } 
  }
  
  public void setChipIconSizeResource(int paramInt) {
    setChipIconSize(this.context.getResources().getDimension(paramInt));
  }
  
  public void setChipIconTint(ColorStateList paramColorStateList) {
    if (this.chipIconTint != paramColorStateList) {
      this.chipIconTint = paramColorStateList;
      if (showsChipIcon())
        DrawableCompat.setTintList(this.chipIcon, paramColorStateList); 
      onStateChange(getState());
    } 
  }
  
  public void setChipIconTintResource(int paramInt) {
    setChipIconTint(AppCompatResources.getColorStateList(this.context, paramInt));
  }
  
  public void setChipIconVisible(int paramInt) {
    setChipIconVisible(this.context.getResources().getBoolean(paramInt));
  }
  
  public void setChipIconVisible(boolean paramBoolean) {
    if (this.chipIconVisible != paramBoolean) {
      boolean bool1;
      boolean bool = showsChipIcon();
      this.chipIconVisible = paramBoolean;
      paramBoolean = showsChipIcon();
      if (bool != paramBoolean) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1) {
        if (paramBoolean) {
          applyChildDrawable(this.chipIcon);
        } else {
          unapplyChildDrawable(this.chipIcon);
        } 
        invalidateSelf();
        onSizeChange();
      } 
    } 
  }
  
  public void setChipMinHeight(float paramFloat) {
    if (this.chipMinHeight != paramFloat) {
      this.chipMinHeight = paramFloat;
      invalidateSelf();
      onSizeChange();
    } 
  }
  
  public void setChipMinHeightResource(int paramInt) {
    setChipMinHeight(this.context.getResources().getDimension(paramInt));
  }
  
  public void setChipStartPadding(float paramFloat) {
    if (this.chipStartPadding != paramFloat) {
      this.chipStartPadding = paramFloat;
      invalidateSelf();
      onSizeChange();
    } 
  }
  
  public void setChipStartPaddingResource(int paramInt) {
    setChipStartPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setChipStrokeColor(ColorStateList paramColorStateList) {
    if (this.chipStrokeColor != paramColorStateList) {
      this.chipStrokeColor = paramColorStateList;
      onStateChange(getState());
    } 
  }
  
  public void setChipStrokeColorResource(int paramInt) {
    setChipStrokeColor(AppCompatResources.getColorStateList(this.context, paramInt));
  }
  
  public void setChipStrokeWidth(float paramFloat) {
    if (this.chipStrokeWidth != paramFloat) {
      this.chipStrokeWidth = paramFloat;
      this.chipPaint.setStrokeWidth(paramFloat);
      invalidateSelf();
    } 
  }
  
  public void setChipStrokeWidthResource(int paramInt) {
    setChipStrokeWidth(this.context.getResources().getDimension(paramInt));
  }
  
  public void setCloseIcon(Drawable paramDrawable) {
    Drawable drawable = getCloseIcon();
    if (drawable != paramDrawable) {
      float f1 = calculateCloseIconWidth();
      if (paramDrawable != null) {
        paramDrawable = DrawableCompat.wrap(paramDrawable).mutate();
      } else {
        paramDrawable = null;
      } 
      this.closeIcon = paramDrawable;
      float f2 = calculateCloseIconWidth();
      unapplyChildDrawable(drawable);
      if (showsCloseIcon())
        applyChildDrawable(this.closeIcon); 
      invalidateSelf();
      if (f1 != f2)
        onSizeChange(); 
    } 
  }
  
  public void setCloseIconContentDescription(CharSequence paramCharSequence) {
    if (this.closeIconContentDescription != paramCharSequence) {
      this.closeIconContentDescription = BidiFormatter.getInstance().unicodeWrap(paramCharSequence);
      invalidateSelf();
    } 
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
    if (this.closeIconEndPadding != paramFloat) {
      this.closeIconEndPadding = paramFloat;
      invalidateSelf();
      if (showsCloseIcon())
        onSizeChange(); 
    } 
  }
  
  public void setCloseIconEndPaddingResource(int paramInt) {
    setCloseIconEndPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setCloseIconResource(int paramInt) {
    setCloseIcon(AppCompatResources.getDrawable(this.context, paramInt));
  }
  
  public void setCloseIconSize(float paramFloat) {
    if (this.closeIconSize != paramFloat) {
      this.closeIconSize = paramFloat;
      invalidateSelf();
      if (showsCloseIcon())
        onSizeChange(); 
    } 
  }
  
  public void setCloseIconSizeResource(int paramInt) {
    setCloseIconSize(this.context.getResources().getDimension(paramInt));
  }
  
  public void setCloseIconStartPadding(float paramFloat) {
    if (this.closeIconStartPadding != paramFloat) {
      this.closeIconStartPadding = paramFloat;
      invalidateSelf();
      if (showsCloseIcon())
        onSizeChange(); 
    } 
  }
  
  public void setCloseIconStartPaddingResource(int paramInt) {
    setCloseIconStartPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public boolean setCloseIconState(int[] paramArrayOfint) {
    if (!Arrays.equals(this.closeIconStateSet, paramArrayOfint)) {
      this.closeIconStateSet = paramArrayOfint;
      if (showsCloseIcon())
        return onStateChange(getState(), paramArrayOfint); 
    } 
    return false;
  }
  
  public void setCloseIconTint(ColorStateList paramColorStateList) {
    if (this.closeIconTint != paramColorStateList) {
      this.closeIconTint = paramColorStateList;
      if (showsCloseIcon())
        DrawableCompat.setTintList(this.closeIcon, paramColorStateList); 
      onStateChange(getState());
    } 
  }
  
  public void setCloseIconTintResource(int paramInt) {
    setCloseIconTint(AppCompatResources.getColorStateList(this.context, paramInt));
  }
  
  public void setCloseIconVisible(int paramInt) {
    setCloseIconVisible(this.context.getResources().getBoolean(paramInt));
  }
  
  public void setCloseIconVisible(boolean paramBoolean) {
    if (this.closeIconVisible != paramBoolean) {
      boolean bool1;
      boolean bool = showsCloseIcon();
      this.closeIconVisible = paramBoolean;
      paramBoolean = showsCloseIcon();
      if (bool != paramBoolean) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1) {
        if (paramBoolean) {
          applyChildDrawable(this.closeIcon);
        } else {
          unapplyChildDrawable(this.closeIcon);
        } 
        invalidateSelf();
        onSizeChange();
      } 
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (this.colorFilter != paramColorFilter) {
      this.colorFilter = paramColorFilter;
      invalidateSelf();
    } 
  }
  
  public void setDelegate(Delegate paramDelegate) {
    this.delegate = new WeakReference<>(paramDelegate);
  }
  
  public void setEllipsize(TextUtils.TruncateAt paramTruncateAt) {
    this.truncateAt = paramTruncateAt;
  }
  
  public void setHideMotionSpec(MotionSpec paramMotionSpec) {
    this.hideMotionSpec = paramMotionSpec;
  }
  
  public void setHideMotionSpecResource(int paramInt) {
    setHideMotionSpec(MotionSpec.createFromResource(this.context, paramInt));
  }
  
  public void setIconEndPadding(float paramFloat) {
    if (this.iconEndPadding != paramFloat) {
      float f = calculateChipIconWidth();
      this.iconEndPadding = paramFloat;
      paramFloat = calculateChipIconWidth();
      invalidateSelf();
      if (f != paramFloat)
        onSizeChange(); 
    } 
  }
  
  public void setIconEndPaddingResource(int paramInt) {
    setIconEndPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setIconStartPadding(float paramFloat) {
    if (this.iconStartPadding != paramFloat) {
      float f = calculateChipIconWidth();
      this.iconStartPadding = paramFloat;
      paramFloat = calculateChipIconWidth();
      invalidateSelf();
      if (f != paramFloat)
        onSizeChange(); 
    } 
  }
  
  public void setIconStartPaddingResource(int paramInt) {
    setIconStartPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setMaxWidth(int paramInt) {
    this.maxWidth = paramInt;
  }
  
  public void setRippleColor(ColorStateList paramColorStateList) {
    if (this.rippleColor != paramColorStateList) {
      this.rippleColor = paramColorStateList;
      updateCompatRippleColor();
      onStateChange(getState());
    } 
  }
  
  public void setRippleColorResource(int paramInt) {
    setRippleColor(AppCompatResources.getColorStateList(this.context, paramInt));
  }
  
  void setShouldDrawText(boolean paramBoolean) {
    this.shouldDrawText = paramBoolean;
  }
  
  public void setShowMotionSpec(MotionSpec paramMotionSpec) {
    this.showMotionSpec = paramMotionSpec;
  }
  
  public void setShowMotionSpecResource(int paramInt) {
    setShowMotionSpec(MotionSpec.createFromResource(this.context, paramInt));
  }
  
  public void setText(CharSequence paramCharSequence) {
    CharSequence charSequence = paramCharSequence;
    if (paramCharSequence == null)
      charSequence = ""; 
    if (this.rawText != charSequence) {
      this.rawText = charSequence;
      this.unicodeWrappedText = BidiFormatter.getInstance().unicodeWrap(charSequence);
      this.textWidthDirty = true;
      invalidateSelf();
      onSizeChange();
    } 
  }
  
  public void setTextAppearance(TextAppearance paramTextAppearance) {
    if (this.textAppearance != paramTextAppearance) {
      this.textAppearance = paramTextAppearance;
      if (paramTextAppearance != null) {
        paramTextAppearance.updateMeasureState(this.context, this.textPaint, this.fontCallback);
        this.textWidthDirty = true;
      } 
      onStateChange(getState());
      onSizeChange();
    } 
  }
  
  public void setTextAppearanceResource(int paramInt) {
    setTextAppearance(new TextAppearance(this.context, paramInt));
  }
  
  public void setTextEndPadding(float paramFloat) {
    if (this.textEndPadding != paramFloat) {
      this.textEndPadding = paramFloat;
      invalidateSelf();
      onSizeChange();
    } 
  }
  
  public void setTextEndPaddingResource(int paramInt) {
    setTextEndPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setTextResource(int paramInt) {
    setText(this.context.getResources().getString(paramInt));
  }
  
  public void setTextStartPadding(float paramFloat) {
    if (this.textStartPadding != paramFloat) {
      this.textStartPadding = paramFloat;
      invalidateSelf();
      onSizeChange();
    } 
  }
  
  public void setTextStartPaddingResource(int paramInt) {
    setTextStartPadding(this.context.getResources().getDimension(paramInt));
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    if (this.tint != paramColorStateList) {
      this.tint = paramColorStateList;
      onStateChange(getState());
    } 
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    if (this.tintMode != paramMode) {
      this.tintMode = paramMode;
      this.tintFilter = DrawableUtils.updateTintFilter(this, this.tint, paramMode);
      invalidateSelf();
    } 
  }
  
  public void setUseCompatRipple(boolean paramBoolean) {
    if (this.useCompatRipple != paramBoolean) {
      this.useCompatRipple = paramBoolean;
      updateCompatRippleColor();
      onStateChange(getState());
    } 
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool1 = super.setVisible(paramBoolean1, paramBoolean2);
    boolean bool2 = bool1;
    if (showsChipIcon())
      bool2 = bool1 | this.chipIcon.setVisible(paramBoolean1, paramBoolean2); 
    bool1 = bool2;
    if (showsCheckedIcon())
      bool1 = bool2 | this.checkedIcon.setVisible(paramBoolean1, paramBoolean2); 
    bool2 = bool1;
    if (showsCloseIcon())
      bool2 = bool1 | this.closeIcon.setVisible(paramBoolean1, paramBoolean2); 
    if (bool2)
      invalidateSelf(); 
    return bool2;
  }
  
  boolean shouldDrawText() {
    return this.shouldDrawText;
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable) {
    Drawable.Callback callback = getCallback();
    if (callback != null)
      callback.unscheduleDrawable(this, paramRunnable); 
  }
  
  public static interface Delegate {
    void onChipDrawableSizeChange();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/chip/ChipDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */