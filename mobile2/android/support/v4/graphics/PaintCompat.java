package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.util.Pair;

public final class PaintCompat {
  private static final String EM_STRING = "m";
  
  private static final String TOFU_STRING = "󟿽";
  
  private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal<>();
  
  public static boolean hasGlyph(Paint paramPaint, String paramString) {
    if (Build.VERSION.SDK_INT >= 23)
      return paramPaint.hasGlyph(paramString); 
    int i = paramString.length();
    if (i == 1 && Character.isWhitespace(paramString.charAt(0)))
      return true; 
    float f1 = paramPaint.measureText("󟿽");
    float f2 = paramPaint.measureText("m");
    float f3 = paramPaint.measureText(paramString);
    if (f3 == 0.0F)
      return false; 
    if (paramString.codePointCount(0, paramString.length()) > 1) {
      if (f3 > 2.0F * f2)
        return false; 
      f2 = 0.0F;
      int j;
      for (j = 0; j < i; j += k) {
        int k = Character.charCount(paramString.codePointAt(j));
        f2 += paramPaint.measureText(paramString, j, j + k);
      } 
      if (f3 >= f2)
        return false; 
    } 
    if (f3 != f1)
      return true; 
    Pair<Rect, Rect> pair = obtainEmptyRects();
    paramPaint.getTextBounds("󟿽", 0, "󟿽".length(), (Rect)pair.first);
    paramPaint.getTextBounds(paramString, 0, i, (Rect)pair.second);
    return true ^ ((Rect)pair.first).equals(pair.second);
  }
  
  private static Pair<Rect, Rect> obtainEmptyRects() {
    Pair<Rect, Rect> pair = sRectThreadLocal.get();
    if (pair == null) {
      pair = new Pair(new Rect(), new Rect());
      sRectThreadLocal.set(pair);
    } else {
      ((Rect)pair.first).setEmpty();
      ((Rect)pair.second).setEmpty();
    } 
    return pair;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/graphics/PaintCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */