package com.trendmicro.hippo;

import java.util.Iterator;

class SlotMapContainer implements SlotMap {
  private static final int LARGE_HASH_SIZE = 2000;
  
  protected SlotMap map;
  
  SlotMapContainer(int paramInt) {
    if (paramInt > 2000) {
      this.map = new HashSlotMap();
    } else {
      this.map = new EmbeddedSlotMap();
    } 
  }
  
  public void addSlot(ScriptableObject.Slot paramSlot) {
    checkMapSize();
    this.map.addSlot(paramSlot);
  }
  
  protected void checkMapSize() {
    SlotMap slotMap = this.map;
    if (slotMap instanceof EmbeddedSlotMap && slotMap.size() >= 2000) {
      slotMap = new HashSlotMap();
      Iterator<ScriptableObject.Slot> iterator = this.map.iterator();
      while (iterator.hasNext())
        slotMap.addSlot(iterator.next()); 
      this.map = slotMap;
    } 
  }
  
  public int dirtySize() {
    return this.map.size();
  }
  
  public ScriptableObject.Slot get(Object paramObject, int paramInt, ScriptableObject.SlotAccess paramSlotAccess) {
    if (paramSlotAccess != ScriptableObject.SlotAccess.QUERY)
      checkMapSize(); 
    return this.map.get(paramObject, paramInt, paramSlotAccess);
  }
  
  public boolean isEmpty() {
    return this.map.isEmpty();
  }
  
  public Iterator<ScriptableObject.Slot> iterator() {
    return this.map.iterator();
  }
  
  public ScriptableObject.Slot query(Object paramObject, int paramInt) {
    return this.map.query(paramObject, paramInt);
  }
  
  public long readLock() {
    return 0L;
  }
  
  public void remove(Object paramObject, int paramInt) {
    this.map.remove(paramObject, paramInt);
  }
  
  public int size() {
    return this.map.size();
  }
  
  public void unlockRead(long paramLong) {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SlotMapContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */