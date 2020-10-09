package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeFloat64Array extends NativeTypedArrayView<Double> {
  private static final int BYTES_PER_ELEMENT = 8;
  
  private static final String CLASS_NAME = "Float64Array";
  
  private static final long serialVersionUID = -1255405650050639335L;
  
  public NativeFloat64Array() {}
  
  public NativeFloat64Array(int paramInt) {
    this(new NativeArrayBuffer((paramInt * 8)), 0, paramInt);
  }
  
  public NativeFloat64Array(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2, paramInt2 * 8);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeFloat64Array()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  protected NativeFloat64Array construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    return new NativeFloat64Array(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  public Double get(int paramInt) {
    if (!checkIndex(paramInt))
      return (Double)js_get(paramInt); 
    throw new IndexOutOfBoundsException();
  }
  
  public int getBytesPerElement() {
    return 8;
  }
  
  public String getClassName() {
    return "Float64Array";
  }
  
  protected Object js_get(int paramInt) {
    return checkIndex(paramInt) ? Undefined.instance : Double.valueOf(Double.longBitsToDouble(ByteIo.readUint64Primitive(this.arrayBuffer.buffer, paramInt * 8 + this.offset, useLittleEndian())));
  }
  
  protected Object js_set(int paramInt, Object paramObject) {
    if (checkIndex(paramInt))
      return Undefined.instance; 
    long l = Double.doubleToLongBits(ScriptRuntime.toNumber(paramObject));
    ByteIo.writeUint64(this.arrayBuffer.buffer, paramInt * 8 + this.offset, l, useLittleEndian());
    return null;
  }
  
  protected NativeFloat64Array realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeFloat64Array)
      return (NativeFloat64Array)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Double set(int paramInt, Double paramDouble) {
    if (!checkIndex(paramInt))
      return (Double)js_set(paramInt, paramDouble); 
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeFloat64Array.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */