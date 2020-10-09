package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.xml.XMLObject;
import org.w3c.dom.Node;

class XML extends XMLObjectImpl {
  static final long serialVersionUID = -630969919086449092L;
  
  private XmlNode node;
  
  XML(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject, XmlNode paramXmlNode) {
    super(paramXMLLibImpl, paramScriptable, paramXMLObject);
    initialize(paramXmlNode);
  }
  
  private XmlNode.Namespace adapt(Namespace paramNamespace) {
    return (paramNamespace.prefix() == null) ? XmlNode.Namespace.create(paramNamespace.uri()) : XmlNode.Namespace.create(paramNamespace.prefix(), paramNamespace.uri());
  }
  
  private void addInScopeNamespace(Namespace paramNamespace) {
    if (!isElement())
      return; 
    if (paramNamespace.prefix() != null) {
      if (paramNamespace.prefix().length() == 0 && paramNamespace.uri().length() == 0)
        return; 
      if (this.node.getQname().getNamespace().getPrefix().equals(paramNamespace.prefix()))
        this.node.invalidateNamespacePrefix(); 
      this.node.declareNamespace(paramNamespace.prefix(), paramNamespace.uri());
      return;
    } 
  }
  
  private String ecmaToString() {
    if (isAttribute() || isText())
      return ecmaValue(); 
    if (hasSimpleContent()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b = 0; b < this.node.getChildCount(); b++) {
        XmlNode xmlNode = this.node.getChild(b);
        if (!xmlNode.isProcessingInstructionType() && !xmlNode.isCommentType())
          stringBuilder.append((new XML(getLib(), getParentScope(), (XMLObject)getPrototype(), xmlNode)).toString()); 
      } 
      return stringBuilder.toString();
    } 
    return toXMLString();
  }
  
  private String ecmaValue() {
    return this.node.ecmaValue();
  }
  
  private int getChildIndexOf(XML paramXML) {
    for (byte b = 0; b < this.node.getChildCount(); b++) {
      if (this.node.getChild(b).isSameNode(paramXML.node))
        return b; 
    } 
    return -1;
  }
  
  private XmlNode[] getNodesForInsert(Object paramObject) {
    if (paramObject instanceof XML)
      return new XmlNode[] { ((XML)paramObject).node }; 
    if (paramObject instanceof XMLList) {
      XMLList xMLList = (XMLList)paramObject;
      paramObject = new XmlNode[xMLList.length()];
      for (byte b = 0; b < xMLList.length(); b++)
        paramObject[b] = (xMLList.item(b)).node; 
      return (XmlNode[])paramObject;
    } 
    return new XmlNode[] { XmlNode.createText(getProcessor(), ScriptRuntime.toString(paramObject)) };
  }
  
  private XML toXML(XmlNode paramXmlNode) {
    if (paramXmlNode.getXml() == null)
      paramXmlNode.setXml(newXML(paramXmlNode)); 
    return paramXmlNode.getXml();
  }
  
  void addMatches(XMLList paramXMLList, XMLName paramXMLName) {
    paramXMLName.addMatches(paramXMLList, this);
  }
  
  XML addNamespace(Namespace paramNamespace) {
    addInScopeNamespace(paramNamespace);
    return this;
  }
  
  XML appendChild(Object paramObject) {
    if (this.node.isParentType()) {
      paramObject = getNodesForInsert(paramObject);
      XmlNode xmlNode = this.node;
      xmlNode.insertChildrenAt(xmlNode.getChildCount(), (XmlNode[])paramObject);
    } 
    return this;
  }
  
  XMLList child(int paramInt) {
    XMLList xMLList = newXMLList();
    xMLList.setTargets(this, null);
    if (paramInt >= 0 && paramInt < this.node.getChildCount())
      xMLList.addToList(getXmlChild(paramInt)); 
    return xMLList;
  }
  
  XMLList child(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
    for (byte b = 0; b < arrayOfXmlNode.length; b++) {
      if (paramXMLName.matchesElement(arrayOfXmlNode[b].getQname()))
        xMLList.addToList(toXML(arrayOfXmlNode[b])); 
    } 
    xMLList.setTargets(this, paramXMLName.toQname());
    return xMLList;
  }
  
  int childIndex() {
    return this.node.getChildIndex();
  }
  
  XMLList children() {
    XMLList xMLList = newXMLList();
    xMLList.setTargets(this, XMLName.formStar().toQname());
    XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
    for (byte b = 0; b < arrayOfXmlNode.length; b++)
      xMLList.addToList(toXML(arrayOfXmlNode[b])); 
    return xMLList;
  }
  
  XMLList comments() {
    XMLList xMLList = newXMLList();
    this.node.addMatchingChildren(xMLList, XmlNode.Filter.COMMENT);
    return xMLList;
  }
  
  boolean contains(Object paramObject) {
    return (paramObject instanceof XML) ? equivalentXml(paramObject) : false;
  }
  
  XMLObjectImpl copy() {
    return newXML(this.node.copy());
  }
  
  public void delete(int paramInt) {
    if (paramInt == 0)
      remove(); 
  }
  
  void deleteXMLProperty(XMLName paramXMLName) {
    XMLList xMLList = getPropertyList(paramXMLName);
    for (byte b = 0; b < xMLList.length(); b++)
      (xMLList.item(b)).node.deleteMe(); 
  }
  
  final String ecmaClass() {
    if (this.node.isTextType())
      return "text"; 
    if (this.node.isAttributeType())
      return "attribute"; 
    if (this.node.isCommentType())
      return "comment"; 
    if (this.node.isProcessingInstructionType())
      return "processing-instruction"; 
    if (this.node.isElementType())
      return "element"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unrecognized type: ");
    stringBuilder.append(this.node);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  XMLList elements(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    xMLList.setTargets(this, paramXMLName.toQname());
    XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
    for (byte b = 0; b < arrayOfXmlNode.length; b++) {
      if (paramXMLName.matches(toXML(arrayOfXmlNode[b])))
        xMLList.addToList(toXML(arrayOfXmlNode[b])); 
    } 
    return xMLList;
  }
  
  boolean equivalentXml(Object paramObject) {
    boolean bool = false;
    if (paramObject instanceof XML)
      return this.node.toXmlString(getProcessor()).equals(((XML)paramObject).node.toXmlString(getProcessor())); 
    if (paramObject instanceof XMLList) {
      paramObject = paramObject;
      if (paramObject.length() == 1)
        bool = equivalentXml(paramObject.getXML()); 
    } else if (hasSimpleContent()) {
      paramObject = ScriptRuntime.toString(paramObject);
      bool = toString().equals(paramObject);
    } 
    return bool;
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    return (paramInt == 0) ? this : Scriptable.NOT_FOUND;
  }
  
  XmlNode getAnnotation() {
    return this.node;
  }
  
  XML[] getAttributes() {
    XmlNode[] arrayOfXmlNode = this.node.getAttributes();
    XML[] arrayOfXML = new XML[arrayOfXmlNode.length];
    for (byte b = 0; b < arrayOfXML.length; b++)
      arrayOfXML[b] = toXML(arrayOfXmlNode[b]); 
    return arrayOfXML;
  }
  
  XML[] getChildren() {
    if (!isElement())
      return null; 
    XmlNode[] arrayOfXmlNode = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
    XML[] arrayOfXML = new XML[arrayOfXmlNode.length];
    for (byte b = 0; b < arrayOfXML.length; b++)
      arrayOfXML[b] = toXML(arrayOfXmlNode[b]); 
    return arrayOfXML;
  }
  
  public String getClassName() {
    return "XML";
  }
  
  public Scriptable getExtraMethodSource(Context paramContext) {
    return hasSimpleContent() ? ScriptRuntime.toObjectOrNull(paramContext, toString()) : null;
  }
  
  public Object[] getIds() {
    return isPrototype() ? new Object[0] : new Object[] { Integer.valueOf(0) };
  }
  
  XML getLastXmlChild() {
    int i = this.node.getChildCount() - 1;
    return (i < 0) ? null : getXmlChild(i);
  }
  
  XmlNode.QName getNodeQname() {
    return this.node.getQname();
  }
  
  XMLList getPropertyList(XMLName paramXMLName) {
    return paramXMLName.getMyValueOn(this);
  }
  
  final XML getXML() {
    return this;
  }
  
  Object getXMLProperty(XMLName paramXMLName) {
    return getPropertyList(paramXMLName);
  }
  
  XML getXmlChild(int paramInt) {
    XmlNode xmlNode = this.node.getChild(paramInt);
    if (xmlNode.getXml() == null)
      xmlNode.setXml(newXML(xmlNode)); 
    return xmlNode.getXml();
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasComplexContent() {
    return hasSimpleContent() ^ true;
  }
  
  boolean hasOwnProperty(XMLName paramXMLName) {
    boolean bool = isPrototype();
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool) {
      if (findPrototypeId(paramXMLName.localName()) == 0)
        bool2 = false; 
    } else if (getPropertyList(paramXMLName).length() > 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  boolean hasSimpleContent() {
    return (isComment() || isProcessingInstruction()) ? false : ((isText() || this.node.isAttributeType()) ? true : (this.node.hasChildElement() ^ true));
  }
  
  boolean hasXMLProperty(XMLName paramXMLName) {
    boolean bool;
    if (getPropertyList(paramXMLName).length() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  Namespace[] inScopeNamespaces() {
    return createNamespaces(this.node.getInScopeNamespaces());
  }
  
  void initialize(XmlNode paramXmlNode) {
    this.node = paramXmlNode;
    paramXmlNode.setXml(this);
  }
  
  XML insertChildAfter(XML paramXML, Object paramObject) {
    if (paramXML == null) {
      prependChild(paramObject);
    } else {
      paramObject = getNodesForInsert(paramObject);
      int i = getChildIndexOf(paramXML);
      if (i != -1)
        this.node.insertChildrenAt(i + 1, (XmlNode[])paramObject); 
    } 
    return this;
  }
  
  XML insertChildBefore(XML paramXML, Object paramObject) {
    if (paramXML == null) {
      appendChild(paramObject);
    } else {
      paramObject = getNodesForInsert(paramObject);
      int i = getChildIndexOf(paramXML);
      if (i != -1)
        this.node.insertChildrenAt(i, (XmlNode[])paramObject); 
    } 
    return this;
  }
  
  boolean is(XML paramXML) {
    return this.node.isSameNode(paramXML.node);
  }
  
  final boolean isAttribute() {
    return this.node.isAttributeType();
  }
  
  final boolean isComment() {
    return this.node.isCommentType();
  }
  
  final boolean isElement() {
    return this.node.isElementType();
  }
  
  final boolean isProcessingInstruction() {
    return this.node.isProcessingInstructionType();
  }
  
  final boolean isText() {
    return this.node.isTextType();
  }
  
  protected Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_3
    //   1: arraylength
    //   2: ifeq -> 22
    //   5: aload_3
    //   6: iconst_0
    //   7: aaload
    //   8: ifnull -> 22
    //   11: aload_3
    //   12: astore_1
    //   13: aload_3
    //   14: iconst_0
    //   15: aaload
    //   16: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   19: if_acmpne -> 33
    //   22: iconst_1
    //   23: anewarray java/lang/Object
    //   26: dup
    //   27: iconst_0
    //   28: ldc_w ''
    //   31: aastore
    //   32: astore_1
    //   33: aload_0
    //   34: aload_1
    //   35: iconst_0
    //   36: aaload
    //   37: invokevirtual ecmaToXml : (Ljava/lang/Object;)Lcom/trendmicro/hippo/xmlimpl/XML;
    //   40: astore_1
    //   41: iload_2
    //   42: ifeq -> 50
    //   45: aload_1
    //   46: invokevirtual copy : ()Lcom/trendmicro/hippo/xmlimpl/XMLObjectImpl;
    //   49: areturn
    //   50: aload_1
    //   51: areturn
  }
  
  int length() {
    return 1;
  }
  
  String localName() {
    return (name() == null) ? null : name().localName();
  }
  
  XML makeXmlFromString(XMLName paramXMLName, String paramString) {
    try {
      return newTextElementXML(this.node, paramXMLName.toQname(), paramString);
    } catch (Exception exception) {
      throw ScriptRuntime.typeError(exception.getMessage());
    } 
  }
  
  QName name() {
    return (isText() || isComment()) ? null : (isProcessingInstruction() ? newQName("", this.node.getQname().getLocalName(), null) : newQName(this.node.getQname()));
  }
  
  Namespace namespace(String paramString) {
    return (paramString == null) ? createNamespace(this.node.getNamespaceDeclaration()) : createNamespace(this.node.getNamespaceDeclaration(paramString));
  }
  
  Namespace[] namespaceDeclarations() {
    return createNamespaces(this.node.getNamespaceDeclarations());
  }
  
  Object nodeKind() {
    return ecmaClass();
  }
  
  void normalize() {
    this.node.normalize();
  }
  
  Object parent() {
    return (this.node.parent() == null) ? null : newXML(this.node.parent());
  }
  
  XML prependChild(Object paramObject) {
    if (this.node.isParentType())
      this.node.insertChildrenAt(0, getNodesForInsert(paramObject)); 
    return this;
  }
  
  XMLList processingInstructions(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    this.node.addMatchingChildren(xMLList, XmlNode.Filter.PROCESSING_INSTRUCTION(paramXMLName));
    return xMLList;
  }
  
  boolean propertyIsEnumerable(Object paramObject) {
    boolean bool1 = paramObject instanceof Integer;
    boolean bool2 = true;
    boolean bool3 = true;
    if (bool1) {
      if (((Integer)paramObject).intValue() != 0)
        bool3 = false; 
    } else if (paramObject instanceof Number) {
      double d = ((Number)paramObject).doubleValue();
      if (d == 0.0D && 1.0D / d > 0.0D) {
        bool3 = bool2;
      } else {
        bool3 = false;
      } 
    } else {
      bool3 = ScriptRuntime.toString(paramObject).equals("0");
    } 
    return bool3;
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    throw ScriptRuntime.typeError("Assignment to indexed XML is not allowed");
  }
  
  void putXMLProperty(XMLName paramXMLName, Object paramObject) {
    if (!isPrototype())
      paramXMLName.setMyValueOn(this, paramObject); 
  }
  
  void remove() {
    this.node.deleteMe();
  }
  
  void removeChild(int paramInt) {
    this.node.removeChild(paramInt);
  }
  
  XML removeNamespace(Namespace paramNamespace) {
    if (!isElement())
      return this; 
    this.node.removeNamespace(adapt(paramNamespace));
    return this;
  }
  
  XML replace(int paramInt, Object paramObject) {
    XMLList xMLList = child(paramInt);
    if (xMLList.length() > 0) {
      insertChildAfter(xMLList.item(0), paramObject);
      removeChild(paramInt);
    } 
    return this;
  }
  
  XML replace(XMLName paramXMLName, Object paramObject) {
    putXMLProperty(paramXMLName, paramObject);
    return this;
  }
  
  void replaceWith(XML paramXML) {
    if (this.node.parent() != null) {
      this.node.replaceWith(paramXML.node);
    } else {
      initialize(paramXML.node);
    } 
  }
  
  void setAttribute(XMLName paramXMLName, Object paramObject) {
    if (isElement()) {
      if (paramXMLName.uri() != null || !paramXMLName.localName().equals("*")) {
        this.node.setAttribute(paramXMLName.toQname(), ScriptRuntime.toString(paramObject));
        return;
      } 
      throw ScriptRuntime.typeError("@* assignment not supported.");
    } 
    throw new IllegalStateException("Can only set attributes on elements.");
  }
  
  XML setChildren(Object paramObject) {
    if (!isElement())
      return this; 
    while (this.node.getChildCount() > 0)
      this.node.removeChild(0); 
    paramObject = getNodesForInsert(paramObject);
    this.node.insertChildrenAt(0, (XmlNode[])paramObject);
    return this;
  }
  
  void setLocalName(String paramString) {
    if (isText() || isComment())
      return; 
    this.node.setLocalName(paramString);
  }
  
  void setName(QName paramQName) {
    if (isText() || isComment())
      return; 
    if (isProcessingInstruction()) {
      this.node.setLocalName(paramQName.localName());
      return;
    } 
    this.node.renameNode(paramQName.getDelegate());
  }
  
  void setNamespace(Namespace paramNamespace) {
    if (isText() || isComment() || isProcessingInstruction())
      return; 
    setName(newQName(paramNamespace.uri(), localName(), paramNamespace.prefix()));
  }
  
  XMLList text() {
    XMLList xMLList = newXMLList();
    this.node.addMatchingChildren(xMLList, XmlNode.Filter.TEXT);
    return xMLList;
  }
  
  Node toDomNode() {
    return this.node.toDomNode();
  }
  
  String toSource(int paramInt) {
    return toXMLString();
  }
  
  public String toString() {
    return ecmaToString();
  }
  
  String toXMLString() {
    return this.node.ecmaToXMLString(getProcessor());
  }
  
  Object valueOf() {
    return this;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */