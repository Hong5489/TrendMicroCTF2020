package com.trendmicro.hippo.tools.debugger;

import java.awt.Component;
import javax.swing.JOptionPane;

class MessageDialogWrapper {
  public static void showMessageDialog(Component paramComponent, String paramString1, String paramString2, int paramInt) {
    String str = paramString1;
    if (paramString1.length() > 60) {
      StringBuilder stringBuilder = new StringBuilder();
      int i = paramString1.length();
      int j = 0;
      byte b = 0;
      while (b < i) {
        char c = paramString1.charAt(b);
        stringBuilder.append(c);
        int k = j;
        if (Character.isWhitespace(c)) {
          int m;
          for (m = b + 1; m < i && !Character.isWhitespace(paramString1.charAt(m)); m++);
          k = j;
          if (m < i) {
            k = j;
            if (j + m - b > 60) {
              stringBuilder.append('\n');
              k = 0;
            } 
          } 
        } 
        b++;
        j = k + 1;
      } 
      str = stringBuilder.toString();
    } 
    JOptionPane.showMessageDialog(paramComponent, str, paramString2, paramInt);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/MessageDialogWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */