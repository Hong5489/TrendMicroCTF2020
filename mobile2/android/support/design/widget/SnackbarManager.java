package android.support.design.widget;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

class SnackbarManager {
  private static final int LONG_DURATION_MS = 2750;
  
  static final int MSG_TIMEOUT = 0;
  
  private static final int SHORT_DURATION_MS = 1500;
  
  private static SnackbarManager snackbarManager;
  
  private SnackbarRecord currentSnackbar;
  
  private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what != 0)
            return false; 
          SnackbarManager.this.handleTimeout((SnackbarManager.SnackbarRecord)param1Message.obj);
          return true;
        }
      });
  
  private final Object lock = new Object();
  
  private SnackbarRecord nextSnackbar;
  
  private boolean cancelSnackbarLocked(SnackbarRecord paramSnackbarRecord, int paramInt) {
    Callback callback = paramSnackbarRecord.callback.get();
    if (callback != null) {
      this.handler.removeCallbacksAndMessages(paramSnackbarRecord);
      callback.dismiss(paramInt);
      return true;
    } 
    return false;
  }
  
  static SnackbarManager getInstance() {
    if (snackbarManager == null)
      snackbarManager = new SnackbarManager(); 
    return snackbarManager;
  }
  
  private boolean isCurrentSnackbarLocked(Callback paramCallback) {
    boolean bool;
    SnackbarRecord snackbarRecord = this.currentSnackbar;
    if (snackbarRecord != null && snackbarRecord.isSnackbar(paramCallback)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isNextSnackbarLocked(Callback paramCallback) {
    boolean bool;
    SnackbarRecord snackbarRecord = this.nextSnackbar;
    if (snackbarRecord != null && snackbarRecord.isSnackbar(paramCallback)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void scheduleTimeoutLocked(SnackbarRecord paramSnackbarRecord) {
    if (paramSnackbarRecord.duration == -2)
      return; 
    int i = 2750;
    if (paramSnackbarRecord.duration > 0) {
      i = paramSnackbarRecord.duration;
    } else if (paramSnackbarRecord.duration == -1) {
      i = 1500;
    } 
    this.handler.removeCallbacksAndMessages(paramSnackbarRecord);
    Handler handler = this.handler;
    handler.sendMessageDelayed(Message.obtain(handler, 0, paramSnackbarRecord), i);
  }
  
  private void showNextSnackbarLocked() {
    SnackbarRecord snackbarRecord = this.nextSnackbar;
    if (snackbarRecord != null) {
      this.currentSnackbar = snackbarRecord;
      this.nextSnackbar = null;
      Callback callback = snackbarRecord.callback.get();
      if (callback != null) {
        callback.show();
      } else {
        this.currentSnackbar = null;
      } 
    } 
  }
  
  public void dismiss(Callback paramCallback, int paramInt) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback)) {
        cancelSnackbarLocked(this.currentSnackbar, paramInt);
      } else if (isNextSnackbarLocked(paramCallback)) {
        cancelSnackbarLocked(this.nextSnackbar, paramInt);
      } 
      return;
    } 
  }
  
  void handleTimeout(SnackbarRecord paramSnackbarRecord) {
    synchronized (this.lock) {
      if (this.currentSnackbar == paramSnackbarRecord || this.nextSnackbar == paramSnackbarRecord)
        cancelSnackbarLocked(paramSnackbarRecord, 2); 
      return;
    } 
  }
  
  public boolean isCurrent(Callback paramCallback) {
    synchronized (this.lock) {
      return isCurrentSnackbarLocked(paramCallback);
    } 
  }
  
  public boolean isCurrentOrNext(Callback paramCallback) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback) || isNextSnackbarLocked(paramCallback))
        return true; 
      return false;
    } 
  }
  
  public void onDismissed(Callback paramCallback) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback)) {
        this.currentSnackbar = null;
        if (this.nextSnackbar != null)
          showNextSnackbarLocked(); 
      } 
      return;
    } 
  }
  
  public void onShown(Callback paramCallback) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback))
        scheduleTimeoutLocked(this.currentSnackbar); 
      return;
    } 
  }
  
  public void pauseTimeout(Callback paramCallback) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback) && !this.currentSnackbar.paused) {
        this.currentSnackbar.paused = true;
        this.handler.removeCallbacksAndMessages(this.currentSnackbar);
      } 
      return;
    } 
  }
  
  public void restoreTimeoutIfPaused(Callback paramCallback) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback) && this.currentSnackbar.paused) {
        this.currentSnackbar.paused = false;
        scheduleTimeoutLocked(this.currentSnackbar);
      } 
      return;
    } 
  }
  
  public void show(int paramInt, Callback paramCallback) {
    synchronized (this.lock) {
      if (isCurrentSnackbarLocked(paramCallback)) {
        this.currentSnackbar.duration = paramInt;
        this.handler.removeCallbacksAndMessages(this.currentSnackbar);
        scheduleTimeoutLocked(this.currentSnackbar);
        return;
      } 
      if (isNextSnackbarLocked(paramCallback)) {
        this.nextSnackbar.duration = paramInt;
      } else {
        SnackbarRecord snackbarRecord = new SnackbarRecord();
        this(paramInt, paramCallback);
        this.nextSnackbar = snackbarRecord;
      } 
      if (this.currentSnackbar != null && cancelSnackbarLocked(this.currentSnackbar, 4))
        return; 
      this.currentSnackbar = null;
      showNextSnackbarLocked();
      return;
    } 
  }
  
  static interface Callback {
    void dismiss(int param1Int);
    
    void show();
  }
  
  private static class SnackbarRecord {
    final WeakReference<SnackbarManager.Callback> callback;
    
    int duration;
    
    boolean paused;
    
    SnackbarRecord(int param1Int, SnackbarManager.Callback param1Callback) {
      this.callback = new WeakReference<>(param1Callback);
      this.duration = param1Int;
    }
    
    boolean isSnackbar(SnackbarManager.Callback param1Callback) {
      boolean bool;
      if (param1Callback != null && this.callback.get() == param1Callback) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/SnackbarManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */