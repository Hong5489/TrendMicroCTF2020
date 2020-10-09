package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
  private static final int[] ATTRS = new int[] { 16843284 };
  
  public static final int HORIZONTAL = 0;
  
  private static final String TAG = "DividerItem";
  
  public static final int VERTICAL = 1;
  
  private final Rect mBounds = new Rect();
  
  private Drawable mDivider;
  
  private int mOrientation;
  
  public DividerItemDecoration(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(ATTRS);
    Drawable drawable = typedArray.getDrawable(0);
    this.mDivider = drawable;
    if (drawable == null)
      Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()"); 
    typedArray.recycle();
    setOrientation(paramInt);
  }
  
  private void drawHorizontal(Canvas paramCanvas, RecyclerView paramRecyclerView) {
    boolean bool;
    int i;
    paramCanvas.save();
    if (paramRecyclerView.getClipToPadding()) {
      bool = paramRecyclerView.getPaddingTop();
      i = paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom();
      paramCanvas.clipRect(paramRecyclerView.getPaddingLeft(), bool, paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight(), i);
    } else {
      bool = false;
      i = paramRecyclerView.getHeight();
    } 
    int j = paramRecyclerView.getChildCount();
    for (byte b = 0; b < j; b++) {
      View view = paramRecyclerView.getChildAt(b);
      paramRecyclerView.getLayoutManager().getDecoratedBoundsWithMargins(view, this.mBounds);
      int k = this.mBounds.right + Math.round(view.getTranslationX());
      int m = this.mDivider.getIntrinsicWidth();
      this.mDivider.setBounds(k - m, bool, k, i);
      this.mDivider.draw(paramCanvas);
    } 
    paramCanvas.restore();
  }
  
  private void drawVertical(Canvas paramCanvas, RecyclerView paramRecyclerView) {
    boolean bool;
    int i;
    paramCanvas.save();
    if (paramRecyclerView.getClipToPadding()) {
      bool = paramRecyclerView.getPaddingLeft();
      i = paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight();
      paramCanvas.clipRect(bool, paramRecyclerView.getPaddingTop(), i, paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom());
    } else {
      bool = false;
      i = paramRecyclerView.getWidth();
    } 
    int j = paramRecyclerView.getChildCount();
    for (byte b = 0; b < j; b++) {
      View view = paramRecyclerView.getChildAt(b);
      paramRecyclerView.getDecoratedBoundsWithMargins(view, this.mBounds);
      int k = this.mBounds.bottom + Math.round(view.getTranslationY());
      int m = this.mDivider.getIntrinsicHeight();
      this.mDivider.setBounds(bool, k - m, i, k);
      this.mDivider.draw(paramCanvas);
    } 
    paramCanvas.restore();
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    Drawable drawable = this.mDivider;
    if (drawable == null) {
      paramRect.set(0, 0, 0, 0);
      return;
    } 
    if (this.mOrientation == 1) {
      paramRect.set(0, 0, 0, drawable.getIntrinsicHeight());
    } else {
      paramRect.set(0, 0, drawable.getIntrinsicWidth(), 0);
    } 
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    if (paramRecyclerView.getLayoutManager() == null || this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawVertical(paramCanvas, paramRecyclerView);
    } else {
      drawHorizontal(paramCanvas, paramRecyclerView);
    } 
  }
  
  public void setDrawable(Drawable paramDrawable) {
    if (paramDrawable != null) {
      this.mDivider = paramDrawable;
      return;
    } 
    throw new IllegalArgumentException("Drawable cannot be null.");
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt == 0 || paramInt == 1) {
      this.mOrientation = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/DividerItemDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */