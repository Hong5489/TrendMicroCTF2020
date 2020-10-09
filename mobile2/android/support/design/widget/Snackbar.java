package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.design.R;
import android.support.design.snackbar.ContentViewCallback;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Snackbar extends BaseTransientBottomBar<Snackbar> {
  public static final int LENGTH_INDEFINITE = -2;
  
  public static final int LENGTH_LONG = 0;
  
  public static final int LENGTH_SHORT = -1;
  
  private static final int[] SNACKBAR_BUTTON_STYLE_ATTR = new int[] { R.attr.snackbarButtonStyle };
  
  private final AccessibilityManager accessibilityManager;
  
  private BaseTransientBottomBar.BaseCallback<Snackbar> callback;
  
  private boolean hasAction;
  
  private Snackbar(ViewGroup paramViewGroup, View paramView, ContentViewCallback paramContentViewCallback) {
    super(paramViewGroup, paramView, paramContentViewCallback);
    this.accessibilityManager = (AccessibilityManager)paramViewGroup.getContext().getSystemService("accessibility");
  }
  
  private static ViewGroup findSuitableParent(View paramView) {
    ViewGroup viewGroup = null;
    while (true) {
      ViewParent viewParent2;
      if (paramView instanceof CoordinatorLayout)
        return (ViewGroup)paramView; 
      if (paramView instanceof android.widget.FrameLayout) {
        if (paramView.getId() == 16908290)
          return (ViewGroup)paramView; 
        viewGroup = (ViewGroup)paramView;
      } 
      View view = paramView;
      if (paramView != null) {
        ViewParent viewParent = paramView.getParent();
        if (viewParent instanceof View) {
          View view1 = (View)viewParent;
        } else {
          viewParent = null;
        } 
        viewParent2 = viewParent;
      } 
      if (viewParent2 == null)
        return viewGroup; 
      ViewParent viewParent1 = viewParent2;
    } 
  }
  
  protected static boolean hasSnackbarButtonStyleAttr(Context paramContext) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
    boolean bool = false;
    int i = typedArray.getResourceId(0, -1);
    typedArray.recycle();
    if (i != -1)
      bool = true; 
    return bool;
  }
  
  public static Snackbar make(View paramView, int paramInt1, int paramInt2) {
    return make(paramView, paramView.getResources().getText(paramInt1), paramInt2);
  }
  
  public static Snackbar make(View paramView, CharSequence paramCharSequence, int paramInt) {
    ViewGroup viewGroup = findSuitableParent(paramView);
    if (viewGroup != null) {
      int i;
      LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
      if (hasSnackbarButtonStyleAttr(viewGroup.getContext())) {
        i = R.layout.mtrl_layout_snackbar_include;
      } else {
        i = R.layout.design_layout_snackbar_include;
      } 
      SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout)layoutInflater.inflate(i, viewGroup, false);
      Snackbar snackbar = new Snackbar(viewGroup, (View)snackbarContentLayout, snackbarContentLayout);
      snackbar.setText(paramCharSequence);
      snackbar.setDuration(paramInt);
      return snackbar;
    } 
    throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
  }
  
  public void dismiss() {
    super.dismiss();
  }
  
  public int getDuration() {
    int i;
    if (this.hasAction && this.accessibilityManager.isTouchExplorationEnabled()) {
      i = -2;
    } else {
      i = super.getDuration();
    } 
    return i;
  }
  
  public boolean isShown() {
    return super.isShown();
  }
  
  public Snackbar setAction(int paramInt, View.OnClickListener paramOnClickListener) {
    return setAction(getContext().getText(paramInt), paramOnClickListener);
  }
  
  public Snackbar setAction(CharSequence paramCharSequence, final View.OnClickListener listener) {
    Button button = ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView();
    if (TextUtils.isEmpty(paramCharSequence) || listener == null) {
      button.setVisibility(8);
      button.setOnClickListener(null);
      this.hasAction = false;
      return this;
    } 
    this.hasAction = true;
    button.setVisibility(0);
    button.setText(paramCharSequence);
    button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            listener.onClick(param1View);
            Snackbar.this.dispatchDismiss(1);
          }
        });
    return this;
  }
  
  public Snackbar setActionTextColor(int paramInt) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(paramInt);
    return this;
  }
  
  public Snackbar setActionTextColor(ColorStateList paramColorStateList) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(paramColorStateList);
    return this;
  }
  
  @Deprecated
  public Snackbar setCallback(Callback paramCallback) {
    BaseTransientBottomBar.BaseCallback<Snackbar> baseCallback = this.callback;
    if (baseCallback != null)
      removeCallback(baseCallback); 
    if (paramCallback != null)
      addCallback(paramCallback); 
    this.callback = paramCallback;
    return this;
  }
  
  public Snackbar setText(int paramInt) {
    return setText(getContext().getText(paramInt));
  }
  
  public Snackbar setText(CharSequence paramCharSequence) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setText(paramCharSequence);
    return this;
  }
  
  public void show() {
    super.show();
  }
  
  public static class Callback extends BaseTransientBottomBar.BaseCallback<Snackbar> {
    public static final int DISMISS_EVENT_ACTION = 1;
    
    public static final int DISMISS_EVENT_CONSECUTIVE = 4;
    
    public static final int DISMISS_EVENT_MANUAL = 3;
    
    public static final int DISMISS_EVENT_SWIPE = 0;
    
    public static final int DISMISS_EVENT_TIMEOUT = 2;
    
    public void onDismissed(Snackbar param1Snackbar, int param1Int) {}
    
    public void onShown(Snackbar param1Snackbar) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Duration {}
  
  public static final class SnackbarLayout extends BaseTransientBottomBar.SnackbarBaseLayout {
    public SnackbarLayout(Context param1Context) {
      super(param1Context);
    }
    
    public SnackbarLayout(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      super.onMeasure(param1Int1, param1Int2);
      int i = getChildCount();
      int j = getMeasuredWidth();
      int k = getPaddingLeft();
      param1Int2 = getPaddingRight();
      for (param1Int1 = 0; param1Int1 < i; param1Int1++) {
        View view = getChildAt(param1Int1);
        if ((view.getLayoutParams()).width == -1)
          view.measure(View.MeasureSpec.makeMeasureSpec(j - k - param1Int2, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824)); 
      } 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/Snackbar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */