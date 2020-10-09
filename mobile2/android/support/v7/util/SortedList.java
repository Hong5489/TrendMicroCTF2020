package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
  private static final int CAPACITY_GROWTH = 10;
  
  private static final int DELETION = 2;
  
  private static final int INSERTION = 1;
  
  public static final int INVALID_POSITION = -1;
  
  private static final int LOOKUP = 4;
  
  private static final int MIN_CAPACITY = 10;
  
  private BatchedCallback mBatchedCallback;
  
  private Callback mCallback;
  
  T[] mData;
  
  private int mNewDataStart;
  
  private T[] mOldData;
  
  private int mOldDataSize;
  
  private int mOldDataStart;
  
  private int mSize;
  
  private final Class<T> mTClass;
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback) {
    this(paramClass, paramCallback, 10);
  }
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback, int paramInt) {
    this.mTClass = paramClass;
    this.mData = (T[])Array.newInstance(paramClass, paramInt);
    this.mCallback = paramCallback;
    this.mSize = 0;
  }
  
  private int add(T paramT, boolean paramBoolean) {
    int j;
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 1);
    if (i == -1) {
      j = 0;
    } else {
      j = i;
      if (i < this.mSize) {
        T t = this.mData[i];
        j = i;
        if (this.mCallback.areItemsTheSame(t, paramT)) {
          if (this.mCallback.areContentsTheSame(t, paramT)) {
            this.mData[i] = paramT;
            return i;
          } 
          this.mData[i] = paramT;
          Callback<T> callback = this.mCallback;
          callback.onChanged(i, 1, callback.getChangePayload(t, paramT));
          return i;
        } 
      } 
    } 
    addToData(j, paramT);
    if (paramBoolean)
      this.mCallback.onInserted(j, 1); 
    return j;
  }
  
  private void addAllInternal(T[] paramArrayOfT) {
    if (paramArrayOfT.length < 1)
      return; 
    int i = sortAndDedup(paramArrayOfT);
    if (this.mSize == 0) {
      this.mData = paramArrayOfT;
      this.mSize = i;
      this.mCallback.onInserted(0, i);
    } else {
      merge(paramArrayOfT, i);
    } 
  }
  
  private void addToData(int paramInt, T paramT) {
    int i = this.mSize;
    if (paramInt <= i) {
      T[] arrayOfT = this.mData;
      if (i == arrayOfT.length) {
        arrayOfT = (T[])Array.newInstance(this.mTClass, arrayOfT.length + 10);
        System.arraycopy(this.mData, 0, arrayOfT, 0, paramInt);
        arrayOfT[paramInt] = paramT;
        System.arraycopy(this.mData, paramInt, arrayOfT, paramInt + 1, this.mSize - paramInt);
        this.mData = arrayOfT;
      } else {
        System.arraycopy(arrayOfT, paramInt, arrayOfT, paramInt + 1, i - paramInt);
        this.mData[paramInt] = paramT;
      } 
      this.mSize++;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("cannot add item to ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" because size is ");
    stringBuilder.append(this.mSize);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  private T[] copyArray(T[] paramArrayOfT) {
    Object[] arrayOfObject = (Object[])Array.newInstance(this.mTClass, paramArrayOfT.length);
    System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramArrayOfT.length);
    return (T[])arrayOfObject;
  }
  
  private int findIndexOf(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3) {
    while (true) {
      int i = -1;
      if (paramInt1 < paramInt2) {
        i = (paramInt1 + paramInt2) / 2;
        T t = paramArrayOfT[i];
        int j = this.mCallback.compare(t, paramT);
        if (j < 0) {
          paramInt1 = i + 1;
          continue;
        } 
        if (j == 0) {
          if (this.mCallback.areItemsTheSame(t, paramT))
            return i; 
          paramInt1 = linearEqualitySearch(paramT, i, paramInt1, paramInt2);
          if (paramInt3 == 1) {
            if (paramInt1 != -1)
              i = paramInt1; 
            return i;
          } 
          return paramInt1;
        } 
        paramInt2 = i;
        continue;
      } 
      paramInt2 = i;
      if (paramInt3 == 1)
        paramInt2 = paramInt1; 
      return paramInt2;
    } 
  }
  
  private int findSameItem(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      if (this.mCallback.areItemsTheSame(paramArrayOfT[paramInt1], paramT))
        return paramInt1; 
      paramInt1++;
    } 
    return -1;
  }
  
  private int linearEqualitySearch(T paramT, int paramInt1, int paramInt2, int paramInt3) {
    for (int i = paramInt1 - 1; i >= paramInt2; i--) {
      T t = this.mData[i];
      if (this.mCallback.compare(t, paramT) != 0)
        break; 
      if (this.mCallback.areItemsTheSame(t, paramT))
        return i; 
    } 
    while (++paramInt1 < paramInt3) {
      T t = this.mData[paramInt1];
      if (this.mCallback.compare(t, paramT) != 0)
        break; 
      if (this.mCallback.areItemsTheSame(t, paramT))
        return paramInt1; 
      paramInt1++;
    } 
    return -1;
  }
  
  private void merge(T[] paramArrayOfT, int paramInt) {
    int i = this.mCallback instanceof BatchedCallback ^ true;
    if (i != 0)
      beginBatchedUpdates(); 
    this.mOldData = this.mData;
    this.mOldDataStart = 0;
    int j = this.mSize;
    this.mOldDataSize = j;
    this.mData = (T[])Array.newInstance(this.mTClass, j + paramInt + 10);
    this.mNewDataStart = 0;
    j = 0;
    while (true) {
      if (this.mOldDataStart < this.mOldDataSize || j < paramInt) {
        int k = this.mOldDataStart;
        int m = this.mOldDataSize;
        if (k == m) {
          paramInt -= j;
          System.arraycopy(paramArrayOfT, j, this.mData, this.mNewDataStart, paramInt);
          j = this.mNewDataStart + paramInt;
          this.mNewDataStart = j;
          this.mSize += paramInt;
          this.mCallback.onInserted(j - paramInt, paramInt);
        } else if (j == paramInt) {
          paramInt = m - k;
          System.arraycopy(this.mOldData, k, this.mData, this.mNewDataStart, paramInt);
          this.mNewDataStart += paramInt;
        } else {
          T[] arrayOfT1;
          T t1 = this.mOldData[k];
          T t2 = paramArrayOfT[j];
          k = this.mCallback.compare(t1, t2);
          if (k > 0) {
            arrayOfT1 = this.mData;
            m = this.mNewDataStart;
            k = m + 1;
            this.mNewDataStart = k;
            arrayOfT1[m] = t2;
            this.mSize++;
            j++;
            this.mCallback.onInserted(k - 1, 1);
            continue;
          } 
          if (k == 0 && this.mCallback.areItemsTheSame(arrayOfT1, (T[])t2)) {
            T[] arrayOfT = this.mData;
            k = this.mNewDataStart;
            this.mNewDataStart = k + 1;
            arrayOfT[k] = t2;
            k = j + 1;
            this.mOldDataStart++;
            j = k;
            if (!this.mCallback.areContentsTheSame(arrayOfT1, (T[])t2)) {
              Callback<T[]> callback = this.mCallback;
              callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(arrayOfT1, (T[])t2));
              j = k;
            } 
            continue;
          } 
          T[] arrayOfT2 = this.mData;
          k = this.mNewDataStart;
          this.mNewDataStart = k + 1;
          arrayOfT2[k] = (T)arrayOfT1;
          this.mOldDataStart++;
          continue;
        } 
      } 
      this.mOldData = null;
      if (i != 0)
        endBatchedUpdates(); 
      return;
    } 
  }
  
  private boolean remove(T paramT, boolean paramBoolean) {
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 2);
    if (i == -1)
      return false; 
    removeItemAtIndex(i, paramBoolean);
    return true;
  }
  
  private void removeItemAtIndex(int paramInt, boolean paramBoolean) {
    T[] arrayOfT = this.mData;
    System.arraycopy(arrayOfT, paramInt + 1, arrayOfT, paramInt, this.mSize - paramInt - 1);
    int i = this.mSize - 1;
    this.mSize = i;
    this.mData[i] = null;
    if (paramBoolean)
      this.mCallback.onRemoved(paramInt, 1); 
  }
  
  private void replaceAllInsert(T paramT) {
    T[] arrayOfT = this.mData;
    int i = this.mNewDataStart;
    arrayOfT[i] = paramT;
    this.mNewDataStart = ++i;
    this.mSize++;
    this.mCallback.onInserted(i - 1, 1);
  }
  
  private void replaceAllInternal(T[] paramArrayOfT) {
    int i = this.mCallback instanceof BatchedCallback ^ true;
    if (i != 0)
      beginBatchedUpdates(); 
    this.mOldDataStart = 0;
    this.mOldDataSize = this.mSize;
    this.mOldData = this.mData;
    this.mNewDataStart = 0;
    int j = sortAndDedup(paramArrayOfT);
    this.mData = (T[])Array.newInstance(this.mTClass, j);
    while (true) {
      if (this.mNewDataStart < j || this.mOldDataStart < this.mOldDataSize) {
        int k = this.mOldDataStart;
        int m = this.mOldDataSize;
        if (k >= m) {
          int n = this.mNewDataStart;
          j -= this.mNewDataStart;
          System.arraycopy(paramArrayOfT, n, this.mData, n, j);
          this.mNewDataStart += j;
          this.mSize += j;
          this.mCallback.onInserted(n, j);
        } else {
          int n = this.mNewDataStart;
          if (n >= j) {
            j = m - k;
            this.mSize -= j;
            this.mCallback.onRemoved(n, j);
          } else {
            T t1 = this.mOldData[k];
            T t2 = paramArrayOfT[n];
            n = this.mCallback.compare(t1, t2);
            if (n < 0) {
              replaceAllRemove();
              continue;
            } 
            if (n > 0) {
              replaceAllInsert(t2);
              continue;
            } 
            if (!this.mCallback.areItemsTheSame(t1, t2)) {
              replaceAllRemove();
              replaceAllInsert(t2);
              continue;
            } 
            T[] arrayOfT = this.mData;
            n = this.mNewDataStart;
            arrayOfT[n] = t2;
            this.mOldDataStart++;
            this.mNewDataStart = n + 1;
            if (!this.mCallback.areContentsTheSame(t1, t2)) {
              Callback<T> callback = this.mCallback;
              callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(t1, t2));
            } 
            continue;
          } 
        } 
      } 
      this.mOldData = null;
      if (i != 0)
        endBatchedUpdates(); 
      return;
    } 
  }
  
  private void replaceAllRemove() {
    this.mSize--;
    this.mOldDataStart++;
    this.mCallback.onRemoved(this.mNewDataStart, 1);
  }
  
  private int sortAndDedup(T[] paramArrayOfT) {
    if (paramArrayOfT.length == 0)
      return 0; 
    Arrays.sort(paramArrayOfT, this.mCallback);
    byte b1 = 0;
    byte b2 = 1;
    for (byte b3 = 1; b3 < paramArrayOfT.length; b3++) {
      T t = paramArrayOfT[b3];
      if (this.mCallback.compare(paramArrayOfT[b1], t) == 0) {
        int i = findSameItem(t, paramArrayOfT, b1, b2);
        if (i != -1) {
          paramArrayOfT[i] = t;
        } else {
          if (b2 != b3)
            paramArrayOfT[b2] = t; 
          b2++;
        } 
      } else {
        if (b2 != b3)
          paramArrayOfT[b2] = t; 
        b1 = b2;
        b2++;
      } 
    } 
    return b2;
  }
  
  private void throwIfInMutationOperation() {
    if (this.mOldData == null)
      return; 
    throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
  }
  
  public int add(T paramT) {
    throwIfInMutationOperation();
    return add(paramT, true);
  }
  
  public void addAll(Collection<T> paramCollection) {
    addAll(paramCollection.toArray((T[])Array.newInstance(this.mTClass, paramCollection.size())), true);
  }
  
  public void addAll(T... paramVarArgs) {
    addAll(paramVarArgs, false);
  }
  
  public void addAll(T[] paramArrayOfT, boolean paramBoolean) {
    throwIfInMutationOperation();
    if (paramArrayOfT.length == 0)
      return; 
    if (paramBoolean) {
      addAllInternal(paramArrayOfT);
    } else {
      addAllInternal(copyArray(paramArrayOfT));
    } 
  }
  
  public void beginBatchedUpdates() {
    throwIfInMutationOperation();
    if (this.mCallback instanceof BatchedCallback)
      return; 
    if (this.mBatchedCallback == null)
      this.mBatchedCallback = new BatchedCallback(this.mCallback); 
    this.mCallback = this.mBatchedCallback;
  }
  
  public void clear() {
    throwIfInMutationOperation();
    if (this.mSize == 0)
      return; 
    int i = this.mSize;
    Arrays.fill((Object[])this.mData, 0, i, (Object)null);
    this.mSize = 0;
    this.mCallback.onRemoved(0, i);
  }
  
  public void endBatchedUpdates() {
    throwIfInMutationOperation();
    Callback callback1 = this.mCallback;
    if (callback1 instanceof BatchedCallback)
      ((BatchedCallback)callback1).dispatchLastEvent(); 
    Callback callback2 = this.mCallback;
    callback1 = this.mBatchedCallback;
    if (callback2 == callback1)
      this.mCallback = ((BatchedCallback)callback1).mWrappedCallback; 
  }
  
  public T get(int paramInt) throws IndexOutOfBoundsException {
    if (paramInt < this.mSize && paramInt >= 0) {
      T[] arrayOfT = this.mOldData;
      if (arrayOfT != null) {
        int i = this.mNewDataStart;
        if (paramInt >= i)
          return arrayOfT[paramInt - i + this.mOldDataStart]; 
      } 
      return this.mData[paramInt];
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Asked to get item at ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" but size is ");
    stringBuilder.append(this.mSize);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int indexOf(T paramT) {
    if (this.mOldData != null) {
      int i = findIndexOf(paramT, this.mData, 0, this.mNewDataStart, 4);
      if (i != -1)
        return i; 
      i = findIndexOf(paramT, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
      return (i != -1) ? (i - this.mOldDataStart + this.mNewDataStart) : -1;
    } 
    return findIndexOf(paramT, this.mData, 0, this.mSize, 4);
  }
  
  public void recalculatePositionOfItemAt(int paramInt) {
    throwIfInMutationOperation();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, false);
    int i = add(t, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public boolean remove(T paramT) {
    throwIfInMutationOperation();
    return remove(paramT, true);
  }
  
  public T removeItemAt(int paramInt) {
    throwIfInMutationOperation();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, true);
    return t;
  }
  
  public void replaceAll(Collection<T> paramCollection) {
    replaceAll(paramCollection.toArray((T[])Array.newInstance(this.mTClass, paramCollection.size())), true);
  }
  
  public void replaceAll(T... paramVarArgs) {
    replaceAll(paramVarArgs, false);
  }
  
  public void replaceAll(T[] paramArrayOfT, boolean paramBoolean) {
    throwIfInMutationOperation();
    if (paramBoolean) {
      replaceAllInternal(paramArrayOfT);
    } else {
      replaceAllInternal(copyArray(paramArrayOfT));
    } 
  }
  
  public int size() {
    return this.mSize;
  }
  
  public void updateItemAt(int paramInt, T paramT) {
    throwIfInMutationOperation();
    T t = get(paramInt);
    if (t == paramT || !this.mCallback.areContentsTheSame(t, paramT)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (t != paramT && this.mCallback.compare(t, paramT) == 0) {
      this.mData[paramInt] = paramT;
      if (i) {
        Callback<T> callback = this.mCallback;
        callback.onChanged(paramInt, 1, callback.getChangePayload(t, paramT));
      } 
      return;
    } 
    if (i) {
      Callback<T> callback = this.mCallback;
      callback.onChanged(paramInt, 1, callback.getChangePayload(t, paramT));
    } 
    removeItemAtIndex(paramInt, false);
    int i = add(paramT, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public static class BatchedCallback<T2> extends Callback<T2> {
    private final BatchingListUpdateCallback mBatchingListUpdateCallback;
    
    final SortedList.Callback<T2> mWrappedCallback;
    
    public BatchedCallback(SortedList.Callback<T2> param1Callback) {
      this.mWrappedCallback = param1Callback;
      this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
    }
    
    public boolean areContentsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areContentsTheSame(param1T21, param1T22);
    }
    
    public boolean areItemsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areItemsTheSame(param1T21, param1T22);
    }
    
    public int compare(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.compare(param1T21, param1T22);
    }
    
    public void dispatchLastEvent() {
      this.mBatchingListUpdateCallback.dispatchLastEvent();
    }
    
    public Object getChangePayload(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.getChangePayload(param1T21, param1T22);
    }
    
    public void onChanged(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onChanged(param1Int1, param1Int2, null);
    }
    
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      this.mBatchingListUpdateCallback.onChanged(param1Int1, param1Int2, param1Object);
    }
    
    public void onInserted(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onInserted(param1Int1, param1Int2);
    }
    
    public void onMoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onMoved(param1Int1, param1Int2);
    }
    
    public void onRemoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onRemoved(param1Int1, param1Int2);
    }
  }
  
  public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
    public abstract boolean areContentsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract boolean areItemsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract int compare(T2 param1T21, T2 param1T22);
    
    public Object getChangePayload(T2 param1T21, T2 param1T22) {
      return null;
    }
    
    public abstract void onChanged(int param1Int1, int param1Int2);
    
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      onChanged(param1Int1, param1Int2);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/util/SortedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */