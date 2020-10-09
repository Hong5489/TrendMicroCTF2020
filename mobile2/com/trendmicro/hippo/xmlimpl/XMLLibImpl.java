package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Ref;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.Wrapper;
import com.trendmicro.hippo.xml.XMLLib;
import com.trendmicro.hippo.xml.XMLObject;
import java.io.Serializable;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public final class XMLLibImpl extends XMLLib implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Scriptable globalScope;
  
  private Namespace namespacePrototype;
  
  private XmlProcessor options = new XmlProcessor();
  
  private QName qnamePrototype;
  
  private XMLList xmlListPrototype;
  
  private XML xmlPrototype;
  
  private XMLLibImpl(Scriptable paramScriptable) {
    this.globalScope = paramScriptable;
  }
  
  private static RuntimeException badXMLName(Object paramObject) {
    String str;
    if (paramObject instanceof Number) {
      str = "Can not construct XML name from number: ";
    } else if (paramObject instanceof Boolean) {
      str = "Can not construct XML name from boolean: ";
    } else {
      if (paramObject == Undefined.instance || paramObject == null) {
        str = "Can not construct XML name from ";
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append(ScriptRuntime.toString(paramObject));
        return (RuntimeException)ScriptRuntime.typeError(stringBuilder1.toString());
      } 
      throw new IllegalArgumentException(paramObject.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(ScriptRuntime.toString(paramObject));
    return (RuntimeException)ScriptRuntime.typeError(stringBuilder.toString());
  }
  
  private void exportToScope(boolean paramBoolean) {
    this.xmlPrototype = newXML(XmlNode.createText(this.options, ""));
    this.xmlListPrototype = newXMLList();
    this.namespacePrototype = Namespace.create(this.globalScope, null, XmlNode.Namespace.GLOBAL);
    this.qnamePrototype = QName.create(this, this.globalScope, null, XmlNode.QName.create(XmlNode.Namespace.create(""), ""));
    this.xmlPrototype.exportAsJSClass(paramBoolean);
    this.xmlListPrototype.exportAsJSClass(paramBoolean);
    this.namespacePrototype.exportAsJSClass(paramBoolean);
    this.qnamePrototype.exportAsJSClass(paramBoolean);
  }
  
  private String getDefaultNamespaceURI(Context paramContext) {
    return getDefaultNamespace(paramContext).uri();
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    XMLLibImpl xMLLibImpl = new XMLLibImpl(paramScriptable);
    if (xMLLibImpl.bindToScope(paramScriptable) == xMLLibImpl)
      xMLLibImpl.exportToScope(paramBoolean); 
  }
  
  private XML parse(String paramString) {
    try {
      return newXML(XmlNode.createElement(this.options, getDefaultNamespaceURI(Context.getCurrentContext()), paramString));
    } catch (SAXException sAXException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot parse XML: ");
      stringBuilder.append(sAXException.getMessage());
      throw ScriptRuntime.typeError(stringBuilder.toString());
    } 
  }
  
  public static Node toDomNode(Object paramObject) {
    if (paramObject instanceof XML)
      return ((XML)paramObject).toDomNode(); 
    throw new IllegalArgumentException("xmlObject is not an XML object in JavaScript.");
  }
  
  private Ref xmlPrimaryReference(Context paramContext, XMLName paramXMLName, Scriptable paramScriptable) {
    Context context = null;
    while (true) {
      XMLObjectImpl xMLObjectImpl1;
      XMLObjectImpl xMLObjectImpl2;
      paramContext = context;
      if (paramScriptable instanceof XMLWithScope) {
        xMLObjectImpl2 = (XMLObjectImpl)paramScriptable.getPrototype();
        if (xMLObjectImpl2.hasXMLProperty(paramXMLName)) {
          if (xMLObjectImpl2 != null)
            paramXMLName.initXMLObject(xMLObjectImpl2); 
          return paramXMLName;
        } 
        paramContext = context;
        if (context == null)
          xMLObjectImpl1 = xMLObjectImpl2; 
      } 
      paramScriptable = paramScriptable.getParentScope();
      if (paramScriptable == null) {
        xMLObjectImpl2 = xMLObjectImpl1;
      } else {
        XMLObjectImpl xMLObjectImpl = xMLObjectImpl1;
        continue;
      } 
      if (xMLObjectImpl2 != null)
        paramXMLName.initXMLObject(xMLObjectImpl2); 
      return paramXMLName;
    } 
  }
  
  Object addXMLObjects(Context paramContext, XMLObject paramXMLObject1, XMLObject paramXMLObject2) {
    XMLList xMLList = newXMLList();
    if (paramXMLObject1 instanceof XMLList) {
      XMLList xMLList1 = (XMLList)paramXMLObject1;
      if (xMLList1.length() == 1) {
        xMLList.addToList(xMLList1.item(0));
      } else {
        xMLList = newXMLListFrom(paramXMLObject1);
      } 
    } else {
      xMLList.addToList(paramXMLObject1);
    } 
    if (paramXMLObject2 instanceof XMLList) {
      paramXMLObject1 = paramXMLObject2;
      for (byte b = 0; b < paramXMLObject1.length(); b++)
        xMLList.addToList(paramXMLObject1.item(b)); 
    } else if (paramXMLObject2 instanceof XML) {
      xMLList.addToList(paramXMLObject2);
    } 
    return xMLList;
  }
  
  Namespace castToNamespace(Context paramContext, Object paramObject) {
    return this.namespacePrototype.castToNamespace(paramObject);
  }
  
  QName castToQName(Context paramContext, Object paramObject) {
    return this.qnamePrototype.castToQName(this, paramContext, paramObject);
  }
  
  QName constructQName(Context paramContext, Object paramObject) {
    return this.qnamePrototype.constructQName(this, paramContext, paramObject);
  }
  
  QName constructQName(Context paramContext, Object paramObject1, Object paramObject2) {
    return this.qnamePrototype.constructQName(this, paramContext, paramObject1, paramObject2);
  }
  
  Namespace[] createNamespaces(XmlNode.Namespace[] paramArrayOfNamespace) {
    Namespace[] arrayOfNamespace = new Namespace[paramArrayOfNamespace.length];
    for (byte b = 0; b < paramArrayOfNamespace.length; b++)
      arrayOfNamespace[b] = this.namespacePrototype.newNamespace(paramArrayOfNamespace[b].getPrefix(), paramArrayOfNamespace[b].getUri()); 
    return arrayOfNamespace;
  }
  
  final XML ecmaToXml(Object paramObject) {
    if (paramObject != null && paramObject != Undefined.instance) {
      if (paramObject instanceof XML)
        return (XML)paramObject; 
      if (paramObject instanceof XMLList) {
        paramObject = paramObject;
        if (paramObject.getXML() != null)
          return paramObject.getXML(); 
        throw ScriptRuntime.typeError("Cannot convert list of >1 element to XML");
      } 
      Object object = paramObject;
      if (paramObject instanceof Wrapper)
        object = ((Wrapper)paramObject).unwrap(); 
      if (object instanceof Node)
        return newXML(XmlNode.createElementFromNode((Node)object)); 
      paramObject = ScriptRuntime.toString(object);
      return (paramObject.length() > 0 && paramObject.charAt(0) == '<') ? parse((String)paramObject) : newXML(XmlNode.createText(this.options, (String)paramObject));
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Cannot convert ");
    stringBuilder.append(paramObject);
    stringBuilder.append(" to XML");
    throw ScriptRuntime.typeError(stringBuilder.toString());
  }
  
  public String escapeAttributeValue(Object paramObject) {
    return this.options.escapeAttributeValue(paramObject);
  }
  
  public String escapeTextValue(Object paramObject) {
    return this.options.escapeTextValue(paramObject);
  }
  
  Namespace getDefaultNamespace(Context paramContext) {
    Context context = paramContext;
    if (paramContext == null) {
      paramContext = Context.getCurrentContext();
      context = paramContext;
      if (paramContext == null)
        return this.namespacePrototype; 
    } 
    Object object = ScriptRuntime.searchDefaultNamespace(context);
    return (object == null) ? this.namespacePrototype : ((object instanceof Namespace) ? (Namespace)object : this.namespacePrototype);
  }
  
  public int getPrettyIndent() {
    return this.options.getPrettyIndent();
  }
  
  XmlProcessor getProcessor() {
    return this.options;
  }
  
  @Deprecated
  Scriptable globalScope() {
    return this.globalScope;
  }
  
  public boolean isIgnoreComments() {
    return this.options.isIgnoreComments();
  }
  
  public boolean isIgnoreProcessingInstructions() {
    return this.options.isIgnoreProcessingInstructions();
  }
  
  public boolean isIgnoreWhitespace() {
    return this.options.isIgnoreWhitespace();
  }
  
  public boolean isPrettyPrinting() {
    return this.options.isPrettyPrinting();
  }
  
  public boolean isXMLName(Context paramContext, Object paramObject) {
    return XMLName.accept(paramObject);
  }
  
  public Ref nameRef(Context paramContext, Object paramObject, Scriptable paramScriptable, int paramInt) {
    if ((paramInt & 0x2) != 0)
      return xmlPrimaryReference(paramContext, toAttributeName(paramContext, paramObject), paramScriptable); 
    throw Kit.codeBug();
  }
  
  public Ref nameRef(Context paramContext, Object paramObject1, Object paramObject2, Scriptable paramScriptable, int paramInt) {
    paramObject1 = XMLName.create(toNodeQName(paramContext, paramObject1, paramObject2), false, false);
    if ((paramInt & 0x2) != 0 && !paramObject1.isAttributeName())
      paramObject1.setAttributeName(); 
    return xmlPrimaryReference(paramContext, (XMLName)paramObject1, paramScriptable);
  }
  
  Namespace newNamespace(String paramString) {
    return this.namespacePrototype.newNamespace(paramString);
  }
  
  QName newQName(XmlNode.QName paramQName) {
    return QName.create(this, this.globalScope, this.qnamePrototype, paramQName);
  }
  
  QName newQName(String paramString1, String paramString2, String paramString3) {
    return this.qnamePrototype.newQName(this, paramString1, paramString2, paramString3);
  }
  
  final XML newTextElementXML(XmlNode paramXmlNode, XmlNode.QName paramQName, String paramString) {
    return newXML(XmlNode.newElementWithText(this.options, paramXmlNode, paramQName, paramString));
  }
  
  XML newXML(XmlNode paramXmlNode) {
    return new XML(this, this.globalScope, this.xmlPrototype, paramXmlNode);
  }
  
  final XML newXMLFromJs(Object paramObject) {
    if (paramObject == null || paramObject == Undefined.instance) {
      paramObject = "";
    } else if (paramObject instanceof XMLObjectImpl) {
      paramObject = ((XMLObjectImpl)paramObject).toXMLString();
    } else {
      paramObject = ScriptRuntime.toString(paramObject);
    } 
    if (!paramObject.trim().startsWith("<>"))
      return (paramObject.indexOf("<") == -1) ? newXML(XmlNode.createText(this.options, (String)paramObject)) : parse((String)paramObject); 
    throw ScriptRuntime.typeError("Invalid use of XML object anonymous tags <></>.");
  }
  
  XMLList newXMLList() {
    return new XMLList(this, this.globalScope, this.xmlListPrototype);
  }
  
  final XMLList newXMLListFrom(Object paramObject) {
    XMLList xMLList = newXMLList();
    if (paramObject == null || paramObject instanceof Undefined)
      return xMLList; 
    if (paramObject instanceof XML) {
      paramObject = paramObject;
      xMLList.getNodeList().add((XML)paramObject);
      return xMLList;
    } 
    if (paramObject instanceof XMLList) {
      paramObject = paramObject;
      xMLList.getNodeList().add(paramObject.getNodeList());
      return xMLList;
    } 
    String str2 = ScriptRuntime.toString(paramObject).trim();
    paramObject = str2;
    if (!str2.startsWith("<>")) {
      paramObject = new StringBuilder();
      paramObject.append("<>");
      paramObject.append(str2);
      paramObject.append("</>");
      paramObject = paramObject.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<fragment>");
    stringBuilder.append(paramObject.substring(2));
    String str1 = stringBuilder.toString();
    if (str1.endsWith("</>")) {
      paramObject = new StringBuilder();
      paramObject.append(str1.substring(0, str1.length() - 3));
      paramObject.append("</fragment>");
      paramObject = newXMLFromJs(paramObject.toString()).children();
      for (byte b = 0; b < paramObject.getNodeList().length(); b++)
        xMLList.getNodeList().add((XML)paramObject.item(b).copy()); 
      return xMLList;
    } 
    throw ScriptRuntime.typeError("XML with anonymous tag missing end anonymous tag");
  }
  
  @Deprecated
  QName qnamePrototype() {
    return this.qnamePrototype;
  }
  
  public void setIgnoreComments(boolean paramBoolean) {
    this.options.setIgnoreComments(paramBoolean);
  }
  
  public void setIgnoreProcessingInstructions(boolean paramBoolean) {
    this.options.setIgnoreProcessingInstructions(paramBoolean);
  }
  
  public void setIgnoreWhitespace(boolean paramBoolean) {
    this.options.setIgnoreWhitespace(paramBoolean);
  }
  
  public void setPrettyIndent(int paramInt) {
    this.options.setPrettyIndent(paramInt);
  }
  
  public void setPrettyPrinting(boolean paramBoolean) {
    this.options.setPrettyPrinting(paramBoolean);
  }
  
  @Deprecated
  XMLName toAttributeName(Context paramContext, Object paramObject) {
    if (paramObject instanceof XMLName)
      return (XMLName)paramObject; 
    if (paramObject instanceof QName)
      return XMLName.create(((QName)paramObject).getDelegate(), true, false); 
    if (!(paramObject instanceof Boolean) && !(paramObject instanceof Number) && paramObject != Undefined.instance && paramObject != null) {
      String str;
      if (paramObject instanceof String) {
        str = (String)paramObject;
      } else {
        str = ScriptRuntime.toString(paramObject);
      } 
      paramObject = str;
      if (str != null) {
        paramObject = str;
        if (str.equals("*"))
          paramObject = null; 
      } 
      return XMLName.create(XmlNode.QName.create(XmlNode.Namespace.create(""), (String)paramObject), true, false);
    } 
    throw badXMLName(paramObject);
  }
  
  public Object toDefaultXmlNamespace(Context paramContext, Object paramObject) {
    return this.namespacePrototype.constructNamespace(paramObject);
  }
  
  XmlNode.QName toNodeQName(Context paramContext, Object paramObject1, Object paramObject2) {
    XmlNode.Namespace namespace;
    if (paramObject2 instanceof QName) {
      paramObject2 = ((QName)paramObject2).localName();
    } else {
      paramObject2 = ScriptRuntime.toString(paramObject2);
    } 
    if (paramObject1 == Undefined.instance) {
      if ("*".equals(paramObject2)) {
        paramContext = null;
      } else {
        namespace = getDefaultNamespace(paramContext).getDelegate();
      } 
    } else if (paramObject1 == null) {
      paramContext = null;
    } else if (paramObject1 instanceof Namespace) {
      namespace = ((Namespace)paramObject1).getDelegate();
    } else {
      namespace = this.namespacePrototype.constructNamespace(paramObject1).getDelegate();
    } 
    paramObject1 = paramObject2;
    if (paramObject2 != null) {
      paramObject1 = paramObject2;
      if (paramObject2.equals("*"))
        paramObject1 = null; 
    } 
    return XmlNode.QName.create(namespace, (String)paramObject1);
  }
  
  XmlNode.QName toNodeQName(Context paramContext, Object paramObject, boolean paramBoolean) {
    if (paramObject instanceof XMLName)
      return ((XMLName)paramObject).toQname(); 
    if (paramObject instanceof QName)
      return ((QName)paramObject).getDelegate(); 
    if (!(paramObject instanceof Boolean) && !(paramObject instanceof Number) && paramObject != Undefined.instance && paramObject != null) {
      if (paramObject instanceof String) {
        paramObject = paramObject;
      } else {
        paramObject = ScriptRuntime.toString(paramObject);
      } 
      return toNodeQName(paramContext, (String)paramObject, paramBoolean);
    } 
    throw badXMLName(paramObject);
  }
  
  XmlNode.QName toNodeQName(Context paramContext, String paramString, boolean paramBoolean) {
    XmlNode.Namespace namespace = getDefaultNamespace(paramContext).getDelegate();
    return (paramString != null && paramString.equals("*")) ? XmlNode.QName.create(null, null) : (paramBoolean ? XmlNode.QName.create(XmlNode.Namespace.GLOBAL, paramString) : XmlNode.QName.create(namespace, paramString));
  }
  
  XMLName toXMLName(Context paramContext, Object paramObject) {
    if (paramObject instanceof XMLName) {
      XMLName xMLName = (XMLName)paramObject;
    } else {
      XMLName xMLName;
      if (paramObject instanceof QName) {
        QName qName = (QName)paramObject;
        xMLName = XMLName.formProperty(qName.uri(), qName.localName());
      } else if (paramObject instanceof String) {
        xMLName = toXMLNameFromString((Context)xMLName, (String)paramObject);
      } else {
        if (!(paramObject instanceof Boolean) && !(paramObject instanceof Number) && paramObject != Undefined.instance && paramObject != null)
          return toXMLNameFromString((Context)xMLName, ScriptRuntime.toString(paramObject)); 
        throw badXMLName(paramObject);
      } 
    } 
    return (XMLName)paramContext;
  }
  
  XMLName toXMLNameFromString(Context paramContext, String paramString) {
    return XMLName.create(getDefaultNamespaceURI(paramContext), paramString);
  }
  
  XMLName toXMLNameOrIndex(Context paramContext, Object paramObject) {
    XMLName xMLName;
    if (paramObject instanceof XMLName) {
      xMLName = (XMLName)paramObject;
    } else if (paramObject instanceof String) {
      paramObject = paramObject;
      long l = ScriptRuntime.testUint32String((String)paramObject);
      if (l >= 0L) {
        ScriptRuntime.storeUint32Result((Context)xMLName, l);
        xMLName = null;
      } else {
        xMLName = toXMLNameFromString((Context)xMLName, (String)paramObject);
      } 
    } else if (paramObject instanceof Number) {
      double d = ((Number)paramObject).doubleValue();
      long l = (long)d;
      if (l == d && 0L <= l && l <= 4294967295L) {
        ScriptRuntime.storeUint32Result((Context)xMLName, l);
        xMLName = null;
      } else {
        throw badXMLName(paramObject);
      } 
    } else if (paramObject instanceof QName) {
      paramObject = paramObject;
      String str = paramObject.uri();
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (str != null) {
        bool2 = bool1;
        if (str.length() == 0) {
          long l = ScriptRuntime.testUint32String(str);
          bool2 = bool1;
          if (l >= 0L) {
            ScriptRuntime.storeUint32Result((Context)xMLName, l);
            bool2 = true;
          } 
        } 
      } 
      if (!bool2) {
        xMLName = XMLName.formProperty(str, paramObject.localName());
      } else {
        xMLName = null;
      } 
    } else {
      if (!(paramObject instanceof Boolean) && paramObject != Undefined.instance && paramObject != null) {
        paramObject = ScriptRuntime.toString(paramObject);
        long l = ScriptRuntime.testUint32String((String)paramObject);
        if (l >= 0L) {
          ScriptRuntime.storeUint32Result((Context)xMLName, l);
          xMLName = null;
        } else {
          xMLName = toXMLNameFromString((Context)xMLName, (String)paramObject);
        } 
        return xMLName;
      } 
      throw badXMLName(paramObject);
    } 
    return xMLName;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XMLLibImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */