package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeUint16Array extends NativeTypedArrayView<Integer> {
  private static final int BYTES_PER_ELEMENT = 2;
  
  private static final String CLASS_NAME = "Uint16Array";
  
  private static final long serialVersionUID = 7700018949434240321L;
  
  public NativeUint16Array() {}
  
  public NativeUint16Array(int paramInt) {
    this(new NativeArrayBuffer((paramInt * 2)), 0, paramInt);
  }
  
  public NativeUint16Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2 * 2);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeUint16Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeUint16Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeUint16Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Integer get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Integer)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 2;
  }
  
  public String getClassName() {
    return "Uint16Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readUint16(this.arrayBuffer.buffer, paramInt * 2 + this.offset, useLittleEndian());
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    int i = Conversions.toUint16(paramObject);
    ByteIo.writeUint16(this.arrayBuffer.buffer, paramInt * 2 + this.offset, i, useLittleEndian());
    return null;
  }
  
  protected NativeUint16Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeUint16Array)
      return (NativeUint16Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Integer set(int paramInt, Integer paramInteger) {
    if (!checkIndex(paramInt))
      return (Integer)js_set(paramInt, paramInteger); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeUint16Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */