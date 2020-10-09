package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ScriptRuntime;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class XmlProcessor implements Serializable {
  private static final long serialVersionUID = 6903514433204808713L;
  
  private transient LinkedBlockingDeque<DocumentBuilder> documentBuilderPool;
  
  private transient DocumentBuilderFactory dom;
  
  private HippoSAXErrorHandler errorHandler = new HippoSAXErrorHandler();
  
  private boolean ignoreComments;
  
  private boolean ignoreProcessingInstructions;
  
  private boolean ignoreWhitespace;
  
  private int prettyIndent;
  
  private boolean prettyPrint;
  
  private transient TransformerFactory xform;
  
  XmlProcessor() {
    setDefault();
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    this.dom = documentBuilderFactory;
    documentBuilderFactory.setNamespaceAware(true);
    this.dom.setIgnoringComments(false);
    this.xform = TransformerFactory.newInstance();
    this.documentBuilderPool = new LinkedBlockingDeque<>(Runtime.getRuntime().availableProcessors() * 2);
  }
  
  private void addCommentsTo(List<Node> paramList, Node paramNode) {
    if (paramNode instanceof Comment)
      paramList.add(paramNode); 
    if (paramNode.getChildNodes() != null)
      for (byte b = 0; b < paramNode.getChildNodes().getLength(); b++)
        addProcessingInstructionsTo(paramList, paramNode.getChildNodes().item(b));  
  }
  
  private void addProcessingInstructionsTo(List<Node> paramList, Node paramNode) {
    if (paramNode instanceof ProcessingInstruction)
      paramList.add(paramNode); 
    if (paramNode.getChildNodes() != null)
      for (byte b = 0; b < paramNode.getChildNodes().getLength(); b++)
        addProcessingInstructionsTo(paramList, paramNode.getChildNodes().item(b));  
  }
  
  private void addTextNodesToRemoveAndTrim(List<Node> paramList, Node paramNode) {
    if (paramNode instanceof Text) {
      Text text = (Text)paramNode;
      if (!false) {
        text.setData(text.getData().trim());
      } else if (text.getData().trim().length() == 0) {
        text.setData("");
      } 
      if (text.getData().length() == 0)
        paramList.add(paramNode); 
    } 
    if (paramNode.getChildNodes() != null)
      for (byte b = 0; b < paramNode.getChildNodes().getLength(); b++)
        addTextNodesToRemoveAndTrim(paramList, paramNode.getChildNodes().item(b));  
  }
  
  private void beautifyElement(Element paramElement, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('\n');
    byte b1;
    for (b1 = 0; b1 < paramInt; b1++)
      stringBuilder.append(' '); 
    String str1 = stringBuilder.toString();
    for (b1 = 0; b1 < this.prettyIndent; b1++)
      stringBuilder.append(' '); 
    String str2 = stringBuilder.toString();
    ArrayList<Node> arrayList = new ArrayList();
    b1 = 0;
    byte b2;
    for (b2 = 0; b2 < paramElement.getChildNodes().getLength(); b2++) {
      if (b2 == 1)
        b1 = 1; 
      if (paramElement.getChildNodes().item(b2) instanceof Text) {
        arrayList.add(paramElement.getChildNodes().item(b2));
      } else {
        b1 = 1;
        arrayList.add(paramElement.getChildNodes().item(b2));
      } 
    } 
    if (b1 != 0)
      for (b2 = 0; b2 < arrayList.size(); b2++)
        paramElement.insertBefore(paramElement.getOwnerDocument().createTextNode(str2), arrayList.get(b2));  
    NodeList nodeList = paramElement.getChildNodes();
    arrayList = new ArrayList<>();
    for (b2 = 0; b2 < nodeList.getLength(); b2++) {
      if (nodeList.item(b2) instanceof Element)
        arrayList.add(nodeList.item(b2)); 
    } 
    Iterator<Node> iterator = arrayList.iterator();
    while (iterator.hasNext())
      beautifyElement((Element)iterator.next(), this.prettyIndent + paramInt); 
    if (b1 != 0)
      paramElement.appendChild(paramElement.getOwnerDocument().createTextNode(str1)); 
  }
  
  private String elementToXmlString(Element paramElement) {
    paramElement = (Element)paramElement.cloneNode(true);
    if (this.prettyPrint)
      beautifyElement(paramElement, 0); 
    return toString(paramElement);
  }
  
  private String escapeElementValue(String paramString) {
    return escapeTextValue(paramString);
  }
  
  private DocumentBuilder getDocumentBuilderFromPool() throws ParserConfigurationException {
    DocumentBuilder documentBuilder1 = this.documentBuilderPool.pollFirst();
    DocumentBuilder documentBuilder2 = documentBuilder1;
    if (documentBuilder1 == null)
      documentBuilder2 = getDomFactory().newDocumentBuilder(); 
    documentBuilder2.setErrorHandler(this.errorHandler);
    return documentBuilder2;
  }
  
  private DocumentBuilderFactory getDomFactory() {
    return this.dom;
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    this.dom = documentBuilderFactory;
    documentBuilderFactory.setNamespaceAware(true);
    this.dom.setIgnoringComments(false);
    this.xform = TransformerFactory.newInstance();
    this.documentBuilderPool = new LinkedBlockingDeque<>(Runtime.getRuntime().availableProcessors() * 2);
  }
  
  private void returnDocumentBuilderToPool(DocumentBuilder paramDocumentBuilder) {
    try {
      paramDocumentBuilder.reset();
      this.documentBuilderPool.offerFirst(paramDocumentBuilder);
    } catch (UnsupportedOperationException unsupportedOperationException) {}
  }
  
  private String toString(Node paramNode) {
    DOMSource dOMSource = new DOMSource(paramNode);
    StringWriter stringWriter = new StringWriter();
    StreamResult streamResult = new StreamResult(stringWriter);
    try {
      Transformer transformer = this.xform.newTransformer();
      transformer.setOutputProperty("omit-xml-declaration", "yes");
      transformer.setOutputProperty("indent", "no");
      transformer.setOutputProperty("method", "xml");
      transformer.transform(dOMSource, streamResult);
      return toXmlNewlines(stringWriter.toString());
    } catch (TransformerConfigurationException transformerConfigurationException) {
      throw new RuntimeException(transformerConfigurationException);
    } catch (TransformerException transformerException) {
      throw new RuntimeException(transformerException);
    } 
  }
  
  private String toXmlNewlines(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramString.length(); b++) {
      if (paramString.charAt(b) == '\r') {
        if (paramString.charAt(b + 1) != '\n')
          stringBuilder.append('\n'); 
      } else {
        stringBuilder.append(paramString.charAt(b));
      } 
    } 
    return stringBuilder.toString();
  }
  
  final String ecmaToXmlString(Node paramNode) {
    String str;
    ProcessingInstruction processingInstruction;
    StringBuilder stringBuilder = new StringBuilder();
    if (this.prettyPrint)
      for (byte b = 0; b; b++)
        stringBuilder.append(' ');  
    if (paramNode instanceof Text) {
      str = ((Text)paramNode).getData();
      if (this.prettyPrint)
        str = str.trim(); 
      stringBuilder.append(escapeElementValue(str));
      return stringBuilder.toString();
    } 
    if (str instanceof Attr) {
      stringBuilder.append(escapeAttributeValue(((Attr)str).getValue()));
      return stringBuilder.toString();
    } 
    if (str instanceof Comment) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("<!--");
      stringBuilder1.append(((Comment)str).getNodeValue());
      stringBuilder1.append("-->");
      stringBuilder.append(stringBuilder1.toString());
      return stringBuilder.toString();
    } 
    if (str instanceof ProcessingInstruction) {
      processingInstruction = (ProcessingInstruction)str;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("<?");
      stringBuilder1.append(processingInstruction.getTarget());
      stringBuilder1.append(" ");
      stringBuilder1.append(processingInstruction.getData());
      stringBuilder1.append("?>");
      stringBuilder.append(stringBuilder1.toString());
      return stringBuilder.toString();
    } 
    stringBuilder.append(elementToXmlString((Element)processingInstruction));
    return stringBuilder.toString();
  }
  
  String escapeAttributeValue(Object paramObject) {
    String str = ScriptRuntime.toString(paramObject);
    if (str.length() == 0)
      return ""; 
    paramObject = newDocument().createElement("a");
    paramObject.setAttribute("b", str);
    paramObject = toString((Node)paramObject);
    return paramObject.substring(paramObject.indexOf('"') + 1, paramObject.lastIndexOf('"'));
  }
  
  String escapeTextValue(Object paramObject) {
    if (paramObject instanceof XMLObjectImpl)
      return ((XMLObjectImpl)paramObject).toXMLString(); 
    paramObject = ScriptRuntime.toString(paramObject);
    if (paramObject.length() == 0)
      return (String)paramObject; 
    Element element = newDocument().createElement("a");
    element.setTextContent((String)paramObject);
    paramObject = toString(element);
    int i = paramObject.indexOf('>') + 1;
    int j = paramObject.lastIndexOf('<');
    if (i < j) {
      paramObject = paramObject.substring(i, j);
    } else {
      paramObject = "";
    } 
    return (String)paramObject;
  }
  
  final int getPrettyIndent() {
    return this.prettyIndent;
  }
  
  final boolean isIgnoreComments() {
    return this.ignoreComments;
  }
  
  final boolean isIgnoreProcessingInstructions() {
    return this.ignoreProcessingInstructions;
  }
  
  final boolean isIgnoreWhitespace() {
    return this.ignoreWhitespace;
  }
  
  final boolean isPrettyPrinting() {
    return this.prettyPrint;
  }
  
  Document newDocument() {
    DocumentBuilder documentBuilder1 = null;
    DocumentBuilder documentBuilder2 = null;
    try {
      DocumentBuilder documentBuilder = getDocumentBuilderFromPool();
      documentBuilder2 = documentBuilder;
      documentBuilder1 = documentBuilder;
      Document document = documentBuilder.newDocument();
      if (documentBuilder != null)
        returnDocumentBuilderToPool(documentBuilder); 
      return document;
    } catch (ParserConfigurationException parserConfigurationException) {
      documentBuilder2 = documentBuilder1;
      RuntimeException runtimeException = new RuntimeException();
      documentBuilder2 = documentBuilder1;
      this(parserConfigurationException);
      documentBuilder2 = documentBuilder1;
      throw runtimeException;
    } finally {}
    if (documentBuilder2 != null)
      returnDocumentBuilderToPool(documentBuilder2); 
    throw documentBuilder1;
  }
  
  final void setDefault() {
    setIgnoreComments(true);
    setIgnoreProcessingInstructions(true);
    setIgnoreWhitespace(true);
    setPrettyPrinting(true);
    setPrettyIndent(2);
  }
  
  final void setIgnoreComments(boolean paramBoolean) {
    this.ignoreComments = paramBoolean;
  }
  
  final void setIgnoreProcessingInstructions(boolean paramBoolean) {
    this.ignoreProcessingInstructions = paramBoolean;
  }
  
  final void setIgnoreWhitespace(boolean paramBoolean) {
    this.ignoreWhitespace = paramBoolean;
  }
  
  final void setPrettyIndent(int paramInt) {
    this.prettyIndent = paramInt;
  }
  
  final void setPrettyPrinting(boolean paramBoolean) {
    this.prettyPrint = paramBoolean;
  }
  
  final Node toXml(String paramString1, String paramString2) throws SAXException {
    DocumentBuilder documentBuilder1 = null;
    DocumentBuilder documentBuilder2 = null;
    DocumentBuilder documentBuilder3 = null;
    DocumentBuilder documentBuilder4 = documentBuilder3;
    DocumentBuilder documentBuilder5 = documentBuilder1;
    DocumentBuilder documentBuilder6 = documentBuilder2;
    try {
      StringBuilder stringBuilder = new StringBuilder();
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      this();
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      stringBuilder.append("<parent xmlns=\"");
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      stringBuilder.append(paramString1);
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      stringBuilder.append("\">");
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      stringBuilder.append(paramString2);
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      stringBuilder.append("</parent>");
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      paramString2 = stringBuilder.toString();
      documentBuilder4 = documentBuilder3;
      documentBuilder5 = documentBuilder1;
      documentBuilder6 = documentBuilder2;
      DocumentBuilder documentBuilder = getDocumentBuilderFromPool();
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      InputSource inputSource = new InputSource();
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      StringReader stringReader = new StringReader();
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      this(paramString2);
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      this(stringReader);
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      Document document = documentBuilder.parse(inputSource);
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      if (this.ignoreProcessingInstructions) {
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        ArrayList<Node> arrayList = new ArrayList();
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        this();
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        addProcessingInstructionsTo(arrayList, document);
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        Iterator<Node> iterator = arrayList.iterator();
        while (true) {
          documentBuilder4 = documentBuilder;
          documentBuilder5 = documentBuilder;
          documentBuilder6 = documentBuilder;
          if (iterator.hasNext()) {
            documentBuilder4 = documentBuilder;
            documentBuilder5 = documentBuilder;
            documentBuilder6 = documentBuilder;
            Node node = iterator.next();
            documentBuilder4 = documentBuilder;
            documentBuilder5 = documentBuilder;
            documentBuilder6 = documentBuilder;
            node.getParentNode().removeChild(node);
            continue;
          } 
          break;
        } 
      } 
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      if (this.ignoreComments) {
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        ArrayList<Node> arrayList = new ArrayList();
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        this();
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        addCommentsTo(arrayList, document);
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        Iterator<Node> iterator = arrayList.iterator();
        while (true) {
          documentBuilder4 = documentBuilder;
          documentBuilder5 = documentBuilder;
          documentBuilder6 = documentBuilder;
          if (iterator.hasNext()) {
            documentBuilder4 = documentBuilder;
            documentBuilder5 = documentBuilder;
            documentBuilder6 = documentBuilder;
            Node node = iterator.next();
            documentBuilder4 = documentBuilder;
            documentBuilder5 = documentBuilder;
            documentBuilder6 = documentBuilder;
            node.getParentNode().removeChild(node);
            continue;
          } 
          break;
        } 
      } 
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      if (this.ignoreWhitespace) {
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        ArrayList<Node> arrayList = new ArrayList();
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        this();
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        addTextNodesToRemoveAndTrim(arrayList, document);
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        Iterator<Node> iterator = arrayList.iterator();
        while (true) {
          documentBuilder4 = documentBuilder;
          documentBuilder5 = documentBuilder;
          documentBuilder6 = documentBuilder;
          if (iterator.hasNext()) {
            documentBuilder4 = documentBuilder;
            documentBuilder5 = documentBuilder;
            documentBuilder6 = documentBuilder;
            Node node = iterator.next();
            documentBuilder4 = documentBuilder;
            documentBuilder5 = documentBuilder;
            documentBuilder6 = documentBuilder;
            node.getParentNode().removeChild(node);
            continue;
          } 
          break;
        } 
      } 
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      NodeList nodeList = document.getDocumentElement().getChildNodes();
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      if (nodeList.getLength() <= 1) {
        Text text;
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        if (nodeList.getLength() == 0) {
          documentBuilder4 = documentBuilder;
          documentBuilder5 = documentBuilder;
          documentBuilder6 = documentBuilder;
          text = document.createTextNode("");
          if (documentBuilder != null)
            returnDocumentBuilderToPool(documentBuilder); 
          return text;
        } 
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        Node node = nodeList.item(0);
        documentBuilder4 = documentBuilder;
        documentBuilder5 = documentBuilder;
        documentBuilder6 = documentBuilder;
        text.getDocumentElement().removeChild(node);
        if (documentBuilder != null)
          returnDocumentBuilderToPool(documentBuilder); 
        return node;
      } 
      documentBuilder4 = documentBuilder;
      documentBuilder5 = documentBuilder;
      documentBuilder6 = documentBuilder;
      throw ScriptRuntime.constructError("SyntaxError", "XML objects may contain at most one node.");
    } catch (IOException iOException) {
      documentBuilder4 = documentBuilder6;
      RuntimeException runtimeException = new RuntimeException();
      documentBuilder4 = documentBuilder6;
      this("Unreachable.");
      documentBuilder4 = documentBuilder6;
      throw runtimeException;
    } catch (ParserConfigurationException parserConfigurationException) {
      documentBuilder4 = documentBuilder5;
      RuntimeException runtimeException = new RuntimeException();
      documentBuilder4 = documentBuilder5;
      this(parserConfigurationException);
      documentBuilder4 = documentBuilder5;
      throw runtimeException;
    } finally {}
    if (documentBuilder4 != null)
      returnDocumentBuilderToPool(documentBuilder4); 
    throw paramString1;
  }
  
  private static class HippoSAXErrorHandler implements ErrorHandler, Serializable {
    private static final long serialVersionUID = 6918417235413084055L;
    
    private HippoSAXErrorHandler() {}
    
    private void throwError(SAXParseException param1SAXParseException) {
      throw ScriptRuntime.constructError("TypeError", param1SAXParseException.getMessage(), param1SAXParseException.getLineNumber() - 1);
    }
    
    public void error(SAXParseException param1SAXParseException) {
      throwError(param1SAXParseException);
    }
    
    public void fatalError(SAXParseException param1SAXParseException) {
      throwError(param1SAXParseException);
    }
    
    public void warning(SAXParseException param1SAXParseException) {
      Context.reportWarning(param1SAXParseException.getMessage());
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XmlProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */