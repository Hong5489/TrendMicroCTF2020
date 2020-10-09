package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ImageViewUtils {
  private static final String TAG = "ImageViewUtils";
  
  private static Method sAnimateTransformMethod;
  
  private static boolean sAnimateTransformMethodFetched;
  
  static void animateTransform(ImageView paramImageView, Matrix paramMatrix) {
    if (Build.VERSION.SDK_INT < 21) {
      paramImageView.setImageMatrix(paramMatrix);
    } else {
      fetchAnimateTransformMethod();
      Method method = sAnimateTransformMethod;
      if (method != null)
        try {
          method.invoke(paramImageView, new Object[] { paramMatrix });
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (InvocationTargetException invocationTargetException) {
          throw new RuntimeException(invocationTargetException.getCause());
        }  
    } 
  }
  
  private static void fetchAnimateTransformMethod() {
    if (!sAnimateTransformMethodFetched) {
      try {
        Method method = ImageView.class.getDeclaredMethod("animateTransform", new Class[] { Matrix.class });
        sAnimateTransformMethod = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ImageViewUtils", "Failed to retrieve animateTransform method", noSuchMethodException);
      } 
      sAnimateTransformMethodFetched = true;
    } 
  }
  
  static void reserveEndAnimateTransform(final ImageView view, Animator paramAnimator) {
    if (Build.VERSION.SDK_INT < 21)
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              ImageView.ScaleType scaleType = (ImageView.ScaleType)view.getTag(R.id.save_scale_type);
              view.setScaleType(scaleType);
              view.setTag(R.id.save_scale_type, null);
              if (scaleType == ImageView.ScaleType.MATRIX) {
                ImageView imageView = view;
                imageView.setImageMatrix((Matrix)imageView.getTag(R.id.save_image_matrix));
                view.setTag(R.id.save_image_matrix, null);
              } 
              param1Animator.removeListener((Animator.AnimatorListener)this);
            }
          }); 
  }
  
  static void startAnimateTransform(ImageView paramImageView) {
    if (Build.VERSION.SDK_INT < 21) {
      ImageView.ScaleType scaleType = paramImageView.getScaleType();
      paramImageView.setTag(R.id.save_scale_type, scaleType);
      if (scaleType == ImageView.ScaleType.MATRIX) {
        paramImageView.setTag(R.id.save_image_matrix, paramImageView.getImageMatrix());
      } else {
        paramImageView.setScaleType(ImageView.ScaleType.MATRIX);
      } 
      paramImageView.setImageMatrix(MatrixUtils.IDENTITY_MATRIX);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/ImageViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */