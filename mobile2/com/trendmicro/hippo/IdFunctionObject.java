package com.trendmicro.hippo;

public class IdFunctionObject extends BaseFunction {
  private static final long serialVersionUID = -5332312783643935019L;
  
  private int arity;
  
  private String functionName;
  
  private final IdFunctionCall idcall;
  
  private final int methodId;
  
  private final Object tag;
  
  private boolean useCallAsConstructor;
  
  public IdFunctionObject(IdFunctionCall paramIdFunctionCall, Object paramObject, int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      this.idcall = paramIdFunctionCall;
      this.tag = paramObject;
      this.methodId = paramInt1;
      this.arity = paramInt2;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public IdFunctionObject(IdFunctionCall paramIdFunctionCall, Object paramObject, int paramInt1, String paramString, int paramInt2, Scriptable paramScriptable) {
    super(paramScriptable, (Scriptable)null);
    if (paramInt2 >= 0) {
      if (paramString != null) {
        this.idcall = paramIdFunctionCall;
        this.tag = paramObject;
        this.methodId = paramInt1;
        this.arity = paramInt2;
        this.functionName = paramString;
        return;
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  static boolean equalObjectGraphs(IdFunctionObject paramIdFunctionObject1, IdFunctionObject paramIdFunctionObject2, EqualObjectGraphs paramEqualObjectGraphs) {
    boolean bool;
    if (paramIdFunctionObject1.methodId == paramIdFunctionObject2.methodId && paramIdFunctionObject1.hasTag(paramIdFunctionObject2.tag) && paramEqualObjectGraphs.equalGraphs(paramIdFunctionObject1.idcall, paramIdFunctionObject2.idcall)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final void addAsProperty(Scriptable paramScriptable) {
    ScriptableObject.defineProperty(paramScriptable, this.functionName, this, 2);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return this.idcall.execIdCall(this, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
  }
  
  public Scriptable createObject(Context paramContext, Scriptable paramScriptable) {
    if (this.useCallAsConstructor)
      return null; 
    throw ScriptRuntime.typeError1("msg.not.ctor", this.functionName);
  }
  
  String decompile(int paramInt1, int paramInt2) {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    if ((paramInt2 & 0x1) != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 == 0) {
      stringBuilder.append("function ");
      stringBuilder.append(getFunctionName());
      stringBuilder.append("() { ");
    } 
    stringBuilder.append("[native code for ");
    IdFunctionCall idFunctionCall = this.idcall;
    if (idFunctionCall instanceof Scriptable) {
      stringBuilder.append(((Scriptable)idFunctionCall).getClassName());
      stringBuilder.append('.');
    } 
    stringBuilder.append(getFunctionName());
    stringBuilder.append(", arity=");
    stringBuilder.append(getArity());
    if (paramInt1 != 0) {
      str = "]\n";
    } else {
      str = "] }\n";
    } 
    stringBuilder.append(str);
    return stringBuilder.toString();
  }
  
  public void exportAsScopeProperty() {
    addAsProperty(getParentScope());
  }
  
  public int getArity() {
    return this.arity;
  }
  
  public String getFunctionName() {
    String str1 = this.functionName;
    String str2 = str1;
    if (str1 == null)
      str2 = ""; 
    return str2;
  }
  
  public int getLength() {
    return getArity();
  }
  
  public Scriptable getPrototype() {
    Scriptable scriptable1 = super.getPrototype();
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null) {
      scriptable2 = getFunctionPrototype(getParentScope());
      setPrototype(scriptable2);
    } 
    return scriptable2;
  }
  
  public Object getTag() {
    return this.tag;
  }
  
  public final boolean hasTag(Object paramObject) {
    boolean bool;
    Object object = this.tag;
    if (paramObject == null) {
      if (object == null) {
        bool = true;
      } else {
        bool = false;
      } 
    } else {
      bool = paramObject.equals(object);
    } 
    return bool;
  }
  
  public void initFunction(String paramString, Scriptable paramScriptable) {
    if (paramString != null) {
      if (paramScriptable != null) {
        this.functionName = paramString;
        setParentScope(paramScriptable);
        return;
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  public final void markAsConstructor(Scriptable paramScriptable) {
    this.useCallAsConstructor = true;
    setImmunePrototypeProperty(paramScriptable);
  }
  
  public final int methodId() {
    return this.methodId;
  }
  
  public final RuntimeException unknown() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("BAD FUNCTION ID=");
    stringBuilder.append(this.methodId);
    stringBuilder.append(" MASTER=");
    stringBuilder.append(this.idcall);
    return new IllegalArgumentException(stringBuilder.toString());
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/IdFunctionObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */