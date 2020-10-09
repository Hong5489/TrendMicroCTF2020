package com.trendmicro.hippo.tools.debugger;

class RunProxy implements Runnable {
  static final int ENTER_INTERRUPT = 4;
  
  static final int LOAD_FILE = 2;
  
  static final int OPEN_FILE = 1;
  
  static final int UPDATE_SOURCE_TEXT = 3;
  
  String alertMessage;
  
  private SwingGui debugGui;
  
  String fileName;
  
  Dim.StackFrame lastFrame;
  
  Dim.SourceInfo sourceInfo;
  
  String text;
  
  String threadTitle;
  
  private int type;
  
  public RunProxy(SwingGui paramSwingGui, int paramInt) {
    this.debugGui = paramSwingGui;
    this.type = paramInt;
  }
  
  public void run() {
    int i = this.type;
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 4) {
            this.debugGui.enterInterruptImpl(this.lastFrame, this.threadTitle, this.alertMessage);
          } else {
            throw new IllegalArgumentException(String.valueOf(this.type));
          } 
        } else {
          String str = this.sourceInfo.url();
          if (!this.debugGui.updateFileWindow(this.sourceInfo) && !str.equals("<stdin>"))
            this.debugGui.createFileWindow(this.sourceInfo, -1); 
        } 
      } else {
        try {
          this.debugGui.dim.evalScript(this.fileName, this.text);
        } catch (RuntimeException runtimeException) {
          SwingGui swingGui = this.debugGui;
          String str = runtimeException.getMessage();
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Run error for ");
          stringBuilder.append(this.fileName);
          MessageDialogWrapper.showMessageDialog(swingGui, str, stringBuilder.toString(), 0);
        } 
      } 
    } else {
      try {
        this.debugGui.dim.compileScript(this.fileName, this.text);
      } catch (RuntimeException runtimeException) {
        SwingGui swingGui = this.debugGui;
        String str = runtimeException.getMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error Compiling ");
        stringBuilder.append(this.fileName);
        MessageDialogWrapper.showMessageDialog(swingGui, str, stringBuilder.toString(), 0);
      } 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/RunProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */