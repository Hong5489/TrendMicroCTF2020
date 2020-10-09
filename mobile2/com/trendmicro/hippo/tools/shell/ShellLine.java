package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Scriptable;
import java.io.InputStream;
import java.nio.charset.Charset;

@Deprecated
public class ShellLine {
  @Deprecated
  public static InputStream getStream(Scriptable paramScriptable) {
    ShellConsole shellConsole = ShellConsole.getConsole(paramScriptable, Charset.defaultCharset());
    if (shellConsole != null) {
      InputStream inputStream = shellConsole.getIn();
    } else {
      shellConsole = null;
    } 
    return (InputStream)shellConsole;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/ShellLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */