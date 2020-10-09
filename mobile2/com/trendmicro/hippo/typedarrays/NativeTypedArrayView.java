package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ExternalArrayData;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.NativeArray;
import com.trendmicro.hippo.NativeArrayIterator;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Symbol;
import com.trendmicro.hippo.SymbolKey;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.Wrapper;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public abstract class NativeTypedArrayView<T> extends NativeArrayBufferView implements List<T>, RandomAccess, ExternalArrayData {
  private static final int Id_BYTES_PER_ELEMENT = 5;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_get = 3;
  
  private static final int Id_length = 4;
  
  private static final int Id_set = 4;
  
  private static final int Id_subarray = 5;
  
  private static final int Id_toString = 2;
  
  private static final int MAX_INSTANCE_ID = 5;
  
  protected static final int MAX_PROTOTYPE_ID = 6;
  
  private static final int SymbolId_iterator = 6;
  
  protected final int length = 0;
  
  protected NativeTypedArrayView() {}
  
  protected NativeTypedArrayView(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2, int paramInt3) {
    super(paramNativeArrayBuffer, paramInt1, paramInt3);
  }
  
  private NativeTypedArrayView<T> js_constructor(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object<T> object;
    NativeTypedArrayView<T> nativeTypedArrayView;
    NativeTypedArrayView nativeTypedArrayView1;
    int i = 0;
    if (!isArg(paramArrayOfObject, 0))
      return construct(NativeArrayBuffer.EMPTY_BUFFER, 0, 0); 
    Object object1 = paramArrayOfObject[0];
    if (object1 == null)
      return construct(NativeArrayBuffer.EMPTY_BUFFER, 0, 0); 
    if (object1 instanceof Number || object1 instanceof String) {
      i = ScriptRuntime.toInt32(object1);
      return construct(makeArrayBuffer(paramContext, paramScriptable, getBytesPerElement() * i), 0, i);
    } 
    if (object1 instanceof NativeTypedArrayView) {
      nativeTypedArrayView1 = (NativeTypedArrayView)object1;
      NativeTypedArrayView<T> nativeTypedArrayView2 = construct(makeArrayBuffer(paramContext, paramScriptable, nativeTypedArrayView1.length * getBytesPerElement()), 0, nativeTypedArrayView1.length);
      for (i = 0; i < nativeTypedArrayView1.length; i++)
        nativeTypedArrayView2.js_set(i, nativeTypedArrayView1.js_get(i)); 
      return nativeTypedArrayView2;
    } 
    if (object1 instanceof NativeArrayBuffer) {
      int j;
      object = (Object<T>)object1;
      if (isArg((Object[])nativeTypedArrayView1, 1))
        i = ScriptRuntime.toInt32(nativeTypedArrayView1[1]); 
      if (isArg((Object[])nativeTypedArrayView1, 2)) {
        j = ScriptRuntime.toInt32(nativeTypedArrayView1[2]) * getBytesPerElement();
      } else {
        j = object.getLength() - i;
      } 
      if (i >= 0 && i <= ((NativeArrayBuffer)object).buffer.length) {
        if (j >= 0 && i + j <= ((NativeArrayBuffer)object).buffer.length) {
          if (i % getBytesPerElement() == 0) {
            if (j % getBytesPerElement() == 0)
              return construct((NativeArrayBuffer)object, i, j / getBytesPerElement()); 
            throw ScriptRuntime.constructError("RangeError", "offset and buffer must be a multiple of the byte size");
          } 
          throw ScriptRuntime.constructError("RangeError", "offset must be a multiple of the byte size");
        } 
        throw ScriptRuntime.constructError("RangeError", "length out of range");
      } 
      throw ScriptRuntime.constructError("RangeError", "offset out of range");
    } 
    if (object1 instanceof NativeArray) {
      NativeArray nativeArray = (NativeArray)object1;
      nativeTypedArrayView = construct(makeArrayBuffer((Context)object, paramScriptable, nativeArray.size() * getBytesPerElement()), 0, nativeArray.size());
      for (i = 0; i < nativeArray.size(); i++) {
        object = (Object<T>)nativeArray.get(i, (Scriptable)nativeArray);
        if (object == Scriptable.NOT_FOUND || object == Undefined.instance) {
          nativeTypedArrayView.js_set(i, Double.valueOf(Double.NaN));
        } else if (object instanceof Wrapper) {
          nativeTypedArrayView.js_set(i, ((Wrapper)object).unwrap());
        } else {
          nativeTypedArrayView.js_set(i, object);
        } 
      } 
      return nativeTypedArrayView;
    } 
    if (ScriptRuntime.isArrayObject(object1)) {
      Object[] arrayOfObject = ScriptRuntime.getArrayElements((Scriptable)object1);
      object = (Object<T>)construct(makeArrayBuffer((Context)object, (Scriptable)nativeTypedArrayView, arrayOfObject.length * getBytesPerElement()), 0, arrayOfObject.length);
      for (i = 0; i < arrayOfObject.length; i++)
        object.js_set(i, arrayOfObject[i]); 
      return (NativeTypedArrayView<T>)object;
    } 
    throw ScriptRuntime.constructError("Error", "invalid argument");
  }
  
  private Object js_subarray(Context paramContext, Scriptable paramScriptable, int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      paramInt1 = this.length + paramInt1; 
    if (paramInt2 < 0)
      paramInt2 = this.length + paramInt2; 
    paramInt1 = Math.max(0, paramInt1);
    paramInt2 = Math.max(0, Math.min(this.length, paramInt2) - paramInt1);
    paramInt1 = Math.min(getBytesPerElement() * paramInt1, this.arrayBuffer.getLength());
    return paramContext.newObject(paramScriptable, getClassName(), new Object[] { this.arrayBuffer, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  private NativeArrayBuffer makeArrayBuffer(Context paramContext, Scriptable paramScriptable, int paramInt) {
    return (NativeArrayBuffer)paramContext.newObject(paramScriptable, "ArrayBuffer", new Object[] { Integer.valueOf(paramInt) });
  }
  
  private void setRange(NativeArray paramNativeArray, int paramInt) {
    if (paramInt <= this.length) {
      if (paramNativeArray.size() + paramInt <= this.length) {
        Iterator iterator = paramNativeArray.iterator();
        while (iterator.hasNext()) {
          js_set(paramInt, iterator.next());
          paramInt++;
        } 
        return;
      } 
      throw ScriptRuntime.constructError("RangeError", "offset + length out of range");
    } 
    throw ScriptRuntime.constructError("RangeError", "offset out of range");
  }
  
  private void setRange(NativeTypedArrayView<T> paramNativeTypedArrayView, int paramInt) {
    int i = this.length;
    if (paramInt < i) {
      if (paramNativeTypedArrayView.length <= i - paramInt) {
        if (paramNativeTypedArrayView.arrayBuffer == this.arrayBuffer) {
          Object[] arrayOfObject = new Object[paramNativeTypedArrayView.length];
          for (i = 0; i < paramNativeTypedArrayView.length; i++)
            arrayOfObject[i] = paramNativeTypedArrayView.js_get(i); 
          for (i = 0; i < paramNativeTypedArrayView.length; i++)
            js_set(i + paramInt, arrayOfObject[i]); 
        } else {
          for (i = 0; i < paramNativeTypedArrayView.length; i++)
            js_set(i + paramInt, paramNativeTypedArrayView.js_get(i)); 
        } 
        return;
      } 
      throw ScriptRuntime.constructError("RangeError", "source array too long");
    } 
    throw ScriptRuntime.constructError("RangeError", "offset out of range");
  }
  
  public void add(int paramInt, T paramT) {
    throw new UnsupportedOperationException();
  }
  
  public boolean add(T paramT) {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(int paramInt, Collection<? extends T> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(Collection<? extends T> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  protected boolean checkIndex(int paramInt) {
    return (paramInt < 0 || paramInt >= this.length);
  }
  
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  protected abstract NativeTypedArrayView<T> construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2);
  
  public boolean contains(Object paramObject) {
    boolean bool;
    if (indexOf(paramObject) >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean containsAll(Collection<?> paramCollection) {
    Iterator<?> iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      if (!contains(iterator.next()))
        return false; 
    } 
    return true;
  }
  
  public void delete(int paramInt) {}
  
  public boolean equals(Object paramObject) {
    try {
      paramObject = paramObject;
      if (this.length != ((NativeTypedArrayView)paramObject).length)
        return false; 
      for (byte b = 0; b < this.length; b++) {
        boolean bool = js_get(b).equals(paramObject.js_get(b));
        if (!bool)
          return false; 
      } 
      return true;
    } catch (ClassCastException classCastException) {
      return false;
    } 
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    NativeTypedArrayView<T> nativeTypedArrayView1;
    StringBuilder stringBuilder;
    NativeTypedArrayView<T> nativeTypedArrayView2;
    int j;
    if (!paramIdFunctionObject.hasTag(getClassName()))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    switch (i) {
      default:
        throw new IllegalArgumentException(String.valueOf(i));
      case 6:
        return new NativeArrayIterator(paramScriptable1, paramScriptable2, NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES);
      case 5:
        if (paramArrayOfObject.length > 0) {
          nativeTypedArrayView1 = realThis(paramScriptable2, paramIdFunctionObject);
          int k = ScriptRuntime.toInt32(paramArrayOfObject[0]);
          if (isArg(paramArrayOfObject, 1)) {
            i = ScriptRuntime.toInt32(paramArrayOfObject[1]);
          } else {
            i = nativeTypedArrayView1.length;
          } 
          return nativeTypedArrayView1.js_subarray(paramContext, paramScriptable1, k, i);
        } 
        throw ScriptRuntime.constructError("Error", "invalid arguments");
      case 4:
        if (paramArrayOfObject.length > 0) {
          nativeTypedArrayView1 = realThis(paramScriptable2, (IdFunctionObject)nativeTypedArrayView1);
          if (paramArrayOfObject[0] instanceof NativeTypedArrayView) {
            if (isArg(paramArrayOfObject, 1)) {
              i = ScriptRuntime.toInt32(paramArrayOfObject[1]);
            } else {
              i = 0;
            } 
            nativeTypedArrayView1.setRange((NativeTypedArrayView<T>)paramArrayOfObject[0], i);
            return Undefined.instance;
          } 
          if (paramArrayOfObject[0] instanceof NativeArray) {
            if (isArg(paramArrayOfObject, 1)) {
              i = ScriptRuntime.toInt32(paramArrayOfObject[1]);
            } else {
              i = 0;
            } 
            nativeTypedArrayView1.setRange((NativeArray)paramArrayOfObject[0], i);
            return Undefined.instance;
          } 
          if (paramArrayOfObject[0] instanceof Scriptable)
            return Undefined.instance; 
          if (isArg(paramArrayOfObject, 2))
            return nativeTypedArrayView1.js_set(ScriptRuntime.toInt32(paramArrayOfObject[0]), paramArrayOfObject[1]); 
        } 
        throw ScriptRuntime.constructError("Error", "invalid arguments");
      case 3:
        if (paramArrayOfObject.length > 0)
          return realThis(paramScriptable2, (IdFunctionObject)nativeTypedArrayView1).js_get(ScriptRuntime.toInt32(paramArrayOfObject[0])); 
        throw ScriptRuntime.constructError("Error", "invalid arguments");
      case 2:
        nativeTypedArrayView2 = realThis(paramScriptable2, (IdFunctionObject)nativeTypedArrayView1);
        j = nativeTypedArrayView2.getArrayLength();
        stringBuilder = new StringBuilder();
        if (j > 0)
          stringBuilder.append(ScriptRuntime.toString(nativeTypedArrayView2.js_get(0))); 
        for (i = 1; i < j; i++) {
          stringBuilder.append(',');
          stringBuilder.append(ScriptRuntime.toString(nativeTypedArrayView2.js_get(i)));
        } 
        return stringBuilder.toString();
      case 1:
        break;
    } 
    return js_constructor((Context)nativeTypedArrayView2, paramScriptable1, paramArrayOfObject);
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    paramIdFunctionObject.put("BYTES_PER_ELEMENT", (Scriptable)paramIdFunctionObject, ScriptRuntime.wrapInt(getBytesPerElement()));
  }
  
  protected int findInstanceIdInfo(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 6) {
      str = "length";
      b = 4;
    } else if (i == 17) {
      str = "BYTES_PER_ELEMENT";
      b = 5;
    } 
    i = b;
    if (str != null) {
      i = b;
      if (str != paramString) {
        i = b;
        if (!str.equals(paramString))
          i = 0; 
      } 
    } 
    return (i == 0) ? super.findInstanceIdInfo(paramString) : instanceIdInfo(5, i);
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.ITERATOR.equals(paramSymbol) ? 6 : 0;
  }
  
  protected int findPrototypeId(String paramString) {
    byte b2;
    String str2;
    byte b1 = 0;
    String str1 = null;
    int i = paramString.length();
    if (i == 3) {
      i = paramString.charAt(0);
      if (i == 103) {
        b2 = b1;
        str2 = str1;
        if (paramString.charAt(2) == 't') {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(1) == 'e')
            return 3; 
        } 
      } else {
        b2 = b1;
        str2 = str1;
        if (i == 115) {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(2) == 't') {
            b2 = b1;
            str2 = str1;
            if (paramString.charAt(1) == 'e')
              return 4; 
          } 
        } 
      } 
    } else if (i == 8) {
      i = paramString.charAt(0);
      if (i == 115) {
        str2 = "subarray";
        b2 = 5;
      } else {
        b2 = b1;
        str2 = str1;
        if (i == 116) {
          str2 = "toString";
          b2 = 2;
        } 
      } 
    } else {
      b2 = b1;
      str2 = str1;
      if (i == 11) {
        str2 = "constructor";
        b2 = 1;
      } 
    } 
    b1 = b2;
    if (str2 != null) {
      b1 = b2;
      if (str2 != paramString) {
        b1 = b2;
        if (!str2.equals(paramString))
          b1 = 0; 
      } 
    } 
    return b1;
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    return js_get(paramInt);
  }
  
  public Object getArrayElement(int paramInt) {
    return js_get(paramInt);
  }
  
  public int getArrayLength() {
    return this.length;
  }
  
  public abstract int getBytesPerElement();
  
  public Object[] getIds() {
    Object[] arrayOfObject = new Object[this.length];
    for (byte b = 0; b < this.length; b++)
      arrayOfObject[b] = Integer.valueOf(b); 
    return arrayOfObject;
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt != 4) ? ((paramInt != 5) ? super.getInstanceIdName(paramInt) : "BYTES_PER_ELEMENT") : "length";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt != 4) ? ((paramInt != 5) ? super.getInstanceIdValue(paramInt) : ScriptRuntime.wrapInt(getBytesPerElement())) : ScriptRuntime.wrapInt(this.length);
  }
  
  protected int getMaxInstanceId() {
    return 5;
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    return checkIndex(paramInt) ^ true;
  }
  
  public int hashCode() {
    int i = 0;
    for (byte b = 0; b < this.length; b++)
      i += js_get(b).hashCode(); 
    return i;
  }
  
  public int indexOf(Object paramObject) {
    for (byte b = 0; b < this.length; b++) {
      if (paramObject.equals(js_get(b)))
        return b; 
    } 
    return -1;
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt == 6) {
      initPrototypeMethod(getClassName(), paramInt, (Symbol)SymbolKey.ITERATOR, "[Symbol.iterator]", 0);
      return;
    } 
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt != 4) {
            if (paramInt == 5) {
              bool = true;
              str = "subarray";
            } else {
              throw new IllegalArgumentException(String.valueOf(paramInt));
            } 
          } else {
            bool = true;
            str = "set";
          } 
        } else {
          bool = true;
          str = "get";
        } 
      } else {
        bool = false;
        str = "toString";
      } 
    } else {
      bool = true;
      str = "constructor";
    } 
    initPrototypeMethod(getClassName(), paramInt, str, null, bool);
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.length == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Iterator<T> iterator() {
    return new NativeTypedArrayIterator<>(this, 0);
  }
  
  protected abstract Object js_get(int paramInt);
  
  protected abstract Object js_set(int paramInt, Object paramObject);
  
  public int lastIndexOf(Object paramObject) {
    for (int i = this.length - 1; i >= 0; i--) {
      if (paramObject.equals(js_get(i)))
        return i; 
    } 
    return -1;
  }
  
  public ListIterator<T> listIterator() {
    return new NativeTypedArrayIterator<>(this, 0);
  }
  
  public ListIterator<T> listIterator(int paramInt) {
    if (!checkIndex(paramInt))
      return new NativeTypedArrayIterator<>(this, paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    js_set(paramInt, paramObject);
  }
  
  protected abstract NativeTypedArrayView<T> realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject);
  
  public T remove(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public boolean remove(Object paramObject) {
    throw new UnsupportedOperationException();
  }
  
  public boolean removeAll(Collection<?> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public boolean retainAll(Collection<?> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public void setArrayElement(int paramInt, Object paramObject) {
    js_set(paramInt, paramObject);
  }
  
  public int size() {
    return this.length;
  }
  
  public List<T> subList(int paramInt1, int paramInt2) {
    throw new UnsupportedOperationException();
  }
  
  public Object[] toArray() {
    Object[] arrayOfObject = new Object[this.length];
    for (byte b = 0; b < this.length; b++)
      arrayOfObject[b] = js_get(b); 
    return arrayOfObject;
  }
  
  public <U> U[] toArray(U[] paramArrayOfU) {
    if (paramArrayOfU.length < this.length)
      paramArrayOfU = (U[])Array.newInstance(paramArrayOfU.getClass().getComponentType(), this.length); 
    byte b = 0;
    while (b < this.length) {
      try {
        paramArrayOfU[b] = (U)js_get(b);
        b++;
      } catch (ClassCastException classCastException) {
        throw new ArrayStoreException();
      } 
    } 
    return (U[])classCastException;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeTypedArrayView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */