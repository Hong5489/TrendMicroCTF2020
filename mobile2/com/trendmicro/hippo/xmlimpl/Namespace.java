package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.IdScriptableObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

class Namespace extends IdScriptableObject {
  private static final int Id_constructor = 1;
  
  private static final int Id_prefix = 1;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  private static final int Id_uri = 2;
  
  private static final int MAX_INSTANCE_ID = 2;
  
  private static final int MAX_PROTOTYPE_ID = 3;
  
  private static final Object NAMESPACE_TAG = "Namespace";
  
  static final long serialVersionUID = -5765755238131301744L;
  
  private XmlNode.Namespace ns;
  
  private Namespace prototype;
  
  private Namespace constructNamespace() {
    return newNamespace("", "");
  }
  
  private Namespace constructNamespace(Object paramObject1, Object paramObject2) {
    if (paramObject2 instanceof QName) {
      QName qName = (QName)paramObject2;
      String str = qName.uri();
      paramObject2 = str;
      if (str == null)
        paramObject2 = qName.toString(); 
    } else {
      paramObject2 = ScriptRuntime.toString(paramObject2);
    } 
    if (paramObject2.length() == 0) {
      if (paramObject1 == Undefined.instance) {
        paramObject1 = "";
      } else {
        paramObject1 = ScriptRuntime.toString(paramObject1);
        if (paramObject1.length() != 0) {
          paramObject2 = new StringBuilder();
          paramObject2.append("Illegal prefix '");
          paramObject2.append((String)paramObject1);
          paramObject2.append("' for 'no namespace'.");
          throw ScriptRuntime.typeError(paramObject2.toString());
        } 
      } 
    } else if (paramObject1 == Undefined.instance) {
      paramObject1 = "";
    } else if (!XMLName.accept(paramObject1)) {
      paramObject1 = "";
    } else {
      paramObject1 = ScriptRuntime.toString(paramObject1);
    } 
    return newNamespace((String)paramObject1, (String)paramObject2);
  }
  
  static Namespace create(Scriptable paramScriptable, Namespace paramNamespace, XmlNode.Namespace paramNamespace1) {
    Namespace namespace = new Namespace();
    namespace.setParentScope(paramScriptable);
    namespace.prototype = paramNamespace;
    namespace.setPrototype((Scriptable)paramNamespace);
    namespace.ns = paramNamespace1;
    return namespace;
  }
  
  private boolean equals(Namespace paramNamespace) {
    return uri().equals(paramNamespace.uri());
  }
  
  private Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject) {
    return (!paramBoolean && paramArrayOfObject.length == 1) ? castToNamespace(paramArrayOfObject[0]) : ((paramArrayOfObject.length == 0) ? constructNamespace() : ((paramArrayOfObject.length == 1) ? constructNamespace(paramArrayOfObject[0]) : constructNamespace(paramArrayOfObject[0], paramArrayOfObject[1])));
  }
  
  private String js_toSource() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    toSourceImpl(this.ns.getPrefix(), this.ns.getUri(), stringBuilder);
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
  
  private Namespace realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof Namespace)
      return (Namespace)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  static void toSourceImpl(String paramString1, String paramString2, StringBuilder paramStringBuilder) {
    paramStringBuilder.append("new Namespace(");
    if (paramString2.length() == 0) {
      if (!"".equals(paramString1))
        throw new IllegalArgumentException(paramString1); 
    } else {
      paramStringBuilder.append('\'');
      if (paramString1 != null) {
        paramStringBuilder.append(ScriptRuntime.escapeString(paramString1, '\''));
        paramStringBuilder.append("', '");
      } 
      paramStringBuilder.append(ScriptRuntime.escapeString(paramString2, '\''));
      paramStringBuilder.append('\'');
    } 
    paramStringBuilder.append(')');
  }
  
  Namespace castToNamespace(Object paramObject) {
    return (paramObject instanceof Namespace) ? (Namespace)paramObject : constructNamespace(paramObject);
  }
  
  Namespace constructNamespace(Object paramObject) {
    String str;
    if (paramObject instanceof Namespace) {
      Namespace namespace = (Namespace)paramObject;
      paramObject = namespace.prefix();
      str = namespace.uri();
    } else if (paramObject instanceof QName) {
      paramObject = paramObject;
      str = paramObject.uri();
      if (str != null) {
        paramObject = paramObject.prefix();
      } else {
        str = paramObject.toString();
        paramObject = null;
      } 
    } else {
      str = ScriptRuntime.toString(paramObject);
      if (str.length() == 0) {
        paramObject = "";
      } else {
        paramObject = null;
      } 
    } 
    return newNamespace((String)paramObject, str);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof Namespace) ? false : equals((Namespace)paramObject);
  }
  
  protected Object equivalentValues(Object paramObject) {
    if (!(paramObject instanceof Namespace))
      return Scriptable.NOT_FOUND; 
    if (equals((Namespace)paramObject)) {
      paramObject = Boolean.TRUE;
    } else {
      paramObject = Boolean.FALSE;
    } 
    return paramObject;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(NAMESPACE_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    boolean bool = true;
    if (i != 1) {
      if (i != 2) {
        if (i == 3)
          return realThis(paramScriptable2, paramIdFunctionObject).js_toSource(); 
        throw new IllegalArgumentException(String.valueOf(i));
      } 
      return realThis(paramScriptable2, paramIdFunctionObject).toString();
    } 
    if (paramScriptable2 != null)
      bool = false; 
    return jsConstructor(paramContext, bool, paramArrayOfObject);
  }
  
  public void exportAsJSClass(boolean paramBoolean) {
    exportAsJSClass(3, getParentScope(), paramBoolean);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 3) {
      str = "uri";
      b = 2;
    } else if (i == 6) {
      str = "prefix";
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
    if (i == 0)
      return super.findInstanceIdInfo(paramString); 
    if (i == 1 || i == 2)
      return instanceIdInfo(5, super.getMaxInstanceId() + i); 
    throw new IllegalStateException();
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 8) {
      i = paramString.charAt(3);
      if (i == 111) {
        str = "toSource";
        b = 3;
      } else if (i == 116) {
        str = "toString";
        b = 2;
      } 
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
  
  public String getClassName() {
    return "Namespace";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return uri();
  }
  
  final XmlNode.Namespace getDelegate() {
    return this.ns;
  }
  
  protected String getInstanceIdName(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    return (i != 1) ? ((i != 2) ? super.getInstanceIdName(paramInt) : "uri") : "prefix";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    return (i != 1) ? ((i != 2) ? super.getInstanceIdValue(paramInt) : this.ns.getUri()) : ((this.ns.getPrefix() == null) ? Undefined.instance : this.ns.getPrefix());
  }
  
  protected int getMaxInstanceId() {
    return super.getMaxInstanceId() + 2;
  }
  
  public int hashCode() {
    return uri().hashCode();
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          b = 0;
          str = "toSource";
        } else {
          throw new IllegalArgumentException(String.valueOf(paramInt));
        } 
      } else {
        b = 0;
        str = "toString";
      } 
    } else {
      b = 2;
      str = "constructor";
    } 
    initPrototypeMethod(NAMESPACE_TAG, paramInt, str, b);
  }
  
  Namespace newNamespace(String paramString) {
    Namespace namespace1 = this.prototype;
    Namespace namespace2 = namespace1;
    if (namespace1 == null)
      namespace2 = this; 
    return create(getParentScope(), namespace2, XmlNode.Namespace.create(paramString));
  }
  
  Namespace newNamespace(String paramString1, String paramString2) {
    if (paramString1 == null)
      return newNamespace(paramString2); 
    Namespace namespace1 = this.prototype;
    Namespace namespace2 = namespace1;
    if (namespace1 == null)
      namespace2 = this; 
    return create(getParentScope(), namespace2, XmlNode.Namespace.create(paramString1, paramString2));
  }
  
  public String prefix() {
    return this.ns.getPrefix();
  }
  
  public String toLocaleString() {
    return toString();
  }
  
  public String toString() {
    return uri();
  }
  
  public String uri() {
    return this.ns.getUri();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/Namespace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */