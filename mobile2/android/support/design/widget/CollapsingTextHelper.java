package android.support.design.widget;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.animation.AnimationUtils;
import android.support.v4.math.MathUtils;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

public final class CollapsingTextHelper {
  private static final boolean DEBUG_DRAW = false;
  
  private static final Paint DEBUG_DRAW_PAINT;
  
  private static final boolean USE_SCALING_TEXTURE;
  
  private boolean boundsChanged;
  
  private final Rect collapsedBounds;
  
  private float collapsedDrawX;
  
  private float collapsedDrawY;
  
  private int collapsedShadowColor;
  
  private float collapsedShadowDx;
  
  private float collapsedShadowDy;
  
  private float collapsedShadowRadius;
  
  private ColorStateList collapsedTextColor;
  
  private int collapsedTextGravity = 16;
  
  private float collapsedTextSize = 15.0F;
  
  private Typeface collapsedTypeface;
  
  private final RectF currentBounds;
  
  private float currentDrawX;
  
  private float currentDrawY;
  
  private float currentTextSize;
  
  private Typeface currentTypeface;
  
  private boolean drawTitle;
  
  private final Rect expandedBounds;
  
  private float expandedDrawX;
  
  private float expandedDrawY;
  
  private float expandedFraction;
  
  private int expandedShadowColor;
  
  private float expandedShadowDx;
  
  private float expandedShadowDy;
  
  private float expandedShadowRadius;
  
  private ColorStateList expandedTextColor;
  
  private int expandedTextGravity = 16;
  
  private float expandedTextSize = 15.0F;
  
  private Bitmap expandedTitleTexture;
  
  private Typeface expandedTypeface;
  
  private boolean isRtl;
  
  private TimeInterpolator positionInterpolator;
  
  private float scale;
  
  private int[] state;
  
  private CharSequence text;
  
  private final TextPaint textPaint;
  
  private TimeInterpolator textSizeInterpolator;
  
  private CharSequence textToDraw;
  
  private float textureAscent;
  
  private float textureDescent;
  
  private Paint texturePaint;
  
  private final TextPaint tmpPaint;
  
  private boolean useTexture;
  
  private final View view;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT < 18) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_SCALING_TEXTURE = bool;
    DEBUG_DRAW_PAINT = null;
    if (false)
      throw new NullPointerException(); 
  }
  
  public CollapsingTextHelper(View paramView) {
    this.view = paramView;
    this.textPaint = new TextPaint(129);
    this.tmpPaint = new TextPaint((Paint)this.textPaint);
    this.collapsedBounds = new Rect();
    this.expandedBounds = new Rect();
    this.currentBounds = new RectF();
  }
  
  private static int blendColors(int paramInt1, int paramInt2, float paramFloat) {
    float f1 = 1.0F - paramFloat;
    float f2 = Color.alpha(paramInt1);
    float f3 = Color.alpha(paramInt2);
    float f4 = Color.red(paramInt1);
    float f5 = Color.red(paramInt2);
    float f6 = Color.green(paramInt1);
    float f7 = Color.green(paramInt2);
    float f8 = Color.blue(paramInt1);
    float f9 = Color.blue(paramInt2);
    return Color.argb((int)(f2 * f1 + f3 * paramFloat), (int)(f4 * f1 + f5 * paramFloat), (int)(f6 * f1 + f7 * paramFloat), (int)(f8 * f1 + f9 * paramFloat));
  }
  
  private void calculateBaseOffsets() {
    float f1 = this.currentTextSize;
    calculateUsingTextSize(this.collapsedTextSize);
    CharSequence charSequence = this.textToDraw;
    float f2 = 0.0F;
    if (charSequence != null) {
      f3 = this.textPaint.measureText(charSequence, 0, charSequence.length());
    } else {
      f3 = 0.0F;
    } 
    int i = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl);
    int j = i & 0x70;
    if (j != 48) {
      if (j != 80) {
        float f4 = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0F;
        float f5 = this.textPaint.descent();
        this.collapsedDrawY = this.collapsedBounds.centerY() + f4 - f5;
      } else {
        this.collapsedDrawY = this.collapsedBounds.bottom;
      } 
    } else {
      this.collapsedDrawY = this.collapsedBounds.top - this.textPaint.ascent();
    } 
    j = i & 0x800007;
    if (j != 1) {
      if (j != 5) {
        this.collapsedDrawX = this.collapsedBounds.left;
      } else {
        this.collapsedDrawX = this.collapsedBounds.right - f3;
      } 
    } else {
      this.collapsedDrawX = this.collapsedBounds.centerX() - f3 / 2.0F;
    } 
    calculateUsingTextSize(this.expandedTextSize);
    charSequence = this.textToDraw;
    float f3 = f2;
    if (charSequence != null)
      f3 = this.textPaint.measureText(charSequence, 0, charSequence.length()); 
    j = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl);
    i = j & 0x70;
    if (i != 48) {
      if (i != 80) {
        f2 = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0F;
        float f = this.textPaint.descent();
        this.expandedDrawY = this.expandedBounds.centerY() + f2 - f;
      } else {
        this.expandedDrawY = this.expandedBounds.bottom;
      } 
    } else {
      this.expandedDrawY = this.expandedBounds.top - this.textPaint.ascent();
    } 
    j &= 0x800007;
    if (j != 1) {
      if (j != 5) {
        this.expandedDrawX = this.expandedBounds.left;
      } else {
        this.expandedDrawX = this.expandedBounds.right - f3;
      } 
    } else {
      this.expandedDrawX = this.expandedBounds.centerX() - f3 / 2.0F;
    } 
    clearTexture();
    setInterpolatedTextSize(f1);
  }
  
  private void calculateCurrentOffsets() {
    calculateOffsets(this.expandedFraction);
  }
  
  private boolean calculateIsRtl(CharSequence paramCharSequence) {
    TextDirectionHeuristicCompat textDirectionHeuristicCompat;
    int i = ViewCompat.getLayoutDirection(this.view);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    if (bool) {
      textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
    } else {
      textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
    } 
    return textDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
  }
  
  private void calculateOffsets(float paramFloat) {
    interpolateBounds(paramFloat);
    this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, paramFloat, this.positionInterpolator);
    this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, paramFloat, this.positionInterpolator);
    setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, paramFloat, this.textSizeInterpolator));
    if (this.collapsedTextColor != this.expandedTextColor) {
      this.textPaint.setColor(blendColors(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), paramFloat));
    } else {
      this.textPaint.setColor(getCurrentCollapsedTextColor());
    } 
    this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, paramFloat, null), lerp(this.expandedShadowDx, this.collapsedShadowDx, paramFloat, null), lerp(this.expandedShadowDy, this.collapsedShadowDy, paramFloat, null), blendColors(this.expandedShadowColor, this.collapsedShadowColor, paramFloat));
    ViewCompat.postInvalidateOnAnimation(this.view);
  }
  
  private void calculateUsingTextSize(float paramFloat) {
    float f3;
    if (this.text == null)
      return; 
    float f1 = this.collapsedBounds.width();
    float f2 = this.expandedBounds.width();
    boolean bool1 = false;
    boolean bool2 = false;
    if (isClose(paramFloat, this.collapsedTextSize)) {
      f3 = this.collapsedTextSize;
      this.scale = 1.0F;
      Typeface typeface1 = this.currentTypeface;
      Typeface typeface2 = this.collapsedTypeface;
      if (typeface1 != typeface2) {
        this.currentTypeface = typeface2;
        bool2 = true;
      } 
      paramFloat = f1;
    } else {
      f3 = this.expandedTextSize;
      Typeface typeface1 = this.currentTypeface;
      Typeface typeface2 = this.expandedTypeface;
      bool2 = bool1;
      if (typeface1 != typeface2) {
        this.currentTypeface = typeface2;
        bool2 = true;
      } 
      if (isClose(paramFloat, this.expandedTextSize)) {
        this.scale = 1.0F;
      } else {
        this.scale = paramFloat / this.expandedTextSize;
      } 
      paramFloat = this.collapsedTextSize / this.expandedTextSize;
      if (f2 * paramFloat > f1) {
        paramFloat = Math.min(f1 / paramFloat, f2);
      } else {
        paramFloat = f2;
      } 
    } 
    boolean bool3 = true;
    bool1 = bool2;
    if (paramFloat > 0.0F) {
      if (this.currentTextSize != f3 || this.boundsChanged || bool2) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.currentTextSize = f3;
      this.boundsChanged = false;
      bool1 = bool2;
    } 
    if (this.textToDraw == null || bool1) {
      this.textPaint.setTextSize(this.currentTextSize);
      this.textPaint.setTypeface(this.currentTypeface);
      TextPaint textPaint = this.textPaint;
      if (this.scale == 1.0F)
        bool3 = false; 
      textPaint.setLinearText(bool3);
      CharSequence charSequence = TextUtils.ellipsize(this.text, this.textPaint, paramFloat, TextUtils.TruncateAt.END);
      if (!TextUtils.equals(charSequence, this.textToDraw)) {
        this.textToDraw = charSequence;
        this.isRtl = calculateIsRtl(charSequence);
      } 
    } 
  }
  
  private void clearTexture() {
    Bitmap bitmap = this.expandedTitleTexture;
    if (bitmap != null) {
      bitmap.recycle();
      this.expandedTitleTexture = null;
    } 
  }
  
  private void ensureExpandedTexture() {
    if (this.expandedTitleTexture != null || this.expandedBounds.isEmpty() || TextUtils.isEmpty(this.textToDraw))
      return; 
    calculateOffsets(0.0F);
    this.textureAscent = this.textPaint.ascent();
    this.textureDescent = this.textPaint.descent();
    TextPaint textPaint = this.textPaint;
    CharSequence charSequence2 = this.textToDraw;
    int i = Math.round(textPaint.measureText(charSequence2, 0, charSequence2.length()));
    int j = Math.round(this.textureDescent - this.textureAscent);
    if (i <= 0 || j <= 0)
      return; 
    this.expandedTitleTexture = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(this.expandedTitleTexture);
    CharSequence charSequence1 = this.textToDraw;
    canvas.drawText(charSequence1, 0, charSequence1.length(), 0.0F, j - this.textPaint.descent(), (Paint)this.textPaint);
    if (this.texturePaint == null)
      this.texturePaint = new Paint(3); 
  }
  
  private int getCurrentExpandedTextColor() {
    int[] arrayOfInt = this.state;
    return (arrayOfInt != null) ? this.expandedTextColor.getColorForState(arrayOfInt, 0) : this.expandedTextColor.getDefaultColor();
  }
  
  private void getTextPaintCollapsed(TextPaint paramTextPaint) {
    paramTextPaint.setTextSize(this.collapsedTextSize);
    paramTextPaint.setTypeface(this.collapsedTypeface);
  }
  
  private void interpolateBounds(float paramFloat) {
    this.currentBounds.left = lerp(this.expandedBounds.left, this.collapsedBounds.left, paramFloat, this.positionInterpolator);
    this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, paramFloat, this.positionInterpolator);
    this.currentBounds.right = lerp(this.expandedBounds.right, this.collapsedBounds.right, paramFloat, this.positionInterpolator);
    this.currentBounds.bottom = lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, paramFloat, this.positionInterpolator);
  }
  
  private static boolean isClose(float paramFloat1, float paramFloat2) {
    boolean bool;
    if (Math.abs(paramFloat1 - paramFloat2) < 0.001F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3, TimeInterpolator paramTimeInterpolator) {
    float f = paramFloat3;
    if (paramTimeInterpolator != null)
      f = paramTimeInterpolator.getInterpolation(paramFloat3); 
    return AnimationUtils.lerp(paramFloat1, paramFloat2, f);
  }
  
  private Typeface readFontFamilyTypeface(int paramInt) {
    TypedArray typedArray = this.view.getContext().obtainStyledAttributes(paramInt, new int[] { 16843692 });
    try {
      String str = typedArray.getString(0);
      if (str != null)
        return Typeface.create(str, 0); 
      return null;
    } finally {
      typedArray.recycle();
    } 
  }
  
  private static boolean rectEquals(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool;
    if (paramRect.left == paramInt1 && paramRect.top == paramInt2 && paramRect.right == paramInt3 && paramRect.bottom == paramInt4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setInterpolatedTextSize(float paramFloat) {
    boolean bool;
    calculateUsingTextSize(paramFloat);
    if (USE_SCALING_TEXTURE && this.scale != 1.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    this.useTexture = bool;
    if (bool)
      ensureExpandedTexture(); 
    ViewCompat.postInvalidateOnAnimation(this.view);
  }
  
  public float calculateCollapsedTextWidth() {
    if (this.text == null)
      return 0.0F; 
    getTextPaintCollapsed(this.tmpPaint);
    TextPaint textPaint = this.tmpPaint;
    CharSequence charSequence = this.text;
    return textPaint.measureText(charSequence, 0, charSequence.length());
  }
  
  public void draw(Canvas paramCanvas) {
    int i = paramCanvas.save();
    if (this.textToDraw != null && this.drawTitle) {
      boolean bool;
      float f3;
      float f1 = this.currentDrawX;
      float f2 = this.currentDrawY;
      if (this.useTexture && this.expandedTitleTexture != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        float f4 = this.textureAscent;
        f3 = this.scale;
        float f5 = this.textureDescent;
        f3 = f4 * f3;
      } else {
        float f5 = this.textPaint.ascent();
        f3 = this.scale;
        this.textPaint.descent();
        float f4 = this.scale;
        f3 = f5 * f3;
      } 
      if (bool) {
        f3 = f2 + f3;
      } else {
        f3 = f2;
      } 
      f2 = this.scale;
      if (f2 != 1.0F)
        paramCanvas.scale(f2, f2, f1, f3); 
      if (bool) {
        paramCanvas.drawBitmap(this.expandedTitleTexture, f1, f3, this.texturePaint);
      } else {
        CharSequence charSequence = this.textToDraw;
        paramCanvas.drawText(charSequence, 0, charSequence.length(), f1, f3, (Paint)this.textPaint);
      } 
    } 
    paramCanvas.restoreToCount(i);
  }
  
  public void getCollapsedTextActualBounds(RectF paramRectF) {
    float f;
    boolean bool = calculateIsRtl(this.text);
    Rect rect = this.collapsedBounds;
    if (!bool) {
      f = rect.left;
    } else {
      f = rect.right - calculateCollapsedTextWidth();
    } 
    paramRectF.left = f;
    paramRectF.top = this.collapsedBounds.top;
    if (!bool) {
      f = paramRectF.left + calculateCollapsedTextWidth();
    } else {
      f = this.collapsedBounds.right;
    } 
    paramRectF.right = f;
    paramRectF.bottom = this.collapsedBounds.top + getCollapsedTextHeight();
  }
  
  public ColorStateList getCollapsedTextColor() {
    return this.collapsedTextColor;
  }
  
  public int getCollapsedTextGravity() {
    return this.collapsedTextGravity;
  }
  
  public float getCollapsedTextHeight() {
    getTextPaintCollapsed(this.tmpPaint);
    return -this.tmpPaint.ascent();
  }
  
  public float getCollapsedTextSize() {
    return this.collapsedTextSize;
  }
  
  public Typeface getCollapsedTypeface() {
    Typeface typeface = this.collapsedTypeface;
    if (typeface == null)
      typeface = Typeface.DEFAULT; 
    return typeface;
  }
  
  public int getCurrentCollapsedTextColor() {
    int[] arrayOfInt = this.state;
    return (arrayOfInt != null) ? this.collapsedTextColor.getColorForState(arrayOfInt, 0) : this.collapsedTextColor.getDefaultColor();
  }
  
  public ColorStateList getExpandedTextColor() {
    return this.expandedTextColor;
  }
  
  public int getExpandedTextGravity() {
    return this.expandedTextGravity;
  }
  
  public float getExpandedTextSize() {
    return this.expandedTextSize;
  }
  
  public Typeface getExpandedTypeface() {
    Typeface typeface = this.expandedTypeface;
    if (typeface == null)
      typeface = Typeface.DEFAULT; 
    return typeface;
  }
  
  public float getExpansionFraction() {
    return this.expandedFraction;
  }
  
  public CharSequence getText() {
    return this.text;
  }
  
  public final boolean isStateful() {
    ColorStateList colorStateList = this.collapsedTextColor;
    if (colorStateList == null || !colorStateList.isStateful()) {
      colorStateList = this.expandedTextColor;
      return (colorStateList != null && colorStateList.isStateful());
    } 
    return true;
  }
  
  void onBoundsChanged() {
    boolean bool;
    if (this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.drawTitle = bool;
  }
  
  public void recalculate() {
    if (this.view.getHeight() > 0 && this.view.getWidth() > 0) {
      calculateBaseOffsets();
      calculateCurrentOffsets();
    } 
  }
  
  public void setCollapsedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!rectEquals(this.collapsedBounds, paramInt1, paramInt2, paramInt3, paramInt4)) {
      this.collapsedBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      this.boundsChanged = true;
      onBoundsChanged();
    } 
  }
  
  public void setCollapsedTextAppearance(int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.view.getContext(), paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor))
      this.collapsedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor); 
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize))
      this.collapsedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.collapsedTextSize); 
    this.collapsedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
    this.collapsedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
    this.collapsedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
    this.collapsedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
    tintTypedArray.recycle();
    if (Build.VERSION.SDK_INT >= 16)
      this.collapsedTypeface = readFontFamilyTypeface(paramInt); 
    recalculate();
  }
  
  public void setCollapsedTextColor(ColorStateList paramColorStateList) {
    if (this.collapsedTextColor != paramColorStateList) {
      this.collapsedTextColor = paramColorStateList;
      recalculate();
    } 
  }
  
  public void setCollapsedTextGravity(int paramInt) {
    if (this.collapsedTextGravity != paramInt) {
      this.collapsedTextGravity = paramInt;
      recalculate();
    } 
  }
  
  public void setCollapsedTextSize(float paramFloat) {
    if (this.collapsedTextSize != paramFloat) {
      this.collapsedTextSize = paramFloat;
      recalculate();
    } 
  }
  
  public void setCollapsedTypeface(Typeface paramTypeface) {
    if (this.collapsedTypeface != paramTypeface) {
      this.collapsedTypeface = paramTypeface;
      recalculate();
    } 
  }
  
  public void setExpandedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!rectEquals(this.expandedBounds, paramInt1, paramInt2, paramInt3, paramInt4)) {
      this.expandedBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      this.boundsChanged = true;
      onBoundsChanged();
    } 
  }
  
  public void setExpandedTextAppearance(int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.view.getContext(), paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor))
      this.expandedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor); 
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize))
      this.expandedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.expandedTextSize); 
    this.expandedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
    this.expandedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
    this.expandedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
    this.expandedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
    tintTypedArray.recycle();
    if (Build.VERSION.SDK_INT >= 16)
      this.expandedTypeface = readFontFamilyTypeface(paramInt); 
    recalculate();
  }
  
  public void setExpandedTextColor(ColorStateList paramColorStateList) {
    if (this.expandedTextColor != paramColorStateList) {
      this.expandedTextColor = paramColorStateList;
      recalculate();
    } 
  }
  
  public void setExpandedTextGravity(int paramInt) {
    if (this.expandedTextGravity != paramInt) {
      this.expandedTextGravity = paramInt;
      recalculate();
    } 
  }
  
  public void setExpandedTextSize(float paramFloat) {
    if (this.expandedTextSize != paramFloat) {
      this.expandedTextSize = paramFloat;
      recalculate();
    } 
  }
  
  public void setExpandedTypeface(Typeface paramTypeface) {
    if (this.expandedTypeface != paramTypeface) {
      this.expandedTypeface = paramTypeface;
      recalculate();
    } 
  }
  
  public void setExpansionFraction(float paramFloat) {
    paramFloat = MathUtils.clamp(paramFloat, 0.0F, 1.0F);
    if (paramFloat != this.expandedFraction) {
      this.expandedFraction = paramFloat;
      calculateCurrentOffsets();
    } 
  }
  
  public void setPositionInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.positionInterpolator = paramTimeInterpolator;
    recalculate();
  }
  
  public final boolean setState(int[] paramArrayOfint) {
    this.state = paramArrayOfint;
    if (isStateful()) {
      recalculate();
      return true;
    } 
    return false;
  }
  
  public void setText(CharSequence paramCharSequence) {
    if (paramCharSequence == null || !paramCharSequence.equals(this.text)) {
      this.text = paramCharSequence;
      this.textToDraw = null;
      clearTexture();
      recalculate();
    } 
  }
  
  public void setTextSizeInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.textSizeInterpolator = paramTimeInterpolator;
    recalculate();
  }
  
  public void setTypefaces(Typeface paramTypeface) {
    this.expandedTypeface = paramTypeface;
    this.collapsedTypeface = paramTypeface;
    recalculate();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/CollapsingTextHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */