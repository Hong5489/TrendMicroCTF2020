package android.support.transition;

import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi18 {
  private static final String TAG = "ViewUtilsApi18";
  
  private static Method sSuppressLayoutMethod;
  
  private static boolean sSuppressLayoutMethodFetched;
  
  private static void fetchSuppressLayoutMethod() {
    if (!sSuppressLayoutMethodFetched) {
      try {
        Method method = ViewGroup.class.getDeclaredMethod("suppressLayout", new Class[] { boolean.class });
        sSuppressLayoutMethod = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ViewUtilsApi18", "Failed to retrieve suppressLayout method", noSuchMethodException);
      } 
      sSuppressLayoutMethodFetched = true;
    } 
  }
  
  static void suppressLayout(ViewGroup paramViewGroup, boolean paramBoolean) {
    fetchSuppressLayoutMethod();
    Method method = sSuppressLayoutMethod;
    if (method != null)
      try {
        method.invoke(paramViewGroup, new Object[] { Boolean.valueOf(paramBoolean) });
      } catch (IllegalAccessException illegalAccessException) {
        Log.i("ViewUtilsApi18", "Failed to invoke suppressLayout method", illegalAccessException);
      } catch (InvocationTargetException invocationTargetException) {
        Log.i("ViewUtilsApi18", "Error invoking suppressLayout method", invocationTargetException);
      }  
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/ViewGroupUtilsApi18.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */