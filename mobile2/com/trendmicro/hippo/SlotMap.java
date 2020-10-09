package com.trendmicro.hippo;

public interface SlotMap extends Iterable<ScriptableObject.Slot> {
  void addSlot(ScriptableObject.Slot paramSlot);
  
  ScriptableObject.Slot get(Object paramObject, int paramInt, ScriptableObject.SlotAccess paramSlotAccess);
  
  boolean isEmpty();
  
  ScriptableObject.Slot query(Object paramObject, int paramInt);
  
  void remove(Object paramObject, int paramInt);
  
  int size();
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SlotMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */