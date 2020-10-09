package android.support.v7.widget;

import java.util.List;

class OpReorderer {
  final Callback mCallback;
  
  OpReorderer(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> paramList) {
    boolean bool = false;
    int i = paramList.size() - 1;
    while (i >= 0) {
      boolean bool1;
      if (((AdapterHelper.UpdateOp)paramList.get(i)).cmd == 8) {
        bool1 = bool;
        if (bool)
          return i; 
      } else {
        bool1 = true;
      } 
      i--;
      bool = bool1;
    } 
    return -1;
  }
  
  private void swapMoveAdd(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    int i = 0;
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart)
      i = 0 - 1; 
    int j = i;
    if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart)
      j = i + 1; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.positionStart)
      paramUpdateOp1.positionStart += paramUpdateOp2.itemCount; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.itemCount)
      paramUpdateOp1.itemCount += paramUpdateOp2.itemCount; 
    paramUpdateOp2.positionStart += j;
    paramList.set(paramInt1, paramUpdateOp2);
    paramList.set(paramInt2, paramUpdateOp1);
  }
  
  private void swapMoveOp(List<AdapterHelper.UpdateOp> paramList, int paramInt1, int paramInt2) {
    AdapterHelper.UpdateOp updateOp1 = paramList.get(paramInt1);
    AdapterHelper.UpdateOp updateOp2 = paramList.get(paramInt2);
    int i = updateOp2.cmd;
    if (i != 1) {
      if (i != 2) {
        if (i == 4)
          swapMoveUpdate(paramList, paramInt1, updateOp1, paramInt2, updateOp2); 
      } else {
        swapMoveRemove(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
      } 
    } else {
      swapMoveAdd(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
    } 
  }
  
  void reorderOps(List<AdapterHelper.UpdateOp> paramList) {
    while (true) {
      int i = getLastMoveOutOfOrder(paramList);
      if (i != -1) {
        swapMoveOp(paramList, i, i + 1);
        continue;
      } 
      break;
    } 
  }
  
  void swapMoveRemove(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    int j;
    boolean bool;
    AdapterHelper.UpdateOp updateOp = null;
    int i = 0;
    if (paramUpdateOp1.positionStart < paramUpdateOp1.itemCount) {
      boolean bool1 = false;
      j = i;
      bool = bool1;
      if (paramUpdateOp2.positionStart == paramUpdateOp1.positionStart) {
        j = i;
        bool = bool1;
        if (paramUpdateOp2.itemCount == paramUpdateOp1.itemCount - paramUpdateOp1.positionStart) {
          j = 1;
          bool = bool1;
        } 
      } 
    } else {
      boolean bool1 = true;
      j = i;
      bool = bool1;
      if (paramUpdateOp2.positionStart == paramUpdateOp1.itemCount + 1) {
        j = i;
        bool = bool1;
        if (paramUpdateOp2.itemCount == paramUpdateOp1.positionStart - paramUpdateOp1.itemCount) {
          j = 1;
          bool = bool1;
        } 
      } 
    } 
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart--;
    } else if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      paramUpdateOp2.itemCount--;
      paramUpdateOp1.cmd = 2;
      paramUpdateOp1.itemCount = 1;
      if (paramUpdateOp2.itemCount == 0) {
        paramList.remove(paramInt2);
        this.mCallback.recycleUpdateOp(paramUpdateOp2);
      } 
      return;
    } 
    if (paramUpdateOp1.positionStart <= paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart++;
    } else if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      int k = paramUpdateOp2.positionStart;
      int m = paramUpdateOp2.itemCount;
      i = paramUpdateOp1.positionStart;
      updateOp = this.mCallback.obtainUpdateOp(2, paramUpdateOp1.positionStart + 1, k + m - i, null);
      paramUpdateOp2.itemCount = paramUpdateOp1.positionStart - paramUpdateOp2.positionStart;
    } 
    if (j != 0) {
      paramList.set(paramInt1, paramUpdateOp2);
      paramList.remove(paramInt2);
      this.mCallback.recycleUpdateOp(paramUpdateOp1);
      return;
    } 
    if (bool) {
      if (updateOp != null) {
        if (paramUpdateOp1.positionStart > updateOp.positionStart)
          paramUpdateOp1.positionStart -= updateOp.itemCount; 
        if (paramUpdateOp1.itemCount > updateOp.positionStart)
          paramUpdateOp1.itemCount -= updateOp.itemCount; 
      } 
      if (paramUpdateOp1.positionStart > paramUpdateOp2.positionStart)
        paramUpdateOp1.positionStart -= paramUpdateOp2.itemCount; 
      if (paramUpdateOp1.itemCount > paramUpdateOp2.positionStart)
        paramUpdateOp1.itemCount -= paramUpdateOp2.itemCount; 
    } else {
      if (updateOp != null) {
        if (paramUpdateOp1.positionStart >= updateOp.positionStart)
          paramUpdateOp1.positionStart -= updateOp.itemCount; 
        if (paramUpdateOp1.itemCount >= updateOp.positionStart)
          paramUpdateOp1.itemCount -= updateOp.itemCount; 
      } 
      if (paramUpdateOp1.positionStart >= paramUpdateOp2.positionStart)
        paramUpdateOp1.positionStart -= paramUpdateOp2.itemCount; 
      if (paramUpdateOp1.itemCount >= paramUpdateOp2.positionStart)
        paramUpdateOp1.itemCount -= paramUpdateOp2.itemCount; 
    } 
    paramList.set(paramInt1, paramUpdateOp2);
    if (paramUpdateOp1.positionStart != paramUpdateOp1.itemCount) {
      paramList.set(paramInt2, paramUpdateOp1);
    } else {
      paramList.remove(paramInt2);
    } 
    if (updateOp != null)
      paramList.add(paramInt1, updateOp); 
  }
  
  void swapMoveUpdate(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    AdapterHelper.UpdateOp updateOp1 = null;
    AdapterHelper.UpdateOp updateOp2 = null;
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart--;
    } else if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      paramUpdateOp2.itemCount--;
      updateOp1 = this.mCallback.obtainUpdateOp(4, paramUpdateOp1.positionStart, 1, paramUpdateOp2.payload);
    } 
    if (paramUpdateOp1.positionStart <= paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart++;
    } else if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      int i = paramUpdateOp2.positionStart + paramUpdateOp2.itemCount - paramUpdateOp1.positionStart;
      updateOp2 = this.mCallback.obtainUpdateOp(4, paramUpdateOp1.positionStart + 1, i, paramUpdateOp2.payload);
      paramUpdateOp2.itemCount -= i;
    } 
    paramList.set(paramInt2, paramUpdateOp1);
    if (paramUpdateOp2.itemCount > 0) {
      paramList.set(paramInt1, paramUpdateOp2);
    } else {
      paramList.remove(paramInt1);
      this.mCallback.recycleUpdateOp(paramUpdateOp2);
    } 
    if (updateOp1 != null)
      paramList.add(paramInt1, updateOp1); 
    if (updateOp2 != null)
      paramList.add(paramInt1, updateOp2); 
  }
  
  static interface Callback {
    AdapterHelper.UpdateOp obtainUpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object);
    
    void recycleUpdateOp(AdapterHelper.UpdateOp param1UpdateOp);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/OpReorderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */