package android.arch.lifecycle;

class FullLifecycleObserverAdapter implements GenericLifecycleObserver {
  private final FullLifecycleObserver mObserver;
  
  FullLifecycleObserverAdapter(FullLifecycleObserver paramFullLifecycleObserver) {
    this.mObserver = paramFullLifecycleObserver;
  }
  
  public void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent) {
    switch (paramEvent) {
      default:
        return;
      case null:
        throw new IllegalArgumentException("ON_ANY must not been send by anybody");
      case null:
        this.mObserver.onDestroy(paramLifecycleOwner);
      case null:
        this.mObserver.onStop(paramLifecycleOwner);
      case null:
        this.mObserver.onPause(paramLifecycleOwner);
      case null:
        this.mObserver.onResume(paramLifecycleOwner);
      case null:
        this.mObserver.onStart(paramLifecycleOwner);
      case null:
        break;
    } 
    this.mObserver.onCreate(paramLifecycleOwner);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/arch/lifecycle/FullLifecycleObserverAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */