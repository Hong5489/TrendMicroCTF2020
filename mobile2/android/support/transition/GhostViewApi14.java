package android.support.transition;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

class GhostViewApi14 extends View implements GhostViewImpl {
  Matrix mCurrentMatrix;
  
  private int mDeltaX;
  
  private int mDeltaY;
  
  private final Matrix mMatrix = new Matrix();
  
  private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
      public boolean onPreDraw() {
        GhostViewApi14 ghostViewApi14 = GhostViewApi14.this;
        ghostViewApi14.mCurrentMatrix = ghostViewApi14.mView.getMatrix();
        ViewCompat.postInvalidateOnAnimation(GhostViewApi14.this);
        if (GhostViewApi14.this.mStartParent != null && GhostViewApi14.this.mStartView != null) {
          GhostViewApi14.this.mStartParent.endViewTransition(GhostViewApi14.this.mStartView);
          ViewCompat.postInvalidateOnAnimation((View)GhostViewApi14.this.mStartParent);
          GhostViewApi14.this.mStartParent = null;
          GhostViewApi14.this.mStartView = null;
        } 
        return true;
      }
    };
  
  int mReferences;
  
  ViewGroup mStartParent;
  
  View mStartView;
  
  final View mView;
  
  GhostViewApi14(View paramView) {
    super(paramView.getContext());
    this.mView = paramView;
    setLayerType(2, null);
  }
  
  static GhostViewImpl addGhost(View paramView, ViewGroup paramViewGroup) {
    GhostViewApi14 ghostViewApi141 = getGhostView(paramView);
    GhostViewApi14 ghostViewApi142 = ghostViewApi141;
    if (ghostViewApi141 == null) {
      FrameLayout frameLayout = findFrameLayout(paramViewGroup);
      if (frameLayout == null)
        return null; 
      ghostViewApi142 = new GhostViewApi14(paramView);
      frameLayout.addView(ghostViewApi142);
    } 
    ghostViewApi142.mReferences++;
    return ghostViewApi142;
  }
  
  private static FrameLayout findFrameLayout(ViewGroup paramViewGroup) {
    ViewGroup viewGroup;
    while (!(paramViewGroup instanceof FrameLayout)) {
      ViewParent viewParent = paramViewGroup.getParent();
      if (!(viewParent instanceof ViewGroup))
        return null; 
      viewGroup = (ViewGroup)viewParent;
    } 
    return (FrameLayout)viewGroup;
  }
  
  static GhostViewApi14 getGhostView(View paramView) {
    return (GhostViewApi14)paramView.getTag(R.id.ghost_view);
  }
  
  static void removeGhost(View paramView) {
    paramView = getGhostView(paramView);
    if (paramView != null) {
      int i = ((GhostViewApi14)paramView).mReferences - 1;
      ((GhostViewApi14)paramView).mReferences = i;
      if (i <= 0) {
        ViewParent viewParent = paramView.getParent();
        if (viewParent instanceof ViewGroup) {
          ViewGroup viewGroup = (ViewGroup)viewParent;
          viewGroup.endViewTransition(paramView);
          viewGroup.removeView(paramView);
        } 
      } 
    } 
  }
  
  private static void setGhostView(View paramView, GhostViewApi14 paramGhostViewApi14) {
    paramView.setTag(R.id.ghost_view, paramGhostViewApi14);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setGhostView(this.mView, this);
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = new int[2];
    getLocationOnScreen(arrayOfInt1);
    this.mView.getLocationOnScreen(arrayOfInt2);
    arrayOfInt2[0] = (int)(arrayOfInt2[0] - this.mView.getTranslationX());
    arrayOfInt2[1] = (int)(arrayOfInt2[1] - this.mView.getTranslationY());
    this.mDeltaX = arrayOfInt2[0] - arrayOfInt1[0];
    this.mDeltaY = arrayOfInt2[1] - arrayOfInt1[1];
    this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    this.mView.setVisibility(4);
  }
  
  protected void onDetachedFromWindow() {
    this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
    this.mView.setVisibility(0);
    setGhostView(this.mView, (GhostViewApi14)null);
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    this.mMatrix.set(this.mCurrentMatrix);
    this.mMatrix.postTranslate(this.mDeltaX, this.mDeltaY);
    paramCanvas.setMatrix(this.mMatrix);
    this.mView.draw(paramCanvas);
  }
  
  public void reserveEndViewTransition(ViewGroup paramViewGroup, View paramView) {
    this.mStartParent = paramViewGroup;
    this.mStartView = paramView;
  }
  
  public void setVisibility(int paramInt) {
    super.setVisibility(paramInt);
    View view = this.mView;
    if (paramInt == 0) {
      paramInt = 4;
    } else {
      paramInt = 0;
    } 
    view.setVisibility(paramInt);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/GhostViewApi14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */