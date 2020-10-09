package com.trendmicro.hippo;

import java.util.Comparator;

public final class Sorting {
  private static final int SMALLSORT = 16;
  
  private static final Sorting sorting = new Sorting();
  
  public static Sorting get() {
    return sorting;
  }
  
  private void hybridSort(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Comparator<Object> paramComparator, int paramInt3) {
    if (paramInt1 < paramInt2) {
      if (paramInt3 == 0 || paramInt2 - paramInt1 <= 16) {
        insertionSort(paramArrayOfObject, paramInt1, paramInt2, paramComparator);
        return;
      } 
      int i = partition(paramArrayOfObject, paramInt1, paramInt2, paramComparator);
      hybridSort(paramArrayOfObject, paramInt1, i, paramComparator, paramInt3 - 1);
      hybridSort(paramArrayOfObject, i + 1, paramInt2, paramComparator, paramInt3 - 1);
    } 
  }
  
  private void insertionSort(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Comparator<Object> paramComparator) {
    for (int i = paramInt1; i <= paramInt2; i++) {
      Object object = paramArrayOfObject[i];
      int j;
      for (j = i - 1; j >= paramInt1 && paramComparator.compare(paramArrayOfObject[j], object) > 0; j--)
        paramArrayOfObject[j + 1] = paramArrayOfObject[j]; 
      paramArrayOfObject[j + 1] = object;
    } 
  }
  
  private int log2(int paramInt) {
    return (int)(Math.log10(paramInt) / Math.log10(2.0D));
  }
  
  private int partition(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Comparator<Object> paramComparator) {
    int i = median(paramArrayOfObject, paramInt1, paramInt2, paramComparator);
    Object object = paramArrayOfObject[i];
    paramArrayOfObject[i] = paramArrayOfObject[paramInt1];
    paramArrayOfObject[paramInt1] = object;
    int j = paramInt1;
    i = paramInt2 + 1;
    while (true) {
      int k = j + 1;
      j = i;
      if (paramComparator.compare(paramArrayOfObject[k], object) < 0) {
        j = k;
        if (k == paramInt2) {
          j = i;
        } else {
          continue;
        } 
      } 
      while (true) {
        i = j - 1;
        if (paramComparator.compare(paramArrayOfObject[i], object) >= 0) {
          j = i;
          if (i == paramInt1)
            break; 
          continue;
        } 
        break;
      } 
      if (k >= i) {
        swap(paramArrayOfObject, paramInt1, i);
        return i;
      } 
      swap(paramArrayOfObject, k, i);
      j = k;
    } 
  }
  
  private void swap(Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
    Object object = paramArrayOfObject[paramInt1];
    paramArrayOfObject[paramInt1] = paramArrayOfObject[paramInt2];
    paramArrayOfObject[paramInt2] = object;
  }
  
  public void hybridSort(Object[] paramArrayOfObject, Comparator<Object> paramComparator) {
    hybridSort(paramArrayOfObject, 0, paramArrayOfObject.length - 1, paramComparator, log2(paramArrayOfObject.length) * 2);
  }
  
  public void insertionSort(Object[] paramArrayOfObject, Comparator<Object> paramComparator) {
    insertionSort(paramArrayOfObject, 0, paramArrayOfObject.length - 1, paramComparator);
  }
  
  public int median(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Comparator<Object> paramComparator) {
    int i = (paramInt2 - paramInt1) / 2 + paramInt1;
    int j = paramInt1;
    int k = j;
    if (paramComparator.compare(paramArrayOfObject[j], paramArrayOfObject[i]) > 0)
      k = i; 
    j = k;
    if (paramComparator.compare(paramArrayOfObject[k], paramArrayOfObject[paramInt2]) > 0)
      j = paramInt2; 
    if (j == paramInt1) {
      if (paramComparator.compare(paramArrayOfObject[i], paramArrayOfObject[paramInt2]) < 0) {
        paramInt1 = i;
      } else {
        paramInt1 = paramInt2;
      } 
      return paramInt1;
    } 
    if (j == i) {
      if (paramComparator.compare(paramArrayOfObject[paramInt1], paramArrayOfObject[paramInt2]) >= 0)
        paramInt1 = paramInt2; 
      return paramInt1;
    } 
    if (paramComparator.compare(paramArrayOfObject[paramInt1], paramArrayOfObject[i]) >= 0)
      paramInt1 = i; 
    return paramInt1;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Sorting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */