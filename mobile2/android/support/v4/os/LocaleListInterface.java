package android.support.v4.os;

import java.util.Locale;

interface LocaleListInterface {
  boolean equals(Object paramObject);
  
  Locale get(int paramInt);
  
  Locale getFirstMatch(String[] paramArrayOfString);
  
  Object getLocaleList();
  
  int hashCode();
  
  int indexOf(Locale paramLocale);
  
  boolean isEmpty();
  
  void setLocaleList(Locale... paramVarArgs);
  
  int size();
  
  String toLanguageTags();
  
  String toString();
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/os/LocaleListInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */