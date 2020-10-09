package com.trendmicro.hippo.typedarrays;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class NativeTypedArrayIterator<T> implements ListIterator<T> {
  private int lastPosition = -1;
  
  private int position;
  
  private final NativeTypedArrayView<T> view;
  
  NativeTypedArrayIterator(NativeTypedArrayView<T> paramNativeTypedArrayView, int paramInt) {
    this.view = paramNativeTypedArrayView;
    this.position = paramInt;
  }
  
  public void add(T paramT) {
    throw new UnsupportedOperationException();
  }
  
  public boolean hasNext() {
    boolean bool;
    if (this.position < this.view.length) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasPrevious() {
    boolean bool;
    if (this.position > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public T next() {
    if (hasNext()) {
      T t = this.view.get(this.position);
      int i = this.position;
      this.lastPosition = i;
      this.position = i + 1;
      return t;
    } 
    throw new NoSuchElementException();
  }
  
  public int nextIndex() {
    return this.position;
  }
  
  public T previous() {
    if (hasPrevious()) {
      int i = this.position - 1;
      this.position = i;
      this.lastPosition = i;
      return this.view.get(i);
    } 
    throw new NoSuchElementException();
  }
  
  public int previousIndex() {
    return this.position - 1;
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  public void set(T paramT) {
    int i = this.lastPosition;
    if (i >= 0) {
      this.view.js_set(i, paramT);
      return;
    } 
    throw new IllegalStateException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/NativeTypedArrayIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */