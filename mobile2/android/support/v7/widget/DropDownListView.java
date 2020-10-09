package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.appcompat.R;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

class DropDownListView extends ListView {
  public static final int INVALID_POSITION = -1;
  
  public static final int NO_POSITION = -1;
  
  private ViewPropertyAnimatorCompat mClickAnimation;
  
  private boolean mDrawsInPressedState;
  
  private boolean mHijackFocus;
  
  private Field mIsChildViewEnabled;
  
  private boolean mListSelectionHidden;
  
  private int mMotionPosition;
  
  ResolveHoverRunnable mResolveHoverRunnable;
  
  private ListViewAutoScrollHelper mScrollHelper;
  
  private int mSelectionBottomPadding = 0;
  
  private int mSelectionLeftPadding = 0;
  
  private int mSelectionRightPadding = 0;
  
  private int mSelectionTopPadding = 0;
  
  private GateKeeperDrawable mSelector;
  
  private final Rect mSelectorRect = new Rect();
  
  DropDownListView(Context paramContext, boolean paramBoolean) {
    super(paramContext, null, R.attr.dropDownListViewStyle);
    this.mHijackFocus = paramBoolean;
    setCacheColorHint(0);
    try {
      Field field = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
      this.mIsChildViewEnabled = field;
      field.setAccessible(true);
    } catch (NoSuchFieldException noSuchFieldException) {
      noSuchFieldException.printStackTrace();
    } 
  }
  
  private void clearPressedItem() {
    this.mDrawsInPressedState = false;
    setPressed(false);
    drawableStateChanged();
    View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
    if (view != null)
      view.setPressed(false); 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mClickAnimation;
    if (viewPropertyAnimatorCompat != null) {
      viewPropertyAnimatorCompat.cancel();
      this.mClickAnimation = null;
    } 
  }
  
  private void clickPressedItem(View paramView, int paramInt) {
    performItemClick(paramView, paramInt, getItemIdAtPosition(paramInt));
  }
  
  private void drawSelectorCompat(Canvas paramCanvas) {
    if (!this.mSelectorRect.isEmpty()) {
      Drawable drawable = getSelector();
      if (drawable != null) {
        drawable.setBounds(this.mSelectorRect);
        drawable.draw(paramCanvas);
      } 
    } 
  }
  
  private void positionSelectorCompat(int paramInt, View paramView) {
    Rect rect = this.mSelectorRect;
    rect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    rect.left -= this.mSelectionLeftPadding;
    rect.top -= this.mSelectionTopPadding;
    rect.right += this.mSelectionRightPadding;
    rect.bottom += this.mSelectionBottomPadding;
    try {
      boolean bool = this.mIsChildViewEnabled.getBoolean(this);
      if (paramView.isEnabled() != bool) {
        Field field = this.mIsChildViewEnabled;
        if (!bool) {
          bool = true;
        } else {
          bool = false;
        } 
        field.set(this, Boolean.valueOf(bool));
        if (paramInt != -1)
          refreshDrawableState(); 
      } 
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } 
  }
  
  private void positionSelectorLikeFocusCompat(int paramInt, View paramView) {
    boolean bool2;
    Drawable drawable = getSelector();
    boolean bool1 = true;
    if (drawable != null && paramInt != -1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool2)
      drawable.setVisible(false, false); 
    positionSelectorCompat(paramInt, paramView);
    if (bool2) {
      Rect rect = this.mSelectorRect;
      float f1 = rect.exactCenterX();
      float f2 = rect.exactCenterY();
      if (getVisibility() != 0)
        bool1 = false; 
      drawable.setVisible(bool1, false);
      DrawableCompat.setHotspot(drawable, f1, f2);
    } 
  }
  
  private void positionSelectorLikeTouchCompat(int paramInt, View paramView, float paramFloat1, float paramFloat2) {
    positionSelectorLikeFocusCompat(paramInt, paramView);
    Drawable drawable = getSelector();
    if (drawable != null && paramInt != -1)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
  }
  
  private void setPressedItem(View paramView, int paramInt, float paramFloat1, float paramFloat2) {
    this.mDrawsInPressedState = true;
    if (Build.VERSION.SDK_INT >= 21)
      drawableHotspotChanged(paramFloat1, paramFloat2); 
    if (!isPressed())
      setPressed(true); 
    layoutChildren();
    int i = this.mMotionPosition;
    if (i != -1) {
      View view = getChildAt(i - getFirstVisiblePosition());
      if (view != null && view != paramView && view.isPressed())
        view.setPressed(false); 
    } 
    this.mMotionPosition = paramInt;
    float f1 = paramView.getLeft();
    float f2 = paramView.getTop();
    if (Build.VERSION.SDK_INT >= 21)
      paramView.drawableHotspotChanged(paramFloat1 - f1, paramFloat2 - f2); 
    if (!paramView.isPressed())
      paramView.setPressed(true); 
    positionSelectorLikeTouchCompat(paramInt, paramView, paramFloat1, paramFloat2);
    setSelectorEnabled(false);
    refreshDrawableState();
  }
  
  private void setSelectorEnabled(boolean paramBoolean) {
    GateKeeperDrawable gateKeeperDrawable = this.mSelector;
    if (gateKeeperDrawable != null)
      gateKeeperDrawable.setEnabled(paramBoolean); 
  }
  
  private boolean touchModeDrawsInPressedStateCompat() {
    return this.mDrawsInPressedState;
  }
  
  private void updateSelectorStateCompat() {
    Drawable drawable = getSelector();
    if (drawable != null && touchModeDrawsInPressedStateCompat() && isPressed())
      drawable.setState(getDrawableState()); 
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    drawSelectorCompat(paramCanvas);
    super.dispatchDraw(paramCanvas);
  }
  
  protected void drawableStateChanged() {
    if (this.mResolveHoverRunnable != null)
      return; 
    super.drawableStateChanged();
    setSelectorEnabled(true);
    updateSelectorStateCompat();
  }
  
  public boolean hasFocus() {
    return (this.mHijackFocus || super.hasFocus());
  }
  
  public boolean hasWindowFocus() {
    return (this.mHijackFocus || super.hasWindowFocus());
  }
  
  public boolean isFocused() {
    return (this.mHijackFocus || super.isFocused());
  }
  
  public boolean isInTouchMode() {
    boolean bool;
    if ((this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int lookForSelectablePosition(int paramInt, boolean paramBoolean) {
    ListAdapter listAdapter = getAdapter();
    if (listAdapter == null || isInTouchMode())
      return -1; 
    int i = listAdapter.getCount();
    if (!getAdapter().areAllItemsEnabled()) {
      int j;
      if (paramBoolean) {
        paramInt = Math.max(0, paramInt);
        while (true) {
          j = paramInt;
          if (paramInt < i) {
            j = paramInt;
            if (!listAdapter.isEnabled(paramInt)) {
              paramInt++;
              continue;
            } 
          } 
          break;
        } 
      } else {
        paramInt = Math.min(paramInt, i - 1);
        while (true) {
          j = paramInt;
          if (paramInt >= 0) {
            j = paramInt;
            if (!listAdapter.isEnabled(paramInt)) {
              paramInt--;
              continue;
            } 
          } 
          break;
        } 
      } 
      return (j < 0 || j >= i) ? -1 : j;
    } 
    return (paramInt < 0 || paramInt >= i) ? -1 : paramInt;
  }
  
  public int measureHeightOfChildrenCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int i = getListPaddingTop();
    int j = getListPaddingBottom();
    getListPaddingLeft();
    getListPaddingRight();
    int k = getDividerHeight();
    Drawable drawable = getDivider();
    ListAdapter listAdapter = getAdapter();
    if (listAdapter == null)
      return i + j; 
    paramInt3 = i + j;
    if (k <= 0 || drawable == null)
      k = 0; 
    paramInt2 = 0;
    drawable = null;
    int m = 0;
    int n = listAdapter.getCount();
    byte b = 0;
    while (b < n) {
      int i1 = listAdapter.getItemViewType(b);
      int i2 = m;
      if (i1 != m) {
        drawable = null;
        i2 = i1;
      } 
      View view2 = listAdapter.getView(b, (View)drawable, (ViewGroup)this);
      ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
      if (layoutParams == null) {
        layoutParams = generateDefaultLayoutParams();
        view2.setLayoutParams(layoutParams);
      } 
      if (layoutParams.height > 0) {
        m = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
      } else {
        m = View.MeasureSpec.makeMeasureSpec(0, 0);
      } 
      view2.measure(paramInt1, m);
      view2.forceLayout();
      m = paramInt3;
      if (b > 0)
        m = paramInt3 + k; 
      paramInt3 = m + view2.getMeasuredHeight();
      if (paramInt3 >= paramInt4) {
        if (paramInt5 < 0 || b <= paramInt5 || paramInt2 <= 0 || paramInt3 == paramInt4)
          paramInt2 = paramInt4; 
        return paramInt2;
      } 
      m = paramInt2;
      if (paramInt5 >= 0) {
        m = paramInt2;
        if (b >= paramInt5)
          m = paramInt3; 
      } 
      b++;
      paramInt2 = m;
      View view1 = view2;
      m = i2;
    } 
    return paramInt3;
  }
  
  protected void onDetachedFromWindow() {
    this.mResolveHoverRunnable = null;
    super.onDetachedFromWindow();
  }
  
  public boolean onForwardedEvent(MotionEvent paramMotionEvent, int paramInt) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iconst_1
    //   3: istore #4
    //   5: iconst_0
    //   6: istore #5
    //   8: aload_1
    //   9: invokevirtual getActionMasked : ()I
    //   12: istore #6
    //   14: iload #6
    //   16: iconst_1
    //   17: if_icmpeq -> 50
    //   20: iload #6
    //   22: iconst_2
    //   23: if_icmpeq -> 53
    //   26: iload #6
    //   28: iconst_3
    //   29: if_icmpeq -> 41
    //   32: iload_3
    //   33: istore #4
    //   35: iload #5
    //   37: istore_2
    //   38: goto -> 165
    //   41: iconst_0
    //   42: istore #4
    //   44: iload #5
    //   46: istore_2
    //   47: goto -> 165
    //   50: iconst_0
    //   51: istore #4
    //   53: aload_1
    //   54: iload_2
    //   55: invokevirtual findPointerIndex : (I)I
    //   58: istore #7
    //   60: iload #7
    //   62: ifge -> 74
    //   65: iconst_0
    //   66: istore #4
    //   68: iload #5
    //   70: istore_2
    //   71: goto -> 165
    //   74: aload_1
    //   75: iload #7
    //   77: invokevirtual getX : (I)F
    //   80: f2i
    //   81: istore_2
    //   82: aload_1
    //   83: iload #7
    //   85: invokevirtual getY : (I)F
    //   88: f2i
    //   89: istore #8
    //   91: aload_0
    //   92: iload_2
    //   93: iload #8
    //   95: invokevirtual pointToPosition : (II)I
    //   98: istore #7
    //   100: iload #7
    //   102: iconst_m1
    //   103: if_icmpne -> 111
    //   106: iconst_1
    //   107: istore_2
    //   108: goto -> 165
    //   111: aload_0
    //   112: iload #7
    //   114: aload_0
    //   115: invokevirtual getFirstVisiblePosition : ()I
    //   118: isub
    //   119: invokevirtual getChildAt : (I)Landroid/view/View;
    //   122: astore #9
    //   124: aload_0
    //   125: aload #9
    //   127: iload #7
    //   129: iload_2
    //   130: i2f
    //   131: iload #8
    //   133: i2f
    //   134: invokespecial setPressedItem : (Landroid/view/View;IFF)V
    //   137: iconst_1
    //   138: istore_3
    //   139: iload_3
    //   140: istore #4
    //   142: iload #5
    //   144: istore_2
    //   145: iload #6
    //   147: iconst_1
    //   148: if_icmpne -> 165
    //   151: aload_0
    //   152: aload #9
    //   154: iload #7
    //   156: invokespecial clickPressedItem : (Landroid/view/View;I)V
    //   159: iload #5
    //   161: istore_2
    //   162: iload_3
    //   163: istore #4
    //   165: iload #4
    //   167: ifeq -> 174
    //   170: iload_2
    //   171: ifeq -> 178
    //   174: aload_0
    //   175: invokespecial clearPressedItem : ()V
    //   178: iload #4
    //   180: ifeq -> 224
    //   183: aload_0
    //   184: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   187: ifnonnull -> 202
    //   190: aload_0
    //   191: new android/support/v4/widget/ListViewAutoScrollHelper
    //   194: dup
    //   195: aload_0
    //   196: invokespecial <init> : (Landroid/widget/ListView;)V
    //   199: putfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   202: aload_0
    //   203: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   206: iconst_1
    //   207: invokevirtual setEnabled : (Z)Landroid/support/v4/widget/AutoScrollHelper;
    //   210: pop
    //   211: aload_0
    //   212: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   215: aload_0
    //   216: aload_1
    //   217: invokevirtual onTouch : (Landroid/view/View;Landroid/view/MotionEvent;)Z
    //   220: pop
    //   221: goto -> 239
    //   224: aload_0
    //   225: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
    //   228: astore_1
    //   229: aload_1
    //   230: ifnull -> 239
    //   233: aload_1
    //   234: iconst_0
    //   235: invokevirtual setEnabled : (Z)Landroid/support/v4/widget/AutoScrollHelper;
    //   238: pop
    //   239: iload #4
    //   241: ireturn
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    if (Build.VERSION.SDK_INT < 26)
      return super.onHoverEvent(paramMotionEvent); 
    int i = paramMotionEvent.getActionMasked();
    if (i == 10 && this.mResolveHoverRunnable == null) {
      ResolveHoverRunnable resolveHoverRunnable = new ResolveHoverRunnable();
      this.mResolveHoverRunnable = resolveHoverRunnable;
      resolveHoverRunnable.post();
    } 
    boolean bool = super.onHoverEvent(paramMotionEvent);
    if (i == 9 || i == 7) {
      i = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
      if (i != -1 && i != getSelectedItemPosition()) {
        View view = getChildAt(i - getFirstVisiblePosition());
        if (view.isEnabled())
          setSelectionFromTop(i, view.getTop() - getTop()); 
        updateSelectorStateCompat();
      } 
      return bool;
    } 
    setSelection(-1);
    return bool;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0)
      this.mMotionPosition = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()); 
    ResolveHoverRunnable resolveHoverRunnable = this.mResolveHoverRunnable;
    if (resolveHoverRunnable != null)
      resolveHoverRunnable.cancel(); 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  void setListSelectionHidden(boolean paramBoolean) {
    this.mListSelectionHidden = paramBoolean;
  }
  
  public void setSelector(Drawable paramDrawable) {
    GateKeeperDrawable gateKeeperDrawable;
    if (paramDrawable != null) {
      gateKeeperDrawable = new GateKeeperDrawable(paramDrawable);
    } else {
      gateKeeperDrawable = null;
    } 
    this.mSelector = gateKeeperDrawable;
    super.setSelector((Drawable)gateKeeperDrawable);
    Rect rect = new Rect();
    if (paramDrawable != null)
      paramDrawable.getPadding(rect); 
    this.mSelectionLeftPadding = rect.left;
    this.mSelectionTopPadding = rect.top;
    this.mSelectionRightPadding = rect.right;
    this.mSelectionBottomPadding = rect.bottom;
  }
  
  private static class GateKeeperDrawable extends DrawableWrapper {
    private boolean mEnabled = true;
    
    GateKeeperDrawable(Drawable param1Drawable) {
      super(param1Drawable);
    }
    
    public void draw(Canvas param1Canvas) {
      if (this.mEnabled)
        super.draw(param1Canvas); 
    }
    
    void setEnabled(boolean param1Boolean) {
      this.mEnabled = param1Boolean;
    }
    
    public void setHotspot(float param1Float1, float param1Float2) {
      if (this.mEnabled)
        super.setHotspot(param1Float1, param1Float2); 
    }
    
    public void setHotspotBounds(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (this.mEnabled)
        super.setHotspotBounds(param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    public boolean setState(int[] param1ArrayOfint) {
      return this.mEnabled ? super.setState(param1ArrayOfint) : false;
    }
    
    public boolean setVisible(boolean param1Boolean1, boolean param1Boolean2) {
      return this.mEnabled ? super.setVisible(param1Boolean1, param1Boolean2) : false;
    }
  }
  
  private class ResolveHoverRunnable implements Runnable {
    public void cancel() {
      DropDownListView.this.mResolveHoverRunnable = null;
      DropDownListView.this.removeCallbacks(this);
    }
    
    public void post() {
      DropDownListView.this.post(this);
    }
    
    public void run() {
      DropDownListView.this.mResolveHoverRunnable = null;
      DropDownListView.this.drawableStateChanged();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/DropDownListView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */