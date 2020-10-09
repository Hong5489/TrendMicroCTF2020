package com.trendmicro.hippo.tools.shell;

import java.io.OutputStream;
import javax.swing.SwingUtilities;

class ConsoleWriter extends OutputStream {
  private StringBuffer buffer;
  
  private ConsoleTextArea textArea;
  
  public ConsoleWriter(ConsoleTextArea paramConsoleTextArea) {
    this.textArea = paramConsoleTextArea;
    this.buffer = new StringBuffer();
  }
  
  private void flushBuffer() {
    String str = this.buffer.toString();
    this.buffer.setLength(0);
    SwingUtilities.invokeLater(new ConsoleWrite(this.textArea, str));
  }
  
  public void close() {
    flush();
  }
  
  public void flush() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buffer : Ljava/lang/StringBuffer;
    //   6: invokevirtual length : ()I
    //   9: ifle -> 16
    //   12: aload_0
    //   13: invokespecial flushBuffer : ()V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  public void write(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buffer : Ljava/lang/StringBuffer;
    //   6: iload_1
    //   7: i2c
    //   8: invokevirtual append : (C)Ljava/lang/StringBuffer;
    //   11: pop
    //   12: iload_1
    //   13: bipush #10
    //   15: if_icmpne -> 22
    //   18: aload_0
    //   19: invokespecial flushBuffer : ()V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	25	finally
    //   18	22	25	finally
  }
  
  public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_2
    //   3: iload_3
    //   4: if_icmpge -> 41
    //   7: aload_0
    //   8: getfield buffer : Ljava/lang/StringBuffer;
    //   11: aload_1
    //   12: iload_2
    //   13: caload
    //   14: invokevirtual append : (C)Ljava/lang/StringBuffer;
    //   17: pop
    //   18: aload_1
    //   19: iload_2
    //   20: caload
    //   21: bipush #10
    //   23: if_icmpne -> 30
    //   26: aload_0
    //   27: invokespecial flushBuffer : ()V
    //   30: iinc #2, 1
    //   33: goto -> 2
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    // Exception table:
    //   from	to	target	type
    //   7	18	36	finally
    //   26	30	36	finally
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/ConsoleWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */