package com.trendmicro.hippo;

import java.util.Iterator;

public class EmbeddedSlotMap implements SlotMap {
  private static final int INITIAL_SLOT_SIZE = 4;
  
  private int count;
  
  private ScriptableObject.Slot firstAdded;
  
  private ScriptableObject.Slot lastAdded;
  
  private ScriptableObject.Slot[] slots;
  
  private void addKnownAbsentSlot(ScriptableObject.Slot[] paramArrayOfSlot, ScriptableObject.Slot paramSlot) {
    int i = getSlotIndex(paramArrayOfSlot.length, paramSlot.indexOrHash);
    ScriptableObject.Slot slot = paramArrayOfSlot[i];
    paramArrayOfSlot[i] = paramSlot;
    paramSlot.next = slot;
  }
  
  private void copyTable(ScriptableObject.Slot[] paramArrayOfSlot1, ScriptableObject.Slot[] paramArrayOfSlot2) {
    int i = paramArrayOfSlot1.length;
    for (byte b = 0; b < i; b++) {
      for (ScriptableObject.Slot slot = paramArrayOfSlot1[b]; slot != null; slot = slot1) {
        ScriptableObject.Slot slot1 = slot.next;
        slot.next = null;
        addKnownAbsentSlot(paramArrayOfSlot2, slot);
      } 
    } 
  }
  
  private ScriptableObject.Slot createSlot(Object paramObject, int paramInt, ScriptableObject.SlotAccess paramSlotAccess, ScriptableObject.Slot paramSlot) {
    ScriptableObject.Slot slot;
    if (this.count == 0) {
      this.slots = new ScriptableObject.Slot[4];
    } else if (paramSlot != null) {
      int j = getSlotIndex(this.slots.length, paramInt);
      ScriptableObject.Slot slot1 = this.slots[j];
      for (paramSlot = slot1; paramSlot != null && (paramSlot.indexOrHash != paramInt || (paramSlot.name != paramObject && (paramObject == null || !paramObject.equals(paramSlot.name)))); paramSlot = paramSlot.next)
        slot1 = paramSlot; 
      if (paramSlot != null) {
        if (paramSlotAccess == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER && !(paramSlot instanceof ScriptableObject.GetterSlot)) {
          paramObject = new ScriptableObject.GetterSlot(paramObject, paramInt, paramSlot.getAttributes());
        } else if (paramSlotAccess == ScriptableObject.SlotAccess.CONVERT_ACCESSOR_TO_DATA && paramSlot instanceof ScriptableObject.GetterSlot) {
          paramObject = new ScriptableObject.Slot(paramObject, paramInt, paramSlot.getAttributes());
        } else {
          return (paramSlotAccess == ScriptableObject.SlotAccess.MODIFY_CONST) ? null : paramSlot;
        } 
        ((ScriptableObject.Slot)paramObject).value = paramSlot.value;
        ((ScriptableObject.Slot)paramObject).next = paramSlot.next;
        if (paramSlot == this.firstAdded) {
          this.firstAdded = (ScriptableObject.Slot)paramObject;
        } else {
          for (slot = this.firstAdded; slot != null && slot.orderedNext != paramSlot; slot = slot.orderedNext);
          if (slot != null)
            slot.orderedNext = (ScriptableObject.Slot)paramObject; 
        } 
        ((ScriptableObject.Slot)paramObject).orderedNext = paramSlot.orderedNext;
        if (paramSlot == this.lastAdded)
          this.lastAdded = (ScriptableObject.Slot)paramObject; 
        if (slot1 == paramSlot) {
          this.slots[j] = (ScriptableObject.Slot)paramObject;
        } else {
          slot1.next = (ScriptableObject.Slot)paramObject;
        } 
        return (ScriptableObject.Slot)paramObject;
      } 
    } 
    int i = this.count;
    ScriptableObject.Slot[] arrayOfSlot = this.slots;
    if ((i + 1) * 4 > arrayOfSlot.length * 3) {
      ScriptableObject.Slot[] arrayOfSlot1 = new ScriptableObject.Slot[arrayOfSlot.length * 2];
      copyTable(arrayOfSlot, arrayOfSlot1);
      this.slots = arrayOfSlot1;
    } 
    if (slot == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER) {
      paramObject = new ScriptableObject.GetterSlot(paramObject, paramInt, 0);
    } else {
      paramObject = new ScriptableObject.Slot(paramObject, paramInt, 0);
    } 
    if (slot == ScriptableObject.SlotAccess.MODIFY_CONST)
      paramObject.setAttributes(13); 
    insertNewSlot((ScriptableObject.Slot)paramObject);
    return (ScriptableObject.Slot)paramObject;
  }
  
  private static int getSlotIndex(int paramInt1, int paramInt2) {
    return paramInt1 - 1 & paramInt2;
  }
  
  private void insertNewSlot(ScriptableObject.Slot paramSlot) {
    this.count++;
    ScriptableObject.Slot slot = this.lastAdded;
    if (slot != null)
      slot.orderedNext = paramSlot; 
    if (this.firstAdded == null)
      this.firstAdded = paramSlot; 
    this.lastAdded = paramSlot;
    addKnownAbsentSlot(this.slots, paramSlot);
  }
  
  public void addSlot(ScriptableObject.Slot paramSlot) {
    if (this.slots == null)
      this.slots = new ScriptableObject.Slot[4]; 
    insertNewSlot(paramSlot);
  }
  
  public ScriptableObject.Slot get(Object paramObject, int paramInt, ScriptableObject.SlotAccess paramSlotAccess) {
    if (this.slots == null && paramSlotAccess == ScriptableObject.SlotAccess.QUERY)
      return null; 
    if (paramObject != null)
      paramInt = paramObject.hashCode(); 
    Object object = null;
    ScriptableObject.Slot[] arrayOfSlot = this.slots;
    if (arrayOfSlot != null) {
      int i = getSlotIndex(arrayOfSlot.length, paramInt);
      ScriptableObject.Slot slot;
      for (slot = this.slots[i]; slot != null; slot = slot.next) {
        object = slot.name;
        if (paramInt == slot.indexOrHash && (object == paramObject || (paramObject != null && paramObject.equals(object))))
          break; 
      } 
      i = null.$SwitchMap$com$trendmicro$hippo$ScriptableObject$SlotAccess[paramSlotAccess.ordinal()];
      if (i != 1) {
        if (i != 2 && i != 3) {
          if (i != 4) {
            if (i != 5) {
              object = slot;
            } else {
              object = slot;
              if (!(slot instanceof ScriptableObject.GetterSlot))
                return slot; 
            } 
          } else {
            object = slot;
            if (slot instanceof ScriptableObject.GetterSlot)
              return slot; 
          } 
        } else {
          object = slot;
          if (slot != null)
            return slot; 
        } 
      } else {
        return slot;
      } 
    } 
    return createSlot(paramObject, paramInt, paramSlotAccess, (ScriptableObject.Slot)object);
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.count == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Iterator<ScriptableObject.Slot> iterator() {
    return new Iter(this.firstAdded);
  }
  
  public ScriptableObject.Slot query(Object paramObject, int paramInt) {
    if (this.slots == null)
      return null; 
    if (paramObject != null)
      paramInt = paramObject.hashCode(); 
    int i = getSlotIndex(this.slots.length, paramInt);
    for (ScriptableObject.Slot slot = this.slots[i]; slot != null; slot = slot.next) {
      Object object = slot.name;
      if (paramInt == slot.indexOrHash && (object == paramObject || (paramObject != null && paramObject.equals(object))))
        return slot; 
    } 
    return null;
  }
  
  public void remove(Object paramObject, int paramInt) {
    if (paramObject != null)
      paramInt = paramObject.hashCode(); 
    if (this.count != 0) {
      int i = getSlotIndex(this.slots.length, paramInt);
      ScriptableObject.Slot slot1 = this.slots[i];
      ScriptableObject.Slot slot2;
      for (slot2 = slot1; slot2 != null && (slot2.indexOrHash != paramInt || (slot2.name != paramObject && (paramObject == null || !paramObject.equals(slot2.name)))); slot2 = slot2.next)
        slot1 = slot2; 
      if (slot2 != null) {
        if ((slot2.getAttributes() & 0x4) != 0) {
          if (!Context.getContext().isStrictMode())
            return; 
          throw ScriptRuntime.typeError1("msg.delete.property.with.configurable.false", paramObject);
        } 
        this.count--;
        if (slot1 == slot2) {
          this.slots[i] = slot2.next;
        } else {
          slot1.next = slot2.next;
        } 
        if (slot2 == this.firstAdded) {
          paramObject = null;
          this.firstAdded = slot2.orderedNext;
        } else {
          for (paramObject = this.firstAdded; ((ScriptableObject.Slot)paramObject).orderedNext != slot2; paramObject = ((ScriptableObject.Slot)paramObject).orderedNext);
          ((ScriptableObject.Slot)paramObject).orderedNext = slot2.orderedNext;
        } 
        if (slot2 == this.lastAdded)
          this.lastAdded = (ScriptableObject.Slot)paramObject; 
      } 
    } 
  }
  
  public int size() {
    return this.count;
  }
  
  private static final class Iter implements Iterator<ScriptableObject.Slot> {
    private ScriptableObject.Slot next;
    
    Iter(ScriptableObject.Slot param1Slot) {
      this.next = param1Slot;
    }
    
    public boolean hasNext() {
      boolean bool;
      if (this.next != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public ScriptableObject.Slot next() {
      ScriptableObject.Slot slot = this.next;
      this.next = this.next.orderedNext;
      return slot;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/EmbeddedSlotMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */