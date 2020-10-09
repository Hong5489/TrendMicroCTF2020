package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdScriptableObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Undefined;

public abstract class NativeArrayBufferView extends IdScriptableObject {
  private static final int Id_buffer = 1;
  
  private static final int Id_byteLength = 3;
  
  private static final int Id_byteOffset = 2;
  
  protected static final int MAX_INSTANCE_ID = 3;
  
  private static final long serialVersionUID = 6884475582973958419L;
  
  private static Boolean useLittleEndian = null;
  
  protected final NativeArrayBuffer arrayBuffer;
  
  protected final int byteLength;
  
  protected final int offset;
  
  public NativeArrayBufferView() {
    this.arrayBuffer = NativeArrayBuffer.EMPTY_BUFFER;
    this.offset = 0;
    this.byteLength = 0;
  }
  
  protected NativeArrayBufferView(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    this.offset = paramInt1;
    this.byteLength = paramInt2;
    this.arrayBuffer = paramNativeArrayBuffer;
  }
  
  protected static boolean isArg(Object[] paramArrayOfObject, int paramInt) {
    boolean bool;
    if (paramArrayOfObject.length > paramInt && !Undefined.instance.equals(paramArrayOfObject[paramInt])) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected static boolean useLittleEndian() {
    if (useLittleEndian == null) {
      Context context = Context.getCurrentContext();
      if (context == null)
        return false; 
      useLittleEndian = Boolean.valueOf(context.hasFeature(19));
    } 
    return useLittleEndian.booleanValue();
  }
  
  protected int findInstanceIdInfo(String paramString) {
    String str2;
    byte b2;
    byte b1 = 0;
    String str1 = null;
    int i = paramString.length();
    if (i == 6) {
      str2 = "buffer";
      b2 = 1;
    } else {
      b2 = b1;
      str2 = str1;
      if (i == 10) {
        i = paramString.charAt(4);
        if (i == 76) {
          str2 = "byteLength";
          b2 = 3;
        } else {
          b2 = b1;
          str2 = str1;
          if (i == 79) {
            str2 = "byteOffset";
            b2 = 2;
          } 
        } 
      } 
    } 
    b1 = b2;
    if (str2 != null) {
      b1 = b2;
      if (str2 != paramString) {
        b1 = b2;
        if (!str2.equals(paramString))
          b1 = 0; 
      } 
    } 
    return (b1 == 0) ? super.findInstanceIdInfo(paramString) : instanceIdInfo(5, b1);
  }
  
  public NativeArrayBuffer getBuffer() {
    return this.arrayBuffer;
  }
  
  public int getByteLength() {
    return this.byteLength;
  }
  
  public int getByteOffset() {
    return this.offset;
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? super.getInstanceIdName(paramInt) : "byteLength") : "byteOffset") : "buffer";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? super.getInstanceIdValue(paramInt) : ScriptRuntime.wrapInt(this.byteLength)) : ScriptRuntime.wrapInt(this.offset)) : this.arrayBuffer;
  }
  
  protected int getMaxInstanceId() {
    return 3;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeArrayBufferView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */