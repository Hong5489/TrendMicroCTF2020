package com.trendmicro.hippo;

import java.io.Serializable;

public class ConsString implements CharSequence, Serializable {
  private static final long serialVersionUID = -8432806714471372570L;
  
  private boolean isFlat;
  
  private CharSequence left;
  
  private final int length;
  
  private CharSequence right;
  
  public ConsString(CharSequence paramCharSequence1, CharSequence paramCharSequence2) {
    this.left = paramCharSequence1;
    this.right = paramCharSequence2;
    this.length = paramCharSequence1.length() + this.right.length();
    this.isFlat = false;
  }
  
  private String flatten() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isFlat : Z
    //   6: ifne -> 190
    //   9: aload_0
    //   10: getfield length : I
    //   13: newarray char
    //   15: astore_1
    //   16: aload_0
    //   17: getfield length : I
    //   20: istore_2
    //   21: new java/util/ArrayDeque
    //   24: astore_3
    //   25: aload_3
    //   26: invokespecial <init> : ()V
    //   29: aload_3
    //   30: aload_0
    //   31: getfield left : Ljava/lang/CharSequence;
    //   34: invokevirtual addFirst : (Ljava/lang/Object;)V
    //   37: aload_0
    //   38: getfield right : Ljava/lang/CharSequence;
    //   41: astore #4
    //   43: aload #4
    //   45: astore #5
    //   47: aload #4
    //   49: instanceof com/trendmicro/hippo/ConsString
    //   52: ifeq -> 99
    //   55: aload #4
    //   57: checkcast com/trendmicro/hippo/ConsString
    //   60: astore #4
    //   62: aload #4
    //   64: getfield isFlat : Z
    //   67: ifeq -> 80
    //   70: aload #4
    //   72: getfield left : Ljava/lang/CharSequence;
    //   75: astore #5
    //   77: goto -> 99
    //   80: aload_3
    //   81: aload #4
    //   83: getfield left : Ljava/lang/CharSequence;
    //   86: invokevirtual addFirst : (Ljava/lang/Object;)V
    //   89: aload #4
    //   91: getfield right : Ljava/lang/CharSequence;
    //   94: astore #5
    //   96: goto -> 153
    //   99: aload #5
    //   101: checkcast java/lang/String
    //   104: astore #4
    //   106: iload_2
    //   107: aload #4
    //   109: invokevirtual length : ()I
    //   112: isub
    //   113: istore_2
    //   114: aload #4
    //   116: iconst_0
    //   117: aload #4
    //   119: invokevirtual length : ()I
    //   122: aload_1
    //   123: iload_2
    //   124: invokevirtual getChars : (II[CI)V
    //   127: aload_3
    //   128: invokevirtual isEmpty : ()Z
    //   131: ifeq -> 140
    //   134: aconst_null
    //   135: astore #4
    //   137: goto -> 149
    //   140: aload_3
    //   141: invokevirtual removeFirst : ()Ljava/lang/Object;
    //   144: checkcast java/lang/CharSequence
    //   147: astore #4
    //   149: aload #4
    //   151: astore #5
    //   153: aload #5
    //   155: astore #4
    //   157: aload #5
    //   159: ifnonnull -> 43
    //   162: new java/lang/String
    //   165: astore #4
    //   167: aload #4
    //   169: aload_1
    //   170: invokespecial <init> : ([C)V
    //   173: aload_0
    //   174: aload #4
    //   176: putfield left : Ljava/lang/CharSequence;
    //   179: aload_0
    //   180: ldc ''
    //   182: putfield right : Ljava/lang/CharSequence;
    //   185: aload_0
    //   186: iconst_1
    //   187: putfield isFlat : Z
    //   190: aload_0
    //   191: getfield left : Ljava/lang/CharSequence;
    //   194: checkcast java/lang/String
    //   197: astore #4
    //   199: aload_0
    //   200: monitorexit
    //   201: aload #4
    //   203: areturn
    //   204: astore #4
    //   206: aload_0
    //   207: monitorexit
    //   208: aload #4
    //   210: athrow
    // Exception table:
    //   from	to	target	type
    //   2	43	204	finally
    //   47	77	204	finally
    //   80	96	204	finally
    //   99	134	204	finally
    //   140	149	204	finally
    //   162	190	204	finally
    //   190	199	204	finally
  }
  
  private Object writeReplace() {
    return toString();
  }
  
  public char charAt(int paramInt) {
    String str;
    if (this.isFlat) {
      str = (String)this.left;
    } else {
      str = flatten();
    } 
    return str.charAt(paramInt);
  }
  
  public int length() {
    return this.length;
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2) {
    String str;
    if (this.isFlat) {
      str = (String)this.left;
    } else {
      str = flatten();
    } 
    return str.substring(paramInt1, paramInt2);
  }
  
  public String toString() {
    String str;
    if (this.isFlat) {
      str = (String)this.left;
    } else {
      str = flatten();
    } 
    return str;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ConsString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */