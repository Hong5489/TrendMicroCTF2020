package com.trendmicro.hippo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashtable implements Serializable, Iterable<Hashtable.Entry> {
  private Entry first = null;
  
  private Entry last = null;
  
  private final HashMap<Object, Entry> map = new HashMap<>();
  
  private Entry makeDummy() {
    Entry entry = new Entry();
    entry.clear();
    return entry;
  }
  
  public void clear() {
    iterator().forEachRemaining(Entry::clear);
    if (this.first != null) {
      Entry entry = new Entry();
      entry.clear();
      this.last.next = entry;
      this.last = entry;
      this.first = entry;
    } 
    this.map.clear();
  }
  
  public Object delete(Object paramObject) {
    paramObject = new Entry(paramObject, null);
    paramObject = this.map.remove(paramObject);
    if (paramObject == null)
      return null; 
    if (paramObject == this.first) {
      if (paramObject == this.last) {
        paramObject.clear();
        ((Entry)paramObject).prev = null;
      } else {
        Entry entry = ((Entry)paramObject).next;
        this.first = entry;
        entry.prev = null;
        if (this.first.next != null)
          this.first.next.prev = this.first; 
      } 
    } else {
      Entry entry = ((Entry)paramObject).prev;
      entry.next = ((Entry)paramObject).next;
      ((Entry)paramObject).prev = null;
      if (((Entry)paramObject).next != null) {
        ((Entry)paramObject).next.prev = entry;
      } else {
        this.last = entry;
      } 
    } 
    return paramObject.clear();
  }
  
  public Object get(Object paramObject) {
    paramObject = new Entry(paramObject, null);
    paramObject = this.map.get(paramObject);
    return (paramObject == null) ? null : ((Entry)paramObject).value;
  }
  
  public boolean has(Object paramObject) {
    paramObject = new Entry(paramObject, null);
    return this.map.containsKey(paramObject);
  }
  
  public Iterator<Entry> iterator() {
    return new Iter(this.first);
  }
  
  public void put(Object paramObject1, Object paramObject2) {
    paramObject1 = new Entry(paramObject1, paramObject2);
    Entry entry = (Entry)this.map.putIfAbsent(paramObject1, paramObject1);
    if (entry == null) {
      if (this.first == null) {
        this.last = (Entry)paramObject1;
        this.first = (Entry)paramObject1;
      } else {
        this.last.next = (Entry)paramObject1;
        ((Entry)paramObject1).prev = this.last;
        this.last = (Entry)paramObject1;
      } 
    } else {
      entry.value = paramObject2;
    } 
  }
  
  public int size() {
    return this.map.size();
  }
  
  public static final class Entry implements Serializable {
    protected boolean deleted;
    
    private final int hashCode;
    
    protected Object key;
    
    protected Entry next;
    
    protected Entry prev;
    
    protected Object value;
    
    Entry() {
      this.hashCode = 0;
    }
    
    Entry(Object param1Object1, Object param1Object2) {
      if (param1Object1 instanceof Number && !(param1Object1 instanceof Double)) {
        this.key = Double.valueOf(((Number)param1Object1).doubleValue());
      } else {
        this.key = param1Object1;
      } 
      if (this.key == null) {
        this.hashCode = 0;
      } else if (param1Object1.equals(Double.valueOf(ScriptRuntime.negativeZero))) {
        this.hashCode = 0;
      } else {
        this.hashCode = this.key.hashCode();
      } 
      this.value = param1Object2;
    }
    
    Object clear() {
      Object object = this.value;
      this.key = Undefined.instance;
      this.value = Undefined.instance;
      this.deleted = true;
      return object;
    }
    
    public boolean equals(Object param1Object) {
      try {
        return ScriptRuntime.sameZero(this.key, ((Entry)param1Object).key);
      } catch (ClassCastException classCastException) {
        return false;
      } 
    }
    
    public int hashCode() {
      return this.hashCode;
    }
    
    public Object key() {
      return this.key;
    }
    
    public Object value() {
      return this.value;
    }
  }
  
  private final class Iter implements Iterator<Entry> {
    private Hashtable.Entry pos;
    
    Iter(Hashtable.Entry param1Entry) {
      Hashtable.Entry entry = Hashtable.this.makeDummy();
      entry.next = param1Entry;
      this.pos = entry;
    }
    
    private void skipDeleted() {
      while (this.pos.next != null && this.pos.next.deleted)
        this.pos = this.pos.next; 
    }
    
    public boolean hasNext() {
      boolean bool;
      skipDeleted();
      Hashtable.Entry entry = this.pos;
      if (entry != null && entry.next != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Hashtable.Entry next() {
      skipDeleted();
      Hashtable.Entry entry = this.pos;
      if (entry != null && entry.next != null) {
        entry = this.pos.next;
        this.pos = this.pos.next;
        return entry;
      } 
      throw new NoSuchElementException();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Hashtable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */