package com.trendmicro.hippo;

public final class NativeArrayIterator extends ES6Iterator {
  private static final String ITERATOR_TAG = "ArrayIterator";
  
  private static final long serialVersionUID = 1L;
  
  private Scriptable arrayLike;
  
  private int index;
  
  private ARRAY_ITERATOR_TYPE type;
  
  private NativeArrayIterator() {}
  
  public NativeArrayIterator(Scriptable paramScriptable1, Scriptable paramScriptable2, ARRAY_ITERATOR_TYPE paramARRAY_ITERATOR_TYPE) {
    super(paramScriptable1, "ArrayIterator");
    this.index = 0;
    this.arrayLike = paramScriptable2;
    this.type = paramARRAY_ITERATOR_TYPE;
  }
  
  static void init(ScriptableObject paramScriptableObject, boolean paramBoolean) {
    ES6Iterator.init(paramScriptableObject, paramBoolean, new NativeArrayIterator(), "ArrayIterator");
  }
  
  public String getClassName() {
    return "Array Iterator";
  }
  
  protected String getTag() {
    return "ArrayIterator";
  }
  
  protected boolean isDone(Context paramContext, Scriptable paramScriptable) {
    long l = this.index;
    paramScriptable = this.arrayLike;
    boolean bool = false;
    if (l >= NativeArray.getLengthProperty(paramContext, paramScriptable, false))
      bool = true; 
    return bool;
  }
  
  protected Object nextValue(Context paramContext, Scriptable paramScriptable) {
    if (this.type == ARRAY_ITERATOR_TYPE.KEYS) {
      int i = this.index;
      this.index = i + 1;
      return Integer.valueOf(i);
    } 
    Scriptable scriptable = this.arrayLike;
    Object object2 = scriptable.get(this.index, scriptable);
    Object object1 = object2;
    if (object2 == ScriptableObject.NOT_FOUND)
      object1 = Undefined.instance; 
    object2 = object1;
    if (this.type == ARRAY_ITERATOR_TYPE.ENTRIES)
      object2 = paramContext.newArray(paramScriptable, new Object[] { Integer.valueOf(this.index), object1 }); 
    this.index++;
    return object2;
  }
  
  public enum ARRAY_ITERATOR_TYPE {
    ENTRIES, KEYS, VALUES;
    
    static {
      ARRAY_ITERATOR_TYPE aRRAY_ITERATOR_TYPE = new ARRAY_ITERATOR_TYPE("VALUES", 2);
      VALUES = aRRAY_ITERATOR_TYPE;
      $VALUES = new ARRAY_ITERATOR_TYPE[] { ENTRIES, KEYS, aRRAY_ITERATOR_TYPE };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeArrayIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */