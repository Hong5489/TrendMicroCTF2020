package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjToIntMap implements Serializable {
  private static final int A = -1640531527;
  
  private static final Object DELETED = new Object();
  
  private static final boolean check = false;
  
  private static final long serialVersionUID = -1542220580748809402L;
  
  private int keyCount;
  
  private transient Object[] keys;
  
  private transient int occupiedCount;
  
  private int power;
  
  private transient int[] values;
  
  public ObjToIntMap() {
    this(4);
  }
  
  public ObjToIntMap(int paramInt) {
    if (paramInt < 0)
      Kit.codeBug(); 
    int i = paramInt * 4 / 3;
    for (paramInt = 2; 1 << paramInt < i; paramInt++);
    this.power = paramInt;
  }
  
  private int ensureIndex(Object paramObject) {
    int i = paramObject.hashCode();
    int j = -1;
    int k = -1;
    Object[] arrayOfObject = this.keys;
    int m = k;
    if (arrayOfObject != null) {
      int n = -1640531527 * i;
      int i1 = this.power;
      int i2 = n >>> 32 - i1;
      Object object = arrayOfObject[i2];
      j = i2;
      m = k;
      if (object != null) {
        i1 = 1 << i1;
        if (object == paramObject || (this.values[i1 + i2] == i && object.equals(paramObject)))
          return i2; 
        m = k;
        if (object == DELETED)
          m = i2; 
        k = i1 - 1;
        n = tableLookupStep(n, k, this.power);
        while (true) {
          j = i2 + n & k;
          object = this.keys[j];
          if (object == null)
            break; 
          if (object == paramObject || (this.values[i1 + j] == i && object.equals(paramObject)))
            return j; 
          i2 = j;
          if (object == DELETED) {
            i2 = j;
            if (m < 0) {
              m = j;
              i2 = j;
            } 
          } 
        } 
      } 
    } 
    if (m >= 0) {
      j = m;
    } else {
      if (this.keys != null) {
        int n = this.occupiedCount;
        if (n * 4 >= (1 << this.power) * 3) {
          rehashTable();
          return insertNewKey(paramObject, i);
        } 
        this.occupiedCount = n + 1;
        this.keys[j] = paramObject;
        this.values[(1 << this.power) + j] = i;
        this.keyCount++;
        return j;
      } 
      rehashTable();
      return insertNewKey(paramObject, i);
    } 
    this.keys[j] = paramObject;
    this.values[(1 << this.power) + j] = i;
    this.keyCount++;
    return j;
  }
  
  private int findIndex(Object paramObject) {
    if (this.keys != null) {
      int i = paramObject.hashCode();
      int j = -1640531527 * i;
      int k = this.power;
      int m = j >>> 32 - k;
      Object object = this.keys[m];
      if (object != null) {
        k = 1 << k;
        if (object == paramObject || (this.values[k + m] == i && object.equals(paramObject)))
          return m; 
        int n = k - 1;
        int i1 = tableLookupStep(j, n, this.power);
        while (true) {
          j = m + i1 & n;
          object = this.keys[j];
          if (object == null)
            break; 
          if (object != paramObject) {
            m = j;
            if (this.values[k + j] == i) {
              m = j;
              if (object.equals(paramObject))
                return j; 
            } 
            continue;
          } 
          return j;
        } 
      } 
    } 
    return -1;
  }
  
  private int insertNewKey(Object paramObject, int paramInt) {
    int i = -1640531527 * paramInt;
    int j = this.power;
    int k = i >>> 32 - j;
    int m = 1 << j;
    int n = k;
    if (this.keys[k] != null) {
      int i1 = m - 1;
      i = tableLookupStep(i, i1, j);
      n = k;
      while (true) {
        k = n + i & i1;
        n = k;
        if (this.keys[k] == null) {
          n = k;
          break;
        } 
      } 
    } 
    this.keys[n] = paramObject;
    this.values[m + n] = paramInt;
    this.occupiedCount++;
    this.keyCount++;
    return n;
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    int i = this.keyCount;
    if (i != 0) {
      this.keyCount = 0;
      int j = 1 << this.power;
      this.keys = new Object[j];
      this.values = new int[j * 2];
      for (j = 0; j != i; j++) {
        Object object = paramObjectInputStream.readObject();
        int k = insertNewKey(object, object.hashCode());
        this.values[k] = paramObjectInputStream.readInt();
      } 
    } 
  }
  
  private void rehashTable() {
    if (this.keys == null) {
      int i = 1 << this.power;
      this.keys = new Object[i];
      this.values = new int[i * 2];
    } else {
      if (this.keyCount * 2 >= this.occupiedCount)
        this.power++; 
      int i = 1 << this.power;
      Object[] arrayOfObject = this.keys;
      int[] arrayOfInt = this.values;
      int j = arrayOfObject.length;
      this.keys = new Object[i];
      this.values = new int[i * 2];
      int k = this.keyCount;
      this.keyCount = 0;
      this.occupiedCount = 0;
      i = 0;
      while (k != 0) {
        Object object = arrayOfObject[i];
        int m = k;
        if (object != null) {
          m = k;
          if (object != DELETED) {
            m = insertNewKey(object, arrayOfInt[j + i]);
            this.values[m] = arrayOfInt[i];
            m = k - 1;
          } 
        } 
        i++;
        k = m;
      } 
    } 
  }
  
  private static int tableLookupStep(int paramInt1, int paramInt2, int paramInt3) {
    paramInt3 = 32 - paramInt3 * 2;
    return (paramInt3 >= 0) ? (paramInt1 >>> paramInt3 & paramInt2 | 0x1) : (paramInt2 >>> -paramInt3 & paramInt1 | 0x1);
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    int i = this.keyCount;
    byte b = 0;
    while (i != 0) {
      Object object = this.keys[b];
      int j = i;
      if (object != null) {
        j = i;
        if (object != DELETED) {
          j = i - 1;
          paramObjectOutputStream.writeObject(object);
          paramObjectOutputStream.writeInt(this.values[b]);
        } 
      } 
      b++;
      i = j;
    } 
  }
  
  public void clear() {
    int i = this.keys.length;
    while (i != 0) {
      Object[] arrayOfObject = this.keys;
      arrayOfObject[--i] = null;
    } 
    this.keyCount = 0;
    this.occupiedCount = 0;
  }
  
  public int get(Object paramObject, int paramInt) {
    Object object = paramObject;
    if (paramObject == null)
      object = UniqueTag.NULL_VALUE; 
    int i = findIndex(object);
    return (i >= 0) ? this.values[i] : paramInt;
  }
  
  public int getExisting(Object paramObject) {
    Object object = paramObject;
    if (paramObject == null)
      object = UniqueTag.NULL_VALUE; 
    int i = findIndex(object);
    if (i >= 0)
      return this.values[i]; 
    Kit.codeBug();
    return 0;
  }
  
  public void getKeys(Object[] paramArrayOfObject, int paramInt) {
    int i = this.keyCount;
    int j = 0;
    int k = paramInt;
    paramInt = j;
    while (i != 0) {
      Object object = this.keys[paramInt];
      int m = i;
      j = k;
      if (object != null) {
        m = i;
        j = k;
        if (object != DELETED) {
          Object object1 = object;
          if (object == UniqueTag.NULL_VALUE)
            object1 = null; 
          paramArrayOfObject[k] = object1;
          j = k + 1;
          m = i - 1;
        } 
      } 
      paramInt++;
      i = m;
      k = j;
    } 
  }
  
  public Object[] getKeys() {
    Object[] arrayOfObject = new Object[this.keyCount];
    getKeys(arrayOfObject, 0);
    return arrayOfObject;
  }
  
  public boolean has(Object paramObject) {
    boolean bool;
    Object object = paramObject;
    if (paramObject == null)
      object = UniqueTag.NULL_VALUE; 
    if (findIndex(object) >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  final void initIterator(Iterator paramIterator) {
    paramIterator.init(this.keys, this.values, this.keyCount);
  }
  
  public Object intern(Object paramObject) {
    boolean bool = false;
    Object object = paramObject;
    if (paramObject == null) {
      bool = true;
      object = UniqueTag.NULL_VALUE;
    } 
    int i = ensureIndex(object);
    this.values[i] = 0;
    if (bool) {
      paramObject = null;
    } else {
      paramObject = this.keys[i];
    } 
    return paramObject;
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.keyCount == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Iterator newIterator() {
    return new Iterator(this);
  }
  
  public void put(Object paramObject, int paramInt) {
    Object object = paramObject;
    if (paramObject == null)
      object = UniqueTag.NULL_VALUE; 
    int i = ensureIndex(object);
    this.values[i] = paramInt;
  }
  
  public void remove(Object paramObject) {
    Object object = paramObject;
    if (paramObject == null)
      object = UniqueTag.NULL_VALUE; 
    int i = findIndex(object);
    if (i >= 0) {
      this.keys[i] = DELETED;
      this.keyCount--;
    } 
  }
  
  public int size() {
    return this.keyCount;
  }
  
  public static class Iterator {
    private int cursor;
    
    private Object[] keys;
    
    ObjToIntMap master;
    
    private int remaining;
    
    private int[] values;
    
    Iterator(ObjToIntMap param1ObjToIntMap) {
      this.master = param1ObjToIntMap;
    }
    
    public boolean done() {
      boolean bool;
      if (this.remaining < 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Object getKey() {
      Object object1 = this.keys[this.cursor];
      Object object2 = object1;
      if (object1 == UniqueTag.NULL_VALUE)
        object2 = null; 
      return object2;
    }
    
    public int getValue() {
      return this.values[this.cursor];
    }
    
    final void init(Object[] param1ArrayOfObject, int[] param1ArrayOfint, int param1Int) {
      this.keys = param1ArrayOfObject;
      this.values = param1ArrayOfint;
      this.cursor = -1;
      this.remaining = param1Int;
    }
    
    public void next() {
      if (this.remaining == -1)
        Kit.codeBug(); 
      if (this.remaining == 0) {
        this.remaining = -1;
        this.cursor = -1;
      } else {
        for (int i = this.cursor;; i = this.cursor) {
          this.cursor = i + 1;
          Object object = this.keys[this.cursor];
          if (object != null && object != ObjToIntMap.DELETED) {
            this.remaining--;
            return;
          } 
        } 
      } 
    }
    
    public void setValue(int param1Int) {
      this.values[this.cursor] = param1Int;
    }
    
    public void start() {
      this.master.initIterator(this);
      next();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ObjToIntMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */