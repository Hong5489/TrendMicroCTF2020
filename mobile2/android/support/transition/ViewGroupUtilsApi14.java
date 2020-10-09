package android.support.transition;

import android.animation.LayoutTransition;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi14 {
  private static final int LAYOUT_TRANSITION_CHANGING = 4;
  
  private static final String TAG = "ViewGroupUtilsApi14";
  
  private static Method sCancelMethod;
  
  private static boolean sCancelMethodFetched;
  
  private static LayoutTransition sEmptyLayoutTransition;
  
  private static Field sLayoutSuppressedField;
  
  private static boolean sLayoutSuppressedFieldFetched;
  
  private static void cancelLayoutTransition(LayoutTransition paramLayoutTransition) {
    if (!sCancelMethodFetched) {
      try {
        Method method1 = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
        sCancelMethod = method1;
        method1.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
      } 
      sCancelMethodFetched = true;
    } 
    Method method = sCancelMethod;
    if (method != null)
      try {
        method.invoke(paramLayoutTransition, new Object[0]);
      } catch (IllegalAccessException illegalAccessException) {
        Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
      } catch (InvocationTargetException invocationTargetException) {
        Log.i("ViewGroupUtilsApi14", "Failed to invoke cancel method by reflection");
      }  
  }
  
  static void suppressLayout(ViewGroup paramViewGroup, boolean paramBoolean) {
    if (sEmptyLayoutTransition == null) {
      LayoutTransition layoutTransition = new LayoutTransition() {
          public boolean isChangingLayout() {
            return true;
          }
        };
      sEmptyLayoutTransition = layoutTransition;
      layoutTransition.setAnimator(2, null);
      sEmptyLayoutTransition.setAnimator(0, null);
      sEmptyLayoutTransition.setAnimator(1, null);
      sEmptyLayoutTransition.setAnimator(3, null);
      sEmptyLayoutTransition.setAnimator(4, null);
    } 
    if (paramBoolean) {
      LayoutTransition layoutTransition = paramViewGroup.getLayoutTransition();
      if (layoutTransition != null) {
        if (layoutTransition.isRunning())
          cancelLayoutTransition(layoutTransition); 
        if (layoutTransition != sEmptyLayoutTransition)
          paramViewGroup.setTag(R.id.transition_layout_save, layoutTransition); 
      } 
      paramViewGroup.setLayoutTransition(sEmptyLayoutTransition);
    } else {
      paramViewGroup.setLayoutTransition(null);
      if (!sLayoutSuppressedFieldFetched) {
        try {
          Field field1 = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
          sLayoutSuppressedField = field1;
          field1.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
          Log.i("ViewGroupUtilsApi14", "Failed to access mLayoutSuppressed field by reflection");
        } 
        sLayoutSuppressedFieldFetched = true;
      } 
      paramBoolean = false;
      boolean bool = false;
      Field field = sLayoutSuppressedField;
      if (field != null) {
        paramBoolean = bool;
        try {
          bool = field.getBoolean(paramViewGroup);
          if (bool) {
            paramBoolean = bool;
            sLayoutSuppressedField.setBoolean(paramViewGroup, false);
          } 
          paramBoolean = bool;
        } catch (IllegalAccessException illegalAccessException) {
          Log.i("ViewGroupUtilsApi14", "Failed to get mLayoutSuppressed field by reflection");
        } 
      } 
      if (paramBoolean)
        paramViewGroup.requestLayout(); 
      LayoutTransition layoutTransition = (LayoutTransition)paramViewGroup.getTag(R.id.transition_layout_save);
      if (layoutTransition != null) {
        paramViewGroup.setTag(R.id.transition_layout_save, null);
        paramViewGroup.setLayoutTransition(layoutTransition);
      } 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/ViewGroupUtilsApi14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */