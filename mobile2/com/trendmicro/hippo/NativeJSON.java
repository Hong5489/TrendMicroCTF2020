package com.trendmicro.hippo;

import com.trendmicro.hippo.json.JsonParser;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public final class NativeJSON extends IdScriptableObject {
  private static final int Id_parse = 2;
  
  private static final int Id_stringify = 3;
  
  private static final int Id_toSource = 1;
  
  private static final Object JSON_TAG = "JSON";
  
  private static final int LAST_METHOD_ID = 3;
  
  private static final int MAX_ID = 3;
  
  private static final int MAX_STRINGIFY_GAP_LENGTH = 10;
  
  private static final long serialVersionUID = -4567599697595654984L;
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    NativeJSON nativeJSON = new NativeJSON();
    nativeJSON.activatePrototypeMap(3);
    nativeJSON.setPrototype(getObjectPrototype(paramScriptable));
    nativeJSON.setParentScope(paramScriptable);
    if (paramBoolean)
      nativeJSON.sealObject(); 
    ScriptableObject.defineProperty(paramScriptable, "JSON", nativeJSON, 2);
  }
  
  private static String ja(NativeArray paramNativeArray, StringifyState paramStringifyState) {
    if (paramStringifyState.stack.search(paramNativeArray) == -1) {
      String str1;
      paramStringifyState.stack.push(paramNativeArray);
      String str2 = paramStringifyState.indent;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramStringifyState.indent);
      stringBuilder.append(paramStringifyState.gap);
      paramStringifyState.indent = stringBuilder.toString();
      LinkedList<String> linkedList = new LinkedList();
      long l1 = paramNativeArray.getLength();
      long l2;
      for (l2 = 0L; l2 < l1; l2++) {
        Object object;
        if (l2 > 2147483647L) {
          object = str(Long.toString(l2), paramNativeArray, paramStringifyState);
        } else {
          object = str(Integer.valueOf((int)l2), paramNativeArray, paramStringifyState);
        } 
        if (object == Undefined.instance) {
          linkedList.add("null");
        } else {
          linkedList.add(object);
        } 
      } 
      if (linkedList.isEmpty()) {
        str1 = "[]";
      } else if (paramStringifyState.gap.length() == 0) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append('[');
        stringBuilder1.append(join((Collection)linkedList, ","));
        stringBuilder1.append(']');
        str1 = stringBuilder1.toString();
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(",\n");
        stringBuilder1.append(paramStringifyState.indent);
        String str = join((Collection)linkedList, stringBuilder1.toString());
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("[\n");
        stringBuilder1.append(paramStringifyState.indent);
        stringBuilder1.append(str);
        stringBuilder1.append('\n');
        stringBuilder1.append(str2);
        stringBuilder1.append(']');
        str1 = stringBuilder1.toString();
      } 
      paramStringifyState.stack.pop();
      paramStringifyState.indent = str2;
      return str1;
    } 
    throw ScriptRuntime.typeError0("msg.cyclic.value");
  }
  
  private static String jo(Scriptable paramScriptable, StringifyState paramStringifyState) {
    if (paramStringifyState.stack.search(paramScriptable) == -1) {
      String str1;
      Object[] arrayOfObject;
      paramStringifyState.stack.push(paramScriptable);
      String str2 = paramStringifyState.indent;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramStringifyState.indent);
      stringBuilder.append(paramStringifyState.gap);
      paramStringifyState.indent = stringBuilder.toString();
      if (paramStringifyState.propertyList != null) {
        arrayOfObject = paramStringifyState.propertyList.toArray();
      } else {
        arrayOfObject = paramScriptable.getIds();
      } 
      LinkedList<String> linkedList = new LinkedList();
      int i = arrayOfObject.length;
      for (byte b = 0; b < i; b++) {
        Object object1 = arrayOfObject[b];
        Object object2 = str(object1, paramScriptable, paramStringifyState);
        if (object2 != Undefined.instance) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append(quote(object1.toString()));
          stringBuilder2.append(":");
          String str = stringBuilder2.toString();
          object1 = str;
          if (paramStringifyState.gap.length() > 0) {
            object1 = new StringBuilder();
            object1.append(str);
            object1.append(" ");
            object1 = object1.toString();
          } 
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append((String)object1);
          stringBuilder1.append(object2);
          linkedList.add(stringBuilder1.toString());
        } 
      } 
      if (linkedList.isEmpty()) {
        str1 = "{}";
      } else if (paramStringifyState.gap.length() == 0) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append('{');
        stringBuilder1.append(join((Collection)linkedList, ","));
        stringBuilder1.append('}');
        str1 = stringBuilder1.toString();
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(",\n");
        stringBuilder1.append(paramStringifyState.indent);
        String str = join((Collection)linkedList, stringBuilder1.toString());
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("{\n");
        stringBuilder1.append(paramStringifyState.indent);
        stringBuilder1.append(str);
        stringBuilder1.append('\n');
        stringBuilder1.append(str2);
        stringBuilder1.append('}');
        str1 = stringBuilder1.toString();
      } 
      paramStringifyState.stack.pop();
      paramStringifyState.indent = str2;
      return str1;
    } 
    throw ScriptRuntime.typeError0("msg.cyclic.value");
  }
  
  private static String join(Collection<Object> paramCollection, String paramString) {
    if (paramCollection == null || paramCollection.isEmpty())
      return ""; 
    Iterator<E> iterator = paramCollection.iterator();
    if (!iterator.hasNext())
      return ""; 
    StringBuilder stringBuilder = new StringBuilder(iterator.next().toString());
    while (iterator.hasNext()) {
      stringBuilder.append(paramString);
      stringBuilder.append(iterator.next().toString());
    } 
    return stringBuilder.toString();
  }
  
  private static Object parse(Context paramContext, Scriptable paramScriptable, String paramString) {
    try {
      JsonParser jsonParser = new JsonParser();
      this(paramContext, paramScriptable);
      return jsonParser.parseValue(paramString);
    } catch (com.trendmicro.hippo.json.JsonParser.ParseException parseException) {
      throw ScriptRuntime.constructError("SyntaxError", parseException.getMessage());
    } 
  }
  
  public static Object parse(Context paramContext, Scriptable paramScriptable, String paramString, Callable paramCallable) {
    Object object = parse(paramContext, paramScriptable, paramString);
    Scriptable scriptable = paramContext.newObject(paramScriptable);
    scriptable.put("", scriptable, object);
    return walk(paramContext, paramScriptable, paramCallable, scriptable, "");
  }
  
  private static String quote(String paramString) {
    StringBuilder stringBuilder = new StringBuilder(paramString.length() + 2);
    stringBuilder.append('"');
    int i = paramString.length();
    for (byte b = 0; b < i; b++) {
      char c = paramString.charAt(b);
      if (c != '\f') {
        if (c != '\r') {
          if (c != '"') {
            if (c != '\\') {
              switch (c) {
                default:
                  if (c < ' ') {
                    stringBuilder.append("\\u");
                    stringBuilder.append(String.format("%04x", new Object[] { Integer.valueOf(c) }));
                    break;
                  } 
                  stringBuilder.append(c);
                  break;
                case '\n':
                  stringBuilder.append("\\n");
                  break;
                case '\t':
                  stringBuilder.append("\\t");
                  break;
                case '\b':
                  stringBuilder.append("\\b");
                  break;
              } 
            } else {
              stringBuilder.append("\\\\");
            } 
          } else {
            stringBuilder.append("\\\"");
          } 
        } else {
          stringBuilder.append("\\r");
        } 
      } else {
        stringBuilder.append("\\f");
      } 
    } 
    stringBuilder.append('"');
    return stringBuilder.toString();
  }
  
  private static String repeat(char paramChar, int paramInt) {
    char[] arrayOfChar = new char[paramInt];
    Arrays.fill(arrayOfChar, paramChar);
    return new String(arrayOfChar);
  }
  
  private static Object str(Object paramObject, Scriptable paramScriptable, StringifyState paramStringifyState) {
    if (paramObject instanceof String) {
      object1 = getProperty(paramScriptable, (String)paramObject);
    } else {
      object1 = getProperty(paramScriptable, ((Number)paramObject).intValue());
    } 
    Object object2 = object1;
    if (object1 instanceof Scriptable) {
      object2 = object1;
      if (hasProperty((Scriptable)object1, "toJSON")) {
        object2 = object1;
        if (getProperty((Scriptable)object1, "toJSON") instanceof Callable)
          object2 = callMethod(paramStringifyState.cx, (Scriptable)object1, "toJSON", new Object[] { paramObject }); 
      } 
    } 
    Object object1 = object2;
    if (paramStringifyState.replacer != null)
      object1 = paramStringifyState.replacer.call(paramStringifyState.cx, paramStringifyState.scope, paramScriptable, new Object[] { paramObject, object2 }); 
    if (object1 instanceof NativeNumber) {
      paramObject = Double.valueOf(ScriptRuntime.toNumber(object1));
    } else if (object1 instanceof NativeString) {
      paramObject = ScriptRuntime.toString(object1);
    } else {
      paramObject = object1;
      if (object1 instanceof NativeBoolean)
        paramObject = ((NativeBoolean)object1).getDefaultValue(ScriptRuntime.BooleanClass); 
    } 
    if (paramObject == null)
      return "null"; 
    if (paramObject.equals(Boolean.TRUE))
      return "true"; 
    if (paramObject.equals(Boolean.FALSE))
      return "false"; 
    if (paramObject instanceof CharSequence)
      return quote(paramObject.toString()); 
    if (paramObject instanceof Number) {
      double d = ((Number)paramObject).doubleValue();
      return (d == d && d != Double.POSITIVE_INFINITY && d != Double.NEGATIVE_INFINITY) ? ScriptRuntime.toString(paramObject) : "null";
    } 
    return (paramObject instanceof Scriptable && !(paramObject instanceof Callable)) ? ((paramObject instanceof NativeArray) ? ja((NativeArray)paramObject, paramStringifyState) : jo((Scriptable)paramObject, paramStringifyState)) : Undefined.instance;
  }
  
  public static Object stringify(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object<Integer> paramObject2, Object<Integer> paramObject3) {
    Callable callable;
    List<Object> list;
    Object<Integer> object = paramObject3;
    String str = "";
    if (paramObject2 instanceof Callable) {
      callable = (Callable)paramObject2;
      list = null;
    } else if (paramObject2 instanceof NativeArray) {
      list = new LinkedList();
      NativeArray nativeArray = (NativeArray)paramObject2;
      paramObject2 = (Object<Integer>)nativeArray.getIndexIds().iterator();
      while (paramObject2.hasNext()) {
        Object object1 = nativeArray.get(((Integer)paramObject2.next()).intValue(), nativeArray);
        if (object1 instanceof String || object1 instanceof Number) {
          list.add(object1);
          continue;
        } 
        if (object1 instanceof NativeString || object1 instanceof NativeNumber)
          list.add(ScriptRuntime.toString(object1)); 
      } 
      callable = null;
    } else {
      list = null;
      callable = null;
    } 
    if (object instanceof NativeNumber) {
      paramObject2 = (Object<Integer>)Double.valueOf(ScriptRuntime.toNumber(paramObject3));
    } else {
      paramObject2 = object;
      if (object instanceof NativeString)
        paramObject2 = (Object<Integer>)ScriptRuntime.toString(paramObject3); 
    } 
    if (paramObject2 instanceof Number) {
      int i = Math.min(10, (int)ScriptRuntime.toInteger(paramObject2));
      if (i > 0) {
        paramObject2 = (Object<Integer>)repeat(' ', i);
      } else {
        paramObject2 = (Object<Integer>)"";
      } 
      paramObject3 = (Object<Integer>)Integer.valueOf(i);
      object = paramObject2;
    } else {
      paramObject3 = paramObject2;
      object = (Object<Integer>)str;
      if (paramObject2 instanceof String) {
        paramObject3 = paramObject2;
        if (paramObject3.length() > 10)
          paramObject3 = (Object<Integer>)paramObject3.substring(0, 10); 
        stringifyState = new StringifyState(paramContext, paramScriptable, "", (String)paramObject3, callable, list, paramObject2);
        paramObject2 = (Object<Integer>)new NativeObject();
        paramObject2.setParentScope(paramScriptable);
        paramObject2.setPrototype(ScriptableObject.getObjectPrototype(paramScriptable));
        paramObject2.defineProperty("", paramObject1, 0);
        return str("", (Scriptable)paramObject2, stringifyState);
      } 
    } 
    paramObject2 = paramObject3;
    paramObject3 = object;
    StringifyState stringifyState = new StringifyState((Context)stringifyState, paramScriptable, "", (String)paramObject3, callable, list, paramObject2);
    paramObject2 = (Object<Integer>)new NativeObject();
    paramObject2.setParentScope(paramScriptable);
    paramObject2.setPrototype(ScriptableObject.getObjectPrototype(paramScriptable));
    paramObject2.defineProperty("", paramObject1, 0);
    return str("", (Scriptable)paramObject2, stringifyState);
  }
  
  private static Object walk(Context paramContext, Scriptable paramScriptable1, Callable paramCallable, Scriptable paramScriptable2, Object paramObject) {
    Object object;
    if (paramObject instanceof Number) {
      object = paramScriptable2.get(((Number)paramObject).intValue(), paramScriptable2);
    } else {
      object = paramScriptable2.get((String)paramObject, paramScriptable2);
    } 
    if (object instanceof Scriptable) {
      Scriptable scriptable = (Scriptable)object;
      if (scriptable instanceof NativeArray) {
        long l1 = ((NativeArray)scriptable).getLength();
        long l2;
        for (l2 = 0L; l2 < l1; l2++) {
          if (l2 > 2147483647L) {
            String str = Long.toString(l2);
            Object object1 = walk(paramContext, paramScriptable1, paramCallable, scriptable, str);
            if (object1 == Undefined.instance) {
              scriptable.delete(str);
            } else {
              scriptable.put(str, scriptable, object1);
            } 
          } else {
            int i = (int)l2;
            Object object1 = walk(paramContext, paramScriptable1, paramCallable, scriptable, Integer.valueOf(i));
            if (object1 == Undefined.instance) {
              scriptable.delete(i);
            } else {
              scriptable.put(i, scriptable, object1);
            } 
          } 
        } 
      } else {
        for (Object object1 : scriptable.getIds()) {
          Object object2 = walk(paramContext, paramScriptable1, paramCallable, scriptable, object1);
          if (object2 == Undefined.instance) {
            if (object1 instanceof Number) {
              scriptable.delete(((Number)object1).intValue());
            } else {
              scriptable.delete((String)object1);
            } 
          } else if (object1 instanceof Number) {
            scriptable.put(((Number)object1).intValue(), scriptable, object2);
          } else {
            scriptable.put((String)object1, scriptable, object2);
          } 
        } 
      } 
    } 
    return paramCallable.call(paramContext, paramScriptable1, paramScriptable2, new Object[] { paramObject, object });
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_1
    //   1: getstatic com/trendmicro/hippo/NativeJSON.JSON_TAG : Ljava/lang/Object;
    //   4: invokevirtual hasTag : (Ljava/lang/Object;)Z
    //   7: ifne -> 22
    //   10: aload_0
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: aload #4
    //   16: aload #5
    //   18: invokespecial execIdCall : (Lcom/trendmicro/hippo/IdFunctionObject;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   21: areturn
    //   22: aload_1
    //   23: invokevirtual methodId : ()I
    //   26: istore #6
    //   28: iload #6
    //   30: iconst_1
    //   31: if_icmpeq -> 200
    //   34: iload #6
    //   36: iconst_2
    //   37: if_icmpeq -> 151
    //   40: iload #6
    //   42: iconst_3
    //   43: if_icmpne -> 138
    //   46: aconst_null
    //   47: astore #7
    //   49: aconst_null
    //   50: astore #8
    //   52: aconst_null
    //   53: astore_1
    //   54: aconst_null
    //   55: astore #4
    //   57: aconst_null
    //   58: astore #9
    //   60: aconst_null
    //   61: astore #10
    //   63: aload #5
    //   65: arraylength
    //   66: istore #6
    //   68: iload #6
    //   70: iconst_1
    //   71: if_icmpeq -> 115
    //   74: aload #10
    //   76: astore #4
    //   78: iload #6
    //   80: iconst_2
    //   81: if_icmpeq -> 110
    //   84: iload #6
    //   86: iconst_3
    //   87: if_icmpeq -> 104
    //   90: aload #7
    //   92: astore_1
    //   93: aload #8
    //   95: astore #5
    //   97: aload #9
    //   99: astore #4
    //   101: goto -> 127
    //   104: aload #5
    //   106: iconst_2
    //   107: aaload
    //   108: astore #4
    //   110: aload #5
    //   112: iconst_1
    //   113: aaload
    //   114: astore_1
    //   115: aload #5
    //   117: iconst_0
    //   118: aaload
    //   119: astore #9
    //   121: aload_1
    //   122: astore #5
    //   124: aload #9
    //   126: astore_1
    //   127: aload_2
    //   128: aload_3
    //   129: aload_1
    //   130: aload #5
    //   132: aload #4
    //   134: invokestatic stringify : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   137: areturn
    //   138: new java/lang/IllegalStateException
    //   141: dup
    //   142: iload #6
    //   144: invokestatic valueOf : (I)Ljava/lang/String;
    //   147: invokespecial <init> : (Ljava/lang/String;)V
    //   150: athrow
    //   151: aload #5
    //   153: iconst_0
    //   154: invokestatic toString : ([Ljava/lang/Object;I)Ljava/lang/String;
    //   157: astore #4
    //   159: aconst_null
    //   160: astore_1
    //   161: aload #5
    //   163: arraylength
    //   164: iconst_1
    //   165: if_icmple -> 173
    //   168: aload #5
    //   170: iconst_1
    //   171: aaload
    //   172: astore_1
    //   173: aload_1
    //   174: instanceof com/trendmicro/hippo/Callable
    //   177: ifeq -> 192
    //   180: aload_2
    //   181: aload_3
    //   182: aload #4
    //   184: aload_1
    //   185: checkcast com/trendmicro/hippo/Callable
    //   188: invokestatic parse : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Callable;)Ljava/lang/Object;
    //   191: areturn
    //   192: aload_2
    //   193: aload_3
    //   194: aload #4
    //   196: invokestatic parse : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
    //   199: areturn
    //   200: ldc 'JSON'
    //   202: areturn
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i != 5) {
      if (i != 8) {
        if (i == 9) {
          str = "stringify";
          b = 3;
        } 
      } else {
        str = "toSource";
        b = 1;
      } 
    } else {
      str = "parse";
      b = 2;
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
    return "JSON";
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt <= 3) {
      boolean bool;
      String str;
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt == 3) {
            bool = true;
            str = "stringify";
          } else {
            throw new IllegalStateException(String.valueOf(paramInt));
          } 
        } else {
          bool = true;
          str = "parse";
        } 
      } else {
        bool = false;
        str = "toSource";
      } 
      initPrototypeMethod(JSON_TAG, paramInt, str, bool);
      return;
    } 
    throw new IllegalStateException(String.valueOf(paramInt));
  }
  
  private static class StringifyState {
    Context cx;
    
    String gap;
    
    String indent;
    
    List<Object> propertyList;
    
    Callable replacer;
    
    Scriptable scope;
    
    Object space;
    
    Stack<Scriptable> stack = new Stack<>();
    
    StringifyState(Context param1Context, Scriptable param1Scriptable, String param1String1, String param1String2, Callable param1Callable, List<Object> param1List, Object param1Object) {
      this.cx = param1Context;
      this.scope = param1Scriptable;
      this.indent = param1String1;
      this.gap = param1String2;
      this.replacer = param1Callable;
      this.propertyList = param1List;
      this.space = param1Object;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJSON.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */