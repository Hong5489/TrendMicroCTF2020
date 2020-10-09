package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.IdScriptableObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeArrayBuffer extends IdScriptableObject {
  public static final String CLASS_NAME = "ArrayBuffer";
  
  private static final int ConstructorId_isView = -1;
  
  private static final byte[] EMPTY_BUF = new byte[0];
  
  public static final NativeArrayBuffer EMPTY_BUFFER = new NativeArrayBuffer();
  
  private static final int Id_byteLength = 1;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_slice = 2;
  
  private static final int MAX_INSTANCE_ID = 1;
  
  private static final int MAX_PROTOTYPE_ID = 2;
  
  private static final long serialVersionUID = 3110411773054879549L;
  
  final byte[] buffer;
  
  public NativeArrayBuffer() {
    this.buffer = EMPTY_BUF;
  }
  
  public NativeArrayBuffer(double paramDouble) {
    if (paramDouble < 2.147483647E9D) {
      if (paramDouble != Double.NEGATIVE_INFINITY) {
        int i = ScriptRuntime.toInt32(paramDouble);
        if (i >= 0) {
          if (i == 0) {
            this.buffer = EMPTY_BUF;
          } else {
            this.buffer = new byte[i];
          } 
          return;
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Negative array length ");
        stringBuilder2.append(paramDouble);
        throw ScriptRuntime.constructError("RangeError", stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Negative array length ");
      stringBuilder1.append(paramDouble);
      throw ScriptRuntime.constructError("RangeError", stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("length parameter (");
    stringBuilder.append(paramDouble);
    stringBuilder.append(") is too large ");
    throw ScriptRuntime.constructError("RangeError", stringBuilder.toString());
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeArrayBuffer()).exportAsJSClass(2, paramScriptable, paramBoolean);
  }
  
  private static boolean isArg(Object[] paramArrayOfObject, int paramInt) {
    boolean bool;
    if (paramArrayOfObject.length > paramInt && !Undefined.instance.equals(paramArrayOfObject[paramInt])) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static NativeArrayBuffer realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeArrayBuffer)
      return (NativeArrayBuffer)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag("ArrayBuffer"))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    boolean bool = true;
    if (i != -1) {
      double d = 0.0D;
      if (i != 1) {
        if (i == 2) {
          double d1;
          NativeArrayBuffer nativeArrayBuffer = realThis(paramScriptable2, paramIdFunctionObject);
          if (isArg(paramArrayOfObject, 0))
            d = ScriptRuntime.toNumber(paramArrayOfObject[0]); 
          if (isArg(paramArrayOfObject, 1)) {
            d1 = ScriptRuntime.toNumber(paramArrayOfObject[1]);
          } else {
            d1 = nativeArrayBuffer.buffer.length;
          } 
          return nativeArrayBuffer.slice(d, d1);
        } 
        throw new IllegalArgumentException(String.valueOf(i));
      } 
      if (isArg(paramArrayOfObject, 0))
        d = ScriptRuntime.toNumber(paramArrayOfObject[0]); 
      return new NativeArrayBuffer(d);
    } 
    if (!isArg(paramArrayOfObject, 0) || !(paramArrayOfObject[0] instanceof NativeArrayBufferView))
      bool = false; 
    return Boolean.valueOf(bool);
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    addIdFunctionProperty((Scriptable)paramIdFunctionObject, "ArrayBuffer", -1, "isView", 1);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    return "byteLength".equals(paramString) ? instanceIdInfo(5, 1) : super.findInstanceIdInfo(paramString);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 5) {
      str = "slice";
      b = 2;
    } else if (i == 11) {
      str = "constructor";
      b = 1;
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
    return i;
  }
  
  public byte[] getBuffer() {
    return this.buffer;
  }
  
  public String getClassName() {
    return "ArrayBuffer";
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt == 1) ? "byteLength" : super.getInstanceIdName(paramInt);
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt == 1) ? ScriptRuntime.wrapInt(this.buffer.length) : super.getInstanceIdValue(paramInt);
  }
  
  public int getLength() {
    return this.buffer.length;
  }
  
  protected int getMaxInstanceId() {
    return 1;
  }
  
  protected void initPrototypeId(int paramInt) {
    String str;
    if (paramInt != 1) {
      if (paramInt == 2) {
        str = "slice";
      } else {
        throw new IllegalArgumentException(String.valueOf(paramInt));
      } 
    } else {
      str = "constructor";
    } 
    initPrototypeMethod("ArrayBuffer", paramInt, str, 1);
  }
  
  public NativeArrayBuffer slice(double paramDouble1, double paramDouble2) {
    byte[] arrayOfByte = this.buffer;
    double d = arrayOfByte.length;
    if (paramDouble2 < 0.0D)
      paramDouble2 = arrayOfByte.length + paramDouble2; 
    int i = ScriptRuntime.toInt32(Math.max(0.0D, Math.min(d, paramDouble2)));
    paramDouble2 = i;
    if (paramDouble1 < 0.0D)
      paramDouble1 = this.buffer.length + paramDouble1; 
    int j = ScriptRuntime.toInt32(Math.min(paramDouble2, Math.max(0.0D, paramDouble1)));
    i -= j;
    NativeArrayBuffer nativeArrayBuffer = new NativeArrayBuffer(i);
    System.arraycopy(this.buffer, j, nativeArrayBuffer.buffer, 0, i);
    return nativeArrayBuffer;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeArrayBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */