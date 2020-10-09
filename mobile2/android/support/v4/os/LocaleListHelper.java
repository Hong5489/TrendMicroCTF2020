package android.support.v4.os;

import android.os.Build;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

final class LocaleListHelper {
  private static final Locale EN_LATN;
  
  private static final Locale LOCALE_AR_XB;
  
  private static final Locale LOCALE_EN_XA;
  
  private static final int NUM_PSEUDO_LOCALES = 2;
  
  private static final String STRING_AR_XB = "ar-XB";
  
  private static final String STRING_EN_XA = "en-XA";
  
  private static LocaleListHelper sDefaultAdjustedLocaleList;
  
  private static LocaleListHelper sDefaultLocaleList;
  
  private static final Locale[] sEmptyList = new Locale[0];
  
  private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
  
  private static Locale sLastDefaultLocale;
  
  private static LocaleListHelper sLastExplicitlySetLocaleList;
  
  private static final Object sLock;
  
  private final Locale[] mList;
  
  private final String mStringRepresentation;
  
  static {
    LOCALE_EN_XA = new Locale("en", "XA");
    LOCALE_AR_XB = new Locale("ar", "XB");
    EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
    sLock = new Object();
    sLastExplicitlySetLocaleList = null;
    sDefaultLocaleList = null;
    sDefaultAdjustedLocaleList = null;
    sLastDefaultLocale = null;
  }
  
  LocaleListHelper(Locale paramLocale, LocaleListHelper paramLocaleListHelper) {
    if (paramLocale != null) {
      int i;
      int m;
      if (paramLocaleListHelper == null) {
        i = 0;
      } else {
        i = paramLocaleListHelper.mList.length;
      } 
      int j = -1;
      int k = 0;
      while (true) {
        m = j;
        if (k < i) {
          if (paramLocale.equals(paramLocaleListHelper.mList[k])) {
            m = k;
            break;
          } 
          k++;
          continue;
        } 
        break;
      } 
      if (m == -1) {
        k = 1;
      } else {
        k = 0;
      } 
      j = k + i;
      Locale[] arrayOfLocale = new Locale[j];
      arrayOfLocale[0] = (Locale)paramLocale.clone();
      if (m == -1) {
        for (k = 0; k < i; k++)
          arrayOfLocale[k + 1] = (Locale)paramLocaleListHelper.mList[k].clone(); 
      } else {
        for (k = 0; k < m; k++)
          arrayOfLocale[k + 1] = (Locale)paramLocaleListHelper.mList[k].clone(); 
        for (k = m + 1; k < i; k++)
          arrayOfLocale[k] = (Locale)paramLocaleListHelper.mList[k].clone(); 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      for (k = 0; k < j; k++) {
        stringBuilder.append(LocaleHelper.toLanguageTag(arrayOfLocale[k]));
        if (k < j - 1)
          stringBuilder.append(','); 
      } 
      this.mList = arrayOfLocale;
      this.mStringRepresentation = stringBuilder.toString();
      return;
    } 
    throw new NullPointerException("topLocale is null");
  }
  
  LocaleListHelper(Locale... paramVarArgs) {
    if (paramVarArgs.length == 0) {
      this.mList = sEmptyList;
      this.mStringRepresentation = "";
    } else {
      Locale[] arrayOfLocale = new Locale[paramVarArgs.length];
      HashSet<Locale> hashSet = new HashSet();
      StringBuilder stringBuilder = new StringBuilder();
      byte b = 0;
      while (b < paramVarArgs.length) {
        Locale locale = paramVarArgs[b];
        if (locale != null) {
          if (!hashSet.contains(locale)) {
            locale = (Locale)locale.clone();
            arrayOfLocale[b] = locale;
            stringBuilder.append(LocaleHelper.toLanguageTag(locale));
            if (b < paramVarArgs.length - 1)
              stringBuilder.append(','); 
            hashSet.add(locale);
            b++;
            continue;
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("list[");
          stringBuilder2.append(b);
          stringBuilder2.append("] is a repetition");
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("list[");
        stringBuilder1.append(b);
        stringBuilder1.append("] is null");
        throw new NullPointerException(stringBuilder1.toString());
      } 
      this.mList = arrayOfLocale;
      this.mStringRepresentation = stringBuilder.toString();
    } 
  }
  
  private Locale computeFirstMatch(Collection<String> paramCollection, boolean paramBoolean) {
    Locale locale;
    int i = computeFirstMatchIndex(paramCollection, paramBoolean);
    if (i == -1) {
      paramCollection = null;
    } else {
      locale = this.mList[i];
    } 
    return locale;
  }
  
  private int computeFirstMatchIndex(Collection<String> paramCollection, boolean paramBoolean) {
    Locale[] arrayOfLocale = this.mList;
    if (arrayOfLocale.length == 1)
      return 0; 
    if (arrayOfLocale.length == 0)
      return -1; 
    int i = Integer.MAX_VALUE;
    int j = i;
    if (paramBoolean) {
      int m = findFirstMatchIndex(EN_LATN);
      if (m == 0)
        return 0; 
      j = i;
      if (m < Integer.MAX_VALUE)
        j = m; 
    } 
    Iterator<String> iterator = paramCollection.iterator();
    int k;
    for (k = j; iterator.hasNext(); k = j) {
      i = findFirstMatchIndex(LocaleHelper.forLanguageTag(iterator.next()));
      if (i == 0)
        return 0; 
      j = k;
      if (i < k)
        j = i; 
    } 
    return (k == Integer.MAX_VALUE) ? 0 : k;
  }
  
  private int findFirstMatchIndex(Locale paramLocale) {
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        if (matchScore(paramLocale, arrayOfLocale[b]) > 0)
          return b; 
        b++;
        continue;
      } 
      return Integer.MAX_VALUE;
    } 
  }
  
  static LocaleListHelper forLanguageTags(String paramString) {
    if (paramString == null || paramString.isEmpty())
      return getEmptyLocaleList(); 
    String[] arrayOfString = paramString.split(",", -1);
    Locale[] arrayOfLocale = new Locale[arrayOfString.length];
    for (byte b = 0; b < arrayOfLocale.length; b++)
      arrayOfLocale[b] = LocaleHelper.forLanguageTag(arrayOfString[b]); 
    return new LocaleListHelper(arrayOfLocale);
  }
  
  static LocaleListHelper getAdjustedDefault() {
    getDefault();
    synchronized (sLock) {
      return sDefaultAdjustedLocaleList;
    } 
  }
  
  static LocaleListHelper getDefault() {
    Locale locale = Locale.getDefault();
    synchronized (sLock) {
      if (!locale.equals(sLastDefaultLocale)) {
        sLastDefaultLocale = locale;
        if (sDefaultLocaleList != null && locale.equals(sDefaultLocaleList.get(0)))
          return sDefaultLocaleList; 
        LocaleListHelper localeListHelper = new LocaleListHelper();
        this(locale, sLastExplicitlySetLocaleList);
        sDefaultLocaleList = localeListHelper;
        sDefaultAdjustedLocaleList = localeListHelper;
      } 
      return sDefaultLocaleList;
    } 
  }
  
  static LocaleListHelper getEmptyLocaleList() {
    return sEmptyLocaleList;
  }
  
  private static String getLikelyScript(Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 21) {
      String str = paramLocale.getScript();
      return !str.isEmpty() ? str : "";
    } 
    return "";
  }
  
  private static boolean isPseudoLocale(String paramString) {
    return ("en-XA".equals(paramString) || "ar-XB".equals(paramString));
  }
  
  private static boolean isPseudoLocale(Locale paramLocale) {
    return (LOCALE_EN_XA.equals(paramLocale) || LOCALE_AR_XB.equals(paramLocale));
  }
  
  static boolean isPseudoLocalesOnly(String[] paramArrayOfString) {
    if (paramArrayOfString == null)
      return true; 
    if (paramArrayOfString.length > 3)
      return false; 
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      String str = paramArrayOfString[b];
      if (!str.isEmpty() && !isPseudoLocale(str))
        return false; 
    } 
    return true;
  }
  
  private static int matchScore(Locale paramLocale1, Locale paramLocale2) {
    boolean bool = paramLocale1.equals(paramLocale2);
    boolean bool1 = true;
    if (bool)
      return 1; 
    if (!paramLocale1.getLanguage().equals(paramLocale2.getLanguage()))
      return 0; 
    if (isPseudoLocale(paramLocale1) || isPseudoLocale(paramLocale2))
      return 0; 
    String str = getLikelyScript(paramLocale1);
    if (str.isEmpty()) {
      String str1 = paramLocale1.getCountry();
      boolean bool2 = bool1;
      if (!str1.isEmpty())
        if (str1.equals(paramLocale2.getCountry())) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }  
      return bool2;
    } 
    return str.equals(getLikelyScript(paramLocale2));
  }
  
  static void setDefault(LocaleListHelper paramLocaleListHelper) {
    setDefault(paramLocaleListHelper, 0);
  }
  
  static void setDefault(LocaleListHelper paramLocaleListHelper, int paramInt) {
    if (paramLocaleListHelper != null) {
      if (!paramLocaleListHelper.isEmpty())
        synchronized (sLock) {
          Locale locale = paramLocaleListHelper.get(paramInt);
          sLastDefaultLocale = locale;
          Locale.setDefault(locale);
          sLastExplicitlySetLocaleList = paramLocaleListHelper;
          sDefaultLocaleList = paramLocaleListHelper;
          if (paramInt == 0) {
            sDefaultAdjustedLocaleList = paramLocaleListHelper;
          } else {
            paramLocaleListHelper = new LocaleListHelper();
            this(sLastDefaultLocale, sDefaultLocaleList);
            sDefaultAdjustedLocaleList = paramLocaleListHelper;
          } 
          return;
        }  
      throw new IllegalArgumentException("locales is empty");
    } 
    throw new NullPointerException("locales is null");
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof LocaleListHelper))
      return false; 
    Locale[] arrayOfLocale = ((LocaleListHelper)paramObject).mList;
    if (this.mList.length != arrayOfLocale.length)
      return false; 
    byte b = 0;
    while (true) {
      paramObject = this.mList;
      if (b < paramObject.length) {
        if (!paramObject[b].equals(arrayOfLocale[b]))
          return false; 
        b++;
        continue;
      } 
      return true;
    } 
  }
  
  Locale get(int paramInt) {
    if (paramInt >= 0) {
      Locale[] arrayOfLocale = this.mList;
      if (paramInt < arrayOfLocale.length)
        return arrayOfLocale[paramInt]; 
    } 
    return null;
  }
  
  Locale getFirstMatch(String[] paramArrayOfString) {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), false);
  }
  
  int getFirstMatchIndex(String[] paramArrayOfString) {
    return computeFirstMatchIndex(Arrays.asList(paramArrayOfString), false);
  }
  
  int getFirstMatchIndexWithEnglishSupported(Collection<String> paramCollection) {
    return computeFirstMatchIndex(paramCollection, true);
  }
  
  int getFirstMatchIndexWithEnglishSupported(String[] paramArrayOfString) {
    return getFirstMatchIndexWithEnglishSupported(Arrays.asList(paramArrayOfString));
  }
  
  Locale getFirstMatchWithEnglishSupported(String[] paramArrayOfString) {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), true);
  }
  
  public int hashCode() {
    int i = 1;
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        i = i * 31 + arrayOfLocale[b].hashCode();
        b++;
        continue;
      } 
      return i;
    } 
  }
  
  int indexOf(Locale paramLocale) {
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        if (arrayOfLocale[b].equals(paramLocale))
          return b; 
        b++;
        continue;
      } 
      return -1;
    } 
  }
  
  boolean isEmpty() {
    boolean bool;
    if (this.mList.length == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int size() {
    return this.mList.length;
  }
  
  String toLanguageTags() {
    return this.mStringRepresentation;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        stringBuilder.append(arrayOfLocale[b]);
        if (b < this.mList.length - 1)
          stringBuilder.append(','); 
        b++;
        continue;
      } 
      stringBuilder.append("]");
      return stringBuilder.toString();
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/os/LocaleListHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */