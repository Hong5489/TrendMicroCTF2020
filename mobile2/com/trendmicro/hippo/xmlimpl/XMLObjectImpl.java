package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionCall;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.NativeWith;
import com.trendmicro.hippo.Ref;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.xml.XMLObject;

abstract class XMLObjectImpl extends XMLObject {
  private static final int Id_addNamespace = 2;
  
  private static final int Id_appendChild = 3;
  
  private static final int Id_attribute = 4;
  
  private static final int Id_attributes = 5;
  
  private static final int Id_child = 6;
  
  private static final int Id_childIndex = 7;
  
  private static final int Id_children = 8;
  
  private static final int Id_comments = 9;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_contains = 10;
  
  private static final int Id_copy = 11;
  
  private static final int Id_descendants = 12;
  
  private static final int Id_elements = 13;
  
  private static final int Id_hasComplexContent = 18;
  
  private static final int Id_hasOwnProperty = 17;
  
  private static final int Id_hasSimpleContent = 19;
  
  private static final int Id_inScopeNamespaces = 14;
  
  private static final int Id_insertChildAfter = 15;
  
  private static final int Id_insertChildBefore = 16;
  
  private static final int Id_length = 20;
  
  private static final int Id_localName = 21;
  
  private static final int Id_name = 22;
  
  private static final int Id_namespace = 23;
  
  private static final int Id_namespaceDeclarations = 24;
  
  private static final int Id_nodeKind = 25;
  
  private static final int Id_normalize = 26;
  
  private static final int Id_parent = 27;
  
  private static final int Id_prependChild = 28;
  
  private static final int Id_processingInstructions = 29;
  
  private static final int Id_propertyIsEnumerable = 30;
  
  private static final int Id_removeNamespace = 31;
  
  private static final int Id_replace = 32;
  
  private static final int Id_setChildren = 33;
  
  private static final int Id_setLocalName = 34;
  
  private static final int Id_setName = 35;
  
  private static final int Id_setNamespace = 36;
  
  private static final int Id_text = 37;
  
  private static final int Id_toSource = 39;
  
  private static final int Id_toString = 38;
  
  private static final int Id_toXMLString = 40;
  
  private static final int Id_valueOf = 41;
  
  private static final int MAX_PROTOTYPE_ID = 41;
  
  private static final Object XMLOBJECT_TAG = "XMLObject";
  
  private XMLLibImpl lib;
  
  private boolean prototypeFlag;
  
  protected XMLObjectImpl(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject) {
    initialize(paramXMLLibImpl, paramScriptable, paramXMLObject);
  }
  
  private static Object arg(Object[] paramArrayOfObject, int paramInt) {
    Object object;
    if (paramInt < paramArrayOfObject.length) {
      object = paramArrayOfObject[paramInt];
    } else {
      object = Undefined.instance;
    } 
    return object;
  }
  
  private XMLList getMatches(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    addMatches(xMLList, paramXMLName);
    return xMLList;
  }
  
  private Object[] toObjectArray(Object[] paramArrayOfObject) {
    Object[] arrayOfObject = new Object[paramArrayOfObject.length];
    for (byte b = 0; b < arrayOfObject.length; b++)
      arrayOfObject[b] = paramArrayOfObject[b]; 
    return arrayOfObject;
  }
  
  private void xmlMethodNotFound(Object paramObject, String paramString) {
    throw ScriptRuntime.notFunctionError(paramObject, paramString);
  }
  
  abstract void addMatches(XMLList paramXMLList, XMLName paramXMLName);
  
  public final Object addValues(Context paramContext, boolean paramBoolean, Object paramObject) {
    if (paramObject instanceof XMLObject) {
      XMLObject xMLObject;
      if (paramBoolean) {
        XMLObjectImpl xMLObjectImpl = this;
        XMLObject xMLObject1 = (XMLObject)paramObject;
        paramObject = xMLObjectImpl;
        xMLObject = xMLObject1;
      } else {
        paramObject = paramObject;
        xMLObject = this;
      } 
      return this.lib.addXMLObjects(paramContext, (XMLObject)paramObject, xMLObject);
    } 
    return (paramObject == Undefined.instance) ? ScriptRuntime.toString(this) : super.addValues(paramContext, paramBoolean, paramObject);
  }
  
  abstract XMLList child(int paramInt);
  
  abstract XMLList child(XMLName paramXMLName);
  
  abstract XMLList children();
  
  abstract XMLList comments();
  
  abstract boolean contains(Object paramObject);
  
  abstract XMLObjectImpl copy();
  
  final XML createEmptyXML() {
    return newXML(XmlNode.createEmpty(getProcessor()));
  }
  
  final Namespace createNamespace(XmlNode.Namespace paramNamespace) {
    return (paramNamespace == null) ? null : this.lib.createNamespaces(new XmlNode.Namespace[] { paramNamespace })[0];
  }
  
  final Namespace[] createNamespaces(XmlNode.Namespace[] paramArrayOfNamespace) {
    return this.lib.createNamespaces(paramArrayOfNamespace);
  }
  
  public void delete(String paramString) {
    Context context = Context.getCurrentContext();
    deleteXMLProperty(this.lib.toXMLNameFromString(context, paramString));
  }
  
  public final boolean delete(Context paramContext, Object paramObject) {
    Context context = paramContext;
    if (paramContext == null)
      context = Context.getCurrentContext(); 
    XMLName xMLName = this.lib.toXMLNameOrIndex(context, paramObject);
    if (xMLName == null) {
      delete((int)ScriptRuntime.lastUint32Result(context));
      return true;
    } 
    deleteXMLProperty(xMLName);
    return true;
  }
  
  abstract void deleteXMLProperty(XMLName paramXMLName);
  
  final String ecmaEscapeAttributeValue(String paramString) {
    paramString = this.lib.escapeAttributeValue(paramString);
    return paramString.substring(1, paramString.length() - 1);
  }
  
  final XML ecmaToXml(Object paramObject) {
    return this.lib.ecmaToXml(paramObject);
  }
  
  abstract XMLList elements(XMLName paramXMLName);
  
  public NativeWith enterDotQuery(Scriptable paramScriptable) {
    XMLWithScope xMLWithScope = new XMLWithScope(this.lib, paramScriptable, this);
    xMLWithScope.initAsDotQuery();
    return xMLWithScope;
  }
  
  public NativeWith enterWith(Scriptable paramScriptable) {
    return new XMLWithScope(this.lib, paramScriptable, this);
  }
  
  protected final Object equivalentValues(Object paramObject) {
    if (equivalentXml(paramObject)) {
      paramObject = Boolean.TRUE;
    } else {
      paramObject = Boolean.FALSE;
    } 
    return paramObject;
  }
  
  abstract boolean equivalentXml(Object paramObject);
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    if (!paramIdFunctionObject.hasTag(XMLOBJECT_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    boolean bool = true;
    if (i == 1) {
      if (paramScriptable2 != null)
        bool = false; 
      return jsConstructor(paramContext, bool, paramArrayOfObject);
    } 
    if (paramScriptable2 instanceof XMLObjectImpl) {
      Object object1;
      XMLObjectImpl xMLObjectImpl = (XMLObjectImpl)paramScriptable2;
      XML xML = xMLObjectImpl.getXML();
      paramIdFunctionObject = null;
      switch (i) {
        default:
          throw new IllegalArgumentException(String.valueOf(i));
        case 41:
          return xMLObjectImpl.valueOf();
        case 40:
          return xMLObjectImpl.toXMLString();
        case 39:
          return xMLObjectImpl.toSource(ScriptRuntime.toInt32(paramArrayOfObject, 0));
        case 38:
          return xMLObjectImpl.toString();
        case 37:
          return xMLObjectImpl.text();
        case 36:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "setNamespace"); 
          xML.setNamespace(this.lib.castToNamespace(paramContext, arg(paramArrayOfObject, 0)));
          return Undefined.instance;
        case 35:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "setName"); 
          if (paramArrayOfObject.length != 0) {
            object = paramArrayOfObject[0];
          } else {
            object = Undefined.instance;
          } 
          xML.setName(this.lib.constructQName(paramContext, object));
          return Undefined.instance;
        case 34:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "setLocalName"); 
          object = arg(paramArrayOfObject, 0);
          if (object instanceof QName) {
            object = ((QName)object).localName();
          } else {
            object = ScriptRuntime.toString(object);
          } 
          xML.setLocalName((String)object);
          return Undefined.instance;
        case 33:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "setChildren"); 
          return xML.setChildren(arg(paramArrayOfObject, 0));
        case 32:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "replace"); 
          object = this.lib.toXMLNameOrIndex(paramContext, arg(paramArrayOfObject, 0));
          object1 = arg(paramArrayOfObject, 1);
          return (object == null) ? xML.replace((int)ScriptRuntime.lastUint32Result(paramContext), object1) : xML.replace((XMLName)object, object1);
        case 31:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "removeNamespace"); 
          return xML.removeNamespace(this.lib.castToNamespace(paramContext, arg(paramArrayOfObject, 0)));
        case 30:
          return ScriptRuntime.wrapBoolean(xMLObjectImpl.propertyIsEnumerable(arg(paramArrayOfObject, 0)));
        case 29:
          if (paramArrayOfObject.length > 0) {
            object = this.lib.toXMLName(paramContext, paramArrayOfObject[0]);
          } else {
            object = XMLName.formStar();
          } 
          return xMLObjectImpl.processingInstructions((XMLName)object);
        case 28:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "prependChild"); 
          return xML.prependChild(arg(paramArrayOfObject, 0));
        case 27:
          return xMLObjectImpl.parent();
        case 26:
          xMLObjectImpl.normalize();
          return Undefined.instance;
        case 25:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "nodeKind"); 
          return xML.nodeKind();
        case 24:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "namespaceDeclarations"); 
          return paramContext.newArray((Scriptable)object1, toObjectArray((Object[])xML.namespaceDeclarations()));
        case 23:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "namespace"); 
          if (paramArrayOfObject.length > 0)
            object = ScriptRuntime.toString(paramArrayOfObject[0]); 
          object = xML.namespace((String)object);
          return (object == null) ? Undefined.instance : object;
        case 22:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "name"); 
          return xML.name();
        case 21:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "localName"); 
          return xML.localName();
        case 20:
          return ScriptRuntime.wrapInt(xMLObjectImpl.length());
        case 19:
          return ScriptRuntime.wrapBoolean(xMLObjectImpl.hasSimpleContent());
        case 18:
          return ScriptRuntime.wrapBoolean(xMLObjectImpl.hasComplexContent());
        case 17:
          object = this.lib.toXMLName(paramContext, arg(paramArrayOfObject, 0));
          return ScriptRuntime.wrapBoolean(xMLObjectImpl.hasOwnProperty((XMLName)object));
        case 16:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "insertChildBefore"); 
          object = arg(paramArrayOfObject, 0);
          return (object == null || object instanceof XML) ? xML.insertChildBefore((XML)object, arg(paramArrayOfObject, 1)) : Undefined.instance;
        case 15:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "insertChildAfter"); 
          object = arg(paramArrayOfObject, 0);
          return (object == null || object instanceof XML) ? xML.insertChildAfter((XML)object, arg(paramArrayOfObject, 1)) : Undefined.instance;
        case 14:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "inScopeNamespaces"); 
          return paramContext.newArray((Scriptable)object1, toObjectArray((Object[])xML.inScopeNamespaces()));
        case 13:
          if (paramArrayOfObject.length == 0) {
            object = XMLName.formStar();
          } else {
            object = this.lib.toXMLName(paramContext, paramArrayOfObject[0]);
          } 
          return xMLObjectImpl.elements((XMLName)object);
        case 12:
          if (paramArrayOfObject.length == 0) {
            object = XmlNode.QName.create(null, null);
          } else {
            object = this.lib.toNodeQName(paramContext, paramArrayOfObject[0], false);
          } 
          return xMLObjectImpl.getMatches(XMLName.create((XmlNode.QName)object, false, true));
        case 11:
          return xMLObjectImpl.copy();
        case 10:
          return ScriptRuntime.wrapBoolean(xMLObjectImpl.contains(arg(paramArrayOfObject, 0)));
        case 9:
          return xMLObjectImpl.comments();
        case 8:
          return xMLObjectImpl.children();
        case 7:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "childIndex"); 
          return ScriptRuntime.wrapInt(xML.childIndex());
        case 6:
          object = this.lib.toXMLNameOrIndex(paramContext, arg(paramArrayOfObject, 0));
          return (object == null) ? xMLObjectImpl.child((int)ScriptRuntime.lastUint32Result(paramContext)) : xMLObjectImpl.child((XMLName)object);
        case 5:
          return xMLObjectImpl.getMatches(XMLName.create(XmlNode.QName.create(null, null), true, false));
        case 4:
          return xMLObjectImpl.getMatches(XMLName.create(this.lib.toNodeQName(paramContext, arg(paramArrayOfObject, 0), true), true, false));
        case 3:
          if (xML == null)
            xmlMethodNotFound(xMLObjectImpl, "appendChild"); 
          return xML.appendChild(arg(paramArrayOfObject, 0));
        case 2:
          break;
      } 
      if (xML == null)
        xmlMethodNotFound(xMLObjectImpl, "addNamespace"); 
      return xML.addNamespace(this.lib.castToNamespace(paramContext, arg(paramArrayOfObject, 0)));
    } 
    throw incompatibleCallError(object);
  }
  
  final void exportAsJSClass(boolean paramBoolean) {
    this.prototypeFlag = true;
    exportAsJSClass(41, getParentScope(), paramBoolean);
  }
  
  protected int findPrototypeId(String paramString) {
    char c2;
    String str2;
    char c;
    char c1 = Character.MIN_VALUE;
    String str1 = null;
    switch (paramString.length()) {
      default:
        c2 = c1;
        str2 = str1;
        break;
      case 22:
        str2 = "processingInstructions";
        c2 = '\035';
        break;
      case 21:
        str2 = "namespaceDeclarations";
        c2 = '\030';
        break;
      case 20:
        str2 = "propertyIsEnumerable";
        c2 = '\036';
        break;
      case 17:
        c = paramString.charAt(3);
        if (c == 'C') {
          str2 = "hasComplexContent";
          c2 = '\022';
          break;
        } 
        if (c == 'c') {
          str2 = "inScopeNamespaces";
          c2 = '\016';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'e') {
          str2 = "insertChildBefore";
          c2 = '\020';
        } 
        break;
      case 16:
        c = paramString.charAt(0);
        if (c == 'h') {
          str2 = "hasSimpleContent";
          c2 = '\023';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'i') {
          str2 = "insertChildAfter";
          c2 = '\017';
        } 
        break;
      case 15:
        str2 = "removeNamespace";
        c2 = '\037';
        break;
      case 14:
        str2 = "hasOwnProperty";
        c2 = '\021';
        break;
      case 12:
        c = paramString.charAt(0);
        if (c == 'a') {
          str2 = "addNamespace";
          c2 = '\002';
          break;
        } 
        if (c == 'p') {
          str2 = "prependChild";
          c2 = '\034';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          c = paramString.charAt(3);
          if (c == 'L') {
            str2 = "setLocalName";
            c2 = '"';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 'N') {
            str2 = "setNamespace";
            c2 = '$';
          } 
        } 
        break;
      case 11:
        c2 = paramString.charAt(0);
        if (c2 != 'a') {
          if (c2 != 'c') {
            if (c2 != 'd') {
              if (c2 != 's') {
                if (c2 != 't') {
                  c2 = c1;
                  str2 = str1;
                  break;
                } 
                str2 = "toXMLString";
                c2 = '(';
                break;
              } 
              str2 = "setChildren";
              c2 = '!';
              break;
            } 
            str2 = "descendants";
            c2 = '\f';
            break;
          } 
          str2 = "constructor";
          c2 = '\001';
          break;
        } 
        str2 = "appendChild";
        c2 = '\003';
        break;
      case 10:
        c = paramString.charAt(0);
        if (c == 'a') {
          str2 = "attributes";
          c2 = '\005';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'c') {
          str2 = "childIndex";
          c2 = '\007';
        } 
        break;
      case 9:
        c2 = paramString.charAt(2);
        if (c2 != 'c') {
          if (c2 != 'm') {
            if (c2 != 'r') {
              if (c2 != 't') {
                c2 = c1;
                str2 = str1;
                break;
              } 
              str2 = "attribute";
              c2 = '\004';
              break;
            } 
            str2 = "normalize";
            c2 = '\032';
            break;
          } 
          str2 = "namespace";
          c2 = '\027';
          break;
        } 
        str2 = "localName";
        c2 = '\025';
        break;
      case 8:
        c2 = paramString.charAt(2);
        if (c2 != 'S') {
          if (c2 != 'i') {
            if (c2 != 'd') {
              if (c2 != 'e') {
                if (c2 != 'm') {
                  if (c2 != 'n') {
                    c2 = c1;
                    str2 = str1;
                    break;
                  } 
                  str2 = "contains";
                  c2 = '\n';
                  break;
                } 
                str2 = "comments";
                c2 = '\t';
                break;
              } 
              str2 = "elements";
              c2 = '\r';
              break;
            } 
            str2 = "nodeKind";
            c2 = '\031';
            break;
          } 
          str2 = "children";
          c2 = '\b';
          break;
        } 
        c = paramString.charAt(7);
        if (c == 'e') {
          str2 = "toSource";
          c2 = '\'';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'g') {
          str2 = "toString";
          c2 = '&';
        } 
        break;
      case 7:
        c = paramString.charAt(0);
        if (c == 'r') {
          str2 = "replace";
          c2 = ' ';
          break;
        } 
        if (c == 's') {
          str2 = "setName";
          c2 = '#';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'v') {
          str2 = "valueOf";
          c2 = ')';
        } 
        break;
      case 6:
        c = paramString.charAt(0);
        if (c == 'l') {
          str2 = "length";
          c2 = '\024';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'p') {
          str2 = "parent";
          c2 = '\033';
        } 
        break;
      case 5:
        str2 = "child";
        c2 = '\006';
        break;
      case 4:
        c = paramString.charAt(0);
        if (c == 'c') {
          str2 = "copy";
          c2 = '\013';
          break;
        } 
        if (c == 'n') {
          str2 = "name";
          c2 = '\026';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 't') {
          str2 = "text";
          c2 = '%';
        } 
        break;
    } 
    c1 = c2;
    if (str2 != null) {
      c1 = c2;
      if (str2 != paramString) {
        c1 = c2;
        if (!str2.equals(paramString))
          c1 = Character.MIN_VALUE; 
      } 
    } 
    return c1;
  }
  
  public final Object get(Context paramContext, Object paramObject) {
    Context context = paramContext;
    if (paramContext == null)
      context = Context.getCurrentContext(); 
    Object object = this.lib.toXMLNameOrIndex(context, paramObject);
    if (object == null) {
      paramObject = get((int)ScriptRuntime.lastUint32Result(context), (Scriptable)this);
      object = paramObject;
      if (paramObject == Scriptable.NOT_FOUND)
        object = Undefined.instance; 
      return object;
    } 
    return getXMLProperty((XMLName)object);
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Context context = Context.getCurrentContext();
    return getXMLProperty(this.lib.toXMLNameFromString(context, paramString));
  }
  
  public final Object getDefaultValue(Class<?> paramClass) {
    return toString();
  }
  
  public Object getFunctionProperty(Context paramContext, int paramInt) {
    if (isPrototype())
      return get(paramInt, (Scriptable)this); 
    Scriptable scriptable = getPrototype();
    return (scriptable instanceof XMLObject) ? ((XMLObject)scriptable).getFunctionProperty(paramContext, paramInt) : NOT_FOUND;
  }
  
  public Object getFunctionProperty(Context paramContext, String paramString) {
    if (isPrototype())
      return super.get(paramString, (Scriptable)this); 
    Scriptable scriptable = getPrototype();
    return (scriptable instanceof XMLObject) ? ((XMLObject)scriptable).getFunctionProperty(paramContext, paramString) : NOT_FOUND;
  }
  
  XMLLibImpl getLib() {
    return this.lib;
  }
  
  public final Scriptable getParentScope() {
    return super.getParentScope();
  }
  
  final XmlProcessor getProcessor() {
    return this.lib.getProcessor();
  }
  
  public final Scriptable getPrototype() {
    return super.getPrototype();
  }
  
  abstract XML getXML();
  
  abstract Object getXMLProperty(XMLName paramXMLName);
  
  public final boolean has(Context paramContext, Object paramObject) {
    Context context = paramContext;
    if (paramContext == null)
      context = Context.getCurrentContext(); 
    XMLName xMLName = this.lib.toXMLNameOrIndex(context, paramObject);
    return (xMLName == null) ? has((int)ScriptRuntime.lastUint32Result(context), (Scriptable)this) : hasXMLProperty(xMLName);
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    Context context = Context.getCurrentContext();
    return hasXMLProperty(this.lib.toXMLNameFromString(context, paramString));
  }
  
  abstract boolean hasComplexContent();
  
  public final boolean hasInstance(Scriptable paramScriptable) {
    return super.hasInstance(paramScriptable);
  }
  
  abstract boolean hasOwnProperty(XMLName paramXMLName);
  
  abstract boolean hasSimpleContent();
  
  abstract boolean hasXMLProperty(XMLName paramXMLName);
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    IdFunctionObject idFunctionObject;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 41:
        b = 0;
        str = "valueOf";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 40:
        b = 1;
        str = "toXMLString";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 39:
        b = 1;
        str = "toSource";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 38:
        b = 0;
        str = "toString";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 37:
        b = 0;
        str = "text";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 36:
        b = 1;
        str = "setNamespace";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 35:
        b = 1;
        str = "setName";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 34:
        b = 1;
        str = "setLocalName";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 33:
        b = 1;
        str = "setChildren";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 32:
        b = 2;
        str = "replace";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 31:
        b = 1;
        str = "removeNamespace";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 30:
        b = 1;
        str = "propertyIsEnumerable";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 29:
        b = 1;
        str = "processingInstructions";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 28:
        b = 1;
        str = "prependChild";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 27:
        b = 0;
        str = "parent";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 26:
        b = 0;
        str = "normalize";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 25:
        b = 0;
        str = "nodeKind";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 24:
        b = 0;
        str = "namespaceDeclarations";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 23:
        b = 1;
        str = "namespace";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 22:
        b = 0;
        str = "name";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 21:
        b = 0;
        str = "localName";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 20:
        b = 0;
        str = "length";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 19:
        b = 0;
        str = "hasSimpleContent";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 18:
        b = 0;
        str = "hasComplexContent";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 17:
        b = 1;
        str = "hasOwnProperty";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 16:
        b = 2;
        str = "insertChildBefore";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 15:
        b = 2;
        str = "insertChildAfter";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 14:
        b = 0;
        str = "inScopeNamespaces";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 13:
        b = 1;
        str = "elements";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 12:
        b = 1;
        str = "descendants";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 11:
        b = 0;
        str = "copy";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 10:
        b = 1;
        str = "contains";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 9:
        b = 0;
        str = "comments";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 8:
        b = 0;
        str = "children";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 7:
        b = 0;
        str = "childIndex";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 6:
        b = 1;
        str = "child";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 5:
        b = 0;
        str = "attributes";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 4:
        b = 1;
        str = "attribute";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 3:
        b = 1;
        str = "appendChild";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 2:
        b = 1;
        str = "addNamespace";
        initPrototypeMethod(XMLOBJECT_TAG, paramInt, str, b);
        return;
      case 1:
        break;
    } 
    if (this instanceof XML) {
      idFunctionObject = new XMLCtor((XML)this, XMLOBJECT_TAG, paramInt, 1);
    } else {
      idFunctionObject = new IdFunctionObject((IdFunctionCall)this, XMLOBJECT_TAG, paramInt, 1);
    } 
    initPrototypeConstructor(idFunctionObject);
  }
  
  final void initialize(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject) {
    boolean bool;
    setParentScope(paramScriptable);
    setPrototype((Scriptable)paramXMLObject);
    if (paramXMLObject == null) {
      bool = true;
    } else {
      bool = false;
    } 
    this.prototypeFlag = bool;
    this.lib = paramXMLLibImpl;
  }
  
  final boolean isPrototype() {
    return this.prototypeFlag;
  }
  
  protected abstract Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject);
  
  abstract int length();
  
  public Ref memberRef(Context paramContext, Object paramObject, int paramInt) {
    boolean bool2;
    boolean bool1 = true;
    if ((paramInt & 0x2) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if ((paramInt & 0x4) == 0)
      bool1 = false; 
    if (bool2 || bool1) {
      XMLName xMLName = XMLName.create(this.lib.toNodeQName(paramContext, paramObject, bool2), bool2, bool1);
      xMLName.initXMLObject(this);
      return xMLName;
    } 
    throw Kit.codeBug();
  }
  
  public Ref memberRef(Context paramContext, Object paramObject1, Object paramObject2, int paramInt) {
    boolean bool2;
    boolean bool1 = true;
    if ((paramInt & 0x2) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if ((paramInt & 0x4) == 0)
      bool1 = false; 
    XMLName xMLName = XMLName.create(this.lib.toNodeQName(paramContext, paramObject1, paramObject2), bool2, bool1);
    xMLName.initXMLObject(this);
    return xMLName;
  }
  
  final QName newQName(XmlNode.QName paramQName) {
    return this.lib.newQName(paramQName);
  }
  
  final QName newQName(String paramString1, String paramString2, String paramString3) {
    return this.lib.newQName(paramString1, paramString2, paramString3);
  }
  
  final XML newTextElementXML(XmlNode paramXmlNode, XmlNode.QName paramQName, String paramString) {
    return this.lib.newTextElementXML(paramXmlNode, paramQName, paramString);
  }
  
  final XML newXML(XmlNode paramXmlNode) {
    return this.lib.newXML(paramXmlNode);
  }
  
  final XML newXMLFromJs(Object paramObject) {
    return this.lib.newXMLFromJs(paramObject);
  }
  
  final XMLList newXMLList() {
    return this.lib.newXMLList();
  }
  
  final XMLList newXMLListFrom(Object paramObject) {
    return this.lib.newXMLListFrom(paramObject);
  }
  
  abstract void normalize();
  
  abstract Object parent();
  
  abstract XMLList processingInstructions(XMLName paramXMLName);
  
  abstract boolean propertyIsEnumerable(Object paramObject);
  
  public final void put(Context paramContext, Object paramObject1, Object paramObject2) {
    Context context = paramContext;
    if (paramContext == null)
      context = Context.getCurrentContext(); 
    XMLName xMLName = this.lib.toXMLNameOrIndex(context, paramObject1);
    if (xMLName == null) {
      put((int)ScriptRuntime.lastUint32Result(context), (Scriptable)this, paramObject2);
      return;
    } 
    putXMLProperty(xMLName, paramObject2);
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    Context context = Context.getCurrentContext();
    putXMLProperty(this.lib.toXMLNameFromString(context, paramString), paramObject);
  }
  
  abstract void putXMLProperty(XMLName paramXMLName, Object paramObject);
  
  public final void setParentScope(Scriptable paramScriptable) {
    super.setParentScope(paramScriptable);
  }
  
  public final void setPrototype(Scriptable paramScriptable) {
    super.setPrototype(paramScriptable);
  }
  
  abstract XMLList text();
  
  abstract String toSource(int paramInt);
  
  public abstract String toString();
  
  abstract String toXMLString();
  
  abstract Object valueOf();
  
  XML xmlFromNode(XmlNode paramXmlNode) {
    if (paramXmlNode.getXml() == null)
      paramXmlNode.setXml(newXML(paramXmlNode)); 
    return paramXmlNode.getXml();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XMLObjectImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */