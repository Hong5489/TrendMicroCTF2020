package com.trendmicro.hippo;

public final class NativeStringIterator extends ES6Iterator {
  private static final String ITERATOR_TAG = "StringIterator";
  
  private static final long serialVersionUID = 1L;
  
  private int index;
  
  private String string;
  
  private NativeStringIterator() {}
  
  NativeStringIterator(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    super(paramScriptable1, "StringIterator");
    this.index = 0;
    this.string = ScriptRuntime.toString(paramScriptable2);
  }
  
  static void init(ScriptableObject paramScriptableObject, boolean paramBoolean) {
    ES6Iterator.init(paramScriptableObject, paramBoolean, new NativeStringIterator(), "StringIterator");
  }
  
  public String getClassName() {
    return "String Iterator";
  }
  
  protected String getTag() {
    return "StringIterator";
  }
  
  protected boolean isDone(Context paramContext, Scriptable paramScriptable) {
    boolean bool;
    if (this.index >= this.string.length()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected Object nextValue(Context paramContext, Scriptable paramScriptable) {
    int i = this.string.offsetByCodePoints(this.index, 1);
    String str = this.string.substring(this.index, i);
    this.index = i;
    return str;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeStringIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */