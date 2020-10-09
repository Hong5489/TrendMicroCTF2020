package com.trendmicro.hippo;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class JavaMembers {
  private Class<?> cl;
  
  NativeJavaMethod ctors;
  
  private Map<String, FieldAndMethods> fieldAndMethods;
  
  private Map<String, Object> members;
  
  private Map<String, FieldAndMethods> staticFieldAndMethods;
  
  private Map<String, Object> staticMembers;
  
  JavaMembers(Scriptable paramScriptable, Class<?> paramClass) {
    this(paramScriptable, paramClass, false);
  }
  
  JavaMembers(Scriptable paramScriptable, Class<?> paramClass, boolean paramBoolean) {
    try {
      Context context = ContextFactory.getGlobal().enterContext();
      ClassShutter classShutter = context.getClassShutter();
      if (classShutter == null || classShutter.visibleToScripts(paramClass.getName())) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        this();
        this.members = (Map)hashMap;
        hashMap = new HashMap<>();
        this();
        this.staticMembers = (Map)hashMap;
        this.cl = paramClass;
        reflect(paramScriptable, paramBoolean, context.hasFeature(13));
        return;
      } 
      throw Context.reportRuntimeError1("msg.access.prohibited", paramClass.getName());
    } finally {
      Context.exit();
    } 
  }
  
  private static void discoverAccessibleMethods(Class<?> paramClass, Map<MethodSignature, Method> paramMap, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getModifiers : ()I
    //   4: invokestatic isPublic : (I)Z
    //   7: istore #4
    //   9: iconst_0
    //   10: istore #5
    //   12: iload #4
    //   14: ifne -> 24
    //   17: aload_0
    //   18: astore #6
    //   20: iload_3
    //   21: ifeq -> 424
    //   24: aload_0
    //   25: astore #7
    //   27: iload_2
    //   28: ifne -> 131
    //   31: iload_3
    //   32: ifeq -> 41
    //   35: aload_0
    //   36: astore #7
    //   38: goto -> 131
    //   41: aload_0
    //   42: astore #6
    //   44: aload_0
    //   45: invokevirtual getMethods : ()[Ljava/lang/reflect/Method;
    //   48: astore #7
    //   50: aload_0
    //   51: astore #6
    //   53: aload #7
    //   55: arraylength
    //   56: istore #8
    //   58: iconst_0
    //   59: istore #9
    //   61: iload #9
    //   63: iload #8
    //   65: if_icmpge -> 479
    //   68: aload #7
    //   70: iload #9
    //   72: aaload
    //   73: astore #10
    //   75: aload_0
    //   76: astore #6
    //   78: new com/trendmicro/hippo/JavaMembers$MethodSignature
    //   81: astore #11
    //   83: aload_0
    //   84: astore #6
    //   86: aload #11
    //   88: aload #10
    //   90: invokespecial <init> : (Ljava/lang/reflect/Method;)V
    //   93: aload_0
    //   94: astore #6
    //   96: aload_1
    //   97: aload #11
    //   99: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   104: ifne -> 121
    //   107: aload_0
    //   108: astore #6
    //   110: aload_1
    //   111: aload #11
    //   113: aload #10
    //   115: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   120: pop
    //   121: iinc #9, 1
    //   124: goto -> 61
    //   127: astore_0
    //   128: goto -> 385
    //   131: aload #7
    //   133: ifnull -> 479
    //   136: aload #7
    //   138: invokevirtual getDeclaredMethods : ()[Ljava/lang/reflect/Method;
    //   141: astore #10
    //   143: aload #10
    //   145: arraylength
    //   146: istore #8
    //   148: iconst_0
    //   149: istore #9
    //   151: iload #9
    //   153: iload #8
    //   155: if_icmpge -> 244
    //   158: aload #10
    //   160: iload #9
    //   162: aaload
    //   163: astore_0
    //   164: aload_0
    //   165: invokevirtual getModifiers : ()I
    //   168: istore #12
    //   170: iload #12
    //   172: invokestatic isPublic : (I)Z
    //   175: ifne -> 190
    //   178: iload #12
    //   180: invokestatic isProtected : (I)Z
    //   183: ifne -> 190
    //   186: iload_3
    //   187: ifeq -> 238
    //   190: new com/trendmicro/hippo/JavaMembers$MethodSignature
    //   193: astore #6
    //   195: aload #6
    //   197: aload_0
    //   198: invokespecial <init> : (Ljava/lang/reflect/Method;)V
    //   201: aload_1
    //   202: aload #6
    //   204: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   209: ifne -> 238
    //   212: iload_3
    //   213: ifeq -> 228
    //   216: aload_0
    //   217: invokevirtual isAccessible : ()Z
    //   220: ifne -> 228
    //   223: aload_0
    //   224: iconst_1
    //   225: invokevirtual setAccessible : (Z)V
    //   228: aload_1
    //   229: aload #6
    //   231: aload_0
    //   232: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   237: pop
    //   238: iinc #9, 1
    //   241: goto -> 151
    //   244: aload #7
    //   246: invokevirtual getInterfaces : ()[Ljava/lang/Class;
    //   249: astore_0
    //   250: aload_0
    //   251: arraylength
    //   252: istore #8
    //   254: iconst_0
    //   255: istore #9
    //   257: iload #9
    //   259: iload #8
    //   261: if_icmpge -> 280
    //   264: aload_0
    //   265: iload #9
    //   267: aaload
    //   268: aload_1
    //   269: iload_2
    //   270: iload_3
    //   271: invokestatic discoverAccessibleMethods : (Ljava/lang/Class;Ljava/util/Map;ZZ)V
    //   274: iinc #9, 1
    //   277: goto -> 257
    //   280: aload #7
    //   282: invokevirtual getSuperclass : ()Ljava/lang/Class;
    //   285: astore_0
    //   286: aload_0
    //   287: astore #7
    //   289: goto -> 131
    //   292: astore_0
    //   293: aload #7
    //   295: astore #6
    //   297: aload #7
    //   299: invokevirtual getMethods : ()[Ljava/lang/reflect/Method;
    //   302: astore #10
    //   304: aload #7
    //   306: astore #6
    //   308: aload #10
    //   310: arraylength
    //   311: istore #8
    //   313: iconst_0
    //   314: istore #9
    //   316: iload #9
    //   318: iload #8
    //   320: if_icmpge -> 382
    //   323: aload #10
    //   325: iload #9
    //   327: aaload
    //   328: astore #11
    //   330: aload #7
    //   332: astore #6
    //   334: new com/trendmicro/hippo/JavaMembers$MethodSignature
    //   337: astore_0
    //   338: aload #7
    //   340: astore #6
    //   342: aload_0
    //   343: aload #11
    //   345: invokespecial <init> : (Ljava/lang/reflect/Method;)V
    //   348: aload #7
    //   350: astore #6
    //   352: aload_1
    //   353: aload_0
    //   354: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   359: ifne -> 376
    //   362: aload #7
    //   364: astore #6
    //   366: aload_1
    //   367: aload_0
    //   368: aload #11
    //   370: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   375: pop
    //   376: iinc #9, 1
    //   379: goto -> 316
    //   382: goto -> 479
    //   385: new java/lang/StringBuilder
    //   388: dup
    //   389: invokespecial <init> : ()V
    //   392: astore_0
    //   393: aload_0
    //   394: ldc 'Could not discover accessible methods of class '
    //   396: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   399: pop
    //   400: aload_0
    //   401: aload #6
    //   403: invokevirtual getName : ()Ljava/lang/String;
    //   406: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   409: pop
    //   410: aload_0
    //   411: ldc ' due to lack of privileges, attemping superclasses/interfaces.'
    //   413: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: pop
    //   417: aload_0
    //   418: invokevirtual toString : ()Ljava/lang/String;
    //   421: invokestatic reportWarning : (Ljava/lang/String;)V
    //   424: aload #6
    //   426: invokevirtual getInterfaces : ()[Ljava/lang/Class;
    //   429: astore_0
    //   430: aload_0
    //   431: arraylength
    //   432: istore #8
    //   434: iload #5
    //   436: istore #9
    //   438: iload #9
    //   440: iload #8
    //   442: if_icmpge -> 461
    //   445: aload_0
    //   446: iload #9
    //   448: aaload
    //   449: aload_1
    //   450: iload_2
    //   451: iload_3
    //   452: invokestatic discoverAccessibleMethods : (Ljava/lang/Class;Ljava/util/Map;ZZ)V
    //   455: iinc #9, 1
    //   458: goto -> 438
    //   461: aload #6
    //   463: invokevirtual getSuperclass : ()Ljava/lang/Class;
    //   466: astore_0
    //   467: aload_0
    //   468: ifnull -> 478
    //   471: aload_0
    //   472: aload_1
    //   473: iload_2
    //   474: iload_3
    //   475: invokestatic discoverAccessibleMethods : (Ljava/lang/Class;Ljava/util/Map;ZZ)V
    //   478: return
    //   479: return
    // Exception table:
    //   from	to	target	type
    //   44	50	127	java/lang/SecurityException
    //   53	58	127	java/lang/SecurityException
    //   78	83	127	java/lang/SecurityException
    //   86	93	127	java/lang/SecurityException
    //   96	107	127	java/lang/SecurityException
    //   110	121	127	java/lang/SecurityException
    //   136	148	292	java/lang/SecurityException
    //   164	186	292	java/lang/SecurityException
    //   190	212	292	java/lang/SecurityException
    //   216	228	292	java/lang/SecurityException
    //   228	238	292	java/lang/SecurityException
    //   244	254	292	java/lang/SecurityException
    //   264	274	292	java/lang/SecurityException
    //   280	286	292	java/lang/SecurityException
    //   297	304	127	java/lang/SecurityException
    //   308	313	127	java/lang/SecurityException
    //   334	338	127	java/lang/SecurityException
    //   342	348	127	java/lang/SecurityException
    //   352	362	127	java/lang/SecurityException
    //   366	376	127	java/lang/SecurityException
  }
  
  private static Method[] discoverAccessibleMethods(Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    discoverAccessibleMethods(paramClass, (Map)hashMap, paramBoolean1, paramBoolean2);
    return (Method[])hashMap.values().toArray((Object[])new Method[hashMap.size()]);
  }
  
  private static MemberBox extractGetMethod(MemberBox[] paramArrayOfMemberBox, boolean paramBoolean) {
    int i = paramArrayOfMemberBox.length;
    for (byte b = 0; b < i; b++) {
      MemberBox memberBox = paramArrayOfMemberBox[b];
      if (memberBox.argTypes.length == 0 && (!paramBoolean || memberBox.isStatic())) {
        if (memberBox.method().getReturnType() != void.class)
          return memberBox; 
        break;
      } 
    } 
    return null;
  }
  
  private static MemberBox extractSetMethod(Class<?> paramClass, MemberBox[] paramArrayOfMemberBox, boolean paramBoolean) {
    for (byte b = 1; b <= 2; b++) {
      int i = paramArrayOfMemberBox.length;
      for (byte b1 = 0; b1 < i; b1++) {
        MemberBox memberBox = paramArrayOfMemberBox[b1];
        if (!paramBoolean || memberBox.isStatic()) {
          Class<?>[] arrayOfClass = memberBox.argTypes;
          if (arrayOfClass.length == 1)
            if (b == 1) {
              if (arrayOfClass[0] == paramClass)
                return memberBox; 
            } else {
              if (b != 2)
                Kit.codeBug(); 
              if (arrayOfClass[0].isAssignableFrom(paramClass))
                return memberBox; 
            }  
        } 
      } 
    } 
    return null;
  }
  
  private static MemberBox extractSetMethod(MemberBox[] paramArrayOfMemberBox, boolean paramBoolean) {
    int i = paramArrayOfMemberBox.length;
    for (byte b = 0; b < i; b++) {
      MemberBox memberBox = paramArrayOfMemberBox[b];
      if ((!paramBoolean || memberBox.isStatic()) && memberBox.method().getReturnType() == void.class && memberBox.argTypes.length == 1)
        return memberBox; 
    } 
    return null;
  }
  
  private MemberBox findExplicitFunction(String paramString, boolean paramBoolean) {
    MemberBox[] arrayOfMemberBox1;
    byte b;
    int i = paramString.indexOf('(');
    if (i < 0)
      return null; 
    if (paramBoolean) {
      Map<String, Object> map = this.staticMembers;
    } else {
      Map<String, Object> map = this.members;
    } 
    MemberBox[] arrayOfMemberBox2 = null;
    if (paramBoolean && i == 0) {
      b = 1;
    } else {
      b = 0;
    } 
    if (b) {
      arrayOfMemberBox1 = this.ctors.methods;
    } else {
      String str = paramString.substring(0, i);
      arrayOfMemberBox1 = (MemberBox[])arrayOfMemberBox1.get(str);
      MemberBox[] arrayOfMemberBox = arrayOfMemberBox1;
      if (!paramBoolean) {
        arrayOfMemberBox = arrayOfMemberBox1;
        if (arrayOfMemberBox1 == null)
          arrayOfMemberBox = (MemberBox[])this.staticMembers.get(str); 
      } 
      arrayOfMemberBox1 = arrayOfMemberBox2;
      if (arrayOfMemberBox instanceof NativeJavaMethod)
        arrayOfMemberBox1 = ((NativeJavaMethod)arrayOfMemberBox).methods; 
    } 
    if (arrayOfMemberBox1 != null) {
      int j = arrayOfMemberBox1.length;
      for (b = 0; b < j; b++) {
        MemberBox memberBox = arrayOfMemberBox1[b];
        String str = liveConnectSignature(memberBox.argTypes);
        if (str.length() + i == paramString.length() && paramString.regionMatches(i, str, 0, str.length()))
          return memberBox; 
      } 
    } 
    return null;
  }
  
  private MemberBox findGetter(boolean paramBoolean, Map<String, Object> paramMap, String paramString1, String paramString2) {
    paramString1 = paramString1.concat(paramString2);
    if (paramMap.containsKey(paramString1)) {
      paramMap = (Map<String, Object>)paramMap.get(paramString1);
      if (paramMap instanceof NativeJavaMethod)
        return extractGetMethod(((NativeJavaMethod)paramMap).methods, paramBoolean); 
    } 
    return null;
  }
  
  private Constructor<?>[] getAccessibleConstructors(boolean paramBoolean) {
    if (paramBoolean && this.cl != ScriptRuntime.ClassClass)
      try {
        Constructor[] arrayOfConstructor = (Constructor[])this.cl.getDeclaredConstructors();
        AccessibleObject.setAccessible((AccessibleObject[])arrayOfConstructor, true);
        return (Constructor<?>[])arrayOfConstructor;
      } catch (SecurityException securityException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not access constructor  of class ");
        stringBuilder.append(this.cl.getName());
        stringBuilder.append(" due to lack of privileges.");
        Context.reportWarning(stringBuilder.toString());
      }  
    return this.cl.getConstructors();
  }
  
  private Field[] getAccessibleFields(boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean2 || paramBoolean1)
      try {
        ArrayList<Field> arrayList = new ArrayList();
        this();
        for (Class<?> clazz = this.cl; clazz != null; clazz = clazz.getSuperclass()) {
          for (Field field : clazz.getDeclaredFields()) {
            int i = field.getModifiers();
            if (paramBoolean2 || Modifier.isPublic(i) || Modifier.isProtected(i)) {
              if (!field.isAccessible())
                field.setAccessible(true); 
              arrayList.add(field);
            } 
          } 
        } 
        return arrayList.<Field>toArray(new Field[arrayList.size()]);
      } catch (SecurityException securityException) {} 
    return this.cl.getFields();
  }
  
  private Object getExplicitFunction(Scriptable paramScriptable, String paramString, Object paramObject, boolean paramBoolean) {
    Map<String, Object> map;
    if (paramBoolean) {
      map = this.staticMembers;
    } else {
      map = this.members;
    } 
    paramObject = null;
    MemberBox memberBox = findExplicitFunction(paramString, paramBoolean);
    if (memberBox != null) {
      Scriptable scriptable = ScriptableObject.getFunctionPrototype(paramScriptable);
      if (memberBox.isCtor()) {
        paramScriptable = new NativeJavaConstructor(memberBox);
        paramScriptable.setPrototype(scriptable);
        paramObject = paramScriptable;
        map.put(paramString, paramScriptable);
      } else {
        paramScriptable = (Scriptable)map.get(memberBox.getName());
        paramObject = paramScriptable;
        if (paramScriptable instanceof NativeJavaMethod) {
          paramObject = paramScriptable;
          if (((NativeJavaMethod)paramScriptable).methods.length > 1) {
            paramObject = new NativeJavaMethod(memberBox, paramString);
            paramObject.setPrototype(scriptable);
            map.put(paramString, paramObject);
          } 
        } 
      } 
    } 
    return paramObject;
  }
  
  static String javaSignature(Class<?> paramClass) {
    if (!paramClass.isArray())
      return paramClass.getName(); 
    byte b = 0;
    while (true) {
      b++;
      paramClass = paramClass.getComponentType();
      if (!paramClass.isArray()) {
        String str = paramClass.getName();
        if (b == 1)
          return str.concat("[]"); 
        StringBuilder stringBuilder = new StringBuilder(str.length() + "[]".length() * b);
        stringBuilder.append(str);
        while (b != 0) {
          b--;
          stringBuilder.append("[]");
        } 
        return stringBuilder.toString();
      } 
    } 
  }
  
  static String liveConnectSignature(Class<?>[] paramArrayOfClass) {
    int i = paramArrayOfClass.length;
    if (i == 0)
      return "()"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    for (int j = 0; j != i; j++) {
      if (j != 0)
        stringBuilder.append(','); 
      stringBuilder.append(javaSignature(paramArrayOfClass[j]));
    } 
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
  
  static JavaMembers lookupClass(Scriptable paramScriptable, Class<?> paramClass1, Class<?> paramClass2, boolean paramBoolean) {
    ClassCache classCache = ClassCache.get(paramScriptable);
    Map<Class<?>, JavaMembers> map = classCache.getClassCacheMap();
    Class<?> clazz = paramClass1;
    while (true) {
      JavaMembers javaMembers = map.get(clazz);
      if (javaMembers != null) {
        if (clazz != paramClass1)
          map.put(paramClass1, javaMembers); 
        return javaMembers;
      } 
      try {
        javaMembers = new JavaMembers(classCache.getAssociatedScope(), clazz, paramBoolean);
        if (classCache.isCachingEnabled()) {
          map.put(clazz, javaMembers);
          if (clazz != paramClass1)
            map.put(paramClass1, javaMembers); 
        } 
        return javaMembers;
      } catch (SecurityException securityException) {
        Class<?> clazz1;
        if (paramClass2 != null && paramClass2.isInterface()) {
          clazz1 = paramClass2;
          paramClass2 = null;
        } else {
          Class<?> clazz2 = clazz.getSuperclass();
          clazz1 = clazz2;
          if (clazz2 == null)
            if (clazz.isInterface()) {
              clazz1 = ScriptRuntime.ObjectClass;
            } else {
              throw securityException;
            }  
        } 
        clazz = clazz1;
      } 
    } 
  }
  
  private void reflect(Scriptable paramScriptable, boolean paramBoolean1, boolean paramBoolean2) {
    Map<String, Object> map;
    Object object3;
    Object<String, FieldAndMethods> object4;
    for (Method method : discoverAccessibleMethods(this.cl, paramBoolean1, paramBoolean2)) {
      Map<String, Object> map1;
      if (Modifier.isStatic(method.getModifiers())) {
        map1 = this.staticMembers;
      } else {
        map1 = this.members;
      } 
      String str = method.getName();
      object3 = map1.get(str);
      if (object3 == null) {
        map1.put(str, method);
      } else {
        ObjArray objArray;
        if (object3 instanceof ObjArray) {
          objArray = (ObjArray)object3;
        } else {
          if (!(object3 instanceof Method))
            Kit.codeBug(); 
          ObjArray objArray1 = new ObjArray();
          objArray1.add(object3);
          objArray.put(str, objArray1);
          objArray = objArray1;
        } 
        objArray.add(method);
      } 
    } 
    null = 0;
    object2 = SYNTHETIC_LOCAL_VARIABLE_4;
    while (null != 2) {
      int i;
      if (!null) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i) {
        map = this.staticMembers;
      } else {
        map = this.members;
      } 
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        MemberBox[] arrayOfMemberBox1;
        object3 = entry.getValue();
        if (object3 instanceof Method) {
          arrayOfMemberBox1 = new MemberBox[1];
          arrayOfMemberBox1[0] = new MemberBox((Method)object3);
        } else {
          object3 = object3;
          int j = object3.size();
          if (j < 2)
            Kit.codeBug(); 
          arrayOfMemberBox1 = new MemberBox[j];
          for (i = 0; i != j; i++)
            arrayOfMemberBox1[i] = new MemberBox((Method)object3.get(i)); 
        } 
        NativeJavaMethod nativeJavaMethod = new NativeJavaMethod(arrayOfMemberBox1);
        if (paramScriptable != null)
          ScriptRuntime.setFunctionProtoAndParent(nativeJavaMethod, paramScriptable); 
        map.put((String)entry.getKey(), nativeJavaMethod);
      } 
      null++;
    } 
    for (Object<String, FieldAndMethods> object2 : getAccessibleFields(paramBoolean1, paramBoolean2)) {
      String str = object2.getName();
      int i = object2.getModifiers();
      try {
        paramBoolean1 = Modifier.isStatic(i);
        if (paramBoolean1) {
          map = this.staticMembers;
        } else {
          map = this.members;
        } 
        object4 = (Object<String, FieldAndMethods>)map.get(str);
        if (object4 == null) {
          map.put(str, object2);
        } else {
          Map<String, FieldAndMethods> map1;
          if (object4 instanceof NativeJavaMethod) {
            object4 = object4;
            object3 = new FieldAndMethods();
            this(paramScriptable, ((NativeJavaMethod)object4).methods, (Field)object2);
            if (paramBoolean1) {
              object2 = (Object<String, FieldAndMethods>)this.staticFieldAndMethods;
            } else {
              map1 = this.fieldAndMethods;
            } 
            object4 = (Object<String, FieldAndMethods>)map1;
            if (map1 == null) {
              object4 = (Object)new HashMap<>();
              super();
              if (paramBoolean1) {
                this.staticFieldAndMethods = (Map<String, FieldAndMethods>)object4;
              } else {
                this.fieldAndMethods = (Map<String, FieldAndMethods>)object4;
              } 
            } 
            object4.put(str, (FieldAndMethods)object3);
            map.put(str, object3);
          } else if (object4 instanceof Field) {
            if (((Field)object4).getDeclaringClass().isAssignableFrom(map1.getDeclaringClass()))
              map.put(str, map1); 
          } else {
            Kit.codeBug();
          } 
        } 
      } catch (SecurityException securityException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not access field ");
        stringBuilder.append(str);
        stringBuilder.append(" of class ");
        stringBuilder.append(this.cl.getName());
        stringBuilder.append(" due to lack of privileges.");
        Context.reportWarning(stringBuilder.toString());
      } 
    } 
    byte b = 0;
    Object object1 = SYNTHETIC_LOCAL_VARIABLE_7;
    while (b != 2) {
      Map<String, Object> map1;
      if (b == 0) {
        paramBoolean1 = true;
      } else {
        paramBoolean1 = false;
      } 
      if (paramBoolean1) {
        map1 = this.staticMembers;
      } else {
        map1 = this.members;
      } 
      HashMap<Object, Object> hashMap = new HashMap<>();
      object2 = (Object<String, FieldAndMethods>)map1.keySet().iterator();
      while (true)
        hashMap.put(SYNTHETIC_LOCAL_VARIABLE_7, new BeanProperty((MemberBox)object4, (MemberBox)map, (NativeJavaMethod)object3)); 
      for (String str : hashMap.keySet())
        map1.put(str, hashMap.get(str)); 
      continue;
      b++;
    } 
    object1 = getAccessibleConstructors(paramBoolean2);
    MemberBox[] arrayOfMemberBox = new MemberBox[object1.length];
    for (b = 0; b != object1.length; b++)
      arrayOfMemberBox[b] = new MemberBox((Constructor<?>)object1[b]); 
    this.ctors = new NativeJavaMethod(arrayOfMemberBox, this.cl.getSimpleName());
  }
  
  Object get(Scriptable paramScriptable, String paramString, Object paramObject, boolean paramBoolean) {
    Map<String, Object> map;
    if (paramBoolean) {
      map = this.staticMembers;
    } else {
      map = this.members;
    } 
    Object object2 = map.get(paramString);
    Object object1 = object2;
    if (!paramBoolean) {
      object1 = object2;
      if (object2 == null)
        object1 = this.staticMembers.get(paramString); 
    } 
    object2 = object1;
    if (object1 == null) {
      Object object = getExplicitFunction(paramScriptable, paramString, paramObject, paramBoolean);
      object2 = object;
      if (object == null)
        return Scriptable.NOT_FOUND; 
    } 
    if (object2 instanceof Scriptable)
      return object2; 
    object1 = Context.getContext();
    try {
      Class<?> clazz;
      if (object2 instanceof BeanProperty) {
        BeanProperty beanProperty = (BeanProperty)object2;
        if (beanProperty.getter == null)
          return Scriptable.NOT_FOUND; 
        paramObject = beanProperty.getter.invoke(paramObject, Context.emptyArgs);
        clazz = beanProperty.getter.method().getReturnType();
      } else {
        Field field = (Field)object2;
        if (paramBoolean)
          paramObject = null; 
        paramObject = field.get(paramObject);
        clazz = field.getType();
      } 
      paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
      return object1.getWrapFactory().wrap((Context)object1, paramScriptable, paramObject, clazz);
    } catch (Exception exception) {
      throw Context.throwAsScriptRuntimeEx(exception);
    } 
  }
  
  Map<String, FieldAndMethods> getFieldAndMethodsObjects(Scriptable paramScriptable, Object paramObject, boolean paramBoolean) {
    Map<String, FieldAndMethods> map;
    if (paramBoolean) {
      map = this.staticFieldAndMethods;
    } else {
      map = this.fieldAndMethods;
    } 
    if (map == null)
      return null; 
    HashMap<Object, Object> hashMap = new HashMap<>(map.size());
    for (FieldAndMethods fieldAndMethods1 : map.values()) {
      FieldAndMethods fieldAndMethods2 = new FieldAndMethods(paramScriptable, fieldAndMethods1.methods, fieldAndMethods1.field);
      fieldAndMethods2.javaObject = paramObject;
      hashMap.put(fieldAndMethods1.field.getName(), fieldAndMethods2);
    } 
    return (Map)hashMap;
  }
  
  Object[] getIds(boolean paramBoolean) {
    Map<String, Object> map;
    if (paramBoolean) {
      map = this.staticMembers;
    } else {
      map = this.members;
    } 
    return map.keySet().toArray(new Object[map.size()]);
  }
  
  boolean has(String paramString, boolean paramBoolean) {
    if (paramBoolean) {
      map = this.staticMembers;
    } else {
      map = this.members;
    } 
    Map<String, Object> map = (Map<String, Object>)map.get(paramString);
    boolean bool = true;
    if (map != null)
      return true; 
    if (findExplicitFunction(paramString, paramBoolean) != null) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    } 
    return paramBoolean;
  }
  
  void put(Scriptable paramScriptable, String paramString, Object paramObject1, Object paramObject2, boolean paramBoolean) {
    Map<String, Object> map;
    if (paramBoolean) {
      map = this.staticMembers;
    } else {
      map = this.members;
    } 
    Object object2 = map.get(paramString);
    Object object3 = object2;
    if (!paramBoolean) {
      object3 = object2;
      if (object2 == null)
        object3 = this.staticMembers.get(paramString); 
    } 
    if (object3 != null) {
      object2 = object3;
      if (object3 instanceof FieldAndMethods)
        object2 = ((FieldAndMethods)map.get(paramString)).field; 
      if (object2 instanceof BeanProperty) {
        object3 = object2;
        if (((BeanProperty)object3).setter != null) {
          if (((BeanProperty)object3).setters == null || paramObject2 == null) {
            object = Context.jsToJava(paramObject2, ((BeanProperty)object3).setter.argTypes[0]);
            try {
              ((BeanProperty)object3).setter.invoke(paramObject1, new Object[] { object });
            } catch (Exception object) {
              throw Context.throwAsScriptRuntimeEx(object);
            } 
            return;
          } 
          ((BeanProperty)object3).setters.call(Context.getContext(), ScriptableObject.getTopLevelScope((Scriptable)object), (Scriptable)object, new Object[] { paramObject2 });
        } else {
          throw reportMemberNotFound(paramString);
        } 
      } else {
        if (!(object2 instanceof Field)) {
          String str;
          if (object2 == null) {
            str = "msg.java.internal.private";
          } else {
            str = "msg.java.method.assign";
          } 
          throw Context.reportRuntimeError1(str, paramString);
        } 
        Field field = (Field)object2;
        object1 = Context.jsToJava(paramObject2, field.getType());
        try {
          field.set(paramObject1, object1);
          return;
        } catch (IllegalAccessException illegalAccessException) {
          if ((field.getModifiers() & 0x10) != 0)
            return; 
          throw Context.throwAsScriptRuntimeEx(illegalAccessException);
        } catch (IllegalArgumentException object1) {
          throw Context.reportRuntimeError3("msg.java.internal.field.type", paramObject2.getClass().getName(), field, paramObject1.getClass().getName());
        } 
      } 
      return;
    } 
    throw reportMemberNotFound(object1);
  }
  
  RuntimeException reportMemberNotFound(String paramString) {
    return Context.reportRuntimeError2("msg.java.member.not.found", this.cl.getName(), paramString);
  }
  
  private static final class MethodSignature {
    private final Class<?>[] args;
    
    private final String name;
    
    private MethodSignature(String param1String, Class<?>[] param1ArrayOfClass) {
      this.name = param1String;
      this.args = param1ArrayOfClass;
    }
    
    MethodSignature(Method param1Method) {
      this(param1Method.getName(), param1Method.getParameterTypes());
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof MethodSignature;
      boolean bool1 = false;
      if (bool) {
        param1Object = param1Object;
        bool = bool1;
        if (((MethodSignature)param1Object).name.equals(this.name)) {
          bool = bool1;
          if (Arrays.equals((Object[])this.args, (Object[])((MethodSignature)param1Object).args))
            bool = true; 
        } 
        return bool;
      } 
      return false;
    }
    
    public int hashCode() {
      return this.name.hashCode() ^ this.args.length;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/JavaMembers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */