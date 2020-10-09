package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.R;
import android.support.design.snackbar.ContentViewCallback;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback {
  private Button actionView;
  
  private int maxInlineActionWidth;
  
  private int maxWidth;
  
  private TextView messageView;
  
  public SnackbarContentLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SnackbarContentLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SnackbarLayout);
    this.maxWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
    this.maxInlineActionWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
    typedArray.recycle();
  }
  
  private static void updateTopBottomPadding(View paramView, int paramInt1, int paramInt2) {
    if (ViewCompat.isPaddingRelative(paramView)) {
      ViewCompat.setPaddingRelative(paramView, ViewCompat.getPaddingStart(paramView), paramInt1, ViewCompat.getPaddingEnd(paramView), paramInt2);
    } else {
      paramView.setPadding(paramView.getPaddingLeft(), paramInt1, paramView.getPaddingRight(), paramInt2);
    } 
  }
  
  private boolean updateViewsWithinLayout(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = false;
    if (paramInt1 != getOrientation()) {
      setOrientation(paramInt1);
      bool = true;
    } 
    if (this.messageView.getPaddingTop() != paramInt2 || this.messageView.getPaddingBottom() != paramInt3) {
      updateTopBottomPadding((View)this.messageView, paramInt2, paramInt3);
      bool = true;
    } 
    return bool;
  }
  
  public void animateContentIn(int paramInt1, int paramInt2) {
    this.messageView.setAlpha(0.0F);
    this.messageView.animate().alpha(1.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    if (this.actionView.getVisibility() == 0) {
      this.actionView.setAlpha(0.0F);
      this.actionView.animate().alpha(1.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    } 
  }
  
  public void animateContentOut(int paramInt1, int paramInt2) {
    this.messageView.setAlpha(1.0F);
    this.messageView.animate().alpha(0.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    if (this.actionView.getVisibility() == 0) {
      this.actionView.setAlpha(1.0F);
      this.actionView.animate().alpha(0.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    } 
  }
  
  public Button getActionView() {
    return this.actionView;
  }
  
  public TextView getMessageView() {
    return this.messageView;
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    this.messageView = (TextView)findViewById(R.id.snackbar_text);
    this.actionView = (Button)findViewById(R.id.snackbar_action);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    int i = paramInt1;
    if (this.maxWidth > 0) {
      int m = getMeasuredWidth();
      int n = this.maxWidth;
      i = paramInt1;
      if (m > n) {
        i = View.MeasureSpec.makeMeasureSpec(n, 1073741824);
        super.onMeasure(i, paramInt2);
      } 
    } 
    int k = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
    int j = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
    if (this.messageView.getLayout().getLineCount() > 1) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    boolean bool = false;
    if (paramInt1 != 0 && this.maxInlineActionWidth > 0 && this.actionView.getMeasuredWidth() > this.maxInlineActionWidth) {
      paramInt1 = bool;
      if (updateViewsWithinLayout(1, k, k - j))
        paramInt1 = 1; 
    } else {
      if (paramInt1 != 0)
        j = k; 
      paramInt1 = bool;
      if (updateViewsWithinLayout(0, j, j))
        paramInt1 = 1; 
    } 
    if (paramInt1 != 0)
      super.onMeasure(i, paramInt2); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/SnackbarContentLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */