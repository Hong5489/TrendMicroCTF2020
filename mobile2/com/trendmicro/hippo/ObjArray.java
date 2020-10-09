package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjArray implements Serializable {
  private static final int FIELDS_STORE_SIZE = 5;
  
  private static final long serialVersionUID = 4174889037736658296L;
  
  private transient Object[] data;
  
  private transient Object f0;
  
  private transient Object f1;
  
  private transient Object f2;
  
  private transient Object f3;
  
  private transient Object f4;
  
  private boolean sealed;
  
  private int size;
  
  private void ensureCapacity(int paramInt) {
    int i = paramInt - 5;
    if (i > 0) {
      Object[] arrayOfObject = this.data;
      if (arrayOfObject == null) {
        paramInt = 10;
        if (10 < i)
          paramInt = i; 
        this.data = new Object[paramInt];
      } else {
        paramInt = arrayOfObject.length;
        if (paramInt < i) {
          if (paramInt <= 5) {
            paramInt = 10;
          } else {
            paramInt *= 2;
          } 
          int j = paramInt;
          if (paramInt < i)
            j = i; 
          arrayOfObject = new Object[j];
          paramInt = this.size;
          if (paramInt > 5)
            System.arraycopy(this.data, 0, arrayOfObject, 0, paramInt - 5); 
          this.data = arrayOfObject;
        } 
      } 
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  private Object getImpl(int paramInt) {
    return (paramInt != 0) ? ((paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? this.data[paramInt - 5] : this.f4) : this.f3) : this.f2) : this.f1) : this.f0;
  }
  
  private static RuntimeException onEmptyStackTopRead() {
    throw new RuntimeException("Empty stack");
  }
  
  private static RuntimeException onInvalidIndex(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt1);
    stringBuilder.append(" ∉ [0, ");
    stringBuilder.append(paramInt2);
    stringBuilder.append(')');
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  private static RuntimeException onSeledMutation() {
    throw new IllegalStateException("Attempt to modify sealed array");
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    int i = this.size;
    if (i > 5)
      this.data = new Object[i - 5]; 
    for (int j = 0; j != i; j++)
      setImpl(j, paramObjectInputStream.readObject()); 
  }
  
  private void setImpl(int paramInt, Object paramObject) {
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt != 4) {
              this.data[paramInt - 5] = paramObject;
            } else {
              this.f4 = paramObject;
            } 
          } else {
            this.f3 = paramObject;
          } 
        } else {
          this.f2 = paramObject;
        } 
      } else {
        this.f1 = paramObject;
      } 
    } else {
      this.f0 = paramObject;
    } 
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    int i = this.size;
    for (int j = 0; j != i; j++)
      paramObjectOutputStream.writeObject(getImpl(j)); 
  }
  
  public final void add(int paramInt, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: getfield size : I
    //   4: istore_3
    //   5: iload_1
    //   6: iflt -> 251
    //   9: iload_1
    //   10: iload_3
    //   11: if_icmpgt -> 251
    //   14: aload_0
    //   15: getfield sealed : Z
    //   18: ifne -> 247
    //   21: iload_1
    //   22: ifeq -> 60
    //   25: aload_2
    //   26: astore #4
    //   28: iload_1
    //   29: iconst_1
    //   30: if_icmpeq -> 83
    //   33: aload_2
    //   34: astore #4
    //   36: iload_1
    //   37: iconst_2
    //   38: if_icmpeq -> 111
    //   41: aload_2
    //   42: astore #4
    //   44: iload_1
    //   45: iconst_3
    //   46: if_icmpeq -> 139
    //   49: aload_2
    //   50: astore #4
    //   52: iload_1
    //   53: iconst_4
    //   54: if_icmpeq -> 167
    //   57: goto -> 194
    //   60: iload_3
    //   61: ifne -> 72
    //   64: aload_0
    //   65: aload_2
    //   66: putfield f0 : Ljava/lang/Object;
    //   69: goto -> 239
    //   72: aload_0
    //   73: getfield f0 : Ljava/lang/Object;
    //   76: astore #4
    //   78: aload_0
    //   79: aload_2
    //   80: putfield f0 : Ljava/lang/Object;
    //   83: iload_3
    //   84: iconst_1
    //   85: if_icmpne -> 97
    //   88: aload_0
    //   89: aload #4
    //   91: putfield f1 : Ljava/lang/Object;
    //   94: goto -> 239
    //   97: aload_0
    //   98: getfield f1 : Ljava/lang/Object;
    //   101: astore_2
    //   102: aload_0
    //   103: aload #4
    //   105: putfield f1 : Ljava/lang/Object;
    //   108: aload_2
    //   109: astore #4
    //   111: iload_3
    //   112: iconst_2
    //   113: if_icmpne -> 125
    //   116: aload_0
    //   117: aload #4
    //   119: putfield f2 : Ljava/lang/Object;
    //   122: goto -> 239
    //   125: aload_0
    //   126: getfield f2 : Ljava/lang/Object;
    //   129: astore_2
    //   130: aload_0
    //   131: aload #4
    //   133: putfield f2 : Ljava/lang/Object;
    //   136: aload_2
    //   137: astore #4
    //   139: iload_3
    //   140: iconst_3
    //   141: if_icmpne -> 153
    //   144: aload_0
    //   145: aload #4
    //   147: putfield f3 : Ljava/lang/Object;
    //   150: goto -> 239
    //   153: aload_0
    //   154: getfield f3 : Ljava/lang/Object;
    //   157: astore_2
    //   158: aload_0
    //   159: aload #4
    //   161: putfield f3 : Ljava/lang/Object;
    //   164: aload_2
    //   165: astore #4
    //   167: iload_3
    //   168: iconst_4
    //   169: if_icmpne -> 181
    //   172: aload_0
    //   173: aload #4
    //   175: putfield f4 : Ljava/lang/Object;
    //   178: goto -> 239
    //   181: aload_0
    //   182: getfield f4 : Ljava/lang/Object;
    //   185: astore_2
    //   186: aload_0
    //   187: aload #4
    //   189: putfield f4 : Ljava/lang/Object;
    //   192: iconst_5
    //   193: istore_1
    //   194: aload_0
    //   195: iload_3
    //   196: iconst_1
    //   197: iadd
    //   198: invokespecial ensureCapacity : (I)V
    //   201: iload_1
    //   202: iload_3
    //   203: if_icmpeq -> 230
    //   206: aload_0
    //   207: getfield data : [Ljava/lang/Object;
    //   210: astore #4
    //   212: aload #4
    //   214: iload_1
    //   215: iconst_5
    //   216: isub
    //   217: aload #4
    //   219: iload_1
    //   220: iconst_5
    //   221: isub
    //   222: iconst_1
    //   223: iadd
    //   224: iload_3
    //   225: iload_1
    //   226: isub
    //   227: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   230: aload_0
    //   231: getfield data : [Ljava/lang/Object;
    //   234: iload_1
    //   235: iconst_5
    //   236: isub
    //   237: aload_2
    //   238: aastore
    //   239: aload_0
    //   240: iload_3
    //   241: iconst_1
    //   242: iadd
    //   243: putfield size : I
    //   246: return
    //   247: invokestatic onSeledMutation : ()Ljava/lang/RuntimeException;
    //   250: athrow
    //   251: iload_1
    //   252: iload_3
    //   253: iconst_1
    //   254: iadd
    //   255: invokestatic onInvalidIndex : (II)Ljava/lang/RuntimeException;
    //   258: athrow
  }
  
  public final void add(Object paramObject) {
    if (!this.sealed) {
      int i = this.size;
      if (i >= 5)
        ensureCapacity(i + 1); 
      this.size = i + 1;
      setImpl(i, paramObject);
      return;
    } 
    throw onSeledMutation();
  }
  
  public final void clear() {
    if (!this.sealed) {
      int i = this.size;
      for (int j = 0; j != i; j++)
        setImpl(j, null); 
      this.size = 0;
      return;
    } 
    throw onSeledMutation();
  }
  
  public final Object get(int paramInt) {
    if (paramInt >= 0 && paramInt < this.size)
      return getImpl(paramInt); 
    throw onInvalidIndex(paramInt, this.size);
  }
  
  public int indexOf(Object paramObject) {
    int i = this.size;
    for (int j = 0; j != i; j++) {
      Object object = getImpl(j);
      if (object == paramObject || (object != null && object.equals(paramObject)))
        return j; 
    } 
    return -1;
  }
  
  public final boolean isEmpty() {
    boolean bool;
    if (this.size == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isSealed() {
    return this.sealed;
  }
  
  public int lastIndexOf(Object paramObject) {
    int i = this.size;
    while (i != 0) {
      Object object = getImpl(--i);
      if (object == paramObject || (object != null && object.equals(paramObject)))
        return i; 
    } 
    return -1;
  }
  
  public final Object peek() {
    int i = this.size;
    if (i != 0)
      return getImpl(i - 1); 
    throw onEmptyStackTopRead();
  }
  
  public final Object pop() {
    if (!this.sealed) {
      int i = this.size - 1;
      if (i != -1) {
        Object object;
        if (i != 0) {
          if (i != 1) {
            if (i != 2) {
              if (i != 3) {
                if (i != 4) {
                  Object[] arrayOfObject = this.data;
                  object = arrayOfObject[i - 5];
                  arrayOfObject[i - 5] = null;
                } else {
                  object = this.f4;
                  this.f4 = null;
                } 
              } else {
                object = this.f3;
                this.f3 = null;
              } 
            } else {
              object = this.f2;
              this.f2 = null;
            } 
          } else {
            object = this.f1;
            this.f1 = null;
          } 
        } else {
          object = this.f0;
          this.f0 = null;
        } 
        this.size = i;
        return object;
      } 
      throw onEmptyStackTopRead();
    } 
    throw onSeledMutation();
  }
  
  public final void push(Object paramObject) {
    add(paramObject);
  }
  
  public final void remove(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield size : I
    //   4: istore_2
    //   5: iload_1
    //   6: iflt -> 204
    //   9: iload_1
    //   10: iload_2
    //   11: if_icmpge -> 204
    //   14: aload_0
    //   15: getfield sealed : Z
    //   18: ifne -> 200
    //   21: iinc #2, -1
    //   24: iload_1
    //   25: ifeq -> 51
    //   28: iload_1
    //   29: iconst_1
    //   30: if_icmpeq -> 71
    //   33: iload_1
    //   34: iconst_2
    //   35: if_icmpeq -> 92
    //   38: iload_1
    //   39: iconst_3
    //   40: if_icmpeq -> 113
    //   43: iload_1
    //   44: iconst_4
    //   45: if_icmpeq -> 134
    //   48: goto -> 159
    //   51: iload_2
    //   52: ifne -> 63
    //   55: aload_0
    //   56: aconst_null
    //   57: putfield f0 : Ljava/lang/Object;
    //   60: goto -> 194
    //   63: aload_0
    //   64: aload_0
    //   65: getfield f1 : Ljava/lang/Object;
    //   68: putfield f0 : Ljava/lang/Object;
    //   71: iload_2
    //   72: iconst_1
    //   73: if_icmpne -> 84
    //   76: aload_0
    //   77: aconst_null
    //   78: putfield f1 : Ljava/lang/Object;
    //   81: goto -> 194
    //   84: aload_0
    //   85: aload_0
    //   86: getfield f2 : Ljava/lang/Object;
    //   89: putfield f1 : Ljava/lang/Object;
    //   92: iload_2
    //   93: iconst_2
    //   94: if_icmpne -> 105
    //   97: aload_0
    //   98: aconst_null
    //   99: putfield f2 : Ljava/lang/Object;
    //   102: goto -> 194
    //   105: aload_0
    //   106: aload_0
    //   107: getfield f3 : Ljava/lang/Object;
    //   110: putfield f2 : Ljava/lang/Object;
    //   113: iload_2
    //   114: iconst_3
    //   115: if_icmpne -> 126
    //   118: aload_0
    //   119: aconst_null
    //   120: putfield f3 : Ljava/lang/Object;
    //   123: goto -> 194
    //   126: aload_0
    //   127: aload_0
    //   128: getfield f4 : Ljava/lang/Object;
    //   131: putfield f3 : Ljava/lang/Object;
    //   134: iload_2
    //   135: iconst_4
    //   136: if_icmpne -> 147
    //   139: aload_0
    //   140: aconst_null
    //   141: putfield f4 : Ljava/lang/Object;
    //   144: goto -> 194
    //   147: aload_0
    //   148: aload_0
    //   149: getfield data : [Ljava/lang/Object;
    //   152: iconst_0
    //   153: aaload
    //   154: putfield f4 : Ljava/lang/Object;
    //   157: iconst_5
    //   158: istore_1
    //   159: iload_1
    //   160: iload_2
    //   161: if_icmpeq -> 185
    //   164: aload_0
    //   165: getfield data : [Ljava/lang/Object;
    //   168: astore_3
    //   169: aload_3
    //   170: iload_1
    //   171: iconst_5
    //   172: isub
    //   173: iconst_1
    //   174: iadd
    //   175: aload_3
    //   176: iload_1
    //   177: iconst_5
    //   178: isub
    //   179: iload_2
    //   180: iload_1
    //   181: isub
    //   182: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   185: aload_0
    //   186: getfield data : [Ljava/lang/Object;
    //   189: iload_2
    //   190: iconst_5
    //   191: isub
    //   192: aconst_null
    //   193: aastore
    //   194: aload_0
    //   195: iload_2
    //   196: putfield size : I
    //   199: return
    //   200: invokestatic onSeledMutation : ()Ljava/lang/RuntimeException;
    //   203: athrow
    //   204: iload_1
    //   205: iload_2
    //   206: invokestatic onInvalidIndex : (II)Ljava/lang/RuntimeException;
    //   209: athrow
  }
  
  public final void seal() {
    this.sealed = true;
  }
  
  public final void set(int paramInt, Object paramObject) {
    if (paramInt >= 0 && paramInt < this.size) {
      if (!this.sealed) {
        setImpl(paramInt, paramObject);
        return;
      } 
      throw onSeledMutation();
    } 
    throw onInvalidIndex(paramInt, this.size);
  }
  
  public final void setSize(int paramInt) {
    if (paramInt >= 0) {
      if (!this.sealed) {
        int i = this.size;
        if (paramInt < i) {
          for (int j = paramInt; j != i; j++)
            setImpl(j, null); 
        } else if (paramInt > i && paramInt > 5) {
          ensureCapacity(paramInt);
        } 
        this.size = paramInt;
        return;
      } 
      throw onSeledMutation();
    } 
    throw new IllegalArgumentException();
  }
  
  public final int size() {
    return this.size;
  }
  
  public final void toArray(Object[] paramArrayOfObject) {
    toArray(paramArrayOfObject, 0);
  }
  
  public final void toArray(Object[] paramArrayOfObject, int paramInt) {
    int i = this.size;
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 4) {
              if (i != 5)
                System.arraycopy(this.data, 0, paramArrayOfObject, paramInt + 5, i - 5); 
              paramArrayOfObject[paramInt + 4] = this.f4;
            } 
            paramArrayOfObject[paramInt + 3] = this.f3;
          } 
          paramArrayOfObject[paramInt + 2] = this.f2;
        } 
        paramArrayOfObject[paramInt + 1] = this.f1;
      } 
      paramArrayOfObject[paramInt + 0] = this.f0;
    } 
  }
  
  public final Object[] toArray() {
    Object[] arrayOfObject = new Object[this.size];
    toArray(arrayOfObject, 0);
    return arrayOfObject;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ObjArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */