package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeInt32Array extends NativeTypedArrayView<Integer> {
  private static final int BYTES_PER_ELEMENT = 4;
  
  private static final String CLASS_NAME = "Int32Array";
  
  private static final long serialVersionUID = -8963461831950499340L;
  
  public NativeInt32Array() {}
  
  public NativeInt32Array(int paramInt) {
    this(new NativeArrayBuffer((paramInt * 4)), 0, paramInt);
  }
  
  public NativeInt32Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2 * 4);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeInt32Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeInt32Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeInt32Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Integer get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Integer)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 4;
  }
  
  public String getClassName() {
    return "Int32Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readInt32(this.arrayBuffer.buffer, paramInt * 4 + this.offset, useLittleEndian());
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    int i = ScriptRuntime.toInt32(paramObject);
    ByteIo.writeInt32(this.arrayBuffer.buffer, paramInt * 4 + this.offset, i, useLittleEndian());
    return null;
  }
  
  protected NativeInt32Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeInt32Array)
      return (NativeInt32Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Integer set(int paramInt, Integer paramInteger) {
    if (!checkIndex(paramInt))
      return (Integer)js_set(paramInt, paramInteger); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeInt32Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */