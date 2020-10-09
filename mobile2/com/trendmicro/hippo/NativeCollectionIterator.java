package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;

public class NativeCollectionIterator extends ES6Iterator {
  private String className;
  
  private transient Iterator<Hashtable.Entry> iterator = Collections.emptyIterator();
  
  private Type type;
  
  public NativeCollectionIterator(Scriptable paramScriptable, String paramString, Type paramType, Iterator<Hashtable.Entry> paramIterator) {
    super(paramScriptable, paramString);
    this.className = paramString;
    this.iterator = paramIterator;
    this.type = paramType;
  }
  
  public NativeCollectionIterator(String paramString) {
    this.className = paramString;
    this.iterator = Collections.emptyIterator();
    this.type = Type.BOTH;
  }
  
  static void init(ScriptableObject paramScriptableObject, String paramString, boolean paramBoolean) {
    ES6Iterator.init(paramScriptableObject, paramBoolean, new NativeCollectionIterator(paramString), paramString);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    this.className = (String)paramObjectInputStream.readObject();
    this.type = (Type)paramObjectInputStream.readObject();
    this.iterator = Collections.emptyIterator();
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(this.className);
    paramObjectOutputStream.writeObject(this.type);
  }
  
  public String getClassName() {
    return this.className;
  }
  
  protected boolean isDone(Context paramContext, Scriptable paramScriptable) {
    return this.iterator.hasNext() ^ true;
  }
  
  protected Object nextValue(Context paramContext, Scriptable paramScriptable) {
    Hashtable.Entry entry = this.iterator.next();
    int i = null.$SwitchMap$com$trendmicro$hippo$NativeCollectionIterator$Type[this.type.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i == 3)
          return paramContext.newArray(paramScriptable, new Object[] { entry.key, entry.value }); 
        throw new AssertionError();
      } 
      return entry.value;
    } 
    return entry.key;
  }
  
  enum Type {
    BOTH, KEYS, VALUES;
    
    static {
      Type type = new Type("BOTH", 2);
      BOTH = type;
      $VALUES = new Type[] { KEYS, VALUES, type };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeCollectionIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */