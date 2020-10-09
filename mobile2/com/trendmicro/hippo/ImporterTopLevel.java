package com.trendmicro.hippo;

public class ImporterTopLevel extends TopLevel {
  private static final Object IMPORTER_TAG = "Importer";
  
  private static final int Id_constructor = 1;
  
  private static final int Id_importClass = 2;
  
  private static final int Id_importPackage = 3;
  
  private static final int MAX_PROTOTYPE_ID = 3;
  
  private static final long serialVersionUID = -9095380847465315412L;
  
  private ObjArray importedPackages = new ObjArray();
  
  private boolean topScopeFlag;
  
  public ImporterTopLevel() {}
  
  public ImporterTopLevel(Context paramContext) {
    this(paramContext, false);
  }
  
  public ImporterTopLevel(Context paramContext, boolean paramBoolean) {
    initStandardObjects(paramContext, paramBoolean);
  }
  
  private Object getPackageProperty(String paramString, Scriptable paramScriptable) {
    Object object = NOT_FOUND;
    synchronized (this.importedPackages) {
      Object[] arrayOfObject = this.importedPackages.toArray();
      byte b = 0;
      while (b < arrayOfObject.length) {
        Object object1 = ((NativeJavaPackage)arrayOfObject[b]).getPkgProperty(paramString, paramScriptable, false);
        null = object;
        if (object1 != null) {
          null = object;
          if (!(object1 instanceof NativeJavaPackage))
            if (object == NOT_FOUND) {
              null = object1;
            } else {
              throw Context.reportRuntimeError2("msg.ambig.import", object.toString(), object1.toString());
            }  
        } 
        b++;
        object = null;
      } 
      return object;
    } 
  }
  
  private void importClass(NativeJavaClass paramNativeJavaClass) {
    String str1 = paramNativeJavaClass.getClassObject().getName();
    String str2 = str1.substring(str1.lastIndexOf('.') + 1);
    Object object = get(str2, this);
    if (object == NOT_FOUND || object == paramNativeJavaClass) {
      put(str2, this, paramNativeJavaClass);
      return;
    } 
    throw Context.reportRuntimeError1("msg.prop.defined", str2);
  }
  
  private void importPackage(NativeJavaPackage paramNativeJavaPackage) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 5
    //   4: return
    //   5: aload_0
    //   6: getfield importedPackages : Lcom/trendmicro/hippo/ObjArray;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_0
    //   13: istore_3
    //   14: iload_3
    //   15: aload_0
    //   16: getfield importedPackages : Lcom/trendmicro/hippo/ObjArray;
    //   19: invokevirtual size : ()I
    //   22: if_icmpeq -> 49
    //   25: aload_1
    //   26: aload_0
    //   27: getfield importedPackages : Lcom/trendmicro/hippo/ObjArray;
    //   30: iload_3
    //   31: invokevirtual get : (I)Ljava/lang/Object;
    //   34: invokevirtual equals : (Ljava/lang/Object;)Z
    //   37: ifeq -> 43
    //   40: aload_2
    //   41: monitorexit
    //   42: return
    //   43: iinc #3, 1
    //   46: goto -> 14
    //   49: aload_0
    //   50: getfield importedPackages : Lcom/trendmicro/hippo/ObjArray;
    //   53: aload_1
    //   54: invokevirtual add : (Ljava/lang/Object;)V
    //   57: aload_2
    //   58: monitorexit
    //   59: return
    //   60: astore_1
    //   61: aload_2
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   14	42	60	finally
    //   49	59	60	finally
    //   61	63	60	finally
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new ImporterTopLevel()).exportAsJSClass(3, paramScriptable, paramBoolean);
  }
  
  private Object js_construct(Scriptable paramScriptable, Object[] paramArrayOfObject) {
    ImporterTopLevel importerTopLevel = new ImporterTopLevel();
    for (byte b = 0; b != paramArrayOfObject.length; b++) {
      Object object = paramArrayOfObject[b];
      if (object instanceof NativeJavaClass) {
        importerTopLevel.importClass((NativeJavaClass)object);
      } else if (object instanceof NativeJavaPackage) {
        importerTopLevel.importPackage((NativeJavaPackage)object);
      } else {
        throw Context.reportRuntimeError1("msg.not.class.not.pkg", Context.toString(object));
      } 
    } 
    importerTopLevel.setParentScope(paramScriptable);
    importerTopLevel.setPrototype(this);
    return importerTopLevel;
  }
  
  private Object js_importClass(Object[] paramArrayOfObject) {
    byte b = 0;
    while (b != paramArrayOfObject.length) {
      Object object = paramArrayOfObject[b];
      if (object instanceof NativeJavaClass) {
        importClass((NativeJavaClass)object);
        b++;
        continue;
      } 
      throw Context.reportRuntimeError1("msg.not.class", Context.toString(object));
    } 
    return Undefined.instance;
  }
  
  private Object js_importPackage(Object[] paramArrayOfObject) {
    byte b = 0;
    while (b != paramArrayOfObject.length) {
      Object object = paramArrayOfObject[b];
      if (object instanceof NativeJavaPackage) {
        importPackage((NativeJavaPackage)object);
        b++;
        continue;
      } 
      throw Context.reportRuntimeError1("msg.not.pkg", Context.toString(object));
    } 
    return Undefined.instance;
  }
  
  private ImporterTopLevel realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (this.topScopeFlag)
      return this; 
    if (paramScriptable instanceof ImporterTopLevel)
      return (ImporterTopLevel)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(IMPORTER_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != 1) {
      if (i != 2) {
        if (i == 3)
          return realThis(paramScriptable2, paramIdFunctionObject).js_importPackage(paramArrayOfObject); 
        throw new IllegalArgumentException(String.valueOf(i));
      } 
      return realThis(paramScriptable2, paramIdFunctionObject).js_importClass(paramArrayOfObject);
    } 
    return js_construct(paramScriptable1, paramArrayOfObject);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 11) {
      i = paramString.charAt(0);
      if (i == 99) {
        str = "constructor";
        b = 1;
      } else if (i == 105) {
        str = "importClass";
        b = 2;
      } 
    } else if (i == 13) {
      str = "importPackage";
      b = 3;
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
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Object object = super.get(paramString, paramScriptable);
    return (object != NOT_FOUND) ? object : getPackageProperty(paramString, paramScriptable);
  }
  
  public String getClassName() {
    String str;
    if (this.topScopeFlag) {
      str = "global";
    } else {
      str = "JavaImporter";
    } 
    return str;
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    return (super.has(paramString, paramScriptable) || getPackageProperty(paramString, paramScriptable) != NOT_FOUND);
  }
  
  @Deprecated
  public void importPackage(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    js_importPackage(paramArrayOfObject);
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          bool = true;
          str = "importPackage";
        } else {
          throw new IllegalArgumentException(String.valueOf(paramInt));
        } 
      } else {
        bool = true;
        str = "importClass";
      } 
    } else {
      bool = false;
      str = "constructor";
    } 
    initPrototypeMethod(IMPORTER_TAG, paramInt, str, bool);
  }
  
  public void initStandardObjects(Context paramContext, boolean paramBoolean) {
    paramContext.initStandardObjects(this, paramBoolean);
    this.topScopeFlag = true;
    IdFunctionObject idFunctionObject = exportAsJSClass(3, this, false);
    if (paramBoolean)
      idFunctionObject.sealObject(); 
    delete("constructor");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ImporterTopLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */