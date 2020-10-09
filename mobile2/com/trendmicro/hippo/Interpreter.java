package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.ScriptNode;
import com.trendmicro.hippo.debug.DebugFrame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Interpreter extends Icode implements Evaluator {
  static final int EXCEPTION_HANDLER_SLOT = 2;
  
  static final int EXCEPTION_LOCAL_SLOT = 4;
  
  static final int EXCEPTION_SCOPE_SLOT = 5;
  
  static final int EXCEPTION_SLOT_SIZE = 6;
  
  static final int EXCEPTION_TRY_END_SLOT = 1;
  
  static final int EXCEPTION_TRY_START_SLOT = 0;
  
  static final int EXCEPTION_TYPE_SLOT = 3;
  
  InterpreterData itsData;
  
  private static void addInstructionCount(Context paramContext, CallFrame paramCallFrame, int paramInt) {
    paramContext.instructionCount += paramCallFrame.pc - paramCallFrame.pcPrevBranch + paramInt;
    if (paramContext.instructionCount > paramContext.instructionThreshold) {
      paramContext.observeInstructionCount(paramContext.instructionCount);
      paramContext.instructionCount = 0;
    } 
  }
  
  private static int bytecodeSpan(int paramInt) {
    if (paramInt != -54 && paramInt != -23)
      if (paramInt != -21) {
        if (paramInt != 50) {
          if (paramInt != 57) {
            if (paramInt != 73) {
              if (paramInt != 5 && paramInt != 6 && paramInt != 7) {
                switch (paramInt) {
                  default:
                    switch (paramInt) {
                      default:
                        switch (paramInt) {
                          default:
                            switch (paramInt) {
                              default:
                                switch (paramInt) {
                                  default:
                                    if (validBytecode(paramInt))
                                      return 1; 
                                    throw Kit.codeBug();
                                  case -11:
                                  case -10:
                                  case -9:
                                  case -8:
                                  case -7:
                                    return 2;
                                  case -6:
                                    break;
                                } 
                                return 3;
                              case -26:
                                return 3;
                              case -27:
                                return 3;
                              case -28:
                                break;
                            } 
                            return 5;
                          case -38:
                            return 2;
                          case -39:
                            return 3;
                          case -40:
                            break;
                        } 
                        return 5;
                      case -45:
                        return 2;
                      case -46:
                        return 3;
                      case -47:
                        return 5;
                      case -49:
                      case -48:
                        break;
                    } 
                  case -61:
                    return 2;
                  case -63:
                  case -62:
                    break;
                } 
                return 3;
              } 
            } else {
              return 3;
            } 
          } else {
            return 2;
          } 
        } else {
          return 3;
        } 
      } else {
        return 5;
      }  
    return 3;
  }
  
  public static NativeContinuation captureContinuation(Context paramContext) {
    if (paramContext.lastInterpreterFrame != null && paramContext.lastInterpreterFrame instanceof CallFrame)
      return captureContinuation(paramContext, (CallFrame)paramContext.lastInterpreterFrame, true); 
    throw new IllegalStateException("Interpreter frames not found");
  }
  
  private static NativeContinuation captureContinuation(Context paramContext, CallFrame paramCallFrame, boolean paramBoolean) {
    NativeContinuation nativeContinuation = new NativeContinuation();
    ScriptRuntime.setObjectProtoAndParent(nativeContinuation, ScriptRuntime.getTopCallScope(paramContext));
    CallFrame callFrame1 = paramCallFrame;
    CallFrame callFrame2 = paramCallFrame;
    while (callFrame1 != null && !callFrame1.frozen) {
      callFrame1.frozen = true;
      for (int i = callFrame1.savedStackTop + 1; i != callFrame1.stack.length; i++) {
        callFrame1.stack[i] = null;
        callFrame1.stackAttributes[i] = 0;
      } 
      if (callFrame1.savedCallOp == 38) {
        callFrame1.stack[callFrame1.savedStackTop] = null;
      } else if (callFrame1.savedCallOp != 30) {
        Kit.codeBug();
      } 
      callFrame2 = callFrame1;
      callFrame1 = callFrame1.parentFrame;
    } 
    if (paramBoolean) {
      while (callFrame2.parentFrame != null)
        callFrame2 = callFrame2.parentFrame; 
      if (!callFrame2.isContinuationsTopFrame)
        throw new IllegalStateException("Cannot capture continuation from JavaScript code not called directly by executeScriptWithContinuations or callFunctionWithContinuations"); 
    } 
    nativeContinuation.initImplementation(paramCallFrame);
    return nativeContinuation;
  }
  
  private static CallFrame captureFrameForGenerator(CallFrame paramCallFrame) {
    paramCallFrame.frozen = true;
    CallFrame callFrame = paramCallFrame.cloneFrozen();
    paramCallFrame.frozen = false;
    callFrame.parentFrame = null;
    callFrame.frameIndex = 0;
    return callFrame;
  }
  
  private static boolean compareIdata(InterpreterData paramInterpreterData1, InterpreterData paramInterpreterData2) {
    return (paramInterpreterData1 == paramInterpreterData2 || Objects.equals(getEncodedSource(paramInterpreterData1), getEncodedSource(paramInterpreterData2)));
  }
  
  private static void doAdd(Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt, Context paramContext) {
    double d;
    boolean bool;
    Object object1 = paramArrayOfObject[paramInt + 1];
    Object object2 = paramArrayOfObject[paramInt];
    if (object1 == UniqueTag.DOUBLE_MARK) {
      d = paramArrayOfdouble[paramInt + 1];
      if (object2 == UniqueTag.DOUBLE_MARK) {
        paramArrayOfdouble[paramInt] = paramArrayOfdouble[paramInt] + d;
        return;
      } 
      bool = true;
    } else if (object2 == UniqueTag.DOUBLE_MARK) {
      d = paramArrayOfdouble[paramInt];
      object2 = object1;
      bool = false;
    } else {
      double d1;
      if (object2 instanceof Scriptable || object1 instanceof Scriptable) {
        paramArrayOfObject[paramInt] = ScriptRuntime.add(object2, object1, paramContext);
        return;
      } 
      if (object2 instanceof CharSequence || object1 instanceof CharSequence) {
        paramArrayOfObject[paramInt] = new ConsString(ScriptRuntime.toCharSequence(object2), ScriptRuntime.toCharSequence(object1));
        return;
      } 
      if (object2 instanceof Number) {
        d = ((Number)object2).doubleValue();
      } else {
        d = ScriptRuntime.toNumber(object2);
      } 
      if (object1 instanceof Number) {
        d1 = ((Number)object1).doubleValue();
      } else {
        d1 = ScriptRuntime.toNumber(object1);
      } 
      paramArrayOfObject[paramInt] = UniqueTag.DOUBLE_MARK;
      paramArrayOfdouble[paramInt] = d + d1;
      return;
    } 
    if (object2 instanceof Scriptable) {
      object1 = ScriptRuntime.wrapNumber(d);
      Object object4 = object1;
      Object object3 = object2;
      if (!bool) {
        object3 = object1;
        object4 = object2;
      } 
      paramArrayOfObject[paramInt] = ScriptRuntime.add(object3, object4, paramContext);
    } else {
      CharSequence charSequence;
      if (object2 instanceof CharSequence) {
        charSequence = (CharSequence)object2;
        CharSequence charSequence1 = ScriptRuntime.toCharSequence(Double.valueOf(d));
        if (bool) {
          paramArrayOfObject[paramInt] = new ConsString(charSequence, charSequence1);
        } else {
          paramArrayOfObject[paramInt] = new ConsString(charSequence1, charSequence);
        } 
      } else {
        double d1;
        if (object2 instanceof Number) {
          d1 = ((Number)object2).doubleValue();
        } else {
          d1 = ScriptRuntime.toNumber(object2);
        } 
        paramArrayOfObject[paramInt] = UniqueTag.DOUBLE_MARK;
        charSequence[paramInt] = d1 + d;
      } 
    } 
  }
  
  private static int doArithmetic(CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2) {
    double d1 = stack_double(paramCallFrame, paramInt2);
    double d2 = stack_double(paramCallFrame, --paramInt2);
    paramArrayOfObject[paramInt2] = UniqueTag.DOUBLE_MARK;
    switch (paramInt1) {
      default:
        paramArrayOfdouble[paramInt2] = d2;
        return paramInt2;
      case 25:
        d2 %= d1;
      case 24:
        d2 /= d1;
      case 23:
        d2 *= d1;
      case 22:
        break;
    } 
    d2 -= d1;
  }
  
  private static int doBitOp(CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2) {
    int i = stack_int32(paramCallFrame, paramInt2 - 1);
    int j = stack_int32(paramCallFrame, paramInt2);
    paramArrayOfObject[--paramInt2] = UniqueTag.DOUBLE_MARK;
    if (paramInt1 != 18) {
      if (paramInt1 != 19) {
        switch (paramInt1) {
          default:
            paramInt1 = i;
            paramArrayOfdouble[paramInt2] = paramInt1;
            return paramInt2;
          case 11:
            paramInt1 = i & j;
            paramArrayOfdouble[paramInt2] = paramInt1;
            return paramInt2;
          case 10:
            paramInt1 = i ^ j;
            paramArrayOfdouble[paramInt2] = paramInt1;
            return paramInt2;
          case 9:
            break;
        } 
        paramInt1 = i | j;
      } else {
        paramInt1 = i >> j;
      } 
    } else {
      paramInt1 = i << j;
    } 
    paramArrayOfdouble[paramInt2] = paramInt1;
    return paramInt2;
  }
  
  private static int doCallSpecial(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    int i = paramArrayOfbyte[paramCallFrame.pc] & 0xFF;
    int j = paramCallFrame.pc;
    boolean bool = true;
    if (paramArrayOfbyte[j + 1] == 0)
      bool = false; 
    j = getIndex(paramArrayOfbyte, paramCallFrame.pc + 2);
    if (bool) {
      paramInt1 -= paramInt2;
      Object object2 = paramArrayOfObject[paramInt1];
      Object object1 = object2;
      if (object2 == UniqueTag.DOUBLE_MARK)
        object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
      paramArrayOfObject[paramInt1] = ScriptRuntime.newSpecial(paramContext, object1, getArgsArray(paramArrayOfObject, paramArrayOfdouble, paramInt1 + 1, paramInt2), paramCallFrame.scope, i);
    } else {
      paramInt1 -= paramInt2 + 1;
      Scriptable scriptable = (Scriptable)paramArrayOfObject[paramInt1 + 1];
      paramArrayOfObject[paramInt1] = ScriptRuntime.callSpecial(paramContext, (Callable)paramArrayOfObject[paramInt1], scriptable, getArgsArray(paramArrayOfObject, paramArrayOfdouble, paramInt1 + 2, paramInt2), paramCallFrame.scope, paramCallFrame.thisObj, i, paramCallFrame.idata.itsSourceFile, j);
    } 
    paramCallFrame.pc += 4;
    return paramInt1;
  }
  
  private static int doCompare(CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2) {
    double d1;
    double d2;
    Object object1 = paramArrayOfObject[--paramInt2 + 1];
    Object object2 = paramArrayOfObject[paramInt2];
    if (object1 == UniqueTag.DOUBLE_MARK) {
      d1 = paramArrayOfdouble[paramInt2 + 1];
      d2 = stack_double(paramCallFrame, paramInt2);
    } else if (object2 == UniqueTag.DOUBLE_MARK) {
      d1 = ScriptRuntime.toNumber(object1);
      d2 = paramArrayOfdouble[paramInt2];
    } else {
      switch (paramInt1) {
        default:
          throw Kit.codeBug();
        case 17:
          bool = ScriptRuntime.cmp_LE(object1, object2);
          paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool);
          return paramInt2;
        case 16:
          bool = ScriptRuntime.cmp_LT(object1, object2);
          paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool);
          return paramInt2;
        case 15:
          bool = ScriptRuntime.cmp_LE(object2, object1);
          paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool);
          return paramInt2;
        case 14:
          break;
      } 
      boolean bool = ScriptRuntime.cmp_LT(object2, object1);
      paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool);
      return paramInt2;
    } 
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    switch (paramInt1) {
      default:
        throw Kit.codeBug();
      case 17:
        if (d2 >= d1)
          bool4 = true; 
        paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool4);
        return paramInt2;
      case 16:
        bool4 = bool1;
        if (d2 > d1)
          bool4 = true; 
        paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool4);
        return paramInt2;
      case 15:
        bool4 = bool2;
        if (d2 <= d1)
          bool4 = true; 
        paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool4);
        return paramInt2;
      case 14:
        break;
    } 
    bool4 = bool3;
    if (d2 < d1)
      bool4 = true; 
    paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool4);
    return paramInt2;
  }
  
  private static int doDelName(Context paramContext, CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2) {
    boolean bool;
    Object object1 = paramArrayOfObject[paramInt2];
    Object object2 = object1;
    if (object1 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt2]); 
    Object object3 = paramArrayOfObject[--paramInt2];
    object1 = object3;
    if (object3 == UniqueTag.DOUBLE_MARK)
      object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt2]); 
    Scriptable scriptable = paramCallFrame.scope;
    if (paramInt1 == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramArrayOfObject[paramInt2] = ScriptRuntime.delete(object1, object2, paramContext, scriptable, bool);
    return paramInt2;
  }
  
  private static int doElemIncDec(Context paramContext, CallFrame paramCallFrame, byte[] paramArrayOfbyte, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt) {
    Object object1 = paramArrayOfObject[paramInt];
    Object object2 = object1;
    if (object1 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt]); 
    Object object3 = paramArrayOfObject[--paramInt];
    object1 = object3;
    if (object3 == UniqueTag.DOUBLE_MARK)
      object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt]); 
    paramArrayOfObject[paramInt] = ScriptRuntime.elemIncrDecr(object1, object2, paramContext, paramCallFrame.scope, paramArrayOfbyte[paramCallFrame.pc]);
    paramCallFrame.pc++;
    return paramInt;
  }
  
  private static boolean doEquals(Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt) {
    Object object2 = paramArrayOfObject[paramInt + 1];
    Object object1 = paramArrayOfObject[paramInt];
    if (object2 == UniqueTag.DOUBLE_MARK) {
      if (object1 == UniqueTag.DOUBLE_MARK) {
        boolean bool;
        if (paramArrayOfdouble[paramInt] == paramArrayOfdouble[paramInt + 1]) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      } 
      return ScriptRuntime.eqNumber(paramArrayOfdouble[paramInt + 1], object1);
    } 
    return (object1 == UniqueTag.DOUBLE_MARK) ? ScriptRuntime.eqNumber(paramArrayOfdouble[paramInt], object2) : ScriptRuntime.eq(object1, object2);
  }
  
  private static int doGetElem(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt) {
    Object object1;
    Object object2 = paramArrayOfObject[--paramInt];
    Object object3 = object2;
    if (object2 == UniqueTag.DOUBLE_MARK)
      object3 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt]); 
    object2 = paramArrayOfObject[paramInt + 1];
    if (object2 != UniqueTag.DOUBLE_MARK) {
      object1 = ScriptRuntime.getObjectElem(object3, object2, paramContext, paramCallFrame.scope);
    } else {
      object1 = ScriptRuntime.getObjectIndex(object3, paramArrayOfdouble[paramInt + 1], (Context)object1, paramCallFrame.scope);
    } 
    paramArrayOfObject[paramInt] = object1;
    return paramInt;
  }
  
  private static int doGetVar(CallFrame paramCallFrame, Object[] paramArrayOfObject1, double[] paramArrayOfdouble1, int paramInt1, Object[] paramArrayOfObject2, double[] paramArrayOfdouble2, int paramInt2) {
    paramInt1++;
    if (!paramCallFrame.useActivation) {
      paramArrayOfObject1[paramInt1] = paramArrayOfObject2[paramInt2];
      paramArrayOfdouble1[paramInt1] = paramArrayOfdouble2[paramInt2];
    } else {
      String str = paramCallFrame.idata.argNames[paramInt2];
      paramArrayOfObject1[paramInt1] = paramCallFrame.scope.get(str, paramCallFrame.scope);
    } 
    return paramInt1;
  }
  
  private static int doInOrInstanceof(Context paramContext, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2) {
    boolean bool;
    Object object1 = paramArrayOfObject[paramInt2];
    Object object2 = object1;
    if (object1 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt2]); 
    Object object3 = paramArrayOfObject[--paramInt2];
    object1 = object3;
    if (object3 == UniqueTag.DOUBLE_MARK)
      object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt2]); 
    if (paramInt1 == 52) {
      bool = ScriptRuntime.in(object1, object2, paramContext);
    } else {
      bool = ScriptRuntime.instanceOf(object1, object2, paramContext);
    } 
    paramArrayOfObject[paramInt2] = ScriptRuntime.wrapBoolean(bool);
    return paramInt2;
  }
  
  private static int doRefMember(Context paramContext, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    Object object1 = paramArrayOfObject[paramInt1];
    Object object2 = object1;
    if (object1 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    Object object3 = paramArrayOfObject[--paramInt1];
    object1 = object3;
    if (object3 == UniqueTag.DOUBLE_MARK)
      object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    paramArrayOfObject[paramInt1] = ScriptRuntime.memberRef(object1, object2, paramContext, paramInt2);
    return paramInt1;
  }
  
  private static int doRefNsMember(Context paramContext, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    Object object1 = paramArrayOfObject[paramInt1];
    Object object2 = object1;
    if (object1 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    Object object3 = paramArrayOfObject[--paramInt1];
    object1 = object3;
    if (object3 == UniqueTag.DOUBLE_MARK)
      object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    Object object4 = paramArrayOfObject[--paramInt1];
    object3 = object4;
    if (object4 == UniqueTag.DOUBLE_MARK)
      object3 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    paramArrayOfObject[paramInt1] = ScriptRuntime.memberRef(object3, object1, object2, paramContext, paramInt2);
    return paramInt1;
  }
  
  private static int doRefNsName(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    Object object1 = paramArrayOfObject[paramInt1];
    Object object2 = object1;
    if (object1 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    Object object3 = paramArrayOfObject[--paramInt1];
    object1 = object3;
    if (object3 == UniqueTag.DOUBLE_MARK)
      object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
    paramArrayOfObject[paramInt1] = ScriptRuntime.nameRef(object1, object2, paramContext, paramCallFrame.scope, paramInt2);
    return paramInt1;
  }
  
  private static int doSetConstVar(CallFrame paramCallFrame, Object[] paramArrayOfObject1, double[] paramArrayOfdouble1, int paramInt1, Object[] paramArrayOfObject2, double[] paramArrayOfdouble2, int[] paramArrayOfint, int paramInt2) {
    if (!paramCallFrame.useActivation) {
      if ((paramArrayOfint[paramInt2] & 0x1) != 0) {
        if ((paramArrayOfint[paramInt2] & 0x8) != 0) {
          paramArrayOfObject2[paramInt2] = paramArrayOfObject1[paramInt1];
          paramArrayOfint[paramInt2] = paramArrayOfint[paramInt2] & 0xFFFFFFF7;
          paramArrayOfdouble2[paramInt2] = paramArrayOfdouble1[paramInt1];
        } 
      } else {
        throw Context.reportRuntimeError1("msg.var.redecl", paramCallFrame.idata.argNames[paramInt2]);
      } 
    } else {
      Object object2 = paramArrayOfObject1[paramInt1];
      Object object1 = object2;
      if (object2 == UniqueTag.DOUBLE_MARK)
        object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble1[paramInt1]); 
      String str = paramCallFrame.idata.argNames[paramInt2];
      if (paramCallFrame.scope instanceof ConstProperties) {
        ((ConstProperties)paramCallFrame.scope).putConst(str, paramCallFrame.scope, object1);
        return paramInt1;
      } 
      throw Kit.codeBug();
    } 
    return paramInt1;
  }
  
  private static int doSetElem(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt) {
    Object object1;
    paramInt -= 2;
    Object object2 = paramArrayOfObject[paramInt + 2];
    Object object3 = object2;
    if (object2 == UniqueTag.DOUBLE_MARK)
      object3 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt + 2]); 
    object2 = paramArrayOfObject[paramInt];
    if (object2 == UniqueTag.DOUBLE_MARK)
      object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt]); 
    Object object4 = paramArrayOfObject[paramInt + 1];
    if (object4 != UniqueTag.DOUBLE_MARK) {
      object1 = ScriptRuntime.setObjectElem(object2, object4, object3, paramContext, paramCallFrame.scope);
    } else {
      object1 = ScriptRuntime.setObjectIndex(object2, paramArrayOfdouble[paramInt + 1], object3, (Context)object1, paramCallFrame.scope);
    } 
    paramArrayOfObject[paramInt] = object1;
    return paramInt;
  }
  
  private static int doSetVar(CallFrame paramCallFrame, Object[] paramArrayOfObject1, double[] paramArrayOfdouble1, int paramInt1, Object[] paramArrayOfObject2, double[] paramArrayOfdouble2, int[] paramArrayOfint, int paramInt2) {
    if (!paramCallFrame.useActivation) {
      if ((paramArrayOfint[paramInt2] & 0x1) == 0) {
        paramArrayOfObject2[paramInt2] = paramArrayOfObject1[paramInt1];
        paramArrayOfdouble2[paramInt2] = paramArrayOfdouble1[paramInt1];
      } 
    } else {
      Object object2 = paramArrayOfObject1[paramInt1];
      Object object1 = object2;
      if (object2 == UniqueTag.DOUBLE_MARK)
        object1 = ScriptRuntime.wrapNumber(paramArrayOfdouble1[paramInt1]); 
      String str = paramCallFrame.idata.argNames[paramInt2];
      paramCallFrame.scope.put(str, paramCallFrame.scope, object1);
    } 
    return paramInt1;
  }
  
  private static boolean doShallowEquals(Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt) {
    double d1;
    double d2;
    Object object2 = paramArrayOfObject[paramInt + 1];
    Object object1 = paramArrayOfObject[paramInt];
    UniqueTag uniqueTag = UniqueTag.DOUBLE_MARK;
    boolean bool = false;
    if (object2 == uniqueTag) {
      d1 = paramArrayOfdouble[paramInt + 1];
      if (object1 == uniqueTag) {
        d2 = paramArrayOfdouble[paramInt];
      } else if (object1 instanceof Number) {
        d2 = ((Number)object1).doubleValue();
      } else {
        return false;
      } 
    } else if (object1 == uniqueTag) {
      d2 = paramArrayOfdouble[paramInt];
      if (object2 instanceof Number) {
        d1 = ((Number)object2).doubleValue();
      } else {
        return false;
      } 
    } else {
      return ScriptRuntime.shallowEq(object1, object2);
    } 
    if (d2 == d1)
      bool = true; 
    return bool;
  }
  
  private static int doVarIncDec(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject1, double[] paramArrayOfdouble1, int paramInt1, Object[] paramArrayOfObject2, double[] paramArrayOfdouble2, int[] paramArrayOfint, int paramInt2) {
    Object object;
    int i = paramInt1 + 1;
    paramInt1 = paramCallFrame.idata.itsICode[paramCallFrame.pc];
    if (!paramCallFrame.useActivation) {
      double d1;
      double d2;
      object = paramArrayOfObject2[paramInt2];
      if (object == UniqueTag.DOUBLE_MARK) {
        d1 = paramArrayOfdouble2[paramInt2];
      } else {
        d1 = ScriptRuntime.toNumber(object);
      } 
      if ((paramInt1 & 0x1) == 0) {
        d2 = 1.0D + d1;
      } else {
        d2 = d1 - 1.0D;
      } 
      if ((paramInt1 & 0x2) != 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      } 
      if ((paramArrayOfint[paramInt2] & 0x1) == 0) {
        if (object != UniqueTag.DOUBLE_MARK)
          paramArrayOfObject2[paramInt2] = UniqueTag.DOUBLE_MARK; 
        paramArrayOfdouble2[paramInt2] = d2;
        paramArrayOfObject1[i] = UniqueTag.DOUBLE_MARK;
        if (paramInt1 == 0)
          d1 = d2; 
        paramArrayOfdouble1[i] = d1;
      } else if (paramInt1 != 0 && object != UniqueTag.DOUBLE_MARK) {
        paramArrayOfObject1[i] = object;
      } else {
        paramArrayOfObject1[i] = UniqueTag.DOUBLE_MARK;
        if (paramInt1 == 0)
          d1 = d2; 
        paramArrayOfdouble1[i] = d1;
      } 
    } else {
      String str = paramCallFrame.idata.argNames[paramInt2];
      paramArrayOfObject1[i] = ScriptRuntime.nameIncrDecr(paramCallFrame.scope, str, (Context)object, paramInt1);
    } 
    paramCallFrame.pc++;
    return i;
  }
  
  static void dumpICode(InterpreterData paramInterpreterData) {}
  
  private static void enterFrame(Context paramContext, CallFrame paramCallFrame, Object[] paramArrayOfObject, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   4: getfield itsNeedsActivation : Z
    //   7: istore #4
    //   9: aload_1
    //   10: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   13: ifnull -> 22
    //   16: iconst_1
    //   17: istore #5
    //   19: goto -> 25
    //   22: iconst_0
    //   23: istore #5
    //   25: iload #4
    //   27: ifne -> 35
    //   30: iload #5
    //   32: ifeq -> 155
    //   35: aload_1
    //   36: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   39: astore #6
    //   41: aload #6
    //   43: ifnonnull -> 57
    //   46: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   49: pop
    //   50: aload #6
    //   52: astore #7
    //   54: goto -> 122
    //   57: aload #6
    //   59: astore #7
    //   61: iload_3
    //   62: ifeq -> 122
    //   65: aload #6
    //   67: astore #7
    //   69: aload #6
    //   71: instanceof com/trendmicro/hippo/NativeWith
    //   74: ifeq -> 122
    //   77: aload #6
    //   79: invokeinterface getParentScope : ()Lcom/trendmicro/hippo/Scriptable;
    //   84: astore #7
    //   86: aload #7
    //   88: ifnull -> 118
    //   91: aload #7
    //   93: astore #6
    //   95: aload_1
    //   96: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   99: ifnull -> 65
    //   102: aload #7
    //   104: astore #6
    //   106: aload_1
    //   107: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   110: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   113: aload #7
    //   115: if_acmpne -> 65
    //   118: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   121: pop
    //   122: iload #5
    //   124: ifeq -> 144
    //   127: aload_1
    //   128: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   131: aload_0
    //   132: aload #7
    //   134: aload_1
    //   135: getfield thisObj : Lcom/trendmicro/hippo/Scriptable;
    //   138: aload_2
    //   139: invokeinterface onEnter : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)V
    //   144: iload #4
    //   146: ifeq -> 155
    //   149: aload_0
    //   150: aload #7
    //   152: invokestatic enterActivationFunction : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)V
    //   155: return
  }
  
  private static void exitFrame(Context paramContext, CallFrame paramCallFrame, Object paramObject) {
    if (paramCallFrame.idata.itsNeedsActivation)
      ScriptRuntime.exitActivationFunction(paramContext); 
    if (paramCallFrame.debuggerFrame != null)
      try {
      
      } finally {
        paramContext = null;
        System.err.println("HIPPO USAGE WARNING: onExit terminated with exception");
      }  
  }
  
  private static Object freezeGenerator(Context paramContext, CallFrame paramCallFrame, int paramInt, GeneratorState paramGeneratorState) {
    if (paramGeneratorState.operation != 2) {
      Object object;
      paramCallFrame.frozen = true;
      paramCallFrame.result = paramCallFrame.stack[paramInt];
      paramCallFrame.resultDbl = paramCallFrame.sDbl[paramInt];
      paramCallFrame.savedStackTop = paramInt;
      paramCallFrame.pc--;
      ScriptRuntime.exitActivationFunction(paramContext);
      if (paramCallFrame.result != UniqueTag.DOUBLE_MARK) {
        object = paramCallFrame.result;
      } else {
        object = ScriptRuntime.wrapNumber(paramCallFrame.resultDbl);
      } 
      return object;
    } 
    throw ScriptRuntime.typeError0("msg.yield.closing");
  }
  
  private static Object[] getArgsArray(Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    if (paramInt2 == 0)
      return ScriptRuntime.emptyArgs; 
    Object[] arrayOfObject = new Object[paramInt2];
    int i = 0;
    while (i != paramInt2) {
      Object object1 = paramArrayOfObject[paramInt1];
      Object object2 = object1;
      if (object1 == UniqueTag.DOUBLE_MARK)
        object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt1]); 
      arrayOfObject[i] = object2;
      i++;
      paramInt1++;
    } 
    return arrayOfObject;
  }
  
  static String getEncodedSource(InterpreterData paramInterpreterData) {
    return (paramInterpreterData.encodedSource == null) ? null : paramInterpreterData.encodedSource.substring(paramInterpreterData.encodedSourceStart, paramInterpreterData.encodedSourceEnd);
  }
  
  private static int getExceptionHandler(CallFrame paramCallFrame, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   4: getfield itsExceptionTable : [I
    //   7: astore_2
    //   8: aload_2
    //   9: ifnonnull -> 14
    //   12: iconst_m1
    //   13: ireturn
    //   14: aload_0
    //   15: getfield pc : I
    //   18: iconst_1
    //   19: isub
    //   20: istore_3
    //   21: iconst_m1
    //   22: istore #4
    //   24: iconst_0
    //   25: istore #5
    //   27: iconst_0
    //   28: istore #6
    //   30: iconst_0
    //   31: istore #7
    //   33: iload #7
    //   35: aload_2
    //   36: arraylength
    //   37: if_icmpeq -> 203
    //   40: aload_2
    //   41: iload #7
    //   43: iconst_0
    //   44: iadd
    //   45: iaload
    //   46: istore #8
    //   48: aload_2
    //   49: iload #7
    //   51: iconst_1
    //   52: iadd
    //   53: iaload
    //   54: istore #9
    //   56: iload #4
    //   58: istore #10
    //   60: iload #5
    //   62: istore #11
    //   64: iload #6
    //   66: istore #12
    //   68: iload #8
    //   70: iload_3
    //   71: if_icmpgt -> 185
    //   74: iload_3
    //   75: iload #9
    //   77: if_icmplt -> 95
    //   80: iload #4
    //   82: istore #10
    //   84: iload #5
    //   86: istore #11
    //   88: iload #6
    //   90: istore #12
    //   92: goto -> 185
    //   95: iload_1
    //   96: ifeq -> 124
    //   99: aload_2
    //   100: iload #7
    //   102: iconst_3
    //   103: iadd
    //   104: iaload
    //   105: iconst_1
    //   106: if_icmpeq -> 124
    //   109: iload #4
    //   111: istore #10
    //   113: iload #5
    //   115: istore #11
    //   117: iload #6
    //   119: istore #12
    //   121: goto -> 185
    //   124: iload #4
    //   126: iflt -> 173
    //   129: iload #6
    //   131: iload #9
    //   133: if_icmpge -> 151
    //   136: iload #4
    //   138: istore #10
    //   140: iload #5
    //   142: istore #11
    //   144: iload #6
    //   146: istore #12
    //   148: goto -> 185
    //   151: iload #5
    //   153: iload #8
    //   155: if_icmple -> 162
    //   158: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   161: pop
    //   162: iload #6
    //   164: iload #9
    //   166: if_icmpne -> 173
    //   169: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   172: pop
    //   173: iload #7
    //   175: istore #10
    //   177: iload #8
    //   179: istore #11
    //   181: iload #9
    //   183: istore #12
    //   185: iinc #7, 6
    //   188: iload #10
    //   190: istore #4
    //   192: iload #11
    //   194: istore #5
    //   196: iload #12
    //   198: istore #6
    //   200: goto -> 33
    //   203: iload #4
    //   205: ireturn
  }
  
  private static int getIndex(byte[] paramArrayOfbyte, int paramInt) {
    return (paramArrayOfbyte[paramInt] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 1] & 0xFF;
  }
  
  private static int getInt(byte[] paramArrayOfbyte, int paramInt) {
    return paramArrayOfbyte[paramInt] << 24 | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 16 | (paramArrayOfbyte[paramInt + 2] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 3] & 0xFF;
  }
  
  static int[] getLineNumbers(InterpreterData paramInterpreterData) {
    UintMap uintMap = new UintMap();
    byte[] arrayOfByte = paramInterpreterData.itsICode;
    int i = arrayOfByte.length;
    for (int j = 0; j != i; j += k) {
      byte b = arrayOfByte[j];
      int k = bytecodeSpan(b);
      if (b == -26) {
        if (k != 3)
          Kit.codeBug(); 
        uintMap.put(getIndex(arrayOfByte, j + 1), 0);
      } 
    } 
    return uintMap.getKeys();
  }
  
  private static int getShort(byte[] paramArrayOfbyte, int paramInt) {
    return paramArrayOfbyte[paramInt] << 8 | paramArrayOfbyte[paramInt + 1] & 0xFF;
  }
  
  private static CallFrame initFrame(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt1, int paramInt2, InterpretedFunction paramInterpretedFunction, CallFrame paramCallFrame) {
    CallFrame callFrame = new CallFrame(paramContext, paramScriptable2, paramInterpretedFunction, paramCallFrame);
    callFrame.initializeArgs(paramContext, paramScriptable1, paramArrayOfObject, paramArrayOfdouble, paramInt1, paramInt2);
    enterFrame(paramContext, callFrame, paramArrayOfObject, false);
    return callFrame;
  }
  
  private static CallFrame initFrameForApplyOrCall(Context paramContext, CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2, int paramInt3, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject, InterpretedFunction paramInterpretedFunction) {
    CallFrame callFrame;
    Scriptable scriptable;
    if (paramInt1 != 0) {
      Object object = paramArrayOfObject[paramInt2 + 2];
      scriptable = (Scriptable)object;
      if (object == UniqueTag.DOUBLE_MARK)
        scriptable = (Scriptable)ScriptRuntime.wrapNumber(paramArrayOfdouble[paramInt2 + 2]); 
      scriptable = ScriptRuntime.toObjectOrNull(paramContext, scriptable, paramCallFrame.scope);
    } else {
      scriptable = null;
    } 
    if (scriptable == null)
      scriptable = ScriptRuntime.getTopCallScope(paramContext); 
    if (paramInt3 == -55) {
      exitFrame(paramContext, paramCallFrame, (Object)null);
      paramCallFrame = paramCallFrame.parentFrame;
    } else {
      paramCallFrame.savedStackTop = paramInt2;
      paramCallFrame.savedCallOp = paramInt3;
    } 
    if (BaseFunction.isApply(paramIdFunctionObject)) {
      if (paramInt1 < 2) {
        paramArrayOfObject = ScriptRuntime.emptyArgs;
      } else {
        paramArrayOfObject = ScriptRuntime.getApplyArguments(paramContext, paramArrayOfObject[paramInt2 + 3]);
      } 
      callFrame = initFrame(paramContext, paramScriptable, scriptable, paramArrayOfObject, (double[])null, 0, paramArrayOfObject.length, paramInterpretedFunction, paramCallFrame);
    } else {
      for (paramInt3 = 1; paramInt3 < paramInt1; paramInt3++) {
        paramArrayOfObject[paramInt2 + 1 + paramInt3] = paramArrayOfObject[paramInt2 + 2 + paramInt3];
        paramArrayOfdouble[paramInt2 + 1 + paramInt3] = paramArrayOfdouble[paramInt2 + 2 + paramInt3];
      } 
      if (paramInt1 < 2) {
        paramInt1 = 0;
      } else {
        paramInt1--;
      } 
      callFrame = initFrame((Context)callFrame, paramScriptable, scriptable, paramArrayOfObject, paramArrayOfdouble, paramInt2 + 2, paramInt1, paramInterpretedFunction, paramCallFrame);
    } 
    return callFrame;
  }
  
  private static CallFrame initFrameForNoSuchMethod(Context paramContext, CallFrame paramCallFrame, int paramInt1, Object[] paramArrayOfObject, double[] paramArrayOfdouble, int paramInt2, int paramInt3, Scriptable paramScriptable1, Scriptable paramScriptable2, ScriptRuntime.NoSuchMethodShim paramNoSuchMethodShim, InterpretedFunction paramInterpretedFunction) {
    CallFrame callFrame2;
    Object[] arrayOfObject = new Object[paramInt1];
    byte b = 0;
    for (int i = paramInt2 + 2; b < paramInt1; i++) {
      Object object1 = paramArrayOfObject[i];
      Object object2 = object1;
      if (object1 == UniqueTag.DOUBLE_MARK)
        object2 = ScriptRuntime.wrapNumber(paramArrayOfdouble[i]); 
      arrayOfObject[b] = object2;
      b++;
    } 
    String str = paramNoSuchMethodShim.methodName;
    Scriptable scriptable = paramContext.newArray(paramScriptable2, arrayOfObject);
    if (paramInt3 == -55) {
      callFrame2 = paramCallFrame.parentFrame;
      exitFrame(paramContext, paramCallFrame, (Object)null);
    } else {
      callFrame2 = paramCallFrame;
    } 
    CallFrame callFrame1 = initFrame(paramContext, paramScriptable2, paramScriptable1, new Object[] { str, scriptable }, (double[])null, 0, 2, paramInterpretedFunction, callFrame2);
    if (paramInt3 != -55) {
      paramCallFrame.savedStackTop = paramInt2;
      paramCallFrame.savedCallOp = paramInt3;
    } 
    return callFrame1;
  }
  
  private static void initFunction(Context paramContext, Scriptable paramScriptable, InterpretedFunction paramInterpretedFunction, int paramInt) {
    InterpretedFunction interpretedFunction = InterpretedFunction.createFunction(paramContext, paramScriptable, paramInterpretedFunction, paramInt);
    ScriptRuntime.initFunction(paramContext, paramScriptable, interpretedFunction, interpretedFunction.idata.itsFunctionType, paramInterpretedFunction.idata.evalScriptFlag);
  }
  
  static Object interpret(InterpretedFunction paramInterpretedFunction, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!ScriptRuntime.hasTopCall(paramContext))
      Kit.codeBug(); 
    if (paramContext.interpreterSecurityDomain != paramInterpretedFunction.securityDomain) {
      Object object = paramContext.interpreterSecurityDomain;
      paramContext.interpreterSecurityDomain = paramInterpretedFunction.securityDomain;
      try {
        return paramInterpretedFunction.securityController.callWithDomain(paramInterpretedFunction.securityDomain, paramContext, paramInterpretedFunction, paramScriptable1, paramScriptable2, paramArrayOfObject);
      } finally {
        paramContext.interpreterSecurityDomain = object;
      } 
    } 
    CallFrame callFrame = initFrame(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, (double[])null, 0, paramArrayOfObject.length, paramInterpretedFunction, (CallFrame)null);
    callFrame.isContinuationsTopFrame = paramContext.isContinuationsTopCall;
    paramContext.isContinuationsTopCall = false;
    return interpretLoop(paramContext, callFrame, (Object)null);
  }
  
  private static Object interpretLoop(Context paramContext, CallFrame paramCallFrame, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: getstatic com/trendmicro/hippo/UniqueTag.DOUBLE_MARK : Lcom/trendmicro/hippo/UniqueTag;
    //   5: astore #4
    //   7: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   10: astore #5
    //   12: aload_3
    //   13: getfield instructionThreshold : I
    //   16: ifeq -> 25
    //   19: iconst_1
    //   20: istore #6
    //   22: goto -> 28
    //   25: iconst_0
    //   26: istore #6
    //   28: aconst_null
    //   29: astore #7
    //   31: iconst_m1
    //   32: istore #8
    //   34: aload_3
    //   35: getfield lastInterpreterFrame : Ljava/lang/Object;
    //   38: ifnull -> 70
    //   41: aload_3
    //   42: getfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   45: ifnonnull -> 59
    //   48: aload_3
    //   49: new com/trendmicro/hippo/ObjArray
    //   52: dup
    //   53: invokespecial <init> : ()V
    //   56: putfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   59: aload_3
    //   60: getfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   63: aload_3
    //   64: getfield lastInterpreterFrame : Ljava/lang/Object;
    //   67: invokevirtual push : (Ljava/lang/Object;)V
    //   70: aload_2
    //   71: ifnull -> 116
    //   74: aload_2
    //   75: instanceof com/trendmicro/hippo/Interpreter$GeneratorState
    //   78: ifeq -> 102
    //   81: aload_2
    //   82: checkcast com/trendmicro/hippo/Interpreter$GeneratorState
    //   85: astore #9
    //   87: aload_3
    //   88: aload_1
    //   89: getstatic com/trendmicro/hippo/ScriptRuntime.emptyArgs : [Ljava/lang/Object;
    //   92: iconst_1
    //   93: invokestatic enterFrame : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;Z)V
    //   96: aconst_null
    //   97: astore #10
    //   99: goto -> 122
    //   102: aload_2
    //   103: instanceof com/trendmicro/hippo/Interpreter$ContinuationJump
    //   106: ifne -> 116
    //   109: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   112: pop
    //   113: goto -> 116
    //   116: aconst_null
    //   117: astore #9
    //   119: aload_2
    //   120: astore #10
    //   122: aconst_null
    //   123: astore #11
    //   125: dconst_0
    //   126: dstore #12
    //   128: aload_3
    //   129: astore_2
    //   130: aload #5
    //   132: astore_3
    //   133: aload_1
    //   134: astore #5
    //   136: aload #10
    //   138: astore_1
    //   139: aload_1
    //   140: ifnull -> 225
    //   143: aload_1
    //   144: astore #14
    //   146: aload #5
    //   148: astore #10
    //   150: aload_2
    //   151: aload_1
    //   152: aload #5
    //   154: iload #8
    //   156: iload #6
    //   158: invokestatic processThrowable : (Lcom/trendmicro/hippo/Context;Ljava/lang/Object;Lcom/trendmicro/hippo/Interpreter$CallFrame;IZ)Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   161: astore #5
    //   163: aload_1
    //   164: astore #14
    //   166: aload #5
    //   168: astore #10
    //   170: aload #5
    //   172: getfield throwable : Ljava/lang/Object;
    //   175: astore_1
    //   176: aload_1
    //   177: astore #14
    //   179: aload #5
    //   181: astore #10
    //   183: aload #5
    //   185: aconst_null
    //   186: putfield throwable : Ljava/lang/Object;
    //   189: aload_1
    //   190: astore #15
    //   192: aload #5
    //   194: astore_1
    //   195: goto -> 262
    //   198: astore_1
    //   199: aload_3
    //   200: astore #16
    //   202: iconst_0
    //   203: istore #8
    //   205: aload #4
    //   207: astore #5
    //   209: aload_2
    //   210: astore_3
    //   211: aload #7
    //   213: astore_2
    //   214: aload #10
    //   216: astore #4
    //   218: aload #16
    //   220: astore #10
    //   222: goto -> 10984
    //   225: aload #9
    //   227: ifnonnull -> 256
    //   230: aload_1
    //   231: astore #14
    //   233: aload #5
    //   235: astore #10
    //   237: aload #5
    //   239: getfield frozen : Z
    //   242: ifeq -> 256
    //   245: aload_1
    //   246: astore #14
    //   248: aload #5
    //   250: astore #10
    //   252: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   255: pop
    //   256: aload_1
    //   257: astore #15
    //   259: aload #5
    //   261: astore_1
    //   262: aload_1
    //   263: getfield stack : [Ljava/lang/Object;
    //   266: astore #10
    //   268: aload_1
    //   269: getfield sDbl : [D
    //   272: astore #17
    //   274: aload_1
    //   275: getfield varSource : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   278: getfield stack : [Ljava/lang/Object;
    //   281: astore #16
    //   283: aload_1
    //   284: getfield varSource : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   287: getfield sDbl : [D
    //   290: astore #18
    //   292: aload_1
    //   293: getfield varSource : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   296: getfield stackAttributes : [I
    //   299: astore #19
    //   301: aload_1
    //   302: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   305: getfield itsICode : [B
    //   308: astore #14
    //   310: aload_1
    //   311: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   314: getfield itsStringTable : [Ljava/lang/String;
    //   317: astore #20
    //   319: aload_1
    //   320: getfield savedStackTop : I
    //   323: istore #21
    //   325: aload_2
    //   326: aload_1
    //   327: putfield lastInterpreterFrame : Ljava/lang/Object;
    //   330: aload #7
    //   332: astore #5
    //   334: aload #17
    //   336: astore #7
    //   338: aload_1
    //   339: getfield pc : I
    //   342: istore #22
    //   344: aload_1
    //   345: iload #22
    //   347: iconst_1
    //   348: iadd
    //   349: putfield pc : I
    //   352: aload #14
    //   354: iload #22
    //   356: baload
    //   357: istore #23
    //   359: iload #23
    //   361: sipush #157
    //   364: if_icmpeq -> 10840
    //   367: iload #23
    //   369: tableswitch default -> 400, -64 -> 10738, -63 -> 10394, -62 -> 10156, -61 -> 10093
    //   400: iload #23
    //   402: tableswitch default -> 656, -59 -> 9901, -58 -> 9738, -57 -> 9607, -56 -> 9525, -55 -> 8032, -54 -> 7793, -53 -> 7724, -52 -> 7705, -51 -> 7686, -50 -> 7674, -49 -> 7607, -48 -> 7508, -47 -> 7399, -46 -> 7307, -45 -> 7251, -44 -> 7242, -43 -> 7233, -42 -> 7224, -41 -> 7215, -40 -> 7172, -39 -> 7129, -38 -> 7080, -37 -> 7074, -36 -> 7068, -35 -> 7062, -34 -> 7056, -33 -> 7050, -32 -> 7044, -31 -> 6905, -30 -> 6826, -29 -> 6786, -28 -> 6725, -27 -> 6664, -26 -> 6581, -25 -> 6417, -24 -> 6244, -23 -> 6179, -22 -> 6163, -21 -> 6101, -20 -> 6031, -19 -> 5880, -18 -> 5777, -17 -> 5660, -16 -> 5571, -15 -> 5516, -14 -> 5481, -13 -> 5414, -12 -> 5297, -11 -> 5197, -10 -> 5172, -9 -> 5044, -8 -> 4906, -7 -> 4846, -6 -> 4723, -5 -> 4677, -4 -> 4663, -3 -> 4594, -2 -> 4534, -1 -> 4502, 0 -> 4472
    //   656: iload #23
    //   658: tableswitch default -> 992, 2 -> 4365, 3 -> 4339, 4 -> 4313, 5 -> 4310, 6 -> 4231, 7 -> 4152, 8 -> 3959, 9 -> 3893, 10 -> 3893, 11 -> 3893, 12 -> 3826, 13 -> 3826, 14 -> 3795, 15 -> 3795, 16 -> 3795, 17 -> 3795, 18 -> 3893, 19 -> 3893, 20 -> 3718, 21 -> 3695, 22 -> 3664, 23 -> 3664, 24 -> 3664, 25 -> 3664, 26 -> 3604, 27 -> 3560, 28 -> 3501, 29 -> 3501, 30 -> 2897, 31 -> 2894, 32 -> 2812, 33 -> 2753, 34 -> 2694, 35 -> 2606, 36 -> 2583, 37 -> 2560, 38 -> 2557, 39 -> 2529, 40 -> 2494, 41 -> 2481, 42 -> 2469, 43 -> 2447, 44 -> 2433, 45 -> 2419, 46 -> 2366, 47 -> 2366, 48 -> 2325, 49 -> 2297, 50 -> 2203, 51 -> 2119, 52 -> 2102, 53 -> 2102, 54 -> 2056, 55 -> 2053, 56 -> 2050, 57 -> 1837, 58 -> 1718, 59 -> 1718, 60 -> 1718, 61 -> 1718, 62 -> 1648, 63 -> 1648, 64 -> 1626, 65 -> 1623, 66 -> 1620, 67 -> 1620, 68 -> 1593, 69 -> 1529, 70 -> 1502, 71 -> 2557, 72 -> 1443, 73 -> 1440, 74 -> 3959, 75 -> 1391, 76 -> 1344, 77 -> 1297, 78 -> 1280, 79 -> 1263, 80 -> 1159, 81 -> 1103
    //   992: aload_1
    //   993: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   996: invokestatic dumpICode : (Lcom/trendmicro/hippo/InterpreterData;)V
    //   999: new java/lang/RuntimeException
    //   1002: astore #10
    //   1004: new java/lang/StringBuilder
    //   1007: astore #7
    //   1009: aload #7
    //   1011: invokespecial <init> : ()V
    //   1014: aload #7
    //   1016: ldc_w 'Unknown icode : '
    //   1019: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1022: pop
    //   1023: aload #7
    //   1025: iload #23
    //   1027: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1030: pop
    //   1031: aload #7
    //   1033: ldc_w ' @ pc : '
    //   1036: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1039: pop
    //   1040: aload #7
    //   1042: aload_1
    //   1043: getfield pc : I
    //   1046: iconst_1
    //   1047: isub
    //   1048: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1051: pop
    //   1052: aload #10
    //   1054: aload #7
    //   1056: invokevirtual toString : ()Ljava/lang/String;
    //   1059: invokespecial <init> : (Ljava/lang/String;)V
    //   1062: aload #10
    //   1064: athrow
    //   1065: astore #10
    //   1067: aload_3
    //   1068: astore #7
    //   1070: iconst_0
    //   1071: istore #8
    //   1073: aload_1
    //   1074: astore #14
    //   1076: aload_2
    //   1077: astore_3
    //   1078: aload #5
    //   1080: astore_2
    //   1081: aload #10
    //   1083: astore_1
    //   1084: aload #4
    //   1086: astore #5
    //   1088: aload #14
    //   1090: astore #4
    //   1092: aload #7
    //   1094: astore #10
    //   1096: aload #15
    //   1098: astore #14
    //   1100: goto -> 10984
    //   1103: aload_0
    //   1104: aload_1
    //   1105: aload #10
    //   1107: aload #7
    //   1109: iload #21
    //   1111: iload #8
    //   1113: invokestatic doRefNsName : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DII)I
    //   1116: istore #21
    //   1118: goto -> 338
    //   1121: astore #10
    //   1123: aload #5
    //   1125: astore #14
    //   1127: aload #4
    //   1129: astore #5
    //   1131: aload_3
    //   1132: astore #7
    //   1134: iconst_0
    //   1135: istore #8
    //   1137: aload_1
    //   1138: astore #4
    //   1140: aload_2
    //   1141: astore_3
    //   1142: aload #14
    //   1144: astore_2
    //   1145: aload #10
    //   1147: astore_1
    //   1148: aload #7
    //   1150: astore #10
    //   1152: aload #15
    //   1154: astore #14
    //   1156: goto -> 10984
    //   1159: aload #9
    //   1161: astore #17
    //   1163: aload_3
    //   1164: astore #24
    //   1166: aload_1
    //   1167: astore #25
    //   1169: aload #10
    //   1171: iload #21
    //   1173: aaload
    //   1174: astore #26
    //   1176: aload #26
    //   1178: astore #27
    //   1180: aload #26
    //   1182: aload #4
    //   1184: if_acmpne -> 1197
    //   1187: aload #7
    //   1189: iload #21
    //   1191: daload
    //   1192: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   1195: astore #27
    //   1197: aload #25
    //   1199: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   1202: astore #26
    //   1204: aload #10
    //   1206: iload #21
    //   1208: aload #27
    //   1210: aload_2
    //   1211: aload #26
    //   1213: iload #8
    //   1215: invokestatic nameRef : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Lcom/trendmicro/hippo/Ref;
    //   1218: aastore
    //   1219: aload_2
    //   1220: astore #9
    //   1222: goto -> 10792
    //   1225: astore #7
    //   1227: aload #5
    //   1229: astore_1
    //   1230: aload #4
    //   1232: astore #5
    //   1234: aload #24
    //   1236: astore #10
    //   1238: aload #17
    //   1240: astore #9
    //   1242: iconst_0
    //   1243: istore #8
    //   1245: aload #25
    //   1247: astore #4
    //   1249: aload_2
    //   1250: astore_3
    //   1251: aload_1
    //   1252: astore_2
    //   1253: aload #7
    //   1255: astore_1
    //   1256: aload #15
    //   1258: astore #14
    //   1260: goto -> 10984
    //   1263: aload_2
    //   1264: aload #10
    //   1266: aload #7
    //   1268: iload #21
    //   1270: iload #8
    //   1272: invokestatic doRefNsMember : (Lcom/trendmicro/hippo/Context;[Ljava/lang/Object;[DII)I
    //   1275: istore #21
    //   1277: goto -> 338
    //   1280: aload_2
    //   1281: aload #10
    //   1283: aload #7
    //   1285: iload #21
    //   1287: iload #8
    //   1289: invokestatic doRefMember : (Lcom/trendmicro/hippo/Context;[Ljava/lang/Object;[DII)I
    //   1292: istore #21
    //   1294: goto -> 338
    //   1297: aload #9
    //   1299: astore #17
    //   1301: aload_3
    //   1302: astore #25
    //   1304: aload_1
    //   1305: astore #25
    //   1307: aload #10
    //   1309: iload #21
    //   1311: aaload
    //   1312: astore #25
    //   1314: aload #25
    //   1316: aload #4
    //   1318: if_acmpeq -> 1338
    //   1321: aload #10
    //   1323: iload #21
    //   1325: aload #25
    //   1327: aload_2
    //   1328: invokestatic escapeTextValue : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/String;
    //   1331: aastore
    //   1332: aload_2
    //   1333: astore #9
    //   1335: goto -> 10792
    //   1338: aload_2
    //   1339: astore #9
    //   1341: goto -> 10792
    //   1344: aload #9
    //   1346: astore #17
    //   1348: aload_3
    //   1349: astore #25
    //   1351: aload_1
    //   1352: astore #25
    //   1354: aload #10
    //   1356: iload #21
    //   1358: aaload
    //   1359: astore #25
    //   1361: aload #25
    //   1363: aload #4
    //   1365: if_acmpeq -> 1385
    //   1368: aload #10
    //   1370: iload #21
    //   1372: aload #25
    //   1374: aload_2
    //   1375: invokestatic escapeAttributeValue : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/String;
    //   1378: aastore
    //   1379: aload_2
    //   1380: astore #9
    //   1382: goto -> 10792
    //   1385: aload_2
    //   1386: astore #9
    //   1388: goto -> 10792
    //   1391: aload #10
    //   1393: iload #21
    //   1395: aaload
    //   1396: astore #25
    //   1398: aload #25
    //   1400: astore #17
    //   1402: aload #25
    //   1404: aload #4
    //   1406: if_acmpne -> 1419
    //   1409: aload #7
    //   1411: iload #21
    //   1413: daload
    //   1414: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   1417: astore #17
    //   1419: aload #10
    //   1421: iload #21
    //   1423: aload #17
    //   1425: aload_2
    //   1426: invokestatic setDefaultNamespace : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;
    //   1429: aastore
    //   1430: aload #9
    //   1432: astore #17
    //   1434: aload_2
    //   1435: astore #9
    //   1437: goto -> 10792
    //   1440: goto -> 10228
    //   1443: aload_1
    //   1444: astore #25
    //   1446: aload #10
    //   1448: iload #21
    //   1450: aaload
    //   1451: astore #24
    //   1453: aload #24
    //   1455: astore #17
    //   1457: aload #24
    //   1459: aload #4
    //   1461: if_acmpne -> 1474
    //   1464: aload #7
    //   1466: iload #21
    //   1468: daload
    //   1469: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   1472: astore #17
    //   1474: aload #10
    //   1476: iload #21
    //   1478: aload #17
    //   1480: aload #5
    //   1482: aload_2
    //   1483: aload #25
    //   1485: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   1488: invokestatic specialRef : (Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Ref;
    //   1491: aastore
    //   1492: aload #9
    //   1494: astore #17
    //   1496: aload_2
    //   1497: astore #9
    //   1499: goto -> 10792
    //   1502: aload #10
    //   1504: iload #21
    //   1506: aload #10
    //   1508: iload #21
    //   1510: aaload
    //   1511: checkcast com/trendmicro/hippo/Ref
    //   1514: aload_2
    //   1515: invokestatic refDel : (Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;
    //   1518: aastore
    //   1519: aload #9
    //   1521: astore #17
    //   1523: aload_2
    //   1524: astore #9
    //   1526: goto -> 10792
    //   1529: aload_1
    //   1530: astore #17
    //   1532: aload #10
    //   1534: iload #21
    //   1536: aaload
    //   1537: astore #24
    //   1539: aload #24
    //   1541: astore #25
    //   1543: aload #24
    //   1545: aload #4
    //   1547: if_acmpne -> 1560
    //   1550: aload #7
    //   1552: iload #21
    //   1554: daload
    //   1555: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   1558: astore #25
    //   1560: iinc #21, -1
    //   1563: aload #10
    //   1565: iload #21
    //   1567: aload #10
    //   1569: iload #21
    //   1571: aaload
    //   1572: checkcast com/trendmicro/hippo/Ref
    //   1575: aload #25
    //   1577: aload_2
    //   1578: aload #17
    //   1580: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   1583: invokestatic refSet : (Lcom/trendmicro/hippo/Ref;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   1586: aastore
    //   1587: aload #17
    //   1589: astore_1
    //   1590: goto -> 338
    //   1593: aload #10
    //   1595: iload #21
    //   1597: aload #10
    //   1599: iload #21
    //   1601: aaload
    //   1602: checkcast com/trendmicro/hippo/Ref
    //   1605: aload_2
    //   1606: invokestatic refGet : (Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;
    //   1609: aastore
    //   1610: aload #9
    //   1612: astore #17
    //   1614: aload_2
    //   1615: astore #9
    //   1617: goto -> 10792
    //   1620: goto -> 6905
    //   1623: goto -> 10474
    //   1626: aload_1
    //   1627: astore #17
    //   1629: iinc #21, 1
    //   1632: aload #10
    //   1634: iload #21
    //   1636: aload #17
    //   1638: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   1641: aastore
    //   1642: aload #17
    //   1644: astore_1
    //   1645: goto -> 338
    //   1648: aload_1
    //   1649: astore #17
    //   1651: aload #17
    //   1653: getfield localShift : I
    //   1656: istore #22
    //   1658: iload #8
    //   1660: iload #22
    //   1662: iadd
    //   1663: istore #8
    //   1665: aload #10
    //   1667: iload #8
    //   1669: aaload
    //   1670: astore #25
    //   1672: iinc #21, 1
    //   1675: iload #23
    //   1677: bipush #62
    //   1679: if_icmpne -> 1695
    //   1682: aload #25
    //   1684: invokestatic enumNext : (Ljava/lang/Object;)Ljava/lang/Boolean;
    //   1687: astore #25
    //   1689: aload #25
    //   1691: astore_1
    //   1692: goto -> 1706
    //   1695: aload #25
    //   1697: aload_2
    //   1698: invokestatic enumId : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;
    //   1701: astore #25
    //   1703: aload #25
    //   1705: astore_1
    //   1706: aload #10
    //   1708: iload #21
    //   1710: aload_1
    //   1711: aastore
    //   1712: aload #17
    //   1714: astore_1
    //   1715: goto -> 338
    //   1718: aload_1
    //   1719: astore #17
    //   1721: aload #10
    //   1723: iload #21
    //   1725: aaload
    //   1726: astore #24
    //   1728: aload #24
    //   1730: astore #25
    //   1732: aload #24
    //   1734: aload #4
    //   1736: if_acmpne -> 1749
    //   1739: aload #7
    //   1741: iload #21
    //   1743: daload
    //   1744: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   1747: astore #25
    //   1749: iinc #21, -1
    //   1752: aload #17
    //   1754: getfield localShift : I
    //   1757: istore #22
    //   1759: iload #22
    //   1761: iload #8
    //   1763: iadd
    //   1764: istore #22
    //   1766: iload #23
    //   1768: bipush #58
    //   1770: if_icmpne -> 1779
    //   1773: iconst_0
    //   1774: istore #8
    //   1776: goto -> 1809
    //   1779: iload #23
    //   1781: bipush #59
    //   1783: if_icmpne -> 1792
    //   1786: iconst_1
    //   1787: istore #8
    //   1789: goto -> 1809
    //   1792: iload #23
    //   1794: bipush #61
    //   1796: if_icmpne -> 1806
    //   1799: bipush #6
    //   1801: istore #8
    //   1803: goto -> 1809
    //   1806: iconst_2
    //   1807: istore #8
    //   1809: aload #10
    //   1811: iload #22
    //   1813: aload #25
    //   1815: aload_2
    //   1816: aload #17
    //   1818: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   1821: iload #8
    //   1823: invokestatic enumInit : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;
    //   1826: aastore
    //   1827: aload #17
    //   1829: astore_1
    //   1830: iload #22
    //   1832: istore #8
    //   1834: goto -> 338
    //   1837: aload #9
    //   1839: astore #17
    //   1841: aload_3
    //   1842: astore #24
    //   1844: aload_1
    //   1845: astore #25
    //   1847: iinc #21, -1
    //   1850: aload #25
    //   1852: getfield localShift : I
    //   1855: istore #22
    //   1857: iload #8
    //   1859: iload #22
    //   1861: iadd
    //   1862: istore #22
    //   1864: aload #25
    //   1866: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   1869: getfield itsICode : [B
    //   1872: aload #25
    //   1874: getfield pc : I
    //   1877: baload
    //   1878: ifeq -> 1887
    //   1881: iconst_1
    //   1882: istore #8
    //   1884: goto -> 1890
    //   1887: iconst_0
    //   1888: istore #8
    //   1890: aload #10
    //   1892: iload #21
    //   1894: iconst_1
    //   1895: iadd
    //   1896: aaload
    //   1897: checkcast java/lang/Throwable
    //   1900: astore #26
    //   1902: iload #8
    //   1904: ifne -> 1913
    //   1907: aconst_null
    //   1908: astore #27
    //   1910: goto -> 1923
    //   1913: aload #10
    //   1915: iload #22
    //   1917: aaload
    //   1918: checkcast com/trendmicro/hippo/Scriptable
    //   1921: astore #27
    //   1923: aload #10
    //   1925: iload #22
    //   1927: aload #26
    //   1929: aload #27
    //   1931: aload #5
    //   1933: aload_2
    //   1934: aload #25
    //   1936: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   1939: invokestatic newCatchScope : (Ljava/lang/Throwable;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   1942: aastore
    //   1943: aload #25
    //   1945: getfield pc : I
    //   1948: istore #8
    //   1950: aload #25
    //   1952: iload #8
    //   1954: iconst_1
    //   1955: iadd
    //   1956: putfield pc : I
    //   1959: aload #25
    //   1961: astore_1
    //   1962: aload #24
    //   1964: astore_3
    //   1965: aload #17
    //   1967: astore #9
    //   1969: iload #22
    //   1971: istore #8
    //   1973: goto -> 338
    //   1976: astore_1
    //   1977: aload #5
    //   1979: astore #9
    //   1981: aload #4
    //   1983: astore #5
    //   1985: aload #24
    //   1987: astore #10
    //   1989: iconst_0
    //   1990: istore #8
    //   1992: aload #25
    //   1994: astore #4
    //   1996: aload_2
    //   1997: astore_3
    //   1998: aload #9
    //   2000: astore_2
    //   2001: aload #17
    //   2003: astore #9
    //   2005: aload #15
    //   2007: astore #14
    //   2009: goto -> 10984
    //   2012: astore #7
    //   2014: aload #5
    //   2016: astore_1
    //   2017: aload #4
    //   2019: astore #5
    //   2021: aload #24
    //   2023: astore #10
    //   2025: aload #17
    //   2027: astore #9
    //   2029: iconst_0
    //   2030: istore #8
    //   2032: aload #25
    //   2034: astore #4
    //   2036: aload_2
    //   2037: astore_3
    //   2038: aload_1
    //   2039: astore_2
    //   2040: aload #7
    //   2042: astore_1
    //   2043: aload #15
    //   2045: astore #14
    //   2047: goto -> 10984
    //   2050: goto -> 7644
    //   2053: goto -> 7545
    //   2056: aload_1
    //   2057: astore #17
    //   2059: iinc #21, 1
    //   2062: aload #17
    //   2064: getfield localShift : I
    //   2067: istore #22
    //   2069: iload #8
    //   2071: iload #22
    //   2073: iadd
    //   2074: istore #8
    //   2076: aload #10
    //   2078: iload #21
    //   2080: aload #10
    //   2082: iload #8
    //   2084: aaload
    //   2085: aastore
    //   2086: aload #7
    //   2088: iload #21
    //   2090: aload #7
    //   2092: iload #8
    //   2094: daload
    //   2095: dastore
    //   2096: aload #17
    //   2098: astore_1
    //   2099: goto -> 338
    //   2102: aload_2
    //   2103: iload #23
    //   2105: aload #10
    //   2107: aload #7
    //   2109: iload #21
    //   2111: invokestatic doInOrInstanceof : (Lcom/trendmicro/hippo/Context;I[Ljava/lang/Object;[DI)I
    //   2114: istore #21
    //   2116: goto -> 338
    //   2119: aload_1
    //   2120: astore #7
    //   2122: aload #7
    //   2124: getfield localShift : I
    //   2127: istore #21
    //   2129: aload #10
    //   2131: iload #8
    //   2133: iload #21
    //   2135: iadd
    //   2136: aaload
    //   2137: astore #10
    //   2139: aload #5
    //   2141: astore #25
    //   2143: aload #4
    //   2145: astore #5
    //   2147: aload_2
    //   2148: astore_1
    //   2149: aload #11
    //   2151: astore_2
    //   2152: iconst_0
    //   2153: istore #21
    //   2155: aload #10
    //   2157: astore #4
    //   2159: aload_3
    //   2160: astore #10
    //   2162: goto -> 11008
    //   2165: astore #10
    //   2167: aload #5
    //   2169: astore #14
    //   2171: aload #4
    //   2173: astore #5
    //   2175: aload_3
    //   2176: astore #7
    //   2178: iconst_0
    //   2179: istore #8
    //   2181: aload_1
    //   2182: astore #4
    //   2184: aload_2
    //   2185: astore_3
    //   2186: aload #14
    //   2188: astore_2
    //   2189: aload #10
    //   2191: astore_1
    //   2192: aload #7
    //   2194: astore #10
    //   2196: aload #15
    //   2198: astore #14
    //   2200: goto -> 10984
    //   2203: aload_1
    //   2204: astore #16
    //   2206: aload #10
    //   2208: iload #21
    //   2210: aaload
    //   2211: astore #17
    //   2213: aload #17
    //   2215: astore #10
    //   2217: aload #17
    //   2219: aload #4
    //   2221: if_acmpne -> 2234
    //   2224: aload #7
    //   2226: iload #21
    //   2228: daload
    //   2229: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   2232: astore #10
    //   2234: aload #14
    //   2236: aload #16
    //   2238: getfield pc : I
    //   2241: invokestatic getIndex : ([BI)I
    //   2244: istore #8
    //   2246: new com/trendmicro/hippo/JavaScriptException
    //   2249: dup
    //   2250: aload #10
    //   2252: aload #16
    //   2254: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   2257: getfield itsSourceFile : Ljava/lang/String;
    //   2260: iload #8
    //   2262: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/String;I)V
    //   2265: astore #10
    //   2267: aload #5
    //   2269: astore #25
    //   2271: aload #4
    //   2273: astore #5
    //   2275: aload_2
    //   2276: astore_1
    //   2277: aload #11
    //   2279: astore_2
    //   2280: iconst_0
    //   2281: istore #21
    //   2283: aload #16
    //   2285: astore #7
    //   2287: aload #10
    //   2289: astore #4
    //   2291: aload_3
    //   2292: astore #10
    //   2294: goto -> 11008
    //   2297: aload_1
    //   2298: astore #17
    //   2300: iinc #21, 1
    //   2303: aload #10
    //   2305: iload #21
    //   2307: aload_2
    //   2308: aload #17
    //   2310: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2313: aload #5
    //   2315: invokestatic bind : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Lcom/trendmicro/hippo/Scriptable;
    //   2318: aastore
    //   2319: aload #17
    //   2321: astore_1
    //   2322: goto -> 338
    //   2325: aload_1
    //   2326: astore #17
    //   2328: aload #17
    //   2330: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   2333: getfield itsRegExpLiterals : [Ljava/lang/Object;
    //   2336: iload #8
    //   2338: aaload
    //   2339: astore #25
    //   2341: iinc #21, 1
    //   2344: aload #10
    //   2346: iload #21
    //   2348: aload_2
    //   2349: aload #17
    //   2351: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2354: aload #25
    //   2356: invokestatic wrapRegExp : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;
    //   2359: aastore
    //   2360: aload #17
    //   2362: astore_1
    //   2363: goto -> 338
    //   2366: iload #21
    //   2368: iconst_1
    //   2369: isub
    //   2370: istore #22
    //   2372: aload #10
    //   2374: aload #7
    //   2376: iload #22
    //   2378: invokestatic doShallowEquals : ([Ljava/lang/Object;[DI)Z
    //   2381: istore #28
    //   2383: iload #23
    //   2385: bipush #47
    //   2387: if_icmpne -> 2396
    //   2390: iconst_1
    //   2391: istore #21
    //   2393: goto -> 2399
    //   2396: iconst_0
    //   2397: istore #21
    //   2399: aload #10
    //   2401: iload #22
    //   2403: iload #28
    //   2405: iload #21
    //   2407: ixor
    //   2408: invokestatic wrapBoolean : (Z)Ljava/lang/Boolean;
    //   2411: aastore
    //   2412: iload #22
    //   2414: istore #21
    //   2416: goto -> 338
    //   2419: iinc #21, 1
    //   2422: aload #10
    //   2424: iload #21
    //   2426: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   2429: aastore
    //   2430: goto -> 338
    //   2433: iinc #21, 1
    //   2436: aload #10
    //   2438: iload #21
    //   2440: getstatic java/lang/Boolean.FALSE : Ljava/lang/Boolean;
    //   2443: aastore
    //   2444: goto -> 338
    //   2447: aload_1
    //   2448: astore #17
    //   2450: iinc #21, 1
    //   2453: aload #10
    //   2455: iload #21
    //   2457: aload #17
    //   2459: getfield thisObj : Lcom/trendmicro/hippo/Scriptable;
    //   2462: aastore
    //   2463: aload #17
    //   2465: astore_1
    //   2466: goto -> 338
    //   2469: iinc #21, 1
    //   2472: aload #10
    //   2474: iload #21
    //   2476: aconst_null
    //   2477: aastore
    //   2478: goto -> 338
    //   2481: iinc #21, 1
    //   2484: aload #10
    //   2486: iload #21
    //   2488: aload #5
    //   2490: aastore
    //   2491: goto -> 338
    //   2494: aload_1
    //   2495: astore #17
    //   2497: iinc #21, 1
    //   2500: aload #10
    //   2502: iload #21
    //   2504: aload #4
    //   2506: aastore
    //   2507: aload #7
    //   2509: iload #21
    //   2511: aload #17
    //   2513: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   2516: getfield itsDoubleTable : [D
    //   2519: iload #8
    //   2521: daload
    //   2522: dastore
    //   2523: aload #17
    //   2525: astore_1
    //   2526: goto -> 338
    //   2529: aload_1
    //   2530: astore #17
    //   2532: iinc #21, 1
    //   2535: aload #10
    //   2537: iload #21
    //   2539: aload_2
    //   2540: aload #17
    //   2542: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2545: aload #5
    //   2547: invokestatic name : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
    //   2550: aastore
    //   2551: aload #17
    //   2553: astore_1
    //   2554: goto -> 338
    //   2557: goto -> 8032
    //   2560: aload_1
    //   2561: astore #17
    //   2563: aload_2
    //   2564: aload #17
    //   2566: aload #10
    //   2568: aload #7
    //   2570: iload #21
    //   2572: invokestatic doSetElem : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI)I
    //   2575: istore #21
    //   2577: aload #17
    //   2579: astore_1
    //   2580: goto -> 338
    //   2583: aload_1
    //   2584: astore #17
    //   2586: aload_2
    //   2587: aload #17
    //   2589: aload #10
    //   2591: aload #7
    //   2593: iload #21
    //   2595: invokestatic doGetElem : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI)I
    //   2598: istore #21
    //   2600: aload #17
    //   2602: astore_1
    //   2603: goto -> 338
    //   2606: aload_1
    //   2607: astore #17
    //   2609: aload #10
    //   2611: iload #21
    //   2613: aaload
    //   2614: astore #24
    //   2616: aload #24
    //   2618: astore #25
    //   2620: aload #24
    //   2622: aload #4
    //   2624: if_acmpne -> 2637
    //   2627: aload #7
    //   2629: iload #21
    //   2631: daload
    //   2632: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   2635: astore #25
    //   2637: iinc #21, -1
    //   2640: aload #10
    //   2642: iload #21
    //   2644: aaload
    //   2645: astore #27
    //   2647: aload #27
    //   2649: astore #24
    //   2651: aload #27
    //   2653: aload #4
    //   2655: if_acmpne -> 2668
    //   2658: aload #7
    //   2660: iload #21
    //   2662: daload
    //   2663: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   2666: astore #24
    //   2668: aload #10
    //   2670: iload #21
    //   2672: aload #24
    //   2674: aload #5
    //   2676: aload #25
    //   2678: aload_2
    //   2679: aload #17
    //   2681: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2684: invokestatic setObjectProp : (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   2687: aastore
    //   2688: aload #17
    //   2690: astore_1
    //   2691: goto -> 338
    //   2694: aload_1
    //   2695: astore #25
    //   2697: aload #10
    //   2699: iload #21
    //   2701: aaload
    //   2702: astore #24
    //   2704: aload #24
    //   2706: astore #17
    //   2708: aload #24
    //   2710: aload #4
    //   2712: if_acmpne -> 2725
    //   2715: aload #7
    //   2717: iload #21
    //   2719: daload
    //   2720: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   2723: astore #17
    //   2725: aload #10
    //   2727: iload #21
    //   2729: aload #17
    //   2731: aload #5
    //   2733: aload_2
    //   2734: aload #25
    //   2736: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2739: invokestatic getObjectPropNoWarn : (Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   2742: aastore
    //   2743: aload #9
    //   2745: astore #17
    //   2747: aload_2
    //   2748: astore #9
    //   2750: goto -> 10792
    //   2753: aload_1
    //   2754: astore #25
    //   2756: aload #10
    //   2758: iload #21
    //   2760: aaload
    //   2761: astore #24
    //   2763: aload #24
    //   2765: astore #17
    //   2767: aload #24
    //   2769: aload #4
    //   2771: if_acmpne -> 2784
    //   2774: aload #7
    //   2776: iload #21
    //   2778: daload
    //   2779: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   2782: astore #17
    //   2784: aload #10
    //   2786: iload #21
    //   2788: aload #17
    //   2790: aload #5
    //   2792: aload_2
    //   2793: aload #25
    //   2795: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2798: invokestatic getObjectProp : (Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   2801: aastore
    //   2802: aload #9
    //   2804: astore #17
    //   2806: aload_2
    //   2807: astore #9
    //   2809: goto -> 10792
    //   2812: aload #10
    //   2814: iload #21
    //   2816: aaload
    //   2817: astore #25
    //   2819: aload #25
    //   2821: astore #17
    //   2823: aload #25
    //   2825: aload #4
    //   2827: if_acmpne -> 2840
    //   2830: aload #7
    //   2832: iload #21
    //   2834: daload
    //   2835: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   2838: astore #17
    //   2840: aload #10
    //   2842: iload #21
    //   2844: aload #17
    //   2846: invokestatic typeof : (Ljava/lang/Object;)Ljava/lang/String;
    //   2849: aastore
    //   2850: aload #9
    //   2852: astore #17
    //   2854: aload_2
    //   2855: astore #9
    //   2857: goto -> 10792
    //   2860: astore #14
    //   2862: aload #5
    //   2864: astore #7
    //   2866: aload #4
    //   2868: astore #5
    //   2870: aload_3
    //   2871: astore #10
    //   2873: iconst_0
    //   2874: istore #8
    //   2876: aload_1
    //   2877: astore #4
    //   2879: aload_2
    //   2880: astore_3
    //   2881: aload #7
    //   2883: astore_2
    //   2884: aload #14
    //   2886: astore_1
    //   2887: aload #15
    //   2889: astore #14
    //   2891: goto -> 10984
    //   2894: goto -> 4472
    //   2897: aload #9
    //   2899: astore #24
    //   2901: aload_3
    //   2902: astore #25
    //   2904: aload_1
    //   2905: astore #17
    //   2907: iload #8
    //   2909: istore #22
    //   2911: iload #6
    //   2913: ifeq -> 2930
    //   2916: aload_2
    //   2917: aload_2
    //   2918: getfield instructionCount : I
    //   2921: bipush #100
    //   2923: iadd
    //   2924: putfield instructionCount : I
    //   2927: goto -> 2930
    //   2930: iload #21
    //   2932: iload #22
    //   2934: isub
    //   2935: istore #21
    //   2937: aload #10
    //   2939: iload #21
    //   2941: aaload
    //   2942: astore #27
    //   2944: aload #27
    //   2946: instanceof com/trendmicro/hippo/InterpretedFunction
    //   2949: istore #28
    //   2951: iload #28
    //   2953: ifeq -> 3149
    //   2956: aload #27
    //   2958: checkcast com/trendmicro/hippo/InterpretedFunction
    //   2961: astore #26
    //   2963: aload #17
    //   2965: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   2968: getfield securityDomain : Ljava/lang/Object;
    //   2971: aload #26
    //   2973: getfield securityDomain : Ljava/lang/Object;
    //   2976: if_acmpne -> 3114
    //   2979: aload #26
    //   2981: aload_2
    //   2982: aload #17
    //   2984: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2987: invokevirtual createObject : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   2990: astore #14
    //   2992: aload #17
    //   2994: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   2997: astore_3
    //   2998: aload #5
    //   3000: astore_2
    //   3001: iload #6
    //   3003: istore #28
    //   3005: aload_0
    //   3006: aload_3
    //   3007: aload #14
    //   3009: aload #10
    //   3011: aload #7
    //   3013: iload #21
    //   3015: iconst_1
    //   3016: iadd
    //   3017: iload #22
    //   3019: aload #26
    //   3021: aload #17
    //   3023: invokestatic initFrame : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;[DIILcom/trendmicro/hippo/InterpretedFunction;Lcom/trendmicro/hippo/Interpreter$CallFrame;)Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   3026: astore_3
    //   3027: aload #10
    //   3029: iload #21
    //   3031: aload #14
    //   3033: aastore
    //   3034: aload #17
    //   3036: iload #21
    //   3038: putfield savedStackTop : I
    //   3041: aload #17
    //   3043: iload #23
    //   3045: putfield savedCallOp : I
    //   3048: aload_3
    //   3049: astore #5
    //   3051: aload_0
    //   3052: astore #10
    //   3054: iload #22
    //   3056: istore #8
    //   3058: aload #15
    //   3060: astore_1
    //   3061: aload #25
    //   3063: astore_3
    //   3064: aload #24
    //   3066: astore #9
    //   3068: iload #28
    //   3070: istore #6
    //   3072: aload_2
    //   3073: astore #7
    //   3075: aload #10
    //   3077: astore_2
    //   3078: goto -> 139
    //   3081: astore_1
    //   3082: aload_0
    //   3083: astore_3
    //   3084: aload #4
    //   3086: astore #5
    //   3088: aload #25
    //   3090: astore #10
    //   3092: aload #24
    //   3094: astore #9
    //   3096: iconst_0
    //   3097: istore #8
    //   3099: aload #17
    //   3101: astore #4
    //   3103: aload #15
    //   3105: astore #14
    //   3107: iload #28
    //   3109: istore #6
    //   3111: goto -> 10984
    //   3114: goto -> 3149
    //   3117: astore_1
    //   3118: aload_0
    //   3119: astore_3
    //   3120: aload #25
    //   3122: astore #10
    //   3124: aload #24
    //   3126: astore #9
    //   3128: aload #5
    //   3130: astore_2
    //   3131: iconst_0
    //   3132: istore #8
    //   3134: aload #4
    //   3136: astore #5
    //   3138: aload #17
    //   3140: astore #4
    //   3142: aload #15
    //   3144: astore #14
    //   3146: goto -> 10984
    //   3149: iload #6
    //   3151: istore #28
    //   3153: aload #9
    //   3155: astore_2
    //   3156: aload #5
    //   3158: astore #24
    //   3160: aload #27
    //   3162: astore #17
    //   3164: aload_1
    //   3165: astore #27
    //   3167: aload #17
    //   3169: instanceof com/trendmicro/hippo/Function
    //   3172: istore #29
    //   3174: iload #29
    //   3176: ifne -> 3236
    //   3179: aload #17
    //   3181: aload #4
    //   3183: if_acmpne -> 3199
    //   3186: aload #7
    //   3188: iload #21
    //   3190: daload
    //   3191: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   3194: astore #17
    //   3196: goto -> 3199
    //   3199: aload #17
    //   3201: invokestatic notFunctionError : (Ljava/lang/Object;)Ljava/lang/RuntimeException;
    //   3204: athrow
    //   3205: aload_1
    //   3206: astore #10
    //   3208: astore_1
    //   3209: aload_0
    //   3210: astore_3
    //   3211: aload #5
    //   3213: astore_2
    //   3214: iconst_0
    //   3215: istore #8
    //   3217: aload #4
    //   3219: astore #5
    //   3221: aload #10
    //   3223: astore #4
    //   3225: aload #25
    //   3227: astore #10
    //   3229: aload #15
    //   3231: astore #14
    //   3233: goto -> 10984
    //   3236: aload #17
    //   3238: checkcast com/trendmicro/hippo/Function
    //   3241: astore #30
    //   3243: aload #30
    //   3245: instanceof com/trendmicro/hippo/IdFunctionObject
    //   3248: istore #29
    //   3250: iload #29
    //   3252: ifeq -> 3337
    //   3255: aload #30
    //   3257: checkcast com/trendmicro/hippo/IdFunctionObject
    //   3260: invokestatic isContinuationConstructor : (Lcom/trendmicro/hippo/IdFunctionObject;)Z
    //   3263: ifeq -> 3334
    //   3266: aload #27
    //   3268: getfield stack : [Ljava/lang/Object;
    //   3271: astore_3
    //   3272: aload #27
    //   3274: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   3277: astore #17
    //   3279: aload_0
    //   3280: astore_1
    //   3281: aload_3
    //   3282: iload #21
    //   3284: aload_1
    //   3285: aload #17
    //   3287: iconst_0
    //   3288: invokestatic captureContinuation : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;Z)Lcom/trendmicro/hippo/NativeContinuation;
    //   3291: aastore
    //   3292: goto -> 3400
    //   3295: astore #5
    //   3297: iconst_0
    //   3298: istore #8
    //   3300: aload #25
    //   3302: astore #10
    //   3304: aload_2
    //   3305: astore #9
    //   3307: aload_1
    //   3308: astore_3
    //   3309: aload #24
    //   3311: astore_2
    //   3312: aload #5
    //   3314: astore_1
    //   3315: aload #4
    //   3317: astore #5
    //   3319: aload #27
    //   3321: astore #4
    //   3323: aload #15
    //   3325: astore #14
    //   3327: iload #28
    //   3329: istore #6
    //   3331: goto -> 10984
    //   3334: goto -> 3337
    //   3337: aload_0
    //   3338: astore #26
    //   3340: iload #21
    //   3342: istore #31
    //   3344: iload #8
    //   3346: istore #23
    //   3348: aload #26
    //   3350: astore #17
    //   3352: iload #23
    //   3354: istore #22
    //   3356: aload #10
    //   3358: aload #7
    //   3360: iload #31
    //   3362: iconst_1
    //   3363: iadd
    //   3364: iload #23
    //   3366: invokestatic getArgsArray : ([Ljava/lang/Object;[DII)[Ljava/lang/Object;
    //   3369: astore #32
    //   3371: aload #26
    //   3373: astore #17
    //   3375: iload #23
    //   3377: istore #22
    //   3379: aload #10
    //   3381: iload #31
    //   3383: aload #30
    //   3385: aload #26
    //   3387: aload #27
    //   3389: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   3392: aload #32
    //   3394: invokeinterface construct : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;
    //   3399: aastore
    //   3400: aload #27
    //   3402: astore_1
    //   3403: aload #25
    //   3405: astore_3
    //   3406: aload #16
    //   3408: astore #17
    //   3410: aload #24
    //   3412: astore #5
    //   3414: iload #28
    //   3416: istore #6
    //   3418: aload_0
    //   3419: astore #16
    //   3421: aload_2
    //   3422: astore #9
    //   3424: aload #16
    //   3426: astore_2
    //   3427: aload #17
    //   3429: astore #16
    //   3431: goto -> 338
    //   3434: astore_1
    //   3435: aload_0
    //   3436: astore_3
    //   3437: aload #4
    //   3439: astore #5
    //   3441: aload #25
    //   3443: astore #10
    //   3445: aload_2
    //   3446: astore #9
    //   3448: iconst_0
    //   3449: istore #8
    //   3451: aload #24
    //   3453: astore_2
    //   3454: aload #27
    //   3456: astore #4
    //   3458: aload #15
    //   3460: astore #14
    //   3462: iload #28
    //   3464: istore #6
    //   3466: goto -> 10984
    //   3469: astore_1
    //   3470: aload #25
    //   3472: astore #10
    //   3474: aload #24
    //   3476: astore #9
    //   3478: iconst_0
    //   3479: istore #8
    //   3481: aload_2
    //   3482: astore_3
    //   3483: aload #5
    //   3485: astore_2
    //   3486: aload #4
    //   3488: astore #5
    //   3490: aload #17
    //   3492: astore #4
    //   3494: aload #15
    //   3496: astore #14
    //   3498: goto -> 10984
    //   3501: iload #8
    //   3503: istore #22
    //   3505: aload_1
    //   3506: astore #25
    //   3508: aload_2
    //   3509: astore #17
    //   3511: aload #25
    //   3513: iload #21
    //   3515: invokestatic stack_double : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)D
    //   3518: dstore #33
    //   3520: aload #10
    //   3522: iload #21
    //   3524: aload #4
    //   3526: aastore
    //   3527: dload #33
    //   3529: dstore #35
    //   3531: iload #23
    //   3533: bipush #29
    //   3535: if_icmpne -> 3543
    //   3538: dload #33
    //   3540: dneg
    //   3541: dstore #35
    //   3543: aload #7
    //   3545: iload #21
    //   3547: dload #35
    //   3549: dastore
    //   3550: aload #9
    //   3552: astore #17
    //   3554: aload_2
    //   3555: astore #9
    //   3557: goto -> 10792
    //   3560: iload #8
    //   3562: istore #22
    //   3564: aload_1
    //   3565: astore #25
    //   3567: aload_2
    //   3568: astore #17
    //   3570: aload #25
    //   3572: iload #21
    //   3574: invokestatic stack_int32 : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)I
    //   3577: istore #22
    //   3579: aload #10
    //   3581: iload #21
    //   3583: aload #4
    //   3585: aastore
    //   3586: aload #7
    //   3588: iload #21
    //   3590: iload #22
    //   3592: i2d
    //   3593: dastore
    //   3594: aload #9
    //   3596: astore #17
    //   3598: aload_2
    //   3599: astore #9
    //   3601: goto -> 10792
    //   3604: iload #8
    //   3606: istore #23
    //   3608: aload_1
    //   3609: astore #25
    //   3611: aload_2
    //   3612: astore #17
    //   3614: iload #23
    //   3616: istore #22
    //   3618: aload #25
    //   3620: iload #21
    //   3622: invokestatic stack_boolean : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)Z
    //   3625: ifne -> 3634
    //   3628: iconst_1
    //   3629: istore #28
    //   3631: goto -> 3637
    //   3634: iconst_0
    //   3635: istore #28
    //   3637: aload_2
    //   3638: astore #17
    //   3640: iload #23
    //   3642: istore #22
    //   3644: aload #10
    //   3646: iload #21
    //   3648: iload #28
    //   3650: invokestatic wrapBoolean : (Z)Ljava/lang/Boolean;
    //   3653: aastore
    //   3654: aload #9
    //   3656: astore #17
    //   3658: aload_2
    //   3659: astore #9
    //   3661: goto -> 10792
    //   3664: aload_1
    //   3665: astore #25
    //   3667: aload_2
    //   3668: astore #17
    //   3670: iload #8
    //   3672: istore #22
    //   3674: aload #25
    //   3676: iload #23
    //   3678: aload #10
    //   3680: aload #7
    //   3682: iload #21
    //   3684: invokestatic doArithmetic : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
    //   3687: istore #21
    //   3689: aload #25
    //   3691: astore_1
    //   3692: goto -> 338
    //   3695: iinc #21, -1
    //   3698: aload_2
    //   3699: astore #17
    //   3701: iload #8
    //   3703: istore #22
    //   3705: aload #10
    //   3707: aload #7
    //   3709: iload #21
    //   3711: aload_2
    //   3712: invokestatic doAdd : ([Ljava/lang/Object;[DILcom/trendmicro/hippo/Context;)V
    //   3715: goto -> 338
    //   3718: aload_1
    //   3719: astore #25
    //   3721: aload_2
    //   3722: astore #17
    //   3724: iload #8
    //   3726: istore #22
    //   3728: aload #25
    //   3730: iload #21
    //   3732: iconst_1
    //   3733: isub
    //   3734: invokestatic stack_double : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)D
    //   3737: dstore #35
    //   3739: aload_2
    //   3740: astore #17
    //   3742: iload #8
    //   3744: istore #22
    //   3746: aload #25
    //   3748: iload #21
    //   3750: invokestatic stack_int32 : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)I
    //   3753: istore #23
    //   3755: iinc #21, -1
    //   3758: aload #10
    //   3760: iload #21
    //   3762: aload #4
    //   3764: aastore
    //   3765: aload_2
    //   3766: astore #17
    //   3768: iload #8
    //   3770: istore #22
    //   3772: aload #7
    //   3774: iload #21
    //   3776: dload #35
    //   3778: invokestatic toUint32 : (D)J
    //   3781: iload #23
    //   3783: bipush #31
    //   3785: iand
    //   3786: lushr
    //   3787: l2d
    //   3788: dastore
    //   3789: aload #25
    //   3791: astore_1
    //   3792: goto -> 338
    //   3795: aload_1
    //   3796: astore #25
    //   3798: aload_2
    //   3799: astore #17
    //   3801: iload #8
    //   3803: istore #22
    //   3805: aload #25
    //   3807: iload #23
    //   3809: aload #10
    //   3811: aload #7
    //   3813: iload #21
    //   3815: invokestatic doCompare : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
    //   3818: istore #21
    //   3820: aload #25
    //   3822: astore_1
    //   3823: goto -> 338
    //   3826: iload #21
    //   3828: iconst_1
    //   3829: isub
    //   3830: istore #31
    //   3832: aload_2
    //   3833: astore #17
    //   3835: iload #8
    //   3837: istore #22
    //   3839: aload #10
    //   3841: aload #7
    //   3843: iload #31
    //   3845: invokestatic doEquals : ([Ljava/lang/Object;[DI)Z
    //   3848: istore #28
    //   3850: iload #23
    //   3852: bipush #13
    //   3854: if_icmpne -> 3863
    //   3857: iconst_1
    //   3858: istore #21
    //   3860: goto -> 3866
    //   3863: iconst_0
    //   3864: istore #21
    //   3866: aload_2
    //   3867: astore #17
    //   3869: iload #8
    //   3871: istore #22
    //   3873: aload #10
    //   3875: iload #31
    //   3877: iload #28
    //   3879: iload #21
    //   3881: ixor
    //   3882: invokestatic wrapBoolean : (Z)Ljava/lang/Boolean;
    //   3885: aastore
    //   3886: iload #31
    //   3888: istore #21
    //   3890: goto -> 338
    //   3893: aload_1
    //   3894: astore #25
    //   3896: aload_2
    //   3897: astore #17
    //   3899: iload #8
    //   3901: istore #22
    //   3903: aload #25
    //   3905: iload #23
    //   3907: aload #10
    //   3909: aload #7
    //   3911: iload #21
    //   3913: invokestatic doBitOp : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
    //   3916: istore #21
    //   3918: aload #25
    //   3920: astore_1
    //   3921: goto -> 338
    //   3924: aload_1
    //   3925: astore #10
    //   3927: astore_1
    //   3928: iconst_0
    //   3929: istore #8
    //   3931: aload_3
    //   3932: astore #7
    //   3934: aload #5
    //   3936: astore_2
    //   3937: aload #17
    //   3939: astore_3
    //   3940: aload #4
    //   3942: astore #5
    //   3944: aload #10
    //   3946: astore #4
    //   3948: aload #7
    //   3950: astore #10
    //   3952: aload #15
    //   3954: astore #14
    //   3956: goto -> 10984
    //   3959: aload #5
    //   3961: astore #30
    //   3963: aload #9
    //   3965: astore #27
    //   3967: iload #6
    //   3969: istore #28
    //   3971: aload_3
    //   3972: astore #26
    //   3974: aload_2
    //   3975: astore #25
    //   3977: aload_1
    //   3978: astore #24
    //   3980: aload #10
    //   3982: iload #21
    //   3984: aaload
    //   3985: astore #32
    //   3987: aload #32
    //   3989: astore #17
    //   3991: aload #32
    //   3993: aload #4
    //   3995: if_acmpne -> 4020
    //   3998: aload #25
    //   4000: astore #17
    //   4002: iload #8
    //   4004: istore #22
    //   4006: aload #7
    //   4008: iload #21
    //   4010: daload
    //   4011: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   4014: astore #32
    //   4016: aload #32
    //   4018: astore #17
    //   4020: iinc #21, -1
    //   4023: aload #10
    //   4025: iload #21
    //   4027: aaload
    //   4028: checkcast com/trendmicro/hippo/Scriptable
    //   4031: astore #37
    //   4033: iload #23
    //   4035: bipush #8
    //   4037: if_icmpne -> 4068
    //   4040: aload #24
    //   4042: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4045: astore #32
    //   4047: aload #37
    //   4049: aload #17
    //   4051: aload #25
    //   4053: aload #32
    //   4055: aload #30
    //   4057: invokestatic setName : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
    //   4060: astore #17
    //   4062: aload #17
    //   4064: astore_1
    //   4065: goto -> 4089
    //   4068: aload #37
    //   4070: aload #17
    //   4072: aload #25
    //   4074: aload #24
    //   4076: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4079: aload #30
    //   4081: invokestatic strictSetName : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
    //   4084: astore #17
    //   4086: aload #17
    //   4088: astore_1
    //   4089: aload #10
    //   4091: iload #21
    //   4093: aload_1
    //   4094: aastore
    //   4095: aload #24
    //   4097: astore_1
    //   4098: aload #26
    //   4100: astore_3
    //   4101: iload #28
    //   4103: istore #6
    //   4105: aload #25
    //   4107: astore_2
    //   4108: aload #27
    //   4110: astore #9
    //   4112: goto -> 338
    //   4115: astore_1
    //   4116: aload #30
    //   4118: astore_2
    //   4119: iconst_0
    //   4120: istore #8
    //   4122: aload #25
    //   4124: astore_3
    //   4125: aload #4
    //   4127: astore #5
    //   4129: aload #26
    //   4131: astore #10
    //   4133: aload #27
    //   4135: astore #9
    //   4137: aload #24
    //   4139: astore #4
    //   4141: aload #15
    //   4143: astore #14
    //   4145: iload #28
    //   4147: istore #6
    //   4149: goto -> 10984
    //   4152: aload_3
    //   4153: astore #25
    //   4155: aload_2
    //   4156: astore #17
    //   4158: iload #8
    //   4160: istore #23
    //   4162: aload_1
    //   4163: astore #24
    //   4165: aload #5
    //   4167: astore #27
    //   4169: iload #21
    //   4171: iconst_1
    //   4172: isub
    //   4173: istore #22
    //   4175: aload #24
    //   4177: iload #21
    //   4179: invokestatic stack_boolean : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)Z
    //   4182: ifeq -> 4221
    //   4185: aload #24
    //   4187: aload #24
    //   4189: getfield pc : I
    //   4192: iconst_2
    //   4193: iadd
    //   4194: putfield pc : I
    //   4197: aload #27
    //   4199: astore #5
    //   4201: aload #24
    //   4203: astore_1
    //   4204: iload #23
    //   4206: istore #8
    //   4208: aload #25
    //   4210: astore_3
    //   4211: aload #17
    //   4213: astore_2
    //   4214: iload #22
    //   4216: istore #21
    //   4218: goto -> 338
    //   4221: aload #17
    //   4223: astore_2
    //   4224: iload #22
    //   4226: istore #21
    //   4228: goto -> 7879
    //   4231: aload_3
    //   4232: astore #25
    //   4234: aload_2
    //   4235: astore #17
    //   4237: iload #8
    //   4239: istore #23
    //   4241: aload_1
    //   4242: astore #24
    //   4244: aload #5
    //   4246: astore #27
    //   4248: iload #21
    //   4250: iconst_1
    //   4251: isub
    //   4252: istore #22
    //   4254: aload #24
    //   4256: iload #21
    //   4258: invokestatic stack_boolean : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)Z
    //   4261: ifne -> 4300
    //   4264: aload #24
    //   4266: aload #24
    //   4268: getfield pc : I
    //   4271: iconst_2
    //   4272: iadd
    //   4273: putfield pc : I
    //   4276: aload #27
    //   4278: astore #5
    //   4280: aload #24
    //   4282: astore_1
    //   4283: iload #23
    //   4285: istore #8
    //   4287: aload #25
    //   4289: astore_3
    //   4290: aload #17
    //   4292: astore_2
    //   4293: iload #22
    //   4295: istore #21
    //   4297: goto -> 338
    //   4300: aload #17
    //   4302: astore_2
    //   4303: iload #22
    //   4305: istore #21
    //   4307: goto -> 7879
    //   4310: goto -> 7879
    //   4313: aload_1
    //   4314: astore #14
    //   4316: aload #14
    //   4318: aload #10
    //   4320: iload #21
    //   4322: aaload
    //   4323: putfield result : Ljava/lang/Object;
    //   4326: aload #14
    //   4328: aload #7
    //   4330: iload #21
    //   4332: daload
    //   4333: putfield resultDbl : D
    //   4336: goto -> 10474
    //   4339: aload_1
    //   4340: astore #17
    //   4342: aload #17
    //   4344: aload #17
    //   4346: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4349: invokestatic leaveWith : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   4352: putfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4355: aload #9
    //   4357: astore #17
    //   4359: aload_2
    //   4360: astore #9
    //   4362: goto -> 10792
    //   4365: aload_2
    //   4366: astore #17
    //   4368: aload_1
    //   4369: astore #25
    //   4371: aload #10
    //   4373: iload #21
    //   4375: aaload
    //   4376: astore #27
    //   4378: aload #27
    //   4380: astore #24
    //   4382: aload #27
    //   4384: aload #4
    //   4386: if_acmpne -> 4399
    //   4389: aload #7
    //   4391: iload #21
    //   4393: daload
    //   4394: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   4397: astore #24
    //   4399: iinc #21, -1
    //   4402: aload #25
    //   4404: aload #24
    //   4406: aload #17
    //   4408: aload #25
    //   4410: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4413: invokestatic enterWith : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   4416: putfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4419: aload #25
    //   4421: astore_1
    //   4422: aload #17
    //   4424: astore_2
    //   4425: goto -> 338
    //   4428: aload_1
    //   4429: astore #10
    //   4431: astore #14
    //   4433: aload #5
    //   4435: astore_1
    //   4436: iconst_0
    //   4437: istore #8
    //   4439: aload #4
    //   4441: astore #5
    //   4443: aload_3
    //   4444: astore #7
    //   4446: aload #14
    //   4448: astore #4
    //   4450: aload_2
    //   4451: astore_3
    //   4452: aload_1
    //   4453: astore_2
    //   4454: aload #4
    //   4456: astore_1
    //   4457: aload #10
    //   4459: astore #4
    //   4461: aload #7
    //   4463: astore #10
    //   4465: aload #15
    //   4467: astore #14
    //   4469: goto -> 10984
    //   4472: aload_1
    //   4473: astore #17
    //   4475: aload #5
    //   4477: astore_2
    //   4478: aload_0
    //   4479: aload #17
    //   4481: iload #23
    //   4483: aload #10
    //   4485: aload #7
    //   4487: iload #21
    //   4489: invokestatic doDelName : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
    //   4492: istore #21
    //   4494: aload #17
    //   4496: astore_1
    //   4497: aload_0
    //   4498: astore_2
    //   4499: goto -> 338
    //   4502: aload #10
    //   4504: iload #21
    //   4506: iconst_1
    //   4507: iadd
    //   4508: aload #10
    //   4510: iload #21
    //   4512: aaload
    //   4513: aastore
    //   4514: aload #7
    //   4516: iload #21
    //   4518: iconst_1
    //   4519: iadd
    //   4520: aload #7
    //   4522: iload #21
    //   4524: daload
    //   4525: dastore
    //   4526: iinc #21, 1
    //   4529: aload_0
    //   4530: astore_2
    //   4531: goto -> 338
    //   4534: aload #10
    //   4536: iload #21
    //   4538: iconst_1
    //   4539: iadd
    //   4540: aload #10
    //   4542: iload #21
    //   4544: iconst_1
    //   4545: isub
    //   4546: aaload
    //   4547: aastore
    //   4548: aload #7
    //   4550: iload #21
    //   4552: iconst_1
    //   4553: iadd
    //   4554: aload #7
    //   4556: iload #21
    //   4558: iconst_1
    //   4559: isub
    //   4560: daload
    //   4561: dastore
    //   4562: aload #10
    //   4564: iload #21
    //   4566: iconst_2
    //   4567: iadd
    //   4568: aload #10
    //   4570: iload #21
    //   4572: aaload
    //   4573: aastore
    //   4574: aload #7
    //   4576: iload #21
    //   4578: iconst_2
    //   4579: iadd
    //   4580: aload #7
    //   4582: iload #21
    //   4584: daload
    //   4585: dastore
    //   4586: iinc #21, 2
    //   4589: aload_0
    //   4590: astore_2
    //   4591: goto -> 338
    //   4594: aload #5
    //   4596: astore_2
    //   4597: aload #10
    //   4599: iload #21
    //   4601: aaload
    //   4602: astore_2
    //   4603: aload #10
    //   4605: iload #21
    //   4607: aload #10
    //   4609: iload #21
    //   4611: iconst_1
    //   4612: isub
    //   4613: aaload
    //   4614: aastore
    //   4615: aload #10
    //   4617: iload #21
    //   4619: iconst_1
    //   4620: isub
    //   4621: aload_2
    //   4622: aastore
    //   4623: aload #7
    //   4625: iload #21
    //   4627: daload
    //   4628: dstore #35
    //   4630: aload #7
    //   4632: iload #21
    //   4634: aload #7
    //   4636: iload #21
    //   4638: iconst_1
    //   4639: isub
    //   4640: daload
    //   4641: dastore
    //   4642: aload #7
    //   4644: iload #21
    //   4646: iconst_1
    //   4647: isub
    //   4648: dload #35
    //   4650: dastore
    //   4651: aload_0
    //   4652: astore_2
    //   4653: aload #9
    //   4655: astore #17
    //   4657: aload_2
    //   4658: astore #9
    //   4660: goto -> 10792
    //   4663: aload #10
    //   4665: iload #21
    //   4667: aconst_null
    //   4668: aastore
    //   4669: iinc #21, -1
    //   4672: aload_0
    //   4673: astore_2
    //   4674: goto -> 338
    //   4677: aload_1
    //   4678: astore #17
    //   4680: aload #5
    //   4682: astore_2
    //   4683: aload #17
    //   4685: aload #10
    //   4687: iload #21
    //   4689: aaload
    //   4690: putfield result : Ljava/lang/Object;
    //   4693: aload #5
    //   4695: astore_2
    //   4696: aload #17
    //   4698: aload #7
    //   4700: iload #21
    //   4702: daload
    //   4703: putfield resultDbl : D
    //   4706: aload #10
    //   4708: iload #21
    //   4710: aconst_null
    //   4711: aastore
    //   4712: iinc #21, -1
    //   4715: aload #17
    //   4717: astore_1
    //   4718: aload_0
    //   4719: astore_2
    //   4720: goto -> 338
    //   4723: iload #8
    //   4725: istore #22
    //   4727: aload #5
    //   4729: astore #17
    //   4731: aload_3
    //   4732: astore #25
    //   4734: aload_1
    //   4735: astore #24
    //   4737: iload #21
    //   4739: iconst_1
    //   4740: isub
    //   4741: istore #23
    //   4743: aload #17
    //   4745: astore_2
    //   4746: aload #24
    //   4748: iload #21
    //   4750: invokestatic stack_boolean : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)Z
    //   4753: ifne -> 4794
    //   4756: aload #17
    //   4758: astore_2
    //   4759: aload #24
    //   4761: aload #24
    //   4763: getfield pc : I
    //   4766: iconst_2
    //   4767: iadd
    //   4768: putfield pc : I
    //   4771: aload #24
    //   4773: astore_1
    //   4774: iload #22
    //   4776: istore #8
    //   4778: aload #25
    //   4780: astore_3
    //   4781: aload #17
    //   4783: astore #5
    //   4785: aload_0
    //   4786: astore_2
    //   4787: iload #23
    //   4789: istore #21
    //   4791: goto -> 338
    //   4794: aload #10
    //   4796: iload #23
    //   4798: aconst_null
    //   4799: aastore
    //   4800: iload #23
    //   4802: iconst_1
    //   4803: isub
    //   4804: istore #21
    //   4806: aload_0
    //   4807: astore_2
    //   4808: goto -> 7879
    //   4811: aload_1
    //   4812: astore #10
    //   4814: astore_1
    //   4815: aload_0
    //   4816: astore #14
    //   4818: iconst_0
    //   4819: istore #8
    //   4821: aload #4
    //   4823: astore #5
    //   4825: aload_3
    //   4826: astore #7
    //   4828: aload #14
    //   4830: astore_3
    //   4831: aload #10
    //   4833: astore #4
    //   4835: aload #7
    //   4837: astore #10
    //   4839: aload #15
    //   4841: astore #14
    //   4843: goto -> 10984
    //   4846: aload_1
    //   4847: astore #17
    //   4849: aload_0
    //   4850: aload #17
    //   4852: aload #10
    //   4854: aload #7
    //   4856: iload #21
    //   4858: aload #16
    //   4860: aload #18
    //   4862: aload #19
    //   4864: iload #8
    //   4866: invokestatic doVarIncDec : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[D[II)I
    //   4869: istore #21
    //   4871: aload_0
    //   4872: astore_2
    //   4873: aload #17
    //   4875: astore_1
    //   4876: goto -> 338
    //   4879: astore_1
    //   4880: aload_3
    //   4881: astore #10
    //   4883: aload_0
    //   4884: astore_3
    //   4885: aload #5
    //   4887: astore_2
    //   4888: iconst_0
    //   4889: istore #8
    //   4891: aload #4
    //   4893: astore #5
    //   4895: aload #17
    //   4897: astore #4
    //   4899: aload #15
    //   4901: astore #14
    //   4903: goto -> 10984
    //   4906: aload #9
    //   4908: astore #24
    //   4910: iload #6
    //   4912: istore #28
    //   4914: aload_3
    //   4915: astore #25
    //   4917: aload #4
    //   4919: astore #27
    //   4921: aload_1
    //   4922: astore #17
    //   4924: aload_2
    //   4925: astore #26
    //   4927: iinc #21, 1
    //   4930: aload #17
    //   4932: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   4935: astore #32
    //   4937: aload #14
    //   4939: aload #17
    //   4941: getfield pc : I
    //   4944: baload
    //   4945: istore #22
    //   4947: aload #5
    //   4949: astore #30
    //   4951: aload #10
    //   4953: iload #21
    //   4955: aload #32
    //   4957: aload #30
    //   4959: aload #26
    //   4961: iload #22
    //   4963: invokestatic nameIncrDecr : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Context;I)Ljava/lang/Object;
    //   4966: aastore
    //   4967: aload #17
    //   4969: aload #17
    //   4971: getfield pc : I
    //   4974: iconst_1
    //   4975: iadd
    //   4976: putfield pc : I
    //   4979: aload #26
    //   4981: astore_2
    //   4982: aload #27
    //   4984: astore #4
    //   4986: aload #17
    //   4988: astore_1
    //   4989: aload #24
    //   4991: astore #9
    //   4993: iload #28
    //   4995: istore #6
    //   4997: aload #30
    //   4999: astore #5
    //   5001: aload #25
    //   5003: astore_3
    //   5004: goto -> 338
    //   5007: astore_1
    //   5008: aload #25
    //   5010: astore #10
    //   5012: aload #26
    //   5014: astore_3
    //   5015: aload #5
    //   5017: astore_2
    //   5018: aload #24
    //   5020: astore #9
    //   5022: iconst_0
    //   5023: istore #8
    //   5025: aload #27
    //   5027: astore #5
    //   5029: aload #17
    //   5031: astore #4
    //   5033: aload #15
    //   5035: astore #14
    //   5037: iload #28
    //   5039: istore #6
    //   5041: goto -> 10984
    //   5044: aload #4
    //   5046: astore #26
    //   5048: aload_1
    //   5049: astore #27
    //   5051: aload #5
    //   5053: astore #24
    //   5055: aload_2
    //   5056: astore #17
    //   5058: aload #10
    //   5060: iload #21
    //   5062: aaload
    //   5063: astore #30
    //   5065: aload #30
    //   5067: astore #25
    //   5069: aload #30
    //   5071: aload #26
    //   5073: if_acmpne -> 5086
    //   5076: aload #7
    //   5078: iload #21
    //   5080: daload
    //   5081: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   5084: astore #25
    //   5086: aload #10
    //   5088: iload #21
    //   5090: aload #25
    //   5092: aload #24
    //   5094: aload #17
    //   5096: aload #27
    //   5098: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5101: aload #14
    //   5103: aload #27
    //   5105: getfield pc : I
    //   5108: baload
    //   5109: invokestatic propIncrDecr : (Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;
    //   5112: aastore
    //   5113: aload #27
    //   5115: aload #27
    //   5117: getfield pc : I
    //   5120: iconst_1
    //   5121: iadd
    //   5122: putfield pc : I
    //   5125: aload #17
    //   5127: astore_2
    //   5128: aload #9
    //   5130: astore #17
    //   5132: aload_2
    //   5133: astore #9
    //   5135: goto -> 10792
    //   5138: aload_1
    //   5139: astore #10
    //   5141: astore_1
    //   5142: aload_3
    //   5143: astore #7
    //   5145: iconst_0
    //   5146: istore #8
    //   5148: aload_2
    //   5149: astore_3
    //   5150: aload #5
    //   5152: astore_2
    //   5153: aload #4
    //   5155: astore #5
    //   5157: aload #10
    //   5159: astore #4
    //   5161: aload #7
    //   5163: astore #10
    //   5165: aload #15
    //   5167: astore #14
    //   5169: goto -> 10984
    //   5172: aload_1
    //   5173: astore #17
    //   5175: aload_0
    //   5176: aload #17
    //   5178: aload #14
    //   5180: aload #10
    //   5182: aload #7
    //   5184: iload #21
    //   5186: invokestatic doElemIncDec : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[B[Ljava/lang/Object;[DI)I
    //   5189: istore #21
    //   5191: aload #17
    //   5193: astore_1
    //   5194: goto -> 338
    //   5197: aload_2
    //   5198: astore #17
    //   5200: aload #10
    //   5202: astore #25
    //   5204: aload_1
    //   5205: astore #24
    //   5207: aload #25
    //   5209: iload #21
    //   5211: aload #25
    //   5213: iload #21
    //   5215: aaload
    //   5216: checkcast com/trendmicro/hippo/Ref
    //   5219: aload #17
    //   5221: aload #24
    //   5223: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5226: aload #14
    //   5228: aload #24
    //   5230: getfield pc : I
    //   5233: baload
    //   5234: invokestatic refIncrDecr : (Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;
    //   5237: aastore
    //   5238: aload #24
    //   5240: aload #24
    //   5242: getfield pc : I
    //   5245: iconst_1
    //   5246: iadd
    //   5247: putfield pc : I
    //   5250: aload #9
    //   5252: astore_2
    //   5253: aload #17
    //   5255: astore #9
    //   5257: aload_2
    //   5258: astore #17
    //   5260: goto -> 10792
    //   5263: aload_1
    //   5264: astore #10
    //   5266: astore_1
    //   5267: aload_3
    //   5268: astore #7
    //   5270: iconst_0
    //   5271: istore #8
    //   5273: aload_2
    //   5274: astore_3
    //   5275: aload #5
    //   5277: astore_2
    //   5278: aload #4
    //   5280: astore #5
    //   5282: aload #10
    //   5284: astore #4
    //   5286: aload #7
    //   5288: astore #10
    //   5290: aload #15
    //   5292: astore #14
    //   5294: goto -> 10984
    //   5297: aload #9
    //   5299: astore #25
    //   5301: iload #6
    //   5303: istore #28
    //   5305: aload_3
    //   5306: astore #24
    //   5308: aload #4
    //   5310: astore #26
    //   5312: aload_2
    //   5313: astore #27
    //   5315: aload_1
    //   5316: astore #17
    //   5318: aload #5
    //   5320: astore #30
    //   5322: aload #17
    //   5324: getfield localShift : I
    //   5327: istore #22
    //   5329: iload #8
    //   5331: iload #22
    //   5333: iadd
    //   5334: istore #8
    //   5336: aload #17
    //   5338: aload #10
    //   5340: iload #8
    //   5342: aaload
    //   5343: checkcast com/trendmicro/hippo/Scriptable
    //   5346: putfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5349: aload #17
    //   5351: astore_1
    //   5352: aload #30
    //   5354: astore #5
    //   5356: aload #24
    //   5358: astore_3
    //   5359: iload #28
    //   5361: istore #6
    //   5363: aload #27
    //   5365: astore_2
    //   5366: aload #26
    //   5368: astore #4
    //   5370: aload #25
    //   5372: astore #9
    //   5374: goto -> 338
    //   5377: astore_1
    //   5378: aload #26
    //   5380: astore #5
    //   5382: aload #30
    //   5384: astore_2
    //   5385: aload #27
    //   5387: astore_3
    //   5388: aload #24
    //   5390: astore #10
    //   5392: aload #25
    //   5394: astore #9
    //   5396: iconst_0
    //   5397: istore #8
    //   5399: aload #17
    //   5401: astore #4
    //   5403: aload #15
    //   5405: astore #14
    //   5407: iload #28
    //   5409: istore #6
    //   5411: goto -> 10984
    //   5414: aload_1
    //   5415: astore #17
    //   5417: aload #17
    //   5419: getfield localShift : I
    //   5422: istore #22
    //   5424: iload #8
    //   5426: iload #22
    //   5428: iadd
    //   5429: istore #8
    //   5431: aload #10
    //   5433: iload #8
    //   5435: aload #17
    //   5437: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5440: aastore
    //   5441: aload #17
    //   5443: astore_1
    //   5444: goto -> 338
    //   5447: aload_1
    //   5448: astore #10
    //   5450: astore_1
    //   5451: aload_3
    //   5452: astore #7
    //   5454: iconst_0
    //   5455: istore #8
    //   5457: aload_2
    //   5458: astore_3
    //   5459: aload #5
    //   5461: astore_2
    //   5462: aload #4
    //   5464: astore #5
    //   5466: aload #10
    //   5468: astore #4
    //   5470: aload #7
    //   5472: astore #10
    //   5474: aload #15
    //   5476: astore #14
    //   5478: goto -> 10984
    //   5481: aload_1
    //   5482: astore #25
    //   5484: aload #5
    //   5486: astore #17
    //   5488: iinc #21, 1
    //   5491: aload #10
    //   5493: iload #21
    //   5495: aload #25
    //   5497: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5500: aload #17
    //   5502: invokestatic typeofName : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/String;
    //   5505: aastore
    //   5506: aload #25
    //   5508: astore_1
    //   5509: aload #17
    //   5511: astore #5
    //   5513: goto -> 338
    //   5516: aload_2
    //   5517: astore #17
    //   5519: aload_1
    //   5520: astore #24
    //   5522: aload #5
    //   5524: astore #25
    //   5526: iinc #21, 1
    //   5529: aload #10
    //   5531: iload #21
    //   5533: aload #25
    //   5535: aload #17
    //   5537: aload #24
    //   5539: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5542: invokestatic getNameFunctionAndThis : (Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;
    //   5545: aastore
    //   5546: iinc #21, 1
    //   5549: aload #10
    //   5551: iload #21
    //   5553: aload_0
    //   5554: invokestatic lastStoredScriptable : (Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Scriptable;
    //   5557: aastore
    //   5558: aload #24
    //   5560: astore_1
    //   5561: aload #25
    //   5563: astore #5
    //   5565: aload #17
    //   5567: astore_2
    //   5568: goto -> 338
    //   5571: aload #4
    //   5573: astore #17
    //   5575: aload_2
    //   5576: astore #25
    //   5578: aload_1
    //   5579: astore #27
    //   5581: aload #5
    //   5583: astore #24
    //   5585: aload #10
    //   5587: iload #21
    //   5589: aaload
    //   5590: astore #26
    //   5592: aload #26
    //   5594: aload #17
    //   5596: if_acmpne -> 5612
    //   5599: aload #7
    //   5601: iload #21
    //   5603: daload
    //   5604: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   5607: astore #26
    //   5609: goto -> 5612
    //   5612: aload #10
    //   5614: iload #21
    //   5616: aload #26
    //   5618: aload #24
    //   5620: aload #25
    //   5622: aload #27
    //   5624: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5627: invokestatic getPropFunctionAndThis : (Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;
    //   5630: aastore
    //   5631: iinc #21, 1
    //   5634: aload #10
    //   5636: iload #21
    //   5638: aload_0
    //   5639: invokestatic lastStoredScriptable : (Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Scriptable;
    //   5642: aastore
    //   5643: aload #27
    //   5645: astore_1
    //   5646: aload #24
    //   5648: astore #5
    //   5650: aload #25
    //   5652: astore_2
    //   5653: aload #17
    //   5655: astore #4
    //   5657: goto -> 338
    //   5660: aload #4
    //   5662: astore #30
    //   5664: aload_2
    //   5665: astore #17
    //   5667: aload #10
    //   5669: astore #27
    //   5671: aload_1
    //   5672: astore #26
    //   5674: aload #27
    //   5676: iload #21
    //   5678: iconst_1
    //   5679: isub
    //   5680: aaload
    //   5681: astore #24
    //   5683: aload #24
    //   5685: astore #25
    //   5687: aload #24
    //   5689: aload #30
    //   5691: if_acmpne -> 5706
    //   5694: aload #7
    //   5696: iload #21
    //   5698: iconst_1
    //   5699: isub
    //   5700: daload
    //   5701: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   5704: astore #25
    //   5706: aload #27
    //   5708: iload #21
    //   5710: aaload
    //   5711: astore #32
    //   5713: aload #32
    //   5715: astore #24
    //   5717: aload #32
    //   5719: aload #30
    //   5721: if_acmpne -> 5734
    //   5724: aload #7
    //   5726: iload #21
    //   5728: daload
    //   5729: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   5732: astore #24
    //   5734: aload #27
    //   5736: iload #21
    //   5738: iconst_1
    //   5739: isub
    //   5740: aload #25
    //   5742: aload #24
    //   5744: aload #17
    //   5746: aload #26
    //   5748: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5751: invokestatic getElemFunctionAndThis : (Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;
    //   5754: aastore
    //   5755: aload #27
    //   5757: iload #21
    //   5759: aload_0
    //   5760: invokestatic lastStoredScriptable : (Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Scriptable;
    //   5763: aastore
    //   5764: aload #9
    //   5766: astore_2
    //   5767: aload #17
    //   5769: astore #9
    //   5771: aload_2
    //   5772: astore #17
    //   5774: goto -> 10792
    //   5777: aload #4
    //   5779: astore #17
    //   5781: aload_2
    //   5782: astore #25
    //   5784: aload #10
    //   5786: iload #21
    //   5788: aaload
    //   5789: astore #27
    //   5791: aload #27
    //   5793: astore #24
    //   5795: aload #27
    //   5797: aload #17
    //   5799: if_acmpne -> 5812
    //   5802: aload #7
    //   5804: iload #21
    //   5806: daload
    //   5807: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   5810: astore #24
    //   5812: aload #10
    //   5814: iload #21
    //   5816: aload #24
    //   5818: aload #25
    //   5820: invokestatic getValueFunctionAndThis : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Callable;
    //   5823: aastore
    //   5824: iinc #21, 1
    //   5827: aload #10
    //   5829: iload #21
    //   5831: aload_0
    //   5832: invokestatic lastStoredScriptable : (Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Scriptable;
    //   5835: aastore
    //   5836: aload #25
    //   5838: astore_2
    //   5839: aload #17
    //   5841: astore #4
    //   5843: goto -> 338
    //   5846: aload_1
    //   5847: astore #10
    //   5849: astore_1
    //   5850: aload_3
    //   5851: astore #7
    //   5853: iconst_0
    //   5854: istore #8
    //   5856: aload_2
    //   5857: astore_3
    //   5858: aload #5
    //   5860: astore_2
    //   5861: aload #4
    //   5863: astore #5
    //   5865: aload #10
    //   5867: astore #4
    //   5869: aload #7
    //   5871: astore #10
    //   5873: aload #15
    //   5875: astore #14
    //   5877: goto -> 10984
    //   5880: aload #9
    //   5882: astore #17
    //   5884: iload #6
    //   5886: istore #28
    //   5888: aload #4
    //   5890: astore #25
    //   5892: aload_2
    //   5893: astore #24
    //   5895: aload_1
    //   5896: astore #30
    //   5898: aload_3
    //   5899: astore #27
    //   5901: aload #5
    //   5903: astore #26
    //   5905: aload #24
    //   5907: aload #30
    //   5909: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5912: aload #30
    //   5914: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   5917: iload #8
    //   5919: invokestatic createFunction : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/InterpretedFunction;I)Lcom/trendmicro/hippo/InterpretedFunction;
    //   5922: astore #32
    //   5924: aload #32
    //   5926: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   5929: getfield itsFunctionType : I
    //   5932: iconst_4
    //   5933: if_icmpne -> 5993
    //   5936: iinc #21, 1
    //   5939: aload #10
    //   5941: iload #21
    //   5943: new com/trendmicro/hippo/ArrowFunction
    //   5946: dup
    //   5947: aload #24
    //   5949: aload #30
    //   5951: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   5954: aload #32
    //   5956: aload #30
    //   5958: getfield thisObj : Lcom/trendmicro/hippo/Scriptable;
    //   5961: invokespecial <init> : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;)V
    //   5964: aastore
    //   5965: aload #30
    //   5967: astore_1
    //   5968: aload #26
    //   5970: astore #5
    //   5972: iload #28
    //   5974: istore #6
    //   5976: aload #27
    //   5978: astore_3
    //   5979: aload #24
    //   5981: astore_2
    //   5982: aload #25
    //   5984: astore #4
    //   5986: aload #17
    //   5988: astore #9
    //   5990: goto -> 338
    //   5993: iinc #21, 1
    //   5996: aload #10
    //   5998: iload #21
    //   6000: aload #32
    //   6002: aastore
    //   6003: aload #30
    //   6005: astore_1
    //   6006: aload #26
    //   6008: astore #5
    //   6010: iload #28
    //   6012: istore #6
    //   6014: aload #27
    //   6016: astore_3
    //   6017: aload #24
    //   6019: astore_2
    //   6020: aload #25
    //   6022: astore #4
    //   6024: aload #17
    //   6026: astore #9
    //   6028: goto -> 338
    //   6031: aload_2
    //   6032: astore #17
    //   6034: aload_1
    //   6035: astore #25
    //   6037: aload #17
    //   6039: aload #25
    //   6041: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   6044: aload #25
    //   6046: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   6049: iload #8
    //   6051: invokestatic initFunction : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/InterpretedFunction;I)V
    //   6054: aload #9
    //   6056: astore_2
    //   6057: aload #17
    //   6059: astore #9
    //   6061: aload_2
    //   6062: astore #17
    //   6064: goto -> 10792
    //   6067: aload_1
    //   6068: astore #10
    //   6070: astore_1
    //   6071: aload_3
    //   6072: astore #7
    //   6074: iconst_0
    //   6075: istore #8
    //   6077: aload_2
    //   6078: astore_3
    //   6079: aload #5
    //   6081: astore_2
    //   6082: aload #4
    //   6084: astore #5
    //   6086: aload #10
    //   6088: astore #4
    //   6090: aload #7
    //   6092: astore #10
    //   6094: aload #15
    //   6096: astore #14
    //   6098: goto -> 10984
    //   6101: iload #6
    //   6103: istore #28
    //   6105: aload_2
    //   6106: astore #17
    //   6108: aload_1
    //   6109: astore #25
    //   6111: iload #28
    //   6113: ifeq -> 6132
    //   6116: aload #17
    //   6118: aload #17
    //   6120: getfield instructionCount : I
    //   6123: bipush #100
    //   6125: iadd
    //   6126: putfield instructionCount : I
    //   6129: goto -> 6132
    //   6132: aload_0
    //   6133: aload #25
    //   6135: aload #10
    //   6137: aload #7
    //   6139: iload #21
    //   6141: aload #14
    //   6143: iload #8
    //   6145: invokestatic doCallSpecial : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI[BI)I
    //   6148: istore #21
    //   6150: aload #25
    //   6152: astore_1
    //   6153: iload #28
    //   6155: istore #6
    //   6157: aload #17
    //   6159: astore_2
    //   6160: goto -> 338
    //   6163: aload_3
    //   6164: astore #7
    //   6166: aload_1
    //   6167: astore #10
    //   6169: aload #10
    //   6171: aload #7
    //   6173: putfield result : Ljava/lang/Object;
    //   6176: goto -> 10474
    //   6179: aload_2
    //   6180: astore #17
    //   6182: iinc #21, 1
    //   6185: aload #10
    //   6187: iload #21
    //   6189: aload #4
    //   6191: aastore
    //   6192: aload #7
    //   6194: iload #21
    //   6196: aload_1
    //   6197: getfield pc : I
    //   6200: iconst_2
    //   6201: iadd
    //   6202: i2d
    //   6203: dastore
    //   6204: aload #17
    //   6206: astore_2
    //   6207: goto -> 7879
    //   6210: aload_1
    //   6211: astore #10
    //   6213: astore_1
    //   6214: aload_3
    //   6215: astore #7
    //   6217: iconst_0
    //   6218: istore #8
    //   6220: aload_2
    //   6221: astore_3
    //   6222: aload #5
    //   6224: astore_2
    //   6225: aload #4
    //   6227: astore #5
    //   6229: aload #10
    //   6231: astore #4
    //   6233: aload #7
    //   6235: astore #10
    //   6237: aload #15
    //   6239: astore #14
    //   6241: goto -> 10984
    //   6244: aload #5
    //   6246: astore #24
    //   6248: aload #9
    //   6250: astore #17
    //   6252: iload #6
    //   6254: istore #28
    //   6256: aload_3
    //   6257: astore #26
    //   6259: aload #4
    //   6261: astore #27
    //   6263: iload #21
    //   6265: istore #22
    //   6267: aload #10
    //   6269: astore #30
    //   6271: aload_1
    //   6272: astore #25
    //   6274: iload #22
    //   6276: aload #25
    //   6278: getfield emptyStackTop : I
    //   6281: iconst_1
    //   6282: iadd
    //   6283: if_icmpne -> 6355
    //   6286: aload #25
    //   6288: getfield localShift : I
    //   6291: istore #21
    //   6293: iload #8
    //   6295: iload #21
    //   6297: iadd
    //   6298: istore #8
    //   6300: aload #30
    //   6302: iload #8
    //   6304: aload #30
    //   6306: iload #22
    //   6308: aaload
    //   6309: aastore
    //   6310: aload #7
    //   6312: iload #8
    //   6314: aload #7
    //   6316: iload #22
    //   6318: daload
    //   6319: dastore
    //   6320: iload #22
    //   6322: iconst_1
    //   6323: isub
    //   6324: istore #21
    //   6326: aload #25
    //   6328: astore_1
    //   6329: aload #26
    //   6331: astore_3
    //   6332: aload #30
    //   6334: astore #10
    //   6336: aload #24
    //   6338: astore #5
    //   6340: iload #28
    //   6342: istore #6
    //   6344: aload #27
    //   6346: astore #4
    //   6348: aload #17
    //   6350: astore #9
    //   6352: goto -> 338
    //   6355: aload_2
    //   6356: astore #24
    //   6358: iload #22
    //   6360: aload #25
    //   6362: getfield emptyStackTop : I
    //   6365: if_icmpeq -> 6375
    //   6368: aload_2
    //   6369: astore #24
    //   6371: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   6374: pop
    //   6375: aload_2
    //   6376: astore #9
    //   6378: goto -> 10792
    //   6381: astore_1
    //   6382: aload #27
    //   6384: astore #5
    //   6386: aload #26
    //   6388: astore #10
    //   6390: iconst_0
    //   6391: istore #8
    //   6393: aload_2
    //   6394: astore_3
    //   6395: aload #24
    //   6397: astore_2
    //   6398: aload #17
    //   6400: astore #9
    //   6402: aload #25
    //   6404: astore #4
    //   6406: aload #15
    //   6408: astore #14
    //   6410: iload #28
    //   6412: istore #6
    //   6414: goto -> 10984
    //   6417: aload #5
    //   6419: astore #25
    //   6421: aload #9
    //   6423: astore #27
    //   6425: iload #6
    //   6427: istore #28
    //   6429: aload_3
    //   6430: astore #30
    //   6432: aload #4
    //   6434: astore #26
    //   6436: aload_2
    //   6437: astore #17
    //   6439: aload_1
    //   6440: astore #32
    //   6442: iload #28
    //   6444: ifeq -> 6459
    //   6447: aload #17
    //   6449: astore #24
    //   6451: aload #17
    //   6453: aload #32
    //   6455: iconst_0
    //   6456: invokestatic addInstructionCount : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;I)V
    //   6459: aload #17
    //   6461: astore #24
    //   6463: aload #32
    //   6465: getfield localShift : I
    //   6468: istore #22
    //   6470: iload #8
    //   6472: iload #22
    //   6474: iadd
    //   6475: istore #8
    //   6477: aload #10
    //   6479: iload #8
    //   6481: aaload
    //   6482: astore #24
    //   6484: aload #24
    //   6486: aload #26
    //   6488: if_acmpeq -> 6527
    //   6491: aload #24
    //   6493: astore #4
    //   6495: aload #26
    //   6497: astore #5
    //   6499: aload #30
    //   6501: astore #10
    //   6503: iconst_0
    //   6504: istore #21
    //   6506: aload #17
    //   6508: astore_1
    //   6509: aload #11
    //   6511: astore_2
    //   6512: aload #27
    //   6514: astore #9
    //   6516: aload #32
    //   6518: astore #7
    //   6520: iload #28
    //   6522: istore #6
    //   6524: goto -> 11008
    //   6527: aload #32
    //   6529: aload #7
    //   6531: iload #8
    //   6533: daload
    //   6534: d2i
    //   6535: putfield pc : I
    //   6538: iload #28
    //   6540: ifeq -> 6553
    //   6543: aload #32
    //   6545: aload #32
    //   6547: getfield pc : I
    //   6550: putfield pcPrevBranch : I
    //   6553: aload #32
    //   6555: astore_1
    //   6556: aload #25
    //   6558: astore #5
    //   6560: iload #28
    //   6562: istore #6
    //   6564: aload #30
    //   6566: astore_3
    //   6567: aload #17
    //   6569: astore_2
    //   6570: aload #26
    //   6572: astore #4
    //   6574: aload #27
    //   6576: astore #9
    //   6578: goto -> 338
    //   6581: aload_1
    //   6582: astore #17
    //   6584: aload_2
    //   6585: astore #24
    //   6587: aload #17
    //   6589: aload #17
    //   6591: getfield pc : I
    //   6594: putfield pcSourceLineStart : I
    //   6597: aload_2
    //   6598: astore #24
    //   6600: aload #17
    //   6602: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   6605: ifnull -> 6639
    //   6608: aload_2
    //   6609: astore #24
    //   6611: aload #14
    //   6613: aload #17
    //   6615: getfield pc : I
    //   6618: invokestatic getIndex : ([BI)I
    //   6621: istore #22
    //   6623: aload_2
    //   6624: astore #24
    //   6626: aload #17
    //   6628: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   6631: aload_2
    //   6632: iload #22
    //   6634: invokeinterface onLineChange : (Lcom/trendmicro/hippo/Context;I)V
    //   6639: aload_2
    //   6640: astore #24
    //   6642: aload #17
    //   6644: aload #17
    //   6646: getfield pc : I
    //   6649: iconst_2
    //   6650: iadd
    //   6651: putfield pc : I
    //   6654: aload #9
    //   6656: astore #17
    //   6658: aload_2
    //   6659: astore #9
    //   6661: goto -> 10792
    //   6664: aload #4
    //   6666: astore #17
    //   6668: aload_1
    //   6669: astore #25
    //   6671: iinc #21, 1
    //   6674: aload #10
    //   6676: iload #21
    //   6678: aload #17
    //   6680: aastore
    //   6681: aload_2
    //   6682: astore #24
    //   6684: aload #7
    //   6686: iload #21
    //   6688: aload #14
    //   6690: aload #25
    //   6692: getfield pc : I
    //   6695: invokestatic getShort : ([BI)I
    //   6698: i2d
    //   6699: dastore
    //   6700: aload_2
    //   6701: astore #24
    //   6703: aload #25
    //   6705: aload #25
    //   6707: getfield pc : I
    //   6710: iconst_2
    //   6711: iadd
    //   6712: putfield pc : I
    //   6715: aload #25
    //   6717: astore_1
    //   6718: aload #17
    //   6720: astore #4
    //   6722: goto -> 338
    //   6725: aload #4
    //   6727: astore #17
    //   6729: aload_1
    //   6730: astore #25
    //   6732: iinc #21, 1
    //   6735: aload #10
    //   6737: iload #21
    //   6739: aload #17
    //   6741: aastore
    //   6742: aload_2
    //   6743: astore #24
    //   6745: aload #7
    //   6747: iload #21
    //   6749: aload #14
    //   6751: aload #25
    //   6753: getfield pc : I
    //   6756: invokestatic getInt : ([BI)I
    //   6759: i2d
    //   6760: dastore
    //   6761: aload_2
    //   6762: astore #24
    //   6764: aload #25
    //   6766: aload #25
    //   6768: getfield pc : I
    //   6771: iconst_4
    //   6772: iadd
    //   6773: putfield pc : I
    //   6776: aload #25
    //   6778: astore_1
    //   6779: aload #17
    //   6781: astore #4
    //   6783: goto -> 338
    //   6786: iinc #21, 1
    //   6789: aload_2
    //   6790: astore #24
    //   6792: aload #10
    //   6794: iload #21
    //   6796: iload #8
    //   6798: newarray int
    //   6800: aastore
    //   6801: iinc #21, 1
    //   6804: aload_2
    //   6805: astore #24
    //   6807: aload #10
    //   6809: iload #21
    //   6811: iload #8
    //   6813: anewarray java/lang/Object
    //   6816: aastore
    //   6817: aload #7
    //   6819: iload #21
    //   6821: dconst_0
    //   6822: dastore
    //   6823: goto -> 338
    //   6826: aload #4
    //   6828: astore #17
    //   6830: aload #10
    //   6832: iload #21
    //   6834: aaload
    //   6835: astore #24
    //   6837: aload #24
    //   6839: astore #25
    //   6841: aload #24
    //   6843: aload #17
    //   6845: if_acmpne -> 6861
    //   6848: aload_2
    //   6849: astore #24
    //   6851: aload #7
    //   6853: iload #21
    //   6855: daload
    //   6856: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   6859: astore #25
    //   6861: iinc #21, -1
    //   6864: aload #7
    //   6866: iload #21
    //   6868: daload
    //   6869: d2i
    //   6870: istore #22
    //   6872: aload_2
    //   6873: astore #24
    //   6875: aload #10
    //   6877: iload #21
    //   6879: aaload
    //   6880: checkcast [Ljava/lang/Object;
    //   6883: iload #22
    //   6885: aload #25
    //   6887: aastore
    //   6888: aload #7
    //   6890: iload #21
    //   6892: iload #22
    //   6894: iconst_1
    //   6895: iadd
    //   6896: i2d
    //   6897: dastore
    //   6898: aload #17
    //   6900: astore #4
    //   6902: goto -> 338
    //   6905: aload_1
    //   6906: astore #17
    //   6908: aload_2
    //   6909: astore #24
    //   6911: aload #10
    //   6913: iload #21
    //   6915: aaload
    //   6916: checkcast [Ljava/lang/Object;
    //   6919: astore #27
    //   6921: iinc #21, -1
    //   6924: aload_2
    //   6925: astore #24
    //   6927: aload #10
    //   6929: iload #21
    //   6931: aaload
    //   6932: checkcast [I
    //   6935: astore #25
    //   6937: iload #23
    //   6939: bipush #67
    //   6941: if_icmpne -> 6982
    //   6944: aload_2
    //   6945: astore #24
    //   6947: aload #17
    //   6949: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   6952: getfield literalIds : [Ljava/lang/Object;
    //   6955: iload #8
    //   6957: aaload
    //   6958: checkcast [Ljava/lang/Object;
    //   6961: aload #27
    //   6963: aload #25
    //   6965: aload_2
    //   6966: aload #17
    //   6968: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   6971: invokestatic newObjectLiteral : ([Ljava/lang/Object;[Ljava/lang/Object;[ILcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   6974: astore #25
    //   6976: aload #25
    //   6978: astore_1
    //   6979: goto -> 7032
    //   6982: aconst_null
    //   6983: astore #25
    //   6985: iload #23
    //   6987: bipush #-31
    //   6989: if_icmpne -> 7011
    //   6992: aload_2
    //   6993: astore #24
    //   6995: aload #17
    //   6997: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   7000: getfield literalIds : [Ljava/lang/Object;
    //   7003: iload #8
    //   7005: aaload
    //   7006: checkcast [I
    //   7009: astore #25
    //   7011: aload_2
    //   7012: astore #24
    //   7014: aload #27
    //   7016: aload #25
    //   7018: aload_2
    //   7019: aload #17
    //   7021: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   7024: invokestatic newArrayLiteral : ([Ljava/lang/Object;[ILcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   7027: astore #25
    //   7029: aload #25
    //   7031: astore_1
    //   7032: aload #10
    //   7034: iload #21
    //   7036: aload_1
    //   7037: aastore
    //   7038: aload #17
    //   7040: astore_1
    //   7041: goto -> 338
    //   7044: iconst_0
    //   7045: istore #8
    //   7047: goto -> 338
    //   7050: iconst_1
    //   7051: istore #8
    //   7053: goto -> 338
    //   7056: iconst_2
    //   7057: istore #8
    //   7059: goto -> 338
    //   7062: iconst_3
    //   7063: istore #8
    //   7065: goto -> 338
    //   7068: iconst_4
    //   7069: istore #8
    //   7071: goto -> 338
    //   7074: iconst_5
    //   7075: istore #8
    //   7077: goto -> 338
    //   7080: aload_2
    //   7081: astore #17
    //   7083: aload_1
    //   7084: astore #25
    //   7086: aload #17
    //   7088: astore #24
    //   7090: aload #14
    //   7092: aload #25
    //   7094: getfield pc : I
    //   7097: baload
    //   7098: istore #8
    //   7100: iload #8
    //   7102: sipush #255
    //   7105: iand
    //   7106: istore #8
    //   7108: aload #25
    //   7110: aload #25
    //   7112: getfield pc : I
    //   7115: iconst_1
    //   7116: iadd
    //   7117: putfield pc : I
    //   7120: aload #25
    //   7122: astore_1
    //   7123: aload #17
    //   7125: astore_2
    //   7126: goto -> 338
    //   7129: aload_2
    //   7130: astore #17
    //   7132: aload_1
    //   7133: astore #25
    //   7135: aload #17
    //   7137: astore #24
    //   7139: aload #14
    //   7141: aload #25
    //   7143: getfield pc : I
    //   7146: invokestatic getIndex : ([BI)I
    //   7149: istore #8
    //   7151: aload #25
    //   7153: aload #25
    //   7155: getfield pc : I
    //   7158: iconst_2
    //   7159: iadd
    //   7160: putfield pc : I
    //   7163: aload #25
    //   7165: astore_1
    //   7166: aload #17
    //   7168: astore_2
    //   7169: goto -> 338
    //   7172: aload_2
    //   7173: astore #17
    //   7175: aload_1
    //   7176: astore #25
    //   7178: aload #17
    //   7180: astore #24
    //   7182: aload #14
    //   7184: aload #25
    //   7186: getfield pc : I
    //   7189: invokestatic getInt : ([BI)I
    //   7192: istore #8
    //   7194: aload #25
    //   7196: aload #25
    //   7198: getfield pc : I
    //   7201: iconst_4
    //   7202: iadd
    //   7203: putfield pc : I
    //   7206: aload #25
    //   7208: astore_1
    //   7209: aload #17
    //   7211: astore_2
    //   7212: goto -> 338
    //   7215: aload #20
    //   7217: iconst_0
    //   7218: aaload
    //   7219: astore #5
    //   7221: goto -> 338
    //   7224: aload #20
    //   7226: iconst_1
    //   7227: aaload
    //   7228: astore #5
    //   7230: goto -> 338
    //   7233: aload #20
    //   7235: iconst_2
    //   7236: aaload
    //   7237: astore #5
    //   7239: goto -> 338
    //   7242: aload #20
    //   7244: iconst_3
    //   7245: aaload
    //   7246: astore #5
    //   7248: goto -> 338
    //   7251: aload_2
    //   7252: astore #17
    //   7254: aload_1
    //   7255: astore #27
    //   7257: aload #17
    //   7259: astore #24
    //   7261: aload #20
    //   7263: aload #14
    //   7265: aload #27
    //   7267: getfield pc : I
    //   7270: baload
    //   7271: sipush #255
    //   7274: iand
    //   7275: aaload
    //   7276: astore #25
    //   7278: aload #25
    //   7280: astore #5
    //   7282: aload #27
    //   7284: aload #27
    //   7286: getfield pc : I
    //   7289: iconst_1
    //   7290: iadd
    //   7291: putfield pc : I
    //   7294: aload #27
    //   7296: astore_1
    //   7297: aload #17
    //   7299: astore_2
    //   7300: aload #25
    //   7302: astore #5
    //   7304: goto -> 338
    //   7307: aload_2
    //   7308: astore #17
    //   7310: aload_1
    //   7311: astore #27
    //   7313: aload #17
    //   7315: astore #24
    //   7317: aload #20
    //   7319: aload #14
    //   7321: aload #27
    //   7323: getfield pc : I
    //   7326: invokestatic getIndex : ([BI)I
    //   7329: aaload
    //   7330: astore #25
    //   7332: aload #25
    //   7334: astore #5
    //   7336: aload #27
    //   7338: aload #27
    //   7340: getfield pc : I
    //   7343: iconst_2
    //   7344: iadd
    //   7345: putfield pc : I
    //   7348: aload #27
    //   7350: astore_1
    //   7351: aload #17
    //   7353: astore_2
    //   7354: aload #25
    //   7356: astore #5
    //   7358: goto -> 338
    //   7361: aload_1
    //   7362: astore #10
    //   7364: astore_1
    //   7365: aload #5
    //   7367: astore #14
    //   7369: aload #4
    //   7371: astore #5
    //   7373: aload_3
    //   7374: astore #7
    //   7376: iconst_0
    //   7377: istore #8
    //   7379: aload_2
    //   7380: astore_3
    //   7381: aload #14
    //   7383: astore_2
    //   7384: aload #10
    //   7386: astore #4
    //   7388: aload #7
    //   7390: astore #10
    //   7392: aload #15
    //   7394: astore #14
    //   7396: goto -> 10984
    //   7399: aload #9
    //   7401: astore #17
    //   7403: iload #6
    //   7405: istore #28
    //   7407: aload #4
    //   7409: astore #26
    //   7411: aload_1
    //   7412: astore #25
    //   7414: aload_3
    //   7415: astore #27
    //   7417: aload_2
    //   7418: astore #24
    //   7420: aload #20
    //   7422: aload #14
    //   7424: aload #25
    //   7426: getfield pc : I
    //   7429: invokestatic getInt : ([BI)I
    //   7432: aaload
    //   7433: astore #30
    //   7435: aload #25
    //   7437: aload #25
    //   7439: getfield pc : I
    //   7442: iconst_4
    //   7443: iadd
    //   7444: putfield pc : I
    //   7447: aload #25
    //   7449: astore_1
    //   7450: iload #28
    //   7452: istore #6
    //   7454: aload #30
    //   7456: astore #5
    //   7458: aload #26
    //   7460: astore #4
    //   7462: aload #27
    //   7464: astore_3
    //   7465: aload #17
    //   7467: astore #9
    //   7469: goto -> 338
    //   7472: astore_1
    //   7473: aload #26
    //   7475: astore #5
    //   7477: aload #27
    //   7479: astore #10
    //   7481: iconst_0
    //   7482: istore #8
    //   7484: aload #17
    //   7486: astore #9
    //   7488: aload_2
    //   7489: astore_3
    //   7490: aload #30
    //   7492: astore_2
    //   7493: aload #25
    //   7495: astore #4
    //   7497: aload #15
    //   7499: astore #14
    //   7501: iload #28
    //   7503: istore #6
    //   7505: goto -> 10984
    //   7508: aload_2
    //   7509: astore #17
    //   7511: aload_1
    //   7512: astore #25
    //   7514: aload #17
    //   7516: astore #24
    //   7518: aload #25
    //   7520: getfield pc : I
    //   7523: istore #8
    //   7525: aload #17
    //   7527: astore #24
    //   7529: aload #25
    //   7531: iload #8
    //   7533: iconst_1
    //   7534: iadd
    //   7535: putfield pc : I
    //   7538: aload #14
    //   7540: iload #8
    //   7542: baload
    //   7543: istore #8
    //   7545: aload_1
    //   7546: astore #17
    //   7548: aload #17
    //   7550: aload #10
    //   7552: aload #7
    //   7554: iload #21
    //   7556: aload #16
    //   7558: aload #18
    //   7560: iload #8
    //   7562: invokestatic doGetVar : (Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[DI)I
    //   7565: istore #21
    //   7567: aload #17
    //   7569: astore_1
    //   7570: goto -> 338
    //   7573: aload_1
    //   7574: astore #10
    //   7576: astore_1
    //   7577: aload_3
    //   7578: astore #7
    //   7580: iconst_0
    //   7581: istore #8
    //   7583: aload_2
    //   7584: astore_3
    //   7585: aload #5
    //   7587: astore_2
    //   7588: aload #4
    //   7590: astore #5
    //   7592: aload #10
    //   7594: astore #4
    //   7596: aload #7
    //   7598: astore #10
    //   7600: aload #15
    //   7602: astore #14
    //   7604: goto -> 10984
    //   7607: aload_2
    //   7608: astore #17
    //   7610: aload_1
    //   7611: astore #25
    //   7613: aload #17
    //   7615: astore #24
    //   7617: aload #25
    //   7619: getfield pc : I
    //   7622: istore #8
    //   7624: aload #17
    //   7626: astore #24
    //   7628: aload #25
    //   7630: iload #8
    //   7632: iconst_1
    //   7633: iadd
    //   7634: putfield pc : I
    //   7637: aload #14
    //   7639: iload #8
    //   7641: baload
    //   7642: istore #8
    //   7644: aload_1
    //   7645: astore #17
    //   7647: aload #17
    //   7649: aload #10
    //   7651: aload #7
    //   7653: iload #21
    //   7655: aload #16
    //   7657: aload #18
    //   7659: aload #19
    //   7661: iload #8
    //   7663: invokestatic doSetVar : (Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[D[II)I
    //   7666: istore #21
    //   7668: aload #17
    //   7670: astore_1
    //   7671: goto -> 338
    //   7674: iinc #21, 1
    //   7677: aload #10
    //   7679: iload #21
    //   7681: aload_3
    //   7682: aastore
    //   7683: goto -> 338
    //   7686: iinc #21, 1
    //   7689: aload #10
    //   7691: iload #21
    //   7693: aload #4
    //   7695: aastore
    //   7696: aload #7
    //   7698: iload #21
    //   7700: dconst_0
    //   7701: dastore
    //   7702: goto -> 338
    //   7705: iinc #21, 1
    //   7708: aload #10
    //   7710: iload #21
    //   7712: aload #4
    //   7714: aastore
    //   7715: aload #7
    //   7717: iload #21
    //   7719: dconst_1
    //   7720: dastore
    //   7721: goto -> 338
    //   7724: aload #4
    //   7726: astore #17
    //   7728: aload_1
    //   7729: astore #25
    //   7731: aload #10
    //   7733: iload #21
    //   7735: aaload
    //   7736: astore #24
    //   7738: aload #24
    //   7740: astore #27
    //   7742: aload #24
    //   7744: aload #17
    //   7746: if_acmpne -> 7762
    //   7749: aload_2
    //   7750: astore #24
    //   7752: aload #7
    //   7754: iload #21
    //   7756: daload
    //   7757: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   7760: astore #27
    //   7762: iinc #21, -1
    //   7765: aload_2
    //   7766: astore #24
    //   7768: aload #25
    //   7770: aload #27
    //   7772: aload #25
    //   7774: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   7777: invokestatic enterDotQuery : (Ljava/lang/Object;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   7780: putfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   7783: aload #25
    //   7785: astore_1
    //   7786: aload #17
    //   7788: astore #4
    //   7790: goto -> 338
    //   7793: iload #21
    //   7795: istore #22
    //   7797: aload_1
    //   7798: astore #17
    //   7800: aload_2
    //   7801: astore #24
    //   7803: aload #17
    //   7805: iload #22
    //   7807: invokestatic stack_boolean : (Lcom/trendmicro/hippo/Interpreter$CallFrame;I)Z
    //   7810: aload #17
    //   7812: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   7815: invokestatic updateDotQuery : (ZLcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   7818: astore #25
    //   7820: aload #25
    //   7822: ifnull -> 7873
    //   7825: aload #10
    //   7827: iload #22
    //   7829: aload #25
    //   7831: aastore
    //   7832: aload_2
    //   7833: astore #24
    //   7835: aload #17
    //   7837: aload #17
    //   7839: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   7842: invokestatic leaveDotQuery : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   7845: putfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   7848: aload_2
    //   7849: astore #24
    //   7851: aload #17
    //   7853: aload #17
    //   7855: getfield pc : I
    //   7858: iconst_2
    //   7859: iadd
    //   7860: putfield pc : I
    //   7863: aload #9
    //   7865: astore #17
    //   7867: aload_2
    //   7868: astore #9
    //   7870: goto -> 10792
    //   7873: iload #22
    //   7875: iconst_1
    //   7876: isub
    //   7877: istore #21
    //   7879: iload #6
    //   7881: istore #28
    //   7883: aload_1
    //   7884: astore #17
    //   7886: iload #28
    //   7888: ifeq -> 7904
    //   7891: aload_2
    //   7892: astore #24
    //   7894: aload_2
    //   7895: aload #17
    //   7897: iconst_2
    //   7898: invokestatic addInstructionCount : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;I)V
    //   7901: goto -> 7904
    //   7904: aload_2
    //   7905: astore #24
    //   7907: aload #14
    //   7909: aload #17
    //   7911: getfield pc : I
    //   7914: invokestatic getShort : ([BI)I
    //   7917: istore #22
    //   7919: iload #22
    //   7921: ifeq -> 7945
    //   7924: aload_2
    //   7925: astore #24
    //   7927: aload #17
    //   7929: aload #17
    //   7931: getfield pc : I
    //   7934: iload #22
    //   7936: iconst_1
    //   7937: isub
    //   7938: iadd
    //   7939: putfield pc : I
    //   7942: goto -> 7969
    //   7945: aload_2
    //   7946: astore #24
    //   7948: aload #17
    //   7950: aload #17
    //   7952: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   7955: getfield longJumps : Lcom/trendmicro/hippo/UintMap;
    //   7958: aload #17
    //   7960: getfield pc : I
    //   7963: invokevirtual getExistingInt : (I)I
    //   7966: putfield pc : I
    //   7969: iload #28
    //   7971: ifeq -> 7987
    //   7974: aload_2
    //   7975: astore #24
    //   7977: aload #17
    //   7979: aload #17
    //   7981: getfield pc : I
    //   7984: putfield pcPrevBranch : I
    //   7987: aload #17
    //   7989: astore_1
    //   7990: iload #28
    //   7992: istore #6
    //   7994: goto -> 338
    //   7997: aload_1
    //   7998: astore #10
    //   8000: astore_1
    //   8001: aload_3
    //   8002: astore #7
    //   8004: iconst_0
    //   8005: istore #8
    //   8007: aload #24
    //   8009: astore_3
    //   8010: aload #5
    //   8012: astore_2
    //   8013: aload #4
    //   8015: astore #5
    //   8017: aload #10
    //   8019: astore #4
    //   8021: aload #7
    //   8023: astore #10
    //   8025: aload #15
    //   8027: astore #14
    //   8029: goto -> 10984
    //   8032: iload #6
    //   8034: istore #28
    //   8036: aload #9
    //   8038: astore #17
    //   8040: aload #5
    //   8042: astore #25
    //   8044: aload #10
    //   8046: astore #30
    //   8048: aload_2
    //   8049: astore #26
    //   8051: aload_3
    //   8052: astore_2
    //   8053: aload_1
    //   8054: astore #27
    //   8056: aload #4
    //   8058: astore #10
    //   8060: iload #28
    //   8062: ifeq -> 8082
    //   8065: aload #26
    //   8067: astore #24
    //   8069: aload #26
    //   8071: aload #26
    //   8073: getfield instructionCount : I
    //   8076: bipush #100
    //   8078: iadd
    //   8079: putfield instructionCount : I
    //   8082: iload #21
    //   8084: iload #8
    //   8086: iconst_1
    //   8087: iadd
    //   8088: isub
    //   8089: istore #22
    //   8091: aload #30
    //   8093: iload #22
    //   8095: aaload
    //   8096: checkcast com/trendmicro/hippo/Callable
    //   8099: astore #38
    //   8101: aload #30
    //   8103: iload #22
    //   8105: iconst_1
    //   8106: iadd
    //   8107: aaload
    //   8108: checkcast com/trendmicro/hippo/Scriptable
    //   8111: astore #37
    //   8113: iload #23
    //   8115: bipush #71
    //   8117: if_icmpne -> 8161
    //   8120: aload #26
    //   8122: astore #24
    //   8124: aload #30
    //   8126: iload #22
    //   8128: aload #38
    //   8130: aload #37
    //   8132: aload #30
    //   8134: aload #7
    //   8136: iload #22
    //   8138: iconst_2
    //   8139: iadd
    //   8140: iload #8
    //   8142: invokestatic getArgsArray : ([Ljava/lang/Object;[DII)[Ljava/lang/Object;
    //   8145: aload #26
    //   8147: invokestatic callRef : (Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Ref;
    //   8150: aastore
    //   8151: aload #7
    //   8153: astore_1
    //   8154: aload #26
    //   8156: astore #7
    //   8158: goto -> 9266
    //   8161: aload #27
    //   8163: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   8166: astore_1
    //   8167: aload #27
    //   8169: getfield useActivation : Z
    //   8172: istore #29
    //   8174: iload #29
    //   8176: ifeq -> 8227
    //   8179: aload #27
    //   8181: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   8184: invokestatic getTopLevelScope : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   8187: astore_1
    //   8188: goto -> 8227
    //   8191: astore_1
    //   8192: aload #10
    //   8194: astore #5
    //   8196: aload_2
    //   8197: astore #10
    //   8199: aload #26
    //   8201: astore_3
    //   8202: aload #25
    //   8204: astore_2
    //   8205: aload #17
    //   8207: astore #9
    //   8209: iconst_0
    //   8210: istore #8
    //   8212: aload #27
    //   8214: astore #4
    //   8216: aload #15
    //   8218: astore #14
    //   8220: iload #28
    //   8222: istore #6
    //   8224: goto -> 10984
    //   8227: aload #38
    //   8229: instanceof com/trendmicro/hippo/InterpretedFunction
    //   8232: istore #29
    //   8234: iload #29
    //   8236: ifeq -> 8447
    //   8239: aload #38
    //   8241: checkcast com/trendmicro/hippo/InterpretedFunction
    //   8244: astore_3
    //   8245: aload #27
    //   8247: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   8250: getfield securityDomain : Ljava/lang/Object;
    //   8253: astore #4
    //   8255: aload_3
    //   8256: getfield securityDomain : Ljava/lang/Object;
    //   8259: astore #24
    //   8261: aload #4
    //   8263: aload #24
    //   8265: if_acmpne -> 8409
    //   8268: iload #23
    //   8270: bipush #-55
    //   8272: if_icmpne -> 8293
    //   8275: aload #27
    //   8277: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   8280: astore #9
    //   8282: aload #26
    //   8284: aload #27
    //   8286: aconst_null
    //   8287: invokestatic exitFrame : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;Ljava/lang/Object;)V
    //   8290: goto -> 8297
    //   8293: aload #27
    //   8295: astore #9
    //   8297: aload_0
    //   8298: aload_1
    //   8299: aload #37
    //   8301: aload #30
    //   8303: aload #7
    //   8305: iload #22
    //   8307: iconst_2
    //   8308: iadd
    //   8309: iload #8
    //   8311: aload_3
    //   8312: aload #9
    //   8314: invokestatic initFrame : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;[DIILcom/trendmicro/hippo/InterpretedFunction;Lcom/trendmicro/hippo/Interpreter$CallFrame;)Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   8317: astore #5
    //   8319: iload #23
    //   8321: bipush #-55
    //   8323: if_icmpeq -> 8340
    //   8326: aload #27
    //   8328: iload #22
    //   8330: putfield savedStackTop : I
    //   8333: aload #27
    //   8335: iload #23
    //   8337: putfield savedCallOp : I
    //   8340: aload_0
    //   8341: astore #14
    //   8343: aload #15
    //   8345: astore_1
    //   8346: aload #25
    //   8348: astore #7
    //   8350: aload #17
    //   8352: astore #9
    //   8354: iload #28
    //   8356: istore #6
    //   8358: aload #10
    //   8360: astore #4
    //   8362: aload_2
    //   8363: astore_3
    //   8364: aload #14
    //   8366: astore_2
    //   8367: goto -> 139
    //   8370: astore_1
    //   8371: aload_0
    //   8372: astore_3
    //   8373: aload_2
    //   8374: astore #7
    //   8376: aload #27
    //   8378: astore #4
    //   8380: aload #25
    //   8382: astore_2
    //   8383: aload #17
    //   8385: astore #9
    //   8387: aload #10
    //   8389: astore #5
    //   8391: iconst_0
    //   8392: istore #8
    //   8394: aload #7
    //   8396: astore #10
    //   8398: aload #15
    //   8400: astore #14
    //   8402: iload #28
    //   8404: istore #6
    //   8406: goto -> 10984
    //   8409: goto -> 8447
    //   8412: astore_1
    //   8413: aload_0
    //   8414: astore_3
    //   8415: aload #10
    //   8417: astore #5
    //   8419: aload_2
    //   8420: astore #10
    //   8422: aload #25
    //   8424: astore_2
    //   8425: aload #17
    //   8427: astore #9
    //   8429: iconst_0
    //   8430: istore #8
    //   8432: aload #27
    //   8434: astore #4
    //   8436: aload #15
    //   8438: astore #14
    //   8440: iload #28
    //   8442: istore #6
    //   8444: goto -> 10984
    //   8447: aload_1
    //   8448: astore #32
    //   8450: aload #10
    //   8452: astore_1
    //   8453: aload #27
    //   8455: astore_3
    //   8456: aload #38
    //   8458: instanceof com/trendmicro/hippo/NativeContinuation
    //   8461: istore #29
    //   8463: iload #29
    //   8465: ifeq -> 8644
    //   8468: new com/trendmicro/hippo/Interpreter$ContinuationJump
    //   8471: astore #4
    //   8473: aload #4
    //   8475: aload #38
    //   8477: checkcast com/trendmicro/hippo/NativeContinuation
    //   8480: aload_3
    //   8481: invokespecial <init> : (Lcom/trendmicro/hippo/NativeContinuation;Lcom/trendmicro/hippo/Interpreter$CallFrame;)V
    //   8484: iload #8
    //   8486: ifne -> 8498
    //   8489: aload #4
    //   8491: aload_2
    //   8492: putfield result : Ljava/lang/Object;
    //   8495: goto -> 8522
    //   8498: aload #4
    //   8500: aload #30
    //   8502: iload #22
    //   8504: iconst_2
    //   8505: iadd
    //   8506: aaload
    //   8507: putfield result : Ljava/lang/Object;
    //   8510: aload #4
    //   8512: aload #7
    //   8514: iload #22
    //   8516: iconst_2
    //   8517: iadd
    //   8518: daload
    //   8519: putfield resultDbl : D
    //   8522: aload_0
    //   8523: astore #9
    //   8525: aload_2
    //   8526: astore #10
    //   8528: aload #11
    //   8530: astore_2
    //   8531: aload_1
    //   8532: astore #5
    //   8534: iconst_0
    //   8535: istore #21
    //   8537: aload #9
    //   8539: astore_1
    //   8540: aload #17
    //   8542: astore #9
    //   8544: aload_3
    //   8545: astore #7
    //   8547: iload #28
    //   8549: istore #6
    //   8551: goto -> 11008
    //   8554: astore #5
    //   8556: aload_0
    //   8557: astore #9
    //   8559: aload_2
    //   8560: astore #10
    //   8562: aload_3
    //   8563: astore #4
    //   8565: aload #25
    //   8567: astore_2
    //   8568: aload_1
    //   8569: astore #7
    //   8571: iconst_0
    //   8572: istore #8
    //   8574: aload #9
    //   8576: astore_3
    //   8577: aload #17
    //   8579: astore #9
    //   8581: aload #5
    //   8583: astore_1
    //   8584: aload #7
    //   8586: astore #5
    //   8588: aload #15
    //   8590: astore #14
    //   8592: iload #28
    //   8594: istore #6
    //   8596: goto -> 10984
    //   8599: astore #5
    //   8601: aload_0
    //   8602: astore #9
    //   8604: aload_2
    //   8605: astore #10
    //   8607: aload_3
    //   8608: astore #4
    //   8610: aload #25
    //   8612: astore_2
    //   8613: aload_1
    //   8614: astore #7
    //   8616: iconst_0
    //   8617: istore #8
    //   8619: aload #9
    //   8621: astore_3
    //   8622: aload #17
    //   8624: astore #9
    //   8626: aload #5
    //   8628: astore_1
    //   8629: aload #7
    //   8631: astore #5
    //   8633: aload #15
    //   8635: astore #14
    //   8637: iload #28
    //   8639: istore #6
    //   8641: goto -> 10984
    //   8644: iload #8
    //   8646: istore #21
    //   8648: aload #38
    //   8650: instanceof com/trendmicro/hippo/IdFunctionObject
    //   8653: istore #29
    //   8655: iload #29
    //   8657: ifeq -> 8961
    //   8660: aload #38
    //   8662: checkcast com/trendmicro/hippo/IdFunctionObject
    //   8665: astore #4
    //   8667: aload #4
    //   8669: invokestatic isContinuationConstructor : (Lcom/trendmicro/hippo/IdFunctionObject;)Z
    //   8672: istore #29
    //   8674: iload #29
    //   8676: ifeq -> 8757
    //   8679: aload_3
    //   8680: getfield stack : [Ljava/lang/Object;
    //   8683: astore #5
    //   8685: aload_3
    //   8686: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   8689: astore #9
    //   8691: aload #5
    //   8693: iload #22
    //   8695: aload_0
    //   8696: aload #9
    //   8698: iconst_0
    //   8699: invokestatic captureContinuation : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;Z)Lcom/trendmicro/hippo/NativeContinuation;
    //   8702: aastore
    //   8703: aload #7
    //   8705: astore_1
    //   8706: aload_0
    //   8707: astore #7
    //   8709: iload #21
    //   8711: istore #8
    //   8713: goto -> 9266
    //   8716: astore #7
    //   8718: iconst_0
    //   8719: istore #8
    //   8721: aload_0
    //   8722: astore #9
    //   8724: aload_2
    //   8725: astore #10
    //   8727: aload_3
    //   8728: astore #4
    //   8730: aload #25
    //   8732: astore_2
    //   8733: aload_1
    //   8734: astore #5
    //   8736: aload #7
    //   8738: astore_1
    //   8739: aload #9
    //   8741: astore_3
    //   8742: aload #17
    //   8744: astore #9
    //   8746: aload #15
    //   8748: astore #14
    //   8750: iload #28
    //   8752: istore #6
    //   8754: goto -> 10984
    //   8757: aload #4
    //   8759: invokestatic isApplyOrCall : (Lcom/trendmicro/hippo/IdFunctionObject;)Z
    //   8762: ifeq -> 8912
    //   8765: aload #37
    //   8767: invokestatic getCallable : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;
    //   8770: astore #24
    //   8772: aload #24
    //   8774: instanceof com/trendmicro/hippo/InterpretedFunction
    //   8777: ifeq -> 8909
    //   8780: aload #24
    //   8782: checkcast com/trendmicro/hippo/InterpretedFunction
    //   8785: astore #24
    //   8787: aload_3
    //   8788: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   8791: getfield securityDomain : Ljava/lang/Object;
    //   8794: astore #39
    //   8796: aload #24
    //   8798: getfield securityDomain : Ljava/lang/Object;
    //   8801: astore #26
    //   8803: aload #39
    //   8805: aload #26
    //   8807: if_acmpne -> 8906
    //   8810: aload_2
    //   8811: astore #10
    //   8813: aload_0
    //   8814: aload_3
    //   8815: iload #21
    //   8817: aload #30
    //   8819: aload #7
    //   8821: iload #22
    //   8823: iload #23
    //   8825: aload #32
    //   8827: aload #4
    //   8829: aload #24
    //   8831: invokestatic initFrameForApplyOrCall : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;I[Ljava/lang/Object;[DIILcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/IdFunctionObject;Lcom/trendmicro/hippo/InterpretedFunction;)Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   8834: astore #5
    //   8836: aload_0
    //   8837: astore_2
    //   8838: aload #10
    //   8840: astore_3
    //   8841: iload #21
    //   8843: istore #8
    //   8845: aload #25
    //   8847: astore #7
    //   8849: aload #17
    //   8851: astore #9
    //   8853: iload #28
    //   8855: istore #6
    //   8857: aload_1
    //   8858: astore #4
    //   8860: aload #15
    //   8862: astore_1
    //   8863: goto -> 139
    //   8866: astore #5
    //   8868: aload_0
    //   8869: astore_2
    //   8870: aload_3
    //   8871: astore #4
    //   8873: iconst_0
    //   8874: istore #8
    //   8876: aload #17
    //   8878: astore #9
    //   8880: aload_1
    //   8881: astore #7
    //   8883: aload_2
    //   8884: astore_3
    //   8885: aload #25
    //   8887: astore_2
    //   8888: aload #5
    //   8890: astore_1
    //   8891: aload #7
    //   8893: astore #5
    //   8895: aload #15
    //   8897: astore #14
    //   8899: iload #28
    //   8901: istore #6
    //   8903: goto -> 10984
    //   8906: goto -> 8961
    //   8909: goto -> 8961
    //   8912: goto -> 8961
    //   8915: astore #5
    //   8917: goto -> 8922
    //   8920: astore #5
    //   8922: iconst_0
    //   8923: istore #8
    //   8925: aload_2
    //   8926: astore #10
    //   8928: aload_3
    //   8929: astore #4
    //   8931: aload_1
    //   8932: astore #7
    //   8934: aload #17
    //   8936: astore #9
    //   8938: aload #25
    //   8940: astore_2
    //   8941: aload_0
    //   8942: astore_3
    //   8943: aload #5
    //   8945: astore_1
    //   8946: aload #7
    //   8948: astore #5
    //   8950: aload #15
    //   8952: astore #14
    //   8954: iload #28
    //   8956: istore #6
    //   8958: goto -> 10984
    //   8961: aload #7
    //   8963: astore #4
    //   8965: aload_2
    //   8966: astore #24
    //   8968: aload #38
    //   8970: instanceof com/trendmicro/hippo/ScriptRuntime$NoSuchMethodShim
    //   8973: istore #29
    //   8975: iload #29
    //   8977: ifeq -> 9181
    //   8980: aload #38
    //   8982: checkcast com/trendmicro/hippo/ScriptRuntime$NoSuchMethodShim
    //   8985: astore #7
    //   8987: aload #7
    //   8989: getfield noSuchMethodMethod : Lcom/trendmicro/hippo/Callable;
    //   8992: astore #26
    //   8994: aload #26
    //   8996: instanceof com/trendmicro/hippo/InterpretedFunction
    //   8999: ifeq -> 9134
    //   9002: aload #26
    //   9004: checkcast com/trendmicro/hippo/InterpretedFunction
    //   9007: astore #39
    //   9009: aload_3
    //   9010: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   9013: getfield securityDomain : Ljava/lang/Object;
    //   9016: astore #40
    //   9018: aload #39
    //   9020: getfield securityDomain : Ljava/lang/Object;
    //   9023: astore #26
    //   9025: aload #40
    //   9027: aload #26
    //   9029: if_acmpne -> 9131
    //   9032: iconst_0
    //   9033: istore #21
    //   9035: aload_0
    //   9036: aload_3
    //   9037: iload #8
    //   9039: aload #30
    //   9041: aload #4
    //   9043: iload #22
    //   9045: iload #23
    //   9047: aload #37
    //   9049: aload #32
    //   9051: aload #7
    //   9053: aload #39
    //   9055: invokestatic initFrameForNoSuchMethod : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;I[Ljava/lang/Object;[DIILcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/ScriptRuntime$NoSuchMethodShim;Lcom/trendmicro/hippo/InterpretedFunction;)Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   9058: astore #5
    //   9060: aload_0
    //   9061: astore_2
    //   9062: aload #24
    //   9064: astore_3
    //   9065: aload #25
    //   9067: astore #7
    //   9069: aload #17
    //   9071: astore #9
    //   9073: iload #28
    //   9075: istore #6
    //   9077: aload_1
    //   9078: astore #4
    //   9080: aload #15
    //   9082: astore_1
    //   9083: goto -> 139
    //   9086: astore #5
    //   9088: aload_0
    //   9089: astore_2
    //   9090: aload_3
    //   9091: astore #4
    //   9093: aload #17
    //   9095: astore #9
    //   9097: aload_1
    //   9098: astore #10
    //   9100: aload_2
    //   9101: astore_3
    //   9102: aload #25
    //   9104: astore_2
    //   9105: aload #5
    //   9107: astore_1
    //   9108: aload #10
    //   9110: astore #5
    //   9112: aload #24
    //   9114: astore #10
    //   9116: aload #15
    //   9118: astore #14
    //   9120: iload #21
    //   9122: istore #8
    //   9124: iload #28
    //   9126: istore #6
    //   9128: goto -> 10984
    //   9131: goto -> 9181
    //   9134: goto -> 9181
    //   9137: astore #5
    //   9139: iconst_0
    //   9140: istore #8
    //   9142: aload_0
    //   9143: astore_2
    //   9144: aload_3
    //   9145: astore #4
    //   9147: aload #17
    //   9149: astore #9
    //   9151: aload_1
    //   9152: astore #10
    //   9154: aload_2
    //   9155: astore_3
    //   9156: aload #25
    //   9158: astore_2
    //   9159: aload #5
    //   9161: astore_1
    //   9162: aload #10
    //   9164: astore #5
    //   9166: aload #24
    //   9168: astore #10
    //   9170: aload #15
    //   9172: astore #14
    //   9174: iload #28
    //   9176: istore #6
    //   9178: goto -> 10984
    //   9181: iconst_0
    //   9182: istore #21
    //   9184: iconst_0
    //   9185: istore #31
    //   9187: aload_0
    //   9188: astore #7
    //   9190: aload #7
    //   9192: aload_3
    //   9193: putfield lastInterpreterFrame : Ljava/lang/Object;
    //   9196: aload_3
    //   9197: iload #23
    //   9199: putfield savedCallOp : I
    //   9202: aload_3
    //   9203: iload #22
    //   9205: putfield savedStackTop : I
    //   9208: aload #4
    //   9210: astore #26
    //   9212: iload #8
    //   9214: istore #21
    //   9216: aload #7
    //   9218: astore #4
    //   9220: iload #21
    //   9222: istore #8
    //   9224: iload #31
    //   9226: istore #8
    //   9228: aload #30
    //   9230: iload #22
    //   9232: aload #38
    //   9234: aload #7
    //   9236: aload #32
    //   9238: aload #37
    //   9240: aload #30
    //   9242: aload #26
    //   9244: iload #22
    //   9246: iconst_2
    //   9247: iadd
    //   9248: iload #21
    //   9250: invokestatic getArgsArray : ([Ljava/lang/Object;[DII)[Ljava/lang/Object;
    //   9253: invokeinterface call : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   9258: aastore
    //   9259: iload #21
    //   9261: istore #8
    //   9263: aload #26
    //   9265: astore_1
    //   9266: iload #22
    //   9268: istore #21
    //   9270: aload #16
    //   9272: astore #24
    //   9274: aload #25
    //   9276: astore #5
    //   9278: aload #17
    //   9280: astore #9
    //   9282: iload #28
    //   9284: istore #6
    //   9286: aload #10
    //   9288: astore #4
    //   9290: aload #7
    //   9292: astore #16
    //   9294: aload #30
    //   9296: astore #10
    //   9298: aload_1
    //   9299: astore #7
    //   9301: aload #27
    //   9303: astore_1
    //   9304: aload_2
    //   9305: astore_3
    //   9306: aload #16
    //   9308: astore_2
    //   9309: aload #24
    //   9311: astore #16
    //   9313: goto -> 338
    //   9316: astore #10
    //   9318: aload_3
    //   9319: astore #4
    //   9321: aload #25
    //   9323: astore_2
    //   9324: aload #17
    //   9326: astore #9
    //   9328: aload_1
    //   9329: astore #5
    //   9331: aload #10
    //   9333: astore_1
    //   9334: aload #7
    //   9336: astore_3
    //   9337: aload #24
    //   9339: astore #10
    //   9341: aload #15
    //   9343: astore #14
    //   9345: iload #21
    //   9347: istore #8
    //   9349: iload #28
    //   9351: istore #6
    //   9353: goto -> 10984
    //   9356: astore #10
    //   9358: aload_0
    //   9359: astore_2
    //   9360: iconst_0
    //   9361: istore #8
    //   9363: aload_3
    //   9364: astore #4
    //   9366: aload #17
    //   9368: astore #9
    //   9370: aload_1
    //   9371: astore #5
    //   9373: aload #10
    //   9375: astore_1
    //   9376: aload_2
    //   9377: astore_3
    //   9378: aload #25
    //   9380: astore_2
    //   9381: aload #24
    //   9383: astore #10
    //   9385: aload #15
    //   9387: astore #14
    //   9389: iload #28
    //   9391: istore #6
    //   9393: goto -> 10984
    //   9396: astore #7
    //   9398: iconst_0
    //   9399: istore #8
    //   9401: aload_0
    //   9402: astore #9
    //   9404: aload_2
    //   9405: astore #10
    //   9407: aload_3
    //   9408: astore #4
    //   9410: aload #25
    //   9412: astore_2
    //   9413: aload_1
    //   9414: astore #5
    //   9416: aload #7
    //   9418: astore_1
    //   9419: aload #9
    //   9421: astore_3
    //   9422: aload #17
    //   9424: astore #9
    //   9426: aload #15
    //   9428: astore #14
    //   9430: iload #28
    //   9432: istore #6
    //   9434: goto -> 10984
    //   9437: astore #7
    //   9439: iconst_0
    //   9440: istore #8
    //   9442: aload_0
    //   9443: astore #9
    //   9445: aload_2
    //   9446: astore #10
    //   9448: aload_3
    //   9449: astore #4
    //   9451: aload #25
    //   9453: astore_2
    //   9454: aload_1
    //   9455: astore #5
    //   9457: aload #7
    //   9459: astore_1
    //   9460: aload #9
    //   9462: astore_3
    //   9463: aload #17
    //   9465: astore #9
    //   9467: aload #15
    //   9469: astore #14
    //   9471: iload #28
    //   9473: istore #6
    //   9475: goto -> 10984
    //   9478: astore_1
    //   9479: iconst_0
    //   9480: istore #8
    //   9482: goto -> 9489
    //   9485: astore_1
    //   9486: iconst_0
    //   9487: istore #8
    //   9489: aload_2
    //   9490: astore #7
    //   9492: aload #26
    //   9494: astore_3
    //   9495: aload #10
    //   9497: astore #5
    //   9499: aload #25
    //   9501: astore_2
    //   9502: aload #17
    //   9504: astore #9
    //   9506: aload #27
    //   9508: astore #4
    //   9510: aload #7
    //   9512: astore #10
    //   9514: aload #15
    //   9516: astore #14
    //   9518: iload #28
    //   9520: istore #6
    //   9522: goto -> 10984
    //   9525: iload #6
    //   9527: istore #28
    //   9529: aload_3
    //   9530: astore #17
    //   9532: aload #4
    //   9534: astore #25
    //   9536: iconst_0
    //   9537: istore #22
    //   9539: iconst_0
    //   9540: istore #23
    //   9542: aload_1
    //   9543: astore #27
    //   9545: iload #8
    //   9547: istore #22
    //   9549: aload_2
    //   9550: astore #4
    //   9552: iload #22
    //   9554: istore #8
    //   9556: aload #27
    //   9558: astore_3
    //   9559: aload #17
    //   9561: astore #24
    //   9563: iload #23
    //   9565: istore #8
    //   9567: aload #25
    //   9569: astore_1
    //   9570: aload #27
    //   9572: getfield localShift : I
    //   9575: istore #23
    //   9577: iload #23
    //   9579: iload #22
    //   9581: iadd
    //   9582: istore #8
    //   9584: aload #10
    //   9586: iload #8
    //   9588: aconst_null
    //   9589: aastore
    //   9590: aload #27
    //   9592: astore_1
    //   9593: iload #28
    //   9595: istore #6
    //   9597: aload #25
    //   9599: astore #4
    //   9601: aload #17
    //   9603: astore_3
    //   9604: goto -> 338
    //   9607: aload_3
    //   9608: astore #25
    //   9610: aload #4
    //   9612: astore #17
    //   9614: iconst_0
    //   9615: istore #23
    //   9617: aload_1
    //   9618: astore #27
    //   9620: iload #8
    //   9622: istore #22
    //   9624: aload #10
    //   9626: iload #21
    //   9628: aaload
    //   9629: astore #26
    //   9631: iinc #21, -1
    //   9634: aload #7
    //   9636: iload #21
    //   9638: daload
    //   9639: d2i
    //   9640: istore #31
    //   9642: aload_2
    //   9643: astore #4
    //   9645: iload #22
    //   9647: istore #8
    //   9649: aload #27
    //   9651: astore_3
    //   9652: aload #25
    //   9654: astore #24
    //   9656: iload #23
    //   9658: istore #8
    //   9660: aload #17
    //   9662: astore_1
    //   9663: aload #10
    //   9665: iload #21
    //   9667: aaload
    //   9668: checkcast [Ljava/lang/Object;
    //   9671: iload #31
    //   9673: aload #26
    //   9675: aastore
    //   9676: aload_2
    //   9677: astore #4
    //   9679: iload #22
    //   9681: istore #8
    //   9683: aload #27
    //   9685: astore_3
    //   9686: aload #25
    //   9688: astore #24
    //   9690: iload #23
    //   9692: istore #8
    //   9694: aload #17
    //   9696: astore_1
    //   9697: aload #10
    //   9699: iload #21
    //   9701: iconst_1
    //   9702: isub
    //   9703: aaload
    //   9704: checkcast [I
    //   9707: iload #31
    //   9709: iconst_m1
    //   9710: iastore
    //   9711: aload #7
    //   9713: iload #21
    //   9715: iload #31
    //   9717: iconst_1
    //   9718: iadd
    //   9719: i2d
    //   9720: dastore
    //   9721: aload #27
    //   9723: astore_1
    //   9724: aload #25
    //   9726: astore_3
    //   9727: aload #17
    //   9729: astore #4
    //   9731: iload #22
    //   9733: istore #8
    //   9735: goto -> 338
    //   9738: aload_3
    //   9739: astore #25
    //   9741: aload #4
    //   9743: astore #17
    //   9745: iconst_0
    //   9746: istore #23
    //   9748: aload_1
    //   9749: astore #27
    //   9751: iload #8
    //   9753: istore #22
    //   9755: aload #10
    //   9757: iload #21
    //   9759: aaload
    //   9760: astore #26
    //   9762: iinc #21, -1
    //   9765: aload #7
    //   9767: iload #21
    //   9769: daload
    //   9770: d2i
    //   9771: istore #31
    //   9773: aload_2
    //   9774: astore #4
    //   9776: iload #22
    //   9778: istore #8
    //   9780: aload #27
    //   9782: astore_3
    //   9783: aload #25
    //   9785: astore #24
    //   9787: iload #23
    //   9789: istore #8
    //   9791: aload #17
    //   9793: astore_1
    //   9794: aload #10
    //   9796: iload #21
    //   9798: aaload
    //   9799: checkcast [Ljava/lang/Object;
    //   9802: iload #31
    //   9804: aload #26
    //   9806: aastore
    //   9807: aload_2
    //   9808: astore #4
    //   9810: iload #22
    //   9812: istore #8
    //   9814: aload #27
    //   9816: astore_3
    //   9817: aload #25
    //   9819: astore #24
    //   9821: iload #23
    //   9823: istore #8
    //   9825: aload #17
    //   9827: astore_1
    //   9828: aload #10
    //   9830: iload #21
    //   9832: iconst_1
    //   9833: isub
    //   9834: aaload
    //   9835: checkcast [I
    //   9838: iload #31
    //   9840: iconst_1
    //   9841: iastore
    //   9842: aload #7
    //   9844: iload #21
    //   9846: iload #31
    //   9848: iconst_1
    //   9849: iadd
    //   9850: i2d
    //   9851: dastore
    //   9852: aload #27
    //   9854: astore_1
    //   9855: aload #25
    //   9857: astore_3
    //   9858: aload #17
    //   9860: astore #4
    //   9862: iload #22
    //   9864: istore #8
    //   9866: goto -> 338
    //   9869: astore #7
    //   9871: aload_3
    //   9872: astore #10
    //   9874: aload #5
    //   9876: astore_2
    //   9877: aload_1
    //   9878: astore #5
    //   9880: aload #7
    //   9882: astore_1
    //   9883: aload #4
    //   9885: astore_3
    //   9886: aload #10
    //   9888: astore #4
    //   9890: aload #24
    //   9892: astore #10
    //   9894: aload #15
    //   9896: astore #14
    //   9898: goto -> 10984
    //   9901: aload #9
    //   9903: astore #25
    //   9905: iload #6
    //   9907: istore #28
    //   9909: aload_3
    //   9910: astore #27
    //   9912: iconst_0
    //   9913: istore #22
    //   9915: aload_1
    //   9916: astore #17
    //   9918: aload_2
    //   9919: astore #26
    //   9921: aload #10
    //   9923: iload #21
    //   9925: aaload
    //   9926: astore #32
    //   9928: aload #4
    //   9930: astore #24
    //   9932: aload #32
    //   9934: astore #30
    //   9936: aload #32
    //   9938: aload #24
    //   9940: if_acmpne -> 9994
    //   9943: aload #7
    //   9945: iload #21
    //   9947: daload
    //   9948: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   9951: astore #30
    //   9953: goto -> 9994
    //   9956: astore_1
    //   9957: aload #17
    //   9959: astore #4
    //   9961: aload #5
    //   9963: astore_2
    //   9964: aload #25
    //   9966: astore #9
    //   9968: aload #26
    //   9970: astore_3
    //   9971: aload #24
    //   9973: astore #5
    //   9975: aload #27
    //   9977: astore #10
    //   9979: aload #15
    //   9981: astore #14
    //   9983: iload #22
    //   9985: istore #8
    //   9987: iload #28
    //   9989: istore #6
    //   9991: goto -> 10984
    //   9994: iinc #21, -1
    //   9997: aload #10
    //   9999: iload #21
    //   10001: aaload
    //   10002: checkcast com/trendmicro/hippo/Scriptable
    //   10005: astore #37
    //   10007: aload #5
    //   10009: astore #32
    //   10011: aload #10
    //   10013: iload #21
    //   10015: aload #37
    //   10017: aload #30
    //   10019: aload #26
    //   10021: aload #32
    //   10023: invokestatic setConst : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Ljava/lang/String;)Ljava/lang/Object;
    //   10026: aastore
    //   10027: aload #32
    //   10029: astore #5
    //   10031: aload #27
    //   10033: astore_3
    //   10034: aload #25
    //   10036: astore #9
    //   10038: iload #28
    //   10040: istore #6
    //   10042: aload #26
    //   10044: astore_2
    //   10045: aload #24
    //   10047: astore #4
    //   10049: aload #17
    //   10051: astore_1
    //   10052: goto -> 338
    //   10055: astore_1
    //   10056: aload #5
    //   10058: astore_2
    //   10059: aload #17
    //   10061: astore #4
    //   10063: aload #25
    //   10065: astore #9
    //   10067: aload #26
    //   10069: astore_3
    //   10070: aload #24
    //   10072: astore #5
    //   10074: aload #27
    //   10076: astore #10
    //   10078: aload #15
    //   10080: astore #14
    //   10082: iload #22
    //   10084: istore #8
    //   10086: iload #28
    //   10088: istore #6
    //   10090: goto -> 10984
    //   10093: aload_1
    //   10094: astore #17
    //   10096: aload #17
    //   10098: getfield pc : I
    //   10101: istore #8
    //   10103: aload #17
    //   10105: iload #8
    //   10107: iconst_1
    //   10108: iadd
    //   10109: putfield pc : I
    //   10112: aload #14
    //   10114: iload #8
    //   10116: baload
    //   10117: istore #8
    //   10119: goto -> 10840
    //   10122: iconst_0
    //   10123: istore #8
    //   10125: aload_3
    //   10126: astore #10
    //   10128: aload_2
    //   10129: astore_3
    //   10130: astore #14
    //   10132: aload #5
    //   10134: astore_2
    //   10135: aload_1
    //   10136: astore #7
    //   10138: aload #14
    //   10140: astore_1
    //   10141: aload #4
    //   10143: astore #5
    //   10145: aload #7
    //   10147: astore #4
    //   10149: aload #15
    //   10151: astore #14
    //   10153: goto -> 10984
    //   10156: aload_1
    //   10157: astore #17
    //   10159: aload #17
    //   10161: getfield frozen : Z
    //   10164: istore #28
    //   10166: iload #28
    //   10168: ifne -> 10228
    //   10171: aload #17
    //   10173: aload #17
    //   10175: getfield pc : I
    //   10178: iconst_1
    //   10179: isub
    //   10180: putfield pc : I
    //   10183: aload #17
    //   10185: invokestatic captureFrameForGenerator : (Lcom/trendmicro/hippo/Interpreter$CallFrame;)Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   10188: astore #10
    //   10190: aload #10
    //   10192: iconst_1
    //   10193: putfield frozen : Z
    //   10196: new com/trendmicro/hippo/NativeGenerator
    //   10199: astore #7
    //   10201: aload #7
    //   10203: aload #17
    //   10205: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   10208: aload #10
    //   10210: getfield fnOrScript : Lcom/trendmicro/hippo/InterpretedFunction;
    //   10213: aload #10
    //   10215: invokespecial <init> : (Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/NativeFunction;Ljava/lang/Object;)V
    //   10218: aload #17
    //   10220: aload #7
    //   10222: putfield result : Ljava/lang/Object;
    //   10225: goto -> 10474
    //   10228: iload #6
    //   10230: istore #28
    //   10232: aload #9
    //   10234: astore #24
    //   10236: iconst_0
    //   10237: istore #22
    //   10239: aload_3
    //   10240: astore #26
    //   10242: aload_1
    //   10243: astore #30
    //   10245: iload #21
    //   10247: istore #31
    //   10249: aload #4
    //   10251: astore #32
    //   10253: aload_2
    //   10254: astore #27
    //   10256: aload #30
    //   10258: getfield frozen : Z
    //   10261: istore #29
    //   10263: iload #29
    //   10265: ifne -> 10284
    //   10268: aload #24
    //   10270: astore #25
    //   10272: aload #27
    //   10274: aload #30
    //   10276: iload #31
    //   10278: aload #24
    //   10280: invokestatic freezeGenerator : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;ILcom/trendmicro/hippo/Interpreter$GeneratorState;)Ljava/lang/Object;
    //   10283: areturn
    //   10284: aload #24
    //   10286: astore #25
    //   10288: aload #30
    //   10290: iload #31
    //   10292: aload #24
    //   10294: iload #23
    //   10296: invokestatic thawGenerator : (Lcom/trendmicro/hippo/Interpreter$CallFrame;ILcom/trendmicro/hippo/Interpreter$GeneratorState;I)Ljava/lang/Object;
    //   10299: astore #37
    //   10301: aload #27
    //   10303: astore #9
    //   10305: aload #24
    //   10307: astore #17
    //   10309: aload #24
    //   10311: astore #25
    //   10313: aload #37
    //   10315: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
    //   10318: if_acmpeq -> 10792
    //   10321: aload #5
    //   10323: astore #25
    //   10325: aload #11
    //   10327: astore_2
    //   10328: aload #27
    //   10330: astore_1
    //   10331: aload #37
    //   10333: astore #4
    //   10335: aload #24
    //   10337: astore #9
    //   10339: aload #32
    //   10341: astore #5
    //   10343: aload #30
    //   10345: astore #7
    //   10347: aload #26
    //   10349: astore #10
    //   10351: iload #22
    //   10353: istore #21
    //   10355: iload #28
    //   10357: istore #6
    //   10359: goto -> 11008
    //   10362: iconst_0
    //   10363: istore #8
    //   10365: aload_3
    //   10366: astore #10
    //   10368: astore_3
    //   10369: aload_1
    //   10370: astore #7
    //   10372: aload_3
    //   10373: astore_1
    //   10374: aload_2
    //   10375: astore_3
    //   10376: aload #5
    //   10378: astore_2
    //   10379: aload #4
    //   10381: astore #5
    //   10383: aload #7
    //   10385: astore #4
    //   10387: aload #15
    //   10389: astore #14
    //   10391: goto -> 10984
    //   10394: aload #9
    //   10396: astore #10
    //   10398: aload_1
    //   10399: astore #7
    //   10401: aload #10
    //   10403: astore #25
    //   10405: aload #7
    //   10407: iconst_1
    //   10408: putfield frozen : Z
    //   10411: aload #10
    //   10413: astore #25
    //   10415: aload #14
    //   10417: aload #7
    //   10419: getfield pc : I
    //   10422: invokestatic getIndex : ([BI)I
    //   10425: istore #21
    //   10427: aload #10
    //   10429: astore #25
    //   10431: new com/trendmicro/hippo/JavaScriptException
    //   10434: astore #14
    //   10436: aload #10
    //   10438: astore #25
    //   10440: aload #14
    //   10442: aload #7
    //   10444: getfield scope : Lcom/trendmicro/hippo/Scriptable;
    //   10447: invokestatic getStopIterationObject : (Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   10450: aload #7
    //   10452: getfield idata : Lcom/trendmicro/hippo/InterpreterData;
    //   10455: getfield itsSourceFile : Ljava/lang/String;
    //   10458: iload #21
    //   10460: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/String;I)V
    //   10463: aload #10
    //   10465: astore #25
    //   10467: aload #10
    //   10469: aload #14
    //   10471: putfield returnedException : Ljava/lang/RuntimeException;
    //   10474: iload #6
    //   10476: istore #28
    //   10478: iconst_0
    //   10479: istore #21
    //   10481: aload_3
    //   10482: astore #17
    //   10484: aload_1
    //   10485: astore #24
    //   10487: aload #5
    //   10489: astore #10
    //   10491: aload #4
    //   10493: astore #7
    //   10495: aload_2
    //   10496: astore #14
    //   10498: aload #9
    //   10500: astore #25
    //   10502: aload #14
    //   10504: aload #24
    //   10506: aconst_null
    //   10507: invokestatic exitFrame : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;Ljava/lang/Object;)V
    //   10510: aload #9
    //   10512: astore #25
    //   10514: aload #24
    //   10516: getfield result : Ljava/lang/Object;
    //   10519: astore #16
    //   10521: aload #24
    //   10523: getfield resultDbl : D
    //   10526: dstore #35
    //   10528: aload #24
    //   10530: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   10533: ifnull -> 10645
    //   10536: aload #24
    //   10538: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   10541: astore_2
    //   10542: aload_2
    //   10543: astore_1
    //   10544: aload_2
    //   10545: astore #4
    //   10547: aload_2
    //   10548: getfield frozen : Z
    //   10551: ifeq -> 10562
    //   10554: aload_2
    //   10555: astore #4
    //   10557: aload_2
    //   10558: invokevirtual cloneFrozen : ()Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   10561: astore_1
    //   10562: aload_1
    //   10563: astore #4
    //   10565: aload_1
    //   10566: aload #16
    //   10568: dload #35
    //   10570: invokestatic setCallResult : (Lcom/trendmicro/hippo/Interpreter$CallFrame;Ljava/lang/Object;D)V
    //   10573: aconst_null
    //   10574: astore #11
    //   10576: aload #14
    //   10578: astore_2
    //   10579: aload #7
    //   10581: astore #4
    //   10583: aload_1
    //   10584: astore #5
    //   10586: dload #35
    //   10588: dstore #12
    //   10590: aload #17
    //   10592: astore_3
    //   10593: aload #15
    //   10595: astore_1
    //   10596: iload #28
    //   10598: istore #6
    //   10600: aload #10
    //   10602: astore #7
    //   10604: goto -> 139
    //   10607: astore_1
    //   10608: aload #10
    //   10610: astore_2
    //   10611: dload #35
    //   10613: dstore #12
    //   10615: aload #14
    //   10617: astore_3
    //   10618: aload #7
    //   10620: astore #5
    //   10622: aload #17
    //   10624: astore #10
    //   10626: aload #16
    //   10628: astore #11
    //   10630: aload #15
    //   10632: astore #14
    //   10634: iload #21
    //   10636: istore #8
    //   10638: iload #28
    //   10640: istore #6
    //   10642: goto -> 10984
    //   10645: aload #15
    //   10647: astore_3
    //   10648: aload #14
    //   10650: astore_1
    //   10651: aload #7
    //   10653: astore #5
    //   10655: goto -> 11475
    //   10658: astore_1
    //   10659: aload #10
    //   10661: astore_2
    //   10662: dload #35
    //   10664: dstore #12
    //   10666: aload #24
    //   10668: astore #4
    //   10670: aload #14
    //   10672: astore_3
    //   10673: aload #7
    //   10675: astore #5
    //   10677: aload #17
    //   10679: astore #10
    //   10681: aload #16
    //   10683: astore #11
    //   10685: aload #15
    //   10687: astore #14
    //   10689: iload #21
    //   10691: istore #8
    //   10693: iload #28
    //   10695: istore #6
    //   10697: goto -> 10984
    //   10700: astore_1
    //   10701: aload #10
    //   10703: astore_2
    //   10704: aload #24
    //   10706: astore #4
    //   10708: aload #14
    //   10710: astore_3
    //   10711: aload #7
    //   10713: astore #5
    //   10715: aload #17
    //   10717: astore #10
    //   10719: aload #16
    //   10721: astore #11
    //   10723: aload #15
    //   10725: astore #14
    //   10727: iload #21
    //   10729: istore #8
    //   10731: iload #28
    //   10733: istore #6
    //   10735: goto -> 10984
    //   10738: aload #9
    //   10740: astore #24
    //   10742: aload_2
    //   10743: astore #27
    //   10745: aload_1
    //   10746: astore #26
    //   10748: aload #27
    //   10750: astore #9
    //   10752: aload #24
    //   10754: astore #17
    //   10756: aload #24
    //   10758: astore #25
    //   10760: aload #26
    //   10762: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   10765: ifnull -> 10792
    //   10768: aload #24
    //   10770: astore #25
    //   10772: aload #26
    //   10774: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   10777: aload #27
    //   10779: invokeinterface onDebuggerStatement : (Lcom/trendmicro/hippo/Context;)V
    //   10784: aload #24
    //   10786: astore #17
    //   10788: aload #27
    //   10790: astore #9
    //   10792: aload #9
    //   10794: astore_2
    //   10795: aload #17
    //   10797: astore #9
    //   10799: goto -> 338
    //   10802: iconst_0
    //   10803: istore #8
    //   10805: aload_3
    //   10806: astore #10
    //   10808: astore #9
    //   10810: aload_1
    //   10811: astore #7
    //   10813: aload #9
    //   10815: astore_1
    //   10816: aload_2
    //   10817: astore_3
    //   10818: aload #5
    //   10820: astore_2
    //   10821: aload #25
    //   10823: astore #9
    //   10825: aload #4
    //   10827: astore #5
    //   10829: aload #7
    //   10831: astore #4
    //   10833: aload #15
    //   10835: astore #14
    //   10837: goto -> 10984
    //   10840: iconst_0
    //   10841: istore #22
    //   10843: aload_3
    //   10844: astore #17
    //   10846: aload #5
    //   10848: astore_3
    //   10849: aload #4
    //   10851: astore #5
    //   10853: aload_1
    //   10854: aload #10
    //   10856: aload #7
    //   10858: iload #21
    //   10860: aload #16
    //   10862: aload #18
    //   10864: aload #19
    //   10866: iload #8
    //   10868: invokestatic doSetConstVar : (Lcom/trendmicro/hippo/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[D[II)I
    //   10871: istore #21
    //   10873: aload #5
    //   10875: astore #4
    //   10877: aload_3
    //   10878: astore #5
    //   10880: aload #17
    //   10882: astore_3
    //   10883: goto -> 338
    //   10886: astore #4
    //   10888: aload_3
    //   10889: astore #7
    //   10891: aload_1
    //   10892: astore #10
    //   10894: aload_2
    //   10895: astore_3
    //   10896: aload #7
    //   10898: astore_2
    //   10899: aload #4
    //   10901: astore_1
    //   10902: aload #10
    //   10904: astore #4
    //   10906: aload #17
    //   10908: astore #10
    //   10910: aload #15
    //   10912: astore #14
    //   10914: iload #22
    //   10916: istore #8
    //   10918: goto -> 10984
    //   10921: astore #14
    //   10923: aload_3
    //   10924: astore #10
    //   10926: iconst_0
    //   10927: istore #8
    //   10929: aload_1
    //   10930: astore #7
    //   10932: aload #14
    //   10934: astore_1
    //   10935: aload_2
    //   10936: astore_3
    //   10937: aload #5
    //   10939: astore_2
    //   10940: aload #4
    //   10942: astore #5
    //   10944: aload #7
    //   10946: astore #4
    //   10948: aload #15
    //   10950: astore #14
    //   10952: goto -> 10984
    //   10955: astore #14
    //   10957: aload_3
    //   10958: astore #10
    //   10960: aload_2
    //   10961: astore_3
    //   10962: iconst_0
    //   10963: istore #8
    //   10965: aload #4
    //   10967: astore #5
    //   10969: aload #14
    //   10971: astore_2
    //   10972: aload #15
    //   10974: astore #14
    //   10976: aload_1
    //   10977: astore #4
    //   10979: aload_2
    //   10980: astore_1
    //   10981: aload #7
    //   10983: astore_2
    //   10984: aload #14
    //   10986: ifnonnull -> 11615
    //   10989: aload_2
    //   10990: astore #25
    //   10992: iload #8
    //   10994: istore #21
    //   10996: aload #4
    //   10998: astore #7
    //   11000: aload #11
    //   11002: astore_2
    //   11003: aload_1
    //   11004: astore #4
    //   11006: aload_3
    //   11007: astore_1
    //   11008: aload #4
    //   11010: ifnonnull -> 11017
    //   11013: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   11016: pop
    //   11017: aconst_null
    //   11018: astore #16
    //   11020: aload #9
    //   11022: ifnull -> 11050
    //   11025: aload #9
    //   11027: getfield operation : I
    //   11030: iconst_2
    //   11031: if_icmpne -> 11050
    //   11034: aload #4
    //   11036: aload #9
    //   11038: getfield value : Ljava/lang/Object;
    //   11041: if_acmpne -> 11050
    //   11044: iconst_1
    //   11045: istore #8
    //   11047: goto -> 11204
    //   11050: aload #4
    //   11052: instanceof com/trendmicro/hippo/JavaScriptException
    //   11055: ifeq -> 11064
    //   11058: iconst_2
    //   11059: istore #8
    //   11061: goto -> 11204
    //   11064: aload #4
    //   11066: instanceof com/trendmicro/hippo/EcmaError
    //   11069: ifeq -> 11078
    //   11072: iconst_2
    //   11073: istore #8
    //   11075: goto -> 11204
    //   11078: aload #4
    //   11080: instanceof com/trendmicro/hippo/EvaluatorException
    //   11083: ifeq -> 11092
    //   11086: iconst_2
    //   11087: istore #8
    //   11089: goto -> 11204
    //   11092: aload #4
    //   11094: instanceof com/trendmicro/hippo/ContinuationPending
    //   11097: ifeq -> 11106
    //   11100: iconst_0
    //   11101: istore #8
    //   11103: goto -> 11204
    //   11106: aload #4
    //   11108: instanceof java/lang/RuntimeException
    //   11111: ifeq -> 11135
    //   11114: aload_1
    //   11115: bipush #13
    //   11117: invokevirtual hasFeature : (I)Z
    //   11120: ifeq -> 11129
    //   11123: iconst_2
    //   11124: istore #8
    //   11126: goto -> 11132
    //   11129: iconst_1
    //   11130: istore #8
    //   11132: goto -> 11204
    //   11135: aload #4
    //   11137: instanceof java/lang/Error
    //   11140: ifeq -> 11165
    //   11143: aload_1
    //   11144: bipush #13
    //   11146: invokevirtual hasFeature : (I)Z
    //   11149: ifeq -> 11158
    //   11152: iconst_2
    //   11153: istore #8
    //   11155: goto -> 11162
    //   11158: iload #21
    //   11160: istore #8
    //   11162: goto -> 11204
    //   11165: aload #4
    //   11167: instanceof com/trendmicro/hippo/Interpreter$ContinuationJump
    //   11170: ifeq -> 11186
    //   11173: iconst_1
    //   11174: istore #8
    //   11176: aload #4
    //   11178: checkcast com/trendmicro/hippo/Interpreter$ContinuationJump
    //   11181: astore #16
    //   11183: goto -> 11204
    //   11186: aload_1
    //   11187: bipush #13
    //   11189: invokevirtual hasFeature : (I)Z
    //   11192: ifeq -> 11201
    //   11195: iconst_2
    //   11196: istore #8
    //   11198: goto -> 11204
    //   11201: iconst_1
    //   11202: istore #8
    //   11204: aload #4
    //   11206: astore_3
    //   11207: aload #16
    //   11209: astore #14
    //   11211: iload #8
    //   11213: istore #22
    //   11215: iload #6
    //   11217: ifeq -> 11260
    //   11220: aload_1
    //   11221: aload #7
    //   11223: bipush #100
    //   11225: invokestatic addInstructionCount : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;I)V
    //   11228: aload #4
    //   11230: astore_3
    //   11231: aload #16
    //   11233: astore #14
    //   11235: iload #8
    //   11237: istore #22
    //   11239: goto -> 11260
    //   11242: astore_3
    //   11243: aconst_null
    //   11244: astore #14
    //   11246: iconst_0
    //   11247: istore #22
    //   11249: goto -> 11260
    //   11252: astore_3
    //   11253: iconst_1
    //   11254: istore #22
    //   11256: aload #16
    //   11258: astore #14
    //   11260: aload #7
    //   11262: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   11265: ifnull -> 11307
    //   11268: aload_3
    //   11269: instanceof java/lang/RuntimeException
    //   11272: ifeq -> 11307
    //   11275: aload_3
    //   11276: checkcast java/lang/RuntimeException
    //   11279: astore #4
    //   11281: aload #7
    //   11283: getfield debuggerFrame : Lcom/trendmicro/hippo/debug/DebugFrame;
    //   11286: aload_1
    //   11287: aload #4
    //   11289: invokeinterface onExceptionThrown : (Lcom/trendmicro/hippo/Context;Ljava/lang/Throwable;)V
    //   11294: goto -> 11307
    //   11297: astore_3
    //   11298: aconst_null
    //   11299: astore #14
    //   11301: iconst_0
    //   11302: istore #22
    //   11304: goto -> 11307
    //   11307: iload #22
    //   11309: ifeq -> 11371
    //   11312: iload #22
    //   11314: iconst_2
    //   11315: if_icmpeq -> 11324
    //   11318: iconst_1
    //   11319: istore #8
    //   11321: goto -> 11328
    //   11324: iload #21
    //   11326: istore #8
    //   11328: aload #7
    //   11330: iload #8
    //   11332: invokestatic getExceptionHandler : (Lcom/trendmicro/hippo/Interpreter$CallFrame;Z)I
    //   11335: istore #8
    //   11337: iload #8
    //   11339: iflt -> 11371
    //   11342: aload_1
    //   11343: astore #14
    //   11345: aload_3
    //   11346: astore_1
    //   11347: aload_2
    //   11348: astore #11
    //   11350: aload #10
    //   11352: astore_3
    //   11353: aload #5
    //   11355: astore #4
    //   11357: aload #7
    //   11359: astore #5
    //   11361: aload #25
    //   11363: astore #7
    //   11365: aload #14
    //   11367: astore_2
    //   11368: goto -> 139
    //   11371: aload_1
    //   11372: aload #7
    //   11374: aload_3
    //   11375: invokestatic exitFrame : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Interpreter$CallFrame;Ljava/lang/Object;)V
    //   11378: aload #7
    //   11380: getfield parentFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   11383: astore #4
    //   11385: aload #4
    //   11387: ifnonnull -> 11558
    //   11390: aload #14
    //   11392: ifnull -> 11468
    //   11395: aload #14
    //   11397: getfield branchFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   11400: ifnull -> 11407
    //   11403: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   11406: pop
    //   11407: aload #14
    //   11409: getfield capturedFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   11412: ifnull -> 11449
    //   11415: iconst_m1
    //   11416: istore #8
    //   11418: aload_1
    //   11419: astore #14
    //   11421: aload_3
    //   11422: astore_1
    //   11423: aload_2
    //   11424: astore #11
    //   11426: aload #10
    //   11428: astore_3
    //   11429: aload #25
    //   11431: astore #7
    //   11433: aload #5
    //   11435: astore_2
    //   11436: aload #4
    //   11438: astore #5
    //   11440: aload_2
    //   11441: astore #4
    //   11443: aload #14
    //   11445: astore_2
    //   11446: goto -> 139
    //   11449: aload #14
    //   11451: getfield result : Ljava/lang/Object;
    //   11454: astore #16
    //   11456: aload #14
    //   11458: getfield resultDbl : D
    //   11461: dstore #35
    //   11463: aconst_null
    //   11464: astore_3
    //   11465: goto -> 11475
    //   11468: dload #12
    //   11470: dstore #35
    //   11472: aload_2
    //   11473: astore #16
    //   11475: aload_1
    //   11476: getfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   11479: ifnull -> 11506
    //   11482: aload_1
    //   11483: getfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   11486: invokevirtual size : ()I
    //   11489: ifeq -> 11506
    //   11492: aload_1
    //   11493: aload_1
    //   11494: getfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   11497: invokevirtual pop : ()Ljava/lang/Object;
    //   11500: putfield lastInterpreterFrame : Ljava/lang/Object;
    //   11503: goto -> 11516
    //   11506: aload_1
    //   11507: aconst_null
    //   11508: putfield lastInterpreterFrame : Ljava/lang/Object;
    //   11511: aload_1
    //   11512: aconst_null
    //   11513: putfield previousInterpreterInvocations : Lcom/trendmicro/hippo/ObjArray;
    //   11516: aload_3
    //   11517: ifnull -> 11537
    //   11520: aload_3
    //   11521: instanceof java/lang/RuntimeException
    //   11524: ifeq -> 11532
    //   11527: aload_3
    //   11528: checkcast java/lang/RuntimeException
    //   11531: athrow
    //   11532: aload_3
    //   11533: checkcast java/lang/Error
    //   11536: athrow
    //   11537: aload #16
    //   11539: aload #5
    //   11541: if_acmpeq -> 11550
    //   11544: aload #16
    //   11546: astore_0
    //   11547: goto -> 11556
    //   11550: dload #35
    //   11552: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   11555: astore_0
    //   11556: aload_0
    //   11557: areturn
    //   11558: aload #4
    //   11560: astore #7
    //   11562: aload #14
    //   11564: ifnull -> 11307
    //   11567: aload #4
    //   11569: astore #7
    //   11571: aload #14
    //   11573: getfield branchFrame : Lcom/trendmicro/hippo/Interpreter$CallFrame;
    //   11576: aload #4
    //   11578: if_acmpne -> 11307
    //   11581: iconst_m1
    //   11582: istore #8
    //   11584: aload_1
    //   11585: astore #14
    //   11587: aload_3
    //   11588: astore_1
    //   11589: aload_2
    //   11590: astore #11
    //   11592: aload #10
    //   11594: astore_3
    //   11595: aload #25
    //   11597: astore #7
    //   11599: aload #5
    //   11601: astore_2
    //   11602: aload #4
    //   11604: astore #5
    //   11606: aload_2
    //   11607: astore #4
    //   11609: aload #14
    //   11611: astore_2
    //   11612: goto -> 139
    //   11615: aload_1
    //   11616: getstatic java/lang/System.err : Ljava/io/PrintStream;
    //   11619: invokevirtual printStackTrace : (Ljava/io/PrintStream;)V
    //   11622: new java/lang/IllegalStateException
    //   11625: dup
    //   11626: invokespecial <init> : ()V
    //   11629: athrow
    // Exception table:
    //   from	to	target	type
    //   150	163	198	finally
    //   170	176	198	finally
    //   183	189	198	finally
    //   237	245	198	finally
    //   252	256	198	finally
    //   262	330	10955	finally
    //   338	352	10921	finally
    //   992	1065	1065	finally
    //   1103	1118	1121	finally
    //   1187	1197	1121	finally
    //   1197	1204	1225	finally
    //   1204	1219	2860	finally
    //   1263	1277	2860	finally
    //   1280	1294	2860	finally
    //   1321	1332	2860	finally
    //   1368	1379	2860	finally
    //   1409	1419	2860	finally
    //   1419	1430	2860	finally
    //   1464	1474	2860	finally
    //   1474	1492	2860	finally
    //   1502	1519	2860	finally
    //   1550	1560	2860	finally
    //   1563	1587	2860	finally
    //   1593	1610	2860	finally
    //   1632	1642	2860	finally
    //   1651	1658	2860	finally
    //   1682	1689	2165	finally
    //   1695	1703	2165	finally
    //   1739	1749	2860	finally
    //   1752	1759	2860	finally
    //   1809	1827	2165	finally
    //   1850	1857	2012	finally
    //   1864	1881	1976	finally
    //   1890	1902	1976	finally
    //   1913	1923	1976	finally
    //   1923	1950	1976	finally
    //   1950	1959	2165	finally
    //   2062	2069	2860	finally
    //   2102	2116	2860	finally
    //   2122	2129	2860	finally
    //   2224	2234	2860	finally
    //   2234	2267	2860	finally
    //   2303	2319	2860	finally
    //   2328	2341	2860	finally
    //   2344	2360	2860	finally
    //   2372	2383	2860	finally
    //   2399	2412	2860	finally
    //   2422	2430	2860	finally
    //   2436	2444	2860	finally
    //   2453	2463	2860	finally
    //   2507	2523	2860	finally
    //   2535	2551	2860	finally
    //   2563	2577	2860	finally
    //   2586	2600	2860	finally
    //   2627	2637	2860	finally
    //   2658	2668	2860	finally
    //   2668	2688	2860	finally
    //   2715	2725	2860	finally
    //   2725	2743	2860	finally
    //   2774	2784	2860	finally
    //   2784	2802	2860	finally
    //   2830	2840	2860	finally
    //   2840	2850	2860	finally
    //   2916	2927	2860	finally
    //   2944	2951	3469	finally
    //   2956	2998	3117	finally
    //   3005	3027	3081	finally
    //   3034	3048	3205	finally
    //   3167	3174	3434	finally
    //   3186	3196	3205	finally
    //   3199	3205	3205	finally
    //   3236	3250	3434	finally
    //   3255	3279	3205	finally
    //   3281	3292	3295	finally
    //   3356	3371	3924	finally
    //   3379	3400	3924	finally
    //   3511	3520	3924	finally
    //   3570	3579	3924	finally
    //   3618	3628	3924	finally
    //   3644	3654	3924	finally
    //   3674	3689	3924	finally
    //   3705	3715	3924	finally
    //   3728	3739	3924	finally
    //   3746	3755	3924	finally
    //   3772	3789	3924	finally
    //   3805	3820	3924	finally
    //   3839	3850	3924	finally
    //   3873	3886	3924	finally
    //   3903	3918	3924	finally
    //   4006	4016	3924	finally
    //   4023	4033	4115	finally
    //   4040	4047	4115	finally
    //   4047	4062	4428	finally
    //   4068	4086	4428	finally
    //   4175	4197	4428	finally
    //   4254	4276	4428	finally
    //   4316	4336	4428	finally
    //   4342	4355	4428	finally
    //   4389	4399	4428	finally
    //   4402	4419	4428	finally
    //   4478	4494	4811	finally
    //   4683	4693	4811	finally
    //   4696	4706	4811	finally
    //   4746	4756	4811	finally
    //   4759	4771	4811	finally
    //   4849	4871	4879	finally
    //   4930	4947	5007	finally
    //   4951	4979	5138	finally
    //   5076	5086	5138	finally
    //   5086	5125	5138	finally
    //   5175	5191	5263	finally
    //   5207	5250	5263	finally
    //   5322	5329	5377	finally
    //   5336	5349	5447	finally
    //   5417	5424	5846	finally
    //   5431	5441	5447	finally
    //   5491	5506	5846	finally
    //   5529	5546	5846	finally
    //   5549	5558	5846	finally
    //   5599	5609	5846	finally
    //   5612	5631	5846	finally
    //   5634	5643	5846	finally
    //   5694	5706	5846	finally
    //   5724	5734	5846	finally
    //   5734	5764	5846	finally
    //   5802	5812	5846	finally
    //   5812	5824	5846	finally
    //   5827	5836	5846	finally
    //   5905	5936	6067	finally
    //   5939	5965	6067	finally
    //   6037	6054	6067	finally
    //   6116	6129	6067	finally
    //   6132	6150	6210	finally
    //   6169	6176	6210	finally
    //   6192	6204	6210	finally
    //   6274	6293	6381	finally
    //   6358	6368	7997	finally
    //   6371	6375	7997	finally
    //   6451	6459	7997	finally
    //   6463	6470	7997	finally
    //   6527	6538	7573	finally
    //   6543	6553	7573	finally
    //   6587	6597	7997	finally
    //   6600	6608	7997	finally
    //   6611	6623	7997	finally
    //   6626	6639	7997	finally
    //   6642	6654	7997	finally
    //   6684	6700	7997	finally
    //   6703	6715	7997	finally
    //   6745	6761	7997	finally
    //   6764	6776	7997	finally
    //   6792	6801	7997	finally
    //   6807	6817	7997	finally
    //   6851	6861	7997	finally
    //   6875	6888	7997	finally
    //   6911	6921	7997	finally
    //   6927	6937	7997	finally
    //   6947	6976	7997	finally
    //   6995	7011	7997	finally
    //   7014	7029	7997	finally
    //   7090	7100	7997	finally
    //   7108	7120	7573	finally
    //   7139	7151	7997	finally
    //   7151	7163	7573	finally
    //   7182	7194	7997	finally
    //   7194	7206	7573	finally
    //   7261	7278	7997	finally
    //   7282	7294	7361	finally
    //   7317	7332	7997	finally
    //   7336	7348	7361	finally
    //   7420	7435	7997	finally
    //   7435	7447	7472	finally
    //   7518	7525	7997	finally
    //   7529	7538	7997	finally
    //   7548	7567	7573	finally
    //   7617	7624	7997	finally
    //   7628	7637	7997	finally
    //   7647	7668	7573	finally
    //   7752	7762	7997	finally
    //   7768	7783	7997	finally
    //   7803	7820	7997	finally
    //   7835	7848	7997	finally
    //   7851	7863	7997	finally
    //   7894	7901	7997	finally
    //   7907	7919	7997	finally
    //   7927	7942	7997	finally
    //   7948	7969	7997	finally
    //   7977	7987	7997	finally
    //   8069	8082	7997	finally
    //   8091	8113	9485	finally
    //   8124	8151	7997	finally
    //   8161	8167	9485	finally
    //   8167	8174	9478	finally
    //   8179	8188	8191	finally
    //   8227	8234	9478	finally
    //   8239	8261	8412	finally
    //   8275	8290	8191	finally
    //   8297	8319	8370	finally
    //   8326	8340	8370	finally
    //   8456	8463	9437	finally
    //   8468	8484	8599	finally
    //   8489	8495	8554	finally
    //   8498	8522	8554	finally
    //   8648	8655	9396	finally
    //   8660	8674	8920	finally
    //   8679	8691	8554	finally
    //   8691	8703	8716	finally
    //   8757	8772	8915	finally
    //   8772	8803	8915	finally
    //   8813	8836	8866	finally
    //   8968	8975	9356	finally
    //   8980	8994	9137	finally
    //   8994	9025	9137	finally
    //   9035	9060	9086	finally
    //   9190	9208	9316	finally
    //   9228	9259	9869	finally
    //   9570	9577	9869	finally
    //   9663	9676	9869	finally
    //   9697	9711	9869	finally
    //   9794	9807	9869	finally
    //   9828	9842	9869	finally
    //   9943	9953	9956	finally
    //   9997	10007	10055	finally
    //   10011	10027	10122	finally
    //   10096	10112	10122	finally
    //   10159	10166	10362	finally
    //   10171	10225	10122	finally
    //   10256	10263	10362	finally
    //   10272	10284	10802	finally
    //   10288	10301	10802	finally
    //   10313	10321	10802	finally
    //   10405	10411	10802	finally
    //   10415	10427	10802	finally
    //   10431	10436	10802	finally
    //   10440	10463	10802	finally
    //   10467	10474	10802	finally
    //   10502	10510	10802	finally
    //   10514	10521	10802	finally
    //   10521	10528	10700	finally
    //   10528	10542	10658	finally
    //   10547	10554	10607	finally
    //   10557	10562	10607	finally
    //   10565	10573	10607	finally
    //   10760	10768	10802	finally
    //   10772	10784	10802	finally
    //   10853	10873	10886	finally
    //   11220	11228	11252	java/lang/RuntimeException
    //   11220	11228	11242	java/lang/Error
    //   11281	11294	11297	finally
  }
  
  private static CallFrame processThrowable(Context paramContext, Object paramObject, CallFrame paramCallFrame, int paramInt, boolean paramBoolean) {
    CallFrame callFrame;
    int[] arrayOfInt;
    if (paramInt >= 0) {
      callFrame = paramCallFrame;
      if (paramCallFrame.frozen)
        callFrame = paramCallFrame.cloneFrozen(); 
      arrayOfInt = callFrame.idata.itsExceptionTable;
      callFrame.pc = arrayOfInt[paramInt + 2];
      if (paramBoolean)
        callFrame.pcPrevBranch = callFrame.pc; 
      callFrame.savedStackTop = callFrame.emptyStackTop;
      int i = callFrame.localShift;
      int j = arrayOfInt[paramInt + 5];
      int k = callFrame.localShift;
      paramInt = arrayOfInt[paramInt + 4];
      callFrame.scope = (Scriptable)callFrame.stack[i + j];
      callFrame.stack[k + paramInt] = paramObject;
    } else {
      int j;
      ContinuationJump continuationJump = (ContinuationJump)paramObject;
      if (continuationJump.branchFrame != arrayOfInt)
        Kit.codeBug(); 
      if (continuationJump.capturedFrame == null)
        Kit.codeBug(); 
      paramInt = continuationJump.capturedFrame.frameIndex + 1;
      int k = paramInt;
      if (continuationJump.branchFrame != null)
        k = paramInt - continuationJump.branchFrame.frameIndex; 
      paramInt = 0;
      paramObject = null;
      CallFrame callFrame1 = continuationJump.capturedFrame;
      int i = 0;
      while (true) {
        j = paramInt;
        if (i != k) {
          if (!callFrame1.frozen)
            Kit.codeBug(); 
          j = paramInt;
          Object object = paramObject;
          if (callFrame1.useActivation) {
            object = paramObject;
            if (paramObject == null)
              object = new CallFrame[k - i]; 
            object[paramInt] = callFrame1;
            j = paramInt + 1;
          } 
          callFrame1 = callFrame1.parentFrame;
          i++;
          paramInt = j;
          paramObject = object;
          continue;
        } 
        break;
      } 
      while (j != 0)
        enterFrame((Context)callFrame, (CallFrame)paramObject[--j], ScriptRuntime.emptyArgs, true); 
      callFrame = continuationJump.capturedFrame.cloneFrozen();
      setCallResult(callFrame, continuationJump.result, continuationJump.resultDbl);
    } 
    callFrame.throwable = null;
    return callFrame;
  }
  
  public static Object restartContinuation(NativeContinuation paramNativeContinuation, Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object object;
    if (!ScriptRuntime.hasTopCall(paramContext))
      return ScriptRuntime.doTopCall(paramNativeContinuation, paramContext, paramScriptable, null, paramArrayOfObject, paramContext.isTopLevelStrict); 
    if (paramArrayOfObject.length == 0) {
      object = Undefined.instance;
    } else {
      object = paramArrayOfObject[0];
    } 
    if ((CallFrame)paramNativeContinuation.getImplementation() == null)
      return object; 
    ContinuationJump continuationJump = new ContinuationJump(paramNativeContinuation, null);
    continuationJump.result = object;
    return interpretLoop(paramContext, (CallFrame)null, continuationJump);
  }
  
  public static Object resumeGenerator(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject1, Object paramObject2) {
    paramObject1 = paramObject1;
    GeneratorState generatorState = new GeneratorState(paramInt, paramObject2);
    if (paramInt == 2)
      try {
        return interpretLoop(paramContext, (CallFrame)paramObject1, generatorState);
      } catch (RuntimeException runtimeException) {
        if (runtimeException == paramObject2)
          return Undefined.instance; 
        throw runtimeException;
      }  
    Object object = interpretLoop((Context)runtimeException, (CallFrame)paramObject1, generatorState);
    if (generatorState.returnedException == null)
      return object; 
    throw generatorState.returnedException;
  }
  
  private static void setCallResult(CallFrame paramCallFrame, Object paramObject, double paramDouble) {
    if (paramCallFrame.savedCallOp == 38) {
      paramCallFrame.stack[paramCallFrame.savedStackTop] = paramObject;
      paramCallFrame.sDbl[paramCallFrame.savedStackTop] = paramDouble;
    } else if (paramCallFrame.savedCallOp == 30) {
      if (paramObject instanceof Scriptable)
        paramCallFrame.stack[paramCallFrame.savedStackTop] = paramObject; 
    } else {
      Kit.codeBug();
    } 
    paramCallFrame.savedCallOp = 0;
  }
  
  private static boolean stack_boolean(CallFrame paramCallFrame, int paramInt) {
    Object object = paramCallFrame.stack[paramInt];
    Boolean bool = Boolean.TRUE;
    boolean bool1 = true;
    boolean bool2 = true;
    if (object == bool)
      return true; 
    if (object == Boolean.FALSE)
      return false; 
    if (object == UniqueTag.DOUBLE_MARK) {
      double d = paramCallFrame.sDbl[paramInt];
      if (d != d || d == 0.0D)
        bool2 = false; 
      return bool2;
    } 
    if (object == null || object == Undefined.instance)
      return false; 
    if (object instanceof Number) {
      double d = ((Number)object).doubleValue();
      if (d == d && d != 0.0D) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      return bool2;
    } 
    return (object instanceof Boolean) ? ((Boolean)object).booleanValue() : ScriptRuntime.toBoolean(object);
  }
  
  private static double stack_double(CallFrame paramCallFrame, int paramInt) {
    Object object = paramCallFrame.stack[paramInt];
    return (object != UniqueTag.DOUBLE_MARK) ? ScriptRuntime.toNumber(object) : paramCallFrame.sDbl[paramInt];
  }
  
  private static int stack_int32(CallFrame paramCallFrame, int paramInt) {
    Object object = paramCallFrame.stack[paramInt];
    return (object == UniqueTag.DOUBLE_MARK) ? ScriptRuntime.toInt32(paramCallFrame.sDbl[paramInt]) : ScriptRuntime.toInt32(object);
  }
  
  private static Object thawGenerator(CallFrame paramCallFrame, int paramInt1, GeneratorState paramGeneratorState, int paramInt2) {
    paramCallFrame.frozen = false;
    int i = getIndex(paramCallFrame.idata.itsICode, paramCallFrame.pc);
    paramCallFrame.pc += 2;
    if (paramGeneratorState.operation == 1)
      return new JavaScriptException(paramGeneratorState.value, paramCallFrame.idata.itsSourceFile, i); 
    if (paramGeneratorState.operation == 2)
      return paramGeneratorState.value; 
    if (paramGeneratorState.operation == 0) {
      if (paramInt2 == 73)
        paramCallFrame.stack[paramInt1] = paramGeneratorState.value; 
      return Scriptable.NOT_FOUND;
    } 
    throw Kit.codeBug();
  }
  
  public void captureStackInfo(HippoException paramHippoException) {
    CallFrame[] arrayOfCallFrame;
    Context context = Context.getCurrentContext();
    if (context == null || context.lastInterpreterFrame == null) {
      paramHippoException.interpreterStackInfo = null;
      paramHippoException.interpreterLineData = null;
      return;
    } 
    if (context.previousInterpreterInvocations == null || context.previousInterpreterInvocations.size() == 0) {
      arrayOfCallFrame = new CallFrame[1];
    } else {
      int k = context.previousInterpreterInvocations.size();
      int m = k;
      if (context.previousInterpreterInvocations.peek() == context.lastInterpreterFrame)
        m = k - 1; 
      arrayOfCallFrame = new CallFrame[m + 1];
      context.previousInterpreterInvocations.toArray((Object[])arrayOfCallFrame);
    } 
    arrayOfCallFrame[arrayOfCallFrame.length - 1] = (CallFrame)context.lastInterpreterFrame;
    int j = 0;
    int i;
    for (i = 0; i != arrayOfCallFrame.length; i++)
      j += (arrayOfCallFrame[i]).frameIndex + 1; 
    int[] arrayOfInt = new int[j];
    i = j;
    j = arrayOfCallFrame.length;
    while (j != 0) {
      for (CallFrame callFrame = arrayOfCallFrame[--j]; callFrame != null; callFrame = callFrame.parentFrame)
        arrayOfInt[--i] = callFrame.pcSourceLineStart; 
    } 
    if (i != 0)
      Kit.codeBug(); 
    paramHippoException.interpreterStackInfo = arrayOfCallFrame;
    paramHippoException.interpreterLineData = arrayOfInt;
  }
  
  public Object compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean) {
    InterpreterData interpreterData = (new CodeGenerator()).compile(paramCompilerEnvirons, paramScriptNode, paramString, paramBoolean);
    this.itsData = interpreterData;
    return interpreterData;
  }
  
  public Function createFunctionObject(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2) {
    if (paramObject1 != this.itsData)
      Kit.codeBug(); 
    return InterpretedFunction.createFunction(paramContext, paramScriptable, this.itsData, paramObject2);
  }
  
  public Script createScriptObject(Object paramObject1, Object paramObject2) {
    if (paramObject1 != this.itsData)
      Kit.codeBug(); 
    return InterpretedFunction.createScript(this.itsData, paramObject2);
  }
  
  public String getPatchedStack(HippoException paramHippoException, String paramString) {
    StringBuilder stringBuilder = new StringBuilder(paramString.length() + 1000);
    String str = SecurityUtilities.getSystemProperty("line.separator");
    CallFrame[] arrayOfCallFrame = (CallFrame[])paramHippoException.interpreterStackInfo;
    int[] arrayOfInt = paramHippoException.interpreterLineData;
    int i = arrayOfCallFrame.length;
    int j = arrayOfInt.length;
    int k;
    for (k = 0; i != 0; k = m) {
      i--;
      int m = paramString.indexOf("com.trendmicro.hippo.Interpreter.interpretLoop", k);
      if (m < 0)
        break; 
      for (m += "com.trendmicro.hippo.Interpreter.interpretLoop".length(); m != paramString.length(); m++) {
        char c = paramString.charAt(m);
        if (c == '\n' || c == '\r')
          break; 
      } 
      stringBuilder.append(paramString.substring(k, m));
      for (CallFrame callFrame = arrayOfCallFrame[i]; callFrame != null; callFrame = callFrame.parentFrame) {
        if (j == 0)
          Kit.codeBug(); 
        j--;
        InterpreterData interpreterData = callFrame.idata;
        stringBuilder.append(str);
        stringBuilder.append("\tat script");
        if (interpreterData.itsName != null && interpreterData.itsName.length() != 0) {
          stringBuilder.append('.');
          stringBuilder.append(interpreterData.itsName);
        } 
        stringBuilder.append('(');
        stringBuilder.append(interpreterData.itsSourceFile);
        k = arrayOfInt[j];
        if (k >= 0) {
          stringBuilder.append(':');
          stringBuilder.append(getIndex(interpreterData.itsICode, k));
        } 
        stringBuilder.append(')');
      } 
    } 
    stringBuilder.append(paramString.substring(k));
    return stringBuilder.toString();
  }
  
  public List<String> getScriptStack(HippoException paramHippoException) {
    ScriptStackElement[][] arrayOfScriptStackElement = getScriptStackElements(paramHippoException);
    ArrayList<String> arrayList = new ArrayList(arrayOfScriptStackElement.length);
    String str = SecurityUtilities.getSystemProperty("line.separator");
    int i = arrayOfScriptStackElement.length;
    for (byte b = 0; b < i; b++) {
      ScriptStackElement[] arrayOfScriptStackElement1 = arrayOfScriptStackElement[b];
      StringBuilder stringBuilder = new StringBuilder();
      int j = arrayOfScriptStackElement1.length;
      for (byte b1 = 0; b1 < j; b1++) {
        arrayOfScriptStackElement1[b1].renderJavaStyle(stringBuilder);
        stringBuilder.append(str);
      } 
      arrayList.add(stringBuilder.toString());
    } 
    return arrayList;
  }
  
  public ScriptStackElement[][] getScriptStackElements(HippoException paramHippoException) {
    if (paramHippoException.interpreterStackInfo == null)
      return null; 
    ArrayList<ScriptStackElement[]> arrayList = new ArrayList();
    CallFrame[] arrayOfCallFrame = (CallFrame[])paramHippoException.interpreterStackInfo;
    int[] arrayOfInt = paramHippoException.interpreterLineData;
    int i = arrayOfCallFrame.length;
    int j = arrayOfInt.length;
    while (i != 0) {
      CallFrame callFrame = arrayOfCallFrame[--i];
      ArrayList<ScriptStackElement> arrayList1 = new ArrayList();
      while (callFrame != null) {
        String str1;
        if (j == 0)
          Kit.codeBug(); 
        int k = j - 1;
        InterpreterData interpreterData = callFrame.idata;
        String str2 = interpreterData.itsSourceFile;
        HippoException hippoException = null;
        j = -1;
        int m = arrayOfInt[k];
        if (m >= 0)
          j = getIndex(interpreterData.itsICode, m); 
        paramHippoException = hippoException;
        if (interpreterData.itsName != null) {
          paramHippoException = hippoException;
          if (interpreterData.itsName.length() != 0)
            str1 = interpreterData.itsName; 
        } 
        callFrame = callFrame.parentFrame;
        arrayList1.add(new ScriptStackElement(str2, str1, j));
        j = k;
      } 
      arrayList.add(arrayList1.<ScriptStackElement>toArray(new ScriptStackElement[arrayList1.size()]));
    } 
    return arrayList.<ScriptStackElement[]>toArray(new ScriptStackElement[arrayList.size()][]);
  }
  
  public String getSourcePositionFromStack(Context paramContext, int[] paramArrayOfint) {
    CallFrame callFrame = (CallFrame)paramContext.lastInterpreterFrame;
    InterpreterData interpreterData = callFrame.idata;
    if (callFrame.pcSourceLineStart >= 0) {
      paramArrayOfint[0] = getIndex(interpreterData.itsICode, callFrame.pcSourceLineStart);
    } else {
      paramArrayOfint[0] = 0;
    } 
    return interpreterData.itsSourceFile;
  }
  
  public void setEvalScriptFlag(Script paramScript) {
    ((InterpretedFunction)paramScript).idata.evalScriptFlag = true;
  }
  
  private static class CallFrame implements Cloneable, Serializable {
    private static final long serialVersionUID = -2843792508994958978L;
    
    final DebugFrame debuggerFrame;
    
    final int emptyStackTop;
    
    final InterpretedFunction fnOrScript;
    
    int frameIndex;
    
    boolean frozen;
    
    final InterpreterData idata;
    
    boolean isContinuationsTopFrame;
    
    final int localShift;
    
    CallFrame parentFrame;
    
    int pc;
    
    int pcPrevBranch;
    
    int pcSourceLineStart;
    
    Object result;
    
    double resultDbl;
    
    double[] sDbl;
    
    int savedCallOp;
    
    int savedStackTop;
    
    Scriptable scope;
    
    Object[] stack;
    
    int[] stackAttributes;
    
    final Scriptable thisObj;
    
    Object throwable;
    
    final boolean useActivation;
    
    final CallFrame varSource;
    
    CallFrame(Context param1Context, Scriptable param1Scriptable, InterpretedFunction param1InterpretedFunction, CallFrame param1CallFrame) {
      DebugFrame debugFrame;
      boolean bool;
      this.idata = param1InterpretedFunction.idata;
      if (param1Context.debugger != null) {
        debugFrame = param1Context.debugger.getFrame(param1Context, this.idata);
      } else {
        debugFrame = null;
      } 
      this.debuggerFrame = debugFrame;
      int i = 0;
      if (debugFrame != null || this.idata.itsNeedsActivation) {
        bool = true;
      } else {
        bool = false;
      } 
      this.useActivation = bool;
      this.emptyStackTop = this.idata.itsMaxVars + this.idata.itsMaxLocals - 1;
      this.fnOrScript = param1InterpretedFunction;
      this.varSource = this;
      this.localShift = this.idata.itsMaxVars;
      this.thisObj = param1Scriptable;
      this.parentFrame = param1CallFrame;
      if (param1CallFrame != null)
        i = param1CallFrame.frameIndex + 1; 
      this.frameIndex = i;
      if (i <= param1Context.getMaximumInterpreterStackDepth()) {
        this.result = Undefined.instance;
        this.pcSourceLineStart = this.idata.firstLinePC;
        this.savedStackTop = this.emptyStackTop;
        return;
      } 
      throw Context.reportRuntimeError("Exceeded maximum stack depth");
    }
    
    private static boolean equals(CallFrame param1CallFrame1, CallFrame param1CallFrame2, EqualObjectGraphs param1EqualObjectGraphs) {
      while (true) {
        if (param1CallFrame1 == param1CallFrame2)
          return true; 
        if (param1CallFrame1 == null || param1CallFrame2 == null)
          break; 
        if (!param1CallFrame1.fieldsEqual(param1CallFrame2, param1EqualObjectGraphs))
          return false; 
        param1CallFrame1 = param1CallFrame1.parentFrame;
        param1CallFrame2 = param1CallFrame2.parentFrame;
      } 
      return false;
    }
    
    private boolean equalsInTopScope(Object param1Object) {
      return ((Boolean)EqualObjectGraphs.<Boolean>withThreadLocal(param1EqualObjectGraphs -> Boolean.valueOf(equals(this, (CallFrame)param1Object, param1EqualObjectGraphs)))).booleanValue();
    }
    
    private boolean fieldsEqual(CallFrame param1CallFrame, EqualObjectGraphs param1EqualObjectGraphs) {
      boolean bool;
      if (this.frameIndex == param1CallFrame.frameIndex && this.pc == param1CallFrame.pc && Interpreter.compareIdata(this.idata, param1CallFrame.idata) && param1EqualObjectGraphs.equalGraphs(this.varSource.stack, param1CallFrame.varSource.stack) && Arrays.equals(this.varSource.sDbl, param1CallFrame.varSource.sDbl) && param1EqualObjectGraphs.equalGraphs(this.thisObj, param1CallFrame.thisObj) && param1EqualObjectGraphs.equalGraphs(this.fnOrScript, param1CallFrame.fnOrScript) && param1EqualObjectGraphs.equalGraphs(this.scope, param1CallFrame.scope)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private boolean isStrictTopFrame() {
      for (CallFrame callFrame = this;; callFrame = callFrame1) {
        CallFrame callFrame1 = callFrame.parentFrame;
        if (callFrame1 == null)
          return callFrame.idata.isStrict; 
      } 
    }
    
    CallFrame cloneFrozen() {
      if (!this.frozen)
        Kit.codeBug(); 
      try {
        CallFrame callFrame = (CallFrame)clone();
        callFrame.stack = (Object[])this.stack.clone();
        callFrame.stackAttributes = (int[])this.stackAttributes.clone();
        callFrame.sDbl = (double[])this.sDbl.clone();
        callFrame.frozen = false;
        return callFrame;
      } catch (CloneNotSupportedException cloneNotSupportedException) {
        throw new IllegalStateException();
      } 
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof CallFrame) {
        Context context = Context.enter();
        try {
          if (ScriptRuntime.hasTopCall(context))
            return equalsInTopScope(param1Object); 
          Scriptable scriptable = ScriptableObject.getTopLevelScope(this.scope);
          return ((Boolean)ScriptRuntime.doTopCall((param1Context, param1Scriptable1, param1Scriptable2, param1ArrayOfObject) -> Boolean.valueOf(equalsInTopScope(param1Object)), context, scriptable, scriptable, ScriptRuntime.emptyArgs, isStrictTopFrame())).booleanValue();
        } finally {
          Context.exit();
        } 
      } 
      return false;
    }
    
    public int hashCode() {
      byte b = 0;
      CallFrame callFrame = this;
      int i = 0;
      while (true) {
        i = (i * 31 + callFrame.pc) * 31 + callFrame.idata.icodeHashCode();
        callFrame = callFrame.parentFrame;
        if (callFrame == null || b >= 8)
          break; 
        b++;
      } 
      return i;
    }
    
    void initializeArgs(Context param1Context, Scriptable param1Scriptable, Object[] param1ArrayOfObject, double[] param1ArrayOfdouble, int param1Int1, int param1Int2) {
      Object[] arrayOfObject = param1ArrayOfObject;
      double[] arrayOfDouble = param1ArrayOfdouble;
      int i = param1Int1;
      if (this.useActivation) {
        arrayOfObject = param1ArrayOfObject;
        if (param1ArrayOfdouble != null)
          arrayOfObject = Interpreter.getArgsArray(param1ArrayOfObject, param1ArrayOfdouble, param1Int1, param1Int2); 
        i = 0;
        arrayOfDouble = null;
      } 
      if (this.idata.itsFunctionType != 0) {
        this.scope = this.fnOrScript.getParentScope();
        if (this.useActivation)
          if (this.idata.itsFunctionType == 4) {
            this.scope = ScriptRuntime.createArrowFunctionActivation(this.fnOrScript, this.scope, arrayOfObject, this.idata.isStrict);
          } else {
            this.scope = ScriptRuntime.createFunctionActivation(this.fnOrScript, this.scope, arrayOfObject, this.idata.isStrict);
          }  
      } else {
        this.scope = param1Scriptable;
        InterpretedFunction interpretedFunction = this.fnOrScript;
        ScriptRuntime.initScript(interpretedFunction, this.thisObj, param1Context, param1Scriptable, interpretedFunction.idata.evalScriptFlag);
      } 
      if (this.idata.itsNestedFunctions != null) {
        if (this.idata.itsFunctionType != 0 && !this.idata.itsNeedsActivation)
          Kit.codeBug(); 
        for (param1Int1 = 0; param1Int1 < this.idata.itsNestedFunctions.length; param1Int1++) {
          if ((this.idata.itsNestedFunctions[param1Int1]).itsFunctionType == 1)
            Interpreter.initFunction(param1Context, this.scope, this.fnOrScript, param1Int1); 
        } 
      } 
      param1Int1 = this.idata.itsMaxFrameArray;
      if (param1Int1 != this.emptyStackTop + this.idata.itsMaxStack + 1)
        Kit.codeBug(); 
      this.stack = new Object[param1Int1];
      this.stackAttributes = new int[param1Int1];
      this.sDbl = new double[param1Int1];
      int j = this.idata.getParamAndVarCount();
      for (param1Int1 = 0; param1Int1 < j; param1Int1++) {
        if (this.idata.getParamOrVarConst(param1Int1))
          this.stackAttributes[param1Int1] = 13; 
      } 
      j = this.idata.argCount;
      param1Int1 = j;
      if (j > param1Int2)
        param1Int1 = param1Int2; 
      System.arraycopy(arrayOfObject, i, this.stack, 0, param1Int1);
      if (arrayOfDouble != null)
        System.arraycopy(arrayOfDouble, i, this.sDbl, 0, param1Int1); 
      while (param1Int1 != this.idata.itsMaxVars) {
        this.stack[param1Int1] = Undefined.instance;
        param1Int1++;
      } 
    }
  }
  
  private static final class ContinuationJump implements Serializable {
    private static final long serialVersionUID = 7687739156004308247L;
    
    Interpreter.CallFrame branchFrame;
    
    Interpreter.CallFrame capturedFrame;
    
    Object result;
    
    double resultDbl;
    
    ContinuationJump(NativeContinuation param1NativeContinuation, Interpreter.CallFrame param1CallFrame) {
      Interpreter.CallFrame callFrame1 = (Interpreter.CallFrame)param1NativeContinuation.getImplementation();
      this.capturedFrame = callFrame1;
      if (callFrame1 == null || param1CallFrame == null) {
        this.branchFrame = null;
        return;
      } 
      Interpreter.CallFrame callFrame2 = this.capturedFrame;
      Interpreter.CallFrame callFrame3 = param1CallFrame;
      int i = callFrame2.frameIndex - callFrame3.frameIndex;
      Interpreter.CallFrame callFrame4 = callFrame2;
      callFrame1 = callFrame3;
      if (i != 0) {
        callFrame1 = callFrame2;
        int j = i;
        if (i < 0) {
          callFrame1 = param1CallFrame;
          callFrame3 = this.capturedFrame;
          j = -i;
        } 
        while (true) {
          param1CallFrame = callFrame1.parentFrame;
          i = j - 1;
          callFrame1 = param1CallFrame;
          j = i;
          if (i == 0) {
            callFrame4 = param1CallFrame;
            callFrame1 = callFrame3;
            if (param1CallFrame.frameIndex != callFrame3.frameIndex) {
              Kit.codeBug();
              callFrame1 = callFrame3;
              callFrame4 = param1CallFrame;
            } 
            break;
          } 
        } 
      } 
      while (callFrame4 != callFrame1 && callFrame4 != null) {
        callFrame4 = callFrame4.parentFrame;
        callFrame1 = callFrame1.parentFrame;
      } 
      this.branchFrame = callFrame4;
      if (callFrame4 != null && !callFrame4.frozen)
        Kit.codeBug(); 
    }
  }
  
  static class GeneratorState {
    int operation;
    
    RuntimeException returnedException;
    
    Object value;
    
    GeneratorState(int param1Int, Object param1Object) {
      this.operation = param1Int;
      this.value = param1Object;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Interpreter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */