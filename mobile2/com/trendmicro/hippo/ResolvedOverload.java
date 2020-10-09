package com.trendmicro.hippo;

import java.util.Arrays;

class ResolvedOverload {
  final int index;
  
  final Class<?>[] types;
  
  ResolvedOverload(Object[] paramArrayOfObject, int paramInt) {
    this.index = paramInt;
    this.types = new Class[paramArrayOfObject.length];
    paramInt = 0;
    int i = paramArrayOfObject.length;
    while (paramInt < i) {
      Object<?> object1 = (Object<?>)paramArrayOfObject[paramInt];
      Object<?> object2 = object1;
      if (object1 instanceof Wrapper)
        object2 = (Object<?>)((Wrapper)object1).unwrap(); 
      object1 = (Object<?>)this.types;
      if (object2 == null) {
        object2 = null;
      } else {
        object2 = (Object<?>)object2.getClass();
      } 
      object1[paramInt] = object2;
      paramInt++;
    } 
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof ResolvedOverload;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    bool = bool1;
    if (Arrays.equals((Object[])this.types, (Object[])((ResolvedOverload)paramObject).types)) {
      bool = bool1;
      if (this.index == ((ResolvedOverload)paramObject).index)
        bool = true; 
    } 
    return bool;
  }
  
  public int hashCode() {
    return Arrays.hashCode((Object[])this.types);
  }
  
  boolean matches(Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length != this.types.length)
      return false; 
    byte b = 0;
    int i = paramArrayOfObject.length;
    while (b < i) {
      Object object1 = paramArrayOfObject[b];
      Object object2 = object1;
      if (object1 instanceof Wrapper)
        object2 = ((Wrapper)object1).unwrap(); 
      if (object2 == null) {
        if (this.types[b] != null)
          return false; 
      } else if (object2.getClass() != this.types[b]) {
        return false;
      } 
      b++;
    } 
    return true;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ResolvedOverload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */