package com.trendmicro.hippo;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class HashSlotMap implements SlotMap {
  private final LinkedHashMap<Object, ScriptableObject.Slot> map = new LinkedHashMap<>();
  
  private ScriptableObject.Slot createSlot(Object paramObject1, int paramInt, Object paramObject2, ScriptableObject.SlotAccess paramSlotAccess) {
    ScriptableObject.Slot slot = this.map.get(paramObject2);
    if (slot != null) {
      if (paramSlotAccess == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER && !(slot instanceof ScriptableObject.GetterSlot)) {
        paramObject1 = new ScriptableObject.GetterSlot(paramObject2, slot.indexOrHash, slot.getAttributes());
      } else {
        if (paramSlotAccess == ScriptableObject.SlotAccess.CONVERT_ACCESSOR_TO_DATA && slot instanceof ScriptableObject.GetterSlot) {
          paramObject1 = new ScriptableObject.Slot(paramObject2, slot.indexOrHash, slot.getAttributes());
          ((ScriptableObject.Slot)paramObject1).value = slot.value;
          this.map.put(paramObject2, paramObject1);
          return (ScriptableObject.Slot)paramObject1;
        } 
        return (paramSlotAccess == ScriptableObject.SlotAccess.MODIFY_CONST) ? null : slot;
      } 
      ((ScriptableObject.Slot)paramObject1).value = slot.value;
      this.map.put(paramObject2, paramObject1);
      return (ScriptableObject.Slot)paramObject1;
    } 
    if (paramSlotAccess == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER) {
      paramObject1 = new ScriptableObject.GetterSlot(paramObject1, paramInt, 0);
    } else {
      paramObject1 = new ScriptableObject.Slot(paramObject1, paramInt, 0);
    } 
    if (paramSlotAccess == ScriptableObject.SlotAccess.MODIFY_CONST)
      paramObject1.setAttributes(13); 
    addSlot((ScriptableObject.Slot)paramObject1);
    return (ScriptableObject.Slot)paramObject1;
  }
  
  public void addSlot(ScriptableObject.Slot paramSlot) {
    Object object;
    if (paramSlot.name == null) {
      object = String.valueOf(paramSlot.indexOrHash);
    } else {
      object = paramSlot.name;
    } 
    this.map.put(object, paramSlot);
  }
  
  public ScriptableObject.Slot get(Object paramObject, int paramInt, ScriptableObject.SlotAccess paramSlotAccess) {
    Object object;
    if (paramObject == null) {
      object = String.valueOf(paramInt);
    } else {
      object = paramObject;
    } 
    ScriptableObject.Slot slot = this.map.get(object);
    int i = null.$SwitchMap$com$trendmicro$hippo$ScriptableObject$SlotAccess[paramSlotAccess.ordinal()];
    if (i != 1) {
      if (i != 2 && i != 3) {
        if (i != 4) {
          if (i == 5 && !(slot instanceof ScriptableObject.GetterSlot))
            return slot; 
        } else if (slot instanceof ScriptableObject.GetterSlot) {
          return slot;
        } 
      } else if (slot != null) {
        return slot;
      } 
      return createSlot(paramObject, paramInt, object, paramSlotAccess);
    } 
    return slot;
  }
  
  public boolean isEmpty() {
    return this.map.isEmpty();
  }
  
  public Iterator<ScriptableObject.Slot> iterator() {
    return this.map.values().iterator();
  }
  
  public ScriptableObject.Slot query(Object paramObject, int paramInt) {
    if (paramObject == null)
      paramObject = String.valueOf(paramInt); 
    return this.map.get(paramObject);
  }
  
  public void remove(Object paramObject, int paramInt) {
    Object object;
    if (paramObject == null) {
      object = String.valueOf(paramInt);
    } else {
      object = paramObject;
    } 
    ScriptableObject.Slot slot = this.map.get(object);
    if (slot != null) {
      if ((slot.getAttributes() & 0x4) != 0) {
        if (!Context.getContext().isStrictMode())
          return; 
        throw ScriptRuntime.typeError1("msg.delete.property.with.configurable.false", paramObject);
      } 
      this.map.remove(object);
    } 
  }
  
  public int size() {
    return this.map.size();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/HashSlotMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */