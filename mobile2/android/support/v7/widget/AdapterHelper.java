package android.support.v7.widget;

import android.support.v4.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper implements OpReorderer.Callback {
  private static final boolean DEBUG = false;
  
  static final int POSITION_TYPE_INVISIBLE = 0;
  
  static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
  
  private static final String TAG = "AHT";
  
  final Callback mCallback;
  
  final boolean mDisableRecycler;
  
  private int mExistingUpdateTypes = 0;
  
  Runnable mOnItemProcessedCallback;
  
  final OpReorderer mOpReorderer;
  
  final ArrayList<UpdateOp> mPendingUpdates = new ArrayList<>();
  
  final ArrayList<UpdateOp> mPostponedList = new ArrayList<>();
  
  private Pools.Pool<UpdateOp> mUpdateOpPool = (Pools.Pool<UpdateOp>)new Pools.SimplePool(30);
  
  AdapterHelper(Callback paramCallback) {
    this(paramCallback, false);
  }
  
  AdapterHelper(Callback paramCallback, boolean paramBoolean) {
    this.mCallback = paramCallback;
    this.mDisableRecycler = paramBoolean;
    this.mOpReorderer = new OpReorderer(this);
  }
  
  private void applyAdd(UpdateOp paramUpdateOp) {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyMove(UpdateOp paramUpdateOp) {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyRemove(UpdateOp paramUpdateOp) {
    int i = paramUpdateOp.positionStart;
    int j = 0;
    int k = paramUpdateOp.positionStart + paramUpdateOp.itemCount;
    byte b = -1;
    int m;
    for (m = paramUpdateOp.positionStart; m < k; m = n) {
      byte b1 = 0;
      int n = 0;
      if (this.mCallback.findViewHolder(m) != null || canFindInPreLayout(m)) {
        if (b == 0) {
          dispatchAndUpdateViewHolders(obtainUpdateOp(2, i, j, null));
          b1 = 1;
        } 
        b = 1;
        n = b1;
        b1 = b;
      } else {
        if (b == 1) {
          postponeAndUpdateViewHolders(obtainUpdateOp(2, i, j, null));
          n = 1;
        } 
        b1 = 0;
      } 
      if (n != 0) {
        n = m - j;
        k -= j;
        m = 1;
      } else {
        j++;
        n = m;
        m = j;
      } 
      n++;
      j = m;
      b = b1;
    } 
    UpdateOp updateOp = paramUpdateOp;
    if (j != paramUpdateOp.itemCount) {
      recycleUpdateOp(paramUpdateOp);
      updateOp = obtainUpdateOp(2, i, j, null);
    } 
    if (b == 0) {
      dispatchAndUpdateViewHolders(updateOp);
    } else {
      postponeAndUpdateViewHolders(updateOp);
    } 
  }
  
  private void applyUpdate(UpdateOp paramUpdateOp) {
    int i = paramUpdateOp.positionStart;
    int j = 0;
    int k = paramUpdateOp.positionStart;
    int m = paramUpdateOp.itemCount;
    int n = -1;
    int i1 = paramUpdateOp.positionStart;
    while (i1 < k + m) {
      int i2;
      int i3;
      if (this.mCallback.findViewHolder(i1) != null || canFindInPreLayout(i1)) {
        int i4 = i;
        i2 = j;
        if (n == 0) {
          dispatchAndUpdateViewHolders(obtainUpdateOp(4, i, j, paramUpdateOp.payload));
          i2 = 0;
          i4 = i1;
        } 
        i3 = 1;
        i = i4;
      } else {
        i2 = i;
        i3 = j;
        if (n == 1) {
          postponeAndUpdateViewHolders(obtainUpdateOp(4, i, j, paramUpdateOp.payload));
          i3 = 0;
          i2 = i1;
        } 
        j = 0;
        i = i2;
        i2 = i3;
        i3 = j;
      } 
      j = i2 + 1;
      i1++;
      n = i3;
    } 
    Object object = paramUpdateOp;
    if (j != paramUpdateOp.itemCount) {
      object = paramUpdateOp.payload;
      recycleUpdateOp(paramUpdateOp);
      object = obtainUpdateOp(4, i, j, object);
    } 
    if (n == 0) {
      dispatchAndUpdateViewHolders((UpdateOp)object);
    } else {
      postponeAndUpdateViewHolders((UpdateOp)object);
    } 
  }
  
  private boolean canFindInPreLayout(int paramInt) {
    int i = this.mPostponedList.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPostponedList.get(b);
      if (updateOp.cmd == 8) {
        if (findPositionOffset(updateOp.itemCount, b + 1) == paramInt)
          return true; 
      } else if (updateOp.cmd == 1) {
        int j = updateOp.positionStart;
        int k = updateOp.itemCount;
        for (int m = updateOp.positionStart; m < j + k; m++) {
          if (findPositionOffset(m, b + 1) == paramInt)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  private void dispatchAndUpdateViewHolders(UpdateOp paramUpdateOp) {
    if (paramUpdateOp.cmd != 1 && paramUpdateOp.cmd != 8) {
      byte b1;
      int i = updatePositionWithPostponed(paramUpdateOp.positionStart, paramUpdateOp.cmd);
      int j = 1;
      int k = paramUpdateOp.positionStart;
      int m = paramUpdateOp.cmd;
      if (m != 2) {
        if (m == 4) {
          b1 = 1;
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("op should be remove or update.");
          stringBuilder.append(paramUpdateOp);
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
      } else {
        b1 = 0;
      } 
      byte b2 = 1;
      while (b2 < paramUpdateOp.itemCount) {
        int n = updatePositionWithPostponed(paramUpdateOp.positionStart + b1 * b2, paramUpdateOp.cmd);
        boolean bool1 = false;
        int i1 = paramUpdateOp.cmd;
        boolean bool2 = false;
        m = 0;
        if (i1 != 2) {
          if (i1 != 4) {
            m = bool1;
          } else if (n == i + 1) {
            m = 1;
          } 
        } else {
          m = bool2;
          if (n == i)
            m = 1; 
        } 
        if (m != 0) {
          m = j + 1;
        } else {
          UpdateOp updateOp = obtainUpdateOp(paramUpdateOp.cmd, i, j, paramUpdateOp.payload);
          dispatchFirstPassAndUpdateViewHolders(updateOp, k);
          recycleUpdateOp(updateOp);
          m = k;
          if (paramUpdateOp.cmd == 4)
            m = k + j; 
          i = n;
          j = 1;
          k = m;
          m = j;
        } 
        b2++;
        j = m;
      } 
      Object object = paramUpdateOp.payload;
      recycleUpdateOp(paramUpdateOp);
      if (j > 0) {
        paramUpdateOp = obtainUpdateOp(paramUpdateOp.cmd, i, j, object);
        dispatchFirstPassAndUpdateViewHolders(paramUpdateOp, k);
        recycleUpdateOp(paramUpdateOp);
      } 
      return;
    } 
    throw new IllegalArgumentException("should not dispatch add or move for pre layout");
  }
  
  private void postponeAndUpdateViewHolders(UpdateOp paramUpdateOp) {
    this.mPostponedList.add(paramUpdateOp);
    int i = paramUpdateOp.cmd;
    if (i != 1) {
      if (i != 2) {
        if (i != 4) {
          if (i == 8) {
            this.mCallback.offsetPositionsForMove(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown update op type for ");
            stringBuilder.append(paramUpdateOp);
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
        } else {
          this.mCallback.markViewHoldersUpdated(paramUpdateOp.positionStart, paramUpdateOp.itemCount, paramUpdateOp.payload);
        } 
      } else {
        this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
      } 
    } else {
      this.mCallback.offsetPositionsForAdd(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
    } 
  }
  
  private int updatePositionWithPostponed(int paramInt1, int paramInt2) {
    int i = this.mPostponedList.size() - 1;
    int j;
    for (j = paramInt1; i >= 0; j = paramInt1) {
      UpdateOp updateOp = this.mPostponedList.get(i);
      if (updateOp.cmd == 8) {
        int k;
        if (updateOp.positionStart < updateOp.itemCount) {
          k = updateOp.positionStart;
          paramInt1 = updateOp.itemCount;
        } else {
          k = updateOp.itemCount;
          paramInt1 = updateOp.positionStart;
        } 
        if (j >= k && j <= paramInt1) {
          if (k == updateOp.positionStart) {
            if (paramInt2 == 1) {
              updateOp.itemCount++;
            } else if (paramInt2 == 2) {
              updateOp.itemCount--;
            } 
            paramInt1 = j + 1;
          } else {
            if (paramInt2 == 1) {
              updateOp.positionStart++;
            } else if (paramInt2 == 2) {
              updateOp.positionStart--;
            } 
            paramInt1 = j - 1;
          } 
        } else {
          paramInt1 = j;
          if (j < updateOp.positionStart)
            if (paramInt2 == 1) {
              updateOp.positionStart++;
              updateOp.itemCount++;
              paramInt1 = j;
            } else {
              paramInt1 = j;
              if (paramInt2 == 2) {
                updateOp.positionStart--;
                updateOp.itemCount--;
                paramInt1 = j;
              } 
            }  
        } 
      } else if (updateOp.positionStart <= j) {
        if (updateOp.cmd == 1) {
          paramInt1 = j - updateOp.itemCount;
        } else {
          paramInt1 = j;
          if (updateOp.cmd == 2)
            paramInt1 = j + updateOp.itemCount; 
        } 
      } else if (paramInt2 == 1) {
        updateOp.positionStart++;
        paramInt1 = j;
      } else {
        paramInt1 = j;
        if (paramInt2 == 2) {
          updateOp.positionStart--;
          paramInt1 = j;
        } 
      } 
      i--;
    } 
    for (paramInt1 = this.mPostponedList.size() - 1; paramInt1 >= 0; paramInt1--) {
      UpdateOp updateOp = this.mPostponedList.get(paramInt1);
      if (updateOp.cmd == 8) {
        if (updateOp.itemCount == updateOp.positionStart || updateOp.itemCount < 0) {
          this.mPostponedList.remove(paramInt1);
          recycleUpdateOp(updateOp);
        } 
      } else if (updateOp.itemCount <= 0) {
        this.mPostponedList.remove(paramInt1);
        recycleUpdateOp(updateOp);
      } 
    } 
    return j;
  }
  
  AdapterHelper addUpdateOp(UpdateOp... paramVarArgs) {
    Collections.addAll(this.mPendingUpdates, paramVarArgs);
    return this;
  }
  
  public int applyPendingUpdatesToPosition(int paramInt) {
    int i = this.mPendingUpdates.size();
    byte b = 0;
    int j;
    for (j = paramInt; b < i; j = paramInt) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      paramInt = updateOp.cmd;
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 8) {
            paramInt = j;
          } else if (updateOp.positionStart == j) {
            paramInt = updateOp.itemCount;
          } else {
            int k = j;
            if (updateOp.positionStart < j)
              k = j - 1; 
            paramInt = k;
            if (updateOp.itemCount <= k)
              paramInt = k + 1; 
          } 
        } else {
          paramInt = j;
          if (updateOp.positionStart <= j) {
            if (updateOp.positionStart + updateOp.itemCount > j)
              return -1; 
            paramInt = j - updateOp.itemCount;
          } 
        } 
      } else {
        paramInt = j;
        if (updateOp.positionStart <= j)
          paramInt = j + updateOp.itemCount; 
      } 
      b++;
    } 
    return j;
  }
  
  void consumePostponedUpdates() {
    int i = this.mPostponedList.size();
    for (byte b = 0; b < i; b++)
      this.mCallback.onDispatchSecondPass(this.mPostponedList.get(b)); 
    recycleUpdateOpsAndClearList(this.mPostponedList);
    this.mExistingUpdateTypes = 0;
  }
  
  void consumeUpdatesInOnePass() {
    consumePostponedUpdates();
    int i = this.mPendingUpdates.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      int j = updateOp.cmd;
      if (j != 1) {
        if (j != 2) {
          if (j != 4) {
            if (j == 8) {
              this.mCallback.onDispatchSecondPass(updateOp);
              this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
            } 
          } else {
            this.mCallback.onDispatchSecondPass(updateOp);
            this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
          } 
        } else {
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
        } 
      } else {
        this.mCallback.onDispatchSecondPass(updateOp);
        this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
      } 
      Runnable runnable = this.mOnItemProcessedCallback;
      if (runnable != null)
        runnable.run(); 
    } 
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    this.mExistingUpdateTypes = 0;
  }
  
  void dispatchFirstPassAndUpdateViewHolders(UpdateOp paramUpdateOp, int paramInt) {
    this.mCallback.onDispatchFirstPass(paramUpdateOp);
    int i = paramUpdateOp.cmd;
    if (i != 2) {
      if (i == 4) {
        this.mCallback.markViewHoldersUpdated(paramInt, paramUpdateOp.itemCount, paramUpdateOp.payload);
      } else {
        throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
      } 
    } else {
      this.mCallback.offsetPositionsForRemovingInvisible(paramInt, paramUpdateOp.itemCount);
    } 
  }
  
  int findPositionOffset(int paramInt) {
    return findPositionOffset(paramInt, 0);
  }
  
  int findPositionOffset(int paramInt1, int paramInt2) {
    int i = this.mPostponedList.size();
    int j = paramInt2;
    for (paramInt2 = paramInt1; j < i; paramInt2 = paramInt1) {
      UpdateOp updateOp = this.mPostponedList.get(j);
      if (updateOp.cmd == 8) {
        if (updateOp.positionStart == paramInt2) {
          paramInt1 = updateOp.itemCount;
        } else {
          int k = paramInt2;
          if (updateOp.positionStart < paramInt2)
            k = paramInt2 - 1; 
          paramInt1 = k;
          if (updateOp.itemCount <= k)
            paramInt1 = k + 1; 
        } 
      } else {
        paramInt1 = paramInt2;
        if (updateOp.positionStart <= paramInt2)
          if (updateOp.cmd == 2) {
            if (paramInt2 < updateOp.positionStart + updateOp.itemCount)
              return -1; 
            paramInt1 = paramInt2 - updateOp.itemCount;
          } else {
            paramInt1 = paramInt2;
            if (updateOp.cmd == 1)
              paramInt1 = paramInt2 + updateOp.itemCount; 
          }  
      } 
      j++;
    } 
    return paramInt2;
  }
  
  boolean hasAnyUpdateTypes(int paramInt) {
    boolean bool;
    if ((this.mExistingUpdateTypes & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasPendingUpdates() {
    boolean bool;
    if (this.mPendingUpdates.size() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasUpdates() {
    boolean bool;
    if (!this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public UpdateOp obtainUpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject) {
    UpdateOp updateOp = (UpdateOp)this.mUpdateOpPool.acquire();
    if (updateOp == null) {
      paramObject = new UpdateOp(paramInt1, paramInt2, paramInt3, paramObject);
    } else {
      updateOp.cmd = paramInt1;
      updateOp.positionStart = paramInt2;
      updateOp.itemCount = paramInt3;
      updateOp.payload = paramObject;
      paramObject = updateOp;
    } 
    return (UpdateOp)paramObject;
  }
  
  boolean onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject) {
    boolean bool = false;
    if (paramInt2 < 1)
      return false; 
    this.mPendingUpdates.add(obtainUpdateOp(4, paramInt1, paramInt2, paramObject));
    this.mExistingUpdateTypes |= 0x4;
    if (this.mPendingUpdates.size() == 1)
      bool = true; 
    return bool;
  }
  
  boolean onItemRangeInserted(int paramInt1, int paramInt2) {
    boolean bool = false;
    if (paramInt2 < 1)
      return false; 
    this.mPendingUpdates.add(obtainUpdateOp(1, paramInt1, paramInt2, null));
    this.mExistingUpdateTypes |= 0x1;
    if (this.mPendingUpdates.size() == 1)
      bool = true; 
    return bool;
  }
  
  boolean onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = false;
    if (paramInt1 == paramInt2)
      return false; 
    if (paramInt3 == 1) {
      this.mPendingUpdates.add(obtainUpdateOp(8, paramInt1, paramInt2, null));
      this.mExistingUpdateTypes |= 0x8;
      if (this.mPendingUpdates.size() == 1)
        bool = true; 
      return bool;
    } 
    throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
  }
  
  boolean onItemRangeRemoved(int paramInt1, int paramInt2) {
    boolean bool = false;
    if (paramInt2 < 1)
      return false; 
    this.mPendingUpdates.add(obtainUpdateOp(2, paramInt1, paramInt2, null));
    this.mExistingUpdateTypes |= 0x2;
    if (this.mPendingUpdates.size() == 1)
      bool = true; 
    return bool;
  }
  
  void preProcess() {
    this.mOpReorderer.reorderOps(this.mPendingUpdates);
    int i = this.mPendingUpdates.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      int j = updateOp.cmd;
      if (j != 1) {
        if (j != 2) {
          if (j != 4) {
            if (j == 8)
              applyMove(updateOp); 
          } else {
            applyUpdate(updateOp);
          } 
        } else {
          applyRemove(updateOp);
        } 
      } else {
        applyAdd(updateOp);
      } 
      Runnable runnable = this.mOnItemProcessedCallback;
      if (runnable != null)
        runnable.run(); 
    } 
    this.mPendingUpdates.clear();
  }
  
  public void recycleUpdateOp(UpdateOp paramUpdateOp) {
    if (!this.mDisableRecycler) {
      paramUpdateOp.payload = null;
      this.mUpdateOpPool.release(paramUpdateOp);
    } 
  }
  
  void recycleUpdateOpsAndClearList(List<UpdateOp> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++)
      recycleUpdateOp(paramList.get(b)); 
    paramList.clear();
  }
  
  void reset() {
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    recycleUpdateOpsAndClearList(this.mPostponedList);
    this.mExistingUpdateTypes = 0;
  }
  
  static interface Callback {
    RecyclerView.ViewHolder findViewHolder(int param1Int);
    
    void markViewHoldersUpdated(int param1Int1, int param1Int2, Object param1Object);
    
    void offsetPositionsForAdd(int param1Int1, int param1Int2);
    
    void offsetPositionsForMove(int param1Int1, int param1Int2);
    
    void offsetPositionsForRemovingInvisible(int param1Int1, int param1Int2);
    
    void offsetPositionsForRemovingLaidOutOrNewView(int param1Int1, int param1Int2);
    
    void onDispatchFirstPass(AdapterHelper.UpdateOp param1UpdateOp);
    
    void onDispatchSecondPass(AdapterHelper.UpdateOp param1UpdateOp);
  }
  
  static class UpdateOp {
    static final int ADD = 1;
    
    static final int MOVE = 8;
    
    static final int POOL_SIZE = 30;
    
    static final int REMOVE = 2;
    
    static final int UPDATE = 4;
    
    int cmd;
    
    int itemCount;
    
    Object payload;
    
    int positionStart;
    
    UpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object) {
      this.cmd = param1Int1;
      this.positionStart = param1Int2;
      this.itemCount = param1Int3;
      this.payload = param1Object;
    }
    
    String cmdToString() {
      int i = this.cmd;
      return (i != 1) ? ((i != 2) ? ((i != 4) ? ((i != 8) ? "??" : "mv") : "up") : "rm") : "add";
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null || getClass() != param1Object.getClass())
        return false; 
      UpdateOp updateOp = (UpdateOp)param1Object;
      int i = this.cmd;
      if (i != updateOp.cmd)
        return false; 
      if (i == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == updateOp.positionStart && this.positionStart == updateOp.itemCount)
        return true; 
      if (this.itemCount != updateOp.itemCount)
        return false; 
      if (this.positionStart != updateOp.positionStart)
        return false; 
      param1Object = this.payload;
      if (param1Object != null) {
        if (!param1Object.equals(updateOp.payload))
          return false; 
      } else if (updateOp.payload != null) {
        return false;
      } 
      return true;
    }
    
    public int hashCode() {
      return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append("[");
      stringBuilder.append(cmdToString());
      stringBuilder.append(",s:");
      stringBuilder.append(this.positionStart);
      stringBuilder.append("c:");
      stringBuilder.append(this.itemCount);
      stringBuilder.append(",p:");
      stringBuilder.append(this.payload);
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/AdapterHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */