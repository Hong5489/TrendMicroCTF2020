package android.support.design.theme;

import android.content.Context;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatViewInflater;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class MaterialComponentsViewInflater extends AppCompatViewInflater {
  protected AppCompatButton createButton(Context paramContext, AttributeSet paramAttributeSet) {
    return (AppCompatButton)new MaterialButton(paramContext, paramAttributeSet);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/theme/MaterialComponentsViewInflater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */