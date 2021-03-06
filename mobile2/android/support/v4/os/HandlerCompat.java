package android.support.v4.os;

import android.os.Build;
import android.os.Handler;
import android.os.Message;

public final class HandlerCompat {
  public static boolean postDelayed(Handler paramHandler, Runnable paramRunnable, Object paramObject, long paramLong) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramHandler.postDelayed(paramRunnable, paramObject, paramLong); 
    Message message = Message.obtain(paramHandler, paramRunnable);
    message.obj = paramObject;
    return paramHandler.sendMessageDelayed(message, paramLong);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/os/HandlerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */