package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeUint8ClampedArray extends NativeTypedArrayView<Integer> {
  private static final String CLASS_NAME = "Uint8ClampedArray";
  
  private static final long serialVersionUID = -3349419704390398895L;
  
  public NativeUint8ClampedArray() {}
  
  public NativeUint8ClampedArray(int paramInt) {
    this(new NativeArrayBuffer(paramInt), 0, paramInt);
  }
  
  public NativeUint8ClampedArray(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeUint8ClampedArray()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeUint8ClampedArray construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeUint8ClampedArray(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Integer get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Integer)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 1;
  }
  
  public String getClassName() {
    return "Uint8ClampedArray";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readUint8(this.arrayBuffer.buffer, this.offset + paramInt);
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    int i = Conversions.toUint8Clamp(paramObject);
    ByteIo.writeUint8(this.arrayBuffer.buffer, this.offset + paramInt, i);
    return null;
  }
  
  protected NativeUint8ClampedArray realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeUint8ClampedArray)
      return (NativeUint8ClampedArray)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Integer set(int paramInt, Integer paramInteger) {
    if (!checkIndex(paramInt))
      return (Integer)js_set(paramInt, paramInteger); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeUint8ClampedArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */