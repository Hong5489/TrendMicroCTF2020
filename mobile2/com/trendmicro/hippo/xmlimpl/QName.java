package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.IdScriptableObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;

final class QName extends IdScriptableObject {
  private static final int Id_constructor = 1;
  
  private static final int Id_localName = 1;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  private static final int Id_uri = 2;
  
  private static final int MAX_INSTANCE_ID = 2;
  
  private static final int MAX_PROTOTYPE_ID = 3;
  
  private static final Object QNAME_TAG = "QName";
  
  static final long serialVersionUID = 416745167693026750L;
  
  private XmlNode.QName delegate;
  
  private XMLLibImpl lib;
  
  private QName prototype;
  
  static QName create(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, QName paramQName, XmlNode.QName paramQName1) {
    QName qName = new QName();
    qName.lib = paramXMLLibImpl;
    qName.setParentScope(paramScriptable);
    qName.prototype = paramQName;
    qName.setPrototype((Scriptable)paramQName);
    qName.delegate = paramQName1;
    return qName;
  }
  
  private boolean equals(QName paramQName) {
    return this.delegate.equals(paramQName.delegate);
  }
  
  private Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject) {
    return (!paramBoolean && paramArrayOfObject.length == 1) ? castToQName(this.lib, paramContext, paramArrayOfObject[0]) : ((paramArrayOfObject.length == 0) ? constructQName(this.lib, paramContext, Undefined.instance) : ((paramArrayOfObject.length == 1) ? constructQName(this.lib, paramContext, paramArrayOfObject[0]) : constructQName(this.lib, paramContext, paramArrayOfObject[0], paramArrayOfObject[1])));
  }
  
  private String js_toSource() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    toSourceImpl(uri(), localName(), prefix(), stringBuilder);
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
  
  private QName realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof QName)
      return (QName)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  private static void toSourceImpl(String paramString1, String paramString2, String paramString3, StringBuilder paramStringBuilder) {
    paramStringBuilder.append("new QName(");
    if (paramString1 == null && paramString3 == null) {
      if (!"*".equals(paramString2))
        paramStringBuilder.append("null, "); 
    } else {
      Namespace.toSourceImpl(paramString3, paramString1, paramStringBuilder);
      paramStringBuilder.append(", ");
    } 
    paramStringBuilder.append('\'');
    paramStringBuilder.append(ScriptRuntime.escapeString(paramString2, '\''));
    paramStringBuilder.append("')");
  }
  
  QName castToQName(XMLLibImpl paramXMLLibImpl, Context paramContext, Object paramObject) {
    return (paramObject instanceof QName) ? (QName)paramObject : constructQName(paramXMLLibImpl, paramContext, paramObject);
  }
  
  QName constructQName(XMLLibImpl paramXMLLibImpl, Context paramContext, Object paramObject) {
    return constructQName(paramXMLLibImpl, paramContext, Undefined.instance, paramObject);
  }
  
  QName constructQName(XMLLibImpl paramXMLLibImpl, Context paramContext, Object paramObject1, Object paramObject2) {
    String str1;
    String str2;
    if (paramObject2 instanceof QName) {
      if (paramObject1 == Undefined.instance)
        return (QName)paramObject2; 
      ((QName)paramObject2).localName();
    } 
    if (paramObject2 == Undefined.instance) {
      str2 = "";
    } else {
      str2 = ScriptRuntime.toString(paramObject2);
    } 
    paramObject2 = paramObject1;
    if (paramObject1 == Undefined.instance)
      if ("*".equals(str2)) {
        paramObject2 = null;
      } else {
        paramObject2 = paramXMLLibImpl.getDefaultNamespace(paramContext);
      }  
    paramContext = null;
    if (paramObject2 != null)
      if (paramObject2 instanceof Namespace) {
        Namespace namespace = (Namespace)paramObject2;
      } else {
        Namespace namespace = paramXMLLibImpl.newNamespace(ScriptRuntime.toString(paramObject2));
      }  
    if (paramObject2 == null) {
      paramObject1 = null;
      paramContext = null;
    } else {
      paramObject1 = paramContext.uri();
      str1 = paramContext.prefix();
    } 
    return newQName(paramXMLLibImpl, (String)paramObject1, str2, str1);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof QName) ? false : equals((QName)paramObject);
  }
  
  protected Object equivalentValues(Object paramObject) {
    if (!(paramObject instanceof QName))
      return Scriptable.NOT_FOUND; 
    if (equals((QName)paramObject)) {
      paramObject = Boolean.TRUE;
    } else {
      paramObject = Boolean.FALSE;
    } 
    return paramObject;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(QNAME_TAG))
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
  
  void exportAsJSClass(boolean paramBoolean) {
    exportAsJSClass(3, getParentScope(), paramBoolean);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 3) {
      str = "uri";
      b = 2;
    } else if (i == 9) {
      str = "localName";
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
    return "QName";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return toString();
  }
  
  final XmlNode.QName getDelegate() {
    return this.delegate;
  }
  
  protected String getInstanceIdName(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    return (i != 1) ? ((i != 2) ? super.getInstanceIdName(paramInt) : "uri") : "localName";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    return (i != 1) ? ((i != 2) ? super.getInstanceIdValue(paramInt) : uri()) : localName();
  }
  
  protected int getMaxInstanceId() {
    return super.getMaxInstanceId() + 2;
  }
  
  public int hashCode() {
    return this.delegate.hashCode();
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
    initPrototypeMethod(QNAME_TAG, paramInt, str, b);
  }
  
  public String localName() {
    return (this.delegate.getLocalName() == null) ? "*" : this.delegate.getLocalName();
  }
  
  QName newQName(XMLLibImpl paramXMLLibImpl, String paramString1, String paramString2, String paramString3) {
    XmlNode.Namespace namespace;
    QName qName1 = this.prototype;
    QName qName2 = qName1;
    if (qName1 == null)
      qName2 = this; 
    qName1 = null;
    if (paramString3 != null) {
      namespace = XmlNode.Namespace.create(paramString3, paramString1);
    } else {
      QName qName = qName1;
      if (paramString1 != null)
        namespace = XmlNode.Namespace.create(paramString1); 
    } 
    paramString1 = paramString2;
    if (paramString2 != null) {
      paramString1 = paramString2;
      if (paramString2.equals("*"))
        paramString1 = null; 
    } 
    return create(paramXMLLibImpl, getParentScope(), qName2, XmlNode.QName.create(namespace, paramString1));
  }
  
  String prefix() {
    return (this.delegate.getNamespace() == null) ? null : this.delegate.getNamespace().getPrefix();
  }
  
  @Deprecated
  final XmlNode.QName toNodeQname() {
    return this.delegate;
  }
  
  public String toString() {
    if (this.delegate.getNamespace() == null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("*::");
      stringBuilder1.append(localName());
      return stringBuilder1.toString();
    } 
    if (this.delegate.getNamespace().isGlobal())
      return localName(); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(uri());
    stringBuilder.append("::");
    stringBuilder.append(localName());
    return stringBuilder.toString();
  }
  
  String uri() {
    return (this.delegate.getNamespace() == null) ? null : this.delegate.getNamespace().getUri();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/QName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */