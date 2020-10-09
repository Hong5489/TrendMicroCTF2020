package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback {
  private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
  
  private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
  
  private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
  
  private static final int MAX_IMAGE_SIZE = 1048576;
  
  private Matrix mTempMatrix;
  
  private static Bitmap createDrawableBitmap(Drawable paramDrawable) {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if (i <= 0 || j <= 0)
      return null; 
    float f = Math.min(1.0F, 1048576.0F / (i * j));
    if (paramDrawable instanceof BitmapDrawable && f == 1.0F)
      return ((BitmapDrawable)paramDrawable).getBitmap(); 
    i = (int)(i * f);
    int k = (int)(j * f);
    Bitmap bitmap = Bitmap.createBitmap(i, k, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Rect rect = paramDrawable.getBounds();
    j = rect.left;
    int m = rect.top;
    int n = rect.right;
    int i1 = rect.bottom;
    paramDrawable.setBounds(0, 0, i, k);
    paramDrawable.draw(canvas);
    paramDrawable.setBounds(j, m, n, i1);
    return bitmap;
  }
  
  public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF) {
    Bundle bundle;
    float[] arrayOfFloat;
    if (paramView instanceof ImageView) {
      ImageView imageView = (ImageView)paramView;
      Drawable drawable1 = imageView.getDrawable();
      Drawable drawable2 = imageView.getBackground();
      if (drawable1 != null && drawable2 == null) {
        Bitmap bitmap = createDrawableBitmap(drawable1);
        if (bitmap != null) {
          bundle = new Bundle();
          bundle.putParcelable("sharedElement:snapshot:bitmap", (Parcelable)bitmap);
          bundle.putString("sharedElement:snapshot:imageScaleType", imageView.getScaleType().toString());
          if (imageView.getScaleType() == ImageView.ScaleType.MATRIX) {
            paramMatrix = imageView.getImageMatrix();
            arrayOfFloat = new float[9];
            paramMatrix.getValues(arrayOfFloat);
            bundle.putFloatArray("sharedElement:snapshot:imageMatrix", arrayOfFloat);
          } 
          return (Parcelable)bundle;
        } 
      } 
    } 
    int i = Math.round(arrayOfFloat.width());
    int j = Math.round(arrayOfFloat.height());
    Bitmap bitmap2 = null;
    Bitmap bitmap1 = bitmap2;
    if (i > 0) {
      bitmap1 = bitmap2;
      if (j > 0) {
        float f = Math.min(1.0F, 1048576.0F / (i * j));
        i = (int)(i * f);
        j = (int)(j * f);
        if (this.mTempMatrix == null)
          this.mTempMatrix = new Matrix(); 
        this.mTempMatrix.set(paramMatrix);
        this.mTempMatrix.postTranslate(-((RectF)arrayOfFloat).left, -((RectF)arrayOfFloat).top);
        this.mTempMatrix.postScale(f, f);
        bitmap1 = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        canvas.concat(this.mTempMatrix);
        bundle.draw(canvas);
      } 
    } 
    return (Parcelable)bitmap1;
  }
  
  public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable) {
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    Bitmap bitmap = null;
    if (paramParcelable instanceof Bundle) {
      Bundle bundle = (Bundle)paramParcelable;
      bitmap = (Bitmap)bundle.getParcelable("sharedElement:snapshot:bitmap");
      if (bitmap == null)
        return null; 
      imageView2 = new ImageView(paramContext);
      imageView1 = imageView2;
      imageView2.setImageBitmap(bitmap);
      imageView2.setScaleType(ImageView.ScaleType.valueOf(bundle.getString("sharedElement:snapshot:imageScaleType")));
      imageView3 = imageView1;
      if (imageView2.getScaleType() == ImageView.ScaleType.MATRIX) {
        float[] arrayOfFloat = bundle.getFloatArray("sharedElement:snapshot:imageMatrix");
        Matrix matrix = new Matrix();
        matrix.setValues(arrayOfFloat);
        imageView2.setImageMatrix(matrix);
        ImageView imageView = imageView1;
      } 
    } else if (imageView2 instanceof Bitmap) {
      Bitmap bitmap1 = (Bitmap)imageView2;
      imageView3 = new ImageView((Context)imageView1);
      imageView3.setImageBitmap(bitmap1);
    } 
    return (View)imageView3;
  }
  
  public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap) {}
  
  public void onRejectSharedElements(List<View> paramList) {}
  
  public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2) {}
  
  public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2) {}
  
  public void onSharedElementsArrived(List<String> paramList, List<View> paramList1, OnSharedElementsReadyListener paramOnSharedElementsReadyListener) {
    paramOnSharedElementsReadyListener.onSharedElementsReady();
  }
  
  public static interface OnSharedElementsReadyListener {
    void onSharedElementsReady();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/app/SharedElementCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */