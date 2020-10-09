package com.trendmicro.hippo;

import java.util.Iterator;
import java.util.concurrent.locks.StampedLock;

class ThreadSafeSlotMapContainer extends SlotMapContainer {
  private final StampedLock lock = new StampedLock();
  
  ThreadSafeSlotMapContainer(int paramInt) {
    super(paramInt);
  }
  
  public void addSlot(ScriptableObject.Slot paramSlot) {
    long l = this.lock.writeLock();
    try {
      checkMapSize();
      this.map.addSlot(paramSlot);
      return;
    } finally {
      this.lock.unlockWrite(l);
    } 
  }
  
  protected void checkMapSize() {
    super.checkMapSize();
  }
  
  public int dirtySize() {
    return this.map.size();
  }
  
  public ScriptableObject.Slot get(Object paramObject, int paramInt, ScriptableObject.SlotAccess paramSlotAccess) {
    long l = this.lock.writeLock();
    try {
      if (paramSlotAccess != ScriptableObject.SlotAccess.QUERY)
        checkMapSize(); 
      paramObject = this.map.get(paramObject, paramInt, paramSlotAccess);
      return (ScriptableObject.Slot)paramObject;
    } finally {
      this.lock.unlockWrite(l);
    } 
  }
  
  public boolean isEmpty() {
    long l = this.lock.tryOptimisticRead();
    boolean bool = this.map.isEmpty();
    if (this.lock.validate(l))
      return bool; 
    l = this.lock.readLock();
    try {
      bool = this.map.isEmpty();
      return bool;
    } finally {
      this.lock.unlockRead(l);
    } 
  }
  
  public Iterator<ScriptableObject.Slot> iterator() {
    return this.map.iterator();
  }
  
  public ScriptableObject.Slot query(Object paramObject, int paramInt) {
    long l = this.lock.tryOptimisticRead();
    ScriptableObject.Slot slot = this.map.query(paramObject, paramInt);
    if (this.lock.validate(l))
      return slot; 
    l = this.lock.readLock();
    try {
      paramObject = this.map.query(paramObject, paramInt);
      return (ScriptableObject.Slot)paramObject;
    } finally {
      this.lock.unlockRead(l);
    } 
  }
  
  public long readLock() {
    return this.lock.readLock();
  }
  
  public void remove(Object paramObject, int paramInt) {
    long l = this.lock.writeLock();
    try {
      this.map.remove(paramObject, paramInt);
      return;
    } finally {
      this.lock.unlockWrite(l);
    } 
  }
  
  public int size() {
    long l = this.lock.tryOptimisticRead();
    int i = this.map.size();
    if (this.lock.validate(l))
      return i; 
    l = this.lock.readLock();
    try {
      i = this.map.size();
      return i;
    } finally {
      this.lock.unlockRead(l);
    } 
  }
  
  public void unlockRead(long paramLong) {
    this.lock.unlockRead(paramLong);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ThreadSafeSlotMapContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */