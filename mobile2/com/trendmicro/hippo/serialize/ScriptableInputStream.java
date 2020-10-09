package com.trendmicro.hippo.serialize;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.UniqueTag;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ScriptableInputStream extends ObjectInputStream {
  private ClassLoader classLoader;
  
  private Scriptable scope;
  
  public ScriptableInputStream(InputStream paramInputStream, Scriptable paramScriptable) throws IOException {
    super(paramInputStream);
    this.scope = paramScriptable;
    enableResolveObject(true);
    Context context = Context.getCurrentContext();
    if (context != null)
      this.classLoader = context.getApplicationClassLoader(); 
  }
  
  protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass) throws IOException, ClassNotFoundException {
    String str = paramObjectStreamClass.getName();
    ClassLoader classLoader = this.classLoader;
    if (classLoader != null)
      try {
        return classLoader.loadClass(str);
      } catch (ClassNotFoundException classNotFoundException) {} 
    return super.resolveClass(paramObjectStreamClass);
  }
  
  protected Object resolveObject(Object paramObject) throws IOException {
    Object object;
    if (paramObject instanceof ScriptableOutputStream.PendingLookup) {
      paramObject = ((ScriptableOutputStream.PendingLookup)paramObject).getName();
      object = ScriptableOutputStream.lookupQualifiedName(this.scope, (String)paramObject);
      if (object == Scriptable.NOT_FOUND) {
        object = new StringBuilder();
        object.append("Object ");
        object.append((String)paramObject);
        object.append(" not found upon deserialization.");
        throw new IOException(object.toString());
      } 
    } else if (paramObject instanceof UniqueTag) {
      object = ((UniqueTag)paramObject).readResolve();
    } else {
      object = paramObject;
      if (paramObject instanceof Undefined)
        object = ((Undefined)paramObject).readResolve(); 
    } 
    return object;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/serialize/ScriptableInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */