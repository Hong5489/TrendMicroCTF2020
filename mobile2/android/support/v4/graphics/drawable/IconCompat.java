package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Preconditions;
import android.text.TextUtils;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
  private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25F;
  
  private static final int AMBIENT_SHADOW_ALPHA = 30;
  
  private static final float BLUR_FACTOR = 0.010416667F;
  
  static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
  
  private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667F;
  
  private static final String EXTRA_INT1 = "int1";
  
  private static final String EXTRA_INT2 = "int2";
  
  private static final String EXTRA_OBJ = "obj";
  
  private static final String EXTRA_TINT_LIST = "tint_list";
  
  private static final String EXTRA_TINT_MODE = "tint_mode";
  
  private static final String EXTRA_TYPE = "type";
  
  private static final float ICON_DIAMETER_FACTOR = 0.9166667F;
  
  private static final int KEY_SHADOW_ALPHA = 61;
  
  private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334F;
  
  private static final String TAG = "IconCompat";
  
  public static final int TYPE_UNKNOWN = -1;
  
  public byte[] mData;
  
  public int mInt1;
  
  public int mInt2;
  
  Object mObj1;
  
  public Parcelable mParcelable;
  
  public ColorStateList mTintList = null;
  
  PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
  
  public String mTintModeStr;
  
  public int mType;
  
  public IconCompat() {}
  
  private IconCompat(int paramInt) {
    this.mType = paramInt;
  }
  
  public static IconCompat createFromBundle(Bundle paramBundle) {
    // Byte code:
    //   0: aload_0
    //   1: ldc 'type'
    //   3: invokevirtual getInt : (Ljava/lang/String;)I
    //   6: istore_1
    //   7: new android/support/v4/graphics/drawable/IconCompat
    //   10: dup
    //   11: iload_1
    //   12: invokespecial <init> : (I)V
    //   15: astore_2
    //   16: aload_2
    //   17: aload_0
    //   18: ldc 'int1'
    //   20: invokevirtual getInt : (Ljava/lang/String;)I
    //   23: putfield mInt1 : I
    //   26: aload_2
    //   27: aload_0
    //   28: ldc 'int2'
    //   30: invokevirtual getInt : (Ljava/lang/String;)I
    //   33: putfield mInt2 : I
    //   36: aload_0
    //   37: ldc 'tint_list'
    //   39: invokevirtual containsKey : (Ljava/lang/String;)Z
    //   42: ifeq -> 58
    //   45: aload_2
    //   46: aload_0
    //   47: ldc 'tint_list'
    //   49: invokevirtual getParcelable : (Ljava/lang/String;)Landroid/os/Parcelable;
    //   52: checkcast android/content/res/ColorStateList
    //   55: putfield mTintList : Landroid/content/res/ColorStateList;
    //   58: aload_0
    //   59: ldc 'tint_mode'
    //   61: invokevirtual containsKey : (Ljava/lang/String;)Z
    //   64: ifeq -> 80
    //   67: aload_2
    //   68: aload_0
    //   69: ldc 'tint_mode'
    //   71: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   74: invokestatic valueOf : (Ljava/lang/String;)Landroid/graphics/PorterDuff$Mode;
    //   77: putfield mTintMode : Landroid/graphics/PorterDuff$Mode;
    //   80: iload_1
    //   81: iconst_m1
    //   82: if_icmpeq -> 169
    //   85: iload_1
    //   86: iconst_1
    //   87: if_icmpeq -> 169
    //   90: iload_1
    //   91: iconst_2
    //   92: if_icmpeq -> 156
    //   95: iload_1
    //   96: iconst_3
    //   97: if_icmpeq -> 143
    //   100: iload_1
    //   101: iconst_4
    //   102: if_icmpeq -> 156
    //   105: iload_1
    //   106: iconst_5
    //   107: if_icmpeq -> 169
    //   110: new java/lang/StringBuilder
    //   113: dup
    //   114: invokespecial <init> : ()V
    //   117: astore_0
    //   118: aload_0
    //   119: ldc 'Unknown type '
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload_0
    //   126: iload_1
    //   127: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   130: pop
    //   131: ldc 'IconCompat'
    //   133: aload_0
    //   134: invokevirtual toString : ()Ljava/lang/String;
    //   137: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   140: pop
    //   141: aconst_null
    //   142: areturn
    //   143: aload_2
    //   144: aload_0
    //   145: ldc 'obj'
    //   147: invokevirtual getByteArray : (Ljava/lang/String;)[B
    //   150: putfield mObj1 : Ljava/lang/Object;
    //   153: goto -> 179
    //   156: aload_2
    //   157: aload_0
    //   158: ldc 'obj'
    //   160: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   163: putfield mObj1 : Ljava/lang/Object;
    //   166: goto -> 179
    //   169: aload_2
    //   170: aload_0
    //   171: ldc 'obj'
    //   173: invokevirtual getParcelable : (Ljava/lang/String;)Landroid/os/Parcelable;
    //   176: putfield mObj1 : Ljava/lang/Object;
    //   179: aload_2
    //   180: areturn
  }
  
  public static IconCompat createFromIcon(Context paramContext, Icon paramIcon) {
    IconCompat iconCompat;
    Preconditions.checkNotNull(paramIcon);
    int i = getType(paramIcon);
    if (i != 2) {
      if (i != 4) {
        iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = paramIcon;
        return iconCompat;
      } 
      return createWithContentUri(getUri(paramIcon));
    } 
    String str = getResPackage(paramIcon);
    try {
      return createWithResource(getResources((Context)iconCompat, str), str, getResId(paramIcon));
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      throw new IllegalArgumentException("Icon resource cannot be found");
    } 
  }
  
  public static IconCompat createFromIcon(Icon paramIcon) {
    Preconditions.checkNotNull(paramIcon);
    int i = getType(paramIcon);
    if (i != 2) {
      if (i != 4) {
        IconCompat iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = paramIcon;
        return iconCompat;
      } 
      return createWithContentUri(getUri(paramIcon));
    } 
    return createWithResource(null, getResPackage(paramIcon), getResId(paramIcon));
  }
  
  static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap paramBitmap, boolean paramBoolean) {
    int i = (int)(Math.min(paramBitmap.getWidth(), paramBitmap.getHeight()) * 0.6666667F);
    Bitmap bitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint(3);
    float f1 = i * 0.5F;
    float f2 = 0.9166667F * f1;
    if (paramBoolean) {
      float f = i * 0.010416667F;
      paint.setColor(0);
      paint.setShadowLayer(f, 0.0F, i * 0.020833334F, 1023410176);
      canvas.drawCircle(f1, f1, f2, paint);
      paint.setShadowLayer(f, 0.0F, 0.0F, 503316480);
      canvas.drawCircle(f1, f1, f2, paint);
      paint.clearShadowLayer();
    } 
    paint.setColor(-16777216);
    BitmapShader bitmapShader = new BitmapShader(paramBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    Matrix matrix = new Matrix();
    matrix.setTranslate((-(paramBitmap.getWidth() - i) / 2), (-(paramBitmap.getHeight() - i) / 2));
    bitmapShader.setLocalMatrix(matrix);
    paint.setShader((Shader)bitmapShader);
    canvas.drawCircle(f1, f1, f2, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  public static IconCompat createWithAdaptiveBitmap(Bitmap paramBitmap) {
    if (paramBitmap != null) {
      IconCompat iconCompat = new IconCompat(5);
      iconCompat.mObj1 = paramBitmap;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static IconCompat createWithBitmap(Bitmap paramBitmap) {
    if (paramBitmap != null) {
      IconCompat iconCompat = new IconCompat(1);
      iconCompat.mObj1 = paramBitmap;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static IconCompat createWithContentUri(Uri paramUri) {
    if (paramUri != null)
      return createWithContentUri(paramUri.toString()); 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithContentUri(String paramString) {
    if (paramString != null) {
      IconCompat iconCompat = new IconCompat(4);
      iconCompat.mObj1 = paramString;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      IconCompat iconCompat = new IconCompat(3);
      iconCompat.mObj1 = paramArrayOfbyte;
      iconCompat.mInt1 = paramInt1;
      iconCompat.mInt2 = paramInt2;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Data must not be null.");
  }
  
  public static IconCompat createWithResource(Context paramContext, int paramInt) {
    if (paramContext != null)
      return createWithResource(paramContext.getResources(), paramContext.getPackageName(), paramInt); 
    throw new IllegalArgumentException("Context must not be null.");
  }
  
  public static IconCompat createWithResource(Resources paramResources, String paramString, int paramInt) {
    if (paramString != null) {
      if (paramInt != 0) {
        IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = paramInt;
        if (paramResources != null) {
          try {
            iconCompat.mObj1 = paramResources.getResourceName(paramInt);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            throw new IllegalArgumentException("Icon resource cannot be found");
          } 
        } else {
          iconCompat.mObj1 = paramString;
        } 
        return iconCompat;
      } 
      throw new IllegalArgumentException("Drawable resource ID must not be 0");
    } 
    throw new IllegalArgumentException("Package must not be null.");
  }
  
  private static int getResId(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getResId(); 
    try {
      return ((Integer)paramIcon.getClass().getMethod("getResId", new Class[0]).invoke(paramIcon, new Object[0])).intValue();
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon resource", illegalAccessException);
      return 0;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon resource", invocationTargetException);
      return 0;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon resource", noSuchMethodException);
      return 0;
    } 
  }
  
  private static String getResPackage(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getResPackage(); 
    try {
      return (String)paramIcon.getClass().getMethod("getResPackage", new Class[0]).invoke(paramIcon, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon package", illegalAccessException);
      return null;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon package", invocationTargetException);
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon package", noSuchMethodException);
      return null;
    } 
  }
  
  private static Resources getResources(Context paramContext, String paramString) {
    if ("android".equals(paramString))
      return Resources.getSystem(); 
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo(paramString, 8192);
      return (applicationInfo != null) ? packageManager.getResourcesForApplication(applicationInfo) : null;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", new Object[] { paramString }), (Throwable)nameNotFoundException);
      return null;
    } 
  }
  
  private static int getType(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getType(); 
    try {
      return ((Integer)paramIcon.getClass().getMethod("getType", new Class[0]).invoke(paramIcon, new Object[0])).intValue();
    } catch (IllegalAccessException illegalAccessException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to get icon type ");
      stringBuilder.append(paramIcon);
      Log.e("IconCompat", stringBuilder.toString(), illegalAccessException);
      return -1;
    } catch (InvocationTargetException invocationTargetException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to get icon type ");
      stringBuilder.append(paramIcon);
      Log.e("IconCompat", stringBuilder.toString(), invocationTargetException);
      return -1;
    } catch (NoSuchMethodException noSuchMethodException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to get icon type ");
      stringBuilder.append(paramIcon);
      Log.e("IconCompat", stringBuilder.toString(), noSuchMethodException);
      return -1;
    } 
  }
  
  private static Uri getUri(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getUri(); 
    try {
      return (Uri)paramIcon.getClass().getMethod("getUri", new Class[0]).invoke(paramIcon, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon uri", illegalAccessException);
      return null;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon uri", invocationTargetException);
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon uri", noSuchMethodException);
      return null;
    } 
  }
  
  private Drawable loadDrawableInner(Context paramContext) {
    int i = this.mType;
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i == 5)
              return (Drawable)new BitmapDrawable(paramContext.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false)); 
          } else {
            Uri uri = Uri.parse((String)this.mObj1);
            String str = uri.getScheme();
            InputStream inputStream = null;
            StringBuilder stringBuilder = null;
            if ("content".equals(str) || "file".equals(str)) {
              try {
                inputStream = paramContext.getContentResolver().openInputStream(uri);
              } catch (Exception exception) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("Unable to load image from URI: ");
                stringBuilder1.append(uri);
                Log.w("IconCompat", stringBuilder1.toString(), exception);
                stringBuilder1 = stringBuilder;
              } 
            } else {
              try {
                FileInputStream fileInputStream = new FileInputStream();
                File file = new File();
                this((String)this.mObj1);
                this(file);
                inputStream = fileInputStream;
              } catch (FileNotFoundException fileNotFoundException) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to load image from path: ");
                stringBuilder.append(uri);
                Log.w("IconCompat", stringBuilder.toString(), fileNotFoundException);
              } 
            } 
            if (inputStream != null)
              return (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeStream(inputStream)); 
          } 
        } else {
          return (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeByteArray((byte[])this.mObj1, this.mInt1, this.mInt2));
        } 
      } else {
        String str2 = getResPackage();
        String str1 = str2;
        if (TextUtils.isEmpty(str2))
          str1 = paramContext.getPackageName(); 
        Resources resources = getResources(paramContext, str1);
        try {
          return ResourcesCompat.getDrawable(resources, this.mInt1, paramContext.getTheme());
        } catch (RuntimeException runtimeException) {
          Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", new Object[] { Integer.valueOf(this.mInt1), this.mObj1 }), runtimeException);
        } 
      } 
      return null;
    } 
    return (Drawable)new BitmapDrawable(runtimeException.getResources(), (Bitmap)this.mObj1);
  }
  
  private static String typeToString(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? ((paramInt != 5) ? "UNKNOWN" : "BITMAP_MASKABLE") : "URI") : "DATA") : "RESOURCE") : "BITMAP";
  }
  
  public void addToShortcutIntent(Intent paramIntent, Drawable paramDrawable, Context paramContext) {
    StringBuilder stringBuilder;
    Bitmap bitmap;
    checkResource(paramContext);
    int i = this.mType;
    if (i != 1) {
      if (i != 2) {
        if (i == 5) {
          bitmap = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
        } else {
          throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
        } 
      } else {
        try {
          Bitmap bitmap1;
          Context context = bitmap.createPackageContext(getResPackage(), 0);
          if (paramDrawable == null) {
            paramIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent.ShortcutIconResource.fromContext(context, this.mInt1));
            return;
          } 
          Drawable drawable = ContextCompat.getDrawable(context, this.mInt1);
          if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            i = ((ActivityManager)context.getSystemService("activity")).getLauncherLargeIconSize();
            bitmap1 = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
          } else {
            bitmap1 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
          } 
          drawable.setBounds(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
          Canvas canvas = new Canvas();
          this(bitmap1);
          drawable.draw(canvas);
        } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Can't find package ");
          stringBuilder.append(this.mObj1);
          throw new IllegalArgumentException(stringBuilder.toString(), nameNotFoundException);
        } 
      } 
    } else {
      Bitmap bitmap1 = (Bitmap)this.mObj1;
      bitmap = bitmap1;
      if (stringBuilder != null)
        bitmap = bitmap1.copy(bitmap1.getConfig(), true); 
    } 
    if (stringBuilder != null) {
      int j = bitmap.getWidth();
      i = bitmap.getHeight();
      stringBuilder.setBounds(j / 2, i / 2, j, i);
      stringBuilder.draw(new Canvas(bitmap));
    } 
    nameNotFoundException.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap);
  }
  
  public void checkResource(Context paramContext) {
    if (this.mType == 2) {
      String str1 = (String)this.mObj1;
      if (!str1.contains(":"))
        return; 
      String str2 = str1.split(":", -1)[1];
      String str3 = str2.split("/", -1)[0];
      str2 = str2.split("/", -1)[1];
      str1 = str1.split(":", -1)[0];
      int i = getResources(paramContext, str1).getIdentifier(str2, str3, str1);
      if (this.mInt1 != i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id has changed for ");
        stringBuilder.append(str1);
        stringBuilder.append("/");
        stringBuilder.append(str2);
        Log.i("IconCompat", stringBuilder.toString());
        this.mInt1 = i;
      } 
    } 
  }
  
  public int getResId() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getResId((Icon)this.mObj1); 
    if (this.mType == 2)
      return this.mInt1; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("called getResId() on ");
    stringBuilder.append(this);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public String getResPackage() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getResPackage((Icon)this.mObj1); 
    if (this.mType == 2)
      return ((String)this.mObj1).split(":", -1)[0]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("called getResPackage() on ");
    stringBuilder.append(this);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public int getType() {
    return (this.mType == -1 && Build.VERSION.SDK_INT >= 23) ? getType((Icon)this.mObj1) : this.mType;
  }
  
  public Uri getUri() {
    return (this.mType == -1 && Build.VERSION.SDK_INT >= 23) ? getUri((Icon)this.mObj1) : Uri.parse((String)this.mObj1);
  }
  
  public Drawable loadDrawable(Context paramContext) {
    checkResource(paramContext);
    if (Build.VERSION.SDK_INT >= 23)
      return toIcon().loadDrawable(paramContext); 
    Drawable drawable = loadDrawableInner(paramContext);
    if (drawable != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
      drawable.mutate();
      DrawableCompat.setTintList(drawable, this.mTintList);
      DrawableCompat.setTintMode(drawable, this.mTintMode);
    } 
    return drawable;
  }
  
  public void onPostParceling() {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield mTintModeStr : Ljava/lang/String;
    //   5: invokestatic valueOf : (Ljava/lang/String;)Landroid/graphics/PorterDuff$Mode;
    //   8: putfield mTintMode : Landroid/graphics/PorterDuff$Mode;
    //   11: aload_0
    //   12: getfield mType : I
    //   15: istore_1
    //   16: iload_1
    //   17: iconst_m1
    //   18: if_icmpeq -> 130
    //   21: iload_1
    //   22: iconst_1
    //   23: if_icmpeq -> 84
    //   26: iload_1
    //   27: iconst_2
    //   28: if_icmpeq -> 60
    //   31: iload_1
    //   32: iconst_3
    //   33: if_icmpeq -> 49
    //   36: iload_1
    //   37: iconst_4
    //   38: if_icmpeq -> 60
    //   41: iload_1
    //   42: iconst_5
    //   43: if_icmpeq -> 84
    //   46: goto -> 144
    //   49: aload_0
    //   50: aload_0
    //   51: getfield mData : [B
    //   54: putfield mObj1 : Ljava/lang/Object;
    //   57: goto -> 144
    //   60: aload_0
    //   61: new java/lang/String
    //   64: dup
    //   65: aload_0
    //   66: getfield mData : [B
    //   69: ldc_w 'UTF-16'
    //   72: invokestatic forName : (Ljava/lang/String;)Ljava/nio/charset/Charset;
    //   75: invokespecial <init> : ([BLjava/nio/charset/Charset;)V
    //   78: putfield mObj1 : Ljava/lang/Object;
    //   81: goto -> 144
    //   84: aload_0
    //   85: getfield mParcelable : Landroid/os/Parcelable;
    //   88: astore_2
    //   89: aload_2
    //   90: ifnull -> 101
    //   93: aload_0
    //   94: aload_2
    //   95: putfield mObj1 : Ljava/lang/Object;
    //   98: goto -> 144
    //   101: aload_0
    //   102: getfield mData : [B
    //   105: astore_2
    //   106: aload_0
    //   107: aload_2
    //   108: putfield mObj1 : Ljava/lang/Object;
    //   111: aload_0
    //   112: iconst_3
    //   113: putfield mType : I
    //   116: aload_0
    //   117: iconst_0
    //   118: putfield mInt1 : I
    //   121: aload_0
    //   122: aload_2
    //   123: arraylength
    //   124: putfield mInt2 : I
    //   127: goto -> 144
    //   130: aload_0
    //   131: getfield mParcelable : Landroid/os/Parcelable;
    //   134: astore_2
    //   135: aload_2
    //   136: ifnull -> 145
    //   139: aload_0
    //   140: aload_2
    //   141: putfield mObj1 : Ljava/lang/Object;
    //   144: return
    //   145: new java/lang/IllegalArgumentException
    //   148: dup
    //   149: ldc_w 'Invalid icon'
    //   152: invokespecial <init> : (Ljava/lang/String;)V
    //   155: athrow
  }
  
  public void onPreParceling(boolean paramBoolean) {
    this.mTintModeStr = this.mTintMode.name();
    int i = this.mType;
    if (i != -1) {
      if (i != 1)
        if (i != 2) {
          if (i != 3) {
            if (i != 4) {
              if (i != 5)
                return; 
            } else {
              this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
              return;
            } 
          } else {
            this.mData = (byte[])this.mObj1;
            return;
          } 
        } else {
          this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
          return;
        }  
      if (paramBoolean) {
        Bitmap bitmap = (Bitmap)this.mObj1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        this.mData = byteArrayOutputStream.toByteArray();
      } else {
        this.mParcelable = (Parcelable)this.mObj1;
      } 
    } else {
      if (!paramBoolean) {
        this.mParcelable = (Parcelable)this.mObj1;
        return;
      } 
      throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
    } 
  }
  
  public IconCompat setTint(int paramInt) {
    return setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public IconCompat setTintList(ColorStateList paramColorStateList) {
    this.mTintList = paramColorStateList;
    return this;
  }
  
  public IconCompat setTintMode(PorterDuff.Mode paramMode) {
    this.mTintMode = paramMode;
    return this;
  }
  
  public Bundle toBundle() {
    // Byte code:
    //   0: new android/os/Bundle
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: aload_0
    //   9: getfield mType : I
    //   12: istore_2
    //   13: iload_2
    //   14: iconst_m1
    //   15: if_icmpeq -> 105
    //   18: iload_2
    //   19: iconst_1
    //   20: if_icmpeq -> 89
    //   23: iload_2
    //   24: iconst_2
    //   25: if_icmpeq -> 73
    //   28: iload_2
    //   29: iconst_3
    //   30: if_icmpeq -> 57
    //   33: iload_2
    //   34: iconst_4
    //   35: if_icmpeq -> 73
    //   38: iload_2
    //   39: iconst_5
    //   40: if_icmpne -> 46
    //   43: goto -> 89
    //   46: new java/lang/IllegalArgumentException
    //   49: dup
    //   50: ldc_w 'Invalid icon'
    //   53: invokespecial <init> : (Ljava/lang/String;)V
    //   56: athrow
    //   57: aload_1
    //   58: ldc 'obj'
    //   60: aload_0
    //   61: getfield mObj1 : Ljava/lang/Object;
    //   64: checkcast [B
    //   67: invokevirtual putByteArray : (Ljava/lang/String;[B)V
    //   70: goto -> 118
    //   73: aload_1
    //   74: ldc 'obj'
    //   76: aload_0
    //   77: getfield mObj1 : Ljava/lang/Object;
    //   80: checkcast java/lang/String
    //   83: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
    //   86: goto -> 118
    //   89: aload_1
    //   90: ldc 'obj'
    //   92: aload_0
    //   93: getfield mObj1 : Ljava/lang/Object;
    //   96: checkcast android/graphics/Bitmap
    //   99: invokevirtual putParcelable : (Ljava/lang/String;Landroid/os/Parcelable;)V
    //   102: goto -> 118
    //   105: aload_1
    //   106: ldc 'obj'
    //   108: aload_0
    //   109: getfield mObj1 : Ljava/lang/Object;
    //   112: checkcast android/os/Parcelable
    //   115: invokevirtual putParcelable : (Ljava/lang/String;Landroid/os/Parcelable;)V
    //   118: aload_1
    //   119: ldc 'type'
    //   121: aload_0
    //   122: getfield mType : I
    //   125: invokevirtual putInt : (Ljava/lang/String;I)V
    //   128: aload_1
    //   129: ldc 'int1'
    //   131: aload_0
    //   132: getfield mInt1 : I
    //   135: invokevirtual putInt : (Ljava/lang/String;I)V
    //   138: aload_1
    //   139: ldc 'int2'
    //   141: aload_0
    //   142: getfield mInt2 : I
    //   145: invokevirtual putInt : (Ljava/lang/String;I)V
    //   148: aload_0
    //   149: getfield mTintList : Landroid/content/res/ColorStateList;
    //   152: astore_3
    //   153: aload_3
    //   154: ifnull -> 164
    //   157: aload_1
    //   158: ldc 'tint_list'
    //   160: aload_3
    //   161: invokevirtual putParcelable : (Ljava/lang/String;Landroid/os/Parcelable;)V
    //   164: aload_0
    //   165: getfield mTintMode : Landroid/graphics/PorterDuff$Mode;
    //   168: astore_3
    //   169: aload_3
    //   170: getstatic android/support/v4/graphics/drawable/IconCompat.DEFAULT_TINT_MODE : Landroid/graphics/PorterDuff$Mode;
    //   173: if_acmpeq -> 186
    //   176: aload_1
    //   177: ldc 'tint_mode'
    //   179: aload_3
    //   180: invokevirtual name : ()Ljava/lang/String;
    //   183: invokevirtual putString : (Ljava/lang/String;Ljava/lang/String;)V
    //   186: aload_1
    //   187: areturn
  }
  
  public Icon toIcon() {
    int i = this.mType;
    if (i != -1) {
      Icon icon;
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 4) {
              if (i == 5) {
                if (Build.VERSION.SDK_INT >= 26) {
                  icon = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                } else {
                  icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                } 
              } else {
                throw new IllegalArgumentException("Unknown type");
              } 
            } else {
              icon = Icon.createWithContentUri((String)this.mObj1);
            } 
          } else {
            icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
          } 
        } else {
          icon = Icon.createWithResource(getResPackage(), this.mInt1);
        } 
      } else {
        icon = Icon.createWithBitmap((Bitmap)this.mObj1);
      } 
      ColorStateList colorStateList = this.mTintList;
      if (colorStateList != null)
        icon.setTintList(colorStateList); 
      PorterDuff.Mode mode = this.mTintMode;
      if (mode != DEFAULT_TINT_MODE)
        icon.setTintMode(mode); 
      return icon;
    } 
    return (Icon)this.mObj1;
  }
  
  public String toString() {
    if (this.mType == -1)
      return String.valueOf(this.mObj1); 
    StringBuilder stringBuilder = (new StringBuilder("Icon(typ=")).append(typeToString(this.mType));
    int i = this.mType;
    if (i != 1)
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i != 5)
              if (this.mTintList != null) {
                stringBuilder.append(" tint=");
                stringBuilder.append(this.mTintList);
              }  
          } else {
            stringBuilder.append(" uri=");
            stringBuilder.append(this.mObj1);
            if (this.mTintList != null) {
              stringBuilder.append(" tint=");
              stringBuilder.append(this.mTintList);
            } 
          } 
        } else {
          stringBuilder.append(" len=");
          stringBuilder.append(this.mInt1);
          if (this.mInt2 != 0) {
            stringBuilder.append(" off=");
            stringBuilder.append(this.mInt2);
          } 
          if (this.mTintList != null) {
            stringBuilder.append(" tint=");
            stringBuilder.append(this.mTintList);
          } 
        } 
      } else {
        stringBuilder.append(" pkg=");
        stringBuilder.append(getResPackage());
        stringBuilder.append(" id=");
        stringBuilder.append(String.format("0x%08x", new Object[] { Integer.valueOf(getResId()) }));
        if (this.mTintList != null) {
          stringBuilder.append(" tint=");
          stringBuilder.append(this.mTintList);
        } 
      }  
    stringBuilder.append(" size=");
    stringBuilder.append(((Bitmap)this.mObj1).getWidth());
    stringBuilder.append("x");
    stringBuilder.append(((Bitmap)this.mObj1).getHeight());
    if (this.mTintList != null) {
      stringBuilder.append(" tint=");
      stringBuilder.append(this.mTintList);
    } 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IconType {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/graphics/drawable/IconCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */