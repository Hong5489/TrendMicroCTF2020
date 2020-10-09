package android.support.design.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;

public class MaterialResources {
  public static ColorStateList getColorStateList(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      int i = paramTypedArray.getResourceId(paramInt, 0);
      if (i != 0) {
        ColorStateList colorStateList = AppCompatResources.getColorStateList(paramContext, i);
        if (colorStateList != null)
          return colorStateList; 
      } 
    } 
    return paramTypedArray.getColorStateList(paramInt);
  }
  
  public static Drawable getDrawable(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      int i = paramTypedArray.getResourceId(paramInt, 0);
      if (i != 0) {
        Drawable drawable = AppCompatResources.getDrawable(paramContext, i);
        if (drawable != null)
          return drawable; 
      } 
    } 
    return paramTypedArray.getDrawable(paramInt);
  }
  
  static int getIndexWithValue(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    return paramTypedArray.hasValue(paramInt1) ? paramInt1 : paramInt2;
  }
  
  public static TextAppearance getTextAppearance(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      paramInt = paramTypedArray.getResourceId(paramInt, 0);
      if (paramInt != 0)
        return new TextAppearance(paramContext, paramInt); 
    } 
    return null;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/resources/MaterialResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */