package com.trendmicro.hippo.tools.shell;

class ConsoleWrite implements Runnable {
  private String str;
  
  private ConsoleTextArea textArea;
  
  public ConsoleWrite(ConsoleTextArea paramConsoleTextArea, String paramString) {
    this.textArea = paramConsoleTextArea;
    this.str = paramString;
  }
  
  public void run() {
    this.textArea.write(this.str);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/ConsoleWrite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */