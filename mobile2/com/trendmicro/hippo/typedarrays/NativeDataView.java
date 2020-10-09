package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

public class NativeDataView extends NativeArrayBufferView {
  public static final String CLASS_NAME = "DataView";
  
  private static final int Id_constructor = 1;
  
  private static final int Id_getFloat32 = 8;
  
  private static final int Id_getFloat64 = 9;
  
  private static final int Id_getInt16 = 4;
  
  private static final int Id_getInt32 = 6;
  
  private static final int Id_getInt8 = 2;
  
  private static final int Id_getUint16 = 5;
  
  private static final int Id_getUint32 = 7;
  
  private static final int Id_getUint8 = 3;
  
  private static final int Id_setFloat32 = 16;
  
  private static final int Id_setFloat64 = 17;
  
  private static final int Id_setInt16 = 12;
  
  private static final int Id_setInt32 = 14;
  
  private static final int Id_setInt8 = 10;
  
  private static final int Id_setUint16 = 13;
  
  private static final int Id_setUint32 = 15;
  
  private static final int Id_setUint8 = 11;
  
  private static final int MAX_PROTOTYPE_ID = 17;
  
  private static final long serialVersionUID = 1427967607557438968L;
  
  public NativeDataView() {}
  
  public NativeDataView(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2) {
    super(paramNativeArrayBuffer, paramInt1, paramInt2);
  }
  
  private int determinePos(Object[] paramArrayOfObject) {
    if (isArg(paramArrayOfObject, 0)) {
      double d = ScriptRuntime.toNumber(paramArrayOfObject[0]);
      if (!Double.isInfinite(d))
        return ScriptRuntime.toInt32(d); 
      throw ScriptRuntime.constructError("RangeError", "offset out of range");
    } 
    return 0;
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeDataView()).exportAsJSClass(17, paramScriptable, paramBoolean);
  }
  
  private NativeDataView js_constructor(Object[] paramArrayOfObject) {
    if (isArg(paramArrayOfObject, 0) && paramArrayOfObject[0] instanceof NativeArrayBuffer) {
      byte b;
      int i;
      NativeArrayBuffer nativeArrayBuffer = (NativeArrayBuffer)paramArrayOfObject[0];
      if (isArg(paramArrayOfObject, 1)) {
        double d = ScriptRuntime.toNumber(paramArrayOfObject[1]);
        if (!Double.isInfinite(d)) {
          b = ScriptRuntime.toInt32(d);
        } else {
          throw ScriptRuntime.constructError("RangeError", "offset out of range");
        } 
      } else {
        b = 0;
      } 
      if (isArg(paramArrayOfObject, 2)) {
        double d = ScriptRuntime.toNumber(paramArrayOfObject[2]);
        if (!Double.isInfinite(d)) {
          i = ScriptRuntime.toInt32(d);
        } else {
          throw ScriptRuntime.constructError("RangeError", "offset out of range");
        } 
      } else {
        i = nativeArrayBuffer.getLength() - b;
      } 
      if (i >= 0) {
        if (b >= 0 && b + i <= nativeArrayBuffer.getLength())
          return new NativeDataView(nativeArrayBuffer, b, i); 
        throw ScriptRuntime.constructError("RangeError", "offset out of range");
      } 
      throw ScriptRuntime.constructError("RangeError", "length out of range");
    } 
    throw ScriptRuntime.constructError("TypeError", "Missing parameters");
  }
  
  private Object js_getFloat(int paramInt, Object[] paramArrayOfObject) {
    int i = determinePos(paramArrayOfObject);
    rangeCheck(i, paramInt);
    boolean bool = true;
    if (!isArg(paramArrayOfObject, 1) || paramInt <= 1 || !ScriptRuntime.toBoolean(paramArrayOfObject[1]))
      bool = false; 
    if (paramInt != 4) {
      if (paramInt == 8)
        return ByteIo.readFloat64(this.arrayBuffer.buffer, this.offset + i, bool); 
      throw new AssertionError();
    } 
    return ByteIo.readFloat32(this.arrayBuffer.buffer, this.offset + i, bool);
  }
  
  private Object js_getInt(int paramInt, boolean paramBoolean, Object[] paramArrayOfObject) {
    Object object;
    boolean bool;
    int i = determinePos(paramArrayOfObject);
    rangeCheck(i, paramInt);
    if (isArg(paramArrayOfObject, 1) && paramInt > 1 && ScriptRuntime.toBoolean(paramArrayOfObject[1])) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 4) {
          if (paramBoolean) {
            object = ByteIo.readInt32(this.arrayBuffer.buffer, this.offset + i, bool);
          } else {
            object = ByteIo.readUint32(this.arrayBuffer.buffer, this.offset + i, bool);
          } 
          return object;
        } 
        throw new AssertionError();
      } 
      if (paramBoolean) {
        object = ByteIo.readInt16(this.arrayBuffer.buffer, this.offset + i, bool);
      } else {
        object = ByteIo.readUint16(this.arrayBuffer.buffer, this.offset + i, bool);
      } 
      return object;
    } 
    if (paramBoolean) {
      object = ByteIo.readInt8(this.arrayBuffer.buffer, this.offset + i);
    } else {
      object = ByteIo.readUint8(this.arrayBuffer.buffer, this.offset + i);
    } 
    return object;
  }
  
  private void js_setFloat(int paramInt, Object[] paramArrayOfObject) {
    int i = determinePos(paramArrayOfObject);
    if (i >= 0) {
      boolean bool;
      if (isArg(paramArrayOfObject, 2) && paramInt > 1 && ScriptRuntime.toBoolean(paramArrayOfObject[2])) {
        bool = true;
      } else {
        bool = false;
      } 
      double d = Double.NaN;
      if (paramArrayOfObject.length > 1)
        d = ScriptRuntime.toNumber(paramArrayOfObject[1]); 
      if (i + paramInt <= this.byteLength) {
        if (paramInt != 4) {
          if (paramInt == 8) {
            ByteIo.writeFloat64(this.arrayBuffer.buffer, this.offset + i, d, bool);
          } else {
            throw new AssertionError();
          } 
        } else {
          ByteIo.writeFloat32(this.arrayBuffer.buffer, this.offset + i, d, bool);
        } 
        return;
      } 
      throw ScriptRuntime.constructError("RangeError", "offset out of range");
    } 
    throw ScriptRuntime.constructError("RangeError", "offset out of range");
  }
  
  private void js_setInt(int paramInt, boolean paramBoolean, Object[] paramArrayOfObject) {
    int i = determinePos(paramArrayOfObject);
    if (i >= 0) {
      boolean bool;
      if (isArg(paramArrayOfObject, 2) && paramInt > 1 && ScriptRuntime.toBoolean(paramArrayOfObject[2])) {
        bool = true;
      } else {
        bool = false;
      } 
      Object object = Integer.valueOf(0);
      if (paramArrayOfObject.length > 1)
        object = paramArrayOfObject[1]; 
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt == 4) {
            if (paramBoolean) {
              int j = Conversions.toInt32(object);
              if (i + paramInt <= this.byteLength) {
                ByteIo.writeInt32(this.arrayBuffer.buffer, this.offset + i, j, bool);
              } else {
                throw ScriptRuntime.constructError("RangeError", "offset out of range");
              } 
            } else {
              long l = Conversions.toUint32(object);
              if (i + paramInt <= this.byteLength) {
                ByteIo.writeUint32(this.arrayBuffer.buffer, this.offset + i, l, bool);
              } else {
                throw ScriptRuntime.constructError("RangeError", "offset out of range");
              } 
            } 
          } else {
            throw new AssertionError();
          } 
        } else if (paramBoolean) {
          int j = Conversions.toInt16(object);
          if (i + paramInt <= this.byteLength) {
            ByteIo.writeInt16(this.arrayBuffer.buffer, this.offset + i, j, bool);
          } else {
            throw ScriptRuntime.constructError("RangeError", "offset out of range");
          } 
        } else {
          int j = Conversions.toUint16(object);
          if (i + paramInt <= this.byteLength) {
            ByteIo.writeUint16(this.arrayBuffer.buffer, this.offset + i, j, bool);
          } else {
            throw ScriptRuntime.constructError("RangeError", "offset out of range");
          } 
        } 
      } else if (paramBoolean) {
        int j = Conversions.toInt8(object);
        if (i + paramInt <= this.byteLength) {
          ByteIo.writeInt8(this.arrayBuffer.buffer, this.offset + i, j);
        } else {
          throw ScriptRuntime.constructError("RangeError", "offset out of range");
        } 
      } else {
        int j = Conversions.toUint8(object);
        if (i + paramInt <= this.byteLength) {
          ByteIo.writeUint8(this.arrayBuffer.buffer, this.offset + i, j);
          return;
        } 
        throw ScriptRuntime.constructError("RangeError", "offset out of range");
      } 
      return;
    } 
    throw ScriptRuntime.constructError("RangeError", "offset out of range");
  }
  
  private void rangeCheck(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0 && paramInt1 + paramInt2 <= this.byteLength)
      return; 
    throw ScriptRuntime.constructError("RangeError", "offset out of range");
  }
  
  private static NativeDataView realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeDataView)
      return (NativeDataView)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(getClassName()))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    switch (i) {
      default:
        throw new IllegalArgumentException(String.valueOf(i));
      case 17:
        realThis(paramScriptable2, paramIdFunctionObject).js_setFloat(8, paramArrayOfObject);
        return Undefined.instance;
      case 16:
        realThis(paramScriptable2, paramIdFunctionObject).js_setFloat(4, paramArrayOfObject);
        return Undefined.instance;
      case 15:
        realThis(paramScriptable2, paramIdFunctionObject).js_setInt(4, false, paramArrayOfObject);
        return Undefined.instance;
      case 14:
        realThis(paramScriptable2, paramIdFunctionObject).js_setInt(4, true, paramArrayOfObject);
        return Undefined.instance;
      case 13:
        realThis(paramScriptable2, paramIdFunctionObject).js_setInt(2, false, paramArrayOfObject);
        return Undefined.instance;
      case 12:
        realThis(paramScriptable2, paramIdFunctionObject).js_setInt(2, true, paramArrayOfObject);
        return Undefined.instance;
      case 11:
        realThis(paramScriptable2, paramIdFunctionObject).js_setInt(1, false, paramArrayOfObject);
        return Undefined.instance;
      case 10:
        realThis(paramScriptable2, paramIdFunctionObject).js_setInt(1, true, paramArrayOfObject);
        return Undefined.instance;
      case 9:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getFloat(8, paramArrayOfObject);
      case 8:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getFloat(4, paramArrayOfObject);
      case 7:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getInt(4, false, paramArrayOfObject);
      case 6:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getInt(4, true, paramArrayOfObject);
      case 5:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getInt(2, false, paramArrayOfObject);
      case 4:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getInt(2, true, paramArrayOfObject);
      case 3:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getInt(1, false, paramArrayOfObject);
      case 2:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getInt(1, true, paramArrayOfObject);
      case 1:
        break;
    } 
    return js_constructor(paramArrayOfObject);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b2;
    String str2;
    char c;
    byte b1 = 0;
    String str1 = null;
    switch (paramString.length()) {
      default:
        b2 = b1;
        str2 = str1;
        break;
      case 11:
        str2 = "constructor";
        b2 = 1;
        break;
      case 10:
        c = paramString.charAt(0);
        if (c == 'g') {
          c = paramString.charAt(9);
          if (c == '2') {
            str2 = "getFloat32";
            b2 = 8;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == '4') {
            str2 = "getFloat64";
            b2 = 9;
          } 
          break;
        } 
        b2 = b1;
        str2 = str1;
        if (c == 's') {
          c = paramString.charAt(9);
          if (c == '2') {
            str2 = "setFloat32";
            b2 = 16;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == '4') {
            str2 = "setFloat64";
            b2 = 17;
          } 
        } 
        break;
      case 9:
        c = paramString.charAt(0);
        if (c == 'g') {
          c = paramString.charAt(8);
          if (c == '2') {
            str2 = "getUint32";
            b2 = 7;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == '6') {
            str2 = "getUint16";
            b2 = 5;
          } 
          break;
        } 
        b2 = b1;
        str2 = str1;
        if (c == 's') {
          c = paramString.charAt(8);
          if (c == '2') {
            str2 = "setUint32";
            b2 = 15;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == '6') {
            str2 = "setUint16";
            b2 = 13;
          } 
        } 
        break;
      case 8:
        c = paramString.charAt(6);
        if (c == '1') {
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getInt16";
            b2 = 4;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == 's') {
            str2 = "setInt16";
            b2 = 12;
          } 
          break;
        } 
        if (c == '3') {
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getInt32";
            b2 = 6;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == 's') {
            str2 = "setInt32";
            b2 = 14;
          } 
          break;
        } 
        b2 = b1;
        str2 = str1;
        if (c == 't') {
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getUint8";
            b2 = 3;
            break;
          } 
          b2 = b1;
          str2 = str1;
          if (c == 's') {
            str2 = "setUint8";
            b2 = 11;
          } 
        } 
        break;
      case 7:
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getInt8";
          b2 = 2;
          break;
        } 
        b2 = b1;
        str2 = str1;
        if (c == 's') {
          str2 = "setInt8";
          b2 = 10;
        } 
        break;
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
    return b1;
  }
  
  public String getClassName() {
    return "DataView";
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 17:
        b = 2;
        str = "setFloat64";
        break;
      case 16:
        b = 2;
        str = "setFloat32";
        break;
      case 15:
        b = 2;
        str = "setUint32";
        break;
      case 14:
        b = 2;
        str = "setInt32";
        break;
      case 13:
        b = 2;
        str = "setUint16";
        break;
      case 12:
        b = 2;
        str = "setInt16";
        break;
      case 11:
        b = 2;
        str = "setUint8";
        break;
      case 10:
        b = 2;
        str = "setInt8";
        break;
      case 9:
        b = 1;
        str = "getFloat64";
        break;
      case 8:
        b = 1;
        str = "getFloat32";
        break;
      case 7:
        b = 1;
        str = "getUint32";
        break;
      case 6:
        b = 1;
        str = "getInt32";
        break;
      case 5:
        b = 1;
        str = "getUint16";
        break;
      case 4:
        b = 1;
        str = "getInt16";
        break;
      case 3:
        b = 1;
        str = "getUint8";
        break;
      case 2:
        b = 1;
        str = "getInt8";
        break;
      case 1:
        b = 3;
        str = "constructor";
        break;
    } 
    initPrototypeMethod(getClassName(), paramInt, str, b);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeDataView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */