package android.support.transition;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;

class TransitionValuesMaps {
  final SparseArray<View> mIdValues = new SparseArray();
  
  final LongSparseArray<View> mItemIdValues = new LongSparseArray();
  
  final ArrayMap<String, View> mNameValues = new ArrayMap();
  
  final ArrayMap<View, TransitionValues> mViewValues = new ArrayMap();
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/TransitionValuesMaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */