package com.trendmicro.hippo.serialize;

import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.UniqueTag;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ScriptableOutputStream extends ObjectOutputStream {
  private Scriptable scope;
  
  private Map<Object, String> table;
  
  public ScriptableOutputStream(OutputStream paramOutputStream, Scriptable paramScriptable) throws IOException {
    super(paramOutputStream);
    this.scope = paramScriptable;
    HashMap<Object, Object> hashMap = new HashMap<>();
    this.table = (Map)hashMap;
    hashMap.put(paramScriptable, "");
    enableReplaceObject(true);
    excludeStandardObjectNames();
  }
  
  static Object lookupQualifiedName(Scriptable paramScriptable, String paramString) {
    Object object;
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ".");
    while (true) {
      object = paramScriptable;
      if (stringTokenizer.hasMoreTokens()) {
        String str = stringTokenizer.nextToken();
        Object object1 = ScriptableObject.getProperty(paramScriptable, str);
        object = object1;
        if (object1 != null) {
          if (!(object1 instanceof Scriptable)) {
            object = object1;
            break;
          } 
          continue;
        } 
      } 
      break;
    } 
    return object;
  }
  
  public void addExcludedName(String paramString) {
    Object object = lookupQualifiedName(this.scope, paramString);
    if (object instanceof Scriptable) {
      this.table.put(object, paramString);
      return;
    } 
    object = new StringBuilder();
    object.append("Object for excluded name ");
    object.append(paramString);
    object.append(" not found.");
    throw new IllegalArgumentException(object.toString());
  }
  
  public void addOptionalExcludedName(String paramString) {
    Object object = lookupQualifiedName(this.scope, paramString);
    if (object != null && object != UniqueTag.NOT_FOUND)
      if (object instanceof Scriptable) {
        this.table.put(object, paramString);
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Object for excluded name ");
        stringBuilder.append(paramString);
        stringBuilder.append(" is not a Scriptable, it is ");
        stringBuilder.append(object.getClass().getName());
        throw new IllegalArgumentException(stringBuilder.toString());
      }  
  }
  
  public void excludeAllIds(Object[] paramArrayOfObject) {
    int i = paramArrayOfObject.length;
    for (byte b = 0; b < i; b++) {
      Object object = paramArrayOfObject[b];
      if (object instanceof String) {
        Scriptable scriptable = this.scope;
        if (scriptable.get((String)object, scriptable) instanceof Scriptable)
          addExcludedName((String)object); 
      } 
    } 
  }
  
  public void excludeStandardObjectNames() {
    String[] arrayOfString = new String[21];
    arrayOfString[0] = "Object";
    arrayOfString[1] = "Object.prototype";
    arrayOfString[2] = "Function";
    arrayOfString[3] = "Function.prototype";
    arrayOfString[4] = "String";
    arrayOfString[5] = "String.prototype";
    arrayOfString[6] = "Math";
    arrayOfString[7] = "Array";
    arrayOfString[8] = "Array.prototype";
    arrayOfString[9] = "Error";
    arrayOfString[10] = "Error.prototype";
    arrayOfString[11] = "Number";
    arrayOfString[12] = "Number.prototype";
    arrayOfString[13] = "Date";
    arrayOfString[14] = "Date.prototype";
    arrayOfString[15] = "RegExp";
    arrayOfString[16] = "RegExp.prototype";
    arrayOfString[17] = "Script";
    arrayOfString[18] = "Script.prototype";
    arrayOfString[19] = "Continuation";
    arrayOfString[20] = "Continuation.prototype";
    byte b;
    for (b = 0; b < arrayOfString.length; b++)
      addExcludedName(arrayOfString[b]); 
    arrayOfString = new String[4];
    arrayOfString[0] = "XML";
    arrayOfString[1] = "XML.prototype";
    arrayOfString[2] = "XMLList";
    arrayOfString[3] = "XMLList.prototype";
    for (b = 0; b < arrayOfString.length; b++)
      addOptionalExcludedName(arrayOfString[b]); 
  }
  
  public boolean hasExcludedName(String paramString) {
    boolean bool;
    if (this.table.get(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void removeExcludedName(String paramString) {
    this.table.remove(paramString);
  }
  
  protected Object replaceObject(Object paramObject) throws IOException {
    String str = this.table.get(paramObject);
    return (str == null) ? paramObject : new PendingLookup(str);
  }
  
  static class PendingLookup implements Serializable {
    private static final long serialVersionUID = -2692990309789917727L;
    
    private String name;
    
    PendingLookup(String param1String) {
      this.name = param1String;
    }
    
    String getName() {
      return this.name;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/serialize/ScriptableOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */