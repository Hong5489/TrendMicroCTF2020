package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.EcmaError;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Ref;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Undefined;

class XMLName extends Ref {
  static final long serialVersionUID = 3832176310755686977L;
  
  private boolean isAttributeName;
  
  private boolean isDescendants;
  
  private XmlNode.QName qname;
  
  private XMLObjectImpl xmlObject;
  
  static boolean accept(Object paramObject) {
    try {
      paramObject = ScriptRuntime.toString(paramObject);
      int i = paramObject.length();
      if (i != 0 && isNCNameStartChar(paramObject.charAt(0))) {
        for (int j = 1; j != i; j++) {
          if (!isNCNameChar(paramObject.charAt(j)))
            return false; 
        } 
        return true;
      } 
      return false;
    } catch (EcmaError ecmaError) {
      if ("TypeError".equals(ecmaError.getName()))
        return false; 
      throw ecmaError;
    } 
  }
  
  private void addAttributes(XMLList paramXMLList, XML paramXML) {
    addMatchingAttributes(paramXMLList, paramXML);
  }
  
  private void addDescendantAttributes(XMLList paramXMLList, XML paramXML) {
    if (paramXML.isElement()) {
      addMatchingAttributes(paramXMLList, paramXML);
      XML[] arrayOfXML = paramXML.getChildren();
      for (byte b = 0; b < arrayOfXML.length; b++)
        addDescendantAttributes(paramXMLList, arrayOfXML[b]); 
    } 
  }
  
  private void addDescendantChildren(XMLList paramXMLList, XML paramXML) {
    if (paramXML.isElement()) {
      XML[] arrayOfXML = paramXML.getChildren();
      for (byte b = 0; b < arrayOfXML.length; b++) {
        if (matches(arrayOfXML[b]))
          paramXMLList.addToList(arrayOfXML[b]); 
        addDescendantChildren(paramXMLList, arrayOfXML[b]);
      } 
    } 
  }
  
  @Deprecated
  static XMLName create(XmlNode.QName paramQName) {
    return create(paramQName, false, false);
  }
  
  static XMLName create(XmlNode.QName paramQName, boolean paramBoolean1, boolean paramBoolean2) {
    XMLName xMLName = new XMLName();
    xMLName.qname = paramQName;
    xMLName.isAttributeName = paramBoolean1;
    xMLName.isDescendants = paramBoolean2;
    return xMLName;
  }
  
  static XMLName create(String paramString1, String paramString2) {
    if (paramString2 != null) {
      XMLName xMLName;
      int i = paramString2.length();
      if (i != 0) {
        char c = paramString2.charAt(0);
        if (c == '*') {
          if (i == 1)
            return formStar(); 
        } else if (c == '@') {
          xMLName = formProperty("", paramString2.substring(1));
          xMLName.setAttributeName();
          return xMLName;
        } 
      } 
      return formProperty((String)xMLName, paramString2);
    } 
    throw new IllegalArgumentException();
  }
  
  @Deprecated
  static XMLName formProperty(XmlNode.Namespace paramNamespace, String paramString) {
    String str = paramString;
    if (paramString != null) {
      str = paramString;
      if (paramString.equals("*"))
        str = null; 
    } 
    XMLName xMLName = new XMLName();
    xMLName.qname = XmlNode.QName.create(paramNamespace, str);
    return xMLName;
  }
  
  static XMLName formProperty(String paramString1, String paramString2) {
    return formProperty(XmlNode.Namespace.create(paramString1), paramString2);
  }
  
  static XMLName formStar() {
    XMLName xMLName = new XMLName();
    xMLName.qname = XmlNode.QName.create(null, null);
    return xMLName;
  }
  
  private static boolean isNCNameChar(int paramInt) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    null = false;
    if ((paramInt & 0xFFFFFF80) == 0) {
      if (paramInt >= 97) {
        if (paramInt <= 122)
          null = true; 
        return null;
      } 
      if (paramInt >= 65) {
        if (paramInt <= 90)
          return true; 
        null = bool1;
        if (paramInt == 95)
          null = true; 
        return null;
      } 
      if (paramInt >= 48) {
        null = bool2;
        if (paramInt <= 57)
          null = true; 
        return null;
      } 
      if (paramInt != 45) {
        null = bool3;
        return (paramInt == 46) ? true : null;
      } 
    } else {
      if ((paramInt & 0xFFFFE000) == 0) {
        if (!isNCNameStartChar(paramInt) && paramInt != 183) {
          null = bool4;
          if (768 <= paramInt) {
            null = bool4;
            if (paramInt <= 879)
              null = true; 
          } 
          return null;
        } 
      } else {
        if (!isNCNameStartChar(paramInt)) {
          null = bool5;
          if (8255 <= paramInt) {
            null = bool5;
            if (paramInt <= 8256)
              null = true; 
          } 
          return null;
        } 
        null = true;
      } 
      null = true;
    } 
    return true;
  }
  
  private static boolean isNCNameStartChar(int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: iconst_0
    //   3: istore_2
    //   4: iconst_0
    //   5: istore_3
    //   6: iconst_0
    //   7: istore #4
    //   9: iload_0
    //   10: bipush #-128
    //   12: iand
    //   13: ifne -> 63
    //   16: iload_0
    //   17: bipush #97
    //   19: if_icmplt -> 34
    //   22: iload_0
    //   23: bipush #122
    //   25: if_icmpgt -> 31
    //   28: iconst_1
    //   29: istore #4
    //   31: iload #4
    //   33: ireturn
    //   34: iload_0
    //   35: bipush #65
    //   37: if_icmplt -> 143
    //   40: iload_0
    //   41: bipush #90
    //   43: if_icmpgt -> 48
    //   46: iconst_1
    //   47: ireturn
    //   48: iload_1
    //   49: istore #4
    //   51: iload_0
    //   52: bipush #95
    //   54: if_icmpne -> 60
    //   57: iconst_1
    //   58: istore #4
    //   60: iload #4
    //   62: ireturn
    //   63: iload_0
    //   64: sipush #-8192
    //   67: iand
    //   68: ifne -> 143
    //   71: sipush #192
    //   74: iload_0
    //   75: if_icmpgt -> 85
    //   78: iload_0
    //   79: sipush #214
    //   82: if_icmple -> 137
    //   85: sipush #216
    //   88: iload_0
    //   89: if_icmpgt -> 99
    //   92: iload_0
    //   93: sipush #246
    //   96: if_icmple -> 137
    //   99: sipush #248
    //   102: iload_0
    //   103: if_icmpgt -> 113
    //   106: iload_0
    //   107: sipush #767
    //   110: if_icmple -> 137
    //   113: sipush #880
    //   116: iload_0
    //   117: if_icmpgt -> 127
    //   120: iload_0
    //   121: sipush #893
    //   124: if_icmple -> 137
    //   127: iload_2
    //   128: istore #4
    //   130: sipush #895
    //   133: iload_0
    //   134: if_icmpgt -> 140
    //   137: iconst_1
    //   138: istore #4
    //   140: iload #4
    //   142: ireturn
    //   143: sipush #8204
    //   146: iload_0
    //   147: if_icmpgt -> 157
    //   150: iload_0
    //   151: sipush #8205
    //   154: if_icmple -> 240
    //   157: sipush #8304
    //   160: iload_0
    //   161: if_icmpgt -> 171
    //   164: iload_0
    //   165: sipush #8591
    //   168: if_icmple -> 240
    //   171: sipush #11264
    //   174: iload_0
    //   175: if_icmpgt -> 185
    //   178: iload_0
    //   179: sipush #12271
    //   182: if_icmple -> 240
    //   185: sipush #12289
    //   188: iload_0
    //   189: if_icmpgt -> 198
    //   192: iload_0
    //   193: ldc 55295
    //   195: if_icmple -> 240
    //   198: ldc 63744
    //   200: iload_0
    //   201: if_icmpgt -> 210
    //   204: iload_0
    //   205: ldc 64975
    //   207: if_icmple -> 240
    //   210: ldc 65008
    //   212: iload_0
    //   213: if_icmpgt -> 222
    //   216: iload_0
    //   217: ldc 65533
    //   219: if_icmple -> 240
    //   222: iload_3
    //   223: istore #4
    //   225: ldc 65536
    //   227: iload_0
    //   228: if_icmpgt -> 243
    //   231: iload_3
    //   232: istore #4
    //   234: iload_0
    //   235: ldc 983039
    //   237: if_icmpgt -> 243
    //   240: iconst_1
    //   241: istore #4
    //   243: iload #4
    //   245: ireturn
  }
  
  void addDescendants(XMLList paramXMLList, XML paramXML) {
    if (isAttributeName()) {
      matchDescendantAttributes(paramXMLList, paramXML);
    } else {
      matchDescendantChildren(paramXMLList, paramXML);
    } 
  }
  
  void addMatches(XMLList paramXMLList, XML paramXML) {
    if (isDescendants()) {
      addDescendants(paramXMLList, paramXML);
    } else if (isAttributeName()) {
      addAttributes(paramXMLList, paramXML);
    } else {
      XML[] arrayOfXML = paramXML.getChildren();
      if (arrayOfXML != null)
        for (byte b = 0; b < arrayOfXML.length; b++) {
          if (matches(arrayOfXML[b]))
            paramXMLList.addToList(arrayOfXML[b]); 
        }  
      paramXMLList.setTargets(paramXML, toQname());
    } 
  }
  
  void addMatchingAttributes(XMLList paramXMLList, XML paramXML) {
    if (paramXML.isElement()) {
      XML[] arrayOfXML = paramXML.getAttributes();
      for (byte b = 0; b < arrayOfXML.length; b++) {
        if (matches(arrayOfXML[b]))
          paramXMLList.addToList(arrayOfXML[b]); 
      } 
    } 
  }
  
  public boolean delete(Context paramContext) {
    XMLObjectImpl xMLObjectImpl = this.xmlObject;
    if (xMLObjectImpl == null)
      return true; 
    xMLObjectImpl.deleteXMLProperty(this);
    return this.xmlObject.hasXMLProperty(this) ^ true;
  }
  
  public Object get(Context paramContext) {
    XMLObjectImpl xMLObjectImpl = this.xmlObject;
    if (xMLObjectImpl != null)
      return xMLObjectImpl.getXMLProperty(this); 
    throw ScriptRuntime.undefReadError(Undefined.instance, toString());
  }
  
  XMLList getMyValueOn(XML paramXML) {
    XMLList xMLList = paramXML.newXMLList();
    addMatches(xMLList, paramXML);
    return xMLList;
  }
  
  public boolean has(Context paramContext) {
    XMLObjectImpl xMLObjectImpl = this.xmlObject;
    return (xMLObjectImpl == null) ? false : xMLObjectImpl.hasXMLProperty(this);
  }
  
  void initXMLObject(XMLObjectImpl paramXMLObjectImpl) {
    if (paramXMLObjectImpl != null) {
      if (this.xmlObject == null) {
        this.xmlObject = paramXMLObjectImpl;
        return;
      } 
      throw new IllegalStateException();
    } 
    throw new IllegalArgumentException();
  }
  
  boolean isAttributeName() {
    return this.isAttributeName;
  }
  
  boolean isDescendants() {
    return this.isDescendants;
  }
  
  String localName() {
    return (this.qname.getLocalName() == null) ? "*" : this.qname.getLocalName();
  }
  
  XMLList matchDescendantAttributes(XMLList paramXMLList, XML paramXML) {
    paramXMLList.setTargets(paramXML, null);
    addDescendantAttributes(paramXMLList, paramXML);
    return paramXMLList;
  }
  
  XMLList matchDescendantChildren(XMLList paramXMLList, XML paramXML) {
    paramXMLList.setTargets(paramXML, null);
    addDescendantChildren(paramXMLList, paramXML);
    return paramXMLList;
  }
  
  final boolean matches(XML paramXML) {
    XmlNode.QName qName = paramXML.getNodeQname();
    String str = null;
    if (qName.getNamespace() != null)
      str = qName.getNamespace().getUri(); 
    if (this.isAttributeName)
      return paramXML.isAttribute() ? (((uri() == null || uri().equals(str)) && (localName().equals("*") || localName().equals(qName.getLocalName())))) : false; 
    if (uri() == null || (paramXML.isElement() && uri().equals(str))) {
      if (localName().equals("*"))
        return true; 
      if (paramXML.isElement() && localName().equals(qName.getLocalName()))
        return true; 
    } 
    return false;
  }
  
  final boolean matchesElement(XmlNode.QName paramQName) {
    return ((uri() == null || uri().equals(paramQName.getNamespace().getUri())) && (localName().equals("*") || localName().equals(paramQName.getLocalName())));
  }
  
  final boolean matchesLocalName(String paramString) {
    return (localName().equals("*") || localName().equals(paramString));
  }
  
  public Object set(Context paramContext, Object paramObject) {
    XMLObjectImpl xMLObjectImpl = this.xmlObject;
    if (xMLObjectImpl != null) {
      if (!this.isDescendants) {
        xMLObjectImpl.putXMLProperty(this, paramObject);
        return paramObject;
      } 
      throw Kit.codeBug();
    } 
    throw ScriptRuntime.undefWriteError(Undefined.instance, toString(), paramObject);
  }
  
  void setAttributeName() {
    this.isAttributeName = true;
  }
  
  @Deprecated
  void setIsDescendants() {
    this.isDescendants = true;
  }
  
  void setMyValueOn(XML paramXML, Object paramObject) {
    Object object;
    if (paramObject == null) {
      object = "null";
    } else {
      object = paramObject;
      if (paramObject instanceof Undefined)
        object = "undefined"; 
    } 
    if (isAttributeName()) {
      paramXML.setAttribute(this, object);
    } else if (uri() == null && localName().equals("*")) {
      paramXML.setChildren(object);
    } else {
      if (object instanceof XMLObjectImpl) {
        object = object;
        paramObject = object;
        if (object instanceof XML) {
          paramObject = object;
          if (((XML)object).isAttribute())
            paramObject = paramXML.makeXmlFromString(this, object.toString()); 
        } 
        object = paramObject;
        if (paramObject instanceof XMLList) {
          for (byte b = 0; b < paramObject.length(); b++) {
            object = ((XMLList)paramObject).item(b);
            if (object.isAttribute())
              ((XMLList)paramObject).replace(b, paramXML.makeXmlFromString(this, object.toString())); 
          } 
          object = paramObject;
        } 
      } else {
        object = paramXML.makeXmlFromString(this, ScriptRuntime.toString(object));
      } 
      paramObject = paramXML.getPropertyList(this);
      if (paramObject.length() == 0) {
        paramXML.appendChild(object);
      } else {
        for (byte b = 1; b < paramObject.length(); b++)
          paramXML.removeChild(paramObject.item(b).childIndex()); 
        paramXML.replace(paramObject.item(0).childIndex(), object);
      } 
    } 
  }
  
  final XmlNode.QName toQname() {
    return this.qname;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.isDescendants)
      stringBuilder.append(".."); 
    if (this.isAttributeName)
      stringBuilder.append('@'); 
    if (uri() == null) {
      stringBuilder.append('*');
      if (localName().equals("*"))
        return stringBuilder.toString(); 
    } else {
      stringBuilder.append('"');
      stringBuilder.append(uri());
      stringBuilder.append('"');
    } 
    stringBuilder.append(':');
    stringBuilder.append(localName());
    return stringBuilder.toString();
  }
  
  String uri() {
    return (this.qname.getNamespace() == null) ? null : this.qname.getNamespace().getUri();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XMLName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */