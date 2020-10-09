package com.trendmicro.hippo.tools.debugger;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

class EvalTextArea extends JTextArea implements KeyListener, DocumentListener {
  private static final long serialVersionUID = -3918033649601064194L;
  
  private SwingGui debugGui;
  
  private List<String> history;
  
  private int historyIndex = -1;
  
  private int outputMark;
  
  public EvalTextArea(SwingGui paramSwingGui) {
    this.debugGui = paramSwingGui;
    this.history = Collections.synchronizedList(new ArrayList<>());
    Document document = getDocument();
    document.addDocumentListener(this);
    addKeyListener(this);
    setLineWrap(true);
    setFont(new Font("Monospaced", 0, 12));
    append("% ");
    this.outputMark = document.getLength();
  }
  
  private void returnPressed() {
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
    //   50: invokevirtual toString : ()Ljava/lang/String;
    //   53: astore #4
    //   55: aload_0
    //   56: getfield debugGui : Lcom/trendmicro/hippo/tools/debugger/SwingGui;
    //   59: getfield dim : Lcom/trendmicro/hippo/tools/debugger/Dim;
    //   62: aload #4
    //   64: invokevirtual stringIsCompilableUnit : (Ljava/lang/String;)Z
    //   67: ifeq -> 165
    //   70: aload #4
    //   72: invokevirtual trim : ()Ljava/lang/String;
    //   75: invokevirtual length : ()I
    //   78: ifle -> 106
    //   81: aload_0
    //   82: getfield history : Ljava/util/List;
    //   85: aload #4
    //   87: invokeinterface add : (Ljava/lang/Object;)Z
    //   92: pop
    //   93: aload_0
    //   94: aload_0
    //   95: getfield history : Ljava/util/List;
    //   98: invokeinterface size : ()I
    //   103: putfield historyIndex : I
    //   106: aload_0
    //   107: ldc '\\n'
    //   109: invokevirtual append : (Ljava/lang/String;)V
    //   112: aload_0
    //   113: getfield debugGui : Lcom/trendmicro/hippo/tools/debugger/SwingGui;
    //   116: getfield dim : Lcom/trendmicro/hippo/tools/debugger/Dim;
    //   119: aload #4
    //   121: invokevirtual eval : (Ljava/lang/String;)Ljava/lang/String;
    //   124: astore #4
    //   126: aload #4
    //   128: invokevirtual length : ()I
    //   131: ifle -> 146
    //   134: aload_0
    //   135: aload #4
    //   137: invokevirtual append : (Ljava/lang/String;)V
    //   140: aload_0
    //   141: ldc '\\n'
    //   143: invokevirtual append : (Ljava/lang/String;)V
    //   146: aload_0
    //   147: ldc '% '
    //   149: invokevirtual append : (Ljava/lang/String;)V
    //   152: aload_0
    //   153: aload_1
    //   154: invokeinterface getLength : ()I
    //   159: putfield outputMark : I
    //   162: goto -> 171
    //   165: aload_0
    //   166: ldc '\\n'
    //   168: invokevirtual append : (Ljava/lang/String;)V
    //   171: aload_0
    //   172: monitorexit
    //   173: return
    //   174: astore_1
    //   175: aload_0
    //   176: monitorexit
    //   177: aload_1
    //   178: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	174	finally
    //   22	39	42	javax/swing/text/BadLocationException
    //   22	39	174	finally
    //   44	49	174	finally
    //   49	106	174	finally
    //   106	146	174	finally
    //   146	162	174	finally
    //   165	171	174	finally
  }
  
  public void changedUpdate(DocumentEvent paramDocumentEvent) {
    /* monitor enter ThisExpression{ObjectType{com/trendmicro/hippo/tools/debugger/EvalTextArea}} */
    /* monitor exit ThisExpression{ObjectType{com/trendmicro/hippo/tools/debugger/EvalTextArea}} */
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
      int j = getCaretPosition();
      i = this.outputMark;
      if (j == i) {
        paramKeyEvent.consume();
      } else if (j > i && !paramKeyEvent.isControlDown()) {
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
    /* monitor enter ThisExpression{ObjectType{com/trendmicro/hippo/tools/debugger/EvalTextArea}} */
    /* monitor exit ThisExpression{ObjectType{com/trendmicro/hippo/tools/debugger/EvalTextArea}} */
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
    //   3: aload_0
    //   4: invokevirtual getCaret : ()Ljavax/swing/text/Caret;
    //   7: invokevirtual setCaret : (Ljavax/swing/text/Caret;)V
    //   10: aload_0
    //   11: aload_0
    //   12: getfield outputMark : I
    //   15: aload_0
    //   16: getfield outputMark : I
    //   19: invokevirtual select : (II)V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
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
  
  public void select(int paramInt1, int paramInt2) {
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


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/EvalTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */