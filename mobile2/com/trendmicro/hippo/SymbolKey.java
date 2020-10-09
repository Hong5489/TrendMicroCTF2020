package com.trendmicro.hippo;

import java.io.Serializable;

public class SymbolKey implements Symbol, Serializable {
  public static final SymbolKey HAS_INSTANCE;
  
  public static final SymbolKey IS_CONCAT_SPREADABLE;
  
  public static final SymbolKey IS_REGEXP;
  
  public static final SymbolKey ITERATOR = new SymbolKey("Symbol.iterator");
  
  public static final SymbolKey MATCH;
  
  public static final SymbolKey REPLACE;
  
  public static final SymbolKey SEARCH;
  
  public static final SymbolKey SPECIES;
  
  public static final SymbolKey SPLIT;
  
  public static final SymbolKey TO_PRIMITIVE;
  
  public static final SymbolKey TO_STRING_TAG = new SymbolKey("Symbol.toStringTag");
  
  public static final SymbolKey UNSCOPABLES;
  
  private static final long serialVersionUID = -6019782713330994754L;
  
  private String name;
  
  static {
    SPECIES = new SymbolKey("Symbol.species");
    HAS_INSTANCE = new SymbolKey("Symbol.hasInstance");
    IS_CONCAT_SPREADABLE = new SymbolKey("Symbol.isConcatSpreadable");
    IS_REGEXP = new SymbolKey("Symbol.isRegExp");
    TO_PRIMITIVE = new SymbolKey("Symbol.toPrimitive");
    MATCH = new SymbolKey("Symbol.match");
    REPLACE = new SymbolKey("Symbol.replace");
    SEARCH = new SymbolKey("Symbol.search");
    SPLIT = new SymbolKey("Symbol.split");
    UNSCOPABLES = new SymbolKey("Symbol.unscopables");
  }
  
  public SymbolKey(String paramString) {
    this.name = paramString;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof SymbolKey;
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool) {
      if (paramObject != this)
        bool2 = false; 
      return bool2;
    } 
    if (paramObject instanceof NativeSymbol) {
      if (((NativeSymbol)paramObject).getKey() == this) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      return bool2;
    } 
    return false;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int hashCode() {
    return System.identityHashCode(this);
  }
  
  public String toString() {
    if (this.name == null)
      return "Symbol()"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Symbol(");
    stringBuilder.append(this.name);
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SymbolKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */