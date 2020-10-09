package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeUint32Array extends NativeTypedArrayView<Long> {
  private static final int BYTES_PER_ELEMENT = 4;
  
  private static final String CLASS_NAME = "Uint32Array";
  
  private static final long serialVersionUID = -7987831421954144244L;
  
  public NativeUint32Array() {}
  
  public NativeUint32Array(int paramInt) {
    this(new NativeArrayBuffer((paramInt * 4)), 0, paramInt);
  }
  
  public NativeUint32Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2 * 4);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeUint32Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeUint32Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeUint32Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Long get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Long)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 4;
  }
  
  public String getClassName() {
    return "Uint32Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readUint32(this.arrayBuffer.buffer, paramInt * 4 + this.offset, useLittleEndian());
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    long l = Conversions.toUint32(paramObject);
    ByteIo.writeUint32(this.arrayBuffer.buffer, paramInt * 4 + this.offset, l, useLittleEndian());
    return null;
  }
  
  protected NativeUint32Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeUint32Array)
      return (NativeUint32Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Long set(int paramInt, Long paramLong) {
    if (!checkIndex(paramInt))
      return (Long)js_set(paramInt, paramLong); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeUint32Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */