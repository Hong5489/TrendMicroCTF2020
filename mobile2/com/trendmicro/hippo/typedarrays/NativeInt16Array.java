package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeInt16Array extends NativeTypedArrayView<Short> {
  private static final int BYTES_PER_ELEMENT = 2;
  
  private static final String CLASS_NAME = "Int16Array";
  
  private static final long serialVersionUID = -8592870435287581398L;
  
  public NativeInt16Array() {}
  
  public NativeInt16Array(int paramInt) {
    this(new NativeArrayBuffer((paramInt * 2)), 0, paramInt);
  }
  
  public NativeInt16Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2 * 2);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeInt16Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeInt16Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeInt16Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Short get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Short)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 2;
  }
  
  public String getClassName() {
    return "Int16Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readInt16(this.arrayBuffer.buffer, paramInt * 2 + this.offset, useLittleEndian());
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    int i = Conversions.toInt16(paramObject);
    ByteIo.writeInt16(this.arrayBuffer.buffer, paramInt * 2 + this.offset, i, useLittleEndian());
    return null;
  }
  
  protected NativeInt16Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeInt16Array)
      return (NativeInt16Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Short set(int paramInt, Short paramShort) {
    if (!checkIndex(paramInt))
      return (Short)js_set(paramInt, paramShort); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeInt16Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */