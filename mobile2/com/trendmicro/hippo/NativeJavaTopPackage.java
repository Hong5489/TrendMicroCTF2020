package com.trendmicro.hippo;

public class NativeJavaTopPackage extends NativeJavaPackage implements Function, IdFunctionCall {
  private static final Object FTAG;
  
  private static final int Id_getClass = 1;
  
  private static final String[][] commonPackages;
  
  private static final long serialVersionUID = -1455787259477709999L;
  
  static {
    String[] arrayOfString1 = { "java", "math" };
    String[] arrayOfString2 = { "java", "net" };
    String[] arrayOfString3 = { "java", "util", "zip" };
    String[] arrayOfString4 = { "java", "text", "resources" };
    String[] arrayOfString5 = { "java", "applet" };
    String[] arrayOfString6 = { "javax", "swing" };
    commonPackages = new String[][] { { "java", "lang", "reflect" }, { "java", "io" }, arrayOfString1, arrayOfString2, arrayOfString3, arrayOfString4, arrayOfString5, arrayOfString6 };
    FTAG = "JavaTopPackage";
  }
  
  NativeJavaTopPackage(ClassLoader paramClassLoader) {
    super(true, "", paramClassLoader);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    NativeJavaTopPackage nativeJavaTopPackage = new NativeJavaTopPackage(paramContext.getApplicationClassLoader());
    nativeJavaTopPackage.setPrototype(getObjectPrototype(paramScriptable));
    nativeJavaTopPackage.setParentScope(paramScriptable);
    byte b = 0;
    while (b != commonPackages.length) {
      NativeJavaTopPackage nativeJavaTopPackage1 = nativeJavaTopPackage;
      byte b1 = 0;
      while (true) {
        String[][] arrayOfString1 = commonPackages;
        if (b1 != (arrayOfString1[b]).length) {
          NativeJavaPackage nativeJavaPackage = nativeJavaTopPackage1.forcePackage(arrayOfString1[b][b1], paramScriptable);
          b1++;
          continue;
        } 
        b++;
      } 
    } 
    IdFunctionObject idFunctionObject = new IdFunctionObject(nativeJavaTopPackage, FTAG, 1, "getClass", 1, paramScriptable);
    String[] arrayOfString = ScriptRuntime.getTopPackageNames();
    NativeJavaPackage[] arrayOfNativeJavaPackage = new NativeJavaPackage[arrayOfString.length];
    for (b = 0; b < arrayOfString.length; b++)
      arrayOfNativeJavaPackage[b] = (NativeJavaPackage)nativeJavaTopPackage.get(arrayOfString[b], nativeJavaTopPackage); 
    paramScriptable = paramScriptable;
    if (paramBoolean)
      idFunctionObject.sealObject(); 
    idFunctionObject.exportAsScopeProperty();
    paramScriptable.defineProperty("Packages", nativeJavaTopPackage, 2);
    for (b = 0; b < arrayOfString.length; b++)
      paramScriptable.defineProperty(arrayOfString[b], arrayOfNativeJavaPackage[b], 2); 
  }
  
  private Scriptable js_getClass(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length > 0 && paramArrayOfObject[0] instanceof Wrapper) {
      NativeJavaTopPackage nativeJavaTopPackage = this;
      String str = ((Wrapper)paramArrayOfObject[0]).unwrap().getClass().getName();
      int i = 0;
      while (true) {
        String str1;
        int j = str.indexOf('.', i);
        if (j == -1) {
          str1 = str.substring(i);
        } else {
          str1 = str.substring(i, j);
        } 
        Object object = nativeJavaTopPackage.get(str1, nativeJavaTopPackage);
        if (object instanceof Scriptable) {
          object = object;
          if (j == -1)
            return (Scriptable)object; 
          i = j + 1;
          continue;
        } 
        break;
      } 
    } 
    throw Context.reportRuntimeError0("msg.not.java.obj");
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return construct(paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Context context = null;
    paramContext = context;
    if (paramArrayOfObject.length != 0) {
      object = paramArrayOfObject[0];
      Object object1 = object;
      if (object instanceof Wrapper)
        object1 = ((Wrapper)object).unwrap(); 
      object = context;
      if (object1 instanceof ClassLoader)
        object = object1; 
    } 
    if (object == null) {
      Context.reportRuntimeError0("msg.not.classloader");
      return null;
    } 
    Object object = new NativeJavaPackage(true, "", (ClassLoader)object);
    ScriptRuntime.setObjectProtoAndParent((ScriptableObject)object, paramScriptable);
    return (Scriptable)object;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (paramIdFunctionObject.hasTag(FTAG) && paramIdFunctionObject.methodId() == 1)
      return js_getClass(paramContext, paramScriptable1, paramArrayOfObject); 
    throw paramIdFunctionObject.unknown();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaTopPackage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */