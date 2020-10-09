package android.support.v7.recyclerview.extensions;

import android.support.v7.util.DiffUtil;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class AsyncDifferConfig<T> {
  private final Executor mBackgroundThreadExecutor;
  
  private final DiffUtil.ItemCallback<T> mDiffCallback;
  
  private final Executor mMainThreadExecutor;
  
  AsyncDifferConfig(Executor paramExecutor1, Executor paramExecutor2, DiffUtil.ItemCallback<T> paramItemCallback) {
    this.mMainThreadExecutor = paramExecutor1;
    this.mBackgroundThreadExecutor = paramExecutor2;
    this.mDiffCallback = paramItemCallback;
  }
  
  public Executor getBackgroundThreadExecutor() {
    return this.mBackgroundThreadExecutor;
  }
  
  public DiffUtil.ItemCallback<T> getDiffCallback() {
    return this.mDiffCallback;
  }
  
  public Executor getMainThreadExecutor() {
    return this.mMainThreadExecutor;
  }
  
  public static final class Builder<T> {
    private static Executor sDiffExecutor = null;
    
    private static final Object sExecutorLock = new Object();
    
    private Executor mBackgroundThreadExecutor;
    
    private final DiffUtil.ItemCallback<T> mDiffCallback;
    
    private Executor mMainThreadExecutor;
    
    static {
    
    }
    
    public Builder(DiffUtil.ItemCallback<T> param1ItemCallback) {
      this.mDiffCallback = param1ItemCallback;
    }
    
    public AsyncDifferConfig<T> build() {
      if (this.mBackgroundThreadExecutor == null)
        synchronized (sExecutorLock) {
          if (sDiffExecutor == null)
            sDiffExecutor = Executors.newFixedThreadPool(2); 
          this.mBackgroundThreadExecutor = sDiffExecutor;
        }  
      return new AsyncDifferConfig<>(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
    }
    
    public Builder<T> setBackgroundThreadExecutor(Executor param1Executor) {
      this.mBackgroundThreadExecutor = param1Executor;
      return this;
    }
    
    public Builder<T> setMainThreadExecutor(Executor param1Executor) {
      this.mMainThreadExecutor = param1Executor;
      return this;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/recyclerview/extensions/AsyncDifferConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */