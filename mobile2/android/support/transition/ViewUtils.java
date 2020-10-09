package android.support.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.Property;
import android.view.View;
import java.lang.reflect.Field;

class ViewUtils {
  static final Property<View, Rect> CLIP_BOUNDS;
  
  private static final ViewUtilsBase IMPL;
  
  private static final String TAG = "ViewUtils";
  
  static final Property<View, Float> TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha") {
      public Float get(View param1View) {
        return Float.valueOf(ViewUtils.getTransitionAlpha(param1View));
      }
      
      public void set(View param1View, Float param1Float) {
        ViewUtils.setTransitionAlpha(param1View, param1Float.floatValue());
      }
    };
  
  private static final int VISIBILITY_MASK = 12;
  
  private static Field sViewFlagsField;
  
  private static boolean sViewFlagsFieldFetched;
  
  static {
    CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds") {
        public Rect get(View param1View) {
          return ViewCompat.getClipBounds(param1View);
        }
        
        public void set(View param1View, Rect param1Rect) {
          ViewCompat.setClipBounds(param1View, param1Rect);
        }
      };
  }
  
  static void clearNonTransitionAlpha(View paramView) {
    IMPL.clearNonTransitionAlpha(paramView);
  }
  
  private static void fetchViewFlagsField() {
    if (!sViewFlagsFieldFetched) {
      try {
        Field field = View.class.getDeclaredField("mViewFlags");
        sViewFlagsField = field;
        field.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.i("ViewUtils", "fetchViewFlagsField: ");
      } 
      sViewFlagsFieldFetched = true;
    } 
  }
  
  static ViewOverlayImpl getOverlay(View paramView) {
    return (ViewOverlayImpl)((Build.VERSION.SDK_INT >= 18) ? new ViewOverlayApi18(paramView) : ViewOverlayApi14.createFrom(paramView));
  }
  
  static float getTransitionAlpha(View paramView) {
    return IMPL.getTransitionAlpha(paramView);
  }
  
  static WindowIdImpl getWindowId(View paramView) {
    return (WindowIdImpl)((Build.VERSION.SDK_INT >= 18) ? new WindowIdApi18(paramView) : new WindowIdApi14(paramView.getWindowToken()));
  }
  
  static void saveNonTransitionAlpha(View paramView) {
    IMPL.saveNonTransitionAlpha(paramView);
  }
  
  static void setAnimationMatrix(View paramView, Matrix paramMatrix) {
    IMPL.setAnimationMatrix(paramView, paramMatrix);
  }
  
  static void setLeftTopRightBottom(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    IMPL.setLeftTopRightBottom(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  static void setTransitionAlpha(View paramView, float paramFloat) {
    IMPL.setTransitionAlpha(paramView, paramFloat);
  }
  
  static void setTransitionVisibility(View paramView, int paramInt) {
    fetchViewFlagsField();
    Field field = sViewFlagsField;
    if (field != null)
      try {
        int i = field.getInt(paramView);
        sViewFlagsField.setInt(paramView, i & 0xFFFFFFF3 | paramInt);
      } catch (IllegalAccessException illegalAccessException) {} 
  }
  
  static void transformMatrixToGlobal(View paramView, Matrix paramMatrix) {
    IMPL.transformMatrixToGlobal(paramView, paramMatrix);
  }
  
  static void transformMatrixToLocal(View paramView, Matrix paramMatrix) {
    IMPL.transformMatrixToLocal(paramView, paramMatrix);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 22) {
      IMPL = new ViewUtilsApi22();
    } else if (Build.VERSION.SDK_INT >= 21) {
      IMPL = new ViewUtilsApi21();
    } else if (Build.VERSION.SDK_INT >= 19) {
      IMPL = new ViewUtilsApi19();
    } else {
      IMPL = new ViewUtilsBase();
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/ViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */