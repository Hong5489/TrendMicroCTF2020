package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class PipeThread extends Thread {
  private InputStream from;
  
  private boolean fromProcess;
  
  private OutputStream to;
  
  PipeThread(boolean paramBoolean, InputStream paramInputStream, OutputStream paramOutputStream) {
    setDaemon(true);
    this.fromProcess = paramBoolean;
    this.from = paramInputStream;
    this.to = paramOutputStream;
  }
  
  public void run() {
    try {
      Global.pipe(this.fromProcess, this.from, this.to);
      return;
    } catch (IOException iOException) {
      throw Context.throwAsScriptRuntimeEx(iOException);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/PipeThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */