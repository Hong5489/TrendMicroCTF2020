package com.trendmicro.hippo;

import com.trendmicro.hippo.debug.DebuggableObject;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

final class EqualObjectGraphs {
  private static final ThreadLocal<EqualObjectGraphs> instance = new ThreadLocal<>();
  
  private final Map<Object, Object> currentlyCompared = new IdentityHashMap<>();
  
  private final Map<Object, Object> knownEquals = new IdentityHashMap<>();
  
  private boolean equalGraphsNoMemo(Object paramObject1, Object paramObject2) {
    boolean bool = paramObject1 instanceof Wrapper;
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    boolean bool4 = true;
    boolean bool5 = true;
    boolean bool6 = true;
    boolean bool7 = true;
    if (bool) {
      if (!(paramObject2 instanceof Wrapper) || !equalGraphs(((Wrapper)paramObject1).unwrap(), ((Wrapper)paramObject2).unwrap()))
        bool7 = false; 
      return bool7;
    } 
    if (paramObject1 instanceof Scriptable) {
      if (paramObject2 instanceof Scriptable && equalScriptables((Scriptable)paramObject1, (Scriptable)paramObject2)) {
        bool7 = bool1;
      } else {
        bool7 = false;
      } 
      return bool7;
    } 
    if (paramObject1 instanceof ConsString)
      return ((ConsString)paramObject1).toString().equals(paramObject2); 
    if (paramObject2 instanceof ConsString)
      return paramObject1.equals(((ConsString)paramObject2).toString()); 
    if (paramObject1 instanceof SymbolKey) {
      if (paramObject2 instanceof SymbolKey && equalGraphs(((SymbolKey)paramObject1).getName(), ((SymbolKey)paramObject2).getName())) {
        bool7 = bool2;
      } else {
        bool7 = false;
      } 
      return bool7;
    } 
    if (paramObject1 instanceof Object[]) {
      if (paramObject2 instanceof Object[] && equalObjectArrays((Object[])paramObject1, (Object[])paramObject2)) {
        bool7 = bool3;
      } else {
        bool7 = false;
      } 
      return bool7;
    } 
    if (paramObject1.getClass().isArray())
      return Objects.deepEquals(paramObject1, paramObject2); 
    if (paramObject1 instanceof List) {
      if (paramObject2 instanceof List && equalLists((List)paramObject1, (List)paramObject2)) {
        bool7 = bool4;
      } else {
        bool7 = false;
      } 
      return bool7;
    } 
    if (paramObject1 instanceof Map) {
      if (paramObject2 instanceof Map && equalMaps((Map<?, ?>)paramObject1, (Map<?, ?>)paramObject2)) {
        bool7 = bool5;
      } else {
        bool7 = false;
      } 
      return bool7;
    } 
    if (paramObject1 instanceof Set) {
      if (paramObject2 instanceof Set && equalSets((Set)paramObject1, (Set)paramObject2)) {
        bool7 = bool6;
      } else {
        bool7 = false;
      } 
      return bool7;
    } 
    return (paramObject1 instanceof NativeGlobal) ? (paramObject2 instanceof NativeGlobal) : ((paramObject1 instanceof JavaAdapter) ? (paramObject2 instanceof JavaAdapter) : ((paramObject1 instanceof NativeJavaTopPackage) ? (paramObject2 instanceof NativeJavaTopPackage) : paramObject1.equals(paramObject2)));
  }
  
  private static boolean equalInterpretedFunctions(InterpretedFunction paramInterpretedFunction1, InterpretedFunction paramInterpretedFunction2) {
    return Objects.equals(paramInterpretedFunction1.getEncodedSource(), paramInterpretedFunction2.getEncodedSource());
  }
  
  private boolean equalLists(List<?> paramList1, List<?> paramList2) {
    if (paramList1.size() != paramList2.size())
      return false; 
    Iterator<?> iterator1 = paramList1.iterator();
    Iterator<?> iterator2 = paramList2.iterator();
    while (iterator1.hasNext() && iterator2.hasNext()) {
      if (!equalGraphs(iterator1.next(), iterator2.next()))
        return false; 
    } 
    return true;
  }
  
  private boolean equalMaps(Map<?, ?> paramMap1, Map<?, ?> paramMap2) {
    if (paramMap1.size() != paramMap2.size())
      return false; 
    Iterator<Map.Entry> iterator1 = sortedEntries(paramMap1);
    Iterator<Map.Entry> iterator2 = sortedEntries(paramMap2);
    while (iterator1.hasNext() && iterator2.hasNext()) {
      Map.Entry entry1 = iterator1.next();
      Map.Entry entry2 = iterator2.next();
      if (!equalGraphs(entry1.getKey(), entry2.getKey()) || !equalGraphs(entry1.getValue(), entry2.getValue()))
        return false; 
    } 
    return true;
  }
  
  private boolean equalObjectArrays(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
    if (paramArrayOfObject1.length != paramArrayOfObject2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfObject1.length; b++) {
      if (!equalGraphs(paramArrayOfObject1[b], paramArrayOfObject2[b]))
        return false; 
    } 
    return true;
  }
  
  private boolean equalScriptables(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    Object[] arrayOfObject1 = getSortedIds(paramScriptable1);
    Object[] arrayOfObject2 = getSortedIds(paramScriptable2);
    boolean bool = equalObjectArrays(arrayOfObject1, arrayOfObject2);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    if (!bool)
      return false; 
    int i = arrayOfObject1.length;
    for (byte b = 0; b < i; b++) {
      if (!equalGraphs(getValue(paramScriptable1, arrayOfObject1[b]), getValue(paramScriptable2, arrayOfObject2[b])))
        return false; 
    } 
    if (!equalGraphs(paramScriptable1.getPrototype(), paramScriptable2.getPrototype()))
      return false; 
    if (!equalGraphs(paramScriptable1.getParentScope(), paramScriptable2.getParentScope()))
      return false; 
    if (paramScriptable1 instanceof NativeContinuation) {
      bool = bool6;
      if (paramScriptable2 instanceof NativeContinuation) {
        bool = bool6;
        if (NativeContinuation.equalImplementations((NativeContinuation)paramScriptable1, (NativeContinuation)paramScriptable2))
          bool = true; 
      } 
      return bool;
    } 
    if (paramScriptable1 instanceof NativeJavaPackage)
      return paramScriptable1.equals(paramScriptable2); 
    if (paramScriptable1 instanceof IdFunctionObject) {
      bool = bool1;
      if (paramScriptable2 instanceof IdFunctionObject) {
        bool = bool1;
        if (IdFunctionObject.equalObjectGraphs((IdFunctionObject)paramScriptable1, (IdFunctionObject)paramScriptable2, this))
          bool = true; 
      } 
      return bool;
    } 
    if (paramScriptable1 instanceof InterpretedFunction) {
      bool = bool2;
      if (paramScriptable2 instanceof InterpretedFunction) {
        bool = bool2;
        if (equalInterpretedFunctions((InterpretedFunction)paramScriptable1, (InterpretedFunction)paramScriptable2))
          bool = true; 
      } 
      return bool;
    } 
    if (paramScriptable1 instanceof ArrowFunction) {
      bool = bool3;
      if (paramScriptable2 instanceof ArrowFunction) {
        bool = bool3;
        if (ArrowFunction.equalObjectGraphs((ArrowFunction)paramScriptable1, (ArrowFunction)paramScriptable2, this))
          bool = true; 
      } 
      return bool;
    } 
    if (paramScriptable1 instanceof BoundFunction) {
      bool = bool4;
      if (paramScriptable2 instanceof BoundFunction) {
        bool = bool4;
        if (BoundFunction.equalObjectGraphs((BoundFunction)paramScriptable1, (BoundFunction)paramScriptable2, this))
          bool = true; 
      } 
      return bool;
    } 
    if (paramScriptable1 instanceof NativeSymbol) {
      bool = bool5;
      if (paramScriptable2 instanceof NativeSymbol) {
        bool = bool5;
        if (equalGraphs(((NativeSymbol)paramScriptable1).getKey(), ((NativeSymbol)paramScriptable2).getKey()))
          bool = true; 
      } 
      return bool;
    } 
    return true;
  }
  
  private boolean equalSets(Set<?> paramSet1, Set<?> paramSet2) {
    return equalObjectArrays(sortedSet(paramSet1), sortedSet(paramSet2));
  }
  
  private static Object[] getIds(Scriptable paramScriptable) {
    return (paramScriptable instanceof ScriptableObject) ? ((ScriptableObject)paramScriptable).getIds(true, true) : ((paramScriptable instanceof DebuggableObject) ? ((DebuggableObject)paramScriptable).getAllIds() : paramScriptable.getIds());
  }
  
  private static Object[] getSortedIds(Scriptable paramScriptable) {
    Object[] arrayOfObject = getIds(paramScriptable);
    Arrays.sort(arrayOfObject, (paramObject1, paramObject2) -> {
          if (paramObject1 instanceof Integer) {
            if (paramObject2 instanceof Integer)
              return ((Integer)paramObject1).compareTo((Integer)paramObject2); 
            if (paramObject2 instanceof String || paramObject2 instanceof Symbol)
              return -1; 
          } else if (paramObject1 instanceof String) {
            if (paramObject2 instanceof String)
              return ((String)paramObject1).compareTo((String)paramObject2); 
            if (paramObject2 instanceof Integer)
              return 1; 
            if (paramObject2 instanceof Symbol)
              return -1; 
          } else if (paramObject1 instanceof Symbol) {
            if (paramObject2 instanceof Symbol)
              return getSymbolName((Symbol)paramObject1).compareTo(getSymbolName((Symbol)paramObject2)); 
            if (paramObject2 instanceof Integer || paramObject2 instanceof String)
              return 1; 
          } 
          throw new ClassCastException();
        });
    return arrayOfObject;
  }
  
  private static String getSymbolName(Symbol paramSymbol) {
    if (paramSymbol instanceof SymbolKey)
      return ((SymbolKey)paramSymbol).getName(); 
    if (paramSymbol instanceof NativeSymbol)
      return ((NativeSymbol)paramSymbol).getKey().getName(); 
    throw new ClassCastException();
  }
  
  private static Object getValue(Scriptable paramScriptable, Object paramObject) {
    if (paramObject instanceof Symbol)
      return ScriptableObject.getProperty(paramScriptable, (Symbol)paramObject); 
    if (paramObject instanceof Integer)
      return ScriptableObject.getProperty(paramScriptable, ((Integer)paramObject).intValue()); 
    if (paramObject instanceof String)
      return ScriptableObject.getProperty(paramScriptable, (String)paramObject); 
    throw new ClassCastException();
  }
  
  private static Iterator<Map.Entry> sortedEntries(Map<?, ?> paramMap) {
    if (!(paramMap instanceof java.util.SortedMap))
      paramMap = new TreeMap<>(paramMap); 
    return paramMap.entrySet().iterator();
  }
  
  private static Object[] sortedSet(Set<?> paramSet) {
    Object[] arrayOfObject = paramSet.toArray();
    Arrays.sort(arrayOfObject);
    return arrayOfObject;
  }
  
  static <T> T withThreadLocal(Function<EqualObjectGraphs, T> paramFunction) {
    EqualObjectGraphs equalObjectGraphs = instance.get();
    if (equalObjectGraphs == null) {
      equalObjectGraphs = new EqualObjectGraphs();
      instance.set(equalObjectGraphs);
      try {
        paramFunction = (Function<EqualObjectGraphs, T>)paramFunction.apply(equalObjectGraphs);
        return (T)paramFunction;
      } finally {
        instance.set(null);
      } 
    } 
    return paramFunction.apply(equalObjectGraphs);
  }
  
  boolean equalGraphs(Object paramObject1, Object paramObject2) {
    if (paramObject1 == paramObject2)
      return true; 
    if (paramObject1 == null || paramObject2 == null)
      return false; 
    Object object = this.currentlyCompared.get(paramObject1);
    if (object == paramObject2)
      return true; 
    if (object != null)
      return false; 
    object = this.knownEquals.get(paramObject1);
    if (object == paramObject2)
      return true; 
    if (object != null)
      return false; 
    object = this.knownEquals.get(paramObject2);
    if (object != null)
      return false; 
    this.currentlyCompared.put(paramObject1, paramObject2);
    boolean bool = equalGraphsNoMemo(paramObject1, paramObject2);
    if (bool) {
      this.knownEquals.put(paramObject1, paramObject2);
      this.knownEquals.put(paramObject2, paramObject1);
    } 
    this.currentlyCompared.remove(paramObject1);
    return bool;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/EqualObjectGraphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */