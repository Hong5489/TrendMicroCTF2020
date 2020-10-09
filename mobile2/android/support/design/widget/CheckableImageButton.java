package android.support.design.widget;

import android.content.Context;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

public class CheckableImageButton extends AppCompatImageButton implements Checkable {
  private static final int[] DRAWABLE_STATE_CHECKED = new int[] { 16842912 };
  
  private boolean checked;
  
  public CheckableImageButton(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CheckableImageButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.imageButtonStyle);
  }
  
  public CheckableImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat() {
          public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
            super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
            param1AccessibilityEvent.setChecked(CheckableImageButton.this.isChecked());
          }
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            param1AccessibilityNodeInfoCompat.setCheckable(true);
            param1AccessibilityNodeInfoCompat.setChecked(CheckableImageButton.this.isChecked());
          }
        });
  }
  
  public boolean isChecked() {
    return this.checked;
  }
  
  public int[] onCreateDrawableState(int paramInt) {
    return this.checked ? mergeDrawableStates(super.onCreateDrawableState(DRAWABLE_STATE_CHECKED.length + paramInt), DRAWABLE_STATE_CHECKED) : super.onCreateDrawableState(paramInt);
  }
  
  public void setChecked(boolean paramBoolean) {
    if (this.checked != paramBoolean) {
      this.checked = paramBoolean;
      refreshDrawableState();
      sendAccessibilityEvent(2048);
    } 
  }
  
  public void toggle() {
    setChecked(this.checked ^ true);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/CheckableImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */