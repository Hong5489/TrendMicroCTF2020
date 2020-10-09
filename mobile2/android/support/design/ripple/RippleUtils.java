package android.support.design.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.util.StateSet;

public class RippleUtils {
  private static final int[] FOCUSED_STATE_SET;
  
  private static final int[] HOVERED_FOCUSED_STATE_SET;
  
  private static final int[] HOVERED_STATE_SET;
  
  private static final int[] PRESSED_STATE_SET;
  
  private static final int[] SELECTED_FOCUSED_STATE_SET;
  
  private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
  
  private static final int[] SELECTED_HOVERED_STATE_SET;
  
  private static final int[] SELECTED_PRESSED_STATE_SET;
  
  private static final int[] SELECTED_STATE_SET;
  
  public static final boolean USE_FRAMEWORK_RIPPLE;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_FRAMEWORK_RIPPLE = bool;
    PRESSED_STATE_SET = new int[] { 16842919 };
    HOVERED_FOCUSED_STATE_SET = new int[] { 16843623, 16842908 };
    FOCUSED_STATE_SET = new int[] { 16842908 };
    HOVERED_STATE_SET = new int[] { 16843623 };
    SELECTED_PRESSED_STATE_SET = new int[] { 16842913, 16842919 };
    SELECTED_HOVERED_FOCUSED_STATE_SET = new int[] { 16842913, 16843623, 16842908 };
    SELECTED_FOCUSED_STATE_SET = new int[] { 16842913, 16842908 };
    SELECTED_HOVERED_STATE_SET = new int[] { 16842913, 16843623 };
    SELECTED_STATE_SET = new int[] { 16842913 };
  }
  
  public static ColorStateList convertToRippleDrawableColor(ColorStateList paramColorStateList) {
    if (USE_FRAMEWORK_RIPPLE) {
      int[][] arrayOfInt3 = new int[2][];
      int[] arrayOfInt4 = new int[2];
      arrayOfInt3[0] = SELECTED_STATE_SET;
      arrayOfInt4[0] = getColorForState(paramColorStateList, SELECTED_PRESSED_STATE_SET);
      int j = 0 + 1;
      arrayOfInt3[j] = StateSet.NOTHING;
      arrayOfInt4[j] = getColorForState(paramColorStateList, PRESSED_STATE_SET);
      return new ColorStateList(arrayOfInt3, arrayOfInt4);
    } 
    int[][] arrayOfInt = new int[10][];
    int[] arrayOfInt1 = new int[10];
    int[] arrayOfInt2 = SELECTED_PRESSED_STATE_SET;
    arrayOfInt[0] = arrayOfInt2;
    arrayOfInt1[0] = getColorForState(paramColorStateList, arrayOfInt2);
    int i = 0 + 1;
    arrayOfInt2 = SELECTED_HOVERED_FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = SELECTED_FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = SELECTED_HOVERED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    arrayOfInt[++i] = SELECTED_STATE_SET;
    arrayOfInt1[i] = 0;
    i++;
    arrayOfInt2 = PRESSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = HOVERED_FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = FOCUSED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    i++;
    arrayOfInt2 = HOVERED_STATE_SET;
    arrayOfInt[i] = arrayOfInt2;
    arrayOfInt1[i] = getColorForState(paramColorStateList, arrayOfInt2);
    arrayOfInt[++i] = StateSet.NOTHING;
    arrayOfInt1[i] = 0;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private static int doubleAlpha(int paramInt) {
    return ColorUtils.setAlphaComponent(paramInt, Math.min(Color.alpha(paramInt) * 2, 255));
  }
  
  private static int getColorForState(ColorStateList paramColorStateList, int[] paramArrayOfint) {
    int i;
    if (paramColorStateList != null) {
      i = paramColorStateList.getColorForState(paramArrayOfint, paramColorStateList.getDefaultColor());
    } else {
      i = 0;
    } 
    if (USE_FRAMEWORK_RIPPLE)
      i = doubleAlpha(i); 
    return i;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/ripple/RippleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */