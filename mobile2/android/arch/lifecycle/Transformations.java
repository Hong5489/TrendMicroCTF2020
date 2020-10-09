package android.arch.lifecycle;

import android.arch.core.util.Function;

public class Transformations {
  public static <X, Y> LiveData<Y> map(LiveData<X> paramLiveData, final Function<X, Y> func) {
    final MediatorLiveData<Y> result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          public void onChanged(X param1X) {
            result.setValue(func.apply(param1X));
          }
        });
    return mediatorLiveData;
  }
  
  public static <X, Y> LiveData<Y> switchMap(LiveData<X> paramLiveData, final Function<X, LiveData<Y>> func) {
    final MediatorLiveData<Y> result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          LiveData<Y> mSource;
          
          public void onChanged(X param1X) {
            LiveData<Y> liveData2 = (LiveData)func.apply(param1X);
            LiveData<Y> liveData1 = this.mSource;
            if (liveData1 == liveData2)
              return; 
            if (liveData1 != null)
              result.removeSource(liveData1); 
            this.mSource = liveData2;
            if (liveData2 != null)
              result.addSource(liveData2, new Observer() {
                    public void onChanged(Y param2Y) {
                      result.setValue(param2Y);
                    }
                  }); 
          }
        });
    return mediatorLiveData;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/arch/lifecycle/Transformations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */