package android.support.design.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

public class TabItem extends View {
  public final int customLayout;
  
  public final Drawable icon;
  
  public final CharSequence text;
  
  public TabItem(Context paramContext) {
    this(paramContext, null);
  }
  
  public TabItem(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.TabItem);
    this.text = tintTypedArray.getText(R.styleable.TabItem_android_text);
    this.icon = tintTypedArray.getDrawable(R.styleable.TabItem_android_icon);
    this.customLayout = tintTypedArray.getResourceId(R.styleable.TabItem_android_layout, 0);
    tintTypedArray.recycle();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/TabItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */