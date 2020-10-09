package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.R;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;

public final class ThemeEnforcement {
  private static final int[] APPCOMPAT_CHECK_ATTRS = new int[] { R.attr.colorPrimary };
  
  private static final String APPCOMPAT_THEME_NAME = "Theme.AppCompat";
  
  private static final int[] MATERIAL_CHECK_ATTRS = new int[] { R.attr.colorSecondary };
  
  private static final String MATERIAL_THEME_NAME = "Theme.MaterialComponents";
  
  public static void checkAppCompatTheme(Context paramContext) {
    checkTheme(paramContext, APPCOMPAT_CHECK_ATTRS, "Theme.AppCompat");
  }
  
  private static void checkCompatibleTheme(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ThemeEnforcement, paramInt1, paramInt2);
    boolean bool = typedArray.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
    typedArray.recycle();
    if (bool)
      checkMaterialTheme(paramContext); 
    checkAppCompatTheme(paramContext);
  }
  
  public static void checkMaterialTheme(Context paramContext) {
    checkTheme(paramContext, MATERIAL_CHECK_ATTRS, "Theme.MaterialComponents");
  }
  
  private static void checkTextAppearance(Context paramContext, AttributeSet paramAttributeSet, int[] paramArrayOfint1, int paramInt1, int paramInt2, int... paramVarArgs1) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ThemeEnforcement, paramInt1, paramInt2);
    int i = R.styleable.ThemeEnforcement_enforceTextAppearance;
    boolean bool = false;
    if (!typedArray.getBoolean(i, false)) {
      typedArray.recycle();
      return;
    } 
    if (paramVarArgs1 == null || paramVarArgs1.length == 0) {
      if (typedArray.getResourceId(R.styleable.ThemeEnforcement_android_textAppearance, -1) != -1)
        bool = true; 
    } else {
      bool = isCustomTextAppearanceValid(paramContext, paramAttributeSet, paramArrayOfint1, paramInt1, paramInt2, paramVarArgs1);
    } 
    typedArray.recycle();
    if (bool)
      return; 
    throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
  }
  
  private static void checkTheme(Context paramContext, int[] paramArrayOfint, String paramString) {
    if (isTheme(paramContext, paramArrayOfint))
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("The style on this component requires your app theme to be ");
    stringBuilder.append(paramString);
    stringBuilder.append(" (or a descendant).");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static boolean isAppCompatTheme(Context paramContext) {
    return isTheme(paramContext, APPCOMPAT_CHECK_ATTRS);
  }
  
  private static boolean isCustomTextAppearanceValid(Context paramContext, AttributeSet paramAttributeSet, int[] paramArrayOfint1, int paramInt1, int paramInt2, int... paramVarArgs1) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, paramArrayOfint1, paramInt1, paramInt2);
    paramInt2 = paramVarArgs1.length;
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      if (typedArray.getResourceId(paramVarArgs1[paramInt1], -1) == -1) {
        typedArray.recycle();
        return false;
      } 
    } 
    typedArray.recycle();
    return true;
  }
  
  public static boolean isMaterialTheme(Context paramContext) {
    return isTheme(paramContext, MATERIAL_CHECK_ATTRS);
  }
  
  private static boolean isTheme(Context paramContext, int[] paramArrayOfint) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramArrayOfint);
    boolean bool = typedArray.hasValue(0);
    typedArray.recycle();
    return bool;
  }
  
  public static TypedArray obtainStyledAttributes(Context paramContext, AttributeSet paramAttributeSet, int[] paramArrayOfint1, int paramInt1, int paramInt2, int... paramVarArgs1) {
    checkCompatibleTheme(paramContext, paramAttributeSet, paramInt1, paramInt2);
    checkTextAppearance(paramContext, paramAttributeSet, paramArrayOfint1, paramInt1, paramInt2, paramVarArgs1);
    return paramContext.obtainStyledAttributes(paramAttributeSet, paramArrayOfint1, paramInt1, paramInt2);
  }
  
  public static TintTypedArray obtainTintedStyledAttributes(Context paramContext, AttributeSet paramAttributeSet, int[] paramArrayOfint1, int paramInt1, int paramInt2, int... paramVarArgs1) {
    checkCompatibleTheme(paramContext, paramAttributeSet, paramInt1, paramInt2);
    checkTextAppearance(paramContext, paramAttributeSet, paramArrayOfint1, paramInt1, paramInt2, paramVarArgs1);
    return TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, paramArrayOfint1, paramInt1, paramInt2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/internal/ThemeEnforcement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */