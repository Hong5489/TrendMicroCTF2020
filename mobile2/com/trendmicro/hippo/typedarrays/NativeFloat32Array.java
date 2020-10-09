package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeFloat32Array extends NativeTypedArrayView<Float> {
  private static final int BYTES_PER_ELEMENT = 4;
  
  private static final String CLASS_NAME = "Float32Array";
  
  private static final long serialVersionUID = -8963461831950499340L;
  
  public NativeFloat32Array() {}
  
  public NativeFloat32Array(int paramInt) {
    this(new NativeArrayBuffer((paramInt * 4)), 0, paramInt);
  }
  
  public NativeFloat32Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2 * 4);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeFloat32Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeFloat32Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeFloat32Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Float get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Float)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 4;
  }
  
  public String getClassName() {
    return "Float32Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : ByteIo.readFloat32(this.arrayBuffer.buffer, paramInt * 4 + this.offset, useLittleEndian());
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    double d = ScriptRuntime.toNumber(paramObject);
    ByteIo.writeFloat32(this.arrayBuffer.buffer, paramInt * 4 + this.offset, d, useLittleEndian());
    return null;
  }
  
  protected NativeFloat32Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeFloat32Array)
      return (NativeFloat32Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Float set(int paramInt, Float paramFloat) {
    if (!checkIndex(paramInt))
      return (Float)js_set(paramInt, paramFloat); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeFloat32Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */