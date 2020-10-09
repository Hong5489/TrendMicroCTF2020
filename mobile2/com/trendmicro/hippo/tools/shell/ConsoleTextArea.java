package com.trendmicro.hippo.tools.shell;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ConsoleTextArea extends JTextArea implements KeyListener, DocumentListener {
  static final long serialVersionUID = 8557083244830872961L;
  
  private ConsoleWriter console1 = new ConsoleWriter(this);
  
  private ConsoleWriter console2 = new ConsoleWriter(this);
  
  private PrintStream err = new PrintStream(this.console2, true);
  
  private List<String> history = new ArrayList<>();
  
  private int historyIndex = -1;
  
  private PipedInputStream in;
  
  private PrintWriter inPipe;
  
  private PrintStream out = new PrintStream(this.console1, true);
  
  private int outputMark = 0;
  
  public ConsoleTextArea(String[] paramArrayOfString) {
    PipedOutputStream pipedOutputStream = new PipedOutputStream();
    this.inPipe = new PrintWriter(pipedOutputStream);
    PipedInputStream pipedInputStream = new PipedInputStream();
    this.in = pipedInputStream;
    try {
      pipedOutputStream.connect(pipedInputStream);
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    getDocument().addDocumentListener(this);
    addKeyListener(this);
    setLineWrap(true);
    setFont(new Font("Monospaced", 0, 12));
  }
  
  public void changedUpdate(DocumentEvent paramDocumentEvent) {
    /* monitor enter ThisExpression{ObjectType{com/trendmicro/hippo/tools/shell/ConsoleTextArea}} */
    /* monitor exit ThisExpression{ObjectType{com/trendmicro/hippo/tools/shell/ConsoleTextArea}} */
  }
  
  public void eval(String paramString) {
    this.inPipe.write(paramString);
    this.inPipe.write("\n");
    this.inPipe.flush();
    this.console1.flush();
  }
  
  public PrintStream getErr() {
    return this.err;
  }
  
  public InputStream getIn() {
    return this.in;
  }
  
  public PrintStream getOut() {
    return this.out;
  }
  
  public void insertUpdate(DocumentEvent paramDocumentEvent) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokeinterface getLength : ()I
    //   8: istore_2
    //   9: aload_1
    //   10: invokeinterface getOffset : ()I
    //   15: istore_3
    //   16: aload_0
    //   17: getfield outputMark : I
    //   20: iload_3
    //   21: if_icmple -> 34
    //   24: aload_0
    //   25: aload_0
    //   26: getfield outputMark : I
    //   29: iload_2
    //   30: iadd
    //   31: putfield outputMark : I
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	37	finally
  }
  
  public void keyPressed(KeyEvent paramKeyEvent) {
    int i = paramKeyEvent.getKeyCode();
    if (i == 8 || i == 37) {
      if (this.outputMark == getCaretPosition())
        paramKeyEvent.consume(); 
      return;
    } 
    if (i == 36) {
      i = getCaretPosition();
      int j = this.outputMark;
      if (i == j) {
        paramKeyEvent.consume();
      } else if (i > j && !paramKeyEvent.isControlDown()) {
        if (paramKeyEvent.isShiftDown()) {
          moveCaretPosition(this.outputMark);
        } else {
          setCaretPosition(this.outputMark);
        } 
        paramKeyEvent.consume();
      } 
    } else if (i == 10) {
      returnPressed();
      paramKeyEvent.consume();
    } else if (i == 38) {
      i = this.historyIndex - 1;
      this.historyIndex = i;
      if (i >= 0) {
        if (i >= this.history.size())
          this.historyIndex = this.history.size() - 1; 
        i = this.historyIndex;
        if (i >= 0) {
          String str = this.history.get(i);
          i = getDocument().getLength();
          replaceRange(str, this.outputMark, i);
          i = this.outputMark + str.length();
          select(i, i);
        } else {
          this.historyIndex = i + 1;
        } 
      } else {
        this.historyIndex = i + 1;
      } 
      paramKeyEvent.consume();
    } else if (i == 40) {
      int j = this.outputMark;
      i = j;
      if (this.history.size() > 0) {
        i = this.historyIndex + 1;
        this.historyIndex = i;
        if (i < 0)
          this.historyIndex = 0; 
        i = getDocument().getLength();
        if (this.historyIndex < this.history.size()) {
          String str = this.history.get(this.historyIndex);
          replaceRange(str, this.outputMark, i);
          i = this.outputMark + str.length();
        } else {
          this.historyIndex = this.history.size();
          replaceRange("", this.outputMark, i);
          i = j;
        } 
      } 
      select(i, i);
      paramKeyEvent.consume();
    } 
  }
  
  public void keyReleased(KeyEvent paramKeyEvent) {
    /* monitor enter ThisExpression{ObjectType{com/trendmicro/hippo/tools/shell/ConsoleTextArea}} */
    /* monitor exit ThisExpression{ObjectType{com/trendmicro/hippo/tools/shell/ConsoleTextArea}} */
  }
  
  public void keyTyped(KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getKeyChar() == '\b') {
      if (this.outputMark == getCaretPosition())
        paramKeyEvent.consume(); 
    } else {
      int i = getCaretPosition();
      int j = this.outputMark;
      if (i < j)
        setCaretPosition(j); 
    } 
  }
  
  public void postUpdateUI() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual requestFocus : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: invokevirtual getCaret : ()Ljavax/swing/text/Caret;
    //   11: invokevirtual setCaret : (Ljavax/swing/text/Caret;)V
    //   14: aload_0
    //   15: aload_0
    //   16: getfield outputMark : I
    //   19: aload_0
    //   20: getfield outputMark : I
    //   23: invokevirtual select : (II)V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	29	finally
  }
  
  public void removeUpdate(DocumentEvent paramDocumentEvent) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokeinterface getLength : ()I
    //   8: istore_2
    //   9: aload_1
    //   10: invokeinterface getOffset : ()I
    //   15: istore_3
    //   16: aload_0
    //   17: getfield outputMark : I
    //   20: iload_3
    //   21: if_icmple -> 52
    //   24: aload_0
    //   25: getfield outputMark : I
    //   28: iload_3
    //   29: iload_2
    //   30: iadd
    //   31: if_icmplt -> 47
    //   34: aload_0
    //   35: aload_0
    //   36: getfield outputMark : I
    //   39: iload_2
    //   40: isub
    //   41: putfield outputMark : I
    //   44: goto -> 52
    //   47: aload_0
    //   48: iload_3
    //   49: putfield outputMark : I
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	44	55	finally
    //   47	52	55	finally
  }
  
  void returnPressed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getDocument : ()Ljavax/swing/text/Document;
    //   6: astore_1
    //   7: aload_1
    //   8: invokeinterface getLength : ()I
    //   13: istore_2
    //   14: new javax/swing/text/Segment
    //   17: astore_3
    //   18: aload_3
    //   19: invokespecial <init> : ()V
    //   22: aload_1
    //   23: aload_0
    //   24: getfield outputMark : I
    //   27: iload_2
    //   28: aload_0
    //   29: getfield outputMark : I
    //   32: isub
    //   33: aload_3
    //   34: invokeinterface getText : (IILjavax/swing/text/Segment;)V
    //   39: goto -> 49
    //   42: astore #4
    //   44: aload #4
    //   46: invokevirtual printStackTrace : ()V
    //   49: aload_3
    //   50: getfield count : I
    //   53: ifle -> 70
    //   56: aload_0
    //   57: getfield history : Ljava/util/List;
    //   60: aload_3
    //   61: invokevirtual toString : ()Ljava/lang/String;
    //   64: invokeinterface add : (Ljava/lang/Object;)Z
    //   69: pop
    //   70: aload_0
    //   71: aload_0
    //   72: getfield history : Ljava/util/List;
    //   75: invokeinterface size : ()I
    //   80: putfield historyIndex : I
    //   83: aload_0
    //   84: getfield inPipe : Ljava/io/PrintWriter;
    //   87: aload_3
    //   88: getfield array : [C
    //   91: aload_3
    //   92: getfield offset : I
    //   95: aload_3
    //   96: getfield count : I
    //   99: invokevirtual write : ([CII)V
    //   102: aload_0
    //   103: ldc '\\n'
    //   105: invokevirtual append : (Ljava/lang/String;)V
    //   108: aload_0
    //   109: aload_1
    //   110: invokeinterface getLength : ()I
    //   115: putfield outputMark : I
    //   118: aload_0
    //   119: getfield inPipe : Ljava/io/PrintWriter;
    //   122: ldc '\\n'
    //   124: invokevirtual write : (Ljava/lang/String;)V
    //   127: aload_0
    //   128: getfield inPipe : Ljava/io/PrintWriter;
    //   131: invokevirtual flush : ()V
    //   134: aload_0
    //   135: getfield console1 : Lcom/trendmicro/hippo/tools/shell/ConsoleWriter;
    //   138: invokevirtual flush : ()V
    //   141: aload_0
    //   142: monitorexit
    //   143: return
    //   144: astore #4
    //   146: aload_0
    //   147: monitorexit
    //   148: aload #4
    //   150: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	144	finally
    //   22	39	42	javax/swing/text/BadLocationException
    //   22	39	144	finally
    //   44	49	144	finally
    //   49	70	144	finally
    //   70	141	144	finally
  }
  
  public void select(int paramInt1, int paramInt2) {
    requestFocus();
    super.select(paramInt1, paramInt2);
  }
  
  public void write(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_0
    //   5: getfield outputMark : I
    //   8: invokevirtual insert : (Ljava/lang/String;I)V
    //   11: aload_1
    //   12: invokevirtual length : ()I
    //   15: istore_2
    //   16: aload_0
    //   17: getfield outputMark : I
    //   20: iload_2
    //   21: iadd
    //   22: istore_2
    //   23: aload_0
    //   24: iload_2
    //   25: putfield outputMark : I
    //   28: aload_0
    //   29: iload_2
    //   30: iload_2
    //   31: invokevirtual select : (II)V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	37	finally
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/ConsoleTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */