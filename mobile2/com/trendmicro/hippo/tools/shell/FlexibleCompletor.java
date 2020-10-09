package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

class FlexibleCompletor implements InvocationHandler {
  private Method completeMethod;
  
  private Scriptable global;
  
  FlexibleCompletor(Class<?> paramClass, Scriptable paramScriptable) throws NoSuchMethodException {
    this.global = paramScriptable;
    this.completeMethod = paramClass.getMethod("complete", new Class[] { String.class, int.class, List.class });
  }
  
  public int complete(String paramString, int paramInt, List<String> paramList) {
    Object[] arrayOfObject;
    int i;
    for (i = paramInt - 1; i >= 0; i--) {
      char c = paramString.charAt(i);
      if (!Character.isJavaIdentifierPart(c) && c != '.')
        break; 
    } 
    String[] arrayOfString = paramString.substring(i + 1, paramInt).split("\\.", -1);
    Object object = this.global;
    paramInt = 0;
    while (paramInt < arrayOfString.length - 1) {
      object = object.get(arrayOfString[paramInt], this.global);
      if (object instanceof Scriptable) {
        object = object;
        paramInt++;
        continue;
      } 
      return paramString.length();
    } 
    if (object instanceof ScriptableObject) {
      arrayOfObject = ((ScriptableObject)object).getAllIds();
    } else {
      arrayOfObject = object.getIds();
    } 
    String str = arrayOfString[arrayOfString.length - 1];
    for (paramInt = 0; paramInt < arrayOfObject.length; paramInt++) {
      if (arrayOfObject[paramInt] instanceof String) {
        String str1 = (String)arrayOfObject[paramInt];
        if (str1.startsWith(str)) {
          String str2 = str1;
          if (object.get(str1, (Scriptable)object) instanceof com.trendmicro.hippo.Function) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str1);
            stringBuilder.append("(");
            str2 = stringBuilder.toString();
          } 
          paramList.add(str2);
        } 
      } 
    } 
    return paramString.length() - str.length();
  }
  
  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) {
    if (paramMethod.equals(this.completeMethod))
      return Integer.valueOf(complete((String)paramArrayOfObject[0], ((Integer)paramArrayOfObject[1]).intValue(), (List<String>)paramArrayOfObject[2])); 
    throw new NoSuchMethodError(paramMethod.toString());
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/FlexibleCompletor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */