package com.trendmicro.hippo;

public class Synchronizer extends Delegator {
  private Object syncObject;
  
  public Synchronizer(Scriptable paramScriptable) {
    super(paramScriptable);
  }
  
  public Synchronizer(Scriptable paramScriptable, Object paramObject) {
    super(paramScriptable);
    this.syncObject = paramObject;
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_0
    //   1: getfield syncObject : Ljava/lang/Object;
    //   4: astore #5
    //   6: aload #5
    //   8: ifnull -> 14
    //   11: goto -> 17
    //   14: aload_3
    //   15: astore #5
    //   17: aload #5
    //   19: instanceof com/trendmicro/hippo/Wrapper
    //   22: ifeq -> 40
    //   25: aload #5
    //   27: checkcast com/trendmicro/hippo/Wrapper
    //   30: invokeinterface unwrap : ()Ljava/lang/Object;
    //   35: astore #5
    //   37: goto -> 40
    //   40: aload #5
    //   42: monitorenter
    //   43: aload_0
    //   44: getfield obj : Lcom/trendmicro/hippo/Scriptable;
    //   47: checkcast com/trendmicro/hippo/Function
    //   50: aload_1
    //   51: aload_2
    //   52: aload_3
    //   53: aload #4
    //   55: invokeinterface call : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   60: astore_1
    //   61: aload #5
    //   63: monitorexit
    //   64: aload_1
    //   65: areturn
    //   66: astore_1
    //   67: aload #5
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   43	64	66	finally
    //   67	70	66	finally
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Synchronizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */