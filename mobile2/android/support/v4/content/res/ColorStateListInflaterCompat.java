package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.compat.R;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ColorStateListInflaterCompat {
  private static final int DEFAULT_COLOR = -65536;
  
  public static ColorStateList createFromXml(Resources paramResources, XmlPullParser paramXmlPullParser, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i;
    AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
    while (true) {
      i = paramXmlPullParser.next();
      if (i != 2 && i != 1)
        continue; 
      break;
    } 
    if (i == 2)
      return createFromXmlInner(paramResources, paramXmlPullParser, attributeSet, paramTheme); 
    throw new XmlPullParserException("No start tag found");
  }
  
  public static ColorStateList createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    String str = paramXmlPullParser.getName();
    if (str.equals("selector"))
      return inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramXmlPullParser.getPositionDescription());
    stringBuilder.append(": invalid color state list tag ");
    stringBuilder.append(str);
    throw new XmlPullParserException(stringBuilder.toString());
  }
  
  private static ColorStateList inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth() + 1;
    int j = -65536;
    int[][] arrayOfInt3 = new int[20][];
    int[] arrayOfInt4 = new int[arrayOfInt3.length];
    byte b = 0;
    while (true) {
      int k = paramXmlPullParser.next();
      if (k != 1) {
        int m = paramXmlPullParser.getDepth();
        if (m >= i || k != 3) {
          if (k != 2 || m > i || !paramXmlPullParser.getName().equals("item"))
            continue; 
          TypedArray typedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorStateListItem);
          int n = typedArray.getColor(R.styleable.ColorStateListItem_android_color, -65281);
          float f = 1.0F;
          if (typedArray.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
            f = typedArray.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0F);
          } else if (typedArray.hasValue(R.styleable.ColorStateListItem_alpha)) {
            f = typedArray.getFloat(R.styleable.ColorStateListItem_alpha, 1.0F);
          } 
          typedArray.recycle();
          int i1 = 0;
          m = paramAttributeSet.getAttributeCount();
          int[] arrayOfInt = new int[m];
          k = 0;
          while (k < m) {
            int i2 = paramAttributeSet.getAttributeNameResource(k);
            int i3 = i1;
            if (i2 != 16843173) {
              i3 = i1;
              if (i2 != 16843551) {
                i3 = i1;
                if (i2 != R.attr.alpha) {
                  if (paramAttributeSet.getAttributeBooleanValue(k, false)) {
                    i3 = i2;
                  } else {
                    i3 = -i2;
                  } 
                  arrayOfInt[i1] = i3;
                  i3 = i1 + 1;
                } 
              } 
            } 
            k++;
            i1 = i3;
          } 
          arrayOfInt = StateSet.trimStateSet(arrayOfInt, i1);
          m = modulateColorAlpha(n, f);
          if (!b || arrayOfInt.length == 0)
            j = m; 
          arrayOfInt4 = GrowingArrayUtils.append(arrayOfInt4, b, m);
          arrayOfInt3 = GrowingArrayUtils.<int[]>append(arrayOfInt3, b, arrayOfInt);
          b++;
          continue;
        } 
      } 
      break;
    } 
    int[] arrayOfInt2 = new int[b];
    int[][] arrayOfInt1 = new int[b][];
    System.arraycopy(arrayOfInt4, 0, arrayOfInt2, 0, b);
    System.arraycopy(arrayOfInt3, 0, arrayOfInt1, 0, b);
    return new ColorStateList(arrayOfInt1, arrayOfInt2);
  }
  
  private static int modulateColorAlpha(int paramInt, float paramFloat) {
    return 0xFFFFFF & paramInt | Math.round(Color.alpha(paramInt) * paramFloat) << 24;
  }
  
  private static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfint) {
    TypedArray typedArray;
    if (paramTheme == null) {
      typedArray = paramResources.obtainAttributes(paramAttributeSet, paramArrayOfint);
    } else {
      typedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfint, 0, 0);
    } 
    return typedArray;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/content/res/ColorStateListInflaterCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */