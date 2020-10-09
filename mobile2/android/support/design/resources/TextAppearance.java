package android.support.design.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.R;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.util.Log;

public class TextAppearance {
  private static final String TAG = "TextAppearance";
  
  private static final int TYPEFACE_MONOSPACE = 3;
  
  private static final int TYPEFACE_SANS = 1;
  
  private static final int TYPEFACE_SERIF = 2;
  
  private Typeface font;
  
  public final String fontFamily;
  
  private final int fontFamilyResourceId;
  
  private boolean fontResolved = false;
  
  public final ColorStateList shadowColor;
  
  public final float shadowDx;
  
  public final float shadowDy;
  
  public final float shadowRadius;
  
  public final boolean textAllCaps;
  
  public final ColorStateList textColor;
  
  public final ColorStateList textColorHint;
  
  public final ColorStateList textColorLink;
  
  public final float textSize;
  
  public final int textStyle;
  
  public final int typeface;
  
  public TextAppearance(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramInt, R.styleable.TextAppearance);
    this.textSize = typedArray.getDimension(R.styleable.TextAppearance_android_textSize, 0.0F);
    this.textColor = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_textColor);
    this.textColorHint = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_textColorHint);
    this.textColorLink = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_textColorLink);
    this.textStyle = typedArray.getInt(R.styleable.TextAppearance_android_textStyle, 0);
    this.typeface = typedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
    paramInt = MaterialResources.getIndexWithValue(typedArray, R.styleable.TextAppearance_fontFamily, R.styleable.TextAppearance_android_fontFamily);
    this.fontFamilyResourceId = typedArray.getResourceId(paramInt, 0);
    this.fontFamily = typedArray.getString(paramInt);
    this.textAllCaps = typedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
    this.shadowColor = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_shadowColor);
    this.shadowDx = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
    this.shadowDy = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
    this.shadowRadius = typedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
    typedArray.recycle();
  }
  
  private void createFallbackTypeface() {
    if (this.font == null)
      this.font = Typeface.create(this.fontFamily, this.textStyle); 
    if (this.font == null) {
      int i = this.typeface;
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            this.font = Typeface.DEFAULT;
          } else {
            this.font = Typeface.MONOSPACE;
          } 
        } else {
          this.font = Typeface.SERIF;
        } 
      } else {
        this.font = Typeface.SANS_SERIF;
      } 
      Typeface typeface = this.font;
      if (typeface != null)
        this.font = Typeface.create(typeface, this.textStyle); 
    } 
  }
  
  public Typeface getFont(Context paramContext) {
    if (this.fontResolved)
      return this.font; 
    if (!paramContext.isRestricted())
      try {
        Typeface typeface = ResourcesCompat.getFont(paramContext, this.fontFamilyResourceId);
        this.font = typeface;
        if (typeface != null)
          this.font = Typeface.create(typeface, this.textStyle); 
      } catch (UnsupportedOperationException unsupportedOperationException) {
      
      } catch (android.content.res.Resources.NotFoundException notFoundException) {
      
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error loading font ");
        stringBuilder.append(this.fontFamily);
        Log.d("TextAppearance", stringBuilder.toString(), exception);
      }  
    createFallbackTypeface();
    this.fontResolved = true;
    return this.font;
  }
  
  public void getFontAsync(Context paramContext, TextPaint paramTextPaint, ResourcesCompat.FontCallback paramFontCallback) {
    if (this.fontResolved) {
      updateTextPaintMeasureState(paramTextPaint, this.font);
      return;
    } 
    createFallbackTypeface();
    if (paramContext.isRestricted()) {
      this.fontResolved = true;
      updateTextPaintMeasureState(paramTextPaint, this.font);
      return;
    } 
    try {
      int i = this.fontFamilyResourceId;
      ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback() {
          public void onFontRetrievalFailed(int param1Int) {
            TextAppearance.this.createFallbackTypeface();
            TextAppearance.access$102(TextAppearance.this, true);
            callback.onFontRetrievalFailed(param1Int);
          }
          
          public void onFontRetrieved(Typeface param1Typeface) {
            TextAppearance textAppearance = TextAppearance.this;
            TextAppearance.access$002(textAppearance, Typeface.create(param1Typeface, textAppearance.textStyle));
            TextAppearance.this.updateTextPaintMeasureState(textPaint, param1Typeface);
            TextAppearance.access$102(TextAppearance.this, true);
            callback.onFontRetrieved(param1Typeface);
          }
        };
      super(this, paramTextPaint, paramFontCallback);
      ResourcesCompat.getFont(paramContext, i, fontCallback, null);
    } catch (UnsupportedOperationException unsupportedOperationException) {
    
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
    
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Error loading font ");
      stringBuilder.append(this.fontFamily);
      Log.d("TextAppearance", stringBuilder.toString(), exception);
    } 
  }
  
  public void updateDrawState(Context paramContext, TextPaint paramTextPaint, ResourcesCompat.FontCallback paramFontCallback) {
    int i;
    updateMeasureState(paramContext, paramTextPaint, paramFontCallback);
    ColorStateList colorStateList = this.textColor;
    if (colorStateList != null) {
      i = colorStateList.getColorForState(paramTextPaint.drawableState, this.textColor.getDefaultColor());
    } else {
      i = -16777216;
    } 
    paramTextPaint.setColor(i);
    float f1 = this.shadowRadius;
    float f2 = this.shadowDx;
    float f3 = this.shadowDy;
    colorStateList = this.shadowColor;
    if (colorStateList != null) {
      i = colorStateList.getColorForState(paramTextPaint.drawableState, this.shadowColor.getDefaultColor());
    } else {
      i = 0;
    } 
    paramTextPaint.setShadowLayer(f1, f2, f3, i);
  }
  
  public void updateMeasureState(Context paramContext, TextPaint paramTextPaint, ResourcesCompat.FontCallback paramFontCallback) {
    if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
      updateTextPaintMeasureState(paramTextPaint, getFont(paramContext));
    } else {
      getFontAsync(paramContext, paramTextPaint, paramFontCallback);
      if (!this.fontResolved)
        updateTextPaintMeasureState(paramTextPaint, this.font); 
    } 
  }
  
  public void updateTextPaintMeasureState(TextPaint paramTextPaint, Typeface paramTypeface) {
    boolean bool;
    float f;
    paramTextPaint.setTypeface(paramTypeface);
    int i = this.textStyle & paramTypeface.getStyle();
    if ((i & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramTextPaint.setFakeBoldText(bool);
    if ((i & 0x2) != 0) {
      f = -0.25F;
    } else {
      f = 0.0F;
    } 
    paramTextPaint.setTextSkewX(f);
    paramTextPaint.setTextSize(this.textSize);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/resources/TextAppearance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */