package com.trendmicro.hippo.xmlimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

class XmlNode implements Serializable {
  private static final boolean DOM_LEVEL_3 = true;
  
  private static final String USER_DATA_XMLNODE_KEY = XmlNode.class.getName();
  
  private static final String XML_NAMESPACES_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
  
  private static final long serialVersionUID = 1L;
  
  private Node dom;
  
  private UserDataHandler events = new XmlNodeUserDataHandler();
  
  private XML xml;
  
  private void addNamespaces(Namespaces paramNamespaces, Element paramElement) {
    if (paramElement != null) {
      String str1 = toUri(paramElement.lookupNamespaceURI(null));
      String str2 = "";
      if (paramElement.getParentNode() != null)
        str2 = toUri(paramElement.getParentNode().lookupNamespaceURI(null)); 
      if (!str1.equals(str2) || !(paramElement.getParentNode() instanceof Element))
        paramNamespaces.declare(Namespace.create("", str1)); 
      NamedNodeMap namedNodeMap = paramElement.getAttributes();
      for (byte b = 0; b < namedNodeMap.getLength(); b++) {
        Attr attr = (Attr)namedNodeMap.item(b);
        if (attr.getPrefix() != null && attr.getPrefix().equals("xmlns"))
          paramNamespaces.declare(Namespace.create(attr.getLocalName(), attr.getValue())); 
      } 
      return;
    } 
    throw new RuntimeException("element must not be null");
  }
  
  private static XmlNode copy(XmlNode paramXmlNode) {
    return createImpl(paramXmlNode.dom.cloneNode(true));
  }
  
  static XmlNode createElement(XmlProcessor paramXmlProcessor, String paramString1, String paramString2) throws SAXException {
    return createImpl(paramXmlProcessor.toXml(paramString1, paramString2));
  }
  
  static XmlNode createElementFromNode(Node paramNode) {
    Node node = paramNode;
    if (paramNode instanceof Document)
      node = ((Document)paramNode).getDocumentElement(); 
    return createImpl(node);
  }
  
  static XmlNode createEmpty(XmlProcessor paramXmlProcessor) {
    return createText(paramXmlProcessor, "");
  }
  
  private static XmlNode createImpl(Node paramNode) {
    if (!(paramNode instanceof Document)) {
      XmlNode xmlNode;
      if (getUserData(paramNode) == null) {
        XmlNode xmlNode1 = new XmlNode();
        xmlNode1.dom = paramNode;
        setUserData(paramNode, xmlNode1);
        xmlNode = xmlNode1;
      } else {
        xmlNode = getUserData((Node)xmlNode);
      } 
      return xmlNode;
    } 
    throw new IllegalArgumentException();
  }
  
  static XmlNode createText(XmlProcessor paramXmlProcessor, String paramString) {
    return createImpl(paramXmlProcessor.newDocument().createTextNode(paramString));
  }
  
  private void declareNamespace(Element paramElement, String paramString1, String paramString2) {
    if (paramString1.length() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("xmlns:");
      stringBuilder.append(paramString1);
      paramElement.setAttributeNS("http://www.w3.org/2000/xmlns/", stringBuilder.toString(), paramString2);
    } else {
      paramElement.setAttribute("xmlns", paramString2);
    } 
  }
  
  private Namespaces getAllNamespaces() {
    Namespaces namespaces = new Namespaces();
    Node node1 = this.dom;
    Node node2 = node1;
    if (node1 instanceof Attr)
      node2 = ((Attr)node1).getOwnerElement(); 
    while (node2 != null) {
      if (node2 instanceof Element)
        addNamespaces(namespaces, (Element)node2); 
      node2 = node2.getParentNode();
    } 
    namespaces.declare(Namespace.create("", ""));
    return namespaces;
  }
  
  private Namespace getDefaultNamespace() {
    String str;
    if (this.dom.lookupNamespaceURI(null) == null) {
      str = "";
    } else {
      str = this.dom.lookupNamespaceURI(null);
    } 
    return Namespace.create("", str);
  }
  
  private String getExistingPrefixFor(Namespace paramNamespace) {
    return getDefaultNamespace().getUri().equals(paramNamespace.getUri()) ? "" : this.dom.lookupPrefix(paramNamespace.getUri());
  }
  
  private Namespace getNodeNamespace() {
    String str1 = this.dom.getNamespaceURI();
    String str2 = this.dom.getPrefix();
    String str3 = str1;
    if (str1 == null)
      str3 = ""; 
    str1 = str2;
    if (str2 == null)
      str1 = ""; 
    return Namespace.create(str1, str3);
  }
  
  private static XmlNode getUserData(Node paramNode) {
    return (XmlNode)paramNode.getUserData(USER_DATA_XMLNODE_KEY);
  }
  
  static XmlNode newElementWithText(XmlProcessor paramXmlProcessor, XmlNode paramXmlNode, QName paramQName, String paramString) {
    if (!(paramXmlNode instanceof Document)) {
      Document document;
      Node node;
      if (paramXmlNode != null) {
        document = paramXmlNode.dom.getOwnerDocument();
      } else {
        document = document.newDocument();
      } 
      if (paramXmlNode != null) {
        node = paramXmlNode.dom;
      } else {
        paramXmlNode = null;
      } 
      Namespace namespace = paramQName.getNamespace();
      if (namespace == null || namespace.getUri().length() == 0) {
        node = document.createElementNS(null, paramQName.getLocalName());
      } else {
        node = document.createElementNS(namespace.getUri(), paramQName.qualify(node));
      } 
      if (paramString != null)
        node.appendChild(document.createTextNode(paramString)); 
      return createImpl(node);
    } 
    throw new IllegalArgumentException("Cannot use Document node as reference");
  }
  
  private void setProcessingInstructionName(String paramString) {
    ProcessingInstruction processingInstruction = (ProcessingInstruction)this.dom;
    processingInstruction.getParentNode().replaceChild(processingInstruction, processingInstruction.getOwnerDocument().createProcessingInstruction(paramString, processingInstruction.getData()));
  }
  
  private static void setUserData(Node paramNode, XmlNode paramXmlNode) {
    paramNode.setUserData(USER_DATA_XMLNODE_KEY, paramXmlNode, paramXmlNode.events);
  }
  
  private String toUri(String paramString) {
    if (paramString == null)
      paramString = ""; 
    return paramString;
  }
  
  void addMatchingChildren(XMLList paramXMLList, Filter paramFilter) {
    NodeList nodeList = this.dom.getChildNodes();
    for (byte b = 0; b < nodeList.getLength(); b++) {
      Node node = nodeList.item(b);
      XmlNode xmlNode = createImpl(node);
      if (paramFilter.accept(node))
        paramXMLList.addToList(xmlNode); 
    } 
  }
  
  final XmlNode copy() {
    return copy(this);
  }
  
  String debug() {
    XmlProcessor xmlProcessor = new XmlProcessor();
    xmlProcessor.setIgnoreComments(false);
    xmlProcessor.setIgnoreProcessingInstructions(false);
    xmlProcessor.setIgnoreWhitespace(false);
    xmlProcessor.setPrettyPrinting(false);
    return xmlProcessor.ecmaToXmlString(this.dom);
  }
  
  void declareNamespace(String paramString1, String paramString2) {
    Node node = this.dom;
    if (node instanceof Element) {
      if (node.lookupNamespaceURI(paramString2) == null || !this.dom.lookupNamespaceURI(paramString2).equals(paramString1))
        declareNamespace((Element)this.dom, paramString1, paramString2); 
      return;
    } 
    throw new IllegalStateException();
  }
  
  void deleteMe() {
    Node node = this.dom;
    if (node instanceof Attr) {
      node = node;
      node.getOwnerElement().getAttributes().removeNamedItemNS(node.getNamespaceURI(), node.getLocalName());
    } else if (node.getParentNode() != null) {
      this.dom.getParentNode().removeChild(this.dom);
    } 
  }
  
  String ecmaToXMLString(XmlProcessor paramXmlProcessor) {
    if (isElementType()) {
      Element element = (Element)this.dom.cloneNode(true);
      Namespace[] arrayOfNamespace = getInScopeNamespaces();
      for (byte b = 0; b < arrayOfNamespace.length; b++)
        declareNamespace(element, arrayOfNamespace[b].getPrefix(), arrayOfNamespace[b].getUri()); 
      return paramXmlProcessor.ecmaToXmlString(element);
    } 
    return paramXmlProcessor.ecmaToXmlString(this.dom);
  }
  
  String ecmaValue() {
    if (isTextType())
      return ((Text)this.dom).getData(); 
    if (isAttributeType())
      return ((Attr)this.dom).getValue(); 
    if (isProcessingInstructionType())
      return ((ProcessingInstruction)this.dom).getData(); 
    if (isCommentType())
      return ((Comment)this.dom).getNodeValue(); 
    if (isElementType())
      throw new RuntimeException("Unimplemented ecmaValue() for elements."); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unimplemented for node ");
    stringBuilder.append(this.dom);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  String getAttributeValue() {
    return ((Attr)this.dom).getValue();
  }
  
  XmlNode[] getAttributes() {
    NamedNodeMap namedNodeMap = this.dom.getAttributes();
    if (namedNodeMap != null) {
      XmlNode[] arrayOfXmlNode = new XmlNode[namedNodeMap.getLength()];
      for (byte b = 0; b < namedNodeMap.getLength(); b++)
        arrayOfXmlNode[b] = createImpl(namedNodeMap.item(b)); 
      return arrayOfXmlNode;
    } 
    throw new IllegalStateException("Must be element.");
  }
  
  XmlNode getChild(int paramInt) {
    return createImpl(this.dom.getChildNodes().item(paramInt));
  }
  
  int getChildCount() {
    return this.dom.getChildNodes().getLength();
  }
  
  int getChildIndex() {
    if (isAttributeType())
      return -1; 
    if (parent() == null)
      return -1; 
    NodeList nodeList = this.dom.getParentNode().getChildNodes();
    for (byte b = 0; b < nodeList.getLength(); b++) {
      if (nodeList.item(b) == this.dom)
        return b; 
    } 
    throw new RuntimeException("Unreachable.");
  }
  
  Namespace[] getInScopeNamespaces() {
    return getAllNamespaces().getNamespaces();
  }
  
  XmlNode[] getMatchingChildren(Filter paramFilter) {
    ArrayList<XmlNode> arrayList = new ArrayList();
    NodeList nodeList = this.dom.getChildNodes();
    for (byte b = 0; b < nodeList.getLength(); b++) {
      Node node = nodeList.item(b);
      if (paramFilter.accept(node))
        arrayList.add(createImpl(node)); 
    } 
    return arrayList.<XmlNode>toArray(new XmlNode[arrayList.size()]);
  }
  
  Namespace getNamespace() {
    return getNodeNamespace();
  }
  
  Namespace getNamespaceDeclaration() {
    return (this.dom.getPrefix() == null) ? getNamespaceDeclaration("") : getNamespaceDeclaration(this.dom.getPrefix());
  }
  
  Namespace getNamespaceDeclaration(String paramString) {
    return (paramString.equals("") && this.dom instanceof Attr) ? Namespace.create("", "") : getAllNamespaces().getNamespace(paramString);
  }
  
  Namespace[] getNamespaceDeclarations() {
    if (this.dom instanceof Element) {
      Namespaces namespaces = new Namespaces();
      addNamespaces(namespaces, (Element)this.dom);
      return namespaces.getNamespaces();
    } 
    return new Namespace[0];
  }
  
  final QName getQname() {
    String str1 = this.dom.getNamespaceURI();
    String str2 = "";
    if (str1 == null) {
      str1 = "";
    } else {
      str1 = this.dom.getNamespaceURI();
    } 
    if (this.dom.getPrefix() != null)
      str2 = this.dom.getPrefix(); 
    return QName.create(str1, this.dom.getLocalName(), str2);
  }
  
  XML getXml() {
    return this.xml;
  }
  
  boolean hasChildElement() {
    NodeList nodeList = this.dom.getChildNodes();
    for (byte b = 0; b < nodeList.getLength(); b++) {
      if (nodeList.item(b).getNodeType() == 1)
        return true; 
    } 
    return false;
  }
  
  void insertChildAt(int paramInt, XmlNode paramXmlNode) {
    Node node2 = this.dom;
    Node node1 = node2.getOwnerDocument().importNode(paramXmlNode.dom, true);
    if (node2.getChildNodes().getLength() >= paramInt) {
      if (node2.getChildNodes().getLength() == paramInt) {
        node2.appendChild(node1);
      } else {
        node2.insertBefore(node1, node2.getChildNodes().item(paramInt));
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("index=");
    stringBuilder.append(paramInt);
    stringBuilder.append(" length=");
    stringBuilder.append(node2.getChildNodes().getLength());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  void insertChildrenAt(int paramInt, XmlNode[] paramArrayOfXmlNode) {
    for (byte b = 0; b < paramArrayOfXmlNode.length; b++)
      insertChildAt(paramInt + b, paramArrayOfXmlNode[b]); 
  }
  
  void invalidateNamespacePrefix() {
    Node node = this.dom;
    if (node instanceof Element) {
      String str = node.getPrefix();
      renameNode(QName.create(this.dom.getNamespaceURI(), this.dom.getLocalName(), null));
      NamedNodeMap namedNodeMap = this.dom.getAttributes();
      for (byte b = 0; b < namedNodeMap.getLength(); b++) {
        if (namedNodeMap.item(b).getPrefix().equals(str))
          createImpl(namedNodeMap.item(b)).renameNode(QName.create(namedNodeMap.item(b).getNamespaceURI(), namedNodeMap.item(b).getLocalName(), null)); 
      } 
      return;
    } 
    throw new IllegalStateException();
  }
  
  final boolean isAttributeType() {
    boolean bool;
    if (this.dom.getNodeType() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  final boolean isCommentType() {
    boolean bool;
    if (this.dom.getNodeType() == 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  final boolean isElementType() {
    short s = this.dom.getNodeType();
    boolean bool = true;
    if (s != 1)
      bool = false; 
    return bool;
  }
  
  final boolean isParentType() {
    return isElementType();
  }
  
  final boolean isProcessingInstructionType() {
    boolean bool;
    if (this.dom.getNodeType() == 7) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean isSameNode(XmlNode paramXmlNode) {
    boolean bool;
    if (this.dom == paramXmlNode.dom) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  final boolean isTextType() {
    return (this.dom.getNodeType() == 3 || this.dom.getNodeType() == 4);
  }
  
  void normalize() {
    this.dom.normalize();
  }
  
  XmlNode parent() {
    Node node = this.dom.getParentNode();
    return (node instanceof Document) ? null : ((node == null) ? null : createImpl(node));
  }
  
  void removeChild(int paramInt) {
    Node node = this.dom;
    node.removeChild(node.getChildNodes().item(paramInt));
  }
  
  void removeNamespace(Namespace paramNamespace) {
    if (paramNamespace.is(getNodeNamespace()))
      return; 
    NamedNodeMap namedNodeMap = this.dom.getAttributes();
    for (byte b = 0; b < namedNodeMap.getLength(); b++) {
      if (paramNamespace.is(createImpl(namedNodeMap.item(b)).getNodeNamespace()))
        return; 
    } 
    String str = getExistingPrefixFor(paramNamespace);
    if (str != null)
      if (paramNamespace.isUnspecifiedPrefix()) {
        declareNamespace(str, getDefaultNamespace().getUri());
      } else if (str.equals(paramNamespace.getPrefix())) {
        declareNamespace(str, getDefaultNamespace().getUri());
      }  
  }
  
  final void renameNode(QName paramQName) {
    this.dom = this.dom.getOwnerDocument().renameNode(this.dom, paramQName.getNamespace().getUri(), paramQName.qualify(this.dom));
  }
  
  void replaceWith(XmlNode paramXmlNode) {
    Node node2 = paramXmlNode.dom;
    Node node1 = node2;
    if (node2.getOwnerDocument() != this.dom.getOwnerDocument())
      node1 = this.dom.getOwnerDocument().importNode(node2, true); 
    this.dom.getParentNode().replaceChild(node1, this.dom);
  }
  
  void setAttribute(QName paramQName, String paramString) {
    Node node = this.dom;
    if (node instanceof Element) {
      paramQName.setAttribute((Element)node, paramString);
      return;
    } 
    throw new IllegalStateException("Can only set attribute on elements.");
  }
  
  final void setLocalName(String paramString) {
    Node node = this.dom;
    if (node instanceof ProcessingInstruction) {
      setProcessingInstructionName(paramString);
    } else {
      String str2 = node.getPrefix();
      String str1 = str2;
      if (str2 == null)
        str1 = ""; 
      Document document = this.dom.getOwnerDocument();
      Node node1 = this.dom;
      this.dom = document.renameNode(node1, node1.getNamespaceURI(), QName.qualify(str1, paramString));
    } 
  }
  
  void setXml(XML paramXML) {
    this.xml = paramXML;
  }
  
  Node toDomNode() {
    return this.dom;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("XmlNode: type=");
    stringBuilder.append(this.dom.getNodeType());
    stringBuilder.append(" dom=");
    stringBuilder.append(this.dom.toString());
    return stringBuilder.toString();
  }
  
  String toXmlString(XmlProcessor paramXmlProcessor) {
    return paramXmlProcessor.ecmaToXmlString(this.dom);
  }
  
  static abstract class Filter {
    static final Filter COMMENT = new Filter() {
        boolean accept(Node param2Node) {
          boolean bool;
          if (param2Node.getNodeType() == 8) {
            bool = true;
          } else {
            bool = false;
          } 
          return bool;
        }
      };
    
    static Filter ELEMENT = new Filter() {
        boolean accept(Node param2Node) {
          short s = param2Node.getNodeType();
          boolean bool = true;
          if (s != 1)
            bool = false; 
          return bool;
        }
      };
    
    static final Filter TEXT = new Filter() {
        boolean accept(Node param2Node) {
          boolean bool;
          if (param2Node.getNodeType() == 3) {
            bool = true;
          } else {
            bool = false;
          } 
          return bool;
        }
      };
    
    static Filter TRUE = new Filter() {
        boolean accept(Node param2Node) {
          return true;
        }
      };
    
    static {
    
    }
    
    static Filter PROCESSING_INSTRUCTION(final XMLName name) {
      return new Filter() {
          boolean accept(Node param2Node) {
            if (param2Node.getNodeType() == 7) {
              param2Node = param2Node;
              return name.matchesLocalName(param2Node.getTarget());
            } 
            return false;
          }
        };
    }
    
    abstract boolean accept(Node param1Node);
  }
  
  class null extends Filter {
    boolean accept(Node param1Node) {
      boolean bool;
      if (param1Node.getNodeType() == 8) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  class null extends Filter {
    boolean accept(Node param1Node) {
      boolean bool;
      if (param1Node.getNodeType() == 3) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  class null extends Filter {
    boolean accept(Node param1Node) {
      if (param1Node.getNodeType() == 7) {
        param1Node = param1Node;
        return name.matchesLocalName(param1Node.getTarget());
      } 
      return false;
    }
  }
  
  class null extends Filter {
    boolean accept(Node param1Node) {
      short s = param1Node.getNodeType();
      boolean bool = true;
      if (s != 1)
        bool = false; 
      return bool;
    }
  }
  
  class null extends Filter {
    boolean accept(Node param1Node) {
      return true;
    }
  }
  
  static class InternalList implements Serializable {
    private static final long serialVersionUID = -3633151157292048978L;
    
    private List<XmlNode> list = new ArrayList<>();
    
    private void _add(XmlNode param1XmlNode) {
      this.list.add(param1XmlNode);
    }
    
    void add(XML param1XML) {
      _add(param1XML.getAnnotation());
    }
    
    void add(InternalList param1InternalList) {
      for (byte b = 0; b < param1InternalList.length(); b++)
        _add(param1InternalList.item(b)); 
    }
    
    void add(InternalList param1InternalList, int param1Int1, int param1Int2) {
      while (param1Int1 < param1Int2) {
        _add(param1InternalList.item(param1Int1));
        param1Int1++;
      } 
    }
    
    void add(XmlNode param1XmlNode) {
      _add(param1XmlNode);
    }
    
    void addToList(Object param1Object) {
      if (param1Object instanceof com.trendmicro.hippo.Undefined)
        return; 
      if (param1Object instanceof XMLList) {
        param1Object = param1Object;
        for (byte b = 0; b < param1Object.length(); b++)
          _add(param1Object.item(b).getAnnotation()); 
      } else if (param1Object instanceof XML) {
        _add(((XML)param1Object).getAnnotation());
      } else if (param1Object instanceof XmlNode) {
        _add((XmlNode)param1Object);
      } 
    }
    
    XmlNode item(int param1Int) {
      return this.list.get(param1Int);
    }
    
    int length() {
      return this.list.size();
    }
    
    void remove(int param1Int) {
      this.list.remove(param1Int);
    }
  }
  
  static class Namespace implements Serializable {
    static final Namespace GLOBAL = create("", "");
    
    private static final long serialVersionUID = 4073904386884677090L;
    
    private String prefix;
    
    private String uri;
    
    static Namespace create(String param1String) {
      Namespace namespace = new Namespace();
      namespace.uri = param1String;
      if (param1String == null || param1String.length() == 0)
        namespace.prefix = ""; 
      return namespace;
    }
    
    static Namespace create(String param1String1, String param1String2) {
      if (param1String1 != null) {
        if (param1String2 != null) {
          Namespace namespace = new Namespace();
          namespace.prefix = param1String1;
          namespace.uri = param1String2;
          return namespace;
        } 
        throw new IllegalArgumentException("Namespace may not lack a URI");
      } 
      throw new IllegalArgumentException("Empty string represents default namespace prefix");
    }
    
    private void setPrefix(String param1String) {
      if (param1String != null) {
        this.prefix = param1String;
        return;
      } 
      throw new IllegalArgumentException();
    }
    
    String getPrefix() {
      return this.prefix;
    }
    
    String getUri() {
      return this.uri;
    }
    
    boolean is(Namespace param1Namespace) {
      String str = this.prefix;
      if (str != null) {
        String str1 = param1Namespace.prefix;
        if (str1 != null && str.equals(str1) && this.uri.equals(param1Namespace.uri))
          return true; 
      } 
      return false;
    }
    
    boolean isDefault() {
      boolean bool;
      String str = this.prefix;
      if (str != null && str.equals("")) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isEmpty() {
      boolean bool;
      String str = this.prefix;
      if (str != null && str.equals("") && this.uri.equals("")) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isGlobal() {
      boolean bool;
      String str = this.uri;
      if (str != null && str.equals("")) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isUnspecifiedPrefix() {
      boolean bool;
      if (this.prefix == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public String toString() {
      if (this.prefix == null) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("XmlNode.Namespace [");
        stringBuilder1.append(this.uri);
        stringBuilder1.append("]");
        return stringBuilder1.toString();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("XmlNode.Namespace [");
      stringBuilder.append(this.prefix);
      stringBuilder.append("{");
      stringBuilder.append(this.uri);
      stringBuilder.append("}]");
      return stringBuilder.toString();
    }
  }
  
  private static class Namespaces {
    private Map<String, String> map = new HashMap<>();
    
    private Map<String, String> uriToPrefix = new HashMap<>();
    
    void declare(XmlNode.Namespace param1Namespace) {
      if (this.map.get(param1Namespace.prefix) == null)
        this.map.put(param1Namespace.prefix, param1Namespace.uri); 
      if (this.uriToPrefix.get(param1Namespace.uri) == null)
        this.uriToPrefix.put(param1Namespace.uri, param1Namespace.prefix); 
    }
    
    XmlNode.Namespace getNamespace(String param1String) {
      return (this.map.get(param1String) == null) ? null : XmlNode.Namespace.create(param1String, this.map.get(param1String));
    }
    
    XmlNode.Namespace getNamespaceByUri(String param1String) {
      return (this.uriToPrefix.get(param1String) == null) ? null : XmlNode.Namespace.create(param1String, this.uriToPrefix.get(param1String));
    }
    
    XmlNode.Namespace[] getNamespaces() {
      ArrayList<XmlNode.Namespace> arrayList = new ArrayList();
      for (String str : this.map.keySet()) {
        XmlNode.Namespace namespace = XmlNode.Namespace.create(str, this.map.get(str));
        if (!namespace.isEmpty())
          arrayList.add(namespace); 
      } 
      return arrayList.<XmlNode.Namespace>toArray(new XmlNode.Namespace[arrayList.size()]);
    }
  }
  
  static class QName implements Serializable {
    private static final long serialVersionUID = -6587069811691451077L;
    
    private String localName;
    
    private XmlNode.Namespace namespace;
    
    static QName create(XmlNode.Namespace param1Namespace, String param1String) {
      if (param1String == null || !param1String.equals("*")) {
        QName qName = new QName();
        qName.namespace = param1Namespace;
        qName.localName = param1String;
        return qName;
      } 
      throw new RuntimeException("* is not valid localName");
    }
    
    @Deprecated
    static QName create(String param1String1, String param1String2, String param1String3) {
      return create(XmlNode.Namespace.create(param1String3, param1String1), param1String2);
    }
    
    private boolean equals(String param1String1, String param1String2) {
      return (param1String1 == null && param1String2 == null) ? true : ((param1String1 == null || param1String2 == null) ? false : param1String1.equals(param1String2));
    }
    
    private boolean namespacesEqual(XmlNode.Namespace param1Namespace1, XmlNode.Namespace param1Namespace2) {
      return (param1Namespace1 == null && param1Namespace2 == null) ? true : ((param1Namespace1 == null || param1Namespace2 == null) ? false : equals(param1Namespace1.getUri(), param1Namespace2.getUri()));
    }
    
    static String qualify(String param1String1, String param1String2) {
      if (param1String1 != null) {
        if (param1String1.length() > 0) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(param1String1);
          stringBuilder.append(":");
          stringBuilder.append(param1String2);
          return stringBuilder.toString();
        } 
        return param1String2;
      } 
      throw new IllegalArgumentException("prefix must not be null");
    }
    
    final boolean equals(QName param1QName) {
      return !namespacesEqual(this.namespace, param1QName.namespace) ? false : (!!equals(this.localName, param1QName.localName));
    }
    
    public boolean equals(Object param1Object) {
      return !(param1Object instanceof QName) ? false : equals((QName)param1Object);
    }
    
    String getLocalName() {
      return this.localName;
    }
    
    XmlNode.Namespace getNamespace() {
      return this.namespace;
    }
    
    public int hashCode() {
      int i;
      String str = this.localName;
      if (str == null) {
        i = 0;
      } else {
        i = str.hashCode();
      } 
      return i;
    }
    
    void lookupPrefix(Node param1Node) {
      if (param1Node != null) {
        String str1 = param1Node.lookupPrefix(this.namespace.getUri());
        String str2 = str1;
        if (str1 == null) {
          str2 = param1Node.lookupNamespaceURI(null);
          String str = str2;
          if (str2 == null)
            str = ""; 
          str2 = str1;
          if (this.namespace.getUri().equals(str))
            str2 = ""; 
        } 
        for (byte b = 0; str2 == null; b++) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("e4x_");
          stringBuilder.append(b);
          String str = stringBuilder.toString();
          if (param1Node.lookupNamespaceURI(str) == null) {
            str2 = str;
            Node node;
            for (node = param1Node; node.getParentNode() != null && node.getParentNode() instanceof Element; node = node.getParentNode());
            node = node;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("xmlns:");
            stringBuilder1.append(str2);
            node.setAttributeNS("http://www.w3.org/2000/xmlns/", stringBuilder1.toString(), this.namespace.getUri());
          } 
        } 
        this.namespace.setPrefix(str2);
        return;
      } 
      throw new IllegalArgumentException("node must not be null");
    }
    
    String qualify(Node param1Node) {
      if (this.namespace.getPrefix() == null)
        if (param1Node != null) {
          lookupPrefix(param1Node);
        } else if (this.namespace.getUri().equals("")) {
          this.namespace.setPrefix("");
        } else {
          this.namespace.setPrefix("");
        }  
      return qualify(this.namespace.getPrefix(), this.localName);
    }
    
    void setAttribute(Element param1Element, String param1String) {
      if (this.namespace.getPrefix() == null)
        lookupPrefix(param1Element); 
      param1Element.setAttributeNS(this.namespace.getUri(), qualify(this.namespace.getPrefix(), this.localName), param1String);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("XmlNode.QName [");
      stringBuilder.append(this.localName);
      stringBuilder.append(",");
      stringBuilder.append(this.namespace);
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  static class XmlNodeUserDataHandler implements UserDataHandler, Serializable {
    private static final long serialVersionUID = 4666895518900769588L;
    
    public void handle(short param1Short, String param1String, Object param1Object, Node param1Node1, Node param1Node2) {}
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XmlNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */