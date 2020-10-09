package com.trendmicro.hippo;

import java.io.Closeable;
import java.util.Iterator;

public class IteratorLikeIterable implements Iterable<Object>, Closeable {
  private boolean closed;
  
  private final Context cx;
  
  private final Scriptable iterator;
  
  private final Callable next;
  
  private final Callable returnFunc;
  
  private final Scriptable scope;
  
  public IteratorLikeIterable(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    this.cx = paramContext;
    this.scope = paramScriptable;
    this.next = ScriptRuntime.getPropFunctionAndThis(paramObject, "next", paramContext, paramScriptable);
    this.iterator = ScriptRuntime.lastStoredScriptable(paramContext);
    Scriptable scriptable = ScriptableObject.ensureScriptable(paramObject);
    if (scriptable.has("return", scriptable)) {
      this.returnFunc = ScriptRuntime.getPropFunctionAndThis(paramObject, "return", paramContext, paramScriptable);
      ScriptRuntime.lastStoredScriptable(paramContext);
    } else {
      this.returnFunc = null;
    } 
  }
  
  public void close() {
    if (!this.closed) {
      this.closed = true;
      Callable callable = this.returnFunc;
      if (callable != null)
        callable.call(this.cx, this.scope, this.iterator, ScriptRuntime.emptyArgs); 
    } 
  }
  
  public Itr iterator() {
    return new Itr();
  }
  
  public final class Itr implements Iterator<Object> {
    private Object nextVal;
    
    public boolean hasNext() {
      Object object1 = IteratorLikeIterable.this.next.call(IteratorLikeIterable.this.cx, IteratorLikeIterable.this.scope, IteratorLikeIterable.this.iterator, ScriptRuntime.emptyArgs);
      Object object2 = ScriptRuntime.getObjectProp(object1, "done", IteratorLikeIterable.this.cx, IteratorLikeIterable.this.scope);
      if (!Undefined.instance.equals(object2)) {
        if (Boolean.TRUE.equals(object2))
          return false; 
        this.nextVal = ScriptRuntime.getObjectProp(object1, "value", IteratorLikeIterable.this.cx, IteratorLikeIterable.this.scope);
        return true;
      } 
      throw ScriptRuntime.undefReadError(object1, "done");
    }
    
    public Object next() {
      return this.nextVal;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/IteratorLikeIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */