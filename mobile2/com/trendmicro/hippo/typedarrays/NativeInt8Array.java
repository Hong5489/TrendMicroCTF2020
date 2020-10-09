package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeInt8Array extends NativeTypedArrayView<Byte> {
  private static final String CLASS_NAME = "Int8Array";
  
  private static final long serialVersionUID = -3349419704390398895L;
  
  public NativeInt8Array() {}
  
  public NativeInt8Array(int paramInt) {
    this(new NativeArrayBuffer(paramInt), 0, paramInt);
  }
  
  public NativeInt8Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeInt8Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeInt8Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeInt8Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Byte get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Byte)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 1;
  }
  
  public String getClassName() {
    return "Int8Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readInt8(this.arrayBuffer.buffer, this.offset + paramInt);
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    int i = Conversions.toInt8(paramObject);
    ByteIo.writeInt8(this.arrayBuffer.buffer, this.offset + paramInt, i);
    return null;
  }
  
  protected NativeInt8Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeInt8Array)
      return (NativeInt8Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Byte set(int paramInt, Byte paramByte) {
    if (!checkIndex(paramInt))
      return (Byte)js_set(paramInt, paramByte); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeInt8Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */