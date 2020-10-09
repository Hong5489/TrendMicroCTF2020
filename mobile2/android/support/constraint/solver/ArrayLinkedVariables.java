package android.support.constraint.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  private static final int NONE = -1;
  
  private int ROW_SIZE = 8;
  
  private SolverVariable candidate = null;
  
  int currentSize = 0;
  
  private int[] mArrayIndices = new int[8];
  
  private int[] mArrayNextIndices = new int[8];
  
  private float[] mArrayValues = new float[8];
  
  private final Cache mCache;
  
  private boolean mDidFillOnce = false;
  
  private int mHead = -1;
  
  private int mLast = -1;
  
  private final ArrayRow mRow;
  
  ArrayLinkedVariables(ArrayRow paramArrayRow, Cache paramCache) {
    this.mRow = paramArrayRow;
    this.mCache = paramCache;
  }
  
  private boolean isNew(SolverVariable paramSolverVariable, LinearSystem paramLinearSystem) {
    int i = paramSolverVariable.usageInRowCount;
    boolean bool = true;
    if (i > 1)
      bool = false; 
    return bool;
  }
  
  final void add(SolverVariable paramSolverVariable, float paramFloat, boolean paramBoolean) {
    if (paramFloat == 0.0F)
      return; 
    if (this.mHead == -1) {
      this.mHead = 0;
      this.mArrayValues[0] = paramFloat;
      this.mArrayIndices[0] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        int m = this.mLast + 1;
        this.mLast = m;
        arrayOfInt1 = this.mArrayIndices;
        if (m >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int j = -1;
    int k;
    for (k = 0; i != -1 && k < this.currentSize; k++) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        float[] arrayOfFloat = this.mArrayValues;
        arrayOfFloat[i] = arrayOfFloat[i] + paramFloat;
        if (arrayOfFloat[i] == 0.0F) {
          if (i == this.mHead) {
            this.mHead = this.mArrayNextIndices[i];
          } else {
            int[] arrayOfInt = this.mArrayNextIndices;
            arrayOfInt[j] = arrayOfInt[i];
          } 
          if (paramBoolean)
            arrayOfInt1.removeFromRow(this.mRow); 
          if (this.mDidFillOnce)
            this.mLast = i; 
          ((SolverVariable)arrayOfInt1).usageInRowCount--;
          this.currentSize--;
        } 
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        j = i; 
      i = this.mArrayNextIndices[i];
    } 
    k = this.mLast;
    i = k + 1;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[k] == -1) {
        i = this.mLast;
      } else {
        i = arrayOfInt.length;
      } 
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    k = i;
    if (i >= arrayOfInt2.length) {
      k = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          k = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              k = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = k;
    if (k >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      k = this.ROW_SIZE * 2;
      this.ROW_SIZE = k;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, k);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (j != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[j];
      arrayOfInt2[j] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  SolverVariable chooseSubject(LinearSystem paramLinearSystem) {
    SolverVariable solverVariable1 = null;
    SolverVariable solverVariable2 = null;
    float f1 = 0.0F;
    float f2 = 0.0F;
    boolean bool1 = false;
    boolean bool2 = false;
    int i = this.mHead;
    byte b = 0;
    while (i != -1 && b < this.currentSize) {
      float f4;
      float f3 = this.mArrayValues[i];
      SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (f3 < 0.0F) {
        f4 = f3;
        if (f3 > -0.001F) {
          this.mArrayValues[i] = 0.0F;
          f4 = 0.0F;
          solverVariable3.removeFromRow(this.mRow);
        } 
      } else {
        f4 = f3;
        if (f3 < 0.001F) {
          this.mArrayValues[i] = 0.0F;
          f4 = 0.0F;
          solverVariable3.removeFromRow(this.mRow);
        } 
      } 
      SolverVariable solverVariable4 = solverVariable1;
      SolverVariable solverVariable5 = solverVariable2;
      f3 = f1;
      float f5 = f2;
      boolean bool3 = bool1;
      boolean bool4 = bool2;
      if (f4 != 0.0F)
        if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
          if (solverVariable2 == null) {
            solverVariable5 = solverVariable3;
            bool3 = isNew(solverVariable3, paramLinearSystem);
            solverVariable4 = solverVariable1;
            f3 = f4;
            f5 = f2;
            bool4 = bool2;
          } else if (f1 > f4) {
            solverVariable5 = solverVariable3;
            bool3 = isNew(solverVariable3, paramLinearSystem);
            solverVariable4 = solverVariable1;
            f3 = f4;
            f5 = f2;
            bool4 = bool2;
          } else {
            solverVariable4 = solverVariable1;
            solverVariable5 = solverVariable2;
            f3 = f1;
            f5 = f2;
            bool3 = bool1;
            bool4 = bool2;
            if (!bool1) {
              solverVariable4 = solverVariable1;
              solverVariable5 = solverVariable2;
              f3 = f1;
              f5 = f2;
              bool3 = bool1;
              bool4 = bool2;
              if (isNew(solverVariable3, paramLinearSystem)) {
                bool3 = true;
                solverVariable4 = solverVariable1;
                solverVariable5 = solverVariable3;
                f3 = f4;
                f5 = f2;
                bool4 = bool2;
              } 
            } 
          } 
        } else {
          solverVariable4 = solverVariable1;
          solverVariable5 = solverVariable2;
          f3 = f1;
          f5 = f2;
          bool3 = bool1;
          bool4 = bool2;
          if (solverVariable2 == null) {
            solverVariable4 = solverVariable1;
            solverVariable5 = solverVariable2;
            f3 = f1;
            f5 = f2;
            bool3 = bool1;
            bool4 = bool2;
            if (f4 < 0.0F)
              if (solverVariable1 == null) {
                solverVariable4 = solverVariable3;
                bool4 = isNew(solverVariable3, paramLinearSystem);
                solverVariable5 = solverVariable2;
                f3 = f1;
                f5 = f4;
                bool3 = bool1;
              } else if (f2 > f4) {
                solverVariable4 = solverVariable3;
                bool4 = isNew(solverVariable3, paramLinearSystem);
                solverVariable5 = solverVariable2;
                f3 = f1;
                f5 = f4;
                bool3 = bool1;
              } else {
                solverVariable4 = solverVariable1;
                solverVariable5 = solverVariable2;
                f3 = f1;
                f5 = f2;
                bool3 = bool1;
                bool4 = bool2;
                if (!bool2) {
                  solverVariable4 = solverVariable1;
                  solverVariable5 = solverVariable2;
                  f3 = f1;
                  f5 = f2;
                  bool3 = bool1;
                  bool4 = bool2;
                  if (isNew(solverVariable3, paramLinearSystem)) {
                    bool4 = true;
                    bool3 = bool1;
                    f5 = f4;
                    f3 = f1;
                    solverVariable5 = solverVariable2;
                    solverVariable4 = solverVariable3;
                  } 
                } 
              }  
          } 
        }  
      i = this.mArrayNextIndices[i];
      b++;
      solverVariable1 = solverVariable4;
      solverVariable2 = solverVariable5;
      f1 = f3;
      f2 = f5;
      bool1 = bool3;
      bool2 = bool4;
    } 
    return (solverVariable2 != null) ? solverVariable2 : solverVariable1;
  }
  
  public final void clear() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable != null)
        solverVariable.removeFromRow(this.mRow); 
      i = this.mArrayNextIndices[i];
    } 
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.currentSize = 0;
  }
  
  final boolean containsKey(SolverVariable paramSolverVariable) {
    if (this.mHead == -1)
      return false; 
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public void display() {
    int i = this.currentSize;
    System.out.print("{ ");
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = getVariable(b);
      if (solverVariable != null) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(solverVariable);
        stringBuilder.append(" = ");
        stringBuilder.append(getVariableValue(b));
        stringBuilder.append(" ");
        printStream.print(stringBuilder.toString());
      } 
    } 
    System.out.println(" }");
  }
  
  void divideByAmount(float paramFloat) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] / paramFloat;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final float get(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  SolverVariable getPivotCandidate() {
    // Byte code:
    //   0: aload_0
    //   1: getfield candidate : Landroid/support/constraint/solver/SolverVariable;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull -> 105
    //   9: aload_0
    //   10: getfield mHead : I
    //   13: istore_2
    //   14: iconst_0
    //   15: istore_3
    //   16: aconst_null
    //   17: astore #4
    //   19: iload_2
    //   20: iconst_m1
    //   21: if_icmpeq -> 102
    //   24: iload_3
    //   25: aload_0
    //   26: getfield currentSize : I
    //   29: if_icmpge -> 102
    //   32: aload #4
    //   34: astore_1
    //   35: aload_0
    //   36: getfield mArrayValues : [F
    //   39: iload_2
    //   40: faload
    //   41: fconst_0
    //   42: fcmpg
    //   43: ifge -> 86
    //   46: aload_0
    //   47: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   50: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   53: aload_0
    //   54: getfield mArrayIndices : [I
    //   57: iload_2
    //   58: iaload
    //   59: aaload
    //   60: astore #5
    //   62: aload #4
    //   64: ifnull -> 83
    //   67: aload #4
    //   69: astore_1
    //   70: aload #4
    //   72: getfield strength : I
    //   75: aload #5
    //   77: getfield strength : I
    //   80: if_icmpge -> 86
    //   83: aload #5
    //   85: astore_1
    //   86: aload_0
    //   87: getfield mArrayNextIndices : [I
    //   90: iload_2
    //   91: iaload
    //   92: istore_2
    //   93: iinc #3, 1
    //   96: aload_1
    //   97: astore #4
    //   99: goto -> 19
    //   102: aload #4
    //   104: areturn
    //   105: aload_1
    //   106: areturn
  }
  
  SolverVariable getPivotCandidate(boolean[] paramArrayOfboolean, SolverVariable paramSolverVariable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: aconst_null
    //   9: astore #5
    //   11: fconst_0
    //   12: fstore #6
    //   14: iload_3
    //   15: iconst_m1
    //   16: if_icmpeq -> 182
    //   19: iload #4
    //   21: aload_0
    //   22: getfield currentSize : I
    //   25: if_icmpge -> 182
    //   28: aload #5
    //   30: astore #7
    //   32: fload #6
    //   34: fstore #8
    //   36: aload_0
    //   37: getfield mArrayValues : [F
    //   40: iload_3
    //   41: faload
    //   42: fconst_0
    //   43: fcmpg
    //   44: ifge -> 161
    //   47: aload_0
    //   48: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   51: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   54: aload_0
    //   55: getfield mArrayIndices : [I
    //   58: iload_3
    //   59: iaload
    //   60: aaload
    //   61: astore #9
    //   63: aload_1
    //   64: ifnull -> 85
    //   67: aload #5
    //   69: astore #7
    //   71: fload #6
    //   73: fstore #8
    //   75: aload_1
    //   76: aload #9
    //   78: getfield id : I
    //   81: baload
    //   82: ifne -> 161
    //   85: aload #5
    //   87: astore #7
    //   89: fload #6
    //   91: fstore #8
    //   93: aload #9
    //   95: aload_2
    //   96: if_acmpeq -> 161
    //   99: aload #9
    //   101: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   104: getstatic android/support/constraint/solver/SolverVariable$Type.SLACK : Landroid/support/constraint/solver/SolverVariable$Type;
    //   107: if_acmpeq -> 129
    //   110: aload #5
    //   112: astore #7
    //   114: fload #6
    //   116: fstore #8
    //   118: aload #9
    //   120: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   123: getstatic android/support/constraint/solver/SolverVariable$Type.ERROR : Landroid/support/constraint/solver/SolverVariable$Type;
    //   126: if_acmpne -> 161
    //   129: aload_0
    //   130: getfield mArrayValues : [F
    //   133: iload_3
    //   134: faload
    //   135: fstore #10
    //   137: aload #5
    //   139: astore #7
    //   141: fload #6
    //   143: fstore #8
    //   145: fload #10
    //   147: fload #6
    //   149: fcmpg
    //   150: ifge -> 161
    //   153: fload #10
    //   155: fstore #8
    //   157: aload #9
    //   159: astore #7
    //   161: aload_0
    //   162: getfield mArrayNextIndices : [I
    //   165: iload_3
    //   166: iaload
    //   167: istore_3
    //   168: iinc #4, 1
    //   171: aload #7
    //   173: astore #5
    //   175: fload #8
    //   177: fstore #6
    //   179: goto -> 14
    //   182: aload #5
    //   184: areturn
  }
  
  final SolverVariable getVariable(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mCache.mIndexedVariables[this.mArrayIndices[i]]; 
      i = this.mArrayNextIndices[i];
    } 
    return null;
  }
  
  final float getVariableValue(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  boolean hasAtLeastOnePositiveVariable() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayValues[i] > 0.0F)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  void invert() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] * -1.0F;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final void put(SolverVariable paramSolverVariable, float paramFloat) {
    if (paramFloat == 0.0F) {
      remove(paramSolverVariable, true);
      return;
    } 
    if (this.mHead == -1) {
      this.mHead = 0;
      this.mArrayValues[0] = paramFloat;
      this.mArrayIndices[0] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        int m = this.mLast + 1;
        this.mLast = m;
        arrayOfInt1 = this.mArrayIndices;
        if (m >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int j = -1;
    int k;
    for (k = 0; i != -1 && k < this.currentSize; k++) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        this.mArrayValues[i] = paramFloat;
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        j = i; 
      i = this.mArrayNextIndices[i];
    } 
    k = this.mLast;
    i = k + 1;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[k] == -1) {
        i = this.mLast;
      } else {
        i = arrayOfInt.length;
      } 
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    k = i;
    if (i >= arrayOfInt2.length) {
      k = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          k = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              k = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = k;
    if (k >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      k = this.ROW_SIZE * 2;
      this.ROW_SIZE = k;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, k);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (j != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[j];
      arrayOfInt2[j] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    if (this.currentSize >= this.mArrayIndices.length)
      this.mDidFillOnce = true; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  public final float remove(SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (this.candidate == paramSolverVariable)
      this.candidate = null; 
    if (this.mHead == -1)
      return 0.0F; 
    int i = this.mHead;
    int j = -1;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        if (i == this.mHead) {
          this.mHead = this.mArrayNextIndices[i];
        } else {
          int[] arrayOfInt = this.mArrayNextIndices;
          arrayOfInt[j] = arrayOfInt[i];
        } 
        if (paramBoolean)
          paramSolverVariable.removeFromRow(this.mRow); 
        paramSolverVariable.usageInRowCount--;
        this.currentSize--;
        this.mArrayIndices[i] = -1;
        if (this.mDidFillOnce)
          this.mLast = i; 
        return this.mArrayValues[i];
      } 
      j = i;
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  int sizeInBytes() {
    return 0 + this.mArrayIndices.length * 4 * 3 + 36;
  }
  
  public String toString() {
    String str = "";
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(" -> ");
      str = stringBuilder2.toString();
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(this.mArrayValues[i]);
      stringBuilder2.append(" : ");
      String str1 = stringBuilder2.toString();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(this.mCache.mIndexedVariables[this.mArrayIndices[i]]);
      str = stringBuilder1.toString();
      i = this.mArrayNextIndices[i];
    } 
    return str;
  }
  
  final void updateFromRow(ArrayRow paramArrayRow1, ArrayRow paramArrayRow2, boolean paramBoolean) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramArrayRow2.variable.id) {
        float f = this.mArrayValues[i];
        remove(paramArrayRow2.variable, paramBoolean);
        ArrayLinkedVariables arrayLinkedVariables = paramArrayRow2.variables;
        i = arrayLinkedVariables.mHead;
        for (b = 0; i != -1 && b < arrayLinkedVariables.currentSize; b++) {
          add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[i]], arrayLinkedVariables.mArrayValues[i] * f, paramBoolean);
          i = arrayLinkedVariables.mArrayNextIndices[i];
        } 
        paramArrayRow1.constantValue += paramArrayRow2.constantValue * f;
        if (paramBoolean)
          paramArrayRow2.variable.removeFromRow(paramArrayRow1); 
        i = this.mHead;
        b = 0;
        continue;
      } 
      i = this.mArrayNextIndices[i];
    } 
  }
  
  void updateFromSystem(ArrayRow paramArrayRow, ArrayRow[] paramArrayOfArrayRow) {
    int i = this.mHead;
    for (int j = 0; i != -1 && j < this.currentSize; j++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable.definitionId != -1) {
        float f = this.mArrayValues[i];
        remove(solverVariable, true);
        ArrayRow arrayRow = paramArrayOfArrayRow[solverVariable.definitionId];
        if (!arrayRow.isSimpleDefinition) {
          ArrayLinkedVariables arrayLinkedVariables = arrayRow.variables;
          j = arrayLinkedVariables.mHead;
          for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
            add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, true);
            j = arrayLinkedVariables.mArrayNextIndices[j];
          } 
        } 
        paramArrayRow.constantValue += arrayRow.constantValue * f;
        arrayRow.variable.removeFromRow(paramArrayRow);
        i = this.mHead;
        j = 0;
        continue;
      } 
      i = this.mArrayNextIndices[i];
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/ArrayLinkedVariables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */