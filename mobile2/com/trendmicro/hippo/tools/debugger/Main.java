package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.tools.shell.Global;
import java.awt.Dimension;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.JFrame;

public class Main {
  private SwingGui debugGui;
  
  private Dim dim = new Dim();
  
  public Main(String paramString) {
    this.debugGui = new SwingGui(this.dim, paramString);
  }
  
  public static void main(String[] paramArrayOfString) {
    Main main = new Main("Debugger");
    main.doBreak();
    main.setExitAction(new IProxy(1));
    System.setIn(main.getIn());
    System.setOut(main.getOut());
    System.setErr(main.getErr());
    Global global = com.trendmicro.hippo.tools.shell.Main.getGlobal();
    global.setIn(main.getIn());
    global.setOut(main.getOut());
    global.setErr(main.getErr());
    main.attachTo((ContextFactory)com.trendmicro.hippo.tools.shell.Main.shellContextFactory);
    main.setScope((Scriptable)global);
    main.pack();
    main.setSize(600, 460);
    main.setVisible(true);
    com.trendmicro.hippo.tools.shell.Main.exec(paramArrayOfString);
  }
  
  public static Main mainEmbedded(ContextFactory paramContextFactory, Scriptable paramScriptable, String paramString) {
    return mainEmbeddedImpl(paramContextFactory, paramScriptable, paramString);
  }
  
  public static Main mainEmbedded(ContextFactory paramContextFactory, ScopeProvider paramScopeProvider, String paramString) {
    return mainEmbeddedImpl(paramContextFactory, paramScopeProvider, paramString);
  }
  
  public static Main mainEmbedded(String paramString) {
    ContextFactory contextFactory = ContextFactory.getGlobal();
    Global global = new Global();
    global.init(contextFactory);
    return mainEmbedded(contextFactory, (Scriptable)global, paramString);
  }
  
  private static Main mainEmbeddedImpl(ContextFactory paramContextFactory, Object paramObject, String paramString) {
    String str = paramString;
    if (paramString == null)
      str = "Debugger (embedded usage)"; 
    Main main = new Main(str);
    main.doBreak();
    main.setExitAction(new IProxy(1));
    main.attachTo(paramContextFactory);
    if (paramObject instanceof ScopeProvider) {
      main.setScopeProvider((ScopeProvider)paramObject);
    } else {
      Scriptable scriptable = (Scriptable)paramObject;
      if (scriptable instanceof Global) {
        paramObject = scriptable;
        paramObject.setIn(main.getIn());
        paramObject.setOut(main.getOut());
        paramObject.setErr(main.getErr());
      } 
      main.setScope(scriptable);
    } 
    main.pack();
    main.setSize(600, 460);
    main.setVisible(true);
    return main;
  }
  
  public void attachTo(ContextFactory paramContextFactory) {
    this.dim.attachTo(paramContextFactory);
  }
  
  public void clearAllBreakpoints() {
    this.dim.clearAllBreakpoints();
  }
  
  @Deprecated
  public void contextCreated(Context paramContext) {
    throw new IllegalStateException();
  }
  
  @Deprecated
  public void contextEntered(Context paramContext) {
    throw new IllegalStateException();
  }
  
  @Deprecated
  public void contextExited(Context paramContext) {
    throw new IllegalStateException();
  }
  
  @Deprecated
  public void contextReleased(Context paramContext) {
    throw new IllegalStateException();
  }
  
  public void detach() {
    this.dim.detach();
  }
  
  public void dispose() {
    clearAllBreakpoints();
    this.dim.go();
    this.debugGui.dispose();
    this.dim = null;
  }
  
  public void doBreak() {
    this.dim.setBreak();
  }
  
  public JFrame getDebugFrame() {
    return this.debugGui;
  }
  
  public PrintStream getErr() {
    return this.debugGui.getConsole().getErr();
  }
  
  public InputStream getIn() {
    return this.debugGui.getConsole().getIn();
  }
  
  public PrintStream getOut() {
    return this.debugGui.getConsole().getOut();
  }
  
  public void go() {
    this.dim.go();
  }
  
  public boolean isVisible() {
    return this.debugGui.isVisible();
  }
  
  public void pack() {
    this.debugGui.pack();
  }
  
  public void setBreakOnEnter(boolean paramBoolean) {
    this.dim.setBreakOnEnter(paramBoolean);
    this.debugGui.getMenubar().getBreakOnEnter().setSelected(paramBoolean);
  }
  
  public void setBreakOnExceptions(boolean paramBoolean) {
    this.dim.setBreakOnExceptions(paramBoolean);
    this.debugGui.getMenubar().getBreakOnExceptions().setSelected(paramBoolean);
  }
  
  public void setBreakOnReturn(boolean paramBoolean) {
    this.dim.setBreakOnReturn(paramBoolean);
    this.debugGui.getMenubar().getBreakOnReturn().setSelected(paramBoolean);
  }
  
  public void setExitAction(Runnable paramRunnable) {
    this.debugGui.setExitAction(paramRunnable);
  }
  
  @Deprecated
  public void setOptimizationLevel(int paramInt) {}
  
  public void setScope(Scriptable paramScriptable) {
    setScopeProvider(IProxy.newScopeProvider(paramScriptable));
  }
  
  public void setScopeProvider(ScopeProvider paramScopeProvider) {
    this.dim.setScopeProvider(paramScopeProvider);
  }
  
  public void setSize(int paramInt1, int paramInt2) {
    this.debugGui.setSize(paramInt1, paramInt2);
  }
  
  @Deprecated
  public void setSize(Dimension paramDimension) {
    this.debugGui.setSize(paramDimension.width, paramDimension.height);
  }
  
  public void setSourceProvider(SourceProvider paramSourceProvider) {
    this.dim.setSourceProvider(paramSourceProvider);
  }
  
  public void setVisible(boolean paramBoolean) {
    this.debugGui.setVisible(paramBoolean);
  }
  
  private static class IProxy implements Runnable, ScopeProvider {
    public static final int EXIT_ACTION = 1;
    
    public static final int SCOPE_PROVIDER = 2;
    
    private Scriptable scope;
    
    private final int type;
    
    public IProxy(int param1Int) {
      this.type = param1Int;
    }
    
    public static ScopeProvider newScopeProvider(Scriptable param1Scriptable) {
      IProxy iProxy = new IProxy(2);
      iProxy.scope = param1Scriptable;
      return iProxy;
    }
    
    public Scriptable getScope() {
      if (this.type != 2)
        Kit.codeBug(); 
      if (this.scope == null)
        Kit.codeBug(); 
      return this.scope;
    }
    
    public void run() {
      if (this.type != 1)
        Kit.codeBug(); 
      System.exit(0);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */