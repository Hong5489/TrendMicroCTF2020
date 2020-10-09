package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UintMap implements Serializable {
  private static final int A = -1640531527;
  
  private static final int DELETED = -2;
  
  private static final int EMPTY = -1;
  
  private static final boolean check = false;
  
  private static final long serialVersionUID = 4242698212885848444L;
  
  private transient int ivaluesShift;
  
  private int keyCount;
  
  private transient int[] keys;
  
  private transient int occupiedCount;
  
  private int power;
  
  private transient Object[] values;
  
  public UintMap() {
    this(4);
  }
  
  public UintMap(int paramInt) {
    if (paramInt < 0)
      Kit.codeBug(); 
    int i = paramInt * 4 / 3;
    for (paramInt = 2; 1 << paramInt < i; paramInt++);
    this.power = paramInt;
  }
  
  private int ensureIndex(int paramInt, boolean paramBoolean) {
    int i = -1;
    int j = -1;
    int[] arrayOfInt = this.keys;
    int k = j;
    if (arrayOfInt != null) {
      int m = -1640531527 * paramInt;
      int n = m >>> 32 - this.power;
      int i1 = arrayOfInt[n];
      if (i1 == paramInt)
        return n; 
      i = n;
      k = j;
      if (i1 != -1) {
        int i2;
        i = j;
        if (i1 == -2)
          i = n; 
        k = this.power;
        i1 = (1 << k) - 1;
        m = tableLookupStep(m, i1, k);
        j = i;
        do {
          i = n + m & i1;
          i2 = arrayOfInt[i];
          if (i2 == paramInt)
            return i; 
          k = j;
          if (i2 == -2) {
            k = j;
            if (j < 0)
              k = i; 
          } 
          n = i;
          j = k;
        } while (i2 != -1);
      } 
    } 
    if (k >= 0) {
      i = k;
    } else {
      if (arrayOfInt != null) {
        int m = this.occupiedCount;
        if (m * 4 >= (1 << this.power) * 3) {
          rehashTable(paramBoolean);
          return insertNewKey(paramInt);
        } 
        this.occupiedCount = m + 1;
        arrayOfInt[i] = paramInt;
        this.keyCount++;
        return i;
      } 
      rehashTable(paramBoolean);
      return insertNewKey(paramInt);
    } 
    arrayOfInt[i] = paramInt;
    this.keyCount++;
    return i;
  }
  
  private int findIndex(int paramInt) {
    int[] arrayOfInt = this.keys;
    if (arrayOfInt != null) {
      int i = -1640531527 * paramInt;
      int j = this.power;
      int k = i >>> 32 - j;
      int m = arrayOfInt[k];
      if (m == paramInt)
        return k; 
      if (m != -1) {
        m = (1 << j) - 1;
        i = tableLookupStep(i, m, j);
        do {
          k = k + i & m;
          j = arrayOfInt[k];
          if (j == paramInt)
            return k; 
        } while (j != -1);
      } 
    } 
    return -1;
  }
  
  private int insertNewKey(int paramInt) {
    int[] arrayOfInt = this.keys;
    int i = -1640531527 * paramInt;
    int j = this.power;
    int k = i >>> 32 - j;
    int m = k;
    if (arrayOfInt[k] != -1) {
      int n = (1 << j) - 1;
      i = tableLookupStep(i, n, j);
      do {
        m = k + i & n;
        k = m;
      } while (arrayOfInt[m] != -1);
    } 
    arrayOfInt[m] = paramInt;
    this.occupiedCount++;
    this.keyCount++;
    return m;
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    int i = this.keyCount;
    if (i != 0) {
      this.keyCount = 0;
      boolean bool1 = paramObjectInputStream.readBoolean();
      boolean bool2 = paramObjectInputStream.readBoolean();
      int j = 1 << this.power;
      if (bool1) {
        this.keys = new int[j * 2];
        this.ivaluesShift = j;
      } else {
        this.keys = new int[j];
      } 
      int k;
      for (k = 0; k != j; k++)
        this.keys[k] = -1; 
      if (bool2)
        this.values = new Object[j]; 
      for (k = 0; k != i; k++) {
        int m = insertNewKey(paramObjectInputStream.readInt());
        if (bool1) {
          j = paramObjectInputStream.readInt();
          this.keys[this.ivaluesShift + m] = j;
        } 
        if (bool2)
          this.values[m] = paramObjectInputStream.readObject(); 
      } 
    } 
  }
  
  private void rehashTable(boolean paramBoolean) {
    if (this.keys != null && this.keyCount * 2 >= this.occupiedCount)
      this.power++; 
    int i = 1 << this.power;
    int[] arrayOfInt = this.keys;
    int j = this.ivaluesShift;
    if (j == 0 && !paramBoolean) {
      this.keys = new int[i];
    } else {
      this.ivaluesShift = i;
      this.keys = new int[i * 2];
    } 
    int k;
    for (k = 0; k != i; k++)
      this.keys[k] = -1; 
    Object[] arrayOfObject = this.values;
    if (arrayOfObject != null)
      this.values = new Object[i]; 
    i = this.keyCount;
    this.occupiedCount = 0;
    if (i != 0) {
      this.keyCount = 0;
      k = 0;
      while (i != 0) {
        int m = arrayOfInt[k];
        int n = i;
        if (m != -1) {
          n = i;
          if (m != -2) {
            n = insertNewKey(m);
            if (arrayOfObject != null)
              this.values[n] = arrayOfObject[k]; 
            if (j != 0)
              this.keys[this.ivaluesShift + n] = arrayOfInt[j + k]; 
            n = i - 1;
          } 
        } 
        k++;
        i = n;
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
    if (i != 0) {
      boolean bool2;
      int j = this.ivaluesShift;
      boolean bool1 = false;
      if (j != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (this.values != null)
        bool1 = true; 
      paramObjectOutputStream.writeBoolean(bool2);
      paramObjectOutputStream.writeBoolean(bool1);
      byte b = 0;
      while (i != 0) {
        int k = this.keys[b];
        j = i;
        if (k != -1) {
          j = i;
          if (k != -2) {
            i--;
            paramObjectOutputStream.writeInt(k);
            if (bool2)
              paramObjectOutputStream.writeInt(this.keys[this.ivaluesShift + b]); 
            j = i;
            if (bool1) {
              paramObjectOutputStream.writeObject(this.values[b]);
              j = i;
            } 
          } 
        } 
        b++;
        i = j;
      } 
    } 
  }
  
  public void clear() {
    int i = 1 << this.power;
    if (this.keys != null) {
      int j;
      for (j = 0; j != i; j++)
        this.keys[j] = -1; 
      if (this.values != null)
        for (j = 0; j != i; j++)
          this.values[j] = null;  
    } 
    this.ivaluesShift = 0;
    this.keyCount = 0;
    this.occupiedCount = 0;
  }
  
  public int getExistingInt(int paramInt) {
    if (paramInt < 0)
      Kit.codeBug(); 
    paramInt = findIndex(paramInt);
    if (paramInt >= 0) {
      int i = this.ivaluesShift;
      return (i != 0) ? this.keys[i + paramInt] : 0;
    } 
    Kit.codeBug();
    return 0;
  }
  
  public int getInt(int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      Kit.codeBug(); 
    paramInt1 = findIndex(paramInt1);
    if (paramInt1 >= 0) {
      paramInt2 = this.ivaluesShift;
      return (paramInt2 != 0) ? this.keys[paramInt2 + paramInt1] : 0;
    } 
    return paramInt2;
  }
  
  public int[] getKeys() {
    int[] arrayOfInt1 = this.keys;
    int i = this.keyCount;
    int[] arrayOfInt2 = new int[i];
    byte b = 0;
    while (i != 0) {
      int j = arrayOfInt1[b];
      int k = i;
      if (j != -1) {
        k = i;
        if (j != -2) {
          k = i - 1;
          arrayOfInt2[k] = j;
        } 
      } 
      b++;
      i = k;
    } 
    return arrayOfInt2;
  }
  
  public Object getObject(int paramInt) {
    if (paramInt < 0)
      Kit.codeBug(); 
    if (this.values != null) {
      paramInt = findIndex(paramInt);
      if (paramInt >= 0)
        return this.values[paramInt]; 
    } 
    return null;
  }
  
  public boolean has(int paramInt) {
    boolean bool;
    if (paramInt < 0)
      Kit.codeBug(); 
    if (findIndex(paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
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
  
  public void put(int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      Kit.codeBug(); 
    paramInt1 = ensureIndex(paramInt1, true);
    if (this.ivaluesShift == 0) {
      int i = 1 << this.power;
      int[] arrayOfInt = this.keys;
      if (arrayOfInt.length != i * 2) {
        int[] arrayOfInt1 = new int[i * 2];
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, i);
        this.keys = arrayOfInt1;
      } 
      this.ivaluesShift = i;
    } 
    this.keys[this.ivaluesShift + paramInt1] = paramInt2;
  }
  
  public void put(int paramInt, Object paramObject) {
    if (paramInt < 0)
      Kit.codeBug(); 
    paramInt = ensureIndex(paramInt, false);
    if (this.values == null)
      this.values = new Object[1 << this.power]; 
    this.values[paramInt] = paramObject;
  }
  
  public void remove(int paramInt) {
    if (paramInt < 0)
      Kit.codeBug(); 
    paramInt = findIndex(paramInt);
    if (paramInt >= 0) {
      this.keys[paramInt] = -2;
      this.keyCount--;
      Object[] arrayOfObject = this.values;
      if (arrayOfObject != null)
        arrayOfObject[paramInt] = null; 
      int i = this.ivaluesShift;
      if (i != 0)
        this.keys[i + paramInt] = 0; 
    } 
  }
  
  public int size() {
    return this.keyCount;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/UintMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */