package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeUint8Array extends NativeTypedArrayView<Integer> {
  private static final String CLASS_NAME = "Uint8Array";
  
  private static final long serialVersionUID = -3349419704390398895L;
  
  public NativeUint8Array() {}
  
  public NativeUint8Array(int paramInt) {
    this(new NativeArrayBuffer(paramInt), 0, paramInt);
  }
  
  public NativeUint8Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeUint8Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeUint8Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeUint8Array(paramNativeArrayBuffer, paramInt1, paramInt2);
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
    return "Uint8Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readUint8(this.arrayBuffer.buffer, this.offset + paramInt);
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    int i = Conversions.toUint8(paramObject);
    ByteIo.writeUint8(this.arrayBuffer.buffer, this.offset + paramInt, i);
    return null;
  }
  
  protected NativeUint8Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeUint8Array)
      return (NativeUint8Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Integer set(int paramInt, Integer paramInteger) {
    if (!checkIndex(paramInt))
      return (Integer)js_set(paramInt, paramInteger); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeUint8Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */