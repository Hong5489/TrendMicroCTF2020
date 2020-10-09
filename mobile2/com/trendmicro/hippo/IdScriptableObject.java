package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class IdScriptableObject extends ScriptableObject implements IdFunctionCall {
  private transient PrototypeValues prototypeValues;
  
  public IdScriptableObject() {}
  
  public IdScriptableObject(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    super(paramScriptable1, paramScriptable2);
  }
  
  private ScriptableObject getBuiltInDescriptor(Symbol paramSymbol) {
    Scriptable scriptable1 = getParentScope();
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = this; 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      int i = prototypeValues.findId(paramSymbol);
      if (i != 0)
        return buildDataDescriptor(scriptable2, this.prototypeValues.get(i), this.prototypeValues.getAttributes(i)); 
    } 
    return null;
  }
  
  private ScriptableObject getBuiltInDescriptor(String paramString) {
    Scriptable scriptable1 = getParentScope();
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = this; 
    int i = findInstanceIdInfo(paramString);
    if (i != 0)
      return buildDataDescriptor(scriptable2, getInstanceIdValue(0xFFFF & i), i >>> 16); 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramString);
      if (i != 0)
        return buildDataDescriptor(scriptable2, this.prototypeValues.get(i), this.prototypeValues.getAttributes(i)); 
    } 
    return null;
  }
  
  protected static EcmaError incompatibleCallError(IdFunctionObject paramIdFunctionObject) {
    throw ScriptRuntime.typeError1("msg.incompat.call", paramIdFunctionObject.getFunctionName());
  }
  
  protected static int instanceIdInfo(int paramInt1, int paramInt2) {
    return paramInt1 << 16 | paramInt2;
  }
  
  private IdFunctionObject newIdFunction(Object paramObject, int paramInt1, String paramString, int paramInt2, Scriptable paramScriptable) {
    if (Context.getContext().getLanguageVersion() < 200) {
      paramObject = new IdFunctionObject(this, paramObject, paramInt1, paramString, paramInt2, paramScriptable);
    } else {
      paramObject = new IdFunctionObjectES6(this, paramObject, paramInt1, paramString, paramInt2, paramScriptable);
    } 
    if (isSealed())
      paramObject.sealObject(); 
    return (IdFunctionObject)paramObject;
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    int i = paramObjectInputStream.readInt();
    if (i != 0)
      activatePrototypeMap(i); 
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    int i = 0;
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null)
      i = prototypeValues.getMaxId(); 
    paramObjectOutputStream.writeInt(i);
  }
  
  public final void activatePrototypeMap(int paramInt) {
    // Byte code:
    //   0: new com/trendmicro/hippo/IdScriptableObject$PrototypeValues
    //   3: dup
    //   4: aload_0
    //   5: iload_1
    //   6: invokespecial <init> : (Lcom/trendmicro/hippo/IdScriptableObject;I)V
    //   9: astore_2
    //   10: aload_0
    //   11: monitorenter
    //   12: aload_0
    //   13: getfield prototypeValues : Lcom/trendmicro/hippo/IdScriptableObject$PrototypeValues;
    //   16: ifnonnull -> 27
    //   19: aload_0
    //   20: aload_2
    //   21: putfield prototypeValues : Lcom/trendmicro/hippo/IdScriptableObject$PrototypeValues;
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: new java/lang/IllegalStateException
    //   30: astore_2
    //   31: aload_2
    //   32: invokespecial <init> : ()V
    //   35: aload_2
    //   36: athrow
    //   37: astore_2
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_2
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   12	26	37	finally
    //   27	37	37	finally
    //   38	40	37	finally
  }
  
  protected void addIdFunctionProperty(Scriptable paramScriptable, Object paramObject, int paramInt1, String paramString, int paramInt2) {
    newIdFunction(paramObject, paramInt1, paramString, paramInt2, ScriptableObject.getTopLevelScope(paramScriptable)).addAsProperty(paramScriptable);
  }
  
  protected final Object defaultGet(String paramString) {
    return super.get(paramString, this);
  }
  
  protected final boolean defaultHas(String paramString) {
    return super.has(paramString, this);
  }
  
  protected final void defaultPut(String paramString, Object paramObject) {
    super.put(paramString, this, paramObject);
  }
  
  public void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject) {
    Object object;
    if (paramObject instanceof String) {
      String str = (String)paramObject;
      int i = findInstanceIdInfo(str);
      if (i != 0) {
        int j = 0xFFFF & i;
        if (isAccessorDescriptor(paramScriptableObject)) {
          delete(j);
        } else {
          checkPropertyDefinition(paramScriptableObject);
          checkPropertyChange(str, getOwnPropertyDescriptor(paramContext, paramObject), paramScriptableObject);
          i >>>= 16;
          object = getProperty(paramScriptableObject, "value");
          if (object != NOT_FOUND && (i & 0x1) == 0 && !sameValue(object, getInstanceIdValue(j)))
            setInstanceIdValue(j, object); 
          setAttributes(str, applyDescriptorToAttributeBitset(i, paramScriptableObject));
          return;
        } 
      } 
      PrototypeValues prototypeValues = this.prototypeValues;
      if (prototypeValues != null) {
        i = prototypeValues.findId(str);
        if (i != 0)
          if (isAccessorDescriptor(paramScriptableObject)) {
            this.prototypeValues.delete(i);
          } else {
            checkPropertyDefinition(paramScriptableObject);
            checkPropertyChange(str, getOwnPropertyDescriptor((Context)object, paramObject), paramScriptableObject);
            int j = this.prototypeValues.getAttributes(i);
            object = getProperty(paramScriptableObject, "value");
            if (object != NOT_FOUND && (j & 0x1) == 0 && !sameValue(object, this.prototypeValues.get(i)))
              this.prototypeValues.set(i, this, object); 
            this.prototypeValues.setAttributes(i, applyDescriptorToAttributeBitset(j, paramScriptableObject));
            if (super.has(str, this))
              super.delete(str); 
            return;
          }  
      } 
    } 
    super.defineOwnProperty((Context)object, paramObject, paramScriptableObject);
  }
  
  public void delete(Symbol paramSymbol) {
    int i = findInstanceIdInfo(paramSymbol);
    if (i != 0 && !isSealed()) {
      if ((i >>> 16 & 0x4) != 0) {
        if (Context.getContext().isStrictMode())
          throw ScriptRuntime.typeError0("msg.delete.property.with.configurable.false"); 
      } else {
        setInstanceIdValue(0xFFFF & i, NOT_FOUND);
      } 
      return;
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramSymbol);
      if (i != 0) {
        if (!isSealed())
          this.prototypeValues.delete(i); 
        return;
      } 
    } 
    super.delete(paramSymbol);
  }
  
  public void delete(String paramString) {
    int i = findInstanceIdInfo(paramString);
    if (i != 0 && !isSealed()) {
      if ((i >>> 16 & 0x4) != 0) {
        if (Context.getContext().isStrictMode())
          throw ScriptRuntime.typeError1("msg.delete.property.with.configurable.false", paramString); 
      } else {
        setInstanceIdValue(0xFFFF & i, NOT_FOUND);
      } 
      return;
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramString);
      if (i != 0) {
        if (!isSealed())
          this.prototypeValues.delete(i); 
        return;
      } 
    } 
    super.delete(paramString);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    throw paramIdFunctionObject.unknown();
  }
  
  public final IdFunctionObject exportAsJSClass(int paramInt, Scriptable paramScriptable, boolean paramBoolean) {
    if (paramScriptable != this && paramScriptable != null) {
      setParentScope(paramScriptable);
      setPrototype(getObjectPrototype(paramScriptable));
    } 
    activatePrototypeMap(paramInt);
    paramScriptable = this.prototypeValues.createPrecachedConstructor();
    if (paramBoolean)
      sealObject(); 
    fillConstructorProperties((IdFunctionObject)paramScriptable);
    if (paramBoolean)
      paramScriptable.sealObject(); 
    paramScriptable.exportAsScopeProperty();
    return (IdFunctionObject)paramScriptable;
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {}
  
  protected int findInstanceIdInfo(Symbol paramSymbol) {
    return 0;
  }
  
  protected int findInstanceIdInfo(String paramString) {
    return 0;
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return 0;
  }
  
  protected int findPrototypeId(String paramString) {
    throw new IllegalStateException(paramString);
  }
  
  public Object get(Symbol paramSymbol, Scriptable paramScriptable) {
    Object object = super.get(paramSymbol, paramScriptable);
    if (object != NOT_FOUND)
      return object; 
    int i = findInstanceIdInfo(paramSymbol);
    if (i != 0) {
      object = getInstanceIdValue(0xFFFF & i);
      if (object != NOT_FOUND)
        return object; 
    } 
    object = this.prototypeValues;
    if (object != null) {
      i = object.findId(paramSymbol);
      if (i != 0) {
        Object object1 = this.prototypeValues.get(i);
        if (object1 != NOT_FOUND)
          return object1; 
      } 
    } 
    return NOT_FOUND;
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Object object = super.get(paramString, paramScriptable);
    if (object != NOT_FOUND)
      return object; 
    int i = findInstanceIdInfo(paramString);
    if (i != 0) {
      object = getInstanceIdValue(0xFFFF & i);
      if (object != NOT_FOUND)
        return object; 
    } 
    object = this.prototypeValues;
    if (object != null) {
      i = object.findId(paramString);
      if (i != 0) {
        Object object1 = this.prototypeValues.get(i);
        if (object1 != NOT_FOUND)
          return object1; 
      } 
    } 
    return NOT_FOUND;
  }
  
  public int getAttributes(Symbol paramSymbol) {
    int i = findInstanceIdInfo(paramSymbol);
    if (i != 0)
      return i >>> 16; 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramSymbol);
      if (i != 0)
        return this.prototypeValues.getAttributes(i); 
    } 
    return super.getAttributes(paramSymbol);
  }
  
  public int getAttributes(String paramString) {
    int i = findInstanceIdInfo(paramString);
    if (i != 0)
      return i >>> 16; 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramString);
      if (i != 0)
        return this.prototypeValues.getAttributes(i); 
    } 
    return super.getAttributes(paramString);
  }
  
  Object[] getIds(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: iload_2
    //   3: invokespecial getIds : (ZZ)[Ljava/lang/Object;
    //   6: astore_3
    //   7: aload_0
    //   8: getfield prototypeValues : Lcom/trendmicro/hippo/IdScriptableObject$PrototypeValues;
    //   11: astore #4
    //   13: aload_3
    //   14: astore #5
    //   16: aload #4
    //   18: ifnull -> 31
    //   21: aload #4
    //   23: iload_1
    //   24: iload_2
    //   25: aload_3
    //   26: invokevirtual getNames : (ZZ[Ljava/lang/Object;)[Ljava/lang/Object;
    //   29: astore #5
    //   31: aload_0
    //   32: invokevirtual getMaxInstanceId : ()I
    //   35: istore #6
    //   37: aload #5
    //   39: astore #4
    //   41: iload #6
    //   43: ifeq -> 240
    //   46: aconst_null
    //   47: astore_3
    //   48: iconst_0
    //   49: istore #7
    //   51: iload #6
    //   53: ifeq -> 177
    //   56: aload_0
    //   57: iload #6
    //   59: invokevirtual getInstanceIdName : (I)Ljava/lang/String;
    //   62: astore #8
    //   64: aload_0
    //   65: aload #8
    //   67: invokevirtual findInstanceIdInfo : (Ljava/lang/String;)I
    //   70: istore #9
    //   72: aload_3
    //   73: astore #4
    //   75: iload #7
    //   77: istore #10
    //   79: iload #9
    //   81: ifeq -> 164
    //   84: iload #9
    //   86: bipush #16
    //   88: iushr
    //   89: istore #9
    //   91: iload #9
    //   93: iconst_4
    //   94: iand
    //   95: ifne -> 120
    //   98: getstatic com/trendmicro/hippo/IdScriptableObject.NOT_FOUND : Ljava/lang/Object;
    //   101: aload_0
    //   102: iload #6
    //   104: invokevirtual getInstanceIdValue : (I)Ljava/lang/Object;
    //   107: if_acmpne -> 120
    //   110: aload_3
    //   111: astore #4
    //   113: iload #7
    //   115: istore #10
    //   117: goto -> 164
    //   120: iload_1
    //   121: ifne -> 138
    //   124: aload_3
    //   125: astore #4
    //   127: iload #7
    //   129: istore #10
    //   131: iload #9
    //   133: iconst_2
    //   134: iand
    //   135: ifne -> 164
    //   138: iload #7
    //   140: ifne -> 149
    //   143: iload #6
    //   145: anewarray java/lang/Object
    //   148: astore_3
    //   149: aload_3
    //   150: iload #7
    //   152: aload #8
    //   154: aastore
    //   155: iload #7
    //   157: iconst_1
    //   158: iadd
    //   159: istore #10
    //   161: aload_3
    //   162: astore #4
    //   164: iinc #6, -1
    //   167: aload #4
    //   169: astore_3
    //   170: iload #10
    //   172: istore #7
    //   174: goto -> 51
    //   177: aload #5
    //   179: astore #4
    //   181: iload #7
    //   183: ifeq -> 240
    //   186: aload #5
    //   188: arraylength
    //   189: ifne -> 205
    //   192: aload_3
    //   193: arraylength
    //   194: iload #7
    //   196: if_icmpne -> 205
    //   199: aload_3
    //   200: astore #4
    //   202: goto -> 240
    //   205: aload #5
    //   207: arraylength
    //   208: iload #7
    //   210: iadd
    //   211: anewarray java/lang/Object
    //   214: astore #4
    //   216: aload #5
    //   218: iconst_0
    //   219: aload #4
    //   221: iconst_0
    //   222: aload #5
    //   224: arraylength
    //   225: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   228: aload_3
    //   229: iconst_0
    //   230: aload #4
    //   232: aload #5
    //   234: arraylength
    //   235: iload #7
    //   237: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   240: aload #4
    //   242: areturn
  }
  
  protected String getInstanceIdName(int paramInt) {
    throw new IllegalArgumentException(String.valueOf(paramInt));
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    throw new IllegalStateException(String.valueOf(paramInt));
  }
  
  protected int getMaxInstanceId() {
    return 0;
  }
  
  protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject) {
    ScriptableObject scriptableObject2 = super.getOwnPropertyDescriptor(paramContext, paramObject);
    ScriptableObject scriptableObject1 = scriptableObject2;
    if (scriptableObject2 == null)
      if (paramObject instanceof String) {
        scriptableObject1 = getBuiltInDescriptor((String)paramObject);
      } else {
        scriptableObject1 = scriptableObject2;
        if (ScriptRuntime.isSymbol(paramObject))
          scriptableObject1 = getBuiltInDescriptor(((NativeSymbol)paramObject).getKey()); 
      }  
    return scriptableObject1;
  }
  
  public boolean has(Symbol paramSymbol, Scriptable paramScriptable) {
    int i = findInstanceIdInfo(paramSymbol);
    if (i != 0) {
      boolean bool = true;
      if ((i >>> 16 & 0x4) != 0)
        return true; 
      if (NOT_FOUND == getInstanceIdValue(0xFFFF & i))
        bool = false; 
      return bool;
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramSymbol);
      if (i != 0)
        return this.prototypeValues.has(i); 
    } 
    return super.has(paramSymbol, paramScriptable);
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    int i = findInstanceIdInfo(paramString);
    if (i != 0) {
      boolean bool = true;
      if ((i >>> 16 & 0x4) != 0)
        return true; 
      if (NOT_FOUND == getInstanceIdValue(0xFFFF & i))
        bool = false; 
      return bool;
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramString);
      if (i != 0)
        return this.prototypeValues.has(i); 
    } 
    return super.has(paramString, paramScriptable);
  }
  
  public final boolean hasPrototypeMap() {
    boolean bool;
    if (this.prototypeValues != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final void initPrototypeConstructor(IdFunctionObject paramIdFunctionObject) {
    int i = this.prototypeValues.constructorId;
    if (i != 0) {
      if (paramIdFunctionObject.methodId() == i) {
        if (isSealed())
          paramIdFunctionObject.sealObject(); 
        this.prototypeValues.initValue(i, "constructor", paramIdFunctionObject, 2);
        return;
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalStateException();
  }
  
  protected void initPrototypeId(int paramInt) {
    throw new IllegalStateException(String.valueOf(paramInt));
  }
  
  public final IdFunctionObject initPrototypeMethod(Object paramObject, int paramInt1, Symbol paramSymbol, String paramString, int paramInt2) {
    paramObject = newIdFunction(paramObject, paramInt1, paramString, paramInt2, ScriptableObject.getTopLevelScope(this));
    this.prototypeValues.initValue(paramInt1, paramSymbol, paramObject, 2);
    return (IdFunctionObject)paramObject;
  }
  
  public final IdFunctionObject initPrototypeMethod(Object paramObject, int paramInt1, String paramString, int paramInt2) {
    return initPrototypeMethod(paramObject, paramInt1, paramString, paramString, paramInt2);
  }
  
  public final IdFunctionObject initPrototypeMethod(Object paramObject, int paramInt1, String paramString1, String paramString2, int paramInt2) {
    Scriptable scriptable = ScriptableObject.getTopLevelScope(this);
    if (paramString2 == null)
      paramString2 = paramString1; 
    paramObject = newIdFunction(paramObject, paramInt1, paramString2, paramInt2, scriptable);
    this.prototypeValues.initValue(paramInt1, paramString1, paramObject, 2);
    return (IdFunctionObject)paramObject;
  }
  
  public final void initPrototypeValue(int paramInt1, Symbol paramSymbol, Object paramObject, int paramInt2) {
    this.prototypeValues.initValue(paramInt1, paramSymbol, paramObject, paramInt2);
  }
  
  public final void initPrototypeValue(int paramInt1, String paramString, Object paramObject, int paramInt2) {
    this.prototypeValues.initValue(paramInt1, paramString, paramObject, paramInt2);
  }
  
  public void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject) {
    int i = findInstanceIdInfo(paramSymbol);
    if (i != 0) {
      if (paramScriptable != this || !isSealed()) {
        if ((i >>> 16 & 0x1) == 0)
          if (paramScriptable == this) {
            setInstanceIdValue(0xFFFF & i, paramObject);
          } else {
            ensureSymbolScriptable(paramScriptable).put(paramSymbol, paramScriptable, paramObject);
          }  
        return;
      } 
      throw Context.reportRuntimeError0("msg.modify.sealed");
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramSymbol);
      if (i != 0) {
        if (paramScriptable != this || !isSealed()) {
          this.prototypeValues.set(i, paramScriptable, paramObject);
          return;
        } 
        throw Context.reportRuntimeError0("msg.modify.sealed");
      } 
    } 
    super.put(paramSymbol, paramScriptable, paramObject);
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    int i = findInstanceIdInfo(paramString);
    if (i != 0) {
      if (paramScriptable != this || !isSealed()) {
        if ((i >>> 16 & 0x1) == 0)
          if (paramScriptable == this) {
            setInstanceIdValue(0xFFFF & i, paramObject);
          } else {
            paramScriptable.put(paramString, paramScriptable, paramObject);
          }  
        return;
      } 
      throw Context.reportRuntimeError1("msg.modify.sealed", paramString);
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramString);
      if (i != 0) {
        if (paramScriptable != this || !isSealed()) {
          this.prototypeValues.set(i, paramScriptable, paramObject);
          return;
        } 
        throw Context.reportRuntimeError1("msg.modify.sealed", paramString);
      } 
    } 
    super.put(paramString, paramScriptable, paramObject);
  }
  
  public void setAttributes(String paramString, int paramInt) {
    ScriptableObject.checkValidAttributes(paramInt);
    int i = findInstanceIdInfo(paramString);
    if (i != 0) {
      if (paramInt != i >>> 16)
        setInstanceIdAttributes(0xFFFF & i, paramInt); 
      return;
    } 
    PrototypeValues prototypeValues = this.prototypeValues;
    if (prototypeValues != null) {
      i = prototypeValues.findId(paramString);
      if (i != 0) {
        this.prototypeValues.setAttributes(i, paramInt);
        return;
      } 
    } 
    super.setAttributes(paramString, paramInt);
  }
  
  protected void setInstanceIdAttributes(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Changing attributes not supported for ");
    stringBuilder.append(getClassName());
    stringBuilder.append(" ");
    stringBuilder.append(getInstanceIdName(paramInt1));
    stringBuilder.append(" property");
    throw ScriptRuntime.constructError("InternalError", stringBuilder.toString());
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    throw new IllegalStateException(String.valueOf(paramInt));
  }
  
  private static final class PrototypeValues implements Serializable {
    private static final int NAME_SLOT = 1;
    
    private static final int SLOT_SPAN = 2;
    
    private static final long serialVersionUID = 3038645279153854371L;
    
    private short[] attributeArray;
    
    private IdFunctionObject constructor;
    
    private short constructorAttrs;
    
    int constructorId;
    
    private int maxId;
    
    private IdScriptableObject obj;
    
    private Object[] valueArray;
    
    PrototypeValues(IdScriptableObject param1IdScriptableObject, int param1Int) {
      if (param1IdScriptableObject != null) {
        if (param1Int >= 1) {
          this.obj = param1IdScriptableObject;
          this.maxId = param1Int;
          return;
        } 
        throw new IllegalArgumentException();
      } 
      throw new IllegalArgumentException();
    }
    
    private Object ensureId(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: getfield valueArray : [Ljava/lang/Object;
      //   4: astore_2
      //   5: aload_2
      //   6: astore_3
      //   7: aload_2
      //   8: ifnonnull -> 59
      //   11: aload_0
      //   12: monitorenter
      //   13: aload_0
      //   14: getfield valueArray : [Ljava/lang/Object;
      //   17: astore_2
      //   18: aload_2
      //   19: astore_3
      //   20: aload_2
      //   21: ifnonnull -> 49
      //   24: aload_0
      //   25: getfield maxId : I
      //   28: iconst_2
      //   29: imul
      //   30: anewarray java/lang/Object
      //   33: astore_3
      //   34: aload_0
      //   35: aload_3
      //   36: putfield valueArray : [Ljava/lang/Object;
      //   39: aload_0
      //   40: aload_0
      //   41: getfield maxId : I
      //   44: newarray short
      //   46: putfield attributeArray : [S
      //   49: aload_0
      //   50: monitorexit
      //   51: goto -> 59
      //   54: astore_3
      //   55: aload_0
      //   56: monitorexit
      //   57: aload_3
      //   58: athrow
      //   59: iload_1
      //   60: iconst_1
      //   61: isub
      //   62: iconst_2
      //   63: imul
      //   64: istore #4
      //   66: aload_3
      //   67: iload #4
      //   69: aaload
      //   70: astore #5
      //   72: aload #5
      //   74: astore_2
      //   75: aload #5
      //   77: ifnonnull -> 184
      //   80: aload_0
      //   81: getfield constructorId : I
      //   84: istore #6
      //   86: iload_1
      //   87: iload #6
      //   89: if_icmpne -> 116
      //   92: aload_0
      //   93: iload #6
      //   95: ldc 'constructor'
      //   97: aload_0
      //   98: getfield constructor : Lcom/trendmicro/hippo/IdFunctionObject;
      //   101: aload_0
      //   102: getfield constructorAttrs : S
      //   105: invokespecial initSlot : (ILjava/lang/Object;Ljava/lang/Object;I)V
      //   108: aload_0
      //   109: aconst_null
      //   110: putfield constructor : Lcom/trendmicro/hippo/IdFunctionObject;
      //   113: goto -> 124
      //   116: aload_0
      //   117: getfield obj : Lcom/trendmicro/hippo/IdScriptableObject;
      //   120: iload_1
      //   121: invokevirtual initPrototypeId : (I)V
      //   124: aload_3
      //   125: iload #4
      //   127: aaload
      //   128: astore_2
      //   129: aload_2
      //   130: ifnull -> 136
      //   133: goto -> 184
      //   136: new java/lang/StringBuilder
      //   139: dup
      //   140: invokespecial <init> : ()V
      //   143: astore_3
      //   144: aload_3
      //   145: aload_0
      //   146: getfield obj : Lcom/trendmicro/hippo/IdScriptableObject;
      //   149: invokevirtual getClass : ()Ljava/lang/Class;
      //   152: invokevirtual getName : ()Ljava/lang/String;
      //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   158: pop
      //   159: aload_3
      //   160: ldc '.initPrototypeId(int id) did not initialize id='
      //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   165: pop
      //   166: aload_3
      //   167: iload_1
      //   168: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   171: pop
      //   172: new java/lang/IllegalStateException
      //   175: dup
      //   176: aload_3
      //   177: invokevirtual toString : ()Ljava/lang/String;
      //   180: invokespecial <init> : (Ljava/lang/String;)V
      //   183: athrow
      //   184: aload_2
      //   185: areturn
      // Exception table:
      //   from	to	target	type
      //   13	18	54	finally
      //   24	49	54	finally
      //   49	51	54	finally
      //   55	57	54	finally
    }
    
    private void initSlot(int param1Int1, Object param1Object1, Object param1Object2, int param1Int2) {
      // Byte code:
      //   0: aload_0
      //   1: getfield valueArray : [Ljava/lang/Object;
      //   4: astore #5
      //   6: aload #5
      //   8: ifnull -> 100
      //   11: aload_3
      //   12: ifnonnull -> 22
      //   15: getstatic com/trendmicro/hippo/UniqueTag.NULL_VALUE : Lcom/trendmicro/hippo/UniqueTag;
      //   18: astore_3
      //   19: goto -> 22
      //   22: iload_1
      //   23: iconst_1
      //   24: isub
      //   25: iconst_2
      //   26: imul
      //   27: istore #6
      //   29: aload_0
      //   30: monitorenter
      //   31: aload #5
      //   33: iload #6
      //   35: aaload
      //   36: ifnonnull -> 68
      //   39: aload #5
      //   41: iload #6
      //   43: aload_3
      //   44: aastore
      //   45: aload #5
      //   47: iload #6
      //   49: iconst_1
      //   50: iadd
      //   51: aload_2
      //   52: aastore
      //   53: aload_0
      //   54: getfield attributeArray : [S
      //   57: iload_1
      //   58: iconst_1
      //   59: isub
      //   60: iload #4
      //   62: i2s
      //   63: i2s
      //   64: sastore
      //   65: goto -> 82
      //   68: aload_2
      //   69: aload #5
      //   71: iload #6
      //   73: iconst_1
      //   74: iadd
      //   75: aaload
      //   76: invokevirtual equals : (Ljava/lang/Object;)Z
      //   79: ifeq -> 85
      //   82: aload_0
      //   83: monitorexit
      //   84: return
      //   85: new java/lang/IllegalStateException
      //   88: astore_2
      //   89: aload_2
      //   90: invokespecial <init> : ()V
      //   93: aload_2
      //   94: athrow
      //   95: astore_2
      //   96: aload_0
      //   97: monitorexit
      //   98: aload_2
      //   99: athrow
      //   100: new java/lang/IllegalStateException
      //   103: dup
      //   104: invokespecial <init> : ()V
      //   107: athrow
      // Exception table:
      //   from	to	target	type
      //   53	65	95	finally
      //   68	82	95	finally
      //   82	84	95	finally
      //   85	95	95	finally
      //   96	98	95	finally
    }
    
    final IdFunctionObject createPrecachedConstructor() {
      if (this.constructorId == 0) {
        int i = this.obj.findPrototypeId("constructor");
        this.constructorId = i;
        if (i != 0) {
          this.obj.initPrototypeId(i);
          IdFunctionObject idFunctionObject = this.constructor;
          if (idFunctionObject != null) {
            idFunctionObject.initFunction(this.obj.getClassName(), ScriptableObject.getTopLevelScope(this.obj));
            this.constructor.markAsConstructor(this.obj);
            return this.constructor;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(this.obj.getClass().getName());
          stringBuilder.append(".initPrototypeId() did not initialize id=");
          stringBuilder.append(this.constructorId);
          throw new IllegalStateException(stringBuilder.toString());
        } 
        throw new IllegalStateException("No id for constructor property");
      } 
      throw new IllegalStateException();
    }
    
    final void delete(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: iload_1
      //   2: invokespecial ensureId : (I)Ljava/lang/Object;
      //   5: pop
      //   6: aload_0
      //   7: getfield attributeArray : [S
      //   10: iload_1
      //   11: iconst_1
      //   12: isub
      //   13: saload
      //   14: iconst_4
      //   15: iand
      //   16: ifeq -> 52
      //   19: invokestatic getContext : ()Lcom/trendmicro/hippo/Context;
      //   22: invokevirtual isStrictMode : ()Z
      //   25: ifne -> 31
      //   28: goto -> 79
      //   31: ldc 'msg.delete.property.with.configurable.false'
      //   33: aload_0
      //   34: getfield valueArray : [Ljava/lang/Object;
      //   37: iload_1
      //   38: iconst_1
      //   39: isub
      //   40: iconst_2
      //   41: imul
      //   42: iconst_1
      //   43: iadd
      //   44: aaload
      //   45: checkcast java/lang/String
      //   48: invokestatic typeError1 : (Ljava/lang/String;Ljava/lang/Object;)Lcom/trendmicro/hippo/EcmaError;
      //   51: athrow
      //   52: aload_0
      //   53: monitorenter
      //   54: aload_0
      //   55: getfield valueArray : [Ljava/lang/Object;
      //   58: iload_1
      //   59: iconst_1
      //   60: isub
      //   61: iconst_2
      //   62: imul
      //   63: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
      //   66: aastore
      //   67: aload_0
      //   68: getfield attributeArray : [S
      //   71: iload_1
      //   72: iconst_1
      //   73: isub
      //   74: iconst_0
      //   75: i2s
      //   76: sastore
      //   77: aload_0
      //   78: monitorexit
      //   79: return
      //   80: astore_2
      //   81: aload_0
      //   82: monitorexit
      //   83: aload_2
      //   84: athrow
      // Exception table:
      //   from	to	target	type
      //   54	79	80	finally
      //   81	83	80	finally
    }
    
    final int findId(Symbol param1Symbol) {
      return this.obj.findPrototypeId(param1Symbol);
    }
    
    final int findId(String param1String) {
      return this.obj.findPrototypeId(param1String);
    }
    
    final Object get(int param1Int) {
      Object object1 = ensureId(param1Int);
      Object object2 = object1;
      if (object1 == UniqueTag.NULL_VALUE)
        object2 = null; 
      return object2;
    }
    
    final int getAttributes(int param1Int) {
      ensureId(param1Int);
      return this.attributeArray[param1Int - 1];
    }
    
    final int getMaxId() {
      return this.maxId;
    }
    
    final Object[] getNames(boolean param1Boolean1, boolean param1Boolean2, Object[] param1ArrayOfObject) {
      // Byte code:
      //   0: aconst_null
      //   1: astore #4
      //   3: iconst_0
      //   4: istore #5
      //   6: iconst_1
      //   7: istore #6
      //   9: iload #6
      //   11: aload_0
      //   12: getfield maxId : I
      //   15: if_icmpgt -> 201
      //   18: aload_0
      //   19: iload #6
      //   21: invokespecial ensureId : (I)Ljava/lang/Object;
      //   24: astore #7
      //   26: iload_1
      //   27: ifne -> 52
      //   30: aload #4
      //   32: astore #8
      //   34: iload #5
      //   36: istore #9
      //   38: aload_0
      //   39: getfield attributeArray : [S
      //   42: iload #6
      //   44: iconst_1
      //   45: isub
      //   46: saload
      //   47: iconst_2
      //   48: iand
      //   49: ifne -> 187
      //   52: aload #4
      //   54: astore #8
      //   56: iload #5
      //   58: istore #9
      //   60: aload #7
      //   62: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
      //   65: if_acmpeq -> 187
      //   68: aload_0
      //   69: getfield valueArray : [Ljava/lang/Object;
      //   72: iload #6
      //   74: iconst_1
      //   75: isub
      //   76: iconst_2
      //   77: imul
      //   78: iconst_1
      //   79: iadd
      //   80: aaload
      //   81: astore #7
      //   83: aload #7
      //   85: instanceof java/lang/String
      //   88: ifeq -> 125
      //   91: aload #4
      //   93: astore #8
      //   95: aload #4
      //   97: ifnonnull -> 109
      //   100: aload_0
      //   101: getfield maxId : I
      //   104: anewarray java/lang/Object
      //   107: astore #8
      //   109: aload #8
      //   111: iload #5
      //   113: aload #7
      //   115: aastore
      //   116: iload #5
      //   118: iconst_1
      //   119: iadd
      //   120: istore #9
      //   122: goto -> 187
      //   125: aload #4
      //   127: astore #8
      //   129: iload #5
      //   131: istore #9
      //   133: iload_2
      //   134: ifeq -> 187
      //   137: aload #4
      //   139: astore #8
      //   141: iload #5
      //   143: istore #9
      //   145: aload #7
      //   147: instanceof com/trendmicro/hippo/Symbol
      //   150: ifeq -> 187
      //   153: aload #4
      //   155: astore #8
      //   157: aload #4
      //   159: ifnonnull -> 171
      //   162: aload_0
      //   163: getfield maxId : I
      //   166: anewarray java/lang/Object
      //   169: astore #8
      //   171: aload #8
      //   173: iload #5
      //   175: aload #7
      //   177: invokevirtual toString : ()Ljava/lang/String;
      //   180: aastore
      //   181: iload #5
      //   183: iconst_1
      //   184: iadd
      //   185: istore #9
      //   187: iinc #6, 1
      //   190: aload #8
      //   192: astore #4
      //   194: iload #9
      //   196: istore #5
      //   198: goto -> 9
      //   201: iload #5
      //   203: ifne -> 208
      //   206: aload_3
      //   207: areturn
      //   208: aload_3
      //   209: ifnull -> 259
      //   212: aload_3
      //   213: arraylength
      //   214: ifne -> 220
      //   217: goto -> 259
      //   220: aload_3
      //   221: arraylength
      //   222: istore #9
      //   224: iload #9
      //   226: iload #5
      //   228: iadd
      //   229: anewarray java/lang/Object
      //   232: astore #8
      //   234: aload_3
      //   235: iconst_0
      //   236: aload #8
      //   238: iconst_0
      //   239: iload #9
      //   241: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
      //   244: aload #4
      //   246: iconst_0
      //   247: aload #8
      //   249: iload #9
      //   251: iload #5
      //   253: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
      //   256: aload #8
      //   258: areturn
      //   259: aload #4
      //   261: astore_3
      //   262: iload #5
      //   264: aload #4
      //   266: arraylength
      //   267: if_icmpeq -> 286
      //   270: iload #5
      //   272: anewarray java/lang/Object
      //   275: astore_3
      //   276: aload #4
      //   278: iconst_0
      //   279: aload_3
      //   280: iconst_0
      //   281: iload #5
      //   283: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
      //   286: aload_3
      //   287: areturn
    }
    
    final boolean has(int param1Int) {
      Object[] arrayOfObject = this.valueArray;
      boolean bool = true;
      if (arrayOfObject == null)
        return true; 
      Object object = arrayOfObject[(param1Int - 1) * 2];
      if (object == null)
        return true; 
      if (object == Scriptable.NOT_FOUND)
        bool = false; 
      return bool;
    }
    
    final void initValue(int param1Int1, Symbol param1Symbol, Object param1Object, int param1Int2) {
      if (1 <= param1Int1 && param1Int1 <= this.maxId) {
        if (param1Symbol != null) {
          if (param1Object != Scriptable.NOT_FOUND) {
            ScriptableObject.checkValidAttributes(param1Int2);
            if (this.obj.findPrototypeId(param1Symbol) == param1Int1) {
              if (param1Int1 == this.constructorId) {
                if (param1Object instanceof IdFunctionObject) {
                  this.constructor = (IdFunctionObject)param1Object;
                  this.constructorAttrs = (short)(short)param1Int2;
                  return;
                } 
                throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
              } 
              initSlot(param1Int1, param1Symbol, param1Object, param1Int2);
              return;
            } 
            throw new IllegalArgumentException(param1Symbol.toString());
          } 
          throw new IllegalArgumentException();
        } 
        throw new IllegalArgumentException();
      } 
      throw new IllegalArgumentException();
    }
    
    final void initValue(int param1Int1, String param1String, Object param1Object, int param1Int2) {
      if (1 <= param1Int1 && param1Int1 <= this.maxId) {
        if (param1String != null) {
          if (param1Object != Scriptable.NOT_FOUND) {
            ScriptableObject.checkValidAttributes(param1Int2);
            if (this.obj.findPrototypeId(param1String) == param1Int1) {
              if (param1Int1 == this.constructorId) {
                if (param1Object instanceof IdFunctionObject) {
                  this.constructor = (IdFunctionObject)param1Object;
                  this.constructorAttrs = (short)(short)param1Int2;
                  return;
                } 
                throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
              } 
              initSlot(param1Int1, param1String, param1Object, param1Int2);
              return;
            } 
            throw new IllegalArgumentException(param1String);
          } 
          throw new IllegalArgumentException();
        } 
        throw new IllegalArgumentException();
      } 
      throw new IllegalArgumentException();
    }
    
    final void set(int param1Int, Scriptable param1Scriptable, Object param1Object) {
      // Byte code:
      //   0: aload_3
      //   1: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
      //   4: if_acmpeq -> 132
      //   7: aload_0
      //   8: iload_1
      //   9: invokespecial ensureId : (I)Ljava/lang/Object;
      //   12: pop
      //   13: aload_0
      //   14: getfield attributeArray : [S
      //   17: iload_1
      //   18: iconst_1
      //   19: isub
      //   20: saload
      //   21: iconst_1
      //   22: iand
      //   23: ifne -> 131
      //   26: aload_2
      //   27: aload_0
      //   28: getfield obj : Lcom/trendmicro/hippo/IdScriptableObject;
      //   31: if_acmpne -> 70
      //   34: aload_3
      //   35: ifnonnull -> 45
      //   38: getstatic com/trendmicro/hippo/UniqueTag.NULL_VALUE : Lcom/trendmicro/hippo/UniqueTag;
      //   41: astore_2
      //   42: goto -> 47
      //   45: aload_3
      //   46: astore_2
      //   47: aload_0
      //   48: monitorenter
      //   49: aload_0
      //   50: getfield valueArray : [Ljava/lang/Object;
      //   53: iload_1
      //   54: iconst_1
      //   55: isub
      //   56: iconst_2
      //   57: imul
      //   58: aload_2
      //   59: aastore
      //   60: aload_0
      //   61: monitorexit
      //   62: goto -> 131
      //   65: astore_2
      //   66: aload_0
      //   67: monitorexit
      //   68: aload_2
      //   69: athrow
      //   70: aload_0
      //   71: getfield valueArray : [Ljava/lang/Object;
      //   74: iload_1
      //   75: iconst_1
      //   76: isub
      //   77: iconst_2
      //   78: imul
      //   79: iconst_1
      //   80: iadd
      //   81: aaload
      //   82: astore #4
      //   84: aload #4
      //   86: instanceof com/trendmicro/hippo/Symbol
      //   89: ifeq -> 118
      //   92: aload_2
      //   93: instanceof com/trendmicro/hippo/SymbolScriptable
      //   96: ifeq -> 131
      //   99: aload_2
      //   100: checkcast com/trendmicro/hippo/SymbolScriptable
      //   103: aload #4
      //   105: checkcast com/trendmicro/hippo/Symbol
      //   108: aload_2
      //   109: aload_3
      //   110: invokeinterface put : (Lcom/trendmicro/hippo/Symbol;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
      //   115: goto -> 131
      //   118: aload_2
      //   119: aload #4
      //   121: checkcast java/lang/String
      //   124: aload_2
      //   125: aload_3
      //   126: invokeinterface put : (Ljava/lang/String;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
      //   131: return
      //   132: new java/lang/IllegalArgumentException
      //   135: dup
      //   136: invokespecial <init> : ()V
      //   139: athrow
      // Exception table:
      //   from	to	target	type
      //   49	62	65	finally
      //   66	68	65	finally
    }
    
    final void setAttributes(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_2
      //   1: invokestatic checkValidAttributes : (I)V
      //   4: aload_0
      //   5: iload_1
      //   6: invokespecial ensureId : (I)Ljava/lang/Object;
      //   9: pop
      //   10: aload_0
      //   11: monitorenter
      //   12: aload_0
      //   13: getfield attributeArray : [S
      //   16: iload_1
      //   17: iconst_1
      //   18: isub
      //   19: iload_2
      //   20: i2s
      //   21: i2s
      //   22: sastore
      //   23: aload_0
      //   24: monitorexit
      //   25: return
      //   26: astore_3
      //   27: aload_0
      //   28: monitorexit
      //   29: aload_3
      //   30: athrow
      // Exception table:
      //   from	to	target	type
      //   12	25	26	finally
      //   27	29	26	finally
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/IdScriptableObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */