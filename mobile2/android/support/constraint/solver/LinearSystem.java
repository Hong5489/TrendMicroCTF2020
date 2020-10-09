package android.support.constraint.solver;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
  private static final boolean DEBUG = false;
  
  public static final boolean FULL_DEBUG = false;
  
  private static int POOL_SIZE = 1000;
  
  public static Metrics sMetrics;
  
  private int TABLE_SIZE = 32;
  
  public boolean graphOptimizer = false;
  
  private boolean[] mAlreadyTestedCandidates = new boolean[32];
  
  final Cache mCache;
  
  private Row mGoal;
  
  private int mMaxColumns = 32;
  
  private int mMaxRows = 32;
  
  int mNumColumns = 1;
  
  int mNumRows = 0;
  
  private SolverVariable[] mPoolVariables = new SolverVariable[POOL_SIZE];
  
  private int mPoolVariablesCount = 0;
  
  ArrayRow[] mRows = null;
  
  private final Row mTempGoal;
  
  private HashMap<String, SolverVariable> mVariables = null;
  
  int mVariablesID = 0;
  
  private ArrayRow[] tempClientsCopy = new ArrayRow[32];
  
  public LinearSystem() {
    this.mRows = new ArrayRow[32];
    releaseRows();
    this.mCache = new Cache();
    this.mGoal = new GoalRow(this.mCache);
    this.mTempGoal = new ArrayRow(this.mCache);
  }
  
  private SolverVariable acquireSolverVariable(SolverVariable.Type paramType, String paramString) {
    SolverVariable solverVariable1;
    SolverVariable solverVariable2 = this.mCache.solverVariablePool.acquire();
    if (solverVariable2 == null) {
      solverVariable2 = new SolverVariable(paramType, paramString);
      solverVariable2.setType(paramType, paramString);
      solverVariable1 = solverVariable2;
    } else {
      solverVariable2.reset();
      solverVariable2.setType((SolverVariable.Type)solverVariable1, paramString);
      solverVariable1 = solverVariable2;
    } 
    int i = this.mPoolVariablesCount;
    int j = POOL_SIZE;
    if (i >= j) {
      j *= 2;
      POOL_SIZE = j;
      this.mPoolVariables = Arrays.<SolverVariable>copyOf(this.mPoolVariables, j);
    } 
    SolverVariable[] arrayOfSolverVariable = this.mPoolVariables;
    j = this.mPoolVariablesCount;
    this.mPoolVariablesCount = j + 1;
    arrayOfSolverVariable[j] = solverVariable1;
    return solverVariable1;
  }
  
  private void addError(ArrayRow paramArrayRow) {
    paramArrayRow.addError(this, 0);
  }
  
  private final void addRow(ArrayRow paramArrayRow) {
    if (this.mRows[this.mNumRows] != null)
      this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]); 
    this.mRows[this.mNumRows] = paramArrayRow;
    paramArrayRow.variable.definitionId = this.mNumRows;
    this.mNumRows++;
    paramArrayRow.variable.updateReferencesWithNewDefinition(paramArrayRow);
  }
  
  private void addSingleError(ArrayRow paramArrayRow, int paramInt) {
    addSingleError(paramArrayRow, paramInt, 0);
  }
  
  private void computeValues() {
    for (byte b = 0; b < this.mNumRows; b++) {
      ArrayRow arrayRow = this.mRows[b];
      arrayRow.variable.computedValue = arrayRow.constantValue;
    } 
  }
  
  public static ArrayRow createRowCentering(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2, boolean paramBoolean) {
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowCentering(paramSolverVariable1, paramSolverVariable2, paramInt1, paramFloat, paramSolverVariable3, paramSolverVariable4, paramInt2);
    if (paramBoolean)
      arrayRow.addError(paramLinearSystem, 4); 
    return arrayRow;
  }
  
  public static ArrayRow createRowDimensionPercent(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, float paramFloat, boolean paramBoolean) {
    ArrayRow arrayRow = paramLinearSystem.createRow();
    if (paramBoolean)
      paramLinearSystem.addError(arrayRow); 
    return arrayRow.createRowDimensionPercent(paramSolverVariable1, paramSolverVariable2, paramSolverVariable3, paramFloat);
  }
  
  public static ArrayRow createRowEquals(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowEquals(paramSolverVariable1, paramSolverVariable2, paramInt);
    if (paramBoolean)
      paramLinearSystem.addSingleError(arrayRow, 1); 
    return arrayRow;
  }
  
  public static ArrayRow createRowGreaterThan(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    SolverVariable solverVariable = paramLinearSystem.createSlackVariable();
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt);
    if (paramBoolean)
      paramLinearSystem.addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable))); 
    return arrayRow;
  }
  
  public static ArrayRow createRowLowerThan(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    SolverVariable solverVariable = paramLinearSystem.createSlackVariable();
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt);
    if (paramBoolean)
      paramLinearSystem.addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable))); 
    return arrayRow;
  }
  
  private SolverVariable createVariable(String paramString, SolverVariable.Type paramType) {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.variables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(paramType, null);
    solverVariable.setName(paramString);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    if (this.mVariables == null)
      this.mVariables = new HashMap<>(); 
    this.mVariables.put(paramString, solverVariable);
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  private void displayRows() {
    displaySolverVariables();
    String str = "";
    for (byte b = 0; b < this.mNumRows; b++) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(this.mRows[b]);
      String str1 = stringBuilder2.toString();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append("\n");
      str = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(this.mGoal);
    stringBuilder.append("\n");
    str = stringBuilder.toString();
    System.out.println(str);
  }
  
  private void displaySolverVariables() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Display Rows (");
    stringBuilder.append(this.mNumRows);
    stringBuilder.append("x");
    stringBuilder.append(this.mNumColumns);
    stringBuilder.append(")\n");
    String str = stringBuilder.toString();
    System.out.println(str);
  }
  
  private int enforceBFS(Row paramRow) throws Exception {
    int k;
    int i = 0;
    byte b = 0;
    int j = 0;
    while (true) {
      k = b;
      if (j < this.mNumRows) {
        if ((this.mRows[j]).variable.mType != SolverVariable.Type.UNRESTRICTED && (this.mRows[j]).constantValue < 0.0F) {
          k = 1;
          break;
        } 
        j++;
        continue;
      } 
      break;
    } 
    j = i;
    if (k) {
      b = 0;
      k = 0;
      while (true) {
        j = k;
        if (!b) {
          Metrics metrics = sMetrics;
          if (metrics != null)
            metrics.bfs++; 
          int m = k + 1;
          float f = Float.MAX_VALUE;
          j = 0;
          i = -1;
          k = -1;
          byte b1 = 0;
          while (b1 < this.mNumRows) {
            float f1;
            int n;
            int i1;
            int i2;
            ArrayRow arrayRow = this.mRows[b1];
            if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
              f1 = f;
              n = j;
              i1 = i;
              i2 = k;
            } else if (arrayRow.isSimpleDefinition) {
              f1 = f;
              n = j;
              i1 = i;
              i2 = k;
            } else {
              f1 = f;
              n = j;
              i1 = i;
              i2 = k;
              if (arrayRow.constantValue < 0.0F) {
                Object object;
                byte b2 = 1;
                while (true) {
                  f1 = f;
                  n = j;
                  Object object1 = object;
                  i2 = k;
                  if (b2 < this.mNumColumns) {
                    SolverVariable solverVariable = this.mCache.mIndexedVariables[b2];
                    float f2 = arrayRow.variables.get(solverVariable);
                    if (f2 <= 0.0F) {
                      f1 = f;
                      i2 = j;
                      Object object2 = object;
                      int i3 = k;
                      continue;
                    } 
                    i2 = 0;
                    n = k;
                    k = i2;
                    while (true) {
                      f1 = f;
                      i2 = j;
                      Object object2 = object;
                      i1 = n;
                      k++;
                      j = i2;
                    } 
                    continue;
                  } 
                  break;
                  b2++;
                  f = f1;
                  j = i2;
                  object = SYNTHETIC_LOCAL_VARIABLE_16;
                  k = i1;
                } 
              } 
            } 
            b1++;
            f = f1;
            j = n;
            i = i1;
            k = i2;
          } 
          if (i != -1) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.definitionId = -1;
            Metrics metrics1 = sMetrics;
            if (metrics1 != null)
              metrics1.pivots++; 
            arrayRow.pivot(this.mCache.mIndexedVariables[k]);
            arrayRow.variable.definitionId = i;
            arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
          } else {
            b = 1;
          } 
          if (m > this.mNumColumns / 2)
            b = 1; 
          k = m;
          continue;
        } 
        break;
      } 
    } 
    return j;
  }
  
  private String getDisplaySize(int paramInt) {
    int i = paramInt * 4 / 1024 / 1024;
    if (i > 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(i);
      stringBuilder1.append(" Mb");
      return stringBuilder1.toString();
    } 
    i = paramInt * 4 / 1024;
    if (i > 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(i);
      stringBuilder1.append(" Kb");
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramInt * 4);
    stringBuilder.append(" bytes");
    return stringBuilder.toString();
  }
  
  private String getDisplayStrength(int paramInt) {
    return (paramInt == 1) ? "LOW" : ((paramInt == 2) ? "MEDIUM" : ((paramInt == 3) ? "HIGH" : ((paramInt == 4) ? "HIGHEST" : ((paramInt == 5) ? "EQUALITY" : ((paramInt == 6) ? "FIXED" : "NONE")))));
  }
  
  public static Metrics getMetrics() {
    return sMetrics;
  }
  
  private void increaseTableSize() {
    int i = this.TABLE_SIZE * 2;
    this.TABLE_SIZE = i;
    this.mRows = Arrays.<ArrayRow>copyOf(this.mRows, i);
    Cache cache = this.mCache;
    cache.mIndexedVariables = Arrays.<SolverVariable>copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
    i = this.TABLE_SIZE;
    this.mAlreadyTestedCandidates = new boolean[i];
    this.mMaxColumns = i;
    this.mMaxRows = i;
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.tableSizeIncrease++;
      metrics = sMetrics;
      metrics.maxTableSize = Math.max(metrics.maxTableSize, this.TABLE_SIZE);
      metrics = sMetrics;
      metrics.lastTableSize = metrics.maxTableSize;
    } 
  }
  
  private final int optimize(Row paramRow, boolean paramBoolean) {
    int k;
    int m;
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.optimize++; 
    int i = 0;
    int j = 0;
    byte b = 0;
    while (true) {
      k = i;
      m = j;
      if (b < this.mNumColumns) {
        this.mAlreadyTestedCandidates[b] = false;
        b++;
        continue;
      } 
      break;
    } 
    while (!k) {
      metrics = sMetrics;
      if (metrics != null)
        metrics.iterations++; 
      i = m + 1;
      if (i >= this.mNumColumns * 2)
        return i; 
      if (paramRow.getKey() != null)
        this.mAlreadyTestedCandidates[(paramRow.getKey()).id] = true; 
      SolverVariable solverVariable = paramRow.getPivotCandidate(this, this.mAlreadyTestedCandidates);
      if (solverVariable != null) {
        if (this.mAlreadyTestedCandidates[solverVariable.id])
          return i; 
        this.mAlreadyTestedCandidates[solverVariable.id] = true;
      } 
      if (solverVariable != null) {
        float f = Float.MAX_VALUE;
        m = -1;
        b = 0;
        while (b < this.mNumRows) {
          float f1;
          ArrayRow arrayRow = this.mRows[b];
          if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
            f1 = f;
            j = m;
          } else if (arrayRow.isSimpleDefinition) {
            f1 = f;
            j = m;
          } else {
            f1 = f;
            j = m;
            if (arrayRow.hasVariable(solverVariable)) {
              float f2 = arrayRow.variables.get(solverVariable);
              f1 = f;
              j = m;
              if (f2 < 0.0F) {
                f2 = -arrayRow.constantValue / f2;
                f1 = f;
                j = m;
                if (f2 < f) {
                  f1 = f2;
                  j = b;
                } 
              } 
            } 
          } 
          b++;
          f = f1;
          m = j;
        } 
        if (m > -1) {
          ArrayRow arrayRow = this.mRows[m];
          arrayRow.variable.definitionId = -1;
          Metrics metrics1 = sMetrics;
          if (metrics1 != null)
            metrics1.pivots++; 
          arrayRow.pivot(solverVariable);
          arrayRow.variable.definitionId = m;
          arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
        } else {
          k = 1;
        } 
      } else {
        k = 1;
      } 
      m = i;
    } 
    return m;
  }
  
  private void releaseRows() {
    byte b = 0;
    while (true) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      if (b < arrayOfArrayRow.length) {
        ArrayRow arrayRow = arrayOfArrayRow[b];
        if (arrayRow != null)
          this.mCache.arrayRowPool.release(arrayRow); 
        this.mRows[b] = null;
        b++;
        continue;
      } 
      break;
    } 
  }
  
  private final void updateRowFromVariables(ArrayRow paramArrayRow) {
    if (this.mNumRows > 0) {
      paramArrayRow.variables.updateFromSystem(paramArrayRow, this.mRows);
      if (paramArrayRow.variables.currentSize == 0)
        paramArrayRow.isSimpleDefinition = true; 
    } 
  }
  
  public void addCenterPoint(ConstraintWidget paramConstraintWidget1, ConstraintWidget paramConstraintWidget2, float paramFloat, int paramInt) {
    SolverVariable solverVariable2 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT));
    SolverVariable solverVariable3 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.TOP));
    SolverVariable solverVariable4 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT));
    SolverVariable solverVariable5 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.BOTTOM));
    SolverVariable solverVariable6 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
    SolverVariable solverVariable7 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
    SolverVariable solverVariable1 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
    SolverVariable solverVariable8 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
    ArrayRow arrayRow = createRow();
    arrayRow.createRowWithAngle(solverVariable3, solverVariable5, solverVariable7, solverVariable8, (float)(Math.sin(paramFloat) * paramInt));
    addConstraint(arrayRow);
    arrayRow = createRow();
    arrayRow.createRowWithAngle(solverVariable2, solverVariable4, solverVariable6, solverVariable1, (float)(Math.cos(paramFloat) * paramInt));
    addConstraint(arrayRow);
  }
  
  public void addCentering(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2, int paramInt3) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowCentering(paramSolverVariable1, paramSolverVariable2, paramInt1, paramFloat, paramSolverVariable3, paramSolverVariable4, paramInt2);
    if (paramInt3 != 6)
      arrayRow.addError(this, paramInt3); 
    addConstraint(arrayRow);
  }
  
  public void addConstraint(ArrayRow paramArrayRow) {
    if (paramArrayRow == null)
      return; 
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.constraints++;
      if (paramArrayRow.isSimpleDefinition) {
        metrics = sMetrics;
        metrics.simpleconstraints++;
      } 
    } 
    if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    boolean bool1 = false;
    boolean bool2 = false;
    if (!paramArrayRow.isSimpleDefinition) {
      updateRowFromVariables(paramArrayRow);
      if (paramArrayRow.isEmpty())
        return; 
      paramArrayRow.ensurePositiveConstant();
      bool1 = bool2;
      if (paramArrayRow.chooseSubject(this)) {
        SolverVariable solverVariable = createExtraVariable();
        paramArrayRow.variable = solverVariable;
        addRow(paramArrayRow);
        bool2 = true;
        this.mTempGoal.initFromRow(paramArrayRow);
        optimize(this.mTempGoal, true);
        bool1 = bool2;
        if (solverVariable.definitionId == -1) {
          if (paramArrayRow.variable == solverVariable) {
            solverVariable = paramArrayRow.pickPivot(solverVariable);
            if (solverVariable != null) {
              Metrics metrics1 = sMetrics;
              if (metrics1 != null)
                metrics1.pivots++; 
              paramArrayRow.pivot(solverVariable);
            } 
          } 
          if (!paramArrayRow.isSimpleDefinition)
            paramArrayRow.variable.updateReferencesWithNewDefinition(paramArrayRow); 
          this.mNumRows--;
          bool1 = bool2;
        } 
      } 
      if (!paramArrayRow.hasKeyVariable())
        return; 
    } 
    if (!bool1)
      addRow(paramArrayRow); 
  }
  
  public ArrayRow addEquality(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowEquals(paramSolverVariable1, paramSolverVariable2, paramInt1);
    if (paramInt2 != 6)
      arrayRow.addError(this, paramInt2); 
    addConstraint(arrayRow);
    return arrayRow;
  }
  
  public void addEquality(SolverVariable paramSolverVariable, int paramInt) {
    int i = paramSolverVariable.definitionId;
    if (paramSolverVariable.definitionId != -1) {
      ArrayRow arrayRow = this.mRows[i];
      if (arrayRow.isSimpleDefinition) {
        arrayRow.constantValue = paramInt;
      } else if (arrayRow.variables.currentSize == 0) {
        arrayRow.isSimpleDefinition = true;
        arrayRow.constantValue = paramInt;
      } else {
        arrayRow = createRow();
        arrayRow.createRowEquals(paramSolverVariable, paramInt);
        addConstraint(arrayRow);
      } 
    } else {
      ArrayRow arrayRow = createRow();
      arrayRow.createRowDefinition(paramSolverVariable, paramInt);
      addConstraint(arrayRow);
    } 
  }
  
  public void addEquality(SolverVariable paramSolverVariable, int paramInt1, int paramInt2) {
    int i = paramSolverVariable.definitionId;
    if (paramSolverVariable.definitionId != -1) {
      ArrayRow arrayRow = this.mRows[i];
      if (arrayRow.isSimpleDefinition) {
        arrayRow.constantValue = paramInt1;
      } else {
        arrayRow = createRow();
        arrayRow.createRowEquals(paramSolverVariable, paramInt1);
        arrayRow.addError(this, paramInt2);
        addConstraint(arrayRow);
      } 
    } else {
      ArrayRow arrayRow = createRow();
      arrayRow.createRowDefinition(paramSolverVariable, paramInt1);
      arrayRow.addError(this, paramInt2);
      addConstraint(arrayRow);
    } 
  }
  
  public void addGreaterBarrier(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, boolean paramBoolean) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, 0);
    if (paramBoolean)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), 1); 
    addConstraint(arrayRow);
  }
  
  public void addGreaterThan(SolverVariable paramSolverVariable, int paramInt) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable, paramInt, solverVariable);
    addConstraint(arrayRow);
  }
  
  public void addGreaterThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt1);
    if (paramInt2 != 6)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), paramInt2); 
    addConstraint(arrayRow);
  }
  
  public void addLowerBarrier(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, boolean paramBoolean) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, 0);
    if (paramBoolean)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), 1); 
    addConstraint(arrayRow);
  }
  
  public void addLowerThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt1);
    if (paramInt2 != 6)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), paramInt2); 
    addConstraint(arrayRow);
  }
  
  public void addRatio(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat, int paramInt) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowDimensionRatio(paramSolverVariable1, paramSolverVariable2, paramSolverVariable3, paramSolverVariable4, paramFloat);
    if (paramInt != 6)
      arrayRow.addError(this, paramInt); 
    addConstraint(arrayRow);
  }
  
  void addSingleError(ArrayRow paramArrayRow, int paramInt1, int paramInt2) {
    paramArrayRow.addSingleError(createErrorVariable(paramInt2, null), paramInt1);
  }
  
  public SolverVariable createErrorVariable(int paramInt, String paramString) {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.errors++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.ERROR, paramString);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    solverVariable.strength = paramInt;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    this.mGoal.addError(solverVariable);
    return solverVariable;
  }
  
  public SolverVariable createExtraVariable() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.extravariables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public SolverVariable createObjectVariable(Object paramObject) {
    SolverVariable solverVariable;
    if (paramObject == null)
      return null; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    null = null;
    if (paramObject instanceof ConstraintAnchor) {
      null = ((ConstraintAnchor)paramObject).getSolverVariable();
      solverVariable = null;
      if (null == null) {
        ((ConstraintAnchor)paramObject).resetSolverVariable(this.mCache);
        solverVariable = ((ConstraintAnchor)paramObject).getSolverVariable();
      } 
      if (solverVariable.id != -1 && solverVariable.id <= this.mVariablesID) {
        null = solverVariable;
        if (this.mCache.mIndexedVariables[solverVariable.id] == null) {
          if (solverVariable.id != -1)
            solverVariable.reset(); 
          int j = this.mVariablesID + 1;
          this.mVariablesID = j;
          this.mNumColumns++;
          solverVariable.id = j;
          solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
          this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
          return solverVariable;
        } 
        return null;
      } 
    } else {
      return null;
    } 
    if (solverVariable.id != -1)
      solverVariable.reset(); 
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public ArrayRow createRow() {
    ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
    if (arrayRow == null) {
      arrayRow = new ArrayRow(this.mCache);
    } else {
      arrayRow.reset();
    } 
    SolverVariable.increaseErrorId();
    return arrayRow;
  }
  
  public SolverVariable createSlackVariable() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.slackvariables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  void displayReadableRows() {
    displaySolverVariables();
    String str1 = " #  ";
    for (byte b = 0; b < this.mNumRows; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(this.mRows[b].toReadableString());
      str1 = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("\n #  ");
      str1 = stringBuilder.toString();
    } 
    String str2 = str1;
    if (this.mGoal != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(this.mGoal);
      stringBuilder.append("\n");
      str2 = stringBuilder.toString();
    } 
    System.out.println(str2);
  }
  
  void displaySystemInformations() {
    int i = 0;
    int j = 0;
    while (j < this.TABLE_SIZE) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int m = i;
      if (arrayOfArrayRow[j] != null)
        m = i + arrayOfArrayRow[j].sizeInBytes(); 
      j++;
      i = m;
    } 
    j = 0;
    int k = 0;
    while (k < this.mNumRows) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int m = j;
      if (arrayOfArrayRow[k] != null)
        m = j + arrayOfArrayRow[k].sizeInBytes(); 
      k++;
      j = m;
    } 
    PrintStream printStream = System.out;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Linear System -> Table size: ");
    stringBuilder.append(this.TABLE_SIZE);
    stringBuilder.append(" (");
    k = this.TABLE_SIZE;
    stringBuilder.append(getDisplaySize(k * k));
    stringBuilder.append(") -- row sizes: ");
    stringBuilder.append(getDisplaySize(i));
    stringBuilder.append(", actual size: ");
    stringBuilder.append(getDisplaySize(j));
    stringBuilder.append(" rows: ");
    stringBuilder.append(this.mNumRows);
    stringBuilder.append("/");
    stringBuilder.append(this.mMaxRows);
    stringBuilder.append(" cols: ");
    stringBuilder.append(this.mNumColumns);
    stringBuilder.append("/");
    stringBuilder.append(this.mMaxColumns);
    stringBuilder.append(" ");
    stringBuilder.append(0);
    stringBuilder.append(" occupied cells, ");
    stringBuilder.append(getDisplaySize(0));
    printStream.println(stringBuilder.toString());
  }
  
  public void displayVariablesReadableRows() {
    displaySolverVariables();
    String str = "";
    byte b = 0;
    while (b < this.mNumRows) {
      String str1 = str;
      if ((this.mRows[b]).variable.mType == SolverVariable.Type.UNRESTRICTED) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append(this.mRows[b].toReadableString());
        str = stringBuilder1.toString();
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append("\n");
        str1 = stringBuilder1.toString();
      } 
      b++;
      str = str1;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(this.mGoal);
    stringBuilder.append("\n");
    str = stringBuilder.toString();
    System.out.println(str);
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    sMetrics = paramMetrics;
  }
  
  public Cache getCache() {
    return this.mCache;
  }
  
  Row getGoal() {
    return this.mGoal;
  }
  
  public int getMemoryUsed() {
    int i = 0;
    byte b = 0;
    while (b < this.mNumRows) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int j = i;
      if (arrayOfArrayRow[b] != null)
        j = i + arrayOfArrayRow[b].sizeInBytes(); 
      b++;
      i = j;
    } 
    return i;
  }
  
  public int getNumEquations() {
    return this.mNumRows;
  }
  
  public int getNumVariables() {
    return this.mVariablesID;
  }
  
  public int getObjectVariableValue(Object paramObject) {
    paramObject = ((ConstraintAnchor)paramObject).getSolverVariable();
    return (paramObject != null) ? (int)(((SolverVariable)paramObject).computedValue + 0.5F) : 0;
  }
  
  ArrayRow getRow(int paramInt) {
    return this.mRows[paramInt];
  }
  
  float getValueFor(String paramString) {
    SolverVariable solverVariable = getVariable(paramString, SolverVariable.Type.UNRESTRICTED);
    return (solverVariable == null) ? 0.0F : solverVariable.computedValue;
  }
  
  SolverVariable getVariable(String paramString, SolverVariable.Type paramType) {
    if (this.mVariables == null)
      this.mVariables = new HashMap<>(); 
    SolverVariable solverVariable1 = this.mVariables.get(paramString);
    SolverVariable solverVariable2 = solverVariable1;
    if (solverVariable1 == null)
      solverVariable2 = createVariable(paramString, paramType); 
    return solverVariable2;
  }
  
  public void minimize() throws Exception {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.minimize++; 
    if (this.graphOptimizer) {
      boolean bool2;
      metrics = sMetrics;
      if (metrics != null)
        metrics.graphOptimizer++; 
      boolean bool1 = true;
      byte b = 0;
      while (true) {
        bool2 = bool1;
        if (b < this.mNumRows) {
          if (!(this.mRows[b]).isSimpleDefinition) {
            bool2 = false;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (!bool2) {
        minimizeGoal(this.mGoal);
      } else {
        metrics = sMetrics;
        if (metrics != null)
          metrics.fullySolved++; 
        computeValues();
      } 
    } else {
      minimizeGoal(this.mGoal);
    } 
  }
  
  void minimizeGoal(Row paramRow) throws Exception {
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.minimizeGoal++;
      metrics = sMetrics;
      metrics.maxVariables = Math.max(metrics.maxVariables, this.mNumColumns);
      metrics = sMetrics;
      metrics.maxRows = Math.max(metrics.maxRows, this.mNumRows);
    } 
    updateRowFromVariables((ArrayRow)paramRow);
    enforceBFS(paramRow);
    optimize(paramRow, false);
    computeValues();
  }
  
  public void reset() {
    byte b;
    for (b = 0; b < this.mCache.mIndexedVariables.length; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[b];
      if (solverVariable != null)
        solverVariable.reset(); 
    } 
    this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
    this.mPoolVariablesCount = 0;
    Arrays.fill((Object[])this.mCache.mIndexedVariables, (Object)null);
    HashMap<String, SolverVariable> hashMap = this.mVariables;
    if (hashMap != null)
      hashMap.clear(); 
    this.mVariablesID = 0;
    this.mGoal.clear();
    this.mNumColumns = 1;
    for (b = 0; b < this.mNumRows; b++)
      (this.mRows[b]).used = false; 
    releaseRows();
    this.mNumRows = 0;
  }
  
  static interface Row {
    void addError(SolverVariable param1SolverVariable);
    
    void clear();
    
    SolverVariable getKey();
    
    SolverVariable getPivotCandidate(LinearSystem param1LinearSystem, boolean[] param1ArrayOfboolean);
    
    void initFromRow(Row param1Row);
    
    boolean isEmpty();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/LinearSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */