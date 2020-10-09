package com.trendmicro.hippo.optimizer;

import com.trendmicro.classfile.ClassFileWriter;
import com.trendmicro.hippo.CompilerEnvirons;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Evaluator;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.HippoException;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.ObjArray;
import com.trendmicro.hippo.ObjToIntMap;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.SecurityController;
import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.Name;
import com.trendmicro.hippo.ast.ScriptNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Codegen implements Evaluator {
  static final String DEFAULT_MAIN_METHOD_CLASS = "com.trendmicro.hippo.optimizer.OptRuntime";
  
  static final String FUNCTION_CONSTRUCTOR_SIGNATURE = "(Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;I)V";
  
  static final String FUNCTION_INIT_SIGNATURE = "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)V";
  
  static final String ID_FIELD_NAME = "_id";
  
  static final String REGEXP_INIT_METHOD_NAME = "_reInit";
  
  static final String REGEXP_INIT_METHOD_SIGNATURE = "(Lcom/trendmicro/hippo/Context;)V";
  
  private static final String SUPER_CLASS_NAME = "com.trendmicro.hippo.NativeFunction";
  
  private static final Object globalLock = new Object();
  
  private static int globalSerialClassCounter;
  
  private CompilerEnvirons compilerEnv;
  
  private ObjArray directCallTargets;
  
  private double[] itsConstantList;
  
  private int itsConstantListSize;
  
  String mainClassName;
  
  String mainClassSignature;
  
  private String mainMethodClass = "com.trendmicro.hippo.optimizer.OptRuntime";
  
  private ObjToIntMap scriptOrFnIndexes;
  
  ScriptNode[] scriptOrFnNodes;
  
  private static void addDoubleWrap(ClassFileWriter paramClassFileWriter) {
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/optimizer/OptRuntime", "wrapDouble", "(D)Ljava/lang/Double;");
  }
  
  static RuntimeException badTree() {
    throw new RuntimeException("Bad tree in codegen");
  }
  
  private static void collectScriptNodes_r(ScriptNode paramScriptNode, ObjArray paramObjArray) {
    paramObjArray.add(paramScriptNode);
    int i = paramScriptNode.getFunctionCount();
    for (int j = 0; j != i; j++)
      collectScriptNodes_r((ScriptNode)paramScriptNode.getFunctionNode(j), paramObjArray); 
  }
  
  private Class<?> defineClass(Object paramObject1, Object paramObject2) {
    Object[] arrayOfObject = (Object[])paramObject1;
    paramObject1 = arrayOfObject[0];
    byte[] arrayOfByte = (byte[])arrayOfObject[1];
    paramObject2 = SecurityController.createLoader(getClass().getClassLoader(), paramObject2);
    try {
      paramObject1 = paramObject2.defineClass((String)paramObject1, arrayOfByte);
      paramObject2.linkClass((Class)paramObject1);
      return (Class<?>)paramObject1;
    } catch (SecurityException securityException) {
    
    } catch (IllegalArgumentException illegalArgumentException) {}
    paramObject2 = new StringBuilder();
    paramObject2.append("Malformed optimizer package ");
    paramObject2.append(illegalArgumentException);
    throw new RuntimeException(paramObject2.toString());
  }
  
  private void emitConstantDudeInitializers(ClassFileWriter paramClassFileWriter) {
    int i = this.itsConstantListSize;
    if (i == 0)
      return; 
    paramClassFileWriter.startMethod("<clinit>", "()V", (short)24);
    double[] arrayOfDouble = this.itsConstantList;
    for (int j = 0; j != i; j++) {
      double d = arrayOfDouble[j];
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("_k");
      stringBuilder.append(j);
      String str2 = stringBuilder.toString();
      String str1 = getStaticConstantWrapperType(d);
      paramClassFileWriter.addField(str2, str1, (short)10);
      int k = (int)d;
      if (k == d) {
        paramClassFileWriter.addPush(k);
        paramClassFileWriter.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
      } else {
        paramClassFileWriter.addPush(d);
        addDoubleWrap(paramClassFileWriter);
      } 
      paramClassFileWriter.add(179, this.mainClassName, str2, str1);
    } 
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod((short)0);
  }
  
  private void emitDirectConstructor(ClassFileWriter paramClassFileWriter, OptFunctionNode paramOptFunctionNode) {
    paramClassFileWriter.startMethod(getDirectCtorName((ScriptNode)paramOptFunctionNode.fnode), getBodyMethodSignature((ScriptNode)paramOptFunctionNode.fnode), (short)10);
    int i = paramOptFunctionNode.fnode.getParamCount();
    int j = i * 3 + 4 + 1;
    paramClassFileWriter.addALoad(0);
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.addInvoke(182, "com/trendmicro/hippo/BaseFunction", "createObject", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.addAStore(j);
    paramClassFileWriter.addALoad(0);
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.addALoad(j);
    int k;
    for (k = 0; k < i; k++) {
      paramClassFileWriter.addALoad(k * 3 + 4);
      paramClassFileWriter.addDLoad(k * 3 + 5);
    } 
    paramClassFileWriter.addALoad(i * 3 + 4);
    paramClassFileWriter.addInvoke(184, this.mainClassName, getBodyMethodName((ScriptNode)paramOptFunctionNode.fnode), getBodyMethodSignature((ScriptNode)paramOptFunctionNode.fnode));
    k = paramClassFileWriter.acquireLabel();
    paramClassFileWriter.add(89);
    paramClassFileWriter.add(193, "com/trendmicro/hippo/Scriptable");
    paramClassFileWriter.add(153, k);
    paramClassFileWriter.add(192, "com/trendmicro/hippo/Scriptable");
    paramClassFileWriter.add(176);
    paramClassFileWriter.markLabel(k);
    paramClassFileWriter.addALoad(j);
    paramClassFileWriter.add(176);
    paramClassFileWriter.stopMethod((short)(j + 1));
  }
  
  private void emitRegExpInit(ClassFileWriter paramClassFileWriter) {
    int i = 0;
    byte b = 0;
    while (true) {
      ScriptNode[] arrayOfScriptNode = this.scriptOrFnNodes;
      if (b != arrayOfScriptNode.length) {
        i += arrayOfScriptNode[b].getRegexpCount();
        b++;
        continue;
      } 
      if (i == 0)
        return; 
      paramClassFileWriter.startMethod("_reInit", "(Lcom/trendmicro/hippo/Context;)V", (short)10);
      paramClassFileWriter.addField("_reInitDone", "Z", (short)74);
      paramClassFileWriter.add(178, this.mainClassName, "_reInitDone", "Z");
      int j = paramClassFileWriter.acquireLabel();
      paramClassFileWriter.add(153, j);
      paramClassFileWriter.add(177);
      paramClassFileWriter.markLabel(j);
      paramClassFileWriter.addALoad(0);
      paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/ScriptRuntime", "checkRegExpProxy", "(Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/RegExpProxy;");
      paramClassFileWriter.addAStore(1);
      b = 0;
      int k = i;
      while (true) {
        arrayOfScriptNode = this.scriptOrFnNodes;
        if (b != arrayOfScriptNode.length) {
          ScriptNode scriptNode = arrayOfScriptNode[b];
          int m = scriptNode.getRegexpCount();
          int n = 0;
          i = j;
          while (n != m) {
            String str2 = getCompiledRegexpName(scriptNode, n);
            String str1 = scriptNode.getRegexpString(n);
            String str3 = scriptNode.getRegexpFlags(n);
            paramClassFileWriter.addField(str2, "Ljava/lang/Object;", (short)10);
            paramClassFileWriter.addALoad(1);
            paramClassFileWriter.addALoad(0);
            paramClassFileWriter.addPush(str1);
            if (str3 == null) {
              paramClassFileWriter.add(1);
            } else {
              paramClassFileWriter.addPush(str3);
            } 
            paramClassFileWriter.addInvoke(185, "com/trendmicro/hippo/RegExpProxy", "compileRegExp", "(Lcom/trendmicro/hippo/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
            paramClassFileWriter.add(179, this.mainClassName, str2, "Ljava/lang/Object;");
            n++;
          } 
          b++;
          j = i;
          continue;
        } 
        paramClassFileWriter.addPush(1);
        paramClassFileWriter.add(179, this.mainClassName, "_reInitDone", "Z");
        paramClassFileWriter.add(177);
        paramClassFileWriter.stopMethod((short)2);
        return;
      } 
      break;
    } 
  }
  
  private void generateCallMethod(ClassFileWriter paramClassFileWriter, boolean paramBoolean) {
    paramClassFileWriter.startMethod("call", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
    int i = paramClassFileWriter.acquireLabel();
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/ScriptRuntime", "hasTopCall", "(Lcom/trendmicro/hippo/Context;)Z");
    paramClassFileWriter.add(154, i);
    boolean bool = false;
    paramClassFileWriter.addALoad(0);
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.addALoad(3);
    byte b = 4;
    paramClassFileWriter.addALoad(4);
    paramClassFileWriter.addPush(paramBoolean);
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/ScriptRuntime", "doTopCall", "(Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Z)Ljava/lang/Object;");
    paramClassFileWriter.add(176);
    paramClassFileWriter.markLabel(i);
    paramClassFileWriter.addALoad(0);
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.addALoad(3);
    paramClassFileWriter.addALoad(4);
    int j = this.scriptOrFnNodes.length;
    if (2 <= j)
      bool = true; 
    int k = 0;
    int m = 0;
    if (bool) {
      paramClassFileWriter.addLoadThis();
      paramClassFileWriter.add(180, paramClassFileWriter.getClassName(), "_id", "I");
      k = paramClassFileWriter.addTableSwitch(1, j - 1);
    } 
    int n = 0;
    while (n != j) {
      ScriptNode scriptNode = this.scriptOrFnNodes[n];
      i = m;
      if (bool)
        if (n == 0) {
          paramClassFileWriter.markTableSwitchDefault(k);
          i = paramClassFileWriter.getStackTop();
        } else {
          paramClassFileWriter.markTableSwitchCase(k, n - 1, m);
          i = m;
        }  
      if (scriptNode.getType() == 110) {
        OptFunctionNode optFunctionNode = OptFunctionNode.get(scriptNode);
        if (optFunctionNode.isTargetOfDirectCall()) {
          int i1 = optFunctionNode.fnode.getParamCount();
          if (i1 != 0) {
            m = 0;
            while (m != i1) {
              paramClassFileWriter.add(190);
              paramClassFileWriter.addPush(m);
              int i2 = paramClassFileWriter.acquireLabel();
              int i3 = paramClassFileWriter.acquireLabel();
              paramClassFileWriter.add(164, i2);
              paramClassFileWriter.addALoad(b);
              paramClassFileWriter.addPush(m);
              paramClassFileWriter.add(50);
              paramClassFileWriter.add(167, i3);
              paramClassFileWriter.markLabel(i2);
              pushUndefined(paramClassFileWriter);
              paramClassFileWriter.markLabel(i3);
              paramClassFileWriter.adjustStackTop(-1);
              paramClassFileWriter.addPush(0.0D);
              paramClassFileWriter.addALoad(4);
              m++;
              b = 4;
            } 
          } 
        } 
      } 
      paramClassFileWriter.addInvoke(184, this.mainClassName, getBodyMethodName(scriptNode), getBodyMethodSignature(scriptNode));
      paramClassFileWriter.add(176);
      n++;
      m = i;
    } 
    paramClassFileWriter.stopMethod((short)5);
  }
  
  private byte[] generateCode(String paramString) {
    int i = this.scriptOrFnNodes[0].getType();
    byte b = 1;
    if (i == 137) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = b;
    if (this.scriptOrFnNodes.length <= 1)
      if (i == 0) {
        j = b;
      } else {
        j = 0;
      }  
    boolean bool = this.scriptOrFnNodes[0].isInStrictMode();
    String str = null;
    if (this.compilerEnv.isGenerateDebugInfo())
      str = this.scriptOrFnNodes[0].getSourceName(); 
    ClassFileWriter classFileWriter = new ClassFileWriter(this.mainClassName, "com.trendmicro.hippo.NativeFunction", str);
    classFileWriter.addField("_id", "I", (short)2);
    if (j)
      generateFunctionConstructor(classFileWriter); 
    if (i != 0) {
      classFileWriter.addInterface("com/trendmicro/hippo/Script");
      generateScriptCtor(classFileWriter);
      generateMain(classFileWriter);
      generateExecute(classFileWriter);
    } 
    generateCallMethod(classFileWriter, bool);
    generateResumeGenerator(classFileWriter);
    generateNativeFunctionOverrides(classFileWriter, paramString);
    j = this.scriptOrFnNodes.length;
    for (i = 0; i != j; i++) {
      ScriptNode scriptNode = this.scriptOrFnNodes[i];
      BodyCodegen bodyCodegen = new BodyCodegen();
      bodyCodegen.cfw = classFileWriter;
      bodyCodegen.codegen = this;
      bodyCodegen.compilerEnv = this.compilerEnv;
      bodyCodegen.scriptOrFn = scriptNode;
      bodyCodegen.scriptOrFnIndex = i;
      bodyCodegen.generateBodyCode();
      if (scriptNode.getType() == 110) {
        OptFunctionNode optFunctionNode = OptFunctionNode.get(scriptNode);
        generateFunctionInit(classFileWriter, optFunctionNode);
        if (optFunctionNode.isTargetOfDirectCall())
          emitDirectConstructor(classFileWriter, optFunctionNode); 
      } 
    } 
    emitRegExpInit(classFileWriter);
    emitConstantDudeInitializers(classFileWriter);
    return classFileWriter.toByteArray();
  }
  
  private void generateExecute(ClassFileWriter paramClassFileWriter) {
    paramClassFileWriter.startMethod("exec", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;", (short)17);
    paramClassFileWriter.addLoadThis();
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.add(89);
    paramClassFileWriter.add(1);
    paramClassFileWriter.addInvoke(182, paramClassFileWriter.getClassName(), "call", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    paramClassFileWriter.add(176);
    paramClassFileWriter.stopMethod((short)3);
  }
  
  private void generateFunctionConstructor(ClassFileWriter paramClassFileWriter) {
    int i;
    boolean bool = true;
    paramClassFileWriter.startMethod("<init>", "(Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;I)V", (short)1);
    paramClassFileWriter.addALoad(0);
    paramClassFileWriter.addInvoke(183, "com.trendmicro.hippo.NativeFunction", "<init>", "()V");
    paramClassFileWriter.addLoadThis();
    paramClassFileWriter.addILoad(3);
    paramClassFileWriter.add(181, paramClassFileWriter.getClassName(), "_id", "I");
    paramClassFileWriter.addLoadThis();
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.addALoad(1);
    if (this.scriptOrFnNodes[0].getType() == 137) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = this.scriptOrFnNodes.length;
    if (i != j) {
      if (2 > j - i)
        bool = false; 
      int k = 0;
      short s = 0;
      if (bool) {
        paramClassFileWriter.addILoad(3);
        k = paramClassFileWriter.addTableSwitch(i + 1, j - 1);
      } 
      int m = i;
      while (m != j) {
        short s1 = s;
        if (bool)
          if (m == i) {
            paramClassFileWriter.markTableSwitchDefault(k);
            s1 = paramClassFileWriter.getStackTop();
          } else {
            paramClassFileWriter.markTableSwitchCase(k, m - 1 - i, s);
            s1 = s;
          }  
        OptFunctionNode optFunctionNode = OptFunctionNode.get(this.scriptOrFnNodes[m]);
        paramClassFileWriter.addInvoke(183, this.mainClassName, getFunctionInitMethodName(optFunctionNode), "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)V");
        paramClassFileWriter.add(177);
        m++;
        s = s1;
      } 
      paramClassFileWriter.stopMethod((short)4);
      return;
    } 
    throw badTree();
  }
  
  private void generateFunctionInit(ClassFileWriter paramClassFileWriter, OptFunctionNode paramOptFunctionNode) {
    paramClassFileWriter.startMethod(getFunctionInitMethodName(paramOptFunctionNode), "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)V", (short)18);
    paramClassFileWriter.addLoadThis();
    paramClassFileWriter.addALoad(1);
    paramClassFileWriter.addALoad(2);
    paramClassFileWriter.addInvoke(182, "com/trendmicro/hippo/NativeFunction", "initScriptFunction", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)V");
    if (paramOptFunctionNode.fnode.getRegexpCount() != 0) {
      paramClassFileWriter.addALoad(1);
      paramClassFileWriter.addInvoke(184, this.mainClassName, "_reInit", "(Lcom/trendmicro/hippo/Context;)V");
    } 
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod((short)3);
  }
  
  private void generateMain(ClassFileWriter paramClassFileWriter) {
    paramClassFileWriter.startMethod("main", "([Ljava/lang/String;)V", (short)9);
    paramClassFileWriter.add(187, paramClassFileWriter.getClassName());
    paramClassFileWriter.add(89);
    paramClassFileWriter.addInvoke(183, paramClassFileWriter.getClassName(), "<init>", "()V");
    paramClassFileWriter.add(42);
    paramClassFileWriter.addInvoke(184, this.mainMethodClass, "main", "(Lcom/trendmicro/hippo/Script;[Ljava/lang/String;)V");
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod((short)1);
  }
  
  private void generateNativeFunctionOverrides(ClassFileWriter paramClassFileWriter, String paramString) {
    String str = "()I";
    paramClassFileWriter.startMethod("getLanguageVersion", "()I", (short)1);
    paramClassFileWriter.addPush(this.compilerEnv.getLanguageVersion());
    short s = 172;
    paramClassFileWriter.add(172);
    paramClassFileWriter.stopMethod((short)1);
    boolean bool1 = false;
    boolean bool2 = true;
    byte b1 = 2;
    for (byte b2 = 0; b2 != 6; b2++) {
      if (b2 != 4 || paramString != null) {
        short s1;
        if (b2) {
          if (b2 != 1) {
            if (b2 != 2) {
              if (b2 != 3) {
                if (b2 != 4) {
                  if (b2 == 5) {
                    s = 3;
                    paramClassFileWriter.startMethod("getParamOrVarConst", "(I)Z", (short)1);
                    s1 = s;
                  } else {
                    throw Kit.codeBug();
                  } 
                } else {
                  s = 1;
                  paramClassFileWriter.startMethod("getEncodedSource", "()Ljava/lang/String;", (short)1);
                  paramClassFileWriter.addPush(paramString);
                  s1 = s;
                } 
              } else {
                s = 2;
                paramClassFileWriter.startMethod("getParamOrVarName", "(I)Ljava/lang/String;", (short)1);
                s1 = s;
              } 
            } else {
              s = 1;
              paramClassFileWriter.startMethod("getParamAndVarCount", str, (short)1);
              s1 = s;
            } 
          } else {
            s = 1;
            paramClassFileWriter.startMethod("getParamCount", str, (short)1);
            s1 = s;
          } 
        } else {
          s = 1;
          paramClassFileWriter.startMethod("getFunctionName", "()Ljava/lang/String;", (short)1);
          s1 = s;
        } 
        int i = this.scriptOrFnNodes.length;
        int j = 0;
        if (i > 1) {
          paramClassFileWriter.addLoadThis();
          paramClassFileWriter.add(180, paramClassFileWriter.getClassName(), "_id", "I");
          j = paramClassFileWriter.addTableSwitch(1, i - 1);
        } 
        int k = 0;
        s = 0;
        while (k != i) {
          ScriptNode scriptNode = this.scriptOrFnNodes[k];
          if (k == 0) {
            if (i > 1) {
              paramClassFileWriter.markTableSwitchDefault(j);
              s = paramClassFileWriter.getStackTop();
            } 
          } else {
            paramClassFileWriter.markTableSwitchCase(j, k - 1, s);
          } 
          if (b2) {
            if (b2 != 1) {
              if (b2 != 2) {
                if (b2 != 3) {
                  if (b2 != 4) {
                    if (b2 == 5) {
                      int m = scriptNode.getParamAndVarCount();
                      boolean[] arrayOfBoolean = scriptNode.getParamAndVarConst();
                      if (m == 0) {
                        paramClassFileWriter.add(3);
                        paramClassFileWriter.add(172);
                      } else if (m == 1) {
                        paramClassFileWriter.addPush(arrayOfBoolean[0]);
                        paramClassFileWriter.add(172);
                      } else {
                        paramClassFileWriter.addILoad(1);
                        int n = paramClassFileWriter.addTableSwitch(1, m - 1);
                        for (int i1 = 0; i1 != m; i1++) {
                          if (paramClassFileWriter.getStackTop() != 0)
                            Kit.codeBug(); 
                          if (i1 == 0) {
                            paramClassFileWriter.markTableSwitchDefault(n);
                          } else {
                            paramClassFileWriter.markTableSwitchCase(n, i1 - 1, 0);
                          } 
                          paramClassFileWriter.addPush(arrayOfBoolean[i1]);
                          paramClassFileWriter.add(172);
                        } 
                      } 
                    } else {
                      throw Kit.codeBug();
                    } 
                  } else {
                    paramClassFileWriter.addPush(scriptNode.getEncodedSourceStart());
                    paramClassFileWriter.addPush(scriptNode.getEncodedSourceEnd());
                    paramClassFileWriter.addInvoke(182, "java/lang/String", "substring", "(II)Ljava/lang/String;");
                    paramClassFileWriter.add(176);
                  } 
                } else {
                  int m = scriptNode.getParamAndVarCount();
                  if (m == 0) {
                    paramClassFileWriter.add(1);
                    paramClassFileWriter.add(176);
                  } else if (m == 1) {
                    paramClassFileWriter.addPush(scriptNode.getParamOrVarName(0));
                    paramClassFileWriter.add(176);
                  } else {
                    paramClassFileWriter.addILoad(1);
                    int i1 = paramClassFileWriter.addTableSwitch(1, m - 1);
                    for (int n = 0; n != m; n++) {
                      if (paramClassFileWriter.getStackTop() != 0)
                        Kit.codeBug(); 
                      String str1 = scriptNode.getParamOrVarName(n);
                      if (n == 0) {
                        paramClassFileWriter.markTableSwitchDefault(i1);
                      } else {
                        paramClassFileWriter.markTableSwitchCase(i1, n - 1, 0);
                      } 
                      paramClassFileWriter.addPush(str1);
                      paramClassFileWriter.add(176);
                    } 
                  } 
                } 
              } else {
                paramClassFileWriter.addPush(scriptNode.getParamAndVarCount());
                paramClassFileWriter.add(172);
              } 
            } else {
              paramClassFileWriter.addPush(scriptNode.getParamCount());
              paramClassFileWriter.add(172);
            } 
          } else {
            if (scriptNode.getType() == 137) {
              paramClassFileWriter.addPush("");
            } else {
              paramClassFileWriter.addPush(((FunctionNode)scriptNode).getName());
            } 
            paramClassFileWriter.add(176);
          } 
          k++;
        } 
        s = 172;
        paramClassFileWriter.stopMethod(s1);
      } 
    } 
  }
  
  private void generateResumeGenerator(ClassFileWriter paramClassFileWriter) {
    int i = 0;
    byte b = 0;
    while (true) {
      ScriptNode[] arrayOfScriptNode = this.scriptOrFnNodes;
      if (b < arrayOfScriptNode.length) {
        if (isGenerator(arrayOfScriptNode[b]))
          i = 1; 
        b++;
        continue;
      } 
      if (!i)
        return; 
      paramClassFileWriter.startMethod("resumeGenerator", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;ILjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
      paramClassFileWriter.addALoad(0);
      paramClassFileWriter.addALoad(1);
      paramClassFileWriter.addALoad(2);
      paramClassFileWriter.addALoad(4);
      paramClassFileWriter.addALoad(5);
      paramClassFileWriter.addILoad(3);
      paramClassFileWriter.addLoadThis();
      paramClassFileWriter.add(180, paramClassFileWriter.getClassName(), "_id", "I");
      i = paramClassFileWriter.addTableSwitch(0, this.scriptOrFnNodes.length - 1);
      paramClassFileWriter.markTableSwitchDefault(i);
      int j = paramClassFileWriter.acquireLabel();
      b = 0;
      while (true) {
        arrayOfScriptNode = this.scriptOrFnNodes;
        if (b < arrayOfScriptNode.length) {
          ScriptNode scriptNode = arrayOfScriptNode[b];
          paramClassFileWriter.markTableSwitchCase(i, b, 6);
          if (isGenerator(scriptNode)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(this.mainClassSignature);
            stringBuilder.append("Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;");
            String str1 = stringBuilder.toString();
            String str2 = this.mainClassName;
            stringBuilder = new StringBuilder();
            stringBuilder.append(getBodyMethodName(scriptNode));
            stringBuilder.append("_gen");
            paramClassFileWriter.addInvoke(184, str2, stringBuilder.toString(), str1);
            paramClassFileWriter.add(176);
          } else {
            paramClassFileWriter.add(167, j);
          } 
          b++;
          continue;
        } 
        paramClassFileWriter.markLabel(j);
        pushUndefined(paramClassFileWriter);
        paramClassFileWriter.add(176);
        paramClassFileWriter.stopMethod((short)6);
        return;
      } 
      break;
    } 
  }
  
  private void generateScriptCtor(ClassFileWriter paramClassFileWriter) {
    paramClassFileWriter.startMethod("<init>", "()V", (short)1);
    paramClassFileWriter.addLoadThis();
    paramClassFileWriter.addInvoke(183, "com.trendmicro.hippo.NativeFunction", "<init>", "()V");
    paramClassFileWriter.addLoadThis();
    paramClassFileWriter.addPush(0);
    paramClassFileWriter.add(181, paramClassFileWriter.getClassName(), "_id", "I");
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod((short)1);
  }
  
  private static String getStaticConstantWrapperType(double paramDouble) {
    return ((int)paramDouble == paramDouble) ? "Ljava/lang/Integer;" : "Ljava/lang/Double;";
  }
  
  private static void initOptFunctions_r(ScriptNode paramScriptNode) {
    int i = 0;
    int j = paramScriptNode.getFunctionCount();
    while (i != j) {
      FunctionNode functionNode = paramScriptNode.getFunctionNode(i);
      new OptFunctionNode(functionNode);
      initOptFunctions_r((ScriptNode)functionNode);
      i++;
    } 
  }
  
  private void initScriptNodesData(ScriptNode paramScriptNode) {
    ObjArray objArray = new ObjArray();
    collectScriptNodes_r(paramScriptNode, objArray);
    int i = objArray.size();
    ScriptNode[] arrayOfScriptNode = new ScriptNode[i];
    this.scriptOrFnNodes = arrayOfScriptNode;
    objArray.toArray((Object[])arrayOfScriptNode);
    this.scriptOrFnIndexes = new ObjToIntMap(i);
    for (int j = 0; j != i; j++)
      this.scriptOrFnIndexes.put(this.scriptOrFnNodes[j], j); 
  }
  
  static boolean isGenerator(ScriptNode paramScriptNode) {
    boolean bool;
    if (paramScriptNode.getType() == 110 && ((FunctionNode)paramScriptNode).isGenerator()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static void pushUndefined(ClassFileWriter paramClassFileWriter) {
    paramClassFileWriter.add(178, "com/trendmicro/hippo/Undefined", "instance", "Ljava/lang/Object;");
  }
  
  private void transform(ScriptNode paramScriptNode) {
    HashMap<Object, Object> hashMap;
    initOptFunctions_r(paramScriptNode);
    int i = this.compilerEnv.getOptimizationLevel();
    String str1 = null;
    String str2 = null;
    String str3 = str1;
    if (i > 0) {
      str3 = str1;
      if (paramScriptNode.getType() == 137) {
        int j = paramScriptNode.getFunctionCount();
        int k = 0;
        while (true) {
          str3 = str2;
          if (k != j) {
            OptFunctionNode optFunctionNode = OptFunctionNode.get(paramScriptNode, k);
            str3 = str2;
            if (optFunctionNode.fnode.getFunctionType() == 1) {
              str1 = optFunctionNode.fnode.getName();
              str3 = str2;
              if (str1.length() != 0) {
                str3 = str2;
                if (str2 == null)
                  hashMap = new HashMap<>(); 
                hashMap.put(str1, optFunctionNode);
              } 
            } 
            k++;
            HashMap<Object, Object> hashMap1 = hashMap;
            continue;
          } 
          break;
        } 
      } 
    } 
    if (hashMap != null)
      this.directCallTargets = new ObjArray(); 
    (new OptTransformer((Map)hashMap, this.directCallTargets)).transform(paramScriptNode, this.compilerEnv);
    if (i > 0)
      (new Optimizer()).optimize(paramScriptNode); 
  }
  
  public void captureStackInfo(HippoException paramHippoException) {
    throw new UnsupportedOperationException();
  }
  
  String cleanName(ScriptNode paramScriptNode) {
    String str;
    if (paramScriptNode instanceof FunctionNode) {
      Name name = ((FunctionNode)paramScriptNode).getFunctionName();
      if (name == null) {
        str = "anonymous";
      } else {
        str = str.getIdentifier();
      } 
    } else {
      str = "script";
    } 
    return str;
  }
  
  public Object compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean) {
    synchronized (globalLock) {
      int i = globalSerialClassCounter + 1;
      globalSerialClassCounter = i;
      null = "c";
      if (paramScriptNode.getSourceName().length() > 0) {
        String str = paramScriptNode.getSourceName().replaceAll("\\W", "_");
        null = str;
        if (!Character.isJavaIdentifierStart(str.charAt(0))) {
          null = new StringBuilder();
          null.append("_");
          null.append(str);
          null = null.toString();
        } 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("com.trendmicro.hippo.gen.");
      stringBuilder.append((String)null);
      stringBuilder.append("_");
      stringBuilder.append(i);
      null = stringBuilder.toString();
      return new Object[] { null, compileToClassFile(paramCompilerEnvirons, (String)null, paramScriptNode, paramString, paramBoolean) };
    } 
  }
  
  public byte[] compileToClassFile(CompilerEnvirons paramCompilerEnvirons, String paramString1, ScriptNode paramScriptNode, String paramString2, boolean paramBoolean) {
    FunctionNode functionNode;
    this.compilerEnv = paramCompilerEnvirons;
    transform(paramScriptNode);
    ScriptNode scriptNode = paramScriptNode;
    if (paramBoolean)
      functionNode = paramScriptNode.getFunctionNode(0); 
    initScriptNodesData((ScriptNode)functionNode);
    this.mainClassName = paramString1;
    this.mainClassSignature = ClassFileWriter.classNameToSignature(paramString1);
    return generateCode(paramString2);
  }
  
  public Function createFunctionObject(Context paramContext, Scriptable paramScriptable, Object<?> paramObject1, Object paramObject2) {
    paramObject1 = (Object<?>)defineClass(paramObject1, paramObject2);
    try {
      return (Function)paramObject1.getConstructors()[0].newInstance(new Object[] { paramScriptable, paramContext, Integer.valueOf(0) });
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to instantiate compiled class:");
      stringBuilder.append(exception.toString());
      throw new RuntimeException(stringBuilder.toString());
    } 
  }
  
  public Script createScriptObject(Object<?> paramObject1, Object paramObject2) {
    paramObject1 = (Object<?>)defineClass(paramObject1, paramObject2);
    try {
      return (Script)paramObject1.newInstance();
    } catch (Exception exception) {
      paramObject1 = (Object<?>)new StringBuilder();
      paramObject1.append("Unable to instantiate compiled class:");
      paramObject1.append(exception.toString());
      throw new RuntimeException(paramObject1.toString());
    } 
  }
  
  String getBodyMethodName(ScriptNode paramScriptNode) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("_c_");
    stringBuilder.append(cleanName(paramScriptNode));
    stringBuilder.append("_");
    stringBuilder.append(getIndex(paramScriptNode));
    return stringBuilder.toString();
  }
  
  String getBodyMethodSignature(ScriptNode paramScriptNode) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    stringBuilder.append(this.mainClassSignature);
    stringBuilder.append("Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;");
    if (paramScriptNode.getType() == 110) {
      OptFunctionNode optFunctionNode = OptFunctionNode.get(paramScriptNode);
      if (optFunctionNode.isTargetOfDirectCall()) {
        int i = optFunctionNode.fnode.getParamCount();
        for (int j = 0; j != i; j++)
          stringBuilder.append("Ljava/lang/Object;D"); 
      } 
    } 
    stringBuilder.append("[Ljava/lang/Object;)Ljava/lang/Object;");
    return stringBuilder.toString();
  }
  
  String getCompiledRegexpName(ScriptNode paramScriptNode, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("_re");
    stringBuilder.append(getIndex(paramScriptNode));
    stringBuilder.append("_");
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
  
  String getDirectCtorName(ScriptNode paramScriptNode) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("_n");
    stringBuilder.append(getIndex(paramScriptNode));
    return stringBuilder.toString();
  }
  
  String getFunctionInitMethodName(OptFunctionNode paramOptFunctionNode) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("_i");
    stringBuilder.append(getIndex((ScriptNode)paramOptFunctionNode.fnode));
    return stringBuilder.toString();
  }
  
  int getIndex(ScriptNode paramScriptNode) {
    return this.scriptOrFnIndexes.getExisting(paramScriptNode);
  }
  
  public String getPatchedStack(HippoException paramHippoException, String paramString) {
    throw new UnsupportedOperationException();
  }
  
  public List<String> getScriptStack(HippoException paramHippoException) {
    throw new UnsupportedOperationException();
  }
  
  public String getSourcePositionFromStack(Context paramContext, int[] paramArrayOfint) {
    throw new UnsupportedOperationException();
  }
  
  void pushNumberAsObject(ClassFileWriter paramClassFileWriter, double paramDouble) {
    if (paramDouble == 0.0D) {
      if (1.0D / paramDouble > 0.0D) {
        paramClassFileWriter.add(178, "com/trendmicro/hippo/optimizer/OptRuntime", "zeroObj", "Ljava/lang/Double;");
      } else {
        paramClassFileWriter.addPush(paramDouble);
        addDoubleWrap(paramClassFileWriter);
      } 
    } else {
      if (paramDouble == 1.0D) {
        paramClassFileWriter.add(178, "com/trendmicro/hippo/optimizer/OptRuntime", "oneObj", "Ljava/lang/Double;");
        return;
      } 
      if (paramDouble == -1.0D) {
        paramClassFileWriter.add(178, "com/trendmicro/hippo/optimizer/OptRuntime", "minusOneObj", "Ljava/lang/Double;");
      } else if (paramDouble != paramDouble) {
        paramClassFileWriter.add(178, "com/trendmicro/hippo/ScriptRuntime", "NaNobj", "Ljava/lang/Double;");
      } else if (this.itsConstantListSize >= 2000) {
        paramClassFileWriter.addPush(paramDouble);
        addDoubleWrap(paramClassFileWriter);
      } else {
        int i = this.itsConstantListSize;
        int j = 0;
        int k = 0;
        if (i == 0) {
          this.itsConstantList = new double[64];
        } else {
          double[] arrayOfDouble = this.itsConstantList;
          while (k != i && arrayOfDouble[k] != paramDouble)
            k++; 
          j = k;
          if (i == arrayOfDouble.length) {
            arrayOfDouble = new double[i * 2];
            System.arraycopy(this.itsConstantList, 0, arrayOfDouble, 0, i);
            this.itsConstantList = arrayOfDouble;
            j = k;
          } 
        } 
        if (j == i) {
          this.itsConstantList[i] = paramDouble;
          this.itsConstantListSize = i + 1;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_k");
        stringBuilder.append(j);
        String str2 = stringBuilder.toString();
        String str1 = getStaticConstantWrapperType(paramDouble);
        paramClassFileWriter.add(178, this.mainClassName, str2, str1);
      } 
    } 
  }
  
  public void setEvalScriptFlag(Script paramScript) {
    throw new UnsupportedOperationException();
  }
  
  public void setMainMethodClass(String paramString) {
    this.mainMethodClass = paramString;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/Codegen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */